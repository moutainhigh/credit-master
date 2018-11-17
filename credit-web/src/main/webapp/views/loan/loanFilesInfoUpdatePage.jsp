<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>重建客户档案</title>
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/loan/loanFilesInfoUpdatePage.css" />
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/loan/loanFilesInfoUpdatePage.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				})
			});
		</script>
	</head>
	<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<c:if test="${resCode == '000000'}">
		<div id="params" style="position: none" loan-id="${loanBaseInfo.loanId}" version="${loanFilesInfo.version}" >
		</div>
		<div data-options="border:false,region:'center',title:'客户档案'">
			<div style="height: 80px;">
				<table class="loan"  style="width: 100%; height: 100%">
					<tr>
						<td class="label">放款营业部:</td>
						<td class="value">${loanBaseInfo.salesDepartmentName}</td>
						<td class="label"></td>
						<td class="value"></td>
						<td class="label"></td>
						<td class="value"></td>
					</tr>
					<tr>
						<td class="label">客户姓名:</td>
						<td id="borrowerName" class="value">${loanBaseInfo.borrowerName}</td>
						<td class="label" >产品类型:</td>
						<td class="value">${loanBaseInfo.loanType}</td>
						<td class="label">签约日期:</td>
						<td class="value">${loanBaseInfo.signDate }</td>
						<td class="label">合同编号:</td>
						<td class="value">${loanBaseInfo.contractNum }</td>
					</tr>
				</table>
			</div>
			<div >
				<form id="loanFilesInfoForm">
					<table  class="loanFilesInfo"  style="width: 100%; height: 100%;table-layout:fixed;">
						<thead>
							<tr>
								<th class="number" >顺序</th>
								<th class="type" >资料类别</th>
								<th class="context"></th>
								<th class="count">页数</th>
							</tr>
						</thead>
						<tbody>
					        <tr>
					            <td valign="middle" class="value">1</td>
					            <td valign="middle" colspan="2" class="value">致客户书</td>
					            <td valign="middle" class="value"> 
					           		<input type="text" class="easyui-textbox " data-options="missingMessage:'致客户书数量为必输项',validType:['positiveIntegerNumer[\'致客户书数量\']','maxLength[8,\'致客户书数量\']']"  <c:choose><c:when test="${loanFilesInfo.autidingFilesCount != null}">value="${loanFilesInfo.autidingFilesCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="autidingFilesCount"/>
					            </td>
					        </tr>
						    <tr>
						        <td valign="middle" class="value">2</td>
						        <td valign="middle" class="value" colspan="2">借款申请表</td>
						        <td valign="middle" class="value"> 
						        <input type="text" class="easyui-textbox" data-options="missingMessage:'借款申请表数量为必输项',validType:['positiveIntegerNumer[\'借款申请表数量\']','maxLength[8,\'借款申请表数量\']']"  <c:choose><c:when test="${loanFilesInfo.requestFilesCount != null}">value="${loanFilesInfo.requestFilesCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="requestFilesCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">3</td>
						        <td valign="middle" class="value" colspan="2">银行卡复印件</td>
						        <td valign="middle" class="value"> 
						        	<input type="text" class="easyui-textbox"   data-options="missingMessage:'银行卡复印件数量为必输项',validType:['positiveIntegerNumer[\'银行卡复印件数量\']','maxLength[8,\'银行卡复印件数量\']']" <c:choose><c:when test="${loanFilesInfo.bankCardFilesCount != null}">value="${loanFilesInfo.bankCardFilesCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="bankCardFilesCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">4</td>
						        <td valign="middle" class="value" colspan="2">签约确认函</td>
						        <td valign="middle" class="value"> 
						       	 <input type="text" class="easyui-textbox"  data-options="missingMessage:'签约确认函数量为必输项',validType:['positiveIntegerNumer[\'签约确认函数量\']','maxLength[8,\'签约确认函数量\']']" <c:choose><c:when test="${loanFilesInfo.serviceFilesCount != null}">value="${loanFilesInfo.serviceFilesCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="serviceFilesCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">5</td>
						        <td valign="middle" class="value" colspan="2">个人信用报告</td>
						        <td valign="middle" class="value"> 
						        	<input type="text" class="easyui-textbox" data-options="missingMessage:'个人信用报告数量为必输项',validType:['positiveIntegerNumer[\'个人信用报告数量\']','maxLength[8,\'个人信用报告数量\']']"  <c:choose><c:when test="${loanFilesInfo.personalCreditReportsCount != null}">value="${loanFilesInfo.personalCreditReportsCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="personalCreditReportsCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">6</td>
						        <td valign="middle" class="value">身份证明</td>
						        <td valign="middle" class="value">
						            <input type="checkbox" value="身份证" <c:if test="${fn:contains(loanFilesInfo.idCertification, '身份证')}">checked="checked"</c:if> id="idCertification1" name="idCertification"/><label for="idCertification1" >身份证</label>
						            <input type="checkbox" value="暂住证" <c:if test="${fn:contains(loanFilesInfo.idCertification, '暂住证')}">checked="checked"</c:if> id="idCertification2" name="idCertification"/><label for="idCertification2" >暂住证</label>
						            <input type="checkbox" value="户口本" <c:if test="${fn:contains(loanFilesInfo.idCertification, '户口本')}">checked="checked"</c:if> id="idCertification3" name="idCertification"/><label for="idCertification3" >户口本</label>
						            <input type="checkbox" value="军官证" <c:if test="${fn:contains(loanFilesInfo.idCertification, '军官证')}">checked="checked"</c:if> id="idCertification4" name="idCertification"/><label for="idCertification4" >军官证</label>
						            <input type="checkbox" value="护照"  <c:if test="${fn:contains(loanFilesInfo.idCertification, '护照')}">checked="checked"</c:if> id="idCertification5" name="idCertification"/><label for="idCertification5" >护照</label>
						            <input type="checkbox" value="结婚证" <c:if test="${fn:contains(loanFilesInfo.idCertification, '结婚证')}">checked="checked"</c:if> id="idCertification6" name="idCertification"/><label for="idCertification6" >结婚证</label>
						            <input type="checkbox" value="其他：" <c:if test="${loanFilesInfo.idCertificationOther != null && fn:trim(loanFilesInfo.idCertificationOther)!=''}">checked="checked"</c:if> id="idCertification7" name="idCertification"/><label for="idCertification7" >其他</label>
						            <input type="text" class="easyui-textbox" data-options="missingMessage:'身份证明其他为必输项',validType:'maxLength[255,\'身份证明其他\']'" <c:choose><c:when test="${loanFilesInfo.idCertificationOther != null && fn:trim(loanFilesInfo.idCertificationOther)!=''}">value="${fn:trim(loanFilesInfo.idCertificationOther)}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="idCertificationOther"/>
						        </td>
						        <td valign="middle" class="value"> 
						      	  <input type="text" class="easyui-textbox" data-options="missingMessage:'身份证明数量为必输项',validType:['positiveIntegerNumer[\'身份证明数量\']','maxLength[8,\'身份证明数量\']']" <c:choose><c:when test="${loanFilesInfo.idCertificationCount != null}">value="${loanFilesInfo.idCertificationCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="idCertificationCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">7</td>
						        <td valign="middle" class="value">收入证明</td>
						        <td valign="middle" class="value">
						            <input type="checkbox" value="收入证明(公章或财务章)" <c:if test="${fn:contains(loanFilesInfo.incomeCertification, '收入证明(公章或财务章)')}">checked="checked"</c:if> id="incomeCertification1" name="incomeCertification"/><label for="incomeCertification1" >收入证明(公章或财务章)</label>
						            <input type="checkbox" value="工资存折" <c:if test="${fn:contains(loanFilesInfo.incomeCertification, '工资存折')}">checked="checked"</c:if> id="incomeCertification2" name="incomeCertification"/><label for="incomeCertification2" >工资存折</label>
						            <input type="checkbox" value="代发工资银行流水" <c:if test="${fn:contains(loanFilesInfo.incomeCertification, '代发工资银行流水')}">checked="checked"</c:if> id="incomeCertification3" name="incomeCertification"/><label for="incomeCertification3" >代发工资银行流</label>
						            <input type="checkbox" value="其他个人银行流水" <c:if test="${fn:contains(loanFilesInfo.incomeCertification, '其他个人银行流水')}">checked="checked"</c:if> id="incomeCertification4" name="incomeCertification"/><label for="incomeCertification4" >其他个人银行流水</label>
						            <input type="checkbox" value="工资单" <c:if test="${fn:contains(loanFilesInfo.incomeCertification, '工资单')}">checked="checked"</c:if> id="incomeCertification5" name="incomeCertification"/><label for="incomeCertification5" >工资单</label>
						            <input type="checkbox" value="其他：" <c:if test="${loanFilesInfo.incomeCertificationOther != null && fn:trim(loanFilesInfo.incomeCertificationOther)!=''}">checked="checked"</c:if> id="incomeCertification6" name="incomeCertification"/><label for="incomeCertification6" >其他</label>
						            <input type="text" class="easyui-textbox" data-options="missingMessage:'收入证明其他为必输项',validType:'maxLength[255,\'收入证明其他\']'" <c:choose><c:when test="${loanFilesInfo.incomeCertificationOther != null && fn:trim(loanFilesInfo.incomeCertificationOther)!=''}">value="${fn:trim(loanFilesInfo.incomeCertificationOther)}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="incomeCertificationOther"/>
						        </td>
						        <td valign="middle" class="value">
						        	<input type="text" class="easyui-textbox"  data-options="missingMessage:'收入证明数量为必输项',validType:['positiveIntegerNumer[\'收入证明数量\']','maxLength[8,\'收入证明数量\']']"  <c:choose><c:when test="${loanFilesInfo.incomeCertificationCount != null}">value="${loanFilesInfo.incomeCertificationCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="incomeCertificationCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">8</td>
						        <td valign="middle" class="value">工作证明</td>
						        <td valign="middle" class="value">
						            <input type="checkbox" value="劳动合同" <c:if test="${fn:contains(loanFilesInfo.workCertification, '劳动合同')}">checked="checked"</c:if> id="workCertification1" name="workCertification"/><label for="workCertification1" >劳动合同</label>
						            <input type="checkbox" value="社保卡" <c:if test="${fn:contains(loanFilesInfo.workCertification, '社保卡')}">checked="checked"</c:if> id="workCertification2" name="workCertification"/><label for="workCertification2" >社保卡</label>
						            <input type="checkbox" value="工作证/工牌/名片" <c:if test="${fn:contains(loanFilesInfo.workCertification, '工作证/工牌/名片')}">checked="checked"</c:if> id="workCertification3" name="workCertification"/><label for="workCertification3" >工作证/工牌/名片</label>
						            <input type="checkbox" value="军官证" <c:if test="${fn:contains(loanFilesInfo.workCertification, '军官证')}">checked="checked"</c:if> id="workCertification4" name="workCertification"/><label for="workCertification4" >军官证</label>
						            <input type="checkbox" value="职业资格证"  <c:if test="${fn:contains(loanFilesInfo.workCertification, '职业资格证')}">checked="checked"</c:if> id="workCertification5" name="workCertification"/><label for="workCertification5" >职业资格证</label>
						            <input type="checkbox" value="其他："  <c:if test="${loanFilesInfo.workCertificationOther != null && fn:trim(loanFilesInfo.workCertificationOther)!=''}">checked="checked"</c:if> id="workCertification6" name="workCertification"/><label for="workCertification6" >其他</label>
						            <input type="text" class="easyui-textbox" data-options="missingMessage:'工作证明其他为必输项',validType:'maxLength[255,\'工作证明其他\']'" <c:choose><c:when test="${loanFilesInfo.workCertificationOther != null && fn:trim(loanFilesInfo.workCertificationOther)!=''}">value="${fn:trim(loanFilesInfo.workCertificationOther)}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="workCertificationOther"/>
						            
						        </td>
						        <td valign="middle" class="value"> 
						        	<input type="text" class="easyui-textbox" data-options="missingMessage:'工作证明数量为必输项',validType:['positiveIntegerNumer[\'工作证明数量\']','maxLength[8,\'工作证明数量\']']" <c:choose><c:when test="${loanFilesInfo.workCertificationCount != null}">value="${loanFilesInfo.workCertificationCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="workCertificationCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">9</td>
						        <td valign="middle" class="value">住址证明</td>
						        <td valign="middle" class="value">
						            <input type="checkbox" value="水电煤、电话、网络、账单/物业管理费单" <c:if test="${fn:contains(loanFilesInfo.addressCertification, '水电煤、电话、网络、账单/物业管理费单')}">checked="checked"</c:if> id="addressCertification1" name="addressCertification"/><label for="addressCertification1" >水电煤、电话、网络、账单/物业管理费单</label>
						            <input type="checkbox" value="租赁合同" <c:if test="${fn:contains(loanFilesInfo.addressCertification, '租赁合同')}">checked="checked"</c:if> id="addressCertification2" name="addressCertification"/><label for="addressCertification2" >租赁合同 </label>
						            <input type="checkbox" value="房租押金收据" <c:if test="${fn:contains(loanFilesInfo.addressCertification, '房租押金收据')}">checked="checked"</c:if> id="addressCertification3" name="addressCertification"/><label for="addressCertification3" >房租押金收据</label>
						            <input type="checkbox" value="信用卡账单" <c:if test="${fn:contains(loanFilesInfo.addressCertification, '信用卡账单')}">checked="checked"</c:if> id="addressCertification4" name="addressCertification"/><label for="addressCertification4" >信用卡账单</label>
						            <input type="checkbox" value="住址证明(单位出具)" <c:if test="${fn:contains(loanFilesInfo.addressCertification, '住址证明(单位出具)')}">checked="checked"</c:if> id="addressCertification5" name="addressCertification"/><label for="addressCertification5" >住址证明(单位出具)</label>
						            <input type="checkbox" value="邮政信件快递单" <c:if test="${fn:contains(loanFilesInfo.addressCertification, '邮政信件快递单')}">checked="checked"</c:if> id="addressCertification6" name="addressCertification"/><label for="addressCertification6" >邮政信件快递单</label>
						            <input type="checkbox" value="其他："  <c:if test="${loanFilesInfo.addressCertificationOther != null && fn:trim(loanFilesInfo.addressCertificationOther)!=''}">checked="checked"</c:if> id="addressCertification7" name="addressCertification"/><label for="addressCertification7" >其他</label>
						            <input type="text" class="easyui-textbox" data-options="missingMessage:'住址证明其他为必输项',validType:'maxLength[255,\'住址证明其他\']'" <c:choose><c:when test="${loanFilesInfo.addressCertificationOther != null && fn:trim(loanFilesInfo.addressCertificationOther)!=''}">value="${fn:trim(loanFilesInfo.addressCertificationOther)}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="addressCertificationOther"/>
						        </td>
						        <td valign="middle" class="value"> 
						        	<input type="text" class="easyui-textbox" data-options="missingMessage:'住址证明数量为必输项',validType:['positiveIntegerNumer[\'住址证明数量\']','maxLength[8,\'住址证明数量\']']" <c:choose><c:when test="${loanFilesInfo.addressCertificationCount != null}">value="${loanFilesInfo.addressCertificationCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="addressCertificationCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">10</td>
						        <td valign="middle" class="value">经营证明</td>
						        <td valign="middle" class="value">
						            <input type="checkbox" value="营业执照" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '营业执照')}">checked="checked"</c:if> id="businessCertification1" name="businessCertification"/><label for="businessCertification1" >营业执照</label>
						            <input type="checkbox" value="机构代码证/税务登记证" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '机构代码证/税务登记证')}">checked="checked"</c:if> id="businessCertification2" name="businessCertification"/><label for="businessCertification2" >机构代码证/税务登记证</label>
						            <input type="checkbox" value="公司账户流水" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '公司账户流水')}">checked="checked"</c:if> id="businessCertification3" name="businessCertification"/><label for="businessCertification3" >公司账户流水</label>
						            <input type="checkbox" value="公司租赁合同及近期租赁发票" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '公司租赁合同及近期租赁发票')}">checked="checked"</c:if> id="businessCertification4" name="businessCertification"/><label for="businessCertification4" >公司租赁合同及近期租赁发票</label>
						            <input type="checkbox" value="订单" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '订单')}">checked="checked"</c:if> id="businessCertification5" name="businessCertification"/><label for="businessCertification5" >订单</label>
						            <input type="checkbox" value="购销合同/协议" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '购销合同/协议')}">checked="checked"</c:if> id="businessCertification6" name="businessCertification"/><label for="businessCertification6" >购销合同/协议</label>
						            <input type="checkbox" value="缴税凭证" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '缴税凭证')}">checked="checked"</c:if> id="businessCertification7" name="businessCertification"/><label for="businessCertification7" >缴税凭证</label>
						            <input type="checkbox" value="公司章程/验证报告" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '公司章程/验证报告')}">checked="checked"</c:if> id="businessCertification8" name="businessCertification"/><label for="businessCertification8" >公司章程/验证报告</label>
						            <input type="checkbox" value="销售小票" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '销售小票')}">checked="checked"</c:if> id="businessCertification9" name="businessCertification"/><label for="businessCertification9" >销售小票</label>
						            <input type="checkbox" value="特殊行业许可证" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '特殊行业许可证')}">checked="checked"</c:if> id="businessCertification10" name="businessCertification"/><label for="businessCertification10" >特殊行业许可证</label>
						            <input type="checkbox" value="财务报表/账本" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '财务报表/账本')}">checked="checked"</c:if> id="businessCertification11" name="businessCertification"/><label for="businessCertification11" >财务报表/账本</label>
						            <input type="checkbox" value="员工工资单" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '员工工资单')}">checked="checked"</c:if> id="businessCertification12" name="businessCertification"/><label for="businessCertification12" >员工工资单</label>
						            <input type="checkbox" value="存货清单" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '存货清单')}">checked="checked"</c:if> id="businessCertification13" name="businessCertification"/><label for="businessCertification13" >存货清单</label>
						            <input type="checkbox" value="应收、应付清单" <c:if test="${fn:contains(loanFilesInfo.businessCertification, '应收、应付清单')}">checked="checked"</c:if> id="businessCertification14" name="businessCertification"/><label for="businessCertification14" >应收、应付清单</label>
						            <input type="checkbox" value="其他："  <c:if test="${loanFilesInfo.businessCertificationOther != null && fn:trim(loanFilesInfo.businessCertificationOther)!=''}">checked="checked"</c:if> id="businessCertification15" name="businessCertification"/><label for="businessCertification15" >其他</label>
						            <input type="text" class="easyui-textbox" data-options="missingMessage:'经营证明其他为必输项',validType:'maxLength[255,\'经营证明其他\']'" <c:choose><c:when test="${loanFilesInfo.businessCertificationOther != null && fn:trim(loanFilesInfo.businessCertificationOther)!=''}">value="${fn:trim(loanFilesInfo.businessCertificationOther)}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="businessCertificationOther"/>
						        </td>
						        <td valign="middle" class="value"> 
						        	<input type="text" class="easyui-textbox" data-options="missingMessage:'经营证明数量为必输项',validType:['positiveIntegerNumer[\'经营证明数量\']','maxLength[8,\'经营证明数量\']']" <c:choose><c:when test="${loanFilesInfo.businessCertificationCount != null}">value="${loanFilesInfo.businessCertificationCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="businessCertificationCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">11</td>
						        <td valign="middle" class="value">资产证明</td>
						        <td valign="middle" class="value">
						            <input type="checkbox" value="房产证/房屋所有权证" <c:if test="${fn:contains(loanFilesInfo.assetCertification, '房产证/房屋所有权证')}">checked="checked"</c:if> id="assetCertification1" name="assetCertification"/><label for="assetCertification1" >房产证/房屋所有权证</label>
						            <input type="checkbox" value="按揭合同/抵押合同" <c:if test="${fn:contains(loanFilesInfo.assetCertification, '按揭合同/抵押合同')}">checked="checked"</c:if> id="assetCertification2" name="assetCertification"/><label for="assetCertification2" >按揭合同/抵押合同</label>
						            <input type="checkbox" value="行驶证/车辆登记证" <c:if test="${fn:contains(loanFilesInfo.assetCertification, '行驶证/车辆登记证')}">checked="checked"</c:if> id="assetCertification3" name="assetCertification"/><label for="assetCertification3" >行驶证/车辆登记证</label>
						            <input type="checkbox" value="放贷流水/银行流水" <c:if test="${fn:contains(loanFilesInfo.assetCertification, '放贷流水/银行流水')}">checked="checked"</c:if> id="assetCertification4" name="assetCertification"/><label for="assetCertification4" >放贷流水/银行流水</label>
						            <input type="checkbox" value="人寿保单" <c:if test="${fn:contains(loanFilesInfo.assetCertification, '人寿保单')}">checked="checked"</c:if> id="assetCertification5" name="assetCertification"/><label for="assetCertification5" >人寿保单</label>
						            <input type="checkbox" value="其他："  <c:if test="${loanFilesInfo.assetCertificationOther != null  && fn:trim(loanFilesInfo.assetCertificationOther)!=''}">checked="checked"</c:if> id="assetCertification6" name="assetCertification"/><label for="assetCertification6" >其他</label>
						            <input type="text" class="easyui-textbox" data-options="missingMessage:'资产证明其他为必输项',validType:'maxLength[255,\'资产证明其他\']'" <c:choose><c:when test="${loanFilesInfo.assetCertificationOther != null  && fn:trim(loanFilesInfo.assetCertificationOther)!=''}">value="${fn:trim(loanFilesInfo.assetCertificationOther)}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="assetCertificationOther"/>
						        </td>
						        <td valign="middle" class="value"> 
						        	<input type="text" class="easyui-textbox" data-options="missingMessage:'资产证明数量为必输项',validType:['positiveIntegerNumer[\'资产证明数量\']','maxLength[8,\'资产证明数量\']']" <c:choose><c:when test="${loanFilesInfo.assetCertificationCount != null}">value="${loanFilesInfo.assetCertificationCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose>  name="assetCertificationCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">12</td>
						        <td valign="middle" class="value">其他证明</td>
						        <td valign="middle" class="value">
						            <input type="checkbox" value="随意贷客户情况说明" <c:if test="${fn:contains(loanFilesInfo.otherCertification, '随意贷客户情况说明')}">checked="checked"</c:if> id="otherCertification1" name="otherCertification"/><label for="otherCertification1" >随意贷客户情况说明</label>
						            <input type="checkbox" value="其他："  <c:if test="${loanFilesInfo.otherCertificationOther != null && fn:trim(loanFilesInfo.otherCertificationOther)!=''}">checked="checked"</c:if> id="otherCertification2" name="otherCertification"/><label for="otherCertification2" >其他</label>
						            <input type="text" class="easyui-textbox" data-options="missingMessage:'其他证明其他为必输项',validType:'maxLength[255,\'其他证明其他\']'" <c:choose><c:when test="${loanFilesInfo.otherCertificationOther != null && fn:trim(loanFilesInfo.otherCertificationOther)!=''}">value="${fn:trim(loanFilesInfo.otherCertificationOther)}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="otherCertificationOther"/>
						        </td>
						        <td valign="middle" class="value"> 
						      	  <input class="easyui-textbox" type="text" data-options="missingMessage:'其他证明数量为必输项',validType:['positiveIntegerNumer[\'其他证明数量\']','maxLength[8,\'其他证明数量\']']" <c:choose><c:when test="${loanFilesInfo.otherCertificationCount != null}">value="${loanFilesInfo.otherCertificationCount}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> name="otherCertificationCount"/>
						        </td>
						    </tr>
						    <tr>
						        <td valign="middle" class="value">13</td>
						        <td valign="middle" class="value">备注</td>
						        <td valign="middle" class="value"> 
						           <input class="easyui-textbox" type="text" data-options="missingMessage:'备注',multiline:true,height:100,width:'100%',validType:'maxLength[255,\'备注\']'" <c:choose><c:when test="${loanFilesInfo.memo != null}">value="${loanFilesInfo.memo}"</c:when><c:otherwise>value=""</c:otherwise></c:choose> data-options="multiline:true,height:100,width:'100%'" name="memo"/>
						        </td>
						        <td valign="middle" class="value"></td>
						    </tr>
					    </tbody>
					</table>
				</form>
			</div>
			<div id="bottonBox" style="width: 90%; height: 30px;text-align: center; ">
				<sec:authorize ifAnyGranted="/loan/loanFilesInfoUpdate">
					<a href="javascript:void(0)" id="submitButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:110px">重建</a>
	   			</sec:authorize>
				<a href="javascript:void(0)" id="resetButton" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:110px">重置</a>
				<a href="javascript:void(0)" id="loanFilesInofListButton" class="easyui-linkbutton" data-options="iconCls:'icon-search'"  style="width:110px">客户档案列表</a>
			</div>
		</div>
	</c:if>
	<c:if test="${resCode != '000000'}">
		<div style="width: 400px; height: auto; margin-left: auto; margin-right: auto; text-align: center;">
			${resMsg}
		</div>
	</c:if>
	</body>
</html>