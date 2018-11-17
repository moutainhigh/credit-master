$(function() {
	Date.prototype.format = function (format) {  
	    var o = {  
	        "M+": this.getMonth() + 1, // month  
	        "d+": this.getDate(), // day  
	        "h+": this.getHours(), // hour  
	        "m+": this.getMinutes(), // minute  
	        "s+": this.getSeconds(), // second  
	        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
	        "S": this.getMilliseconds()  
	        // millisecond  
	    }  ;
	    if (/(y+)/.test(format))  
	        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
	            .substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	        if (new RegExp("(" + k + ")").test(format))  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
	    return format;  
	};
	
	function formatDateboxMMSS(value) {  
	    if (value == null || value == '') {  
	        return '';  
	    }  
	    var dt;  
	    if (value instanceof Date) {  
	        dt = value;  
	    } else {  
	        dt = new Date(value);  
	    }  
	    return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
	} 
	
	
	
	
    // 批量导入窗口对象
    var importExcelWinSsj = $("#importExcelWinSsj");
    var uploadUrl=global.contextPath+"/loan/loanSsjImport";
    
    // 导入文件表单对象
    var baseFileForm = $("#baseFileForm");
    var baseFileFormSsj = $("#baseFileFormSsj");
    
   
    /**
     *  随手记债权导入窗口
     */
    $('#ssjImportBtn').click(function(){
    	importExcelWinSsj.window('open');
        baseFileFormSsj.form('clear');
    });
    
    
    var tempfileName="";
    /** 批量导入处理随手记 **/
    $("#batchImportSsj").click(function(){
        var file = $("#uploadfileSsj").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("操作提示","确认导入吗？",function(r){
            if(r){
            	baseFileFormSsj.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: uploadUrl,
                    hasDownloadFile:true,
                    success: function () {
                        
                        
                    },
                    error: function (data) {
                    	var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        var attachment = data.attachment;
                        if (resCode == '000000') {
                        	$.messager.alert("提示", "操作成功");
                        	 setTimeout(function(){
                        		 importExcelWinSsj.window('close');
                                $("#sumMoney").text(parseFloat(data.attachment.sumMoney).toFixed(2));
                             	$("#digMoneyListTotal").text(data.attachment.digMoneyTotal);
                             	$("#successMoney").text(parseFloat(data.attachment.successMoney).toFixed(2));
                             	$("#successTotal").text(data.attachment.successBeans);
                             	$("#faileMoney").text(parseFloat(data.attachment.faileMoney).toFixed(2));                        	
                             	$("#failTotal").text(data.attachment.failBeans);
     							if((data.attachment.digMoneyTotal-0)>0){
     								$('#digSsjBtn').show();  
     								tempfileName=data.attachment.fileName;
     							}
     							if((data.attachment.failBeans-0)==0 &&(data.attachment.successBeans-0)>0 ){
     								$('#updateBatchNumSsj').show(); 
     							}else{
     								$('#updateBatchNumSsj').hide();
     							}
                          }, 1000);
                        	
                        }else{
                        	$.messager.alert('提示信息',resMsg,'warning');
                        }
//                        $.messager.alert('警告','文件上传失败!','warning');
                    }
                });
            }
        });
    });
    
    
    //导出文件
    $('#digSsjBtn').click(function(){     	
        var downloadUrl = global.contextPath + '/loan/downSsj';  
        location.href = downloadUrl+"?fileName="+tempfileName;
	}); 
    
    
    //更新批次号
    $('#updateBatchNumSsj').click(function(){     
    	$.messager.confirm("操作提示","\n确认生成批次号同时会删除导入文件？",function(r){
    		if(r){
    			var updateBatchNumUrl = global.contextPath + '/loan/updateBatchNumSsj';  
//    			 location.href =updateBatchNumUrl+"?fileName="+tempfileName;
    			 
    			 $.ajaxPackage({
    		   			type : 'get', 
    		   			url : updateBatchNumUrl+"?fileName="+tempfileName,
    		   			dataType : "json",
    		   			success : function (data,textStatus,jqXHR) {				
    		   				var resCode = data.resCode;				
    		   				var resMsg = data.resMsg;
    		   				var attachment = data.attachment;
    		   				if (resCode == '000000') {
    		   					$.messager.alert('提示信息',"操作成功！");
    		   					setTimeout(function(){
                                	 importExcelWinSsj.window('close');
        							$('#digSsjBtn').show();  
        							$('#updateBatchNumSsj').show();
                             }, 1000);	
    		   				} else {
    		   					$.messager.alert('提示信息',resMsg,'warning');
    		   				}
    		   			},
    		   			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
    		   				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
    		   			},
    		   			complete : function() {
    		   				
    		   			}
    		   		});
    			 
    			 
    		}
    	});
    }); 
});