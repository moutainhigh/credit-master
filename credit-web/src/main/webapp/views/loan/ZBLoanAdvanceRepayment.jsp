<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>结清证明</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'form', 'tooltip', 'validatebox', 'combogrid' ],
			'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath
							+ '/resources/js/loan/ZBLoanAdvanceRepayment.js');
					importJSExt(urlJs, function() {
						/** 脚本加载成功回调方法 **/

					});
				});
			});
</script>

</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div
		data-options="region:'north',split:false,border:true,title:'查询条件',collapsible:false"
		style="padding:10px;height:75px;margin: 0px;">
		<div data-options="region:'center',fit:true">
			<form id="searchForm" style="width: 100%; height: 100%;">
				<table style="text-align: right:">
					<tr>
						<td>借款人:</td>
						<td><input class="easyui-validatebox easyui-textbox"
							type="text" id="personInfo.name" name="personInfo.name" /></td>
						<td>身份证号:</td>
						<td><input class="easyui-textbox" value=""
							validType="idCard" type="text" id="personInfo.idnum"
							name="personInfo.idnum" /></td>
						<td>合同编号:</td>
					    <td><input class="easyui-textbox" type="text" id="contractNum" name="contractNum" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div
		data-options="border:false,region:'center',title:'结清证明',noheader:true">
		<table id="LoanAdvanceRepaymentDataGrid" class="easyui-datagrid"
			title="结清证明" style="width: 100%; height: 100%"/>
		
				<div id="tb" style="padding: 3px;">
				<div style="margin-bottom: 0px">
		</div>
		<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
		<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" 
			data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>
	</div>

</body>
</html>
