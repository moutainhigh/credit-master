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
<title>代付通道配置</title>
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
					urlJs.push(global.contextPath + '/resources/js/offer/payChannel.js');
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
					<td>合同来源:</td>
					<td>
						<select class="easyui-combobox" id='fundsSources' name='fundsSources' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<option value='' >请选择</option>
							<c:forEach var="fundsSource" items="${fundsSources}">
                                 <option value="${fundsSource.code}">${fundsSource.value}</option>
                            </c:forEach>
						</select>
					</td>
					<td>代付通道:</td>
					<td>
						<select class="easyui-combobox" id='paySysNo' name='paySysNo' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<option value='' >请选择</option>
							<c:forEach var = "paySysNos" items = "${paySysNos}">
								<option value="${paySysNos}">${paySysNos}</option>
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
		<table id="payChannelDataGrid" class="easyui-datagrid" data-options=""></table>
		<div id="tb" style="padding: 3px;">
			<div style="margin-bottom: 0px">
			<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
			<a href="#" class="easyui-linkbutton" id="clearCondition" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" class="easyui-linkbutton" id="addBut" iconCls="icon-add" plain="true">新增</a>
			<a href="#" class="easyui-linkbutton" id="updateBut" iconCls="icon-save" plain="true">修改</a>
			</div>
		</div>
	</div>
	
	
	<!--新增划扣通道配置 -->
	<div id="addPayChannelPanel" class="easyui-window easyui-layout"
		title="新增修改代付通道配置" style="padding: 20px;"data-options="width:330,height:230">
		<div>
			<form method="post" id="dataForm">
				<table style="text-align: right:;">
					<tr>
						<td>合同来源：</td>
						<td>
							<select class="easyui-combobox" id='fundsSourcesEdit' name='fundsSourcesEdit' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<c:forEach var="fundsSource" items="${fundsSources}">
                                 <option value="${fundsSource.code}">${fundsSource.value}</option>
                            </c:forEach>
						</select>
						</td>
					</tr>
					<tr>
						<td>代付通道：</td>
						<td>
						<select class="easyui-combobox" id='paySysNoEdit' name='paySysNoEdit' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<c:forEach var = "paySysNos" items = "${paySysNos}">
								<option value="${paySysNos}">${paySysNos}</option>
							</c:forEach>
						</select>
						</td>
					</tr>
				</table>
				<input id ="payChannelId" name = "payChannelId" type="hidden" />
			</form>
			<br />
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" id="payChannelSubmitAdd">提交</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton" id="payChannelColseAdd">取消</a>
			</div>
		</div>
	</div>
	
</body>
</html>
