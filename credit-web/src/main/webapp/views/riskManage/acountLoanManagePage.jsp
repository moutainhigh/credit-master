<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>账务债权操作</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','messager'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/riskManage/acountLoanManagePage.js');
            urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法  **/
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 80px; margin: 0px;" data-options="region:'north'">
        <form id="searchForm">
            <table cellpadding="5">
                <tr>
                    <td>姓名：</td>
                    <td><input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'借款人姓名\']'" style="width:150px;"/></td>
                    <td>手机：</td>
                    <td><input class="easyui-textbox" id="mobile" name="mobile" validType="mobile" style="width:150px;"/></td>
                    <td>身份证号：</td>
                    <td><input class="easyui-textbox" id="idNum" name="idNum" validType="idCard" style="width:150px;"/></td>
                    <td>合同编号：</td>
                    <td><input class="easyui-textbox" type="text" name="contractNum" style="width:150px;" /></td>
				</tr>
            </table>
        </form>
    </div>

    <!-- DataGrid 工具栏按钮 -->
    <div id="tb" style="padding:3px;height:30px;">
        <div style="margin-bottom:0px">
            <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
            <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>
            <a href="#" class="easyui-linkbutton" id="importBtn" iconCls="pic_42" plain="true">债权导入</a>
        </div>
    </div>
    
    <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">

        <!-- 表格标签 -->
        <table id="acountLoanManageDataGrid"></table>
    </div>

    <!-- Excel 导入 -->
    <div id="importExcelWin" class="easyui-window editContentPanel" title="Excel导入" data-options="closed : true,collapsible : false,minimizable : false,
											maximizable : false,modal : true,resizable : false" style="padding:0px;">
        <div style="text-align:center;padding:10px;" >
            <h3>账务债权操作导入</h3>
            <form  id="creditFileForm"  enctype="multipart/form-data" method="post">
                <input class="easyui-filebox" name="uploadFile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                <a href="javascript:void(0)" id="importExcelBut" class="easyui-linkbutton" data-options="iconCls:'pic_52'" >导入</a>
            </form>
        </div>
    </div>
</body>
</html>
