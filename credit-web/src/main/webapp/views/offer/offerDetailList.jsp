<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>委托代扣报盘</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>	
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/offer/offerDetailList.js');
					
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				});
			});
		</script>	
	</head>
	<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
		<div class="easyui-panel" style="padding:0px;margin: 0px; " data-options="region:'center'">
				<form id="searchForm">
				<input type="hidden" name="offerId"  id="offerId"  value="${offerId}"/>
				<input type="hidden" name="isThird"  id="isThird"  value="${isThird}"/>
				<input type="hidden" name="fundsSources"  id="fundsSources"  value="${fundsSources}"/>
			</form>
			<!-- 表格标签 -->
			<table id="offerDetailDataGrid" class="easyui-datagrid" data-options="" style="padding:0px;margin: 0px;height: 100%;width: 100% "></table>
		</div>
	</body>
</html>

