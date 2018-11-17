<%@page import="com.zdmoney.credit.common.login.UserContext"%>
<%@page import="com.alibaba.dubbo.common.utils.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.zdmoney.credit.system.service.pub.ISysParamDefineService" %>
<%@ page import="com.zdmoney.credit.common.util.SpringContextUtil" %>
<%@ page import="com.zdmoney.credit.system.domain.BaseMessage" %>
<%@ page import="com.zdmoney.credit.common.login.vo.User" %>
<%@ page import="com.zdmoney.credit.system.service.pub.IBaseMessageService" %>
<%@ page import="com.zdmoney.credit.system.service.BaseMessageServiceImpl" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>

<%
	ISysParamDefineService sysParamDefineServiceImpl = (ISysParamDefineService)SpringContextUtil.getBean("sysParamDefineServiceImpl");
	/** 征审系统地址（外包） **/
	String zhengShenSystemUrl = sysParamDefineServiceImpl.getSysParamValueCache("system.thirdparty", "system.zhengshen.url");
	/** 报表系统地址 （外包） **/
	String baoBiaoSystemUrl = sysParamDefineServiceImpl.getSysParamValueCache("system.thirdparty", "system.baobiao.url");
	/** 子系统切换 **/
	String subSysUrl = sysParamDefineServiceImpl.getSysParamValueCache("system.thirdparty", "system.subsys.url");
	//新录单系统地址
	String cfsSystemUrlNew = sysParamDefineServiceImpl.getSysParamValueCache("system.thirdparty", "system.cfs.url");
	//新贷前报表系统地址
	String rmsSystemUrlNew = sysParamDefineServiceImpl.getSysParamValueCache("system.thirdparty", "system.rms.url");
	//试点营业部编码（多个逗号分割）
	String pilotOrganSystemUrlNew = sysParamDefineServiceImpl.getSysParamValue("organization", "pilot.organization");
	
	/** 读取信贷报表系统地址 **/
	String creditReportUrl = sysParamDefineServiceImpl.getSysParamValueCache("system.thirdparty", "system.credit.report.url");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<title>信贷系统</title>
	<%@ include file="/views/common/headIncludeFile.jsp"%>
	<link type="text/css"  rel="stylesheet"  href="${path}/resources/css/sysIcon.css"/>
	<link href="${path}/resources/css/thirdparty/style.css" rel="stylesheet" type="text/css" />
	<%
		BaseMessageServiceImpl baseMessageServiceImpl = (BaseMessageServiceImpl)SpringContextUtil.getBean("baseMessageServiceImpl");
		/** 查看未读消息数量 **/
		int messageCount = 0;
		String userOrgCode = null;
		User user = UserContext.getUser();
		if (user != null) {
			BaseMessage baseMessage = new BaseMessage();
			baseMessage.setReceiver(user.getId());
			messageCount = baseMessageServiceImpl.countNoView(baseMessage);
			userOrgCode = user.getOrgCode();
		}
		pageContext.setAttribute("messageCount", messageCount);
		boolean flag = false;
		if(StringUtils.isNotEmpty(pilotOrganSystemUrlNew)){
			String[] pilotOrgCodes = pilotOrganSystemUrlNew.split(",");
			for(String pilotOrgCode : pilotOrgCodes){
				if(StringUtils.isEmpty(pilotOrgCode)){
					continue;
				}
				if(StringUtils.isNotEmpty(userOrgCode) && userOrgCode.indexOf(pilotOrgCode) == 0){
					flag = true;
					continue;
				}
			}
		}
	%>
	<script type="text/javascript">
		var creditReportUrl = '<%=creditReportUrl%>';
		/** 初始化方法集合 **/
		var initMethod = [];
		importPluginsExt(['linkbutton','progressbar','tooltip'
		        ,'panel','window','dialog','messager','layout','form','menu','tabs','splitbutton','accordion'
		        ,'textbox','combo','combobox','validatebox','tooltip'],'business', function() {
			
			$(function() {
				var urlJs = [];
				urlJs.push(global.contextPath + '/resources/js/common/tabs.js');
				urlJs.push(global.contextPath + '/resources/js/index.js');
				importJSExt(urlJs,function(){
					/** 脚本加载成功回调方法 **/
					if (initMethod.length) {
						$.each(initMethod, function(index,element){
							if ($.isFunction(element)) {
								element($)
							}
						});
					}
				});
			});
		});
	</script>

</head>

