<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>客户档案详细</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/loan/loanFilesInfoDetailPage.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				})
			});
		</script>		
	</head>
	<body class="easyui-layout" >
	<jsp:include page="/views/common/initPageMast.jsp" />	
		<div data-options="border:false,region:'center'">
			<div style="width:780px;height:1030px;margin-left: auto;margin-right: auto;">
				<iframe id="loanFilesInfoDetail" src="<%=request.getContextPath()%>/loan/loanFilesInfoDetail?loanId=${loanId}"  style="width:780px;height:990px; border-width:0 "></iframe>
				<div style="width: 780px; height: 30px; text-align: center; margin-top: 10px">
					<a id="printDetail" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print'" style="width:80px">打印</a>
				</div>
			</div>
		</div>
	</body>
</html>
