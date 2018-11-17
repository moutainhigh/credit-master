<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>机构帐卡信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/zhuxue/zhuxueOrganizationCard.js');
					
					importJSExt(urlJs,function(){
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
		<!-- DataGrid 工具栏按钮 -->
		<div id="tb" style="padding:3px;padding-top: 0px;">
			<div style="width: 100%; height: 30px; border-top: 1px solid #ddd;">
			  <table style="width: 100%; height: 100%;">
			    <tr style="width: 100%; height: 100%;">
			      <td style="width: 5%;">机构名称：</td>
			      <td style="width: 16%;">${zhuxueOrganization.name}</td>
			      <td style="width: 5%;">机构代码：</td>
			      <td style="width: 12%;">${zhuxueOrganization.code}</td>
			      <td style="width: 6%;">机构总收入：</td>
			      <td style="width: 15%;">${totalIncome}</td>
			      <td style="width: 6%;">机构总支出：</td>
			      <td style="width: 15%;">${totalPay}</td>
			      <td style="width: 5%;">挂账金额：</td>
			      <td style="width: 15%;">${totalAmount}</td>
			    </tr>
			  </table>
			</div>
		</div>
		<!-- DataGrid 工具栏按钮 -->
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:75px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm">
				<table cellpadding="5">
					<tr>
						<td>交易开始日期：</td>
						<td><input class="easyui-datebox" type="text" data-options="editable: false"  style="width: 180px;" name="startDate" id="startDate"/></td>
						<td>交易结束日期 ：</td>
						<td>
						  <input class="easyui-datebox" type="text" data-options="editable: false"  style="width: 180px;" name="endDate" id="endDate"/>
						  <input class="easyui-text" type="hidden" value="${zhuxueOrganization.id}" name="zhuxueOrganizationId" id="zhuxueOrganizationId"/>
						</td>
						<td>
						  <a href="#" class="easyui-linkbutton" id="searchBut"  iconCls="icon-search" plain="true">查询</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px; width: 100%;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="dataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
	</body>
</html>