<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>客户账户管理还保障金</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/repay/customerAccountManagerRepaymentEarnestMoneyPage.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				})
			});
		</script>			
	</head>
	<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />		
	<c:if test="${resCode == '000000'}">
		<div id="params" style="position: none" data-account-id="${customerAccountInfo.accountId}" data-customer-id="${customerAccountInfo.customerId}" data-account-balance="${customerAccountInfo.accountBalance}" data-loan-state="${customerAccountInfo.loanState}" data-loan-type="${customerAccountInfo.loanType}"></div>
		<div data-options="border:false,region:'center',noheader:true">
			<div class="easyui-panel" title="还保障金" style="width:100%;height:100%;"  data-options="fit:true">   
				<div style="width:1030px;margin-left: auto;margin-right: auto;">
					<form id="conditionForm">
						<table style="width:auto; border-spacing:10px;table-layout:fixed;">
							<tr >
								<td style="width: 70px; height:30px;text-align: left;font-size: 1.1em;">客户名称：</td>
								<td style="width: 150px;height:30px;text-align: left;font-size: 1.1em;">${customerAccountInfo.customerName}</td>
								<td style="width: 140px;height:30px;text-align: left;font-size: 1.1em;">证件号码(机构代码)：</td>
								<td style="width: 120px;height:30px;text-align: left;font-size: 1.1em;">${customerAccountInfo.customerIdNum}</td>
								<td style="width: 70px;height:30px;text-align: left;font-size: 1.1em;">账户余额：</td>
								<td id="accountBalance" style="width: 100px;height:30px;text-align: left;font-size: 1.1em;">${customerAccountInfo.accountBalance}</td>
								<td style="width: 70px;height:30px;text-align: left;font-size: 1.1em;">机构名称：</td>
								<td style="width: 200px;height:30px;text-align: left;font-size: 1.1em;">${customerAccountInfo.organizationName}</td>
							</tr>
							<tr >
								<td style="height:30px;text-align: left;font-size: 1.1em;">还款金额：</td>
								<td style="height:30px;text-align: left;font-size: 1.1em;">
									<input class="easyui-textbox" type="text" id="amount" name="amount" data-options="missingMessage:'还款金额为必输项',required:true,validType:['positiveFloatNumer[\'还款金额\']','maxLength[18,\'还款金额\']']"  style="height: 28px;width: 150px;font-size: 1.1em;"/>
								</td>
								<td colspan="6" ></td>
							</tr>
							<tr >
								<td style="height:30px;text-align: left;font-size: 1.1em;">备注：</td>
								<td colspan="7" style="height:100px;text-align: left;font-size: 1.1em;">
									<input class="easyui-textbox" type="text" id="memo" name="memo" data-options="missingMessage:'备注为必输项',required:true,validType:'maxLength[255,\'客户名称\']',multiline:true,fit:true"  style="height: 100px;width:800px;font-size: 1.1em;"/>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div id="bottonBox" style="width: 960px; height: 30px; margin:auto; text-align: center; ">
					<sec:authorize ifAnyGranted="/repay/customerAccountManagerRepaymentEarnestMoney">
						<a href="javascript:void(0)" id="submitButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:80px">还款</a>
		   			</sec:authorize>				
					<a href="javascript:void(0)" id="clearButton" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px">清空</a>
				</div>
			</div> 
		</div>
	</c:if>
	<c:if test="${resCode != '000000'}">
		<div style="width: 400px; height: auto; margin-left: auto; margin-right: auto; text-align: center;">
			${resMsg}
		</div>
	</c:if>
	</body>
</html>