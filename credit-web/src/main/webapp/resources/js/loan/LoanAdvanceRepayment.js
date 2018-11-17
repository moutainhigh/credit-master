
$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath
			+ '/system/loan/listLoanSpecialRepaymentPage';

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
	$.loanAd = {
			
	}
	$.loanAd.shengqing = function(index){
//	function shengqing(index) {
		var personId = $('#LoanAdvanceRepaymentDataGrid').datagrid('getRows')[index].personId;
		var loanid = $('#LoanAdvanceRepaymentDataGrid').datagrid('getRows')[index].loanid;
		var money = $('#LoanAdvanceRepaymentDataGrid').datagrid('getRows')[index].money;
		var id=loanid+","+personId+","+money;
		var shengqingurl=global.contextPath
		+ '/system/loan/insertLoanSpecialRepayment' ;
		$.ajaxPackage({
			type : 'get',
			url : shengqingurl + '/' + id,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				var resMsg = data.resMsg;
				$.messager.alert('结果', resMsg);
				reloadDataGrid();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown
						+ '!', 'error');
			},
			complete : function() {
				
			}
			
		});
	}
	//撤销
	$.loanAd.cx = function(index){
//	function cx(index) {
		var loanId = $('#LoanAdvanceRepaymentDataGrid').datagrid('getRows')[index].loanid;
		var cxgurl=global.contextPath
		+ '/system/loan/cxLoanSpecialRepayment' ;
		$.ajaxPackage({
			type : 'get',
			url : cxgurl + '/' + loanId,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				var resMsg = data.resMsg;
				$.messager.alert('结果', resMsg);
				reloadDataGrid();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown
						+ '!', 'error');
			},
			complete : function() {
				
			}
			
		});
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
								// 是否有撤销权限
			                    var isCanConfirmReceive = $("#isCanConfirmReceive").val();
			                    // 是否有申请权限
			                    var isApplyConfirmReceive = $("#isApplyConfirmReceive").val();
								var str = "";
								if (value == true) {
									 if(isApplyConfirmReceive=="true"){
										 str = "<a href='javascript:void(0)'  onclick='$.loanAd.shengqing("
											 + index
											 + ")' class='easyui-linkbutton loanFilesInfoUpdate' style='margin-right: 20px;'  loan-id='"
											 + row.id
											 + "' personId='"
											 + row.personId + "'>申请</a>";
									 }
								} else if (value == false) {
									 if(isCanConfirmReceive=="true"){
										 str = "<a href='javascript:void(0)' onclick='$.loanAd.cx("
											 + index
											 + ")' class='easyui-linkbutton loanFilesInfoAdd' loan-id='"
											 + row.loanId + "'>撤销</a>";
									 }
								}
								return str;
							}
						} ] ],
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
	 $("#clearCondition").bind("click",function(envent){
		    
	    	$("#searchForm").form("reset");
 });
})



	