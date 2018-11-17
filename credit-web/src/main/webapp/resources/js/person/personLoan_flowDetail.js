$(function(){
	$.flowDetail = {
		dataGrid : $('#flowDetailDataGrid'),
		/** 还款计划数据源地址 **/
		queryDataUrl : global.contextPath + '/loanInfo/loadFlowDetailData' + '/' + loanId,
		
		/** 每页显示的记录条数，默认为10 **/
		pageSize : 1000,
		/** 设置每页记录条数的列表 **/
		pageSizeList : [1000],
		/** 分页对象 **/
		pager : undefined,
		
		reloadDataGrid : function() {
			$.flowDetail.dataGrid.datagrid('reloadData',{
				url : $.flowDetail.queryDataUrl
			});
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.flowDetail.pg = {
		'page' : 1,
		'rows' : $.flowDetail.pageSize
	}
	
	$.flowDetail.dataGrid.datagrid({
		pg : $.flowDetail.pg,
		//是否显示行号
		rownumbers : true,
		//是否单选
		singleSelect : true,
		////是否可折叠的
		collapsible : false,
		//自适应列宽
		fitColumns : true,
		fit : true,
		//height : '100%',
		//是否开启分页
		pagination : true,
		//锁定列定义
		frozenColumns : [[
		      
		]],
		columns : [[
			//列定义
			{field : 'tradeDate',title : '交易日期',width : 25,formatter:$.DateUtil.dateFormatToStr},
			{field : 'tradeType',title : '交易方式',width : 25},
			{field : 'tradeCode',title : '交易类型',width : 25},
			{field : 'qichuBalance',title : '期初余额（元）',width : 25,vType : 'rmb'},
			{field : 'income',title : '收入（元）',width : 25,vType : 'rmb'},
			{field : 'outlay',title : '支出（元）',width : 25,vType : 'rmb'},
			{field : 'qimoBalance',title : '期末余额（元）',width : 25,vType : 'rmb'},
			{field : 'memo',title : '备注',width : 100},
			{field : 'tradeNo',title : '',width : 20,formatter : function(value,row,rowIndex){
				return '<a href="javascript:void(0)" onClick="$.singleFlowDetail.showWin(' + rowIndex + ');">查看明细</a>';
			}}
		]],
		//每页显示的记录条数，默认为10
		pageSize : $.flowDetail.pageSize,
		//可以设置每页记录条数的列表
		pageList : $.flowDetail.pageSizeList,
		//自定义行样式
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	
		$.flowDetail.pager = $.flowDetail.dataGrid.datagrid('getPager');
		$.flowDetail.pager.pagination({
			onSelectPage : function(pageNumber,pageSize) {
				$.flowDetail.pg.page = pageNumber;
				$.flowDetail.pg.rows = pageSize;
				$.flowDetail.reloadDataGrid();
			}
		});
	
	
	//默认进页面加载数据
	$.flowDetail.reloadDataGrid();
	
	
	$.singleFlowDetail = {
			dataGrid : $('#singleFlowDetailDataGrid'),
			showSingleFlowDetailWin : $('#showSingleFlowDetailWin'),
			/** 查询单条卡账详细信息 数据源地址 **/
			queryDataUrl : global.contextPath + '/loanInfo/loadSingleFlowDetailData',
			
			/** 每页显示的记录条数，默认为10 **/
			pageSize : 1000,
			/** 设置每页记录条数的列表 **/
			pageSizeList : [100],
			/** 分页对象 **/
			pager : undefined,
			
			reloadDataGrid : function() {
				$.singleFlowDetail.dataGrid.datagrid('reloadData',{
					url : $.singleFlowDetail.queryDataUrl
				});
			},
			showWin : function(rowIndex) {
				var row = $.flowDetail.dataGrid.datagrid('getRows')[rowIndex];
				if (row) {
					var tradeNo = row.tradeNo;
					if (tradeNo) {
						$.singleFlowDetail.showSingleFlowDetailWin.window('open');
						$.singleFlowDetail.dataGrid.datagrid('reloadData',{
							url : $.singleFlowDetail.queryDataUrl + '/' + tradeNo
						});
					}
					
				}
				
				
			}
		}
		
		/** 分页参数（page:当前第N页，rows:一页N行） **/
		$.singleFlowDetail.pg = {
			'page' : 1,
			'rows' : $.singleFlowDetail.pageSize
		}
		
		$.singleFlowDetail.dataGrid.datagrid({
			pg : $.singleFlowDetail.pg,
			//是否显示行号
			rownumbers : true,
			//是否单选
			singleSelect : true,
			////是否可折叠的
			collapsible : false,
			//自适应列宽
			fitColumns : true,
			fit : true,
			//height : '100%',
			//是否开启分页
			pagination : false,
			//锁定列定义
			frozenColumns : [[
			      
			]],
			columns : [[
				//列定义
				{field : 'tradeDate',title : '明细生成日期',width : 20,formatter:$.DateUtil.dateFormatToStr},
				{field : 'accountTitleName',title : '明细项目',width : 25},
				{field : 'itemCount',title : '期数',width : 25},
				{field : 'amount',title : '金额',width : 25,vType : 'rmb'},
			]],
			//每页显示的记录条数，默认为10
			pageSize : $.singleFlowDetail.pageSize,
			//可以设置每页记录条数的列表
			pageList : $.singleFlowDetail.pageSizeList,
			//自定义行样式
			rowStyler : function(index,row) {
				if (index % 2 == 0) {
					//return 'background-color:#AABBCC;color:#fff;';
				}
			}
		});
		
		/*$.parser.onComplete = function(){
			$.singleFlowDetail.pager = $.singleFlowDetail.dataGrid.datagrid('getPager');
			$.singleFlowDetail.pager.pagination({
				onSelectPage : function(pageNumber,pageSize) {
					$.singleFlowDetail.pg.page = pageNumber;
					$.singleFlowDetail.pg.rows = pageSize;
					$.singleFlowDetail.reloadDataGrid();
				}
			});
		}*/
	
	
})