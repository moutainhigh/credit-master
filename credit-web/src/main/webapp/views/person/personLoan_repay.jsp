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
		<title>还款汇总信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			
		</script>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="">
		<div style="padding:5px;">
			<c:if test="${empty  offerRepayInfoVo}">
					无借款数据或已结清
			</c:if>
			<c:if test="${not empty  offerRepayInfoVo}">
				<div class="easyui-accordion" id="" data-options="multiple:true" style="width:100%;">
					<div title="还款汇总信息" data-options="iconCls:'icon-ok'" style="">
						<table cellpadding="5"  width="100%" border="0" rules="rows"  style="table-layout:fixed;">
							<tr>
								<td width="100px" class="">姓名：</td>
								<td width="">${offerRepayInfoVo.name }</td>
								<td width="160px" class="">证件类型：</td>
								<td width="">${offerRepayInfoVo.idType }</td>
								<td width="160px" class="">身份证号：</td>
								<td width="">${offerRepayInfoVo.idNum }</td>
								<td width="80px" class="">手机：</td>
								<td width="">${offerRepayInfoVo.mphone }</td>
							</tr>
							<tr>
								<td width="" class="">逾期起始日：</td>
								<td><fmt:formatDate value="${offerRepayInfoVo.overDueDate }"  pattern="yyyy-MM-dd"  /></td>
								<td width="" class="">逾期总数：</td>
								<td>${offerRepayInfoVo.overDueTerm }</td>
								<td width="" class="">逾期利息：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.overInterest }" type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
								<td width="" class="">逾期本金：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.overCorpus }" type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
							</tr>
							<tr>
								<td width="" class="">罚息起算日：</td>
								<td><fmt:formatDate value="${offerRepayInfoVo.fineDate }"  pattern="yyyy-MM-dd"  /></td>
								<td width="" class="">罚息天数：</td>
								<td>${offerRepayInfoVo.fineDay }</td>
								<td width="" class="">剩余本息和：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.remnant }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
								<td width="" class="">罚息金额：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.fine }" type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
							</tr>
							<tr>
								<td width="" class="">当期还款日：</td>
								<td><fmt:formatDate value="${offerRepayInfoVo.currDate }"  pattern="yyyy-MM-dd"  /></td>
								<td width="" class="">当前期数：</td>
								<td>${offerRepayInfoVo.currTerm }</td>
								<td width="" class="">当期利息：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.currInterest }" type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
								<td width="" class="">当期本金：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.currCorpus }" type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
							</tr>
							<tr>
								<td width="" class="">挂账金额：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.accAmount }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
								<td width="" class="">减免金额：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.relief }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
								<td width="" class="">应还总额（不含当期）：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.overdueAmount }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
								<td width="" class="">应还总额（包含当期）：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.currAllAmount }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
							</tr>
							<tr>
								<td width="" class="">账户类别：</td>
								<td>${offerRepayInfoVo.repaymentLevel }</td>
								<td width="" class="">剩余本金：</td>
								<td><fmt:formatNumber value="${offerRepayInfoVo.residualPactMoney}"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></td>
							</tr>							
						</table>
					</div>
				</div>
			</c:if>
		</div>
	</body>
</html>












