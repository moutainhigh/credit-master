<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%response.setStatus(HttpServletResponse.SC_OK);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>500信息提示</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resources/css/errorAll.css">
	</head>
	<body style="background:#edf6fa;">
		<div id="doc_main">
			<section class="bd clearfix">
				<div class="module-error">
					<div class="error-main clearfix">
						<div class="label"></div>
						<div class="info">
							<h3 class="title">啊哦，服务器压力过大，请稍等重试。</h3>
							<div class="reason">
								<p>可能的原因：</p>
								<p>1.服务端忙或异常。</p>
							</div>
							<div class="oper">
								<p><a href="<%=request.getContextPath()%>/views/index.jsp">回到首页&gt;</a></p>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
	</body>
</html>