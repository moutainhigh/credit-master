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
		<title>借款详细信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			
		</script>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="">
		<div style="padding:5px;">
			<c:if test="${empty  loan}">
					无借款数据
			</c:if>
			<c:if test="${not empty  loan}">
				<div class="easyui-accordion" id="" data-options="multiple:true" style="width:100%;">
						<div title="借款类型：${loan.loanType}" data-options="iconCls:'icon-large-shapes'" style="">
							<table cellpadding="5"  width="100%" border="0" rules="rows"  style="table-layout:fixed;">
								<tr>
									<td width="150" style="word-break:break-all">借款人：</td>
									<td><label>${loan.personInfo.name }</label></td>
									<td width="150" style="word-break:break-all">性别：</td>
									<td><label>${loan.personInfo.sex }</td>
									<td width="170" style="word-break:break-all">身份证号：</td>
									<td><label>${loan.personInfo.idnum }</label></td>
								</tr>
								<tr>
									<td>申请金额（元）：</td>
									<td><label><fmt:formatNumber value="${loan.requestMoney }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
									<td>申请期限（月）：</td>
									<td><label>${loan.requestTime }</label></td>
									<td>申请日期：</td>
									<td><label><fmt:formatDate value="${loan.requestDate }"  pattern="yyyy-MM-dd"  /></label></td>
								</tr>
								<tr>
									<td>审批金额（元）：</td>
									<td><label><fmt:formatNumber value="${loan.money }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
									<td>借款期限（月）：</td>
									<td><label>${loan.time }</label></td>
									<td>&nbsp;</td>
									<td><label>&nbsp;</label></td>
								</tr>
								<tr>
									<td>状态：</td>
									<td><label>${loan.loanState }</label></td>
									<td>月还款能力（元）：</td>
									<td><label><fmt:formatNumber value="${loan.restoreem }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
									<td>用途：</td>
									<td><label>${loan.purpose }</label></td>
								</tr>
								<tr>
									<td>是否委外：</td>
									<td><label>${loan.isOutSourcing}</label></td>	
									<td>还款银行：</td>
									<td><label>
										<c:if test="${not empty loan.giveloanBank}">
											<c:out value="${loan.giveloanBank.bankName} (${loan.giveloanBank.account}) "/>
										</c:if>
									</label></td>
									<td>放款银行：</td>
									<td><label>
										<c:if test="${not empty loan.grantLoanBank}">
											<c:out value="${loan.grantLoanBank.bankName} (${loan.grantLoanBank.account}) "/>
										</c:if>
									</label></td>
								</tr>
								<tr>
									<td>管理营业部：</td>
									<td><label>
										<c:if test="${not empty loan.comOrganization}">
											<c:out value="${loan.comOrganization.name} "/>
										</c:if>
									</label></td>
									<td>客户经理：</td>
									<td><label>
										<c:if test="${not empty loan.salesMan}">
											<c:out value="${loan.salesMan.name} "/>
										</c:if>
									</label></td>
									<td>客服：</td>
									<td><label>
										<c:if test="${not empty loan.crmMan}">
											<c:out value="${loan.crmMan.name} "/>
										</c:if>
									</label></td>
								</tr>
								<tr>
									<td>合同来源：</td>
									<td><label>${loan.fundsSources }</label></td>
									<td>债权去向：</td>
									<td><label>${loan.loanBelong}</label></td>						
									<td>对公存入账户：</td>
									<td width="750">
										<label>
											<c:if test="${not empty loan.accountDetail}">
												<c:out value="${loan.accountDetail.accountName}
												${loan.accountDetail.accountBank}"/>
												<c:if test="${not empty loan.accountDetail.accountNum}">
													<c:out value="(${loan.accountDetail.accountNum})"></c:out>
												</c:if>
											</c:if>
										</label>
									</td>	
								</tr>
								<tr>
									<td>转让批次：</td>
									<td><label>${loan.transferBatch}</label></td>
									<td>转让状态：</td>
									<td><label>${loan.transferState}</label></td>
								</tr>
							</table>
						</div>
				</div>
			</c:if>
		</div>
	</body>
</html>












