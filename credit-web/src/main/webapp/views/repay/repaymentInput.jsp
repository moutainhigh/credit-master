<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>还款录入</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','messager'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/repay/repaymentInput.js');
					urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
					urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				});
			});
		</script>
		<style type="text/css">
			
		</style>
	</head>
	<body class="easyui-layout">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<!-- DataGrid 工具栏按钮 -->
		<div id="tb" style="padding:3px;">
			<div style="margin-bottom:0px">
				<a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true" style=";">查询</a>
				<a href="javascript:void(0)" id="clearBut" class="easyui-linkbutton" 
							data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;float: none;">重置</a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<sec:authorize ifAnyGranted="/offer/repayInfo/repaymentInput">
					<a href="#" class="easyui-linkbutton" id="repayBut"  iconCls="pic_46" style="" plain="true">还款</a>
				</sec:authorize>
				<sec:authorize ifAnyGranted="/offer/repayInfo/importRepayInfoFile,/offer/repayInfo/importCarRepayInfoFile,/offer/repayInfo/importZfRepayInfoFile">
					<a href="#" class="easyui-linkbutton" id="importBut"  iconCls="pic_42" style="" plain="true">导入</a>
				</sec:authorize>
			</div>
		</div>
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:75px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm">
				<table cellpadding="5">
					<tr>
						<td>姓名：</td>
						<td><input class="easyui-textbox" type="text" value="" name="name" ></input></td>
						<td>身份证号：</td>
						<td>
							<input class="easyui-textbox" type="text" value="" name="idnum" validType="idCard" ></input>
						</td>
						<td>合同编号：</td>
						<td>
							<input class="easyui-textbox" type="text" value="" name="contractNum"></input>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
			<!-- 表格标签 -->
			<table id="dataGrid" class="" data-options="">
				            
			</table>
		</div>
		
		<!-- Excel 导入 -->
		<div id="importExcelWin" class="easyui-window editContentPanel" title="Excel导入" data-options="closed : true,collapsible : false,minimizable : false,
											maximizable : false,modal : true,resizable : false" style="padding:0px;">
			<sec:authorize ifAnyGranted="/offer/repayInfo/importRepayInfoFile">
				<div style="text-align:center;padding:10px;" >
					<h3>信贷系统还款导入</h3>
					<form  id="creditFileForm"  enctype="multipart/form-data" method="post">
						<input class="easyui-filebox" name="uploadFile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
						<a href="javascript:void(0)" id="importExcelBut" class="easyui-linkbutton" data-options="iconCls:'pic_52'" >导入</a>
					</form>
				</div>
			</sec:authorize>
			<sec:authorize ifAnyGranted="/offer/repayInfo/importCarRepayInfoFile">
				<div style="text-align:center;padding:15px;" >
					<h3>车企贷系统还款导入(导入文件的后缀必须以"_car"结尾)</h3>
					<form  id="carFileForm"  enctype="multipart/form-data" method="post">
						<input class="easyui-filebox" name="uploadFile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
						<a href="javascript:void(0)" id="importCarExcelBut" class="easyui-linkbutton" data-options="iconCls:'pic_52'" >导入</a>
					</form>
				</div>
			</sec:authorize>
			<sec:authorize ifAnyGranted="/offer/repayInfo/importZfRepayInfoFile">
				<div style="text-align:center;padding:15px;" >
					<h3>证方系统还款导入(导入文件的后缀必须以"_zf"结尾)</h3>
					<form id="zfFileForm" enctype="multipart/form-data" method="post">
						<input class="easyui-filebox" id="zfRepayInfoFile" name="zfRepayInfoFile" data-options="prompt:'请选择文件...',buttonText:'选择文件'" style="width:300px"/>
						<a href="#" id="importZfRepayInfoBtn" class="easyui-linkbutton" data-options="iconCls:'pic_52'" >导入</a>
					</form>
				</div>
			</sec:authorize>
			<br/>
		</div>
		<!-- Excel 导入 -->
		
		<!-- 还款信息窗口 -->
		<div id="repayWin" class="easyui-window " title="还款信息" data-options="" style="">
		<div class="editContentPanel" style="padding:10px;">
			<form id="repayForm" >
				<input  type="hidden" name="id" ></input>
				<table cellpadding="5"  border="0" rules="rows" >
					<tr>
						<td width="150px" class="">姓名：</td>
						<td width="100px"><input  readonly="readonly" class="inputLabel" name="name" style="width:90px;"/></td>
						<td width="140px" class="">身份证号：</td>
						<td width="110px"><input  readonly="readonly" class="inputLabel" name="idNum"/></td>
						<td width="100px" class="">手机：</td>
						<td width="10px"><input  readonly="readonly" class="inputLabel" name="mphone"/></td>
					</tr>
					<tr>
						<td width="" class="">逾期起始日：</td>
						<td><input  readonly="readonly" class="inputLabel" name="overDueDate" style="width:90px;"/></td>
						<td width="" class="">逾期总数：</td>
						<td><input  readonly="readonly" class="inputLabel" name="overDueTerm"/></td>
						<td width="" class="">逾期应还本息和：</td>
						<td><input  readonly="readonly" class=" inputLabel thousand" name="overAmount" /></td>
					</tr>
					<tr>
						<td width="" class="">罚息起算日：</td>
						<td><input  readonly="readonly" class="inputLabel" name="fineDate" style="width:90px;"/></td>
						<td width="" class="">罚息天数：</td>
						<td><input  readonly="readonly" class="inputLabel" name="fineDay"/></td>
						<td width="" class="">罚息金额：</td>
						<td><input  readonly="readonly" class="inputLabel thousand" name="fine"/></td>
					</tr>
					<tr>
						<td width="" class="">当期还款日：</td>
						<td><input  readonly="readonly" class="inputLabel" name="currDate" style="width:90px;"/></td>
						<td width="" class="">当前期数：</td>
						<td><input  readonly="readonly" class="inputLabel" name="currTerm"/></td>
						<td width="" class=""><span id="isOneTimeFlag">当期应还本息和</span>：</td>
						<td><input  readonly="readonly" class="inputLabel thousand" name="curBX"/></td>
					</tr>
					<tr>
						<td width="" class="">挂账金额：</td>
						<td><input  readonly="readonly" class="inputLabel thousand" name="accAmount" style="width:90px;"/></td>
						<td width="" class="">减免金额：</td>
						<td><input  readonly="readonly" class="inputLabel thousand" name="relief"/></td>
						<td width="" class="">合同编号：</td>
						<td><input  readonly="readonly" class="inputLabel" name="contractNum" style="width:120px;"/></td>
					</tr>
					<tr>
						<td width="" class="">应还总额（不含当期）：</td>
						<td><input  readonly="readonly" class="inputLabel thousand" name="overdueAmount" style="width:90px;"/></td>
						<td width="" class="">应还总额（包含当期）：</td>
						<td><input  readonly="readonly" class="inputLabel thousand" name="currAllAmount"/></td>
						<td width="" class="">&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td width="" class="">还款日期：</td>
						<td><input  readonly="readonly" class="inputLabel" name="curDate" value="<fmt:formatDate value="<%=new java.util.Date() %>"  pattern="yyyy-MM-dd"  />"/></td>
						<td width="" class="">还款金额：</td>
						<td>
							<input name="allAmount" type="hidden" />
							<input class="easyui-numberbox"  id="amount" name="amount" style="width:100px;" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true"  validType=""></input>
						</td>
						<td width="" class="">还款方式：</td>
						<td>
							<select class="easyui-combobox" name="tradeType" id="tradeType" style="width:80px;" data-options="editable:false,panelHeight:'auto',required:true" >
								<option value="现金">现金</option>
								<option value="转账">转账</option>
							</select>
						</td>
					</tr>
					<tr>
						<td width="" class="">备注：</td>
						<td colspan="5"><input class="easyui-textbox" name="memo" data-options="multiline:true" style="height:60px;width:400px;"></input></td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding-top:15px;">
				<a href="javascript:void(0)" id="repaySubmitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"  style="margin-right:15px;">还款</a>
				<a href="javascript:void(0)" id="repayCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
			</div>
			</div>
		</div>
		<!-- 还款信息窗口 -->
	</body>
</html>