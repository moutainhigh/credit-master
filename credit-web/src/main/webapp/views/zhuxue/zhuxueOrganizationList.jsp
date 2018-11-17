<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>还款管理 - 罚息减免申请</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/zhuxue/zhuxueOrganization.js');
					
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
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:75px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm">
				<table cellpadding="5">
					<tr>
						<td>机构内部编号：</td>
						<td><input class="easyui-textbox" type="text" name="code" id="code"/></td>
						<td>机构类型：</td>
						<td>
						  <select class="easyui-combobox" data-options="panelHeight:'auto'" style="width:100px;" name="orgType" id="orgType">
						    <option value="">请选择</option>
                            <c:forEach var="orgType" items="${OrgTypes}">
                                <option value="${orgType.value}">${orgType.value}</option>
                            </c:forEach>
						  </select>
						</td>
						<td>机构名称：</td>
						<td><input class="easyui-textbox" type="text" name="name" id="name"/></td>
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