<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>撤销管理</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
			                  ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/chexiao/undoManage.js');
					
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				});
			});
		</script>
	</head>
	<body class="easyui-layout">
		<jsp:include page="/views/common/initPageMast.jsp" />
		   <!-- DataGrid 工具栏按钮 -->
    <div id="tb" style="padding:3px;">
        <div style="margin-bottom:0px">
            <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
            <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" id="updateBtn" iconCls="icon-save" plain="true">更改还款状态</a>
        </div>
    </div>
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:75px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm">
				<table cellpadding="5">
					<tr>
						<td>合同编号：</td>
						<td><input class="easyui-textbox" type="text" name="contract_num" id="contract_num"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px; width: 100%;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="dataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		
		
		  <!-- 更改还款状态面板 -->
    <div id="updateStatePanel" class="easyui-window" title="还款状态变更"  style="padding:20px;">
        <form method="post"  id="dataForm">
            <table cellpadding="3">
                <tr>
                    <td>该客户为预结清客户,请选择更新后的还款状态:</td> 
               </tr>
                <tr>    
                 <td style="text-align: center;">
                    <select id="updateLoanState" name="updateLoanState"  class="easyui-combobox" data-options="editable: false, panelHeight:'auto'"  style="width:100px;">
		 				<option value="正常">正常</option>
		 				<option value="逾期">逾期</option>
                    </select>
                     </td>
                </tr>
            </table>
        </form>
        <div style="text-align:center;padding-top:10px;">
                 <a href="javascript:void(0)" id="submitBtn" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
           		 <a href="javascript:void(0)" id="closeBtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
        </div>
    </div>
		
	</body>
</html>