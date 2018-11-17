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
<title>申请书管理</title>
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
            urlJs.push(global.contextPath + '/resources/js/repay/yobokuan.js');
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
                    <td>理财机构：</td>
                    <td>
                        <select class="easyui-combobox" id="organization" name="organization" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
                            <c:forEach var="fundsSource" items="${fundsSources}">
                                <option value="${fundsSource.code}">${fundsSource.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>预拨资金(元)：</td>
                    <td><input class="easyui-numberbox" style="width:100px"  id="yuboAmt" name="yuboAmt" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true"></td>                  
                </tr>
            </table>
        </form>
    </div>
    
    <!-- DataGrid 工具栏按钮 -->
    <div class="easyui-panel" id="tb" style="padding: 5px;height: 40px;">
        <a href="#" class="easyui-linkbutton" id="createBtn" iconCls="icon-search" plain="true">生成划拨申请书</a>
        <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
    
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="dataGrid"></table>
    </div>

    <!-- Excel导入面板 -->
    <div id="importExcelWin" class="easyui-window editContentPanel" title="划拨申请书导入"
        data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
        style="width: 610px; height: 250px; padding: 0px;">
        <div style="text-align: center; padding: 0px; margin: 20px 0px;">
            <form id="baseFileForm" enctype="multipart/form-data" method="post">
                <input type="hidden"  id="hksqBatchNum" name="hksqBatchNum" value=""/>
                <label>划拨申请书原文件(pdf)：</label>
                <input class="easyui-filebox" id="applyPdfEsignatureFile" name="applyEsignaturePdf"
                       data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width: 300px" />
                <a href="#" style="margin-right:-20px" class="easyui-linkbutton" id="submitEsignatureBtn" data-options="iconCls:'icon-ok'">签章</a>
                <br></br>
                <label style="margin-left:-50px">划拨申请书签章文件(pdf)：</label>
                <input class="easyui-filebox" id="applyPdfFile" name="applyFile"
                    data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width: 300px" /> 
                <br></br>
                <label>划拨申请书(xls)：</label>
                <input class="easyui-filebox" id="applyXlsFile" name="applyFile"
                    data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width: 300px" /> 
            </form>
            <br></br>
            <div style="text-align:center;padding-top:5px;">
                <a href="#" class="easyui-linkbutton" id="submitBtn" data-options="iconCls:'icon-ok'">提交</a>
                <a href="#" class="easyui-linkbutton" id="closeBtn" data-options="iconCls:'icon-cancel'">关闭</a>
            </div>
        </div>
    </div>
    <!-- Excel导入面板 -->
</body>
</html>