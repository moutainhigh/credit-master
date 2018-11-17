<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>App公告管理</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
.tree-node {
	height: 21px;
	white-space: nowrap;
	cursor: pointer;
	padding-top: 1px;
}

.tree-icon {
	margin-left: 2px;
	margin-right: 2px;
}

div.vertical {
	display: table;
	border: 0px solid #FF0099;
	width: 100%;
	height: 100%;
	_position: relative;
	overflow: hidden;
	text-align: center;
}

div.verticalInner {
	vertical-align: middle;
	display: table-cell;
	_position: absolute;
	_top: 50%;
}

#picContainer img {
	MAX-WIDTH: 95% !important;
	HEIGHT: auto !important;
}
</style>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/sysIcon.css" />
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid', 'pagination', 'form', 'tooltip',
			'validatebox', 'combogrid' ], 'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
			urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
			urlJs.push(global.contextPath + '/resources/js/app/appNoticeManager.js');

			importJSExt(urlJs, function() {
				/** 脚本加载成功回调方法 **/

			});
		})
	});
</script>
<style type="text/css">
</style>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />

	<!-- DataGrid 工具栏按钮 -->
	<div id="tb" style="padding: 3px;">
		<div style="margin-bottom: 0px">
			<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true" style="">查询</a> <a
				href="javascript:void(0)" id="clearBut" class="easyui-linkbutton"
				data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width: 60px; float: none;">重置</a>
			&nbsp;&nbsp;&nbsp;&nbsp; <a href="#" class="easyui-linkbutton" id="saveBut" iconCls="pic_46" style="" plain="true">新增</a>
			<a href="#" class="easyui-linkbutton" id="updateBut" iconCls="pic_46" style="" plain="true">修改</a>
		</div>
	</div>
	<!-- DataGrid 工具栏按钮 -->
	<!-- 
	<div class="easyui-panel" title="查询条件" style="padding:5px;height:109px;margin: 0px;" data-options="region:'north'">
		<form id="searchForm">
			<table cellpadding="5">
				<tr>
					<td>姓名：</td>
					<td><input class="easyui-textbox" type="text" name="name" ></input></td>
					<td>手机：</td>
					<td><input class="easyui-textbox" type="text" name="mphone"  validType="mobile"></input></td>
					<td>身份证号：</td>
					<td><input class="easyui-textbox" type="text" value="" name="idnum" validType="idCard" ></input></td>
					<td>客户经理：</td>
					<td>
						<input id="salesMan" name="salesMan"  class="custComboGrid" configValue="{width:120,baseParm:{employeeType:'业务员',inActive:'t'}}"/> 
					</td>
				</tr>
				<tr>
				  <td>合同编号：</td>
				  <td><input class="easyui-textbox" type="text" name="contractNum" ></input></td>
				</tr>
			</table>
		</form>
	</div>
	 -->
	<div class="easyui-panel" style="padding: 0px; margin: 0px;" data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="dataGrid" class="easyui-datagrid" data-options=""></table>
	</div>

	<div id="noticeWin" class="easyui-window " title="公告维护"
		data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'"
		style="">
		<div class="editContentPanel" style="padding: 10px;">
			<form id="noticeForm">
				<input type="hidden" name="id" />
				<div style="text-align: left;">
					<table style="margin: auto">
						<tr>
							<td>标题：</td>
							<td><input class="easyui-textbox" style="width: 270px" name="title" data-options="required:true"
								validType="maxLength[30,'']"></td>
						</tr>
						<tr>
							<td>通知类型：</td>
							<td>
								<select class="easyui-combobox" name="noticeType" style="width: 270px;" data-options="panelHeight:'auto',required:true"/>
									<c:forEach items="${noticeTypes}" var="notTypeMap">
										<c:forEach items="${notTypeMap}" var="notType">
											<option value="${notType.value}">${notType.value} </option>
										</c:forEach>

									</c:forEach>
							</td>
						</tr>
						<tr>
							<td>内容：</td>
							<td><input class="easyui-textbox" style="height: 80px; width: 270px;" name="content"
								data-options="multiline:true,required:true" validType="maxLength[300,'']"></td>
						</tr>
						<tr>
							<td>是否有效：</td>
							<td><select class="easyui-combobox" name="isValid" style="width: 270px;" data-options="panelHeight:'auto'" />
								<option value="1">有效</option>
								<option value="0">无效</option></td>
						</tr>
					</table>
				</div>
			</form>
			<div style="text-align: center; padding-top: 10px;">
				<a href="javascript:void(0)" id="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a> <a
					href="javascript:void(0)" id="closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
		</div>
	</div>
</body>
</html>




