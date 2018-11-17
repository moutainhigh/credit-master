(function($){
	"use strict";

	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){
		//设置日期组件显示格式
	    $('.easyui-datebox').datebox('addClearButton', 'icon-clear');
	    
	    //初始化档案管理表格
		$("#loanFilesInfoDataGrid").datagrid({
			columns : [[
					      //列定义
					      {field : 'borrowName',              title : '借款人',      align:"left", halign:"left", width : 10},
					      {field : 'loanType',                title : '借款类型',     align:"left", halign:"left",width : 10},
					      {field : 'borrowIdNum',             title : '身份证号',     align:"left", halign:"left",width : 10},
					      {field : 'salesmanName',            title : '客户经理',     align:"left", halign:"left",width : 10},
					      {field : 'crmName',                 title : '客服',        align:"left", halign:"left",width : 10},
					      {field : 'money',                   title : '放款金额',     align:"left", halign:"left",width : 10},
					      {field : 'grantMoneyDate',          title : '放款日期',     align:"left", halign:"left",width : 10},
					      {field : 'time',                    title : '借款期限',     align:"left", halign:"left",width : 10},
					      {field : 'contractNum',             title : '合同编号',     align:"left", halign:"left",width : 10},
					      {field : 'isCreateLoanFilesInfo',   title : '操作',        align:"left", halign:"left",width : 10,formatter:function(value,row,index){
					    	  var str = "";
					    	  if(value == 1){
					    		  str = "<a href='javascript:void(0)' class='easyui-linkbutton loanFilesInfoUpdate' style='margin-right: 20px;'  loan-id='"+row.loanId+"' borrow-name='"+ row.borrowName +"' ></a><a href='javascript:void(0)' class='easyui-linkbutton loanFilesInfoDetail' loan-id='"+row.loanId+"' borrow-name='"+ row.borrowName +"'contractNum='"+row.contractNum+"'></a>"
					    	  }else if(value == 0){
					    		  str = "<a href='javascript:void(0)' class='easyui-linkbutton loanFilesInfoAdd' loan-id='"+row.loanId+"' borrow-name='"+ row.borrowName +"' contractNum='"+row.contractNum+"'></a>";
					    	  }
					    	  return str;
					      }}
			]],
			rownumbers : true,			
			singleSelect:true,
			collapsible:false,
			isCanQuery:false,
			fitColumns:true,
			fit:true,
			autoRowHeight:true,
			striped:true,
			url:global.contextPath+"/loan/loanFilesInfoList",
			method:"POST",
			loadMsg:null,
			pagination:true,
			pageNumber:1,
			pageSize:10,
			pageList:[10,20,30,40,50],
			loader:function(param,success,error){
				var opts = $("#loanFilesInfoDataGrid").datagrid("options");
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
				$(".easyui-linkbutton.loanFilesInfoAdd").linkbutton({
					text:'新建',
	                plain:true,
	                iconCls:'icon-add',
	                onClick:function(){
	                	var loanId = $(this).attr("loan-id");
	                	var borrowName = $(this).attr("borrow-name");
	                	top.$.iframeTabs.add({
	                		id:"loanFilesInfo-Add-"+loanId,
	                		text:"新建客户档案("+borrowName+")",
	                		url:global.contextPath+"/loan/loanFilesInfoAddPage?loanId="+loanId
	                	});
	                }
				});
				$(".easyui-linkbutton.loanFilesInfoUpdate").linkbutton({
					text:'重建',
	                plain:true,
	                iconCls:'icon-edit',
	                onClick:function(){
	                	var loanId = $(this).attr("loan-id");
	                	var borrowName = $(this).attr("borrow-name");
	                	top.$.iframeTabs.add({
	                		id:"loanFilesInfo-Update-"+loanId,
	                		text:"重建客户档案("+borrowName+")",
	                		url:global.contextPath+"/loan/loanFilesInfoUpdatePage?loanId="+loanId
	                	});
	                }
				});
				$(".easyui-linkbutton.loanFilesInfoDetail").linkbutton({
					text:'查看',
	                plain:true,
	                iconCls:'icon-search',
	                onClick:function(){
	                	var loanId = $(this).attr("loan-id");
	                	var borrowName = $(this).attr("borrow-name");
	                	top.$.iframeTabs.add({
	                		id:"loanFilesInfo-Detail-"+loanId,
	                		text:"查看客户档案("+borrowName+")",
	                		url:global.contextPath+"/loan/loanFilesInfoDetailPage?loanId="+loanId
	                	});
	                }
				});
				$("#loanFilesInfoDataGrid").datagrid("resize");
			}
		});
		$("#loanFilesInfoDataGrid").datagrid("resize");
		
	    //查询档案管理列表
	    $("#submitButton").bind("click",function(event){
	    	if($(this).linkbutton("options").disabled==false){
		    	if($("#conditionForm").form("validate")){
		    		var opts = $("#loanFilesInfoDataGrid").datagrid("options"),
		    		params = {},
		    		borrowName = $.trim($("#borrowName").textbox("getValue")),
		    		borrowMphone = $.trim($("#borrowMphone").textbox("getValue")),
		    		borrowIdNum = $.trim($("#borrowIdNum").textbox("getValue")),
		    		salesmanId = $.trim($("#salesMan").combo("getValue")),
		    		grantMoneyDateStart = $.trim($("#grantMoneyDateStart").datebox("getValue")),
		    		grantMoneyDateEnd = $.trim($("#grantMoneyDateEnd").datebox("getValue")),
		    		contractNum = $.trim($("#contractNum").textbox("getValue"));
		    		if(borrowName!=""){
		    			params.borrowName=borrowName;
		    		}
		    		if(borrowMphone!=""){
		    			params.borrowMphone=borrowMphone;
		    		}
		    		if(borrowIdNum!=""){
		    			params.borrowIdNum=borrowIdNum;
		    		}
		    		if(salesmanId!=""){
		    			params.salesmanId=salesmanId;
		    		}
		    		if(grantMoneyDateStart!=""){
		    			params.grantMoneyDateStart=grantMoneyDateStart;
		    		}
		    		if(grantMoneyDateEnd!=""){
		    			params.grantMoneyDateEnd=grantMoneyDateEnd;
		    		}
		    		if(contractNum!=""){
		    			params.contractNum=contractNum;
		    		}
			    	if(Object.keys(params).length!=0){
			    		opts.isCanQuery = true;
			    		$("#loanFilesInfoDataGrid").datagrid("load",params);
		    		}else{
		    			$.messager.alert('提示','查询条件不能为空','warning');
		    		}
		    	}
	    	}
	    });
	    
	    //清空查询条件
	    $("#clearButton").bind("click",function(event){
	    	if($(this).linkbutton("options").disabled==false){
	    		$("#conditionForm").form("clear");
				$('.easyui-datebox').each(function(index,valueEle){ 
					$(valueEle).datebox('getIcon',0).css('visibility','hidden');
				});
	    	}
	    });
	    
	    //刷新档案管理列表
	    $(window).bind("refreashLoanFilesList",function(event,params){
	    	$("#loanFilesInfoDataGrid").datagrid("reload");
	    });
	});
})(jQuery);
