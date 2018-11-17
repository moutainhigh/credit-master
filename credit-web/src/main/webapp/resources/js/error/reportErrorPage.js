(function($){
	"use strict";
	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){
		var errorMessage = $("[resMsg]").attr("resMsg");
		$.messager.alert("提示", errorMessage , "error")
	});
})(jQuery);
