<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>EasyUI示例</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript" charset="UTF-8" src="<%=request.getContextPath() %>/resources/js/config.js"></script>
		<style type="text/css">
			
		</style>
	</head>
	<body class="easyui-layout">
			<div id="tblog" style="padding:3px;">
			<div style="margin-bottom:0px">				
				<a href="#" class="easyui-linkbutton" id="dellog"  iconCls="icon-remove" plain="true">删除日志</a>
				<a href="#" class="easyui-linkbutton" id="viewlog"  iconCls="icon-search" plain="true">查看日志</a>
			</div>
			</div>
	</body>
</html>
