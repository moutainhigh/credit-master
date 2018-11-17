/**查询机构帐卡信息**/
function viewZhuxueOrganizationCard(id) {
	var tab = {};
	tab.id = 'zhuxueOrganizationCard_'+id;
	tab.text = '机构帐卡信息';
	tab.url = $.zhuxueOrganization.viewZhuxueOrganizationCard + '/' + id;
	/** 调用父级添加选项卡方法 **/
	parent.$.iframeTabs.add(tab);
}

$(function() {
	$.zhuxueOrganization = {
		/** 表格数据源地址 * */
		dataGridUrl : global.contextPath + '/zhuxue/searchZhuxueOrganization',
		
		/**查看帐卡信息**/
		viewZhuxueOrganizationCard : global.contextPath + '/zhuxue/zhuxueOrganizationCard',
		
		/** 客户信息表格 * */
		dataGrid : $('#dataGrid'),
		
		/** 分页控件 * */
		pager : undefined,
		/** 查询条件数据项表单实例 * */
		searchForm : $('#searchForm'),
		/** 每页显示的记录条数，默认为10 * */
		pageSize : 10,
		/** 设置每页记录条数的列表 * */
		pageSizeList : [ 10, 20, 30, 40, 50 ],
		/** 加载表格数据 * */
		reloadDataGrid : function() {
			if ($.zhuxueOrganization.searchForm.form('validate')) {
				/** 获取查询表单数据转换成JSON对象 * */
				var searchMsg = $.zhuxueOrganization.searchForm.serialize();
				/** 对参数进行解码(显示中文) * */
				searchMsg = decodeURIComponent(searchMsg);
				var queryParam = $.serializeToJsonObject(searchMsg);
				/** 未输入查询条件不给予查询 * */
				if ($.isEmpty(queryParam.code) && $.isEmpty(queryParam.orgType) && $.isEmpty(queryParam.name)) {
					$.messager.alert('提示信息', '至少输入1个查询条件!', 'warning');
				} else {
					queryParam.url = $.zhuxueOrganization.dataGridUrl;
					$.zhuxueOrganization.dataGrid.datagrid('reloadData', queryParam);
				}
			}
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.zhuxueOrganization.pg = {
		'page' : 1,
		'rows' : $.zhuxueOrganization.pageSize
	}
	
	/**查询**/
	$('#searchBut').click(function() {
		$.zhuxueOrganization.pg.page = 1;
		$.zhuxueOrganization.reloadDataGrid();
	});
	
	$.zhuxueOrganization.dataGrid.datagrid({
		pg : $.zhuxueOrganization.pg,
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
		      {field : 'code',title : '机构内部编号',width : 2},
		      {field : 'orgType',title : '机构类型',width : 2},
		      {field : 'name',title : '机构名称',width : 8},
		      {field : 'owner',title : '法定代表人姓名',width : 4},
		      {
		    	  field : 'dateSigned',
		    	  title : '签约日期',
		    	  width : 2,
		    	  formatter : $.DateUtil.dateFormatToStr
		      },
		      {field : 'marginRate',title : '保证金比例',width : 2},
		      {field : 'guazhangjine',title : '挂账金额',width : 2},
		      {field : 'tradeAmount',title : '开户总金额',width : 2},
		      {
		    	  field : 'AccountInfo',
		    	  title : '账卡信息',
		    	  width : 4,
		    	  formatter : function(value, row, index) {
						var str = "<a href='javascript:void(0)' onclick='viewZhuxueOrganizationCard("+row.id+")' class='easyui-linkbutton' style='margin-right: 20px;'>账卡信息</a>";
						return str;
				  }
		      }
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.zhuxueOrganization.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.zhuxueOrganization.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	$.zhuxueOrganization.pager = $.zhuxueOrganization.dataGrid.datagrid('getPager');
	$.zhuxueOrganization.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.zhuxueOrganization.pg.page = pageNumber;
			$.zhuxueOrganization.pg.rows = pageSize;
			$.zhuxueOrganization.reloadDataGrid();
		}
	});
	
	$.zhuxueOrganization.dataGrid.datagrid('resize');
})