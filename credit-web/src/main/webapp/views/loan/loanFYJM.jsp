<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>费用减免申请</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'form', 'tooltip', 'validatebox' ], 'business',
			function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath
							+ '/resources/js/loan/loanFYJM.js');
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
	<!-- 新增面板 -->
	<div id="addDataPanel" class="easyui-window easyui-layout" title="费用减免申请面板" style="padding: 20px;">
		<form method="post" id="dataForm">
			<table>
				<tr>
					<td>减免金额:</td>
					<td><input class="easyui-numberbox" validType="numberRangeValid[1,999999]" data-options="required:true" precision="2" type="text" name="amount" id="amount" ></input></td>
				</tr>
			</table>
		</form>
		<div style="text-align: center; padding-top: 10px;">
			<a href="javascript:void(0)" name="submitBut" id="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
			<a href="javascript:void(0)" id="closeBut" name="closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>

	<div data-options="region:'north',split:false,border:true,title:'查询条件',collapsible:false" style="padding:5px;height:105px;margin: 0px;">
		<div>
			<form id="searchForm">
				<table>
					<tr>
						<td>姓名:</td>
						<td><input class="easyui-validatebox easyui-textbox" type="text" id="borrowerName" name="loanContract.borrowerName" /></td>
						<td>身份证号:</td>
						<td><input class="easyui-textbox" validType="idCard" value="" type="text" id="idnum" name="loanContract.idnum" /></td>
						<td>客户经理:</td>
						<td><input id="salesmanId" name="salesmanId" class="custComboGrid" configValue="{width:120,baseParm:{employeeType:'业务员',inActive:'t'}}" /></td>
						<td>手机:</td>
						<td style="width: 19%; height: 30px; text-align: left;"><input value="" class="easyui-textbox" type="text" id="mphone" validType="mobile" name="personInfo.mphone" /></td>
					</tr>
					<tr>
					    <td>合同编号:</td>
					    <td><input class="easyui-textbox" type="text" id="contractNum" name="contractNum" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div data-options="border:false,region:'center',title:'费用减免申请',noheader:true">
		<table id="LoanAdvanceRepaymentDataGrid" class="easyui-datagrid" />
		<div id="tb" style="padding: 3px;">
			<div style="margin-bottom: 0px">
				<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
				<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<sec:authorize ifAnyGranted="/system/loan/insertLoanSpecialRepaymentFYJM">
					<a href="#" class="easyui-linkbutton" id="apply" iconCls="icon-add" plain="true">申请</a> 
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/loan/updateLoanSpecialRepaymentFYJM">
					<a href="#" class="easyui-linkbutton" id="cancel" iconCls="icon-search" plain="true">取消</a> 
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/loan/log">
					<a href="#" class="easyui-linkbutton" id="log" iconCls="icon-search" plain="true">日志</a>
				</sec:authorize>
			</div>
		</div>
	</div>
</body>
</html>
