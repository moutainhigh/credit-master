$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/offerChannel/offerChannelList';
	
	var insertOfferChannel = global.contextPath + '/offerChannel/saveOrUpdateOfferChannel';
	
	var findOfferChannelById = global.contextPath + '/offerChannel/offerChannelById';
	// 表格实例
	var testDataGrid = $('#testDataGrid');
	var addOfferChannelPanel = $('#addOfferChannelPanel');
	var updateOfferChannelPanel = $('#updateOfferChannelPanel');
	var createMunePanel = $('#createMunePanel');
	var createRolePanel = $('#createRolePanel');
	var orgPanel = $('#orgPanel');
	// 新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	
	var updateForm = $("#updateForm");
	// 查询数据项表单实例
	var searchForm = $('#searchForm');
	// 新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	
	/** 操作提示 * */
	var tips = $('#tips');
	var orgtips = $('#orgtips');
	/** 分页参数（page:当前第N页，rows:一页N行） * */
	var pg = {
		'page' : 1,
		'rows' : pageSize
	};
	var paySysNoData = {
		'0' : '通联支付',
		'4' : '上海银联支付',
		'6' : '用友支付',
		'10': '爱特代扣',
		'18' : '快捷通',
		'20' : '宝付',
		'22' : '银生宝',
		'00019' : '外贸3',
		'00021' : '陆金所'
	};
	var stateData = {
			'0' : '无效',
			'1' : '有效'
	};
	/** 树 * */
	var comOrganizationTree = $('#comOrganizationTree');

	testDataGrid.datagrid({

		pg : pg,
		// 提交方式
		method : 'get',
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
		// 锁定列定义
		hideColumn : [ [ {
			field : 'id',
			width : 50
		}] ],
		
		columns : [ [
		// 列定义
		{
			field : 'loanBelong',
			title : '债权去向',
			width : 100
		}, {
			field : 'paySysNo',
			title : '划扣通道',
			width : 100,
			formatter : function(value, row, index) {
				return paySysNoData[value + ''] || '未知';
			},
		}
		, {
			field : 'state',
			title : '是否有效',
			width : 100,
			formatter : function(value, row, index) {
				return stateData[value + ''] || '未知';
			},
			
		}, {
			field : 'createTime',
			title : '创建时间',
			width : 200,
			formatter : $.DateUtil.dateFormatToStr
		}, {
			field : 'creator',
			title : '创建人',
			width : 100,
			
		}, {
			field : 'updator',
			title : '修改人',
			width : 100
		}, {
			field : 'updateTime',
			title : '修改时间',
			width : 100,
			formatter : $.DateUtil.dateFormatToStr
		}, {
			field : 'id',
			title : 'id',
			width : 100,
			hidden:  true 
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
	// $('#productTree').tree('reload');
	// 表格分页实例

	var pager = testDataGrid.datagrid('getPager');
	pager.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			pg.page = pageNumber;
			pg.rows = pageSize;
			reloadDataGrid();
		}
	});

	// 新增面板参数定义
	addOfferChannelPanel.window({
		// width : 800,
		// height : 300,
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
		iconCls : 'icon-save',
	});
	
	// 新增面板参数定义
	updateOfferChannelPanel.window({
		// width : 800,
		// height : 300,
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
		iconCls : 'icon-save',
	});


	 $("#clearCondition").bind("click", function(envent) {
		$("#searchForm").form("reset");
	});
	// 新增按钮添加事件
	$("#addBut").click(function() {
		// 显示窗口
		addOfferChannelPanel.window('open');
		addOfferChannelPanel.panel({title: "新增"});
		$('#userCodes').textbox({'readonly':false});
		// 重置表单数据
		dataForm.form('clear');
		$('#loanBelongAdd').combobox('defaultOneItem');
		$('#paySysNoAdd').combobox('defaultOneItem');
		$('#stateAdd').combobox('defaultOneItem');
		$('#addtype').val("1");
	});

	// 修改按钮添加事件
	$("#updateBut").click(
		function() {
			updateOfferChannelPanel.panel({title: "修改"});
			var selectedRow = getSelectedRow(testDataGrid);
			if (selectedRow) {
				var id = selectedRow.id;
				$("#offerChannelId").val(id);
				// 根据id查询，在赋值
				$.ajax({
					type : 'get',
					url : findOfferChannelById,
					data : "id=" + id,
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						var resCode = data.resCode;
						if (resCode == '000000') {
							//清理form
							updateForm.form('clear');
							var a = data.attachment;
							// 填充到表单项里
							updateForm.form('load', a);
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown, d) {
						$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					},
					complete : function() {
						// 显示窗口
						updateOfferChannelPanel.window('open');
					}
				});
			} else {
				$.messager.alert('警告', '请选中需要修改的记录!', 'warning');
			}
	});

	// 查询按钮添加事件
	$("#searchBut").click(function() {
		pg.page = 1;
		reloadDataGrid();
	});

	// 修改按钮添加事件
	function updateButEvent() {
		var selectedRow = getSelectedRow(testDataGrid);
		if (selectedRow) {
			// 请求服务端获取某条记录信息
			var reqId = selectedRow.id;
			$.ajaxPackage({
				type : 'get',
				url : loadDataUrl + '/' + reqId,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					// 从服务器上获取到记录信息
					var attachment = data.attachment;
					if (resCode == '000000') {
						// 显示窗口
						addOfferChannelPanel.window('open');
						// 填充到表单项里
						dataForm.form('load', attachment);
					} else {
						// 操作失败

					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown
							+ '!', 'error');
				},
				complete : function() {

				}
			});
		} else {
			$.messager.alert('警告', '请选中需要修改的记录!', 'warning');
		}
	}

	// 关闭按钮事件
	$("#productTreeColse").click(function() {
		// 找到按钮属于的Window对象
//		loadMuneTreeContent();
		$('#createMunePanel').window('close');

	})
	// 关闭按钮事件
	$("#updateofferChannelClose").click(function() {
		// 找到按钮属于的Window对象
		$('#updateOfferChannelPanel').window('close');

	})
	
	// 关闭按钮事件
	$("#employeeColse").click(function() {
		// 找到按钮属于的Window对象
		$('#addOfferChannelPanel').window('close');
		
	})

	// 关闭按钮事件
	$("#roleTreeColse").click(function() {
//		loadRoleTreeContent();
		// 找到按钮属于的Window对象
		$('#createRolePanel').window('close');

	})
	// 全局关闭按钮事件
	$('a[name=closeBut]').click(function() {
		// 找到按钮属于的Window对象
		var ownWin = $(this).parents("div.easyui-window:eq(0)");
		if (ownWin.length) {
			ownWin.window('close');
		}
	})

	// 指定某Window下提交按钮添加事件
	$(addOfferChannelPanel).find('a[name=submitBut]:eq(0)').click(
			function() {
				// 检查表单项是否通过验证
				if (dataForm.form('validate')) {
					// 提交到服务端，进行处理
					$.ajaxPackage({
						type : 'post',
						url : saveOrUpdateUrl,
						data : dataForm.serialize(),
						dataType : "json",
						success : function(data) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							if (resCode == '000000') {
								// 操作成功 重新加载列表数据
								reloadDataGrid();
								resMsg = '操作成功';
							} else {
								// 操作失败

							}
							$.messager.alert('结果', resMsg);
						},
						error : function(XMLHttpRequest, textStatus, errorThrown, d) {
							$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
						},
						complete : function() {
							// 关闭窗口
							addOfferChannelPanel.window('close');
						}
					});
				} else {
					// 存在必须项，未填充值
				}
			})


	// 获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = dataGrid.datagrid('getSelected');
		return selectedRow;
	}

	// 加载表格数据
	function reloadDataGrid() {
		// 获取查询表单数据转换成JSON对象
		var searchMsg = searchForm.serialize();
		// 对参数进行解码(显示中文)
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url = dataGridUrl;
		$(testDataGrid).datagrid('reloadData', queryParam);
	}

	// 默认查询表格数据
	reloadDataGrid();
	
	// 新增员工提交
	$("#offerChannelSubmit").click(
			function() {
				if (dataForm.form('validate')) {
					var a = $('#addOfferChannelPanel').find('input[name="id"]')
							.val();
					$.ajaxPackage({
						type : 'get',
						url : insertOfferChannel,
						data : dataForm.serialize(),
						dataType : "json",
						success : function(data, textStatus, jqXHR) {
							// 从服务器上获取到记录信息
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							if (resCode == '000000') {
								$.messager.alert("操作提示", resMsg);
								addOfferChannelPanel.window('close');
								reloadDataGrid();
							} else {
								$.messager.alert('异常信息',resMsg);
							}
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown, d) {
							$.messager.alert('异常信息', textStatus + '  :  '
									+ errorThrown + '!', 'error');
						},
						complete : function() {
							// 关闭窗口
							$('#userCodes').textbox({'readonly':false});
							
							
						}
					});
				}
			})
    // 修改提交
	$("#updateofferChannelSubmit").click(
			function() {
				if (updateForm.form('validate')) {
					var a = $('#updateofferChannelSubmit').find('input[name="id"]')
							.val();
					$.ajaxPackage({
						type : 'get',
						url : insertOfferChannel,
						data : updateForm.serialize(),
						dataType : "json",
						success : function(data, textStatus, jqXHR) {
							// 从服务器上获取到记录信息
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							if (resCode == '000000') {
								$.messager.alert("操作提示", resMsg);
								updateOfferChannelPanel.window('close');
								reloadDataGrid();
							} else {
								$.messager.alert('异常信息',resMsg);
							}
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown, d) {
							$.messager.alert('异常信息', textStatus + '  :  '
									+ errorThrown + '!', 'error');
						},
						complete : function() {
							// 关闭窗口
							$('#userCodes').textbox({'readonly':false});
							
							
						}
					});
				}
			})

			

	  /** 清空提示信息 **/
    function clearMessage(){
        $("#message").text("");
    }

})
