$(function(){
	//登陆表单对象
	var resetForm = $('#loginForm');

	//地址（获取员工权限信息写入session，跳转到首页）
	var loginUrl = '/views/login.jsp';
	//验证员工和密码地址
	var verifyLoginUrl = '/system/user/verifyLogin';
	//忘记密码对应地址
	var resetpwd = '/system/user/resetPwd';
	
	//登陆事件
	$('#submitBtn').click(function(){
//		loginTip.html('登陆中,请稍等!');
		$('#submitBtn').attr("disabled",true);
//		if (resetForm.form("validate")) {
		
		var params = resetForm.serializeObject();
		
		 var strArray = window.location.search.substr(1).split("&");
		 $.each(strArray,function(index,value){
			  var param= value.split("=");
			  params[param[0]]=param[1];
		 });

		
			$.ajaxPackage({
				type : 'post', 
				url : global.contextPath + resetpwd ,
				data : params,
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					if (resCode == '000000') {
						//修改成功 跳转到首页
						alert('重置密码成功！');
						window.location.href = global.contextPath + loginUrl;
					} else {
						//修改失败 清空表单数据
						resetForm.form('clear');
						alert(data.resMsg);
						$('#submitBtn').attr("disabled",false);
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					alert('出错啦！');
					$('#submitBtn').attr("disabled",false);
				}
			});
//		}
	})
	
	
	//清空表单事件
	$('#clearBtn').click(function(){
		resetForm.form('clear');
	})
	
})