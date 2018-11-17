$(function(){	
		parent.$('#batchDownloadBtn').hide();
		parent.$('#createBatchNum').hide();
		parent.$('#updateBatchNumBtn').hide();
		parent.$('#eloanExportBtn').unbind().click(function(){
			var startQueryDate=parent.$('#grantBeginDate').datebox("getValue");
			var endQueryDate=parent.$('#grantEndDate').datebox("getValue");
			
	    	$.downloadFile({
	    		url:global.contextPath+"/loan/eloanExportJmhz"+"?"+"startQueryDate="+startQueryDate+"&endQueryDate="+endQueryDate,
	    		params:{},
	    		successFunc:function(data){
	    			if(data==null){
	    				$.messager.alert('提示','下载成功！','info');
	    			}else{
	    				if(data.resMsg!=""){
	    					$.messager.alert('提示',data.resMsg,'warning');		    
	    				}else{
	    					$.messager.alert('提示','下载失败！','warning');		  
	    				}
	    			}
	    		},
	    		failFunc:function(data){
	    			$.messager.alert('提示','下载失败！','error');
	    		},
	    		maskWindow:window.parent
	    		
	    	});
		});
	
		
		
		
		var importListFile = $("#importListFile");	
		var baseFileForm=$("#baseFileForm");
		var uploadUrl=global.contextPath+"/loan/importListFile";
	    $('#importListFileBtn').click(function(){
	    	baseFileForm.form('clear');
	    	importListFile.window('open');
	    	
	    });	
	    
	    $("#batchImport").unbind().click(function(){
	        var file = $("#uploadfile").filebox("getValue");
	        if($.isEmpty(file)){
	            $.messager.alert('警告','请选择导入文件!','warning');
	            return;
	        }
	        $.messager.confirm("操作提示","确认导入吗？",function(r){
	            if(r){
	            	baseFileForm.ajaxSubmit({
	                    type: "post",
	                    dataType : 'json',
	                    url: uploadUrl,
	                    hasDownloadFile:true,
	                    success: function () {
	                    	$.messager.alert('提示','下载成功！','info');
	                    },
	                    error: function (data) {
	                    	if(data!=null&&data.hasOwnProperty("resMsg")){
	                    		 $.messager.alert('操作提示',data.resMsg,'warning');
	                    	}else{
	                    		 $.messager.alert('操作提示','文件上传失败!','warning');
	                    	}
	                    }
	                });
	            }
	        });
	    });
		
});
