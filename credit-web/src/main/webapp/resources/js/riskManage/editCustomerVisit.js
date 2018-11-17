$(function() {
    /** 保存回访记录url **/
    var saveUrl = global.contextPath + '/riskManage/saveCustomerVisitInfo';
    /** 返回回访管理页面url **/
    var backUrl = global.contextPath + '/riskManage/visitManage';
    /** 回访记录表单对象 **/
    var submitForm = $("#submitForm");
    /** 默认展开全部面板 **/
    $('#accordion').accordion('select','客户信息');
    $('#accordion').accordion('select','回访信息');
    
    /** 下拉框初始选中 **/
    $("#tel").combobox("setValue",$("#hidTel").val());
    $("#channel").combobox("setValue",$("#hidChannel").val());
    $("#sAttitude").combobox("setValue",$("#hidSAttitude").val());
    
    /** 保存处理 **/
    $("#submitBtn").click(function(){
        if(!check()){
            return;
        }
        // 债权Id
        var loanId = $("#loanId").val();
        $.messager.confirm("提示","确认保存客户回访信息吗？",function(r){
            if(r){
                $.ajaxPackage({
                    type : 'post', 
                    url : saveUrl,
                    data : submitForm.serialize(),
                    dataType : "json",
                    success : function (data) { 
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        if (resCode == '000000') {
                            $.messager.alert('提示','保存成功!','info');
                            /** 跳转到回访管理页面 **/
                            //setTimeout(function(){window.location.href = backUrl + "?loanId="+ loanId;}, 1000);
                            setTimeout(function(){
                                // 刷新回访管理页面
                                top.iframeDispatchEvent("refreashVisitManageInfo");
                                // 关闭回访信息保存页面
                                parent.$.iframeTabs.close({id:"saveCustomerVisitInfo",text:"保存客户回访信息"});
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
    
    /** 保存前的校验处理 **/
    function check(){
        if(!submitForm.form("validate")){
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
    
    /** 返回处理（返回回访管理页面） **/
    /*$("#backBtn").click(function(){
        // 债权Id
        var loanId = $("#loanId").val();
        window.location.href= backUrl + "?loanId="+ loanId;
    });*/
    
    /** 外拨软电话 **/
    $("#dial").click(function(){
        var phoneNum = $("#tel").combobox("getValue");
        if ($.isEmpty(phoneNum)){
            $.messager.alert('警告',"请选择电话！",'warning');
            return;
        }
        // 如果上次呼叫还处于话后处理状态，则自动完成话后处理
        if (csSoftPhone.getAgentInfo("AGENTSTATE") == 19) {
            csSoftPhone.finishWrapup();
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
        var isShanghaiDep = signsalesdepCode.substr(0, "01010001".length) == "01010001";
        // 去掉电话号码中的横线
        dailNumber = dailNumber.replace('-', '');
        // 对外地手机号的处理，外地的判断依据简单规则：根据网点判断而非根据手机号码判断
        if (isMphone && !isShanghaiDep) {
            return 90 + dailNumber;
        } else {
            return 9 + dailNumber;
        }
    }
})
