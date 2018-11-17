<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>员工登陆页</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link href="${path}/resources/css/thirdparty/style.css" rel="stylesheet" type="text/css" />
		
		<script type="text/javascript">
			if (self != top) {
				/** 如果登陆页在iframe中显示的话，将URL重定向到顶层窗口 **/
				top.window.location.href = '${path}/system/user/jumpLogin';
			}
			importPluginsExt(['jquery','form'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/login.js');
					urlJs.push(global.contextPath + '/resources/js/thirdparty/cloud.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
					});
				});
			});
		</script>

		<script language="javascript">
			$(function(){
    			$('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
				$(window).resize(function(){  
   			    	$('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
   				 })  
			});  
        </script> 
	</head>
<body style="background-color:#1c77ac; background-image:url(${path}/resources/images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">
    <div id="mainBody">
		<div id="cloud1" class="cloud"></div>
		<div id="cloud2" class="cloud"></div>
	</div> 
	
	<div class="logintop">
		<span>欢迎登录证大后台管理界面平台</span>
		<!-- 
		<ul>
			<li><a href="#">忘记m</a></li>
			<li><a href="#">帮助</a></li>
			<li><a href="#">关于</a></li>
		</ul>
		 -->
	</div>

	<div class="loginbody">
	<span class="systemlogo"></span>
    <form id="loginForm" method="post" >
		<div class="loginbox">

			<ul>
				<li>
                  <input name="userCode" type="text" class="loginuser" value="" onclick="JavaScript:this.value=''"/>		
				</li>
				<li>
				   <input name="passWord" type="password" class="loginpwd"  onclick="JavaScript:this.value=''"/>
				</li>
				 
				<li>
				   <input onclick="submitForm()" id="loginBut"  type="button" class="loginbtn" value="登录" onclick="submitForm()" />
				   <label><a id="forgetPwd"  href="javascript:void(0)"  class="easyui-linkbutton"  data-options="plain:true" >>>忘记密码</a></label><label id="loginTip" style="color:red;"></label>
				</li>
			</ul>
		</div>
    </form>
	</div>

	<div class="loginbm">
		上海证大投资咨询有限公司 版权所有
	</div>
	 
</body>
</html>