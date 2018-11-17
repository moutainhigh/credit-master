$(function(){
    $.queryReliefInfo = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/applyReliefRepayManager/queryReliefInfoPage',
        exportUrl: global.contextPath + '/applyReliefRepayManager/exportQueryReliefInfoReport',
        /** 数据表格对象 **/
        dataGrid : $('#queryReliefInfoDataGrid'),
        /** 分页控件 **/
        pager : undefined,
        applyStatuses : {
            '1' : '申请',
            '2' : '通过',
            '3' : '拒绝',
            '4' : '取消',
            '5' : '生效',
            '6' : '失效',
            '7' : '完成'
        },
        /** 减免类型 **/
        applyTypes: {
            '01':'一般减免',
            '02':'结清减免'
        },
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if (!$.queryReliefInfo.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.queryReliefInfo.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.queryReliefInfo.dataGridUrl;
            /** 查询并加载数据**/
            $.queryReliefInfo.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.queryReliefInfo.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.queryReliefInfo.pg = {
        'page' : 1,
        'rows' : $.queryReliefInfo.pageSize
    }

    /** DataGrid初始化 **/
    $.queryReliefInfo.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.queryReliefInfo.pg,
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
        /** 设置true，表示适应它的父容器 **/
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        /** 数据长度超出，自动换行 **/
        nowrap : true,
        /** 禁止服务端排序 **/
        remoteSort:false,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            // 列定义
            {
                field : 'contractNum',
                title : '合同编号',
                width : '10%'
            },           {
                field : 'salesDepartmentName',
                title : '管理营业部',
                width : '10%'
            },           {
                field : 'name',
                title : '客户姓名',
                width : '10%'
            },            {
                field : 'loanType',
                title : '借款类型',
                width : '10%'
            },
            {
                field : 'repayAmount',
                title : '应还总额',
                width : '10%'
            },{
                field : 'reliefAmonut',
                title : '减免总额',
                width : '10%'
            },{
                field : 'releifDate',
                title : '减免日期',
                width : '10%',
                formatter:$.DateUtil.dateFormatToStr
            },{
                field : 'applyType',
                title : '减免类型',
                width : '10%',
                formatter : function(value, row, index) {
                    return $.queryReliefInfo.applyTypes[value + ''] || '未知';
                },
            },
            {
                field : 'applySource',
                title : '申请来源',
                width : '10%',
                formatter : function(value, row, index) {
                    if (row.flag == "0" && row.isSpecial == "0") {
                        return "门店";
                    }
                    return "总部";
                },
            },{
                field : 'isSpecial',
                title : '是否特殊',
                width : '5%',
                formatter : function(value, row, index) {
                    if (row.isSpecial == "0") {
                        return "否";
                    }
                    return "是";
                },
            }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.queryReliefInfo.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.queryReliefInfo.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        }
    });

    /** 表格分页组件 **/
    $.queryReliefInfo.pager = $.queryReliefInfo.dataGrid.datagrid('getPager');
    $.queryReliefInfo.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.queryReliefInfo.pg.page = pageNumber;
            $.queryReliefInfo.pg.rows = pageSize;
            $.queryReliefInfo.reloadDataGrid();
        }
    });
    $.queryReliefInfo.reloadDataGrid();
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.queryReliefInfo.pg.page = 1;
        $.queryReliefInfo.reloadDataGrid();
    });

    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.queryReliefInfo.searchForm.form('reset');
    });
    /** 导出 **/
    $("#exportBtn").click(function(){
        $.messager.confirm("提示","最大可导出50000条记录，请确认要导出Excel文件吗？",function(r){
            if(r){
                /** 获取查询表单数据转换成JSON对象 **/
                var searchMsg = $.queryReliefInfo.searchForm.serialize();
                /** 对参数进行解码(显示中文) **/
                searchMsg = decodeURIComponent(searchMsg);
                /** 字符串转换为对象 **/
                var queryParam = $.serializeToJsonObject(searchMsg);
                $.downloadFile({
                    url:$.queryReliefInfo.exportUrl,
                    isDownloadBigFile:true,
                    params:queryParam,
                    successFunc:function(data){
                        if(data== null){
                            $.messager.alert('提示','下载成功！','info');
                        }else{
                            if(data.resMsg!= null){
                                $.messager.alert('警告',data.resMsg,'warning');
                            }else{
                                $.messager.alert('异常','下载失败！','error');
                            }
                        }
                    },
                    failFunc:function(data){
                        $.messager.alert('异常','下载失败！','error');
                    }
                });
            }
        });
    });
})