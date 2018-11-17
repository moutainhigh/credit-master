<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>还款管理 - 减免申请</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
					urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
					urlJs.push(global.contextPath + '/resources/js/specialRepayment/reliefPenaltyList.js');
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
				<sec:authorize ifAnyGranted="/specialRepayment/changeReliefPenaltyState">
					<a href="#" class="easyui-linkbutton" id="changeStateBut"  iconCls="pic_46"  style="" plain="true">减免申请/取消</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/specialRepayment/importReliefPenaltyStateFile">
					<a href="#" class="easyui-linkbutton" id="importBut"  iconCls="pic_42" style="" plain="true">导入</a>
				</sec:authorize>
			</div>
		</div>
		<!-- DataGrid 工具栏按钮 -->
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:108px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm">
				<table cellpadding="5">
					<tr>
						<td>姓名：</td>
						<td><input class="easyui-textbox" type="text" name="name" ></input></td>
						<td>手机：</td>
						<td><input class="easyui-textbox" type="text" value="" name="mphone" validType="mobile"></input></td>
						<td>身份证号：</td>
						<td><input class="easyui-textbox" type="text" value="" name="idnum" validType="idCard"  ></input></td>
						<td>客户经理：</td>
						<td>
							<input id="salesMan" name="salesMan"  class="custComboGrid" configValue="{width:120,baseParm:{employeeType:'业务员',inActive:'t'}}"/>
						</td>
					</tr>
					<tr>
					    <td>合同编号:</td>
				        <td><input class="easyui-textbox" type="text"
						name="contractNum" id="contractNum"></input></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="dataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		<div id="changeSpecialStateWin" class="easyui-window " title="减免申请" 
						data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="">
			<div class="editContentPanel" style="padding:10px;">
			<div style="color:red;margin-bottom: 10px;font-size: 12px;"></div>
			<form id="changeStateForm">
				<input type="hidden" name="id" />
				<input type="hidden" name="currDate" id="currDate" />
				<table>
					<tr>
						<td style="width:100px">减免申请：</td>
						<td><input class="easyui-switchbutton" style="width:100px" id="reliefPenaltySwitchBut" name="stateSwitchBut" data-options="onText:'申请',offText:'关闭'"></td>
					</tr>
					<tr id="moneyDateDiv">
						<td style="color:red;text-align: right;">减免金额：</td>
						<td>
							<input class="easyui-numberbox"  style="width:100px;" name="money" precision="2" value="0.01"  />
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding-top:10px;">
				<a href="javascript:void(0)" id="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
				<a href="javascript:void(0)" id="closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
			</div>
			</div>
		</div>
		
		<!-- Excel 导入 -->
		<div id="importExcelWin" class="easyui-window editContentPanel" title="Excel导入" data-options="closed : true,collapsible : false,minimizable : false,
											maximizable : false,modal : true,resizable : false" style="padding:0px;">
			<div style="text-align:center;padding:10px;" >
				<h3>减免申请批量导入</h3>
				<form  id="fileForm"  enctype="multipart/form-data" method="post">
					<input class="easyui-filebox" name="uploadFile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px">
					<a href="javascript:void(0)" id="importExcelBut" class="easyui-linkbutton" data-options="iconCls:'pic_52'" >导入</a>
				</form>
			</div>
			<br/>
		</div>
		<!-- Excel 导入 -->
	</body>
</html>