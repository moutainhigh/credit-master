$(function() {
    $.customerVisit = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/riskManage/customerVisitSearch',
        /** 客户回访信息表格 **/
        customerVisitDataGrid : $('#customerVisitDataGrid'),
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
            if ($.customerVisit.searchForm.form('validate')) {
                /** 获取查询表单数据转换成JSON对象 **/
                var searchMsg = $.customerVisit.searchForm.serialize();
                /** 对参数进行解码(显示中文) **/
                searchMsg = decodeURIComponent(searchMsg);
                /** 字符串转换为对象 **/
                var queryParam = $.serializeToJsonObject(searchMsg);  
                queryParam.url = $.customerVisit.dataGridUrl;
                $.customerVisit.customerVisitDataGrid.datagrid('reloadData',queryParam);
            }
        }
    };
    
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.customerVisit.pg = {
        'page' : 1,
        'rows' : $.customerVisit.pageSize
    }
    
    /** DataGrid初始化 **/
    $.customerVisit.customerVisitDataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.customerVisit.pg,
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
            field : 'signsalesdepName',
            title : '放款营业部',
            width : '12%'
        }, {
            field : 'salesdepartmentName',
            title : '管理营业部',
            width : '12%'
        }, {
            field : 'borrowerName',
            title : '借款人',
            width : '5%',
            formatter:function(value,row,index){
                if(value){
                    return "<a href='javascript:void(0)' class='borrowerName' onclick='personInfoTab("+ index+ ");' loanId='"+row.loanId+"' borrowerName='"+ value +"' >"+ value +"</a>";
                }
                return '';
            }
        }, {
            field : 'salesmanName',
            title : '客户经理',
            width : '5%'
        }, {
            field : 'loanType',
            title : '借款类型',
            width : '7%',
            formatter:function(value,row,index){
                if(value){
                    return "<a href='javascript:void(0)' class='loanType' onclick='loanbaseTab("+ index+ ");' loanId='"+row.loanId+"' loanType='"+ value +"' >"+ value +"</a>";
                }
                return '';
            }
        },{
            field : 'contractNum',
            title : '合同编号',
            width : '7%'
        }, {
            field : 'visitDate',
            title : '回访日期',
            width : '6%'
        }, {
            field : 'channel',
            title : '渠道',
            width : '6%'
        }, {
            field : 'sAttitude',
            title : '服务态度',
            width : '6%'
        }, {
            field : 'additionalCharges',
            title : '额外费用',
            width : '4%',
            formatter : function(value, row, rowIndex) {
                if (value) {
                    if(value=='t'){
                        return '是';
                    }
                    return '否';;
                }
            }
        }, {
            field : 'advice',
            title : '建议',
            width : '14%'
        }, {
            field : 'memo',
            title : '备注',
            width : '14%'
        } ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.customerVisit.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.customerVisit.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
            /*if(row.assignState=='1'){
            	return 'background-color:#FF1493;';
            }*/
            if(row.transferBatch != '' && row.transferBatch != '未转让' && row.transferBatch != undefined){
            	return 'background-color:#FF0000;';
            }
        }
    });
    
    /** 表格分页组件 **/
    $.customerVisit.pager = $.customerVisit.customerVisitDataGrid.datagrid('getPager');
    $.customerVisit.pager.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.customerVisit.pg.page = pageNumber;
            $.customerVisit.pg.rows = pageSize;
            if(searhCheck()){
                $.customerVisit.reloadDataGrid();
            }
        }
    });
    
    /** 页面自适应 **/
    $.customerVisit.customerVisitDataGrid.datagrid('resize');
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        if(!$(this).linkbutton("options").disabled){
            $.customerVisit.pg.page = 1;
            if(searhCheck()){
                $.customerVisit.reloadDataGrid();
            }
        }
    })
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        if(!$(this).linkbutton("options").disabled){
            $.customerVisit.searchForm.form('reset');
        }
    })
    
    /** 查询校验 **/
    function searhCheck(){
        /** 获取查询表单数据转换成JSON对象 **/
        var searchMsg = $.customerVisit.searchForm.serialize();
        /** 对参数进行解码(显示中文) **/
        searchMsg = decodeURIComponent(searchMsg);
        /** 字符串转换为对象 **/
        var queryParam = $.serializeToJsonObject(searchMsg);
        /** 未输入查询条件不给予查询 **/
        if ($.isEmpty(queryParam.name) 
            && $.isEmpty(queryParam.mobile) 
            && $.isEmpty(queryParam.idNum)
            && $.isEmpty(queryParam.startDate) 
            && $.isEmpty(queryParam.endDate) 
            && $.isEmpty(queryParam.loanType) 
            && $.isEmpty(queryParam.loanId)
            && $.isEmpty(queryParam.contractNum)) {
            $.messager.alert('警告','请至少输入一个查询条件!','warning');
            return false;
        } 
        if(!datecheck(queryParam.startDate,queryParam.endDate)){
            return false;
        }
        return true;
    }
    
    /** 时间大小校验 **/
    function datecheck(sDate,eDate){
        if($.isEmpty(sDate) || $.isEmpty(eDate)){
            return true;
        }
        var startDate = new Date(sDate.replace(/\-/g, "\/"));
        var endDate = new Date(eDate.replace(/\-/g, "\/"));
        if(!$.isEmpty(sDate) && !$.isEmpty(eDate) && startDate > endDate){
            $.messager.alert('警告','回访日期（开始日期）不能大于回访日期（截止日期）!','warning');
            return false;
        }
        return true;
    }
    
    /** 页面初始化处理 **/
    function initPage(){
        // 回访管理页面点击回访记录按钮，跳转到客户回访查询页面，跟根据返回的债权Id查询刷新页面数据
        var loanId = $("#loanId").val();
        if(!$.isEmpty(loanId)){
            // 禁用查询按钮
            //$("#searchBtn").linkbutton("disable");
            // 禁用重置按钮
            //$("#clearBtn").linkbutton("disable");
            // 重新加载数据
            $.customerVisit.reloadDataGrid();
        }
    }
    
    // 页面初始化处理
    initPage();
})

/** 查看客户详细资料 **/
function personInfoTab(rowIndex) {
    var row = $.customerVisit.customerVisitDataGrid.datagrid('getRows')[rowIndex];
    var id = row.borrowerId;
    var name = row.borrowerName;
    var tab = {};
    tab.id = 'personDetail_' + id;
    tab.text = name + ' - 详细资料';
    tab.iconCls = "pic_1";
    tab.url = global.contextPath + '/person/viewPersonDetailPage' + '/' + id;
    // ** 调用父级添加选项卡方法 **//*
    parent.$.iframeTabs.add(tab);
}

/** 查看客户借款详细资料 **/
function loanbaseTab(rowIndex) {
    var row = $.customerVisit.customerVisitDataGrid.datagrid('getRows')[rowIndex];
    var id = row.loanId;
    var name = row.borrowerName;
    var loanType = row.loanType;
    var tab = {};
    tab.id = 'loanDetail_' + id;
    tab.text = name+"("+loanType+")" + ' - 借款详细信息';
    tab.iconCls = "pic_1";
    tab.url = global.contextPath + '/loanInfo/viewPersonLoanDetailPage' + '/' + id;
    // ** 调用父级添加选项卡方法 **//*
    parent.$.iframeTabs.add(tab);
}
