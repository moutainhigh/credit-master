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
				$(function() {
					importJSExt([],function(){
						
						/** 脚本加载成功回调方法 **/
						$.personDetail = {
							tabsObj : $('#tabs'),
							/** 客户详细信息地址 **/
							personDetailUrl : global.contextPath + '/person/loadPersonData' + '/<%=request.getAttribute("id")%>?isFrame=true',
							
							/** 客户联系人信息地址 **/
							personContactDetailUrl : global.contextPath + '/personContact/viewContactInfo' + '/<%=request.getAttribute("id")%>',
							
							/** 客户借款信息地址 **/
							personLoanDetailUrl : global.contextPath + '/person/loadPersonLoanData' + '/<%=request.getAttribute("id")%>',
							
							/** 借款详细信息页面地址 **/
							personLoanAllDetailUrl : global.contextPath + '/loanInfo/viewPersonLoanDetailPage',
							
							/** 加载客户详细信息 **/
							/**refreshPersonInfo : function() {
								$('#personInfo').panel('refresh',$.personDetail.personDetailUrl);
							},**/
							
							/** 加载客户联系人信息 **/
							/**refreshPersonContactInfo : function() {
								$('#personContactInfo').panel('refresh',$.personDetail.personContactDetailUrl);
							},**/
							
							/** 加载借款信息 **/
							/**refreshPersonLoanInfo : function() {
								$('#personLoanInfo').panel('refresh',$.personDetail.personLoanDetailUrl);
							},**/
							
							/** 显示某笔借款详细信息 **/
							showLoanDetailInfo : function(id,title) {
								var tab = {};
								tab.id = 'loanDetail_' + id;
								tab.text = title + ' - 借款详细信息';
								tab.iconCls = 'pic_45';
								tab.url = $.personDetail.personLoanAllDetailUrl + '/' + id;
								/** 调用父级添加选项卡方法 **/
								parent.$.iframeTabs.add(tab);
							}
						}
						$('#personInfo').panel('options').href = $.personDetail.personDetailUrl;
						$('#personContactInfo').panel('options').href = $.personDetail.personContactDetailUrl;
						$('#personLoanInfo').panel('options').href = $.personDetail.personLoanDetailUrl;
						
						/** 默认选中第一个选项卡 **/
						$.personDetail.tabsObj.tabs('unselect',0);
						$.personDetail.tabsObj.tabs('select',0);
						
						$('#personInfoRefBut').click(function(){
							//$.personDetail.refreshPersonInfo();
						})
						
						$('#personContactInfoRefBut').click(function(){
							//$.personDetail.refreshPersonContactInfo();
						})
						
						$('#personLoanInfoRefBut').click(function(){
							//$.personDetail.refreshPersonLoanInfo();
						})
					});
				});
			});
	
		</script>
	</head>
	<body data-options="" style="margin:0px;">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<div class="easyui-tabs" style="" id="tabs" data-options="fit:true">
			<div title="基本信息" id="personInfo" data-options="href:'',iconCls:'icon-large-smartart'" style="padding:0px"></div>
			<div title="相关联系人"  id="personContactInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px"></div>
			<div title="借款信息" id="personLoanInfo" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px"></div>
		</div>
		<div id="personInfoTools">
			<a href="javascript:void(0)" class="icon-mini-refresh" id="personInfoRefBut" ></a>
		</div>
		<div id="personContactInfoTools">
			<a href="javascript:void(0)" class="icon-mini-refresh" id="personContactInfoRefBut" ></a>
		</div>
		<div id="personLoanInfoTools">
			<a href="javascript:void(0)" class="icon-mini-refresh" id="personLoanInfoRefBut" ></a>
		</div>
	</body>
</html>