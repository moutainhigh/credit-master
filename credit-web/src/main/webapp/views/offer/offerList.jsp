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
        <title>委托代扣报盘</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>        
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                              ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/offer/offer.js');
                    importJSExt(urlJs,function(){
                        /** 脚本加载成功回调方法 **/
                    });
                });
            });
        </script>
    </head>
    <body class="easyui-layout">
        <jsp:include page="/views/common/initPageMast.jsp" />
        <div id="offerParams" style="display:none;" data-offer-perms="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.authorities}" ></div>
        <!-- DataGrid 工具栏按钮 -->
        <div id="tb" style="padding:3px;">
            <div style="margin-bottom:0px">
                <a href="#" class="easyui-linkbutton" id="searchBut" iconCls="icon-search" plain="true">查询</a>
                <a href="#" class="easyui-linkbutton" id="clearCondition" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <sec:authorize ifAnyGranted="/offer/offerInfo/insert">
                    <a href="#" class="easyui-linkbutton" id="addBut" iconCls="icon-add" plain="true">实时划扣</a>
                </sec:authorize>
                <a href="#" class="easyui-linkbutton" id="exportBtn" iconCls="pic_51" plain="true">EXCEL导出</a>
            </div>
        </div>
        <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 145px; margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr>
                        <td>姓名：</td>
                        <td><input class="easyui-textbox" id="name" name="name" style="width: 150px;" /></td>
                        <td>身份证号：</td>
                        <td><input class="easyui-textbox" id="idNum" name='idNum' validType="idCard" style="width: 180px;" /></td>
                        <td>报盘日期：</td>
                        <td>
                            <input class="easyui-datebox" id="offerDate" name="offerDate" value="${offerDate}" data-options="editable:false" style="width: 150px;" />
                        </td>
                    </tr>
                    <tr>
                        <td>状态：</td>
                        <td>
                            <select class="easyui-combobox" id='state' name='state' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                                    <option value="">全部</option>
                                    <c:forEach var="state" items="${states}">
                                        <option value="${state}">${state}</option>
                                    </c:forEach>
                            </select>
                        </td>
                        <td>合同编号:</td>
                        <td><input class="easyui-textbox" id="contractNum" name="contractNum" style="width: 180px;"/></td>
                        <td>当前报盘营业部：</td>
                        <td>${orgName}</td>
                    </tr>
                    <tr>
                        <td>划扣通道：</td>
                        <td>
                            <select class="easyui-combobox" id='paySysNo' name='paySysNo' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                                    <option value="">全部</option>
                                    <c:forEach var="paySysNo" items="${paySysNos}">
                                        <option value="${paySysNo.code}">${paySysNo.value}</option>
                                    </c:forEach>
                            </select>
                        </td>
                        <td>开户银行:</td>
						<td>
							<select class="easyui-combobox" name="bankCode" id="bankCode"  style="width:180px;" 
								data-options="validType:'comboboxValidator[\'#bankCode\',\'开户银行\']'"></select>
						</td>
						<td>债权去向:</td>
						<td>
                            <select class="easyui-combobox" id='loanBelong' name='loanBelong' data-options="editable: false" style="width: 150px;">
                                    <option value="">全部</option>
                                    <c:forEach var="loanBelong" items="${loanBelongs}">
                                        <option value="${loanBelong.codeTitle}">${loanBelong.codeTitle}</option>
                                    </c:forEach>
                            </select>
                        </td>
                     </tr>
                </table>
            </form>
        </div>
        <div class="easyui-panel" style="padding:0px;margin: 0px;" data-options="region:'center'">
            <!-- 表格标签 -->
            <table id="dataGrid" class="easyui-datagrid"></table>
        </div>
        
        <!-- 新增面板 -->
        <div id="addDataPanel" class="easyui-window" title="实时划扣"  style="padding:20px;" data-options="closed:true,title:'实时划扣',collapsible : false,minimizable : false,maximizable : false" style="width: 450px; padding: 30px 60px"  >
            <form method="post"  id="dataForm">
                <input type="hidden" name="loanId" />
                <input type="hidden" id="isShowPayChannel" value="${isShowPayChannel}" name="isShowPayChannel"/>
                <table cellpadding="10">
                    <tr>
                        <td>借款人:</td>
                        <td>
                            <input class="easyui-textbox" id="borrowName" name="borrowName" data-options="required:true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>合同编号:</td>
                        <td><input class="easyui-textbox" id="contractNumAdd" name="contractNum" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td>报盘金额:</td>
                        <td>
                            <input class="easyui-numberbox" id="offerAmount" name="offerAmount" 
                            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true" validType="numberRangeValid[0.01,99999999] "/>
                        </td>
                    </tr>
                    <tr>
	                    <td><div id="paySysNoAddStrDiv" style="display:none">划扣通道:</div></td>
	                    <td>
	                    <div id="paySysNoAddDiv" style="width: 173px; display:none">
	                         <select class="easyui-combobox" id='paySysNoAdd' name='paySysNoAdd' data-options="editable: false, panelHeight:'auto'"  style="width: 173px; display:none">
	                         </select>
	                       </div> 
	                    </td>
               		</tr>
                </table>
            </form>
            <div style="text-align:center;padding-top:10px;">
                <a href="javascript:void(0)" id = "submitBut" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>
            </div>
        </div>
    </body>
</html>