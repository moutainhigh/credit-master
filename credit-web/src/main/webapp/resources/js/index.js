$(function() {
	
	//菜单树对象
	var treeContainer = $('#treeContainer');
	//Tab选项卡对象
	var tabsPanel = $('#tabsPanel');
	var url=global.contextPath +'/system/baseMessage/message';
	var listcoutUrl=global.contextPath +'/system/baseMessage/listCount';
	/** 链接到报表系统地址 **/
	var mainUrl = '';
	if (!$.isEmpty(creditReportUrl)) {
		mainUrl = creditReportUrl + '/index/initHomePage';
	}
	var messageTip = $('#showMessageCount');
	$.loadMessageCount = function(){
		$.ajaxPackage({
			type : 'get', 
			url : listcoutUrl,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					if (attachment > 0) {
						//messageTip.html('消息中心(' + attachment + '条未读)</strong>');
						messageTip.html(attachment);
					}  else {
						messageTip.html('0');
					}
				} else {
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});		
	}
	
	$('#updatePassWord').click(function(){		
		var updatePassWdPanel=$('#updatePassWdPanel');		
		updatePassWdPanel.window('open');
	})
	
	//初始首页面板
	$.iframeTabs.add({
		id : 'index',
		text : '首页',
		iconCls : 'pic_44',
		closable : false,
        url : mainUrl
	});	
	$('#showMessage').click(function(){
		$.iframeTabs.add({'text':'消息中心','url':url});
	})
	
	$('#showMessageCount').click(function(){
		$.iframeTabs.add({'text':'消息中心','url':url});
	})

	$(".easyui-tree").tree({
        onBeforeExpand:function(node,param){          
        },   
        onClick : function(node){  
        	if (treeContainer.tree('isLeaf',node.target)) {        	
				var attributes = node.attributes;	
				if (attributes) {
					var tab = {};
					tab.id = 'Menu_' + node.id;
					tab.text = node.text;
					tab.url = attributes.url;
					tab.iconCls = node.iconCls;
					$.iframeTabs.add(tab);
				}
			} else {				
				//点击父菜单无事件
			}
  
        }
      });
	
		
	//清空表单事件
	$('#resetBut').click(function(){
		$('#updatePassWdForm').form('clear');
	})

	$('#submitBut').click(function(){
		updatePassWD();
	})
		function updatePassWD() {
		var updatePassWdPanel=$('#updatePassWdPanel');		
		var userCode=$('#userCode').val();
		var passWord=$('#passWord').val();
		var newPassWord=$('#newPassWord').val();
		var newPassWordAgain=$('#newPassWordAgain').val();	
		if(userCode==''){
			$.messager.alert('提示信息','用户名不能为空','error');
			return;
		}		
		if(passWord==''){
			$.messager.alert('提示信息','旧密码不能为空','error');
			return;
		}	
		if(newPassWord==''){
			$.messager.alert('提示信息','新密码不能为空','error');
			return;
		}
		if(newPassWord.length<6 ||newPassWord.length>10){
			$.messager.alert('提示信息','新的密码为6位-10位','error');
			return;
		}
		if(newPassWordAgain==''){
			$.messager.alert('提示信息','确认密码不能为空','error');
			return;
		}
		if(newPassWordAgain.length<6 ||newPassWordAgain.length>10){
			$.messager.alert('提示信息','确认密码为6位-10位','error');
			return;
		}
		//修改密码
		var updatePassWdForm = $('#updatePassWdForm');
		$.ajaxPackage({
			type : 'post', 
//			url : global.contextPath + '/system/user/savePassword'+'/'+userCode+'/'+passWord+'/'+newPassWord+'/'+newPassWordAgain,
			url : global.contextPath + '/system/user/savePassword',
			dataType : "json",
			data : updatePassWdForm.serialize(),
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				if (resCode == '000000') {
					$.messager.alert('结果',"密码修改成功!");
					updatePassWdPanel.window('close');
					setInterval(function(){top.window.location.href =global.contextPath+"/j_spring_cas_security_logout";},1000);		
					return;
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
		//显示窗口
	}
	

})