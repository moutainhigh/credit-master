$(function() {
	$.searchOfferTransaction = {
		/** 表格数据源地址 * */
		dataGridUrl : global.contextPath + '/fee/loanFeeTransaction/search',
		/** 导出Excel地址 * */
		exportUrl : global.contextPath + '/fee/loanFeeTransaction/exportFile',
		/** 报盘 DataGrid表格 * */
		dataGrid : $('#dataGrid'),
		/** 分页控件 * */
		pager : undefined,
		/** 查询条件数据项表单实例 * */
		searchForm : $('#searchForm'),
		/** 每页显示的记录条数，默认为10 * */
		pageSize : 10,
		/** 设置每页记录条数的列表 * */
		pageSizeList : [ 10, 20, 30, 40, 50 ],
		/** 加载表格数据 * */
		reloadDataGrid : function() {
			if (!$.searchOfferTransaction.searchForm.form('validate')) {
				return;
			}
			var queryParam = $.searchOfferTransaction.searchForm.serializeObject();
			/** 未输入查询条件不给予查询 * */
			if ($.isEmpty(queryParam.offerDateBegin) || $.isEmpty(queryParam.offerDateEnd)) {
				$.messager.alert('警告', '必须设置报盘日期范围！', 'warning');
				return;
			}
			queryParam.url = $.searchOfferTransaction.dataGridUrl;
			$.searchOfferTransaction.dataGrid.datagrid('reloadData', queryParam);
		},
		/** 加载回盘表格数据 * */
		reloadOfferTransactionDataGrid : function(offerId) {
			if (offerId) {
				var queryParam = {};
				queryParam.offerId = offerId;
				queryParam.url = $.searchOfferTransaction.searchTransactionUrl;
				$.searchOfferTransaction.offerTransactionDataGrid.datagrid('reloadData', queryParam);
			}
		},

		init : function() {
			/** 查询处理 * */
			$('#searchBtn').click(function() {
				$.searchOfferTransaction.pg.page = 1;
				$.searchOfferTransaction.reloadDataGrid();
			})

			/** 重置处理 * */
			$('#clearBtn').click(function() {
				$.searchOfferTransaction.searchForm.form("reset");
				var formData = {};
				formData.bankCode = '0';
				formData.offerDateBegin = $('#sysDate').val();
				formData.offerDateEnd = $('#sysDate').val();
				$.searchOfferTransaction.searchForm.form('load', formData);
			})
			/** 导出Excel * */
			$('#exportBtn').click(function() {

				if (!$.searchOfferTransaction.searchForm.form('validate')) {
					return;
				}
				var queryParam = $.searchOfferTransaction.searchForm.serializeObject();
				/** 未输入查询条件不给予查询 * */
				if ($.isEmpty(queryParam.offerDateBegin) || $.isEmpty(queryParam.offerDateEnd)) {
					$.messager.alert('警告', '必须设置报盘日期范围！', 'warning');
					return;
				}
				$.messager.confirm("提示", "最大可导出50000条记录，请确认要导出Excel文件吗？", function(r) {
					if (r) {
						$.downloadFile({
							url : $.searchOfferTransaction.exportUrl,
							isDownloadBigFile : true,
							params : queryParam,
							successFunc : function(data) {
								if (data == null) {
									$.messager.alert('提示', '下载成功！', 'info');
								} else {
									if (data.resMsg != null) {
										$.messager.alert('警告', data.resMsg, 'warning');
									} else {
										$.messager.alert('异常', '下载失败！', 'error');
									}
								}
							},
							failFunc : function(data) {
								$.messager.alert('异常', '下载失败！', 'error');
							}
						});
					}
				});
			})

			$.searchOfferTransaction.dataGrid.datagrid('resize');

			/** ===================开户银行=========================* */
			/** 从服务端获取银行数据,将数据填充到前端下拉框 * */
			/** 开户银行下拉框参数定义 * */
			$('#bankCode').combobox({
				valueField : 'id',
				textField : 'text',
				// panelHeight : 'auto',
				filter : function(q, row) {
					var opts = $(this).combobox("options");
					return row[opts.textField].indexOf(q) > -1;
				},
				formatter : function(row) {
					var opts = $(this).combobox("options");
					return row[opts.textField];
				}
			})
			$.ajaxPackage({
				type : 'post',
				url : global.contextPath + '/offer/offerBankDic/getBankInfo',
				isShowLoadMask : false,
				dataType : "json",
				success : function(data) {
					/** data 服务端返回数据 * */
					data.unshift({
						"id" : "0",
						"text" : "全部"
					});
					$('#bankCode').combobox('loadDataExt', data);
					$('#bankCode').combobox('defaultOneItem');
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				},
				complete : function() {

				}
			});
			/** ===================开户银行=========================* */
			$('#offerDateBegin').datebox('setValue', $('#sysDate').val());
			$('#offerDateEnd').datebox('setValue', $('#sysDate').val());
		}
	}

	/** 分页参数（page:当前第N页，rows:一页N行） * */
	$.searchOfferTransaction.pg = {
		'page' : 1,
		'rows' : $.searchOfferTransaction.pageSize
	}

	/** dataGrid表格组件初始化 * */
	$.searchOfferTransaction.dataGrid.datagrid({
		pg : $.searchOfferTransaction.pg,
		/** 提交方式 * */
		method : 'get',
		/** 查询超时时间，暂时设定为3分钟 * */
		timeout : 180000,
		/** 是否显示行号 * */
		rownumbers : true,
		/** 是否单选 * */
		singleSelect : true,
		/** 是否可折叠的 * */
		collapsible : false,
		/** 自适应列宽 * */
		fitColumns : true,
		/** 自适应父窗口 * */
		fit : true,
		/** 是否开启分页 * */
		pagination : true,
		/** 加载数据提示信息 * */
		loadMsg : '数据加载中,请稍等...',
		/** 锁定列定义 * */
		frozenColumns : [ [] ],
		columns : [ [
		/** 列定义 * */
		{
			field : 'contractNum',
			title : '合同编号',
			width : 20
		}, {
			field : 'fundsSources',
			title : '合同来源',
			width : 10
		}, {
			field : 'name',
			title : '借款人',
			width : 8
		}, {
			field : 'idNum',
			title : '身份证号',
			width : 20
		}, {
			field : 'bankName',
			title : '银行',
			width : 13
		}, {
			field : 'bankAcct',
			title : '账号',
			width : 19
		}, {
			field : 'offerDate',
			title : '报盘日期',
			width : 10,
			formatter : $.DateUtil.dateFormatToStr
		}, {
			field : 'amount',
			title : '报盘金额',
			width : 10,
			vType : 'rmb'
		}, {
			field : 'factAmount',
			title : '回盘金额',
			width : 10,
			formatter : function(value, row, index) {
				if (row.trxState == '已发送') {
					return '';
				}
				return value;
			},
			vType : 'rmb'
		}, {
			field : 'responseTime',
			title : '回盘日期',
			width : 10,
			formatter : $.DateUtil.dateFormatToStr
		}, {
			field : 'type',
			title : '划扣方式',
			width : 10
		}, {
			field : 'paySysNoReal',
			title : '划扣通道',
			width : 10
		}, {
			field : 'merId',
			title : '划扣商户',
			width : 10
		}, {
			field : 'rtnCode',
			title : '划扣状态',
			width : 8
		}, {
			field : 'orgMemo',
			title : '营业部备注',
			width : 7
		},{
			field : 'failReason',
			title : '失败原因',
			width : 15
		}
		
		] ],
		/** 每页显示的记录条数，默认为10 * */
		pageSize : $.searchOfferTransaction.pageSize,
		/** 可以设置每页记录条数的列表 * */
		pageList : $.searchOfferTransaction.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 * */
		rowStyler : function(index, row) {
			if (index % 2 == 0) {
			}
		},
		onLoadSuccess : function(data) {
			/** 加载数据后自适应表格 * */
			$.searchOfferTransaction.dataGrid.datagrid('resize');
		}
	});

	/** 分页组件初始化 * */
	$.searchOfferTransaction.pager = $.searchOfferTransaction.dataGrid.datagrid('getPager');
	$.searchOfferTransaction.pager.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			$.searchOfferTransaction.pg.page = pageNumber;
			$.searchOfferTransaction.pg.rows = pageSize;
			$.searchOfferTransaction.reloadDataGrid();
		}
	});

	/** 页面初始化 * */
	$.searchOfferTransaction.init();
})