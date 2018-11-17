<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <title>减免管理-减免申请</title>
  <jsp:include page="/views/common/headIncludeFile.jsp" />
  <style type="text/css">
  </style>

  <script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
      $(function() {
        var urlJs = [];
        urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
        urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
        urlJs.push(global.contextPath + '/resources/js/specialRepayment/applyReliefProcessed.js?v1');
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
        <td>审批环节:</td>
        <td>
          <select class="easyui-combobox" id="nodeId" name="nodeId" style="width: 173px;">
            <option value="">请选择</option>
            <c:forEach var="nodeInfo" items="${nodeInfos}">
              <option value="${nodeInfo.id}">${nodeInfo.nodeName}</option>
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
  <table id="applyReliefProcessDataGrid" class ="easyui-datagrid" data-options=""></table>
  <div id="tb" style="padding: 3px;">
    <div style="margin-bottom: 0px">
      <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
      <a href="javascript:void(0)" id="clearBtn" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true,formId:'#searchForm'" style="width:60px;">重置</a>
      <a href="javascript:void(0)" id="createApplyRelief" class="easyui-linkbutton" iconCls="icon-add" plain="true">新建</a>
<sec:authorize ifAnyGranted="/applyReliefRepayManager/jumpApplyReliefInfo/2">
      <a href="javascript:void(0)" id="createSpApplyRelief" class="easyui-linkbutton" iconCls="icon-add" plain="true">新建特殊减免</a>
  </sec:authorize>
<sec:authorize ifAnyGranted="/applyReliefRepayManager/importReliefPenaltyStateFile">
      <a href="javascript:void(0)" id="importBut" class="easyui-linkbutton"   data-options="iconCls:'pic_51',plain:true">导入特殊减免</a>
  </sec:authorize>
      &nbsp;&nbsp;&nbsp;&nbsp;
    </div>
  </div>
</div>

<!--减免录入窗口-->
<div id="applyReliefWin" class="easyui-window " title="减免申请"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="">
  <div class="applyReliefPanel" style="padding:10px;">
    <div style="color:red;margin-bottom: 10px;font-size: 12px;"></div>
    <form id="applyReliefForm">
      <table>
        <tr>
          <td style="width:100px">合同编号：</td>
          <td><input class="easyui-textbox" type="text" name="contractNum" id="applyReliefContractNum"/></td>
        </tr>
        <tr >
          <td style="width:100px">客户姓名：</td>
          <td>
            <input class="easyui-textbox" type="text" name="name" id="applyReliefName"/>
          </td>
        </tr>
        <tr >
          <td style="width:100px">证件号码：</td>
          <td>
            <input class="easyui-textbox" type="text" name="idNum" id="applyReliefidNum"/>
          </td>
        </tr>
        <tr >
          <td style="width:100px">减免类型：</td>
          <td>
              <select class="easyui-combobox" id="applyReliefWinType" name="applyReliefType" style="width: 170px" data-options="editable:false,panelHeight:'auto'">
              <c:forEach var="applyReliefType" items="${applyReliefTypes}">
                <c:choose>
                  <c:when test="${applyReliefType.code=='01'}">
                    <option value="${applyReliefType.code}"  selected>${applyReliefType.value}</option>
                  </c:when>
                  <c:otherwise>
                    <option value="${applyReliefType.code}" >${applyReliefType.value}</option>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </select>
          </td>
        </tr>
      </table>
    </form>
    <div style="text-align:center;padding-top:20px;padding-bottom: 20px">
      <a href="javascript:void(0)" id="applyReliefSubmitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >确定</a>
      <a href="javascript:void(0)" id="applyReliefCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >返回</a>
    </div>
  </div>
</div>

<!--减免取消窗口-->
<div id="cancelApplyWin" class="easyui-window " title="取消减免"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="">
  <div class="applyReliefPanel" style="padding:10px;">
    <div style="color:red;margin-bottom: 10px;font-size: 12px;"></div>
    <form id="cancelApplyForm">
      <input type="hidden" name="id" value=""/>
      <table>
        <tr>
          <td>
            <span>请输入取消原因</span>
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
      <a href="javascript:void(0)" id="cancelApplySubmitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >确定</a>
      <a href="javascript:void(0)" id="cancelApplyCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >返回</a>
    </div>
  </div>
</div>

<!--申请减免 详情 跟审批日志-->
<div id="applyReliefInfoWin" class="easyui-window" title="减免申请"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="width: 900px;height: 530px">
  <div class="easyui-tabs" id="applyReliefInfoTab" style="padding:0px" data-options="fit:true">
    <div title="申请详情" id="appleReliefInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px" data-options="fit:true">
      <iframe id = "jumpApplyReliefInfoPage" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%"></iframe>
    </div>
    <div title="操作日志" id="reliefOperateInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px" data-options="fit:true">
      <iframe id = "jumpReliefOperateInfoPage" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%" data-options="fit:true"></iframe>
    </div>
  </div>
  </div>

<!--特殊减免录入窗口-->
<div id="spApplyReliefWin" class="easyui-window " title="特殊减免申请"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="">
  <div class="spApplyReliefPanel" style="padding:10px;">
    <div style="color:red;margin-bottom: 10px;font-size: 12px;"></div>
    <form id="spApplyReliefForm">
      <table>
        <tr>
          <td style="width:100px">合同编号：</td>
          <td><input class="easyui-textbox" type="text" name="contractNum" id="spApplyReliefContractNum"/></td>
        </tr>
        <tr >
          <td style="width:100px">客户姓名：</td>
          <td>
            <input class="easyui-textbox" type="text" name="name" id="spApplyReliefName"/>
          </td>
        </tr>
        <tr >
          <td style="width:100px">证件号码：</td>
          <td>
            <input class="easyui-textbox" type="text" name="idNum" id="spApplyReliefidNum"/>
          </td>
        </tr>
<%--        <tr >
          <td style="width:100px">减免类型：</td>
          <td>
            <select class="easyui-combobox" id="spApplyReliefWinType" name="applyReliefType" style="width: 170px" data-options="editable:false,panelHeight:'auto'">
                    <option value="1"  selected>特殊减免</option>
            </select>
          </td>
        </tr>--%>
      </table>
    </form>
    <div style="text-align:center;padding-top:15px;padding-bottom: 15px">
      <a href="javascript:void(0)" id="spApplyReliefSubmitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >确定</a>
      <a href="javascript:void(0)" id="spApplyReliefCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >返回</a>
    </div>
  </div>
</div>

<!-- Excel 导入 -->
<div id="importExcelWin" class="easyui-window editContentPanel" title="Excel导入" data-options="closed : true,collapsible : false,minimizable : false,
											maximizable : false,modal : true,resizable : false" style="padding:0px;">
  <div style="text-align:center;padding:10px;" >
    <h3>特殊减免申请批量导入</h3>
    <form  id="fileForm"  enctype="multipart/form-data" method="post">
      <input class="easyui-filebox" name="uploadFile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px">
      <a href="javascript:void(0)" id="importExcelBut" class="easyui-linkbutton" data-options="iconCls:'pic_52'" >导入</a>
    </form>
  </div>
  <br/>
</div>
<!-- Excel 导入 -->

<!--申请减免 详情 跟审批日志-->
<div id="spApplyReliefInfoWin" class="easyui-window" title="特殊减免申请"
     data-options="closed : true,resizable : false,collapsible : false,minimizable : false,modal : true,
											maximizable : false,iconCls : 'icon-save'" style="width: 900px;height: 530px">
  <div class="easyui-tabs" id="spApplyReliefInfoTab" style="padding:0px" data-options="fit:true">
    <div title="申请详情" id="spAppleReliefInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px" data-options="fit:true">
      <iframe id = "spJumpApplyReliefInfoPage" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%"></iframe>
    </div>
  </div>
</div>
</body>
</html>
