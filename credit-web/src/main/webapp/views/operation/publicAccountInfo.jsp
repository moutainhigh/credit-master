<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>对公账户还款</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','form','datebox','validatebox'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
            urlJs.push(global.contextPath + '/resources/js/operation/publicAccountInfo.js');
            urlJs.push(global.contextPath + '/resources/js/operation/publicRec.js');
            urlJs.push(global.contextPath + '/resources/js/operation/operateLog.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                // 是否有修改对公领取时间的权限
                var isUpdateReceiveTime = "${isUpdateReceiveTime}";
                // 动态设置工具栏高度
                if(isUpdateReceiveTime == 'true'){
                    $("#tb").css({height:50});
                } else {
                    $("#tb").css({height:25});
                }
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <input type="hidden" id="sysDate" name="sysDate" value='<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>' />
    <input type="hidden" id="isCanConfirmReceive" name="isCanConfirmReceive" value="${isCanConfirmReceive}" />
    <input type="hidden" id="isCanChannelConfirm" name="isCanChannelConfirm" value="${isCanChannelConfirm}" />
    <input type="hidden" id="isCancelReceive" name="isCancelReceive" value="${isCancelReceive}" />
    <input type="hidden" id="isSearchLog" name="isSearchLog" value="${isSearchLog}" />
    <!-- DataGrid 工具栏按钮 -->
    <div class="easyui-panel" id="tb" style="padding: 5px; height: 40px;">
        <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
        <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;
        <sec:authorize ifAnyGranted="/operation/batchImportPublicAccountInfo,/operation/batchImportCarLoanAccountInfo">
            <a href="#" class="easyui-linkbutton" id="batchImportBtn" iconCls="pic_52" plain="true">导入</a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/newTemplateImportPublicAccountInfo,/operation/newTemplateImportCarLoanAccountInfo,/operation/newTemplateImportBusinessAccountInfo">
            <a href="#" class="easyui-linkbutton" id="newImportBtn" iconCls="pic_52" plain="true">新模板导入</a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/exportPublicAccountReceiveInfo">
            <a href="#" class="easyui-linkbutton" id="exportReceiveInfoBtn" iconCls="pic_51" plain="true">导出已认领结果</a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/exportCarLoanAccountReceiveInfo">
            <a href="#" class="easyui-linkbutton" id="exportCarLoanReceiveInfoBtn" iconCls="pic_51" plain="true">导出车企贷已认领结果</a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/exportBusinessAccountReceiveInfo">
            <a href="#" class="easyui-linkbutton" id="exportNewBusinessReceiveInfoBtn" iconCls="pic_51" plain="true">导出证方已认领结果</a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/exportPublicAccountRepayReceiveInfo">
            <a href="#" class="easyui-linkbutton" id="exportRepayReceiveInfoBtn" iconCls="pic_51" plain="true">导出对公账户还款认领情况表</a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/exportAccountSearchResult">
            <a href="#" class="easyui-linkbutton" id="exportSearchResultBtn" iconCls="pic_51" plain="true">导出查询结果</a>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/updateReceiveTime">
            <br/>
            <a href="#" class="easyui-linkbutton" id="updateReceiveTime" iconCls="icon-edit" plain="true">修改对公还款时间</a>
        </sec:authorize>
        <span id="message" style="color:red">${message}</span>
    </div>
    
    <!-- 查询条件工具栏 -->
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 120px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <input type="hidden" id="userId" name="userId" value="${user.id}"/>
            <input type="hidden" id="userCode" name="userCode" value="${user.userCode}"/>
            <input type="hidden" id="orgCode" name="orgCode" value="${user.orgCode}"/>
            <input type="hidden" id="employeeType" name="employeeType" value="${user.employeeType}"/>
            <input type="hidden" id="statusInfo" name="statusInfo" value="${statusInfo}"/>
            <table cellpadding="5">
                <tr>
                    <td>还款日期（起）：</td>
                    <td><input class="easyui-datebox" id="repayDateStart" name="repayDateStart" style="width:150px" 
                                data-options="editable:true,validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/>
                    </td>
                    <td>还款日期（止）：</td>
                    <td><input class="easyui-datebox" id="repayDateEnd" name="repayDateEnd" style="width:150px" 
                                data-options="editable:true,validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/>
                    </td>
                    <td>还款金额：</td>
                    <td><input class="easyui-textbox" id="amount" name="amount" style="width:150px"
                                data-options="validType:['positiveFloatNumer[\'还款金额\']','maxLength[20,\'还款金额\']']"/></td>
                    <td>对方单位：</td>
                    <td><input class="easyui-textbox" id="secondCompany" name="secondCompany" style="width:150px" data-options="validType:'maxLength[128,\'对方单位\']'"/></td>
                </tr>
                <tr>
                    <td>认领日期（起）：</td>
                    <td><input class="easyui-datebox" id="recTimeStart" name="recTimeStart" style="width:150px"
                                data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/></td>
                    <td>认领日期（止）：</td>
                    <td><input class="easyui-datebox" id="recTimeEnd" name="recTimeEnd" style="width:150px" 
                                data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/></td>
                    <td>认领者工号：</td>
                    <td>
                        <input class="easyui-textbox" id="recUsercode" name="recUsercode" style="width:150px" data-options="validType:'maxLength[50,\'认领者工号\']'"/>
                    </td>
                    <td>状态：</td>
                    <td>
                        <select class="easyui-combobox" id="status" name="status" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
                            <option value="">请选择</option>
                            <c:forEach var="status" items="${statusList}">
                                <option value="${status.value}">${status.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="publicAccountDataGrid" ></table>
    </div>
    
    <!-- Excel导入面板 -->
    <div id="importExcelWin" class="easyui-window editContentPanel" title="Excel批量导入" 
            data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
            style="width:550px;height:250px;padding:0px;">
        <sec:authorize ifAnyGranted="/operation/batchImportPublicAccountInfo">
            <div style="text-align:center;padding: 0px; margin: 50px 0px;">
                <form id="baseFileForm" enctype="multipart/form-data" method="post">
                    <label>对公账户批量导入：</label>
                    <input class="easyui-filebox" id="publicAccountfile" name="publicAccountfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                    <a href="#" class="easyui-linkbutton" id="publicAccountBatchImport" iconCls="pic_52" plain="false">导入</a>
                </form>
            </div>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/batchImportCarLoanAccountInfo">
            <div style="text-align:center;padding: 0px; margin: 50px 0px;">
                <form id="carFileForm" enctype="multipart/form-data" method="post">
                    <label>&nbsp;车企贷批量导入：</label>
                    <input class="easyui-filebox" id="carLoanAccountfile" name="carLoanAccountfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                    <a href="#" class="easyui-linkbutton" id="carLoanAccountBatchImport" iconCls="pic_52" plain="false">导入</a>
                </form>
            </div>
        </sec:authorize>
    </div>
    
    <!-- 新模板  -->
    <div id="newImportExcelWin" class="easyui-window editContentPanel" title="Excel批量导入" 
            data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
            style="width:550px;height:250px;padding:0px;">
        <sec:authorize ifAnyGranted="/operation/newTemplateImportPublicAccountInfo">
            <div style="text-align:center;padding: 0px; margin: 30px 0px;">
                <form id="newCreditFileForm" enctype="multipart/form-data" method="post">
                    <label>对公账户批量导入（新）：</label>
                    <input class="easyui-filebox" id="newPublicAccountfile" name="newPublicAccountfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                    <a href="#" class="easyui-linkbutton" id="newCreditImportBtn" name="newCreditImportBtn" iconCls="pic_52" plain="false">导入</a>
                </form>
            </div>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/newTemplateImportCarLoanAccountInfo">
            <div style="text-align:center;padding: 0px; margin: 30px 0px;">
                <form id="newCarFileForm" enctype="multipart/form-data" method="post">
                    <label>&nbsp;车企贷批量导入（新）：</label>
                    <input class="easyui-filebox" id="newCarLoanAccountfile" name="newCarLoanAccountfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                    <a href="#" class="easyui-linkbutton" id="newCarImportBtn" name="newCarImportBtn" iconCls="pic_52" plain="false">导入</a>
                </form>
            </div>
        </sec:authorize>
        <sec:authorize ifAnyGranted="/operation/newTemplateImportBusinessAccountInfo">
            <div style="text-align:center;padding: 0px; margin: 30px 0px;">
                <form id="newBusinessFileForm" enctype="multipart/form-data" method="post">
                    <label>&nbsp;&nbsp;证方批量导入（新）：</label>
                    <input class="easyui-filebox" id="newBusinessfile" name="newBusinessfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                    <a href="#" class="easyui-linkbutton" id="newBusinessImportBtn" name="newBusinessImportBtn" iconCls="pic_52" plain="false">导入</a>
                </form>
            </div>
        </sec:authorize>
    </div>
    <!-- Excel导入面板 -->
    
    <!-- 日志显示表格 -->
    <div id="showLogWin" class="easyui-window" title="日志信息">
        <!-- 日志信息表格 -->
        <table id="logDataGrid" class="easyui-datagrid" data-options=""></table>
    </div>
    
    <!-- 对公账户领取时间修改面板 -->
    <div id="updateDataPanel" class="easyui-window" title="修改对公还款时间" style="padding:20px;">
        <form method="post"  id="dataForm">
            <input type="hidden" id="startDictionaryId" name="startDictionaryId" />
            <input type="hidden" id="endDictionaryId" name="endDictionaryId" />
            <table cellpadding="5">
                <tr>
                    <td>领取开始时间：</td>
                    <td><input class="easyui-textbox" type="text" id="startTime" name="startTime" data-options="required:true"/></td>
                </tr>
                <tr>
                    <td>领取结束时间：</td>
                    <td><input class="easyui-textbox" type="text" id="endTime" name="endTime" data-options="required:true"/></td>
                </tr>
            </table>
        </form>
        <div style="text-align:center;padding-top:10px;">
            <sec:authorize ifAnyGranted="/operation/updateReceiveTime">
                <a href="javascript:void(0)" id="submitBtn" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
            </sec:authorize>
            <a href="javascript:void(0)" id="closeBtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
        </div>
    </div>
    
    <!-- 认领面板信息 -->
    <div id="recTB" style="padding:3px;">
        <div style="margin-bottom:0px">
            <a href="#" class="easyui-linkbutton" id="recSearchBtn" iconCls="icon-search" plain="true" >查询</a>
            <a href="#" class="easyui-linkbutton" id="recClearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>
        </div>
    </div>
    <div id="showRectWin" class="easyui-window" title="对公认领">
        <div class="easyui-layout" style="height:480px;padding:0px;">
            <div class="easyui-panel" data-options="region:'north'" style="height:100px">
                <form id="recSearchForm">
                    <input type="hidden" id="accountId" name="accountId" />
                    <table cellpadding="5">
                        <tr>
                            <td>还款日期：</td>
                            <td>
                                <span id="recTradeDate"></span>
                            </td>
                            <td>还款金额：</td>
                            <td>
                                <span id="recTradeAmount"></span>
                            </td>
                            <td>状态：</td>
                            <td>
                                <span id="recStatus"></span>
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>姓名：</td>
                            <td><input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'借款人姓名\']'" style="width:150px"/></td>
                            <td>联系电话：</td>
                            <td><input class="easyui-textbox" id="contractPhone" name="contractPhone" data-options="validType:'maxLength[20,\'联系电话\']'" style="width:150px"/></td>
                            <td>身份证号：</td>
                            <td><input class="easyui-textbox" id="idNum" name="idNum" validType="idCard" style="width:150px"/></td>
                        </tr>
                        <tr>
                            <td>合同编号：</td>
                            <td><input class="easyui-textbox" id="contractNum" name="contractNum" data-options="validType:'maxLength[100,\'合同编号\']'" style="width:150px"/></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="easyui-panel" title="" data-options="region:'center'">
                <!-- 联系人表格 -->
                <table id="recDataGrid" class="easyui-datagrid"></table>
            </div>
         </div>
    </div>
    <!-- 认领面板信息 -->
</body>
</html>
