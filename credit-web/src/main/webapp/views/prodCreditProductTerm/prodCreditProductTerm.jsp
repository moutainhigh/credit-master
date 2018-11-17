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
<title>产品期限配置</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
</style>

<script type="text/javascript">
	importPluginsExt(['panel','combobox','window','layout','datagrid','pagination'
                  ,'form','tooltip','validatebox'],'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/js/prodCreditProductTerm/prodCreditProductTerm.js');
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
			<table cellpadding="5">
				<tr>
					<td>产品类型:</td>
					<td><input class="easyui-combobox" id="productId"
						name="productId" style="width: 100px;"
						data-options="valueField:'id',textField:'loanType','editable':false,panelHeight:'auto'">
						
					</input></td>
					<td>可选期限:</td>
					<td><select name="term" id="term" style="width: 80px;" class="easyui-combobox" data-options="'editable':false,panelHeight:'auto'">
							<option id="0" value="0">全部</option>
							<option id="12">12</option>
							<option id="18">18</option>
							<option id="24">24</option>
							<option id="36">36</option>
							<option id="48">48</option>
					</select></td>
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
				<sec:authorize ifAnyGranted="/system/ProdCreditProductTerm/saveOrUpdateProdCreditProductTerm">
					<a href="#" class="easyui-linkbutton" id="addBut"
						iconCls="icon-add" plain="true">新增</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/ProdCreditProductTerm/saveOrUpdateProdCreditProductTerm">
					<a href="#" class="easyui-linkbutton" id="updateBut"
						iconCls="icon-save" plain="true">修改</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/system/ProdCreditProductTerm/deleteProdCreditProductTermById">
					<a href="#" class="easyui-linkbutton" id="delBut"
						iconCls="icon-remove" plain="true">删除</a>
				</sec:authorize>
				<a href="#" class="easyui-linkbutton" id="searchBut"
					iconCls="icon-search" plain="true">查询</a>
			</div>
		</div>
	</div>


	<!-- 新增面板 -->
	<div id="addDataPanel" class="easyui-window" title="新增/修改消息"
		style="padding: 20px;">
		<form method="post" id="dataForm">
			<input type="hidden" name="id" />
			<table cellpadding="10">
				<tr>
					<td>产品类型:</td>
					<td><input class="easyui-combobox" id="p"
						name="productId" style="width: 100px;"
						data-options="valueField:'id',textField:'loanType','editable':false,panelHeight:'auto'">
						
					</input></td>
				</tr>
				<tr>
					<td>借款期限:</td>
					<td><select name="term" id="t" class="easyui-combobox" style="width: 80px;"  data-options="'editable':false,panelHeight:'auto'">
							<option id="12">12</option>
							<option id="18">18</option>
							<option id="24">24</option>
							<option id="36">36</option>
							<option id="48">48</option>
					</select></td>
				</tr>
				<tr>
					<td>审批金额起(单位:元) :</td>
					<td><input type="text" name="lowerLimit" id="lowerLimit" 
						 class="easyui-numberbox" data-options="required:true"></input></td>
				</tr>

				<tr>
					<td>审批金额止(单位:元) :</td>
					<td><input type="text" name="upperLimit" id="upperLimit"
						 class="easyui-numberbox" data-options="required:true"></input></td>
				</tr>

			</table>
		</form>
		<div style="text-align: center; padding-top: 10px;">
			<a href="javascript:void(0)" name="submitBut"
				class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>  <a
				href="javascript:void(0)" id="closeBut" name="closeBut" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</div>
</body>
</html>
