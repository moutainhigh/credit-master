/**
 * EasyUI 异步加载组件及JS脚本 公共方法
 * By Ivan
 */
$.loader = {};

/** 解析组件及加载脚本超时时间（毫秒） **/
$.loader.parseTimeOut = 20000;

/** EasyUI组件解析完成状态位 ** */
$.loader.easyUIParseDone = false;
/** 公共JS加载完成状态位 ** */
$.loader.commonJSLoadingDone = false;

/** 
 * 导入EasyUI组件
 *  plugins EasyUI插件名（可传集合）
 *  type common（代表引入公共插件） 、business（代表引入各模块插件）
 *  callback 所有插件加载成功回调方法
 *  
 * * */
$.loader.importPluginsExt = function(plugins, type, callback) {
		if ($.isFunction(callback)) {
			if (type == 'business') {
				
			}
			callback.call();
	}
}
window.importPluginsExt = $.loader.importPluginsExt;

/** 
 * 导入JS脚本核心方法
 *  jsUrl  JavaScript 脚本路径（可传集合）
 *  type common（代表引入公共JS） 、business（代表引入各模块JS）
 *  callback 所有脚本加载成功回调方法
 *  
 * * */
$.loader.coreImportJS = function(jsUrl,type, callback) {
	var jsUrlArr = [];
	if (typeof jsUrl == "string") {
		jsUrlArr.push(jsUrl);
	} else {
		for (var i = 0; i < jsUrl.length; i++) {
			jsUrlArr.push(jsUrl[i]);
		}
	};
	if (jsUrlArr.length == 0) {
		_doCallback();
	}
	function _loadJs(index) {
		var url = jsUrlArr[index];
		if (!url) {
			return;
		}
		/** 存在版本号参数时，添加到URL 防止读取客户端缓存 **/
		if (!$.isEmpty($.loader.requestVersion)) {
			url += '?' + $.loader.requestVersion;
		}
		var done = false;
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.language = 'javascript';
		script.src = url;
		// onreadystatechange和script.readyState属性事件是IE特有的，IE不支持script.onload
		// firefox,chrome等其它浏览器支持只script.onload事件
		script.onload = script.onreadystatechange = function() {
			if (!done
					&& (!script.readyState || script.readyState == 'loaded' || script.readyState == 'complete')) {
				done = true;
				script.onload = script.onreadystatechange = null;
				jsUrlArr.shift();
				_loadJs(0);
			}
		}
		document.getElementsByTagName("head")[0].appendChild(script);
	};
	/** 当前时间，计算超时 * */
	var curDate = new Date().getTime();
	/** 等待所有JS脚本加载成功 **/
	function _callbackTime() {
		window.setTimeout(function() {
			//$.myConsole.writeLog('1');
			/** 超过3秒放弃监听 * */
			if ((new Date().getTime() - curDate) >= $.loader.parseTimeOut) {
				/** 超时状态 **/
				
			} else {
				if (jsUrlArr.length == 0 ) {
					_doCallback();
				} else {
					/** 继续等待 **/
					_callbackTime();
				}
			}
		}, 100);
	};
	_callbackTime();
	/** 脚本已全部加载成功回调方法 **/
	function _doCallback() {
		
		/** 脚本加载成功回调方法 * */
		if (type == 'business') {
			$.myConsole.writeLog('页面加载总用时:' + (new Date().getTime() - $.system.curDate));
			$.initPageMast.hide();
			/** 恢复样式 **/
			$("body").css('overflowX','');
			$("body").css('overflowY','');
		}
		
		if (callback) {
			callback.call();
		}
	}
	_loadJs(0);
}

/** 
 * 各业务模块导入JS脚本
 *  jsUrl  JavaScript 脚本路径（可传集合）
 *  callback 所有脚本加载成功回调方法
 *  
 * * */
window.importJSExt = function(jsUrl, callback) {
	var curDate = new Date().getTime();
	function _importJSExt () {
		window.setTimeout(function(){
			//$.myConsole.writeLog('2');
			if ((new Date().getTime() - curDate) >= $.loader.parseTimeOut) {
				/** 超时状态 **/
				
			} else {
				/** 判断公共JS是否加载完成，各业务JS一定等公共JS加载完成之后加载 **/
				if ($.loader.commonJSLoadingDone && $.loader.easyUIParseDone) {
					$.loader.coreImportJS(jsUrl,'business',callback);
				} else {
					/** 处于等待状态 **/
					_importJSExt();
				}
			}
		}, 100)
	};
	_importJSExt();
}
/** 
 * 公共JS脚本导入
 *  jsUrl  JavaScript 脚本路径（可传集合）
 *  callback 所有脚本加载成功回调方法
 *  
 * * */
$.loader.importCommonJSExt = function(url, callback) {
	$.loader.coreImportJS(url,'common',function(){
		/** 公共JS加载完成 ** */
		$.loader.commonJSLoadingDone = true;
		if (callback) {
			callback.call();
		}
	})
	
}
window.importCommonJSExt = $.loader.importCommonJSExt;