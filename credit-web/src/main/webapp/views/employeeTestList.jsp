<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ include file="/views/common/headIncludeFile.jsp"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<script type="text/javascript" charset="UTF-8" src="${path}/resources/js/config.js"></script>
		<style type="text/css">
			
		</style>
	</head>
	<body class="easyui-layout">
			<div id="tbemployee" style="padding:3px;">
			<div style="margin-bottom:0px">		
			<sec:authorize ifAnyGranted="/system/userTest/update">
				<a href="#" class="easyui-linkbutton" id="updateEmployee"  iconCls="icon-remove" plain="true">员工修改</a>
			</sec:authorize>
				
				<a href="#" class="easyui-linkbutton" id="insertEmployee"  iconCls="icon-search" plain="true">员工新增</a>
				<a href="#" class="easyui-linkbutton" id="delEmployee"  iconCls="icon-search" plain="true">员工删除</a>
			</div>
			</div>
	</body>
</html>
