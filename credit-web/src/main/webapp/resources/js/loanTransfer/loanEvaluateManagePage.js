$(function() {
	
	// 导入文件表单对象
    var baseFileForm = $("#baseFileFormEval");
    // 批量导入窗口对象
    var importExcelWinEval = $("#importExcelWinEval");   
    // 导入url
    var importUrl=global.contextPath+"/loanTransfer/loanEvaluateImport";
    // 导出url
    var exportUrl=global.contextPath+"/loanTransfer/loanEvaluateExport";
    
    /** 打开导入窗口 **/
    $('#importLoanEvaluateBtn').click(function(){
        importExcelWinEval.window('open');
        baseFileForm.form('clear');
    });
    
    /**导入处理 **/
    $("#confirmImportEvalBtn").click(function(){
        var file = $("#uploadFileEval").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("操作提示","确认导入吗？",function(r){
            if(r){
                baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: importUrl,
                    hasDownloadFile:true,                 
                    success: function (data) {
                    	$.messager.alert('信息','导入成功');
                        setTimeout(function(){
                            importExcelWinEval.window('close');
                        }, 500);
                    },
                    error: function (data) {
                    	$.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
                    }
                });
            }
        });
    });
    
    
    
    /** 导出债权转让信息 **/
    $("#exportLoanEvaluateBtn").click(function(){
        $.messager.confirm("提示","确认要导出债权转让文件吗？",function(r){
            if(r){
                $.downloadFile({
                    url:exportUrl,
                    isDownloadBigFile:true,
                    successFunc:function(data){
                        if(data == null){
                            $.messager.alert('提示','导出成功！','info');
                        }else{
                            if(data.resMsg!= null){
                                $.messager.alert('警告',data.resMsg,'warning');
                            }else{
                                $.messager.alert('异常','导出失败！','error');
                            }
                        }
                    },
                    failFunc:function(data){
                        $.messager.alert('异常','导出失败！','error');
                    }
                });
            }
        });
    });
});