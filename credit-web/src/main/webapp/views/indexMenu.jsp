<!-- 首页 左侧菜单子页面 -->
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.zdmoney.credit.common.tree.EasyUITree" %>
<%@ page import="com.zdmoney.credit.common.login.vo.UserCoreDetails" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>  
<style type="text/css">
	.tree-node {
		height: 20px;
		white-space: nowrap;
		cursor: pointer;
		padding-top: 1px;
	}
	.tree-icon {
		margin-top : 2px;
		margin-right : 1px;
	}
</style>
<%
	UserCoreDetails userCoreDetails = (UserCoreDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	pageContext.setAttribute("userCoreDetails",userCoreDetails);
%>
<div class="easyui-accordion" data-options="fit:true,animate:true">
	<c:forEach items="${userCoreDetails.user.menuResource }" var="permission" varStatus="status">
		<div title="${permission.text}" data-options="iconCls:'${permission.iconCls}'" style="padding:3px;">
			<ul class="easyui-tree" id="menuTree${status.index }" data-options="lines:true">
				<%
					EasyUITree tree = (EasyUITree)pageContext.getAttribute("permission") ;
				%>
				<script type="text/javascript">
					initMethod.push(
						(function($){
							var treea = $('#menuTree${status.index}');
							if (treea.length) {
								var treeData = <%=JSONObject.toJSONString(tree.getChildren())%>;
								if (treeData.length) {
									treea.tree('loadData',treeData);
								}
							}
						})
					);
				</script>
			</ul>
		</div>
	</c:forEach> 
</div>