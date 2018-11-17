<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>管理门店变更</title>
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
            urlJs.push(global.contextPath + '/resources/js/repay/managerSalesDeptChange.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 110px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <table cellpadding="5">
                <tr>
                    <td>借款人：</td>
                    <td><input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'借款人姓名\']'" style="width:150px;"/></td>
                    <td>身份证号：</td>
                    <td><input class="easyui-textbox" id="idNum" name="idNum" validType="idCard" style="width:150px;"/></td>
                    <td>借款类型：</td>
                    <td>
                        <select class="easyui-combobox" id="loanType" name="loanType" data-options="panelHeight:'auto'" style="width:100px;">
                            <option value="">请选择</option>
                            <c:forEach var="loanType" items="${loanTypes}">
                                <option value="${loanType.value}">${loanType.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>管理营业部：</td>
                    <td>
                        <select class="easyui-combobox" id="salesDepartmentId" name="salesDepartmentId" style="width:180px;">
                            <option value="">请选择</option>
                            <c:forEach var="salesDeptInfo" items="${salesDeptInfoList}">
                                <option value="${salesDeptInfo.id}">${salesDeptInfo.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr><td>合同编号:</td>
					    <td><input class="easyui-textbox" id="contractNum" name="contractNum" style="width:150px;"/></td></tr>
            </table>
        </form>
    </div>
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="repayDataGrid" data-options=""></table>
    </div>
    <div class="easyui-panel" id="tb">
        <a href="#" class="easyui-linkbutton" id="searchBtn"  iconCls="icon-search" plain="true">查询</a>
        <a href="#" class="easyui-linkbutton" id="clearBtn"  data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <sec:authorize ifAnyGranted="/repay/batchImportSalesDeptRepayInfo">
            <a href="#" class="easyui-linkbutton" id="batchImportBtn" iconCls="pic_52" plain="true">导入</a>
        </sec:authorize>
    </div>
    
    <!-- Excel导入面板 -->
    <div id="importExcelWin" class="easyui-window editContentPanel" title="Excel批量导入" 
            data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
            style="width:550px;height:200px;padding:0px;">
        <div style="text-align:center;padding: 0px; margin: 60px 0px;">
            <form  id="baseFileForm"  enctype="multipart/form-data" method="post">
                <label>管理门店批量导入变更：</label>
                <input class="easyui-filebox" id="uploadfile" name="uploadfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                <a href="#" class="easyui-linkbutton" id="batchImport"  iconCls="pic_52" plain="false">导入</a>
            </form>
        </div>
    </div>
    <!-- Excel导入面板 -->
</body>
</html>
