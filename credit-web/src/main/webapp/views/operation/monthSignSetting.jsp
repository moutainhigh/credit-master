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
<title>月中/月末签单设置</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','form','datebox','validatebox'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/operation/monthSignSetting.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <div class="easyui-panel" title="设置条件" style="padding: 5px; height: 100px; margin: 0px;" data-options="region:'north'">
        <form id="updateForm" method="post">
            <input type="hidden" id="hideExecuteFlag" name="hideExecuteFlag" value="${executeFlag}"/>
            <table cellpadding="5">
                <tr>
                    <td>当前月份：</td>
                    <td><input class="easyui-textbox" type="text" id="nowMonth" name="nowMonth" value="${nowMonth}" readonly="readonly"/></td>
                    <td>执行月末签约特殊处理：</td>
                    <td>
                        <select class="easyui-combobox" id="executeFlag" name="executeFlag" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
                            <c:forEach var="executeFlag" items="${executeFlags}">
                                <option value="${executeFlag}">${executeFlag}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <sec:authorize ifAnyGranted="/operation/monthSignSetUp">
                            <a href="#" class="easyui-linkbutton" id="setingBtn" iconCls="icon-edit" plain="true">设置完成</a>
                        </sec:authorize>
                    </td>
                    <td></td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
