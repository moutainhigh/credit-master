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
		<title>查看-客户详细资料信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			importPluginsExt(['tabs','window','panel','layout','form'],'business', function() {	
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/person/editPersonDetail.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						$("body").css('overflowX','hidden');
					});
				})
			});
		</script>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="margin:0px;">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<form id="personDetailForm">
		<input id="tmpCurDate" type="hidden" value="<fmt:formatDate value="<%=new java.util.Date() %>"  pattern="yyyy-MM-dd"  />" ></input>
		<c:if test="${empty personInfo}" >
			无数据
		</c:if>
		<c:if test="${!empty personInfo}" >
		<input type="hidden" name="id" id="personId" value="${personInfo.id }" />
			<div data-options="" style="margin:0px;" align="center">
				<div class="easyui-accordion"  id="accordion" data-options="multiple:true" style="width:99%" data-options=""  >
					<div title="个人信息" data-options="iconCls:'icon-man'" >
						<table cellpadding="10"  width="100%" border="0" rules="rows"  class="editContentPanel" style="table-layout:fixed;">
							<tr>
								<td width="160" style="word-break:break-all" class="required">姓名：</td>
								<td><input class="easyui-textbox" type="text" name="name"  value="${personInfo.name }" data-options="required:true"  validType="maxLength[10,' ']"></input></td>
								<td width="160" style="word-break:break-all" class="required">性别：</td>
								<td>
									<input value="${personInfo.sex }" class="easyui-combobox" id="sex" name="sex" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
								</td>
								<td width="170" style="word-break:break-all" class="required">身份证号：</td>
								<td>
									<input class="easyui-textbox" type="text" name="idnum" id="idnum"  value="${personInfo.idnum }" data-options="required:true" validType="idCard" ></input>
								</td>
							</tr>
							<tr>
								<td class="required">婚姻状况：</td>
								<td>
									<input value="${personInfo.married }" class="easyui-combobox" id="married" name="married" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
								</td>
								<td class="required">最高学历：</td>
								<td>
									<input value="${personInfo.edLevel }" class="easyui-combobox" id="edLevel" name="edLevel" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
								</td>
								<td class="required">子女数目：</td>
								<td>
									<input class="easyui-numberbox"  name="childrenCount"  value="${personInfo.childrenCount }" data-options="required:true"  validType="maxLength[2,' ']"></input>
								</td>
							</tr>
							<tr>
								<td class="required">户籍地址(需与身份证一致)：</td>
								<td colspan="3">
									<input class="easyui-textbox" type="text" name="hrAddress"  value="${personInfo.hrAddress }" data-options="required:true" style="width:350px" validType="maxLength[50,' ']"></input>
								</td>
								<td>邮编：</td>
								<td>
									<input class="easyui-numberbox"  name="hrPostcode"  value="${personInfo.hrPostcode }" data-options="" validType="postCode"  ></input>
								</td>
							</tr>
							<tr>
								<td class="required">现居住地址：</td>
								<td colspan="3">
									<input class="easyui-textbox" type="text" name="address"  value="${personInfo.address }" data-options="required:true" style="width:350px" validType="maxLength[50,' ']"></input>
								</td>
								<td>邮编：</td>
								<td>
									<input class="easyui-numberbox"  name="postcode"  value="${personInfo.postcode }" data-options="" validType="postCode"  ></input>
								</td>
							</tr>
							<tr>
								<td class="required">手机：</td>
								<td>
									<input class="easyui-textbox" type="text" name="mphone"  value="${personInfo.mphone }" data-options="required:true" validType="mobile" missingMessage="该输入项为必输项"></input>
								</td>
								<td>现住宅电话：</td>
								<td>
									<input class="easyui-textbox" type="text" name="tel"  value="${personInfo.tel }" data-options="" validType="tel"></input>
								</td>
								<td>QQ：</td>
								<td>
									<input class="easyui-numberbox"  name="qq"  value="${personInfo.qq }" data-options=""  validType="maxLength[15,' ']"></input>
								</td>
							</tr>
							<tr>
								<td>邮箱：</td>
								<td>
									<input class="easyui-textbox" type="text" name="email"  value="${personInfo.email }"  data-options="validType:['email','maxLength[25,\'\']']"></input>
								</td>
								<td>优先联系地址：</td>
								<td>
									<input value="${personInfo.addressPriority }" class="easyui-combobox" id="addressPriority" name="addressPriority" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
								</td>
								<td>信用卡：</td>
								<td>
									<c:choose>
										<c:when test="${personInfo.hasCreditCard == 't'}">
											<input type="checkbox" checked="checked" value="t" name="hasCreditCard" />
										</c:when>
										<c:otherwise>
											<input type="checkbox" value="t" name="hasCreditCard" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td>贷款：</td>
								<td>
									<label>
										<c:choose>
											<c:when test="${personInfo.hasLoan == 't'}">
												<input type="checkbox" checked="checked" value="t" name="hasLoan" />
											</c:when>
											<c:otherwise>
												<input type="checkbox" value="t" name="hasLoan" />
											</c:otherwise>
										</c:choose>
									</label>
								</td>
								<td class="required">单张信用卡最高额度（元）：</td>
								<td>
									<input class="easyui-numberbox"  name="cardMaxAmount" precision="2" value="${personInfo.cardMaxAmount }" data-options="required:true"  validType="numberRangeValid[1,999999]"></input>
								</td>
								<td class="required">每月家庭支出（元）：</td>
								<td>
									<input class="easyui-numberbox"  name="familyExpense" precision="2" value="${personInfo.familyExpense }" data-options="required:true"  validType="numberRangeValid[1,999999]"></input>
								</td>
							</tr>
							<tr>
								<td>管理营业部：</td>
								<td><label>${personInfo.comOrganization.name }</label></td>
								<td>备注：</td>
								<td colspan="3">
									<input class="easyui-textbox" type="text" name="memo"  value="${personInfo.memo }" data-options="" style="width:99%" validType="maxLength[500,'']"></input>
								</td>
							</tr>
						</table>
					</div>
					<div title="房产信息" data-options="iconCls:'icon-man'" >
						<c:if test="${fn:length(houses) <= 0}">
							暂无数据
						</c:if>
						<c:if test="${fn:length(houses) > 0}">
							<input type="hidden" name="personHouseInfo.id" value="${houses[0].id }" />
							<table cellpadding="10"  width="100%" border="0" rules="rows" style="table-layout:fixed;" class="editContentPanel">
								<tr>
									<td width="160">房产类型：</td>
									<td>
										<input value="${personInfo.houseType }" class="easyui-combobox" id="houseType" name="houseType" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
									</td>
									<td width="160">房贷：</td>
									<td>
										<label>
											<c:choose>
												<c:when test="${houses[0].hasLoan == 't'}">
													<input type="checkbox" checked="checked" value="t" name="personHouseInfo.hasLoan" />
												</c:when>
												<c:otherwise>
													<input type="checkbox" value="t" name="personHouseInfo.hasLoan" />
												</c:otherwise>
											</c:choose>
										</label>
									</td>
									<td width="170">购买时间：</td>
									<td>
										<input class="easyui-datebox" name="personHouseInfo.buyTime"  value="<fmt:formatDate value="${houses[0].buyTime }"  pattern="yyyy-MM-dd"  />" 
													 editable="true" data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#tmpCurDate\',\'当天\']']" ></input>
									</td>
								</tr>
								<tr>
									<td>购买价格（元）：</td>
									<td>
										<input class="easyui-numberbox"  name="personHouseInfo.price" precision="2" value="${houses[0].price }"  validType="numberRangeValid[1,999999]"></input>
									</td>
									<td>建筑面积（平米）：</td>
									<td>
										<input class="easyui-numberbox"  name="personHouseInfo.buildingArea" precision="2" value="${houses[0].buildingArea }" validType="numberRangeValid[1,1000]" ></input>
									</td>
									<td>产权比例（%）：</td>
									<td>
										<input class="easyui-numberbox"  name="personHouseInfo.propertyProportion" precision="2" value="${houses[0].propertyProportion }"  validType="numberRangeValid[0.01,100]"></input>
									</td>
								</tr>
								<tr>
									<td>贷款年限：</td>
									<td>
										<input class="easyui-numberbox"  name="personHouseInfo.loanPeriod" value="${houses[0].loanPeriod }"  validType="numberRangeValid[1,999]" ></input>
									</td>
									<td>月供（元）：</td>
									<td colspan="3">
										<input class="easyui-numberbox"  name="personHouseInfo.monthlyPayment"  precision="2" value="${houses[0].monthlyPayment }"  validType="numberRangeValid[1,999999]" ></input>
									</td>
								</tr>
								<tr>
									<td>房产地址：</td>
									<td colspan="3">
										<input class="easyui-textbox" type="text" name="personHouseInfo.address"  value="${houses[0].address }" data-options="" style="width:350px" validType="maxLength[50,'']"></input>
									</td>
									<td>邮编：</td>
									<td>
										<input class="easyui-numberbox"  name="personHouseInfo.postcode"  value="${houses[0].postcode }" data-options="" validType="'postCode" ></input>
									</td>
								</tr>
							</table>
						</c:if>
					</div>
					<div title="车辆信息" data-options="iconCls:'icon-man'" >
						<c:if test="${fn:length(cars) <= 0}">
							暂无数据
						</c:if>
						<c:if test="${fn:length(cars) > 0}">
							<input type="hidden" name="personCarInfo.id" value="${cars[0].id }" />
							<table cellpadding="10"  width="100%" border="0" rules="rows" style="table-layout:fixed;" class="editContentPanel">
								<tr>
									<td width="160">购车类型：</td>
									<td>
										<input value="${cars[0].carType }" class="easyui-combobox" id="personCarInfo_carType" name="personCarInfo.carType" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
									</td>
									<td width="160">车辆购买时间：</td>
									<td>
										<input class="easyui-datebox" name="personCarInfo.buyTime" value="<fmt:formatDate value="${cars[0].buyTime }"  pattern="yyyy-MM-dd"  />" 
												editable="true" data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#tmpCurDate\',\'当天\']']" ></input>
									</td>
									<td width="170">	购车价格（不含车辆购置税）：</td>
									<td>
										<input class="easyui-numberbox"  name="personCarInfo.price"  precision="2" value="${cars[0].price }"  validType="numberRangeValid[1,999999]"></input>
									</td>
								</tr>
								<tr>
									<td>车贷：</td>
									<td>
										<label>
											<c:choose>
												<c:when test="${cars[0].hasLoan == 't'}">
													<input type="checkbox" checked="checked" value="t" name="personCarInfo.hasLoan" />
												</c:when>
												<c:otherwise>
													<input type="checkbox" value="t" name="personCarInfo.hasLoan" />
												</c:otherwise>
											</c:choose>
										</label>
									</td>
									<td>贷款期限：</td>
									<td>
										<input class="easyui-numberbox"  name="personCarInfo.loanPeriod"  value="${cars[0].loanPeriod }"  validType="numberRangeValid[1,999]" ></input>
									</td>
									<td>月供金额（元）：</td>
									<td>
										<input class="easyui-numberbox"  name="personCarInfo.monthlyPayment"  precision="2" value="${cars[0].monthlyPayment }"  validType="numberRangeValid[1,999999]"></input>
									</td>
								</tr>
							</table>
						</c:if>
					</div>
					<div title="职业信息" data-options="iconCls:'icon-man'" >
						<table cellpadding="10"  width="100%" border="0" rules="rows" style="table-layout:fixed;" class="editContentPanel">
							<tr>
								<td width="160" class="required">学校/公司名称：</td>
								<td>
									<input class="easyui-textbox" type="text" name="company"  value="${personInfo.company }" data-options="required:true" style="width:250px" validType="maxLength[50,'']"></input>
								</td>
								<td width="160" class="required">单位性质：</td>
								<td>
									<input value="${personInfo.cType }" class="easyui-combobox" id="cType" name="cType" data-options="valueField:'id',textField:'text','editable':false,required:true,panelHeight:'auto'"/>
								</td>
								<td width="170" class="required">所属行业：</td>
								<td>
									<input value="${personInfo.industryType }" class="easyui-combobox" id="industryType" name="industryType" data-options="valueField:'id',textField:'text','editable':false,required:true,panelHeight:300"/>
								</td>
							</tr>
							<tr>
								<td class="required">所属部门/专业：</td>
								<td>
									<input class="easyui-textbox" type="text" name="department"  value="${personInfo.department }" data-options="required:true" style="width:250px" validType="maxLength[30,'']"></input>
								</td>
								<td class="required">职位级别：</td>
								<td>
									<input value="${personInfo.officialRank }" class="easyui-combobox" id="officialRank" name="officialRank" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
								</td>
								<td class="required">学校/公司固定电话：</td>
								<td>
									<input class="easyui-textbox" type="text" name="cTel"  value="${personInfo.cTel }" data-options="required:true" validType="tel" missingMessage="该输入项为必输项"></input>
								</td>
							</tr>
							<tr>
								<td class="required">学校/单位地址：</td>
								<td>
									<input class="easyui-textbox" type="text" name="cAddress"  value="${personInfo.cAddress }" data-options="required:true" style="width:250px" validType="maxLength[50,'']"></input>
								</td>
								<td>邮编：</td>
								<td>
									<input class="easyui-numberbox"  name="cCode"  value="${personInfo.cCode }" data-options="" validType="'postCode" ></input>
								</td>
								<td class="required">入职时间：</td>
								<td>
									<input class="easyui-datebox" name="cEnterTime" value="<fmt:formatDate value="${personInfo.cEnterTime }"  pattern="yyyy-MM-dd"  />"
											editable="true"  required="true" data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#tmpCurDate\',\'当天\']']" ></input>
								</td>
							</tr>
							<tr>
								<td class="required">发薪方式：</td>
								<td>
									<input value="${personInfo.payType }" class="easyui-combobox" id="payType" name="payType" data-options="valueField:'id',textField:'text','editable':false,required:true,panelHeight:'auto'"/>
								</td>
								<td class="required">月发薪日：</td>
								<td>
									<input class="easyui-numberbox"  name="payDay"  value="${personInfo.payDay }" data-options="required:true"  validType="numberRangeValid[1,31]"></input>
								</td>
								<td class="required">月基本薪金（元）：</td>
								<td>
									<input class="easyui-numberbox"  name="wage"  value="${personInfo.wage }" precision="2" data-options="required:true"  validType="numberRangeValid[1,999999]"></input>
								</td>
							</tr>
							<tr>
								<td class="required">其他收入（元）：</td>
								<td>
									<input class="easyui-numberbox"  name="otherMonthlyIncome"  value="${personInfo.otherMonthlyIncome }" precision="2" data-options="required:true"  validType="numberRangeValid[1,999999]"></input>
								</td>
								<td class="required">每月总收入（元）：</td>
								<td>
									<input class="easyui-numberbox"  name="totalMonthlyIncome"  value="${personInfo.totalMonthlyIncome }" precision="2" data-options="required:true"  validType="numberRangeValid[1,999999]"></input>
								</td>
								<td class="required">职业类型：</td>
								<td>
									<input value="${personInfo.profession }" class="easyui-combobox" id="profession" name="profession" data-options="valueField:'id',textField:'text','editable':false,required:true,panelHeight:'auto'"/>
								</td>
							</tr>
						</table>
						
					</div>
					<div title="企业信息" data-options="iconCls:'icon-man'" >
						<c:if test="${fn:length(entrepreneurs) <= 0}">
							暂无数据
						</c:if>
						<c:if test="${fn:length(entrepreneurs) > 0}">
							<input type="hidden" name="personEntrepreneurInfo.id" value="${entrepreneurs[0].id }" />
							<table cellpadding="10"  width="100%" border="0" rules="rows" style="table-layout:fixed;" class="editContentPanel">
								<tr>
									<td width="160" style="text-align:left;">私营企业类型：</td>
									<td>
										<input value="${entrepreneurs[0].enterpriseType }" class="easyui-combobox" id="personEntrepreneurInfo_enterpriseType" name="personEntrepreneurInfo.enterpriseType" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
									</td>
									<td width="160">&nbsp;</td>
									<td><label>&nbsp;</label></td>
									<td width="170">&nbsp;</td>
									<td><label>&nbsp;</label></td>
								</tr>
								<tr>
									<td>成立时间：</td>
									<td>
										<input class="easyui-datebox" name="personEntrepreneurInfo.timeFounded" value="${entrepreneurs[0].timeFounded }"
													editable="true"  data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#tmpCurDate\',\'当天\']']" ></input> 
									</td>
									<td>经营场所：</td>
									<td>
										<input value="${entrepreneurs[0].premisesType }" class="easyui-combobox" id="personEntrepreneurInfo_premisesType" name="personEntrepreneurInfo.premisesType" data-options="valueField:'id',textField:'text','editable':false,panelHeight:'auto'"/>
									</td>
									<td>员工人数：</td>
									<td>
										<input class="easyui-numberbox"  name="personEntrepreneurInfo.employeeAmount"  value="${entrepreneurs[0].employeeAmount }" validType="numberRangeValid[1,9999]" ></input>
									</td>
								</tr>
								<tr>
									<td>占股比例（%）：</td>
									<td>
										<input class="easyui-numberbox"  name="personEntrepreneurInfo.shareholdingRatio"  value="${entrepreneurs[0].shareholdingRatio }" precision="2"  validType="numberRangeValid[0.01,100]" ></input>
									</td>
									<td>月净利润额（万）：</td>
									<td>
										<input class="easyui-numberbox"  name="personEntrepreneurInfo.monthlyNetProfit"  value="${entrepreneurs[0].monthlyNetProfit }" precision="2"  validType="numberRangeValid[1,999999]" ></input>
									</td>
									<td>企业净利润率（%）：</td>
									<td>
										<input class="easyui-numberbox"  name="personEntrepreneurInfo.profitMargin"  value="${entrepreneurs[0].profitMargin }" precision="2"  validType="numberRangeValid[0.01,100]"></input>
									</td>
								</tr>
							</table>
						</c:if>
					</div>
				</div>
			</div>
		</c:if>
		<div data-options="fit:true" align="center" class="editContentPanel" style="padding:20px;">
			<a href="javascript:void(0)" id="submitBut" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
		</div>
		</form>
	</body>
</html>