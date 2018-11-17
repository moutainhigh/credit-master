<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>借款合同客服变更</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                      ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/operation/loanContractCustomerServiceInfo.js');
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
    <!-- DataGrid 工具栏按钮 -->
    <div id="tb" style="padding:3px;height:30px;">
        <div style="margin-bottom:0px">
            <form id="updateForm" method="post">
                <input type="hidden" id="loanIds" name="loanIds"/>
                <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
                <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <label>新客服：</label>
                <input id="newCrmId" name="newCrmId"  class="custComboGrid" configValue="{width:150,baseParm:{employeeType:'客服,管理人员',inActive:'t'}}"/>
                <sec:authorize ifAnyGranted="/operation/batchUpdateLoanContractCustomerServiceInfo">
                    <a href="#" class="easyui-linkbutton" id="batchUpdateBtn" iconCls="icon-save" plain="true">批量更新</a>
                </sec:authorize>
                <sec:authorize ifAnyGranted="/operation/exportLoanContractCustomerServiceInfo">
                    <a href="#" class="easyui-linkbutton" id="exportBtn" iconCls="pic_51" plain="true">EXCEL导出</a>
                </sec:authorize>
            </form>
        </div>
    </div>
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 120px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <table cellpadding="5">
                <tr>
                    <td>姓名：</td>
                    <td><input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'借款人姓名\']'" style="width:150px;"/></td>
                    <td>手机：</td>
                    <td><input class="easyui-textbox" id="mobile" name="mobile" validType="mobile" style="width:150px;"/></td>
                    <td>身份证号：</td>
                    <td><input class="easyui-textbox" id="idNum" name="idNum" validType="idCard"/></td>
                </tr>
                <tr>
                    <td>当前客服：</td>
                    <td>
                        <input id="crmId" name="crmId"  class="custComboGrid" configValue="{width:150}"/>
                    </td>
                    <td>借款状态：</td>
                    <td>
                        <select class="easyui-combobox" id="loanState" name="loanState" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
                            <option value="">请选择</option>
                            <c:forEach var="loanState" items="${loanStates}">
                                <option value="${loanState}">${loanState}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>合同编号:</td>
				    <td><input class="easyui-textbox" id="contractNum" name="contractNum" /></td>
                </tr>
            </table>
        </form>
    </div>
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
        <!-- 表格标签 -->
        <table id="loanContractCustomerDataGrid" ></table>
    </div>
</body>
</html>
