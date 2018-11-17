$(function(){
	//登陆表单对象
	var loginForm = $('#loginForm');
	//登陆失败时Tip对象
	var loginTip = $('#loginTip');
	//地址（获取员工权限信息写入session，跳转到首页）
//	var loginUrl = '/system/user/login';
	var loginUrl = '/views/index.jsp';
	//验证员工和密码地址
//	var verifyLoginUrl = '/system/user/verifyLogin';
//	var verifyLoginUrl = '/login/verifyLogin';
	var verifyLoginUrl = '/j_spring_security_check';
	//忘记密码对应地址
	var forgetPwdUrl = '/system/user/forgetPwd';
	
	//登陆事件
	$('#loginBut').click(function(){
		alert('!'+verifyLoginUrl);
		loginTip.html('登陆中,请稍等!');
		$('#loginBut').attr("disabled",true);  
		if (loginForm.form("validate")) {
			$.ajaxPackage({
				type : 'post', 
				url : global.contextPath + verifyLoginUrl,
				data : loginForm.serialize(),
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					if (resCode == '000000') {
						//登陆成功 跳转到首页
						window.location.href = global.contextPath + loginUrl;
					} else {
						//登陆失败 清空表单数据
						loginForm.form('clear');
						loginTip.html(data.resMsg);
						$('#loginBut').attr("disabled",false);
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					loginTip.html(textStatus + '  :  ' + errorThrown + '!');
					$('#loginBut').attr("disabled",false);
				}
			});
		}
	})
	
	$('#forgetPwd').bind("click", function(){
		$.ajaxPackage({
			type : 'post', 
			url : global.contextPath + forgetPwdUrl,
			data : loginForm.serialize(),
			dataType : "json",
			success : function (data) { 
				var resCode = data.resCode;
				if (resCode == '000000') {
					//登陆成功 跳转到首页
					loginTip.html("已发送邮件！");
				} else {
					//登陆失败 清空表单数据
					loginForm.form('clear');
					loginTip.html(data.resMsg);
					$('#loginBut').attr("disabled",false);
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				loginTip.html(textStatus + '  :  ' + errorThrown + '!');
				$('#loginBut').attr("disabled",false);
			}
		});
	})
	
	//清空表单事件
	$('#resetBut').click(function(){
		loginForm.form('clear');
	})
	
})