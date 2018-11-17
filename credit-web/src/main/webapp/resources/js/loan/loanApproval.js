
$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/system/loan/approvalList';
	var insertUrl = global.contextPath + '/system/loan/approvalSuccess';

	var logUrl = global.contextPath + '/system/loan/log';
	var canalUrl = global.contextPath + '/system/loan/approvalNO';
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
	pg = {
		'page' : 1,
		'rows' : 10
	}

	LoanAdvanceRepaymentDataGrid.datagrid({
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
			field : 'loanSpecialRepayment',
			width : 50,
			formatter : function(value, row, rowIndex) {
				if (value) {
					return value.id;
				}
			}
		}, {
			field : 'specialRepaymentState',
			width : 50,
			formatter : function(value, row, rowIndex) {
				return row.loanSpecialRepayment.specialRepaymentState;
			}
		} ] ],
		columns : [ [
		// 列定义
		{
			field : 'id',
			title : '合同号',
			width : 50

		}, {
			field : 'loanContract',
			title : '借款人',
			width : 50,
			formatter : function(value, row, rowIndex) {

				if (value) {
					return value.borrowerName;
				}
			}
		}, {
			field : 'idnum',
			title : '身份证号',
			width : 50,
			formatter : function(value, row, rowIndex) {
				return row.loanContract.idnum;
			}
		},

		{
			field : 'loanInitialInfo',
			title : '借款类型',
			width : 50,
			formatter : function(value, row, rowIndex) {
				if (value) {
					return value.loanType;
				}
			}
		}, {
			field : 'pactMoney',
			title : '合同金额',
			width : 50,
			vType : 'rmb',
			formatter : function(value, row, rowIndex) {
				return row.loanContract.pactMoney;
			}
		}, {
			field : 'loanProduct',
			title : '剩余本金',
			width : 50,
			vType : 'rmb',
			formatter : function(value, row, rowIndex) {
				return row.loanProduct.residualPactMoney;
			}
		},

		{
			field : 'remainingInterest',
			title : '剩余利息',
			width : 50,
			vType : 'rmb'
		}, {
			field : 'bxcount',
			title : '剩余本息和',
			width : 50,
			vType : 'rmb'
		}, {
			field : 'penalty',
			title : '违约金',
			width : 50,
			vType : 'rmb'
		}, {
			field : 'time',
			title : '借款期限',
			width : 50,
			formatter : function(value, row, rowIndex) {
				return row.loanProduct.time;
			}
		}, {
			field : 'currentTerm',
			title : '当前期数',
			width : 50

		}, {
			field : 'loanState',
			title : '借款状态',
			width : 50
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

	// 新增面板参数定义
	addDataPanel.window({
		width : 300,
		height : 200,
		// 定义窗口是不是模态窗口
		modal : true,
		// 定义是否显示折叠按钮
		collapsible : false,
		// 定义是否显示最小化按钮
		minimizable : false,
		// 定义是否显示最大化按钮
		maximizable : false,
		// 定义是否显示关闭按钮
		closable : true,
		// 定义是否关闭了窗口
		closed : true,
		// 定义是否窗口能被拖拽
		draggable : true,
		// 定义是否窗口可以调整尺寸
		resizable : false,
		// 如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
		shadow : true,
		// 定义如何放置窗口 true 就放在它的父容器里 false 就浮在所有元素的顶部
		inline : true,
		// 样式定义
		iconCls : 'icon-save'
	})

	// 查询按钮添加事件
	$("#searchBut").click(function() {
		if (searchForm.form('validate')) {
			pg.page = 1;
			reloadDataGrid();
		}
	});

	// 申请提交
	$("#apply").click(function() {
		var selectedRow = getSelectedRow(LoanAdvanceRepaymentDataGrid);
		if (!selectedRow) {
			$.messager.alert('警告', '请选中需要申请的记录!', 'warning');
			return;
		}
		// 请求服务端获取某条记录信息
		var speId = selectedRow.loanSpecialRepayment.id;
		$.ajaxPackage({
			type : 'get',
			url : insertUrl + '/' + speId,
			dataType : "json",
			data : dataForm.serialize(),
			success : function(data, textStatus, jqXHR) {
				var resCode = data.resCode;
				var resMsg = data.resMsg;
				// 从服务器上获取到记录信息
				var attachment = data.attachment;
				if (resCode == '000000') {
					// 操作成功
					$.messager.alert('结果',resMsg);
					addDataPanel.window('close');
					reloadDataGrid();
				} else {
					// 操作失败
					$.messager.alert('结果',resMsg);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			},
			complete : function() {
				
			}
		});
	});

	// 拒绝
	$("#cancel").click(function() {
		var selectedRow = getSelectedRow(LoanAdvanceRepaymentDataGrid);
		if (!selectedRow) {
			$.messager.alert('警告', '请选中需要申请的记录!', 'warning');
			return;
		}
		// 请求服务端获取某条记录信息
		var speId = selectedRow.loanSpecialRepayment.id;
		$.ajaxPackage({
			type : 'get',
			url : canalUrl + '/' + speId,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				var resCode = data.resCode;
				var resMsg = data.resMsg;
				// 从服务器上获取到记录信息
				var attachment = data.attachment;
				if (resCode == '000000') {
					// 操作成功
					$.messager.alert('结果',resMsg);
					reloadDataGrid();
				} else {
					// 操作失败
					$.messager.alert('结果',resMsg);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			},
			complete : function() {
				
			}
		});
	});

	// 查看日志
	$('#log').click(function() {
		var selectedRow = getSelectedRow(LoanAdvanceRepaymentDataGrid);
		if (selectedRow) {
			var loanId = selectedRow.id;
			var tab = {};
			tab.id = 'loanDetail_' + loanId;
			tab.text = '日志查看';
			tab.url = logUrl + '/' + loanId;
			// ** 调用父级添加选项卡方法 **//*
			parent.$.iframeTabs.add(tab);
		}
	})

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
	// reloadDataGrid();

	// 获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = dataGrid.datagrid('getSelected');
		return selectedRow;
	}

	// 关闭按钮事件
	$("#closeBut").click(function() {
		// 找到按钮属于的Window对象
		addDataPanel.window('close');

	})

	 $("#clearCondition").bind("click", function(envent) {
		$("#searchForm").form("reset");
	});
})
