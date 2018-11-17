$(function() {
    $.approveReliefFinish = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/applyReliefRepayManager/applyReliefPage/234567/approve',
        /** 数据表格对象 **/
        dataGrid : $('#approveReliefFinishDataGrid'),
        applyReliefInfoWin:$("#applyReliefInfoWin"),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 减免状态 **/
        applyStatuses : {
            '1' : '申请',
            '2' : '通过',
            '3' : '拒绝',
            '4' : '取消',
            '5' : '生效',
            '6' : '失效',
            '7' : '完成'
        },
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
            if (!$.approveReliefFinish.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.approveReliefFinish.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.approveReliefFinish.dataGridUrl;
            /** 查询并加载数据**/
            $.approveReliefFinish.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.approveReliefFinish.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.approveReliefFinish.pg = {
        'page' : 1,
        'rows' : $.approveReliefFinish.pageSize
    }

    /** DataGrid初始化 **/
    $.approveReliefFinish.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.approveReliefFinish.pg,
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
        nowrap : false,
        /** 禁止服务端排序 **/
        remoteSort:false,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            // 列定义
             {
                field : 'contractNum',
                title : '合同编号',
                width : '8%'
            },           {
                field : 'name',
                title : '客户姓名',
                width : '5%'
            },           {
                field : 'idNum',
                title : '身份证号',
                width : '8%'
            },            {
                field : 'loanType',
                title : '借款类型',
                width : '8%'
            },{
                field : 'createTime',
                title : '申请时间',
                width : '8%',
                formatter:$.DateUtil.dateFormatToFullStr
            },
            {
                field : 'applyAmount',
                title : '申请减免金额',
                width : '5%'
            },{
                field : 'effectiveMoney',
                title : '生效减免金额',
                width : '5%'
            },{
                field : 'isSpecial',
                title : '特殊减免',
                width : '5%',
                formatter : function(value, row, index) {
                    if (row.isSpecial == "0") {
                        return "否"
                    }
                    return "是";
                },
            },{
                field : 'applyType',
                title : '减免类型',
                width : '5%',
                formatter : function(value, row, index) {
                    return $.approveReliefFinish.applyTypes[value + ''] || '未知';
                },
            },{
                field : 'applicationStatus',
                title : '减免状态',
                width : '5%',
                formatter : function(value, row, index) {
                    return $.approveReliefFinish.applyStatuses[value + ''] || '未知';
                },
            }, {
                field: 'memo1',
                title: '备注',
                width: '29%',
                formatter : function(value, row, index){
                    if ($.isEmpty(row.memo1)) {
                        return ;
                    }
                    var updateTime = row.updateTime;
                    if ($.isEmpty(row.updateTime)) {
                        updateTime = "";
                    }
                    var updator = row.updator;
                    if ($.isEmpty(row.updateTime)) {
                        updator = "";
                    }
                    var memo = $.DateUtil.dateFormatToFullStr(updateTime) + ' ' + updator +  ' ' + row.memo1;
                    return memo;
                }
            }, {
                field : 'operate',
                title : '操作',
                width: '4%',
                formatter:function(value,row,index){
                    if(row){
                        if(row.applicationStatus == '1' || row.applicationStatus == '2') {
                            var elements = "";
                            elements = "<a href='javascript:void(0)' class='approveDetailInfo' applyId='" + row.applyId + "' contractNum='" + row.contractNum + "'>详情</a>&nbsp;&nbsp;";
                            return elements;
                        }
                    }
                }
            }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.approveReliefFinish.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.approveReliefFinish.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 页面自适应 **/
            $.approveReliefFinish.dataGrid.datagrid('resize');
            $(".approveDetailInfo").click(function() {
                var applyId = $(this).attr("applyId");
                var contractNum = $(this).attr("contractNum");
                var applyDetailInfoUrl = $.approveReliefInfo.applyDetailInfoUrl + "?applyId=" + applyId + "&contractNum=" + contractNum;
                var reliefOperteInfoUrl = $.approveReliefInfo.reliefOperateInfoUrl + "?applyId=" + applyId;
                $("#jumpApplyReliefInfoPage").attr("src", applyDetailInfoUrl);
                $("#jumpReliefOperateInfoPage").attr("src", reliefOperteInfoUrl);
                /** 默认选中第一个选项卡 **/
                $.approveReliefInfo.tabsObj.tabs('unselect', 0);
                $.approveReliefInfo.tabsObj.tabs('select', 0);
                $.approveReliefFinish.applyReliefInfoWin.window("open");
            })
            $(".acceptRelief").linkbutton({
                text:'同意',
                plain:true,
                //iconCls:'pic_17',
                onClick:function() {
                    var applyId = $(this).attr("applyId");
                    acceptOrRefuseRelief(applyId,2);
                }
            })
            $(".refuseRelief").linkbutton({
                text:'拒绝',
                plain:true,
                //iconCls:'pic_17',
                onClick:function() {
                    var applyId = $(this).attr("applyId");
                    acceptOrRefuseRelief(applyId,3);
                }
            })
        }
    });

    /** 表格分页组件 **/
    $.approveReliefFinish.pager = $.approveReliefFinish.dataGrid.datagrid('getPager');
    $.approveReliefFinish.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.approveReliefFinish.pg.page = pageNumber;
            $.approveReliefFinish.pg.rows = pageSize;
            $.approveReliefFinish.reloadDataGrid();
        }
    });
    $.approveReliefFinish.reloadDataGrid();
    function acceptOrRefuseRelief(applyId, status, memo){
        var tips = '';
        if(status == '3') {
            tips = '确定拒绝减免操作?';
        }else if (status = '2'){
            tips = '确定同意减免操作?';
        }
        var param = {
            id:applyId,
            applicationStatus:status,
            memo:memo
        }
        $.messager.confirm('确认',tips,function(r){ if (r) {
            $.ajaxPackage({
                type : 'post',
                url : $.approveReliefFinish.updateApplyReliefUrl,
                dataType : 'json',
                data : param,
                success : function (data,textStatus,jqXHR) {
                    var resCode = data.resCode;
                    var resMsg = data.resMsg;
                    //从服务器上获取到记录信息
                    var attachment = data.attachment;
                    if (resCode == '000000') {
                        $.approveReliefFinish.reloadDataGrid();
                        $.messager.alert('提示信息','操作成功!');
                    } else {
                        $.messager.alert('警告',resMsg,'error');
                    }
                },
                error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                    $.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
                },
                complete : function() {
                    $.applyReliefFinish.reloadDataGrid();
                }
            });
        }});
    }

    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.approveReliefFinish.pg.page = 1;
        $.approveReliefFinish.reloadDataGrid();
    });

    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.approveReliefFinish.searchForm.form('reset');
    });

    $.approveReliefInfo = {
        tabsObj : $("#applyReliefInfoTab"),
        /** 减免申请详情 **/
        applyDetailInfoUrl : global.contextPath + "/applyReliefRepayManager/applyDetailInfoPage",
        /** 减免审批操作详情 **/
        reliefOperateInfoUrl : global.contextPath + "/applyReliefRepayManager/reliefOperateInfoPage"
    }

})
