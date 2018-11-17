<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
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
		<title>算话报文管理</title>
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
						urlJs.push(global.contextPath + '/resources/js/data/dataCpcs.js');
				
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				})
			});
			//重置
			$(function(){
				 $("#clearBtn").bind("click", function(envent) {
					$("#conditionForm").form("reset");
					$("#title1").css('display','none');
		       	    $("#title2").css('display','block');
				 }); 
			});
			
			
		</script>			
	</head>
	<%
	   Calendar c = Calendar.getInstance();
       c.add(Calendar.MONTH, -1);
       String defaultMonth = new SimpleDateFormat("yyyy-MM").format(c.getTime());
	   request.setAttribute("defaultMonth", defaultMonth);
	%>
	<body class="easyui-layout">
		<jsp:include page="/views/common/initPageMast.jsp" />		
		<div style="height: 150px; width: 100%;">
			<div class="easyui-panel" title="算话数据上传"  style="width:100%;height:111px;" >   
				<form id="conditionForm" style="width: 100%; height: 100%;">
				  <table>
						<tr>
				 		<td>上传类型：</td>
                    	<td>
	                        <select class="easyui-combobox" id="state" name="state" style="width:150px;">
	                            <!-- <option value="">请选择</option> -->
	                            <option value="1">数据共享</option>
	                            <option value="2">反欺诈评分</option>
	                        </select>
                    	</td> 
                    	</tr>
                	</table>
                    <table class="" id="title1" style="height: 100%;border-spacing:5px;table-layout:fixed;display:none;float:left;">
                    	<tr>
                    <td>统计日期：</td>
                      	<td style="width: 160px;text-align: left;">
							<input class="easyui-datebox" type="text" name="countDateStart"  id="countDateStart"  value="${countDateStart}" style="width: 150px;"></input>
						</td>
				    <td>至：</td>
						 <td style="width: 160px;text-align: left;">
							<input class="easyui-datebox" type="text" name="countDateEnd"  id="countDateEnd"  value="${countDateEnd}" style="width: 150px;"></input>
						</td>
						</tr>	
					</table>
					<table class="" id="title2" style="height: 100%;border-spacing:5px;table-layout:fixed;display:block;">
						<tr >
							<td>统计月份：</td>
	                        <td style="width: 160px;text-align: left;">
								<input class="easyui-datebox" type="text"  name="monthDate"  id="monthDate" value = "${defaultMonth}" style="width: 150px;" ></input>
							</td>
						</tr>	
					</table>
				</form>
			</div>
			<div style="padding: 3px;" class="datagrid-toolbar">
				<div id="bottonBox" style="margin-bottom: 0px">
	                <a href="#" class="easyui-linkbutton" id="importBtn" iconCls="pic_29" style="" plain="true">数据上传</a>
	                &nbsp;&nbsp;&nbsp;&nbsp;
	                <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;float: none;">重置</a>
				</div>
			</div>
		</div>
	</body>
</html>