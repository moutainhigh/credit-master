$(function(){
	$.flowDetail = {
		dataGrid : $('#flowDetailDataGrid'),
		/** 还款计划数据源地址 **/
		queryDataUrl : global.contextPath + '/repay/repayMark/loadFlowDetailData' + '/' + loanId,
		
		repayForm : $('#repayForm'),
		
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
			{field : 'memo',title : '备注',width : 40},
			{field : 'financeMemo',title : '财务备注',width : 40},
			{field : 'tradeNo',title : '',width : 30,formatter : function(value,row,rowIndex){
				return '<a href="javascript:void(0)" onClick="$.financeMemoWinDetail.showWin(' + rowIndex + ');">财务备注</a> &nbsp;|&nbsp;<a href="javascript:void(0)" onClick="$.singleFlowDetail.showWin(' + rowIndex + ');">查看明细</a>';
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
		},
		onLoadSuccess : function(){
			/** 筛选出备注栏所有的单元格 **/
			var fileds = $('td[field=financeMemo]');
			$.each(fileds,function(p1,p2){
				var divContent = $(p2).find('div:first');
				divContent.css('word-wrap','break-word');
				divContent.css('white-space','normal');
			})
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
			queryDataUrl : global.contextPath + '/repay/repayMark/loadSingleFlowDetailData',
			
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
		
	
	$.financeMemoWinDetail = {
			financeMemoWin : $('#financeMemoWin'),
			repayForm : $('#repayForm'),
			saveDataUrl : global.contextPath + '/repay/repayMark/saveOrUpdateMemo',
			loanRow : undefined,
			showWin : function(rowIndex) {
				var row = $.flowDetail.dataGrid.datagrid('getRows')[rowIndex];
				if (row) {
					var id = row.id;
					if (id) {
						var financeMemo=row.financeMemo;
						if(financeMemo){
							   $("#memo").textbox('setValue',financeMemo);
						}
						$.financeMemoWinDetail.loanRow = row;
						$.financeMemoWinDetail.financeMemoWin.window('open');
					}
				}
			},
			init : function() {
				/** 关闭窗口事件 **/
				$('#repayCloseBut').click(function(){
					$.financeMemoWinDetail.financeMemoWin.window('close');
					$.financeMemoWinDetail.repayForm.form('clear');
				})
				
				/** 发送还款申请事件 **/
				$('#repaySubmitBut').click(function(){
					if ($.financeMemoWinDetail.repayForm.form('validate')) {
						
						var memo = $.financeMemoWinDetail.repayForm.find('input[name=memo]').val();
						if (memo.length > 50) {
							$.messager.alert('警告','备注长度在50字内','warning');
							return;
						}
						
						$.financeMemoWinDetail.repayForm.find('input[name=id]').val($.financeMemoWinDetail.loanRow.id);
						
						$.ajaxPackage({
							type : 'post', 
							url : $.financeMemoWinDetail.saveDataUrl,
							dataType : 'json',
							data : $.financeMemoWinDetail.repayForm.serialize(),
							success : function (data,textStatus,jqXHR) {
								var resCode = data.resCode;
								var resMsg = data.resMsg;
								if (resCode == '000000') {
									$.flowDetail.reloadDataGrid();
									$.messager.alert('还款备注','保存成功!');
								} else {
									$.flowDetail.reloadDataGrid();
									$.messager.alert('警告',resMsg,'error');
								}
							},
							error : function (XMLHttpRequest, textStatus, errorThrown,d) {
								$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!','error');
							},
							complete : function() {
								$.financeMemoWinDetail.financeMemoWin.window('close');
								$.financeMemoWinDetail.repayForm.form('clear');
							}
						});
					}
				})
			}
	}
	
	$.financeMemoWinDetail.financeMemoWin.window({
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
	$.financeMemoWinDetail.init();
})