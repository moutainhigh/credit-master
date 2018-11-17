$(function() {
    $.offLineOfferReturn = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/offLineOffer/offerReturn/search',
        /** 导入回盘信息url **/
        importUrl : global.contextPath + '/offLineOffer/offerReturn/importReturnOffer',
        /** 导出回盘信息url **/
        exportUrl : global.contextPath + '/offLineOffer/offerReturn/exportReturnOffer',
        /** 数据信息表格 **/
        dataGrid : $('#dataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 上传文件窗口 **/
        importExcelWin : $("#importExcelWin"),
        /** 回盘批量导入表单对象 **/
        baseFileForm : $("#baseFileForm"),
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if(!$.offLineOfferReturn.searchForm.form("validate")){
                return false;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.offLineOfferReturn.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数 **/
            queryParam.url = $.offLineOfferReturn.dataGridUrl;
            /** 查询处理 **/
            $.offLineOfferReturn.dataGrid.datagrid('reloadData', queryParam);
        }
    };

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.offLineOfferReturn.pg = {
        'page' : 1,
        'rows' : $.offLineOfferReturn.pageSize
    };

    /** 网格数据对象初始化 **/
    $.offLineOfferReturn.dataGrid.datagrid({
        pg : $.offLineOfferReturn.pg,
        /** 提交方式 * */
        method : 'get',
        /** 是否显示行号 * */
        rownumbers : true,
        /** 是否单选 * */
        singleSelect : true,
        /** 是否可折叠的 * */
        collapsible : false,
        /** 自适应列宽 * */
        fitColumns : true,
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        fit : true,
        /** 是否开启分页 * */
        pagination : true,
        /** 加载数据提示 * */
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            /** 列定义 * */
            {
                field : 'id',
                title : 'id',
                hidden : true
            },{
                field : 'loanId',
                title : 'loanId',
                hidden : true
            }, {
                field : 'contractNum',
                title : '合同编号',
                width : '8%'
            }, {
                field : 'fundsSources',
                title : '合同来源',
                width : '6%'
            }, {
                field : 'name',
                title : '借款人',
                width : '6%'
            },{
                field : 'idNum',
                title : '身份证号',
                width : '9%'
            },{
                field : 'bankName',
                title : '银行',
                width : '9%'
            },{
                field : 'bankAcct',
                title : '账号',
                width : '9%'
            },{
                field : 'reqTime',
                title : '报盘日期',
                width : '6%',
                formatter: $.DateUtil.dateFormatToStr
            }, {
                field : 'payAmount',
                title : '报盘金额',
                width : '5%',
                vType : 'rmb'
            },{
                field : 'actualAmount',
                title : '回盘金额',
                width : '5%',
                vType : 'rmb'
            },{
                field : 'rspReceiveTime',
                title : '回盘日期',
                width : '6%',
                formatter: $.DateUtil.dateFormatToStr
            },{
                field : 'type',
                title : '划扣方式',
                width : '5%',
                formatter:function(value,row,index){
                    if(value){
                        if($.isEmpty(value)){
                            return "自动划扣";
                        }
                        return value;
                    }
                }
            },{
                field : 'paySysNo',
                title : '划扣通道',
                width : '7%'
            },{
                field : 'rtnCode',
                title : '划扣状态',
                width : '8%'
            },{
                field : 'memo',
                title : '备注',
                width : '10%'
            }
        ]],
            
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.offLineOfferReturn.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.offLineOfferReturn.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 **/
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        /** 加载数据完成后的回调方法 **/
        onLoadSuccess : function(data) {
            $.offLineOfferReturn.dataGrid.datagrid('resize');
        }
    });

    /** 分页处理 **/
    $.offLineOfferReturn.pager = $.offLineOfferReturn.dataGrid.datagrid('getPager');
    $.offLineOfferReturn.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.offLineOfferReturn.pg.page = pageNumber;
            $.offLineOfferReturn.pg.rows = pageSize;
            $.offLineOfferReturn.reloadDataGrid();
        }
    });
    
    /** 查询处理 **/
    $('#searchBtn').click(function() {
        $.offLineOfferReturn.pg.page = 1;
        $.offLineOfferReturn.reloadDataGrid();
    });
    
    /** 查询校验 **/
    function searchCheck(){
        if(!$.offLineOfferReturn.searchForm.form("validate")){
            return false;
        }
        // 姓名
        var name = $.trim($("#name").val());
        // 证件号码
        var idNum = $.trim($("#idNum").val());
        // 报盘日期（开始日期）
        var startOfferDate = $("#startOfferDate").datebox("getValue");
        // 报盘日期（截止日期）
        var endOfferDate = $("#endOfferDate").datebox("getValue");
        // 是否成功
        var returnCode = $("#returnCode").combobox("getValue");
        // 合同来源
        var fundsSource = $("#fundsSource").combobox("getValue");
        // 查询条件必须输入其中一个
        if ($.isEmpty(name) 
            && $.isEmpty(idNum)
            && $.isEmpty(startOfferDate) 
            && $.isEmpty(endOfferDate)
            && $.isEmpty(returnCode) 
            && $.isEmpty(fundsSource)) {
            $.messager.alert('警告', '请至少输入一个查询条件!', 'warning');
            return false;
        }
        var beginDate = new Date(startOfferDate.replace(/\-/g, "\/"));
        var endDate = new Date(endOfferDate.replace(/\-/g, "\/"));
        if(!$.isEmpty(startOfferDate) && !$.isEmpty(endOfferDate) && beginDate > endDate){
            $.messager.alert('警告','报盘日期（开始时间）不能大于报盘日期（截止时间）！','warning');
            return false;
        }
        // 防止输入空白查询
        $("#name").val(name);
        $("#idNum").val(idNum);
        return true;
    }

    /** 重置处理 **/
    $("#clearBtn").click(function() {
        if (!$(this).linkbutton("options").disabled) {
            $.offLineOfferReturn.searchForm.form("reset");
        }
    });
    
    /** 打开导入窗口 **/
    $('#importBtn').click(function(){
        $.offLineOfferReturn.importExcelWin.window('open');
        $.offLineOfferReturn.baseFileForm.form('clear');
    });
    
    /** 线下还款回盘信息批量导入 **/
    $("#importReturnOfferBtn").click(function(){
        var file = $("#returnOfferFile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件！','warning');
            return;
        }
        $.messager.confirm("提示","确认导入回盘信息吗？",function(r){
            if(r){
                $.offLineOfferReturn.baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: $.offLineOfferReturn.importUrl,
                    hasDownloadFile : true,
                    success: function () {
                        $.messager.alert('提示','操作完成！','info');
                        setTimeout(function(){
                            $.offLineOfferReturn.importExcelWin.window('close');
                            $.offLineOfferReturn.reloadDataGrid();
                        }, 1000);
                    },
                    error: function (data) {
                        $.messager.alert('警告',data.resMsg,'warning');
                    }
                });
            }
        });
    });
    
    /** 导出回盘处理 **/
    $("#exportBtn").click(function(){
        if(!$.offLineOfferReturn.searchForm.form("validate")){
            return;
        }
        $.messager.confirm("提示","最大可导出50000条记录，请确认要导出Excel文件吗？",function(r){
            if(r){
                $.downloadFile({
                    url:$.offLineOfferReturn.exportUrl,
                    isDownloadBigFile:true,
                    params:$.offLineOfferReturn.searchForm.serializeObject(),
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
    
    /**===================开户银行=========================**/
    /** 从服务端获取银行数据,将数据填充到前端下拉框 **/
    /** 开户银行下拉框参数定义 **/
    $('#bankCode').combobox({
        valueField : 'id',
        textField : 'text',
        //panelHeight : 'auto',
        filter : function(q,row) {
            var opts=$(this).combobox("options");
            return row[opts.textField].indexOf(q)>-1;
        },
        formatter : function(row) {
            var opts=$(this).combobox("options");
            return row[opts.textField];
        }
    })
    function initBankInfoData() {
        $.ajaxPackage({
            type : 'post', 
            url : global.contextPath + '/offer/offerBankDic/getBankInfo',
            isShowLoadMask : false,
            dataType : "json",
            success : function (data) { 
                /** data 服务端返回数据 **/
                data.unshift({"id":"0","text":"全部"});
                $('#bankCode').combobox('loadDataExt',data);
                $('#bankCode').combobox('defaultOneItem');
            },
            error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                $.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
            },
            complete : function() {
                
            }
        });
    }
    initBankInfoData();
    /**===================开户银行=========================**/
});
