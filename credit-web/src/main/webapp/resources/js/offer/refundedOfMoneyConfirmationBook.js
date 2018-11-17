/**
 * Created by ym10094 on 2016/11/29.
 */
$(function() {
    $.refundedOfMoneyConfirmationBook = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/offer/trustOffer/refundedOfMoneyConfirmationBook/search',
        exportExcelUrl: global.contextPath+"/offer/trustOffer/exportRefundedOfMoneyConfirmationBookExcel",
        exportPdfUrl: global.contextPath+'/offer/trustOffer/exportRefundedOfMoneyConfirmationBookPdf',
        exportSubAccountDetailUrl: global.contextPath+'/offer/trustOffer/exportSubAccountDetailExcel',
        exportTemporaryAmountUrl: global.contextPath+'/offer/trustOffer/exportTemporaryAmountExcel',
        /** 数据表格对象 **/
        dataGrid : $('#dataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),

        /** 导出（Xls）权限 **/
        isExportXls : $("#isExportXls").val(),
        /** 导出（PDF  ）权限 **/
        isImportPdf : $("#isImportPdf").val(),
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if (!$.refundedOfMoneyConfirmationBook.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.refundedOfMoneyConfirmationBook.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.refundedOfMoneyConfirmationBook.dataGridUrl;
            /** 查询并加载数据**/
            $.refundedOfMoneyConfirmationBook.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.refundedOfMoneyConfirmationBook.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.refundedOfMoneyConfirmationBook.pg = {
        'page' : 1,
        'rows' : $.refundedOfMoneyConfirmationBook.pageSize
    }

    /** DataGrid初始化 **/
    $.refundedOfMoneyConfirmationBook.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.refundedOfMoneyConfirmationBook.pg,
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
        pagination : false,
        /** 数据长度超出，自动换行 **/
        nowrap : true,
        /** 禁止服务端排序 **/
        remoteSort:false,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            // 列定义
            {
                field : 'loanBelongs',
                title : '债权去向',
                width : '15%'
            },{
                field : 'tradeDate',
                title : '提交日期',
                width : '20%',
                formatter:$.DateUtil.dateFormatToStr
            },{
                field : 'operate',
                title : '操作',
                width : '63%',
                formatter:function(value,row,index){
                    if(row){
                        var elements = "";
                        var exportDisabled = false;
                        var tradeDate = $.DateUtil.dateFormatToStr(row.tradeDate);
                        elements = "<a href='javascript:void(0)' class='easyui-linkbutton excel' data-options='disabled:"+exportDisabled+"' data-loanBelongs='" + row.loanBelongs + "' data-tradeDate='"+ tradeDate + "'>导出EXCEL</a>" +
                            "<a href='javascript:void(0)' class='easyui-linkbutton pdf' data-options='disabled:"+exportDisabled+"' data-loanBelongs='" + row.loanBelongs +
                            "' data-tradeDate='"+ tradeDate + "'>导出PDF</a>";
                            if(row.loanBelongs == '渤海2' || row.loanBelongs == '华瑞渤海'){
                                elements +="<a href='#' class='exportSubAccountDetailExcel' data-options='disabled:"+exportDisabled+"' data-loanBelongs='" + row.loanBelongs + "' data-tradeDate='"+ tradeDate + "'></a>";
                                elements +="<a href='#' class='temporaryAmount' data-options='disabled:"+exportDisabled+"' data-loanBelongs='" + row.loanBelongs + "' data-tradeDate='"+ tradeDate + "'></a>";	
                            }
                        return elements;
                    }
                }
            }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.refundedOfMoneyConfirmationBook.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.refundedOfMoneyConfirmationBook.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 导出回款确认书excel文件 **/
            $(".easyui-linkbutton.excel").linkbutton({
                text:'回款确认书EXCEL',
                plain:true,
                iconCls:'download_1',
                onClick :function(){
                    if($(this).linkbutton("options").disabled == false) {
                        var tradeDate =  $(this).attr("data-tradeDate");
                        var loanBelongs = $(this).attr("data-loanBelongs");
                        $.messager.confirm("提示", "确认导出EXCEL文件吗？", function (r) {
                            if (r) {
                                var params = {"tradeDate": tradeDate,"loanBelongs":loanBelongs};
                                ajaxSubmit($.refundedOfMoneyConfirmationBook.exportExcelUrl, params ,false);

                            }
                        });
                    }
                }
            });
            
            /** 导出回款确认书pdf文件 **/
            $(".easyui-linkbutton.pdf").linkbutton({
                text:'回款确认书PDF',
                plain :true,
                iconCls :'download_1',
                onClick : function() {
                    if ($(this).linkbutton("options").disabled == false) {
                        var tradeDate =  $(this).attr("data-tradeDate");
                        var loanBelongs = $(this).attr("data-loanBelongs");
                        $.messager.confirm("提示", "确认导出PDF文件吗？", function (r) {
                            if (r) {
                                var params = {"tradeDate": tradeDate,"loanBelongs":loanBelongs};
                                ajaxSubmit($.refundedOfMoneyConfirmationBook.exportPdfUrl, params, false);

                            }
                        });
                    }
                }
            });

            /** 导出分账明细报表 **/
            $(".exportSubAccountDetailExcel").linkbutton({
                text:'分账明细',
                plain :true,
                iconCls :'download_1',
                onClick : function() {
                    if ($(this).linkbutton("options").disabled == false) {
                        var tradeDate =  $(this).attr("data-tradeDate");
                        var loanBelongs = $(this).attr("data-loanBelongs");
                        $.messager.confirm("提示", "确认导出分账明细表吗？", function (r) {
                            if (r) {
                                var params = {"tradeDate": tradeDate,"loanBelongs":loanBelongs};
                                ajaxSubmit($.refundedOfMoneyConfirmationBook.exportSubAccountDetailUrl, params, false);
                            }
                        });
                    }
                }
            });

            /** 导出其他暂收款报表 **/
            $(".temporaryAmount").linkbutton({
                text:'其他暂收款',
                plain :true,
                iconCls :'download_1',
                onClick : function() {
                    if (!$(this).linkbutton("options").disabled) {
                        var tradeDate =  $(this).attr("data-tradeDate");
                        var loanBelongs = $(this).attr("data-loanBelongs");
                        $.messager.confirm("提示", "确认导出其他暂收款报表吗？", function (r) {
                           if (r) {
                                var params = {"tradeDate": tradeDate,"loanBelongs":loanBelongs};
                                ajaxSubmit($.refundedOfMoneyConfirmationBook.exportTemporaryAmountUrl, params, false);
                            }
                        });
                    }
                }
            });
            /** 页面自适应 **/
            $.refundedOfMoneyConfirmationBook.dataGrid.datagrid('resize');
        }
    });

    /** 表格分页组件 **/
    $.refundedOfMoneyConfirmationBook.pager = $.refundedOfMoneyConfirmationBook.dataGrid.datagrid('getPager');
    $.refundedOfMoneyConfirmationBook.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.refundedOfMoneyConfirmationBook.pg.page = pageNumber;
            $.refundedOfMoneyConfirmationBook.pg.rows = pageSize;
            $.refundedOfMoneyConfirmationBook.reloadDataGrid();
        }
    });

    /** 查询处理 **/
    $("#searchBtn").click(function(){
        var tradeDate =  $("#tradeDate").datebox("getValue");
        if($.isEmpty(tradeDate)){
            $.messager.alert('警告','提交日期不能为空！','warning');
            return ;
        }
        $.refundedOfMoneyConfirmationBook.pg.page = 1;
        $.refundedOfMoneyConfirmationBook.reloadDataGrid();
    });

    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.refundedOfMoneyConfirmationBook.searchForm.form('reset');
    });
    /** 提交异步请求 **/
    function ajaxSubmit(url, params, isRefresh){
        $.downloadFile({
            url:url,
            isDownloadBigFile:true,
            params:params,
            successFunc:function(data){
                if(data == null){
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
})
