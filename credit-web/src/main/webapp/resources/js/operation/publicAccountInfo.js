$(function() {
    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/operation/searchPublicAccountInfo';
    
    // 对公账户批量导入url
    var batchImportUrl = global.contextPath + '/operation/batchImportPublicAccountInfo';
    
    // 对公账户车企贷批量导入url
    var batchImportCarLoanUrl = global.contextPath + '/operation/batchImportCarLoanAccountInfo';
    
    // 对公账户导出已认领结果url
    var exportUrl = global.contextPath + '/operation/exportPublicAccountReceiveInfo';
    
    // 对公账户导出车企贷已认领结果url
    var exportCarLoanUrl = global.contextPath + '/operation/exportCarLoanAccountReceiveInfo';
    
    // 导出证方已认领结果url
    var exportBusinessUrl = global.contextPath + '/operation/exportBusinessAccountReceiveInfo';
    
    // 对公账户导出查询结果url
    var exportSearchResultUrl = global.contextPath + '/operation/exportAccountSearchResult';
    
    // 对公账户导出还款认领情况表url
    var exportRepayReceiveUrl = global.contextPath + '/operation/exportPublicAccountRepayReceiveInfo';
    
    // 领取校验url
    var receiveUrl = global.contextPath + '/operation/publicAccountReceiveInfo';
    
    // 领取url
    var receiveInitUrl = global.contextPath + '/operation/publicAccountReceiveInfoInit';
    
    // 撤销url
    var cancelUrl = global.contextPath + '/operation/cancelReceive';
    
    // 渠道确认url
    var channelConfirmUrl = global.contextPath + '/operation/channelConfirm';
    
    // 加载对公账户领取时间url
    var loadReceiveTimeUrl = global.contextPath + '/operation/loadReceiveTime';
    
    // 修改对公账户领取时间url
    var updateReceiveTimeUrl = global.contextPath + '/operation/updateReceiveTime';
    
    // 查询对公还款操作日志信息url
    var searchLogInfoUrl = global.contextPath + '/operation/searchLogInfo';
    
    // 对公账户批量导入url（新模板）
    var newImportUrl = global.contextPath + '/operation/newTemplateImportPublicAccountInfo';
    
    // 对公账户车企贷批量导入url（新模板）
    var newImportCarLoanUrl = global.contextPath + '/operation/newTemplateImportCarLoanAccountInfo';
    
    // 证方批量导入url（新模板）
    var newImportBusinessUrl = global.contextPath + '/operation/newTemplateImportBusinessAccountInfo';
    
    // 表格实例对象
    var publicAccountDataGrid = $('#publicAccountDataGrid');
    
    // 查询条件数据项表单实例对象
    var searchForm = $('#searchForm');
    
    // 上传文件面板
    var importExcelWin = $("#importExcelWin");
    
    // 对公账户批量导入表单对象
    var baseFileForm = $("#baseFileForm");
    
    // 车企贷批量导入表单对象
    var carFileForm = $("#carFileForm");
    
    // 修改领取时间面板对象
    var updateDataPanel = $("#updateDataPanel");
    
    // 修改领取时间表单对象
    var dataForm = $("#dataForm");
    
    // 对公、车企贷批量导入窗口对象
    var newImportExcelWin = $("#newImportExcelWin");
    
    // 对公批量导入表单对象
    var newCreditFileForm = $("#newCreditFileForm");
    
    // 车企贷批量导入表单对象
    var newCarFileForm = $("#newCarFileForm");
    
    // 企贷批量导入表单对象
    var newBusinessFileForm = $("#newBusinessFileForm");
    
    // 每页显示的记录条数，默认为10
    var pageSize = 10;
    
    // 设置每页记录条数的列表
    var pageSizeList = [ 10, 20, 30, 40, 50 ];
    
    // 定义表格参数
    $.pg = {'page' : 1,'rows' : pageSize };
    
    // 还款日期（起）添加清除按钮
    $("#repayDateStart").datebox("addClearButton","icon-clear");
    
    // 还款日期（止）添加清除按钮
    $("#repayDateEnd").datebox("addClearButton","icon-clear");
    
    // 认领日期（起）添加清除按钮
    $("#recTimeStart").datebox("addClearButton","icon-clear");
    
    // 认领日期（止）添加清除按钮
    $("#recTimeEnd").datebox("addClearButton","icon-clear");

    publicAccountDataGrid.datagrid({
        pg : $.pg,
        // 查询url
        //url:dataGridUrl,
        // 提交方式
        method : 'get',
        // 是否显示行号
        rownumbers : true,
        // 是否单选
        singleSelect : true,
        //是否可折叠的
        collapsible : false,
        // 自适应列宽
        fitColumns : true,
        // 设置成false的情况下，如果数据长度超出，则自动换行 
        nowrap : false,
        // 自适应父窗口
        fit : true,
        // 是否开启分页
        pagination : true,
        columns : [ [
        // 列定义
        {
            field : 'repayDate',
            title : '交易日期',
            width : '6%',
            formatter:function(value,row,index){
                return formatDate(value);
            }
        },{
            field : 'time',
            title : '交易时间',
            width : '5%'
        },{
            field : 'firstAccount',
            title : '本方账号',
            width : '9%'
        },{
            field : 'secondAccount',
            title : '对方账号',
            width : '9%'
        },{
            field : 'type',
            title : '借/贷',
            width : '3%'
        },{
            field : 'amount',
            title : '金额',
            width : '5%',
            formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }
        },{
            field : 'voucherNo',
            title : '凭证号',
            width : '5%'
        },{
            field : 'secondCompany',
            title : '对方单位',
            width : '8%'
        },{
            field : 'secondBank',
            title : '对方行号',
            width : '5%'
        },{
            field : 'purpose',
            title : '用途',
            width : '8%'
        },{
            field : 'remark',
            title : '摘要',
            width : '8%'
        },{
            field : 'comments',
            title : '附言',
            width : '7%'
        },{
            field : 'status',
            title : '状态',
            width : '5%'
        },{
            field : 'id',
            title : '操作',
            width : '16%',
            formatter:function(value,row,index){
                if(value){
                    var elements = "";
                    var status = row.status;
                    var recOperatorId = row.recOperatorId;
                    var userCode = $("#userCode").val();
                    var employeeType = $("#employeeType").val();
                    var userId = $("#userId").val();
                    // 是否有领取权限
                    var isCanConfirmReceive = $("#isCanConfirmReceive").val();
                    // 是否有渠道确认权限
                    var isCanChannelConfirm = $("#isCanChannelConfirm").val();
                    // 是否有撤销权限
                    var isCancelReceive = $("#isCancelReceive").val();
                    // 是否可以查看日志
                    var isSearchLog = $("#isSearchLog").val();
                    if(status=="未认领" && isCanConfirmReceive=="true"){
                        elements+="<a href='javascript:void(0)' class='receive' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    if(recOperatorId==userId && (status=="已认领" || status=="渠道确认") && isCancelReceive=="true"){
                        elements+="<a href='javascript:void(0)' class='cancel' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    if(isSearchLog=="true"){
                        elements+="<a href='javascript:void(0)' class='log' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    if(status=="未认领" && isCanChannelConfirm=="true"){
                        elements+="<a href='javascript:void(0)' class='channelConfirm' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    return elements;
                }
            }
        }] ],
        // 每页显示的记录条数，默认为10
        pageSize : pageSize,
        // 可以设置每页记录条数的列表
        pageList : pageSizeList,
        // 工具条
        toolbar : "#tb",
        // 页脚工具条
        //footer  : "#footer",
        // 自定义行样式
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
                
            }
        },
        onLoadSuccess:function(data){
            $(".receive").linkbutton({
                text:'领取',
                plain:true,
                iconCls:'pic_36',
                onClick:function(){
                    var id = $(this).attr("id");
                    $.ajaxPackage({
                        url:receiveUrl,
                        type:"get",
                        data:{"id":id},
                        dataType:"json",
                        success:function(response, textStatus, jqXHR){
                            var resCode = response.resCode;
                            var resMsg = response.resMsg;
                            if(resCode != "000000"){
                                $.messager.alert("警告", resMsg, "warning");
                                return;
                            }
                            var row = $.dataGrid.getSelectedRow(publicAccountDataGrid);
                            if (!row) {
                                $.messager.alert('警告','请选中待认领的对公还款记录！','warning');
                                return;
                            }
                            /** 选中待认领对公还款记录 **/
                            $.publicRec.recRow = row;
                            /** 清空表单数据 **/
                            $.publicRec.recSearchForm.form("clear");
                            /** 打开对公还款认领窗口 **/
                            $.publicRec.showRectWin.window('open');
                            /** 加载表单数据 **/
                            var tradeDate = $.DateUtil.dateFormatToStr(row.repayDate);
                            var tradeAmount = $.comdify(row.amount);
                            $("#recTradeDate").text(tradeDate);
                            $("#recTradeAmount").text(tradeAmount);
                            $("#recStatus").text(row.status);
                            $.publicRec.recSearchForm.form("load",{"accountId":row.id});
                            /** 表格数据初始化 **/
                            $.publicRec.recDataGrid.datagrid("clearData");
                        },
                        error:function(response, textStatus, jqXHR){
                            $.messager.alert("异常", "操作失败", "error");
                        },
                        complete:function(jqXHR,textStatus){
                        }
                    });
                    /*parent.$.iframeTabs.add({
                        id:"receive-"+id,
                        text:"领取还款记录",
                        url:receiveUrl+"?id="+id
                    });*/
                }
            });
            
            $(".cancel").linkbutton({
                text:'撤销',
                plain:true,
                iconCls:'icon-cancel',
                onClick:function(){
                    var id = $(this).attr("id");
                    var loanId = $(this).attr("loan_id");
                    $.messager.confirm("提示","您确定撤销吗？",function(r){
                        if(r){
                            $.ajaxPackage({
                                url:cancelUrl,
                                type:"post",
                                data:{"id":id,"loanId":loanId},
                                dataType:"json",
                                success:function(response, textStatus, jqXHR){
                                    var resCode = response.resCode;    
                                    var resMsg = response.resMsg; 
                                    if(resCode == "000000"){
                                        $.messager.alert("提示", resMsg, "info");
                                        reloadDataGrid();
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
                    });
                }
            });
            
            $(".log").linkbutton({
                text:'日志',
                plain:true,
                iconCls:'pic_53',
                onClick:function(){
                    var id = $(this).attr("id");
                    var url = searchLogInfoUrl;
                    url = url +"?objectId="+id;
                    url = url +"&logNames=PublicAccountReceiveInfoController&logNames=PublicAccountInterfaceController";
                    $.operateLog.dataGridUrl = url;
                    $.operateLog.showLogWin.window('open');
                    $.operateLog.pg.page = 1;
                    $.operateLog.reloadDataGrid();
                }
            });
            
            $(".channelConfirm").linkbutton({
                text:'渠道确认',
                plain:true,
                iconCls:'pic_17',
                onClick:function(){
                    var id = $(this).attr("id");
                    var loanId = $(this).attr("loan_id");
                    $.messager.confirm("提示","您确定渠道确认吗？",function(r){
                        if(r){
                            $.ajaxPackage({
                                url:channelConfirmUrl,
                                type:"post",
                                data:{"id":id,"loanId":loanId},
                                dataType:"json",
                                success:function(response, textStatus, jqXHR){
                                    var resCode = response.resCode;    
                                    var resMsg = response.resMsg; 
                                    if(resCode == "000000"){
                                        $.messager.alert("提示", resMsg, "info");
                                        reloadDataGrid();
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
                    });
                }
            });
            publicAccountDataGrid.datagrid('resize');
        }
    });
    
    // 表格分页实例
    var publicAccountDataGridPG = publicAccountDataGrid.datagrid('getPager');
    publicAccountDataGridPG.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.pg.page = pageNumber;
            $.pg.rows = pageSize;
            reloadDataGrid();
        }
    });
    
    // 新增面板参数定义
    updateDataPanel.window({
        width : 400,
        height : 200,
        //定义窗口是不是模态窗口
        modal : true,
        // 面板距父窗口左边的距离
        left:($(window).width() - 400) * 0.5,
        // 面板距父窗口顶部的距离
        top:($(window).height() - 200) * 0.5,
        //定义是否显示折叠按钮
        collapsible : false,
        //定义是否显示最小化按钮
        minimizable : false,
        //定义是否显示最大化按钮
        maximizable : false,
        //定义是否显示关闭按钮
        closable : true,
        //定义是否关闭了窗口
        closed : true,
        //定义是否窗口能被拖拽
        draggable : true,
        //定义是否窗口可以调整尺寸
        resizable : false,
        //如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
        shadow : true,
        //定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部
        inline : true,
        //样式定义
        iconCls : 'icon-save'
    });
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.pg.page = 1;
        reloadDataGrid();
    });
    
    /** 查询校验 **/
    function searchCheck(){
        // 还款日期起
        var repayDateStart = $("#repayDateStart").datebox("getValue");
        // 还款日期止
        var repayDateEnd = $("#repayDateEnd").datebox("getValue");
        // 还款金额
        var amount = $.trim($("#amount").val());
        // 对方单位
        var secondCompany = $.trim($("#secondCompany").val());
        // 状态
        var status=$("#status").combobox("getValue");
        // 认领日期(起)
        var recTimeStart=$("#recTimeStart").datebox("getValue");
        // 认领日期(止)
        var recTimeEnd=$("#recTimeEnd").datebox("getValue");
        // 认领者工号
        var recUsercode = $.trim($("#recUsercode").val());
        // 查询条件必须输入其中一个
        if ($.isEmpty(repayDateStart)
            && $.isEmpty(repayDateEnd)
            && $.isEmpty(amount)
            && $.isEmpty(secondCompany) 
            && $.isEmpty(status)
            && $.isEmpty(recTimeStart) 
            && $.isEmpty(recTimeEnd)
            && $.isEmpty(recUsercode)) {
            $.messager.alert('警告', '请至少输入一个查询条件！', 'warning');
            return false;
        }
        if(!$.isEmpty(amount) && isNaN(amount)){
            $.messager.alert('警告','还款金额必须输入数字！','warning');
            return false;
        }
        var re = /^[0-9]*[1-9][0-9]*$/ ; 
        /*if(!$.isEmpty(recOperatorId) && !re.test(recOperatorId)){
            $.messager.alert('警告','认领者工号必须输入正整数！','warning');
            return false;
        }*/
        
        // 如果还款日期（起）和还款日期（止）均不为空，则还款日期（起）必须小于还款日期（止）
        if(!$.isEmpty(repayDateStart) && !$.isEmpty(repayDateEnd)){
            var beginDate = new Date(repayDateStart.replace(/\-/g, "\/"));
            var endDate = new Date(repayDateEnd.replace(/\-/g, "\/"));
            if( beginDate > endDate){
                $.messager.alert('警告','还款日期（起）不能大于还款日期（止）！','warning');
                return false;
            }
        }
        
        // 如果认领日期（起）和认领日期（止）均不为空，则认领日期（起）必须小于认领日期（止）
        if(!$.isEmpty(recTimeStart) && !$.isEmpty(recTimeEnd)){
            var beginDate = new Date(recTimeStart.replace(/\-/g, "\/"));
            var endDate = new Date(recTimeEnd.replace(/\-/g, "\/"));
            if( beginDate > endDate){
                $.messager.alert('警告','认领日期（起）不能大于认领日期（止）！','warning');
                return false;
            }
        }
        // 防止输入空白查询
        $("#amount").val(amount);
        $("#secondCompany").val(secondCompany);
        $("#recUsercode").val(recUsercode);
        return true;
    }
    
    /** 打开批量导入窗口 **/
    $('#batchImportBtn').click(function(){
        importExcelWin.window('open');
        baseFileForm.form('clear');
        carFileForm.form('clear');
    })
    
    /** 批量导入 **/
    $("#publicAccountBatchImport").click(function(){
        var file = $("#publicAccountfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件！','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
                // 提交表单
                ajaxSubmit(baseFileForm, batchImportUrl, true);
            }
        });
    });
    
    /** 车企贷批量导入 **/
    $("#carLoanAccountBatchImport").click(function(){
        var file = $("#carLoanAccountfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件！','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
                // 提交表单
                ajaxSubmit(carFileForm, batchImportCarLoanUrl, false);
            }
        });
    });
    
    /** 对公账户导出已认领结果 **/
    $("#exportReceiveInfoBtn").click(function(){
        if(!validate()){
            return;
        }
        $.messager.confirm("提示","确认导出已认领结果吗？",function(r){
            if(r){
                submitForm(searchForm,exportUrl);
            }
        });
    });
    
    /** 对公账户导出车企贷已认领结果 **/
    $("#exportCarLoanReceiveInfoBtn").click(function(){
        $.messager.confirm("提示","确认导出车企贷已认领结果吗？",function(r){
            if(r){
                submitForm(searchForm,exportCarLoanUrl);
            }
        });
    });
    
    /** 导出证方系统已认领结果 **/
    $("#exportNewBusinessReceiveInfoBtn").click(function(){
        $.messager.confirm("提示","确认导出证方已认领结果吗？",function(r){
            if(r){
                submitForm(searchForm, exportBusinessUrl);
            }
        });
    });
    
    /** 对公账户导出对公账户还款认领情况表 **/
    $("#exportRepayReceiveInfoBtn").click(function(){
        if(!validate()){
            return;
        }
        var isOk = exportRepayReceiveInfoCheck();
        if(isOk){
            $.messager.confirm("提示","确认导出对公账户还款认领情况表吗？",function(r){
                if(r){
                    submitForm(searchForm,exportRepayReceiveUrl);
                }
            });
        }else{
            $.messager.confirm("提示","未选择认领日期范围，以今天作为筛选条件导出吗？",function(r){
                if(r){
                    submitForm(searchForm,exportRepayReceiveUrl);
                }
            });
        }
    });
    
    /** 导出对公账户还款认领情况表校验 **/
    function exportRepayReceiveInfoCheck(){
        var recTimeStart = $("#recTimeStart").datetimebox("getValue");
        var recTimeEnd = $("#recTimeEnd").datetimebox("getValue");
        if($.isEmpty(recTimeStart) && $.isEmpty(recTimeEnd)){
            return false;
        }
        return true;
    }
    
    /** 对公账户导出查询结果 **/
    $("#exportSearchResultBtn").click(function(){
        if(!validate()){
            return;
        }
        $.messager.confirm("提示","确认导出查询结果吗？",function(r){
            if(r){
                submitForm(searchForm,exportSearchResultUrl);
            }
        });
    });
    
    /** 提交表单 **/
    function submitForm(form,url){
        $.downloadFile({
            url:url,
            isDownloadBigFile:true,
            params:form.serializeObject(),
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
    
    /** 表单校验 **/
    function validate(){
        if(!searchForm.form("validate")){
            return false;
        }
        if(!searchCheck()){
            return false;
        }
        return true;
    }
    
    // 加载表格数据
    function reloadDataGrid() {
        if(!validate()){
            return;
        }
        // 获取查询表单数据转换成JSON对象
        var searchMsg = searchForm.serialize();
        // 对参数进行解码(显示中文)
        searchMsg = decodeURIComponent(searchMsg);
        var queryParam = $.serializeToJsonObject(searchMsg);
        queryParam.url = dataGridUrl;
        publicAccountDataGrid.datagrid('reloadData', queryParam);
    }
    
    /** 在$.publicAccount命名空间中定义查询方法，以供认领页面调用 **/
    $.publicAccount = {
        reloadDataGrid : function(){
            reloadDataGrid();
        }
    }
    
    /** 格式化时间 **/
    function formatDate(value){
        if (value == undefined) {
            return "";
        }
        var date = new Date(value);
        var year = date.getFullYear();
        var month = (date.getMonth() + 1);
        var day = date.getDate();
        if (month < 10) {
            month = "0" + month;
        }
        if(day < 10){
            day = "0"+ day;
        }
        return year+"-"+month+"-"+day;
    }
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        searchForm.form('reset');
    })
    
    /** 打开修改领取时间面板 **/
    $("#updateReceiveTime").click(function(){
        $.ajaxPackage({
            url:loadReceiveTimeUrl,
            type:"post",
            dataType:"json",
            success:function(response, textStatus, jqXHR){
                var resCode = response.resCode;
                var resMsg = response.resMsg;
                var attachment = response.attachment;
                if(resCode == "000000"){
                    // 清空表单
                    dataForm.form("clear");
                    //显示窗口
                    updateDataPanel.window('open');
                    // 加载表单数据
                    dataForm.form("load",attachment);
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
    });
    
    /** 修改对公账户还款领取时间 **/
    $("#submitBtn").click(function(){
        if(!dataForm.form("validate")){
            return;
        }
        
        // 开始领取时间
        var startTime = $.trim($("#startTime").val());
        // 结束领取时间
        var endTime = $.trim($("#endTime").val());
        if(!updateCheck(startTime,endTime)){
            return;
        }
        
        // 表单数据清空前后空格
        $("#startTime").val(startTime);
        $("#endTime").val(endTime);
        
        $.messager.confirm("提示","确认修改吗？",function(r){
            if(r){
                dataForm.form('submit', {
                    url: updateReceiveTimeUrl,
                    onSubmit: function(){
                    },
                    success:function(data){
                        //data = eval("("+ data +")");
                        data = JSON.parse(data);
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        if (resCode == '000000') {
                            // 关闭窗口
                            updateDataPanel.window('close');
                            $.messager.alert('提示','修改成功！','info');
                        }else{
                            $.messager.alert('警告',resMsg,'warning');
                        }
                    }
                });
            }
        });
    });
    
    /** 修改对公账户还款领取时间的校验 **/
    function updateCheck(startTime,endTime){
        // 开始领取时间合法性校验
        if(!isTimePart(startTime)){
            $.messager.alert('警告','领取开始时间不合法，正确格式应类似09:05！','warning');
            return false;
        }
        // 结束领取时间合法性校验
        if(!isTimePart(endTime)){
            $.messager.alert('警告','领取结束时间不合法，正确格式应类似17:30！','warning');
            return false;
        }
        // 时间大小校验
        var start = startTime.split(":");
        var end = endTime.split(":");
        var num1 = parseInt(start[0]) * 60 + parseInt(start[1]);
        var num2 = parseInt(end[0]) * 60 + parseInt(end[1]);
        if(num1 > num2){
            $.messager.alert('警告','领取开始时间不能大于领取结束时间！','warning');
            return false;
        }
        return true;
    }
    
    /** 判断一个字符串是否为合法的时间格式：HH:MM **/
    function isTimePart(time) {
        var reg = /^(\d{2}):(\d{2})$/;
        var r = time.match(reg);
        if (r == null) {
            return false;
        }
        // 小時
        var h = r[1];
        // 分
        var m = r[2];
        // 数值校验
        if (isNaN(h) || isNaN(m)) {
            return false;
        }
        h = parseInt(h);
        m = parseInt(m);
        if (h < 0 || h > 23) {
            // 限制小时的范围
            return false;
        }
        if (m < 0 || m > 59) {
            // 限制分钟的范围
            return false;
        }
        return true;
    }

    /** 关闭对公账户还款领取时间修改面板 **/
    $("#closeBtn").click(function() {
        if (updateDataPanel) {
            updateDataPanel.window('close');
        }
    });
    
    
    /** 打开批量导入窗口（新模板） **/
    $('#newImportBtn').click(function(){
        // 打开批量导入窗口
        newImportExcelWin.window('open');
        // 清空表单对象
        newCreditFileForm.form('clear');
        // 清空表单对象
        newCarFileForm.form('clear');
        // 清空表单对象
        newBusinessFileForm.form('clear');
    })
    
    /** 对公批量导入（新模板） **/
    $("#newCreditImportBtn").click(function(){
        // 导入文件
        var file = $("#newPublicAccountfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件！','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
                // 提交表单
                ajaxSubmit(newCreditFileForm, newImportUrl, true);
            }
        });
    });
    
    /** 车企贷批量导入（新模板） **/
    $("#newCarImportBtn").click(function(){
        // 导入文件
        var file = $("#newCarLoanAccountfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件！','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
                // 提交表单
                ajaxSubmit(newCarFileForm, newImportCarLoanUrl, false);
            }
        });
    });
    
    /** 证方批量导入（新模板） **/
    $("#newBusinessImportBtn").click(function(){
        // 导入文件
        var file = $("#newBusinessfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件！','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
                // 提交表单
                ajaxSubmit(newBusinessFileForm, newImportBusinessUrl, false);
            }
        });
    });
    
    /** 异步提交表单 **/
    function ajaxSubmit(form, url, isRefresh){
        form.ajaxSubmit({
            type: "post",
            dataType : 'json',
            url: url,
            hasDownloadFile : true,
            success: function () {
                $.messager.alert('提示',"操作完成！",'info');
                setTimeout(function(){
                    if(isRefresh){
                        reloadDataGrid();
                    }
                }, 1000);
            },
            error: function (data) {
                $.messager.alert('警告',data.resMsg,'warning');
            }
        });
    }
})
