$(function() {
    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/repay/searchOnlineRepaymentDetail';
    
    // 导出excel文件url
    var exportUrl = global.contextPath + '/repay/exportOnlineRepaymentDetail';
    
    // 表格实例
    var repaymentDetailDataGrid = $('#repaymentDetailDataGrid');
    
    // 查询条件数据项表单实例
    var searchForm = $('#searchForm');
    
    // 还款日期初始化
    $("#tradeDate").datebox("addClearButton","icon-clear");
    
    // 每页显示的记录条数，默认为10
    var pageSize = 10;
    
    // 设置每页记录条数的列表
    var pageSizeList = [ 10, 20, 30, 40, 50 ];
    
    // 定义表格参数
    $.pg = {'page' : 1,'rows' : pageSize };

    repaymentDetailDataGrid.datagrid({
        pg : $.pg,
        // 提交方式
        method : 'get',
        // 是否显示行号
        rownumbers : true,
        // 是否单选
        singleSelect : true,
        //是否可折叠的
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
            width : 50
        },{
            field : 'loanType',
            title : '借款类型',
            width : 50
        },{
            field : 'idnum',
            title : '身份证号',
            width : 50
        },{
            field : 'amount',
            title : '交易金额',
            width : 50,
            vType : 'rmb'
        },{
            field : 'fx',
            title : '罚息',
            width : 50,
            vType : 'rmb'
        },{
            field : 'jmfx',
            title : '减免金额',
            width : 50,
            vType : 'rmb'
        },{
            field : 'fxj',
            title : '风险金',
            width : 50,
            vType : 'rmb'
        },{
            field : 'lx',
            title : '利息',
            width : 50,
            vType : 'rmb'
        },{
            field : 'bj',
            title : '本金',
            width : 50,
            vType : 'rmb'
        },{
            field : 'tradeDate',
            title : '交易日期',
            width : 50,
            formatter:function(value,row,index){
                if(value){
                    return $.dates.formatter(value);
                }
            }
        },{
            field : 'loanState',
            title : '借款状态',
            width : 50
        },{
	    	field : 'contractNum',
	    	title : '合同编号',
	    	width : 50
	    }] ],
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
    
    // 表格分页实例
    var repaymentDetailDataGridPG = repaymentDetailDataGrid.datagrid('getPager');
    repaymentDetailDataGridPG.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.pg.page = pageNumber;
            $.pg.rows = pageSize;
            checkValue();
            if(validate()){
                reloadDataGrid();
            }
        }
    });
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        checkValue();
        if(validate()){
            $.pg.page = 1;
            reloadDataGrid();
        }
    })
    
    /** 查询条件必须输入校验 **/
    function check(){
        // 管理营业部
        var salesDepartment = $('#salesDepartmentId').combobox('getValue');
        if($.isEmpty(salesDepartment)){
            $.messager.alert('警告','请选择管理营业部!','warning');
            return false;
        }
        // 还款日期
        var tradeDate = $('#tradeDate').datebox('getValue');
        if($.isEmpty(tradeDate)){
            $.messager.alert('警告','请输入还款日期!','warning');
            return false;
        }
    }
   
    /** Excel导出处理 **/
    $("#exportBtn").click(function(){
        checkValue();
        if(validate()){
            $.messager.confirm("提示","确认要导出Excel文件吗？",function(r){
                if(r){
                    $.downloadFile({
                        url:exportUrl,
                        isDownloadBigFile:true,
                        params:searchForm.serializeObject(),
                        successFunc:function(data){
                            if(data== null){
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
            });
        }
    });
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        searchForm.form('reset');
    });
    
    /** 在下拉框中输入值，如果匹配到下拉框的值，则会被选中，否则就清空 **/
    function checkValue(){
        // 如果输入了管理营业部，但是没有匹配到下拉框的值，则会被清空
        $("#salesDepartmentId").combobox("checkValue");
    }
    
    /** 表单校验 **/
    function validate(){
        if(!searchForm.form("validate")){
            //$.messager.alert('警告','查询条件输入不合法!','warning');
            return false;
        }
        return true;
    }
    
    // 加载表格数据
    function reloadDataGrid() {
        // 获取查询表单数据转换成JSON对象
        var searchMsg = searchForm.serialize();
        // 对参数进行解码(显示中文)
        searchMsg = decodeURIComponent(searchMsg);
        var queryParam = $.serializeToJsonObject(searchMsg);
        queryParam.url = dataGridUrl;
        repaymentDetailDataGrid.datagrid('reloadData', queryParam);
    }
});
