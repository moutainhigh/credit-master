<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <title>备案导出</title>
  <jsp:include page="/views/common/headIncludeFile.jsp" />
  <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
  <script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
      ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
      $(function() {
        var urlJs = [];
        urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
        urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
        urlJs.push(global.contextPath + '/resources/js/loan/putOnRecordExportPage.js');
        importJSExt(urlJs,function(){
          /** 脚本加载成功回调方法 **/
        });
      });
    });
  </script>
</head>
<body class="easyui-layout">
<jsp:include page="/views/common/initPageMast.jsp" />
<div id="offerParams" style="display:none;" data-offer-perms="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.authorities}" ></div>
<div data-options="region:'north',split:false,border:true,collapsible:false" style="height: 115px;">
  <div class="easyui-panel" title="查询条件" style="width:100%;height:80px;">
    <form id="searchForm" style="width: 100%; height: 100%;">
      <table style="height: 100%;border-spacing:5px;table-layout:fixed;">
        <tr>
          <td>协议签署日期：</td>
          <td>
            <input class=" easyui-datebox" id="beginSignDate" name="beginSignDate" style="width:120px" data-options="editable : false" value="${beginSignDate}" validType="dateRangeValid['<','sysdate','当前时间']"/> ~
            <input class=" easyui-datebox" id="endSignDate" name="endSignDate" style="width:120px" data-options="editable : false" value="${endSignDate}" validType="dateRangeValid['>=','#beginSignDate','开始时间']"/>
          </td>
          <td style="width: 60px;text-align: left;">理财机构：</td>
          <td style="width: 150px;text-align: left;">
            <select class="easyui-combobox" data-options ="editable:false, panelHeight:'auto'" name="financialorg" id="financialorg" style="width: 130px;">
                  <option value="">请选择...</option>
                  <option value="WMXT">外贸信托</option>
            </select>
          </td>
          <td style="width: 100px;text-align: left;">银行卡所属地区：</td>
          <td style="width: 150px;text-align: left;">
            <select class="easyui-combobox" data-options ="editable:false, panelHeight:'auto'" name="regionType" id="regionType" style="width: 130px;">
              <option value="01">深圳地区</option>
              <option value="99">异地</option>
            </select>
          </td>
          <td style="width: 60px;text-align: left;">合同编号：</td>
          <td style="width: 150px;text-align: left;">
            <input class="easyui-validatebox easyui-textbox" type="text" id="contractNum" name="contractNum" style="width: 150px;"/>
          </td>
        </tr>
      </table>
    </form>
  </div>
  <div style="padding: 3px;" class="datagrid-toolbar">
    <div id="bottonBox" style="margin-bottom: 0px">
      <a href="javascript:void(0)" format="html" class="easyui-linkbutton" id="searchButs" data-options="iconCls:'icon-search',plain:true" style="width:60px">查询</a>
      <a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>
      <a href="javascript:void(0)" format="xls" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" style="width:90px" id="eloanExportBtn">导出</a>
    </div>
  </div>
</div>
<div data-options="border:false,region:'center',noheader:true,panelHeight:'auto'">
  <iframe id="putOnRecorDefault" name="putOnRecorDefault" style="width:100%;height:100%;" frameborder="0"></iframe>
</div>
</body>
</html>
