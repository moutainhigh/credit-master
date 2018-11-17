$(function() {
    $.visitManage = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/riskManage/visitManageSearch',
        /** 加载保存客户回访记录页面地址 **/
        loadVisitUrl: global.contextPath + '/riskManage/loadEditCustomerVisit',
        /** 客户回访查询页面地址 **/
        viewCustVisitUrl: global.contextPath + '/riskManage/customerVisit',
        /** 保存回访记录url **/
        saveUrl : global.contextPath + '/riskManage/saveCustomerVisitInfo',
        /** 客户回访信息表格 **/
        visitManageDataGrid : $('#visitManageDataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 客户回访编辑窗口 **/
        custVisitWin : $("#custVisitWin"),
        /** 客户回访编辑表单对象 **/
        submitForm : $("#submitForm"),
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 每页显示的记录条数，默认为5 **/
        pageSize : 5,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [5,10,15,20,25],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            if ($.visitManage.searchForm.form('validate')) {
                /** 获取查询表单数据转换成JSON对象 **/
                var searchMsg = $.visitManage.searchForm.serialize();
                /** 对参数进行解码(显示中文) **/
                searchMsg = decodeURIComponent(searchMsg);
                /** 字符串转换为对象 **/
                var queryParam = $.serializeToJsonObject(searchMsg);  
                queryParam.url = $.visitManage.dataGridUrl;
                $.visitManage.visitManageDataGrid.datagrid('reloadData',queryParam);
            }
        }
    };
    
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.visitManage.pg = {
        'page' : 1,
        'rows' : $.visitManage.pageSize
    }
    
    /** DataGrid初始化 **/
    $.visitManage.visitManageDataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.visitManage.pg,
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
        nowrap : true,
        /** 禁止服务端排序 **/
        remoteSort:false,
        /** 允许多列排序 **/
        //multiSort:true,
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
            width : '7%',
            formatter:function(value,row,index){
                if(value){
                    return "<a href='javascript:void(0)' class='borrowerName' onclick='personInfoTab("+ index + ");' loanId='"+row.loanId+"' borrowerName='"+ value +"' >"+ value +"</a>";
                }
                return '';
            }
        }, {
            field : 'loanType',
            title : '借款类型',
            width : '6%',
            formatter:function(value,row,index){
                if(value){
                    return "<a href='javascript:void(0)' class='loanType' onclick='loanbaseTab("+ index + ");' loanId='"+row.loanId+"' loanType='"+ value +"' >"+ value +"</a>";
                }
                return '';
            }
        }, {
            field : 'salesmanName',
            title : '客户经理',
            width : '7%'
        }, {
            field : 'requestMoney',
            title : '申请金额',
            width : '7%',
            vType : 'rmb'
        }, {
            field : 'requestTime',
            title : '申请期限',
            width : '4%'
        }, {
            field : 'requestDate',
            title : '申请日期',
            width : '7%',
            //sortable:true,
            formatter:function(value,row,index){
                return $.DateUtil.dateFormatToStr(value);
            }
        }, {
            field : 'signDate',
            title : '签约日期',
            width : '5%',
            formatter:function(value,row,index){
                return $.DateUtil.dateFormatToStr(value);
            }
        }, {
            field : 'money',
            title : '审批金额',
            width : '7%',
            vType : 'rmb'
        }, {
            field : 'time',
            title : '借款期限',
            width : '4%'
        },{
            field : 'contractNum',
            title : '合同编号',
            width : '7%'
        },{
            field : 'grantMoneyDate',
            title : '放款日期',
            width : '5%',
            formatter:function(value,row,index){
                return $.DateUtil.dateFormatToStr(value);
            }
        }, {
            field : 'borrowerId',
            title : '操作',
            width : '9%',
            formatter:function(value,row,index){
                if(value){
                    var elements = "<a href='javascript:void(0)' class='visit' loanId='"+row.loanId+"' loanType='"+ row.loanType +"'>回访</a>";
                    if(row.visitNum > 0){
                        elements = "<a href='javascript:void(0)' class='visitRecord' style='color:#800000' loanId='"+row.loanId+"' borrowerName='"+ row.borrowerName +"'>回访记录</a>&nbsp;" + elements;
                    }
                    return elements;
                }
                return '';
            }
        } ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.visitManage.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.visitManage.pageSizeList,
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
        },
        onLoadSuccess:function(data){
            /** 回访按钮点击处理 **/
            $(".visit").click(function(){
                var loanId = $(this).attr("loanId");
                visit(loanId);
            });
            
            /** 回访记录按钮点击处理 **/
            $(".visitRecord").click(function(){
                var loanId = $(this).attr("loanId");
                var borrowerName = $(this).attr("borrowerName");
                viewCustvisit(loanId,borrowerName);
            });
        }
    });
    
    /** 表格分页组件 **/
    $.visitManage.pager = $.visitManage.visitManageDataGrid.datagrid('getPager');
    $.visitManage.pager.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.visitManage.pg.page = pageNumber;
            $.visitManage.pg.rows = pageSize;
            search();
        }
    });
    
    /** 页面自适应 **/
    $.visitManage.visitManageDataGrid.datagrid('resize');
    
    /** 查询处理 **/
    $("#searchBtn").click(function(){
        $.visitManage.pg.page = 1;
        search();
    });
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.visitManage.searchForm.form('reset');
    });
    
    /** 查询 **/
    function search(){
        if(searhCheck()){
            $.visitManage.reloadDataGrid();
        }
    }
    
    /** 查询校验 **/
    function searhCheck(){
        /** 获取查询表单数据转换成JSON对象 **/
        var searchMsg = $.visitManage.searchForm.serialize();
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
            && $.isEmpty(queryParam.contractNum)) {
            $.messager.alert('警告','请至少输入一个查询条件！','warning');
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
            $.messager.alert('警告','放款起始日期不能大于放款截止日期!','warning');
            return false;
        }
        return true;
    }
    
    /** 拨号方式切换事件 **/
    $("#dialMode").click(function(){
        var isChecked = $(this).prop("checked");
        if(isChecked){
            $("#labelTel").hide();
            $("#labelPhone").show();
        }else{
            $("#labelTel").show();
            $("#labelPhone").hide();
        }
    });
    
    /** 保存处理 **/
    $("#submitBtn").click(function(){
        if(!check()){
            return;
        }
        // 债权编号
        var loanId = $("#loanId").val();
        $.messager.confirm("提示","确认保存客户回访信息吗？",function(r){
            if(r){
                $.ajaxPackage({
                    type : 'post', 
                    url : $.visitManage.saveUrl,
                    data : $.visitManage.submitForm.serialize(),
                    dataType : "json",
                    success : function (data) { 
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        if (resCode == '000000') {
                            $.messager.alert('提示','保存成功！','info');
                            // 刷新一览信息页面
                            search();
                            setTimeout(function(){
                                // 关闭提示对话框
                                $(".messager-body").window('close');
                            }, 1000);
                        } else {
                            //操作失败
                            $.messager.alert('警告',resMsg,'warning');
                        }
                    },
                    error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                        $.messager.alert('异常',textStatus + '  :  ' + errorThrown + '!','error');
                    },
                    complete : function() {
                    }
                });
            }
        });
    });
    
    /** 关闭客户回访编辑窗口 **/
    $("#closeBtn").click(function(){
        if($.visitManage.custVisitWin){
            $.visitManage.custVisitWin.window('close');
        }
    });
    
    /** 保存前的校验处理 **/
    function check(){
        if(!$.visitManage.submitForm.form("validate")){
            return false;
        }
        
        // 必须先点击回访按钮，才能保存回访信息
        var loanId = $("#loanId").val();
        if($.isEmpty(loanId)){
            $.messager.alert('警告',"请选择一个客户回访！",'warning');
            return false;
        }
        // 渠道非空校验
        var channel = $("#channel").combobox("getValue");
        if($.isEmpty(channel)){
            $.messager.alert('警告',"渠道为必选项！",'warning');
            return false;
        }
        // 服务态度非空校验
        var sAttitude = $("#sAttitude").combobox("getValue");
        if($.isEmpty(sAttitude)){
            $.messager.alert('警告',"服务态度为必选项！",'warning');
            return false;
        }
        // 建议长度校验
        var advice = $("#advice").val();
        if(getByteLength(advice) > 1024){
            $.messager.alert('警告',"建议不能超过1024位！",'warning');
            return false;
        }
        // 备注长度校验
        var memo = $("#memo").val();
        if(getByteLength(memo) > 1024){
            $.messager.alert('警告',"备注不能超过1024位！",'warning');
            return false;
        }
        return true;
    }
    
    /** 获取字符串长度，一个汉字等于两个字节 **/
    function getByteLength(val) { 
        var length = 0; 
        for (var i = 0; i < val.length; i++) { 
            if (val[i].match(/[^x00-xff]/ig) != null){// 全角 
                length += 2; 
            }else{// 半角 
                length += 1;
            }
        }
        return length; 
    } 
    
    /** 外拨软电话 **/
    $("#dial").click(function(){
        // 必须先点击回访按钮，才能外拨电话
        var loanId = $("#loanId").val();
        if($.isEmpty(loanId)){
            $.messager.alert('警告',"请选择一个客户回访！",'warning');
            return false;
        }
        // 是否选择手动输入手机号码拨号
        var isChecked = $("#dialMode").prop("checked");
        var phoneNum = null;
        if(isChecked){
            phoneNum = $.trim($("#phone").val());
        }else{
            phoneNum = $("#tel").combobox("getValue");
        }
        if ($.isEmpty(phoneNum)){
            $.messager.alert('警告',"请选择或输入电话号码！",'warning');
            return;
        }
        
        /*var r = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?|(13\d{9})|(14[57]\d{8})|(15[0-35-9]\d{8})|(18[0-35-9]\d{8})$/;
        if(!r.test(phoneNum)){
            $.messager.alert('警告',"请选择或输入合法的电话号码！",'warning');
            return;
        }*/
        
        // 软电话控件对象
        var csSoftPhone = document.getElementById("csSoftPhone");
        // 软电话状态，如 0：未登录，3：已登录，19：话后处理状态
        var state = csSoftPhone.getAgentInfo("AGENTSTATE");
        // 如果上次呼叫还处于话后处理状态，则自动完成话后处理，所以只要页面不刷新，就不需反复连接软电话服务
        if (state == "19") {
            csSoftPhone.finishWrapup();
        }else if(state== "3"){ // 状态是3，表示已登录，无需再次连接软电话服务
            
        }else{
            // 连接软电话Server
            connectSoftPhone();
        }
        csSoftPhone.Dial(getTel(phoneNum),"");
    });
    
    /** 连接软电话Server **/
    function connectSoftPhone() {
        var csSoftPhoneObj = document.getElementById("csSoftPhone");
        // 登录用户的员工号
        var userCode = $("#userCode").val();
        // 工号必须配置到话务系统中，否则无法登陆连接软电话服务，测试工号可用：999
        csSoftPhoneObj.AgentID = userCode;
        //csSoftPhoneObj.AgentID = "999";
        csSoftPhoneObj.setInitParam("LOGONMODE","0");
        csSoftPhoneObj.setInitParam("GETLOCALSETTING","1");
        // 坐席消息弹屏  
        csSoftPhoneObj.setInitParam("AGENTMSGMODE",2);
        csSoftPhoneObj.connectServer();
        csSoftPhoneObj.logon();
    }

    /** 编辑获取电话号码 **/
    function getTel(dailNumber) {
        var endIndex = dailNumber.indexOf("（");
        if (endIndex != -1) {
            dailNumber = dailNumber.substring(0, endIndex);
        }
        // 首先判断是不是固话
        var isMphone = dailNumber.indexOf("-") == -1;
        // 对上海固话的处理，去掉前面的区号
        if (dailNumber.indexOf("021-") == 0) {
            dailNumber = dailNumber.replace('021-', '');
        }
        // 放款营业部的区域码
        var signsalesdepCode = $("#signsalesdepCode").val();
        // 是不是上海的营业部，01010001是上海所有营业部的前置区域码
        var isShanghaiDep = signsalesdepCode.substr(0, 8) == "01010001";
        // 去掉电话号码中的横线
        dailNumber = dailNumber.replace('-', '');
        // 如果号码前已经加9或者90、则直接返回这个号码
        if (dailNumber.substring(0, 1) == "9"){
            return dailNumber;
        }
        // 如果是上海本地的手机号码，则前面加9
        if (isMphone && isShanghaiDep) {
            return 9 + dailNumber;
        }
        // 对外地手机号的处理，外地的判断依据简单规则：根据网点判断而非根据手机号码判断
        return 90 + dailNumber;
    }
    
    /** 初始化回访编辑窗口 **/
    function initVisitWin(){
        // 清空表单对象
        $.visitManage.submitForm.form('clear');
        // 折叠客户信息面板 
        $('#accordion').accordion('select','客户信息');
        // 展开回访信息面板 
        $('#accordion').accordion('select','回访信息');
    }
    // 初始化回访编辑窗口
    initVisitWin();
})

