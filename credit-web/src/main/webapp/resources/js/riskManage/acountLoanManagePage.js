$(function() {
    $.acountLoanManage = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/riskManage/acountLoanManageSearch',
        /** 取消操作地址**/
        cancelAssignStateUrl :global.contextPath + '/riskManage/cancelAssignState',
        /**导入债权地址**/
        importCreditFileUrl :global.contextPath + '/riskManage/importLoanInfExtFile',
        /** 客户回访信息表格 **/
        acountLoanManageDataGrid : $('#acountLoanManageDataGrid'),
        /** Excel 导入窗口 **/
        importExcelWin : $('#importExcelWin'),
        /** 导入信贷系统Form **/
        creditFileForm : $('#creditFileForm'),

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
            if ($.acountLoanManage.searchForm.form('validate')) {
                /** 获取查询表单数据转换成JSON对象 **/
                var searchMsg = $.acountLoanManage.searchForm.serialize();
                /** 对参数进行解码(显示中文) **/
                searchMsg = decodeURIComponent(searchMsg);
                /** 字符串转换为对象 **/
                var queryParam = $.serializeToJsonObject(searchMsg);  
                queryParam.url = $.acountLoanManage.dataGridUrl;
                $.acountLoanManage.acountLoanManageDataGrid.datagrid('reloadData',queryParam);
            }
        }
    };
    
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.acountLoanManage.pg = {
        'page' : 1,
        'rows' : $.acountLoanManage.pageSize
    }
    
    /** DataGrid初始化 **/
    $.acountLoanManage.acountLoanManageDataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.acountLoanManage.pg,
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
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        /** 列定义 * */
        columns : [ [ {
            field : 'loanId',
            title : '债权Id',
            hidden: true
        }, {
            field : 'name',
            title : '借款人',
            width : '12%'
        }, {
            field : 'loanType',
            title : '借款类型',
            width : '12%'
        },{
            field : 'contractNum',
            title : '合同编号',
            width : '12%'
        },{
            field : 'idNum',
            title : '身份证号',
            width : '12%'
        },{
            field : 'mobile',
            title : '手机号',
            width : '12%'
        }, {
            field : 'assignState',
            title : '操作',
            width : '5%',
            formatter:function(value,row,index){
                if(value == '1'){
                    return "<a href='javascript:void(0)' onclick='cancelAssignState("+row.loanId+");' class='assignStateCancel' loanId='"+row.loanId+"' >取消</a>";
                }
                return '';
            }
        } ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.acountLoanManage.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.acountLoanManage.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        }
    });
    
    /** 表格分页组件 **/
    $.acountLoanManage.pager = $.acountLoanManage.acountLoanManageDataGrid.datagrid('getPager');
    $.acountLoanManage.pager.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.acountLoanManage.pg.page = pageNumber;
            $.acountLoanManage.pg.rows = pageSize;
            if(searhCheck()){
                $.acountLoanManage.reloadDataGrid();
            }
        }
    });
    
    /** 页面自适应 **/
    $.acountLoanManage.acountLoanManageDataGrid.datagrid('resize');
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        if(!$(this).linkbutton("options").disabled){
            $.acountLoanManage.pg.page = 1;
            if(searhCheck()){
                $.acountLoanManage.reloadDataGrid();
            }
        }
    })
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        if(!$(this).linkbutton("options").disabled){
            $.acountLoanManage.searchForm.form('reset');
        }
    })

    /**  显示导入窗口事件 **/
    $('#importBtn').click(function(){
        $.acountLoanManage.creditFileForm.form('clear');
        $.acountLoanManage.importExcelWin.window('open');
    })

    /** 查询校验 **/
    function searhCheck(){
        /** 获取查询表单数据转换成JSON对象 **/
        var searchMsg = $.acountLoanManage.searchForm.serialize();
        /** 对参数进行解码(显示中文) **/
        searchMsg = decodeURIComponent(searchMsg);
        /** 字符串转换为对象 **/
        var queryParam = $.serializeToJsonObject(searchMsg);
        /** 未输入查询条件不给予查询 **/
        /*
        if ($.isEmpty(queryParam.name) 
            && $.isEmpty(queryParam.mobile) 
            && $.isEmpty(queryParam.idNum)
            && $.isEmpty(queryParam.contractNum)) {
            $.messager.alert('警告','请至少输入一个查询条件!','warning');
            return false;
        }
        */
        return true;
    }

    /**  导入Excel（信贷系统）事件 **/
    $('#importExcelBut').click(function(){
        /** 判断是否选中文件 **/
        var fileName = $.acountLoanManage.creditFileForm.find('input[type=file]').val();
        if ($.isEmpty(fileName)) {
            $.messager.alert('提示信息','请选择上传的文件!','warning');
            return;
        }
        $.messager.confirm('上传文件','确定上传文件?',function(r){
                if (r) {
                    $.acountLoanManage.creditFileForm.ajaxSubmit({
                        type : 'post',
                        dataType : 'json',
                        url : $.acountLoanManage.importCreditFileUrl,
                        hasDownloadFile : true,
                        success: function (data) {
                            $.messager.alert('信息','操作成功');
                            $.acountLoanManage.importExcelWin.window('close');
                            $.acountLoanManage.reloadDataGrid(true);
                        },
                        error: function (data) {
                            $.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
                        }
                    });
                }}
        );
    })

})
function cancelAssignState(loanId){
    var loanId = loanId;
    $.messager.confirm("提示","确定取消操作吗？",function(r){
        if(r){
            $.ajaxPackage({
                type : 'post',
                url : $.acountLoanManage.cancelAssignStateUrl+"/"+loanId,
                dataType : "json",
                success : function (data) {
                    var resCode = data.resCode;
                    var resMsg = data.resMsg;
                    if (resCode == '000000') {
                        $.messager.alert('提示','操作成功!','info');
                    } else {
                        //操作失败
                        $.messager.alert('警告',resMsg,'warning');
                    }
                },
                error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                    $.messager.alert('异常',textStatus + '  :  ' + errorThrown + '!','error');
                },
                complete : function() {
                    $.acountLoanManage.reloadDataGrid();
                }
            });
        }
    })
}
