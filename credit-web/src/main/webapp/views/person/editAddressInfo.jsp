<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String index = request.getParameter("index");
%>
	<form method="post" class="">
		<input type="hidden" name="id">
		<input type="hidden" name="className">
		<input type="hidden" name="objectId" >
		<table style="padding:5px;margin-top:0px;" border="0" >
			<tr>
				<td style="width:50px;">类型：</td>
				<td>
					<select class="easyui-combobox" name="addressType" style="width:100px;" data-options="editable:false,panelHeight:'auto',required:true" missingMessage="该输入项为必选项">
						<c:forEach var="x"  items="${addressTypeEnum }">
							<option value="${x.name }">${x.name }</option>
						</c:forEach>
					</select>
				</td>
				<td style="width:60px;">优先级：</td>
				<td>
					<select class="easyui-combobox" name="priority" style="width:50px;" data-options="editable:false,panelHeight:'auto',required:true" missingMessage="该输入项为必选项">
						<c:forEach var="x"  items="${priorityEnum }">
							<option value="${x.name }">${x.name }</option>
						</c:forEach>
					</select>
				</td>
				<td style="width:50px;">邮编：</td>
				<td>
					<input class="easyui-textbox" type="text" name="postcode" style="width:100px;" data-options="required:true"  validType="postCode" missingMessage="该输入项为必输项"></input>
				</td>
				<td>有效性：</td>
				<td>
					<input type="checkbox" checked="checked" value="t" name="valid" />
				</td>
			</tr>
			<tr>
				<td>地址：</td>
				<td colspan="3">
					<input class="easyui-textbox" type="text" name="content" style="width:250px;" data-options="required:true"  validType="maxLength[50,' ']"></input>
				</td>
				<td>备注：</td>
				<td colspan="3">
					<input class="easyui-textbox" type="text" name="memo" style="width:200px;" data-options=""  validType="maxLength[50,' ']"></input>
				</td>
			</tr>
		</table>
		<div style="padding:5px 0;text-align:right;padding-right:30px">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="$.telAddress.saveItem.call($.telAddress.addressDataGrid,<%=index%>)">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="$.telAddress.cancelItem.call($.telAddress.addressDataGrid,<%=index%>)">关闭</a>
		</div>
	</form>
