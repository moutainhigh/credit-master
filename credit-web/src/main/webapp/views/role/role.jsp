
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<title>角色管理</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
</style>
<script type="text/javascript">
	importPluginsExt(['panel','combobox','window','layout','datagrid','pagination'
                  ,'form','tooltip','validatebox'],'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/js/role/role.js');
			
			importJSExt(urlJs,function(){
				/** 脚本加载成功回调方法 **/
				
			});
		});
	});
</script>
</head>
<body class="easyui-layout">
<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-panel" title="查询条件"
		style="padding: 5px; height: 75px; margin: 0px;"
		data-options="region:'north'">
		<form id="searchForm">
			<table cellpadding="5">
				<tr>
					<td>角色名称:</td>
					<td><input class="easyui-textbox" type="text" name="roleName" data-options="validType:'maxLength[50,\'名称\']'"></input></td>
					<td>创建人:</td>
					<td><input class="easyui-textbox" type="text" name="creator"></input></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;"
		data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="roleDataGrid" class="easyui-datagrid" data-options=""></table>
		<div id="tb" style="padding: 3px;">
			<div style="margin-bottom: 0px">
				<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
				<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<sec:authorize ifAnyGranted="/system/role/saveOrUpdateDataRole">
					<a href="#" class="easyui-linkbutton" id="addBut" iconCls="icon-add" plain="true">新增</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/role/saveOrUpdateDataRole">
					<a href="#" class="easyui-linkbutton" id="updateBut" iconCls="icon-save" plain="true">修改</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/role/deleteDataRole">
					<a href="#" class="easyui-linkbutton" id="delBut" iconCls="icon-remove" plain="true">删除</a>
				</sec:authorize>
				<%-- <sec:authorize ifAnyGranted="/system/ComEmployeeRole/insertRoleHierarchy">
					<a href="#" class="easyui-linkbutton" id="createRoleBut"
						iconCls="icon-add" plain="true">可分配角色</a>
				</sec:authorize> --%>
				<sec:authorize ifAnyGranted="/system/ComRolePermission/insertComRolePermission">
					<a href="#" class="easyui-linkbutton" id="createMuneBut" iconCls="icon-add" plain="true">分配菜单权限</a>
				</sec:authorize>
			</div>
		</div>
	</div>

	<!-- 分配菜单权限面板 -->
	<div id="createMunePanel" class="easyui-window" title="分配菜单权限"
		style="padding: 20px;">
		<ul id="productTree" class="easyui-tree"></ul>
		<input name="roleId" type="hidden" /> <br />
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" id="productTreeSubmit" data-options="iconCls:'icon-ok'">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton commonCloseBut" id="productTreeColse" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
	<!-- 分配角色面板 -->
	<div id="createRolePanel" class="easyui-window" title="分配角色权限"
		style="padding: 20px;">
		<ul id="roleTree" class="easyui-tree"></ul>
		<input name="roleId" type="hidden" /> <br />
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" id="roleTreeSubmit">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton commonCloseBut" id="roleTreeColse">取消</a>
		</div>
	</div>
	<!-- 新增面板 -->
	<div id="addDataPanel" class="easyui-window" title="修改面板"
		style="padding: 20px;">
		<form method="post" id="dataForm">
			<input type="hidden" name="id" />
			<table cellpadding="5" style="width: 280px">
				<tr>
					<td>角色名:</td>
					<td><input class="easyui-textbox" type="text" name="roleName" data-options="validType:'maxLength[50,\'名称\']',required:true"></input></td>
				</tr>
				<tr>
					<td>所属部门:</td>
					<td><input class="easyui-combobox" id="sp1" name="sp1" style="width: 200px;" data-options="valueField:'codeName',textField:'codeTitle','editable':false,panelHeight:'auto',required:true"></input></td>
				</tr>		
			</table>
		</form>
		<div style="text-align: center; padding-top: 10px;">
			<a href="javascript:void(0)" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
			<a href="javascript:void(0)" name="closeBut" class="easyui-linkbutton commonCloseBut" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
</body>
</html>
