$(function() {
    $.loanFeeInfoList = {
        /** 表格数据源地址 * */
        dataGridUrl : global.contextPath + '/fee/LoanFeeInfolist/searchLoanFeeListInfo',
        /** 表格数据源地址 * */
        offerRepayDataGridUrl : global.contextPath + '/fee/loanFeeRepayInfo/search',
        /** 客户信息表格 * */
        dataGrid : $('#dataGrid'),
        /** 服务费流水表格 **/
        offerRepayDataGrid : $('#offerRepayDataGrid'),
        /** 服务费流水窗口 **/
        offerRepayWin : $('#offerRepayWin'),
        /** 分页控件 * */
        pager : undefined,
        /** 查询条件数据项表单实例 * */
        searchForm : $('#searchForm'),
        /** 每页显示的记录条数，默认为10 * */
        pageSize : 10,
        /** 设置每页记录条数的列表 * */
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载表格数据 * */
        reloadDataGrid : function(noConditionTip) {
            if (!$.loanFeeInfoList.searchForm.form('validate')) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 * */
            var searchMsg = $.loanFeeInfoList.searchForm.serialize();
            /** 对参数进行解码(显示中文) * */
            searchMsg = decodeURIComponent(searchMsg);
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 未输入查询条件不给予查询 * */
            /**if ($.isEmpty(queryParam.name) 
                    && $.isEmpty(queryParam.idnum)
                    && $.isEmpty(queryParam.contractNum)
                    && $.isEmpty(queryParam.grantMoneyDateStart)
                    && $.isEmpty(queryParam.grantMoneyDateEnd)
                    && $.isEmpty(queryParam.state)) {
                if(typeof noConditionTip == 'boolean' && noConditionTip){
                    
                }else{
                    $.messager.alert('警告', '请至少输入一个查询条件！', 'warning');
                }
                return;
            } * */
            queryParam.url = $.loanFeeInfoList.dataGridUrl;
            $.loanFeeInfoList.dataGrid.datagrid('reloadData', queryParam);
        },

        init : function() {
            /** 查询处理 **/
            $('#searchBtn').click(function() {
                $.loanFeeInfoList.pg.page = 1;
                $.loanFeeInfoList.reloadDataGrid();
            })

            /** 重置处理 **/
            $('#clearBtn').click(function() {
                $.loanFeeInfoList.searchForm.form('reset');
            })
        }
        }
            



    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.loanFeeInfoList.pg = {
        'page' : 1,
        'rows' : $.loanFeeInfoList.pageSize
    }

    /** dataGrid表格组件初始化 **/
    $.loanFeeInfoList.dataGrid.datagrid({
        pg : $.loanFeeInfoList.pg,
        /** 提交方式 **/
        method : 'get',
        /** 查询超时时间，暂时设定为3分钟 **/
        timeout : 180000,
        /** 是否显示行号 **/
        rownumbers : true,
        /** 是否单选 **/
        singleSelect : true,
        /** 是否可折叠的 **/
        collapsible : false,
        /** 自适应列宽 **/
        fitColumns : true,
        /** 自适应父窗口 **/
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        /** 加载数据提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        /** 锁定列定义 **/
        frozenColumns : [ [
        ] ],
        columns : [ [
        /** 列定义 **/
        {
            field : 'name',
            title : '客户姓名',
            width : 10
        }, {
            field : 'idNum',
            title : '身份证号',
            width : 20
//            formatter:function(value,row,index){
//                if(value){
//                    if(value.length >4){
//                        return "**"+ value.substr(value.length -4);
//                    }
//                    return value;
//                }
//                return "";
//            }
        }, {
            field : 'contractNum',
            title : '合同编号',
            width : 20
        },{
            field : 'fundsSources',
            title : '合同来源',
            width : 20
        },{
            field : 'grantMoneyDate',
            title : '放款日期',
            width : 10,
            formatter : $.DateUtil.dateFormatToStr
        },{
            field : 'salesDepartmentName',
            title : '管理营业部',
            width : 20
        },{
            field : 'loanType',
            title : '借款类型',
            width : 10
        },{
            field : 'amount',
            title : '应收费',
            width : 10,
            vType : 'rmb'
        },{
            field : 'receiveAmount',
            title : '已收费',
            width : 10,
            vType : 'rmb'
        },{
            field : 'unpaidAmount',
            title : '未收费',
            width : 10,
            vType : 'rmb'
        },
        {
            field : 'state',
            title : '收费状态',
            width : 10
        },
        {
			field : 'id',
			title : '操作',
			width : 10,
			formatter : function(value, row, index) {
//				if (value) {
					var elements = "";
					elements += "<a href='javascript:void(0)' class='viewOfferRepay' feeId='" + row.id
							+ "' loanId='" + row.loanId + "' feeId='" + row.feeId + "' ></a>";
					return elements;
//				}
			}
        }
        ] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.loanFeeInfoList.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.loanFeeInfoList.pageSizeList,
        toolbar : '#tb',
        /** 自定义行样式 **/
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
           
            $(".viewOfferRepay").linkbutton({
				text : '收费流水',
				plain : true,
				iconCls : 'icon-search',
				onClick : function() {
					var feeId = $(this).attr('feeId');
					if (!$.isEmpty(feeId)) {
						var queryParam = {};
						queryParam.feeId = feeId;
						queryParam.url = $.loanFeeInfoList.offerRepayDataGridUrl;
						/** 加载表格数据 **/
			            $.loanFeeInfoList.offerRepayDataGrid.datagrid('reloadData', queryParam);
						/** 显示窗口 **/
			            $.loanFeeInfoList.offerRepayWin.window('open');
					}
				}
			})
			 /** 加载数据后自适应表格 **/
            $.loanFeeInfoList.dataGrid.datagrid('resize');
            
        }
    });
    
    /** dataGrid表格组件初始化 **/
    $.loanFeeInfoList.offerRepayDataGrid.datagrid({
//        pg : $.loanFeeInfoList.pg,
        /** 提交方式 **/
        method : 'get',
        /** 查询超时时间，暂时设定为3分钟 **/
        timeout : 180000,
        /** 是否显示行号 **/
        rownumbers : true,
        /** 是否单选 **/
        singleSelect : true,
        /** 是否可折叠的 **/
        collapsible : false,
        /** 自适应列宽 **/
        fitColumns : true,
        /** 自适应父窗口 **/
        fit : true,
        /** 是否开启分页 **/
        pagination : false,
        /** 加载数据提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        /** 锁定列定义 **/
        frozenColumns : [ [
        ] ],
        columns : [ [
        /** 列定义 **/
        {
            field : 'tradeTime',
            title : '记账时间',
            width : 30,
            formatter : $.DateUtil.dateFormatToStr
        }, {
            field : 'tradeType',
            title : '划扣渠道',
            width : 30
        }, {
            field : 'amount',
            title : '金额',
            width : 50,
            vType : 'rmb'
        }
        ] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 9999,
        /** 可以设置每页记录条数的列表 **/
        pageList : [9999],
        /** 自定义行样式 **/
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 加载数据后自适应表格 **/
            $.loanFeeInfoList.offerRepayDataGrid.datagrid('resize');
        }
    });
    
    $.loanFeeInfoList.offerRepayWin.window({
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
    

    /** 分页组件初始化 **/
    $.loanFeeInfoList.pager = $.loanFeeInfoList.dataGrid.datagrid('getPager');
    $.loanFeeInfoList.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.loanFeeInfoList.pg.page = pageNumber;
            $.loanFeeInfoList.pg.rows = pageSize;
            $.loanFeeInfoList.reloadDataGrid();
        }
    });

    
    /** 页面初始化 **/
    $.loanFeeInfoList.init();
})