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
					 urlJs.push(global.contextPath + '/resources/js/loan/querBatchNumJmhzDefault.js'); 
			            urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
			            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				});
			});
		</script>		
	</head>
	<body class="easyui-layout" style="width:100%; height: 100%">
		<jsp:include page="/views/common/initPageMast.jsp" />
			<input   value="${org}" id="orgTemp" type="hidden"/>
		<sec:authorize ifAnyGranted="/loan/importListFile">	
			<div data-options="region:'north',split:false,border:true,collapsible:false" style="height: 158px;">				
					<a href="javascript:void(0)" format="xls" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" style="width:130px" id="importListFileBtn">黑名单上传下载</a>				
			</div>
		</sec:authorize>
		
			    
    <div id="importListFile" class="easyui-window editContentPanel" title="黑名单上传下载" 
            data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
            style="width:550px;height:200px;padding:0px;">
        <div style="text-align:center">
            <h2>名单导入</h2>
            <form  id="baseFileForm"  enctype="multipart/form-data" method="post">
                <input class="easyui-filebox" id="uploadfile" name="uploadfile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
                <a href="#" class="easyui-linkbutton" id="batchImport"  iconCls="pic_52" plain="false">导入</a>
            </form>
        </div>
    </div>
	</body>
</html>
