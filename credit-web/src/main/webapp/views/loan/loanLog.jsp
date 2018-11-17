<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>结清证明</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />
<script type="text/javascript">
	importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid',
			'pagination', 'form', 'tooltip', 'validatebox' ], 'business',
			function() {
				$(function() {
					var urlJs = [];
					importJSExt(urlJs, function() {
						/** 脚本加载成功回调方法 **/

						
						$(function() {

							// 表格数据源地址
							var dataGridUrl = global.contextPath
									+ '/system/loan/logList';
							
							// 查询数据项表单实例
							var searchForm = $('#searchForm');
							// 每页显示的记录条数，默认为10
							var pageSize = 10;
							// 设置每页记录条数的列表
							var pageSizeList = [ 10, 20, 30, 40, 50 ];
							/** 操作提示 * */
							var tips = $('#tips');
							// 新增|修改数据项表单实例
							var dataForm = $('#dataForm');
							var LoanAdvanceRepaymentDataGrid = $('#LoanAdvanceRepaymentDataGrid');
							var addDataPanel = $('#addDataPanel');
							$.pg = {
								'page' : 1,
								'rows' : 10
							}


							LoanAdvanceRepaymentDataGrid
									.datagrid({
										pg : $.pg,
										// 是否显示行号
										rownumbers : true,
										// 是否单选
										singleSelect : true,
										// //是否可折叠的
										collapsible : false,
										// 自适应列宽
										fitColumns : true,
										fit : true,
										// height : '100%',
										// 是否开启分页
										pagination : true,
										hideColumn : [ [ {
											field : 'appNo',
											width : 50
										} ] ],
										columns : [ [
												// 列定义
												{
													field : 'creator',
													title : '操作者',
													width : 50

												},
												{
													field : 'loanFlowState',
													title : '状态',
													width : 50

												},
												{
													field : 'createTime',
													title : '操作时间',
													width : 50,
													formatter : $.DateUtil.dateFormatToFullStr
												},
												{
													field : 'content',
													title : '日志内容',
													width : 50
												}] ],
										// 每页显示的记录条数，默认为10
										pageSize : pageSize,
										// 可以设置每页记录条数的列表
										pageList : pageSizeList,
										// 自定义行样式
										rowStyler : function(index, row) {
											if (index % 2 == 0) {
											}
										}
									});
							
							// 表格分页实例
							var testDataGridPG = LoanAdvanceRepaymentDataGrid.datagrid('getPager');
							testDataGridPG.pagination({
								onSelectPage : function(pageNumber, pageSize) {
									$.pg.page = pageNumber;
									$.pg.rows = pageSize;
									reloadDataGrid();
								}
							});

							// 加载表格数据
							function reloadDataGrid() {
								// 获取查询表单数据转换成JSON对象
								var searchMsg = searchForm.serialize();
								/** 对参数进行解码(显示中文) * */
								searchMsg = decodeURIComponent(searchMsg);
								var queryParam = $.serializeToJsonObject(searchMsg) || {};
								var loanId= '<%=request.getAttribute("loanId")%>';
								queryParam.url = dataGridUrl+'/' + loanId;
								$(LoanAdvanceRepaymentDataGrid).datagrid('reloadData', queryParam);

							}
							reloadDataGrid();

							// 获取选中的行
							function getSelectedRow(dataGrid) {
								var selectedRow = dataGrid.datagrid('getSelected');
								return selectedRow;
							}
							
						})

					});
				});
			});
</script>

</head>
<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div
		data-options="border:false,region:'center',title:'费用减免申请',noheader:true">
		<table id="LoanAdvanceRepaymentDataGrid" class="easyui-datagrid"
			title="合同日志" style="width: 100%; height: 100%"
			data-options="border:true,noheader:true,singleSelect:true,collapsible:true,toolbar:'#tb'" />

	</div>

	
</body>
</html>
