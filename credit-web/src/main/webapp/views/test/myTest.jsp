<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>EasyUI示例</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript" charset="UTF-8" src="<%=request.getContextPath() %>/resources/js/test/myTest.js"></script>
		<style type="text/css">
			
		</style>
	</head>
	<body class="easyui-layout">
		<!-- DataGrid 工具栏按钮 -->
		<div id="tb" style="padding:3px;">
			<div style="margin-bottom:0px">
				<a href="#" class="easyui-linkbutton" id="addBut"  iconCls="icon-add"   plain="true">新增</a>
				<a href="#" class="easyui-linkbutton" id="updateBut"  iconCls="icon-save" plain="true">修改</a>
				<a href="#" class="easyui-linkbutton" id="delBut"  iconCls="icon-remove" plain="true">删除</a>
				<a href="#" class="easyui-linkbutton" id="searchBut"  iconCls="icon-search" plain="true">查询</a>
			</div>
		</div>
		<!-- DataGrid 工具栏按钮 -->
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:75px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm">
				<table cellpadding="5">
					<tr>
						<td>用户名:</td>
						<td><input class="easyui-textbox" type="text" name="userName" ></input></td>
						<td>姓名:</td>
						<td><input class="easyui-textbox" type="text" name="realName" ></input></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="testDataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		<!-- 新增面板 -->
		<div id="addDataPanel" class="easyui-window" title="新增、修改面板"  style="padding:20px;">
			<form method="post"  id="dataForm">
				<input type="hidden" name="id" />
				<table cellpadding="5">
					<tr>
						<td>用户名:</td>
						<td><input class="easyui-textbox" type="text" name="userName" data-options="required:true"></input></td>
					</tr>
					<tr>
						<td>姓名:</td>
						<td><input class="easyui-textbox" type="text" name="realName" data-options="required:true"></input></td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding-top:10px;">
				<a href="javascript:void(0)" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
				<a href="javascript:void(0)" name="closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
			</div>
		</div>
	</body>
</html>
