$(function() {
    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/debitQueue/searchQueueManagementList';

    var resendDebitUrl = global.contextPath + '/debitQueue/debitQueueLogResend';

    var oneKeyResendUrl = global.contextPath + '/debitQueue/oneKeyResendDebitQueueLog';

    var marginPayUrl = global.contextPath + '/debitQueue/marginPay';
    
    // 更新划扣队列Url
    var updateDebitQueueUrl  = global.contextPath + '/debitQueue/updateDebitQueue';
    // 表格实例
    var debitQueueDataGrid = $('#debitQueueDataGrid');

    // 查询条件数据项表单实例
    var searchForm = $('#searchForm');
    
    /** 更新面板 **/
    var updateDataPanel = $('#updateDataPanel');
    /** 更新表单对象 **/
    var dataForm = $('#dataForm');

    var currDate = $('#currDate').val();
    var currDate2 = $('#currDate2').val();
    // 每页显示的记录条数，默认为10
    var pageSize = 10;

    // 设置每页记录条数的列表
    var pageSizeList = [ 10, 20, 30, 40, 50 ];

    // 定义表格参数
    var pg = {
        'page' : 1,
        'rows' : pageSize
    };
    var debitNotifyStates = {
        '1' : '待通知',
        '2' : '已通知',
        '3' : '通知失败',
        '4' : '通知成功'
    };
    var debitResultStates = {
        '1' : '未划扣',
        '2' : '划扣中',
        '3' : '划扣失败',
        '4' : '划扣成功'
    };
    var debitTypes = {
        '1' : '委托划扣',
        '2' : '机构划扣',
        '3' : '当期代偿',
        '4' : '逾期回购'
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
    var payPartys = {
        '1' : '借款人',
        '2' : '服务公司',
        '3' : '准备金',
        '4' : '保证金'
    }
    debitQueueDataGrid.datagrid({
        pg : pg,
        // 提交方式
        method : 'post',
        /** 是否显示行号 **/
        rownumbers : true,
        /** 是否单选 **/
        singleSelect : false,
        /** 是否可折叠的 **/
        collapsible : false,
        /** 自适应列宽 **/
        fitColumns : true,
        /** 设置true，表示适应它的父容器 **/
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        /** 禁止服务端排序 **/
        remoteSort : false,
        columns : [ [
        // 列定义
        {
            field : 'debitId',
            title : '划扣ID',
            hidden : 'true'
        }, {
            field : 'loanId',
            title : '债权ID',
            hidden : 'true'
        }, {
            field : 'id',
            title : 'id',
            width : '7%',
            checkbox : true
        }, {
            field : 'custName',
            title : '客户姓名',
            width : 15
        }, {
            field : 'idNum',
            title : '证件号码',
            width : 25
        }, {
            field : 'contractNum',
            title : '合同编号',
            width : 25
        }, {
            field : 'repayType',
            title : '还款类型',
            width : 15,
            formatter : function(value, row, index) {
                return repayTypes[value + ''] || '未知';
            },
        }, {
            field : 'payParty',
            title : '扣款帐户',
            width : 15,
            formatter : function(value, row, index) {
                return payPartys[value + ''] || '未知';
            },
        }, {
            field : 'debitNotifyState',
            title : '通知状态',
            width : 15,
            formatter : function(value, row, index) {
                return debitNotifyStates[value + ''] || '未知';
            },
        }, {
            field : 'debitResultState',
            title : '划扣状态',
            width : 15,
            formatter : function(value, row, index) {
                return debitResultStates[value + ''] || '未知';
            },
        }, {
            field : 'batchId',
            title : '批次号',
            width : 30
        }, {
            field : 'debitNo',
            title : '序号',
            width : 30
        }, {
            field : 'repayTerm',
            title : '划扣期数',
            width : 10
        }, {
            field : 'amount',
            title : '划扣金额',
            width : 15,
            vType : 'rmb'
        }, {
            field : 'frozenAmount',
            title : '冻结金额',
            width : 15,
            vType : 'rmb'
        }, {
            field : 'deductTime',
            title : '划扣时间',
            width : 25
        }, {
            field : 'returnTime',
            title : '回盘时间',
            width : 25,
            formatter : $.DateUtil.dateFormatToFullStr
        }, {
            field : 'memo',
            title : '备注',
            width : 20
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

    var pager = debitQueueDataGrid.datagrid('getPager');
    pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            pg.page = pageNumber;
            pg.rows = pageSize;
            reloadDataGrid();
        }
    });

    $("#clearCondition").bind("click", function(envent) {
        $("#searchForm").form("reset");
    });

    // 查询按钮添加事件
    $("#searchBut").click(function() {
        pg.page = 1;
        reloadDataGrid();
    });

    // 加载表格数据
    function reloadDataGrid() {
        // 获取查询表单数据转换成JSON对象
        var searchMsg = searchForm.serialize();
        // 对参数进行解码(显示中文)
        searchMsg = decodeURIComponent(searchMsg);
        var queryParam = $.serializeToJsonObject(searchMsg);
        queryParam.url = dataGridUrl;
        debitQueueDataGrid.datagrid('reloadData', queryParam);
    }
    
    /** 代扣重发**/
    $("#debitResend").click(function() {
        var rows = debitQueueDataGrid.datagrid('getSelections');
        if (rows.length <= 0) {
            $.messager.alert('警告', "请至少选择一条需要发起划扣的记录！", 'warning');
            return;
        }
        var debitIds = [];
        for (var i = 0; i < rows.length; i++) {
            // 划扣状态
            var debitResultState = rows[i].debitResultState;
            if(debitResultState == '2'){
                $.messager.alert('警告', "不能选择划扣状态是【划扣中】的记录！", 'warning');
                return;
            }
            if(debitResultState == '4'){
                $.messager.alert('警告', "不能选择划扣状态是【划扣成功】的记录！", 'warning');
                return;
            }
            // 还款类型
            var repayType = rows[i].repayType;
            if(repayType == '02'){
                $.messager.alert('警告', "不能选择还款类型是【机构还款】的记录！", 'warning');
                return;
            }
            if(repayType == '06'){
                $.messager.alert('警告', "不能选择还款类型是【逾期还回】的记录！", 'warning');
                return;
            }
            if(repayType == '07'){
                $.messager.alert('警告', "不能选择还款类型是【回购结清】的记录！", 'warning');
                return;
            }
            var deductTime = rows[i].deductTime;
            // 划扣队列数据生成日期
            var debitDate = stringToDate(deductTime,'-');
            // 当前系统日期
            var sysDate = stringToDate(currDate,'-');
            // 划扣队列创建日期和系统日期相差天数
            var diffDays = (sysDate - debitDate) / (1000*60*60*24);
            // 委托还款需在划扣日+1天代扣
            if(repayType == '01' && diffDays < 1){
                $.messager.alert('警告', "委托还款不能在还款日当天发起代扣！", 'warning');
                return;
            }
            // 提前结清还款需在划扣日+1天代扣
            if(repayType == '05' && diffDays < 1){
                $.messager.alert('警告', "提前结清还款不能在还款日当天发起代扣！", 'warning');
                return;
            }
            // 逾期准备金代偿、一次性回购准备金代偿必须在T+1日操作
            if(repayType == '03' && diffDays > 0){
                $.messager.alert('警告', "逾期准备金代偿只能在日终后一天发起代扣！", 'warning');
                return;
            }
            // 一次性回购准备金代偿必须在T+1日操作
            if(repayType == '04' && diffDays > 0){
                $.messager.alert('警告', "一次性回购准备金代偿只能在日终后一天发起代扣！", 'warning');
                return;
            }
            
            // 收集参数
            debitIds.push(rows[i].debitId);
        }
        var tipMessage = '您已选择了：' + debitIds.length + '条记录，确认发起划扣吗？';
        $.messager.confirm('提示',tipMessage,function(r) {
            if (r) {
                var params = {
                    "debitIds" : debitIds.join()
                };
                $.ajaxPackage({
                    type : 'post',
                    url : resendDebitUrl,
                    data : params,
                    dataType : "json",
                    success : function(data, textStatus, jqXHR) {
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        // 从服务器上获取到记录信息
                        var attachment = data.attachment;
                        if (resCode != '000000') {
                            // 操作失败
                            $.messager.alert('警告',resMsg,"warning");
                            return;
                        }
                        // 操作成功
                        $.messager.alert('提示', resMsg, "info");
                        // 刷新页面
                        reloadDataGrid();
                    },
                    error : function(XMLHttpRequest,textStatus,errorThrown, d) {
                        $.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
                    },
                    complete : function() {
                        
                    }
                });
            }
        });
    });

    /** 一键重发**/
    $("#oneKeyResend").click(function() {
        var tipMessage = '您确定要一键重发吗？';
        $.messager.confirm('提示', tipMessage, function(r) {
            if (r) {
                $.ajaxPackage({
                    type : 'get',
                    url : oneKeyResendUrl,
                    dataType : "json",
                    success : function(data, textStatus, jqXHR) {
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        // 从服务器上获取到记录信息
                        var attachment = data.attachment;
                        if (resCode != '000000') {
                            // 操作失败
                            $.messager.alert('警告',resMsg,"warning");
                            return;
                        }
                        // 操作成功
                        $.messager.alert('提示', resMsg, "info");
                        // 刷新页面
                        reloadDataGrid();
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown, d) {
                        $.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
                    },
                    complete : function() {
                        
                    }
                });
            }
        })
    });

    /** 保证金代偿（逾期代偿和一次性回购）**/
    $("#marginPay").click(function() {
        var rows = debitQueueDataGrid.datagrid('getSelections');
        if (rows.length <= 0) {
            $.messager.alert('警告', "请至少选择一条需要发起划扣的记录！", 'warning');
            return;
        }
        var debitIds = [];
        for (var i = 0; i < rows.length; i++) {
            var repayType = rows[i].repayType;
            var debitResultState = rows[i].debitResultState;
            var deductTime = rows[i].deductTime;
            var debitId = rows[i].debitId;
            
            if(repayType != '03' && repayType != '04'){
                $.messager.alert('警告', "必须选择还款类型是【逾期代偿】或【一次性回购】的记录！", 'warning');
                return;
            }
            
            if(debitResultState == '2'){
                $.messager.alert('警告', "不能选择划扣状态是【划扣中】的记录！", 'warning');
                return;
            }
            if(debitResultState == '4'){
                $.messager.alert('警告', "不能选择划扣状态是【划扣成功】的记录！", 'warning');
                return;
            }
            
            // 划扣队列数据生成日期
            var debitDate = stringToDate(deductTime,'-');
            // 当前系统日期
            var sysDate = stringToDate(currDate,'-');
            // 划扣队列创建日期和系统日期相差天数
            var diffDays = (sysDate - debitDate) / (1000*60*60*24);
            
            // 逾期代偿、一次性回购 保证金代偿必须在T+2日操作
            if(diffDays < 1){
                $.messager.alert('警告', "【逾期代偿】或【一次性回购】保证金代偿只能在T+2日后发起代扣！", 'warning');
                return;
            }
            
            /*03：逾期代偿，04：一次性回购*/
            debitIds.push(debitId);
        }
        
        var tipMessage = '您已选择了：' + debitIds.length + '条记录，确认发起划扣吗？';
        $.messager.confirm('提示', tipMessage, function(r) {
            if (r) {
                var params = {
                    "debitIds" : debitIds.join()
                };
                $.ajaxPackage({
                    type : 'post',
                    url : marginPayUrl,
                    data : params,
                    dataType : "json",
                    success : function(data, textStatus, jqXHR) {
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        // 从服务器上获取到记录信息
                        var attachment = data.attachment;
                        if (resCode != '000000') {
                            // 操作失败
                            $.messager.alert('警告',resMsg,"warning");
                            return;
                        }
                        // 操作成功
                        $.messager.alert('提示', resMsg, "info");
                        // 刷新页面
                        reloadDataGrid();
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown, d) {
                        $.messager.alert('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
                    },
                    complete : function() {
                    }
                });
            }
        });
    });
    
    /** 字符串日期转换为Date格式 **/
    function stringToDate(dateStr, separator){ 
        if(!separator){ 
            separator="-"; 
        } 
        var dateArr = dateStr.split(separator); 
        var year = parseInt(dateArr[0]); 
        var month; 
        // 处理月份为04这样的情况
        if(dateArr[1].indexOf("0") == 0){
            month = parseInt(dateArr[1].substring(1)); 
        }else{ 
            month = parseInt(dateArr[1]); 
        } 
        var day = parseInt(dateArr[2]); 
        var date = new Date(year, month -1, day);
        return date; 
    }
    // 默认查询表格数据
    // reloadDataGrid();
    
    /** 打开划扣队列数据编辑窗口 * */
    $("#editDataBtn").click(function() {
    	var rows = debitQueueDataGrid.datagrid('getSelections');
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
        dataForm.form("clear");
        //显示窗口
        updateDataPanel.window('open');
        // 合同编号文本框设置只可读
        $("#u_contractNum").textbox({readonly:true});
        // 加载表单数据
        dataForm.form("load",row);
    });
    
    /** 提交划扣队列数据编辑更新处理 **/
    $("#submitBtn").click(function(){
        if (!dataForm.form('validate')) {
            return;
        }
        /** 获取查询表单数据转换成JSON对象 **/
        var searchMsg = dataForm.serialize();
        /** 对参数进行解码(显示中文) **/
        searchMsg = decodeURIComponent(searchMsg);
        /** 字符串转换为对象 **/
        var data = $.serializeToJsonObject(searchMsg);
        /** 添加id参数 **/
        data.id = data.debitId;
        
        $.messager.confirm("提示", "确认要更新此条划扣记录吗？", function(r){
            if(r){
                /** 发送更新处理请求 **/
                $.ajaxPackage({
                    url: updateDebitQueueUrl,
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
                        updateDataPanel.window('close');
                        // 刷新页面
                        reloadDataGrid();
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
        if(updateDataPanel){
            updateDataPanel.window('close');
        }
    });
});
