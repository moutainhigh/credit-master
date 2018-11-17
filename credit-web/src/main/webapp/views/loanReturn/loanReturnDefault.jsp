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
<title>管理门店变更</title>
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
            urlJs.push(global.contextPath + '/resources/js/loanReturn/loanReturn.js');
            importJSExt(urlJs,function(){
                /** 脚本加载成功回调方法 **/
                
            });
        });
    });
</script>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-panel" title="查询条件" style="padding: 5px; height: 90px; margin: 0px;" data-options="region:'north'">
		<form id="searchForm" style="width: 100%; height: 100%;">
			<table style="height: 100%;border-spacing:5px;table-layout:fixed;">
				<tr>
					<td style="width: 80px;text-align: left;">姓名：</td>
					<td style="width: 150px;text-align: left;">
						<input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'借款人姓名\']'"  style="width: 150px;"/></td>
					<td style="width: 80px;text-align: left;">身份证号：</td>
					<td style="width: 180px;text-align: left;">
						<input class="easyui-textbox" id="idNum" name="idNum" validType="idCard" style="width: 180px;"/></td>
					<td style="width: 80px;text-align: left;">合同来源：</td>
					<td style="width: 150px;text-align: left;">
						<select class="easyui-combobox" id="loanType" name="loanType" data-options="panelHeight:'auto' ,editable:false" style="width: 150px;">
							<option value="">全部</option>
							<option value="证大P2P">证大P2P</option>
							<option value="积木盒子">积木盒子</option>
						</select>
					</td>
					<td style="width: 80px;text-align: left;">导入日期：</td>
					<td style="width: 150px;text-align: left;">
						<input class="easyui-validatebox easyui-datebox" data-options="editable:false" type="text" id="startQueryDate" name="startQueryDate"  style="width: 150px;" />
					</td>
					<td>合同编号:</td>
					    <td style="width: 80px;text-align: left;">
					        <input  class="easyui-textbox" id="contractNum" name="contractNum" style="width: 150px;"/></td>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" style="width: 70px;">查询</a> 
						<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width: 70px;">重置</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="easyui-panel" style="padding: 0px; margin: 0px;" data-options="region:'center'">
		<!-- 表格标签 -->
		<table id="loanReturnDataGrid" data-options=""></table>
	</div>
	<div class="easyui-panel" title="批量导入" id="tb">		
			<label> 
			<sec:authorize ifAnyGranted="/loanReturn/loanReturnImport">	
				<a href="#" class="easyui-linkbutton"	id="batchImportBtnreturnLoanImport" iconCls="pic_52" plain="true">导入</a>
			</sec:authorize>
				<div class="nav">
					<b>&nbsp;债权金额合同金额汇总：</b><span id="totalAmount">0</span>&nbsp;&nbsp;&nbsp;<span
						id="totalPactMoney">0</span><br> <b>&nbsp;笔数汇总：</b><span
						id="loanreturnListTotal">0</span> </br>
				</div>
			</label>
		
		<sec:authorize ifAnyGranted="/loanReturn/digmoneyImport">	
			 <a href="#" class="easyui-linkbutton"	id="batchImportBtndigmoneyImport" iconCls="pic_52" plain="true">挖财导入</a>	
		</sec:authorize>	 	
			<div >
				<b>&nbsp;本次导入债权</b>金额<span id="sumMoney">0</span>元&nbsp;&nbsp;&nbsp;<span
					id="digMoneyListTotal">0</span>笔&nbsp;&nbsp;&nbsp; 正常债权金额<span
					id="successMoney">0</span>元&nbsp;&nbsp;&nbsp;<span
					id="successTotal">0</span>笔&nbsp;&nbsp;&nbsp; 异常债权金额<span
					id="faileMoney">0</span>元&nbsp;&nbsp;&nbsp;<span id="failTotal">0</span>笔	&nbsp;&nbsp;
				<sec:authorize ifAnyGranted="/loanReturn/downLoadExcel">	   				
				    <input type="button" id="digMoneyBtn" value="债权导出" style="display: none;" />	&nbsp;&nbsp;	
				 </sec:authorize>
				 <sec:authorize ifAnyGranted="/loanReturn/updateBatchNum">	  
					<input type="button" id="updateBatchNum" value="生成批次号" style="display: none;" />
				 </sec:authorize>
			</div>
		
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

	<!-- 挖财债权Excel导入面板 -->
	<div id="importExcelWinWC" class="easyui-window editContentPanel"
		title="Excel批量导入"
		data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true,resizable:false"
		style="width: 550px; height: 200px; padding: 0px;">
		<div style="text-align: center">
			<h2>挖财债权导入</h2>
			<form id="baseFileFormWC" enctype="multipart/form-data" method="post">
				<input class="easyui-filebox" id="uploadfileWC" name="uploadfile"
					data-options="prompt:'请选择文件...',buttonText:'选择文件'"
					style="width: 300px" /> <a href="#" class="easyui-linkbutton"
					id="batchImportWC" iconCls="pic_52" plain="false">导入</a>
			</form>
		</div>
	</div>
	<!-- Excel导入面板 -->
</body>
</html>
