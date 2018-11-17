$(function() {
    /** 收集登录用户所有权限菜单 **/
    var offerPerms = parent.$("#offerParams").data("offerPerms");
    var offerPerms = offerPerms.replace("\[", "");
    offerPerms = offerPerms.replace("\]", "");
    offerPerms = offerPerms.split(",");
    var permsObj = {};
    $.each(offerPerms, function(index, value) {
        permsObj[$.trim(value)] = $.trim(value);
    });

    /** 参数初始化 **/
    $.loanExternalDebt = {
        /** 表格数据源地址 * */
        dataGridUrl : global.contextPath + '/loan/listExternalDebt',
        /** 客户信息表格 * */
        dataGrid : $('#querBatchNumDefaultDataGrid'),
        /** 分页控件 * */
        pager : undefined,
        /** 每页显示的记录条数，默认为10 * */
        pageSize : 10,
        /** 设置每页记录条数的列表 * */
        pageSizeList : [ 10, 20, 30, 40, 50],
        /** 加载表格数据 * */
        reloadDataGrid : function() {
            /** 获取查询表单数据转换成JSON对象 * */
            var queryParam = {};
            var batchNum = parent.$('#batchNum').val();
            var orgTemp = parent.$('#financialorg').combobox('getValue');
            // 合同编号
            var contractNum = parent.$('#contractNum').val();
            queryParam.org = orgTemp;
            queryParam.batchNum = batchNum;
            // 合同编号
            queryParam.contractNum = contractNum;
            queryParam.url = $.loanExternalDebt.dataGridUrl;
            $.loanExternalDebt.dataGrid.datagrid('reloadData', queryParam);
        }
    };

    /** 分页参数（page:当前第N页，rows:一页N行） * */
    $.loanExternalDebt.pg = {
        'page' : 1,
        'rows' : $.loanExternalDebt.pageSize
    };

    /** dataGrid初始化 **/
    $.loanExternalDebt.dataGrid.datagrid({
        pg : $.loanExternalDebt.pg,
        /** 提交方式 * */
        method : 'get',
        /** 是否显示行号 * */
        rownumbers : true,
        /** 是否单选 * */
        singleSelect : true,
        /** 是否可折叠的 * */
        collapsible : false,
        /** 自适应列宽 * */
        fitColumns : false,
        fit : true,
        // height : '100%',
        /** 是否开启分页 * */
        pagination : true,
        loadMsg : '数据加载中,请稍等...',
        columns : [ [/** 列定义 * */
            {
                field : 'batchNum',
                title : '批次号',
                width : '20%',
                formatter : function(value, row, rowIndex) {
                    return "<a href='querBatchNumDetail?batNum=" + value + "'>" + value + "</a>";
                }
            },
            {
                field : 'date',
                title : '生成日期',
                width : '10%',
                formatter : function(value, row, index) {
                    var str = "";
                    if (row != null && row.batchNum != null) {
                        if (row.batchNum.substring(0, 4) == 'WC2-') {
                            str = row.batchNum.substring(4, 8)
                                    + "-"
                                    + row.batchNum.substring(8, 10)
                                    + "-"
                                    + row.batchNum.substring(10, 12)
                                    + " "
                                    + row.batchNum.substring(12, 14)
                                    + ":"
                                    + row.batchNum.substring(14, 16)
                                    + ":00";
                        } else if (row.batchNum.substring(0, 4) == 'LXXD') {
                        	str = row.batchNum.substring(4, 8)
                            + "-"
                            + row.batchNum.substring(8, 10)
                            + "-"
                            + row.batchNum.substring(10, 12)
                            + " "
                            + row.batchNum.substring(12, 14)
                            + ":"
                            + row.batchNum.substring(14, 16)
                            + ":00";
                       } else if (row.batchNum.substring(0, 4) == 'WMXT') {
                       	str = row.batchNum.substring(4, 8)
                        + "-"
                        + row.batchNum.substring(8, 10)
                        + "-"
                        + row.batchNum.substring(10, 12)
                        + " "
                        + row.batchNum.substring(12, 14)
                        + ":"
                        + row.batchNum.substring(14, 16)
                        + ":00";
                       }else if (row.batchNum.substring(0, 4) == 'SSJ2') {
                            str = row.batchNum.substring(3, 7)
                                    + "-"
                                    + row.batchNum.substring(7, 9)
                                    + "-"
                                    + row.batchNum.substring(9, 11)
                                    + " "
                                    + row.batchNum.substring(11, 13)
                                    + ":"
                                    + row.batchNum.substring(12, 14)
                                    + ":00";
                        } else if (row.batchNum.substring(0, 4) == 'BHXT') {
                            str = row.batchNum.substring(4, 8)
                                + "-"
                                + row.batchNum.substring(8, 10)
                                + "-"
                                + row.batchNum.substring(10, 12)
                                + " "
                                + row.batchNum.substring(12, 14)
                                + ":"
                                + row.batchNum.substring(14, 16)
                                + ":00";
                        } else if( row.batchNum.substring(0, 4) == 'BH2-' ){
                            str = row.batchNum.substring(4, 8)
                            + "-"
                            + row.batchNum.substring(8, 10)
                            + "-"
                            + row.batchNum.substring(10, 12)
                            + " "
                            + row.batchNum.substring(12, 14)
                            + ":"
                            + row.batchNum.substring(14, 16)
                            + ":00";
                        } else if( row.batchNum.substring(0, 4) == 'WM2-' ){
                            str = row.batchNum.substring(4, 8)
                            + "-"
                            + row.batchNum.substring(8, 10)
                            + "-"
                            + row.batchNum.substring(10, 12)
                            + " "
                            + row.batchNum.substring(12, 14)
                            + ":"
                            + row.batchNum.substring(14, 16)
                            + ":00";
                        }else if(row.batchNum.substring(0, 4) == 'BSYH'){
                        	str = row.batchNum.substring(4, 8)
                            + "-"
                            + row.batchNum.substring(8, 10)
                            + "-"
                            + row.batchNum.substring(10, 12)
                            + " "
                            + row.batchNum.substring(12, 14)
                            + ":"
                            + row.batchNum.substring(14, 16)
                            + ":00";
                        } else if( row.batchNum.substring(0, 4) == 'HRBH' ){
                            str = row.batchNum.substring(4, 8)
                                + "-"
                                + row.batchNum.substring(8, 10)
                                + "-"
                                + row.batchNum.substring(10, 12)
                                + " "
                                + row.batchNum.substring(12, 14)
                                + ":"
                                + row.batchNum.substring(14, 16)
                                + ":00";
                        }  else {
                            str = row.batchNum.substring(2, 6)
                                    + "-"
                                    + row.batchNum.substring(6, 8)
                                    + "-"
                                    + row.batchNum.substring(8, 10)
                                    + " "
                                    + row.batchNum.substring(10, 12)
                                    + ":"
                                    + row.batchNum.substring(12, 14)
                                    + ":00";
                        }
                    }
                    return str;
                }
            },
            {
                field : 'operator',
                title : '操作',
                width : '50%',
                formatter : function(value, row, index) {
                    var str = "";
                    if (row.batchNum.substring(0, 4) == 'WC2-' || row.batchNum.substring(0, 4) == 'BH2-' || row.batchNum.substring(0, 4) == 'HRBH' || row.batchNum.substring(0, 4) == 'WM2-' || row.batchNum.substring(0, 2) == 'HM' || row.batchNum.substring(0, 4) == 'LXXD' || row.batchNum.substring(0, 4) == 'WMXT'||  row.batchNum.substring(0, 4) == 'BHXT'|| row.batchNum.substring(0, 4) == 'BSYH') {
                        // 挖财2和海门小贷不显示【债权导出(供爱特) 】和【还款计划导出(供爱特) 】下载按钮
                    } else {
                        if (permsObj["/loan/eloanExport"] != null) {
                            str += "<a href='#' batchNum='"+ row.batchNum + "' loanId='" + row.loanId + "' offerId='"+row.id+"' name='"+row.name+"' " +
                                    "class='exportExtDebt' style='margin-right: 20px;'></a>";
                        }
                        if (permsObj["/loan/exportExtDebtReyment"] != null) {
                            str += "<a href='#' batchNum='"+ row.batchNum + "' loanId='" + row.loanId + "' class='exportExtDebtReyment' style='margin-right: 20px;'></a>";
                        }
                    }
    
                    if (row.batchNum.substring(0, 2) == 'AT') {
                        // 不显示
                    } else if (row.batchNum.substring(0, 2) == 'SS') {
                        // 不显示
                    } else {
                    	if (permsObj["/loan/exportExtDebtCheckInfo"] != null) {
                    		str += "<a href='#' batchNum='"+ row.batchNum + "' class='exportExtDebtCheckInfo' style='margin-right: 20px;'></a>";
                    	}
                        if(row.batchNum.substring(0,4) == 'WMXT'){
                        	if (permsObj["/loan/exportExtDebtInfo"] != null) {
                        		str += "<a href='#' batchNum='"+ row.batchNum + "' class='exportTxtDebtInfo' style='margin-right: 20px;'></a>";
                        	}
                        }else{
                            if (permsObj["/loan/exportExtDebtInfo"] != null) {
                                str += "<a href='#' batchNum='"+ row.batchNum + "' class='exportExtDebtInfo' style='margin-right: 20px;'></a>";
                            }
                        }
                    }
                    if(row.batchNum.substring(0,4) == 'WMXT'){
                    	if (permsObj["/loan/exportExtRepaySchedule"] != null) {
                            str += "<a href='#' batchNum='"+ row.batchNum + "' class='exportExtRepaySchedule' style='margin-right: 20px;'></a>";
                        }
                    }
                    return str;
                }
            }
        ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.loanExternalDebt.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.loanExternalDebt.pageSizeList,
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess : function(data) {
            /** 债权导出(供爱特) **/
            $(".exportExtDebt").linkbutton({
                text : '债权导出(供爱特)',
                plain : true,
                iconCls : 'pic_51',
                onClick : function() {
                    // 批次号
                    var batchNum = $(this).attr("batchNum");
                    var url = global.contextPath + "/loan/eloanExport";
                    var json = {"batchNum":batchNum};
                    $.messager.confirm("提示","确认下载【债权导出(供爱特)】文件吗？",function(r) {
                        if (r) {
                            // 下载文件
                            downloadFile(url, json);
                        }
                    });
                }
            });
            /** 还款计划导出(供爱特) **/
            $(".exportExtDebtReyment").linkbutton({
                text : '还款计划导出(供爱特)',
                plain : true,
                iconCls : 'pic_51',
                onClick : function() {
                    // 批次号
                    var batchNum = $(this).attr("batchNum");
                    var url = global.contextPath + "/loan/exportExtDebtReyment";
                    var json = {"batchNum":batchNum};
                    $.messager.confirm("提示","确认下载【还款计划导出(供爱特)】文件吗？",function(r) {
                        if (r) {
                            // 下载文件
                            downloadFile(url, json);
                        }
                    });
                }
            });
            /** 债权导出(供理财)  **/
            $(".exportExtDebtInfo").linkbutton({
                text : '债权导出(供理财)',
                plain : true,
                iconCls : 'pic_51',
                onClick : function() {
                    // 批次号
                    var batchNum = $(this).attr("batchNum");
                    var url = global.contextPath + "/loan/exportExtDebtInfo";
                    var json = {"batchNum":batchNum};
                    $.messager.confirm("提示","确认下载【债权导出(供理财)】文件吗？",function(r) {
                        if (r) {
                            // 下载文件
                            downloadFile(url, json);
                        }
                    });
                }
            });
            /** 债权审核导出(供理财) excel文件**/
            $(".exportExtDebtCheckInfo").linkbutton({
                text : '债权审核导出(供理财)',
                plain : true,
                iconCls : 'pic_51',
                onClick : function() {
                    // 批次号
                    var batchNum = $(this).attr("batchNum");
                    var url = global.contextPath + "/loan/exportExtDebtCheckInfo";
                    var json = {"batchNum":batchNum};
                    $.messager.confirm("提示","确认下载【债权审核导出(供理财)】文件吗？",function(r) {
                        if (r) {
                            // 下载文件
                            downloadFile(url, json);
                        }
                    });
                }
            });
            
            /** 债权导出(供理财) txt 文件**/
            $(".exportTxtDebtInfo").linkbutton({
                text : '债权导出(供理财)',
                plain : true,
                iconCls : 'pic_46',
                onClick : function() {
                    // 批次号
                    var batchNum = $(this).attr("batchNum");
                    var url = global.contextPath + "/loan/exportTxtDebtInfo";
                    var json = {"batchNum":batchNum};
                    $.messager.confirm("提示","确认下载【债权审核导出(供理财)】文件吗？",function(r) {
                        if (r) {
                            // 下载文件
                            downloadFile(url, json);
                        }
                    });
                }
            });
            
            /** 还款计划导出(供外贸信托) txt 文件**/
            $(".exportExtRepaySchedule").linkbutton({
                text : '还款计划导出',
                plain : true,
                iconCls : 'pic_46',
                onClick : function() {
                    // 批次号
                    var batchNum = $(this).attr("batchNum");
                    var url = global.contextPath + "/loan/exportExtRepaySchedule";
                    var json = {"batchNum":batchNum};
                    $.messager.confirm("提示","确认下载【还款计划导出】文件吗？",function(r) {
                        if (r) {
                            // 下载文件
                            downloadFile(url, json);
                        }
                    });
                }
            });
            
            /** 页面自适应 **/
            $.loanExternalDebt.dataGrid.datagrid('resize');
        }
    });

    /** 分页对象初始化 **/
    $.loanExternalDebt.pager = $.loanExternalDebt.dataGrid.datagrid('getPager');
    $.loanExternalDebt.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.loanExternalDebt.pg.page = pageNumber;
            $.loanExternalDebt.pg.rows = pageSize;
            $.loanExternalDebt.reloadDataGrid();
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
    $.loanExternalDebt.reloadDataGrid();
    /** 隐藏下载按钮、生成批次按钮**/
    parent.$('#batchDownloadBtn').hide();
    parent.$('#createBatchNum').hide();
});
