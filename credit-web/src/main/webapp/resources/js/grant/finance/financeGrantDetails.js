/**
 * Created by ym10094 on 2016/11/22.
 */
$(function() {
    $.grantDetails = {
        /** 放款款详细信息表格数据源地址 **/
        dataGridUrl : global.contextPath + "/grant/finance/searchFinaceGrantDetails",
        /** 放款详细信息表格对象 **/
        grantDetailsDataGrid : $("#detailsDataGrid"),
        /** 放款详细信息窗口 **/
        grantDetailsWin : $("#grantDetails"),
        /** 分页控件 **/
        pager : undefined,
        /** loanId**/
        grantLoanId : undefined,
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载联系人数据 **/
        reloadDataGrid : function() {
            if ($.isEmpty($.grantDetails.grantLoanId)) {
                $.messager.alert('提示信息', "缺少查询放款详情所必须的参数！", 'error');
                return;
            }
            /** 字符串转换为对象 **/
            var queryParam = {
                "loanId" : $.grantDetails.grantLoanId
            };
            /** 追加url参数**/
            queryParam.url = $.grantDetails.dataGridUrl;
            /** 查询并加载数据**/
            $.grantDetails.grantDetailsDataGrid.datagrid("reloadData",queryParam);
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） * */
    $.grantDetails.pg = {
        'page' : 1,
        'rows' : $.grantDetails.pageSize
    }

    /** 报盘流水信息表格对象初始化 **/
    $.grantDetails.grantDetailsDataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.grantDetails.pg,
        /** 提交方式 **/
        method : 'get',
        /** 是否显示行号 * */
        rownumbers : true,
        /** 是否单选 * */
        singleSelect : true,
        /** 是否可折叠的 * */
        collapsible : false,
        /** 自适应列宽 * */
        fitColumns : true,
        /** 设置true，表示适应它的父容器 **/
        fit : true,
        /** 是否开启分页 * */
        pagination : true,
        /** 数据长度超出，自动换行 **/
        nowrap : true,
        /** 禁止服务端排序 **/
        remoteSort : false,
        loadMsg : '数据加载中,请稍等...',
        columns : [ [ {
            field : 'grantApplyDate',
            title : '放款申请时间',
            width : '20%',
            formatter : $.DateUtil.dateFormatToFullStr
        }, {
            field : 'grantApplyFinishDate',
            title : '放款时间',
            width : '20%',
            formatter : $.DateUtil.dateFormatToFullStr
        }, {
            field : 'grantState',
            title : '放款结果',
            width : '10%',
            formatter:function(value,row,index){
                var result = "";
                if(value){
                    if(value == '01'){
                        result = "申请中";
                    }else if(value == '02'){
                        result = "放款成功";
                    }else if(value == '03'){
                        result = "放款失败";
                    }else if(value == '04'){
                        result = "关闭";
                    }else {
                        result = "未申请";
                    }
                    return result;
                }
            }
        }, {
            field : 'remark',
            title : '备注',
            width : '40%'
        } ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.grantDetails.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.grantDetails.pageSizeList,
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        }
    });

    /** 分页组件初始化 **/
    $.grantDetails.pager = $.grantDetails.grantDetailsDataGrid.datagrid('getPager');
    $.grantDetails.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.grantDetails.pg.page = pageNumber;
            $.grantDetails.pg.rows = pageSize;
            $.grantDetails.reloadDataGrid();
        }
    });

    /** 放款详细窗口面板参数定义 * */
    $.grantDetails.grantDetailsWin.window({
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