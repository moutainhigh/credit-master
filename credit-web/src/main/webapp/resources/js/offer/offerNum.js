$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/offerNum/offerNumList';
	
	//var findOfferNumByCode = global.contextPath + '/offerNum/findOfferNumByCode';
	// 新增划扣次数
	var insertOfferNum = global.contextPath + '/offerNum/insert';
	// 修改划扣次数
	var updateOfferNum = global.contextPath + '/offerNum/update';
	// 删除划扣次数
	var deleteOfferNum = global.contextPath + '/offerNum/delete';



	// 表格实例
	var testDataGrid = $('#testDataGrid');
	var insertOfferNumPanel = $('#insertOfferNumPanel');
	// 新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	// 查询数据项表单实例
	var searchForm = $('#searchForm');
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	/** 操作提示 * */
	var tips = $('#tips');
	/** 分页参数（page:当前第N页，rows:一页N行） * */
	var pg = {
		'page' : 1,
		'rows' : pageSize
	};
	
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
			field : 'name',
			title : '姓名',
			width : 100
		},{
			field : 'userCode',
			title : '工号',
			width : 100
		},{
			field : 'offerCount',
			title : '划扣次数',
			width : 100
		},{
			field : 'createTime',
			title : '创建时间',
			width : 100,
			formatter : $.DateUtil.dateFormatToStr
		},{
			field : 'updateTime',
			title : '更新时间',
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
	insertOfferNumPanel.window({
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
		iconCls : 'icon-save'
	});

	 $("#clearCondition").bind("click", function(envent) {
		$("#searchForm").form("reset");
	});

	// 新增按钮添加事件
	$("#addBut").click(function() {
		// 显示窗口
		insertOfferNumPanel.window('open');
		insertOfferNumPanel.panel({title: "新增"});
		$('#userCode').textbox({'readonly':false});
		// 重置表单数据
		dataForm.form('clear');
	});

	// 修改按钮添加事件
	$("#updateBut").click(function() {
			insertOfferNumPanel.panel({title: "修改"});
			var selectedRow = getSelectedRow(testDataGrid);
			if (!selectedRow) {
				$.messager.alert('警告', '请选中需要修改的记录！', 'warning');
				return;
			}
			//清理form
			dataForm.form('clear');
			$('#userCode').textbox({'readonly':true});
			// 填充到表单项里
			dataForm.form('load', selectedRow);
			// 显示窗口
			insertOfferNumPanel.window('open');
			
			/*var userCode = selectedRow.userCode;
			// 根据id查询，在赋值
			$.ajax({
				type : 'get',
				url : dataGridUrl,
				data : "userCode=" + userCode,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					var resCode = data.resCode;
					if (resCode == '000000') {
						//清理form
						dataForm.form('clear');
						var a = data.attachment;
						$('#userCode').textbox({'readonly':true});
						// 填充到表单项里
						dataForm.form('load', a);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				},
				complete : function() {
					// 显示窗口
					insertOfferNumPanel.window('open');
				}
			});*/
	});

	// 查询按钮添加事件
	$("#searchBut").click(function() {
		pg.page = 1;
		reloadDataGrid();
	});

	// 删除按钮添加事件
	$("#deleteBut").click(	
	function() {
		var selectedRow = getSelectedRow(testDataGrid);
		if (selectedRow) {
			$.messager.confirm('请确认', '确认删除?', function(r) {
				if (r) {
					var userCode = selectedRow.userCode;
					// 通过主键编号删除记录
					$.ajaxPackage({
						type : 'get',
						url : deleteOfferNum,
						data : "userCode=" + userCode,
						dataType : "json",
						success : function(data) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							if (resCode == '000000') {
								$.messager.alert('结果', resMsg);
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
			});
		} else {
			$.messager.alert('警告', '请选中需要删除的记录!', 'warning');
		}
	});

	// 关闭按钮事件
	$("#offerNumColse").click(function() {
		// 找到按钮属于的Window对象
		$('#insertOfferNumPanel').window('close');
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
	
	// 新增划扣次数提交
	$("#offerNumSubmit").click(
			function() {
				// 检查表单项是否通过验证
				if (dataForm.form('validate')) {
					var a = $('#insertOfferNumPanel').panel('options').title;
					var url = '';
					if(a =='新增'){
						 url = insertOfferNum;
					}else{
						 url = updateOfferNum;
					}
					$.ajaxPackage({
						type : 'get',
						url : url,
						data : dataForm.serialize(),
						dataType : "json",
						success : function(data, textStatus, jqXHR) {
							// 从服务器上获取到记录信息
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							if (resCode == '000000') {
								$.messager.alert("操作提示", resMsg);
								insertOfferNumPanel.window('close');
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
			} else {
				// 存在必须项，未填充值
			}
})
	  /** 清空提示信息 **/
    function clearMessage(){
        $("#message").text("");
    }
})
