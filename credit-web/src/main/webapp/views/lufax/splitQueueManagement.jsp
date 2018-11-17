<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="expires" content="0"/>
        <title>分账管理</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>        
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                              ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
                    urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
                    urlJs.push(global.contextPath + '/resources/js/lufax/splitQueueManagement.js');
                    importJSExt(urlJs,function(){
                        /** 脚本加载成功回调方法 **/
                    });
                });
            });
        </script>
    </head>
    <body class="easyui-layout">
        <jsp:include page="/views/common/initPageMast.jsp" />
        <!-- DataGrid 工具栏按钮 -->
        <div id="tb" style="padding:3px;">
            <div style="margin-bottom:0px">
                <a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
                <a href="#" class="easyui-linkbutton" id="clearCondition" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="#" class="easyui-linkbutton" id="repeatSendBut" iconCls="pic_61" plain="true">分账重发</a>
                <a href="#" class="easyui-linkbutton" id="oneAllSendBut" iconCls="pic_61" plain="true">一键重发</a>
                <a href="#" class="easyui-linkbutton" id="syncRepaymentPlanBtn" iconCls="pic_61" plain="true">同步还款计划</a>
                <a href="#" class="easyui-linkbutton" id="syncRepaymentRecordBtn" iconCls="pic_61" plain="true">同步还款记录</a>
                <a href="#" class="easyui-linkbutton" id="syncRepaymentDetailBtn" iconCls="pic_61" plain="true">同步还款明细</a>
                <a href="#" class="easyui-linkbutton" id="editDataBtn" iconCls="pic_36" plain="true">编辑</a>
            </div>
        </div>
        <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 180px; margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr>
                        <td>客户姓名：</td>
                        <td><input class="easyui-textbox" id="name" name="name" style="width:220px;" /></td>
                        <td>证件号码：</td>
                        <td><input class="easyui-textbox" id="idNum" name='idNum' validType="idCard" style="width: 150px;" /></td>
                        <td>合同编号：</td>
                        <td><input class="easyui-textbox" id="contractNum" name="contractNum" style="width:180px;"/></td>
                    </tr>
                    <tr>
                        <td>通知状态：</td>
                        <td>
                            <select class="easyui-combobox" id='splitNotifyState' name='splitNotifyState' data-options="editable: false, panelHeight:'auto'" style="width: 220px;">
                                    <option value="">全部</option>
                                    <c:forEach var="splitNotifyState" items="${splitNotifyStates}">
                                        <option value="${splitNotifyState.code}">${splitNotifyState.value}</option>
                                    </c:forEach>
                            </select>
                        </td>
                        <td>分账状态：</td>
                        <td>
                            <select class="easyui-combobox" id='splitResultState' name='splitResultState' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                                    <option value="">全部</option>
                                    <c:forEach var="splitResultState" items="${splitResultStates}">
                                        <option value="${splitResultState.code}">${splitResultState.value}</option>
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
                         <td>创建时间：</td>
                        <td>
                            <input class="easyui-datebox" id="startCreateTime" name="startCreateTime" style="width:100px" value="${sysdate}" data-options="validType:'date'"/> ~
                            <input class="easyui-datebox" id="endCreateTime" name="endCreateTime" style="width:100px" value="${sysdate}" data-options="validType:'date'"/>
                        </td>
                        <td>是否结算：</td>
                        <td>
                            <select class="easyui-combobox" id='payOffType' name='payOffType' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                                <option value="">全部</option>
                                   <option value="1">未结算</option>
                                   <option value="2">已结算</option>
                            </select>
                        </td>
                        <td>批次号：</td>
                        <td>
                            <input class="easyui-textbox" name="batchId" id="batchId" style="width: 180px;" />
                        </td>
                     </tr>
                     <tr>
                        <td>序号：</td>
                        <td>
                            <input class="easyui-textbox" name="debitNo" id="debitNo" style="width: 220px;" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
            <!-- 表格标签 -->
            <table id="dataGrid" class="easyui-datagrid"></table>
        </div>
        
        <!-- 分账队列数据修改面板 -->
        <div id="updateDataPanel" class="easyui-window" title="更新" style="padding:20px;" 
            data-options="modal: true, collapsible: false, minimizable: false, maximizable: false, closable: true, closed: true, draggable: true, shadow: true, iconCls: 'icon-save'">
            <form method="post" id="dataForm">
                <!-- 分账队列ID -->
                <input type="hidden" id="splitId" name="splitId" />
                <table cellpadding="5">
                    <tr>
                        <td>合同编号：</td>
                        <td>
                            <input class="easyui-textbox" id="u_contractNum" name="contractNum" data-options="required:true,validType:'maxLength[50,\'合同编号\']'" style="width:200px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>通知状态：</td>
                        <td>
                            <select class="easyui-combobox" id='u_splitNotifyState' name='splitNotifyState' data-options="editable: false, panelHeight:'auto'" style="width: 200px;">
                                <c:forEach var="splitNotifyState" items="${splitNotifyStates}">
                                    <option value="${splitNotifyState.code}">${splitNotifyState.value}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>分账状态：</td>
                        <td>
                            <select class="easyui-combobox" id='u_splitResultState' name='splitResultState'
                                data-options="editable: false, panelHeight:'auto'" style="width: 200px;">
                                <c:forEach var="splitResultState" items="${splitResultStates}">
                                    <option value="${splitResultState.code}">${splitResultState.value}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>结算状态：</td>
                        <td>
                            <select class="easyui-combobox" id='u_payOffType' name='payOffType' data-options="editable: false, panelHeight:'auto'" style="width: 200px;">
                                <option value="1">未结算</option>
                                <option value="2">已结算</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>冻结金额：</td>
                        <td>
                            <input class="easyui-textbox" id="u_frozenAmount" name="amount" data-options="required:true,validType:['positiveFloatNumer[\'冻结金额\']','maxLength[15,\'冻结金额\']']" style="width:200px;"/>
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