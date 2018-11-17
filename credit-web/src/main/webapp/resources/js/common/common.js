(function($){
	//扩展jquery对象，添加serializeObject方法，序列化表单返回Object
	jQuery.fn.extend({
		serializeObject: function(addBlank) {
			var obj = {},
			array = this.serializeArray(),
			addBlank=addBlank||false;
			$.each(array,function(arrayIndex,dataObj){
				var isAdd = false;
				if(dataObj.value!=null && $.trim(dataObj.value)!=""){
					isAdd=true;
				}else{
					isAdd=addBlank;
				}
				if(isAdd){
					if(!obj.hasOwnProperty(dataObj.name)){
						obj[dataObj.name]=dataObj.value;
					}else{
						if($.isArray(obj[dataObj.name])==true){
							obj[dataObj.name].push(dataObj.value);
						}else{
							var newArray = [];
							newArray.push(dataObj.value);
							obj[dataObj.name]=newArray;
						}
					}
				}
			});
			return obj;
		}
	});
	
	var r20 = /%20/g,
	rbracket = /\[\]$/,
	rCRLF = /\r?\n/g,
	rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
	rsubmittable = /^(?:input|select|textarea|keygen)/i;
	
	function buildParams( prefix, obj, traditional, add ) {
		var name;

		if ( jQuery.isArray( obj ) ) {
			// Serialize array item.
			jQuery.each( obj, function( i, v ) {
				if ( traditional || rbracket.test( prefix ) ) {
					// Treat each array item as a scalar.
					add( prefix, v );

				} else {
					// Item is non-scalar (array or object), encode its numeric index.
					buildParams( prefix + "[" + ( typeof v === "object" ? i : "" ) + "]", v, traditional, add );
				}
			});

		} else if ( !traditional && jQuery.type( obj ) === "object" ) {
			// Serialize object item.
			for ( name in obj ) {
				buildParams( prefix + "[" + name + "]", obj[ name ], traditional, add );
			}

		} else {
			// Serialize scalar item.
			add( prefix, obj );
		}
	}
	
	jQuery.param = function( a, traditional ) {

		var prefix,
			s = [],
			add = function( key, value ) {
				// If value is a function, invoke it and return its value
				value = jQuery.isFunction( value ) ? value() : ( value == null ? "" : value );
				s[ s.length ] = encodeURIComponent( key ) + "=" + encodeURIComponent( value );
			};

		// Set traditional to true for jQuery <= 1.3.2 behavior.
		if ( traditional === undefined ) {
			traditional = jQuery.ajaxSettings && jQuery.ajaxSettings.traditional;
		}

		// If an array was passed in, assume that it is an array of form elements.
		if ( jQuery.isArray( a ) || ( a.jquery && !jQuery.isPlainObject( a ) ) ) {
			// Serialize the form elements
			jQuery.each( a, function() {
				add( this.name, $.trim(this.value));
			});

		} else {
			// If traditional, encode the "old" way (the way 1.3.2 or older
			// did it), otherwise encode params recursively.
			for ( prefix in a ) {
				buildParams( prefix, a[ prefix ], traditional, add );
			}
		}

		// Return the resulting serialization
		//return s.join( "&" ).replace( r20, "+" );
		return s.join( "&" );
	};
	
		//定义系统命名空间
		window.zd={};
		
		//按钮禁用时间
		zd.disableTime = 30000;
		
		/** 页面初始化Loading遮罩层 **/
		$.initPageMast = {}
		$.initPageMast.hide = function() {
			$('#initPageMastDiv.initPageMast').hide();
		}
		
		/** 遮罩层板块 **/
		/** 遮罩层参数**/
		var loadMaskOpts = {};
		loadMaskOpts.container = $("body");
		loadMaskOpts.loadMsg = '处理中,请稍等...';
		
		var wrap = loadMaskOpts.container;
		
		$.loadMask = {};
		/** 显示 **/
		$.loadMask.show = function(flag) {
			flag = flag || new Date().getTime();
//			if ($('div.datagrid-mask').length == 0) {
				$("<div class=\"datagrid-mask datagrid-mask_" + flag + "\"></div>").css({"z-index":"9998",display:"block",width:wrap.width(),height:wrap.height()}).appendTo(wrap);
				$("<div class=\"datagrid-mask-msg datagrid-mask-msg_" + flag + "\"></div>").html(loadMaskOpts.loadMsg).appendTo(wrap).css({"padding-top":"10px","padding-right":"10px","padding-bottom":"12px","z-index":"9999",display:"block",left:(wrap.width()-$("div.datagrid-mask-msg",wrap).outerWidth())/2,top:(wrap.height()-$("div.datagrid-mask-msg",wrap).outerHeight())/2});
//			}
		}
		/** 隐藏 **/
		$.loadMask.hide = function(flag) {
			flag = flag || new Date().getTime();
			wrap.find("div.datagrid-mask-msg_" + flag).remove();
			wrap.find("div.datagrid-mask_" + flag).remove();
		}
		/** 遮罩层板块 **/
		
		//Ajax配置项默认值
		$.ajaxVariable = {};
		//默认数据包
		$.ajaxVariable.data = {};
		//默认异步、同步标识
		$.ajaxVariable.async = true;
		//默认请求方式
		$.ajaxVariable.type = 'get';
		//默认返回的数据类型
		$.ajaxVariable.dataType = 'json';
		//是否显示遮罩层
		$.ajaxVariable.isShowLoadMask = true;
		//请求超时时间(毫秒)
		$.ajaxVariable.timeout = 60000;
		//默认回调函数
		$.ajaxVariable.emptyFun = function() {};
		
		/**
		 * 针对JQuery.Ajax进行封装(便于内部控制)
		 * 
		 * url 请求远程地址
		 * data 推送远程数据包 如{'date':'20150505','time':'141414'} 或 date=20150505&time=141414
		 * async 默认值: true。默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。
	     *       	 注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
		 * type  请求方式("POST" 或 "GET")， 默认为 "GET"
		 * dataType  预期服务器返回的数据类型，常用的如：xml、html、json、text
		 * success  成功回调函数
		 * error 失败回调函数
		 * complete 完成回调函数(成功和失败均回调)
		 * 
		 */
		$.ajaxPackage = function(options) {
			var url = options.url;
			var data = options.data;
			var async = options.async;
			var type = options.type;
			var dataType = options.dataType;
			var success = options.success;
			var error = options.error;
			var complete = options.complete;
			var isShowLoadMask = options.isShowLoadMask;
			var timeout = options.timeout;
			/** 遮罩层随机数 **/
			var r = new Date().getTime();
			
			if ($.isEmpty(url)) {
				/** 缺少远程地址参数 **/
				$.myConsole.writeWarnLog('缺少远程地址参数');
				return;
			}
			data = $.isEmpty(data) ? $.ajaxVariable.data : data;
			async = $.isEmpty(async) ? $.ajaxVariable.async : async;
			type = $.isEmpty(type) ? $.ajaxVariable.type : type;
			isShowLoadMask = $.isEmpty(isShowLoadMask) ? $.ajaxVariable.isShowLoadMask : isShowLoadMask;
			timeout = $.isEmpty(timeout) ? $.ajaxVariable.timeout : timeout;
			
			type = $.toLowerCase(type);
			if (type != 'get' && type != 'post') {
				/** 请求方式参数有误 **/
				$.myConsole.writeWarnLog('请求方式参数有误');
				return;
			}
			
			dataType = $.isEmpty(dataType) ? $.ajaxVariable.dataType : dataType;
			dataType = $.toLowerCase(dataType);
			if (dataType != 'xml' && dataType != 'html' && dataType != 'script' && dataType != 'json' 
				&& dataType != 'jsonp' && dataType != 'text') {
				/** 请求方式参数有误 **/
				$.myConsole.writeWarnLog('请求方式参数有误');
				return;
			}
			//成功回调函数
			success = $.isFunction(success) ? success : $.ajaxVariable.emptyFun;
			//失败回调函数
			error = $.isFunction(error) ? error : $.ajaxVariable.emptyFun;
			//完成回调函数
			complete = $.isFunction(complete) ? complete : $.ajaxVariable.emptyFun;
			//追加随机数
			url = $.urlAppendRandom(url);
			
			if (isShowLoadMask) {
				/** 显示遮罩层 **/
				$.loadMask.show(r);
			}
			$.ajax({
				url : url,
				type : type,
				timeout : timeout,
				data : data,
				async : async,
				dataType : dataType,
				success : function (data,textStatus,xmlHttpRequest) { 
					var resCode = data.resCode;	
					var resMsg = data.resMsg;	
					if (resCode == '900001') {
						$.messager.alert('提示信息',resMsg,'error');
						return;
					}
					if (resCode == '900002') {
						//top.window.location.href =global.contextPath+"/system/user/jumpLogin";
						$.messager.alert('会话超时','会话超时，系统处理中，请稍等！','warning');
						window.setTimeout(function(){
							window.location.reload(true);
							//top.window.location.href =global.contextPath+"/views/index.jsp";
						}, 200);
						return;
					}
					try {
						success.call(xmlHttpRequest,data,textStatus,xmlHttpRequest);
					} catch(e){
						$.messager.alert('提示信息','Ajax 返回解析异常(' + e + ')','error');
					}
				},
				error : function (xmlHttpRequest, textStatus, errorThrown) {
					error.call(xmlHttpRequest,xmlHttpRequest,textStatus,errorThrown);
				},
				complete : function(xmlHttpRequest,textStatus) {
					if (isShowLoadMask) {
						/** 隐藏遮罩层 **/
						$.loadMask.hide(r);
					}
					complete.call(xmlHttpRequest,xmlHttpRequest,textStatus);
				}
			});
		}
	
		
	/**
	 * 添加html5 iframe之间传递消息
	 */
	$(window).on("message",function(event){
		var data={};
		if(event.originalEvent!=null){
			data = JSON.parse(event.originalEvent.data)||{};
		}
		window.iframeDispatchEvent(data.eventType,data.params);
	});

	/**
	 * @author 00236633
	 * 窗口之间传递事件
	 */
	window.iframeDispatchEvent=function(eventType,params){
		if(eventType!=null){
			try{
				$(window).trigger(eventType,[params]);
			}catch(e){
				console.debug(e);
			}
			$("iframe").each(function(index, iframeElement) { 
				try{
					//同域iframe之间传递消息
					if(iframeElement.contentWindow.iframeDispatchEvent!=null){
						iframeElement.contentWindow.iframeDispatchEvent(eventType,params);
					}
				}catch(e){
					try{
						//不同域iframe之间传递消息
						if(iframeElement.contentWindow.postMessage!=null){
							iframeElement.contentWindow.postMessage(JSON.stringify({eventType:eventType,params:params}),"*");
						}
					}catch(e1){
						console.debug(e);
					}
				}
			});
		}
	};
	
	/**
	 * 获取应用系统前台uuid
	 */
	window.getAppUuid = function () {
	    var s = [];
	    var hexDigits = "0123456789abcdef";
	    for (var i = 0; i < 36; i++) {
	        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	    }
	    s[14] = "4";
	    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
	    s[8] = s[13] = s[18] = s[23] = "-";
	    var uuid = s.join("")+(jQuery.guid++);
	    return uuid;
	};
	
	/**
	 * url 请求url
	 * params 提交到后台参数
	 * successFunc 下载成功回调
	 * failFunc 下载失败回调
	 * maskWindow 遮罩window
	 * @author 00236633
	 */
	$.downloadFile = function(options){
		var opts = $.extend({}, $.downloadFile.defaults,options);
		
		opts.htmlDownloadFileToken=window.getAppUuid();
		opts.r = new Date().getTime();
		opts.url=opts.url||"";
		opts.params=opts.params||{};
		opts.maskWindow=opts.maskWindow||window;
		if(!$.isWindow(opts.maskWindow)){
			opts.maskWindow=window;
		}
		opts.htmlDownloadFileTokenName = "htmlDownloadFileToken";
		
		if(opts.isDownloadBigFile === true ){
			opts.params.isBigFile=true;
		}
		
		if($.trim(opts.url)=="" ){
			return;
		}

		if(!$.isEmptyObject(opts.params)){
			var queryString = $.param(opts.params)||"";
			queryString=$.trim(queryString);
    		if(queryString.length>0 && opts.url.indexOf("&")==-1){
    			queryString="?"+queryString;
    		}
    		opts.url=opts.url+queryString;
		}	
		
		var frameId = 'downloadFile_Frame_' + (new Date().getTime());
		var $frame = $('<iframe id='+frameId+' name='+frameId+'></iframe>').appendTo('body');
		$frame.css({
			position:'absolute',
			top:-1000,
			left:-1000
		});
		$frame.on('load', function(event){
			completeHandle(false);
		});
		
		//cookie检查
		function cookieCheckFunction(){
			try{
				var cookieValue  = $.cookie(opts.htmlDownloadFileToken);
				if(cookieValue!=null){
					cookieCheckCompleteHandle.timeOutId = setTimeout(cookieCheckCompleteHandle,3000);
				}else{
					cookieCheckFunction.timeOutId=setTimeout(cookieCheckFunction,1000);
				}
			}catch(e){
				console.info(e);
			}
		}
		
		//发现cookie
		function cookieCheckCompleteHandle(){
			if(opts.isDownloadBigFile === true ){
				var params = {
					htmlDownloadFileToken:opts.htmlDownloadFileToken
				};
				
				function tempFunction(){
					$.ajaxPackage({
						url:global.contextPath + '/frameWork/downloadBigFile',
						async:true,
						type:"POST",
						data:params,
						dataType:"json",
						isShowLoadMask:false,
						success:function(response, textStatus, jqXHR){
							if(response.resCode == "000000" && response.attachment.downloadFileState === true ){
								 completeHandle(true);
							}else{
								setTimeout(tempFunction,5000);
							}
						},
						error:function(response, textStatus, jqXHR){
							completeHandle(true);
						},
						complete:function(jqXHR,textStatus){
						}
					});
				}
				setTimeout(tempFunction,0);
			}else{
				 completeHandle(true);
			}
		}

		//下载完成后处理
		function completeHandle(result){
			try{
				if(cookieCheckCompleteHandle.timeOutId!=null){
					clearTimeout(cookieCheckCompleteHandle.timeOutId);
					cookieCheckCompleteHandle.timeOutId=null;
				}
			}catch(e){
			}
			try{
				if(cookieCheckFunction.timeOutId!=null){
					clearTimeout(cookieCheckFunction.timeOutId);
					cookieCheckFunction.timeOutId=null;
				}
			}catch(e){
			}
			
			if(completeHandle.cbInvoked==null){
				completeHandle.cbInvoked=0;
			}
			if(completeHandle.cbInvoked>0){
				return;
			}
			
			if(result==true){
				if($.isFunction(opts.successFunc)){
					try{
						opts.successFunc();	
					}catch(e){
						console.info(e);
					}
				}else{
					console.info(opts.successFunc+"不是函数");
				}
			}else{
				var data = null;
				try{
    				var body = $($frame[0].contentWindow.document.body);
    				data = body.html();
    				var ta = body.find('>textarea');
    				if (ta.length){
    					data = ta.val();
    				} else {
    					var pre = body.find('>pre');
    					if (pre.length){
    						data = pre.html();
    					}else{
    						var div = body.find('>div');
    						if(div.length>0){
    							try{
    								data=JSON.stringify(div.data());
    							}catch(e){
    							}
    						}
    					}
    				}
					eval("data = " + data);
					if($.isFunction(opts.successFunc)){
						try{
							opts.successFunc(data);	
						}catch(e){
							console.info(e);
						}
					}else{
						console.info(opts.successFunc+"不是函数");
					}
				}catch(e){
					if($.isFunction(opts.failFunc)){
						try{
							opts.failFunc(e);	
						}catch(e){
							console.info(e);
						}
					}else{
						console.info(opts.failFunc+"不是函数");
					}
				}
			}
				
			opts.maskWindow.$.loadMask.hide(opts.r);
			try{
				$.cookie(opts.htmlDownloadFileToken, '', { expires: -1,path:global.contextPath });
			}catch(e){
				console.info(e);
			}	
			try{
				$.cookie(opts.htmlDownloadFileTokenName, '', { expires: -1,path:global.contextPath });
			}catch(e){
				console.info(e);
			}
			opts=null;
			$frame.unbind();
			$frame.remove();
		}
		
		
		opts.maskWindow.$.loadMask.show(opts.r);
		$.cookie(opts.htmlDownloadFileTokenName,opts.htmlDownloadFileToken,{path:global.contextPath });
		cookieCheckFunction();
		$frame.attr('src', opts.url);
	};
	//下载文件默认配置
	$.downloadFile.defaults={
		url: null,
		isDownloadBigFile:false,
		params: {},
		successFunc:function(){
			
		},
		failFunc:function(){
			
		},
		maskWindow:window			
	};
	
	/**
	 * 对于下载文件加载加遮罩
	 */
	$.downloadFileSpecialHandle = function(successFunc,failFunc,maskWindow){
		maskWindow=maskWindow||window;
		if(!$.isWindow(maskWindow)){
			maskWindow=window;
		}
		
		//下载完成后处理
		function completeHandle(){
			if(completeHandle.times==null){
				completeHandle.times=0;
			}
			completeHandle.errorResult=!!completeHandle.errorResult;
			completeHandle.times+=1;
			if(completeHandle.errorResult==false && completeHandle.times <=6 ){
				setTimeout(completeHandle,500);
			}else {
				if(completeHandle.errorResult==false){
					if($.isFunction(successFunc)){
						try{
							successFunc(completeHandle.data);	
						}catch(e){
							console.info(e);
						}
					}else{
						console.info(successFunc+"不是函数");
					}
				}else{
					if($.isFunction(failFunc)){
						try{
							failFunc(completeHandle.data);	
						}catch(e){
							console.info(e);
						}
					}else{
						console.info(failFunc+"不是函数");
					}
				}
				
				maskWindow.$.loadMask.hide(cookieCheckFunction.r);
				try{
					if(cookieCheckFunction.timeOutId!=null){
						clearTimeout(cookieCheckFunction.timeOutId);
						cookieCheckFunction.timeOutId=null;
					}
				}catch(e){
					console.info(e);
				}	
				try{
					$.cookie('htmlDownloadFileToken', '', { expires: -1,path:global.contextPath });
				}catch(e){
					console.info(e);
				}
				try{
					$.cookie(cookieCheckFunction.htmlDownloadFileToken, '', { expires: -1 ,path:global.contextPath});
				}catch(e){
					console.info(e);
				}	
			}
		}
		
		function cookieCheckFunction(){
			try{
				cookieCheckFunction.cookieValue  = $.cookie(cookieCheckFunction.htmlDownloadFileToken);
				if(cookieCheckFunction.cookieValue!=null){
					completeHandle();
				}else{
					cookieCheckFunction.timeOutId=setTimeout(cookieCheckFunction,1000);
				}
			}catch(e){
				console.info(e);
			}
		}
		
		//下载文件回调预处理，设置cookie和监控cookie
		cookieCheckFunction.r = new Date().getTime();
		maskWindow.$.loadMask.show(cookieCheckFunction.r);
		cookieCheckFunction.htmlDownloadFileToken=window.getAppUuid();
		$.cookie("htmlDownloadFileToken",cookieCheckFunction.htmlDownloadFileToken,{path:global.contextPath});
		cookieCheckFunction();
		
		return function(success,data,immediateCallBack){
			completeHandle.errorResult=!!success;
			completeHandle.data=data;
			if(immediateCallBack===true){
				completeHandle();
			}
		}
	}
	//tempFunction
})(jQuery);


