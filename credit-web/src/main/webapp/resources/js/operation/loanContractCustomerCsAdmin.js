$(function() {
    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/csadmin/searchLoanContractCustomerServiceInfo';
    
    // 更新客服url
    var updateUrl = global.contextPath + '/operation/updateLoanContractCustomerServiceInfo';
    
    // 批量更新客服url
    var batchUpdateUrl = global.contextPath + '/csadmin/batchUpdateLoanContractCustomerServiceInfo';
    
    // 导出excel文件url
    var exportUrl = global.contextPath + '/csadmin/exportLoanContractCustomerServiceInfo';
    
    // 表格实例
    var loanContractCustomerDataGrid = $('#loanContractCustomerDataGrid');
    
    // 查询条件数据项表单实例
    var searchForm = $('#searchForm');
    
    // 更新客服表单对象
    var updateForm = $("#updateForm");
    
    // 每页显示的记录条数，默认为10
    var pageSize = 10;
    
    // 设置每页记录条数的列表
    var pageSizeList = [ 10, 20, 30, 40, 50 ];
    
    // 定义表格参数
    $.pg = {'page' : 1,'rows' : pageSize };

    loanContractCustomerDataGrid.datagrid({
        pg : $.pg,
        // 提交方式
        method : 'get',
        // 是否显示行号
        rownumbers : true,
        // 是否单选
        singleSelect : false,
        //是否可折叠的
        collapsible : false,
        // 自适应列宽
        fitColumns : true,
        fit : true,
        // 是否开启分页
        pagination : true,
        
        columns : [ [
        // 列定义
        {   field:'loanId',
            checkbox:true
        },{
            field : 'name',
            title : '借款人',
            width : 50
        },{
            field : 'idNum',
            title : '身份证号',
            width : 50,
            formatter:function(value,row,index){
                if(value){
                    if(value.length >4){
                        return "**"+value.substr(value.length -4);
                    }
                    return value;
                }
                return "";
            }
        },{
            field : 'loanType',
            title : '借款类型',
            width : 50
        },{
            field : 'requestMoney',
            title : '申请金额',
            width : 50,
            vType : 'rmb'
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        },{
            field : 'money',
            title : '审批金额',
            width : 50,
            vType : 'rmb'
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        },{
            field : 'pactMoney',
            title : '合同金额',
            width : 50,
            vType : 'rmb'
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        },{
            field : 'grantMoney',
            title : '放款金额',
            width : 50,
            vType : 'rmb'
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        },{
            field : 'time',
            title : '借款期限',
            width : 50
        },{
            field : 'requestDate',
            title : '申请日期',
            width : 50
        },{
        	field : 'contractNum',
        	title : '合同编号',
        	width : 50
        },{
            field : 'loanState',
            title : '借款状态',
            width : 50
        },{
            field : 'orgName',
            title : '所属营业部',
            width : 50
        },{
            field : 'crmId',
            title : '当前客服Id',
            width : 50,
            hidden: true
        },{
            field : 'crmName',
            title : '当前客服',
            width : 50
        },{
            field : 'inActive',
            title : '是否在职',
            width : 50,
            formatter:function(value,row,index){
                if(value){
                    if(value=="t"){
                        return "是";
                    }else{
                        return "否";
                    }
                }
                return "";
            }
        }] ],
        // 每页显示的记录条数，默认为10
        pageSize : pageSize,
        // 可以设置每页记录条数的列表
        pageList : pageSizeList,
        // 工具条
        toolbar : "#tb",
        //footer  : "#footer",
        // 自定义行样式
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
                
            }
        }
    });
    
    // 表格分页实例
    var loanContractCustomerDataGridPG = loanContractCustomerDataGrid.datagrid('getPager');
    loanContractCustomerDataGridPG.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.pg.page = pageNumber;
            $.pg.rows = pageSize;
            if(searchForm.form("validate")){
                if(!check()){
                     $.messager.alert('警告','请至少输入一个查询条件!','warning');
                     return;
                }
                reloadDataGrid();
            }
        }
    });
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        if(searchForm.form("validate")){
            // 查询校验
            var flag = check();
            if(!flag){
                $.messager.alert('警告','请至少输入一个查询条件!','warning');
                return;
            }
            $.pg.page = 1;
            reloadDataGrid();
        }
    })
    
    /** 查询条件必须输入其中之一的校验 **/
    function check(){
        var name = $.trim($("#name").val());
        var mobile = $.trim($("#mobile").val());
        var idNum = $.trim($("#idNum").val());
        var crmId = $('#crmId').combobox('getValue');
        var loanState = $('#loanState').combobox('getValue');
        var contractNum = $.trim($("#contractNum").val());
        var orgId = $('#orgId').combobox('getValue');
        if($.isEmpty(name) && $.isEmpty(mobile) && $.isEmpty(idNum) && $.isEmpty(crmId)&& $.isEmpty(loanState)&& $.isEmpty(contractNum)&& $.isEmpty(orgId)){
            return false;
        }
        // 防止输入空白查询
        $("#name").val(name);
        $("#mobile").val(mobile);
        $("#idNum").val(idNum);
        $("#contractNum").val(contractNum);
        return true;
    }
    
    /** 批量更新处理 **/
    $("#batchUpdateBtn").click(function(){
        var rows = loanContractCustomerDataGrid.datagrid('getSelections');
        var flag = updateCheck(rows);
        if(!flag){
            return;
        }
        var loanIds = "";
        for(var i=0;i<rows.length;i++){
            if(loanIds==""){
                loanIds = rows[i].loanId;
            }else{
                loanIds = loanIds+","+rows[i].loanId;
            }
        }
        $("#loanIds").val(loanIds);
        var newCrmName = $("#newCrmId").combobox("getText");
        var word = "当前所选客服将变更为："+newCrmName+"。请确认！";
        $.messager.confirm("提示",word,function(r){
            if(r){
                updateForm.form('submit', {
                    url: batchUpdateUrl,
                    onSubmit: function(){
                    },
                    success:function(data){
                        data = JSON.parse(data);
                        var resCode = data.resCode;
                        if(resCode == '000000'){
                            $.messager.alert('提示','更改成功!','info');
                            reloadDataGrid();
                        }else{
                            $.messager.alert('异常','变更借款合同客服发生异常!','error');
                        }
                    }
                });
            }
        });
    });
    
    /** 批量更新校验 **/
    function updateCheck(rows){
        if(rows.length == 0){
            $.messager.alert('警告','请至少选中一条记录更新','warning');
            return false;
        }
        // 新客服
        var newCrmId = $("#newCrmId").combobox("getValue");
        if($.isEmpty(newCrmId)){
            $.messager.alert('警告','请选择一个新客服','warning');
            return false;
        }
        
        // 变更前后是否同一个客服
        var isSameCrm = false;
        // 客服是否在职
        var isInActive = false;
        for(var i=0;i<rows.length;i++){
            if(rows[i].inActive=="t"){
                isInActive = true;
            }
            if(newCrmId== rows[i].crmId){
                isSameCrm = true;
            }
        }
        
        /*if(isInActive){
            $.messager.alert('警告','选择的客服中存在尚未离职的，无法更改','warning');
            return false;
        }*/
        
        if(isSameCrm){
            $.messager.alert('警告','新客服和当前客服不能是同一个，请重新选择新客服','warning');
            return false;
        }
        return true;
    }
    
    /** Excel导出处理 **/
    $("#exportBtn").click(function(){
        if(!searchForm.form("validate")){
            return;
        }
        // 导出校验
        var flag = check();
        if(!flag){
            $.messager.alert('警告','请至少输入一个查询条件,此条件用于查询导出数据!','warning');
            return;
        }
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
    });
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        searchForm.form('reset');
    })
    
    $('#orgId').combobox({
    url: global.contextPath +'/csclose/searchShutedShop',
    method: 'post',
    valueField:'id',
    textField:'name',
    loadFilter: function(e) {
    	var content = e;
        var o = [{ 'id': "", 'name': '--请选择--' }];
        $('#orgId').combobox("select", "");
        return o.concat(content);
    }
    });
    
    // 加载表格数据
    function reloadDataGrid() {
        // 获取查询表单数据转换成JSON对象
        var searchMsg = searchForm.serialize();
        // 对参数进行解码(显示中文)
        searchMsg = decodeURIComponent(searchMsg);
        var queryParam = $.serializeToJsonObject(searchMsg);
        queryParam.url = dataGridUrl;
        queryParam.r = new Date().getTime();
        loanContractCustomerDataGrid.datagrid('reloadData', queryParam);
    }
})
