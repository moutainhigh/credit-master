(function($){
	"use strict";
	
	/**
	 * jQuery ready后执行初始化页面
	 */
	$(function(){
		//绑定打印事件
		$("#printDetail").bind("click",function(event){
			$("#loanFilesInfoDetail").get(0).contentWindow.print();
		});
		//设置查看档案宽度和高度
		window.setIframeRectangle=function(width,height){
			$("#loanFilesInfoDetail").parent().height(height+30);
			$("#loanFilesInfoDetail").height(height+10);
		};
	});
})(jQuery);
