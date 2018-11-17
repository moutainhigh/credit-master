/**
 * Created by ym10094 on 2016/9/12.
 * 备案查询-导出 深圳地区
 */
$(function(){
    /** 参数初始化 **/
    $.putOnRecordSZInit = {
        /** 表格数据源地址 * */
        dataGridUrl : global.contextPath + '/loan/put/listputOnRecordSZ',
        /** 客户信息表格 * */
        dataGrid : $('#querPutOnRecordSZDataGrid'),
        /** 分页控件 * */
        pager : undefined,
        /** 每页显示的记录条数，默认为10 * */
        pageSize : 10,
        /** 设置每页记录条数的列表 * */
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 查询条件表单 **/
        searchForm : parent.$('#searchForm'),
        /** 加载表格数据 * */
        reloadDataGrid : function() {
            /** 获取查询表单数据转换成JSON对象 * */
            var queryParam = {};
            if($.putOnRecordSZInit.searchForm.form('validate')) {
                //将表单数据转换成json对象
                var searchMsg= $.putOnRecordSZInit.searchForm.serialize();
                /** 对参数进行解码(显示中文) * */
                searchMsg = decodeURIComponent(searchMsg);
                queryParam= $.serializeToJsonObject(searchMsg);
            }
            queryParam = queryParam || {};
            queryParam.url = $.putOnRecordSZInit.dataGridUrl;
            $.putOnRecordSZInit.dataGrid.datagrid('reloadData', queryParam);
        }
    };
    /** 分页参数（page:当前第N页，rows:一页N行） * */
    $.putOnRecordSZInit.pg = {
        'page' : 1,
        'rows' : $.putOnRecordSZInit.pageSize
    };

    /** dataGrid初始化 **/
    $.putOnRecordSZInit.dataGrid.datagrid({
        pg: $.putOnRecordSZInit.pg,
        /** 提交方式 * */
        method: 'get',
        /** 是否显示行号 * */
        rownumbers: false,
        /** 是否单选 * */
        singleSelect: true,
        /** 是否可折叠的 * */
        collapsible: false,
        /** 自适应列宽 * */
        fitColumns: false,
        fit: true,
        // height : '100%',
        /** 是否开启分页 * */
        pagination: true,
        loadMsg: '数据加载中,请稍等...',
        columns: [[/** 列定义 * */
            {
                field: 'enterpriseCode',
                title: '商户代码',
                width: '10%'
            },
            {
                field: 'expenditure',
                title: '费项',
                width: '10%'
            },
            {
                field: 'bankType',
                title: '行别',
                width: '10%'
            },
            {
                field: 'account',
                title: '账号',
                width: '15%'
            },
            {
                field: 'accountName',
                title: '户名',
                width: '10%'
            }]
        ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize: $.putOnRecordSZInit.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList: $.putOnRecordSZInit.pageSizeList,
        toolbar: '#tb',
        /** 自定义行样式 * */
        rowStyler: function (index, row) {
            if (index % 2 == 0) {
            }
        },
    })

    /** 分页对象初始化 **/
    $.putOnRecordSZInit.pager = $.putOnRecordSZInit.dataGrid.datagrid('getPager');
    $.putOnRecordSZInit.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.putOnRecordSZInit.pg.page = pageNumber;
            $.putOnRecordSZInit.pg.rows = pageSize;
            $.putOnRecordSZInit.reloadDataGrid();
        }
    });

    /** 下载文件通用方法 **/
    function downloadFile(url, data){
        $.downloadFile({
            url : url,
            isDownloadBigFile : true,
            params : data,
            successFunc : function(data) {
                if (data == null) {
                    $.messager.alert('提示','下载成功！','info');
                } else {
                    if (data.resMsg != null) {
                        $.messager.alert('警告',data.resMsg,'warning');
                    } else {
                        $.messager.alert('异常','下载失败！','error');
                    }
                }
            },
            failFunc : function(data) {
                $.messager.alert('异常','下载失败！','error');
            }
        });
    }

    /** 初始查询 **/
    $.putOnRecordSZInit.reloadDataGrid();
})