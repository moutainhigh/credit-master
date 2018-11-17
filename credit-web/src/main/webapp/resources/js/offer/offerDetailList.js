$(function() {
	
	$.offerlist = {
			/** 表格数据源地址 **/
			dataGridUrl : global.contextPath + '/offer/offerInfo/searchOfferTransaction',
			/** 客户信息表格 **/
			dataGrid : $('#offerDetailDataGrid'),
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
				/** 清空表格数据 **/
				//$.offerlist.dataGrid.datagrid('loadData',{total:0,rows:[]});
				/** 获取查询表单数据转换成JSON对象 **/
				var searchMsg = $.offerlist.searchForm.serialize();
				/** 对参数进行解码(显示中文) **/
				searchMsg = decodeURIComponent(searchMsg);
				var queryParam = {}; 
				var offerId=$('#offerId').val();
				var isThird=$('#isThird').val();
				var fundsSources=$('#fundsSources').val();
				queryParam.url = $.offerlist.dataGridUrl;
				queryParam.offerId=offerId;
				queryParam.isThird=isThird;
				queryParam.fundsSources=fundsSources;
				$.offerlist.dataGrid.datagrid('reloadData',queryParam);
			}
		};
	
	
		/** 分页参数（page:当前第N页，rows:一页N行） **/
		$.offerlist.pg = {
			'page' : 1,
			'rows' : $.offerlist.pageSize
		}
		
	
	
		$.offerlist.dataGrid.datagrid({
			pg : $.offerlist.pg,
			/** 提交方式 **/
			method : 'get',
			/** 是否显示行号 **/
			rownumbers : true,
			/** 是否单选 **/
			singleSelect : true,
			/** 是否可折叠的 **/
			collapsible : false,
			/** 自适应列宽 **/
			fitColumns : false,
			fit : true,
			//height : '100%',
			/** 是否开启分页 **/
			pagination : true,
			loadMsg : '数据加载中,请稍等...',
			columns : [[
			      /** 列定义 **/
			      {
			field : 'id',
			title : 'ID',
			width : "10%",
			hidden : true
		}, {
			field : 'loanId',
			title : 'loanId',
			width : "10%",
			hidden : true
		}, {
			field : 'trxSerialNo',
			title : '交易流水号',
			width : "10%"
		}, {
			field : 'trxState',
			title : '交易状态',
			width : "10%"
		}, {
			field : 'payAmount',
			title : '报盘金额',
			width : "7%",
			vType : 'rmb'
		}, {
			field : 'actualAmount',
			title : '回盘金额',
			width : "10%",
			vType : 'rmb'/*,
			formatter:function(value,row,index){
                if(row.trxState=="扣款成功"){
                    return $.comdify(row.payAmount);
                }else{
                	return 0.00;
                }
            }*/
		}, {
			field : 'rtnCode',
			title : '返回码',
			width : "10%"
		}, {
			field : 'rtnInfo',
			title : '返回信息',
			width : "10%"
		}, {
			field : 'memo',
			title : '备注',
			width : "10%"
		}
			]],
			/** 每页显示的记录条数，默认为10 **/
			pageSize : $.offerlist.pageSize,
			/** 可以设置每页记录条数的列表 **/
			pageList : $.offerlist.pageSizeList,
			toolbar : '#tb',
			/** 自定义行样式 **/
			rowStyler : function(index,row) {
				if (index % 2 == 0) {
					//return 'background-color:#AABBCC;color:#fff;';
				}
			}
		});
		
		$.offerlist.pager = $.offerlist.dataGrid.datagrid('getPager');
		
		$.offerlist.pager.pagination({
			onSelectPage : function(pageNumber,pageSize) {
				$.offerlist.pg.page = pageNumber;
				$.offerlist.pg.rows = pageSize;
				$.offerlist.reloadDataGrid();
			}
		});
		
		$.offerlist.reloadDataGrid();	
		
})
