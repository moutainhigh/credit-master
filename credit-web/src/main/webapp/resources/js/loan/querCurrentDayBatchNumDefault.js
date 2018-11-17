$(function(){	
	Date.prototype.format = function (format) {  
	    var o = {  
	        "M+": this.getMonth() + 1, // month  
	        "d+": this.getDate(), // day  
	        "h+": this.getHours(), // hour  
	        "m+": this.getMinutes(), // minute  
	        "s+": this.getSeconds(), // second  
	        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
	        "S": this.getMilliseconds()  
	        // millisecond  
	    }  
	    if (/(y+)/.test(format))  
	        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
	            .substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	        if (new RegExp("(" + k + ")").test(format))  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
	    return format;  
	}  
	function formatDatebox(value) {  
	    if (value == null || value == '') {  
	        return '';  
	    }  
	    var dt;  
	    if (value instanceof Date) {  
	        dt = value;  
	    } else {  
	        dt = new Date(value);  
	    }  
	  
	    return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)  
	}
	
	
	$.loanExternalDebtCurrent = {
			
			/** 表格数据源地址 **/
			dataGridUrl : global.contextPath + '/loan/listCurrentDayExternalDebt',
			/** 客户信息表格 **/
			dataGrid : $('#querCurrentDayBatchNumDetailDataGrid'),
			/** 分页控件 **/
			pager : undefined,
			/** 每页显示的记录条数，默认为10 **/
			pageSize : 999,
			/** 设置每页记录条数的列表 **/
			pageSizeList : [999],
			/** 加载表格数据 **/
			reloadDataGrid : function() {
				/** 获取查询表单数据转换成JSON对象 **/	
				var	queryParam ={};
				var orgTemp = parent.$('#financialorg').combobox('getValue');
				//合同编号
				var contractNum = parent.$('#contractNum').val();  
				queryParam.org=orgTemp;
				//合同编号
				queryParam.contractNum = contractNum;
				queryParam.url=$.loanExternalDebtCurrent.dataGridUrl;	
				$.loanExternalDebtCurrent.dataGrid.datagrid('reloadData',queryParam);
				
			}
		};
	
	
		/** 分页参数（page:当前第N页，rows:一页N行） **/
		$.loanExternalDebtCurrent.pg = {
			'page' : 1,
			'rows' : $.loanExternalDebtCurrent.pageSize
		};
		

		$.loanExternalDebtCurrent.dataGrid.datagrid({
			pg : $.loanExternalDebtCurrent.pg,
			/** 提交方式 **/
			method : 'get',
			/** 是否显示行号 **/
			rownumbers : false,
			selectOnCheck:false,
			checkOnSelect:false,
			/** 是否单选 **/
			singleSelect : true,
			/** 是否可折叠的 **/
			collapsible : false,
			/** 自适应列宽 **/
			fitColumns : false,
			fit : true,
			//height : '100%',
			/** 是否开启分页 **/
			pagination : true,
			loadMsg : '数据加载中,请稍等...',
			columns : [[
			      /** 列定义 **/
			      	  {
			      		  field:'id',   
			      		  width : '2%',
			      		  title : '<input class="dataGridCheckBoxTitle" type="checkbox"></input>',
			      		  formatter : function(value, row, index) {
		                	row.index=index;
		                    return '<input  class="dataGridCheckBox" type="checkbox" data-loan-id="' + row.id + '" data-index="'+index+'" value="' + row.id + '">';
			      		  } 
			      	  },
				      {field : 'name',title : '姓名',width : '10%'},
				      {field : 'idnum',title : '身份证',width : '20%'},
				      {field : 'loanType',title : '产品类型',width : '10%'},
				      {field : 'rateey',title : '利率',width : '10%',vType : 'percent'},
				      {field : 'time',title : '期限',width : '10%'},
				      {field : 'pactMoney',title : '合同金额',width : '10%',vType : 'rmb'},
				      {field : 'grantMoney',title : '放款金额',width : '10%',vType : 'rmb'},
				      {field : 'startrdate',title : '首还款日期',width : '8%',formatter: formatDatebox},
				      {field : 'contractNum',title : '合同编号',width : '9%'}
			      
			]],
			/** 每页显示的记录条数，默认为10 **/
			pageSize : $.loanExternalDebtCurrent.pageSize,
			/** 可以设置每页记录条数的列表 **/
			pageList : $.loanExternalDebtCurrent.pageSizeList,
			toolbar : '#tb',
			/** 自定义行样式 **/
			rowStyler : function(index,row) {
				if (index % 2 == 0) {
					//return 'background-color:#AABBCC;color:#fff;';
				}
			},
			onLoadSuccess:function(data){
	        
			}
			});
		
		
		
		$.loanExternalDebtCurrent.pager = $.loanExternalDebtCurrent.dataGrid.datagrid('getPager');		
		$.loanExternalDebtCurrent.pager.pagination({
			onSelectPage : function(pageNumber,pageSize) {
				$.loanExternalDebtCurrent.pg.page = pageNumber;
				$.loanExternalDebtCurrent.pg.rows = pageSize;
				$.loanExternalDebtCurrent.reloadDataGrid();
			}
		});
	
		$.loanExternalDebtCurrent.reloadDataGrid();
		parent.$('#batchDownloadBtn').hide();
		parent.$('#createBatchNum').show();
		parent.$('#updateBatchNumBtn').hide();
		
		$(".dataGridCheckBoxTitle").unbind();
		
	    /** 单击dataGrid checkbox事件 **/
	    $(document).on("click",".dataGridCheckBoxTitle,.dataGridCheckBox",function(event) {
	    	var currentClass =$(this).attr("class")||"",
	    	currentTitleClass=currentClass.indexOf("Title")!=-1?currentClass:currentClass+"Title",
	    	currentSubClass=currentTitleClass.substring(0,currentTitleClass.indexOf("Title")),
	    	allSelect=true;
	    	
	    	if(currentClass == currentTitleClass ){
	    		$("."+currentSubClass).prop("checked",$("."+currentTitleClass).prop("checked"));
	    	}else{
	    		$("."+currentSubClass).each(function(index,element){
	    			if($(this).prop("checked")==false){
	    				allSelect=false;
	    				return false;
	    			}
	    		});
	    		$("."+currentTitleClass).prop("checked",allSelect);
	    	}
	    	if(currentClass.indexOf("dataGridCheckBox")!=-1){
				var selGrantMoneyCount = 0,
				selPactMoneyCount = 0,
				dataGridRows = $('#querCurrentDayBatchNumDetailDataGrid').datagrid('getRows'),
				count=0;
				$("."+currentSubClass).each(function(index,element){
					if($(this).prop("checked")==true){
						count = $(this).data("index")-0;
						selGrantMoneyCount += parseFloat(dataGridRows[count].grantMoney);
						selPactMoneyCount += parseFloat(dataGridRows[count].pactMoney);
					}
				});
				$("#selGrantMoneyCount").html(selGrantMoneyCount.toFixed(2));
			    $("#selPactMoneyCount").html(selPactMoneyCount.toFixed(2));
	    	}
	    });
		

		parent.$('#createBatchNum').unbind().click(function(){
			var $selected = $(".dataGridCheckBox:checked");
	        if($selected.length>0){
	        	 var orgTemp = parent.$('#financialorg').combobox('getValue');            	 
	        	 var tipMessage='您已选择债权数为：' + $selected.length + '条，\n放款金额为：' + $("#selGrantMoneyCount").html() + '元，\n合同金额为：'+ $("#selPactMoneyCount").html()  +'元，\n确认生成批次号？';
	        	 top.$.messager.confirm('提示', tipMessage, function(r){
	    			if (r){
	    				var ids = [];
	    				$selected.each(function(index,element){
	    					 ids.push($(this).data("loanId"));
	    				});
		           		$.ajaxPackage({
		           			type : 'get', 
		           			url : global.contextPath + '/loan/createBatchNum?org='+orgTemp+'&ids='+ids,
		           			dataType : "json",
		           			success : function (data,textStatus,jqXHR) {				
		           				var resCode = data.resCode;				
		           				var resMsg = data.resMsg;
		           				var attachment = data.attachment;
		           				if (resCode == '000000') {
		           					$.loanExternalDebtCurrent.reloadDataGrid();
		           				} else {
		           					$.messager.alert('提示信息',resMsg,'error');
		           				}
		           			},
		           			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
		           				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
		           			},
		           			complete : function() {
		           				
		           			}
		           		});
	    			}
	    		});
	        }else{
	        	$.messager.alert('提示信息',"请选择相应的债权信息",'error');
	            return false;
	        } 
			
		});
});
