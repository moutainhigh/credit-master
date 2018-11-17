$(function() {
    $.feeInput = {
        /** 表格数据源地址 * */
        dataGridUrl : global.contextPath + '/fee/feeInput/searchLoanFeeInputInfo',
        /** 单条收费录入地址 * */
        singleRepaymentUrl : global.contextPath + '/fee/feeInput/singleRepayment',
        /** 批量收费录入地址 * */
        batchRepaymentUrl : global.contextPath + '/fee/feeInput/batchRepayment',
        /** 客户信息表格 * */
        dataGrid : $('#dataGrid'),
        /** 还款窗口 * */
        repayWin : $('#repayWin'),
        /** Excel 导入窗口 * */
        importExcelWin : $('#importExcelWin'),
        /** 导入信贷系统Form * */
        creditFileForm : $('#creditFileForm'),
        /** 还款表单窗口 * */
        repayForm : $('#repayForm'),
        /** 分页控件 * */
        pager : undefined,
        /** 查询条件数据项表单实例 * */
        searchForm : $('#searchForm'),
        /** 每页显示的记录条数，默认为10 * */
        pageSize : 10,
        /** 设置每页记录条数的列表 * */
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载表格数据 * */
        reloadDataGrid : function(noConditionTip) {
            if (!$.feeInput.searchForm.form('validate')) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 * */
            var searchMsg = $.feeInput.searchForm.serialize();
            /** 对参数进行解码(显示中文) * */
            searchMsg = decodeURIComponent(searchMsg);
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 未输入查询条件不给予查询 * */
            if ($.isEmpty(queryParam.name) 
                    && $.isEmpty(queryParam.idNum)
                    && $.isEmpty(queryParam.contractNum)
                    && $.isEmpty(queryParam.grantMoneyDateStart)
                    && $.isEmpty(queryParam.grantMoneyDateEnd)
                    && $.isEmpty(queryParam.state)
                    && $.isEmpty(queryParam.fundsSources)) {
                if(typeof noConditionTip == 'boolean' && noConditionTip){
                    
                }else{
                    $.messager.alert('警告', '请至少输入一个查询条件！', 'warning');
                }
                return;
            } 
            queryParam.url = $.feeInput.dataGridUrl;
            $.feeInput.dataGrid.datagrid('reloadData', queryParam);
        },

        init : function() {
            /** 查询处理 **/
            $('#searchBtn').click(function() {
                $.feeInput.pg.page = 1;
                $.feeInput.reloadDataGrid();
            })

            /** 重置处理 **/
            $('#clearBtn').click(function() {
                $.feeInput.searchForm.form('reset');
            })
            
            /** 点击还款按钮事件 **/
            $('#repayBtn').click(function() {
                var row = $.dataGrid.getSelectedRow($.feeInput.dataGrid);
                if(!row){
                    $.messager.alert('警告', '请选中需要还款的客户！', 'warning');
                    return;
                }
                var curDate = $("#curDate").val();
                row.curDate = curDate;
                row.memo = "";
                /** 清空表单 **/
                $.feeInput.repayForm.form('clear');
                /** 默认选中一项 **/
                $('#tradeType').combobox('defaultOneItem');
                /** 加载表单数据 **/
                $.feeInput.repayForm.form('load',row);
                /** 打开还款窗口 **/
                $.feeInput.repayWin.window('open');
                /** 格式化金额（千分位） * */
                $.feeInput.comdify();
            })

            /** 点击还款确认处理 **/
            $('#repaySubmitBtn').click(function() {
                if (!$.feeInput.repayForm.form('validate')) {
                    return;
                }
                /** 还款录入金额 * */
                var amount = $("#repaymentAmount").val();
                if (parseFloat(amount) < 0.01) {
                    $.messager.alert('警告','还款金额不能小于0.01元,请修改！','warning');
                    return;
                }
                $.messager.confirm('提示','确定还款操作吗？',function(r) {
                    if (r) {
                        var json = {"loanId":$("#loanId").val(),"amount":amount,"tradeType":$("#tradeType").combobox("getValue"),"memo":$("#memo").val()};
                        $.ajaxPackage({
                            type : 'post',
                            url : $.feeInput.singleRepaymentUrl,
                            dataType : 'json',
                            data : json,
                            success : function(data,textStatus,jqXHR) {
                                var resCode = data.resCode;
                                var resMsg = data.resMsg;
                                // 从服务器上获取到记录信息
                                var attachment = data.attachment;
                                if (resCode != '000000') {
                                    $.messager.alert('警告',resMsg,'warning');
                                    return;
                                }
                                $.messager.alert('提示','还款录入成功！','info');
                                $.feeInput.repayWin.window('close');
                                $.feeInput.reloadDataGrid(true);
                            },
                            error : function(XMLHttpRequest,textStatus,errorThrown, d) {
                                $.messager.alert('异常',textStatus + '  :  ' + errorThrown + '!','error');
                            },
                            complete : function() {
                            }
                        });
                    }
                });
            })

            /** 关闭收费录入窗口 **/
            $('#repayCloseBtn').click(function() {
                $.feeInput.repayWin.window('close');
            })

            /** 打开导入窗口 **/
            $('#importBtn').click(function() {
                $.feeInput.importExcelWin.window('open');
                $.feeInput.creditFileForm.form('clear');
            })

            /** 批量导入收费录入 * */
            $('#importExcelBtn').click(function() {
                /** 判断是否选中文件 * */
                var file = $("#repayInfoFile").filebox("getValue");
                if ($.isEmpty(file)) {
                    $.messager.alert('警告', '请选择上传的文件！','warning');
                    return;
                }
                $.messager.confirm('提示','确定上传文件吗？',function(r) {
                    if (r) {
                        $.feeInput.creditFileForm.ajaxSubmit({
                            type : 'post',
                            dataType : 'json',
                            url : $.feeInput.batchRepaymentUrl,
                            hasDownloadFile : true,
                            success : function(data) {
                                $.messager.alert('提示','操作成功！','info');
                                $.feeInput.importExcelWin.window('close');
                                $.feeInput.reloadDataGrid(true);
                            },
                            error : function(data) {
                                $.messager.alert('警告',data.resCode + ':' + data.resMsg, 'warning');
                            }
                        });
                    }
                });
            })
        },
        /** 格式化金额（千分位） * */
        comdify : function() {
            $('input.thousand').each(function() {
                var value = $(this).val();
                value = $.comdify(value);
                value = '￥' + value;
                $(this).val(value);
            })
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.feeInput.pg = {
        'page' : 1,
        'rows' : $.feeInput.pageSize
    }

    /** dataGrid表格组件初始化 **/
    $.feeInput.dataGrid.datagrid({
        pg : $.feeInput.pg,
        /** 提交方式 **/
        method : 'get',
        /** 查询超时时间，暂时设定为3分钟 **/
        timeout : 180000,
        /** 是否显示行号 **/
        rownumbers : true,
        /** 是否单选 **/
        singleSelect : true,
        /** 是否可折叠的 **/
        collapsible : false,
        /** 自适应列宽 **/
        fitColumns : true,
        /** 自适应父窗口 **/
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        /** 加载数据提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        /** 锁定列定义 **/
        frozenColumns : [ [
        ] ],
        columns : [ [
        /** 列定义 **/
        {
            field : 'salesDepartmentName',
            title : '管理营业部',
            width : 50
        }, {
            field : 'name',
            title : '借款人',
            width : 30
        }, {
            field : 'idNum',
            title : '身份证号',
            width : 30,
            formatter:function(value,row,index){
                if(value){
                    if(value.length > 6){
                        return "**"+ value.substr(value.length - 6);
                    }
                    return value;
                }
                return "";
            }
        }, {
            field : 'contractNum',
            title : '合同编号',
            width : 50
        }, {
            field : 'loanType',
            title : '借款类型',
            width : 30
        }, {
            field : 'pactMoney',
            title : '合同金额',
            width : 40,
            vType : 'rmb'
        }, {
            field : 'time',
            title : '借款期限',
            width : 30
        }, {
            field : 'grantMoneyDate',
            title : '放款日期',
            width : 40,
            formatter : $.DateUtil.dateFormatToStr
        }, {
            field : 'state',
            title : '收费状态',
            width : 30
        }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.feeInput.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.feeInput.pageSizeList,
        toolbar : '#tb',
        /** 自定义行样式 **/
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 加载数据后自适应表格 **/
            $.feeInput.dataGrid.datagrid('resize');
        }
    });

    /** 分页组件初始化 **/
    $.feeInput.pager = $.feeInput.dataGrid.datagrid('getPager');
    $.feeInput.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.feeInput.pg.page = pageNumber;
            $.feeInput.pg.rows = pageSize;
            $.feeInput.reloadDataGrid();
        }
    });

    /** 还款窗口初始化 **/
    $.feeInput.repayWin.window({
        //定义窗口是不是模态窗口
        modal : true,
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
    
    /** 页面初始化 **/
    $.feeInput.init();
})