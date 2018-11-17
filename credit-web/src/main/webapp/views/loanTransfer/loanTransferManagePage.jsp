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
<title>债权转让管理</title>
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
            urlJs.push(global.contextPath + '/resources/js/loanTransfer/loanTransferManagePage.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div data-options="region:'north',split:false,border:true,collapsible:false" style="height: 115px;">
		<div class="easyui-panel" title="查询条件" style="padding: 5px; height:80px; margin: 0px;" data-options="region:'north'">
			<form id="searchForm" style="width: 100%; height: 100%;">
				<table style="height: 100%;border-spacing:5px;table-layout:fixed;">
					<tr>
						<td>合同编号:</td>
						<td style="width: 70px;text-align: left;">
							<input  class="easyui-textbox" id="contractNum" name="contractNum" style="width: 180px;"/>
						</td>
						<td style="width: 90px;text-align: left;">转让批次：</td>
						<td style="width: 150px;text-align: left;">
							<!-- <select class="easyui-combobox" id="transferBatch" name="transferBatch" data-options="panelHeight:'auto' ,editable:false" style="width: 150px;">
								<option value=''>请选择</option>
								<c:forEach items="${transferBatchs}" var="transferBatch">
									<option value="${transferBatch.batchs}">${transferBatch.batchs}</option>
								</c:forEach>
							</select>
							 -->
							 <select class="easyui-combobox" name="transferBatch" id="transferBatch" data-options="panelHeight:'auto'" style="width:180px;"></select>
							
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="padding: 3px;" class="datagrid-toolbar">
			<a href="#" class="easyui-linkbutton"   id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>		
			<a href="#" class="easyui-linkbutton"	id="importLoanTransferBtn" iconCls="pic_52" plain="true">导入</a>
			<a href="#" class="easyui-linkbutton"	id="exportLoanTransferBtn" iconCls="pic_51" plain="true">导出</a>
		</div>
	</div>
	
	<!-- 债权转让Excel导入面板 -->
	<div id="importExcelWin" class="easyui-window editContentPanel"	title="Excel导入" 
		data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
		style="width: 500px; height: 120px; padding: 0px;">
		<h2></h2>
		<div style="text-align: center">
			<form id="baseFileForm" enctype="multipart/form-data" method="post">
				<input class="easyui-filebox" id="uploadFile" name="uploadFile"
					data-options="prompt:'请选择文件...',buttonText:'选择文件'"
					style="width: 300px" /> <a href="#" class="easyui-linkbutton"
					id="confirmImportBtn" iconCls="pic_52" plain="false">导入</a>
			</form>
		</div>
	</div>
	<!-- Excel导入面板 -->
</body>
</html>
