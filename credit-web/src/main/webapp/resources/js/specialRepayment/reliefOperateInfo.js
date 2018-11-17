$(function(){
    $.ReliefOperateInfo = {
        dataGridUrl: global.contextPath + "/applyReliefRepayManager/queryReliefOperateInfos",
        operateDataGrid:$("#operateDataGrid"),
        searchForm:$("#reliefOperateForm"),
        applyReliefInfoTab:window.parent.$("#applyReliefInfoTab"),
        validate: function () {
            if (!$.ReliefOperateInfo.searchForm.form('validate')) {
                return false;
            }
            return true;
        },
        reloadDataGrid: function () {
            if (!$.ReliefOperateInfo.validate()) {
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.ReliefOperateInfo.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 查询并加载数据**/
            $.ajaxPackage({
                type : "get",
                url : $.ReliefOperateInfo.dataGridUrl,
                dataType : "json",
                data : queryParam,
                success : function (data,textStatus,jqXHR) {
                    var resCode = data.resCode;
                    var resMsg = data.resMsg;
                    var attachment = data.attachment;
                    if (resCode == '000000') {
                        /** 服务端返回正常，填充数据 * */
                        $.ReliefOperateInfo.operateDataGrid.datagrid('loadData',attachment);
                    } else if(resCode == '800000'){
                        /** 操作警告提示 * */
                        $.messager.alert('警告',resMsg,'warning');
                    } else {
                        /** 操作失败 * */
                        $.messager.alert('异常信息',resMsg,'error');
                    }
                },
                error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                    parent.$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
                }
            });
        }
    }

    $.ReliefOperateInfo.operateDataGrid.datagrid({
        rownumbers: true,
        singleSelect: false,
        collapsible: false,
        fitColumns: true,
        showHeader:true,
        resizeHandle:true,
        showColumn:true,
        fit : true,
        columns:[[
            {
                field:'usercode',
                title:'操作员工号',
                width:"10%"
            },
            {
                field:'operateName',
                title:'操作员姓名',
                width:"10%"
            },
            {
                field:'operateDate',
                title:'操作时间',
                width:"20%",
                formatter:$.DateUtil.dateFormatToFullStr
            },
            {
                field:'operateStatus',
                title:'操作结果',
                width:"10%"
            },
            {
                field:'memo',
                title:'备注',
                width:"40%"
            }
        ]]
    });
    //$.ReliefOperateInfo.reloadDataGrid();
    $.ReliefOperateInfo.applyReliefInfoTab.tabs({
        onSelect:function(){
            loanReliefOperateInfo();
        }
    });
    function loanReliefOperateInfo(){
        var tab = $.ReliefOperateInfo.applyReliefInfoTab.tabs('getSelected');
        var index = $.ReliefOperateInfo.applyReliefInfoTab.tabs('getTabIndex',tab);
        if (1 == index) {
            $.ReliefOperateInfo.reloadDataGrid();
        }
    }
    loanReliefOperateInfo();
})