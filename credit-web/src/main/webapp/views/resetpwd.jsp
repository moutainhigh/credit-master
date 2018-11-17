<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>密码重置页</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link href="${path}/resources/css/thirdparty/style.css" rel="stylesheet" type="text/css" />
		
		<script type="text/javascript">
 			//if (self != top) {
				/** 如果登陆页在iframe中显示的话，将URL重定向到顶层窗口 **/
				//top.window.location.href = '${path}/system/user/jumpLogin';
			//} 
			importPluginsExt(['jquery','form'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/resetpwd.js');
					urlJs.push(global.contextPath + '/resources/js/thirdparty/cloud.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
					});
				});
			});
		</script>

<!-- 		<script language="javascript">
			$(function(){
    			$('.loginBox').css({'position':'absolute','left':($(window).width()-692)/2});
				$(window).resize(function(){  
   			    	$('.loginBox').css({'position':'absolute','left':($(window).width()-692)/2});
   				 })  
			});  
        </script>  -->
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
<center>

	<div class="loginbody">
	<h1><font face="verdana">重置密码</font></h1>
	<span class="systemlogo"></span>
    <form id="loginForm" method="post" >
		<div class='loginBox'>

			<ul>
				<li>
                  <p><font size="1" face="arial" >请输入密码：</font>
                  <input name="password1"  id="password1" type="password" class="loginpwd" value="" onclick="JavaScript:this.value=''"/>	</p>
				</li>
				<li>
				<br />
				   <p><font size="1" face="arial" >请确认密码：</font>
				   <input name="password2"  id="password2"  type="password" class="loginpwd" value="" onclick="JavaScript:this.value=''"/></p>
				</li>
				 
				<li>
					<br />
				   <a id="submitBtn"  href="javascript:void(0)"  class="easyui-linkbutton"  style="width:80px">确定</a>
				   <a id="clearBtn"  href="javascript:void(0)"  class="easyui-linkbutton"   style="width:80px">清空</a>
				   
				</li>
			</ul>
		</div>
    </form>
	</div>
</center>
	<div class="loginbm">
		上海证大投资咨询有限公司 版权所有
	</div>
	 
</body>
</html>