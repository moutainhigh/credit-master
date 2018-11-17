<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>业绩归属变更</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
</style>
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'datalist', 'form', 'switchbutton', 'tooltip',
			'validatebox', 'combogrid' ], 'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath
					+ '/resources/js/operation/performanceBelongChange.js');
			urlJs.push(global.contextPath
					+ '/resources/js/common/salesManCommon.js');
			importJSExt(urlJs, function() {
				/** 脚本加载成功回调方法 **/

			});
		});
	});
</script>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<!-- DataGrid 工具栏按钮 -->
	<div id="tb" style="padding: 3px;">
		<div style="margin-bottom: 0px">
			<a href="#" class="easyui-linkbutton" id="searchBtn"iconCls="icon-search" plain="true">查询</a>
			<a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width: 60px; float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<sec:authorize ifAnyGranted="/operation/updatePerformanceBelongInfo">
				<a href="#" class="easyui-linkbutton" id="updateBtn" iconCls="icon-save" plain="true">变更销售信息</a>
				<a href="#" class="easyui-linkbutton" id="updateRecorded" iconCls="icon-save" plain="true">变更录单渠道</a>
			</sec:authorize>

		</div>
	</div>
	<div class="easyui-panel" title="查询条件"
		style="padding: 5px; height: 120px; margin: 0px;"
		data-options="region:'north'">
		<form id="searchForm">
			<table cellpadding="5">
				<tr>
					<td>姓名：</td>
					<td><input class="easyui-textbox" id="name" name="name"
						data-options="validType:'maxLength[60,\'借款人姓名\']'"
						style="width: 250px;" /></td>
					<td>手机：</td>
					<td><input class="easyui-textbox" id="mobile" name="mobile"
						validType="mobile" style="width: 120px;" /></td>
					<td>身份证号：</td>
					<td><input class="easyui-textbox" id="idNum" name="idNum"
						validType="idCard" /></td>
				</tr>
				<tr>
					<td>销售团队：</td>
					<td><select class="easyui-combobox" id="salesTeamId"
						name="salesTeamId" style="width: 250px;"
						data-options="editable:true">
							<option value="">请选择</option>
							<c:forEach var="salesTeamInfo" items="${salesTeamInfoList}">
								<option value="${salesTeamInfo.id}">${salesTeamInfo.name}</option>
							</c:forEach>
					</select></td>
					<td>客户经理：</td>
					<td><input id="salesManId" name="salesManId"
						class="custComboGrid"
						configValue="{width:120,baseParm:{employeeType:'业务员'}}" /></td>
					<td>合同编号:</td>
					<td><input class="easyui-textbox" id="contractNum"
						name="contractNum" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;"
		data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="performanceBelongDataGrid" data-options=""></table>
	</div>

	<!-- 修改面板 -->
	<div id="updateDataPanel" class="easyui-window" title="业绩归属变更"
		style="padding: 20px;">
		<form method="post" id="dataForm">
			<input type="hidden" name="loanId" />
			<table cellpadding="5">
				<tr>
					<td>当前客户经理:</td>
					<td><input class="easyui-textbox" type="text"
						id="salesManName" name="salesManName" data-options="readonly:true"
						style="width: 250px;" /></td>
				</tr>
				<tr>
					<td>当前所属团队:</td>
					<td><input class="easyui-textbox" type="text"
						id="salesTeamName" name="salesTeamName"
						data-options="readonly:true" style="width: 250px;"></input></td>
				</tr>
				<tr>
					<td>变更后销售团队</td>
					<td id="teamTd"><input id="updateSalesTeam"
						name="updateSalesTeam" style="width: 250px;" /></td>
				</tr>
				<tr>
					<td>变更后客户经理</td>
					<td><input id="updateSalesMan" name="updateSalesMan"
						data-options="required:true" class="custComboGrid"
						configValue="{width:250,baseParm:{employeeType:'业务员',inActive:'t'}}" />
					</td>
				</tr>
			</table>
		</form>
		<div style="text-align: center; padding-top: 10px;">
			<sec:authorize ifAnyGranted="/operation/updatePerformanceBelongInfo">
				<a href="javascript:void(0)" id="submitBtn"
					class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
			</sec:authorize>
			<a href="javascript:void(0)" id="closeBtn" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>

	<!-- 变更录单渠道面板 -->
	<div id="updateApplyDataPanel" class="easyui-window" title="录单渠道变更"
		style="padding: 20px;">
		<form method="post" id="applydataForm">
			<input type="hidden" name="loanId" />
			<table cellpadding="5">
				<tr>
					<td>当前录单渠道:</td>
					<td><input class="easyui-textbox" type="text" id="applyInputFlag" name="applyInputFlag" data-options="readonly:true" style="width: 170px;" /></td>
				</tr>

				<tr>
					<td>变更后录单渠道</td>
					<td id="ApplyTd"> 
					<input id="updateapplyinputflag" name="updateapplyinputflag" style="width: 170px;" /></td>
				</tr>
			</table>
		</form>
		<div style="text-align: center; padding-top: 10px;">
			<sec:authorize ifAnyGranted="/operation/updatePerformanceBelongInfo">
				<a href="javascript:void(0)" id="AppsubmitBtn"
					class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
			</sec:authorize>
			<a href="javascript:void(0)" id="ApplycloseBtn" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
	
</body>
</html>
