<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <title>减免管理-减免查询</title>
  <jsp:include page="/views/common/headIncludeFile.jsp" />
  <style type="text/css">
  </style>

  <script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','form','datebox','validatebox'],'business', function() {
      $(function() {
        var urlJs = [];
        urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
        urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
        urlJs.push(global.contextPath + '/resources/js/specialRepayment/queryReliefInfo.js');
        importJSExt(urlJs,function(){
          /** 脚本加载成功回调方法 **/
        });
      });
    });
  </script>
</head>
<body class="easyui-layout">
<jsp:include page="/views/common/initPageMast.jsp" />
<div class="easyui-panel"
     style="padding: 5px; height: 100px; margin: 0px; text-align: center;"
     data-options="region:'north'">
  <form id="searchForm">
    <table  cellpadding="5">
      <tr>
        <td>身份证号:</td>
        <td><input class="easyui-textbox" type="text" name="idNum" id="idNum" validType="idCard" /></td>
        <td>合同编号:</td>
        <td><input class="easyui-textbox" type="text" name="contractNum" id="contractNum"/></td>
        <td>客户姓名:</td>
        <td><input class="easyui-textbox" type="text" name="name" id="name"/></td>
        <td>减免类型</td>
        <td>
          <select class="easyui-combobox" id="applyReliefType" name="applyReliefType" style="width: 173px" data-options="editable:false,panelHeight:'auto'">
            <option value="">请选择</option>
            <c:forEach var="applyReliefType" items="${applyReliefTypes}">
              <option value="${applyReliefType.code}">${applyReliefType.value}</option>
            </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <td>特殊减免</td>
        <td>
          <select class="easyui-combobox" id="specialReliefFlag" name="specialReliefFlag" style="width: 173px" data-options="editable:false,panelHeight:'auto'">
            <option value="">请选择</option>
            <c:forEach var="specialReliefType" items="${specialReliefTypes}">
              <option value="${specialReliefType.code}">${specialReliefType.value}</option>
            </c:forEach>
          </select>
        </td>
        <td>起始减免日期：</td>
        <td>
          <input class="easyui-datebox" id="startApplyDate" name="startApplyDate" value="${applyDate}" data-options="editable:false"/>
        </td>
        <td>终止减免日期</td>
        <td>
          <input class="easyui-datebox" id="endApplyDate" name="endApplyDate" value="${applyDate}" data-options="editable:false"
                 validType="dateRangeValid['>=','#startApplyDate','起始减免日期']"/>
        </td>
      </tr>
    </table>
  </form>
</div>


<div class="easyui-panel" style="padding: 0px; margin: 0px;"
     data-options="region:'center'">
  <!-- 表格标签 -->
  <table id="queryReliefInfoDataGrid" class ="easyui-datagrid" data-options=""></table>
  <div id="tb" style="padding: 3px;">
    <div style="margin-bottom: 0px">
      <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
      <a href="javascript:void(0)" id="clearBtn" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#searchFinishForm'" style="width:60px;">重置</a>
      <a href="javascript:void(0)" class="easyui-linkbutton" id="exportBtn" iconCls="pic_51" plain="true">EXCEL导出</a>
      &nbsp;&nbsp;&nbsp;
    </div>
  </div>
</div>

</body>
</html>
