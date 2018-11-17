$(function () {
    $.nodeEmployyMnanage = {
        /** 表格数据源地址 **/
        dataGridUrl: global.contextPath + '/applyReliefRepayManager/approveNodeEmployeeManagePage',
        findNodeTreeUrl: global.contextPath + '/applyReliefRepayManager/queryNodeTree',
        insertNodeEmployeeUrl: global.contextPath + '/applyReliefRepayManager/insertNodeEmployee',
        deleteNodeEmployeeUrl: global.contextPath + '/applyReliefRepayManager/deleteNodeEmployee',
        /** 数据表格对象 **/
        dataGrid: $('#nodeEmployeeDatagrid'),
        /** 分页控件 **/
        pager: undefined,
        /** 查询条件数据项表单实例 **/
        searchForm: $('#searchForm'),
        addNodeEmployeeWin: $("#addNodeEmployeeWin"),
        /** 每页显示的记录条数，默认为10 **/
        pageSize: 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList: [10, 20, 30, 40, 50],
        /** 加载表格数据 **/
        reloadDataGrid: function () {
            if (!$.nodeEmployyMnanage.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.nodeEmployyMnanage.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.nodeEmployyMnanage.dataGridUrl;
            /** 查询并加载数据**/
            $.nodeEmployyMnanage.dataGrid.datagrid('reloadData', queryParam);
        },
        /** 查询校验 **/
        validate: function () {
            if (!$.nodeEmployyMnanage.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.nodeEmployyMnanage.pg = {
        'page': 1,
        'rows': $.nodeEmployyMnanage.pageSize
    }

    /** DataGrid初始化 **/
    $.nodeEmployyMnanage.dataGrid.datagrid({
        /** 分页参数对象 **/
        pg: $.nodeEmployyMnanage.pg,
        /** 提交方式 **/
        method: 'get',
        /** 是否显示行号 **/
        rownumbers: true,
        /** 是否单选 **/
        singleSelect: true,
        /** 是否可折叠的 **/
        collapsible: false,
        /** 自适应列宽 **/
        fitColumns: true,
        /** 设置true，表示适应它的父容器 **/
        fit: true,
        /** 是否开启分页 **/
        pagination: true,
        /** 数据长度超出，自动换行 **/
        nowrap: true,
        /** 禁止服务端排序 **/
        remoteSort: false,
        /** 加载提示信息 **/
        loadMsg: '数据加载中,请稍等...',
        // 锁定列定义
        hideColumn: [[{
            field: 'id',
            width: 50
        }]],
        columns: [[
            // 列定义
            {
                field: 'name',
                title: '姓名',
                width: '10%'
            },
            {
                field: 'userCode',
                title: '工号',
                width: '10%'
            },
            {
                field: 'nodeName',
                title: '环节',
                width: '10%'
            },
            {
                field: 'createTime',
                title: '创建时间',
                width: '10%',
                formatter: $.DateUtil.dateFormatToFullStr
            },
            {
                field: 'operate',
                title: '操作',
                width: '10%',
                formatter: function (value, row, index) {
                    if (row) {
                        var elements = "";
                        elements = "<a href='javascript:void(0)' class='deleteNodeEmployee' id='" + row.id + "'>删除</a>&nbsp;&nbsp;";
                        return elements;
                    }
                }
            }]],
        /** 每页显示的记录条数，默认为10 **/
        pageSize: $.nodeEmployyMnanage.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList: $.nodeEmployyMnanage.pageSizeList,
        /** 工具栏 **/
        toolbar: '#tb',
        /** 自定义行样式 * */
        rowStyler: function (index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess: function (data) {
            /** 页面自适应 **/
            $(".deleteNodeEmployee").click(function () {
                var id = $(this).attr("id");
                var params = {
                    "id": id
                }
                var url = $.nodeEmployyMnanage.deleteNodeEmployeeUrl
                ajaxDeleteNodeEmployee(params, url);
            })
        }
    });

    /** 表格分页组件 **/
    $.nodeEmployyMnanage.pager = $.nodeEmployyMnanage.dataGrid.datagrid('getPager');
    $.nodeEmployyMnanage.pager.pagination({
        onSelectPage: function (pageNumber, pageSize) {
            $.nodeEmployyMnanage.pg.page = pageNumber;
            $.nodeEmployyMnanage.pg.rows = pageSize;
            $.nodeEmployyMnanage.reloadDataGrid();
        }
    });
    /** 查询处理 **/
    $("#searchBtn").click(function () {
        $.nodeEmployyMnanage.pg.page = 1;
        $.nodeEmployyMnanage.reloadDataGrid();
    });

    /** 重置处理 **/
    $('#clearBtn').click(function () {
        $.nodeEmployyMnanage.searchForm.form('reset');
    });

    $("#createNodeEmployeeBtn").click(function () {
        $.ajaxPackage({
            type: 'get',
            url: $.nodeEmployyMnanage.findNodeTreeUrl,
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                var resCode = data.resCode;
                var treeData = data.attachment;
                var resMsg = data.resMsg;
                if (resCode == '000000') {
                    $('#nodeTree').tree('loadData', treeData);
                } else if (resCode == '800000') {
                    /** 操作警告提示 * */
                    $.messager.alert('警告', resMsg, 'warning');
                } else {
                    /** 操作失败 * */
                    $.messager.alert('异常信息', resMsg, 'error');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown, d) {
                $.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
            },
            complete: function () {

            }
        });
        $.nodeEmployyMnanage.addNodeEmployeeWin.window("open");
    });

    // 点击提交，获取选中的角色checkbox集合
    $("#addNodeEmployeeSubmitBut").click(function () {
        var userCode = $('#addNodeEmployeefForm').find('input[name="userCode"]').val();
        var nodes = $('#nodeTree').tree('getChecked');
        var nodeIds = '';
        for (var i = 0; i < nodes.length; i++) {
            if (nodeIds != '')
                nodeIds += ',';
            nodeIds += nodes[i].id;
        }
        var params = {
            "userCode": userCode,
            "nodeIds": nodeIds
        }
        $.ajaxPackage({
            type: 'post',
            url: $.nodeEmployyMnanage.insertNodeEmployeeUrl,
            data: params,
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                // 从服务器上获取到记录信息
                var resCode = data.resCode;
                var resMsg = data.resMsg;
                if (resCode == '000000') {
                    $.messager.alert("操作提示", "操作成功！");
                    $.nodeEmployyMnanage.addNodeEmployeeWin.window("close");
                    $.nodeEmployyMnanage.reloadDataGrid();
                } else if (resCode == '800000') {
                    /** 操作警告提示 * */
                    $.messager.alert('警告', resMsg, 'warning');
                } else {
                    /** 操作失败 * */
                    $.messager.alert('异常信息', resMsg, 'error');
                }
            },
            error: function (XMLHttpRequest, textStatus,
                             errorThrown, d) {
                $.messager.alert('异常信息', textStatus + '  :  '
                    + errorThrown + '!', 'error');
            },
            complete: function () {
            }
        });
    });

    $("#addNodeEmployeeCloseBut").click(function () {
        $.nodeEmployyMnanage.addNodeEmployeeWin.window("close");
    });

    $.nodeEmployyMnanage.addNodeEmployeeWin.window({
        onBeforeClose: function () {
            console.log("清空分配环节窗口！");
            clearAddNodeEmployeeWin();
        }
    });
    function clearAddNodeEmployeeWin() {
        $('#addNodeEmployeefForm').form("clear");
    }

    function ajaxDeleteNodeEmployee(params, url) {
        $.ajaxPackage({
            type: 'post',
            url: url,
            data: params,
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                // 从服务器上获取到记录信息
                var resCode = data.resCode;
                var resMsg = data.resMsg;
                if (resCode == '000000') {
                    $.messager.alert("操作提示", "操作成功！");
                    $.nodeEmployyMnanage.reloadDataGrid();
                } else if (resCode == '800000') {
                    /** 操作警告提示 * */
                    $.messager.alert('警告', resMsg, 'warning');
                } else {
                    /** 操作失败 * */
                    $.messager.alert('异常信息', resMsg, 'error');
                }
            },
            error: function (XMLHttpRequest, textStatus,
                             errorThrown, d) {
                $.messager.alert('异常信息', textStatus + '  :  '
                    + errorThrown + '!', 'error');
            }
        });
    }

})
