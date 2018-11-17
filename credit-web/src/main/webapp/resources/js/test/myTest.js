$(function() {
	
	//表格数据源地址
	var dataGridUrl = global.contextPath + '/master/myTest/search';
	//新增 修改地址
	var saveOrUpdateUrl = global.contextPath + '/master/myTest/saveOrUpdateData';
	//查询某条记录详细信息地址
	var loadDataUrl = global.contextPath + '/master/myTest/loadData';
	//查询某条记录详细信息地址
	var deleteDataUrl = global.contextPath + '/master/myTest/deleteData';
	
	//表格实例
	var testDataGrid = $('#testDataGrid');
	var addDataPanel = $('#addDataPanel');
	//新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	//查询条件数据项表单实例
	var searchForm = $('#searchForm');
	//每页显示的记录条数，默认为10
	var pageSize = 10;
	//设置每页记录条数的列表
	var pageSizeList = [10,20,30,40,50];
	
	/** 给工具栏按钮添加事件 **/
	$('#addBut').click(function(){
		addButEvent();
	})
	
	$('#updateBut').click(function(){
		updateButEvent();
	})
	
	$('#delBut').click(function(){
		delButEvent();
	})
	
	$('#searchBut').click(function(){
		searchButEvent();
	})
	
	$.pg = {
			'page' : 1,
			'rows' : 10
		}
	
	testDataGrid.datagrid({
		pg : $.pg, 
		//url : dataGridUrl,
		//提交方式
		method : 'get',
		//是否显示行号
		rownumbers : true,
		//是否单选
		singleSelect : true,
		////是否可折叠的
		collapsible : false,
		//自适应列宽
		fitColumns : true,
		fit : true,
		//height : '100%',
		//是否开启分页
		pagination : true,
		//锁定列定义
		frozenColumns : [[
		      {field : 'id',title : '编号',width : 50}
		]],
		columns : [[
		      //列定义
		      {field : 'userName',title : '用户名',width : 100},
		      {field : 'realName',title : '姓名',width : 100},
		      {field : 'mobile',title : '手机号',width : 100},
		      {field : 'address',title : '住址',width : 100},
		      {field : 'postCode',title : '邮编',width : 100},
		      {field : 'email',title : '邮箱地址',width : 100},
		      {field : 'custom',title : '自定义',width : 100}
		]],
		//每页显示的记录条数，默认为10
		pageSize : pageSize,
		//可以设置每页记录条数的列表
		pageList : pageSizeList,
		toolbar : '#tb',
		//自定义行样式
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	
	var p = testDataGrid.datagrid('getPager');
	$(p).pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.pg.page = pageNumber;
			$.pg.rows = pageSize;
			reloadDataGrid();
		}
	});
	
	//表格分页实例
	var testDataGridPG = testDataGrid.datagrid('getPager');
	testDataGridPG.pagination({
		beforePageText : '第',
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
		onBeforeRefresh : function() {
			//$(this).pagination('loading');
			//alert('before refresh');
			//$(this).pagination('loaded');
		}
	});
	
	
	
	//新增面板参数定义
	addDataPanel.window({
		width : 300,
		height : 200,
		//定义窗口是不是模态窗口
		modal : true,
		//定义是否显示折叠按钮
		collapsible : false,
		//定义是否显示最小化按钮
		minimizable : false,
		//定义是否显示最大化按钮
		maximizable : false,
		//定义是否显示关闭按钮
		closable : true,
		//定义是否关闭了窗口
		closed : true,
		//定义是否窗口能被拖拽
		draggable : true,
		//定义是否窗口可以调整尺寸
		resizable : false,
		//如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
		shadow : true,
		//定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部
		inline : true,
		//样式定义
		iconCls : 'icon-save'
	})
	
	//查询按钮添加事件
	function searchButEvent() {
		$.pg.page=1;
		reloadDataGrid();
	}
	
	//新增按钮添加事件
	function addButEvent() {
		//显示窗口
		addDataPanel.window('open');
		//重置表单数据
		dataForm.form('clear');
	}
	
	//修改按钮添加事件
	function updateButEvent() {
		var selectedRow = $.dataGrid.getSelectedRow(testDataGrid);
		if (selectedRow) {
			//请求服务端获取某条记录信息
			var reqId = selectedRow.id;
			$.ajaxPackage({
				type : 'get', 
				url : loadDataUrl + '/' + reqId,
				dataType : "json",
				success : function (data,textStatus,jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					//从服务器上获取到记录信息
					var attachment = data.attachment;
					if (resCode == '000000') {
						//操作成功 
						
						//显示窗口
						addDataPanel.window('open');
						//填充到表单项里
						dataForm.form('load',attachment);
					} else {
						//操作失败
						
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		} else {
			$.messager.alert('警告','请选中需要修改的记录!','warning');
		}
	}
	
	//删除按钮添加事件
	function delButEvent() {
		var selectedRow = $.dataGrid.getSelectedRow(testDataGrid);
		if (selectedRow) {
			$.messager.confirm('请确认', '确认删除?', function(r){
				if (r) {
					var reqId = selectedRow.id;
					deleteRowById(reqId);
				}
			});
		} else {
			$.messager.alert('警告','请选中需要删除的记录!','warning');
		}
	}
	
	//全局关闭按钮事件
	$('a[name=closeBut]').click(function(){
		//找到按钮属于的Window对象
		var ownWin = $(this).parents("div.easyui-window:eq(0)");
		if (ownWin.length) {
			ownWin.window('close');
		}
	})
	
	//指定某Window下提交按钮添加事件
	$(addDataPanel).find('a[name=submitBut]:eq(0)').click(function(){
		//检查表单项是否通过验证
		if (dataForm.form('validate')) {
			//提交到服务端，进行处理
			$.ajaxPackage({
				type : 'post', 
				url : saveOrUpdateUrl,
				data : dataForm.serialize(),
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') {
						//操作成功 重新加载列表数据
						reloadDataGrid();
						resMsg = '操作成功';
					} else {
						//操作失败
						
					}
					$.messager.alert('结果',resMsg);
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					//关闭窗口
					addDataPanel.window('close');
				}
			});
		} else {
			//存在必须项，未填充值
		}
	})
	
	//通过主键编号删除记录
	function deleteRowById(id) {
		if (id) {
			$.ajaxPackage({
				type : 'get', 
				url : deleteDataUrl + '/' + id,
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') { 
						//操作成功 重新加载列表数据
						reloadDataGrid();
						resMsg = '操作成功';
					} else {
						//操作失败
						
					}
					$.messager.alert('结果',resMsg);
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		}
	}
	
	//加载表格数据
	function reloadDataGrid() {
		//获取查询表单数据转换成JSON对象
		var searchMsg = searchForm.serialize();
		//对参数进行解码(显示中文)
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);
		
		testDataGrid.datagrid('reloadData',{
			url : dataGridUrl
		});
		
	}
	
	//默认查询表格数据
	reloadDataGrid();
	
})
