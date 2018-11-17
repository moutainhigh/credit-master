$(function() {
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
	    }  ;
	    if (/(y+)/.test(format))  
	        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
	            .substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	        if (new RegExp("(" + k + ")").test(format))  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
	    return format;  
	};
	
	function formatDateboxMMSS(value) {  
	    if (value == null || value == '') {  
	        return '';  
	    }  
	    var dt;  
	    if (value instanceof Date) {  
	        dt = value;  
	    } else {  
	        dt = new Date(value);  
	    }  
	    return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
	} 
	$.loanReturnlist = {
			/** 表格数据源地址 **/
			dataGridUrl : global.contextPath + '/loanReturn/listLoanReturn',
			/** 客户信息表格 **/
			dataGrid : $('#loanReturnDataGrid'),
			/** 分页控件 **/
			pager : undefined,
			/** 查询条件数据项表单实例 **/
			/** 每页显示的记录条数，默认为10 **/
			pageSize : 10,
			/** 设置每页记录条数的列表 **/
			pageSizeList : [10,20,30,40,50],
			/** 加载表格数据 **/
			reloadDataGrid : function() {
				var startQueryDate=$('#startQueryDate').datebox("getValue");
		    	var name=$('#name').val();
		    	var idnum=$('#idNum').val();
		    	var fundsSources=$('#loanType').combobox('getValue');
		    	var contractNum=$('#contractNum').val();
				var params={};
				params.name=name;
				params.startQueryDate=startQueryDate;
				params.idnum=idnum;
				var queryString="?name="+name+"&idnum="+idnum+"&startQueryDate="+startQueryDate+"&fundsSources="+fundsSources+"&contractNum="+contractNum;	
				var	queryParam ={};
				queryParam.url=$.loanReturnlist.dataGridUrl+queryString;				
				$.loanReturnlist.dataGrid.datagrid('reloadData',queryParam);
			}
		};
		/** 分页参数（page:当前第N页，rows:一页N行） **/
		$.loanReturnlist.pg = {
			'page' : 1,
			'rows' : $.loanReturnlist.pageSize
		};
	
	$.loanReturnlist.dataGrid.datagrid({
		pg : $.loanReturnlist.pg,
		/** 提交方式 **/
		method : 'get',
		/** 是否显示行号 **/
		rownumbers : true,
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
		            {
		                field : 'fundsSources',
		                title : '合同来源',
		                width : '10%'
		            }, {
		                field : 'name',
		                title : '借款人',
		                width : '12%'
		            },{
		                field : 'idnum',
		                title : '身份证',
		                width : '12%'
		            }, {
		                field : 'loanType',
		                title : '借款类型',
		                width : '12%'
		            }, {
		                field : 'pactMoney',
		                title : '合同金额',
		                width : '10%',
		                vType : 'rmb'
		            },{
		                field : 'signDate',
		                title : '签约日期',
		                width : '9%',
		                formatter: formatDateboxMMSS
		            }, {
		                field : 'importReason',
		                title : '导入原因',
		                width : '12%'
		            }, {
		                field : 'createTime',
		                title : '导入日期',
		                width : '10%',
		                formatter: formatDateboxMMSS
		            }, {
		            	field : 'contractNum',
						title : '合同编号',
		                width : '9%'
		            }, 
				]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.loanReturnlist.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.loanReturnlist.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		},
        // 回调函数
        callBackFunction : function(value) {
			/** 根据选择条件计算总金额、笔数 **/
			$("#totalAmount").text(value.calculteData.totalAmount);
			$("#totalPactMoney").text(value.calculteData.totalPactMoney);
			$("#loanreturnListTotal").text(value.calculteData.loanreturnListTotal);
		}
		
		
	});
	
	
	$.loanReturnlist.pager = $.loanReturnlist.dataGrid.datagrid('getPager');
	$.loanReturnlist.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.loanReturnlist.pg.page = pageNumber;
			$.loanReturnlist.pg.rows = pageSize;
			$.loanReturnlist.reloadDataGrid();
		}
	});
	
	$.loanReturnlist.reloadDataGrid();	
	
	
	
    // 批量导入窗口对象
    var importExcelWin = $("#importExcelWin");    
    var importExcelWinWC = $("#importExcelWinWC");
    var uploadUrl=global.contextPath+"/loanReturn/loanReturnImport";
    var uploadUrldigmoney=global.contextPath+"/loanReturn/digmoneyImport";
    
    // 导入文件表单对象
    var baseFileForm = $("#baseFileForm");
    var baseFileFormWC = $("#baseFileFormWC");
	
	 /** 查询处理 **/
    $("#searchBtn").click(function(){
    	if($("#searchForm").form("validate")){
    		$.loanReturnlist.pg = 1;
        	$.loanReturnlist.reloadDataGrid();
    	}
    });
	
    
    
    /** 打开导入窗口 **/
    $('#batchImportBtnreturnLoanImport').click(function(){
        importExcelWin.window('open');
        baseFileForm.form('clear');
    });
    
    /**
     *  挖财导入窗口
     */
    $('#batchImportBtndigmoneyImport').click(function(){
        importExcelWinWC.window('open');
        baseFileFormWC.form('clear');
    });
    /** 批量导入处理 **/
    $("#batchImport").click(function(){
        var file = $("#uploadfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("操作提示","确认导入吗？",function(r){
            if(r){
                baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: uploadUrl,
                    hasDownloadFile:true,                 
                    success: function (data) {
                        setTimeout(function(){
                            importExcelWin.window('close');
                            $.loanReturnlist.reloadDataGrid();	
                        }, 1000);
                    },
                    error: function (data) {
                    	if(data!=null&&data.hasOwnProperty("resMsg")){
                   		 $.messager.alert('操作提示',data.resMsg,'warning');
                   	}else{
                   		 $.messager.alert('操作提示','文件上传失败!','warning');
                   	}
                    }
                });
            }
        });
    });
    
    var tempfileName="";
    /** 批量导入处理挖财 **/
    $("#batchImportWC").click(function(){
        var file = $("#uploadfileWC").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("操作提示","确认导入吗？",function(r){
            if(r){
            	baseFileFormWC.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: uploadUrldigmoney,
                    success: function (data) {
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        var attachment = data.attachment;
                        if (resCode == '000000') {
                        	 setTimeout(function(){
                        		 importExcelWinWC.window('close');
                                 $.loanReturnlist.reloadDataGrid();	
                                $("#sumMoney").text(parseFloat(data.attachment.sumMoney).toFixed(2));
                             	$("#digMoneyListTotal").text(data.attachment.digMoneyTotal);
                             	$("#successMoney").text(parseFloat(data.attachment.successMoney).toFixed(2));
                             	$("#successTotal").text(data.attachment.successBeans);
                             	$("#faileMoney").text(parseFloat(data.attachment.faileMoney).toFixed(2));                        	
                             	$("#failTotal").text(data.attachment.failBeans);
     							if((data.attachment.digMoneyTotal-0)>0){
     								$('#digMoneyBtn').show();  
     								tempfileName=data.attachment.fileName;
     							}
     							if((data.attachment.failBeans-0)==0 &&(data.attachment.successBeans-0)>0 ){
     								$('#updateBatchNum').show(); 
     							}else{
     								$('#updateBatchNum').hide();
     							}
                          }, 1000);
                        	
                        }else{
                        	$.messager.alert('提示信息',resMsg,'warning');
                        }
                        
                    },
                    error: function (msg) {
                        $.messager.alert('警告','文件上传失败!','warning');
                    }
                });
            }
        });
    });
    
    //清空查询条件
    $("#clearCondition").bind("click",function(envent){
    	if($(this).linkbutton("options").disabled==false){
    		$("#searchForm").form("reset");
    	}
    });
    
    

    
    //导出文件
    $('#digMoneyBtn').click(function(){     	
        var downloadUrl = global.contextPath + '/loanReturn/downLoadExcel';  
        location.href = downloadUrl+"?fileName="+tempfileName;
	}); 
    
    
    //更新批次号
    $('#updateBatchNum').click(function(){     
    	$.messager.confirm("操作提示","\n确认生成批次号同时会删除导入文件？",function(r){
    		if(r){
    			var updateBatchNumUrl = global.contextPath + '/loanReturn/updateBatchNum';  
//    			 location.href =updateBatchNumUrl+"?fileName="+tempfileName;
    			 
    			 $.ajaxPackage({
    		   			type : 'get', 
    		   			url : updateBatchNumUrl+"?fileName="+tempfileName,
    		   			dataType : "json",
    		   			success : function (data,textStatus,jqXHR) {				
    		   				var resCode = data.resCode;				
    		   				var resMsg = data.resMsg;
    		   				var attachment = data.attachment;
    		   				if (resCode == '000000') {
    		   					$.messager.alert('提示信息',"操作成功！");
    		   					setTimeout(function(){
                           		 importExcelWinWC.window('close');
                                    $.loanReturnlist.reloadDataGrid();	
                                   $("#sumMoney").text(parseFloat(0).toFixed(2));
                                	$("#digMoneyListTotal").text(0);
                                	$("#successMoney").text(parseFloat(0).toFixed(2));
                                	$("#successTotal").text(0);
                                	$("#faileMoney").text(parseFloat(0).toFixed(2));                        	
                                	$("#failTotal").text(0);
        							$('#digMoneyBtn').hide();  
        							$('#updateBatchNum').hide();
                             }, 1000);	
    		   				} else {
    		   					$.messager.alert('提示信息',resMsg,'warning');
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
    }); 
});