/** 查看客户详细资料 **/
function personInfoTab(rowIndex) {
    var row = $.visitManage.visitManageDataGrid.datagrid('getRows')[rowIndex];
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
    var row = $.visitManage.visitManageDataGrid.datagrid('getRows')[rowIndex];
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

/** 点击回访按钮处理 **/
function visit(loanId){
    $.ajaxPackage({
        url:$.visitManage.loadVisitUrl,
        type:"get",
        data : {"loanId":loanId},
        dataType:"json",
        success:function(response, textStatus, jqXHR){
            var resCode = response.resCode;
            var resMsg = response.resMsg;
            var attachment = response.attachment;
            var loan = attachment.loan;
            var personVisit = attachment.personVisit;
            var personTelInfos = attachment.personTelInfos;
            var user = attachment.user;
            if(resCode == "000000"){
                // 清空表单对象
                $.visitManage.submitForm.form('clear');
                
                // 设置客户债权信息
                $("#visitBorrowerName").text(loan.borrowerName);
                $("#visitSignsalesdepName").text(loan.signsalesdepName);
                $("#visitLoanType").text(loan.loanType);
                $("#visitSalesmanName").text(loan.salesmanName);
                
                $("#visitRequestDate").text($.DateUtil.dateFormatToStr(loan.requestDate));
                $("#visitRequestMoney").text($.comdify(loan.requestMoney,2));
                $("#visitRequestTime").text(loan.requestTime);
                $("#visitSignDate").text($.DateUtil.dateFormatToStr(loan.signDate));
                
                $("#visitGrantMoneyDate").text($.DateUtil.dateFormatToStr(loan.grantMoneyDate));
                $("#visitMoney").text($.comdify(loan.money,2));
                $("#visitTime").text(loan.time);
                $("#visitSalesdepartmentName").text(loan.salesdepartmentName);
                
                // 加载表单数据
                $.visitManage.submitForm.form("load",{"loanId":loan.loanId,"signsalesdepCode":loan.signsalesdepCode});
                if (personTelInfos != null){
                    // 加载电话下拉列表框
                    $('#tel').combobox('loadData',personTelInfos);
                }
                if(personVisit != null){
                    $.visitManage.submitForm.form("load",personVisit);
                    $.visitManage.submitForm.form("load",{"phone":personVisit.tel});
                }
                
                // 折叠客户信息面板 
                $('#accordion').accordion('select','客户信息');
                // 展开回访信息面板 
                $('#accordion').accordion('select','回访信息');
                // 显示电话选择下拉框
                $("#labelTel").show();
                // 隐藏电话输入框
                $("#labelPhone").hide();
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

/** 点击回访记录按钮处理 **/
function viewCustvisit(loanId,borrowerName){
    /** 债权编号 **/
    var id = loanId;
    /** tab页名称 **/
    var name = "客户回访查询-"+ borrowerName;
    var tab = {};
    tab.id = 'customerVisit_' + id;
    tab.iconCls = 'pic_56';
    tab.text = name;
    tab.url = $.visitManage.viewCustVisitUrl + '?loanId=' + id;
    /** 调用父级添加选项卡方法 **/
    parent.$.iframeTabs.add(tab);
}
