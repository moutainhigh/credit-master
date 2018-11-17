$(function() {
    $.offLineConfig = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/offLineOffer/offLineConfig/search',
        /** 设置行别、行号url **/
        setRegionTypeUrl : global.contextPath + '/offLineOffer/offLineConfig/setRegionType',
        /** 数据信息表格 **/
        dataGrid : $('#dataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if(!$.offLineConfig.searchForm.form("validate")){
                return false;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.offLineConfig.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数 **/
            queryParam.url = $.offLineConfig.dataGridUrl;
            /** 查询处理 **/
            $.offLineConfig.dataGrid.datagrid('reloadData', queryParam);
        }
    };

    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.offLineConfig.pg = {
        'page' : 1,
        'rows' : $.offLineConfig.pageSize
    };

    /** 网格数据对象初始化 **/
    $.offLineConfig.dataGrid.datagrid({
        pg : $.offLineConfig.pg,
        /** 查询超时时间，暂时设定为3分钟 **/
        timeout : 180000,
        /** 提交方式 * */
        method : 'get',
        /** 是否显示行号 * */
        rownumbers : true,
        /** 是否单选 * */
        singleSelect : false,
        /** 是否可折叠的 * */
        collapsible : false,
        /** 自适应列宽 * */
        fitColumns : true,
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        fit : true,
        /** 是否开启分页 * */
        pagination : true,
        /** 加载数据提示 * */
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            /** 列定义 * */
            {
                field : 'loanId',
                title : 'loanId',
                hidden : true
            }, {
                field : 'id',
                title : 'id',
                checkbox:true
            }, {
                field : 'contractNum',
                title : '合同编号',
                width : '10%'
            }, {
                field : 'fundsSources',
                title : '合同来源',
                width : '8%'
            }, {
                field : 'name',
                title : '客户姓名',
                width : '8%'
            }, {
                field : 'idNum',
                title : '身份证号',
                width : '15%'
            }, {
                field : 'account',
                title : '银行账号',
                width : '12%'
            }, {
                field : 'bankName',
                title : '银行名称',
                width : '10%'
            }, {
                field : 'fullName',
                title : '支行名称',
                width : '15%'
            }, {
                field : 'bankType',
                title : '行别',
                width : '8%'
            }, {
                field : 'bankNo',
                title : '行号',
                width : '10%'
            }
        ]],
            
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.offLineConfig.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.offLineConfig.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 **/
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        /** 加载数据完成后的回调方法 **/
        onLoadSuccess : function(data) {
            $.offLineConfig.dataGrid.datagrid('resize');
        }
    });

    /** 分页处理 **/
    $.offLineConfig.pager = $.offLineConfig.dataGrid.datagrid('getPager');
    $.offLineConfig.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.offLineConfig.pg.page = pageNumber;
            $.offLineConfig.pg.rows = pageSize;
            $.offLineConfig.reloadDataGrid();
        }
    });
    
    /** 查询处理 **/
    $('#searchBtn').click(function() {
        $.offLineConfig.pg.page = 1;
        $.offLineConfig.reloadDataGrid();
    });
    
    /** 设置行号 **/
    $("#settingBtn").click(function(){
        // 获取选中的行
        //var rows = $.offLineConfig.datagrid.datagrid('getSelections');
        var rows = $("#dataGrid").datagrid('getSelections');
        if(rows.length == 0){
            $.messager.alert('警告','请至少选中一条记录更新！','warning');
            return false;
        }
        // 银行卡所属地区
        var regionType = $('#selectRegionType').combobox('getValue');
        if($.isEmpty(regionType)){
            $.messager.alert('警告','请选择银行卡所属地区！','warning');
            return false;
        }
        // 收集请求参数
        var json = getParams(rows);
        // 银行卡所属地区（名称）
        var regionTypeName = $('#selectRegionType').combobox('getText');
        $.messager.confirm("提示","确认将所选银行卡的所属地区设置为【"+ regionTypeName +"】吗？",function(r){
            if(r){
                // 发送请求
                $.ajaxPackage({
                    url:$.offLineConfig.setRegionTypeUrl,
                    type:"post",
                    data:json,
                    dataType:"json",
                    success:function(response, textStatus, jqXHR){
                        var resCode = response.resCode;
                        var resMsg = response.resMsg;
                        if(resCode != "000000"){
                            $.messager.alert("警告", resMsg, "warning");
                            return;
                        }
                        $.messager.alert("提示", "设置成功！", "info");
                        // 重新刷新页面
                        $.offLineConfig.reloadDataGrid();
                    },
                    error:function(response, textStatus, jqXHR){
                        $.messager.alert("异常信息", "操作异常", "error");
                    },
                    complete:function(jqXHR,textStatus){
                    }
                });
            }
        });
    });
    
    /** 重置处理 **/
    $("#clearBtn").click(function() {
        if (!$(this).linkbutton("options").disabled) {
            $.offLineConfig.searchForm.form("reset");
        }
    });
    
    /** 收集请求参数（以json的格式）**/
    function getParams(rows){
        // 收集债权银行账户ID
        var bankIds = "";
        for(var i=0;i<rows.length;i++){
            if(bankIds ==""){
                bankIds = rows[i].loanBankId;
            }else{
                bankIds = bankIds +","+rows[i].loanBankId;
            }
        }
        // 银行卡所属地区
        var regionType = $('#selectRegionType').combobox('getValue');
        var json = {"bankIds":bankIds,"regionType":regionType};
        return json;
    }
});
