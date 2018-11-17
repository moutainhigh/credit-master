<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<title>结清客户打印页面</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					//绑定打印事件
					$("#printDetail").on("click",function(event){
						$("#zbLoanAdvanceRepayment").get(0).contentWindow.print();
					});
					//设置查看档案宽度和高度
					window.setIframeRectangle=function(width,height){
						$("#zbLoanAdvanceRepayment").parent().height(height+30);
						$("#zbLoanAdvanceRepayment").height(height);
					};
					
					$("#zbLoanAdvanceRepayment").on("load",function(event){
						$("#zbLoanAdvanceRepayment").get(0).contentWindow.setIframeBodyHeight($("#zbLoanAdvanceRepayment").parent().height()-40);
						var urlJs = [];
						importJSExt(urlJs,function(){
							/** 脚本加载成功回调方法 **/
							
						});
					});
					
			
				})
			});
		</script>		
	</head>
	<body class="easyui-layout" >
	<jsp:include page="/views/common/initPageMast.jsp" />	
		<div data-options="border:false,region:'center'">
			<div style="width:100%;height:100%;margin-left: auto;margin-right: auto;">
				<iframe id="zbLoanAdvanceRepayment" src="<%=request.getContextPath()%>/system/loan/print/${loanId}"  style="width:100%;height:990px; border-width:0 "></iframe>
				<div style="width: 100%px; height: 30px; text-align: center; margin-top: 10px">
					<a id="printDetail" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print'" style="width:80px">打印</a>
				</div>
			</div>
		</div>
	</body>
</html>