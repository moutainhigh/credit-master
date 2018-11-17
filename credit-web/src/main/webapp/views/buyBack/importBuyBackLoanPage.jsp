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
<title>导入回购债权</title>
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
            urlJs.push(global.contextPath + '/resources/js/buyBack/importBuyBackLoanPage.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-panel" title="查询条件" style="padding: 5px; height: 110px; margin: 0px;" data-options="region:'north'">
		<form id="searchForm" style="width: 100%; height: 100%;">
			<table style="height: 100%;border-spacing:5px;table-layout:fixed;">
				<tr>
					<td style="width: 70px;text-align: left;">回购时间起:</td>
					<td style="width: 180px;text-align: left;">
						<input class="easyui-validatebox easyui-datebox" data-options="editable:false" type="text" id="start" name="start"  style="width: 150px;" />
					</td>
					<td style="width: 70px;text-align: left;">回购时间止:</td>
					<td style="width: 180px;text-align: left;">
						<input class="easyui-validatebox easyui-datebox" data-options="editable:false" type="text" id="end" name="end"  style="width: 150px;" />
					</td>
					<td style="width: 70px;text-align: left;">债权去向:</td>
					<td style="width: 150px;text-align: left;">
						<select class="easyui-combobox" id="loanBelongs" name="loanBelongs" data-options="panelHeight:'auto' ,editable:false" style="width: 150px;">
							<option value=''>请选择</option>
							<c:forEach items="${loanBelongs}" var="loanBelong">
								<option value="${loanBelong}">${loanBelong}</option>
							</c:forEach>
						</select>
					</td>
					<td style="width: 70px;text-align: left;">合同来源：</td>
					<td style="width: 150px;text-align: left;">
						<select class="easyui-combobox" id="fundsSources" name="fundsSources" data-options="panelHeight:'auto' ,editable:false" style="width: 150px;">
							<option value=''>请选择</option>
							<c:forEach items="${loanSources}" var="loanSource">
								<option value="${loanSource}">${loanSource}</option>
							</c:forEach>
						</select>
					</td>
					
				</tr>
				<tr>
					<td style="width: 70px;text-align: left;">导入时间起:</td>
					<td style="width: 180px;text-align: left;">
						<input class="easyui-validatebox easyui-datebox" data-options="editable:false" type="text" id="importStart" name="importStart"  style="width: 150px;" />
					</td>
					<td style="width: 70px;text-align: left;">导入时间止:</td>
					<td style="width: 180px;text-align: left;">
						<input class="easyui-validatebox easyui-datebox" data-options="editable:false" type="text" id="importEnd" name="importEnd"  style="width: 150px;" />
					</td>
					<td style="width: 70px;text-align: left;">身份证号：</td>
					<td style="width: 180px;text-align: left;">
					<input class="easyui-textbox" id="idnum" name="idnum" validType="idCard" style="width: 180px;"/></td>
					<td>合同编号:</td>
					<td style="width: 70px;text-align: left;">
					<input  class="easyui-textbox" id="contractNum" name="contractNum" style="width: 180px;"/></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;" data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="importBuyBackLoanDataGrid" data-options=""></table>
	</div>
	<div class="easyui-panel" title="批量导入" id="tb">
			<a href="javascript:void(0)" id="searchBtn"  class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" style="width:60px">查询</a>
			<a href="javascript:void(0)" id="clearBtn" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>		
			<label> 
			<sec:authorize ifAnyGranted="/buyBack/buyBackLoanImport">	
				<a href="#" class="easyui-linkbutton"	id="batchImportBtnreturnLoanImport" iconCls="pic_52" plain="true">导入</a>
			</sec:authorize>
			<a href="#" class="easyui-linkbutton"	id="exportBtn" iconCls="pic_51" plain="true">Excel导出</a>
			</label>	
	</div>

	<!-- 债权Excel导入面板 -->
	<div id="importExcelWin" class="easyui-window editContentPanel"
		title="Excel批量导入"
		data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
		style="width: 550px; height: 200px; padding: 0px;">
		<div style="text-align: center">
			<h2>债权导入</h2>
			<form id="baseFileForm" enctype="multipart/form-data" method="post">
				<input class="easyui-filebox" id="uploadfile" name="uploadfile"
					data-options="prompt:'请选择文件...',buttonText:'选择文件'"
					style="width: 300px" /> <a href="#" class="easyui-linkbutton"
					id="batchImport" iconCls="pic_52" plain="false">导入</a>
			</form>
		</div>
	</div>
	<!-- Excel导入面板 -->
</body>
</html>
