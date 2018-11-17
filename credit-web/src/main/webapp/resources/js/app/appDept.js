$(function() {
	$.appOrgManager = $.extend($.appOrgManager || {}, {
		/** 管理营业部列表 数据源地址 * */
		deptDataGridUrl : global.contextPath + '/appManager/appOrganizationManagerPage',
		/** 新增/更新管理营业部信息 * */
		saveOrUpdateAreaDeptUrl : global.contextPath + '/appManager/saveAppOrganizationManager',
		/** 删除管理营业部 地址 * */
		/** 管理营业部列表 * */
		appOrgDataGrid : $('#appOrgDataGrid'),
		appOrgSearchForm : $('#appOrgSearchForm'),
		appOrgConfigForm : $('#appOrgConfigForm'),
		appOrgConfigWin : $('#appOrgConfigWin'),
		deptDataGridPg : {
			page : 1,
			rows : 10
		},
		isValidData : {
			'0' : '开启旧',
			'1' : '关闭',
			'2' : '开启新'
		},
		/** 刷新管理营业部列表数据 * */
		appOrgDataGridData : function() {
				// 获取查询表单数据转换成JSON对象
				var searchMsg = $.appOrgManager.appOrgSearchForm.serialize();
				/** 对参数进行解码(显示中文) * */
				searchMsg = decodeURIComponent(searchMsg);
	
				var queryParam = $.serializeToJsonObject(searchMsg);
				queryParam.url = $.appOrgManager.deptDataGridUrl;
				$.appOrgManager.appOrgDataGrid.datagrid('reloadData', queryParam);
		},
		/** 页面初始化操作 * */
		init : function() {

			$.appOrgManager.appOrgConfigWin.window({
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

			$.appOrgManager.appOrgDataGrid.datagrid({
				pg : $.appOrgManager.deptDataGridPg,
				/** 提交方式 * */
				method : 'get',
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
				// height : '100%',
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
					field : 'name',
					title : '管理营业部',
					width : 1
				}, {
					field : 'updateTimeStr',
					title : '更新时间',
					width : 1
				}, {
					field : 'orgId',
					title : 'orgId',
					hidden:  true ,
					width : 1
				}, {
					field : 'state',
					title : '是否开启',
					formatter : function(value, row, index) {
						return $.appOrgManager.isValidData[value + ''] || '未知';
					},
					width : 1
				}] ],
				/** 每页显示的记录条数，默认为10 * */
				pageSize : $.appOrgManager.deptDataGridPg.rows,
				/** 可以设置每页记录条数的列表 * */
				pageList : [ 2, 10, 20, 30, 40, 50 ],
				toolbar : '#deptDataGirdTB',
				/** 自定义行样式 * */
				rowStyler : function(index, row) {
					if (index % 2 == 0) {
						// return 'background-color:#AABBCC;color:#fff;';
					}
				}
			});
			$.appOrgManager.appOrgDataGrid.datagrid('getPager').pagination({
				onSelectPage : function(pageNumber, pageSize) {
					$.appOrgManager.deptDataGridPg.page = pageNumber;
					$.appOrgManager.deptDataGridPg.rows = pageSize;
					/** 刷新管理营业部列表数据 * */
					$.appOrgManager.appOrgDataGridData();
				}
			});

			$.appOrgManager.appOrgDataGrid.datagrid('resize');

			$('#orgAdd').click(function() {
				$.appOrgManager.showAppDeptConfigWin();
				var formData = {};
				formData.saveOrUpdate = '1';
				$.appOrgManager.appOrgConfigForm.form('load', formData);
			})
			$('#orgModify').click(function() {
				//var row = $.dataGrid.getSelectedRow($.csOutsourcingAreaIdNum.idNumDataGrid);
				var row =$.dataGrid.getSelectedRow($.appOrgManager.appOrgDataGrid);
				if(row){
					var id = row.id;
					if(id){
						$.appOrgManager.appOrgConfigForm.find('input[name=id]').val(id);
						$.appOrgManager.appOrgConfigForm.find('input[name=saveOrUpdate]').val('2');
						//$('#state').
						$('#orgIdConfig').combobox('select',row.orgId);
						$('#orgStateConfig').combobox('select',row.state);
						
						$.appOrgManager.appOrgConfigWin.window('open');
					}
				}
			})
			/** 新增/更新身份证信息 * */
			$('#submitAreaDeptReq').click(function() {
				if ($.appOrgManager.appOrgConfigForm.form('validate')) {
					$.messager.confirm('请确认', '确认提交?', function(r) {
						if (r) {
							var formData = $.appOrgManager.appOrgConfigForm.serialize();
							$.ajaxPackage({
								type : 'post',
								url : $.appOrgManager.saveOrUpdateAreaDeptUrl,
								data : formData,
								dataType : "json",
								success : function(data, textStatus, jqXHR) {
									var resCode = data.resCode;
									var resMsg = data.resMsg;
									var attachment = data.attachment;
									if (resCode == '000000') {
										/** 刷新树结构 * */
										$.messager.alert('提示信息', '操作成功');
										$.appOrgManager.closeAppDeptConfigWin();
										$.appOrgManager.appOrgDataGridData();
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
			$('#areaDeptCloseWinBut').click(function() {
				$.appOrgManager.closeAppDeptConfigWin();
			});
			$('#orgSearchBut').click(function(){
				// 查询按钮添加事件
					if ($.appOrgManager.appOrgSearchForm.form('validate')) {
						$.appOrgManager.deptDataGridPg.page = 1;
						$.appOrgManager.appOrgDataGridData();
					}
			});
			 $("#clearConditionOrg").bind("click",function(envent){
			    	$("#appOrgSearchForm").form("reset");
		     });
		},
		showAppDeptConfigWin : function() {
			$.appOrgManager.appOrgConfigForm.form('clear');
			$('#orgId').combobox('defaultOneItem');
			$.appOrgManager.appOrgConfigWin.window('open');
		},
		closeAppDeptConfigWin : function() {
			$.appOrgManager.appOrgConfigWin.window('close');
		}
	})

	$.appOrgManager.init();

})