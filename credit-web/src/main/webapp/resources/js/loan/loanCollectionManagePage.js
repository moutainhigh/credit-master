(function($){
	"use strict";

    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/loan/loanCollectionManageList';
    
    // 更新 已提交、未提交url
    var updateUrl = global.contextPath + '/loan/updateCollectionByStatus';
   
    // 导出excel文件url
    var exportUrl = global.contextPath + '/loan/exportLoanCollectionRecord';
	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){	    
	    //初始化档案管理表格
		$("#loanCollectionDataDataGrid").datagrid({
			columns : [[
					      //列定义
					      {
			                field : 'op10',
			                title : '<input class="dataGridCheckBoxTitle" type="checkbox"></input>',
			                width : '2%',
			                formatter : function(value, row, index) {
			                	row.index=index;
		                        return '<input  class="dataGridCheckBox" type="checkbox" data-loan-id="' + row.loanId + '" data-index="'+index+'" value="' + row.loanId + '">';
			                }
			              },
					      {field : 'borrowName',              title : '借款人',      align:"left", halign:"left", width : 10},
					      {field : 'borrowIdNum',             title : '身份证号',     align:"left", halign:"left",width : 10},
					      {field : 'signDate',         		   title : '放款日期',     align:"left", halign:"left",width : 10},
					      {field : 'contractNum',             title : '合同编号',     align:"left", halign:"left",width : 10},
					      {field : 'signDeptName',            title : '营业部',     align:"left", halign:"left",width : 10},
					      {field : 'operateType',             title : '提交状态',        align:"left", halign:"left",width : 10}					      
			]],
			rownumbers : false,			
			singleSelect:true,
			collapsible:false,
			isCanQuery:false,
			fitColumns:true,
			fit:true,
			autoRowHeight:true,
			striped:true,
			url:dataGridUrl,
			method:"POST",
			loadMsg:null,
			pagination:true,
			pageNumber:1,
			pageSize:100,
			pageList:[100],
			loader:function(param,success,error){
				var opts = $("#loanCollectionDataDataGrid").datagrid("options");
				var isCanQuery = opts.isCanQuery||false;
				if(isCanQuery){
					$("#bottonBox .easyui-linkbutton").linkbutton("disable");
					$.ajaxPackage({
						url:opts.url,
						async:true,
						type:opts.method,
						data:param,
						dataType:"json",
						isShowLoadMask:true,
						success:function(response, textStatus, jqXHR){
							if(response.resCode == "000000"){
								success(response.attachment);
							}else{
								$.messager.alert("提示", response.resMsg, "error", function(){
									error.apply(this, arguments);
								});
							}
						},
						error:function(response, textStatus, jqXHR){
							$.messager.alert("提示", "查询失败", "error", function(){
								error.apply(this, arguments);
							});
						},
						complete:function(jqXHR,textStatus){
							$("#bottonBox .easyui-linkbutton").linkbutton("enable");
						}
					});
				}
				return isCanQuery;
			},
			onLoadSuccess:function(data){
				$("#loanCollectionDataDataGrid").datagrid("resize");
			}
		});
		$("#loanCollectionDataDataGrid").datagrid("resize");
		
		$(".dataGridCheckBoxTitle").unbind();
		
	    /** 单机选择框 **/
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
	    });
	    
		$('.yesNoSubmit').click(function(){
			var $selected = $(".dataGridCheckBox:checked");
	        if($selected.length>0){
	        	 var statusText = $(this).text();
	        	 var status = $(this).data("status");
	        	 var tipMessage='您确定更新以下档案信息为【'+statusText+'】';
	        	 top.$.messager.confirm('提示', tipMessage, function(r){
	    			if (r){
	    				var ids = [];
	    				$selected.each(function(index,element){
	    					 ids.push($(this).data("loanId"));
	    				});
		           		$.ajaxPackage({
		           			type : 'get', 
		           			url : updateUrl+'?status='+status+'&ids='+ids,
		           			dataType : "json",
		           			success : function (data,textStatus,jqXHR) {				
		           				var resCode = data.resCode;				
		           				var resMsg = data.resMsg;
		           				var attachment = data.attachment;
		           				if (resCode == '000000') {
		           					$("#loanCollectionDataDataGrid").datagrid("reload");
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
	        	$.messager.alert('提示信息',"请选择相应的档案信息",'warning');
	            return false;
	        } 	
		});
	    
	    
	    //查询档案管理列表
	    $("#submitButton").bind("click",function(event){
	    	if($(this).linkbutton("options").disabled==false){
		    	if($("#conditionForm").form("validate")){
		    		var opts = $("#loanCollectionDataDataGrid").datagrid("options"),
		    		params = {};
		    		params = getParam();
			    	if($.isEmpty(params.borrowIdNum)&&$.isEmpty(params.borrowName)&&$.isEmpty(params.contractNum)&&$.isEmpty(params.signBeginDate)&&$.isEmpty(params.signEndDate)
			    			&&$.isEmpty(params.signDepartmentId)&&$.isEmpty(params.operateType)){
		    			$.messager.alert('提示','查询条件不能为空','warning');
		    		}else{
			    		opts.isCanQuery = true;
			    		$("#loanCollectionDataDataGrid").datagrid("load",params);
		    		}
		    	}
	    	}
	    });
	    
	    /** Excel导出处理 **/
	    $("#exportBtn").click(function(){
	    	if(!$("#conditionForm").form("validate")){
	            return;
	        }
	        // 导出校验
	        $.messager.confirm("提示","确认要导出Excel文件吗？",function(r){
	            if(r){
	            	var paramArray = getParam();
	                $.downloadFile({
	                    url:exportUrl,
	                    isDownloadBigFile:true,
	                    params:paramArray,
	                    successFunc:function(data){
	                        if(data== null){
	                            $.messager.alert('提示','下载成功！','info');
	                        }else{
	                            if(data.resMsg!= null){
	                                $.messager.alert('警告',data.resMsg,'warning');
	                            }else{
	                                $.messager.alert('异常','下载失败！','error');
	                            }
	                        }
	                    },
	                    failFunc:function(data){
	                        $.messager.alert('异常','下载失败！','error');
	                    }
	                });
	            }
	        });
	    });
	    //清空查询条件
	    $("#clearButton").bind("click",function(event){
	    	if($(this).linkbutton("options").disabled==false){
	    		$("#conditionForm").form("reset");
				$('.easyui-datebox').each(function(index,valueEle){ 
					$(valueEle).datebox('getIcon',0).css('visibility','hidden');
				});
	    	}
	    });
	    
	    //刷新档案管理列表
	    $(window).bind("refreashLoanFilesList",function(event,params){
	    	$("#loanCollectionDataDataGrid").datagrid("reload");
	    });
	    
	    function getParam(){
    		var params = {},
    		borrowName = $.trim($("#borrowName").textbox("getValue")),
    		borrowIdNum = $.trim($("#borrowIdNum").textbox("getValue")),
    		contractNum = $.trim($("#contractNum").textbox("getValue")),
    		signBeginDate = $.trim($("#signBeginDate").datebox("getValue")),
    		signEndDate = $.trim($("#signEndDate").datebox("getValue")),
    		signDepartmentId = $.trim($("#signDepartmentId").combo("getValue")),
    		operateType = $.trim($("#operateType").combo("getValue"));
    		if(borrowName!=""){
    			params.borrowName=borrowName;
    		}
    		if(borrowIdNum!=""){
    			params.borrowIdNum=borrowIdNum;
    		}
    		if(contractNum!=""){
    			params.contractNum=contractNum;
    		}
    		if(signBeginDate!=""){
    			params.signBeginDate=signBeginDate;
    		}
    		if(signEndDate!=""){
    			params.signEndDate=signEndDate;
    		}
    		if(signDepartmentId!=""){
    			params.signDepartmentId=signDepartmentId;
    		}
    		if(operateType !=""){
    			params.operateType=operateType;
    		}
    		return params
	    }
	});
})(jQuery);
