$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/system/role/findRoleList';

	// 加载菜单书的地址
	var createTree = global.contextPath	+ '/system/comPermission/createMuneTree';
	
	//加载角色
	var findParent = global.contextPath + '/system/SysDictionary/findParent';

	// 提交菜单书的地址
	var insertComEmployeePermission = global.contextPath + '/system/ComRolePermission/insertComRolePermission';

	// 根据用户id查询菜单地址
	var findEmpPerByRId = global.contextPath + '/system/ComRolePermission/findEmpPerByRid';

	// 加载角色树的地址
	var createRoleTree = global.contextPath + '/system/role/createRoleTree';

	// 新增 修改地址
	var saveOrUpdateUrl = global.contextPath + '/system/role/saveOrUpdateDataRole';

	// 查询某条记录详细信息地址
	var loadDataUrl = global.contextPath + '/system/role/loadDataRole';

	// 查询某条记录详细信息地址
	var deleteDataUrl = global.contextPath + '/system/role/deleteDataRole';

	//可分配角色
	var findComRoleHierarchyByRoleId=global.contextPath + '/system/role/findComRoleHierarchyByRoleId';
	
	var saveComRoleHierarchy=global.contextPath + '/system/role/saveComRoleHierarchy';
	// 表格实例
	var roleDataGrid = $('#roleDataGrid');
	var addDataPanel = $('#addDataPanel');
	var createMunePanel = $('#createMunePanel');
	var createRolePanel = $('#createRolePanel');
	// 新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	// 查询条件数据项表单实例
	var searchForm = $('#searchForm');

	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	/** 分页参数（page:当前第N页，rows:一页N行） * */
	var pg = {
		'page' : 1,
		'rows' : pageSize
	}
	
	roleDataGrid.datagrid({
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
		hideColumn : [ [ {
			field : 'id',
			width : 50
		} ] ],
		columns : [ [
		// 列定义
		{
			field : 'roleName',
			title : '角色名称',
			width : 100
		}, {
			field : 'creator',
			title : '创建人',
			width : 100
		}, {
			field : 'createTime',
			title : '创建时间',
			width : 100,
			formatter : $.DateUtil.dateFormatToStr
		}, {

			field : 'updateTime',
			title : '更新时间',
			width : 100,
			formatter : $.DateUtil.dateFormatToStr
		}, {
			field : 'updator',
			title : '更新人',
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

	 $("#clearCondition").bind("click",function(envent){
	    	$("#searchForm").form("reset");
     });

	/* 加载权限组数据 */
	$('#productTree').tree({
		checkbox : true
	})

	/* 加载权限组数据 */
	$('#roleTree').tree({
		checkbox : true
	})
	
	//新增按钮添加事件
	function setParentId() {
		$.ajaxPackage({
			type : 'get', 
			url : findParent,
			dataType : "json",
			isShowLoadMask:false,
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$('#sp1').combobox('loadData',attachment);
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

	setParentId();

	// 初始化菜单树
	function loadMuneTreeContent() {
		$.ajaxPackage({
			type : 'get',
			url : createTree,
			dataType : "json",
			isShowLoadMask:false,
			success : function(data, textStatus, jqXHR) {
				$('#productTree').tree('loadData', data.attachment[0].children);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			},
			complete : function() {

			}
		});
	}

	// 初始菜单树
	loadMuneTreeContent();

	// $('#productTree').tree('reload');
	// 表格分页实例

	
	
	var pager = roleDataGrid.datagrid('getPager');
	pager.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			pg.page = pageNumber;
			pg.rows = pageSize;
			reloadDataGrid();
		}
	});
	// 分配角色权限

	createRolePanel.window({
		width : 300,
		height : 400,
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
		 onBeforeClose: function () {
			loadRoleTreeContent();
			            }
	})
	// 分配菜单权限

	createMunePanel.window({
		width : 500,
		height : 400,
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
		onBeforeClose : function() {
			loadMuneTreeContent();
		}
	})

	// 新增面板参数定义
	addDataPanel.window({
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

	// 新增按钮添加事件
	$("#addBut").click(function() {
		// $('#addDataPanel').attr('title','新增面板');
		// 显示窗口
		addDataPanel.window('open');
		addDataPanel.panel({
			title : "新增"
		});
		// 重置表单数据
		dataForm.form('clear');
		$('#isActive').combobox('defaultOneItem');
	});

	// 分配菜单事件
	$("#createMuneBut").click(
		function() {
			// 获取列表
			var selectedRow = getSelectedRow(roleDataGrid);
			if (selectedRow) {
				// 获取选中的id
				var roleId = selectedRow.id;
				// 根据id查询用户拥有的权限菜单
				$('#createMunePanel').find('input[name="roleId"]').val(
						roleId);
				createMunePanel.window('center');
				// 根据userId获取用户拥有的菜单
				$.ajaxPackage({
					type : 'get',
					url : findEmpPerByRId,
					data : "empId=" + roleId,
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						var resCode = data.resCode;
						if (resCode == '000000') {
							var pid = data.attachment;
							var groups = pid.split(",");
							for (var i = 0; i < groups.length; i++) {
								var groupId = groups[i];
								var node = $('#productTree').tree('find', groupId);
								if (node != null) {
									var children = $('#productTree').tree('getChildren', node.target);
									if (children.length == 0) {
										$('#productTree').tree('check',	node.target);
									}
								}
							}
						}
					},
					error : function(XMLHttpRequest, textStatus,
							errorThrown, d) {
						$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					},
					complete : function() {

					}
				});

				// 显示窗口
				createMunePanel.window('open');

			} else {
				$.messager.alert('警告', '请选择需要分配菜单权限的人员!', 'warning');
			}
		});

	// 点击提交，获取选中的checkbox集合
	$("#productTreeSubmit").click(
		function() {
			var roleId = $('#createMunePanel').find('input[name="roleId"]').val();
			var nodes = $('#productTree').tree('getChecked', [ 'checked', 'indeterminate' ]);
			var s = '';
			for (var i = 0; i < nodes.length; i++) {
				if (s != '')
					s += ',';
				s += nodes[i].id;
			}
			$.ajaxPackage({
				type : 'get',
				url : insertComEmployeePermission,
				data : "roleId=" + roleId + "&pid=" + s,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					// alert(data.attachment[0].children);
					// 从服务器上获取到记录信息
					var resCode = data.resCode;
					if (resCode == '000000') {
						$.messager.alert("操作提示", "操作成功！");
					}
				},
				error : function(XMLHttpRequest, textStatus,
						errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  '
							+ errorThrown + '!', 'error');
				},
				complete : function() {
					// 显示窗口
					createMunePanel.window('close');
					// loadMuneTreeContent();
				}
			});
		})

	// 修改按钮添加事件
	$("#updateBut").click(
		function() {
			var selectedRow = getSelectedRow(roleDataGrid);
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
							// 操作成功
							addDataPanel.panel({title : "修改"});
							// 显示窗口
							addDataPanel.window('open');
							// 填充到表单项里
							dataForm.form('load', attachment);
	
						} else {
							// 操作失败
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown, d) {
						$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					},
					complete : function() {
	
					}
				});
			} else {
				$.messager.alert('警告', '请选中需要修改的记录!', 'warning');
			}
	});

	// 删除按钮添加事件
	$("#delBut").click(function() {
		var selectedRow = getSelectedRow(roleDataGrid);
		if (selectedRow) {
			$.messager.confirm('请确认', '确认删除?', function(r) {
				if (r) {
					var reqId = selectedRow.id;
					deleteRowById(reqId);
				}
			});
		} else {
			$.messager.alert('警告', '请选中需要删除的记录!', 'warning');
		}
	})

	// 关闭按钮事件
	$("#productTreeColse").click(function() {
		// 找到按钮属于的Window对象
		$('#createMunePanel').window('close');
//		loadMuneTreeContent();

	})
	// 关闭按钮事件
	$("#roleTreeColse").click(function() {
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
	$(addDataPanel).find('a[name=submitBut]:eq(0)').click(function() {
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
						$.messager.alert('结果', resMsg);
					} else {
						// 操作失败
						$.messager.alert('错误', resMsg);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				},
				complete : function() {
					// 关闭窗口
					addDataPanel.window('close');
				}
			});
		}
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

	// 获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = dataGrid.datagrid('getSelected');
		return selectedRow;
	}

	// 初始化角色树
	function loadRoleTreeContent() {
		$.ajaxPackage({
			type : 'get',
			url : createRoleTree,
			dataType : "json",
			isShowLoadMask:false,
			success : function(data, textStatus, jqXHR) {
				$('#roleTree').tree('loadData', data.attachment[0].children);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			},
			complete : function() {

			}
		});
	}

	loadRoleTreeContent();
	// 分配角色事件
	$("#createRoleBut").click(function() {
		// 获取列表
		var selectedRow = getSelectedRow(roleDataGrid);
		if (selectedRow) {
			// 获取选中的id
			var roleId = selectedRow.id;
			// 根据id查询用户拥有的权限菜单
			$('#createRolePanel').find('input[name="roleId"]').val(roleId);
				$.ajaxPackage({
					type : 'get',
					url : findComRoleHierarchyByRoleId,
					data : "roleId=" + roleId,
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						var resCode = data.resCode;
						if (resCode == '000000') {
							var pid = data.attachment;
							var groups = pid.split(",");
							if (groups.length > 0) {
								for (var i = 0; i < groups.length; i++) {
									var groupId = groups[i];
									var node = $('#roleTree').tree('find', groupId);
									if (node) {
										$('#roleTree').tree('check', node.target);
									}
								}
							}
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown, d) {
						$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					},
					complete : function() {

					}
				});

			// 显示窗口
			createRolePanel.window('open');

		} else {
			$.messager.alert('警告', '请选中需要分配的人员!', 'warning');
		}
	});

	// 点击提交，获取选中的角色checkbox集合
	$("#roleTreeSubmit").click(function() {
		var roleId = $('#createRolePanel').find('input[name="roleId"]')
				.val();
		var nodes = $('#roleTree').tree('getChecked');
		var s = '';
		for (var i = 0; i < nodes.length; i++) {
			if (s != '')
				s += ',';
			s += nodes[i].id;
		}
		$.ajaxPackage({
			type : 'get',
			url : saveComRoleHierarchy,
			data : "roleId=" + roleId + "&pid=" + s,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {

				// 从服务器上获取到记录信息
				var resCode = data.resCode;
				if (resCode == '000000') {
					// alert(data.attachment[0].children);
					$.messager.alert("操作提示", "操作成功！");
				}
			},
			error : function(XMLHttpRequest, textStatus,
					errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  '
						+ errorThrown + '!', 'error');
			},
			complete : function() {
				createRolePanel.window('close');
			}
		});
	})

	// 关闭按钮事件
	$("#roleTreeColse").click(function() {
//		loadRoleTreeContent();
		// 找到按钮属于的Window对象
		$('#createRolePanel').window('close');

	})

	// 加载表格数据
	function reloadDataGrid() {
		// 获取查询表单数据转换成JSON对象
		var searchMsg = searchForm.serialize();
		// 对参数进行解码(显示中文)
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url = dataGridUrl;
		$(roleDataGrid).datagrid('reloadData', queryParam);
	}

	// 默认查询表格数据
	reloadDataGrid();

})
