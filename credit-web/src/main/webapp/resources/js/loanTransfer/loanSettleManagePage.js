$(function () {
    // 导入文件表单对象
    var baseFileForm = $("#baseFileForm");
    // 批量导入窗口对象
    var importExcelWin = $("#importExcelWin");
    // 导入url
    var importUrl = global.contextPath + "/loanSettle/loanSettleImport";


    /** 打开导入窗口 **/
    $('#importLoanTransferBtn').click(function () {
        importExcelWin.window('open');
        baseFileForm.form('clear');
    });

    /**导入处理 **/
    $("#confirmImportBtn").click(function () {
        var file = $("#uploadFile").filebox("getValue");
        if ($.isEmpty(file)) {
            $.messager.alert('警告', '请选择导入文件!', 'warning');
            return;
        }
        $.messager.confirm("操作提示", "确认导入吗？", function (r) {
            if (r) {
                baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType: 'json',
                    url: importUrl,
                    hasDownloadFile: true,
                    success: function (data) {
                        $.messager.alert('信息', '导入成功');
                        setTimeout(function () {
                            importExcelWin.window('close');
                        }, 800);
                    },
                    error: function (data) {
                        $.messager.alert('警告', data.resCode + ':' + data.resMsg, 'warning');
                    }
                });
            }
        });
    });
});