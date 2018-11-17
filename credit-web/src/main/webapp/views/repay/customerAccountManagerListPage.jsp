<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>客户账户管理列表</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/repay/customerAccountManagerListPage.js');
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
		<div data-options="region:'north',split:false,border:true,title:'查询条件',collapsible:false" style="height: 80px;">
			<div style="height: 100%;">
				<form id="conditionForm">
					<table style="height: 100%;border-spacing:10px">
						<tr >
							<td   >
								客户名称：
							</td>
							<td >
								<input class="easyui-textbox" type="text" id="customerName" data-options="validType:'maxLength[80,\'客户名称\']'" />
							</td>
							<td >
								证件号码(机构代码)：
							</td>
							<td >
								<input class="easyui-textbox" type="text" id="customerIdNum" data-options=""  />
							</td>
							<td >
								合同编号：
							</td>
							<td >
								<input class="easyui-textbox" type="text" id="contractNum" name ="contractNum" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div id="customerAccountManagerToolbar" style="padding: 3px;">
			<div id="bottonBox" style="margin-bottom: 0px">
				<a href="javascript:void(0)" id="submitButton"  class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" style="width:60px">查询</a>
				<a href="javascript:void(0)" id="clearButton" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false,region:'center'">
			<table id="customerAccountManagerDataGrid" class="easyui-datagrid" title="客户账户管理列表"   data-options="toolbar:'#customerAccountManagerToolbar',border:true,noheader:true,singleSelect:true,collapsible:true"/>
		</div>
	</c:if>
	<c:if test="${resCode != '000000'}">
		<div style="width: 400px; height: auto; margin-left: auto; margin-right: auto; text-align: center;">
			${resMsg}
		</div>
	</c:if>
	</body>
</html>