$(function() {
    $.applyReliefInfo = {
        repayForm:$("#repayForm"),
        applyReliefUrl:global.contextPath + '/applyReliefRepayManager/applyReliefSubmit',
        flowChartUrl:global.contextPath + '/applyReliefRepayManager/queryApproveFlowChart'
    };
    //应还金额
    var currAmount = parseFloat($("input[name='currAllAmount']").val());
    installApplyRelief();
    function installApplyRelief() {
        var fineMoney = $.applyReliefInfo.repayForm.find("input[name='fine']").val();
        var maxReleifMoney = $.applyReliefInfo.repayForm.find("input[name='maxReleifMoney']").val();
        var ruleInmaxReleifAmount = $.applyReliefInfo.repayForm.find("input[name='ruleInMaxReliefAmount']").val();
        var applyType = $.applyReliefInfo.repayForm.find("input[name='reliefTypeCode']").val();
        var specialReliefFlag = $.applyReliefInfo.repayForm.find("input[name='specialReliefFlag']").val();//默认为 非特殊减免 0 特殊减免 1

        $.applyReliefInfo.repayForm.find("input[name='afterReliefAmount']").val(currAmount);
        //默认填充罚息金额
/*        if (fineMoney > 0.01) {
            var afterRelief = (currAmount - fineMoney).toFixed(2);
            $.applyReliefInfo.repayForm.find("input[name='applyReliefMoney']").val(fineMoney);
            if (afterRelief < 0) {
                afterRelief = 0.00;
            }
            $("#applyReliefSpan").find("input[type='text']").val(fineMoney);
            $.applyReliefInfo.repayForm.find("input[name='afterReliefAmount']").val(afterRelief);
        }*/
        //标注 为通过特殊减免入口申请的减免
        if (specialReliefFlag == '1') {
            $("#showFlowChart").hide();
            toopTip("#applyReliefSpan","最大减免金额："+ maxReleifMoney);
            return;
        }
        if ("02" == applyType) {
            toopTip("#applyReliefSpan","规则内最大减免金额："+ (ruleInmaxReleifAmount*1 + fineMoney*1).toFixed(2));
        }else{
            toopTip("#applyReliefSpan","最大减免金额："+ maxReleifMoney);
        }
    }

    $("#applyReliefSpan").find("input[type='text']").focus(function(){
        $("#applyReliefSpan").find("input[type='text']").val("");
    });

    $("#applyReliefSpan").find("input[type='text']").blur(function(){
        var relief = parseFloat($("#applyReliefSpan").find("input[type='text']").val());
        if(!relief){
            return;
        }
        if(relief < 0.01){
            relief = 0.00;
            $(this).val(0.00);
        }
        var afterRelief = (currAmount-relief).toFixed(2);
        if (afterRelief < 0) {
            afterRelief = 0.00;
        }
        $.applyReliefInfo.repayForm.find("input[name='afterReliefAmount']").val(afterRelief);
        var specialReliefFlag = $.applyReliefInfo.repayForm.find("input[name='specialReliefFlag']").val();//默认为 非特殊减免 0 特殊减免 1
        //标注 为通过特殊减免入口申请的减免
        if (specialReliefFlag == '1') {
            //console.log("特殊减免申请不显示审批流程!");
            return;
        }
        showFlowChart();
    });
    $("#repaySubmitBut").click(function(){
        var relief = $.applyReliefInfo.repayForm.find("input[name='applyReliefMoney']").val();
        var memo = $.applyReliefInfo.repayForm.find("input[name='memo']").val();
        var loanId = $.applyReliefInfo.repayForm.find("input[name='loanId']").val();
        var applyType = $.applyReliefInfo.repayForm.find("input[name='reliefTypeCode']").val();
        var flowId = $.applyReliefInfo.repayForm.find("input[name='flowId']").val();
        var maxReleifMoney = $.applyReliefInfo.repayForm.find("input[name='maxReleifMoney']").val();
        var ruleInmaxReleifAmount = $.applyReliefInfo.repayForm.find("input[name='ruleInMaxReliefAmount']").val();
        var fineMoney = $.applyReliefInfo.repayForm.find("input[name='fine']").val();
        var repaymentLevel = $.applyReliefInfo.repayForm.find("input[name='repaymentLevel']").val();
        var specialReliefFlag = $.applyReliefInfo.repayForm.find("input[name='specialReliefFlag']").val();//默认为 非特殊减免 0 特殊减免 1
        var param = {
            "loanId":loanId,
            "applyType":applyType,
            "applyAmount":relief,
            "memo":memo,
            "flowId":flowId,
            "repayLevel":repaymentLevel,
            "isSpecial":specialReliefFlag
        };
        if(!relief){
            $.messager.alert('警告','您输入的金额不能为空','warning');
            return;
        }
        relief = parseFloat(relief);
        if(relief < 0.01){
            $.messager.alert('警告','您输入的金额不能小于0.01元,请修改!','warning');
            return;
        }
        // 标注 通过特殊减免入口申请的
        if (specialReliefFlag == '1') {
            if(relief > parseFloat(maxReleifMoney)){
                $.messager.alert('警告','您输入的金额已超过最大可减免金额！','warning');
                installApplyRelief();
                return;
            }
            $.messager.confirm('确认','确定申请减免操作?',function(r){ if (r) {
                submitApplyRelief(param);
            }});
            return;
        }
        if ($.isEmpty(flowId)) {
            $.messager.alert('警告','审批流程获取失败！请联系客服！','warning');
            installApplyRelief();
            return;
        }
        if ("02" == applyType && relief < parseFloat(maxReleifMoney) && relief > (ruleInmaxReleifAmount*1 + fineMoney*1)) {
                $.messager.confirm('确认','您申请的减免金额已超额，请走特殊审批流程!',function(r){ if (r) {
                    submitApplyRelief(param);
                }});
            return;
        }else{
            if(relief > parseFloat(maxReleifMoney)){
                $.messager.alert('警告','您输入的金额已超过最大可减免金额！','warning');
                installApplyRelief();
                return;
            }
        }
        $.messager.confirm('确认','确定申请减免操作?',function(r){ if (r) {
            submitApplyRelief(param);
        }});
    });
    $("#repayCloseBut").click(function(){
        window.parent.$('#applyReliefInfoWin').window('close');
        window.parent.$('#spApplyReliefInfoWin').window('close');
        $("#repayForm").form("reset");
    });
    /**
     * 提交减免申请操作
     * @param param
     */
    function submitApplyRelief(param){
        $.ajaxPackage({
            type : 'post',
            url : $.applyReliefInfo.applyReliefUrl,
            dataType : 'json',
            data : param,
            success : function (data,textStatus,jqXHR) {
                var resCode = data.resCode;
                var resMsg = data.resMsg;
                //从服务器上获取到记录信息
                var attachment = data.attachment;
                if (resCode == '000000') {
                    window.parent.$('#applyReliefInfoWin').window('close');
                    window.parent.$('#spApplyReliefInfoWin').window('close');
                    window.parent.$.applyReliefProcess.reloadDataGrid();
                } else {
                    $.messager.alert('警告',resMsg,'warning');
                }
            },
            error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                $.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
            },
            complete : function() {
            }
        });
    }

    function toopTip(idOrClass,showText){
        $(idOrClass).tooltip({
            position: 'top',
            content: '<span style="color:#6A6A6A">' + showText + '</span>',
            onShow: function(){
                $(this).tooltip('tip').css({
                    backgroundColor: '#ffffff',
                    borderColor: '#ff8c40'
                });
            }
        });
    }

    function showFlowChart(){
        var relief = $.applyReliefInfo.repayForm.find("input[name='applyReliefMoney']").val();
        var applyType = $.applyReliefInfo.repayForm.find("input[name='reliefTypeCode']").val();
        var overDueDate = $.applyReliefInfo.repayForm.find("input[name='overDueDate']").val();
        //var applyType = $.applyReliefInfo.repayForm.find("input[name='reliefTypeCode']").val();
        var ruleInmaxReleifAmount = $.applyReliefInfo.repayForm.find("input[name='ruleInMaxReliefAmount']").val();
        var maxReleifMoney = $.applyReliefInfo.repayForm.find("input[name='maxReleifMoney']").val();
        var fineMoney = $.applyReliefInfo.repayForm.find("input[name='fine']").val();
        //var isRuleIn = false;

/*        if ("02" == applyType && relief < parseFloat(maxReleifMoney) && relief > (ruleInmaxReleifAmount*1 + fineMoney*1)) {
            $.messager.confirm('确认','您申请的减免金额已超额，请走特殊审批流程!',function(r){ if (r) {
            }
            });
            return;
        }else{*/
            if(relief > parseFloat(maxReleifMoney)){
                $.messager.alert('警告','您输入的金额已超过最大可减免金额！','warning');
                installApplyRelief();
                return;
            }
        //}
/*        if ("02" == applyType  && relief <= (ruleInmaxReleifAmount*1 + fineMoney*1)) {
            //规则内减免
            isRuleIn = true;
        }*/
        var params = {
            "applyType":applyType,
            "OverDueDate":overDueDate,
            "applyReliefAmount":relief,
            "maxReliefAmount":maxReleifMoney,
            "fine":fineMoney,
            "ruleInMaxReliefAmount":ruleInmaxReleifAmount
        };
        //清空之前的流程
        $.applyReliefInfo.repayForm.find("input[name='flowId']").attr("value","");
        $("#approveFlowChart").empty();
        $.ajaxPackage({
            type : "post",
            url : $.applyReliefInfo.flowChartUrl,
            dataType : "json",
            data : params,
            success : function (data,textStatus,jqXHR) {
                var resCode = data.resCode;
                var resMsg = data.resMsg;
                var attachment = data.attachment;
                if (resCode == '000000') {
                    console.log("流程图："+attachment);
                    $.each(attachment,function(index,item){
                        if (this.stopNodeId == '0') {
                            $("#approveFlowChart").append(this.startNodeName);
                            $.applyReliefInfo.repayForm.find("input[name='flowId']").attr("value",this.flowId);
                            return;
                        }
                        $("#approveFlowChart").append(this.startNodeName+"→");
                    });
                    /** 服务端返回正常，创建流程图* */
                } else if(resCode == '800000'){
                    /** 操作警告提示 * */
                    $.messager.alert('警告',resMsg,'warning');
                } else {
                    /** 操作失败 * */
                    $.messager.alert('异常信息',resMsg,'error');
                }
            },
            error : function (XMLHttpRequest, textStatus, errorThrown,d) {
                parent.$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
            }
        });
    }
});
