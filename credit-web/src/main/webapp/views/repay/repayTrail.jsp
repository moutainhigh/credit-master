<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>还款试算</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>	
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/repay/repayTrail.js');
					
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				});
			});
		</script>	
	</head>
	<body class="easyui-layout">
	<jsp:include page="/views/common/initPageMast.jsp" />
		<div data-options="region:'north',split:false,border:true,collapsible:false" style="height: 146px;">
		<div class="easyui-panel" title="查询条件"  style="width:100%;height:111px;" >   
			<form id="conditionForm" style="width: 100%; height: 100%;">
				<table style="height: 100%;border-spacing:5px;table-layout:fixed;">
					<tr >
						<td style="width: 72px;text-align: left;">姓名:</td>
						<td style="width: 160px;text-align: left;"><input class="easyui-textbox" type="text" name="name"  id="name"  name="name" style="width: 150px;"></input></td>
						<td style="width: 72px;text-align: left;">手机:</td>
						<td style="width: 160px;text-align: left;"><input class="easyui-textbox" type="text" value="" name="mphone" id="mphone" validType="mobile" style="width: 150px;"></input></td>
						<td style="width: 72px;text-align: left;">身份证号:</td>
						<td style="width: 150px;text-align: left;"><input class="easyui-textbox" type="text" value="" name="idnum" id="idnum"  validType="idCard" style="width: 150px;"></input></td>
					</tr>	
						<tr>
							<td style="text-align: left;">
								还款方式:
							</td>
							<td style="text-align: left;" >
								<select  class="easyui-combobox" id='repayType' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
									<c:forEach items="${repayTypeArr}" var="repayType"> 
										<option value="${repayType}" >${repayType}</option>  
									</c:forEach>  
								
								</select>
							</td>
							<td style="text-align: left;" >
								还款日期:
							</td>
							<td  style="text-align: left;">
								<input class="easyui-validatebox easyui-datebox" type="text" id="grantMoneyDateStart" data-options="editable : false"  name="grantMoneyDateStart" value="${repayDate}" style="width: 150px;"/>
							</td>
							<td style="text-align: left;" >
								合同编号:
							</td>
							<td style="text-align: left;"><input class="easyui-textbox" type="text" name="contractNum"  id="contractNum"  name="contractNum" style="width: 150px;"></input></td>
						</tr>
				</table>
			</form>
		</div>
		<div style="padding: 3px;" class="datagrid-toolbar">
			<div id="bottonBox" style="margin-bottom: 0px">
				<a href="javascript:void(0)" class="easyui-linkbutton" id="searchBut"  iconCls="icon-search" plain="true" style="width: 60px;">查询</a>
				<a href="javascript:void(0)" id="clearCondition" class="easyui-linkbutton" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>
			</div>
		</div>
	</div>
	

		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<!-- 表格标签 -->
			<iframe id="listRepayTrailDefault" name="listRepayTrailDefault" style="width:100%;height:100%;" frameborder="0"></iframe>			
		</div>
	</body>
</html>