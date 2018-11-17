<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>查看-客户详细资料信息</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			importPluginsExt(['tabs','window','panel','layout'],'business', function() {
				$(function(){
					importJSExt([],function(){
						/** 脚本加载成功回调方法 **/
						$.personLoan = {
								tabsObj : $('#tabsaaa'),
								/** 借款基本信息地址 **/
								loanDetailUrl : global.contextPath + '/loanInfo/loadLoanInfoData' + '/<%=request.getAttribute("id")%>',
								/** 借款还款汇总信息地址 **/
								jumpRepayPageUrl : global.contextPath + '/loanInfo/jumpRepayPage' + '/<%=request.getAttribute("id")%>',
								/** 借款还款详细信息地址 **/
								jumpRepaymentDetailPageUrl : global.contextPath + '/loanInfo/jumpRepaymentDetailPage' + '/<%=request.getAttribute("id")%>',
								/** 账卡信息地址 **/
								jumpFlowDetailPageUrl : global.contextPath + '/loanInfo/jumpFlowDetailPage' + '/<%=request.getAttribute("id")%>'
								
								/** 加载借款基本信息 **/
								/**refreshPersonLoanInfo : function() {
									$('#loanInfoDiv').panel('refresh',$.personLoan.loanDetailUrl);
								},**/
								
								/** 加载还款汇总信息 **/
								/**refreshPersonLoanRepay : function() {
									$('#loanRepayDiv').panel('refresh',$.personLoan.jumpRepayPageUrl);
								},**/
								
								/** 加载还款详细信息 **/
								/**refreshRepaymentDetailInfo : function() {
									$('#repaymentDetailInfoDiv').panel('refresh',$.personLoan.jumpRepaymentDetailPageUrl);
								},**/
								
								/** 加载账卡信息 **/
								/**refreshFlowDetailInfo : function() {
									$('#flowDetailInfoDiv').panel('refresh',$.personLoan.jumpFlowDetailPageUrl);
								}**/
								
						}
						
						$('#loanInfo').panel('options').href = $.personLoan.loanDetailUrl;
						$('#loanRepay').panel('options').href = $.personLoan.jumpRepayPageUrl;
						$('#repaymentDetailInfo').panel('options').href = $.personLoan.jumpRepaymentDetailPageUrl;
						$('#flowDetailInfo').panel('options').href = $.personLoan.jumpFlowDetailPageUrl;
						
						/** 默认选中第一个选项卡 **/
						$.personLoan.tabsObj.tabs('unselect',0);
						$.personLoan.tabsObj.tabs('select',0);
						
						$('#loanInfoRefBut').click(function(){
							//$.personLoan.refreshPersonLoanInfo()
						})
						$('#loanRepayRefBut').click(function(){
							//$.personLoan.refreshPersonLoanRepay()
						})
						$('#repaymentDetailInfoRefBut').click(function(){
							//$.personLoan.refreshRepaymentDetailInfo();
						})
						$('#flowDetailInfoRefBut').click(function(){
							//$.personLoan.refreshFlowDetailInfo();
						})
						
						/** 上手默认加载第一次面板内容 **/
						//$.personLoan.refreshPersonLoanInfo();
						
						/** 脚本加载成功回调方法 **/
						
					})
				})
			});
			
		</script>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="margin:0px;">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<div class="easyui-tabs" id="tabsaaa" data-options="fit:true">
			<div title="借款信息" id="loanInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px"></div>
			<div title="还款汇总信息"  id="loanRepay" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px"></div>
			<div title="还款详细信息" id="repaymentDetailInfo" data-options="iconCls:'icon-large-smartart',tools:''"  style="padding:0px"></div>
			<div title="账卡信息" id="flowDetailInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px"></div>
		</div>
		<div id=""></div>
		<div id="loanInfoTools">
			<a href="javascript:void(0)" class="icon-mini-refresh" id="loanInfoRefBut" ></a>
		</div>
		<div id="loanRepayTools">
			<a href="javascript:void(0)" class="icon-mini-refresh" id="loanRepayRefBut" ></a>
		</div>
		<div id="repaymentDetailInfoTools">
			<a href="javascript:void(0)" class="icon-mini-refresh" id="repaymentDetailInfoRefBut" ></a>
		</div>
		<div id="flowDetailInfoTools">
			<a href="javascript:void(0)" class="icon-mini-refresh" id="flowDetailInfoRefBut" ></a>
		</div>
	</body>
</html>