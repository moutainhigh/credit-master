/**
 * 扩展EasyUI Combobox组件
 */
$.extend($.fn.combobox.defaults, {

});

$.extend($.fn.combobox.methods, {
	/** 验证输入的内容在数据集合中是否存在（不存在则清空内容） * */
	checkValue : function(jq) {
		/** 模拟一个键盘事件 * */
		var event = jQuery.Event("keydown");
		/** keyCode=13是回车 * */
		event.keyCode = 13;
		/** 找到对应的文本框控件 * */
		var textBox = $(jq).next().find('input.textbox-text');
		if (textBox.length > 0) {
			/** 触发回车事件 * */
			textBox.trigger(event);
		} else {
			/** 未找到文本框控件 * */
			$.myConsole.writeWarnLog('$.fn.combobox.methods.checkValue not found textbox!');
		}
	},
	/** 下拉框默认选中第一项 * */
	defaultOneItem : function(jq) {
		var valueField = jq.combobox('options').valueField;
		var textField = jq.combobox('options').textField;
		var data = jq.combobox('getData');
		/** 下拉框默认值 * */
		var defaultValue = eval('data[0].' + valueField);
		jq.combobox('select',defaultValue);
	},
	/**
	 * 下拉框延迟加载
	 */
	delayLoad : function(jq, param) {
		$(jq).siblings("span").click(function() {
			/** 请求地址 * */
			var url = param.url;
			/** 请求参数 * */
			var postData = param.data;

			if ($.isEmpty(url)) {
				$.messager.alert('警告', '缺少url参数!', 'warning');
				return;
			}
			var options = jq.combobox('options');

			if (options.isLoaded) {

			} else {
				options.isLoaded = true;
				var defaultItemArr = [];
				var defaultItem = {};
				var img = '<img width="13px" height="13px" src="' + global.smallLoading + '"/>&nbsp;&nbsp;';
				eval('defaultItem.' + options.textField + '=\'' + img + '加载中\';');
				defaultItem.disabled = true;
				defaultItemArr.push(defaultItem);
				jq.combobox("loadData", defaultItemArr);

				$.ajaxPackage({
					type : 'post',
					url : url,
					dataType : "json",
					isShowLoadMask : false,
					data : postData,
					success : function(data, textStatus, jqXHR) {
						var resCode = data.resCode;
						var resMsg = data.resMsg;
						if (resCode == '000000') {
							var attachment = data.attachment;
							// $(jq).combobox('options').panelHeight = 200;
							jq.combobox("loadData", attachment);
							// $(jq).combobox('resize');
						} else {
							options.isLoaded = false;
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown, d) {
						options.isLoaded = false;
					},
					complete : function() {

					}
				});
			}
		});
	},
	/** 扩展填充数据方法 **/
	loadDataExt : function(jq, param) {
		$.fn.combobox.methods.loadData(jq, param);
		var textField = $(jq).combobox('options').textField;
		var options = $(jq).combobox('options');
		options.dataCache = options.dataCache || [];
		if ($.isArray(param)) {
			$.each(param, function(i, v) {
				var key = 'v.' + textField;
				options.dataCache.push(eval(key));
			});
		}
	}
});