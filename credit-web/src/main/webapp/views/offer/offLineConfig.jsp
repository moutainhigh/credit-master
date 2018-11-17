<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="expires" content="0"/>
        <title>行别行号配置</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css" rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                              ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
                    urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
                    urlJs.push(global.contextPath + '/resources/js/offer/offLineConfig.js');
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
        <div class="easyui-panel" id="tb" style="padding: 5px;">
            <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
            <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <sec:authorize ifAnyGranted="/offLineOffer/offLineConfig/setRegionType">
                <span>
                    <label>银行卡所属地区：</label>
                    <select class="easyui-combobox" id="selectRegionType" name="selectRegionType" style="width:120px" data-options="editable:false,panelHeight:'auto'">
                        <option value="">全部</option>
                        <option value="01">深圳地区</option>
                        <option value="99">非深圳地区</option>
                    </select>
                </span>
                <a href="#" class="easyui-linkbutton" id="settingBtn" iconCls="icon-ok" plain="true">设置行别行号</a>
            </sec:authorize>
        </div>
        
        <div class="easyui-panel" title="查询条件" style="padding:5px;height:110px;margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr >
                        <td>客户姓名：</td>
                        <td>
                            <input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'客户姓名\']'" style="width:150px"/>
                        </td>
                        <td>身份证号：</td>
                        <td>
                            <input class="easyui-textbox" id="idNum" name='idNum' validType="idCard" style="width:150px"/>
                        </td>
                        <td>合同编号：</td>
                        <td><input class="easyui-textbox" id="contractNum" name="contractNum" style="width: 150px;"/></td>
                    </tr>
                    <tr>
                        <td>合同来源：</td>
                        <td>
                            <select class="easyui-combobox" id='fundsSource' name="fundsSource" style="width:150px" data-options="editable:false,panelHeight:'auto'">
                                <option value="">全部</option>
                                <c:forEach var="fundsSource" items="${fundsSources}">
                                    <option value="${fundsSource}">${fundsSource}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>银行卡所属地区：</td>
                        <td>
                            <select class="easyui-combobox" id="regionType" name="regionType" style="width:150px" data-options="editable:false,panelHeight:'auto'">
                                <option value="">全部</option>
                                <option value="01">深圳地区</option>
                                <option value="99">非深圳地区</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <!-- 表格标签 -->
        <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
            <table id="dataGrid" class="easyui-datagrid"></table>
        </div>
        
    </body>
</html>