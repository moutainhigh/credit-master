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
        <title>债权导出供第三方</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                              ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
                $(function() {
                    var urlJs = [];
                       urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
                        urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
                     urlJs.push(global.contextPath + '/resources/js/loan/loanExternalDebt.js?v2'); 
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
                        <td style="width: 60px;text-align: left;">查询条件：</td>
                        <td style="width: 150px;text-align: left;">
                            <select name="querySelect" id="querySelect" class="easyui-combobox" data-options ="editable:false, panelHeight:'auto'" style="width: 130px;">
                                <c:choose>
                                  <c:when test="${userCode=='wc2'}">
                                    <option value="1">查询批次号</option>
                                  </c:when>
                                  <c:when test="${userCode=='lxxd'}">
                                    <option value="1">查询批次号</option>
                                  </c:when>
                                  <c:when test="${userCode=='wmxt'}">
                                    <option value="1">查询批次号</option>
                                  </c:when>
                                  <c:when test="${userCode=='wm2'}">
                                    <option value="1">查询批次号</option>
                                  </c:when>
                                  <c:when test="${userCode=='bsyh'}">
                                    <option value="1">查询批次号</option>
                                  </c:when>
                                  <c:otherwise>
                                   <option value="1">查询批次号</option>
                                   <option value="0">查询当天可生成批次</option>
                                  </c:otherwise>
                                </c:choose>
                            </select>
                        </td>
                        <td style="width: 60px;text-align: left;">理财机构：</td>
                        <td style="width: 150px;text-align: left;">
                            <select class="easyui-combobox" data-options ="editable:false, panelHeight:'auto'" name="financialorg" id="financialorg" style="width: 130px;">
                                <c:choose>
                                  <c:when test="${userCode=='wc2'}">
                                    <option value="WC2-">挖财2</option>
                                  </c:when>
                                  <c:when test="${userCode=='lxxd'}">
                                    <option value="LXXD">龙信小贷</option>
                                  </c:when>
                                  <c:when test="${userCode=='wmxt'}">
                                    <option value="WMXT">外贸信托</option>
                                  </c:when>
                                  <c:when test="${userCode=='wm2'}">
                                    <option value="WM2-">外贸2</option>
                                  </c:when>
                                  <c:when test="${userCode=='bsyh'}">
                                    <option value="BSYH">包商银行</option>
                                  </c:when>
                                  <c:otherwise>
                                  <option value="XL">新浪财富</option>
                                  <option value="WC">挖财财富</option>
                                  <option value="AT">证大爱特</option>
                                  <option value="JMHZ">积木盒子</option>
                                  <option value="SSJ">随手记</option>
                                  <option value="HM">海门小贷</option>
                                  <option value="WC2-">挖财2</option>
                                  <option value="LXXD">龙信小贷</option>
                                  <option value="WMXT">外贸信托</option>
                                  <option value="BHXT">渤海信托</option>
                                  <option value="WM2-">外贸2</option>
                                  <option value="BH2-">渤海2</option>
                                  <option value="BSYH">包商银行</option>
                                  <option value="HRBH">华瑞渤海</option>
                                  </c:otherwise>
                                </c:choose>
                            </select>
                        </td>
                        <td style="width: 84px;text-align: left;" id="startQueryDate">债权导出日期：</td>
                        <td style="width: 220px;text-align: left;" id="startQueryDateValue">
                            <input class="easyui-validatebox easyui-datebox" type="text" id="grantBeginDate" name="grantBeginDate" data-options="editable: false" value="${monthStartDate}" style="width: 100px;"/> ~  
                            <input class="easyui-validatebox easyui-datebox" type="text" id="grantEndDate" name="grantEndDate" data-options="editable: false" value="${currentDate}" style="width: 95px;"/>
                        </td>
                        <td style="width: 60px;text-align: left;">批次号：</td>
                        <td style="width: 150px;text-align: left;">
                            <input class="easyui-validatebox easyui-textbox" type="text" id="batchNum" name="batchNum" style="width: 150px;"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div style="padding: 3px;" class="datagrid-toolbar">
            <div id="bottonBox" style="margin-bottom: 0px">
                <a href="javascript:void(0)" format="html" class="easyui-linkbutton" id="searchButs" data-options="iconCls:'icon-search',plain:true" style="width:60px">查询</a>
                <sec:authorize ifAnyGranted="/loan/eloanExportJmhz">
                    <a href="javascript:void(0)" format="xls" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" style="width:90px" id="eloanExportBtn">导出表格</a>
                </sec:authorize>
                <sec:authorize ifAnyGranted="/loan/getLoanBatchImg">
                    <a href="javascript:void(0)" format="xls" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" style="width:105px" id="batchDownloadBtn">附件批量下载</a>
                </sec:authorize>
                <sec:authorize ifAnyGranted="/loan/createBatchNum">
                    <a href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" style="width:90px" id="createBatchNum">生成批次</a>
                </sec:authorize>
                <sec:authorize ifAnyGranted="/loan/updateBatchNum">
                    <a href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" style="width:90px" id="updateBatchNumBtn">更新批次</a>
                </sec:authorize>
                <a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>
            </div>
        </div>
    </div>
    <div data-options="border:false,region:'center',noheader:true,panelHeight:'auto'">
        <iframe id="listLoanBaseDefault" name="listLoanBaseDefault" style="width:100%;height:100%;" frameborder="0"></iframe>
    </div>
    </body>
</html>
