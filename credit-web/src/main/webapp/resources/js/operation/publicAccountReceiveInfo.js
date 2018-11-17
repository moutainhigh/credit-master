$(function() {
    // 表格数据源地址
    var dataGridUrl = global.contextPath + '/operation/searchPublicAccountReceiveInfo';
    
    // 确认领取url
    var receiveUrl = global.contextPath + '/operation/confirmReceive';
    
    // 还款信息页面
    var backUrl = global.contextPath + '/operation/publicAccountInfo';
    
    // 表格实例对象
    var receiveInfoDataGrid = $('#receiveInfoDataGrid');
    
    // 查询条件数据项表单实例对象
    var searchForm = $('#searchForm');
    
    // 每页显示的记录条数，默认为10
    var pageSize = 10;
    
    // 设置每页记录条数的列表
    var pageSizeList = [ 10, 20, 30, 40, 50 ];
    
    // 定义表格参数
    $.pg = {'page' : 1,'rows' : pageSize };

    receiveInfoDataGrid.datagrid({
        pg : $.pg,
        // 查询url
        //url:dataGridUrl,
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
            width : '8%',
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
            width : '8%'
        },{
            field : 'idNum',
            title : '身份证号',
            width : '5%',
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
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        },{
            field : 'money',
            title : '审批金额',
            width : '8%',
            vType : 'rmb'
            /*formatter:function(value,row,index){
                if(value){
                    return $.comdify(value);
                }
            }*/
        },{
            field : 'time',
            title : '借款期限',
            width : '5%',
        },{
            field : 'loanState',
            title : '状态',
            width : '8%'
        },{
        	field : 'contractNum',
        	title : '合同编号',
        	width : '7%'
        },{
            field : 'loanId',
            title : '操作',
            width : '12%',
            formatter:function(value,row,index){
                if(value){
                    var borrowerName = row.borrowerName;
                    var recCount = row.recCount;
                    var elements ="<a href='javascript:void(0)' class='receive' loanId='"+row.loanId+"' recCount='"+recCount+"' borrowerName='"+ row.borrowerName +"' ></a>";
                    return elements;
                }
            }
        }] ],
        // 每页显示的记录条数，默认为10
        pageSize : pageSize,
        // 可以设置每页记录条数的列表
        pageList : pageSizeList,
        // 工具条
        toolbar : "#tb",
        // 页脚工具条
        //footer  : "#footer",
        // 自定义行样式
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
                
            }
        },
        onClickCell:function(index, field, value){
            
        },
        onLoadSuccess:function(data){
            $(".receive").linkbutton({
                text:'确认领取',
                plain:true,
                iconCls:'pic_36',
                onClick:function(){
                    var id = $("#accountId").val();
                    var loanId = $(this).attr("loanId");
                    var borrowerName  = $(this).attr("borrowerName");
                    $.messager.confirm("提示","确认领取吗？",function(r){
                        if(r){
                            $.ajaxPackage({
                                url:receiveUrl,
                                type:"post",
                                data:{"id":id,"loanId":loanId,"borrowerName":borrowerName},
                                dataType:"json",
                                success:function(response, textStatus, jqXHR){
                                    var resCode = response.resCode;    
                                    var resMsg = response.resMsg; 
                                    if(resCode == "000000"){
                                        location.href= backUrl+"?statusInfo="+resMsg;
                                        // 关闭认领窗口
                                        /*parent.$.iframeTabs.close({
                                            id:"receive-"+id,
                                            text:"领取还款记录"
                                        });*/
                                    }else{
                                        $.messager.alert("警告", resMsg, "warning");
                                        reloadDataGrid();
                                    }
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
            receiveInfoDataGrid.datagrid('resize');
        }
    });
    
    // 表格分页实例
    var receiveInfoDataGridPG = receiveInfoDataGrid.datagrid('getPager');
    receiveInfoDataGridPG.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.pg.page = pageNumber;
            $.pg.rows = pageSize;
            if(!validate()){
                return;
            }
            reloadDataGrid();
        }
    });
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        // 校验有误
        if(!validate()){
            return;
        }
        $.pg.page = 1;
        reloadDataGrid();
    });
    
    /** 查询校验 **/
    function searchCheck(){
        var name = $.trim($("#name").val());
        var contractPhone = $.trim($("#contractPhone").val());
        var idNum = $.trim($("#idNum").val());
        var contractNum = $.trim($("#contractNum").val());
        if($.isEmpty(name) && $.isEmpty(contractPhone) && $.isEmpty(idNum) && $.isEmpty(contractNum)){
            $.messager.alert('警告','请至少输入一个查询条件!','warning');
            return false;
        }
        // 防止输入空白查询
        $("#name").val(name);
        $("#contractPhone").val(contractPhone);
        $("#idNum").val(idNum);
        $("#contractNum").val(contractNum);
        return true;
    }
    
    /** 返回处理 **/
    $("#backBtn").click(function(){
        location.href= backUrl;
    });
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        searchForm.form('reset');
    })
    
    /** 表单校验 **/
    function validate(){
        if(!searchForm.form("validate")){
            return false;
        }
        if(!searchCheck()){
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
        receiveInfoDataGrid.datagrid('reloadData', queryParam);
    }
    
    /** 格式化时间 **/
    function formatDate(value){
        if (value == undefined) {
            return "";
        }
        var date = new Date(value);
        var year = date.getFullYear();
        var month = (date.getMonth() + 1);
        var day = date.getDate();
        if (month < 10) {
            month = "0" + month;
        }
        if(day < 10){
            day = "0"+ day;
        }
        return year+"-"+month+"-"+day;
    }
})
//** 查看客户详细资料 **//*
function personInfoTab(rowIndex) {
    var row = $('#receiveInfoDataGrid').datagrid('getRows')[rowIndex];
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

function loanbaseTab(rowIndex) {
    var row = $('#receiveInfoDataGrid').datagrid('getRows')[rowIndex];
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
