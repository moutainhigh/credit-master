(function($){
	"use strict";
	
	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){
	    //绑定提交按钮事件
	    $("#submitButton").bind("click",function(envent){
	    	if($(this).linkbutton("options").disabled==false){
		    	if($("#conditionForm").form("validate")){
		    		
		    		var params = $("#conditionForm").serializeObject()||{};
		    		params.accountId = $("#params").data("accountId");
		    		params.customerId = $("#params").data("customerId");
		    		params.loanType = $("#params").data("loanType");

					$("#bottonBox .easyui-linkbutton").linkbutton("disable");
					$.ajaxPackage({
						url:global.contextPath+"/repay/customerAccountManagerPrestore",
						async:true,
						type:"POST",
						data:params,
						dataType:"json",
						isShowLoadMask:true,
						success:function(response, textStatus, jqXHR){
							if(response.resCode == "000000"){
								refreshCustomerAccountInfo(params.accountId,params.customerId,params.loanType);
								$.messager.alert("提示", "预存操作成功", "info");
								$("#conditionForm").form("clear");
					        	//刷新客户账号管理列表
			                	top.iframeDispatchEvent("refreashcustomerAccountManagerList");
							}else{
								$.messager.alert("提示", response.resMsg, "error");
							}
						},
						error:function(response, textStatus, jqXHR){
							$.messager.alert("提示", "预存操作失败", "error");
						},
						complete:function(jqXHR,textStatus){
							$("#bottonBox .easyui-linkbutton").linkbutton("enable");
						}
					});
		    	}
	    	}
	    });
	    
	    //清空查询条件
	    $("#clearButton").bind("click",function(envent){
	    	if($(this).linkbutton("options").disabled==false){
	    		$("#conditionForm").form("clear");
	    	}
	    });
	    
	    //刷新客户账户信息
	    function refreshCustomerAccountInfo(accountId,customerId,loanType){
	    	var params = {};
    		params.accountId = accountId;
    		params.customerId = customerId;
    		params.loanType = loanType;
			$.ajaxPackage({
				url:global.contextPath+"/repay/findCustomerAccountInfo",
				async:true,
				type:"POST",
				data:params,
				dataType:"json",
				isShowLoadMask:false,
				success:function(response, textStatus, jqXHR){
					if(response.resCode == "000000"){
						$("#accountBalance").html( response.attachment.customerAccountInfo.accountBalance+"");
					}else{
						$.messager.alert("提示", response.resMsg, "error");
					}
				},
				error:function(response, textStatus, jqXHR){
					$.messager.alert("提示", "查询客户账户信息失败", "error");
				}
			});
	    }
	});
})(jQuery);
