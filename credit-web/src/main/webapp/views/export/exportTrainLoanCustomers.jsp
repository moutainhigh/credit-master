<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>直通车放款客户质量追踪表</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'datalist', 'form', 'switchbutton', 'tooltip',
			'validatebox', 'combogrid' ], 'business', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/js/export/exportTrainLoanCustomers.js');
			importJSExt(urlJs, function() {
				/** 脚本加载成功回调方法 **/
			});
		})
	});
</script>
</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div
		data-options="region:'north',split:false,border:true,collapsible:false"
		style="height: 146px;">
		<div class="easyui-panel" title="查询条件"
			style="width: 100%; height: 111px;">
			<form id="conditionForm" style="width: 100%; height: 100%;">
				<table
					style="height: 100%; border-spacing: 5px; table-layout: fixed;">
					<tr>
						<td style="width: 72px; text-align: left;">导出日期:</td>
						<td style="width: 160px; text-align: left;"><input
							class="easyui-datebox" type="text" name="reportDate"
							id="reportDate" value="${reportDate}" style="width: 150px;"></input>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="padding: 3px;" class="datagrid-toolbar">
			<div id="bottonBox" style="margin-bottom: 0px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					id="submitBtn" iconCls="icon-search" plain="true"
					style="width: 60px;">导出</a>
			</div>
		</div>
	</div>
</body>
</html>