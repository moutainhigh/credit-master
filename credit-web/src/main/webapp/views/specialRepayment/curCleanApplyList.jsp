<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>还款管理 - 申请提前扣款</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/specialRepayment/curCleanApplyList.js');
					urlJs.push(global.contextPath + '/resources/js/common/salesManCommon.js');
					
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
		<div id="tb" style="padding:3px;">
			<div style="margin-bottom:0px">
				<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true" style=";">查询</a>
				<a href="javascript:void(0)" id="clearBut" class="easyui-linkbutton" 
							data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;float: none;">重置</a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<sec:authorize  ifAnyGranted="/specialRepayment/changeCurCleanApplyState">
					<a href="#" class="easyui-linkbutton" id="changeStateBut"  iconCls="pic_46"  style="" plain="true">变更申请状态</a>
				</sec:authorize>
			</div>
		</div>
		<!-- DataGrid 工具栏按钮 -->
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
		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="dataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		<div id="changeSpecialStateWin" class="easyui-window " title="提前扣款（申请/取消）" 
						data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="">
			<div class="editContentPanel" style="padding:10px;">
				<div style="color:red;margin-bottom: 10px;font-size: 12px;">提示：申请提前扣款后，提前一次性结清申请自动取消！</div>
				<form id="changeStateForm">
					<input type="hidden" name="id" />
					<input type="hidden" name="currDate" id="currDate" />
					<!-- 当期还款日 -->
					<input type="hidden" name="curReturnDate" id="curReturnDate" />
					<div style="text-align: center; " >
						<table style="margin:auto">
							<tr>
								<td>提前扣款：</td>
								<td><input class="easyui-switchbutton" style="width:100px" id="curCleanApplyState" name="curCleanApplyState" data-options="onText:'申请',offText:'取消'"></td>
							</tr>
							<tr class="noOneTimeSetDateDiv">
								<td style="color:red;text-align: right;">自动生效时间：</td>
								<td>
									<input class="easyui-datebox" name="requestDate" id="requestDate"  style="width:100px"
															data-options="formatter:$.dates.formatter,parser:$.dates.parser,'editable':false"></input>
								</td>
							</tr>
						</table>
					</div>
				</form>
				<div style="text-align:center;padding-top:10px;">
					<a href="javascript:void(0)" id="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
					<a href="javascript:void(0)" id="closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
				</div>
			</div>
		</div>
	</body>
</html>