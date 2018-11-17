<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>线上还款明细</title>
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
            urlJs.push(global.contextPath + '/resources/js/repay/onlineRepaymentDetail.js');
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
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 75px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <table cellpadding="5">
                <tr>
                    <td>管理营业部：</td>
                    <td>
                        <select class="easyui-combobox" id="salesDepartmentId" name="salesDepartmentId" data-options="required:true" style="width:180px;">
                            <c:forEach var="salesDeptInfo" items="${salesDeptInfoList}">
                                <option value="${salesDeptInfo.id}">${salesDeptInfo.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>还款日期：</td>
                    <td><input class="easyui-datebox" id="tradeDate" name="tradeDate"
                               data-options="required:true,editable:true,validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']" /></td>
                    <td>合同编号:</td>
					<td><input class="easyui-textbox" type="text" name="contractNum"  id="contractNum"  name="contractNum" style="width: 150px;"></input></td>
                </tr>
            </table>
        </form>
    </div>
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="repaymentDetailDataGrid" data-options=""></table>
    </div>
    <div class="easyui-panel" id="tb">
        <a href="#" class="easyui-linkbutton" id="searchBtn"  iconCls="icon-search" plain="true">查询</a>
        <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <sec:authorize ifAnyGranted="/repay/exportOnlineRepaymentDetail">
            <a href="#" class="easyui-linkbutton" id="exportBtn"  iconCls="pic_51" plain="true">EXCEL导出</a>
        </sec:authorize>
    </div>
</body>
</html>
