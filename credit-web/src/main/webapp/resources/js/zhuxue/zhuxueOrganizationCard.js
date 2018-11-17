$(function() {
	$.zhuxueOrganizationCard = {
		/** 表格数据源地址 * */
		dataGridUrl : global.contextPath + '/zhuxue/searchZhuxueOrganizationAccountInfo',
		
		/** 客户信息表格 * */
		dataGrid : $('#dataGrid'),
		
		/** 分页控件 * */
		pager : undefined,
		/** 查询条件数据项表单实例 * */
		searchForm : $('#searchForm'),
		/** 每页显示的记录条数，默认为10 * */
		pageSize : 10000,
		/** 设置每页记录条数的列表 * */
		pageSizeList : [ 10000 ],
		/** 加载表格数据 * */
		reloadDataGrid : function() {
			if ($.zhuxueOrganizationCard.searchForm.form('validate')) {
				/** 获取查询表单数据转换成JSON对象 * */
				var searchMsg = $.zhuxueOrganizationCard.searchForm.serialize();
				/** 对参数进行解码(显示中文) * */
				searchMsg = decodeURIComponent(searchMsg);
				var queryParam = $.serializeToJsonObject(searchMsg);
				/** 未输入查询条件不给予查询 * */
				if ($.isEmpty(queryParam.zhuxueOrganizationId)) {
					$.messager.alert('提示信息', '无法获取组织机构ID!', 'warning');
				} else {
					queryParam.url = $.zhuxueOrganizationCard.dataGridUrl;
					$.zhuxueOrganizationCard.dataGrid.datagrid('reloadData', queryParam);
				}
			}
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.zhuxueOrganizationCard.pg = {
		'page' : 1,
		'rows' : $.zhuxueOrganizationCard.pageSize
	}
	
	/**查询**/
	$('#searchBut').click(function() {
		$.zhuxueOrganizationCard.pg.page = 1;
		$.zhuxueOrganizationCard.reloadDataGrid();
	});
	
	$.zhuxueOrganizationCard.dataGrid.datagrid({
		pg : $.zhuxueOrganizationCard.pg,
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
		/** 锁定列定义 **/
//		frozenColumns : [[
//		      {field : 'id',title : '编号',width : 50}
//		]],
		columns : [[
		      /** 列定义 **/
		      {field : 'name',title : '姓名',width : 2},
		      {field : 'tradeDate',title : '交易日期',width : 2,formatter : $.DateUtil.dateFormatToStr},
		      {field : 'tradeType',title : '交易方式',width : 2},
		      {field : 'tradeCodeStr',title : '交易类型',width : 2},
		      {field : 'income',title : '收入',width : 2},
		      {field : 'payment',title : '支出',width : 2}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.zhuxueOrganizationCard.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.zhuxueOrganizationCard.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	$.zhuxueOrganizationCard.pager = $.zhuxueOrganizationCard.dataGrid.datagrid('getPager');
	$.zhuxueOrganizationCard.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.zhuxueOrganizationCard.pg.page = pageNumber;
			$.zhuxueOrganizationCard.pg.rows = pageSize;
			$.zhuxueOrganizationCard.reloadDataGrid();
		}
	});
	
	$.zhuxueOrganizationCard.dataGrid.datagrid('resize');
})