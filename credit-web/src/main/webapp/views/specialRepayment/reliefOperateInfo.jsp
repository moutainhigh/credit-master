<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta http-equiv="pragma" content="no-cache"/>
  <meta http-equiv="cache-control" content="no-cache"/>
  <meta http-equiv="expires" content="0"/>
  <title>操作日志</title>
  <jsp:include page="/views/common/headIncludeFile.jsp" />

  <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
  <script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','messager'],'business', function() {
      $(function() {
        var urlJs = [];
        urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
        urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
        urlJs.push(global.contextPath + '/resources/js/specialRepayment/reliefOperateInfo.js');
        importJSExt(urlJs,function(){
          /** 脚本加载成功回调方法 **/

        });
        $("#repayCloseBut").click(function(){
          window.parent.$('#applyReliefInfoWin').window('close');
        })
      });
    });
  </script>
</head>
<body class="easyui-layout" data-options="fit:true">
<jsp:include page="/views/common/initPageMast.jsp" />
<form id = "reliefOperateForm">
  <input type="hidden" name="applyId" value="${applyId}"/>
</form>
<!-- 申请减免详细信息窗口 -->
<div id="reliefOperateInfoPanel" class="easyui-panel"
     style="padding: 0px; height: 87%;width: 100%; margin: 0px;"
     data-options="region:'north'">
  <table id="operateDataGrid" class ="easyui-datagrid" data-options=""  style="padding: 0px; margin: 0px;"></table>
</div>


<div class="easyui-panel" style="padding: 0px; margin: 0px;height: 13%;width: 100%;"
     data-options="region:'center'">
  <div style="text-align:center;padding-top:15px;">
    <a href="javascript:void(0)" id="repayCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >返回</a>
  </div>
</div>
</body>
</html>