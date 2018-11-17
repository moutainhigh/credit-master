$(function() {
    $.privateRec  = {
        /** 被认领客户信息表格数据源地址 **/
        dataGridUrl : global.contextPath + '/repayment/searchPrivateAccountReceiveInfo',
        /** 确认领取url **/
        confirmReceiveUrl : global.contextPath + '/repayment/confirmReceive',
        /** 对私还款记录(前台选中的行对象) **/
        recRow : undefined,
        /** 被认领人信息窗口 **/
        showRectWin : $('#showRectWin'),
        /** 被认领人信息表格 **/
        recDataGrid : $('#recDataGrid'),
        /** 被认领人信息查询表单对象 **/
        recSearchForm : $('#recSearchForm'),
        /** 分页控件 **/
        pager : undefined,
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 加载被认领人信息数据 **/
        reloadDataGrid : function() {
            if(!$.privateRec.validate()){
                return;
            }
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.privateRec.recSearchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 追加url参数**/
            queryParam.url = $.privateRec.dataGridUrl;
            /** 查询并加载数据**/
            $.privateRec.recDataGrid.datagrid('reloadData',queryParam);
        },
        /** 查询校验 **/
        validate : function(){
            if(!$.privateRec.recSearchForm.form("validate")){
                return false;
            }
            if(!searchCheck()){
                return false;
            }
            return true;
        }
    }
    
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.privateRec.pg = {
        'page' : 1,
        'rows' : $.privateRec.pageSize
    }
    
    /** DataGrid初始化 **/
    $.privateRec.recDataGrid.datagrid({
        pg : $.privateRec.pg,
        /** 如果按姓名且只输入一个字符的情况下，查询待认领客户的贷款信息的效率不高，所以超时时间设定为3分钟 **/
        timeout : 180000,
        /** 是否显示行号 **/
        rownumbers : true,
        /** 是否单选 **/
        singleSelect : true,
        /** 是否可折叠的 **/
        collapsible : false,
        /** 自适应列宽 **/
        fitColumns : true,
        /** 自适应父窗口 **/
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        columns : [[{/** 列定义 **/
            field : 'borrowerName',
            title : '借款人',
            width : '8%',
            formatter:function(value,row,index){
                if(value){
                    return "<a href='javascript:void(0)' class='borrowerName' onclick='personInfoTab("+ index+ ");' loanId='"+row.loanId+"' borrowerName='"+ value +"' >"+ value +"</a>";
                }
                return '';
            }
        },{
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
            field : 'salesmanName',
            title : '客户经理',
            width : '8%'
        },{
            field : 'crmName',
            title : '客服',
            width : '7%'
        },{
            field : 'idNum',
            title : '身份证号',
            width : '6%',
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
            field : 'profession',
            title : '职业类型',
            width : '6%'
        },{
            field : 'purpose',
            title : '用途',
            width : '8%'
        },{
            field : 'pactMoney',
            title : '合同金额',
            width : '8%',
            vType : 'rmb'
        },{
            field : 'money',
            title : '审批金额',
            width : '8%',
            vType : 'rmb'
        },{
            field : 'time',
            title : '借款期限',
            width : '6%',
        },{
            field : 'loanState',
            title : '状态',
            width : '6%'
        },{
            field : 'contractNum',
            title : '合同编号',
            width : '10%'
        },{
            field : 'loanId',
            title : '操作',
            width : '10%',
            formatter:function(value,row,index){
                if(value){
                    var borrowerName = row.borrowerName;
                    var recCount = row.recCount;
                    var elements ="<a href='javascript:void(0)' class='confirmReceive' loanId='"+row.loanId+"' borrowerName='"+ row.borrowerName +"' ></a>";
                    return elements;
                }
            }
        }]],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.privateRec.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.privateRec.pageSizeList,
        /** 工具栏定义 **/
        toolbar : '#recTB',
        /** 自定义行样式 **/
        rowStyler : function(index,row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            $(".confirmReceive").linkbutton({
                text:'确认领取',
                plain:true,
                iconCls:'pic_36',
                onClick:function(){
                    var id = $("#accountId").val();
                    var loanId = $(this).attr("loanId");
                    var borrowerName  = $(this).attr("borrowerName");
                    $.messager.confirm("提示","确认领取吗？",function(r){
                        if(r){
                            var json = {"id":id,"loanId":loanId,"borrowerName":borrowerName};
                            $.ajaxPackage({
                                url:$.privateRec.confirmReceiveUrl,
                                type:"post",
                                data: json,
                                dataType:"json",
                                success:function(response, textStatus, jqXHR){
                                    var resCode = response.resCode;
                                    var resMsg = response.resMsg;
                                    if(resCode != "000000"){
                                        $.messager.alert("警告", resMsg, "warning");
                                        return;
                                    }
                                    $.messager.alert("提示", resMsg, "info");
                                    setTimeout(function(){
                                        /** 关闭对私还款认领窗口 **/
                                        $.privateRec.showRectWin.window('close');
                                        /** 刷新对私还款页面数据记录 **/
                                        $.privateAccount.reloadDataGrid();
                                    }, 600);
                                },
                                error:function(response, textStatus, jqXHR){
                                    $.messager.alert("异常", "操作失败", "error");
                                },
                                complete:function(jqXHR,textStatus){
                                }
                            });
                        }
                    });
                }
            });
            /** 页面自适应 **/
            $.privateRec.recDataGrid.datagrid('resize');
        }
    });
    
    /** 分页对象初始化 **/
    $.privateRec.pager = $.privateRec.recDataGrid.datagrid('getPager');
    $.privateRec.pager.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.privateRec.pg.page = pageNumber;
            $.privateRec.pg.rows = pageSize;
            $.privateRec.reloadDataGrid();
        }
    });
    
    /** 被认领人信息窗口面板参数定义 **/
    $.privateRec.showRectWin.window({
        /** 窗口宽度 **/
        width : $(window).width() * 7 / 10,
        /** 窗口距父窗口顶部的距离 **/
        left:($(window).width() * 3 / 10) * 0.5,
        /** 定义窗口是不是模态窗口 **/
        modal : true,
        /** 定义是否显示折叠按钮 **/
        collapsible : false,
        /** 定义是否显示最小化按钮 **/
        minimizable : false,
        /** 定义是否显示最大化按钮 **/
        maximizable : false,
        /** 定义是否显示关闭按钮 **/
        closable : true,
        /** 定义是否关闭窗口 **/
        closed : true,
        /** 定义是否窗口能被拖拽 **/
        draggable : true,
        /** 定义是否可以调整窗口尺寸 **/
        resizable : false,
        /** 如果设为 true， 当窗口能够显示阴影的时候将会显示阴影 **/
        shadow : true,
        /** 定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部 **/
        inline : true,
        /** 样式定义 **/
        iconCls : 'pic_57'
    });
    
    /** 查询被认领客户信息 **/
    $("#recSearchBtn").click(function(){
        $.privateRec.pg.page = 1;
        $.privateRec.reloadDataGrid();
    });

    /** 查询校验 * */
    function searchCheck() {
        var name = $.trim($("#name").val());
        var contractPhone = $.trim($("#contractPhone").val());
        var idNum = $.trim($("#idNum").val());
        var contractNum = $.trim($("#contractNum").val());
        if ($.isEmpty(name) && $.isEmpty(contractPhone) && $.isEmpty(idNum) && $.isEmpty(contractNum)) {
            $.messager.alert('警告', '请至少输入一个查询条件！', 'warning');
            return false;
        }
        // 防止输入空白查询
        $("#name").val(name);
        $("#contractPhone").val(contractPhone);
        $("#idNum").val(idNum);
        $("#contractNum").val(contractNum);
        return true;
    }
    
    /** 重置处理 **/
    $('#recClearBtn').click(function() {
        $.privateRec.recSearchForm.form('reset');
    })
})
/** 查看客户详细资料 **/
function personInfoTab(rowIndex) {
    var row = $('#recDataGrid').datagrid('getRows')[rowIndex];
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
/** 查看客户债权信息 **/
function loanbaseTab(rowIndex) {
    var row = $('#recDataGrid').datagrid('getRows')[rowIndex];
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
