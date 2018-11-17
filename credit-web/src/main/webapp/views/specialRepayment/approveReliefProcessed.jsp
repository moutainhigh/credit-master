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
  <title>减免管理-减免审批</title>
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
        urlJs.push(global.contextPath + '/resources/js/specialRepayment/approveReliefProcessed.js?1');
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
        <td>合同编号:</td>
        <td><input class="easyui-textbox" type="text" name="contractNum" id="contractNum"/></td>
        <td>客户姓名:</td>
        <td><input class="easyui-textbox" type="text" name="name" id="name"/></td>
        <td>身份证号:</td>
        <td><input class="easyui-textbox" type="text" name="idNum" id="idNum" validType="idCard" /></td>
        <td>减免类型:</td>
        <td>
          <select class="easyui-combobox" id="applyReliefType" name="applyReliefType" style="width: 173px"  data-options="editable:false,panelHeight:'auto'">
            <option value="">请选择</option>
          <c:forEach var="applyReliefType" items="${applyReliefTypes}">
            <option value="${applyReliefType.code}">${applyReliefType.value}</option>
          </c:forEach>
        </select>
        </td>
      </tr>
      <tr>
        <td>申请时间:</td>
        <td>
          <input class="easyui-datebox" id="startApplyDate" name="startApplyDate" value="${applyDate}" data-options="editable:false"/>
        </td>
        <td>至</td>
        <td>
          <input class="easyui-datebox" id="endApplyDate" name="endApplyDate" value="${applyDate}" data-options="editable:false"/>
        </td>
        <td>管理网点:</td>
        <td>
          <select class="easyui-combobox" id="salesDepartmentId" name="salesDepartmentId" style="width: 173px;">
          <option value="">请选择</option>
          <c:forEach var="salesDeptInfo" items="${salesTeamInfoList}">
            <option value="${salesDeptInfo.id}">${salesDeptInfo.name}</option>
          </c:forEach>
        </select>
        </td>
      </tr>
    </table>
  </form>
</div>


<div class="easyui-panel" style="padding: 0px; margin: 0px;"
     data-options="region:'center'">
  <!-- 表格标签 -->
  <table id="approveReliefProcessDataGrid" class ="easyui-datagrid" data-options=""></table>
  <div id="tb" style="padding: 3px;">
    <div style="margin-bottom: 0px">
      <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
      <a href="javascript:void(0)" id="clearBtn" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#searchForm'" style="width:60px;">重置</a>
      &nbsp;&nbsp;
      <a href="#" class="easyui-linkbutton" id="acceptReliefBtn"  iconCls="pic_33" plain="true">同意</a>
      <a href="#" class="easyui-linkbutton" id="refuseReliefBtn" iconCls="pic_33" plain="true">拒绝</a>
    </div>
  </div>
</div>

<!--减免拒绝窗口-->
<div id="refuseApproveWin" class="easyui-window " title="减免拒绝"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="">
  <div class="refuseApprovePanel" style="padding:10px;">
    <div style="color:red;margin-bottom: 10px;font-size: 12px;"></div>
    <form id="refuseApproveForm">
      <input type="hidden" name="id" value=""/>
      <table>
        <tr>
          <td>
            <span>请输入拒绝原因</span>
          </td>
        </tr>
        <tr>
          <td>
            <input class="easyui-textbox" name="memo1" data-options="multiline:true"
                   style="width: 200px;height:60px;">
          </td>
        </tr>
      </table>
    </form>
    <div style="text-align:center;padding-top:20px;padding-bottom: 20px">
      <a href="javascript:void(0)" id="refuseApproveSubmitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >确定</a>
      <a href="javascript:void(0)" id="refuseApproveCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >返回</a>
    </div>
  </div>
</div>


<!--申请详情 详情 跟审批日志-->
<div id="applyReliefInfoWin" class="easyui-window" title="减免审批"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="width: 900px;height: 530px">
  <div class="easyui-tabs" id="applyReliefInfoTab" style="padding:0px" data-options="fit:true">
    <div title="申请详情" id="applyReliefInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px" data-options="fit:true">
      <iframe id = "jumpApplyReliefInfoPage" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%"></iframe>
    </div>
    <div title="操作日志" id="reliefOperateInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px" data-options="fit:true">
      <iframe id = "jumpReliefOperateInfoPage" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%" data-options="fit:true"></iframe>
    </div>
  </div>
  </div>
</body>
</html>
