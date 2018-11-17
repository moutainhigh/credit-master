<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>客户档案管理列表</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/loan/loanFilesInfoListPage.js');
					urlJs.push(global.contextPath + '/resources/js/common/salesManCommon.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				})
			});
		</script>
	</head>
	<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<c:if test="${resCode == '000000'}">
		<div data-options="region:'north',split:false,border:true,title:'查询条件',collapsible:false" style="height: 125px;">
			<div style="width: 100%; height: 75px; padding-bottom: 1px; margin:auto;">
				<form id="conditionForm" style="width:100%;height:100%;">
					<table style="height: 100%;border-spacing:5px">
						<tr >
							<td style="width: 80px;text-align: left;">
								姓名:
							</td>
							<td style="width: 200px;text-align: left;">
								<input class="easyui-textbox" type="text" id="borrowName" data-options="validType:'maxLength[80,\'名称\']'"  style="width: 200px;"/>
							</td>
							<td style="width: 80px;text-align: left;">
								手机:
							</td>
							<td style="width: 200px;text-align: left;">
								<input class="easyui-textbox" type="text" id="borrowMphone" data-options="validType:'mobile'" style="width: 200px;"/>
							</td>
							<td style="width: 80px;text-align: left;">
								身份证号:
							</td>
							<td style="width: 200px;text-align: left;">
								<input class="easyui-textbox" type="text" id="borrowIdNum"  data-options="validType:'idCard'" style="width: 200px;"/>
							</td>
							<td></td>
						</tr>
						<tr>
							<td style="text-align: left;">
								客户经理:
							</td>
							<td style="text-align: left;">
								<input id="salesMan" name="salesMan"  class="custComboGrid" configValue="{width:'200px',baseParm:{employeeType:'业务员',inActive:'t'}}"/> 							
							</td>
							<td style="text-align: left;" >
								放款开始日期:
							</td>
							<td  style="text-align: left;">
								<input class="easyui-datebox" type="text" id="grantMoneyDateStart" data-options="editable : false"   style="width: 200px;"/>
							</td>
							<td style="text-align: left;">
								放款结束日期:
							</td>
							<td  style="text-align: left;">
								<input class="easyui-datebox" id="grantMoneyDateEnd"  data-options="editable : false"  type="text" style="width: 200px;"/>
							</td>
						</tr>
						<tr>
						   <td style="width: 80px;text-align: left;">合同编号:</td>
					       <td style="width: 200px;text-align: left;"><input class="easyui-textbox" type="text" id="contractNum"  style="width: 200px;"/></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div id="loanFilesInfoDataGridToolbar" style="padding: 3px;">
			<div id="bottonBox" style="margin-bottom: 0px">
				<a href="javascript:void(0)" id="submitButton"  class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" style="width:60px">查询</a>
				<a href="javascript:void(0)" id="clearButton" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>
			</div>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<table id="loanFilesInfoDataGrid" class="easyui-datagrid" data-options="toolbar:'#loanFilesInfoDataGridToolbar'" style="width:100%;height:100%"  />
		</div>
	</c:if>
	<c:if test="${resCode != '000000'}">
		<div style="width: 400px; height: auto; margin-left: auto; margin-right: auto; text-align: center;">
			${resMsg}
		</div>
	</c:if>
	</body>
</html>
