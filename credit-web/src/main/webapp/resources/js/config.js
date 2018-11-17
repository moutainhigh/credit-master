$(function(){
	//修改密码
	$('#changeWd').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/changePassWD/changeWd',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"修改密码操作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	}) 
	//查看密码
	$('#viewWd').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/changePassWD/viewWd',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"查看密码操作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	}) 
	
	
	$('#dellog').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/logViewTest/dellog',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"删除操作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	}) 
	
	
	$('#viewlog').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/logViewTest/viewlog',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"查看日志操作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	}) 
	
	//员工修改
	$('#updateEmployee').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/userTest/update',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				if (resCode == '000000') {
					$.messager.alert('结果',"员工修改操作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	}) 
	
	//员工新增
		$('#insertEmployee').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/userTest/insert',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {	
				var resCode = data.resCode;				
				var resMsg = data.resMsg;			
				if (resCode == '000000') {
					$.messager.alert('结果',"员工新增作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	})
	
	//员工删除
		$('#delEmployee').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/userTest/delete',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"员工删除作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	})
	
	
	
	
	
	
//角色修改
	$('#updateRole').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/roleTest/update',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"角色修改操作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	}) 
	
	//角色新增
		$('#insertRole').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/roleTest/insert',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"角色修改操作成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	})
	
	//角色删除
		$('#delRole').click(function(){
		$.ajaxPackage({
			type : 'get', 
			url : global.contextPath+'/system/roleTest/delete',
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$.messager.alert('结果',"角色删除成功!");
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});	
	})
	
	
	
	
	
	
	
	
	
	
	
})