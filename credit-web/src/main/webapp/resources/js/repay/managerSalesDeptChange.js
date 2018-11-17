$(function() {
    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/repay/searchSalesDeptRepayInfo';
    
    // 批量导入的url
    var uploadUrl = global.contextPath + '/repay/batchImportSalesDeptRepayInfo';
    
    // 表格实例
    var repayDataGrid = $('#repayDataGrid');
    
    // 查询条件数据项表单实例
    var searchForm = $('#searchForm');
    
    // 批量导入窗口对象
    var importExcelWin = $("#importExcelWin");
    
    // 导入文件表单对象
    var baseFileForm = $("#baseFileForm");

    // 每页显示的记录条数，默认为10
    var pageSize = 10;
    
    // 设置每页记录条数的列表
    var pageSizeList = [ 10, 20, 30, 40, 50 ];
    
    // 定义表格参数
    $.pg = {'page' : 1,'rows' : pageSize };
        
    repayDataGrid.datagrid({
        pg : $.pg,
        //url : dataGridUrl,
        // 提交方式
        method : 'get',
        // 是否显示行号
        rownumbers : true,
        // 是否单选
        singleSelect : true,
        // //是否可折叠的
        collapsible : false,
        // 自适应列宽
        fitColumns : true,
        fit : true,
        // 是否开启分页
        pagination : true,
        
        columns : [ [
        // 列定义
        {
            field : 'name',
            title : '借款人',
            width : 30
        }, {
            field : 'idNum',
            title : '身份证号',
            width : 30
        }, {
            field : 'loanType',
            title : '借款类型',
            width : 30
        }, {
            field : 'signDate',
            title : '签约日期',
            width : 30
        }, {
            field : 'loanStatus',
            title : '借款状态',
            width : 30
        }, {
        	field : 'contractNum',
        	title : '合同编号',
        	width:30
        }, {
            field : 'signSalesDeptName',
            title : '放款营业部',
            width : 30
        }, {
            field : 'salesDepartmentName',
            title : '管理营业部',
            width : 30
        } ] ],
        // 每页显示的记录条数，默认为10
        pageSize : pageSize,
        // 可以设置每页记录条数的列表
        pageList : pageSizeList,
        // 工具条
        toolbar : "#tb",
        // 自定义行样式
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
                
            }
        }
    });
    
    var repayDataGridPG = repayDataGrid.datagrid('getPager');
    repayDataGridPG.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.pg.page = pageNumber;
            $.pg.rows = pageSize;
            $('#salesDepartmentId').combobox('checkValue');
            var flag = check();
            if(searchForm.form("validate") && flag){
                reloadDataGrid();
            }
        }
    });
    
    /** 查询处理 **/
    $("#searchBtn").click(function(noConditionTip){
        search(noConditionTip);
    });
    
    /** 查询 **/
    function search(noConditionTip){
        $('#salesDepartmentId').combobox('checkValue');
        $.pg.page = 1;
        var flag = check(noConditionTip);
        if(searchForm.form("validate") && flag){
            reloadDataGrid();
        }
    }
    
    /** 查询条件必须输入其中之一的校验 **/
    function check(noConditionTip){
        var name = $.trim($("#name").val());
        var idNum = $.trim($("#idNum").val());
        var loanType = $('#loanType').combobox('getValue');
        var salesDepartmentId = $('#salesDepartmentId').combobox('getValue');
        var contractNum = $.trim($("#contractNum").val());
        if($.isEmpty(name) && $.isEmpty(idNum) && $.isEmpty(loanType) && $.isEmpty(salesDepartmentId)&& $.isEmpty(contractNum)){
        	if (typeof noConditionTip == 'boolean' && noConditionTip) {
				
			} else {
				$.messager.alert('警告','请至少输入一个查询条件!','warning');
				return false;
			}
        }
        // 防止输入空白查询
        $("#name").val(name);
        $("#idNum").val(idNum);
        $("#contractNum").val(contractNum);
        return true;
    }

    // 加载表格数据
    function reloadDataGrid() {
        var searchMsg = searchForm.serialize();
        // 对参数进行解码(显示中文)
        searchMsg = decodeURIComponent(searchMsg);
        var queryParam = $.serializeToJsonObject(searchMsg);
        queryParam.url = dataGridUrl;
        repayDataGrid.datagrid("reloadData",queryParam);
    }
    
    /** 打开批量导入窗口 **/
    $('#batchImportBtn').click(function(){
        importExcelWin.window('open');
        baseFileForm.form('clear');
    })
    
    /** 批量导入处理 **/
    $("#batchImport").click(function(){
        var file = $("#uploadfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("提示","确认导入吗？",function(r){
            if(r){
                baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: uploadUrl,
                    hasDownloadFile : true,
                    success: function (data) {
                        setTimeout(function(){
                        	$.messager.alert('信息','操作成功');
                            importExcelWin.window('close');
                            search(true);
                        }, 1000);
                    },
                    error: function (data) {
                        $.messager.alert('警告',data.resMsg,'warning');
                    }
                });
            }
        });
    });
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        searchForm.form('reset');
    })
})
