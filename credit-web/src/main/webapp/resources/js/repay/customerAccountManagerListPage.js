(function($){
	"use strict";
	
	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){
		
	    //初始化客户账号管理
		$("#customerAccountManagerDataGrid").datagrid({
			columns : [[
					      //列定义
					      {field : 'customerName',          title : '客户名称',     align:"left",  halign:"left", width : 10,formatter:function(value,row,index){
				    		  var str="";
				    		  if(row!=null&&row.customerId!=null){
				    			  if(row.zhuxuedaiOrg=='0'){
				    				  str="<a href='javascript:void(0)' class='customer-name' data-account-id='"+(row.accountId?row.accountId:"")+"' data-customer-id='"+ (row.customerId?row.customerId:"") +"'  data-customer-name='"+ (row.customerName?row.customerName:"") +"' data-loan-type='"+ (row.loanType?row.loanType:"") +"' >"+(value!=null?value:" ")+"</a>"	  
				    			  }else{
				    				  str = (value!=null?value:" ");
				    			  }
				    		  }
					    	  return str;
						  }},
					      {field : 'customerIdNum',         title : '证件号码(机构代码)',     align:"left",  halign:"left", width : 10},
					      {field : 'loanType',              title : '借款类型',     align:"left",  halign:"left", width : 8,formatter:function(value,row,index){
					    	  var str="";
					    	  if(row!=null&&row.customerId!=null&&value!=null){
					    		  str="<a href='javascript:void(0)' class='loan-type' data-account-id='"+(row.accountId?row.accountId:"")+"' data-customer-id='"+ (row.customerId?row.customerId:"") +"'  data-customer-name='"+ (row.customerName?row.customerName:"") +"' data-loan-type='"+ (row.loanType?row.loanType:"") +"'>"+(value!=null?value:" ")+"</a>"  
					    	  }
					    	  return str;
					      }},
					      {field : 'loanTrem',              title : '借款期限',     align:"left",  halign:"left", width : 8},
					      {field : 'grantMoneyDate',        title : '放款日期',     align:"left",  halign:"left", width : 10},
					      {field : 'firstRepaymentDate',    title : '首次还款日期',  align:"left",  halign:"left", width : 10},
					      {field : 'accountBalance',        title : '账户余额',     align:"left", halign:"left", width : 10},
					      {field : 'oneTimeRepaymentAll',        title : '一次性结清金额',     align:"left", halign:"left", width : 10,formatter:function(value,row,index){
							  if (!(row!=null && row.loanState == "预结清")) {
								  return "-";
							  }
							  return row.oneTimeRepaymentAll;
						  }},
					      {field : 'contractNum',           title : '合同编号',     align:"left", halign:"left", width : 10},
					      {field : 'operate',               title : '操作',        align:"left",  halign:"left", width : 14,formatter:function(value,row,index){
					    	  var str="";
					    	  if(row!=null&&row.customerId!=null){
					    		  var withdrawDepositsDisabled = false;
					    		  var accountBalance = row.accountBalance||"";
					    		  accountBalance=accountBalance.replace(",","");
					    		  if(isNaN(accountBalance - 0) || (accountBalance - 0) <= 0){
					    			  withdrawDepositsDisabled = true;
					    		  }
					    		  var prestoreDisabled = false;
					    		  if(row.loanState==="结清" || row.loanState==="预结清"){
					    			  prestoreDisabled = true;
					    		  }
					    		  str="<a href='javascript:void(0)' class='easyui-linkbutton withdrawDeposits' data-options='disabled:"+withdrawDepositsDisabled+"' style='margin-right: 20px;' data-zhuxuedai-org='"+(row.zhuxuedaiOrg?row.zhuxuedaiOrg:"")+"' data-account-id='"+(row.accountId?row.accountId:"")+"' data-customer-id='"+ (row.customerId?row.customerId:"") +"'  data-customer-name='"+ (row.customerName?row.customerName:"") +"' data-loan-type='"+ (row.loanType?row.loanType:"") +"'data-contract-num='"+(row.contractNum?row.contractNum:"")+"' ></a><a href='javascript:void(0)' class='easyui-linkbutton prestore' data-options='disabled:"+prestoreDisabled+"'   data-account-id='"+(row.accountId?row.accountId:"")+"' data-customer-id='"+ (row.customerId?row.customerId:"") +"'  data-customer-name='"+ (row.customerName?row.customerName:"") +"' data-loan-type='"+ (row.loanType?row.loanType:"")+"'data-contract-num='"+(row.contractNum?row.contractNum:"")+"' ></a>"
					    		  /*if(row.loanType=="助学贷"){
					    			  str+="<a href='javascript:void(0)' class='easyui-linkbutton repaymentEarnestMoney' style='margin-right: 20px;'  data-account-id='"+(row.accountId?row.accountId:"")+"' data-customer-id='"+ (row.customerId?row.customerId:"") +"'  data-customer-name='"+ (row.customerName?row.customerName:"") +"' data-loan-type='"+ (row.loanType?row.loanType:"") +"' ></a>"
					    		  }*/
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
			striped:true,
			url:global.contextPath+"/repay/customerAccountManagerList",
			method:"POST",
			loadMsg:null,
			pagination:true,
			pageNumber:1,
			pageSize:10,
			pageList:[10,20,30,40,50],
			loader:function(param,success,error){
				var opts = $("#customerAccountManagerDataGrid").datagrid("options");
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
				$("a.customer-name").unbind().bind("click",function(event){
					var loanId = $(this).data("accountId");
					var customerName = $(this).data("customerName");
					var customerId = $(this).data("customerId");
					top.$.iframeTabs.add({
						"id": 'personDetail_' + customerId,
						"text":customerName+"("+customerId+ ")- 详细资料",
						"url":global.contextPath + '/person/viewPersonDetailPage' + '/' + customerId
					});
				})
				$("a.loan-type").unbind().bind("click",function(event){
					var loanId = $(this).data("accountId");
                	var customerName = $(this).data("customerName");
                	var loanType = $(this).data("loanType");
                	top.$.iframeTabs.add({
                		"id": 'loanDetail_' + loanId,
                		"text":customerName+"("+loanType+ ")- 详细资料",
                		"url":global.contextPath + '/loanInfo/viewPersonLoanDetailPage/'+loanId
                	});
				})
				$(".easyui-linkbutton.withdrawDeposits").linkbutton({
					text:'提现',
	                plain:true,
	                iconCls:'icon-edit',
	                onClick:function(){
	                	if($(this).linkbutton("options").disabled==false){
		                	var accountId = $(this).data("accountId");
		                	var customerId = $(this).data("customerId");
		                	var loanType = $(this).data("loanType");
		                	var customerName = $(this).data("customerName");
		                	var contractNum = $(this).data("contractNum");
		                	var zhuxuedaiOrg = $(this).data("zhuxuedaiOrg");
		                	var queryParams = "accountId="+accountId+"&customerId="+customerId+"&zhuxuedaiOrg="+zhuxuedaiOrg;
		                	if(loanType!=""){
		                		queryParams+="&loanType="+loanType+"&contractNum="+contractNum;
		                	}
		                	top.$.iframeTabs.add({
		                		id:"CustomerAccoutManager-WithdrawDeposits-"+accountId+"-"+customerId+"-"+contractNum,
		                		text:"客户账户管理提现("+customerName+")",
		                		url:global.contextPath+"/repay/customerAccountManagerWithdrawDepositsPage?"+queryParams
		                	});
	                	}
	                }
				});
				$(".easyui-linkbutton.prestore").linkbutton({
					text:'预存',
	                plain:true,
	                iconCls:'icon-edit',
	                onClick:function(){
	                	if($(this).linkbutton("options").disabled==false){
		                	var accountId = $(this).data("accountId");
		                	var customerId = $(this).data("customerId");
		                	var loanType = $(this).data("loanType");
		                	var customerName = $(this).data("customerName");
		                	var contractNum = $(this).data("contractNum");
		                	var queryParams = "accountId="+accountId+"&customerId="+customerId;
		                	if(loanType!=""){
		                		queryParams+="&loanType="+loanType+"&contractNum="+contractNum;
		                	}
		                	top.$.iframeTabs.add({
		                		id:"CustomerAccoutManager-Prestore-"+accountId+"-"+customerId+"-"+contractNum,
		                		text:"客户账户管理预存("+customerName+")",
		                		url:global.contextPath+"/repay/customerAccountManagerPrestorePage?"+queryParams
		                	});
	                	}
	                }
				});
				$(".easyui-linkbutton.repaymentEarnestMoney").linkbutton({
					text:'还保障金',
	                plain:true,
	                iconCls:'icon-edit',
	                onClick:function(){
	                	var accountId = $(this).data("accountId");
	                	var customerId = $(this).data("customerId");
	                	var loanType = $(this).data("loanType");
	                	var customerName = $(this).data("customerName");
	                	var queryParams = "accountId="+accountId+"&customerId="+customerId;
	                	if(loanType!=""){
	                		queryParams+="&loanType="+loanType;
	                	}
	                	top.$.iframeTabs.add({
	                		id:"CustomerAccoutManager-Prestore-"+accountId+"-"+customerId,
	                		text:"客户账户管理还保障金("+customerName+")",
	                		url:global.contextPath+"/repay/customerAccountManagerRepaymentEarnestMoneyPage?"+queryParams
	                	});
	                }
				});				
				$("#customerAccountManagerDataGrid").datagrid("resize");
			}
		});
		$("#customerAccountManagerDataGrid").datagrid("resize");
		
	    //查询客户账号列表
	    $("#submitButton").bind("click",function(envent){
	    	if($(this).linkbutton("options").disabled==false){
		    	if($("#conditionForm").form("validate")){
		    		var customerName = $.trim($("#customerName").textbox("getValue")),
		    		customerIdNum = $.trim($("#customerIdNum").textbox("getValue")),
		    		contractNum = $.trim($("#contractNum").textbox("getValue"));
		    		if($.trim(customerName)!="" || $.trim(customerIdNum)!="" || $.trim(contractNum)){
			    		var opts = $("#customerAccountManagerDataGrid").datagrid("options"),
			    		params = {};
			    		opts.isCanQuery = true;
			    		if(customerName!=""){
			    			params.customerName=customerName;
			    		}
			    		if(customerIdNum!=""){
			    			params.customerIdNum=customerIdNum;
			    		}
			    		if(contractNum!=""){
			    			params.contractNum=contractNum;
			    		}
			    		$("#customerAccountManagerDataGrid").datagrid("load",params);
		    		}else{
		    			$.messager.alert("提示", "客户名称、证件号码和合同编号不能同时为空", "error");
		    		}
		    	}
	    	}
	    });
	    
	    //清空查询条件
	    $("#clearButton").bind("click",function(envent){
	    	if($(this).linkbutton("options").disabled==false){
	    		$("#conditionForm").form("clear");
	    		$("#errorMessage").show();
	    	}
	    });
	    
	    //刷新客户账号列表
	    $(window).bind("refreashcustomerAccountManagerList",function(event,params){
	    	$("#customerAccountManagerDataGrid").datagrid("reload");
	    });	    
	});
})(jQuery);
