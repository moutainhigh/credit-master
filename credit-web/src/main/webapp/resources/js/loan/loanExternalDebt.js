$(function() {
    /** 隐藏相关按钮 **/
    $('#eloanExportBtn').hide();
    $('#batchDownloadBtn').hide();
    $('#createBatchNum').hide();
    $('#updateBatchNumBtn').hide();
    /** 隐藏债权导出日期 **/
    $('#startQueryDate').hide();
    $('#startQueryDateValue').hide();
    // 隐藏相关显示
    $('#financialorg').combobox({
        onSelect : function() {
            // 理财机构
            var value = $('#financialorg').combobox('getValue');
            var querySelectvalue = $('#querySelect').combobox('getValue');
            if (querySelectvalue == '1') {// 查询历史批次号页面
                if (value == 'JMHZ') {
                    $('#startQueryDate').show();
                    $('#startQueryDateValue').show();
                    $('#eloanExportBtn').show();
                    $('#batchDownloadBtn').hide();
                } else {
                    $('#startQueryDate').hide();
                    $('#startQueryDateValue').hide();
                    $('#eloanExportBtn').hide();
                }
            } else {// 查询当天可生成批次
                if (value == 'JMHZ') {
                    $('#startQueryDate').show();
                    $('#startQueryDateValue').show();
                    $('#eloanExportBtn').show();
                    $('#batchDownloadBtn').hide();
                    $('#createBatchNum').hide();
                } else {
                    $('#startQueryDate').hide();
                    $('#startQueryDateValue').hide();
                    $('#eloanExportBtn').hide();
                    $('#batchDownloadBtn').hide();
                    $('#createBatchNum').show();
                }
            }
        },
        onChange : function(newValue, oldValue) {
            execute(newValue, oldValue);
        }
    });

    /** 查询处理 **/
    $('#searchButs').click(function() {
        var newValue = $('#financialorg').combobox('getValue');
        execute(newValue, null);
    });
    
    /** 理财机构下拉框切换处理 **/
    function execute(newValue, oldValue){
        // 理财机构
        var querySelectvalue = $('#querySelect').combobox('getValue');
        // 批次号
        var batchNum = $('#batchNum').val();
        var params = {};
        var url = global.contextPath;
        params.org = newValue;
        params.batchNum = batchNum;
        var queryString = $.param(params);
        if (queryString != null && queryString.length > 0) {
            queryString = "?" + queryString;
        } else {
            queryString = "";
        }
        if (querySelectvalue == '1') {// 查询批次号
            if (newValue == "XL") {// 新浪财富
                url += "/loan/querBatchNumDefault" + queryString;
            } else if (newValue == "WC") {// 挖财财富
                url += "/loan/querBatchNumDefault" + queryString;
            } else if(newValue=="LXXD"){//龙信小贷
            	url += "/loan/querBatchNumDefault" + queryString;
            }else if(newValue=="WMXT"){//外贸信托
            	url += "/loan/querBatchNumDefault" + queryString;
            }else if (newValue == "AT") {// 爱特
                url += "/loan/querBatchNumDefault" + queryString;
            } else if (newValue == "JMHZ") {// 积木盒子
                url += "/loan/querBatchNumJmhzDefault" + queryString;
            } else if (newValue == "WC2-") {
                url += "/loan/querBatchNumDefault" + queryString;
            } else if (newValue == "SSJ") {// 随手记
                url += "/loan/querBatchNumDefault" + queryString;
            } else if (newValue == "HM") {// 海门小贷
                url += "/loan/querBatchNumDefault" + queryString;
            }else if (newValue == "BHXT"){//渤海信托
                url += "/loan/querBatchNumDefault" + queryString;
            }else if (newValue == "BH2-"){//渤海2
                url += "/loan/querBatchNumDefault" + queryString;
            }else if (newValue == "WM2-"){//外贸2
                url += "/loan/querBatchNumDefault" + queryString;
            }else if (newValue == "BSYH"){//包商银行
                url += "/loan/querBatchNumDefault" + queryString;
            }else if (newValue == "HRBH"){//华瑞渤海
            url += "/loan/querBatchNumDefault" + queryString;
        }
        } else {// 查询当天批次号
            if (newValue == "XL") {// 新浪财富
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            } else if (newValue == "WC") {// 挖财财富
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            } else if(newValue=="LXXD"){//龙信小贷
            	url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            } else if(newValue=="WMXT"){//外贸信托
            	url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            } else if (newValue == "AT") {// 爱特
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            } else if (newValue == "JMHZ") {// 积木盒子
                url += "/loan/querBatchNumJmhzDefault" + queryString;
            } else if (newValue == "WC2-") {
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            } else if (newValue == "SSJ") {// 随手记
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            } else if (newValue == "HM") {// 海门小贷
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            }else if(newValue == "BHXT"){//渤海信托
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            }else if(newValue == "BH2-"){//渤海2
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            }else if(newValue == "WM2-"){//外贸2
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            }else if(newValue == "BSYH"){//包商银行
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            }else if(newValue == "HRBH"){//华瑞渤海
                url += "/loan/querCurrentDayBatchNumDefault" + queryString;
            }
        }
        // 隐藏更新批次按钮
        $('#updateBatchNumBtn').hide();
        $("#listLoanBaseDefault").attr("src", url);
    }

    /** 重置 **/
    $("#clearCondition").bind("click", function(envent) {
        if (!$(this).linkbutton("options").disabled) {
            $("#searchForm").form("reset");
            $('#eloanExportBtn').hide();
            $('#batchDownloadBtn').hide();
            $('#createBatchNum').hide();
            $('#updateBatchNumBtn').hide();
            $('#startQueryDate').hide();
            $('#startQueryDateValue').hide();
        }
    });
});
