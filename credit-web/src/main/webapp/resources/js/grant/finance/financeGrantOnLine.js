/**
 * Created by ym10094 on 2016/11/8.
 */
$(function(){
    $.financeGrantOnLine = {
        /** 表格数据来源 **/
        dataGridUrl : global.contextPath+"/grant/finance/searchFinnaceGrantOnLine",
        /**导出地址**/
        exportUrl :global.contextPath+"/grant/finance/exportFinanceGrantOnLineExl",
        /**回退地址**/
        repealUrl :global.contextPath+"/grant/finance/financeGrantRepeal",
        /**放款地址**/
        grantApplyUrl : global.contextPath+"/grant/finance/financeGrantApply",
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
            if (!$.financeGrantOnLine.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.financeGrantOnLine.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.financeGrantOnLine.dataGridUrl;
            /** 查询并加载数据**/
            $.financeGrantOnLine.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.financeGrantOnLine.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }
    $.financeGrantOnLine.pg = {
        'page' : 1,
        'rows' : $.financeGrantOnLine.pageSize
    }
    $.financeGrantOnLine.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.financeGrantOnLine.pg,
        /** 提交方式 **/
        method : 'get',
        /** 是否显示行号 **/
        rownumbers : true,
        /** 是否单选 **/
        singleSelect : false,
        /** 是否可折叠的 **/
        collapsible : false,
        /** 自适应列宽 **/
        fitColumns : true,
        /** 设置true，表示适应它的父容器 **/
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        /** 禁止服务端排序 **/
        remoteSort:false,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        columns :[[
            //列定义
            {
                field : 'loanId',
                title : '债权ID',
                hidden :'true'
            },
            {
                field :'id',
                title :'id',
                checkbox:true
            },
            {
                field :'contractNum',
                title :'合同编号',
                width : '10%'
            },
            {
                field :'name',
                title :'姓名',
                width : '7%'
            },
            {
                field :'idNum',
                title :'身份证号码',
                width :'10%',
            },
            {
                field :'founsSource',
                title :'合同来源',
                width :'6%'
            },
            {
                field :'pactMoney',
                title :'合同金额',
                width :'7%',
                formatter:function(value,row,index){
                    if(value){
                        return $.comdify(value);
                    }
                }
            },
            {
                field :'grantMoney',
                title :'放款金额',
                width :'7%',
                formatter:function(value,row,index){
                    if(value){
                        return $.comdify(value);
                    }
                }
            },
            {
                field :'bankName',
                title :'所属银行',
                width :'8%'
            },
            {
                field :'branchBankName',
                title :'所属分行',
                width :'8%'
            },
            {
                field :'bankAccount',
                title :'银行卡号',
                width :'9%'
            },
            {
                field :'grantApplyDate',
                title :'申请时间',
                width :'8%',
                formatter : $.DateUtil.dateFormatToFullStr
            },
            {
                field :'grantState',
                title :'放款状态',
                width :'6%'
            },
            {
                field : 'opera',
                title : '操作',
                width : '10%',
                formatter : function(value, row, index) {
                    if (row) {
                        var element = "";
                        var grantDisabled = true;
                        if ((row.grantState == '未放款' || row.grantState == '放款失败') && row.founsSource != '陆金所') {
                            grantDisabled = false;
                        }
                        if ($.type(row.id) == "undefined") {
                            row.id = null;
                        }
                        element = "<a href='#' class='easyui-linkbutton grant' data-options='disabled:"+grantDisabled
                                + "' data-loan-id='" + row.loanId +"' founsSource='"+row.founsSource+"' batchNum='"+row.batchNum+"' contractNum='"+row.contractNum+"'>放款</a>"
                                + "<a href='#' class='easyui-linkbutton search' data-loan-id='" + row.loanId
                                + "' data-app-no='" + row.appNo + "'>查询详情</a>";
                        return element;
                    }
                }
            }
        ]],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.financeGrantOnLine.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.financeGrantOnLine.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess :function(data){
            $(".easyui-linkbutton.grant").linkbutton({
                plain:true,
                /*iconCls:'icon-money_3',*/
                iconCls:'pic_36',
                onClick :function(){
                    if(!$(this).linkbutton("options").disabled) {
                        var loanIds = $(this).attr("data-loan-id");
                        var founsSource = $(this).attr("founsSource");
                        var batchNum = $(this).attr("batchNum");
                        var contractNum = $(this).attr("contractNum");
                        /*if((founsSource == '渤海2' || founsSource == '华瑞渤海') && (batchNum==null || batchNum=='undefined')){
                        	$.messager.alert('警告',contractNum+"借款未生成批次号，不可进行放款操作。",'warning');
                        	return;
                        }*/
                        $.messager.confirm("提示", "确认申请放款吗？", function (r) {
                            if (r) {
                                var params = {"loanIds":loanIds};
                                ajaxSubmit($.financeGrantOnLine.grantApplyUrl, params);
                            }
                        });
                    }
                }
            });
            
            $(".easyui-linkbutton.search").linkbutton({
                plain :true,
                iconCls :'icon-search',
                onClick : function(){
                    var loanId = $(this).attr("data-loan-id");
                    $.grantDetails.grantDetailsWin.window('open');
                    $.grantDetails.pg.page = 1;
                    $.grantDetails.grantLoanId = loanId;
                    $.grantDetails.pager.pagination('refresh',{
                        pageNumber:$.grantDetails.pg.page,
                        pageSize:$.grantDetails.pg.pageSize
                    });
                    $.grantDetails.reloadDataGrid();
                }
            })
            
            /** 页面自适应 **/
            $.financeGrantOnLine.dataGrid.datagrid('resize');
        }
    })

    /** 表格分页组件 **/
    $.financeGrantOnLine.pager = $.financeGrantOnLine.dataGrid.datagrid('getPager');
    $.financeGrantOnLine.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.financeGrantOnLine.pg.page = pageNumber;
            $.financeGrantOnLine.pg.rows = pageSize;
            $.financeGrantOnLine.reloadDataGrid();
        }
    });

    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.financeGrantOnLine.pg.page = 1;
        $.financeGrantOnLine.reloadDataGrid();
    });
    
    /** 重置 **/
    $("#clearBtn").bind("click", function(envent) {
        if (!$(this).linkbutton("options").disabled) {
            $("#searchForm").form("reset");;
        }
    });

    /**导出**/
    $("#exportBtn").click( function () {
        if (!$.financeGrantOnLine.validate()) {
            return;
        }
        $.messager.confirm("提示", "确认导出放款申请结果文件吗？", function (r) {
            if (r) {
                exportSubmit($.financeGrantOnLine.exportUrl);
            }
        });
        
    });

    /** 放款提交 **/
    $('#grantBtn').click(function(){
        var rows = $.financeGrantOnLine.dataGrid.datagrid('getSelections');
        if(rows.length <= 0){
            $.messager.alert('警告',"请选择需申请放款的记录！",'warning');
            return ;
        }
        var loanIds = [];
        var source = rows[0].founsSource;
        for(var i=0;i<rows.length;i++){
            var state = rows[i].grantState;
            var founsSource = rows[i].founsSource;
            var batchNum = rows[i].batchNum;
            var contractNum = rows[i].contractNum;
            if(founsSource == '陆金所'){
            	$.messager.alert('警告',"合同来源为陆金所债权不需进行放款操作！",'warning');
            	return;
            }
            if(state == '未放款' || state == '放款失败'){
                loanIds.push(rows[i].loanId);
            }
            if(source != founsSource){
            	$.messager.alert('警告',"合同来源不一致，无法进行批量放款。请选择相同合同来源的债权进行批量放款！",'warning');
                return ;
            }
        }
        if(loanIds.length <= 0){
            $.messager.alert('警告',"请至少选择一条未放款或者放款失败的记录！",'warning');
            return ;
        }
        var tipMessage='您已选择了：' + loanIds.length + '条记录，确认申请放款吗？';
        top.$.messager.confirm('提示', tipMessage, function(r){
            if (r){
                var params = {"loanIds":loanIds.join()};
                ajaxSubmit($.financeGrantOnLine.grantApplyUrl,params);
            }
        });
    });
    
    /** 导出文件 **/
    function exportSubmit(url){
        console.log("url："+url);
        var params = {};
        var searchMsg = $.financeGrantOnLine.searchForm.serialize();
        searchMsg = decodeURIComponent(searchMsg);
        params = $.serializeToJsonObject(searchMsg);
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

    /**
     * 异步
     * @param url
     * @param params
     */
    function ajaxSubmit(url,params){
        $.ajaxPackage({
            url:url,
            type:"post",
            data:params,
            dataType:"json",
            timeout : 300000,
            success:function(response, textStatus, jqXHR){
                var resCode = response.resCode;
                var resMsg = response.resMsg;
                if(resCode == "000000"){
                    $.messager.alert("提示",resMsg, "info");
                    // 刷新页面
                    $.financeGrantOnLine.reloadDataGrid();
                }else{
                    $.messager.alert("警告", resMsg, "warning");
                }
            },
            error:function(response, textStatus, jqXHR){
                $.messager.alert("异常", "操作失败", "error");
            },
            complete:function(jqXHR,textStatus){
            }
        });
    }
})