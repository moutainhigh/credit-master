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
  <title>审批环节节点与员工关系管理</title>
  <jsp:include page="/views/common/headIncludeFile.jsp" />
  <style type="text/css">
  </style>

  <script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','form','datebox','validatebox'],'business', function() {
      $(function() {
        var urlJs = [];
        urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
        urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
        urlJs.push(global.contextPath + '/resources/js/specialRepayment/approveNodeEmployeeManage.js');
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
     style="padding: 5px; height: 50px; margin: 0px; text-align: center;"
     data-options="region:'north'">
  <form id="searchForm">
    <table  cellpadding="5">
      <tr>
        <td>姓名:</td>
        <td><input class="easyui-textbox" type="text" name="name" id="name"/></td>
        <td>工号:</td>
        <td><input class="easyui-textbox" type="text" name="userCode" id="userCode"/></td>
      </tr>
    </table>
  </form>
</div>


<div class="easyui-panel" style="padding: 0px; margin: 0px;"
     data-options="region:'center'">
  <!-- 表格标签 -->
  <table id="nodeEmployeeDatagrid" ></table>
  <div id="tb" style="padding: 3px;">
    <div style="margin-bottom: 0px">
      <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
      <a href="javascript:void(0)" id="clearBtn" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#searchForm'" style="width:60px;">重置</a>
      <a href="javascript:void(0)" id="createNodeEmployeeBtn" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" style="width:120px;">分配环节</a>
      &nbsp;&nbsp;&nbsp;&nbsp;
    </div>
  </div>
</div>

<!--新增窗口-->
<div id="addNodeEmployeeWin" class="easyui-window " title="新增"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="width : 400px;height : 400px;">
  <div class="applyReliefPanel" style="padding:10px;">
    <div style="color:red;margin-bottom: 10px;font-size: 12px;"></div>
    <form id="addNodeEmployeefForm">
      <table>
        <tr>
          <td style="width:100px">工号：</td>
          <td><input class="easyui-textbox" type="text" name="userCode"/></td>
        </tr>
        <tr >
          <td style="width:100px">选择环节：</td>
          <td>
<%--            <select class="easyui-combobox" id="nodeId" name="nodeId" style="width: 170px">
              <c:forEach var="nodes" items="${nodeList}">
                <option value="${nodes.id}">${nodes.name}</option>
              </c:forEach>
            </select>--%>
              <ul id="nodeTree" class="easyui-tree" checkbox="true"></ul>
          </td>
        </tr>
      </table>
    </form>
    <div style="text-align:center;padding-top:20px;padding-bottom: 20px">
      <a href="javascript:void(0)" id="addNodeEmployeeSubmitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >确定</a>
      <a href="javascript:void(0)" id="addNodeEmployeeCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >返回</a>
    </div>
  </div>
</div>

</body>
</html>
