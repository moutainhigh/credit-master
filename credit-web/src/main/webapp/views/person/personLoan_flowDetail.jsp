<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>账卡信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="">
		<script type="text/javascript">
			/** 借款编号 **/
			var loanId = <%=request.getAttribute("id")%>;
			
			importPluginsExt([],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/person/personLoan_flowDetail.js');
					importJSExt(urlJs,function(){
						
					});
				});
			});
		</script>
		<table id="flowDetailDataGrid" class="easyui-datagrid" data-options=""></table>
		<div id="showSingleFlowDetailWin" class="easyui-window" title="账卡详细信息" data-options="closed:true,modal : true,collapsible : false,minimizable : false,maximizable : false,resizable : false,iconCls:'pic_57'"  style="width:600px;height:350px;padding:0px;">
			<table id="singleFlowDetailDataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		
	</body>
</html>












