<%@ page language="java"  pageEncoding="UTF-8"%>
<!-- 静态文件（JS、CSS等）公共引入 -->
<%
	String contextPath = request.getContextPath();
	session.setAttribute("path", contextPath);
%>
<link type="text/css"  rel="stylesheet"  href="<%=contextPath %>/resources/easyui/themes/gray/easyui.css?${buildNumber.value}"/>
<link type="text/css"  rel="stylesheet"  href="<%=contextPath %>/resources/easyui/themes/icon.css?${buildNumber.value}"/>
<link type="text/css"  rel="stylesheet"  href="<%=contextPath %>/resources/css/common.css?${buildNumber.value}"/>
<link type="text/css"  rel="stylesheet"  href="<%=contextPath %>/resources/css/easyuiExt.css?${buildNumber.value}"/>
<script type="text/javascript" charset="UTF-8" src="<%=contextPath %>/resources/easyui/jquery.min.js?${buildNumber.value}"></script>
<script type="text/javascript" charset="UTF-8" src="<%=contextPath %>/resources/easyui/jquery.cookie.js?${buildNumber.value}"></script>
<script type="text/javascript" charset="UTF-8" src="<%=contextPath %>/resources/js/common/systemConfig.js?${buildNumber.value}"></script>
<script type="text/javascript" charset="UTF-8" src="<%=contextPath %>/resources/js/common/jqueryMethodToolsExt.js?${buildNumber.value}"></script>
<script type="text/javascript" charset="UTF-8" src="<%=contextPath %>/resources/js/common/logger.js?${buildNumber.value}"></script>
<script type="text/javascript" charset="UTF-8" src="<%=contextPath %>/resources/js/common/easyuiExt/easyUILoaderExt.js?${buildNumber.value}"></script>

<script type="text/javascript">
	/** Maven构建工程里动态填充此值(buildNumber.value) **/
	var requestVersion = '${buildNumber.value}';
	if ($.loader) {
		$.extend($.loader,{
			/** 加载JS文件时，加上版本号参数，保证让客户端第一次加载始终获取最新内容 **/
			requestVersion : '' + requestVersion
		})
	}
	//easyloader.theme = 'gray';
	if (self != top) {
		/** 如果登陆页在iframe中显示的话，将URL重定向到顶层窗口 **/
		var requestUrl = window.location.href;
		if ($.endWith(requestUrl,'/views/index.jsp')) {
			top.window.location.href = window.location.href;
		}
	}
	var global = {};
	global.contextPath = '<%=contextPath%>';
	/** loading小图标 （32*32）**/
	global.smallLoading = global.contextPath + '/resources/images/smallLoading.gif';
	
	importPluginsExt([],'common', function() {
		$(function() {
			var urlJs = [];
			urlJs.push(global.contextPath + '/resources/easyui/jquery.easyui.min.js');
			urlJs.push(global.contextPath + '/resources/easyui/locale/easyui-lang-zh_CN.js');
			urlJs.push(global.contextPath + '/resources/js/common/common.js');
			urlJs.push(global.contextPath + '/resources/js/common/easyuiExt/comboboxExt.js');
			urlJs.push(global.contextPath + '/resources/js/common/easyuiExt/dataGridExt.js');
			urlJs.push(global.contextPath + '/resources/js/common/easyuiExt/dateBoxExt.js');
			urlJs.push(global.contextPath + '/resources/js/common/easyuiExt/switchButtonExt.js');
			urlJs.push(global.contextPath + '/resources/js/common/easyuiExt/validateBoxExt.js');
			urlJs.push(global.contextPath + '/resources/js/common/easyuiExt/formExt.js');
			urlJs.push(global.contextPath + '/resources/js/common/date.js');
			importCommonJSExt(urlJs,function(){
				$(function(){
					$.parser.onComplete = function() {
						$.myConsole.writeLog('EasyUI 渲染完成');
						$.loader.easyUIParseDone = true;
					}
					$.parser.parse();
				})
			});
		});
	});
</script>