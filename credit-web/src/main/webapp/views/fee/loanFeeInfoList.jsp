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
        <title>收费查询</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','messager'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/fee/loanFeeInfoList.js');
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
            </div>
        </div>
        <div class="easyui-panel" title="查询条件" style="padding:5px;height:110px;margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr>
                        <td>客户姓名：</td>
                        <td><input class="easyui-textbox" name="name" style="width:150px"/></td>
                        <td>身份证号：</td>
                        <td>
                            <input class="easyui-textbox" name="idNum" validType="idCard" style="width:150px"/>
                        </td>
                        <td>合同编号：</td>
                        <td>
                            <input class="easyui-textbox" name="contractNum" style="width:150px" />
                        </td>
                         <td>放款日期：</td>
                        <td><input class="easyui-datebox" id="grantMoneyDate" name="grantMoneyDate" style="width:150px" data-options="editable:true,validType:['date','dateRangeValid[\'&lt;=\',\'#sysDate\',\'当天\']']"/>
                        </td>
                        <tr>
                        <td>收费状态：</td>
	                    <td>
	                        <select class="easyui-combobox" id="state" name="state" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
	                            <option value="">请选择</option>
	                                <option value="已收取">已收取</option>
	                                <option value="未收取">未收取</option>
	                                <option value="部分收取">部分收取</option>
	                        </select>
	                    </td>
	                     <td>合同来源：</td>
                        <td>
                        <select class="easyui-combobox" id="fundsSources" name="fundsSources" 
                            data-options="panelHeight:'auto' ,editable:false" style="width: 100px;">
                             <option value="">请选择</option>
                             <c:forEach var="sources" items="${fundsSources}">
	                         	<option value="${sources}">${sources}</option>
	                         </c:forEach>
                        </select>
                       </td>
	                    </tr>
                    </tr>
                </table>
            </form>
        </div>
        <div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
            <!-- 表格标签 -->
            <table id="dataGrid"></table>
        </div>
		<!-- 收费流水查询 窗口 -->
        <div id="offerRepayWin" class="easyui-window"  title="服务费流水信息" data-options="width:'500px',height:'250px'">
            <table id="offerRepayDataGrid"></table>
        </div>
        <!-- 收费流水查询 窗口 -->
    </body>
</html>