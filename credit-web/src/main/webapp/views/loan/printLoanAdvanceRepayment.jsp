<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>结清证明</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt([ 'panel', 'combobox', 'window', 'layout', 'datagrid','pagination', 'form', 'tooltip', 'validatebox', 'combogrid' ],'business', function() {
				$(function() {
					window.setIframeBodyHeight = function(parentHeight){
						if(parentHeight > $("body").height()){
							$("body").height(parentHeight);
						}
						window.parent.setIframeRectangle($("body").width(),$("body").height());
					};
					$(window).on("load",function(event){
						var urlJs = [];
						importJSExt(urlJs,function(){
							/** 脚本加载成功回调方法 **/
							
						});
					});
				})
			});
		</script>
	</head>
	<body class="easyui-layout">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<div style="width:100%;height: 100%;margin: auto;padding: 0;">
			<p align="center" style="text-align:center">
				<span style="font-size:16.0pt;font-family:宋体;font-weight: bold;">结清确认书</span>
			</p>
			<p align="center" style="text-align:center">
				<span style="font-size:16.0pt;font-family:宋体;font-weight: bold;">&nbsp;</span>
			</p>
			<p align="left" style="text-align:left;line-height:150%">
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">尊敬的</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${borrowerName}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">女士/先生，您好：</span>
			</p>
			<p align="left" style="text-align:left;line-height:150%;text-indent:2em; ">
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">本司于</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${year}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">年</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${month}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">月</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${dayOfMonth}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">日收到您一次性偿还的共计人民币</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${dxamount}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">（RMB</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${amount}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">）的款项，该款项用于您一次性归还您与戴卫新（身份证号：</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;320684198310057194&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">）于</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${ryear}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">年</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${rmonth}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">月</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${rdayOfMonth}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">日在</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${signingSite}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">签署的合同编号为</span>
				<span style="font-size:12.0pt;line-height:150%;border-bottom: 1px solid  black;">&nbsp;${contractNum}&nbsp;</span>
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">的《借款协议》所确定的相关款项。</span>
			</p>
			<p align="left" style="text-align:left;line-height:150%;text-indent:2em; ">
			特此证明，您已将该借款协议中的全部本金、利息、逾期罚息及其他相关费用全部偿还完毕。
			</p>
			<p align="center" style="text-align:center">
				<span style="font-size:16.0pt;font-family:宋体;font-weight: bold;">&nbsp;</span>
			</p>
			<p align="center" style="text-align:center">
				<span style="font-size:16.0pt;font-family:宋体;font-weight: bold;">&nbsp;</span>
			</p>
			<p align="center" style="text-align:center">
				<span style="font-size:16.0pt;font-family:宋体;font-weight: bold;">&nbsp;</span>
			</p>
			<p align="right" style="text-align:right;">
				<span style="font-size:12.0pt;font-family:宋体;">上海证大投资咨询有限公司</span>
			</p>
			<p align="right" style="text-align:right;">
				<span style="font-size:12.0pt;line-height:150%;font-family:宋体;">${zyear} 年 ${zmonth} 月 ${zdayOfMonth} 日</span>
			</p>
		</div>
	</body>
</html>
