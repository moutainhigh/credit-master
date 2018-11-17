<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>债权标识-随手记</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath() %>/resources/css/sysIcon.css" />
<style type="text/css">
</style>
<script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','form','datebox','validatebox'],'business', function() {
        $(function() {
            var urlJs = [];
            urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
            urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
            urlJs.push(global.contextPath + '/resources/js/loan/loanSsj.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-panel" title="批量导入" id="tb" style="height:230px;margin-top:30px;">
		<sec:authorize ifAnyGranted="/loan/loanSsjImport">	
			 <a href="#" class="easyui-linkbutton"	id="ssjImportBtn" iconCls="pic_52" plain="true" style="margin-left:10px;">EXCEL导入</a>	
		</sec:authorize>	 	
		<div class="nav" style="margin-top:10px;margin-left:10px;">
				本次导入债权金额<span id="sumMoney" style="margin-left:30px;margin-right:10px;">0</span>元
				<span id="digMoneyListTotal"  style="margin-left:30px;margin-right:10px;">0</span>笔 ,
				正常债权金额<span id="successMoney" style="margin-left:30px;margin-right:10px;">0</span>元
				<span id="successTotal" style="margin-left:30px;margin-right:10px;">0</span>笔,
				 异常债权金额<span id="faileMoney" style="margin-left:30px;margin-right:10px;">0</span>元
				 <span id="failTotal" style="margin-left:30px;margin-right:10px;">0</span>笔
			&nbsp;&nbsp;
			
			<sec:authorize ifAnyGranted="/loan/downSsj">
			    <a href="#" class="easyui-linkbutton" id="digSsjBtn" iconCls="pic_51" plain="true" style="display: none;">EXCEL导出</a>		   				
			    &nbsp;&nbsp;	
			 </sec:authorize>
			 <sec:authorize ifAnyGranted="/loan/updateBatchNumSsj">	 
			    <a href="#" class="easyui-linkbutton" id="updateBatchNumSsj" iconCls="icon-save" plain="true" style="display: none;">生成批次号</a> 
			 </sec:authorize>
		</div>
	</div>

	<!-- Excel导入面板 -->

	<!-- 随手记债权Excel导入面板 -->
	<div id="importExcelWinSsj" class="easyui-window editContentPanel"
		title="Excel批量导入"
		data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
		style="width: 550px; height: 200px; padding: 0px;">
		<div style="text-align: center">
			<h2>导入</h2>
			<form id="baseFileFormSsj" enctype="multipart/form-data" method="post">
				<input class="easyui-filebox" id="uploadfileSsj" name="uploadfile"
					data-options="prompt:'请选择文件...',buttonText:'选择文件'"
					style="width: 300px" /> <a href="#" class="easyui-linkbutton"
					id="batchImportSsj" iconCls="pic_52" plain="false">导入</a>
			</form>
		</div>
	</div>
	<!-- Excel导入面板 -->
</body>
</html>
