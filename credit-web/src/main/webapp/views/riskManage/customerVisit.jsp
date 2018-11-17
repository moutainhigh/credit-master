<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>客户回访查询</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                      ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/riskManage/customerVisit.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法  **/
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <input type="hidden" id="sysDate" name="sysDate" value='<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>' />
    <c:if test="${loanId != null && loanId !=''}">
    <div class="easyui-panel" title="回访信息列表" data-options="region:'north',collapsible:false">
        <form id="searchForm">
            <input type="hidden" id="loanId" name="loanId" value="${loanId}"/>
        </form>
    </div>
    </c:if>
    <c:if test="${loanId == null || loanId==''}">
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 143px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <input type="hidden" id="loanId" name="loanId" value="${loanId}"/>
            <table cellpadding="5">
                <tr>
                    <td>姓名：</td>
                    <td><input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'借款人姓名\']'" style="width:150px;"/></td>
                    <td>手机：</td>
                    <td><input class="easyui-textbox" id="mobile" name="mobile" validType="mobile" style="width:150px;"/></td>
                    <td>身份证号：</td>
                    <td><input class="easyui-textbox" id="idNum" name="idNum" validType="idCard" style="width:150px;"/></td>
                </tr>
                <tr>
                    <td>开始日期：</td>
                    <td><input class="easyui-datebox" id="startDate" name="startDate" style="width:150px" value="${currentDate}"
                                data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/></td>
                    <td>截止日期：</td>
                    <td><input class="easyui-datebox" id="endDate" name="endDate" style="width:150px" value="${currentDate}"
                                data-options="validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/></td>
                    <td>借款类型：</td>
                    <td>
                        <select class="easyui-combobox" id="loanType" name="loanType" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
                            <option value="">请选择</option>
                            <c:forEach var="loanType" items="${loanTypes}">
                                <option value="${loanType}">${loanType}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
					  <td>合同编号：</td>
					  <td><input class="easyui-textbox" type="text" name="contractNum" style="width:150px;"></input></td>
				</tr>
            </table>
        </form>
    </div>
    </c:if>
    
    <c:if test="${loanId == null || loanId==''}">
    <!-- DataGrid 工具栏按钮 -->
    <div id="tb" style="padding:3px;height:30px;">
        <div style="margin-bottom:0px">
            <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
            <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>
        </div>
    </div>
    </c:if>
    
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="customerVisitDataGrid"></table>
    </div>
</body>
</html>
