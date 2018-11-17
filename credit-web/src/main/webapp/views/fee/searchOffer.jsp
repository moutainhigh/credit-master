<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"%>
<%@ page import="java.util.Date" %>
<%@ page import="com.zdmoney.credit.common.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="expires" content="0"/>
        <title>服务费报盘信息</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','messager'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/fee/searchOffer.js');
                    urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
                    urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
                    importJSExt(urlJs,function(){
                        /** 脚本加载成功回调方法 **/
                    });
                });
            });
        </script>
    </head>
    <body class="easyui-layout">
    	<input type="hidden" id="sysDate" name="sysDate" value='<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>' />
        <jsp:include page="/views/common/initPageMast.jsp" />
        <!-- DataGrid 工具栏按钮 -->
        <div id="tb" style="padding:3px;">
            <div style="margin-bottom:0px">
                <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true" style=";">查询</a>
                <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true,formId:'#conditionForm'" style="width:60px;float: none;">重置</a>
            </div>
        </div>
        <div class="easyui-panel" title="查询条件" style="padding:5px;height:145px;margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr>
                        <td>借款人：</td>
                        <td><input class="easyui-textbox" name="name" style="width:200px"/></td>
                        <td>身份证号：</td>
                        <td>
                            <input class="easyui-textbox" name="idNum" validType="idCard" style="width:150px"/>
                        </td>
                        <td>合同编号：</td>
                        <td>
                            <input class="easyui-textbox" name="contractNum" style="width:150px" />
                        </td>
                    </tr>
                    <tr>
                        <td>报盘状态：</td>
	                    <td>
	                        <select class="easyui-combobox" id="state" name="state" style="width:200px;" data-options="editable:false,panelHeight:'auto'">
	                            <option value="">请选择</option>
	                            <c:forEach var="state" items="${states}">
	                                <option value="${state}">${state}</option>
	                            </c:forEach>
	                        </select>
	                    </td>
	                    <td>划扣通道：</td>
                        <td>
                            <select class="easyui-combobox" id='paySysNo' name='paySysNo' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                                    <option value="">全部</option>
                                    <c:forEach var="paySysNo" items="${paySysNos}">
                                        <option value="${paySysNo.code}">${paySysNo.desc}</option>
                                    </c:forEach>
                            </select>
                        </td>
	                    <td>合同来源：</td>
	                    <td>
	                        <select class="easyui-combobox" id="fundsSources" name="fundsSources" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
	                            <option value="">请选择</option>
	                            <c:forEach var="sources" items="${fundsSources}">
	                                <option value="${sources}">${sources}</option>
	                            </c:forEach>
	                        </select>
	                    </td>
                    </tr>
                    <tr>
                    	<td>开户银行:</td>
						<td>
							<select class="easyui-combobox" name="bankCode" id="bankCode"  style="width:200px;" 
								data-options="validType:'comboboxValidator[\'#bankCode\',\'开户银行\']'"></select>
						</td>
						<td>收取状态：</td>
	                    <td>
	                        <select class="easyui-combobox" id="loanFeeState" name="loanFeeState" style="width:150px;" data-options="editable:false,panelHeight:'auto'">
	                            <option value="">请选择</option>
	                            <c:forEach var="loanFeeState" items="${loanFeeStates}">
	                                <option value="${loanFeeState}">${loanFeeState}</option>
	                            </c:forEach>
	                        </select>
	                    </td>
                        <td>报盘日期：</td>
                        <td colspan="3">
                        	<input class="easyui-datebox" id="offerDateBegin" name="offerDateBegin" style="width:95px" 
                                    data-options="editable:true,validType:['date','dateRangeValid[\'&lt;=\',\'#offerDateEnd\',\'结束日期\']']"  /><span style="padding-left: 6px;">～</span>
                            <input class="easyui-datebox" id="offerDateEnd" name="offerDateEnd" style="width:95px" 
                                    data-options="editable:true,validType:['date','dateRangeValid[\'&gt;=\',\'#offerDateBegin\',\'开始日期\']']" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
            <!-- 表格标签 -->
            <table id="dataGrid"></table>
        </div>
        
        <!-- 实时划扣窗口 -->
        <div id="actualTimeOfferWin" class="easyui-window editContentPanel" title="实时划扣" 
                data-options="closed : true,collapsible : false,minimizable : false, maximizable : false,modal : true,resizable : false">
            <div style="padding: 20px;">
				<form  id="actualTimeOfferForm" >
					<input type="hidden"  name="feeId" id="feeId"/>
					<table cellpadding="5">
	                    <tr>
	                        <td>划扣金额：</td>
	                        <td>
	                        	<input class="easyui-numberbox" id="amount" name="amount" style="width:100px;" data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true" validType=""></input>
	                        </td>
	                    </tr>
	                </table>
				</form>
				<div style="text-align:center;padding-top:15px;">
					<a href="#" id="submitBtn" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" style="margin-right:15px;">还款</a>
					<a href="#" id="closeBtn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
				</div>
			</div>
        </div>
        <!-- 实时划扣窗口 -->
        
        <!-- 回盘详细信息窗口 -->
        <div id="offerTransactionWin" class="easyui-window "  title="详细信息" data-options="width:'800px',height:'300px'">
            <table id="offerTransactionDataGrid"></table>
        </div>
        <!-- 回盘详细信息窗口 -->
    </body>
</html>