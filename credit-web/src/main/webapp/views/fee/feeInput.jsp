<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"%>
<%@ page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="expires" content="0"/>
        <title>收费录入</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','messager'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/fee/feeInput.js');
                    urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
                    urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
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
        <!-- DataGrid 工具栏按钮 -->
        <div id="tb" style="padding:3px;">
            <div style="margin-bottom:0px">
                <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" style=";">查询</a>
                <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;float: none;">重置</a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <sec:authorize ifAnyGranted="/fee/feeInput/singleRepayment">
                    <a href="#" class="easyui-linkbutton" id="repayBtn" iconCls="pic_46" style="" plain="true">还款</a>
                </sec:authorize>
                <sec:authorize ifAnyGranted="/fee/feeInput/batchRepayment">
                    <a href="#" class="easyui-linkbutton" id="importBtn" iconCls="pic_42" style="" plain="true">导入</a>
                </sec:authorize>
            </div>
        </div>
        <div class="easyui-panel" title="查询条件" style="padding:5px;height:145px;margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr>
                        <td>姓名：</td>
                        <td><input class="easyui-textbox" name="name" style="width:150px"/></td>
                        <td>身份证号：</td>
                        <td>
                            <input class="easyui-textbox" name="idNum" validType="idCard" style="width:150px"/>
                        </td>
                        <td>合同编号：</td>
                        <td>
                            <input class="easyui-textbox" name="contractNum" style="width:150px" />
                        </td>
                    </tr>
                    <tr>
                        <td>放款日期（起）：</td>
                        <td><input class="easyui-datebox" id="grantMoneyDateStart" name="grantMoneyDateStart" style="width:150px" 
                                    data-options="editable:true,validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/>
                        </td>
                        <td>放款日期（止）：</td>
                        <td><input class="easyui-datebox" id="grantMoneyDateEnd" name="grantMoneyDateEnd" style="width:150px" 
                                    data-options="editable:true,validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/>
                        </td>
                        <td>收费状态：</td>
	                    <td>
	                        <select class="easyui-combobox" id="state" name="state" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
	                            <option value="">请选择</option>
	                            <c:forEach var="state" items="${states}">
	                                <option value="${state.value}">${state.value}</option>
	                            </c:forEach>
	                        </select>
	                    </td>
                    </tr>
                     <tr>
	                    <td>合同来源：</td>
	                    <td>
	                        <select class="easyui-combobox" id="fundsSources" name="fundsSources" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
	                            <option value="">请选择</option>
	                            <c:forEach var="fundsSources" items="${fundsSources}">
	                                <option value="${fundsSources}">${fundsSources}</option>
	                            </c:forEach>
	                        </select>
	                    </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
            <!-- 表格标签 -->
            <table id="dataGrid"></table>
        </div>
        
        <!-- Excel 导入 -->
        <div id="importExcelWin" class="easyui-window editContentPanel" title="Excel导入" 
                data-options="closed : true,collapsible : false,minimizable : false, maximizable : false,modal : true,resizable : false">
            <sec:authorize ifAnyGranted="/fee/feeInput/batchRepayment">
                <div style="text-align:center;padding:10px;" >
                    <h3>信贷系统收费录入</h3>
                    <form  id="creditFileForm" enctype="multipart/form-data" method="post">
                        <input class="easyui-filebox" id="repayInfoFile" name="repayInfoFile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                        <a href="#" id="importExcelBtn" class="easyui-linkbutton" data-options="iconCls:'pic_52'" >导入</a>
                    </form>
                </div>
            </sec:authorize>
        </div>
        <!-- Excel 导入 -->
        
        <!-- 还款信息窗口 -->
        <div id="repayWin" class="easyui-window " title="还款信息">
            <div class="editContentPanel" style="padding:10px;">
                <form id="repayForm" >
                    <input  type="hidden" id="loanId" name="loanId" ></input>
                    <table cellpadding="5"  border="0" rules="rows" >
                        <tr>
                            <td width="100px">姓名：</td>
                            <td width="120px"><input readonly="readonly" class="inputLabel" name="name" /></td>
                            <td width="100px">身份证号：</td>
                            <td width="120px"><input readonly="readonly" class="inputLabel" name="idNum"/></td>
                            <td width="100px">合同编号：</td>
                            <td width="120px"><input readonly="readonly" class="inputLabel" name="contractNum"/></td>
                        </tr>
                        <tr>
                            <td>应收服务费：</td>
                            <td><input readonly="readonly" class="inputLabel thousand" name="amount"/></td>
                            <td>实收服务费：</td>
                            <td><input readonly="readonly" class="inputLabel thousand" name="receiveAmount"/></td>
                            <td>未收服务费：</td>
                            <td><input readonly="readonly" class="inputLabel thousand" name="unpaidAmount"/></td>
                        </tr>
                        <tr>
                            <td>还款日期：</td>
                            <td><input readonly="readonly" class="inputLabel" id="curDate" name="curDate" value="<fmt:formatDate value="<%=new java.util.Date() %>" pattern="yyyy-MM-dd" />"/></td>
                            <td>还款金额：</td>
                            <td>
                                <input class="easyui-numberbox" id="repaymentAmount" name="repaymentAmount" style="width:100px;" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true" validType=""></input>
                            </td>
                            <td>还款方式：</td>
                            <td>
                                <select class="easyui-combobox" name="tradeType" id="tradeType" style="width:80px;" data-options="editable:false,panelHeight:'auto',required:true" >
                                    <option value="现金">现金</option>
                                    <option value="转账">转账</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>备注：</td>
                            <td colspan="5">
                                <input class="easyui-textbox" id="memo" name="memo" data-options="multiline:true" style="height:60px;width:400px;"/>
                            </td>
                        </tr>
                    </table>
                </form>
                <div style="text-align:center;padding-top:15px;">
                    <a href="#" id="repaySubmitBtn" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="margin-right:15px;">还款</a>
                    <a href="#" id="repayCloseBtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
                </div>
            </div>
        </div>
        <!-- 还款信息窗口 -->
    </body>
</html>