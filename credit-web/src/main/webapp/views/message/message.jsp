<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<%@ include file="/views/common/headIncludeFile.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/message/message.js');
					
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
		<div id="employeeToolbar" style="height: 32px" >
			<form id="employeeForm">
				<table cellpadding="1" style="font-size: 12px;">
					<tr>
						<td style="width:40px;" >姓名：</td>
						<td style="width:110px" ><input class="easyui-textbox" type="text"  style="width:100px" name="name" ></input></td>
						<td style="width:40px;">工号：</td>
						<td style="width:110px" ><input class="easyui-textbox" type="text" style="width:100px" name="userCode" ></input></td>
						<td >
							<a href="javascript:void(0)" class="easyui-linkbutton employee query"  data-options="iconCls:'icon-search',plain:true,formId:'#employeeForm',dataGridId:'#receiver'"  >查询</a>
							<a href="javascript:void(0)" class="easyui-linkbutton employee clear"  data-options="iconCls:'icon-clear',plain:true,dataGridId:'#receiver'" >清空已选中值</a>
						</td>
					</tr>
				</table>
			</form>
		</div>				
			<div id="tb" style="padding:3px;">
			<div style="margin-bottom:0px">
			<sec:authorize ifAnyGranted="/system/baseMessage/insert">
				<a href="#" class="easyui-linkbutton" id="addBut"  iconCls="icon-add"   plain="true">新增消息</a>
			</sec:authorize>
			<sec:authorize ifAnyGranted="/system/baseMessage/updatebaseMessageByState">
				<a href="#" class="easyui-linkbutton" id="updateBut"  iconCls="icon-save" plain="true">标记为未读</a>
			</sec:authorize>
			<sec:authorize ifAnyGranted="/system/baseMessage/delete">
				<a href="#" class="easyui-linkbutton" id="delBut"  iconCls="icon-remove" plain="true">删除</a>
			</sec:authorize>
			<sec:authorize ifAnyGranted="/system/baseMessage/listOneMessage">
				<a href="#" class="easyui-linkbutton" id="viewBut"  iconCls="icon-search" plain="true">查看</a>
			</sec:authorize>
			</div>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="messageDataGrid" class="easyui-datagrid" data-options="" ></table>
		</div>
		<!-- 新增面板 -->
		<div id="addDataPanel" class="easyui-window" title="新增消息"  style="padding:20px; left:500px">
			<form method="post"  id="dataForm">
				<input type="hidden" name="id" />
				<table cellpadding="10">
					<tr>
						<td>收件人:</td>
						<td>
							<select id="receiver" name="receiver" class="employee easyui-combogrid" data-options="toolbar:'#employeeToolbar',required:true" style="width: 200px;" />
	<!-- 						<input class="easyui-combobox" id="receiver" name="receiver"      data-options="valueField:'id',textField:'name',required:true">	 -->					
						</td>
					</tr>
					<tr>
						<td>标题:</td>
						<td><input class="easyui-textbox" type="text" name="title" data-options="required:true" style="width: 200px;"></input></td>
					</tr>
					<tr>
						<td>消息类型:</td>
						<td>
							<select class="easyui-combobox" name="type" style="width:200px;" data-options="required:true,editable : false" style="width: 200px;">
								<option value="0" selected>系统通知</option>
								<option value="1">信件</option>	
								<option value="2">回盘提醒</option>
							</select>
						</td>
					</tr>
						<tr>
						<td>内容:</td>
						<td>
						<input class="easyui-textbox" name="content" data-options="multiline:true" style="height:200px; width:400px"></input>
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding-top:10px;">
			<sec:authorize ifAnyGranted="/system/baseMessage/insert">
				<a href="javascript:void(0)" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
			</sec:authorize>
				<a href="javascript:void(0)" name="closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
			</div>
		</div>
		
		
		<!-- 查看面板 -->
		<div id="viewDataPanel" class="easyui-window" title="查看消息"  style="padding:20px;">
			<form method="post"  id="dataFormView">
				<input type="hidden" name="id" id="viewId"/>
				<table cellpadding="10">
					<tr>
						<td>发件人:</td>
						<td><input class="easyui-textbox" type="text" name="senderName" data-options="required:true"  readonly="readonly"></input></td>				
						</td>
					</tr>
					<tr>
						<td>标题:</td>
						<td><input class="easyui-textbox" type="text" name="title" data-options="required:true" readonly="readonly"></input></td>
					</tr>
					<tr>
						<td>发送时间:</td>
						<td><input class="easyui-textbox" type="text" name="sendTimeStr" data-options="required:true" readonly="readonly" ></input></td>
					</tr>
					
						<tr>
						<td>内容:</td>
						<td>
						<input class="easyui-textbox" name="content" data-options="multiline:true" style="height:200px; width:400px" readonly="readonly"></input>
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding-top:10px;">
			<sec:authorize ifAnyGranted="/system/baseMessage/delete">
				<a href="#" class="easyui-linkbutton" id="delButs"  iconCls="icon-remove"   plain="true"> 删除</a>
			</sec:authorize>
			<sec:authorize ifAnyGranted="/system/baseMessage/updatebaseMessageByState">
				<a href="#" class="easyui-linkbutton" id="updateButs"  iconCls="icon-save" plain="true">标记未读</a>
			</sec:authorize>
			</div>
		</div>
	</body>
</html>
