$(function() {
    $.privateAccount = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/repayment/searchPrivateAccountInfo',
        /** 对私导入url **/
        importUrl: global.contextPath + '/repayment/importPrivateAccountInfo',
        /** 对私导出已认领结果url **/
        exportReceiveInfoUrl: global.contextPath + '/repayment/exportPrivateAccountReceiveInfo',
        /** 对私导出查询结果url **/
        exportSearchResultUrl: global.contextPath + '/repayment/exportPrivateAccountSearchResult',
        /** 领取校验url **/
        receiveCheckUrl:global.contextPath + '/repayment/privateAccountReceiveCheck',
        /** 领取url **/
        receiveInitUrl : global.contextPath + '/repayment/publicAccountReceiveInfoInit',
        /** 撤销url**/
        cancelUrl : global.contextPath + '/repayment/cancelReceive',
        /** 渠道确认url **/
        channelConfirmUrl : global.contextPath + '/repayment/channelConfirm',
        /** 加载对公账户领取时间url **/
        loadReceiveTimeUrl : global.contextPath + '/repayment/loadReceiveTime',
        /** 修改对公账户领取时间url **/
        updateReceiveTimeUrl : global.contextPath + '/repayment/updateReceiveTime',
        /** 查询操作日志url **/
        searchLogInfoUrl : global.contextPath + '/repayment/searchLogInfo',
        /** 对私账户信息表格对象 **/
        privateAccountDataGrid : $('#privateAccountDataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 上传文件面板 **/
        importExcelWin : $("#importExcelWin"),
        /** 对私批量导入表单对象 **/
        baseFileForm : $("#baseFileForm"),
        /** 修改领取时间面板对象 **/
        updateDataPanel : $("#updateDataPanel"),
        /** 修改领取时间表单对象 **/
        dataForm : $("#dataForm"),
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 员工工号 **/
        userCode : $("#userCode").val(),
        /** 员工类型 **/
        employeeType : $("#employeeType").val(),
        /** 员工id **/
        userId : $("#userId").val(),
        /** 是否有领取权限 **/
        isCanConfirmReceive : $("#isCanConfirmReceive").val(),
        /** 是否有渠道确认权限 **/
        isCanChannelConfirm : $("#isCanChannelConfirm").val(),
        /** 是否有撤销权限 **/
        isCancelReceive : $("#isCancelReceive").val(),
        /** 是否可以查看日志 **/
        isSearchLog : $("#isSearchLog").val(),
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if (!$.privateAccount.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.privateAccount.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.privateAccount.dataGridUrl;
            /** 查询并加载数据**/
            $.privateAccount.privateAccountDataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.privateAccount.searchForm.form('validate')) {
                return false;
            }
            if (!searchCheck()) {
                return false;
            }
            return true;
        }
    }
    
    /** 交易日期（起）添加清除按钮 **/
    $("#tradeDateStart").datebox("addClearButton","icon-clear");
    /** 交易日期（止）添加清除按钮 **/
    $("#tradeDateEnd").datebox("addClearButton","icon-clear");
    /** 认领日期（起）添加清除按钮 **/
    $("#recTimeStart").datebox("addClearButton","icon-clear");
    /** 认领日期（止）添加清除按钮 **/
    $("#recTimeEnd").datebox("addClearButton","icon-clear");

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.privateAccount.pg = {
        'page' : 1,
        'rows' : $.privateAccount.pageSize
    }

    /** DataGrid初始化 **/
    $.privateAccount.privateAccountDataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.privateAccount.pg,
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
            field : 'tradeDate',
            title : '交易日期',
            width : '6%',
            formatter:function(value,row,index){
                return $.DateUtil.dateFormatToStr(value);
            }
        },{
            field : 'tradeTime',
            title : '交易时间',
            width : '6%'
        },{
            field : 'tradeAmount',
            title : '交易金额',
            width : '5%',
            formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }
        },{
            field : 'secondAccount',
            title : '对方账号',
            width : '10%'
        },{
            field : 'secondName',
            title : '对方户名',
            width : '6%'
        },{
            field : 'tradeBank',
            title : '交易行',
            width : '8%'
        },{
            field : 'tradeType',
            title : '交易类型',
            width : '5%'
        },{
            field : 'tradeChannel',
            title : '交易渠道',
            width : '5%'
        },{
            field : 'tradePurpose',
            title : '交易用途',
            width : '9%'
        },{
            field : 'tradeRemark',
            title : '交易摘要',
            width : '9%'
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
                    /** 显示领取链接 **/
                    if(status=="未认领" && $.privateAccount.isCanConfirmReceive=="true"){
                        elements+="<a href='#' class='receive' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    /** 显示撤销链接 **/
                    if(recOperatorId==$.privateAccount.userId && (status=="已认领" || status=="渠道确认") && $.privateAccount.isCancelReceive=="true"){
                        elements+="<a href='#' class='cancel' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    /** 显示查看日志链接 **/
                    if($.privateAccount.isSearchLog=="true"){
                        elements+="<a href='#' class='log' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    /** 显示渠道确认链接 **/
                    if(status=="未认领" && $.privateAccount.isCanChannelConfirm=="true"){
                        elements+="<a href='#' class='channelConfirm' id='"+row.id+"' loan_id='"+ row.loanId +"' ></a>";
                    }
                    return elements;
                }
            }
        }] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.privateAccount.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.privateAccount.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 领取操作**/
            $(".receive").linkbutton({
                text:'领取',
                plain:true,
                iconCls:'pic_36',
                onClick:function(){
                    var id = $(this).attr("id");
                    var loanId = $(this).attr("loan_id");
                    /** 参数信息 **/
                    var json = {"id":id,"loanId":loanId};
                    $.ajaxPackage({
                        url:$.privateAccount.receiveCheckUrl,
                        type:"get",
                        data: json,
                        dataType:"json",
                        success:function(response, textStatus, jqXHR){
                            var resCode = response.resCode;    
                            var resMsg = response.resMsg;
                            if(resCode != "000000"){
                                $.messager.alert("警告", resMsg, "warning");
                                return;
                            }
                            var row = $.dataGrid.getSelectedRow($.privateAccount.privateAccountDataGrid);
                            if (!row) {
                                $.messager.alert('警告','请选中待认领的对私还款记录！','warning');
                                return;
                            }
                            /** 选中待认领对私还款记录 **/
                            $.privateRec.recRow = row;
                            /** 清空表单数据 **/
                            $.privateRec.recSearchForm.form("clear");
                            /** 打开对私还款认领窗口 **/
                            $.privateRec.showRectWin.window('open');
                            /** 加载表单数据 **/
                            var tradeDate = $.DateUtil.dateFormatToStr(row.tradeDate);
                            var tradeAmount = $.comdify(row.tradeAmount);
                            $("#recTradeDate").text(tradeDate);
                            $("#recTradeAmount").text(tradeAmount);
                            $("#recStatus").text(row.status);
                            $.privateRec.recSearchForm.form("load",{"accountId":row.id});
                            /** 表格数据初始化 **/
                            $.privateRec.recDataGrid.datagrid("clearData");
                        },
                        error:function(response, textStatus, jqXHR){
                            $.messager.alert("异常", "操作失败", "error");
                        },
                        complete:function(jqXHR,textStatus){
                        }
                    });
                }
            });
            
            /** 撤销操作**/
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
                                url:$.privateAccount.cancelUrl,
                                type:"post",
                                data:{"id":id,"loanId":loanId},
                                dataType:"json",
                                success:function(response, textStatus, jqXHR){
                                    var resCode = response.resCode;
                                    var resMsg = response.resMsg;
                                    if(resCode != "000000"){
                                        $.messager.alert("警告", resMsg, "warning");
                                        return;
                                    }
                                    $.messager.alert("提示", resMsg, "info");
                                    /** 刷新查询数据**/
                                    //search();
                                    $.privateAccount.reloadDataGrid();
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
            
            /** 查看日志操作**/
            $(".log").linkbutton({
                text:'日志',
                plain:true,
                iconCls:'pic_53',
                onClick:function(){
                    var id = $(this).attr("id");
                    var url = $.privateAccount.searchLogInfoUrl;
                    url = url +"?objectId="+id;
                    url = url +"&logName=PrivateAccountInfoController";
                    $.operateLog.dataGridUrl = url;
                    $.operateLog.showLogWin.window('open');
                    $.operateLog.pg.page = 1;
                    $.operateLog.reloadDataGrid();
                }
            });
            
            /** 渠道确认操作**/
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
                                url:$.privateAccount.channelConfirmUrl,
                                type:"post",
                                data:{"id":id,"loanId":loanId},
                                dataType:"json",
                                success:function(response, textStatus, jqXHR){
                                    var resCode = response.resCode;
                                    var resMsg = response.resMsg;
                                    if(resCode != "000000"){
                                        $.messager.alert("警告", resMsg, "warning");
                                        return;
                                    }
                                    $.messager.alert("提示", resMsg, "info");
                                    /** 刷新查询数据**/
                                    //search();
                                    $.privateAccount.reloadDataGrid();
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
            $.privateAccount.privateAccountDataGrid.datagrid('resize');
        }
    });
    
    /** 表格分页组件 **/
    $.privateAccount.pager = $.privateAccount.privateAccountDataGrid.datagrid('getPager');
    $.privateAccount.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.privateAccount.pg.page = pageNumber;
            $.privateAccount.pg.rows = pageSize;
            // 查询
            //search();
            $.privateAccount.reloadDataGrid();
        }
    });
    
    /** 领取时间修改窗口参数定义 **/
    $.privateAccount.updateDataPanel.window({
        /** 窗口宽度 **/
        width : 400,
        /** 窗口高度 **/
        height : 200,
        /** 窗口距父窗口左边的距离 **/
        left:($(window).width() - 400) * 0.5,
        /** 窗口距父窗口顶部的距离 **/
        top:($(window).height() - 200) * 0.5,
        /** 定义窗口是不是模态窗口 **/
        modal : true,
        /** 定义是否显示折叠按钮 **/
        collapsible : false,
        /** 定义是否显示最小化按钮 **/
        minimizable : false,
        /** 定义是否显示最大化按钮 **/
        maximizable : false,
        /** 定义是否显示关闭按钮 **/
        closable : true,
        /** 定义是否关闭窗口 **/
        closed : true,
        /** 定义是否窗口能被拖拽 **/
        draggable : true,
        /** 定义是否可以调整窗口尺寸 **/
        resizable : false,
        /** 如果设为 true， 当窗口能够显示阴影的时候将会显示阴影 **/
        shadow : true,
        /** 定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部 **/
        inline : true,
        /** 样式定义 **/
        iconCls : 'pic_66'
    });
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.privateAccount.pg.page = 1;
        $.privateAccount.reloadDataGrid();
    });
    
    /** 查询校验 **/
    function searchCheck(){
        // 交易日期起
        var tradeDateStart = $("#tradeDateStart").datebox("getValue");
        // 交易日期止
        var tradeDateEnd = $("#tradeDateEnd").datebox("getValue");
        // 交易金额
        var tradeAmount = $.trim($("#tradeAmount").val());
        // 对方姓名
        var secondName = $.trim($("#secondName").val());
        // 状态
        var status=$("#status").combobox("getValue");
        // 认领日期(起)
        var recTimeStart=$("#recTimeStart").datebox("getValue");
        // 认领日期(止)
        var recTimeEnd=$("#recTimeEnd").datebox("getValue");
        // 认领者工号
        var recUsercode = $.trim($("#recUsercode").val());
        // 查询条件必须输入其中一个
        if ($.isEmpty(tradeDateStart)
            && $.isEmpty(tradeDateEnd)
            && $.isEmpty(tradeAmount)
            && $.isEmpty(secondName) 
            && $.isEmpty(status)
            && $.isEmpty(recTimeStart) 
            && $.isEmpty(recTimeEnd)
            && $.isEmpty(recUsercode)) {
            $.messager.alert('警告', '请至少输入一个查询条件！', 'warning');
            return false;
        }
        if(!$.isEmpty(tradeAmount) && isNaN(tradeAmount)){
            $.messager.alert('警告','还款金额必须输入数字！','warning');
            return false;
        }
        // 如果交易日期（起）和交易日期（止）均不为空，则交易日期（起）必须小于交易日期（止）
        if(!$.isEmpty(tradeDateStart) && !$.isEmpty(tradeDateEnd)){
            var beginDate = new Date(tradeDateStart.replace(/\-/g, "\/"));
            var endDate = new Date(tradeDateEnd.replace(/\-/g, "\/"));
            if( beginDate > endDate){
                $.messager.alert('警告','交易日期（起）不能大于交易日期（止）！','warning');
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
        $("#tradeAmount").val(tradeAmount);
        $("#secondName").val(secondName);
        $("#recUsercode").val(recUsercode);
        return true;
    }
    
    /** 打开导入窗口 **/
    $('#importBtn').click(function(){
        $.privateAccount.importExcelWin.window('open');
        $.privateAccount.baseFileForm.form('clear');
    })
    
    /** 对私还款信息批量导入 **/
    $("#privateAccountImport").click(function(){
        var file = $("#privateAccountfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件！','warning');
            return;
        }
        $.messager.confirm("提示","确认批量导入吗？",function(r){
            if(r){
                $.privateAccount.baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: $.privateAccount.importUrl,
                    hasDownloadFile : true,
                    success: function () {
                        $.messager.alert('信息','操作完成！','info');
                        setTimeout(function(){
                            $.privateAccount.importExcelWin.window('close');
                            $.privateAccount.reloadDataGrid();
                        }, 1000);
                    },
                    error: function (data) {
                        $.messager.alert('警告',data.resMsg,'warning');
                    }
                });
            }
        });
    });
    
    /** 对私还款导出已认领结果 **/
    $("#exportReceiveInfoBtn").click(function(){
        if(!$.privateAccount.validate()){
            return;
        }
        $.messager.confirm("提示","确认导出已认领结果吗？",function(r){
            if(r){
                submitForm($.privateAccount.searchForm, $.privateAccount.exportReceiveInfoUrl,true);
            }
        });
    });
    
    /** 对私还款导出查询结果 **/
    $("#exportSearchResultBtn").click(function(){
        if(!$.privateAccount.validate()){
            return;
        }
        $.messager.confirm("提示","确认导出查询结果吗？",function(r){
            if(r){
                submitForm($.privateAccount.searchForm,$.privateAccount.exportSearchResultUrl,false);
            }
        });
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
                        $.privateAccount.reloadDataGrid();
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
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.privateAccount.searchForm.form('reset');
    })
    
    /** 打开修改领取时间面板 **/
    $("#updateReceiveTime").click(function(){
        $.ajaxPackage({
            url:$.privateAccount.loadReceiveTimeUrl,
            type:"post",
            dataType:"json",
            success:function(response, textStatus, jqXHR){
                var resCode = response.resCode;
                var resMsg = response.resMsg;
                var attachment = response.attachment;
                if(resCode != "000000"){
                    $.messager.alert("警告", resMsg, "warning");
                    return;
                }
                // 清空表单
                $.privateAccount.dataForm.form("clear");
                //显示窗口
                $.privateAccount.updateDataPanel.window('open');
                // 加载表单数据
                $.privateAccount.dataForm.form("load",attachment);
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
        if(!$.privateAccount.dataForm.form("validate")){
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
                $.ajaxPackage({
                    url:$.privateAccount.updateReceiveTimeUrl,
                    type:"post",
                    data: $.privateAccount.dataForm.serializeObject(),
                    dataType:"json",
                    success:function(response, textStatus, jqXHR){
                        var resCode = response.resCode;
                        var resMsg = response.resMsg;
                        if(resCode != "000000"){
                            $.messager.alert("警告", resMsg, "warning");
                            return;
                        }
                        $.messager.alert('提示','修改成功！','info');
                        // 关闭窗口
                        $.privateAccount.updateDataPanel.window('close');
                    },
                    error:function(response, textStatus, jqXHR){
                        $.messager.alert("异常", "操作失败", "error");
                    },
                    complete:function(jqXHR,textStatus){
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
        if ($.privateAccount.updateDataPanel) {
            $.privateAccount.updateDataPanel.window('close');
        }
    });
})
