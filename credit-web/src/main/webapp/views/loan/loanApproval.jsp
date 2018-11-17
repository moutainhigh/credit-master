<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>费用减免审核</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'form', 'tooltip', 'validatebox' ], 'business',
			function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath
							+ '/resources/js/loan/loanApproval.js');
					urlJs.push(global.contextPath
							+ '/resources/js/common/salesManCommon.js');
					importJSExt(urlJs, function() {
						/** 脚本加载成功回调方法 **/

					});
				});
			});
</script>

</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div data-options="region:'north',split:false,border:true,title:'查询条件',collapsible:false" style="padding:5px;height:75px;margin: 0px;">
		<div>
			<form id="searchForm">
				<table>
					<tr>
						<td>姓名:</td>
						<td><input class="easyui-validatebox easyui-textbox" type="text" id="borrowerName" name="loanContract.borrowerName" /></td>
						<td>身份证号:</td>
						<td style="width: 19%; height: 30px; text-align: left;"><input class="easyui-textbox" type="text" value="" id="idnum"  validType="idCard" name="loanContract.idnum" /></td>
						<td>客户经理:</td>
						<td><input id="salesmanId" name="salesmanId" class="custComboGrid" configValue="{width:120,baseParm:{employeeType:'业务员',inActive:'t'}}" /></td>
						<td>手机:</td>
						<td style="width: 19%; height: 30px; text-align: left;"><input class="easyui-textbox" type="text" value="" id="mphone" validType="mobile" name="personInfo.mphone" /></td>
						<td></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div data-options="border:false,region:'center',title:'费用减免审核',noheader:true">
		<table id="LoanAdvanceRepaymentDataGrid" class="easyui-datagrid" title="结清证明" style="width: 100%; height: 100%" data-options="border:true,noheader:true,singleSelect:true,collapsible:true,toolbar:'#tb'" />
		<div id="tb" style="padding: 3px;">
			<div style="margin-bottom: 0px">
				<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
				<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<sec:authorize ifAnyGranted="/system/loan/approvalSuccess">
					<a href="#" class="easyui-linkbutton" id="apply" iconCls="icon-add"	plain="true">审核</a> 
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/loan/approvalNO">
					<a href="#" class="easyui-linkbutton" id="cancel" iconCls="icon-search" plain="true">拒绝</a> 
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/loan/log">
					<a href="#"	class="easyui-linkbutton" id="log" iconCls="icon-search" plain="true">日志</a>
				</sec:authorize>
			</div>
		</div>
	</div>
</body>
</html>
