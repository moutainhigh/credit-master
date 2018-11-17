<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>借款查询</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<script type="text/javascript">
    importPluginsExt([],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/easyui/datagrid-detailview.js');
            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
            urlJs.push(global.contextPath + '/resources/js/vloanInfo/vloanInfo.js');
            urlJs.push(global.contextPath + '/resources/js/vloanInfo/contactInfo.js');
            urlJs.push(global.contextPath + '/resources/js/common/salesManCommon.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
<jsp:include page="/views/common/initPageMast.jsp" />
    <div id="tb" style="padding: 3px;">
        <div style="margin-bottom: 0px">
            <input type="hidden" id="csUrl" value="${csUrl}"/>
            <input type="hidden" id="picUrl" value="${picUrl}"/>
            <input type="hidden" id="contactRecordUrl" value="${contactRecordUrl}"/>
            <input type="hidden" id="storeServerUrl" value="${storeServerUrl}"/>
            <input type="hidden" id="userCode" name="userCode" value="${user.userCode}"/>
            <input type="hidden" id="userName" name="userName" value="${user.name}"/>
            <a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
            <a href="#" class="easyui-linkbutton" id="clearCondition" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;">重置</a>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" id="detileBut" iconCls="pic_6" plain="true">查看附件</a>
            <a href="#" id="contactInfo" name="contactInfo" class="easyui-linkbutton" iconCls="pic_36" plain="true">通话详单解析</a>
            <sec:authorize ifAnyGranted="/upload/fileDetailForXinDai">
                <a href="#" class="easyui-linkbutton" id="showVideoBtn" iconCls="video_5" plain="true">查看音/视频</a>
            </sec:authorize>
            <sec:authorize ifAnyGranted="/upload/fileDetailForXinDaiManage">
                <a href="#" class="easyui-linkbutton" id="editVideoBtn" iconCls="video_3" plain="true">管理音/视频</a>
            </sec:authorize>
        </div>
    </div>
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 145px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <table  cellpadding="5">
                <tr>
                    <td>姓名：</td>
                    <td><input class="easyui-textbox" name="personInfo.name" id="name" style="width:150px;"/></td>
                    <td>联系电话：</td>
                    <td><input class="easyui-textbox" name="personInfo.mphone" id="mphone" validType="mobile" style="width:150px;"/></td>
                    <td>身份证号：</td>
                    <td><input class="easyui-textbox" name="personInfo.idnum" id="idNum" validType="idCard" style="width:175px;"/></td>
                    <td>借款状态：</td>
                    <td>
                        <select class="easyui-combobox" id="loanState" id="loanState" name="loanState" data-options="editable:false" style="width: 175px;">
                            <option value="">请选择</option>
                            <c:forEach var="loanState" items="${loanStates}">
                                <option value="${loanState.value}">${loanState.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>客户经理：</td>
                    <td>
                        <input id="salesMan" name="salesmanId" class="custComboGrid" configValue="{width:150,baseParm:{employeeType:'业务员',inActive:'t'}}"/>
                    </td>
                    <td>借款类型：</td>
                    <td>
                        <select class="easyui-combobox" id="loanType" name="loanType" data-options="editable:false" style="width: 150px;">
                            <option value="">请选择</option>
                            <c:forEach var="loanType" items="${loanTypes}">
                                <option value="${loanType.value}">${loanType.value}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>管理营业部：</td>
                    <td>
                        <select class="easyui-combobox" id="salesDepartmentId" name="salesDepartmentId" style="width: 175px;">
                            <option value="">请选择</option>
                              <c:forEach var="salesDeptInfo" items="${salesTeamInfoList}">
                                <option value="${salesDeptInfo.id}">${salesDeptInfo.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>放款营业部：</td>
                    <td>
                        <select class="easyui-combobox" id="signSalesDepId" name="signSalesDepId" style="width: 175px;">
                            <option value="">请选择</option>
                            <c:forEach var="hkDeptInfo" items="${hkTeamInfoList}">
                                <option value="${hkDeptInfo.id}">${hkDeptInfo.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                  <td>合同编号：</td>
                  <td><input class="easyui-textbox" name="contractNum" id="contractNum" style="width: 150px;"/></td>
                  <td>申请件号：</td>
                  <td><input class="easyui-textbox" name="appNo" id="appNo" style="width: 150px;"/></td>
                </tr>
        </table>
        </form>
    </div>

    <div class="easyui-panel" style="padding: 0px; margin: 0px;" data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="VloaninfoDataGrid" class ="easyui-datagrid" data-options=""></table>
    </div>

    <div id="contactInfoWin" class="easyui-window" title="通话详单解析" data-options="closed:true"  style="width:700px;height:400px;padding:0px;">
        <table id="contactInfoGrid" class="easyui-datagrid" data-options=""></table>
    </div>

    <div id="contactRecordWin" class="easyui-window" title="通话记录" data-options="closed:true"  data-options="fit:true" style="width:800px;height:300px;padding:0px;">
        <iframe id="contactRecordFrame" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%"></iframe>
    </div>
</body>
</html>
