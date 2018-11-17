<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>报表查询错误</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript" charset="UTF-8" src="<%=request.getContextPath()%>/resources/js/report/reportErrorPage.js"></script>
	</head>
	<body class="easyui-layout">
		<div  data-res-msg="${resMsg}" data-res-code="${resCode}"  style="width: 400px; height: auto; margin-left: auto; margin-right: auto; text-align: center;">
			${resMsg}
		</div>
	</body>
</html>
