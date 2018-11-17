//** 查看客户详细资料 **//*
function personInfoTab(rowIndex) {
	var name = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].name;
	var id = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].borrowerId;
	var tab = {};
	tab.id = 'personDetail_' + id;
	tab.text = name + ' - 详细资料';
	tab.url = global.contextPath + '/person/viewPersonDetailPage' + '/' + id;
	// ** 调用父级添加选项卡方法 **//*
	parent.$.iframeTabs.add(tab);
}

function loanbaseTab(rowIndex) {
	var name = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].name;
	var id = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].id;
	var tab = {};
	tab.id = 'loanDetail_' + id;
	tab.text = name + ' - 借款详细信息';
	tab.url = global.contextPath + '/repay/repayMark/viewPersonLoanDetailPage' + '/'
			+ id;
	// ** 调用父级添加选项卡方法 **//*
	parent.$.iframeTabs.add(tab);
}
$(function() {

	// 表格数据源地址

	//var vloanInfoList = global.contextPath + '/repay/repayMark/vloanInfoList';
	var dataGridUrl = global.contextPath + '/repay/repayMark/searchLoanList';

	// 查询数据项表单实例
	var searchForm = $('#searchForm');
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	/** 操作提示 * */
	var tips = $('#tips');

	var VloaninfoDataGrid = $('#VloaninfoDataGrid');

	var pg = {
			'page' : 1,
			'rows' : pageSize
		}
	VloaninfoDataGrid
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
					field : 'appNo'
				} ] ],
				columns : [ [
						// 列定义
						{
							field : 'name',
							title : '借款人',
							width : 50//,
							//formatter : function(value, row, rowIndex) {
							//	return '<a name="idclick" onclick="personInfoTab('
							//			+ rowIndex
							//			+ ');" href="javascript:void(0);" id="idclick">'
							//			+ value + '</a>';
							//}
						},
						{
							field : 'loanType',
							title : '借款类型',
							width : 50,
							formatter : function(value, row, rowIndex) {
								return '<a name="loanbaseclick" onclick="loanbaseTab('
										+ rowIndex
										+ ');" href="javascript:void(0);" id="loanbaseclick">'
										+ value + '</a>';
							}
						}, {
							field : 'personInfoName',
							title : '客户经理',
							width : 50
						}, {
							field : 'salesManName',
							title : '客服',
							width : 50
						}, {
							field : 'idnum',
							title : '身份证号',
							width : 50
						}, {
							field : 'profession',
							title : '职业类型',
							width : 50
						}, {
							field : 'purpose',
							title : '用途',
							width : 50
						}, {
							field : 'pactMoney',
							title : '合同金额',
							width : 50
						}, {
							field : 'money',
							title : '审批金额',
							width : 50
						}, {
							field : 'time',
							title : '借款期限',
							width : 25
						}
						, {
							field : 'loanState',
							title : '状态',
							width : 25
						}
						,{
							field : 'contractNum',
							title : '合同编号',
							width : 45
						}] ],
				// 每页显示的记录条数，默认为10
				pageSize : pageSize,
				// 可以设置每页记录条数的列表
				pageList : pageSizeList,
				toolbar : '#tb',
				// 自定义行样式
				rowStyler : function(index, row) {
					if (index % 2 == 0) {
						// return 'background-color:#AABBCC;color:#fff;';
					}
				}
			});

	// 表格分页实例
	var testDataGridPG = VloaninfoDataGrid.datagrid('getPager');
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

		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url = dataGridUrl;
		$(VloaninfoDataGrid).datagrid('reloadData', queryParam);

	}

	/** 查询条件必须输入其中之一的校验 * */
	function check() {
		//var name = $.trim($("#name").val());
		var idNum = $.trim($("#idNum").val());
		var loanState = $('#loanState').combobox('getValue');
		var contractNum = $.trim($("#contractNum").val());
		var fundsSources = $('#fundsSources').combobox('getValue');
		
		if ($.isEmpty(idNum) && $.isEmpty(loanState)&& $.isEmpty(contractNum)&& $.isEmpty(fundsSources)) {
			return false;
		}
		// 防止输入空白查询
		//$("#name").val(name);
		//$("#idNum").val(idNum);
		return true;
	}
	
	 $("#clearCondition").bind("click",function(envent){
	    	$("#searchForm").form("reset");
     });
	// 获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = dataGrid.datagrid('getSelected');
		return selectedRow;
	}

	// 查询按钮添加事件
	$("#searchBut").click(function() {
		if (searchForm.form('validate')) {
			pg.page = 1;
			var flag = check();
			if (flag) {
				reloadDataGrid();
			} else {
				$.messager.alert('警告', '请必须输入一个查询条件!', 'warning');
			}
		}
	});
})
