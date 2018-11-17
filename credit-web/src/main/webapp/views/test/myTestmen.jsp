<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>EasyUI示例</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
	</head>
	<body class="easyui-layout">
		<!-- DataGrid 工具栏按钮 -->
		<div class="easyui-panel" title="查询条件" style="padding:5px;height:100px;margin: 0px;" data-options="region:'north'">
			<form id="searchForm" action="/credit-web/core/loanCore/createLoanTrial" method="get">
				<table cellpadding="5">
					<tr>
						<td>姓名:</td>
						<td><input class="easyui-textbox" type="text" name="name" /></td>
						<td>贷款类型:</td>
						<td>
						    <select class="easyui-combobox" name="loanType">
								<option value="00001">随薪贷</option>
								<option value="00002">随意贷</option>
								<option value="00003">随意贷A</option>
								<option value="00004">随意贷B</option>
								<option value="00005">随意贷C</option>
								<option value="00006">随房贷</option>
								<option value="00007">助学贷</option>
								<option value="00008">车贷</option>
								<option value="00009">薪生贷</option>
								<option value="00010">随车贷</option>
								<option value="00011">随房贷A</option>
								<option value="00012">随房贷B</option>
								<option value="00013">公积金贷</option>
								<option value="00014">保单贷</option>
								<option value="00015">网购达人贷A</option>
								<option value="00016">淘宝商户贷</option>
								<option value="00017">学历贷</option>
								<option value="00018">卡友贷</option>
								<option value="00020">网购达人贷B</option>
							</select>
						</td>
						<td>借款金额:</td>
						<td><input class="easyui-textbox" type="text" name="money" /></td>
						<td>借款期数:</td>
						<td>
						    <select class="easyui-combobox" name="time" style="width: 80px;">
								<option value="12">12</option>
								<option value="18">18</option>
								<option value="24">24</option>
								<option value="36">36</option>
							</select>
						</td>
						<td>预计首次还款日:</td>
						<td><input class="easyui-datebox" type="text" name="firstRepaymentDate" /></td>
						<td>合同来源（渠道）:</td>
						<td>
							<select class="easyui-combobox" name="fundsSources">
								<option value=""></option>
								<option value="00001">证大P2P</option>
								<option value="00002">向上360P</option>
								<option value="00003">证大爱特</option>
								<option value="00004">国民信托</option>
								<option value="00005">华澳信托</option>
								<option value="00006">证大爱特2</option>
								<option value="00007">积木盒子</option>
								<option value="00008">挖财2</option>
								<option value="00011">海门小贷</option>
								<option value="00012">渤海信托</option>
								<option value="00013">龙信小贷</option>
								<option value="00014">外贸信托</option>
								<option value="00015">渤海2</option>
								<option value="00016">捞财宝</option>
								<option value="00017">外贸2</option>
								<option value="00018">包商银行</option>
								<option value="00019">外贸3</option>
								<option value="00020">华瑞渤海</option>
								<option value="00021">陆金所</option>
							</select>
						</td>
					</tr>
				</table>
				<div style="text-align:center;padding-top:10px;">
					<button type="submit">提交</button>
				</div>
			</form>
		</div>
	</body>
</html>
