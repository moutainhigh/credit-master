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
		<script type="text/javascript">
			
		</script>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="">
		<div style="padding:5px;">
			<c:if test="${empty  loans}">
					无借款数据
			</c:if>
			<c:if test="${not empty  loans}">
				<div class="easyui-accordion" id="" data-options="multiple:true" style="width:100%;">
					<c:forEach items="${loans}"  varStatus="varStatus"  var="item">
						<div title="借款类型：${item.loanType}" data-options="iconCls:'icon-large-shapes'" style="">
							<table cellpadding="5"  width="100%" border="0" rules="rows"  style="table-layout:fixed;">
								<tr>
									<td width="150" style="word-break:break-all">审批金额：</td>
									<td><label><fmt:formatNumber value="${item.money }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
									<td width="150" style="word-break:break-all">申请日期：</td>
									<td><label><fmt:formatDate value="${item.requestDate }"  pattern="yyyy-MM-dd"  /></label></td>
									<td width="110" style="word-break:break-all">状态：</td>
									<td><label>${item.loanState }</label></td>
								</tr>
								<tr>
									<td>借款期限（月）：</td>
									<td><label>${item.time }</label></td>
									<td>月还款能力（元）：</td>
									<td><label><fmt:formatNumber value="${item.restoreem }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
									<td>用途：</td>
									<td><label>${item.purpose }</label></td>
								</tr>
								<tr>
									<td>管理营业部：</td>
									<td><label>
										<c:if test="${not empty item.comOrganization}">
											<c:out value="${item.comOrganization.name} "/>
										</c:if>
									</label></td>
									<td>客户经理：</td>
									<td><label>
										<c:if test="${not empty item.salesMan}">
											<c:out value="${item.salesMan.name} "/>
										</c:if>
									</label></td>
									<td>客服：</td>
									<td><label>
										<c:if test="${not empty item.crmMan}">
											<c:out value="${item.crmMan.name} "/>
										</c:if>
									</label></td>
								</tr>
								<tr>
									<td>还款银行：</td>
									<td colspan="2"><label>
										<c:if test="${not empty item.giveloanBank}">
											<c:out value="${item.giveloanBank.bankName} (${item.giveloanBank.account}) "/>
											<br/>
											开户网点：<c:out value="${item.giveloanBank.fullName}"/>
										</c:if>
									</label></td>
									<td>放款银行：</td>
									<td colspan="2"><label>
										<c:if test="${not empty item.grantLoanBank}">
											<c:out value="${item.grantLoanBank.bankName} (${item.grantLoanBank.account}) "/>
											<br/>
											开户网点：<c:out value="${item.grantLoanBank.fullName}"/>
										</c:if>
									</label></td>
								</tr>
							</table>
							<div style="padding-left:10px;">
								<a href="javascript:void(0);" onclick="$.personDetail.showLoanDetailInfo(${item.id},'${item.personInfo.name}(${item.loanType})');">查看详细</a>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:if>
		</div>
	</body>
</html>