(function($){
	$.extend({
		isString : function(str) {
			return typeof(str) == 'string';
		},
		startWith : function(str,str1) {
			var reg=new RegExp("^"+str1);
		    return reg.test(str);
		},
		endWith : function(str,str1) {
			var reg=new RegExp(str1+"$");
		    return reg.test(str);
		},
		/** 向左补字符串 **/
		lPad : function(value,len,splitStr){
			value = value + '';
			splitStr = splitStr + '';
			len = len - value.length + 1;
			if (len <= 0) {
				
			} else {
				value = Array(len).join(splitStr) + value;
			}
			return value;
		},
		/** 向右补字符串 **/
		rPad : function(value,len,splitStr){
			if ($.isEmpty(value)) {
				return '';
			}
			value = value + '';
			splitStr = splitStr + '';
			len = len - value.length + 1;
			if (len <= 0) {
				
			} else {
				value = value + Array(len).join(splitStr);
			}
			return value;
		},
		/**
		 * 判断变量是否为空
		 */
		isEmpty : function(value) {
			if (typeof(value) =='boolean') {
				return false;
			}
			if ($.isString(value)) {
				value = $.trim(value);
			}
			return (value == null || value == '' || typeof(value) == 'undefined');
		},
		
		/**
		 * 日期工具包
		 */
		DateUtil : {
			/** 验证日期正则表达式 **/
			ereg : /^(\d{4})(-)(\d{1,2})(-)(\d{1,2})$/,
			/** 判断是否为日期格式(YYYY-MM-DD) **/
			isDateFormat : function(value) {
				if (!$.isString(value)) {
					return false;
				}
				var r = value.match($.DateUtil.ereg);
				if (r == null) {
					return false;
				}
				var d = new Date(r[1], r[3] - 1, r[5]);
				var result = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[5]);
				return result;
			},
			/** 字符串或数字转换成日期对象 **/
			parseDate : function(value){
				if (typeof(value) == 'string') {
					var r = value.match($.DateUtil.ereg);
					if (r == null) {
						return null;
					}
					var d = new Date(r[1], r[3] - 1, r[5]);
					return d;
				} else if (typeof(value) == 'number') {
					return new Date(value);
				} else {
					return null;
				}
			},
			/** 将日期对象转换成字符串（输出格式YYYY-MM-DD） **/
			dateFormatToStr : function (value) {
				var d= $.DateUtil.parseDate(value);
				if (d) {
					var year = d.getFullYear();
					var month = d.getMonth() + 1;
					var day = d.getDate();
					year = $.lPad(year,2,'0');
					month = $.lPad(month,2,'0');
					day = $.lPad(day,2,'0');
					return year + '-' + month + '-' + day;
				} else {
					return '';
				}
			},
			/** 将日期对象转换成字符串（输出格式YYYY-MM-DD hh:mi:ss） **/
			dateFormatToFullStr : function (value) {
				var d= $.DateUtil.parseDate(value);
				if (d) {
					var year = d.getFullYear();
					var month = d.getMonth() + 1;
					var day = d.getDate();
					var hours = d.getHours();
					var minutes = d.getMinutes();
					var seconds = d.getSeconds();
					
					year = $.lPad(year,2,'0');
					month = $.lPad(month,2,'0');
					day = $.lPad(day,2,'0');
					
					hours = $.lPad(hours,2,'0');
					minutes = $.lPad(minutes,2,'0');
					seconds = $.lPad(seconds,2,'0');
					
					var str = year + '-' + month + '-' + day;
					if (!$.isEmpty(hours) && !$.isEmpty(minutes) && !$.isEmpty(seconds)) {
						str += ' ' + hours + ':' + minutes + ':' + seconds;
					}
					return str;
				} else {
					return '';
				}
			}
		},
		/**
		 * 将字母转换成大写
		 */
		toUpperCase : function (string) {
			if ($.isString(string)) {
				return string.toUpperCase();
			}
			return string;
		},
		
		/**
		 * 将字母转换成小写
		 */
		toLowerCase : function (string) {
			if ($.isString(string)) {
				return string.toLowerCase();
			}
			return string;
		},
		
		/**
		 * 在地址后面追加随机数参数
		 */
		urlAppendRandom : function(url) {
			//随机数Key值
			var randomKeyName = 'r';
			if ($.isEmpty(url) || !$.isString(url)) {
				return url;
			}
			if (url.indexOf(randomKeyName + '=') < 0) {
				if (url.indexOf('?') < 0) {
					url += '?';
				} else {
					url += '&';
				}
				url += randomKeyName + '=' + new Date().getTime();
			}
			return url;
		},
		
		/**
		 * 将字符串转换成JSON对象 如 a=b&c=d&e=f 结果{'a':'b','c':'d','e':'f'}
		 */
		serializeToJsonObject : function (data) {
			var jsonObj = {};
			var params = data.split('&');
			$.each(params,function(index,value){
				if (value) {
					var paramItem = value.split('=');
					if (paramItem.length >= 2) {
						var paramName = paramItem[0];
						var paramValue = value.replace(paramName + '=','');
						paramValue = paramValue || '';
						paramValue = paramValue.replace(/'/gm,'\\\'');
						eval('jsonObj[\'' + paramName + '\'] = \''+ paramValue + '\'');
					}
				}
			})
			return jsonObj;
		},
		/**
		 * 奖对象转换成字符串如（'a':'b','c':'d','e':'f'} 转换成a=b&c=d&e=f ）
		 * @param param
		 * @param key
		 */
		parseParam: function (param, key) {
			var paramStr = "";
			if (param instanceof String || param instanceof Number || param instanceof Boolean) {
				paramStr += "&" + key + "=" + encodeURIComponent(param);
			} else {
				$.each(param, function (i) {
					var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
					paramStr += '&' + $.parseParam(this, k);
				});
			}
			return paramStr.substr(1);
		},
		/** 
		 *数字千分位格式化 
	     * @public 
	     * @param mixed mVal 数值 
	     * @param int iAccuracy 小数位精度(默认为2) 
	     * @return string 
	     */  
		comdify : function(mVal, iAccuracy){  
			var fTmp = 0.00;//临时变量  
			var iFra = 0;//小数部分  
			var iInt = 0;//整数部分  
			var aBuf = new Array(); //输出缓存  
			var bPositive = true; //保存正负值标记(true:正数)  
			/** 
			 * 输出定长字符串，不够补0 
	         * <li>闭包函数</li> 
	         * @param int iVal 值 
	         * @param int iLen 输出的长度 
	         */  
			function funZero(iVal, iLen){  
				var sTmp = iVal.toString();  
				var sBuf = new Array();  
				for(var i=0,iLoop=iLen-sTmp.length; i<iLoop; i++)  
					sBuf.push('0');  
				sBuf.push(sTmp);  
				return sBuf.join('');  
			};  
			
			if (typeof(iAccuracy) === 'undefined')  
				iAccuracy = 2;  
			bPositive = (mVal >= 0);//取出正负号  
			fTmp = (isNaN(fTmp = parseFloat(mVal))) ? 0 : Math.abs(fTmp);//强制转换为绝对值数浮点  
			//所有内容用正数规则处理  
			iInt = parseInt(fTmp); //分离整数部分  
			iFra = parseInt((fTmp - iInt) * Math.pow(10,iAccuracy) + 0.5); //分离小数部分(四舍五入)  
			do{  
				aBuf.unshift(funZero(iInt % 1000, 3));  
			}while((iInt = parseInt(iInt/1000)));
			aBuf[0] = parseInt(aBuf[0],10).toString();//最高段区去掉前导0  
			return ((bPositive)?'':'-') + aBuf.join(',') +'.'+ ((0 === iFra)?'00':funZero(iFra, iAccuracy));  
		},
		/** 
		 * 将千分位格式的数字字符串转换为浮点数 
	     * @public 
	     * @param string sVal 数值字符串 
	     * @return float 
	     */  
		unComdify : function(sVal){
			sVal = sVal + '';
			var fTmp = parseFloat(sVal.replace(/,/g, ''));  
			return (isNaN(fTmp) ? 0 : fTmp);  
		},
		/** Map集合 合并 **/
		mergeMap : function(map1,map2) {
			for (var key in map2) {
				map1[key] = map2[key];
			}
			return map1;
		},
		/** 获取字符串长度(一个中文占2、3个长度) **/
		chineseLength : function(str) {
			var len = 0;
			for (var i=0; i<str.length; i++) {
		    	var c = str.charCodeAt(i);
		    	if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
		    		/** 单字节加1 **/
		    		len++;
		    	} else {
		    		len += $.system.oneChineseLength;
		    	}
			}
			return len;
		}
	});
})(jQuery);


