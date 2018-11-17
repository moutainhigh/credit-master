/**
 * 扩展EasyUI SwitchButton组件
 */
$.extend($.fn.switchbutton.methods, {
	/** 判断SwitchButton是否选中（开启）**/
	isCheckedExt : function(jq) {
		/** input 控件Name值 * */
		var name = $(jq).attr('switchbuttonname');
		var className = '.switchbutton-value';
		if ($.isEmpty(name))  {
			$.messager.alert('提示','获取Name参数失败','warning');
			return;
		}
		var searchCon = $('input[type=checkbox][name=' + name + ']' + className);
		if (searchCon.length == 0) {
			$.messager.alert('提示','未找到CheckBox控件（' + searchCon.selector + '）','warning');
			return;
		} else if (searchCon.length == 1) {
			return searchCon.is(':checked');
		} else {
			$.messager.alert('提示','存在多个CheckBox控件（' + searchCon.selector + '）','warning');
			return;
		}
	}
});