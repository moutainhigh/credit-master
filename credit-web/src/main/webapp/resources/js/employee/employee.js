$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/system/user/search';

	// 加载菜单书的地址
	var createTree = global.contextPath + '/system/comPermission/createMuneTree';

	// 加载角色树的地址
	var createRoleTree = global.contextPath + '/system/role/createRoleHTree';

	// 提交菜单书的地址
	var insertComEmployeePermission = global.contextPath + '/system/ComEmployeePermission/insertComEmployeePermission';

	// 提交角色树的地址
	var insertComEmployeeRole = global.contextPath + '/system/ComEmployeeRole/insertComEmployeeRole';

	// 根据用户id查询菜单地址
	var findEmpPerByEId = global.contextPath + '/system/ComEmployeePermission/findEmpPerByEId';

	// 根据用户id查询角色菜单地址
	var findEmpPerByEIdAndRole= global.contextPath + '/system/ComEmployeePermission/findEmpPerByEIdAndRole';

	// 根据用户id查询角色地址
	var findEmpRoleByEId = global.contextPath + '/system/ComEmployeeRole/findEmpRoleByEId';

	/** 查询某营业网点信息 请求地址 * */
	var loadOrgDataUrl = global.contextPath	+ '/system/comOrganization/loadOrgData';

	var loadChildNodeUrl = global.contextPath + '/system/comOrganization/loadChildNode';

	// 新增员工
	var insertComEmployee = global.contextPath + '/system/user/insertComEmployee';

	// 删除员工
	var updateEmployee = global.contextPath + '/system/user/updateEmployee';

	var findComEmployeeById = global.contextPath + '/system/user/findComEmployeeById';

     //入职批量导入
	var entryImportEmp = global.contextPath + '/system/user/entryImportEmp';

	//离职批量导入
	var quitImportEmp = global.contextPath + '/system/user/quitImportEmp';

	var ydImportEmp = global.contextPath + '/system/user/ydImportEmp';

	// 表格实例
	var testDataGrid = $('#testDataGrid');
	var addEmployeePanel = $('#addEmployeePanel');
	var createMunePanel = $('#createMunePanel');
	var createRolePanel = $('#createRolePanel');
	var importExcelWin = $('#importExcelWin');
	var orgPanel = $('#orgPanel');
	// 新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	var entryFileForm = $('#entryFileForm');
	var quitFileForm = $('#quitFileForm');
	var ydFileForm= $('#ydFileForm');
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
			field : 'name',
			title : '姓名',
			width : 100
		}, {
			field : 'usercode',
			title : '工号',
			width : 100
		}
		, {
			field : 'employeeType',
			title : '员工类型',
			width : 100
		}, {
			field : 'fullName',
			title : '所属组织机构',
			width : 200
		}, {
			field : 'createTime',
			title : '创建时间',
			width : 100,
			formatter : $.DateUtil.dateFormatToStr
		}, {
			field : 'creator',
			title : '创建人',
			width : 100
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

	/* 加载权限组数据 */
	$('#productTree').tree({
		checkbox : true
	});

	/* 加载权限组数据 */
	$('#roleTree').tree({
		checkbox : true
	});

	// 菜单树
	function loadMuneTreeContent() {
		$.ajaxPackage({
			type : 'get',
			url : createTree,
			dataType : "json",
			isShowLoadMask:false,
			success : function(data, textStatus, jqXHR) {
				// alert(data.attachment[0].children);
				$('#productTree').tree('loadData',
						data.attachment[0].children);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  '
						+ errorThrown + '!', 'error');
			},
			complete : function() {

			}
		});
	}

	// 初始化角色树
	function loadRoleTreeContent() {
		$.ajaxPackage({
			type : 'get',
			url : createRoleTree,
			dataType : "json",
			isShowLoadMask:false,
			success : function(data, textStatus, jqXHR) {
				// alert(data.attachment[0].children);
				$('#roleTree').tree('loadData', data.attachment[0].children);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			},
			complete : function() {

			}
		});
	}

	loadMuneTreeContent();
	loadRoleTreeContent();
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
		onBeforeClose : function() {
			loadRoleTreeContent();
		}
	});

	// 组织机构面板
	orgPanel.window({
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

	// 新增面板参数定义
	addEmployeePanel.window({
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
		//旧密码
		oldPassword:"",
		//在关闭窗口后将oldPassword置为空
		onClose:function(){
			$(this).data("oldPassword","");
		}
	});

	 $("#clearCondition").bind("click", function(envent) {
		$("#searchForm").form("reset");
	});

	// 新增按钮添加事件
	$("#addBut").click(function() {
		// 显示窗口
		addEmployeePanel.window('open');
		addEmployeePanel.panel({title: "新增"});
		$('#userCodes').textbox({'readonly':false});
		// 重置表单数据
		dataForm.form('clear');
		$('#inActive').combobox('defaultOneItem');
		$('#employeeType').combobox('defaultOneItem');
		orgtips.html('<p></p>');
		$('#addorgBut').linkbutton({    
		    iconCls: 'icon-add',
		    text:'新增组织机构'
		});  
	});

	// 批量导入按钮添加事件
	$("#import").click(function() {
		// 显示窗口
		importExcelWin.window('open');
		entryFileForm.form('clear');
		quitFileForm.form('clear');;
		ydFileForm.form('clear');

	});

	//点击组织机构面板
	$("#addorgBut").click(function() {
		$('#comOrganizationTree').children('li').remove();
		/** 调用初始化树结点方法 **/
		initTree(null,0,function () {
			/** 默认选中第一个结点数据 **/
			var defaultNodeId = comOrganizationTree.tree('getRoot').id;
			if (!$.isEmpty(defaultNodeId)) {
				selectedAndLoadData(defaultNodeId);
			}
		});
		orgPanel.window('open');
	});

	// 修改按钮添加事件
	$("#updateBut").click(
		function() {
			addEmployeePanel.panel({title: "修改"});
			$('#addorgBut').linkbutton({    
			    iconCls: 'icon-save' ,
			    text:'修改组织机构'
			});  
			var selectedRow = getSelectedRow(testDataGrid);
			if (selectedRow) {
				$('#comOrganizationTree').children('li').remove();
				/** 调用初始化树结点方法 * */
				initTree(null, 0);
				var userId = selectedRow.id;
				// 根据id查询，在赋值
				$.ajax({
					type : 'get',
					url : findComEmployeeById,
					data : "id=" + userId,
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						var resCode = data.resCode;
						if (resCode == '000000') {
							//清理form
							dataForm.form('clear');
							var a = data.attachment;
							$('#userCodes').textbox({'readonly':true});
							// 填充到表单项里
							dataForm.form('load', a);
							addEmployeePanel.data("oldPassword",a.password);
							orgtips.html('<p>当前操作：营业网点(<span style="color:red;">'+a.memo+'</span>)</p>');
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown, d) {
						$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
					},
					complete : function() {
						// 显示窗口
						addEmployeePanel.window('open');
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

	// 分配菜单事件
	$("#createMuneBut").click(function() {
		// 获取列表
		var selectedRow = getSelectedRow(testDataGrid);
		if (selectedRow) {

			function showOpen() {
				createMunePanel.window('center');
				// 显示窗口
				createMunePanel.window('open');
			}
			// 获取选中的id
			var userId = selectedRow.id;
			// 根据id查询用户拥有的权限菜单
			$('#createMunePanel').find('input[name="userId"]').val(userId);
			findEmpPerByEIdAndRoleF(userId, showOpen);
			findProductByEmployeeAndRoleId(userId, showOpen);
		} else {
			$.messager.alert('警告', '请选中需要分配的人员!', 'warning');
		}
	});

	// 分配菜单事件，第二步，查询自己拥有的菜单
	function findProductByEmployeeAndRoleId(userId, showo) {
		// 根据userId获取用户拥有的菜单
		$.ajax({
			type : 'get',
			url : findEmpPerByEId,
			data : "empId=" + userId,
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
							var a = $('#productTree').tree('getChildren',
									node.target);

							if (a.length <= 0) {
								if (node) {
									$('#productTree').tree('check', node.target);
								}
							}
						}
					}

					if (showo.state == true) {
						showo();
					} else {
						showo.state = true
					}
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			},
			complete : function() {

			}
		});
	}

	//分配菜单事件，第一步，根据用户的角色查询已经拥有的菜单，置灰，不能修改
	function findEmpPerByEIdAndRoleF(userId, showo) {
		// 根据userId获取用户拥有的菜单
		$.ajax({
			type : 'get',
			url : findEmpPerByEIdAndRole,
			data : "empId=" + userId,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				var resCode = data.resCode;
				if (resCode == '000000') {
					var pid = data.attachment;
					var groups = pid.split(",");
					for (var i = 0; i < groups.length; i++) {
						var groupId = groups[i];

						var node = $('#productTree').tree('find',
								groupId);
						if (node != null) {
							var a = $('#productTree').tree(
									'getChildren', node.target);
							if (a.length <= 0) {
								if (node) {
									$(node.target).find('span.tree-checkbox').addClass('tree-checkbox-disabled1').unbind().click(function() {
										return false;
									});
								}
							}
						}
					}

					if (showo.state == true) {
						showo();
					} else {
						showo.state = true
					}
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
				$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
			},
			complete : function() {

			}
		});
	}

	// 分配角色事件
	$("#createRoleBut").click(
		function() {
			// 获取列表
			var selectedRow = getSelectedRow(testDataGrid);
			if (selectedRow) {

				createRolePanel.window('center');
				// 获取选中的id
				var userId = selectedRow.id;
				// 根据id查询用户拥有的权限菜单
				$('#createRolePanel').find('input[name="userId"]').val(userId);
				// 根据userId获取用户拥有的菜单
				$.ajaxPackage({
					type : 'get',
					url : findEmpRoleByEId,
					data : "empId=" + userId,
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						var resCode = data.resCode;
						if (resCode == '000000') {
							var pid = data.attachment;
							var groups = pid.split(",");
							for (var i = 0; i < groups.length; i++) {
								var groupId = groups[i];
								var node = $('#roleTree').tree('find', groupId);
								if (node != null) {
									$('#roleTree').tree('check', node.target);
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

	// 点击提交，获取选中的checkbox集合
	$("#productTreeSubmit").click(
		function() {
			var userId = $('#createMunePanel').find('input[name="userId"]')
					.val();
			var nodes = $('#productTree').tree('getChecked',
					[ 'checked', 'indeterminate' ])
			var s = '';
			for (var i = 0; i < nodes.length; i++) {
				if (s != '')
					s += ',';
				s += nodes[i].id;
			}
			$.ajaxPackage({
				type : 'get',
				url : insertComEmployeePermission,
				data : "empId=" + userId + "&pid=" + s,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					// alert(data.attachment[0].children);
					// 从服务器上获取到记录信息
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') {
						$.messager.alert("操作提示", "操作成功！");
					} else {
						$.messager.alert('异常信息', resMsg, 'error');
					}
				},
				error : function(XMLHttpRequest, textStatus,
						errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
				},
				complete : function() {
					createMunePanel.window('close');
				}
			});

		})

	// 点击提交，获取选中的角色checkbox集合
	$("#roleTreeSubmit").click(
		function() {
			var userId = $('#createRolePanel').find('input[name="userId"]')
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
				url : insertComEmployeeRole,
				data : "empId=" + userId + "&pid=" + s,
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
						addEmployeePanel.window('open');
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

	// 删除按钮添加事件
	function delButEvent() {
		var selectedRow = getSelectedRow(testDataGrid);
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
	}

	// 关闭按钮事件
	$("#productTreeColse").click(function() {
		// 找到按钮属于的Window对象
//		loadMuneTreeContent();
		$('#createMunePanel').window('close');

	})
	// 关闭按钮事件
	$("#employeeColse").click(function() {
		// 找到按钮属于的Window对象
		orgtips.html('<p>当前操作：营业网点(<span style="color:red;"></span>)</p>');
		$('#addEmployeePanel').find('input[name="orgId"]').val();
		$('#addEmployeePanel').window('close');

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
	$(addEmployeePanel).find('a[name=submitBut]:eq(0)').click(
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
							addEmployeePanel.window('close');
						}
					});
				} else {
					// 存在必须项，未填充值
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
	}

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

	/** 初始化树结点 * */
	function initTree(parentNode, parentId, callBackFun) {
		if (parentNode) {
			if (parentNode.attributes.isLoad == '1') {
				return;
			}
		}
		$.ajaxPackage({
			type : 'get',
			url : loadChildNodeUrl + '/' + parentId,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				var resCode = data.resCode;
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					/** 操作成功* */
					comOrganizationTree.tree('append', {
						parent : ($.isEmpty(parentNode) == true ? null
								: parentNode.target),
						data : attachment
					});
					if (parentNode) {
						parentNode.attributes.isLoad = '1';
					}
					if ($.isFunction(callBackFun)) {
						callBackFun();
					}
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
	
	
	$("#organizationTreeSubmit").click(function() {
		var path=$('#path').val();
		orgtips.html('<p>当前操作：营业网点(<span style="color:red;">' + path + '</span>)</p>');
		
		initTree(null, 0);
		// 找到按钮属于的Window对象
		orgPanel.window('close');

	})
	// 关闭按钮事件
	$("#organizationTreeColse").click(function() {
		initTree(null, 0);
		// 找到按钮属于的Window对象
		orgPanel.window('close');

	})
	comOrganizationTree.tree({
		/** 单击树结点事件 * */
		onClick : function(node) {
			var parentId = node.id;
			$('#addEmployeePanel').find('input[name="orgId"]').val(parentId);
			if ($.isEmpty(parentId)) {
				return;
			}
			/** 加载子结点数据，追加到树结构中* */
			initTree(node, parentId);
			selectedAndLoadData(parentId);
		}
	})
	/** 结点结点同时加载结点详细信息 **/
	function selectedAndLoadData(nodeId) {
		var nodeTmp = comOrganizationTree.tree('find',nodeId);
		if (!$.isEmpty(nodeTmp)) {
			comOrganizationTree.tree('select',nodeTmp.target);
			
			/** 在页面上添加新增和修改提示信息 **/
			var path = getParentPath(nodeTmp)
			$('#addEmployeePanel').find('input[name="path"]').val(path);
			tips.html('<p>当前操作：营业网点(<span style="color:red;">' + path + '</span>)</p>');
	      //  orgtips.html('<p>当前操作：<a href="#" class="easyui-linkbutton" id="updateorgBut" iconCls="icon-add" plain="true">营业网点</a>(<span style="color:red;">' + 
	        		//path + '</span>)</p>');
					
			
		} else {
			$.messager.alert('提示信息','结点编号[' + nodeId + ']未找到!','warning');
		}
	}
	
	/** 获取父结点路径 如 /证大投资/北区/江苏分部 **/
	function getParentPath (node) {
		var path = '';
		var parentNode = comOrganizationTree.tree('getParent',node.target);
		if (parentNode) {
			var pathTmp = getParentPath(parentNode);
			path = pathTmp + '/' + node.text;
			return path;
		} else {
			return '/' + node.text;
		}
	}
	
	
	// 新增员工提交
	$("#employeeSubmit").click(
			function() {
				if (dataForm.form('validate')) {
					var ordId = $('#orgId').val();
					
					var oldPassword =  addEmployeePanel.data("oldPassword");
					if(oldPassword != $('#password').val() ){
						if(!checkPass($('#password').val())){
							return;
						}
					}
			
					if (ordId == '') {
						$.messager.alert("操作提示", "请选择组织机构！");
						return;
					}
					var a = $('#addEmployeePanel').find('input[name="id"]')
							.val();
					$.ajaxPackage({
						type : 'get',
						url : insertComEmployee,
						data : dataForm.serialize(),
						dataType : "json",
						success : function(data, textStatus, jqXHR) {
							// 从服务器上获取到记录信息
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							if (resCode == '000000') {
								$.messager.alert("操作提示", resMsg);
								addEmployeePanel.window('close');
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

			
			function checkPass(newPassWord){
				/*if(newPassWord.length<6 ||newPassWord.length>10){
					$.messager.alert('提示信息','密码为6位-10位','error');
					return false;
				}
				
				if(newPassWord.length<6 ||newPassWord.length>10){
					$.messager.alert('提示信息','确认密码为6位-10位','error');
					return false;
				}*/
				var regex = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$/;
				var flag=regex.test(newPassWord);
				if(!flag){
					$.messager.alert('提示信息','系统密码为6位-10位，必须是字母和数字的组合','error');
					return false;
				}
				return true;
	        }
	// 删除员工提交
	$("#delBut").click(function() {
		var selectedRow = getSelectedRow(testDataGrid);
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
			url : updateEmployee,
			data : "id=" + reqId,
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				// 从服务器上获取到记录信息
				var resCode = data.resCode;
				if (resCode == '000000') {
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
	
	  /** 清空提示信息 **/
    function clearMessage(){
        $("#message").text("");
    }
	
	 /** 入职批量导入 **/
    $("#entryImport").click(function(){
        // 清空提示信息
      //  clearMessage();
        var file = $("#entryfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
            	entryFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: entryImportEmp,
                    hasDownloadFile:true,
                    success: function () {
                  	   $.messager.alert('提示','下载成功！','info');
                     },
                     error: function (data) {
                    	if(data!=null&&data.hasOwnProperty("resMsg")){
                    		 $.messager.alert('操作提示',data.resMsg,'warning');
                    	}else{
                    		 $.messager.alert('操作提示','文件上传失败!','warning');
                    	}
                     }
                });
            }
        });
    });
    
	 /** 离职批量导入 **/
    $("#quitfileBatchImport").click(function(){
        // 清空提示信息
      //  clearMessage();
        var file = $("#quitfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
            	quitFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: quitImportEmp,
                    hasDownloadFile:true,
                    success: function () {
                 	   $.messager.alert('提示','下载成功！','info');
                    },
                    error: function (data) {
	                   	if(data!=null&&data.hasOwnProperty("resMsg")){
	                   		 $.messager.alert('操作提示',data.resMsg,'warning');
	                   	}else{
	                   		 $.messager.alert('操作提示','文件上传失败!','warning');
	                   	}
                    }
                });
            }
        });
    });

	 /** 异动批量导入 **/
   $("#ydfileBatchImport").click(function(){
       // 清空提示信息
     //  clearMessage();
       var file = $("#ydfile").filebox("getValue");
       if($.isEmpty(file)){
           $.messager.alert('警告','请选择导入文件!','warning');
           return;
       }
       $.messager.confirm("提示","确认批量导入吗？",function(r){
           if(r){
        	   ydFileForm.ajaxSubmit({
                   type: "post",
                   dataType : 'json',
                   url: ydImportEmp,
                   hasDownloadFile:true,
                   success: function () {
                	   $.messager.alert('提示','下载成功！','info');
                   },
                   error: function (data) {
                      	if(data!=null&&data.hasOwnProperty("resMsg")){
                      		 $.messager.alert('操作提示',data.resMsg,'warning');
                      	}else{
                      		 $.messager.alert('操作提示','文件上传失败!','warning');
                      	}
                   }
               });
           }
       });
   });
})
