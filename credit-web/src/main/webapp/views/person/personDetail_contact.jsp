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
		<title>相关联系人信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="">
		<div style="padding:5px;">
			<div class="easyui-accordion" id="" data-options="multiple:true" style="width:100%;">
				<c:if test="${empty  contacts}">
					无数据
				</c:if>
				<c:forEach items="${contacts}"  varStatus="varStatus"  var="item">
					<div title="第${varStatus.count }个联系人:${item.name}" data-options="iconCls:'icon-man'" style="">
						<table cellpadding="5"  width="100%" border="0" rules="rows"  style="table-layout:fixed;">
							<tr>
								<td width="150" style="word-break:break-all">姓名：</td>
								<td><label>${item.name }</label></td>
								<td width="150" style="word-break:break-all">联系人类型：</td>
								<td><label>${item.contactType }</label></td>
								<td width="170" style="word-break:break-all">关系：</td>
								<td><label>${item.relation }</label></td>
							</tr>
							<tr>
								<td>手机：</td>
								<td><label>${item.mphone }</label></td>
								<td>家庭电话：</td>
								<td><label>${item.tel }</label></td>
								<td>身份证：</td>
								<td><label>${item.idCard }</label></td>
							</tr>
							<tr>
								<td>现居地址：</td>
								<td><label>${item.address }</label></td>
								<td>所在公司：</td>
								<td><label>${item.company }</label></td>
								<td>部门：</td>
								<td><label>${item.department }</label></td>
							</tr>
							<tr>
								<td>职务：</td>
								<td><label>${item.duty }</label></td>
								<td>公司电话：</td>
								<td><label>${item.ctel }</label></td>
								<td>是否知晓贷款：</td>
								<td>
									<c:choose>
										<c:when test="${item.isKnowLoan == 't'}">是</c:when>
										<c:when test="${item.isKnowLoan == 'f'}">否</c:when>
										<c:otherwise>未知</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</table>
					</div>
				</c:forEach>
			</div>
		</div>
	</body>
</html>