<body>
	<jsp:include page="/views/common/initPageMast.jsp" />
	<div class="easyui-layout" style="" data-options="fit:true">
	    <!-- 头部信息 -->
		<div data-options="region:'north'" style="background:url(${path}/resources/images/topbg.gif) repeat-x;">
			<div class="topleft">
      			<a href="#" target="_parent"><img src="${path}/resources/images/logo.png" title="系统首页" /></a>  
    		</div>
            
            <ul class="nav">
				<li><a href="<%=subSysUrl %>"   target="_self"><img src="${path}/resources/images/i02.png" height="45" width="45" title="子系统切换" /><h2>子系统切换</h2></a></li>
    			<%--<li><a href="<%=flag?cfsSystemUrlNew:zhengShenSystemUrl %>"   target="_self"><img src="${path}/resources/images/i02.png" height="45" width="45" title="征审系统" /><h2>征审系统</h2></a></li>--%>
    			<%--<li><a href="<%=flag?rmsSystemUrlNew:baoBiaoSystemUrl %>" target="_self"><img src="${path}/resources/images/i04.png" height="45" width="45" title="报表系统" /><h2>报表系统</h2></a></li>--%>
    		</ul>
            
    		<div class="topright">    
    			<ul>
    				<sec:authorize ifAnyGranted="/system/user/modifyPassword">
   						<li><a href="#" id="updatePassWord">修改密码</a> </li>
    				</sec:authorize>
   				 	<li><a href="${path}/j_spring_cas_security_logout" id="" >登出</a> </li>
   		 		</ul>

   		 		<div class="user">
    				<span><sec:authentication property="principal.user.name"/></span>
    				<!-- 消息中心 -->	
    				<sec:authorize ifAnyGranted="/system/baseMessage/message">
    				<i id="showMessage" onmouseover="this.style.cursor='pointer'" onmouseout="this.style.cursor='default'">消息中心</i>
						<c:choose> 
		            		<c:when test="${messageCount gt 0}">
		          				<b id="showMessageCount" onmouseover="this.style.cursor='pointer'" onmouseout="this.style.cursor='default'">${messageCount}</b>
		            		</c:when> 
		            		<c:when test="${messageCount eq '0'}">    		
		          				<b id="showMessageCount" onmouseover="this.style.cursor='pointer'" onmouseout="this.style.cursor='default'">0</b>
		            		</c:when>             
						</c:choose> 		
		   			</sec:authorize>
    			</div>       
    		</div>  
		</div>
		<!-- 头部信息 结束 -->	
			
			<div data-options="region:'south',split:true" style="height:2px;">
				<!-- 尾部 -->
				<!-- 
				<div data-options="fit:true" style="text-align:center;padding-top:15px;font-size:14px;">© 2003-2015 ITeye.com. All rights reserved. [ 京ICP证110151号 京公网安备110105010620 ] </div>
			     -->
			</div>
		
			<div data-options="region:'west',split:true" title="我的菜单" style="width:200px;">
				<!-- 左侧部分（菜单） -->
				<jsp:include page="/views/indexMenu.jsp" />
			</div>
			<div data-options="region:'center'" title="">
				<!--  中间部分（各模块主区域） -->
				<div id="tabsPanel" class="easyui-tabs" data-options="tools:'#tab-tools',fit:true,pill:false" style="width:700px;height:250px"></div>
			</div>
		</div>
		
		
		<div class="easyui-window" title="" data-options="closed:true,title:'修改密码',collapsible : false,minimizable : false,maximizable : false" style="width: 450px; padding: 30px 70px" id="updatePassWdPanel">
			<form method="post"  id="updatePassWdForm">
			<table style="height:140px; border-spacing:10px;table-layout:fixed;" >
                 <tbody>
                            <tr class="prop" >
                                <td></td> <td></td>
                                <td valign="top" class="name" style="width: 70px">
                                    <label for="usercode">登录账号：</label>
                                </td>
                                <td valign="top" class="value">
                                    <input type="text" name="userCode" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.user.userCode}" readonly="readonly" id="userCode" style="width: 150px" />
                                </td>
                                <td></td> <td></td>
                            </tr>

                            <tr class="prop">
                                <td></td> <td></td>
                                <td valign="top" >
                                    <label for="passowrd" class="required">登录密码：</label>
                                </td>
                                <td valign="top" >
                                    <input type="password" name="passWord" value="" id="passWord" class="easyui-textbox"  data-options="required:true" missingMessage="该输入项为必输项" style="width: 150px"/>
                                </td>
                                <td></td> <td></td>
                            </tr>

                            <tr class="prop">
                                <td></td> <td></td>
                                <td valign="top" >
                                    <label for="newPassword" class="required">新的密码：</label>
                                </td>
                                <td valign="top" class="value">
                                    <input type="password" name="newPassWord" value="" id="newPassWord"  class="easyui-textbox" data-options="required:true" missingMessage="该输入项为必输项" style="width: 150px"/>
                                </td>
                                <td></td> <td></td>
                            </tr>

                            <tr class="prop">
                                <td></td> <td></td>
                                <td valign="top" >
                                    <label for="newPasswordAgain" class="required">确认密码：</label>
                                </td>
                                <td valign="top" >
                                    <input type="password" name="newPassWordAgain" value="" id="newPassWordAgain" class="easyui-textbox" data-options="required:true" missingMessage="该输入项为必输项" style="width: 150px"/>
                                </td>
                                <td></td> <td></td>
                            </tr>
                        </tbody>
                    </table>
                    <div style="text-align:center;padding-top:10px;">
				<a href="javascript:void(0)" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" id="submitBut">修改密码</a>
		</div>
		</form>
	
	</div>

	</body>
</html>