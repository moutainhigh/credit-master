$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath+ '/system/loan/ZBlistLoanSpecialRepaymentPage';

	// 查询数据项表单实例
	var searchForm = $('#searchForm');
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	/** 操作提示 * */
	var tips = $('#tips');

	var LoanAdvanceRepaymentDataGrid = $('#LoanAdvanceRepaymentDataGrid');

	pg = {
		'page' : 1,
		'rows' : 10
	}

	LoanAdvanceRepaymentDataGrid
			.datagrid({
				pg : pg,
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
							field : 'name',
							title : '借款人',
							width : 50

						},
						{
							field : 'idnum',
							title : '身份证号',
							width : 50

						},
						{
							field : 'loanType',
							title : '借款类型',
							width : 50
						},
						{
							field : 'signDate',
							title : '签约日期',
							width : 50,
							formatter : $.DateUtil.dateFormatToStr
						},
						{
							field : 'loanState',
							title : '借款状态',
							width : 50
						}
						,
						{
							field : 'creator',
							title : '申请人',
							width : 50
						},
						{
							field : 'contractNum',
							title : '合同编号',
							width : 50
						},
						{
							field : 'flage',
							title : '操作',
							align : "left",
							halign : "center",
							width : "12%",
							formatter : function(value, row, index) {
								var	str = "";
								if(row!=null&&row.loanid!=null ){
									str = "<a href='javascript:void(0)' class='easyui-linkbutton print' style='margin-right: 20px;'  data-loan-id='"+ row.loanid+ "' data-index="+ index + ">打印结清证明</a>";	
								}
								return str;
							}
						} ] ],
				// 每页显示的记录条数，默认为10
				pageSize : pageSize,
				// 可以设置每页记录条数的列表
				pageList : pageSizeList,
				toolbar : '#tb',
				// 自定义行样式
				rowStyler : function(index, row) {
					if (index % 2 == 0) {
					}
				}
			});
	// 查询按钮添加事件
	$("#searchBut").click(function() {
		if (searchForm.form('validate')) {
			pg.page = 1;
			reloadDataGrid();
		}
	});
	// 表格分页实例
	var testDataGridPG = LoanAdvanceRepaymentDataGrid.datagrid('getPager');
	testDataGridPG.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			pg.page = pageNumber;
			pg.rows = pageSize;
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
		queryParam.url = dataGridUrl;
		$(LoanAdvanceRepaymentDataGrid).datagrid('reloadData', queryParam);

	}
	reloadDataGrid();

	// 获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = dataGrid.datagrid('getSelected');
		return selectedRow;
	}
	
    /** 单击打印结清按钮事件 **/
    $(document).on("click",".easyui-linkbutton.print",function(event) {
    	var loanid = $(this).data("loanId");
    	top.$.iframeTabs.add({
    		id:"ZBLoanAdvanceRepayment-print-page-"+loanid,
    		text:"打印结清证明",
    		url:global.contextPath+"/system/loan/printPage/"+loanid
    	});
    });
    
    $("#clearCondition").bind("click",function(envent){
	    
    	$("#searchForm").form("reset");
});
})
