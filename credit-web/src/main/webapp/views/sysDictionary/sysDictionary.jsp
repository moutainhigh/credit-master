<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>数据字典</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
</style>
<script type="text/javascript">
	importPluginsExt(['panel','combobox','window','layout','datagrid','pagination'
                  ,'form','tooltip','validatebox'],'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/js/sysDictionary/sysDictionary.js');
			
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
			<input type="hidden" id="pid" name="pid" />
			<table cellpadding="5">
				<tr>
<!-- 					<td>代码:</td> -->
<!-- 					<td><input class="easyui-textbox" type="text" name="codeName"></input></td> -->
					<td>标题:</td>
					<td><input class="easyui-textbox" type="text" name="codeTitle"></input></td>
					<td>类型:</td>
					<td><input class="easyui-textbox" type="text" name="codeType"></input></td>
					<td>类型名称:</td>
					<td><input class="easyui-textbox" type="text" name="codeTypeTitle"></input></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;"
		data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="sysDataGrid" class="easyui-datagrid" data-options=""></table>
		<div id="tb" style="padding: 3px;">
			<div style="margin-bottom: 0px">
				<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
				<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<sec:authorize ifAnyGranted="/system/SysDictionary/saveOrUpdateData">
					<a href="#" class="easyui-linkbutton" id="addBut" iconCls="icon-add" plain="true">新增</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/SysDictionary/saveOrUpdateData">
					<a href="#" class="easyui-linkbutton" id="updateBut" iconCls="icon-save" plain="true">修改</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/SysDictionary/deleteData">
					<a href="#" class="easyui-linkbutton" id="delBut" iconCls="icon-remove" plain="true">删除</a>
				</sec:authorize>
			</div>
		</div>
	</div>
	<!-- 新增面板 -->
	<div id="addDataPanel" class="easyui-window" title="新增,修改消息"
		style="padding: 20px;">
		<form method="post" id="dataForm">
			<input type="hidden" name="id" />
			<table>
			   <tr>
					<td>代码:</td>
					<td><input type="text" id="codeType" name="codeName" class="easyui-textbox" data-options="required:true,validType:'maxLength[32,\'名称\']'"> </input></td>
				</tr>
				<tr>
					<td>标题:</td>
					<td><input type="text" id="codeType" name="codeTitle" class="easyui-textbox" data-options="required:true,validType:'maxLength[32,\'名称\']'"> </input></td>
				</tr>
				<tr>
					<td>类型:</td>
					<td><input type="text" id="codeType" name="codeType" class="easyui-textbox" data-options="required:true,validType:'maxLength[32,\'名称\']'"> </input></td>
				</tr>

				<tr>
					<td>类型名称:</td>
					<td><input type="text" name="codeTypeTitle" id="codeTypeTitle" class="easyui-textbox" data-options="required:true,validType:'maxLength[128,\'名称\']'"></input></td>
				</tr>
				<tr>
					<td>排序:</td>
					<td><input id="seqence" name="seqence" class="easyui-textbox"></input></td>
				</tr>
                <tr>
					<td>代码值:</td>
					<td><input id="codeValue" name="codeValue" class="easyui-textbox"></input></td>
				</tr>
			</table>
		</form>
		<div style="text-align: center; padding-top: 10px;">
			<a href="javascript:void(0)" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
			<a href="javascript:void(0)" id="closeBut" name="closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
</body>
</html>
