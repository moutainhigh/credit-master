function addTab(parentId) {
	parent.$.iframeTabs.add({
		'text' : '数据字典子页',
		'url' : global.contextPath + '/system/SysDictionary/findSysDictionaryListBy?parentId=' + parentId
	});
}

$(function() {
	
	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/system/SysDictionary/findSysDictionaryList';

	var findParent = global.contextPath + '/system/SysDictionary/findParent';
	
	var insertUrl = global.contextPath + '/system/SysDictionary/saveOrUpdateDataAndSysD';
	
	var loadDataSysDictionary = global.contextPath + '/system/SysDictionary/loadDataSysDictionary';
	
	var deleteDataUrl=global.contextPath + '/system/SysDictionary/deleteData';

	// 每页显示的记录条数，默认为10
	var pageSize = 10;

	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];

	var sysDataGrid = $('#sysDataGrid');

	// 查询条件数据项表单实例
	var dataForm = $('#dataForm');
	var searchForm = $('#searchForm');
	var addDataPanel = $('#addDataPanel');
	/** 分页参数（page:当前第N页，rows:一页N行） * */
	var pg = {
		'page' : 1,
		'rows' : pageSize
	}
	sysDataGrid.datagrid({
	//	url : dataGridUrl,
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
		
		
		columns : [ [
		// 列定义
		 {
			field : 'codeName',
			title : '代码',
			width : 100,
			formatter :function(value,row,rowIndex){
		    	  return '<a name="idclick" onclick="addTab('+row.id+');" href="javascript:void(0);" id="idclick">' + value + '</a>';
		      }
		}, {
			field : 'codeTitle',
			title : '标题',
			width : 100
		},
		{
			field : 'codeType',
			title : '类型',
			width : 100
		}
		, {
			field : 'codeTypeTitle',
			title : '类型名称',
			width : 100
		}, {
			field : 'seqence',
			title : '排序',
			width : 100
		}, {
			field : 'codeValue',
			title : '代码值',
			width : 100
		}

		] ],
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
	
	
	//新增面板参数定义
	addDataPanel.window({
		width : 600,
		height : 500,
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
	
	// 查询按钮添加事件
	$("#searchBut").click(function() {
		pg.page = 1;
		reloadDataGrid();
	});

	// 表格分页实例
	var pager = sysDataGrid.datagrid('getPager');
	pager.pagination({
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
		// 对参数进行解码(显示中文)
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url = dataGridUrl;		
		$(sysDataGrid).datagrid('reloadData', queryParam);
	}

	 $("#clearCondition").bind("click",function(envent){
	    	$("#searchForm").form("reset");
	 });

	// 默认查询表格数据
	reloadDataGrid();
	$('#addBut').click(function() {
		addButEvent();
	})

	$('#updateBut').click(function() {
		updateButEvent();
	})

	//新增按钮添加事件
	function setParentId() {
		$.ajaxPackage({
			type : 'get', 
			url : findParent,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$('#parentId').combobox('loadData',attachment);
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});		
	}

	  //setParentId();
	function addButEvent() {
		// 显示窗口
		addDataPanel.window('open');
		addDataPanel.panel({
			title : "新增"
		});
		// 重置表单数据
		dataForm.form('clear');
	}

	//修改按钮添加事件
	function updateButEvent() {
		addDataPanel.panel({title: "修改"});
		var selectedRow = getSelectedRow(sysDataGrid);
		if (!selectedRow) {
			$.messager.alert('警告', '请选中需要修改的记录!', 'warning');
			return;
		}
		var id=	selectedRow.id
		$.ajaxPackage({
			type : 'get', 
			url : loadDataSysDictionary+ '/' + id,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					dataForm.form('clear');
					dataForm.form('load',attachment);
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				//显示窗口
				addDataPanel.window('open');
			}
		});
	}

	//指定某Window下提交按钮添加事件
	$(addDataPanel).find('a[name=submitBut]:eq(0)').click(function(){
		//检查表单项是否通过验证
		if (dataForm.form('validate')) {
			//提交到服务端，进行处理
			$.ajaxPackage({
				type : 'post', 
				url : insertUrl,
				data : dataForm.serialize(),
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') {
						//操作成功 重新加载列表数据
						resMsg = '操作成功';
					}
					$.messager.alert('结果',resMsg);
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					//关闭窗口
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

	// 关闭按钮事件
	$("#closeBut").click(function() {
		// 找到按钮属于的Window对象
		$('#addDataPanel').window('close');
	})

	// 删除按钮添加事件
	$("#delBut").click(function() {
		var selectedRow = getSelectedRow(sysDataGrid);
		if (!selectedRow) {
			$.messager.alert('警告', '请选中需要删除的记录!', 'warning');
			return;
		}
		$.messager.confirm('请确认', '确认删除?', function(r) {
			if (r) {
				var reqId = selectedRow.id;
				deleteRowById(reqId);
			}
		});
	})

	// 通过主键编号删除记录
	function deleteRowById(id) {
		if (id) {
			$.ajaxPackage({
				type : 'get',
				url : deleteDataUrl + '/' + id,
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

				}
			});
		}
	}
})
