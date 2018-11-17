<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>首页</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<style type="text/css">
    #loanDistributeAmountReport {
     float: left;
     width: 50%;
    }
    #loanDistributeQuantityReport {
     float: right;
     width: 50%;
    }
    #container {
     width: 100%;
    }
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','form','datebox','validatebox'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/amcharts/amcharts.js');
            urlJs.push(global.contextPath + '/resources/amcharts/serial.js');
            urlJs.push(global.contextPath + '/resources/amcharts/pie.js');
            urlJs.push(global.contextPath + '/resources/amcharts/themes/light.js');
            urlJs.push(global.contextPath + '/resources/amcharts/serial.js');
            urlJs.push(global.contextPath + '/resources/js/main.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                
            });
        });
    });
    
    // 放款业绩走势统计信息
    var loanPerformance = <%=request.getAttribute("loanPerformance")%>;
    // 放款分布情况统计信息
    var loanDistribute = <%=request.getAttribute("loanDistribute")%>;
    // 当前月
    var currenMonth = '<%=request.getAttribute("currenMonth")%>';
    // 是否展示图表
    var ifAnyGranted = '<%=request.getAttribute("ifAnyGranted")%>';
</script>
</head>
<body class="easyui-layout">
    <jsp:include page="/views/common/initPageMast.jsp" />
    <c:if test="${ifAnyGranted eq 'false' }">
        <div id="mainDiv"></div>
    </c:if>
    <c:if test="${ifAnyGranted eq 'true' }">
        <div id="mainDiv" data-options="border:false,region:'center',noheader:true" style="overflow: auto;">
            <dir/>
            <div>
                <a href="#" class="easyui-linkbutton" id="showPerformance" iconCls="icon-add" plain="true">业绩展示</a>
            </div>
            <div id="container">
                <div id="columnDiv" style="height: 250px;"></div>
                <br />
                <div id="pieDiv" style="text-align:center">
                    <div id="pieTitle" style="font-size:20px"></div>
                    <div id="loanDistributeAmountReport" style="height: 450px;"></div>
                    <div id="loanDistributeQuantityReport" style="height: 450px;"></div>
                </div>
            </div>
        </div>
    </c:if>
</body>
</html>
