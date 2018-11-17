$(function() {
	$.contact  = {
		/** 联系人表格数据源地址 **/
		contactDataGridUrl : global.contextPath + '/personContact/search',
		/** 查询单个联系人信息地址 **/
		loadContactDataUrl : global.contextPath + '/personContact/loadContactData',
		/** 新增、修改联系人信息地址 **/
		saveOrUpdateUrl : global.contextPath + '/personContact/saveOrUpdateData',
		/** 客户记录(前台选中的行对象) **/
		personRow : undefined,
		
		/** 显示联系人窗口 **/
		showContactWin : $('#showContactWin'),
		/** 修改联系人窗口 **/
		editContactWin : $('#editContactWin'),
		/** 修改联系人表单 **/
		editCoontactForm : $('#editCoontactForm'),
		/** 联系人表格 **/
		contactDataGrid : $('#contactDataGrid'),
		/** 分页控件 **/
		pager : undefined,
		/** 每页显示的记录条数，默认为10 **/
		pageSize : 10,
		/** 设置每页记录条数的列表 **/
		pageSizeList : [10,20,30,40,50],
		
		/** 加载联系人数据 **/
		reloadContactDataGrid : function() {
			/** 清空表格数据 **/
			var personId = undefined;
			if ($.contact.personRow) {
				personId = $.contact.personRow.id;
			}
			if (!$.isEmpty(personId)) {
				$.contact.contactDataGrid.datagrid('reloadData',{
					url : $.contact.contactDataGridUrl + '/' + personId
				});
			} else {
				$.messager.alert('异常信息','缺少客户编号参数!','error');
			}
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.contact.pg = {
		'page' : 1,
		'rows' : $.contact.pageSize
	}
	
	$.contact.contactDataGrid.datagrid({
		pg : $.contact.pg,
		/** 是否显示行号 **/
		rownumbers : true,
		/** 是否单选 **/
		singleSelect : true,
		/** 是否可折叠的 **/
		collapsible : false,
		/** 自适应列宽 **/
		fitColumns : true,
		fit : true,
		/** 是否开启分页 **/
		pagination : true,
		loadMsg : '数据加载中,请稍等...',
		toolbar : '#contactTB',
		columns : [[
		      /** 列定义 **/
		      {field : 'name',title : '姓名',width : 5},
		      {field : 'idCard',title : '身份证号',width : 10},
		      {field : 'relation',title : '关系',width : 5},
		      {field : 'mphone',title : '手机号',width : 10,formatter : function(value,row,rowIndex){
		    	  return value;
		      }},
		      {field : 'tel',title : '家庭电话',width : 10},
		      {field : 'ctel',title : '公司电话',width : 10},
		      {field : 'duty',title : '职务',width : 10}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.contact.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.contact.pageSizeList,
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	$.contact.pager = $.contact.contactDataGrid.datagrid('getPager');
	$.contact.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.contact.pg.page = pageNumber;
			$.contact.pg.rows = pageSize;
			$.contact.reloadContactDataGrid();
		}
	});
	
	/** 我的联系人窗口面板参数定义 **/
	$.contact.showContactWin.window({
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
	
	/** 修改联系人窗口面板参数定义 **/
	$.contact.editContactWin.window({
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
	
	
	/** 新增 绑定事件 **/
	$('#contact_addDataBut').click(function(){
		if ($.isEmpty($.contact.personRow) || $.isEmpty($.contact.personRow.id)) {
			$.messager.alert('异常信息','缺少客户信息!','error');
		} else {
			$.contact.editContactWin.window('open');
			$.contact.editCoontactForm.form('clear');
			var formData = {};
			formData.personId = $.contact.personRow.id;
			formData.contactType = '';
			$.contact.editCoontactForm.form('load',formData);
			
			$('#contactRelation').combobox('defaultOneItem');
			$('#contactType').combobox('defaultOneItem');
			
		}
	})
	
	/** 查看/修改 绑定事件 **/
	$('#contact_loadDataBut').click(function(){
		var row = $.dataGrid.getSelectedRow($.contact.contactDataGrid);
		if (row) {
			$.ajaxPackage({
				type : 'get', 
				url : $.contact.loadContactDataUrl + '/' + row.id,
				dataType : "json",
				success : function (data,textStatus,jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					//从服务器上获取到记录信息
					var attachment = data.attachment;
					if (resCode == '000000') {
						/** 显示修改联系人面板 **/
						$.contact.editContactWin.window('open');
						$.contact.editCoontactForm.form('clear');
						/** 填充到表单项里 **/
						$.contact.editCoontactForm.form('load',attachment);
					} else {
						/** 操作失败 **/
						$.messager.alert('异常信息',resMsg,'error');
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		} else {
			$.messager.alert('警告','请选中需要维护的客户!','warning');
		}
	})
	
	/** 变更电话、地址信息 绑定事件 **/
	$('#contact_editTelAddressBut').click(function(){
		var row = $.dataGrid.getSelectedRow($.contact.contactDataGrid);
		if (row) {
			$.telAddress.objectId = row.id;
			/** 操作客户的电话及地址 **/
			$.telAddress.className = 'zdsys.Contact';
			$.telAddress.editTelAddressWin.window('open');
			/** 查询联系电话数据 **/
			$.telAddress.reloadTelDataGrid();
			/** 查询联系地址数据 **/
			$.telAddress.reloadAddressDataGrid();
		} else {
			$.messager.alert('警告','请选中需要维护的客户!','warning');
		}
	})
	
	
	/** 提交新增、修改联系人请求 **/
	$('#contact_submitBut').click(function(){
		if($.contact.editCoontactForm.form('validate')) {
			/** 手机、家庭电话、公司电话至少填写一项判断 **/
			var formData = $.contact.editCoontactForm.serialize();
			var formDataJson = $.serializeToJsonObject(formData);
			if ($.isEmpty(formDataJson.mphone) && $.isEmpty(formDataJson.tel)
					&& $.isEmpty(formDataJson.ctel)) {
				$.messager.alert('警告','手机、家庭电话、公司电话至少填写一个!','warning');
				return;
			}
			/** 提交请求到服务器 **/
			$.ajaxPackage({
				type : 'post', 
				url : $.contact.saveOrUpdateUrl,
				data : formData,
				dataType : "json",
				success : function (data) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') {
						/** 刷数据表格 **/
						$.contact.reloadContactDataGrid($.contact.personRow.id);
						/** 新增、修改成功 **/
						window.setTimeout(function(){
							$.messager.alert('结果','操作成功');
						}, 100);
					} else {
						/** 新增、修改失败 **/
						$.messager.alert('结果',resMsg,'error');
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					//关闭窗口
					$.contact.editContactWin.window('close');
				}
			});
		}
	})
	
	/** 关闭窗口 **/
	$('#contact_closeBut').click(function(){
		$.contact.editContactWin.window('close');
	})
	
	
	
})