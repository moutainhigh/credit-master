$(function() {
    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/operation/searchPerformanceBelongInfo';
    
    // 变更客户经理url
    var updateUrl = global.contextPath + '/operation/updatePerformanceBelongInfo';
    
    // 变更录单渠道url
    var ApplyupdateUrl = global.contextPath + '/operation/updatePerformanceBelongRecorded';
    
    // 加载更新窗口url
    var loadOptionInfoUrl = global.contextPath + '/operation/loadOptionInfo';
   // 加载切换录单渠道按钮url
    var loadOptionapplyUrl = global.contextPath + '/operation/loadOptionapply';
    // 表格实例
    var performanceDataGrid = $('#performanceBelongDataGrid');
    
    var updateDataPanel = $('#updateDataPanel');
    var updateApplyDataPanel = $('#updateApplyDataPanel');
    
    // 查询条件数据项表单对象
    var searchForm = $('#searchForm');
    
    // 更新业绩归属表单对象
    var dataForm = $("#dataForm");
    // 更改录单渠道表单对象
    var applydataForm = $("#applydataForm");
    // 变更后的销售团队
    var updateSalesTeam = $("#updateSalesTeam");
    var updateapplyinputflag=$("#updateapplyinputflag");

    // 每页显示的记录条数，默认为10
    var pageSize = 10;
    
    // 设置每页记录条数的列表
    var pageSizeList = [ 10, 20, 30, 40, 50 ];
    
    // 定义表格参数
    $.pg = {'page' : 1,'rows' : pageSize };
    
    /** 变更后的录单渠道下拉框初始化 **/
    updateapplyinputflag.combobox({
        valueField:'applyInput',
        textField:'applyInput',
        panelHeight:"80",
        required:true
    });
    // 加载录单渠道下拉框
    updateapplyinputflag.combobox("delayLoad",{url:loadOptionapplyUrl});
    
    performanceDataGrid.datagrid({
        pg : $.pg,
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
        
        // 锁定列定义
        /*frozenColumns : [ [ {
            field : 'name',
            title : '借款人',
            width : 50
        } ] ],*/
        
        columns : [ [
        // 列定义
        {
            field : 'name',
            title : '借款人',
            width : 50
        },{
            field : 'loanId',
            title : '债权编号',
            width : 50,
            hidden: true
        },
        {
            field : 'applyInputFlag',
            title : '录单渠道',
            width : 50,
            hidden: true
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
        }, {
            field : 'loanType',
            title : '借款类型',
            width : 50
        }, {
            field : 'requestMoney',
            title : '申请金额',
            width : 50,
            vType : 'rmb'
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        }, {
            field : 'pactMoney',
            title : '合同金额',
            width : 50,
            vType : 'rmb'
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        }, {
            field : 'time',
            title : '借款期限',
            width : 30
        },{
        	field : 'contractNum',
        	title : '合同编号',
        	width : 50
        },{
            field : 'salesManName',
            title : '当前客户经理',
            width : 50
        },{
            field : 'salesTeamName',
            title : '当前所属团队',
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
    var performanceDataGridPG = performanceDataGrid.datagrid('getPager');
    performanceDataGridPG.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.pg.page = pageNumber;
            $.pg.rows = pageSize;
            // 如果输入了销售团队，但是没有匹配到下拉框的值，则会被清空
            $("#salesTeamId").combobox("checkValue");
            var flag = check();
            if(searchForm.form("validate") && flag){
                reloadDataGrid();
            }
        }
    });
    
    // 新增面板参数定义
    updateDataPanel.window({
        width : 500,
        height : 300,
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
    
    
    updateApplyDataPanel.window({
    	width : 350,
        height : 210,
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
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        // 如果输入了销售团队，但是没有匹配到下拉框的值，则会被清空
        $("#salesTeamId").combobox("checkValue");
        var flag = check();
        if(searchForm.form("validate") && flag){
            $.pg.page = 1;
            reloadDataGrid();
        }
    })
    
    /** 查询条件必须输入其中之一的校验 **/
    function check(){
        var name = $.trim($("#name").val());
        var mobile = $.trim($("#mobile").val());
        var idNum = $.trim($("#idNum").val());
        var salesTeamId = $('#salesTeamId').combobox('getValue');
        var salesManId = $('#salesManId').combobox('getValue');
        var contractNum = $.trim($("#contractNum").val());
        if($.isEmpty(name) && $.isEmpty(mobile) && $.isEmpty(idNum) && $.isEmpty(salesTeamId) && $.isEmpty(salesManId)&& $.isEmpty(contractNum)){
            $.messager.alert('警告','请必须输入一个查询条件!','warning');
            return false;
        }
        // 防止输入空白查询
        $("#name").val(name);
        $("#mobile").val(mobile);
        $("#idNum").val(idNum);
        $("#contractNum").val(contractNum);
        return true;
    }
    
    /** 打开更新客户经理、销售团队面板 **/
    $("#updateBtn").click(function(){
        var selectedRow = performanceDataGrid.datagrid('getSelected');
        if(!selectedRow){
            $.messager.alert('警告','请选中需要修改的记录!','warning');
            return;
        }
        var loanId = selectedRow.loanId;
        var salesManName = selectedRow.salesManName;
        var salesTeamName = selectedRow.salesTeamName;
        // 清空表单
        dataForm.form("clear");
        //显示窗口
        updateDataPanel.window('open');
        // 加载表单数据
        dataForm.form("load",{'loanId':loanId,'salesManName':salesManName,'salesTeamName':salesTeamName});
        /** 变更后的销售团队下拉框初始化 **/
        updateSalesTeam.combobox({
            valueField:'id',
            textField:'name',
            required:true
        });
        // 加载销售团队下拉框
        updateSalesTeam.combobox("delayLoad",{url:loadOptionInfoUrl});
    });
    
    /** 打开录单渠道更新面板 **/
    $("#updateRecorded").click(function(){
    	
        var selectedRow = performanceDataGrid.datagrid('getSelected');
        if(!selectedRow){
            $.messager.alert('警告','请选中需要修改的记录!','warning');
            return;
        }
        var loanId = selectedRow.loanId;
        var applyInputFlag = selectedRow.applyInputFlag; 
        if (applyInputFlag == "applyInput") {
        	applyInputFlag= '普通营业部';
		} else if (applyInputFlag == "directApplyInput") {
			applyInputFlag= '直通车营业部';
		} else {
			applyInputFlag= '';
		}
        // 清空表单
        applydataForm.form("clear");
        //显示窗口
        updateApplyDataPanel.window('open');
       // 加载表单数据
        applydataForm.form("load",{'loanId':loanId,'applyInputFlag':applyInputFlag});
        
    /*    *//** 变更后的录单渠道下拉框初始化 **//*
        updateapplyinputflag.combobox({
            valueField:'applyInput',
            textField:'applyInput',
            required:true
        });
        // 加载录单渠道下拉框
        updateapplyinputflag.combobox("delayLoad",{url:loadOptionapplyUrl});*/
        
    });
    
    /** 加载销售团队下拉框信息 **/ 
    /*updateSalesTeam.siblings("span").first().click(function(){*/
//    $("#teamTd").click(function(){
//        updateSalesTeam.combobox("setValue","");
//        //updateSalesTeam.combobox("reload",loadOptionInfoUrl);
//        
//    });
    
    /**  提交更新处理 **/
    $("#submitBtn").click(function(){
        // 如果输入了变更后的销售团队，但是没有匹配到下拉框的值，则会被清空
        $("#updateSalesTeam").combobox("checkValue");
        // 如果输入了变更后的客服经理，但是没有匹配到下拉框的值，则会被清空
        $("#updateSalesMan").combobox("checkValue");
        if (dataForm.form('validate')) {
            var salesManName = $("#salesManName").val();
            var salesTeamName = $("#salesTeamName").val();
            var updateSalesMan = $("#updateSalesMan").combobox("getText");
            var updateSalesTeam = $("#updateSalesTeam").combobox("getText");
            var word = "当前客户经理为" + salesManName + "，所属团队为" + salesTeamName 
            + "；变更后客户经理为" + updateSalesMan + "，所属团队为" + updateSalesTeam + "。请确认！";
            $.messager.confirm("提示",word,function(r){
                if(r){
                    dataForm.form('submit', {
                        url: updateUrl,
                        onSubmit: function(){
                        },
                        success:function(data){
                            data = JSON.parse(data);
                            var resCode = data.resCode;
                            var resMsg = data.resMsg;
                            if (resCode == '000000') {
                                // 关闭窗口
                                updateDataPanel.window('close');
                                $.messager.alert('提示','更改成功!','info');
                                reloadDataGrid();
                            }else{
                                $.messager.alert('异常','变更业绩归属发生异常!','error');
                            }
                        }
                    });
                }
            });
        }
    });
    
    /**  AppsubmitBtn提交更新处理 **/
    $("#AppsubmitBtn").click(function(){
        // 如果输入了变更后的录单渠道，但是没有匹配到下拉框的值，则会被清空
        $("#updateapplyinputflag").combobox("checkValue");
        if (applydataForm.form('validate')) {
            var applyInputFlag = $("#applyInputFlag").val();
            var updateapplyflag = $("#updateapplyinputflag").combobox("getText");
            
            var word = "当前录单渠道为" + applyInputFlag + "；变更后录单渠道为" + updateapplyflag  + "。请确认！";
           
            $.messager.confirm("提示",word,function(r){
                if(r){
                	applydataForm.form('submit', {
                        url: ApplyupdateUrl,
                        onSubmit: function(){
                        },
                        success:function(data){
                            data = JSON.parse(data);
                            var resCode = data.resCode;
                            var resMsg = data.resMsg;
                            if (resCode == '000000') {
                                // 关闭窗口
                            	updateApplyDataPanel.window('close');
                                $.messager.alert('提示','更改成功!','info');
                                reloadDataGrid();
                            }else{
                                $.messager.alert('异常','变更业绩归属发生异常!','error');
                            }
                        }
                    });
                }
            });
        }
    });
    
    /**  关闭更新窗口 **/
    $("#ApplycloseBtn").click(function(){
        if(updateApplyDataPanel){
        	updateApplyDataPanel.window('close');
        }
    });
    /**  关闭更新窗口 **/
    $("#closeBtn").click(function(){
        if(updateDataPanel){
            updateDataPanel.window('close');
        }
    });
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        searchForm.form('reset');
    });

    // 加载表格数据
    function reloadDataGrid() {
        var searchMsg = searchForm.serialize();
        // 对参数进行解码(显示中文)
        searchMsg = decodeURIComponent(searchMsg);
        var queryParam = $.serializeToJsonObject(searchMsg);
        queryParam.url = dataGridUrl;
        performanceDataGrid.datagrid("reloadData",queryParam);
    }
})
