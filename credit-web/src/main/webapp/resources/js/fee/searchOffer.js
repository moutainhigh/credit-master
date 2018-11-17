$(function() {
	$.searchOffer = {
		/** 表格数据源地址 * */
		dataGridUrl : global.contextPath + '/fee/loanFeeOffer/search',
		/** 查询回盘信息地址 * */
		searchTransactionUrl : global.contextPath + '/fee/loanFeeTransaction/searchByOfferId',
		/** 实时报盘地址 * */
		actualTimeOfferUrl : global.contextPath + '/fee/loanFeeOffer/actualTimeOffer',
		/** 实时查询未收服务费 * */
		queryFeeAmountUrl : global.contextPath + '/fee/loanFeeInfo/queryFeeAmount',
		/** 报盘 DataGrid表格 * */
		dataGrid : $('#dataGrid'),
		/** 回盘 DataGrid表格 * */
		offerTransactionDataGrid : $('#offerTransactionDataGrid'),
		/** 分页控件 * */
		pager : undefined,
		/** 查询条件数据项表单实例 * */
		searchForm : $('#searchForm'),
		/** 实时划扣表单 * */
		actualTimeOfferForm : $('#actualTimeOfferForm'),
		/** 回盘数据窗口 * */
		offerTransactionWin : $('#offerTransactionWin'),
		/** 实时划扣窗口 * */
		actualTimeOfferWin : $('#actualTimeOfferWin'),
		/** 每页显示的记录条数，默认为10 * */
		pageSize : 10,
		/** 设置每页记录条数的列表 * */
		pageSizeList : [ 10, 20, 30, 40, 50 ],
		/** 加载表格数据 * */
		reloadDataGrid : function() {
			if (!$.searchOffer.searchForm.form('validate')) {
				return;
			}
			/** 获取查询表单数据转换成JSON对象 * */
			var searchMsg = $.searchOffer.searchForm.serialize();
			/** 对参数进行解码(显示中文) * */
			searchMsg = decodeURIComponent(searchMsg);
			var queryParam = $.serializeToJsonObject(searchMsg);
			/** 未输入查询条件不给予查询 * */
			if ($.isEmpty(queryParam.offerDateBegin) || $.isEmpty(queryParam.offerDateEnd)) {
				$.messager.alert('警告', '必须设置报盘日期范围！', 'warning');
				return;
			}
			queryParam.url = $.searchOffer.dataGridUrl;
			$.searchOffer.dataGrid.datagrid('reloadData', queryParam);
		},
		/** 加载回盘表格数据 * */
		reloadOfferTransactionDataGrid : function(offerId) {
			if (offerId) {
				var queryParam = {};
				queryParam.offerId = offerId;
				queryParam.url = $.searchOffer.searchTransactionUrl;
				$.searchOffer.offerTransactionDataGrid.datagrid('reloadData', queryParam);
			}
		},

		init : function() {
			/** 查询处理 * */
			$('#searchBtn').click(function() {
				$.searchOffer.pg.page = 1;
				$.searchOffer.reloadDataGrid();
			})

			/** 重置处理 * */
			$('#clearBtn').click(function() {
				$.searchOffer.searchForm.form("reset");
				var formData = {};
				formData.bankCode = '0';
				formData.offerDateBegin = $('#sysDate').val();
				formData.offerDateEnd = $('#sysDate').val();
				$.searchOffer.searchForm.form('load',formData);
			})

			/** 点击还款确认处理 * */
			$('#submitBtn').click(function() {
				if (!$.searchOffer.actualTimeOfferForm.form('validate')) {
					return;
				}
				/** 还款录入金额 * */
				var amount = $("#amount").val();
				if (parseFloat(amount) < 0.01) {
					$.messager.alert('警告', '划扣金额不能小于0.01元,请修改！', 'warning');
					return;
				}
				$.messager.confirm('提示', '确定操作吗？', function(r) {
					if (r) {
						/** 获取查询表单数据转换成JSON对象 * */
						var searchMsg = $.searchOffer.actualTimeOfferForm.serialize();
						/** 对参数进行解码(显示中文) * */
						searchMsg = decodeURIComponent(searchMsg);
						var requestData = $.serializeToJsonObject(searchMsg);
						$.ajaxPackage({
							type : 'post',
							url : $.searchOffer.actualTimeOfferUrl,
							dataType : 'json',
							data : requestData,
							success : function(data, textStatus, jqXHR) {
								var resCode = data.resCode;
								var resMsg = data.resMsg;
								// 从服务器上获取到记录信息
								var attachment = data.attachment;
								if (resCode != '000000') {
									$.messager.alert('警告', resMsg, 'warning');
									return;
								}
								$.messager.alert('提示', '操作成功！', 'info');
							},
							error : function(XMLHttpRequest, textStatus, errorThrown, d) {
								$.messager.alert('异常', textStatus + '  :  ' + errorThrown + '!', 'error');
							},
							complete : function() {
								$.searchOffer.reloadDataGrid();
								$.searchOffer.actualTimeOfferWin.window('close');
							}
						});
					}
				});
			})

			/** 关闭收费录入窗口 * */
			$('#closeBtn').click(function() {
				$.searchOffer.actualTimeOfferWin.window('close');
			})
			$.searchOffer.dataGrid.datagrid('resize');
			
			/**===================开户银行=========================**/
		    /** 从服务端获取银行数据,将数据填充到前端下拉框 **/
		    /** 开户银行下拉框参数定义 **/
			$('#bankCode').combobox({
				valueField : 'id',
				textField : 'text',
				//panelHeight : 'auto',
				filter : function(q,row) {
					var opts=$(this).combobox("options");
					return row[opts.textField].indexOf(q)>-1;
				},
				formatter : function(row) {
					var opts=$(this).combobox("options");
					return row[opts.textField];
				}
			})
			$.ajaxPackage({
				type : 'post', 
				url : global.contextPath + '/offer/offerBankDic/getBankInfo',
				isShowLoadMask : false,
				dataType : "json",
				success : function (data) { 
					/** data 服务端返回数据 **/
					data.unshift({"id":"0","text":"全部"});
					$('#bankCode').combobox('loadDataExt',data);
					$('#bankCode').combobox('defaultOneItem');
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		    /**===================开户银行=========================**/
			$('#offerDateBegin').datebox('setValue', $('#sysDate').val());
			$('#offerDateEnd').datebox('setValue', $('#sysDate').val());
		}
	}

	/** 分页参数（page:当前第N页，rows:一页N行） * */
	$.searchOffer.pg = {
		'page' : 1,
		'rows' : $.searchOffer.pageSize
	}

	/** dataGrid表格组件初始化 * */
	$.searchOffer.dataGrid.datagrid({
		pg : $.searchOffer.pg,
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
					field : 'name',
					title : '借款人',
					width : 8
				},
				{
					field : 'idNum',
					title : '身份证号',
					width : 20
				},
				{
					field : 'offerDate',
					title : '报盘日期',
					width : 13,
					formatter : $.DateUtil.dateFormatToStr
				},
				{
					field : 'amount',
					title : '报盘金额',
					width : 10,
					vType : 'rmb'
				},
				{
					field : 'receiveAmount',
					title : '回盘金额',
					width : 10,
					vType : 'rmb'
				},
				{
					field : 'contractNum',
					title : '合同编号',
					width : 18
				},
				{
					field : 'bankName',
					title : '银行',
					width : 20
				},
				{
					field : 'bankAcct',
					title : '账号',
					width : 20
				},
				{
					field : 'type',
					title : '划扣类型',
					width : 10
				},
				{
					field : 'paySysNo',
					title : '划扣通道',
					width : 12
				},
				{
					field : 'state',
					title : '状态',
					width : 10
				},
				{
					field : 'loanFeeState',
					title : '收取状态',
					width : 10
				},
				{
					field : 'memo',
					title : '备注',
					width : 20
				},
				{
                    field : 'creator',
                    title : '操作人',
                    width : 8
                },
				{
					field : 'id',
					title : '操作',
					width : 30,
					formatter : function(value, row, index) {
						if (value) {
							var elements = "";
							elements += "<a href='javascript:void(0)' class='createOffer' offerId='" + row.id
									+ "' loanId='" + row.loanId + "' feeId='" + row.feeId + "' ></a>";
							elements += "<a href='javascript:void(0)' class='viewTransaction' offerId='" + row.id
									+ "' loanId='" + row.loanId + "' ></a>";
							return elements;
						}
					}
				} ] ],
		/** 每页显示的记录条数，默认为10 * */
		pageSize : $.searchOffer.pageSize,
		/** 可以设置每页记录条数的列表 * */
		pageList : $.searchOffer.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 * */
		rowStyler : function(index, row) {
			if (index % 2 == 0) {
			}
		},
		onLoadSuccess : function(data) {
			$(".createOffer").linkbutton({
				text : '实时划扣',
				plain : true,
				iconCls : 'pic_36',
				onClick : function() {
					var feeId = $(this).attr("feeId");
					var requestData = {};
					requestData.feeId = feeId;
					/** 实时查询未收服务费 * */
					$.ajaxPackage({
						type : 'post',
						url : $.searchOffer.queryFeeAmountUrl,
						dataType : 'json',
						data : requestData,
						success : function(data, textStatus, jqXHR) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							// attachment 未收服务费
							var attachment = data.attachment;
							if (resCode != '000000') {
								$.messager.alert('警告', resMsg, 'warning');
								return;
							}
							var formData = {};
							formData.feeId = feeId;
							formData.amount = attachment;
							$.searchOffer.actualTimeOfferForm.form('load', formData);
							$.searchOffer.actualTimeOfferWin.window('open');
						},
						error : function(XMLHttpRequest, textStatus, errorThrown, d) {
							$.messager.alert('异常', textStatus + '  :  ' + errorThrown + '!', 'error');
						},
						complete : function() {

						}
					});
				}
			})
			$(".viewTransaction").linkbutton({
				text : '查看详情',
				plain : true,
				iconCls : 'pic_36',
				onClick : function() {
					var offerId = $(this).attr("offerId");
					$.searchOffer.offerTransactionDataGrid.datagrid('resize');
					$.searchOffer.offerTransactionWin.window('open');
					/** 加载回盘数据 * */
					$.searchOffer.reloadOfferTransactionDataGrid(offerId);
				}
			})
			/** 加载数据后自适应表格 * */
			$.searchOffer.dataGrid.datagrid('resize');
		}
	});

	/** dataGrid表格组件初始化 * */
	$.searchOffer.offerTransactionDataGrid.datagrid({
		pg : $.searchOffer.pg,
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
		pagination : false,
		/** 加载数据提示信息 * */
		loadMsg : '数据加载中,请稍等...',
		/** 锁定列定义 * */
		frozenColumns : [ [] ],
		columns : [ [
		/** 列定义 * */
		{
			field : 'serialNo',
			title : '交易流水号',
			width : 12
		}, {
			field : 'trxState',
			title : '交易状态',
			width : 6
		}, {
			field : 'amount',
			title : '报盘金额',
			width : 6,
			vType : 'rmb'
		}, {
			field : 'factAmount',
			title : '回盘金额',
			width : 6,
			vType : 'rmb'
		}, {
			field : 'rtnCode',
			title : '返回码(TPP)',
			width : 8
		}, {
			field : 'rtnInfo',
			title : '返回信息(TPP)',
			width : 8
		}, {
			field : 'memo',
			title : '失败原因(TPP)',
			width : 8
		} ] ],
		/** 每页显示的记录条数，默认为10 * */
		// pageSize : $.searchOffer.pageSize,
		/** 可以设置每页记录条数的列表 * */
		// pageList : $.searchOffer.pageSizeList,
		// toolbar : '#tb',
		/** 自定义行样式 * */
		onLoadSuccess : function(data) {
			/** 加载数据后自适应表格 * */
			// $.searchOffer.offerTransactionDataGrid.datagrid('resize');
		}
	});

	/** 分页组件初始化 * */
	$.searchOffer.pager = $.searchOffer.dataGrid.datagrid('getPager');
	$.searchOffer.pager.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			$.searchOffer.pg.page = pageNumber;
			$.searchOffer.pg.rows = pageSize;
			$.searchOffer.reloadDataGrid();
		}
	});

	$.searchOffer.offerTransactionWin.window({
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
	});

	$.searchOffer.actualTimeOfferWin.window({
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
	});

	/** 页面初始化 * */
	$.searchOffer.init();
})