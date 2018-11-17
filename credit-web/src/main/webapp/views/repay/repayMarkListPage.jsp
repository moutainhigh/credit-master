<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>还款备注</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
</style>

<script type="text/javascript">
	importPluginsExt(['panel','combobox','window','layout','datagrid','pagination'
                  ,'form','tooltip','validatebox','combogrid'],'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
			urlJs.push(global.contextPath + '/resources/js/repay/repayMarkListPage.js');
			
			importJSExt(urlJs,function(){
				/** 脚本加载成功回调方法 **/
				
			});
		});
	});
</script>
</head>
<body class="easyui-layout">
<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-panel" title="查询条件" style="padding: 10px; height: 100px; margin: 0px; text-align: center;"
		data-options="region:'north'">
		<form id="searchForm">
			<table  cellpadding="5">
				<tr>
					<td style="width: 72px;text-align: left;">合同编号:</td>
					<td style="width: 160px;text-align: left;"><input class="easyui-textbox" type="text" name="contractNum"  id="contractNum"  name="name" style="width: 150px;"></input></td>
					<td style="width: 72px;text-align: left;">合同来源:</td>
					<td style="width: 160px;text-align: left;">
						<select class="easyui-combobox" id='fundsSources' name='fundsSources' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                           	<option value="">请选择</option>
                               <c:forEach var="fundsSources" items="${fundsSources}">
                               	<option value="${fundsSources}">${fundsSources}</option>
                               </c:forEach>
                          </select>
					</td>
					<td style="width: 72px;text-align: left;">身份证号:</td>
					<td style="width: 150px;text-align: left;"><input class="easyui-textbox" type="text" value="" name="idNum" id="idNum"  validType="idCard" style="width: 150px;"></input></td>
					<td style="width: 72px;text-align: left;">借款状态:</td>
					<td style="width: 150px;text-align: left;">
						<select class="easyui-combobox" id='loanState' name='loanState' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                           	<option value="">请选择</option>
                               <c:forEach var="loanState" items="${loanState}">
                               	<option value="${loanState}">${loanState}</option>
                               </c:forEach>
                           </select>
					</td>
				</tr>
		</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;" data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="VloaninfoDataGrid" class ="easyui-datagrid" data-options=""></table>
		
		<div id="tb" style="padding: 3px;">
		  <div style="margin-bottom: 0px">
			<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>	
			<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" 
			data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>
		  </div>
		</div>
	</div>
	
</body>
</html>
