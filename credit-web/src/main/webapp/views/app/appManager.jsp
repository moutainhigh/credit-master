<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>App登录配置管理</title>
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
	importPluginsExt(['panel','combobox','window','layout','datagrid','pagination'
	                  ,'form','tooltip','validatebox','combogrid'],'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
			urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
			urlJs.push(global.contextPath + '/resources/js/app/appManager.js');
			urlJs.push(global.contextPath + '/resources/js/app/appDept.js');
			//urlJs.push(global.contextPath + '/resources/js/app/appIdNum.js');

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

	<!-- 显示APP个人管理配置 -->
	<div class="easyui-panel" title="" style="padding: 5px; margin: 0px;" data-options="region:'center'">
		<div class="easyui-layout" data-options="fit:true">
			<!-- 营业部配置 -->
			<div data-options="region:'east',iconCls:'icon-reload',title:'营业部配置',split:false,collapsible:false"
				style="width: 50%;">
				<div class="easyui-panel" title="查询条件" style="padding: 10px; height: 100px; margin: 0px; text-align: center;"data-options="region:'north'">
					<form id="appOrgSearchForm">
						<table  cellpadding="5">
							<tr>
								<td style="width: 72px;text-align: left;">营业部:</td>
								<td style="width: 150px;text-align: left;"><select class="easyui-combobox" id="orgId" name="orgId" style="width: 180px;"
							data-options="validType:'comboboxValidator[\'#orgId\',\'管理营业部\']'">
								<option value="">请选择</option>
								<c:forEach var="salesDeptInfo" items="${salesDeptInfoList}">
									<option value="${salesDeptInfo.id}">${salesDeptInfo.name}</option>
								</c:forEach>
						</select></td>
							</tr>
						</table>
					</form>
				</div>
				<div class="easyui-panel" style="padding: 0px; margin: 0px;height:87%;" data-options="region:'center'">
		<!-- 表格标签 -->
		
					<table class="easyui-datagrid" id="appOrgDataGrid" style="" data-options="">
					</table>
				</div>
			</div>
			<div id="deptDataGirdTB" style="padding: 3px;">
					  <div style="margin-bottom: 0px">
						<a href="#" class="easyui-linkbutton" id="orgSearchBut" iconCls="icon-search" plain="true">查询</a>	
						<a href="javascript:void(0)" id="clearConditionOrg" class="easyui-linkbutton" 
						data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>
						<a href="#" class="easyui-linkbutton" id="orgAdd" iconCls="pic_46" style="" plain="true">新增</a>
						<a href="#" class="easyui-linkbutton" id="orgModify" iconCls="pic_46" style="" plain="true">修改</a>
					  </div>
					</div>
			<!-- 员工配置 -->
			<div data-options="region:'west',iconCls:'icon-reload',title:'员工配置 ',split:false,collapsible:false"
				style="width: 50%;">
				<div class="easyui-panel" title="查询条件" style="padding: 10px; height: 100px; margin: 0px; text-align: center;"data-options="region:'north'">
					<form id="appEmployeeSearchForm">
						<table  cellpadding="5">
							<tr>
								<td style="width: 72px;text-align: left;">姓名:</td>
								<td style="width: 150px;text-align: left;"><input class="easyui-textbox" type="text" value="" name="name" id="name"   style="width: 150px;"></input></td>
								<td style="width: 72px;text-align: left;">员工工号:</td>
								<td style="width: 150px;text-align: left;"><input class="easyui-textbox" type="text" value="" name="userCode" id="userCode"   style="width: 150px;"></input></td>
								<!-- <td style="width: 72px;text-align: left;">营业部:</td>
								<td style="width: 150px;text-align: left;"><select class="easyui-combobox" id="orgId" name="orgId" style="width: 180px;"
							data-options="validType:'comboboxValidator[\'#deptId\',\'管理营业部\']'">
								<option value="">请选择</option>
								<c:forEach var="salesDeptInfo" items="${salesDeptInfoList}">
									<option value="${salesDeptInfo.id}">${salesDeptInfo.name}</option>
								</c:forEach>
						</select></td> -->
							</tr>
						</table>
					</form>
				</div>
				<div class="easyui-panel" style="padding: 0px; margin: 0px;height:87%;" data-options="region:'center'">
					<!-- 表格标签 -->
					<table class="easyui-datagrid" id="employeeDataGrid" style="" data-options=""></table>
				</div>
			</div>
			<div id="employeeTb" style="padding: 3px;">
			  <div style="margin-bottom: 0px">
				<a href="#" class="easyui-linkbutton" id="employeeSearchBut" iconCls="icon-search" plain="true">查询</a>	
				<a href="javascript:void(0)" id="clearConditionEmployee" class="easyui-linkbutton" 
				data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>
				<a href="#" class="easyui-linkbutton" id="employeeAdd" iconCls="pic_46" style="" plain="true">新增</a>
				<a href="#" class="easyui-linkbutton" id="employeeModify" iconCls="pic_46" style="" plain="true">修改</a>
			  </div>
			</div>
		</div>
	</div>

	<!-- 新增身份证信息窗口 -->
	<div id="appEmployeeConfigWin" class="easyui-window " title="新增App登录员工" data-options="">
		<div class="editContentPanel" style="padding: 5px;">
			<div id="appEmployeeConfigTips" style="padding: 5px; font-size: 14px;"></div>
			<form id="appEmployeeConfigForm">
				<input type="hidden" name="id"></input> 
				<input type="hidden" name="saveOrUpdate" value="12132"></input>
				<table cellpadding="2" cellspacing="2" border="0">
					<tr>
						<td width="100px" class="">员工工号：</td>
						<td width="190px"><input class="easyui-textbox" id="userCodeConfig" name="userCode" style="width: 150px;"
							data-options="required:true" ></input ></td>
					</tr>
					<tr>
						<td width="" class="">是否开启：</td>
						<td width=""><select class="easyui-combobox" id="employeeStateConfig" name="state" style="width: 150px;"
							data-options="required:true,editable:false,panelHeight:'auto'">
								<option value="0">开启旧</option>
								<option value="2">开启新</option>
								<option value="1">关闭</option>
						</select></td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding-top: 15px;">
				<a href="javascript:void(0)" id="submitAppEmployeeReq" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
					style="margin-right: 15px;">提交</a> <a href="javascript:void(0)" id="appEmployeeCloseWinBut" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
		</div>
	</div> 
	<!-- 新增身份证信息窗口 -->


	<!-- 新增管理营业部信息窗口  -->
	<div id="appOrgConfigWin" class="easyui-window " title="新增/修改管理营业部信息" data-options="">
		<div class="editContentPanel" style="padding: 5px;">
			<div id="deptTips" style="padding: 5px; font-size: 14px;"></div>
			<form id="appOrgConfigForm">
				<input type="hidden" name="id"></input> 
				<input type="hidden" name="saveOrUpdate"></input>
				<table cellpadding="2" cellspacing="2" border="0">
					<tr>
						<td width="80" class="">管理营业部：</td>
						<td width="190px"><select class="easyui-combobox" id="orgIdConfig" name="orgId" style="width: 180px;"
							data-options=" required:true, validType:'comboboxValidator[\'#deptId\',\'管理营业部\']'">
								<c:forEach var="salesDeptInfo" items="${salesDeptInfoList}">
									<option value="${salesDeptInfo.id}">${salesDeptInfo.name}</option>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td width="" class="">是否有效：</td>
						<td width=""><select class="easyui-combobox" id="orgStateConfig" name="state" style="width: 180px;"
							data-options="required:true, editable:false,panelHeight:'auto'">
								<option value="0">开启旧</option>
								<option value="2">开启新</option>
								<option value="1">关闭</option>
						</select></td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding-top: 15px;">
				<a href="javascript:void(0)" id="submitAreaDeptReq" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
					style="margin-right: 15px;">提交</a> <a href="javascript:void(0)" id="areaDeptCloseWinBut" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
		</div>
	</div>
	<!-- 新增管理营业部信息窗口 -->


</body>
</html>




