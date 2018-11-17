/**
 * Created by ym10094 on 2017/6/21.
 */
/**
 * Created by ym10094 on 2016/11/29.
 */
$(function() {
    $.applyReliefFinish = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/applyReliefRepayManager/applyReliefPage/234567/apply',
        /** 数据表格对象 **/
        dataGrid : $('#applyReliefFinishDataGrid'),
        applyReliefInfoWin:$("#applyReliefInfoWin"),
        tabsObj : $("#applyReliefInfoTab"),
        applyDetailInfoUrl : global.contextPath + "/applyReliefRepayManager/applyDetailInfoPage",
        /** 减免审批操作详情 **/
        reliefOperateInfoUrl : global.contextPath + "/applyReliefRepayManager/reliefOperateInfoPage",
        /** 分页控件 **/
        pager : undefined,
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
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if (!$.applyReliefFinish.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.applyReliefFinish.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.applyReliefFinish.dataGridUrl;
            /** 查询并加载数据**/
            $.applyReliefFinish.dataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.applyReliefFinish.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.applyReliefFinish.pg = {
        'page' : 1,
        'rows' : $.applyReliefFinish.pageSize
    }

    /** DataGrid初始化 **/
    $.applyReliefFinish.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.applyReliefFinish.pg,
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
            },{
                field : 'effectiveMoney',
                title : '生效减免金额',
                width : '10%'
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
                    return $.applyReliefFinish.applyTypes[value + ''] || '未知';
                },
            },
            {
                field : 'applicationStatus',
                title : '减免状态',
                width : '10%',
                formatter : function(value, row, index) {
                    return $.applyReliefFinish.applyStatuses[value + ''] || '未知';
                },
            },{
                field : 'operate',
                title : '操作',
                width : '5%',
                formatter:function(value,row,index){
                    //通过与申请中的状态能显示详情
                    if(row.applicationStatus == '1' || row.applicationStatus == '2'){
                        var elements = "";
                        if(row.applicationStatus){
                            elements = "<a href='javascript:void(0)' class='applyDetailInfo' applyId='"+ row.applyId +"'isSpecial='"+ row.isSpecial +"' contractNum='"+row.contractNum+"'>详情</a>&nbsp;&nbsp;";
                        }
                        return elements;
                    }
                }
            }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.applyReliefFinish.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.applyReliefFinish.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 页面自适应 **/
            $.applyReliefFinish.dataGrid.datagrid('resize');
            $(".applyDetailInfo").click(function(){
                var applyId = $(this).attr("applyId");
                var contractNum = $(this).attr("contractNum");
                var isSpecial = $(this).attr("isSpecial");
                var applyDetailInfoUrl = $.applyReliefFinish.applyDetailInfoUrl + "?applyId=" + applyId + "&contractNum=" + contractNum + "&specialReliefFlag=" + isSpecial;
                var reliefOperteInfoUrl = $.applyReliefFinish.reliefOperateInfoUrl + "?applyId=" + applyId;
                $("#jumpApplyReliefInfoPage").attr("src", applyDetailInfoUrl);
                $("#jumpReliefOperateInfoPage").attr("src", reliefOperteInfoUrl);
                /** 默认选中第一个选项卡 **/
                $.applyReliefFinish.tabsObj.tabs('unselect', 0);
                $.applyReliefFinish.tabsObj.tabs('select', 0);
                $.applyReliefFinish.applyReliefInfoWin.find("#repayForm").form('clear');
                if (isSpecial == "1") {
                    $.applyReliefFinish.tabsObj.tabs('close', 1);
                }
                    $.applyReliefFinish.applyReliefInfoWin.window("open");
            })
        }
    });

    /** 表格分页组件 **/
    $.applyReliefFinish.pager = $.applyReliefFinish.dataGrid.datagrid('getPager');
    $.applyReliefFinish.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.applyReliefFinish.pg.page = pageNumber;
            $.applyReliefFinish.pg.rows = pageSize;
            $.applyReliefFinish.reloadDataGrid();
        }
    });
    $.applyReliefFinish.reloadDataGrid();
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.applyReliefFinish.pg.page = 1;
        $.applyReliefFinish.reloadDataGrid();
    });

    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.applyReliefFinish.searchForm.form('reset');
    });
})
