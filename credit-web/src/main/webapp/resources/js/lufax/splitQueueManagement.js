$(function() {
    $.splitQueueManagement = {
        /** 表格数据源地址 * */
        dataGridUrl : global.contextPath + '/splitQueueManagement/splitQueueManagementList',
        /**选择数据，发送分账信息 分账重发**/
        repeatSendUrl :global.contextPath + '/splitQueueManagement/splitQueueRepeatSend',
        /**发送所有未分账，分账失败的信息一键重发**/
        oneAllSendUrl : global.contextPath + '/splitQueueManagement/splitQueueOneAllSend',
        /** 同步还款计划Url**/
        syncRepaymentPlanUrl : global.contextPath + '/splitQueueManagement/syncRepaymentPlan',
        /** 同步还款记录Url**/
        syncRepaymentRecordUrl : global.contextPath + '/splitQueueManagement/syncRepaymentRecord',
        /** 同步还款明细Url**/
        syncRepaymentDetailUrl : global.contextPath + '/splitQueueManagement/syncRepaymentDetail',
        /** 编辑分账独立数据Url**/
        updateSplitQueueUrl : global.contextPath + '/splitQueueManagement/updateSplitQueue',
        /** 客户信息表格 * */
        dataGrid : $('#dataGrid'),
        /** 分页控件 * */
        pager : undefined,
        /** 查询条件数据项表单实例 * */
        searchForm : $('#searchForm'),
        /** 更新面板 **/
        updateDataPanel : $('#updateDataPanel'),
        /** 更新表单对象 **/
        dataForm : $('#dataForm'),
        /** 每页显示的记录条数，默认为10 * */
        pageSize : 10,
        /** 设置每页记录条数的列表 * */
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载表格数据 * */
        reloadDataGrid : function() {
            /** 获取查询表单数据转换成JSON对象 * */
            var searchMsg = $.splitQueueManagement.searchForm.serialize();
            /** 对参数进行解码(显示中文) * */
            searchMsg = decodeURIComponent(searchMsg);
            var queryParam = $.serializeToJsonObject(searchMsg);
            queryParam.url = $.splitQueueManagement.dataGridUrl;
            $.splitQueueManagement.dataGrid.datagrid('reloadData', queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if (!$.splitQueueManagement.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    };
    /** 分页参数（page:当前第N页，rows:一页N行） * */
    $.splitQueueManagement.pg = {
        'page' : 1,
        'rows' : $.splitQueueManagement.pageSize
    };
    var splitNotifyStates = {
            '1': '待通知', 
            '2': '已通知', 
            '3': '通知失败', 
            '4': '通知成功'    
        };
    var SplitResultStates = {
            '1' : '未分账',
            '2' : '分账中',
            '3' : '分账失败',
            '4' : '分账成功'        
        };
    var payOffTypes = {
            '1' : '未结算',
            '2' : '已结算'
        };
    var repayTypes = {
            '01' : '委托还款',
            '02' : '机构还款',
            '03' : '逾期代偿',
            '04' : '一次性回购',
            '05' : '提前结清',
            '06' : '逾期还回',
            '07' : '回购结清'
        }
    $.splitQueueManagement.dataGrid.datagrid({
        pg : $.splitQueueManagement.pg,
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
        fit : true,
        // height : '100%',
        /** 是否开启分页 * */
        pagination : true,
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
                /** 列定义 * */
                {
                    field : 'loanId',
                    title : 'loanId',
                    hidden : true
                },
                {
                    field : 'splitId',
                    title : 'splitId',
                    checkbox:true
                },
                {
                    field : 'name',
                    title : '客户姓名',
                    width : 5
                },
                {
                    field : 'idNum',
                    title : '证件号码',
                    width : 8
                },
                {
                    field : 'contractNum',
                    title : '合同编号',
                    width : 8
                },
                {
                    field : 'repayType',
                    title : '还款类型',
                    width : 5,
                    formatter : function(value, row, index) {
                        return repayTypes[value + ''] || '未知';
                    }
                },
                {
                    field : 'batchId',
                    title : '批次号',
                    width : 9
                },
                {
                    field : 'debitNo',
                    title : '序号',
                    width : 9
                },
                {
                    field : 'createTime',
                    title : '创建时间',
                    width : 8,
                    formatter : $.DateUtil.dateFormatToFullStr
                },
                {
                    field : 'splitNotifyState',
                    title : '通知状态',
                    width : 5,
                    formatter : function(value, row, index) {
                        return splitNotifyStates[value];    
                    }
                },
                {
                    field : 'splitResultState',
                    title : '分账状态',
                    width : 5,
                    formatter : function(value, row, index) {
                        return SplitResultStates[value];    
                    }
                },
                {
                    field : 'payOffType',
                    title : '是否已结算',
                    width : 5,
                    formatter : function(value, row, index) {
                        return payOffTypes[value];    
                    }
                },
                {
                    field : 'amount',
                    title : '冻结金额',
                    width : 5
                }
            ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.splitQueueManagement.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.splitQueueManagement.pageSizeList,
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess : function(data) {
            /** 页面自适应 **/
            $.splitQueueManagement.dataGrid.datagrid('resize');
        }
    });
    
    /** 分页组件初始化 **/
    $.splitQueueManagement.pager = $.splitQueueManagement.dataGrid.datagrid('getPager');
    $.splitQueueManagement.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.splitQueueManagement.pg.page = pageNumber;
            $.splitQueueManagement.pg.rows = pageSize;
            $.splitQueueManagement.reloadDataGrid();
        }
    });
    
    //查询处理
    $('#searchBut').click(function() {
        if ($("#searchForm").form("validate")) {
            $.splitQueueManagement.pg.page = 1;
            $.splitQueueManagement.reloadDataGrid();
        }
    });
    
    /** 重置处理 **/
    $("#clearCondition").bind("click", function(envent) {
        if (!$(this).linkbutton("options").disabled) {
            $("#searchForm").form("reset");
        }
    });
    
    /** 分账重发 **/
    $('#repeatSendBut').click(function(){
        var rows = $.splitQueueManagement.dataGrid.datagrid('getSelections');
        if(rows.length <= 0){
            $.messager.alert('警告',"请选择需同步陆金所分账的记录！",'warning');
            return ;
        }
        var splitIds = [];
        var repayTypes = [];
        for(var i=0;i<rows.length;i++){
            var splitNotifyState = rows[i].splitNotifyState;//分账通知状态
            var splitId = rows[i].splitId;
            var repayType = rows[i].repayType;
            var str = "";
            if(splitNotifyState == '1' || splitNotifyState == '3'){
                splitIds.push(splitId);
                if(repayType == '01'){
                     str += repayType+"="+splitId 
                }else if(repayType == '02'){
                    str += repayType+"="+splitId 
                }else if(repayType == '03'){
                    str += repayType+"="+splitId 
                }else if(repayType == '04'){
                    str += repayType+"="+splitId 
                }else if(repayType == '05'){
                    str += repayType+"="+splitId 
                }else if(repayType == '06'){
                    str += repayType+"="+splitId 
                }
                repayTypes.push(str);
            }
        }
        if(splitIds.length <= 0){
            $.messager.alert('警告',"请至少选择一条未分账或者分账失败的记录！",'warning');
            return ;
        }
        var tipMessage='您已选择了：' + splitIds.length + '条记录，确认要向陆金所发送分账信息吗？';
        top.$.messager.confirm('提示', tipMessage, function(r){
            if (r){
                var params = {"repayTypes":repayTypes.join()};
                ajaxSubmit($.splitQueueManagement.repeatSendUrl,params);
            }
        });
    });
    
    /** 一键重发 **/
    $('#oneAllSendBut').click(function(){
        var tipMessage='确定要将全部未分账和分账失败的记录同步给陆金所吗？';
        top.$.messager.confirm('提示', tipMessage, function(r){
            if (r){
                var params = {};
                ajaxSubmit($.splitQueueManagement.oneAllSendUrl,params);
            }
        });
    });
    
    /**
     * 异步
     * @param url
     * @param params
     */
    function ajaxSubmit(url,params){
        $.ajaxPackage({
            url:url,
            type:"post",
            data:params,
            dataType:"json",
            success:function(response, textStatus, jqXHR){
                var resCode = response.resCode;
                var resMsg = response.resMsg;
                if(resCode == "000000"){
                    $.messager.alert("提示",resMsg, "info");
                    // 刷新页面
                    $.splitQueueManagement.reloadDataGrid();
                }else{
                    $.messager.alert("警告", resMsg, "warning");
                }
            },
            error:function(response, textStatus, jqXHR){
                $.messager.alert("异常", "操作失败", "error");
            },
            complete:function(jqXHR,textStatus){
            }
        });
    }
    
    /** 同步还款计划* */
    $("#syncRepaymentPlanBtn").click(function() {
        // 发起推送还款计划的请求
        submitRequest("1", $.splitQueueManagement.syncRepaymentPlanUrl);
    });
    
    /** 同步还款记录**/
    $("#syncRepaymentRecordBtn").click(function(){
        // 发起推送还款记录的请求
        submitRequest("2", $.splitQueueManagement.syncRepaymentRecordUrl);
    });
    
    /** 同步还款明细**/
    $("#syncRepaymentDetailBtn").click(function(){
        // 发起推送还款明细的请求
        submitRequest("3", $.splitQueueManagement.syncRepaymentDetailUrl);
    });
    
    /** 发起推送数据的请求处理方法**/
    function submitRequest(type, url){
        var rows = $.splitQueueManagement.dataGrid.datagrid('getSelections');
        if (rows.length <= 0) {
            $.messager.alert('警告', "请选择一条待同步的记录！", 'warning');
            return;
        }
        if (rows.length > 1) {
            $.messager.alert('警告', "最多只能选择一条待同步的记录！", 'warning');
            return;
        }
        var row = rows[0];
        // 分账队列ID
        var splitId = row.splitId;
        // 分账状态
        var splitResultState = row.splitResultState;
        // 还款类型
        var repayType = row.repayType;
        if (splitResultState != '1' && splitResultState != '3') {
            $.messager.alert('警告', "必须选择分账状态是【未分账】或者【分账失败】的记录！", 'warning');
            return;
        }
        if(type == '3'){
            if(repayType == '04'){
                $.messager.alert('警告', "还款类型是【一次性回购】的分账记录不需要推送还款明细信息给陆金所！", 'warning');
                return;
            }
            if(repayType == '06'){
                $.messager.alert('警告', "还款类型是【逾期还回】的分账记录不需要推送还款明细信息给陆金所！", 'warning');
                return;
            }
        }
        var tip = "还款数据";
        if(type=='1'){
            tip = "还款计划";
        } else if(type=='2'){
            tip = "还款记录";
        } else if(type=='3'){
            tip = "还款明细";
        }
        var tips = "确认推送"+ tip +"给陆金所吗？";
        $.messager.confirm("提示", tips, function(r){
            if(r){
                var params = {"splitId" : splitId, "repayType" : repayType};
                ajaxSubmit(url, params);
            }
        });
    }
    
    /** 分账队列数据编辑 * */
    $("#editDataBtn").click(function() {
        var rows = $.splitQueueManagement.dataGrid.datagrid('getSelections');
        if (rows.length <= 0) {
            $.messager.alert('警告', "请选择一条待编辑的记录！", 'warning');
            return;
        }
        if (rows.length > 1) {
            $.messager.alert('警告', "最多只能选择一条待编辑的记录！", 'warning');
            return;
        }
        var row = rows[0];
        // 清空表单
        $.splitQueueManagement.dataForm.form("clear");
        //显示窗口
        $.splitQueueManagement.updateDataPanel.window('open');
        // 合同编号文本框设置只可读
        $("#u_contractNum").textbox({readonly:true});
        // 加载表单数据
        $.splitQueueManagement.dataForm.form("load",row);
    });
    
    /** 提交数据编辑更新处理 **/
    $("#submitBtn").click(function(){
        if (!$.splitQueueManagement.dataForm.form('validate')) {
            return;
        }
        /** 获取查询表单数据转换成JSON对象 **/
        var searchMsg = $.splitQueueManagement.dataForm.serialize();
        /** 对参数进行解码(显示中文) **/
        searchMsg = decodeURIComponent(searchMsg);
        /** 字符串转换为对象 **/
        var data = $.serializeToJsonObject(searchMsg);
        /** 添加id参数 **/
        data.id = data.splitId;
        /** 添加冻结金额参数 **/
        data.frozenAmount = data.amount;
        
        $.messager.confirm("提示", "确认要更新此条分账记录吗？", function(r){
            if(r){
                /** 发送更新处理请求 **/
                $.ajaxPackage({
                    url:$.splitQueueManagement.updateSplitQueueUrl,
                    type:"post",
                    data:data,
                    dataType:"json",
                    success:function(response, textStatus, jqXHR){
                        var resCode = response.resCode;
                        var resMsg = response.resMsg;
                        if (resCode != '000000') {
                            $.messager.alert('警告', resMsg, 'warning');
                            return;
                        }
                        $.messager.alert('提示','操作成功！','info');
                        // 关闭面板窗口
                        $.splitQueueManagement.updateDataPanel.window('close');
                        // 刷新页面
                        $.splitQueueManagement.reloadDataGrid();
                    },
                    error:function(response, textStatus, jqXHR){
                        $.messager.alert("异常", "操作异常", "error");
                    },
                    complete:function(jqXHR,textStatus){
                    }
                });
            }
        });
    });
    
    /** 关闭面板窗口 **/
    $("#closeBtn").click(function(){
        if($.splitQueueManagement.updateDataPanel){
            $.splitQueueManagement.updateDataPanel.window('close');
        }
    });
});
