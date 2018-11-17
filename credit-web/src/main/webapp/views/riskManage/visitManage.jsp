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
<title>回访管理</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                      ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/riskManage/visitManage.js');
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
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 110px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <%-- <input type="hidden" id="loanId" name="loanId" value="${loanId}"/> --%>
            <input type="hidden" id="userCode" name="userCode" value="${user.userCode}"/>
            <table cellpadding="5">
                <tr>
                    <td>姓名：</td>
                    <td><input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'客户姓名\']'" style="width:100px;"/></td>
                    <td>手机：</td>
                    <td><input class="easyui-textbox" id="mobile" name="mobile" validType="mobile" style="width:100px;"/></td>
                    <td>身份证号：</td>
                    <td><input class="easyui-textbox" id="idNum" name="idNum" validType="idCard" style="width:140px;"/></td>
                    <td>合同编号：</td>
                    <td><input class="easyui-textbox" id="contractNum" name="contractNum" style="width:125px;"/></td>
                </tr>
                <tr>
                    <td>放款起始日期：</td>
                    <td><input class="easyui-datebox" id="startDate" name="startDate" style="width:100px"
                                data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/></td>
                    <td>放款截止日期：</td>
                    <td><input class="easyui-datebox" id="endDate" name="endDate" style="width:100px"
                                data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/></td>
                    <td>借款类型：</td>
                    <td>
                        <select class="easyui-combobox" id="loanType" name="loanType" style="width:140px;" data-options="editable:false,panelHeight:'auto'">
                            <option value="">请选择</option>
                            <c:forEach var="loanType" items="${loanTypes}">
                                <option value="${loanType}">${loanType}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    
    <!-- DataGrid 工具栏按钮 -->
    <div id="tb" style="padding:3px;height:30px;">
        <div style="margin-bottom:0px">
            <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
            <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>
        </div>
    </div>
    
    <div class="easyui-panel" data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="visitManageDataGrid"></table>
    </div>
    
    <div data-options="region:'south'" title="客户回访" style="height:350px;">
        <div class="easyui-accordion" id="accordion" data-options="multiple:true" style="width:100%;">
            <c:if test="${isCanSaveCustomerVisitInfo eq 'true'}">
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
            <div title="客户信息" data-options="iconCls:'icon-man'">
                <table cellpadding="3"  width="100%" border="0" rules="rows" style="table-layout:fixed;">
                    <tr>
                        <td>借款人：</td>
                        <td><label id="visitBorrowerName"></label></td>
                        <td>放款营业部：</td>
                        <td><label id="visitSignsalesdepName"></label></td>
                        <td>借款类型：</td>
                        <td><label id="visitLoanType"></label></td>
                        <td>客户经理：</td>
                        <td><label id="visitSalesmanName"></label></td>
                    </tr>
                    <tr>
                        <td>申请日期：</td>
                        <td><label id="visitRequestDate"></label></td>
                        <td>申请金额：</td>
                        <td><label id="visitRequestMoney"></label></td>
                        <td>申请期限：</td>
                        <td><label id="visitRequestTime"></label></td>
                        <td>签约日期：</td>
                        <td><label id="visitSignDate"></label></td>
                    </tr>
                    <tr>
                        <td>放款日期：</td>
                        <td><label id="visitGrantMoneyDate"></label></td>
                        <td>审批金额：</td>
                        <td><label id="visitMoney"></label></td>
                        <td>借款期限：</td>
                        <td><label id="visitTime"></label></td>
                        <td>管理营业部：</td>
                        <td><label id="visitSalesdepartmentName"></label></td>
                    </tr>
                </table>
            </div>
            <div title="回访信息" data-options="iconCls:'pic_57'">
                <form id="submitForm">
                    <input type="hidden" id="loanId" name="loanId"/>
                    <input type="hidden" id="signsalesdepCode" name="signsalesdepCode"/>
                    <table cellpadding="1">
                        <tr>
                            <td>拨号方式：</td>
                            <td>
                                <input type="checkbox" id="dialMode" name="dialMode" value="0"/><label>是否输入拨号</label>
                            </td>
                            <td>电话：</td>
                            <td>
                                <span id ="labelTel">
                                    <select class="easyui-combobox" id="tel" name="tel" style="width:120px;" 
                                        data-options="editable:false,panelHeight:'auto',valueField:'content',textField:'content'">
                                    </select>
                                </span>
                                <span id ="labelPhone" style="display:none">
                                    <input class="easyui-textbox" id="phone" name="phone" style="width:120px;"/>
                                </span>
                                <c:if test="${isCanSaveCustomerVisitInfo eq 'true'}">
                                    <a href="#" id="dial" name="dial" class="easyui-linkbutton" data-options="iconCls:'pic_58'" plain="false">拨号</a>
                                </c:if>
                            </td>
                            <%-- <td>回访日期：</td>
                            <td><label><fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd HH:mm" /></label></td> --%>
                            <td>渠道：</td>
                            <td>
                                <select class="easyui-combobox" id="channel" name="channel" style="width:100px;" data-options="editable:false,panelHeight:'auto'">
                                    <!-- <option value="">请选择</option> -->
                                    <option value="朋友介绍">朋友介绍</option>
                                    <option value="销售电话">销售电话</option>
                                    <option value="手机短信">手机短信</option>
                                    <option value="网络广告">网络广告</option>
                                    <option value="平面广告">平面广告</option>
                                    <option value="其它">其它</option>
                                </select>
                            </td>
                            <td>服务态度：</td>
                            <td>
                                <select class="easyui-combobox" id="sAttitude" name="sAttitude" style="width:100px;" data-options="editable:false,panelHeight:'auto'">
                                    <!-- <option value="">请选择</option> -->
                                    <option value="非常满意">非常满意</option>
                                    <option value="满意">满意</option>
                                    <option value="一般">一般</option>
                                    <option value="不满意">不满意</option>
                                    <option value="其它">其它</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>额外费用：</td>
                            <td>
                                <input type="checkbox" id="additionalCharges" name="additionalCharges" value="t"/>
                            </td>
                            <td>建议：</td>
                            <td colspan="5">
                                <input class="easyui-textbox" id="advice" name="advice" style="width:450px;" data-options="validType:'maxLength[512,\'建议\']'"/>
                            </td>
                        </tr>
                        <tr>
                            <td>备注：</td>
                            <td colspan="5"><textarea id="memo" name="memo" rows="2" cols="80"></textarea></td>
                            <td colspan="2">
                                <c:if test="${isCanSaveCustomerVisitInfo eq 'true'}">
                                    &nbsp;&nbsp;&nbsp;
                                    <a href="#" class="easyui-linkbutton" id="submitBtn" name="submitBtn" data-options="iconCls:'icon-ok'">保存</a>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                    <%-- <div data-options="fit:true" align="left" class="editContentPanel" style="padding:3px;">
                        <a href="#" class="easyui-linkbutton" id="closeBtn" name="closeBtn" data-options="iconCls:'icon-cancel'">关闭</a>
                        <c:if test="${isCanSaveCustomerVisitInfo eq 'true'}">
                            <a href="#" class="easyui-linkbutton" id="submitBtn" name="submitBtn" data-options="iconCls:'icon-ok'">保存</a>
                        </c:if>
                    </div> --%>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
