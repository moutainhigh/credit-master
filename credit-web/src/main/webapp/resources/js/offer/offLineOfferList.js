$(function() {
    $.offLineOfferList = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/offLineOffer/offerList/search',
        /** 导出报盘文件地址 **/
        exportOfferUrl : global.contextPath + '/offLineOffer/offerList/exportOffer',
        /** 线下还款报盘实时划扣url **/
        realTimeDeductUrl : global.contextPath + '/offLineOffer/offerList/realTimeDeduct',
        /** 线下还款关闭报盘url **/
        closeOfferUrl : global.contextPath + '/offLineOffer/offerList/closeOffer',
        /** 查看报盘明细url **/
        viewDetailUrl : global.contextPath + "/offer/offerInfo/offerInfoDetail",
        /** 数据信息表格 **/
        dataGrid : $('#dataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 实时划扣窗口 **/
        realTimeDeductWin : $('#realTimeDeductWin'),
        /** 实时划扣窗口 **/
        dataForm : $('#dataForm'),
        /** 是否有导出线下还款报盘文件权限 **/
        isCanExportOffer : $("#isCanExportOffer").val(),
        /** 是否有实时划扣权限 **/
        isCanRealTimeDeduct : $("#isCanRealTimeDeduct").val(),
        /** 是否有关闭报盘权限 **/
        isCanCloseOffer : $("#isCanCloseOffer").val(),
        /** 每页显示的记录条数，默认为10 * */
        pageSize : 10,
        /** 设置每页记录条数的列表 * */
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载表格数据 * */
        reloadDataGrid : function() {
            if (!$.offLineOfferList.searchForm.form("validate")) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 * */
            var searchMsg = $.offLineOfferList.searchForm.serialize();
            /** 对参数进行解码(显示中文) * */
            searchMsg = decodeURIComponent(searchMsg);
            var queryParam = $.serializeToJsonObject(searchMsg);
            queryParam.url = $.offLineOfferList.dataGridUrl;
            $.offLineOfferList.dataGrid.datagrid('reloadData', queryParam);
        }
    };

    /** 分页参数（page:当前第N页，rows:一页N行） * */
    $.offLineOfferList.pg = {
        'page' : 1,
        'rows' : $.offLineOfferList.pageSize
    };

    $.offLineOfferList.dataGrid.datagrid({
        pg : $.offLineOfferList.pg,
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
        /** 自适应父窗口 * */
        fit : true,
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        /** 是否开启分页 * */
        pagination : true,
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
                /** 列定义 * */
                {
                    field : 'id',
                    title : 'ID',
                    hidden : true
                },
                {
                    field : 'loanId',
                    title : 'loanId',
                    hidden : true
                },
                {
                    field : 'offerDate',
                    title : '报盘日期',
                    width : 6,
                    formatter : $.DateUtil.dateFormatToStr
                },
                {
                    field : 'name',
                    title : '借款人',
                    width : 6
                },
                {
                    field : 'contractNum',
                    title : '合同编号',
                    width : 8
                },
                {
                    field : 'bankName',
                    title : '银行',
                    width : 8
                },
                {
                    field : 'bankAcct',
                    title : '账号',
                    width : 9
                },
                {
                    field : 'amount',
                    title : '报盘金额',
                    width : 6,
                    vType : 'rmb'
                },
                {
                    field : 'actualAmount',
                    title : '回盘金额',
                    width : 6,
                    vType : 'rmb'
                },
                {
                    field : 'updateTime',
                    title : '最后变更日期',
                    width : 8,
                    formatter : $.DateUtil.dateFormatToFullStr
                },
                {
                    field : 'state',
                    title : '状态',
                    width : 6
                },
                {
                    field : 'type',
                    title : '划扣方式',
                    width : 5
                },
                {
                    field : 'paySysNo',
                    title : '划扣通道',
                    width : 6
                },
                {
                    field : 'exportState',
                    title : '导出状态',
                    width : 5
                },
                {
                    field : 'memo',
                    title : '备注',
                    width : 10
                },
                {
                    field : 'operate',
                    title : '操作',
                    width : 15,
                    formatter : function(value, row, index) {
                        var elements = "";
                        var offerDate = $.DateUtil.dateFormatToStr(row.offerDate);
                        var sysdate = formatDate(new Date());
                        if (row != null && row.id != null && row.state != '已关闭' && offerDate == sysdate) {
                            if ($.offLineOfferList.isCanRealTimeDeduct =="true" && row.state != '已报盘') {
                                elements += "<a href='#' class='realTimeDeduct' loanId='"
                                        + row.loanId
                                        + "' offerId='"
                                        + row.id
                                        + "' amount='"
                                        + row.amount
                                        + "' actualAmount='"
                                        + row.actualAmount
                                        + "' state='"
                                        + row.state
                                        + "' name='"
                                        + row.name
                                        + "' idnum='"
                                        + row.idnum
                                        + "' contractNum='"
                                        + row.contractNum + "'></a>";
                            }
                            if ($.offLineOfferList.isCanCloseOffer =="true") {
                                if (row.state == '未报盘' || row.oldState == '已回盘非全额') {
                                    elements += "<a href='#' class='closeOffer' loanId='"
                                        + row.loanId
                                        + "' offerId='"
                                        + row.id
                                        + "' oldState='"
                                        + row.oldState
                                        + "' amount='"
                                        + row.amount
                                        + "' actualAmount='"
                                        + row.actualAmount
                                        + "' state='"
                                        + row.state
                                        + "' name='"
                                        + row.name
                                        + "' idnum='"
                                        + row.idnum
                                        + "' contractNum='"
                                        + row.contractNum
                                        + "' ></a>";
                                }
                            }
                        }
                        if (row != null && row.id != null) {
                            elements += "<a href='#' class='viewDetail' loanId='"
                                    + row.loanId
                                    + "' offerId='"
                                    + row.id
                                    + "' name='"
                                    + row.name
                                    + "' isThird='"
                                    + row.isThird
                                    + "' fundsSources='"
                                    +row.fundsSources
                                    + "' ></a>";
                        }
                        return elements;
                    }
                } 
            ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.offLineOfferList.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.offLineOfferList.pageSizeList,
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess : function(data) {
            /** 查看详情 **/
            $(".viewDetail").linkbutton({
                text : '查看报盘详情',
                plain : true,
                iconCls : 'icon-search',
                onClick : function() {
                    var offerId = $(this).attr("offerId");
                    var name = $(this).attr("name");
                    var isThird = $(this).attr("isThird");
                    var fundsSources = $(this).attr("fundsSources");
                    top.$.iframeTabs.add({
                        id : "offerDetail_viewDetail_" + offerId,
                        text : "报回盘明细("+ name + ")",
                        iconCls : "pic_1",
                        url : $.offLineOfferList.viewDetailUrl +"?offerId=" + offerId + "&isThird=" + isThird+"&fundsSources="+fundsSources
                    });
                }
            });
            
            /** 关闭报盘 **/
            $(".closeOffer").linkbutton({
                text : '关闭',
                plain : true,
                iconCls : 'icon-cancel',
                onClick : function() {
                    var offerId = $(this).attr("offerId");
                    var tipMessage = '确认关闭该报盘记录吗？';
                    $.messager.confirm("提示",tipMessage,function(r) {
                        if (r) {
                            $.ajaxPackage({
                                type : 'get',
                                url : $.offLineOfferList.closeOfferUrl + "?offerId=" + offerId,
                                dataType : "json",
                                success : function(data,textStatus,jqXHR) {
                                    var resCode = data.resCode;
                                    var resMsg = data.resMsg;
                                    if (resCode != '000000') {
                                        $.messager.alert('警告',resMsg,'warning');
                                        return;
                                    }
                                    $.messager.alert('提示',"操作成功！","info");
                                    // 刷新页面
                                    $.offLineOfferList.reloadDataGrid();
                                },
                                error : function(XMLHttpRequest,textStatus,errorThrown,d) {
                                    $.messager.alert('异常信息',textStatus + ' : ' + errorThrown + '!','error');
                                },
                                complete : function() {
                                }
                            });
                        }
                    });
                }
            });
            
            /** 实时划扣 **/
            $(".realTimeDeduct").linkbutton({
                text : '实时划扣',
                plain : true,
                iconCls : 'icon-ok',
                onClick : function() {
                    // 报盘状态
                    var state = $(this).attr("state");
                    if (state == '已报盘') {
                        $.messager.alert('警告','该记录未回盘,不能进行实时划扣！', 'warning');
                        return;
                    }
                    var contractNum = $(this).attr("contractNum");
                    var name = $(this).attr("name");
                    var amount = $(this).attr("amount");
                    var json = {"name":name,"contractNum":contractNum,"offerAmount":amount};
                    $.offLineOfferList.dataForm.form('clear');
                    $.offLineOfferList.dataForm.form("load", json);
                    // 借款人输入框设置只读
                    $("#borrowerName").textbox({'editable':false});
                    // 合同编号输入框设置只读
                    $("#offerContractNum").textbox({'editable':false});
                    $.offLineOfferList.realTimeDeductWin.window('open');
                }
            });
            /** 页面自适应 **/
            $.offLineOfferList.dataGrid.datagrid('resize');
        }
    });
    
    /** 分页组件初始化 **/
    $.offLineOfferList.pager = $.offLineOfferList.dataGrid.datagrid('getPager');
    $.offLineOfferList.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.offLineOfferList.pg.page = pageNumber;
            $.offLineOfferList.pg.rows = pageSize;
            $.offLineOfferList.reloadDataGrid();
        }
    });

    /** 实时划扣窗口初始化 **/
    $.offLineOfferList.realTimeDeductWin.window({
        // 窗口长度
        width : 400,
        // 窗口宽度
        height : 250,
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
    });
    
    /** 查询事件 **/
    $('#searchBtn').click(function() {
        $.offLineOfferList.pg.page = 1;
        $.offLineOfferList.reloadDataGrid();
    });
    
    /** 线下还款导出报盘文件 **/
    $("#exportOfferBtn").click(function() {
        if (!$.offLineOfferList.searchForm.form("validate")) {
            return;
        }
        $.messager.confirm("提示","确认导出线下还款报盘文件吗？",function(r){
            if(r){
                submitForm($.offLineOfferList.searchForm, $.offLineOfferList.exportOfferUrl, true);
            }
        });
    });
    
    /** 线下还款报盘实时划扣 **/
    $("#realTimeDeductBtn").click(function(){
        $.offLineOfferList.dataForm.form('clear');
        // 借款人输入框设置可编辑
        $("#borrowerName").textbox({'editable':true});
        // 合同编号输入框设置可编辑
        $("#offerContractNum").textbox({'editable':true});
        $.offLineOfferList.realTimeDeductWin.window('open');
    });
    
    /** 提交实时划扣请求 **/
    $("#submitDeductBtn").click(function() {
        // 检查表单项是否通过验证
        if (!$.offLineOfferList.dataForm.form('validate')) {
            return;
        }
        // 划扣金额
        var offerAmount = $('#offerAmount').val();
        if (Number(offerAmount) < 0) {
            $.messager.alert('警告', '报盘金额必须大于零！', 'warning');
            return;
        }
        $.messager.confirm("提示","确认实时划扣吗？",function(r){
            // 提交到服务端，进行处理
            $.ajaxPackage({
                type : 'post',
                url : $.offLineOfferList.realTimeDeductUrl,
                data : $.offLineOfferList.dataForm.serialize(),
                dataType : "json",
                success : function(data) {
                    var resCode = data.resCode;
                    var resMsg = data.resMsg;
                    if (resCode != '000000') {
                        $.messager.alert('警告', resMsg, 'warning');
                        return;
                    }
                    // 操作成功 重新加载列表数据
                    $.messager.alert('提示', resMsg, "info");
                    $.offLineOfferList.realTimeDeductWin.window('close');
                    $.offLineOfferList.reloadDataGrid();
                },
                error : function(XMLHttpRequest, textStatus,errorThrown, d) {
                    $.messager.alert('异常信息', textStatus + ' : ' + errorThrown + '!', 'error');
                },
                complete : function() {
                }
            });
        });
    });
    
    /**  关闭实时划扣窗口 **/
    $("#closeBtn").click(function(){
        if($.offLineOfferList.realTimeDeductWin){
            $.offLineOfferList.realTimeDeductWin.window('close');
        }
    });

    /** 重置处理 **/
    $("#clearBtn").click(function(){
        if (!$(this).linkbutton("options").disabled) {
            $.offLineOfferList.searchForm.form("reset");
        }
    });
    
    /** 提交表单 **/
    function submitForm(form,url,isRefresh){
        $.downloadFile({
            url:url,
            isDownloadBigFile:true,
            params:form.serializeObject(),
            successFunc:function(data){
                if(data== null){
                    $.messager.alert('提示','下载成功！','info');
                    if(isRefresh){
                        // 刷新页面
                        $.offLineOfferList.reloadDataGrid();
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
    
    /** 日期格式化 **/
    function formatDate (date) {
        var month = date.getMonth() + 1;
        if(month < 10){
            month = "0"+month;
        }
        var day = date.getDate();
        if(day < 10){
            day = "0" + day;
        }
        return date.getFullYear()+"-"+ month+"-"+ day;
    }
    
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
    });
    
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
    // 加载银行下拉框信息
    initBankInfoData();
    /**===================开户银行=========================**/
});
