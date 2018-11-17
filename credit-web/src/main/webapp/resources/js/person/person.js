$(function() {
	
	$.person  = {
			/** 表格数据源地址 **/
			dataGridUrl : global.contextPath + '/person/search',
			/** 修改客户详细信息地址（iframe） **/
			loadPersonDataUrl : global.contextPath + '/person/loadPersonData',
			/** 查看客户详细信息地址 **/
			viewPersonDetailPageUrl : global.contextPath + '/person/viewPersonDetailPage',
			
			/** 客户信息表格 **/
			customerDataGrid : $('#customerDataGrid'),
			/** 分页控件 **/
			pager : undefined,
			/** 查询条件数据项表单实例 **/
			searchForm : $('#searchForm'),
			/** 每页显示的记录条数，默认为10 **/
			pageSize : 10,
			/** 设置每页记录条数的列表 **/
			pageSizeList : [10,20,30,40,50],
			
			/** 加载表格数据 **/
			reloadDataGrid : function() {
				if ($.person.searchForm.form('validate')) {
					/** 获取查询表单数据转换成JSON对象 **/
					var searchMsg = $.person.searchForm.serialize();
					searchMsg = searchMsg.replace(/\+/g,' ');
					/** 对参数进行解码(显示中文) **/
					searchMsg = decodeURIComponent(searchMsg);
					var queryParam = $.serializeToJsonObject(searchMsg);  
					/** 未输入查询条件不给予查询 **/
					if ($.isEmpty(queryParam.name) && $.isEmpty(queryParam.mphone) && $.isEmpty(queryParam.idnum)
							&& $.isEmpty(queryParam.realEstateLicenseCode) && $.isEmpty(queryParam.contactName) && $.isEmpty(queryParam.contactMPhone)) {
						$.messager.alert('提示信息','至少输入一个查询条件!','warning');
					} else {
						queryParam.url = $.person.dataGridUrl;
						$.person.customerDataGrid.datagrid('reloadData',queryParam);
					}
				}
			}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.person.pg = {
		'page' : 1,
		'rows' : $.person.pageSize
	}
	
	$('#searchBut').click(function() {
		$.person.pg.page = 1;
		$.person.reloadDataGrid();
	})
	
	$('#clearBut').click(function() {
		$.person.searchForm.form('reset');
	})
	
	/** 查看客户详细资料 **/
	$('#showDetailBut').click(function() {
		/** 判断是否选中客户 **/
		var row = $.dataGrid.getSelectedRow($.person.customerDataGrid);
		if (row) {
			/** 客户编号 **/
			var id = row.id;
			/** 客户姓名 **/
			var name = row.name;
			var tab = {};
			tab.id = 'personDetail_' + id;
			tab.iconCls = 'pic_45';
			tab.text = name + ' - 详细资料';
			tab.url = $.person.viewPersonDetailPageUrl + '/' + id;
			/** 调用父级添加选项卡方法 **/
			parent.$.iframeTabs.add(tab);
		} else {
			$.messager.alert('警告','请选中需要查看的客户!','warning');
		}
	})
	
	/** 修改客户详细资料 **/
	$('#editDetailBut').click(function() {
		/** 判断是否选中客户 **/
		var row = $.dataGrid.getSelectedRow($.person.customerDataGrid);
		if (row) {
			/** 客户编号 **/
			var id = row.id;
			/** 客户姓名 **/
			var name = row.name;
			var tab = {};
			tab.text = name + ' - 修改详细资料';
			tab.iconCls = 'pic_45';
			tab.url = $.person.loadPersonDataUrl + '/' + id + '?isEdit=1';
			/** 调用父级添加选项卡方法 **/
			parent.$.iframeTabs.add(tab);
		} else {
			$.messager.alert('警告','请选中需要维护的客户!','warning');
		}
	})
	
	/** 维护联系人信息 **/
	$('#showContactBut').click(function() {
		/** 判断是否选中客户 **/
		var row = $.dataGrid.getSelectedRow($.person.customerDataGrid);
		if (row) {
			/** 选中客户记录 **/
			$.contact.personRow = row;
			$.contact.showContactWin.window('open');
			$.contact.pg.page = 1;
			$.contact.pager.pagination('refresh',{
                pageNumber:$.contact.pg.page,
                pageSize:$.contact.pg.pageSize
            });
			$.contact.reloadContactDataGrid($.contact.personRow.id);
		} else {
			$.messager.alert('警告','请选中需要维护的客户!','warning');
		}
	})
	
	
	
	$.person.customerDataGrid.datagrid({
		//url : $.person.dataGridUrl,
		pg : $.person.pg,
		/** 提交方式 **/
		method : 'get',
		/** 是否显示行号 **/
		rownumbers : true,
		/** 是否单选 **/
		singleSelect : true,
		/** 是否可折叠的 **/
		collapsible : false,
		/** 自适应列宽 **/
		fitColumns : true,
		fit : true,
		//height : '100%',
		/** 是否开启分页 **/
		pagination : true,
		loadMsg : '数据加载中,请稍等...',
		/** 锁定列定义 **/
//		frozenColumns : [[
//		      {field : 'id',title : '编号',width : 50}
//		]],
		columns : [[
		      /** 列定义 **/
		      {field : 'name',title : '姓名',width : 5},
		      {field : 'sex',title : '性别',width : 5},
		      {field : 'comOrganization',title : '管理营业部',width : 20,formatter : function(value,row,rowIndex){
		    	  if (value) {
		    		  return value.name;
		    	  }
		      }},
		      {field : 'idnum',title : '身份证号',width : 10},
		      {field : 'mphone',title : '手机号',width : 10},
		      {field : 'tel',title : '现住宅电话',width : 10},
		      {field : 'realEstateLicenseCode',title : '房产证号',width : 40}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.person.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.person.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	$.person.pager = $.person.customerDataGrid.datagrid('getPager');
	$.person.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.person.pg.page = pageNumber;
			$.person.pg.rows = pageSize;
			$.person.reloadDataGrid();
		}
	});
	
	$.person.customerDataGrid.datagrid('resize');
	
})