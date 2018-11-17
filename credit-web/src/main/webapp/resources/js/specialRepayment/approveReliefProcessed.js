$(function() {
    $.approveReliefProcess = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/applyReliefRepayManager/applyReliefPage/1/approve',
        updateApplyReliefUrl : global.contextPath + '/applyReliefRepayManager/updateApplyReliefStatus',
        /** 数据表格对象 **/
        dataGrid : $('#approveReliefProcessDataGrid'),
        /** 减免详情 **/
        applyReliefInfoWin:$("#applyReliefInfoWin"),
        /** 减免拒绝窗口 **/
        refuseApproveWin:$("#refuseApproveWin"),
        /** 减免拒绝表单 **/
        refuseApproveForm:$("#refuseApproveForm"),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 减免类型 **/
        applyTypes: {
            '01':'一般减免',
            '02':'结清减免'
        },
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if (!$.approveReliefProcess.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.approveReliefProcess.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.approveReliefProcess.dataGridUrl;
            /** 查询并加载数据**/
            $.approveReliefProcess.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.approveReliefProcess.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.approveReliefProcess.pg = {
        'page' : 1,
        'rows' : $.approveReliefProcess.pageSize
    }

    /** DataGrid初始化 **/
    $.approveReliefProcess.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.approveReliefProcess.pg,
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
        nowrap : true,
        /** 禁止服务端排序 **/
        remoteSort:false,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            // 列定义
            {
                field :'applyId',
                title :'id',
                width : '7%',
                checkbox:true
            },
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
            },{
                field : 'applyType',
                title : '减免类型',
                width : '10%',
                formatter : function(value, row, index) {
                    return $.approveReliefProcess.applyTypes[value + ''] || '未知';
                },
            },{
                field : 'operate',
                title : '操作',
                width : '26%',
                formatter:function(value,row,index){
                    if(row){
                        var elements = "";
                        elements = "<a href='javascript:void(0)' class='approveDetailInfo' applyId='"+ row.applyId +"' contractNum='"+row.contractNum+"'>详情</a>&nbsp;&nbsp;";
                        elements += "<a href='javascript:void(0)' class='acceptRelief' applyId='"+ row.applyId +"'>同意</a>&nbsp;&nbsp;";
                        elements += "<a href='javascript:void(0)' class='refuseRelief' applyId='"+ row.applyId +"'>拒绝</a>&nbsp;&nbsp;";
                        return elements;
                    }
                }
            }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.approveReliefProcess.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.approveReliefProcess.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 页面自适应 **/
            $.approveReliefProcess.dataGrid.datagrid('resize');
            $(".approveDetailInfo").click(function() {
                var applyId = $(this).attr("applyId");
                var contractNum = $(this).attr("contractNum");
                var applyDetailInfoUrl = $.approveReliefInfo.applyDetailInfoUrl + "?applyId=" + applyId + "&contractNum=" + contractNum;
                var reliefOperteInfoUrl = $.approveReliefInfo.reliefOperateInfoUrl + "?applyId=" + applyId;
                $("#jumpApplyReliefInfoPage").attr("src", applyDetailInfoUrl);
                $("#jumpReliefOperateInfoPage").attr("src", reliefOperteInfoUrl);
                $.approveReliefProcess.applyReliefInfoWin.window("open");
                /** 默认选中第一个选项卡 **/
                $.approveReliefInfo.tabsObj.tabs('unselect',0);
                $.approveReliefInfo.tabsObj.tabs('select',0);
            })
            $(".acceptRelief").click(function() {
                    var applyId = $(this).attr("applyId");
                    acceptOrRefuseRelief(applyId,2);
            })
            $(".refuseRelief").click(function() {
                    var applyId = $(this).attr("applyId");
                    $.approveReliefProcess.refuseApproveForm.form("clear");
                    $.approveReliefProcess.refuseApproveForm.find("input[name='id']").val(applyId);
                    $.approveReliefProcess.refuseApproveWin.window('open');
                    //acceptOrRefuseRelief(applyId,3);
            })
        }
    });

    /** 表格分页组件 **/
    $.approveReliefProcess.pager = $.approveReliefProcess.dataGrid.datagrid('getPager');
    $.approveReliefProcess.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.approveReliefProcess.pg.page = pageNumber;
            $.approveReliefProcess.pg.rows = pageSize;
            $.approveReliefProcess.reloadDataGrid();
        }
    });
    $.approveReliefProcess.reloadDataGrid();
    $("#acceptReliefBtn").click(function(){
        var rows = $.approveReliefProcess.dataGrid.datagrid('getSelections');
        if(rows.length <= 0){
            $.messager.alert('警告',"请选择需要同意的记录！",'warning');
            return ;
        }
        var ids = [];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].applyId);
        }
        acceptOrRefuseRelief(ids.join(),2);
    })

    $("#refuseReliefBtn").click(function(){
        var rows = $.approveReliefProcess.dataGrid.datagrid('getSelections');
        if(rows.length <= 0){
            $.messager.alert('警告',"请选择需要拒绝的记录！",'warning');
            return ;
        }
        var ids = [];
        for(var i=0;i<rows.length;i++){
            ids.push(rows[i].applyId);
        }
        $.approveReliefProcess.refuseApproveForm.form("clear");
        $.approveReliefProcess.refuseApproveForm.find("input[name='id']").val(ids.join());
        $.approveReliefProcess.refuseApproveWin.window('open');
    })

    /**
     * 拒绝减免window返回操作
     */
    $("#refuseApproveCloseBut").click(function(){
        $.approveReliefProcess.refuseApproveWin.window("close");
        $.approveReliefProcess.refuseApproveForm.form("reset");
    });

    /**
     * 减免取消window提交操作
     */
    $("#refuseApproveSubmitBut").click(function() {
        var memo1 = $.approveReliefProcess.refuseApproveForm.find("input[name='memo1']").val();
        var applyId =  $.approveReliefProcess.refuseApproveForm.find("input[name='id']").val();
        if (memo1 == null || memo1 == '') {
            $.messager.alert('警告', '拒绝原因不能为空!', 'warning');
            return;
        }
        acceptOrRefuseRelief(applyId,3,memo1);
    });

    function acceptOrRefuseRelief(applyId, status, memo){
        var tips = '';
        if(status == '3') {
            tips = '确定拒绝减免操作?';
        }else if (status = '2'){
            tips = '确定同意减免操作?';
        }
        var param = {};
        if(applyId.indexOf(',')>0){
            param = {
                ids:applyId,
                applicationStatus:status,
                memo1:memo
            }
        }else{
            param = {
                id:applyId,
                applicationStatus:status,
                memo1:memo
            }
        }
        $.messager.confirm('确认',tips,function(r){ if (r) {
            $.ajaxPackage({
                type : 'post',
                url : $.approveReliefProcess.updateApplyReliefUrl,
                dataType : 'json',
                data : param,
                success : function (data,textStatus,jqXHR) {
                    var resCode = data.resCode;
                    var resMsg = data.resMsg;
                    //从服务器上获取到记录信息
                    var attachment = data.attachment;
                    if (resCode == '000000') {
                        $.approveReliefProcess.reloadDataGrid();
                        $.messager.alert('提示信息','操作成功!');
                    } else {
                        $.messager.alert('警告',resMsg,'error');
                    }
                },
                error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                    $.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
                },
                complete : function() {
                    $.approveReliefProcess.reloadDataGrid();
                    $.approveReliefProcess.refuseApproveWin.window("close");
                }
            });
        }});
    }


    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.approveReliefProcess.pg.page = 1;
        $.approveReliefProcess.reloadDataGrid();
    });

    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.approveReliefProcess.searchForm.form('reset');
    });

    $.approveReliefInfo = {
        tabsObj : $("#applyReliefInfoTab"),
        /** 减免申请详情 **/
        applyDetailInfoUrl : global.contextPath + "/applyReliefRepayManager/applyDetailInfoPage",
        /** 减免申请取消 **/
        cancelApplyReliefUrl : global.contextPath + "/applyReliefRepayManager/cancelApplyReliefUrl",
        /** 减免审批操作详情 **/
        reliefOperateInfoUrl : global.contextPath + "/applyReliefRepayManager/reliefOperateInfoPage"

    }

})
