$(function() {
    $.requestManagement = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/repay/search',
        /** 生成划拨申请书 **/
        createApplication: global.contextPath + '/repay/createApplication',
        /** 划拨申请书导出（PDF） **/
        exportApplyPdfUrl: global.contextPath + '/repay/exportApplyPdf',
        /** 划拨申请书导出（Xls） **/
        exportApplyXlsUrl: global.contextPath + '/repay/exportApplyXls',
        /** 预拨确认书导出（PDF） **/
        exportConfirmPdfUrl: global.contextPath + '/repay/exportConfirmPdf',
        /** 预拨确认书导出（Xls） **/
        exportConfirmXlsUrl: global.contextPath + '/repay/exportConfirmXls',
        /** 放款申请明细导出（Xls） **/
        exportGrantDetailUrl: global.contextPath + '/repay/exportGrantDetailXls',
      
        /** 数据表格对象 **/
        dataGrid : $('#dataGrid'),
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
//            if (!$.requestManagement.validate()) {
//                return;
//            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.requestManagement.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);          
            /** 追加url参数**/
            queryParam.url = $.requestManagement.dataGridUrl;
            /** 查询并加载数据**/queryParam
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
            field : 'createTime',
            title : '生成时间',
            width : '10%',
            formatter : function(value, row, rowIndex) {
                var elements = "";
                if (row != null ) {
                	elements=$.dates.format(value)
                }
                return elements;
            }
        },{
            field : 'fundsSource',
            title : '理财机构',
            width : '6%'
        },{
            field : 'operate',
            title : '操作',
            width : '70%',
            formatter:function(value,row,index){
                if(row){
                    var elements = "";
                    /** 显示划拨申请书导出（Pdf）链接 **/
                    if(row.status1 == "2"){
                        elements +="<a href='#' class='applyPdf' id='"+ row.id +"'></a>";
                    }
                    /** 显示划拨申请书导出（Xls）链接 **/
                    if(row.status1 == "2"){
                        elements +="<a href='#' class='applyXls' id='"+ row.id +"'></a>";
                    }
                    /** 显示预拨确认书导出（pdf）链接 **/
                    if(row.status2 == "2"){
                        elements +="<a href='#' class='confirmPdf' id='"+ row.id +"'></a>";
                    }
                    /** 显示预拨确认书导出（xls）链接 **/
                    if(row.status2 == "2"){
                        elements +="<a href='#' class='confirmXls' id='"+ row.id +"'></a>";
                    }
                    /** 显示放款明细（Xls）导出链接 **/
                    if(row.status3 == "2"){
                        elements +="<a href='#' class='grantDetailXls' id='"+ row.id +"'></a>";
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
                    // 记录id
                    var id = $(this).attr("id");
                    $.messager.confirm("提示","确认导出划拨申请书（PDF）文件吗？",function(r){
                        if(r){
                            var params = {"id":id};
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
                    var id = $(this).attr("id");
                    $.messager.confirm("提示","确认导出划拨申请书（Xls）文件吗？",function(r){
                        if(r){
                            var params = {"id":id};
                            ajaxSubmit($.requestManagement.exportApplyXlsUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 预拨确认书导出（pdf） **/
            $(".confirmPdf").linkbutton({
                text:'预拨确认书导出（pdf）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // 债权批次号
                    var id = $(this).attr("id");
                    $.messager.confirm("提示","确认导出预拨确认书（pdf）文件吗？",function(r){
                        if(r){
                            var params = {"id":id};
                            ajaxSubmit($.requestManagement.exportConfirmPdfUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 预拨确认书导出（xls） **/
            $(".confirmXls").linkbutton({
                text:'预拨确认书导出（xls）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // id
                    var id = $(this).attr("id");
                    $.messager.confirm("提示","确认导出预拨确认书导出（xls）文件吗？",function(r){
                        if(r){
                            var params = {"id":id};
                            ajaxSubmit($.requestManagement.exportConfirmXlsUrl, params ,false);
                        }
                    });
                }
            });
            
            /** 放款明细导出（Xls） **/
            $(".grantDetailXls").linkbutton({
                text:'放款明细导出（Xls）',
                plain:true,
                iconCls:'download_1',
                onClick:function(){
                    // id
                    var id = $(this).attr("id");
                    $.messager.confirm("提示","确认导出放款明细导出（Xls）文件吗？",function(r){
                        if(r){
                            var params = {"id":id};
                            ajaxSubmit($.requestManagement.exportGrantDetailUrl, params ,false);
                        }
                    });
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
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.requestManagement.searchForm.form('reset');
    });
    
    /** 生成划拨申请书 **/
    $("#createBtn").click(function(){       
        $.messager.confirm("提示","确认生成划拨申请书吗？",function(r){
            if(r){
            	/** 获取查询表单数据转换成JSON对象 **/
                var searchMsg = $.requestManagement.searchForm.serialize();
                /** 对参数进行解码(显示中文) **/
                searchMsg = decodeURIComponent(searchMsg);
                /** 字符串转换为对象 **/
                var queryParam = $.serializeToJsonObject(searchMsg);
                if(queryParam.yuboAmt<=0){
	               	 $.messager.alert('提示','预拨资金需大于0!','warning');
	               	 return;
                }
                $.ajaxPackage({
                    type: "post",
                    dataType : 'json',
                    url: $.requestManagement.createApplication,
                    data:queryParam,
                    success: function (data) {
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        if(resCode !="000000"){
                            $.messager.alert('警告',resMsg,'warning');
                            return;
                        }
                        $.messager.alert('提示',resMsg,'info');
                        $.requestManagement.pg.page = 1;
                        $.requestManagement.reloadDataGrid();
                    },
                    error: function (data) {
                        $.messager.alert('警告','操作失败！','warning');
                    }
                });
            }
        });
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
    $(document).ready(function(){ 
    	$.requestManagement.pg.page = 1;
        $.requestManagement.reloadDataGrid();
	}); 
})