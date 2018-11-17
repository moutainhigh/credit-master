<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title></title>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/loan/loanFilesInfoDetail.css" />
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/loan/loanFilesInfoDetail.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				})
			});
		</script>			
	</head>
	<body>
	<jsp:include page="/views/common/initPageMast.jsp" />		
		<div class="contentBox">
			<div class="title">证大财富</div>
			<div class="loanFilesInfoNum">
				<span class="label" >档案编号：</span>
				<span class="value">${loanFilesInfo.borrowerDocFlowNum}</span>
			</div>
			<div class="organization">
				<span class="label">营业部：</span>
				<span class="value">${loanBaseInfo.salesDepartmentName}</span>
			</div>
			<div class="customer">
				<span class="name-label">客户姓名：</span>
				<span class="name-value">${loanBaseInfo.borrowerName}</span> 
				<span class="product-label">产品类型：</span>
				<span class="product-value">${loanBaseInfo.loanType}</span>
				<span class="date-label">签约日期：</span>
				<span class="year-value">${fn:substring(loanBaseInfo.signDate,0,4)}</span>
				<span class="year-label">年</span> 
				<span class="month-value">${fn:substring(loanBaseInfo.signDate,5,7) - 0}</span>
				<span class="month-label">月</span> 
				<span class="day-value">${fn:substring(loanBaseInfo.signDate,8,10) - 0}</span>
				<span class="day-label">日</span>
			</div>
	
			<table class="loanFilesInfoTable" >
				<caption>客户档案一览表</caption>
				<col style="width: 10%;"/>
			    <col style="width: 12%" />
			     <col style="width: 68%" />
			    <col style="width: 10%" />
				<tbody>
					<tr>
						<td  class="header" >顺序</td>
						<td class="header" colspan="2"  >资料类别</td>
						<td class="header"  >页数</td>
					</tr>
					<tr>
						<td >1</td>
						<c:if test="${loanFilesInfo.mark == null}">
							<td colspan="2" >审批单</td>
						</c:if>
						<c:if test="${loanFilesInfo.mark == 'v1'}">
							<td colspan="2" >审批单</td>
						</c:if>
						<c:if test="${loanFilesInfo.mark == 'v2'}">
							<td colspan="2" >致客户书</td>
						</c:if>
						<td ><c:if test="${loanFilesInfo.autidingFilesCount != null }">${loanFilesInfo.autidingFilesCount}</c:if></td>
					</tr>
					<tr>
						<td >2</td>
						<td colspan="2" >借款申请表</td>
						<td ><c:if test="${loanFilesInfo.requestFilesCount != null }">${loanFilesInfo.requestFilesCount}</c:if></td>
					</tr>
					<tr>
						<td >3</td>
						<td colspan="2">银行卡复印件</td>
						<td ><c:if test="${loanFilesInfo.bankCardFilesCount != null }">${loanFilesInfo.bankCardFilesCount}</c:if></td>
					</tr>
					<tr>
						<td>4</td>
						<c:if test="${loanFilesInfo.mark == null}">
							<td colspan="2">借款人签约记录表</td>
						</c:if>
						<c:if test="${loanFilesInfo.mark == 'v1'}">
							<td colspan="2">借款人签约记录表</td>
						</c:if>
						<c:if test="${loanFilesInfo.mark == 'v2'}">
							<td colspan="2">签约确认函</td>
						</c:if>
						<td><c:if test="${loanFilesInfo.serviceFilesCount != null }">${loanFilesInfo.serviceFilesCount}</c:if></td>
					</tr>
					<tr>
						<td>5</td>
						<td colspan="2">个人信用报告</td>
						<td><c:if test="${loanFilesInfo.personalCreditReportsCount != null }">${loanFilesInfo.personalCreditReportsCount}</c:if></td>
					</tr>
					<tr>
						<td style="height: 57px;">6</td>
						<td>身份证明</td>
						<td><c:if test="${loanFilesInfo.idCertification != null  }">${loanFilesInfo.idCertification}</c:if><c:if test="${loanFilesInfo.idCertificationOther != null && loanFilesInfo.idCertificationOther != '' }"><c:if test="${loanFilesInfo.idCertification != null && loanFilesInfo.idCertification != '' }">,</c:if>其他：${loanFilesInfo.idCertificationOther}</c:if></td>
						<td><c:if test="${loanFilesInfo.idCertificationCount != null }">${loanFilesInfo.idCertificationCount}</c:if></td>
					</tr>
					<tr>
						<td style="height: 55px;">7</td>
						<td>收入证明</td>
						<td><c:if test="${loanFilesInfo.incomeCertification != null && fn:trim(loanFilesInfo.incomeCertification)!='' }">${fn:trim(loanFilesInfo.incomeCertification)}</c:if><c:if test="${loanFilesInfo.incomeCertificationOther != null && fn:trim(loanFilesInfo.incomeCertificationOther)!='' }"><c:if test="${loanFilesInfo.incomeCertification != null && fn:trim(loanFilesInfo.incomeCertification)!='' }">,</c:if>其他：${fn:trim(loanFilesInfo.incomeCertificationOther)}</c:if></td>
						<td><c:if test="${loanFilesInfo.incomeCertificationCount != null }">${loanFilesInfo.incomeCertificationCount}</c:if></td>
					</tr>
					<tr>
						<td style="height: 51px;">8</td>
						<td>工作证明</td>
						<td><c:if test="${loanFilesInfo.workCertification != null && fn:trim(loanFilesInfo.workCertification)!=''}">${fn:trim(loanFilesInfo.workCertification)}</c:if><c:if test="${loanFilesInfo.workCertificationOther != null && fn:trim(loanFilesInfo.workCertificationOther)!='' }"><c:if test="${loanFilesInfo.workCertification != null && fn:trim(loanFilesInfo.workCertification)!='' }">,</c:if>其他：${fn:trim(loanFilesInfo.workCertificationOther)}</c:if></td>
						<td><c:if test="${loanFilesInfo.workCertificationCount != null }">${loanFilesInfo.workCertificationCount}</c:if></td>
					</tr>
					<tr>
						<td style="height: 73px;">9</td>
						<td>住址证明</td>
						<td><c:if test="${loanFilesInfo.addressCertification != null && fn:trim(loanFilesInfo.addressCertification)!='' }">${fn:trim(loanFilesInfo.addressCertification)}</c:if><c:if test="${loanFilesInfo.addressCertificationOther != null && fn:trim(loanFilesInfo.addressCertificationOther)!='' }"><c:if test="${loanFilesInfo.addressCertification != null && fn:trim(loanFilesInfo.addressCertification)!='' }">,</c:if>其他：${fn:trim(loanFilesInfo.addressCertificationOther)}</c:if></td>
						<td><c:if test="${loanFilesInfo.addressCertificationCount != null }">${loanFilesInfo.addressCertificationCount}</c:if></td>
					</tr>
					<tr>
						<td style="height: 113px;">10</td>
						<td>经营证明</td>
						<td><c:if test="${loanFilesInfo.businessCertification != null && fn:trim(loanFilesInfo.businessCertification)!='' }">${fn:trim(loanFilesInfo.businessCertification)}</c:if><c:if test="${loanFilesInfo.businessCertificationOther != null && fn:trim(loanFilesInfo.businessCertificationOther)!='' }"><c:if test="${loanFilesInfo.businessCertification != null && fn:trim(loanFilesInfo.businessCertification)!='' }">,</c:if>其他：${fn:trim(loanFilesInfo.businessCertificationOther)}</c:if></td>
						<td><c:if test="${loanFilesInfo.businessCertificationCount != null }">${loanFilesInfo.businessCertificationCount}</c:if></td>
					</tr>
					<tr>
						<td style="height: 73px;">11</td>
						<td>资产证明</td>
						<td><c:if test="${loanFilesInfo.assetCertification != null && fn:trim(loanFilesInfo.assetCertification)!='' }">${fn:trim(loanFilesInfo.assetCertification)}</c:if><c:if test="${loanFilesInfo.assetCertificationOther != null && fn:trim(loanFilesInfo.assetCertificationOther)!='' }"><c:if test="${loanFilesInfo.assetCertification != null && fn:trim(loanFilesInfo.assetCertification)!=''}">,</c:if>其他：${fn:trim(loanFilesInfo.assetCertificationOther)}</c:if></td>
						<td><c:if test="${loanFilesInfo.assetCertificationCount != null }">${loanFilesInfo.assetCertificationCount}</c:if></td>
					</tr>
					<tr>
						<td style="height: 50px;">12</td>
						<td>其他证明</td>
						<td><c:if test="${loanFilesInfo.otherCertification != null && fn:trim(loanFilesInfo.otherCertification)!=''}">${fn:trim(loanFilesInfo.otherCertification)}</c:if><c:if test="${loanFilesInfo.otherCertificationOther != null && fn:trim(loanFilesInfo.otherCertificationOther)!=''  }"><c:if test="${loanFilesInfo.otherCertification != null && fn:trim(loanFilesInfo.otherCertification)!='' }">,</c:if>其他：${fn:trim(loanFilesInfo.otherCertificationOther)}</c:if></td>
						<td><c:if test="${loanFilesInfo.otherCertificationCount != null}">${loanFilesInfo.otherCertificationCount}</c:if></td>
					</tr>
					<tr>
						<td >13</td>
						<td colspan="3" style="height: 75px; text-align: left">备注： <c:if test="${loanFilesInfo.memo != null && fn:trim(loanFilesInfo.memo)!=''}">${fn:trim(loanFilesInfo.memo)}</c:if></td>
					</tr>
				</tbody>
			</table>
	
			<div class="loanFilesInfoInput" >
				<span class="name-label">档案整理人：</span>
				<span class="name-value">${loanFilesInfo.operatorName}</span> 
				<span class="date-label">日期：</span>
				<span class="year-value">${fn:substring(loanFilesInfo.dateCreated,0,4)}</span>
				<span class="year-label">年</span> 
				<span class="month-value">${fn:substring(loanFilesInfo.dateCreated,5,7) - 0}</span> 
				<span class="month-label">月</span> 
				<span class="day-value">${fn:substring(loanFilesInfo.dateCreated,8,10) - 0}</span>
				<span class="day-label">日</span>
			</div>
			<div class="loanFilesInfoManager">
				<span class="name-label">档案管理员：</span>
				<span class="name-value"></span>
				<span class="date-label">日期：</span>
				<span class="year-value"></span>
				<span class="year-label">年</span> 
				<span class="month-value"></span> 
				<span class="month-label">月</span> 
				<span class="day-value"></span>
				<span class="day-label">日</span>
			</div>
		</div>
	</body>
</html>
