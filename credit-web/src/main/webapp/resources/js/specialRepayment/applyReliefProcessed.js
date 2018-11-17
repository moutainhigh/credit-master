/**
 * Created by ym10094 on 2017/6/21.
 */
/**
 * Created by ym10094 on 2016/11/29.
 */
$(function() {
    $.applyReliefProcess = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/applyReliefRepayManager/applyReliefPage/1/apply',
        applyReliefCheckUrl : global.contextPath + '/applyReliefRepayManager/applyReliefCheck',
        spApplyReliefCheckUrl : global.contextPath + '/applyReliefRepayManager/spApplyReliefCheck',
        importExcelUrl : global.contextPath + '/applyReliefRepayManager/importReliefPenaltyStateFile',
        /** 数据表格对象 **/
        dataGrid : $('#applyReliefProcessDataGrid'),
        applyReliefWin: $("#applyReliefWin"),
        applyReliefForm:$("#applyReliefForm"),
        applyReliefInfoWin:$("#applyReliefInfoWin"),
        cancelApplyWin:$("#cancelApplyWin"),
        cancelApplyForm:$("#cancelApplyForm"),
        spApplyReliefWin:$("#spApplyReliefWin"),
        spApplyReliefForm:$("#spApplyReliefForm"),
        spApplyReliefInfoWin:$("#spApplyReliefInfoWin"),
        importExcelWin:$("#importExcelWin"),
        fileForm:$("#fileForm"),
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
            if (!$.applyReliefProcess.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.applyReliefProcess.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.applyReliefProcess.dataGridUrl;
            /** 查询并加载数据**/
            $.applyReliefProcess.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.applyReliefProcess.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.applyReliefProcess.pg = {
        'page' : 1,
        'rows' : $.applyReliefProcess.pageSize
    }

    /** DataGrid初始化 **/
    $.applyReliefProcess.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.applyReliefProcess.pg,
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
                field : 'name',
                title : '客户姓名',
                width : '10%'
            },           {
                field : 'idNum',
                title : '身份证号',
                width : '10%'
            },            {
                field : 'loanType',
                title : '借款类型',
                width : '10%'
            },{
                field : 'createTime',
                title : '申请时间',
                width : '10%',
                formatter:$.DateUtil.dateFormatToFullStr
            },
            {
                field : 'applyAmount',
                title : '申请减免金额',
                width : '10%'
            },
            {
                field : 'nodeName',
                title : '审批环节',
                width : '10%'
            },
            {
                field : 'operate',
                title : '操作',
                width : '28%',
                formatter:function(value,row,index){
                    if(row){
                        var elements = "";
                        elements = "<a href='javascript:void(0)' class='applyDetailInfo' applyId='"+ row.applyId +"' contractNum='"+row.contractNum+"'>详情</a>&nbsp;&nbsp;";
                        elements +=  "<a href='javascript:void(0)' class='cancelApplyRelief' applyId='"+ row.applyId +"' >取消</a>";
                        return elements;
                    }
                }
            }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.applyReliefProcess.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.applyReliefProcess.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 页面自适应 **/
            $.applyReliefProcess.dataGrid.datagrid('resize');
            $(".applyDetailInfo").click(function(){
                var applyId = $(this).attr("applyId");
                var contractNum = $(this).attr("contractNum");
                var applyDetailInfoUrl = $.applyReliefInfo.applyDetailInfoUrl + "?applyId=" + applyId + "&contractNum=" + contractNum;
                var reliefOperteInfoUrl = $.applyReliefInfo.reliefOperateInfoUrl + "?applyId=" + applyId;
                $("#jumpApplyReliefInfoPage").attr("src", applyDetailInfoUrl);
                $("#jumpReliefOperateInfoPage").attr("src", reliefOperteInfoUrl);
                /** 默认选中第一个选项卡 **/
                $.applyReliefInfo.tabsObj.tabs('unselect',0);
                $.applyReliefInfo.tabsObj.tabs('select',0);
                $.applyReliefProcess.applyReliefInfoWin.window("open");

            })
            $(".cancelApplyRelief").click(function() {
                    $.applyReliefProcess.cancelApplyForm.form("clear");
                    var applyId = $(this).attr("applyId");
                    $.applyReliefProcess.cancelApplyWin.find("input[name='id']").val(applyId);
                    $.applyReliefProcess.cancelApplyWin.window('open');
            })
        }
    });

    /** 表格分页组件 **/
    $.applyReliefProcess.pager = $.applyReliefProcess.dataGrid.datagrid('getPager');
    $.applyReliefProcess.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.applyReliefProcess.pg.page = pageNumber;
            $.applyReliefProcess.pg.rows = pageSize;
            $.applyReliefProcess.reloadDataGrid();
        }
    });
    $.applyReliefProcess.reloadDataGrid();
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.applyReliefProcess.pg.page = 1;
        $.applyReliefProcess.reloadDataGrid();
    });

    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.applyReliefProcess.searchForm.form('reset');
    });
    /**新建减免申请**/
    $("#createApplyRelief").click(function () {
        $.applyReliefProcess.applyReliefWin.window('open');
    });
    /**
     * 申请减免window提交操作
     */
    $("#applyReliefSubmitBut").click(function(){
        var contractNum = $("#applyReliefContractNum").val();
        var name = $("#applyReliefName").val();
        var idNum = $("#applyReliefidNum").val();
        var applyReliefType = $.applyReliefProcess.applyReliefForm.find("input[name='applyReliefType']").val();
        if ($.isEmpty(contractNum)) {
            $.messager.alert('警告','缺少合同编号参数!','warning');
            return;
        }
        if ($.isEmpty(name)) {
            $.messager.alert('警告','缺少客户姓名参数!','warning');
            return;
        }
        if ($.isEmpty(idNum)) {
            $.messager.alert('警告','缺少证件号码参数!','warning');
            return;
        }
        if ($.isEmpty(applyReliefType)) {
            $.messager.alert('警告','请选择减免类型!','warning');
            return;
        }
        var params = {
            contractNum :contractNum,
            idNum:idNum,
            applyReliefType:applyReliefType
        }
        $.ajaxPackage({
            type : "post",
            url : $.applyReliefProcess.applyReliefCheckUrl,
            data : $.applyReliefProcess.applyReliefForm.serialize(),
            dataType : "json",
            success : function (data,textStatus,jqXHR) {
                var resCode = data.resCode;
                var resMsg = data.resMsg;
                if (resCode == '000000') {
                    //打开申请减免详情窗口
                    //$.applyReliefProcess.applyReliefWin.window("close");
                    var jumApplyReleftInfoUrl = $.applyReliefInfo.applyReliefInfoUrl+"?"+ $.parseParam(params);
                    var reliefOperteInfoUrl = $.applyReliefInfo.reliefOperateInfoUrl;
                    $("#jumpApplyReliefInfoPage").attr("src",jumApplyReleftInfoUrl);
                    $("#jumpReliefOperateInfoPage").attr("src", reliefOperteInfoUrl);
                    /** 默认选中第一个选项卡 **/
                    $.applyReliefInfo.tabsObj.tabs('unselect',0);
                    $.applyReliefInfo.tabsObj.tabs('select',0);
                    $.applyReliefProcess.applyReliefInfoWin.window("open");
                    $.applyReliefProcess.applyReliefWin.window('close');
                } else {
                    $.messager.alert('警告',resMsg,'error');
                }
            },
            error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                $.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
            },
            complete : function() {
            }
        });
    });

    /**
     * 申请减免window返回操作
     */
    $("#applyReliefCloseBut").click(function(){
        $.applyReliefProcess.applyReliefWin.window("close");
        $.applyReliefProcess.applyReliefForm.form("reset");

    });

    /**
     * 减免取消window返回操作
     */
    $("#cancelApplyCloseBut").click(function(){
        $.applyReliefProcess.cancelApplyWin.window("close");
        $.applyReliefProcess.cancelApplyForm.form("reset");

    });

    /**
     * 减免取消window提交操作
     */
    $("#cancelApplySubmitBut").click(function(){
        var memo1 = $.applyReliefProcess.cancelApplyForm.find("input[name='memo1']").val();
        if(memo1 == null || memo1 == ''){
            $.messager.alert('警告','取消原因不能为空!','warning');
            return;
        }
        $.ajaxPackage({
            type : "post",
            url : $.applyReliefInfo.cancelApplyReliefUrl,
            data : $.applyReliefProcess.cancelApplyForm.serialize(),
            dataType : "json",
            success : function (data,textStatus,jqXHR) {
                var resCode = data.resCode;
                var resMsg = data.resMsg;
                if (resCode == '000000') {
                    $.messager.alert('提示','取消成功！','info');
                } else {
                    $.messager.alert('警告',resMsg,'error');
                }
            },
            error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                $.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
            },
            complete : function() {
                $.applyReliefProcess.reloadDataGrid();
                $.applyReliefProcess.cancelApplyWin.window("close");
            }
        });
    });

    /**
     * 申请减免window关闭之前清空数据
     */
    $.applyReliefProcess.applyReliefWin.window({
            onBeforeClose : function () {
                $.applyReliefProcess.applyReliefForm.form("reset");
            }
        }
    );
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

    $.applyReliefInfo = {
        tabsObj : $("#applyReliefInfoTab"),
        /** 减免申请页面 **/
        applyReliefInfoUrl : global.contextPath + "/applyReliefRepayManager/jumpApplyReliefInfo/1",
        /** 减免申请详情 **/
        applyDetailInfoUrl : global.contextPath + "/applyReliefRepayManager/applyDetailInfoPage",
        /** 减免申请取消 **/
        cancelApplyReliefUrl : global.contextPath + "/applyReliefRepayManager/cancelApplyReliefUrl",
        /** 减免审批操作详情 **/
        reliefOperateInfoUrl : global.contextPath + "/applyReliefRepayManager/reliefOperateInfoPage"

    }

    /**打开特殊减免申请窗口*/
    $("#createSpApplyRelief").click(function () {
        $.applyReliefProcess.spApplyReliefWin.window('open');
    });
    /**
     *关闭特殊减免申请窗口
     */
    $("#spApplyReliefCloseBut").click(function(){
        $.applyReliefProcess.spApplyReliefWin.window("close");
        $.applyReliefProcess.spApplyReliefForm.form("reset");

    });
    /**
     * 申请特殊减免window关闭之前清空数据
     */
    $.applyReliefProcess.spApplyReliefWin.window({
            onBeforeClose : function () {
                $.applyReliefProcess.spApplyReliefForm.form("reset");
            }
        }
    );
    /**
     * 申请减免window提交操作
     */
    $("#spApplyReliefSubmitBut").click(function(){
        var contractNum = $("#spApplyReliefContractNum").val();
        var name = $("#spApplyReliefName").val();
        var idNum = $("#spApplyReliefidNum").val();
        if ($.isEmpty(contractNum)) {
            $.messager.alert('警告','缺少合同编号参数!','warning');
            return;
        }
        if ($.isEmpty(name)) {
            $.messager.alert('警告','缺少客户姓名参数!','warning');
            return;
        }
        if ($.isEmpty(idNum)) {
            $.messager.alert('警告','缺少证件号码参数!','warning');
            return;
        }
        var params = {
            contractNum :contractNum,
            idNum:idNum,
            specialReliefFlag:'1'
        }
        $.ajaxPackage({
            type : "post",
            url : $.applyReliefProcess.spApplyReliefCheckUrl,
            data : $.applyReliefProcess.spApplyReliefForm.serialize(),
            dataType : "json",
            success : function (data,textStatus,jqXHR) {
                var resCode = data.resCode;
                var resMsg = data.resMsg;
                if (resCode == '000000') {
                    //打开申请减免详情窗口
                    //$.applyReliefProcess.applyReliefWin.window("close");
                    var spJumApplyReleftInfoUrl = $.applyReliefInfo.applyReliefInfoUrl+"?"+ $.parseParam(params);
                    $("#spJumpApplyReliefInfoPage").attr("src",spJumApplyReleftInfoUrl);
                    $.applyReliefProcess.spApplyReliefInfoWin.window("open");
                    $.applyReliefProcess.spApplyReliefWin.window('close');
                } else {
                    $.messager.alert('警告',resMsg,'error');
                }
            },
            error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                $.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
            },
            complete : function() {
            }
        });
    });

    $("#importBut").click(function () {
        $.applyReliefProcess.importExcelWin.window("open");
    });
    /**  导入Excel（信贷系统）事件 **/
    $("#importExcelBut").click(function(){
        /** 判断是否选中文件 **/
        var fileName = $.applyReliefProcess.fileForm.find("input[type=file]").val();
        if ($.isEmpty(fileName)) {
            $.messager.alert("提示信息","请选择上传的文件!","warning");
            return;
        }
        $.messager.confirm("上传文件","确定上传文件?",function(r){
                if (r) {
                    $.applyReliefProcess.fileForm.ajaxSubmit({
                        type : "post",
                        dataType : "json",
                        url : $.applyReliefProcess.importExcelUrl,
                        hasDownloadFile : true,
                        success: function (data) {
                            $.messager.alert("信息","操作成功");
                            $.applyReliefProcess.importExcelWin.window("close");
                        },
                        error: function (data) {
                            $.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
                        }
                    });
                }}
        );
    })
})
