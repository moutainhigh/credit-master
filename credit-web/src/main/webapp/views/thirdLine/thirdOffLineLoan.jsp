<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>第三方线下放款</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/resources/css/sysIcon.css" />
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','form','datebox','validatebox'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
            urlJs.push(global.contextPath + '/resources/js/payment/thirdOffLineLoan.js');
            urlJs.push(global.contextPath + '/resources/js/payment/offerDetail.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <!-- 查询条件工具栏 -->
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 80px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <table cellpadding="5">
                <tr>
                    <td>客户姓名：</td>
                    <td>
                        <input class="easyui-textbox" id="name" name="name" style="width: 150px;"/>
                    </td>
                    <td>身份证号：</td>
                    <td>
                        <input class="easyui-textbox" id="idnum" name="idnum" validType="idCard" style="width: 150px;"/>
                    </td>
                    <%-- <td>报盘状态：</td>
                    <td>
                        <select class="easyui-combobox" id="state" name="state" 
                            data-options="panelHeight:'auto' ,editable:false" style="width: 100px;">
                            <option value="">请选择</option>
                            <c:forEach var="state" items="${states}">
                                <option value="${state}">${state}</option>
                            </c:forEach>
                        </select>
                    </td> --%>
                </tr>
                <%-- <tr>
                    <td>报盘日期：</td>
                    <td>
                        <input class="easyui-datebox" id="offerTimeStart" name="offerTimeStart" style="width:100px" value="${sysdate}" data-options="validType:'date'"/> ~
                        <input class="easyui-datebox" id="offerTimeEnd" name="offerTimeEnd" style="width:100px" value="${sysdate}" data-options="validType:'date'"/>
                    </td>
                </tr> --%>
            </table>
        </form>
    </div>
    
    <!-- DataGrid工具栏按钮 -->
    <div class="easyui-panel" id="tb" style="padding: 5px; height: 40px;">
        <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" style="width: 60px;">查询</a> 
        <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width: 60px;">重置</a>
        <sec:authorize ifAnyGranted="/loan/thirdOffLineLoan/importLoanExcel">
            <a href="#" class="easyui-linkbutton" id="importAlreadyLoanBtn" iconCls="pic_51" plain="true">导入已放款文件</a>
        </sec:authorize>
    </div>
    
    <!-- DataGrid数据表格区域 -->
    <div class="easyui-panel" style="padding: 0px; margin: 0px;" data-options="region:'center'">
        <table id="thirdOffLineLoanDataGrid"></table>
    </div>
    
    <!-- 放款流水查看窗口-->
    <div id="offerDetailWin" class="easyui-window" title="放款流水" data-options="closed:true" style="width: 1000px; height: 500px; padding: 0px;">
        <!-- 放款流水表格 -->
        <table id="offerDetailDataGrid" class="easyui-datagrid"></table>
    </div>
    
    <!-- 导入放款文件导入面板 -->
    <div id="importExcelWin" class="easyui-window editContentPanel" title="Excel导入"
        data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
        style="width: 550px; height: 200px; padding: 0px;">
        <div style="text-align:center;padding: 0px; margin: 50px 0px;">
            <form id="baseFileForm" enctype="multipart/form-data" method="post">
                <label>导入已放款文件：</label>
                <input class="easyui-filebox" id="alreadyLoanFile" name="offLineLoanFile"
                    data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width: 300px" /> 
                <a href="#" class="easyui-linkbutton" id="batchImportAlreadyLoan" iconCls="pic_52" plain="false">导入</a>
            </form>
        </div>
    </div>
    
    
    <!-- 查看附件窗口 -->
    <div id="winAttach" class="easyui-window" title="附件查看" 
        data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false,iconCls:'icon-save'"
        style="width: 898px; height: 520px; padding: 1px;">
        <iframe src="" id="attacheLoan" width="875px" height="475px" scrolling="no"></iframe>
    </div>
    <!-- 查看附件窗口 -->
</body>
</html>
