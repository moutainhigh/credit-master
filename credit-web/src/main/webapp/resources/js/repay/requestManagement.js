$(function() {
    $.requestManagement = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/requestManagement/search',
        /** 划拨申请书导出（PDF） **/
        exportApplyPdfUrl: global.contextPath + '/requestManagement/exportApplyPdf',
        /** 划拨申请书导出（Xls） **/
        exportApplyXlsUrl: global.contextPath + '/requestManagement/exportApplyXls',
        /** 放款申请明细导出（Xls） **/
        exportLoanApplyXlsUrl: global.contextPath + '/requestManagement/exportLoanApplyXls',
        /** 放款申请明细导出（Txt） **/
        exportLoanApplyTxtUrl: global.contextPath + '/requestManagement/exportLoanApplyTxt',
        /** 还款计划导出（Xls） **/
        exportPayPlanXlsUrl: global.contextPath + '/requestManagement/exportPayPlanXls',
        /** 划拨申请书导入 **/
        importUrl: global.contextPath + '/requestManagement/importApplyPdfAndXls',
        importEsignatureUrl: global.contextPath + '/requestManagement/applyPdfEsignature',
        /** 数据表格对象 **/
        dataGrid : $('#dataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 上传文件面板 **/
        importExcelWin : $("#importExcelWin"),
        /** 划拨申请书导入表单对象 **/
        baseFileForm : $("#baseFileForm"),
        /** 划拨申请书导出（PDF）权限 **/
        isExportApplyPdf : $("#isExportApplyPdf").val(),
        /** 划拨申请书导出（Xls）权限 **/
        isExportApplyXls : $("#isExportApplyXls").val(),
        /** 放款申请明细导出（Xls）权限 **/
        isExportLoanApplyXls : $("#isExportLoanApplyXls").val(),
        /** 放款申请明细导出（Txt）权限 **/
        isExportLoanApplyTxt : $("#isExportLoanApplyTxt").val(),
        /** 还款计划导出（Xls）权限 **/
        isExportPayPlanXls : $("#isExportPayPlanXls").val(),
        /** 划拨申请书导入权限 **/
        isImportApplyPdf : $("#isImportApplyPdf").val(),
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if (!$.requestManagement.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.requestManagement.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.requestManagement.dataGridUrl;
            /** 查询并加载数据**/
            $.requestManagement.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.requestManagement.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }
    
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.requestManagement.pg = {
        'page' : 1,
        'rows' : $.requestManagement.pageSize
    }

    /** DataGrid初始化 **/
    $.requestManagement.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.requestManagement.pg,
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
            field : 'batchNum',
            title : '批次号',
            width : '12%'
        },{
            field : 'createTime',
            title : '生成时间',
            width : '10%',
            formatter : function(value, row, rowIndex) {
                var elements = "";
                if (row != null ) {
                    // 债权批次号
                    var batchNum = row.batchNum;
                    if(batchNum.indexOf("BHXT")>=0 || batchNum.indexOf("BH2-")>=0 || batchNum.indexOf("HRBH")>=0){
                        elements = batchNum.substring(4, 8) + "-"+ batchNum.substring(8, 10) + "-" + batchNum.substring(10, 12) + " "+
                        batchNum.substring(12, 14) + ":"+ batchNum.substring(14, 16) + ":00";
                    }
                }
                return elements;
            }
        },{
            field : 'status',
            title : '状态',
            width : '6%'
        },{
            field : 'operate',
            title : '操作',
            width : '70%',
            formatter:function(value,row,index){
                if(row){
                    var elements = "";
                    /** 显示划拨申请书导出（Pdf）链接 **/
                    if($.requestManagement.isExportApplyPdf == "true"){
                        elements +="<a href='#' class='applyPdf' batchNum='"+ row.batchNum +"'></a>";
                    }
                    /** 显示划拨申请书导出（Xls）链接 **/
                    if($.requestManagement.isExportApplyXls == "true"){
                        elements +="<a href='#' class='applyXls' batchNum='"+ row.batchNum +"'></a>";
                    }
                    /** 显示放款申请明细导出（Xls）链接 **/
                    if($.requestManagement.isExportLoanApplyXls == "true"){
                        elements +="<a href='#' class='loanApplyXls' batchNum='"+ row.batchNum +"'></a>";
                    }
                    /** 显示放款申请明细导出（Txt）链接 **/
                    if($.requestManagement.isExportLoanApplyTxt == "true"){
                        elements +="<a href='#' class='loanApplyTxt' batchNum='"+ row.batchNum +"'></a>";
                    }
                    /** 显示还款计划导出（Xls）链接 **/
                    if($.requestManagement.isExportPayPlanXls == "true"){
                        elements +="<a href='#' class='payPlanXls' batchNum='"+ row.batchNum +"'></a>";
                    }
                    /** 显示还款计划导出（Xls）链接 **/
                    if($.requestManagement.isImportApplyPdf == "true"){
                        elements +="<a href='#' class='importApply' batchNum='"+ row.batchNum +"'></a>";
                    }
                    return elements;
                }
            }
        }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.requestManagement.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.requestManagement.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 划拨申请书导出（PDF） **/
            $(".applyPdf").linkbutton({
                text:'划拨申请书导出（PDF）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // 债权批次号
                    var batchNum = $(this).attr("batchNum");
                    $.messager.confirm("提示","确认导出划拨申请书（PDF）文件吗？",function(r){
                        if(r){
                            var params = {"batchNum":batchNum};
                            ajaxSubmit($.requestManagement.exportApplyPdfUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 划拨申请书导出（Xls） **/
            $(".applyXls").linkbutton({
                text:'划拨申请书导出（Xls）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // 债权批次号
                    var batchNum = $(this).attr("batchNum");
                    $.messager.confirm("提示","确认导出划拨申请书（Xls）文件吗？",function(r){
                        if(r){
                            var params = {"batchNum":batchNum};
                            ajaxSubmit($.requestManagement.exportApplyXlsUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 放款申请明细导出（Xls） **/
            $(".loanApplyXls").linkbutton({
                text:'放款申请明细导出（Xls）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // 债权批次号
                    var batchNum = $(this).attr("batchNum");
                    $.messager.confirm("提示","确认导出放款申请明细（Xls）文件吗？",function(r){
                        if(r){
                            var params = {"batchNum":batchNum};
                            ajaxSubmit($.requestManagement.exportLoanApplyXlsUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 放款申请明细导出（Txt） **/
            $(".loanApplyTxt").linkbutton({
                text:'放款申请明细导出（Txt）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // 债权批次号
                    var batchNum = $(this).attr("batchNum");
                    $.messager.confirm("提示","确认导出放款申请明细（Txt）文件吗？",function(r){
                        if(r){
                            var params = {"batchNum":batchNum};
                            ajaxSubmit($.requestManagement.exportLoanApplyTxtUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 还款计划导出（Xls） **/
            $(".payPlanXls").linkbutton({
                text:'还款计划导出（Xls）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // 债权批次号
                    var batchNum = $(this).attr("batchNum");
                    $.messager.confirm("提示","确认导出还款计划（Xls）文件吗？",function(r){
                        if(r){
                            var params = {"batchNum":batchNum};
                            ajaxSubmit($.requestManagement.exportPayPlanXlsUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 划拨申请书导入 **/
            $(".importApply").linkbutton({
                text:'划拨申请书导入',
                plain:true,
                iconCls:'upload_3',
                onClick:function(){
                    // 债权批次号
                    var batchNum = $(this).attr("batchNum");
                    $.requestManagement.importExcelWin.window('open');
                    $.requestManagement.baseFileForm.form('clear');
                    $("#hksqBatchNum").attr("value",batchNum);
                }
            });
            
            /** 页面自适应 **/
            $.requestManagement.dataGrid.datagrid('resize');
        }
    });
    
    /** 表格分页组件 **/
    $.requestManagement.pager = $.requestManagement.dataGrid.datagrid('getPager');
    $.requestManagement.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.requestManagement.pg.page = pageNumber;
            $.requestManagement.pg.rows = pageSize;
            $.requestManagement.reloadDataGrid();
        }
    });
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.requestManagement.pg.page = 1;
        $.requestManagement.reloadDataGrid();
    });
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.requestManagement.searchForm.form('reset');
    });
    
    /** 划拨申请书上传 **/
    $("#submitBtn").click(function(){
        // 划拨申请书签章文件(pdf)文件
        var pdfFile = $("#applyPdfFile").filebox("getValue");
        if($.isEmpty(pdfFile)){
            $.messager.alert('警告','请选择导入已签章划拨申请书(pdf)文件！','warning');
            return;
        }
        // 文件名转换为小写
        pdfFile = pdfFile.toLowerCase();
        // 必须是pdf文件
        if(pdfFile.lastIndexOf(".pdf")==-1){
            $.messager.alert('警告','文件类型错误！必须是pdf格式。','warning');
            return ;
        }
        // 划拨申请书(xls)文件
        var xlsFile = $("#applyXlsFile").filebox("getValue");
        if($.isEmpty(xlsFile)){
            $.messager.alert('警告','请选择导入划拨申请书(xls)文件！','warning');
            return;
        }
        // 文件名转换为小写
        xlsFile = xlsFile.toLowerCase();
        // 必须是xls文件
        if(xlsFile.lastIndexOf(".xls")==-1){
            $.messager.alert('警告','文件类型错误！必须是xls格式。','warning');
            return ;
        }
        // 划拨申请书签章(pdf)文件批次号
        var pdfSeqNo = pdfFile.substring(pdfFile.lastIndexOf("_") + 1,pdfFile.lastIndexOf(".pdf"));
        // 划拨申请书(xls)文件批次号
        var xlsSeqNo = xlsFile.substring(xlsFile.lastIndexOf("_") + 1,xlsFile.lastIndexOf(".xls"));
        // 两个文件的批次号必须相同
        if(pdfSeqNo != xlsSeqNo){
            $.messager.alert('警告','请导入批次号相同的文件！','warning');
            return ;
        }
        $.messager.confirm("提示","确认上传划拨申请书吗？",function(r){
            if(r){
                $.requestManagement.baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: $.requestManagement.importUrl,
                    hasDownloadFile : false,
                    success: function (data) {
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        if(resCode !="000000"){
                            $.messager.alert('警告',resMsg,'warning');
                            return;
                        }
                        $.messager.alert('提示',resMsg,'info');
                        setTimeout(function(){
                            $.requestManagement.importExcelWin.window('close');
                            $.requestManagement.reloadDataGrid();
                        }, 1000);
                    },
                    error: function (data) {
                        $.messager.alert('警告','操作失败！','warning');
                    }
                });
            }
        });
    });

    /** 划拨申请书(pdf)文件签章 **/
    $("#submitEsignatureBtn").click(function(){
        console.log("签章");
        var applyEsignaturePdf=$("#applyPdfEsignatureFile").filebox("getValue");
        if($.isEmpty(applyEsignaturePdf)){
            $.messager.alert('警告','请按规则导入需要签章的原文件！','warning');
            return ;
        }
        if(applyEsignaturePdf.lastIndexOf(".pdf")==-1){
            $.messager.alert('警告','请按规则导入正确的文件格式(pdf)！','warning');
            return ;
        }
        $.messager.confirm("提示","确认上传划拨申请书原文件吗？",function(r) {
            if (r) {
                $.requestManagement.baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType: 'json',
                    url: $.requestManagement.importEsignatureUrl,
                    hasDownloadFile: true,
                    success: function (data) {
                        if(data == null ){
                            $.messager.alert('信息', "盖章成功！", 'info');
/*                            setTimeout(function () {
                                $.requestManagement.importExcelWin.window('close');
                                $.requestManagement.reloadDataGrid();
                            }, 1000);*/
                        }

                    },
                    error: function (data) {
                        $.messager.alert('警告', data.resMsg, 'warning');
                    }
                });
            }
        })
    })
    /** 关闭导入窗口 **/
    $("#closeBtn").click(function() {
        if ($.requestManagement.importExcelWin) {
            $.requestManagement.importExcelWin.window('close');
        }
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
                    if(isRefresh){
                        // 刷新页面
                        $.requestManagement.reloadDataGrid();
                    }
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
