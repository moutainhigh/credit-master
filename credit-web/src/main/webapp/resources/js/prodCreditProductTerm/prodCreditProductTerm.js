$(function() {
	// 表格数据源地址
	var dataGridUrl = global.contextPath
			+ '/system/ProdCreditProductTerm/search';
	var findProductId = global.contextPath
			+ '/system/ProdCreditProductInfo/findProdCreditProductInfoList';
	var insertProdCreditProductInfo = global.contextPath
			+ '/system/ProdCreditProductTerm/saveOrUpdateProdCreditProductTerm';
	var loadDataProdCreditProductTerm = global.contextPath
			+ '/system/ProdCreditProductTerm/loadDataProdCreditProductTerm';

	var deleteProdCreditProductTerm = global.contextPath
			+ '/system/ProdCreditProductTerm/deleteProdCreditProductTermById';
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	var sysDataGrid = $('#sysDataGrid');
	// 查询条件数据项表单实例
	var dataForm = $('#dataForm');
	var searchForm = $('#searchForm');
	var addDataPanel = $('#addDataPanel');
	/** 产品类型下拉框空数据项 * */
	var emptyItem = {
		'id' : '',
		'loanType' : '全部'
	};
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	var pg = {
		'page' : 1,
		'rows' : pageSize
	}

	// 关闭按钮事件
	$("#closeBut").click(function() {
		// 找到按钮属于的Window对象
		addDataPanel.window('close');
	})

	sysDataGrid.datagrid({
		//url : dataGridUrl,
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

		frozenColumns : [ [ {
			field : 'productName',
			title : '产品类型',
			width : 100,
		} ] ],
		columns : [ [
		// 列定义
		{
			field : 'term',
			title : '可选期限',
			width : 100
		}, {
			field : 'lowerLimit',
			title : '审批金额起(单位:元)',
			width : 100
		}, {
			field : 'upperLimit',
			title : '审批金额止(单位:元)',
			width : 100
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
	
	var pager = sysDataGrid.datagrid('getPager');
	pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			pg.page = pageNumber;
			pg.rows = pageSize;
			reloadDataGrid();
		}
	});
	

	// 新增面板参数定义
	addDataPanel.window({
		width : 600,
		height : 500,
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
		pg.page = 1;
		reloadDataGrid();
	});

	// 表格分页实例
	var roleDataGridPG = sysDataGrid.datagrid('getPager');
	roleDataGridPG.pagination({
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
		onBeforeRefresh : function() {
		}
	});

	// 加载表格数据
	function reloadDataGrid() {
		// 获取查询表单数据转换成JSON对象
		var searchMsg = searchForm.serialize();
		// 对参数进行解码(显示中文)
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url=dataGridUrl;
		$(sysDataGrid).datagrid('reloadData', queryParam);

	}

	// 默认查询表格数据
	reloadDataGrid();
	$('#addBut').click(function() {
		addButEvent();
	})

	$('#updateBut').click(function() {
		updateButEvent();
	})
	// 新增按钮添加事件
	function setProductId() {
		$.ajaxPackage({
			type : 'get',
			url : findProductId,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				var resCode = data.resCode;
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					attachment.unshift(emptyItem);
					$('#productId').combobox('loadData', attachment);
					$('#p').combobox('loadData', attachment);
				} else {
					/** 操作失败* */
					$.messager.alert('提示信息', resMsg, 'error');
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('提示信息', textStatus + '  :  ' + errorThrown
						+ '!', 'error');
			},
			complete : function() {

			}
		});

	}
	setProductId();
	function addButEvent() {
		// 显示窗口
		addDataPanel.window('open');

		addDataPanel.panel({title: "新增"});
		// 重置表单数据
		dataForm.form('clear');
		$('#p').combobox('defaultOneItem');
		$('#t').combobox('defaultOneItem');
	}
	// 修改按钮添加事件
	function updateButEvent() {
		addDataPanel.panel({title: "修改"});
		var selectedRow = getSelectedRow(sysDataGrid);
		if (selectedRow) {
			var id = selectedRow.id
			$.ajaxPackage({
				type : 'get',
				url : loadDataProdCreditProductTerm + '/' + id,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					var attachment = data.attachment;
					if (resCode == '000000') {
						dataForm.form('load', attachment);
					} else {
						/** 操作失败* */
						$.messager.alert('提示信息', resMsg, 'error');
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('提示信息', textStatus + '  :  ' + errorThrown
							+ '!', 'error');
				},
				complete : function() {
					// 显示窗口
					addDataPanel.window('open');
				}
			});
		} else {
			$.messager.alert('警告', '请选中需要修改的记录!', 'warning');
		}

	}

	// 指定某Window下提交按钮添加事件
	$(addDataPanel).find('a[name=submitBut]:eq(0)').click(
			function() {
				// 检查表单项是否通过验证
				if (dataForm.form('validate')) {
					//判断金额问题
					var lowerLimit=$('#lowerLimit').val();
					var upperLimit=$('#upperLimit').val();
					if(eval(lowerLimit)==0)
						{
						$.messager.alert('注意', '审批起始金额不能为0。');
						return;
						}
					
					if(eval(upperLimit)==0)
					{
					$.messager.alert('注意', '审批终止金额不能为0。');
					return;
					}
					if(eval(upperLimit)<=eval(lowerLimit))
						{
						$.messager.alert('注意', '审批起始金额比终止金额大。');
						return;
						}
					// 提交到服务端，进行处理
					$.ajaxPackage({
						type : 'post',
						url : insertProdCreditProductInfo,
						data : dataForm.serialize(),
						dataType : "json",
						success : function(data) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							if (resCode == '000000') {
								// 操作成功 重新加载列表数据
								$.messager.alert('结果', resMsg);
							} else {
								$.messager.alert('结果', resMsg);
							}
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown, d) {
							$.messager.alert('异常信息', textStatus + '  :  '
									+ errorThrown + '!', 'error');
						},
						complete : function() {
							// 关闭窗口
							addDataPanel.window('close');
							reloadDataGrid();
						}
					});
				}
			})
	// 获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = sysDataGrid.datagrid('getSelected');
		return selectedRow;
	}

	// 删除提交
	$("#delBut").click(function() {
		var selectedRow = getSelectedRow(sysDataGrid);
		if (selectedRow) {
			$.messager.confirm('请确认', '确认删除?', function(r) {
				if (r) {
					var reqId = selectedRow.id;
					deleteEmp(reqId);
				}
			});
		} else {
			$.messager.alert('警告', '请选中需要删除的记录!', 'warning');
		}

	})
	function deleteEmp(reqId) {
		$.ajaxPackage({
			type : 'get',
			url : deleteProdCreditProductTerm + '/' + reqId,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				// 从服务器上获取到记录信息
				var resCode = data.resCode;
				if (resCode == '000000') {
					// alert(data.attachment[0].children);
					$.messager.alert("操作提示", "操作成功！");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown
						+ '!', 'error');
			},
			complete : function() {
				reloadDataGrid();
			}
		});
	}

})
