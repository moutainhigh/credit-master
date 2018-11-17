<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>代扣管理</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css" rel="stylesheet"
    href="<%=request.getContextPath() %>/resources/css/sysIcon.css" />
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
            urlJs.push(global.contextPath + '/resources/js/repay/debitQueueManagementPage.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
            });
        })
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 180px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <input type="hidden" id="currDate" name="currDate" value="${currDate}" />
            <input type="hidden" id="currDate2" name="currDate2" value="${currDate2}" />
            <table cellpadding="5">
                <tr>
                    <td>客户姓名：</td>
                    <td>
                        <input class="easyui-textbox" type="text" name="custName" id="custName" style="width: 180px;" />
                    </td>
                    <td>证件号码：</td>
                    <td>
                        <input class="easyui-textbox" type="text" name="idNum" id="idNum" style="width: 220px;" />
                    </td>
                    <td>合同编号：</td>
                    <td>
                        <input class="easyui-textbox" type="text" name="contractNum" id="contractNum" style="width: 180px;" />
                    </td>
                </tr>
                <tr>
                    <td>划扣状态：</td>
                    <td>
                        <select class="easyui-combobox" id='debitResultState' name='debitResultState'
                            data-options="editable: false, panelHeight:'auto'" style="width: 180px;">
                            <option value="">请选择</option>
                            <c:forEach var="debitResultState" items="${debitResultStates}">
                                <option value="${debitResultState.code}">${debitResultState.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>通知状态：</td>
                    <td>
                        <select class="easyui-combobox" id='debitNotifyState' name='debitNotifyState'
                            data-options="editable: false, panelHeight:'auto'" style="width: 220px;">
                            <option value="">请选择</option>
                            <c:forEach var="debitNotifyState" items="${debitNotifyStates}">
                                <option value="${debitNotifyState.code}">${debitNotifyState.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>还款类型：</td>
                    <td>
                        <select class="easyui-combobox" id='repayType' name='repayType'
                            data-options="editable: false, panelHeight:'auto'" style="width: 180px;">
                            <option value="">请选择</option>
                            <c:forEach var="repayType" items="${repayTypes}">
                                <option value="${repayType.code}">${repayType.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>扣款帐户：</td>
                    <td>
                        <select class="easyui-combobox" id='payParty' name='payParty' 
                            data-options="editable: false, panelHeight:'auto'" style="width: 180px;">
                            <option value="">请选择</option>
                            <c:forEach var="payParty" items="${payPartys}">
                                <option value="${payParty.code}">${payParty.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>划扣时间：</td>
                    <td>
                        <input class="easyui-datebox" id="deductStartDate" name="deductStartDate" style="width: 100px" /> ~ 
                        <input class="easyui-datebox" id="deductEndDate" name="deductEndDate" style="width: 100px" />
                    </td>
                    <td>批次号：</td>
                    <td>
                        <input class="easyui-textbox" name="batchId" id="batchId" style="width: 180px;" />
                    </td>
                </tr>
                <tr>
                    <td>序号：</td>
                    <td>
                        <input class="easyui-textbox" name="debitNo" id="debitNo" style="width: 180px;" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="easyui-panel" style="padding: 0px; margin: 0px;" data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="debitQueueDataGrid" data-options=""></table>

        <div id="tb" style="padding: 3px;">
            <div style="margin-bottom: 0px">
                <a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true" style="width: 70px;">查询</a> 
                <a href="#" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width: 70px;">重置</a>
                <a href="#" class="easyui-linkbutton" id="debitResend" iconCls="pic_33" plain="true">代扣重发</a> 
                <a href="#" class="easyui-linkbutton" id="oneKeyResend" iconCls="pic_33" plain="true">一键重发</a> 
                <a href="#" class="easyui-linkbutton" id="marginPay" iconCls="pic_33" plain="true">保证金代垫（回购）</a>
                <a href="#" class="easyui-linkbutton" id="editDataBtn" iconCls="pic_36" plain="true">编辑</a>
            </div>
        </div>
    </div>
    
    <!-- 划扣队列数据修改面板 -->
    <div id="updateDataPanel" class="easyui-window" title="更新" style="padding:20px;" 
        data-options="modal: true, collapsible: false, minimizable: false, maximizable: false, closable: true, closed: true, draggable: true, shadow: true, iconCls: 'icon-save'">
        <form method="post" id="dataForm">
            <!-- 分账队列ID -->
            <input type="hidden" id="debitId" name="debitId" />
            <table cellpadding="5">
                <tr>
                    <td>合同编号：</td>
                    <td>
                        <input class="easyui-textbox" id="u_contractNum" name="contractNum" data-options="required:true,validType:'maxLength[50,\'合同编号\']'" style="width:250px;"/>
                    </td>
                </tr>
                <tr>
                    <td>交易流水：</td>
                    <td>
                        <input class="easyui-textbox" id="u_tradeNo" name="tradeNo" data-options="validType:'maxLength[50,\'交易流水号\']'" style="width:250px;"/>
                    </td>
                </tr>
                <tr>
                    <td>通知状态：</td>
                    <td>
                        <select class="easyui-combobox" id='u_debitNotifyState' name='debitNotifyState' 
                            data-options="editable: false, panelHeight:'auto'" style="width: 250px;">
                            <c:forEach var="debitNotifyState" items="${debitNotifyStates}">
                                <option value="${debitNotifyState.code}">${debitNotifyState.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>划扣状态：</td>
                    <td>
                        <select class="easyui-combobox" id='u_debitResultState' name='debitResultState'
                            data-options="editable: false, panelHeight:'auto'" style="width: 250px;">
                            <c:forEach var="debitResultState" items="${debitResultStates}">
                                <option value="${debitResultState.code}">${debitResultState.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>还款类型：</td>
                    <td>
                        <select class="easyui-combobox" id='u_repayType' name='repayType'
                            data-options="editable: false, panelHeight:'auto'" style="width: 250px;">
                            <c:forEach var="repayType" items="${repayTypes}">
                                <option value="${repayType.code}">${repayType.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>扣款账户：</td>
                    <td>
                        <select class="easyui-combobox" id='u_payParty' name='payParty' 
                            data-options="editable: false, panelHeight:'auto'" style="width: 250px;">
                            <c:forEach var="payParty" items="${payPartys}">
                                <option value="${payParty.code}">${payParty.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>划扣金额：</td>
                    <td>
                        <input class="easyui-textbox" id="u_amount" name="amount" 
                            data-options="required:true,validType:['positiveFloatNumer[\'划扣金额\']','maxLength[15,\'划扣金额\']']" style="width:250px;"/>
                    </td>
                </tr>
                <tr>
                    <td>划扣期数：</td>
                    <td>
                        <input class="easyui-numberbox" id="u_repayTerm" name="repayTerm" 
                            data-options="min:1, precision:0, max:48, required:true,validType:'maxLength[2,\'划扣期数\']'" style="width:250px;"/>
                    </td>
                </tr>
                <tr>
                    <td>备注：</td>
                    <td>
                        <input class="easyui-textbox" id="u_memo" name="memo" 
                            data-options="validType:'maxLength[1000,\'备注\']'" style="width:250px;"/>
                    </td>
                </tr>
            </table>
        </form>
        <div style="text-align:center;padding-top:10px;">
            <a href="#" id="submitBtn" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
            <a href="#" id="closeBtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
        </div>
    </div>
</body>
</html>