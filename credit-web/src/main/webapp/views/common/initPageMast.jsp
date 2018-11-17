<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 页面初始化Loading遮罩层 -->
<script type="text/javascript">
	document.body.style.overflowX = 'hidden';
	document.body.style.overflowY = 'hidden';
	$(function(){
		/** 控制页面遮罩层的高度及图片的位置 **/
		try {
			var imgWidth = 440;
			var imgHeight = 220;
			
			$('#initPageMastDiv').css('height',$(document).height() + 'px');
			
			//$.myConsole.writeLog($(window).height());
			
			$('#initPageMastDiv').find('img').css('left',Math.round(($(window).width() - imgWidth)/2) + 'px');
			$('#initPageMastDiv').find('img').css('top',Math.round($(document).scrollTop() + (($(window).height() - imgHeight)/2)) + 'px');
			$('#initPageMastDiv').find('img').show();
		} catch(e){
			
		}
	})
</script>
<div class="initPageMast" id="initPageMastDiv">
	<img src="<%=request.getContextPath() %>/resources/images/pageLoading.gif"  class="initPageMast_Img"/>
</div>