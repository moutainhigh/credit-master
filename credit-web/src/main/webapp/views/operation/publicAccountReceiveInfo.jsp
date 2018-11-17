<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>领取还款记录</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','form','datebox','validatebox'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/operation/publicAccountReceiveInfo.js');
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
    <div class="easyui-panel" id="tb" style="padding: 5px;">
        <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" >查询</a>
        <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="#" class="easyui-linkbutton" id="backBtn" iconCls="icon-back" plain="true">返回</a>
        <span id="message" style="color:red">${message}</span>
    </div>
    
    <!-- 查询条件工具栏 -->
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 130px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <input type="hidden" id="accountId" name="accountId" value="${publicAccountInfo.id}"/>
            <input type="hidden" id="userId" name="userId" value="${user.id}"/>
            <input type="hidden" id="userCode" name="userCode" value="${user.userCode}"/>
            <input type="hidden" id="orgCode" name="orgCode" value="${user.orgCode}"/>
            <table cellpadding="5">
                <tr>
                    <td>还款日期：</td>
                    <td><fmt:formatDate value="${publicAccountInfo.repayDate }" pattern="yyyy-MM-dd"/></td>
                    <td>还款金额：</td>
                    <td><fmt:formatNumber value="${publicAccountInfo.amount }" pattern="#,#00.00#"/></td>
                    <td>状态：</td>
                    <td>${publicAccountInfo.status}</td>
                    <td></td>
                </tr>
                <tr>
                    <td>姓名：</td>
                    <td><input class="easyui-textbox" type="text" id="name" name="name" data-options="validType:'maxLength[60,\'借款人姓名\']'"/></td>
                    <td>联系电话：</td>
                    <td><input class="easyui-textbox" type="text" id="contractPhone" name="contractPhone" data-options="validType:'maxLength[20,\'联系电话\']'"/></td>
                    <td>身份证号：</td>
                    <td><input class="easyui-textbox" type="text" id="idNum" name="idNum" validType="idCard"/></td>
                </tr>
                <tr>
                    <td>合同编号：</td>
                    <td><input class="easyui-textbox" type="text" id="contractNum" name="contractNum"/></td>
                </tr>
            </table>
        </form>
    </div>
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="receiveInfoDataGrid" ></table>
    </div>
</body>
</html>
