$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/system/loan/findLoanBaseList';
	var insertUrl = global.contextPath
			+ '/system/loan/insertLoanSpecialRepaymentFYJM';

	var isYear = global.contextPath
	+ '/system/loan/findYearsqFYJM';
	
	var logUrl = global.contextPath + '/system/loan/log';
	var canalUrl = global.contextPath
			+ '/system/loan/updateLoanSpecialRepaymentFYJM';
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
	var pg = {
			'page' : 1,
			'rows' : pageSize
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
			field : 'id',
			width : 50
		},
		{
			field : 'accAmount',
			width : 50
		},
		{
			field : 'loanSpecialRepayment',
			width : 50,
			formatter : function(value, row, rowIndex) {
				if (value) {
					return value.id;
				}
			}
		}] ],
		columns : [ [
		// 列定义

		{
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
		},

		{
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
			field : 'contractNum',
			title : '合同编号',
			width : 50

		}, {
			field : 'loanState',
			title : '借款状态',
			width : 50
		}, {
			field : 'isApply',
			title : '申请状态',
			width : 50
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

	// 申请按钮添加事件
	$("#apply").click(function() {
		var selectedRow = getSelectedRow(LoanAdvanceRepaymentDataGrid);
		if (selectedRow) {
			var a=selectedRow.isApply;
			if(a=="已申请") {
				$.messager.alert('结果', '已经申请过了，不能重新申请');
				return;
			}
			var reqId = selectedRow.id;
			$.ajaxPackage({
				type : 'get',
				url : isYear + '/' + reqId,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					// 从服务器上获取到记录信息
					var attachment = data.attachment;
					if (resCode == '000000') {
						// 操作成功
						$.messager.alert('结果', resMsg);
					} else {
						// 重置表单数据
						dataForm.form('clear');
						addDataPanel.window('open');
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				},
				complete : function() {

				}
			});
		} else {
			$.messager.alert('警告', '请选中需要申请的记录!', 'warning');
		}
	});

	// 申请提交
	$("#submitBut").click(
		function() {
			var selectedRow = getSelectedRow(LoanAdvanceRepaymentDataGrid);
			if (selectedRow) {
				var penalty = selectedRow.penalty;
				var remainingInterest = selectedRow.remainingInterest;
				var loanProduct = selectedRow.loanProduct.residualPactMoney;
				var amount = $("#amount").val();
				//挂账金额
				var accAmount=selectedRow.accAmount;
				//减去挂账
				var countMoney=(penalty+remainingInterest+loanProduct)-accAmount;
				var amountAfter=amount;
				var penaltyAfter = 0;
	            var remainingInterestAfter = 0;
	            var residualPactMoneyAfter = 0;
	            if(parseFloat(amount)<=0) {
	            	$.messager.alert('结果', '减免金额应大于0，请重新输入');
					return;
            	}
				if(parseFloat(countMoney)<parseFloat(amount)) {
					$.messager.alert('结果', '减免金额应小于等于应还总额，请重新输入');
					return;
				}
				if((penalty - amount)>=0) {
					penaltyAfter = amount;
				} else {
					penaltyAfter = penalty;
					amount = amount - penaltyAfter;
					if((remainingInterest - amount)>= 0) {
						remainingInterestAfter = amount;
					} else {
						remainingInterestAfter = remainingInterest;
						amount = amount - remainingInterestAfter;
							if((loanProduct -amount)>=0) {
								residualPactMoneyAfter = amount;
								residualPactMoneyAfter= Math.round((parseFloat(residualPactMoneyAfter))*100)/100;//保留2位小数。
							}
						}
				}

				$.messager.confirm('请确认', '确认申请减免金额' + Number(amountAfter).toFixed(2) + "\n金额减免顺序：违约金-" +  Number(penaltyAfter).toFixed(2)  + "、利息-"	+  Number(remainingInterestAfter).toFixed(2) + "、本金-" +  Number(residualPactMoneyAfter).toFixed(2),
					function(r) {
						if (r) {
							// 请求服务端获取某条记录信息
							var reqId = selectedRow.id;
							$.ajaxPackage({
								type : 'get',
								url : insertUrl + '/' + reqId,
								dataType : "json",
								data : dataForm.serialize(),
								success : function(data, textStatus,
										jqXHR) {
									var resCode = data.resCode;
									var resMsg = data.resMsg;
									// 从服务器上获取到记录信息
									var attachment = data.attachment;
									if (resCode == '000000') {
										// 操作成功
										$.messager.alert('结果', resMsg);
										addDataPanel.window('close');
										reloadDataGrid();
									} else {
										// 操作失败
										$.messager.alert('结果', resMsg);
									}
								},
								error : function(XMLHttpRequest, textStatus, errorThrown, d) {
									$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
								},
								complete : function() {

								}
							});

						}
					});
			} else {
				$.messager.alert('警告', '请选中需要申请的记录!', 'warning');
			}
		});

	// 取消
	$("#cancel").click(
		function() {
			var selectedRow = getSelectedRow(LoanAdvanceRepaymentDataGrid);
			// 请求服务端获取某条记录信息
			var loanId = selectedRow.id;
			var isApply = selectedRow.isApply;
			if(isApply=="未申请") {
				$.messager.alert('结果', '记录未申请，请重新选择');
				return;
			}
			$.ajaxPackage({
				type : 'get',
				url : canalUrl + '/' + loanId,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					// 从服务器上获取到记录信息
					var attachment = data.attachment;
					if (resCode == '000000') {
						// 操作成功
						$.messager.alert('结果', resMsg);
					
					} else {
						// 操作失败
						$.messager.alert('结果', resMsg);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				},
				complete : function() {
					reloadDataGrid();
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
	//reloadDataGrid();

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
