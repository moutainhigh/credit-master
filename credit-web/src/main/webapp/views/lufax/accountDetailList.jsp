<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>员工管理</title>
<%
	String contextPath = request.getContextPath();
	session.setAttribute("path", contextPath);
%>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=contextPath %>/resources/css/sysIcon.css"/>
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'form', 'tooltip', 'validatebox' ], 'business',
			function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
		            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
					urlJs.push(global.contextPath + '/resources/js/lufax/accountDetail.js');
					importJSExt(urlJs, function() {
						/** 脚本加载成功回调方法 **/

					});
				});
			});
</script>
<style type="text/css">
</style>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-panel" title="查询条件"
		style="padding: 5px; height: 75px; margin: 0px;"
		data-options="region:'north'">
		<form id="searchForm">
			<table cellpadding="5">
				<tr>
					<td>日期：</td>
                    <td>
                        <input class="easyui-datebox" id="startDate" name="startDate" style="width:120px" value="${sysdate}" data-options="validType:'date'"/> ~
                        <input class="easyui-datebox" id="endDate" name="endDate" style="width:120px" value="${sysdate}" data-options="validType:'date'"/>
                    </td>					
					<td>类型:</td>
					<td>
						<select class="easyui-combobox" id='accountTradeType' name='accountTradeType' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<option value='' >全部</option>
							<c:forEach var = "accountTradeType" items = "${accountTradeTypes}">
								<option value="${accountTradeType.code}">${accountTradeType.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;"
		data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="testDataGrid" class="easyui-datagrid" data-options=""></table>
		<%--                <c:if test=""></c:if> --%>
		<!--               <input type="button" value="新增"/> -->
		<div id="tb" style="padding: 3px;">
			<div style="margin-bottom: 0px">
			<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
			<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" class="easyui-linkbutton" id="rechargeWindow" iconCls="icon-add" plain="true">充值</a>
				<a href="#" class="easyui-linkbutton" id="withdrawalsWindow" iconCls="icon-edit" plain="true" data-options="disabled:true">提现</a>
				<a href="#" class="easyui-linkbutton" id="importDetailToExc" iconCls="icon-save" plain="true">导出</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<font style="color: red;font-weight: bold;">余额：</font><a id="totalAmt" style="color: red;font-weight: bold;"></a>&nbsp;(单位：元)
			</div>
		</div>
	</div>
	<!-- 充值 -->
	<div id="rechargePanel" class="easyui-window" title="充值" style="padding: 20px;" data-options="width:400,height:270,modal:true,collapsible:false,minimizable:false,maximizable:false">
		<div>
			<form method="post" id="dataForm">
				<table style="text-align: right">
					<tr>
						<td>充值金额：
							<input class="easyui-numberbox" style="width:250px"  id="amount" name="amount" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true,prompt:'请输入充值金额'"/>
						</td>
					</tr>
					<tr>
						<td>备&nbsp;&nbsp;注：
							<input class="easyui-textbox" id="memo" name="memo" data-options="multiline:true,prompt:'请输入备注信息',required:true" style="width:250px;height:100px"/>
						</td>
					</tr>
				</table>
			</form>
			<br />
			<div style="text-align: center; padding: 5px">
				<a href="#" class="easyui-linkbutton" id="rechargeSubmit" iconCls="icon-ok">充值</a>
			</div>
		</div>
	</div>
	<!-- 提现 -->
	<div id="withdrawalsPanel" class="easyui-window" title="提现" style="padding: 20px;" data-options="width:400,height:270,modal:true,collapsible:false,minimizable:false,maximizable:false">
		<div>
			<form method="post" id="dataFormTx">
				<table style="text-align: right">
					<tr>
						<td>提现金额：
							<input class="easyui-numberbox" style="width:250px"  id="amountTx" name="amountTx" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true,prompt:'请输入充值金额'"/>
						</td>
					</tr>
					<tr>
						<td>备&nbsp;&nbsp;注：
							<input class="easyui-textbox" id="memoTx" name="memoTx" data-options="multiline:true,prompt:'请输入备注信息',required:true" style="width:250px;height:100px"/>
						</td>
					</tr>
				</table>
			</form>
			<br />
			<div style="text-align: center; padding: 5px">
				<a href="#" class="easyui-linkbutton" id="withdrawalsSubmit" iconCls="icon-ok">提现</a>
			</div>
		</div>
	</div>
</body>
</html>
