$(function() {
	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/payChannel/payChannelPageList';
	//保存或更新代付通道
	var saveOrUpdateChannelUrl = global.contextPath + '/payChannel/saveOrUpdatePayChannel';
	//表格显示
	var payChannelDataGrid = $('#payChannelDataGrid');
	//查询数据项表单实例
	var searchForm = $('#searchForm');
	//新增窗口
	var addPayChannelPanel = $('#addPayChannelPanel');
	//新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	/** 分页参数（page:当前第N页，rows:一页N行） * */
	var pg = {
		'page' : 1,
		'rows' : pageSize
	};
	var paySysNoData = {
		'0' : '通联支付',
		'18' : '快捷通'
	};
	var stateData = {
		'0' : '无效',
		'1' : '有效'
	};
	payChannelDataGrid.datagrid({
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
		//fitColumns : true,
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
			field : 'fundsSources',
			title : '合同来源',
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
			width : 100,
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
			width : 30,
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
			}
		}
	});

	var pager = payChannelDataGrid.datagrid('getPager');
	pager.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			pg.page = pageNumber;
			pg.rows = pageSize;
			reloadDataGrid();
		}
	});
	
	// 加载表格数据
	function reloadDataGrid() {
		var searchMsg = searchForm.serialize();
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url = dataGridUrl;
		$(payChannelDataGrid).datagrid('reloadData', queryParam);
	}
	
	// 查询按钮添加事件
	$("#searchBut").click(function() {
		pg.page = 1;
		reloadDataGrid();
	});
	
	//重置按钮
	$("#clearCondition").bind("click", function(envent) {
		 searchForm.form("reset");
	});
	 
	// 新增面板参数定义
	addPayChannelPanel.window({
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
	
	// 新增按钮添加事件
	$("#addBut").click(function() {
		addPayChannelPanel.window('open');
		addPayChannelPanel.panel({title: "新增"});
		dataForm.form('clear');
		$('#fundsSourcesEdit').combobox('enable');
		$('#fundsSourcesEdit').combobox('defaultOneItem');
		$('#paySysNoEdit').combobox('defaultOneItem');
	});
	
	//新增代付通道提交
	$("#payChannelSubmitAdd").click(
		function() {
			if (dataForm.form('validate')) {
				$.ajaxPackage({
					type : 'get',
					url : saveOrUpdateChannelUrl,
					data : dataForm.serialize(),
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						var resCode = data.resCode;
						var resMsg = data.resMsg;
						if (resCode == '000000') {
							$.messager.alert("操作提示", resMsg);
							addPayChannelPanel.window('close');
							reloadDataGrid();
						} else {
							$.messager.alert('异常信息',resMsg);
						}
					},
					error : function(XMLHttpRequest, textStatus,
							errorThrown, d) {
						$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					}
				});
			}
	})
	
	// 新增按钮添加事件
	$("#updateBut").click(function() {
		var selectedRow = payChannelDataGrid.datagrid('getSelected');
		if(selectedRow){
			dataForm.form('clear');
			addPayChannelPanel.window('open');
			addPayChannelPanel.panel({title: "修改"});
			var payId = selectedRow.id;
			var funds = selectedRow.fundsSources;
			var payNo = selectedRow.paySysNo;
			$('#fundsSourcesEdit').combobox('disable');
			$('#fundsSourcesEdit').combobox('setValue',funds);
			$('#paySysNoEdit').combobox('setValue',paySysNoData[payNo]);
			$('#payChannelId').val(payId);
		}else{
			$.messager.alert('警告', '请选中需要修改的记录!', 'warning');
		}
	});
	
	// 关闭按钮事件
	$("#payChannelColseAdd").click(function() {
		$('#addPayChannelPanel').window('close');
	})
})
