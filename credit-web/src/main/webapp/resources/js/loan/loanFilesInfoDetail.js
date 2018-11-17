(function($){
	"use strict";
	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){
		window.onload = function(event){
			window.parent.setIframeRectangle($("body").width(),$("body").height());
		}
	});
})(jQuery);
