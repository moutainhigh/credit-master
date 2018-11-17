$(function() {
    $.offerDetail = {
        /** 报盘流水信息表格数据源地址 **/
        dataGridUrl : global.contextPath + '/payment/thirdUnderLine/listLoanOrder',
        /** 报盘流水信息表格对象 **/
        offerDetailDataGrid : $('#offerDetailDataGrid'),
        /** 报盘流水信息窗口 **/
        offerDetailWin : $('#offerDetailWin'),
        /** 分页控件 **/
        pager : undefined,
        /** 报盘Id**/
        offerId : undefined,
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载联系人数据 **/
        reloadDataGrid : function() {
            if($.isEmpty($.offerDetail.offerId)){
                $.messager.alert('异常','缺少url参数信息！','error');
                return;
            }
            /** 清空表格数据 **/
            $.offerDetail.offerDetailDataGrid.datagrid('reloadData', {
                url : $.offerDetail.dataGridUrl + '?' + 'offerId=' + $.offerDetail.offerId
            });
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） * */
    $.offerDetail.pg = {
        'page' : 1,
        'rows' : $.offerDetail.pageSize
    }

    /** 报盘流水信息表格对象初始化 **/
    $.offerDetail.offerDetailDataGrid.datagrid({
        pg : $.offerDetail.pg,
        /** 是否显示行号 * */
        rownumbers : true,
        /** 是否单选 * */
        singleSelect : true,
        /** 是否可折叠的 * */
        collapsible : false,
        /** 自适应列宽 * */
        fitColumns : true,
        fit : true,
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        /** 是否开启分页 * */
        pagination : true,
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
        /** 列定义 * */
        {
            field : 'flowNumber',
            title : '流水号',
            width : 10
        }, {
            field : 'recordNumber',
            title : '记录序号',
            width : 8
        }, {
            field : 'financialType',
            title : '报盘类型',
            width : 8,
            formatter : function(value) {
                if (value == "SXF") {
                    return '服务费';
                } else {
                    return '放款金额';
                }
            }
        }, {
            field : 'offerTime',
            title : '报盘时间',
            width : 15,
            formatter : $.DateUtil.dateFormatToFullStr
        }, {
            field : 'amount',
            title : '金额(元)',
            width : 8
        }, {
            field : 'bankCode',
            title : '银行代码',
            width : 8
        }, {
            field : 'accountNumber',
            title : '账号',
            width : 15
        }, {
            field : 'accountName',
            title : '户名',
            width : 8
        }, {
            field : 'accountType',
            title : '账户类型',
            width : 8,
            formatter : function(value, row, index){
                var type = "";
                if(value == "0"){
                    type = "私人";
                }else if(value == "1"){
                    type = "公司";
                }
                return type;
            }
        }, {
            field : 'currencyType',
            title : '货币类型',
            width : 8
        }, {
            field : 'returnTime',
            title : '回盘时间',
            width : 15,
            formatter : $.DateUtil.dateFormatToFullStr
        }, {
            field : 'state',
            title : '结果/失败原因',
            width : 15
        } ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.offerDetail.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.offerDetail.pageSizeList,
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        }
    });

    /** 分页组件初始化 **/
    $.offerDetail.pager = $.offerDetail.offerDetailDataGrid.datagrid('getPager');
    $.offerDetail.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.offerDetail.pg.page = pageNumber;
            $.offerDetail.pg.rows = pageSize;
            $.offerDetail.reloadDataGrid();
        }
    });

    /** 报盘流水窗口面板参数定义 * */
    $.offerDetail.offerDetailWin.window({
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
})