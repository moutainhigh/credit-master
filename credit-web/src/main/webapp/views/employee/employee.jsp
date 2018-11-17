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
					urlJs.push(global.contextPath + '/resources/js/employee/employee.js');
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
					<td>姓名:</td>
					<td><input class="easyui-textbox" type="text" name="name"></input></td>
					<td>工号:</td>
					<td><input class="easyui-textbox" type="text" name="usercode"></input></td>
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
				<sec:authorize ifAnyGranted="/system/user/insertComEmployee">
					<a href="#" class="easyui-linkbutton" id="addBut" iconCls="icon-add" plain="true">新增</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/user/updateEmployee">
					<a href="#" class="easyui-linkbutton" id="updateBut" iconCls="icon-save" plain="true">修改</a>
				</sec:authorize>
<%-- 				<sec:authorize ifAnyGranted="/system/user/deleteEmployee"> --%>
<!-- 					<a href="#" class="easyui-linkbutton" id="delBut" -->
<!-- 						iconCls="icon-remove" plain="true">离职</a> -->
<%-- 				</sec:authorize> --%>
				<%-- <sec:authorize ifAnyGranted="/system/ComEmployeeRole/insertComEmployeeRole">
					<a href="#" class="easyui-linkbutton" id="createRoleBut"
						iconCls="icon-add" plain="true">分配角色</a>
				</sec:authorize> --%>
				<sec:authorize ifAnyGranted="/system/ComEmployeeRole/insertComEmployeeRole">
					<a href="#" class="easyui-linkbutton" id="createRoleBut" iconCls="icon-add" plain="true">分配角色</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/ComEmployeePermission/insertComEmployeePermission">
					<a href="#" class="easyui-linkbutton" id="createMuneBut" iconCls="icon-add" plain="true">分配菜单权限</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/user/entryImportEmp,/system/user/quitImportEmp,/system/user/ydImportEmp">
					<a href="#" class="easyui-linkbutton" id="import" iconCls="pic_52" plain="true">批量导入</a>
				</sec:authorize>
			</div>
		</div>
	</div>

 <!-- Excel导入面板 -->
    <div id="importExcelWin" class="easyui-window editContentPanel" title="Excel批量导入" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false" style="width:550px;padding:0px;">
        <sec:authorize ifAnyGranted="/system/user/entryImportEmp">
	        <div style="text-align:center;padding: 0px; margin: 20px 0px;" >
	            <form  id="entryFileForm"  enctype="multipart/form-data" method="post">
	                <label>入职批量导入：</label>
	                <input class="easyui-filebox" id="entryfile" name="entryfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
	                <a href="#" class="easyui-linkbutton" id="entryImport"  iconCls="pic_52" plain="false">导入</a>
	            </form>
	        </div>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/system/user/quitImportEmp">
	        <div style="text-align:center;padding: 0px; margin: 20px 0px;" >
	            <form  id="quitFileForm"  enctype="multipart/form-data" method="post">
	                <label>&nbsp;离职批量导入：</label>
	                <input class="easyui-filebox" id="quitfile" name="quitfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
	                <a href="#" class="easyui-linkbutton" id="quitfileBatchImport"  iconCls="pic_52" plain="false">导入</a>
	            </form>
	        </div>
        </sec:authorize>
         <sec:authorize ifAnyGranted="/system/user/ydImportEmp">
	         <div style="text-align:center;padding: 0px; margin: 20px 0px;" >
	            <form  id="ydFileForm"  enctype="multipart/form-data" method="post">
	                <label>&nbsp;异动批量导入：</label>
	                <input class="easyui-filebox" id="ydfile" name="ydfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
	                <a href="#" class="easyui-linkbutton" id="ydfileBatchImport"  iconCls="pic_52" plain="false">导入</a>
	            </form>
	        </div>
        </sec:authorize>
    </div>

	<!-- 分配菜单权限面板 -->
	<div id="createMunePanel" class="easyui-window" title="分配菜单权限"
		style="padding: 20px;">
		<ul id="productTree" class="easyui-tree"></ul>
		<input name="userId" type="hidden" /> <br />
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" id="productTreeSubmit">提交</a> <a href="javascript:void(0)" class="easyui-linkbutton commonCloseBut" id="productTreeColse">取消</a>
		</div>
	</div>

	<!-- 分配角色面板 -->
	<div id="createRolePanel" class="easyui-window" title="分配角色权限"
		style="padding: 20px;">
		<ul id="roleTree" class="easyui-tree"></ul>
		<input name="userId" type="hidden" /> <br />
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" id="roleTreeSubmit">提交</a> <a href="javascript:void(0)" class="easyui-linkbutton commonCloseBut" id="roleTreeColse">取消</a>
		</div>
	</div>
	<div id="orgPanel" class="easyui-window easyui-layout" title="组织机构面板"
		style="padding: 20px;" data-options="width:500,height:600">
		<div id="tips"></div>
		<div class="easyui-panel" >
			<ul id="comOrganizationTree" class="easyui-tree" data-options="region:'center',fit:true" style="height: 400px;"></ul>
		</div>
			<div style="text-align: center; padding: 5px;height: 50px">
			<a href="javascript:void(0)" class="easyui-linkbutton" id="organizationTreeSubmit">提交</a> <a href="javascript:void(0)" class="easyui-linkbutton commonCloseBut" id="organizationTreeColse">取消</a>
		</div>
	</div>

	<!--新增用户 -->
	<div id="addEmployeePanel" class="easyui-window easyui-layout"
		title="新增修改用户" style="padding: 20px;"
		data-options="width:400,height:400">
		<div>
			<form method="post" id="dataForm">
				<table style="text-align: right:;">
					<tr>
						<td>姓名：</td>
						<td><input type="text" name="name" id="name" data-options="required:true" class="easyui-textbox" /></td>
					</tr>
					<tr>
						<td>工号：</td>
						<td><input type="text" name="usercode" id="userCodes" data-options="required:true" class="easyui-textbox" /></td>
					</tr>
					<tr>
						<td>登陆密码：</td>
						<td><input type="password" name="password" id="password" data-options="required:true" class="easyui-textbox" /></td>
					</tr>
					<tr>
						<td>邮箱：</td>
						<td><input type="text" name="email" id="email" class="easyui-textbox" data-options="required:true,validType:'email'" /></td>
					</tr>
					<tr>
						<td>手机号：</td>
						<td><input type="text" name="mobile" id="mobile" class="easyui-textbox" data-options="required:true,validType:'mobile'" /></td>
					</tr>
					<tr>
						<td>员工类型：</td>
						<td>
							<select name="employeeType" id="employeeType" style="width: 150px;" class="easyui-combobox" data-options="'editable':false,panelHeight:'auto'">
								<option id="管理人员">管理人员</option>
								<option id="业务员">业务员</option>
								<option id="客服">客服</option>
								<option id="行政助理">行政助理</option>
								<option id="审核员">审核员</option>
								<option id="审批员">审批员</option>
								<option id="系统管理员">系统管理员</option>
								<option id="催收员">催收员</option>
								<option id="催收管理员">催收管理员</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>是否在职：</td>
						<td>
							<select name="inActive" id="inActive" style="width: 150px;" class="easyui-combobox" data-options="'editable':false,panelHeight:'auto'"><option id="t" value="t">是</option>
								<option id="f" value="f">否</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<a href="#" class="easyui-linkbutton" id="addorgBut" iconCls="icon-add" plain="true">组织机构</a>
							<div id="orgtips"></div>
						</td>
					</tr>
				</table>
				<input type="hidden" id="id" name="id" />
				<input type="hidden" id="Eid" name="Eid" />
				<input type="hidden" id="orgId" name="orgId" />
			 	<input type="hidden" id="path" name="path" />
			</form>

			<br />
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					id="employeeSubmit">提交</a> <a href="javascript:void(0)"
					class="easyui-linkbutton commonCloseBut" id="employeeColse">取消</a>
			</div>
		</div>
	</div>
</body>
</html>
