/** 在Date原型对象上扩展format方法**/
Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds()
    };
    if (/(y+)/.test(format)){
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for ( var k in o){
        if (new RegExp("(" + k + ")").test(format)){
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

$(function() {
    // 显示批量下载按钮
    $('#batchDownloadBtn').show();
    
    $.loanExternalDebt = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/loan/listquerBatchNumDetail',
        /** 客户信息表格 **/
        dataGrid : $('#querBatchNumDetailDataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [ 10, 20, 30, 40, 50,200],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            /** 获取查询表单数据转换成JSON对象 **/
            var queryParam = {};
            var orgTemp = '';
            orgTemp = $('#batchNumTempDetail').val();
            var org = parent.$('#financialorg').combobox('getValue');	
            queryParam.batNum = orgTemp;
            queryParam.org = org;
            queryParam.url = $.loanExternalDebt.dataGridUrl;
            $.loanExternalDebt.dataGrid.datagrid('reloadData', queryParam);
        }
    };
    
    
    var offerPerms = parent.$("#offerParams").data("offerPerms");
    var offerPerms = offerPerms.replace("\[","");
    offerPerms = offerPerms.replace("\]","");
    offerPerms=offerPerms.split(",");
    var permsObj = {};
    $.each(offerPerms,function(index,value){
  	  permsObj[$.trim(value)]=$.trim(value);
    });

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.loanExternalDebt.pg = {
        'page' : 1,
        'rows' : $.loanExternalDebt.pageSize
    };

    $.loanExternalDebt.dataGrid.datagrid({
        pg : $.loanExternalDebt.pg,
        /** 提交方式 **/
        method : 'get',
        /** 是否显示行号 **/
        rownumbers : false,
        /** 是否单选 **/
        singleSelect : true,
        /** 是否可折叠的 **/
        collapsible : false,
        checkOnSelect : false,
        selectOnCheck : false,
        /** 自适应列宽 **/
        fitColumns : false,
        fit : true,
        //height : '100%',
        /** 是否开启分页 **/
        pagination : true,
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            /** 列定义 **/
            {
                field : 'op10',
                title : '<input class="dataGridCheckBoxTitle" type="checkbox"></input>',
                width : '2%',
                formatter : function(value, row, index) {
                	row.index=index;
                    if (row != null && row.changeFlag == '0') {//历史数据
                        if (row != null && row.batchNum != null) {
                            return '<input class="dataGridCheckBox" type="checkbox" data-loan-id="' + row.id + '" data-index="'+index+'"  value="'+ row.id + '" checked disabled>';
                        } else {
                            return '<input class="dataGridCheckBox" type="checkbox" data-loan-id="' + row.id + '" data-index="'+index+'" value="'+ row.id + '" disabled>';
                        }
                    } else {
                        if (row != null && row.batchNum != null) {
                            return '<input  class="dataGridCheckBox" type="checkbox" data-loan-id="' + row.id + '" data-index="'+index+'" value="' + row.id + '" checked>';
                        } else {
                            return '<input  class="dataGridCheckBox" type="checkbox" data-loan-id="' + row.id + '" data-index="'+index+'" value="' + row.id + '">';
                        }
                    }
                }
            },
            {
                field : 'id',
                title : 'id',
                hidden : true
            },
            {
                field : 'batchNum',
                title : 'batchNum',
                hidden : true
            },
            {
                field : 'appNo',
                title : 'appNo',
                hidden : true
            },
            {
                field : 'name',
                title : '姓名',
                width : '5%'
            },
            {
                field : 'idnum',
                title : '身份证',
                width : '10%'
            },
            {
                field : 'loanType',
                title : '产品类型',
                width : '5%'
            },
            {
                field : 'rateey',
                title : '利率',
                width : '5%',
                vType : 'percent'
            },
            {
                field : 'time',
                title : '期限',
                width : '10%'
            },
            {
                field : 'pactMoney',
                title : '合同金额',
                width : '10%',
                vType : 'rmb'
            },
            {
                field : 'grantMoney',
                title : '放款金额',
                width : '10%',
                vType : 'rmb'
            },
            {
                field : 'startrdate',
                title : '首还款日期',
                width : '5%',
                formatter : formatDatebox
            },
            {
			      field : 'contractNum',
		    	  title : '合同编号',
		    	  width : '10%'
			},
            {
                field : 'op11',
                title : '<input class="xinShenCheckBoxTitle" type="checkbox" >附件下载</input>',
                width : '10%',
                formatter : function(value, row, index) {
                	if(permsObj["/loan/getLoanBatchImgXs"]!=null){
	                    if (row != null && row.batchNum != null) {
	                    	var checked = row.changeFlag == '0' || row.batchNum!=null ?"checked":"";
	                        return '<input  class="xinShenCheckBox" type="checkbox" data-loan-id="' + row.id + '" data-index="'+index+'" '+checked+' value="' + row.id + '"><a href="javascript:void(0)" class="singleDownload" data-loan-id="' + row.id + '" data-app-no="'+ row.appNo + '" data-type="XS" >信审</a>';
	                    } 
                	}
                    return "";
                }
            },
            {
                field : 'op22',
                title : '<input class="contractCheckBoxTitle" type="checkbox"  >附件下载</input>',
                width : '10%',
                formatter : function(value, row, index) {
                	if(permsObj["/loan/getLoanBatchImgHt"]!=null){
	                    if (row != null && row.batchNum != null) {
	                    	var checked = row.changeFlag == '0'  || row.batchNum!=null ?"checked":"";
	                        return '<input  class="contractCheckBox" type="checkbox"  data-loan-id="' + row.id + '" data-index="'+index+'" '+checked+' value="' + row.id + '"><a href="javascript:void(0)"  class="singleDownload" data-loan-id="' + row.id + '" data-app-no="'+ row.appNo + '" data-type="HT" >合同</a>';
	                    } 
                	}
                    return "";
                }
            },
            {
                field : 'op23',
                title : '<input class="zhengXinCheckBoxTitle" type="checkbox"  >附件下载</input>',
                width : '10%',
                formatter : function(value, row, index) {
                	if(permsObj["/loan/getLoanBatchImgHt"]!=null){
	                    if (row != null && row.batchNum != null) {
	                    	var checked = row.changeFlag == '0'  || row.batchNum!=null ?"checked":"";
	                        return '<input  class="zhengXinCheckBox" type="checkbox"  data-loan-id="' + row.id + '" data-index="'+index+'" '+checked+' value="' + row.id + '"><a href="javascript:void(0)"  class="singleDownload" data-loan-id="' + row.id + '" data-app-no="'+ row.appNo + '" data-type="ZX" >征信</a>';
	                    } 
                	}
                    return "";
                }
            }


        ] ],
        
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.loanExternalDebt.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.loanExternalDebt.pageSizeList,
        toolbar : '#tb',
        /** 自定义行样式 **/
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess : function(){
        	var loadCompleteSelect = function(currentTitleClass,currentSubClass){
        		var allSelect=true;
        		var count = 0;
        		$("."+currentSubClass).each(function(index,element){
        			count++;
        			if($(this).prop("checked")==false){
        				allSelect=false;
        				return false;
        			}
        		});
        		if(count==0){
        			allSelect=false;
        		}
        		$("."+currentTitleClass).prop("checked",allSelect);
        	},
        	loadCompleteCompute = function(){
        		var selGrantMoneyCount = 0,
    			selPactMoneyCount = 0,
    			selServiceMoneyCount = 0,
    			count=0,
    			dataGridRows = $.loanExternalDebt.dataGrid.datagrid('getRows');
    			$(".dataGridCheckBox").each(function(index,element){
    				if($(this).prop("checked")==true){
    					count = $(this).data("index")-0;
    					selGrantMoneyCount += parseFloat(dataGridRows[count].grantMoney);
    					selPactMoneyCount += parseFloat(dataGridRows[count].pactMoney);
    					if(parseFloat(dataGridRows[count].time)=='12'){
    						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0177);
    					}
    					if(parseFloat(dataGridRows[count].time)=='18'){
    						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0263);
    					}
    					if(parseFloat(dataGridRows[count].time)=='24'){
    						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0353);
    					}
    					if(parseFloat(dataGridRows[count].time)=='36'){
    						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0540);
    					}
    				}
    			});
    			$("#selGrantMoneyCount").html(selGrantMoneyCount.toFixed(2));
    		    $("#selPactMoneyCount").html(selPactMoneyCount.toFixed(2));
    		    $("#selServiceMoneyCount").html(selServiceMoneyCount.toFixed(2));
        	},loadCompleteSetDisable = function(){
        		var count=0,
        		dataGridRows = $.loanExternalDebt.dataGrid.datagrid('getRows'),
        		allSelect=true;
            	if ($.isArray(dataGridRows) && dataGridRows.length > 0) {
    				if (dataGridRows[0].changeFlag == '1') {
    					parent.$('#updateBatchNumBtn').show();
    				} else {
    					parent.$('#updateBatchNumBtn').hide();
    				}
          			$(".dataGridCheckBox").each(function(index,element){
        				if($(this).prop("checked")==true){
        					count = $(this).data("index")-0;
    		        		if (dataGridRows[count].changeFlag == '0') { //历史数据 
    	        				$(this).prop("disabled", true);
    		        		}else{
    		        			$(this).prop("disabled", false);
    		        			allSelect=false;
    		        		}
        				}
        			});
          			$(".dataGridCheckBoxTitle").prop("disabled", allSelect);
            	}
        	};
        	//设置表头checkbox
        	loadCompleteSelect("dataGridCheckBoxTitle","dataGridCheckBox");
        	loadCompleteSelect("xinShenCheckBoxTitle","xinShenCheckBox");
        	loadCompleteSelect("contractCheckBoxTitle","contractCheckBox");
        	loadCompleteSelect("zhengXinCheckBoxTitle","zhengXinCheckBox");
        	//设置计算值
        	loadCompleteCompute();
        	//设置历史状态
        	loadCompleteSetDisable();
        }
    });
    
    $.loanExternalDebt.pager = $.loanExternalDebt.dataGrid.datagrid('getPager');
    $.loanExternalDebt.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.loanExternalDebt.pg.page = pageNumber;
            $.loanExternalDebt.pg.rows = pageSize;
            $.loanExternalDebt.reloadDataGrid();
        }
    });
    
	$(".dataGridCheckBoxTitle,.xinShenCheckBoxTitle,.contractCheckBoxTitle,.zhengXinCheckBoxTitle").unbind();
    
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
			selServiceMoneyCount = 0,
			dataGridRows = $('#querBatchNumDetailDataGrid').datagrid('getRows'),
			count=0;
			$("."+currentSubClass).each(function(index,element){
				if($(this).prop("checked")==true){
					count = $(this).data("index")-0;
					selGrantMoneyCount += parseFloat(dataGridRows[count].grantMoney);
					selPactMoneyCount += parseFloat(dataGridRows[count].pactMoney);					
					if(parseFloat(dataGridRows[count].time)=='12'){
						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0177);
					}
					if(parseFloat(dataGridRows[count].time)=='18'){
						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0263);
					}
					if(parseFloat(dataGridRows[count].time)=='24'){
						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0353);
					}
					if(parseFloat(dataGridRows[count].time)=='36'){
						selServiceMoneyCount += parseFloat(dataGridRows[count].pactMoney*0.0540);
					}
				}
			});
			$("#selGrantMoneyCount").html(selGrantMoneyCount.toFixed(2));
		    $("#selPactMoneyCount").html(selPactMoneyCount.toFixed(2));
		    $("#selServiceMoneyCount").html(selServiceMoneyCount.toFixed(2));
    	}
    });
    
    /** 单击信审、合同 checkbox事件 **/
    $(document).on("click",".xinShenCheckBoxTitle,.xinShenCheckBox,.contractCheckBoxTitle,.contractCheckBox,.zhengXinCheckBoxTitle,.zhengXinCheckBox",function(event) {
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
    });
    
    $.loanExternalDebt.reloadDataGrid();
    
    
    /** 单击信审、合同 连接下载事件 **/
    $(document).on("click",".singleDownload",function(event) {
    	var loanId = $(this).data("loanId"),
    	type = $(this).data("type");
    	if(type=="XS"){
    		downloadAttachment("", loanId, "",type);
    	}else if(type=="HT"){
    		  downloadAttachment("", "", loanId,type);
    	}else if(type=="ZX"){
    		downloadAttachment("", "", "",type,loanId);
    	}
    });
    
    parent.$('#batchDownloadBtn').show();
    parent.$('#batchDownloadBtn').unbind().on("click", function() {  
//        var ary_xs = "";
//        var ary_ht = "";
//        $(".xinShenCheckBox:checked").each(function(i, n) {
//            if (i != 0) {
//            	ary_xs += ",";
//            }
//            ary_xs += $(this).data("loanId");
//        });
//        $(".contractCheckBox:checked").each(function(i, n) {
//            if (i != 0) {
//            	ary_ht += ",";
//            }
//            ary_ht += $(this).data("loanId");
//        });
//       
//        // 附件下载（包括批量下载和单一选择下载）
//        downloadAttachment("", ary_xs, ary_ht,"");
        
        var ids = "";
        var ids_xs = "";
        var ids_ht = "";
        var ids_zx = "";
        $(".xinShenCheckBox:checked").each(function(i, n) {
            if (i != 0) {
                ids_xs += ",";
            }
            ids_xs += $(this).data("loanId");
        });
        $(".contractCheckBox:checked").each(function(i, n) {
            if (i != 0) {
                ids_ht += ",";
            }
            ids_ht += $(this).data("loanId");
        });
        $(".zhengXinCheckBox:checked").each(function(i, n) {
            if (i != 0) {
            	ids_zx += ",";
            }
            ids_zx += $(this).data("loanId");
        });
        var ary = new Array();
        var ary_xs = ids_xs.split(",");
        var ary_ht = ids_ht.split(",");
        for (var i = 0; i < ary_xs.length; i++) {
            var id_xs = ary_xs[i];
            for (var j = 0; j < ary_ht.length; j++) {
                var id_ht = ary_ht[j];
                if (id_xs == id_ht) {
                    ary.push(id_xs);
                }
            }
        }
        if (ary.length > 0) {
            for (var i = 0; i < ary.length; i++) {
                var id = ary[i];
                ary_xs = $.map(ary_xs, function(value, index) {
                    if (id == value) {
                        return null;
                    }
                    return value;
                });
                ary_ht = $.map(ary_ht, function(value, index) {
                    if (id == value) {
                        return null;
                    }
                    return value;
                });
            }
        }
        // 附件下载（包括批量下载和单一选择下载）
        downloadAttachment(ary.join(","), ary_xs.join(","), ary_ht.join(","),"", ids_zx);  
        
        
        
    });

    /** 更新批次号 **/
    parent.$('#updateBatchNumBtn').unbind().click(function() {
    	 // 选择条数
    		var $selected = $(".dataGridCheckBox:checked");      
            var orgTemp = parent.$('#financialorg').combobox('getValue');
            var batchNum = $('#batchNumTempDetail').val();
            // 放款金额
            var grantMoney = $("#selGrantMoneyCount").html();
            // 合同金额
            var pactMoney = $("#selPactMoneyCount").html();
            // 应付服务费
            var  serviceMoney = $("#selServiceMoneyCount").html();
            // 确认语句
            var tipMessage = '您已选择债权数为：'+ $selected.length + '条，\n放款金额为：' + grantMoney + '元，\n合同金额为：'+ pactMoney + '元，\n应付服务费为：'+ serviceMoney + '元，\n确认更新批次号？';
            top.$.messager.confirm("提示",tipMessage,function(r){
                if(r){
                    var ids = [];
    				$selected.each(function(index,element){
    					 ids.push($(this).data("loanId"));
    				});
                    $.ajaxPackage({
                        type : 'get',
                        url : global.contextPath + '/loan/updateBatchNum?org='+ orgTemp + '&batchNum=' + batchNum + '&ids='+ ids,
                        dataType : "json",
                        success : function(data, textStatus, jqXHR) {
                            var resCode = data.resCode;
                            var resMsg = data.resMsg;
                            var attachment = data.attachment;
                            if (resCode == '000000') {
                                $.loanExternalDebt.reloadDataGrid();
                            } else {
                                $.messager.alert('提示信息', resMsg, 'warning');
                            }
                        },
                        error : function(XMLHttpRequest, textStatus,errorThrown, d) {
                            $.messager.alert('提示信息', textStatus + '  :  '+ errorThrown + '!', 'error');
                        },
                        complete : function() {

                        }
                    });
                }
            });
       
    });
    
    /** 日期格式 **/
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
        return dt.format("yyyy-MM-dd");
    }

    /** 附件下载（包括批量下载和单一选择下载） **/
    function downloadAttachment(ary, ary_xs, ary_ht,type,ary_zx) {
        var ajaxForm = parent.$("#searchForm");
        var downloadUrl = global.contextPath + '/loan/getLoanBatchImg';        
        var params = ajaxForm.serializeObject();        
        params.ary = ary;
        params.ary_xs = ary_xs;
        params.ary_ht = ary_ht;   
        params.type = type; 
        params.ary_zx = ary_zx;
        
//		$.downloadFile(downloadUrl,params,function(){
//			$.messager.alert('提示','下载成功！','info');
//		},function(data){
//			if(data!=null&&data.resMsg!=null){
//				$.messager.alert('警告',data.resCode+":"+data.resMsg,'warning');
//	       	}else{
//	       		 $.messager.alert('提示',"下载失败",'warning');
//	       	}
//		},window.parent);
    
    	$.downloadFile({
    		url:downloadUrl,
    		params:params,
    		successFunc:function(data){
    			if(data==null){
    				$.messager.alert('提示','下载成功！','info');
    			}else{
    				if(data.resMsg!=""){
    					$.messager.alert('提示',data.resMsg,'warning');		    
    				}else{
    					$.messager.alert('提示','下载失败！','warning');		  
    				}
    			}
    		},
    		failFunc:function(data){
    			$.messager.alert('提示','下载失败！','error');
    		},
    		maskWindow:window.parent
    		
    	});
        
    
    }
    
    

    
});
