<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.zdmoney.credit.common.util.Strings" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<%
			String isFrame = Strings.convertValue(request.getParameter("isFrame"),String.class);
			request.setAttribute("isFrame",isFrame);
		%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>客户详细资料信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','accordion'],'business', function() {
				$(function() {
					var urlJs = [];
					<c:if test="${empty isFrame}" >
						importJSExt(urlJs,function(){
						
						});
					</c:if>
				});
			});
		</script>
	</head>
	<body data-options="" style="">
		<c:if test="${empty isFrame}" >
			<jsp:include page="/views/common/initPageMast.jsp" />
		</c:if>
		<c:if test="${empty personInfo}" >
			无数据
		</c:if>
		<c:if test="${!empty personInfo}" >
			<div data-options="" style="margin:0px;padding:5px;">
				<div class="easyui-accordion" id="accordion" data-options="multiple:true" style="width:100%;">
					<div title="个人信息" data-options="iconCls:'icon-man'" style="padding:10px;">
						<table cellpadding="5"  width="100%" border="0" rules="rows"  style="table-layout:fixed;">
							<tr>
								<td width="160" style="word-break:break-all">姓名：</td>
								<td><label>${personInfo.name }</label></td>
								<td width="160" style="word-break:break-all">性别：</td>
								<td><label>${personInfo.sex }</label></td>
								<td width="170" style="word-break:break-all">身份证号：</td>
								<td><label>${personInfo.idnum }</label></td>
							</tr>
							<tr>
								<td>婚姻状况：</td>
								<td><label>${personInfo.married }</label></td>
								<td>最高学历：</td>
								<td><label>${personInfo.edLevel }</label></td>
								<td>子女数目：</td>
								<td><label>${personInfo.childrenCount }</label></td>
							</tr>
							<tr>
								<td>户籍地址(需与身份证一致)：</td>
								<td colspan="3"><label>${personInfo.hrAddress }</label></td>
								<td>邮编：</td>
								<td><label>${personInfo.hrPostcode }</label></td>
							</tr>
							<tr>
								<td>现居住地址：</td>
								<td colspan="3"><label>${personInfo.address }</label></td>
								<td>邮编：</td>
								<td><label>${personInfo.postcode }</label></td>
							</tr>
							<tr>
								<td>手机：</td>
								<td><label>${personInfo.mphone }</label></td>
								<td>现住宅电话：</td>
								<td><label>${personInfo.tel }</label></td>
								<td>QQ：</td>
								<td><label>${personInfo.qq }</label></td>
							</tr>
							<tr>
								<td>邮箱：</td>
								<td><label>${personInfo.email }</label></td>
								<td>优先联系地址：</td>
								<td><label>${personInfo.addressPriority }</label></td>
								<td>信用卡：</td>
								<td>
									<label>
										<c:choose>
											<c:when test="${personInfo.hasCreditCard == 't'}">有</c:when>
											<c:when test="${personInfo.hasCreditCard == 'f'}">无</c:when>
											<c:otherwise>未知</c:otherwise>
										</c:choose>
									</label>
								</td>
							</tr>
							<tr>
								<td>贷款：</td>
								<td>
									<label>
										<c:choose>
											<c:when test="${personInfo.hasLoan == 't'}">有</c:when>
											<c:when test="${personInfo.hasLoan == 'f'}">无</c:when>
											<c:otherwise>未知</c:otherwise>
										</c:choose>
									</label>
								</td>
								<td>单张信用卡最高额度（元）：</td>
								<td><label><fmt:formatNumber value="${personInfo.cardMaxAmount }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
								<td>每月家庭支出（元）：</td>
								<td><label><fmt:formatNumber value="${personInfo.familyExpense }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
							</tr>
							<tr>
								<td>管理营业部：</td>
								<td><label>${personInfo.comOrganization.name }</label></td>
								<td>备注：</td>
								<td colspan="3"><label>${personInfo.memo }</label></td>
							</tr>
						</table>
					</div>
					<div title="房产信息" data-options="iconCls:'icon-man'" style="padding:10px;">
						<c:if test="${fn:length(houses) <= 0}">
							暂无数据
						</c:if>
						<c:if test="${fn:length(houses) > 0}">
							<table cellpadding="5"  width="100%" border="0" rules="rows" style="table-layout:fixed;">
								<tr>
									<td width="160">房产类型：</td>
									<td><label>${personInfo.houseType}</label></td>
									<td width="160">房贷：</td>
									<td>
										<label>
											<c:choose>
												<c:when test="${houses[0].hasLoan == 't'}">有</c:when>
												<c:when test="${houses[0].hasLoan == 'f'}">无</c:when>
												<c:otherwise>未知</c:otherwise>
											</c:choose>
										</label>
									</td>
									<td width="170">购买时间：</td>
									<td><label><fmt:formatDate value="${houses[0].buyTime }"  pattern="yyyy-MM-dd"  /></label></td>
								</tr>
								<tr>
									<td>购买价格（元）：</td>
									<td><label><fmt:formatNumber value="${houses[0].price}"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
									<td>建筑面积（平米）：</td>
									<td><label>${houses[0].buildingArea}</label></td>
									<td>产权比例（%）：</td>
									<td><label>${houses[0].propertyProportion}</label></td>
								</tr>
								<tr>
									<td>贷款年限：</td>
									<td><label>${houses[0].loanPeriod}</label></td>
									<td>月供（元）：</td>
									<td colspan="3"><label><fmt:formatNumber value="${houses[0].monthlyPayment}"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
								</tr>
								<tr>
									<td>房产地址：</td>
									<td colspan="3"><label>${houses[0].address}</label></td>
									<td>邮编：</td>
									<td><label>${houses[0].postcode}</label></td>
								</tr>
							</table>
						</c:if>
					</div>
					<div title="车辆信息" data-options="iconCls:'icon-man'" style="padding:10px;">
						<c:if test="${fn:length(cars) <= 0}">
							暂无数据
						</c:if>
						<c:if test="${fn:length(cars) > 0}">
							<table cellpadding="5"  width="100%" border="0" rules="rows" style="table-layout:fixed;">
								<tr>
									<td width="160">购车类型：</td>
									<td><label>${cars[0].carType}</label></td>
									<td width="160">车辆购买时间：</td>
									<td><label><fmt:formatDate value="${cars[0].buyTime }"  pattern="yyyy-MM-dd"  /></label></td>
									<td width="170">	购车价格（不含车辆购置税）：</td>
									<td><label><fmt:formatNumber value="${cars[0].price}"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
								</tr>
								<tr>
									<td>车贷：</td>
									<td>
										<label>
											<c:choose>
												<c:when test="${cars[0].hasLoan == 't'}">有</c:when>
												<c:when test="${cars[0].hasLoan == 'f'}">无</c:when>
												<c:otherwise>未知</c:otherwise>
											</c:choose>
										</label>
									</td>
									<td>贷款期限：</td>
									<td><label>${cars[0].loanPeriod}</label></td>
									<td>月供金额（元）：</td>
									<td><label><fmt:formatNumber value="${cars[0].monthlyPayment}"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
								</tr>
							</table>
						</c:if>
					</div>
					<div title="职业信息" data-options="iconCls:'icon-man'" style="padding:10px;">
						<table cellpadding="5"  width="100%" border="0" rules="rows" style="table-layout:fixed;">
							<tr>
								<td width="160">学校/公司名称：</td>
								<td><label>${personInfo.company }</label></td>
								<td width="160">单位性质：</td>
								<td><label>${personInfo.cType }</label></td>
								<td width="170">	所属行业：</td>
								<td><label>${personInfo.industryType }</label></td>
							</tr>
							<tr>
								<td>所属部门/专业：</td>
								<td><label>${personInfo.department }</label></td>
								<td>职位级别：</td>
								<td><label>${personInfo.officialRank }</label></td>
								<td>学校/公司固定电话：</td>
								<td><label>${personInfo.cTel }</label></td>
							</tr>
							<tr>
								<td>学校/单位地址：</td>
								<td><label>${personInfo.cAddress }</label></td>
								<td>邮编：</td>
								<td><label>${personInfo.cCode }</label></td>
								<td>入职时间：</td>
								<td><label><fmt:formatDate value="${personInfo.cEnterTime }"  pattern="yyyy-MM-dd"  /></label></td>
							</tr>
							<tr>
								<td>发薪方式：</td>
								<td><label>${personInfo.payType }</label></td>
								<td>月发薪日：</td>
								<td><label>${personInfo.payDay }</label></td>
								<td>月基本薪金（元）：</td>
								<td><label><fmt:formatNumber value="${personInfo.wage }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
							</tr>
							<tr>
								<td>其他收入（元）：</td>
								<td><label><fmt:formatNumber value="${personInfo.otherMonthlyIncome }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
								<td>每月总收入（元）：</td>
								<td><label><fmt:formatNumber value="${personInfo.totalMonthlyIncome }"  type="currency" minFractionDigits="0"  maxFractionDigits="2"/></label></td>
								<td>职业类型：</td>
								<td><label>${personInfo.profession }</label></td>
							</tr>
						</table>
					</div>
					<div title="企业信息" data-options="iconCls:'icon-man'" style="padding:10px;">
						<c:if test="${fn:length(entrepreneurs) <= 0}">
							暂无数据
						</c:if>
						<c:if test="${fn:length(entrepreneurs) > 0}">
							<table cellpadding="5"  width="100%" border="0" rules="rows" style="table-layout:fixed;">
								<tr>
									<td width="160" style="text-align:left;">私营企业类型：</td>
									<td><label>${entrepreneurs[0].enterpriseType}</label></td>
									<td width="160">&nbsp;</td>
									<td><label>&nbsp;</label></td>
									<td width="170">&nbsp;</td>
									<td><label>&nbsp;</label></td>
								</tr>
								<tr>
									<td>成立时间：</td>
									<td><label>${entrepreneurs[0].timeFounded}</label></td>
									<td>经营场所：</td>
									<td><label>${entrepreneurs[0].premisesType}</label></td>
									<td>员工人数：</td>
									<td><label>${entrepreneurs[0].employeeAmount}</label></td>
								</tr>
								<tr>
									<td>占股比例（%）：</td>
									<td><label>${entrepreneurs[0].shareholdingRatio}</label></td>
									<td>月净利润额（万）：</td>
									<td><label>${entrepreneurs[0].monthlyNetProfit}</label></td>
									<td>企业净利润率（%）：</td>
									<td><label>${entrepreneurs[0].profitMargin}</label></td>
								</tr>
							</table>
						</c:if>
					</div>
				</div>
			</div>
		</c:if>
	</body>
</html>