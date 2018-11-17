<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String index = request.getParameter("index");
%>
	<form method="post" class="">
		<input type="hidden" name="id">
		<input type="hidden" name="className">
		<input type="hidden" name="objectId" >
		<table style="padding:5px;margin-top:0px;" border="0" class="editContentPanel">
			<tr>
				<td style="width:50px;">类型：</td>
				<td style="width:110px;">
					<select class="easyui-combobox" name="telType" style="width:100px;" data-options="editable:false,panelHeight:'auto',required:true" missingMessage="该输入项为必选项">
						<c:forEach var="x"  items="${telTypeEnum }">
							<option value="${x.name }">${x.name }</option>
						</c:forEach>
					</select>
				</td>
				<td style="width:50px;">号码：</td>
				<td style="width:140px;">
					<input class="easyui-textbox" type="text" name="content" style="width:130px;" data-options="required:true"  missingMessage="该输入项为必输项"></input>
				</td>
				<td style="width:50px;">优先级：</td>
				<td style="width:60px;">
					<select class="easyui-combobox" name="priority" style="width:50px;" data-options="editable:false,panelHeight:'auto',required:true" missingMessage="该输入项为必选项">
						<c:forEach var="x"  items="${priorityEnum }">
							<option value="${x.name }">${x.name }</option>
						</c:forEach>
					</select>
				</td>
				<td style="width:50px;">有效性：</td>
				<td>
					<input type="checkbox" checked="checked" value="t" name="valid" />
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td colspan="7">
					<input class="easyui-textbox" type="text" name="memo" style="width:400px;" data-options="" validType="maxLength[50,' ']" ></input>
				</td>
			</tr>
		</table>
		<div style="padding:5px 0;text-align:right;padding-right:30px">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="$.telAddress.saveItem.call($.telAddress.telDataGrid,<%=index%>)">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="$.telAddress.cancelItem.call($.telAddress.telDataGrid,<%=index%>)">关闭</a>
		</div>
	</form>