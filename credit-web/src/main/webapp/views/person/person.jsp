<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>客户资料查询</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			importPluginsExt([],'business', function() {
				$(function() {
					
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/easyui/datagrid-detailview.js');
					urlJs.push(global.contextPath + '/resources/js/person/person.js');
					urlJs.push(global.contextPath + '/resources/js/person/personContact.js');
					urlJs.push(global.contextPath + '/resources/js/person/personBank.js');
					urlJs.push(global.contextPath + '/resources/js/person/personTelAddress.js');
					importJSExt(urlJs,function(){
						
					});
				});
			});
		</script>
	</head>
	<body class="easyui-layout" style=" ">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<!-- DataGrid 工具栏按钮 -->
		<div id="tb" style="padding:3px;">
			<div style="margin-bottom:0px">
				<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true" style=";">查询</a>
				<a href="javascript:void(0)" id="clearBut" class="easyui-linkbutton" 
							data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;float: none;">重置</a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" class="easyui-linkbutton" id="showDetailBut"  iconCls="pic_31"  style=";" plain="true">查看详细信息</a>
				<sec:authorize ifAnyGranted="/person/saveOrUpdate">
					  <a href="#" class="easyui-linkbutton" id="editDetailBut"  iconCls="pic_36"  style="" plain="true">修改详细信息</a>
				</sec:authorize>
				<sec:authorize  ifAnyGranted="/personContact/saveOrUpdateData">
					<a href="#" class="easyui-linkbutton" id="showContactBut"  iconCls="pic_36"  style="" plain="true">维护联系人信息</a>
				</sec:authorize>
				<sec:authorize  ifAnyGranted="/offer/loanBank/updateLoanBank">
					<a href="#" class="easyui-linkbutton" id="editBankBut"  iconCls="pic_36"  style="" plain="true">变更开户银行</a>
				</sec:authorize>
				<sec:authorize  ifAnyGranted="/personTel/saveOrUpdate,/personAddress/saveOrUpdate">
					<a href="#" class="easyui-linkbutton" id="editTelAddressBut"  iconCls="pic_36"  style="" plain="true">变更电话、地址信息</a>
				</sec:authorize>
			</div>
		</div>
		<!-- DataGrid 工具栏按钮 -->
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:115px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm">
				<table cellpadding="5">
					<tr>
						<td>姓名：</td>
						<td><input class="easyui-textbox" type="text" name="name" value=""></input></td>
						<td>手机：</td>
						<td><input class="easyui-textbox" type="text" name="mphone"  validType="mobile"></input></td>
						<td>身份证号：</td>
						<td><input class="easyui-textbox" type="text" value="" name="idnum" validType="idCard" ></input></td>
						<td>房产证号：</td>
						<td><input class="easyui-textbox" type="text" name="realEstateLicenseCode" ></input></td>
					</tr>
					<tr>
						<td>联系人：</td>
						<td><input class="easyui-textbox" type="text" name="contactName" ></input></td>
						<td>联系人手机：</td>
						<td><input class="easyui-textbox" type="text"  value="" name="contactMPhone"  validType="mobile"></input></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="customerDataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		
		<!-- 维护联系人信息 -->
		<div id="contactTB" style="padding:3px;">
			<div style="margin-bottom:0px">
				<a href="#" class="easyui-linkbutton" id="contact_addDataBut"  iconCls="icon-add"  style="float:left;" plain="true">新增</a>
				<a class="datagrid-btn-separator"></a>
				<a href="#" class="easyui-linkbutton" id="contact_loadDataBut"  iconCls="icon-add"  style="float:left;" plain="true">查看/修改</a>
				<a class="datagrid-btn-separator"></a>
				<a href="#" class="easyui-linkbutton" id="contact_editTelAddressBut"  iconCls="icon-add"   plain="true">变更电话、地址信息</a>
			</div>
		</div>
		<div id="showContactWin" class="easyui-window" title="联系人信息" data-options="closed:true" style="width:700px;height:400px;padding:0px;">
			<!-- 联系人表格 -->
			<table id="contactDataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		
		<div id="editContactWin" class="easyui-window " title="修改联系人信息" data-options="closed:true" style="">
			<div class="editContentPanel" style="padding:10px;">
				<form id="editCoontactForm">
					<input  type="hidden" name="id" ></input>
					<input  type="hidden" name="personId" ></input>
					<table cellpadding="5">
						<tr>
							<td width="80px" class="required">姓名:</td>
							<td><input class="easyui-textbox" type="text" name="name" data-options="required:true" style="" validType="maxLength[10,' ']" ></input></td>
							<td width="80px" class="required">关系:</td>
							<td>
								<select class="easyui-combobox" id="contactRelation" name="relation" data-options="editable:false,panelHeight:'auto',required:true,width:155" missingMessage="该输入项为必选项">
									<c:forEach var="x"  items="${contactRelationEnum }">
										<option value="${x.name }">${x.name }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>身份证号:</td>
							<td><input class="easyui-textbox" type="text" name="idCard" data-options="" style="" validType="idCard"></input></td>
							<td>是否知晓贷款:</td>
							<td><input type="checkbox"  value="t" name="isKnowLoan" /></td>
						</tr>
						<tr>
							<td>手机:</td>
							<td><input class="easyui-textbox" type="text" name="mphone" data-options="" style="" validType="mobile"></input></td>
							<td>家庭电话:</td>
							<td><input class="easyui-textbox" type="text" name="tel" data-options="" style="" validType="tel"></input></td>
						</tr>
						<tr>
							<td>公司电话:</td>
							<td><input class="easyui-textbox" type="text" name="ctel" data-options="" style="" validType="tel"></input></td>
							<td>联系人类型:</td>
							<td>
								<select class="easyui-combobox" id="contactType" name="contactType" data-options="editable:false,panelHeight:'auto',required:true,width:155" missingMessage="该输入项为必选项">
									<c:forEach var="x"  items="${contactTypeEnum }">
										<option value="${x.name }">${x.name }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>职务:</td>
							<td><input class="easyui-textbox" type="text" name="duty" data-options="" style="" validType="maxLength[50,' ']" ></input></td>
							<td>部门:</td>
							<td><input class="easyui-textbox" type="text" name="department" data-options="" style="" validType="maxLength[50,' ']" ></input></td>
						</tr>
						<tr>
							<td>现居地址:</td>
							<td colspan="3"><input class="easyui-textbox" type="text" name="address" data-options=""  validType="maxLength[50,' ']" style="width:440px;"></input></td>
						</tr>
						<tr>
							<td>所在公司:</td>
							<td colspan="3"><input class="easyui-textbox" type="text" name="company" data-options=""  validType="maxLength[50,' ']" style="width:440px;"></input></td>
						</tr>
					</table>
				</form>
				<div style="text-align:center;padding-top:10px;">
					<a href="javascript:void(0)" id="contact_submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
					<a href="javascript:void(0)" id="contact_closeBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
				</div>
			</div>
		</div>
		<!-- 维护联系人信息 -->
		
		<!-- 变更开户银行信息 -->
		<div id="bankTB" style="padding:3px;">
			<div style="margin-bottom:0px">
				<a href="#" class="easyui-linkbutton" id="bank_loadDataBut"  iconCls="icon-add"   plain="true">查看/修改</a>
			</div>
		</div>
		<div id="editBankWin" class="easyui-window " title="变更开户银行信息" data-options="closed:true" style="">
			<div class="editContentPanel" style="padding:10px;">
				<form id="editBankForm">
					<input type="hidden" name="id" ></input>
					<input type="hidden" name="objectId"></input>
					<table cellpadding="5">
						<tr>
							<td width="80px" class="required">开户银行:</td>
							<td>
								    <select class="easyui-combobox" name="bankCode" id="bankCombo"  style="width:240px;" data-options=""></select>
							</td>
						</tr>
						<tr>
							<td class="required">开户网点:</td>
							<td><input class="easyui-textbox" type="text" name="fullName" data-options="required:true" style="width:240px;" validType="maxLength[40,' ']"></input></td>
						</tr>
						<tr>
							<td class="required">账户:</td>
							<td><input class="easyui-textbox" type="text" name="account" style="width:240px;"  data-options="required:true,validType:['numer[\' \']','maxLength[30,\'\']']"></input></td>
						</tr>
					</table>
				</form>
				<div style="text-align:center;padding-top:10px;">
					<a href="javascript:void(0)" id="updateBankBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >提交</a>
					<a href="javascript:void(0)" id="closeBankBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
				</div>
			</div>
		</div>
		<div id="showMyBankListWin" class="easyui-window" title="银行卡信息" data-options="closed:true" style="width:700px;height:400px;padding:0px;">
			<!-- 银行卡表格 -->
			<table id="bankDataGrid" class="easyui-datagrid" data-options=""></table>
		</div>
		<!-- 变更开户银行信息 -->
		
		
		<!-- 变更电话、地址信息 -->
		<div id="telToolbar">
			<a href="javascript:void(0)" id="telNewItem" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
		</div>
		<div id="addressToolbar">
			<a href="javascript:void(0)" id="addressNewItem" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
		</div>
		
		<div id="editTelAddressWin" class="easyui-window" title="变更电话、地址信息" data-options="closed:true" style="width:850px;height:500px;padding:0px;">
			<div class="easyui-tabs" style="" data-options="tabPosition:'top',fit:true">
				<div title="电话" data-options="iconCls:'icon-large-smartart'" style="padding:0px">
					<!-- 联系电话数据 -->
					<table id="telDataGrid" class="easyui-datagrid" data-options=""></table>
				</div>
				<div title="地址" data-options="iconCls:'icon-large-smartart'" style="padding:0px">
					<!-- 联系地址数据 -->
					<table id="addressDataGrid" class="easyui-datagrid" data-options=""></table>
				</div>
			</div>
		</div>
		<!-- 变更电话、地址信息 -->
	</body>
</html>