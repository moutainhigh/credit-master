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
  <title>回款确认书</title>
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
        urlJs.push(global.contextPath + '/resources/js/offer/refundedOfMoneyConfirmationBook.js');
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
<!-- 查询条件工具栏 -->
<div class="easyui-panel" title="查询条件" style="padding: 5px; height: 80px; margin: 0px;" data-options="region:'north'">
  <form id="searchForm">
    <table style="height: 100%;border-spacing:5px;table-layout:fixed;">
      <tr>
        <td>提交日期：</td>
        <td>
          <input class="easyui-datebox" id="tradeDate" name="tradeDate" style="width:120px" data-options="editable : false" value="${tradeDate}" validType="dateRangeValid['<=','#sysDate','当天']"/>
        </td>
        <td>债权去向：</td>
        <td>
          <select class="easyui-combobox" id="loanBelongs" name="loanBelongs" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
            <option value="">请选择</option>
            <c:forEach var="loanBelong" items="${loanBelongs}">
              <option value="${loanBelong.value}">${loanBelong.value}</option>
            </c:forEach>
          </select>
        </td>
      </tr>
    </table>
  </form>
</div>

<!-- DataGrid 工具栏按钮 -->
<div class="easyui-panel" id="tb" style="padding: 5px;height: 35px;">
  <a href="javascript:void(0)" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>
  &nbsp;&nbsp;&nbsp;&nbsp;
</div>

<div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
  <!-- 表格标签 -->
  <table id="dataGrid"></table>
</div>
</body>
</html>
