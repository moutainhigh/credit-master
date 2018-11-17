<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>保存客户回访</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                      ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/riskManage/editCustomerVisit.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <input type="hidden" id="sysDate" name="sysDate" value='<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>' />
    <input type="hidden" id="hidTel" name="hidTel" value="${personVisit.tel}"/>
    <input type="hidden" id="hidChannel" name="hidChannel" value="${personVisit.channel}"/>
    <input type="hidden" id="hidSAttitude" name="hidSAttitude" value="${personVisit.sAttitude}"/>
    <input type="hidden" id="signsalesdepCode" name="signsalesdepCode" value="${loan.signsalesdepCode}"/>
    <input type="hidden" id="userCode" name="userCode" value="${user.userCode}"/>
    <div style="padding:5px;">
        <div class="easyui-accordion" id="accordion" data-options="multiple:true" style="width:100%;">
            <div title="客户信息" data-options="iconCls:'icon-man'" style="">
                <table cellpadding="5"  width="100%" border="0" rules="rows"  style="table-layout:fixed;">
                    <tr>
                        <td width="150" style="word-break:break-all">借款人：</td>
                        <td><label>${loan.borrowerName}</label></td>
                        <td width="150" style="word-break:break-all">放款营业部：</td>
                        <td><label>${loan.signsalesdepName}</label></td>
                        <td width="150" style="word-break:break-all">借款类型：</td>
                        <td><label>${loan.loanType}</label></td>
                        <td width="150" style="word-break:break-all">客户经理：</td>
                        <td><label>${loan.salesmanName}</label></td>
                    </tr>
                    <tr>
                        <td>申请日期：</td>
                        <td><label><fmt:formatDate value="${loan.requestDate}" pattern="yyyy-MM-dd" /></label></td>
                        <td>申请金额：</td>
                        <td><label><fmt:formatNumber value="${loan.requestMoney}" type="currency" minFractionDigits="0" maxFractionDigits="2"/></label></td>
                        <td>申请期限：</td>
                        <td><label>${loan.requestTime}</label></td>
                        <td>签约日期：</td>
                        <td><label><fmt:formatDate value="${loan.signDate}" pattern="yyyy-MM-dd" /></label></td>
                    </tr>
                    <tr>
                        <td>放款日期：</td>
                        <td><label><fmt:formatDate value="${loan.grantMoneyDate}" pattern="yyyy-MM-dd" /></label></td>
                        <td>审批金额：</td>
                        <td><label><fmt:formatNumber value="${loan.money}" type="currency" minFractionDigits="0" maxFractionDigits="2"/></label></td>
                        <td>借款期限：</td>
                        <td><label>${loan.time}</label></td>
                        <td>管理营业部：</td>
                        <td><label>${loan.salesdepartmentName}</label></td>
                    </tr>
                </table>
            </div>
            <c:if test="${user.employeeType ne '系统管理员'}">
                <span style="color:red">如需使用软电话服务，请使用谷歌浏览器40.0以下的版本。</span><br/>
                <object id="csSoftPhone"
                        type="application/x-itst-activex"
                        clsid="{A972798F-50FC-4818-BCE2-2472BC68766C}"
                        event_evtLogonSucc="OnLogonSucc" 
                        event_evtStateChange="OnevtStateChange"
                        event_evtCallArrive="OnevtCallArrive"
                        style="width: 100%; height: 65pt">
                </object>
            </c:if>
            <div title="回访信息" data-options="iconCls:'pic_57'">
                <form id="submitForm">
                    <input type="hidden" id="loanId" name="loanId" value="${loan.loanId}"/>
                    <table cellpadding="5">
                        <tr>
                            <td>电话：</td>
                            <td>
                                <select class="easyui-combobox" id="tel" name="tel" style="width:120px;" data-options="editable:false,panelHeight:'auto'">
                                    <option value="">请选择</option>
                                    <c:forEach var="personTelInfo" items="${personTelInfos}">
                                        <option value="${personTelInfo.content}">${personTelInfo.content}</option>
                                    </c:forEach>
                                </select>
                                <c:if test="${user.employeeType ne '系统管理员'}">
                                    <a href="javascript:void(0)" id="dial" name="dial" class="easyui-linkbutton" data-options="iconCls:'pic_58'" plain="false">拨号</a>
                                </c:if>
                            </td>
                            <td>回访日期：</td>
                            <td><label><fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd HH:mm" /></label></td>
                            <td>渠道：</td>
                            <td>
                                <select class="easyui-combobox" id="channel" name="channel" style="width:120px;" data-options="editable:false,panelHeight:'auto'">
                                    <option value="">请选择</option>
                                    <c:forEach var="channel" items="${channels}">
                                        <option value="${channel}">${channel}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>服务态度：</td>
                            <td>
                                <select class="easyui-combobox" id="sAttitude" name="sAttitude" style="width:120px;" data-options="editable:false,panelHeight:'auto'">
                                    <option value="">请选择</option>
                                    <c:forEach var="sAttitude" items="${sAttitudes}">
                                        <option value="${sAttitude}">${sAttitude}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>额外费用：</td>
                            <td>
                                <input type="checkbox" id="additionalCharges" name="additionalCharges" value="t"
                                    <c:if test="${personVisit.additionalCharges eq 't'}">checked='checked'</c:if> />
                            </td>
                            <td>建议：</td>
                            <td colspan="5">
                                <input class="easyui-textbox" id="advice" name="advice" value="${personVisit.advice}" style="width:450px;" 
                                        data-options="validType:'maxLength[512,\'建议\']'" />
                                </td>
                        </tr>
                        <tr>
                            <td rowspan="3" >备注：</td>
                            <td colspan="7"><textarea id="memo" name="memo" rows="3" cols="100">${personVisit.memo}</textarea></td>
                        </tr>
                    </table>
                    <div data-options="fit:true" align="left" class="editContentPanel" style="padding:10px;">
                        <!-- <a href="#" class="easyui-linkbutton" id="backBtn" iconCls="icon-back" plain="false" >返回</a> -->
                        <sec:authorize ifAnyGranted="/riskManage/saveCustomerVisitInfo">
                            <a href="#" class="easyui-linkbutton" id="submitBtn" name="submitBtn"  data-options="iconCls:'icon-ok'">保存</a>
                        </sec:authorize>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
