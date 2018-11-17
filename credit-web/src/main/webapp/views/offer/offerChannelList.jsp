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
					urlJs.push(global.contextPath + '/resources/js/offer/offerChannel.js');
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
					<td>债权去向:</td>
					<td>
						<select class="easyui-combobox" id='loanBelong' name='loanBelong' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<option value='' >全部</option>
							<c:forEach var="loanBelongs" items="${loanBelongs}">
                                 <option value="${loanBelongs.codeTitle}">${loanBelongs.codeTitle}</option>
                            </c:forEach>
						</select>
					</td>
					<td>划扣通道:</td>
					<td>
						<select class="easyui-combobox" id='paySysNo' name='paySysNo' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<option value='' >全部</option>
							<c:forEach var = "paySysNos" items = "${paySysNos}">
								<option value="${paySysNos.code}">${paySysNos.value}</option>
							</c:forEach>
						</select>
					</td>
					<td>是否有效:</td>
					<td>
						<select class="easyui-combobox" id='state' name='state' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<option value='' >全部</option>
							<option value='1' >有效</option>
							<option value='0' >无效</option>
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
				<sec:authorize ifAnyGranted="/system/user/insertComEmployee">
					<a href="#" class="easyui-linkbutton" id="addBut" iconCls="icon-add" plain="true">新增</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/user/updateEmployee">
					<a href="#" class="easyui-linkbutton" id="updateBut" iconCls="icon-save" plain="true">修改</a>
				</sec:authorize>
			</div>
		</div>
	</div>


	<!--新增划扣通道配置 -->
	<div id="addOfferChannelPanel" class="easyui-window easyui-layout"
		title="新增修改划扣通道配置" style="padding: 20px;"
		data-options="width:400,height:300">
		<div>
			<form method="post" id="dataForm">
				<table style="text-align: right:;">
					<tr>
						<td>债权去向：</td>
						<td>
							<select class="easyui-combobox" id='loanBelongAdd' name='loanBelong' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
								<c:forEach var="loanBelongs" items="${loanBelongs}">
                                	 <option value="${loanBelongs.codeTitle}">${loanBelongs.codeTitle}</option>
                           	 	</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>划扣通道：</td>
						<td>
						<select class="easyui-combobox" id='paySysNoAdd' name='paySysNo' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
							<c:forEach var = "paySysNos" items = "${paySysNos}">
								<option value="${paySysNos.code}">${paySysNos.value}</option>
							</c:forEach>
						</select>
						</td>
					</tr>
					<tr>
						<td>是否有效：</td>
						<td>
							<select class="easyui-combobox" id='stateAdd' name='state' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
								<option value='1'  >有效</option>
								<option value='0' >无效</option>
							</select>
						</td>
					</tr>
				</table>
				<input type="hidden" id="addtype" name="type" value="1" />
			</form>

			<br />
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					id="offerChannelSubmit">提交</a> <a href="javascript:void(0)"
					class="easyui-linkbutton commonCloseBut" id="employeeColse">取消</a>
			</div>
		</div>
	</div>
	
	
	<!--修改划扣通道配置 -->
	<div id="updateOfferChannelPanel" class="easyui-window easyui-layout"
		title="修改划扣通道配置" style="padding: 20px;"
		data-options="width:400,height:200">
		<div>
			<form method="post" id="updateForm">
					<tr>
						<td>是否有效：</td>
						<td>
							<select class="easyui-combobox" id='stateUpdate' name='state' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
								<option value='1'  >有效</option>
								<option value='0' >无效</option>
							</select>
						</td>
					</tr>
				</table>
				<input id ="offerChannelId" name = "id" type="hidden" />
				<input type="hidden" id="type" name="type" value="2" />
			</form>

			<br />
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					id="updateofferChannelSubmit">提交</a> <a href="javascript:void(0)"
					class="easyui-linkbutton commonCloseBut" id="updateofferChannelClose">取消</a>
			</div>
		</div>
	</div>
</body>
</html>
