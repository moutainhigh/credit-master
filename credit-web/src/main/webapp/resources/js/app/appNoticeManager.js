$(function() {
	$.appNoticeManager = $.extend($.appNoticeManager || {}, {
		/** 查询公告列表 数据源地址 * */
		dataGridUrl : global.contextPath + '/app/noticeManager/searchNotice',
		/** 新增/更新公告信息 * */
		saveOrUpdateUrl : global.contextPath + '/app/noticeManager/saveOrUpdateData',
		/** 加载公告信息 * */
		loadNoticeInfoUrl : global.contextPath + '/app/noticeManager/loadNoticeInfo',
		dataGrid : $('#dataGrid'),
		noticeWin : $('#noticeWin'),
		noticeForm : $('#noticeForm'),
		dataGridPg : {
			page : 1,
			rows : 10
		},
		/** 刷新管理营业部列表数据 * */
		reloadDataGridData : function() {
			// 获取查询表单数据转换成JSON对象
			// var searchMsg = $.appOrgManager.appOrgSearchForm.serialize();
			/** 对参数进行解码(显示中文) * */
			// searchMsg = decodeURIComponent(searchMsg);
			// var queryParam = $.serializeToJsonObject(searchMsg);
			var queryParam = {};
			queryParam.url = $.appNoticeManager.dataGridUrl;
			$.appNoticeManager.dataGrid.datagrid('reloadData', queryParam);
		},
		/** 页面初始化操作 * */
		init : function() {
			$.appNoticeManager.dataGrid.datagrid({
				pg : $.appNoticeManager.dataGridPg,
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
					field : 'title',
					title : '标题',
					width : 10,
					formatter : function(value, row, index) {
						var content = '<span title="' + value + '" class="note">' + value + '</span>';
						return content;
					}
				}, {
					field : 'content',
					title : '内容',
					width : 20,
					formatter : function(value, row, index) {
						var content = '<span title="' + value + '" class="note">' + value + '</span>';
						return content;
					}
				}, {
					field : 'isValid',
					title : '是否有效',
					width : 3,
					formatter : function(value, row, index) {
						var str = '';
						if (value == '1') {
							str = '有效';
						} else if (value == '0') {
							str = '无效';
						} else {
							str = '未知';
						}
						return str;
					}
				}, {
					field : 'noticeType',
					title : '通知类型',
					width : 3
				}, {
					field : 'inputTime',
					title : '录入时间',
					width : 5
				} ] ],
				/** 每页显示的记录条数，默认为10 * */
				pageSize : $.appNoticeManager.dataGridPg.rows,
				/** 可以设置每页记录条数的列表 * */
				pageList : [ 2, 10, 20, 30, 40, 50 ],
				toolbar : '#tb',
				onLoadSuccess : function(data) {
					$(".note").tooltip({
						onShow : function() {
							$(this).tooltip('tip').css({
								// width:'300',
								border : '1px solid red',
								boxShadow : '1px 1px 3px #292929'
							});
						}
					})
				}
			});
			$.appNoticeManager.dataGrid.datagrid('getPager').pagination({
				onSelectPage : function(pageNumber, pageSize) {
					$.appNoticeManager.dataGridPg.page = pageNumber;
					$.appNoticeManager.dataGridPg.rows = pageSize;
					/** 刷新管理营业部列表数据 * */
					$.appNoticeManager.reloadDataGridData();
				}
			});

			$.appNoticeManager.noticeWin.window({
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

			$.appNoticeManager.dataGrid.datagrid('resize');

			$('#searchBut').click(function() {
				$.appNoticeManager.reloadDataGridData();
			})

			$('#saveBut').click(function() {
				$.appNoticeManager.noticeForm.form('clear')
				var formData = {};
				formData.isValid = '1';
				$.appNoticeManager.noticeForm.form('load', formData);
				$.appNoticeManager.noticeWin.window('open');
			})

			$('#updateBut').click(function() {
				var selectedRow = $.dataGrid.getSelectedRow($.appNoticeManager.dataGrid);
				if (selectedRow) {
					var formData = {};
					formData.id = selectedRow.id;
					$.ajaxPackage({
						type : 'get',
						url : $.appNoticeManager.loadNoticeInfoUrl,
						data : formData,
						dataType : 'json',
						success : function(data, textStatus, jqXHR) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							var attachment = data.attachment;
							if (resCode == '000000') {
								$.appNoticeManager.noticeWin.window('open');
								$.appNoticeManager.noticeForm.form('load', attachment);
							} else {
								$.messager.alert('警告', resMsg, 'error');
							}
						},
						error : function(XMLHttpRequest, textStatus, errorThrown, d) {
							$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
						},
						complete : function() {

						}
					});
				} else {
					$.messager.alert('警告', '请选中公告!', 'warning');
				}

			})

			$('#removeBut').click(function() {

			})

			$('#closeBut').click(function() {
				$.appNoticeManager.noticeWin.window('close');
			})

			$('#submitBut').click(function() {
				if ($.appNoticeManager.noticeForm.form('validate')) {
					$.messager.confirm('确认', '确认提交?', function(r) {
						if (r) {
							$.ajaxPackage({
								type : 'get',
								url : $.appNoticeManager.saveOrUpdateUrl,
								data : $.appNoticeManager.noticeForm.serialize(),
								dataType : 'json',
								success : function(data, textStatus, jqXHR) {
									var resCode = data.resCode;
									var resMsg = data.resMsg;
									var attachment = data.attachment;
									if (resCode == '000000') {
										$.messager.alert('信息', '操作成功');
										$.appNoticeManager.reloadDataGridData();
									} else {
										$.messager.alert('警告', resMsg, 'error');
									}
								},
								error : function(XMLHttpRequest, textStatus, errorThrown, d) {
									$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
								},
								complete : function() {
									$.appNoticeManager.noticeWin.window('close');
								}
							});
						}
					});
				}
			})

		}

	})

	$.appNoticeManager.init();

})