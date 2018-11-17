$(function() {
	$.csOutsourcingAreaIdNum = $.extend($.csOutsourcingAreaIdNum || {}, {
		/** 身份证列表 数据源地址 * */
		idNumDataGridUrl : global.contextPath + '/outsourcingAreaIdNum/searchIdNumList',
		/** 新增/更新身份证信息 * */
		saveOrUpdateAreaIdNumUrl : global.contextPath + '/outsourcingAreaIdNum/saveOrUpdateAreaIdnum',
		/** 删除身份证信息 地址 * */
		deleteAreaIdNumUrl : global.contextPath + '/outsourcingAreaIdNum/deleteAreaIdnum',
		/** 查询身份证信息 地址 * */
		loadAreaIdNumInfoUrl : global.contextPath + '/outsourcingAreaIdNum/loadAreaIdNumInfo',
		/** 身份证号列表 * */
		idNumDataGrid : $('#idNumDataGrid'),
		areaConfigIdNumWin : $('#areaConfigIdNumWin'),
		areaConfigIdNumForm : $('#areaConfigIdNumForm'),
		idNumTips : $('#idNumTips'),
		idNumDataGridPg : {
			page : 1,
			rows : 10
		},
		isValidData : {
			'0' : '有效',
			'1' : '无效'
		},
		/** 刷新身份证号列表数据 * */
		reloadIdNumDataGridData : function() {
			var selectedNode = $.csOutsourcingArea.selectedNode;
			/** 区域编号 * */
			var areaId = selectedNode.id;
			if (!$.isEmpty(areaId)) {
				var queryParam = {};
				queryParam.url = $.csOutsourcingAreaIdNum.idNumDataGridUrl;
				queryParam.areaId = areaId;
				$.csOutsourcingAreaIdNum.idNumDataGrid.datagrid('reloadData', queryParam);
			}
		},
		/** 页面初始化操作 * */
		init : function() {
			$.csOutsourcingAreaIdNum.areaConfigIdNumWin.window({
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

			$.csOutsourcingAreaIdNum.idNumDataGrid.datagrid({
				pg : $.csOutsourcingAreaIdNum.idNumDataGridPg,
				/** 提交方式 * */
				method : 'get',
				width : '100%',
				/** 是否显示行号 * */
				rownumbers : true,
				/** 是否单选 * */
				singleSelect : true,
				/** 是否可折叠的 * */
				collapsible : false,
				selectOnCheck : true,
				checkOnSelect : true,
				/** 自适应列宽 * */
				fitColumns : true,
				fit : true,
				/** 是否开启分页 * */
				pagination : true,
				loadMsg : '数据加载中,请稍等...',
				/** 锁定列定义 * */
				frozenColumns : [ [
				// {field : '',checkbox:'true',title:'123'}
				] ],
				columns : [ [
				/** 列定义 * */
				{
					field : 'idNum',
					title : '身份证号（前4位）',
					width : 1
				}, {
					field : 'isValid',
					title : '是否有效',
					formatter : function(value, row, index) {
						return $.csOutsourcingAreaIdNum.isValidData[value + ''] || '未知';
					},
					width : 1
				}, {
					field : 'memo',
					title : '备注',
					width : 1
				} ] ],
				/** 每页显示的记录条数，默认为10 * */
				pageSize : $.csOutsourcingAreaIdNum.idNumDataGridPg.rows,
				/** 可以设置每页记录条数的列表 * */
				pageList : [ 2, 10, 20, 30, 40, 50 ],
				toolbar : '#idNumDataGirdTB',
				/** 自定义行样式 * */
				rowStyler : function(index, row) {
					if (index % 2 == 0) {
						// return 'background-color:#AABBCC;color:#fff;';
					}
				}
			});
			$.csOutsourcingAreaIdNum.idNumDataGrid.datagrid('getPager').pagination({
				onSelectPage : function(pageNumber, pageSize) {
					$.csOutsourcingAreaIdNum.idNumDataGridPg.page = pageNumber;
					$.csOutsourcingAreaIdNum.idNumDataGridPg.rows = pageSize;
					/** 刷新身份证号列表数据 * */
					$.csOutsourcingAreaIdNum.reloadIdNumDataGridData();
				}
			});

			$.csOutsourcingAreaIdNum.idNumDataGrid.datagrid('resize');

			$('#idNumAdd').click(function() {
				var selectedNode = $.csOutsourcingArea.selectedNode;
				if (selectedNode && selectedNode.id != '0') {
					var areaPath = $.csOutsourcingArea.getParentPath(selectedNode);
					$.csOutsourcingAreaIdNum.idNumTips.html('当前区域：' + areaPath);
					$.csOutsourcingAreaIdNum.showAreaIdNumConfigWin();
					var areaId = selectedNode.id;
					var formData = {};
					formData.isValid = '0';
					formData.areaId = areaId;
					$.csOutsourcingAreaIdNum.areaConfigIdNumForm.form('load', formData);
				} else {
					$.messager.alert('提示信息', '请先选择区域!', 'warning');
				}
			})
			$('#idNumModify').click(function() {
				var row = $.dataGrid.getSelectedRow($.csOutsourcingAreaIdNum.idNumDataGrid);
				if (row) {
					var formData = {};
					formData.id = row.id;
					$.ajaxPackage({
						type : 'get',
						url : $.csOutsourcingAreaIdNum.loadAreaIdNumInfoUrl,
						data : formData,
						dataType : "json",
						success : function(data, textStatus, jqXHR) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							var attachment = data.attachment;
							if (resCode == '000000') {
								/** 操作成功* */
								$.csOutsourcingAreaIdNum.showAreaIdNumConfigWin();
								$.csOutsourcingAreaIdNum.areaConfigIdNumForm.form('load', attachment);
							} else {
								/** 操作失败* */
								$.messager.alert('提示信息', resMsg, 'error');
							}
						},
						error : function(XMLHttpRequest, textStatus, errorThrown, d) {
							$.messager.alert('提示信息', textStatus + '  :  ' + errorThrown + '!', 'error');
						},
						complete : function() {

						}
					});
				} else {
					$.messager.alert('警告', '请先选中列表中的数据!', 'warning');
				}
			})
			$('#idNumRemove').click(function() {
				var row = $.dataGrid.getSelectedRow($.csOutsourcingAreaIdNum.idNumDataGrid);
				if (row) {
					$.messager.confirm('请确认', '确认删除?', function(r) {
						if (r) {
							var formData = {};
							formData.id = row.id;
							$.ajaxPackage({
								type : 'get',
								url : $.csOutsourcingAreaIdNum.deleteAreaIdNumUrl,
								data : formData,
								dataType : "json",
								success : function(data, textStatus, jqXHR) {
									var resCode = data.resCode;
									var resMsg = data.resMsg;
									var attachment = data.attachment;
									if (resCode == '000000') {
										/** 操作成功* */
										$.csOutsourcingAreaIdNum.reloadIdNumDataGridData();
										$.messager.alert('提示信息', '操作成功!', 'info');
									} else {
										/** 操作失败* */
										$.messager.alert('提示信息', resMsg, 'error');
									}
								},
								error : function(XMLHttpRequest, textStatus, errorThrown, d) {
									$.messager.alert('提示信息', textStatus + '  :  ' + errorThrown + '!', 'error');
								},
								complete : function() {

								}
							});
						}
					});
				} else {
					$.messager.alert('警告', '请先选中列表中的数据!', 'warning');
				}
			})
			/** 新增/更新身份证信息 * */
			$('#submitAreaIdNumReq').click(function() {
				if ($.csOutsourcingAreaIdNum.areaConfigIdNumForm.form('validate')) {
					$.messager.confirm('请确认', '确认提交?', function(r) {
						if (r) {
							var formData = $.csOutsourcingAreaIdNum.areaConfigIdNumForm.serialize();
							$.ajaxPackage({
								type : 'post',
								url : $.csOutsourcingAreaIdNum.saveOrUpdateAreaIdNumUrl,
								data : formData,
								dataType : "json",
								success : function(data, textStatus, jqXHR) {
									var resCode = data.resCode;
									var resMsg = data.resMsg;
									var attachment = data.attachment;
									if (resCode == '000000') {
										/** 刷新树结构 * */
										$.messager.alert('提示信息', '操作成功');
										$.csOutsourcingAreaIdNum.closeAreaIdNumConfigWin();
										$.csOutsourcingAreaIdNum.reloadIdNumDataGridData();
									} else {
										/** 操作失败* */
										$.messager.alert('提示信息', resMsg, 'error');
									}
								},
								error : function(XMLHttpRequest, textStatus, errorThrown, d) {
									$.messager.alert('提示信息', textStatus + '  :  ' + errorThrown + '!', 'error');
								},
								complete : function() {

								}
							});
						}
					});
				}
			})
			$('#areaIdNumCloseWinBut').click(function() {
				$.csOutsourcingAreaIdNum.closeAreaIdNumConfigWin();
			})
		},
		showAreaIdNumConfigWin : function() {
			$.csOutsourcingAreaIdNum.areaConfigIdNumForm.form('clear');
			$.csOutsourcingAreaIdNum.areaConfigIdNumWin.window('open');
		},
		closeAreaIdNumConfigWin : function() {
			$.csOutsourcingAreaIdNum.areaConfigIdNumWin.window('close');
		},
	})

	$.csOutsourcingAreaIdNum.init();

})