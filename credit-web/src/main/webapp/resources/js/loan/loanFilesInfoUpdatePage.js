(function($){
	"use strict";
	
	/**
	 * 初始化更新档案管理页面
	 */
	var initLoanFilesInfo = function(){
		//设置其他内容为readonly
		$("#loanFilesInfoForm :checkbox[value='其他：']").each(function(index,valueEle){
			var value = $(valueEle).nextAll(".easyui-textbox").attr("value")||"";
			if($(valueEle).prop("checked")){
				$(valueEle).nextAll(".easyui-textbox").textbox({"readonly":false,"value":value,required:true});
			}else{
				$(valueEle).nextAll(".easyui-textbox").textbox({"readonly":true,"value":value,required:false});
			}
		});
		
		//初始化数量控制
		$("#loanFilesInfoForm :checkbox:first-child").each(function(index,valueEle){
			var parent = $(valueEle).parent();
			var isChecked = false;
			$(":checkbox",parent).each(function(index, valueEleTemp) { 
				if($(valueEleTemp).prop("checked") == true){
					isChecked = true;
				}
			});
			$(".easyui-textbox",parent.next()).textbox({required:isChecked});
		})
	};
	
	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){
		
		//控制其他内容验证
		$("#loanFilesInfoForm :checkbox[value='其他：']").bind("click",function(event){
			var tempJq = $(this);
			if(tempJq.prop("checked") == true){
				tempJq.nextAll(".easyui-textbox").textbox({"readonly":false,required:true});
			}else{
				tempJq.nextAll(".easyui-textbox").textbox({"readonly":true,"value":"",required:false});
			}
		});
		
		//控制凭证数量验证
		$("#loanFilesInfoForm :checkbox").bind("click",function(event){
			var parentJq = $(this).parent();
			var isChecked = false;
			$(":checkbox",parentJq).each(function(index, valueEle) { 
				if($(valueEle).prop("checked") == true){
					isChecked = true;
				}
			});
			$(".easyui-textbox",parentJq.next()).textbox({required:isChecked});
		});
		
	    //更新档案
	    $("#submitButton").bind("click",function(envent){
	    	if($(this).linkbutton("options").disabled==false){
		    	if($("#loanFilesInfoForm").form("validate")){
		    		var params = {};
		    		params.loanId=$("#params").attr("loan-id");
		    		params.version=$("#params").attr("version");
		    		
		    		//设置数量
		    		$("#loanFilesInfoForm .easyui-textbox[textboxname$='Count']").each(function(index,valueEle){
		    			var key = $(valueEle).attr("textboxname");
		    			var value = $.trim($(valueEle).textbox("getValue"));
		    			if(!isNaN(Number(value))){
		    				params[key]=value;
		    			}
		    		});
		    		
		    		//设置文本
		    		$("#loanFilesInfoForm .easyui-textbox:not([textboxname$='Count'])").each(function(index,valueEle){
		    			var key = $(valueEle).attr("textboxname");
		    			var value = $.trim($(valueEle).textbox("getValue"));
		    			if(value!=""){
		    				params[key]=value;
		    			}
		    		});
		    		
		    		//设置checkbox值
		    		$("#loanFilesInfoForm :checkbox[value!='其他：']").each(function(index,valueEle){
		    			if($(valueEle).prop("checked")){
		    				var key = $(valueEle).attr("name");
			    			var value = $.trim($(valueEle).val());
			    			if(value.length>0){
			    				var valueTemp = params[key]||"";
			    				if(valueTemp.length>0){
			    					valueTemp+=",";
			    				}
			    				valueTemp+=value;
			    				params[key]=valueTemp;
			    			}
		    			}
		    		});
		    		
		    		//发送查询请求
		    		$("#bottonBox .easyui-linkbutton").linkbutton("disable");
					$.ajaxPackage({
						url:global.contextPath+"/loan/loanFilesInfoUpdate",
						async:true,
						type:"POST",
						data:params,
						dataType:"json",
						isShowLoadMask:true,
						success:function(response, textStatus, jqXHR){
							if(response.resCode == "000000"){
								$.messager.alert("提示", "更新客户档案成功", "info", function(){
									var loanId = params.loanId;
				                	var borrowName = $.trim($("#borrowerName").text());
				                	//重新客户档案管理详细窗口
									top.$.iframeTabs.close({
										id:"loanFilesInfo-Detail-"+loanId,
				                		text:"查看客户档案("+borrowName+")"
									});
				                	top.$.iframeTabs.add({
				                		id:"loanFilesInfo-Detail-"+loanId,
				                		text:"查看客户档案("+borrowName+")",
				                		url:global.contextPath+"/loan/loanFilesInfoDetailPage?loanId="+loanId
				                	});
				                	//关闭重建客户档案窗口
									top.$.iframeTabs.close({
										id:"loanFilesInfo-Update-"+loanId,
				                		text:"重建客户档案("+borrowName+")"
									});
								});
							}else{
								$.messager.alert("提示", response.resMsg, "error");
							}
						},
						error:function(response, textStatus, jqXHR){
							$.messager.alert("提示", "查询失败", "error");
						},
						complete:function(jqXHR,textStatus){
							$("#bottonBox .easyui-linkbutton").linkbutton("enable");
						}
					});
		    	}
	    	}
	    });
	    
	    //清空表单
	    $("#resetButton").bind("click",function(envent){
	    	if($(this).linkbutton("options").disabled==false){
		    	$("#loanFilesInfoForm").form("reset");
		    	initLoanFilesInfo();
	    	}
	    });
	    
	    //跳到查询列表
	    $("#loanFilesInofListButton").bind("click",function(envent){
	    	if($(this).linkbutton("options").disabled==false){
		    	top.$.iframeTabs.add({
	        		text:"客户档案管理",
	        		url:global.contextPath+"/loan/loanFilesInfoListPage"
	        	});
	    	}

	    });
		
		//初始化更新界面
	    initLoanFilesInfo();
	});
})(jQuery);