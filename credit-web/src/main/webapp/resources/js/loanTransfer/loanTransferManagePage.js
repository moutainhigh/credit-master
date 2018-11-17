$(function() {
	// 查询条件
	var searchForm = $("#searchForm");
	// 导入文件表单对象
    var baseFileForm = $("#baseFileForm");
    // 批量导入窗口对象
    var importExcelWin = $("#importExcelWin");   
    // 查询批次号url
    var batchUrl=global.contextPath+"/loanTransfer/getLoanTransferBatchList";
    // 导入url
    var importUrl=global.contextPath+"/loanTransfer/loanTransferImport";
    // 导出url
    var exportUrl=global.contextPath+"/loanTransfer/loanTransferExport";
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
    	searchForm.form("reset");
    });
    
    
    
    /** 打开导入窗口 **/
    $('#importLoanTransferBtn').click(function(){
        importExcelWin.window('open');
        baseFileForm.form('clear');
    });
    
    /**导入处理 **/
    $("#confirmImportBtn").click(function(){
        var file = $("#uploadFile").filebox("getValue");
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
                            importExcelWin.window('close');
                        }, 800);
                        reloadTransferBatchData();
                    },
                    error: function (data) {
                    	$.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
                    }
                });
            }
        });
    });
    
    
    $('#transferBatch').combobox({
		valueField : 'id',
		textField : 'text',
		filter : function(q,row) {
			var opts=$(this).combobox("options");
			return row[opts.textField].indexOf(q)>-1;
		},
		formatter : function(row) {
			var opts=$(this).combobox("options");
			return row[opts.textField];
		}
	})
	
    function reloadTransferBatchData() {
		$.ajaxPackage({
			type : 'post', 
			url : batchUrl,
			isShowLoadMask : false,
			dataType : "json",
			success : function (data){
				data.unshift({"id":"","text":"请选择"});
				$('#transferBatch').combobox('loadDataExt',data);
				$('#transferBatch').combobox('defaultOneItem');
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
    
    /**
     * 加载转让批次号
     */
    reloadTransferBatchData();
    
    /** 导出债权转让信息 **/
    $("#exportLoanTransferBtn").click(function(){
        $.messager.confirm("提示","确认要导出债权转让文件吗？",function(r){
            if(r){
                $.downloadFile({
                    url:exportUrl,
                    isDownloadBigFile:true,
                    params:searchForm.serializeObject(),
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