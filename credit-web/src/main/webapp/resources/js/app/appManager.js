$(function() {
	$.appEmployee = $.extend($.appEmployee || {}, {
		/**员工查询列表 * */
		appEmployeeListUrl  : global.contextPath + '/appManager/appEmployeeManagerPage',
		/** 新增/修改员工信息 地址 * */
		saveOrUpdateAppEmployeeUrl : global.contextPath + '/appManager/saveAppEmployeeManager',
		/** 新增/修改员工 * */
		appEmployeeConfigWin : $('#appEmployeeConfigWin'),
		appEmployeeConfigForm : $('#appEmployeeConfigForm'),
		appEmployeeConfigTips : $('#appEmployeeConfigTips'),
		
		appEmployeeSearchForm : $('#appEmployeeSearchForm'),
		employeeDataGrid : $('#employeeDataGrid'),
		
		employeeDataGridPg : {
			"page" : 1,
			"rows" : 10
		},
		isValidData : {
			'0' : '开启旧',
			'1' : '关闭',
			'2' : '开启新'
		},
		/** 刷新身份证号列表数据 * */
		reloademployeeDataGridData : function() {
			// 获取查询表单数据转换成JSON对象
			var searchMsg = $.appEmployee.appEmployeeSearchForm.serialize();
			/** 对参数进行解码(显示中文) * */
			searchMsg = decodeURIComponent(searchMsg);

			var queryParam = $.serializeToJsonObject(searchMsg);
			queryParam.url = $.appEmployee.appEmployeeListUrl;
			$($.appEmployee.employeeDataGrid).datagrid('reloadData', queryParam);
		},
		/** 页面初始化操作 * */
		init : function() {
			this.appEmployeeConfigWin.window({
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
			
			$.appEmployee.employeeDataGrid.datagrid({
				pg : $.appEmployee.employeeDataGridPg,
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
					field : 'userCode',
					title : '员工工号',
					width : 1
				}, {
					field : 'name',
					title : '姓名',
					width : 1
				}, {
					field : 'updateTimeStr',
					title : '更新时间',
					width : 1
				} , {
					field : 'orgName',
					title : '营业部',
					width : 1
				}, {
					field : 'employeeType',
					title : '职务',
					width : 1
				}, {
					field : 'state',
					title : '是否开启',
					formatter : function(value, row, index) {
						return $.appEmployee.isValidData[value + ''] || '未知';
					},
					width : 1
				}] ],
				/** 每页显示的记录条数，默认为10 * */
				pageSize : $.appEmployee.employeeDataGridPg.rows,
				/** 可以设置每页记录条数的列表 * */
				pageList : [ 2, 10, 20, 30, 40, 50 ],
				toolbar : '#employeeTb',
				/** 自定义行样式 * */
				rowStyler : function(index, row) {
					if (index % 2 == 0) {
						// return 'background-color:#AABBCC;color:#fff;';
					}
				}
			});
			// 表格分页实例
			/*$.appEmployee.employeeDataGrid.datagrid('getPager').pagination({
				onSelectPage : function(pageNumber, pageSize) {
					$.appEmployee.employeeDataGridPg.page = pageNumber;
					$.appEmployee.employeeDataGridPg.rows = pageSize;
					
					*//** 刷新身份证号列表数据 * *//*
					$.appEmployee.reloademployeeDataGridData();
				}
			});*/
			
			$.appEmployee.pager = $.appEmployee.employeeDataGrid.datagrid('getPager');
		    $.appEmployee.pager.pagination({
		        onSelectPage : function(pageNumber, pageSize) {
		            $.appEmployee.employeeDataGridPg.page = pageNumber;
		            $.appEmployee.employeeDataGridPg.rows = pageSize;
		            // 查询
		            //search();
		            $.appEmployee.reloademployeeDataGridData();
		        }
		    });

			$.appEmployee.employeeDataGrid.datagrid('resize');
			/**员工新增配置*/
			/** 新增/更新身份证信息 * */
			$('#submitAppEmployeeReq').click(function() {
				if ($.appEmployee.appEmployeeConfigForm.form('validate')) {
					$.messager.confirm('请确认', '确认提交?', function(r) {
						if (r) {
							var formData = $.appEmployee.appEmployeeConfigForm.serialize();
							$.ajaxPackage({
								type : 'post',
								url : $.appEmployee.saveOrUpdateAppEmployeeUrl,
								data : formData,
								dataType : "json",
								success : function(data, textStatus, jqXHR) {
									var resCode = data.resCode;
									var resMsg = data.resMsg;
									var attachment = data.attachment;
									if (resCode == '000000') {
										/** 刷新树结构 * */
										$.messager.alert('提示信息', '操作成功');
										$.appEmployee.closeAppEmployeeConfigWin();
										$.appEmployee.reloademployeeDataGridData();
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
			});
			
			$('#employeeModify').click(function(){
				//var row = $.dataGrid.getSelectedRow($.csOutsourcingAreaIdNum.idNumDataGrid);
				var row =$.dataGrid.getSelectedRow($.appEmployee.employeeDataGrid);
				if(row){
					var id = row.id;
					if(id){
						
						var userCode = row.userCode;
						var state = row.state;
						$.appEmployee.appEmployeeConfigForm.find('input[name=id]').val(id);
						$.appEmployee.appEmployeeConfigForm.find('input[name=saveOrUpdate]').val('2');
						//$('#state').
						$("#userCodeConfig").textbox('setValue',userCode);
						$('#employeeStateConfig').combobox('select',row.state);
						//$('#cc').combobox('select'
						$.appEmployee.appEmployeeConfigWin.window('open');
					}
				}
				
			});
			/** 打开新增窗口 * */
			$('#employeeAdd').click(function() {
				$.appEmployee.showAppEmployeeConfigWin();
				var formData = {};
				formData.saveOrUpdate = "1";
				$.appEmployee.appEmployeeConfigForm.form('load', formData);
			});

			$('#appEmployeeCloseWinBut').click(function() {
				$.appEmployee.closeAppEmployeeConfigWin();
			});
			$('#employeeSearchBut').click(function(){
				// 查询按钮添加事件
					if ($.appEmployee.appEmployeeSearchForm.form('validate')) {
						$.appEmployee.employeeDataGridPg.page = 1;
						$.appEmployee.reloademployeeDataGridData();
					}
			});
			 $("#clearConditionEmployee").bind("click",function(envent){
			    	$("#appEmployeeSearchForm").form("reset");
		     });
		},
		showAppEmployeeConfigWin : function() {
			$.appEmployee.appEmployeeConfigForm.form('clear');
			$.appEmployee.appEmployeeConfigWin.window('open');
		},
		closeAppEmployeeConfigWin : function() {
			$.appEmployee.appEmployeeConfigWin.window('close');
		}
	})
	$.appEmployee.init();

})