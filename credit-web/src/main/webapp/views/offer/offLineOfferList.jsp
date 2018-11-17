<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="expires" content="0"/>
        <title>线下还款报盘</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                              ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
                    urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
                    urlJs.push(global.contextPath + '/resources/js/offer/offLineOfferList.js');
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
        <input type="hidden" id="isCanExportOffer" name="isCanExportOffer" value="${isCanExportOffer}" />
        <input type="hidden" id="isCanRealTimeDeduct" name="isCanRealTimeDeduct" value="${isCanRealTimeDeduct}" />
        <input type="hidden" id="isCanCloseOffer" name="isCanCloseOffer" value="${isCanCloseOffer}" />
        <!-- DataGrid 工具栏按钮 -->
        <div id="tb" style="padding:3px;">
            <div style="margin-bottom:0px">
                <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
                <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <sec:authorize ifAnyGranted="/offLineOffer/offerList/realTimeDeduct">
                    <a href="#" class="easyui-linkbutton" id="realTimeDeductBtn" iconCls="icon-add" plain="true">实时划扣</a>
                </sec:authorize>
                <sec:authorize ifAnyGranted="/offLineOffer/offerList/exportOffer">
                    <a href="#" class="easyui-linkbutton" id="exportOfferBtn" iconCls="pic_51" plain="true">导出报盘</a>
                </sec:authorize>
            </div>
        </div>
        <div class="easyui-panel" title="查询条件" style="padding: 5px; height: 145px; margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr>
                        <td>姓名：</td>
                        <td><input class="easyui-textbox" id="name" name="name" style="width: 180px;" /></td>
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
                            <select class="easyui-combobox" id='state' name='state' data-options="editable: false, panelHeight:'auto'" style="width: 180px;">
                                    <option value="">全部</option>
                                    <c:forEach var="state" items="${states}">
                                        <option value="${state}">${state}</option>
                                    </c:forEach>
                            </select>
                        </td>
                        <td>合同编号：</td>
                        <td><input class="easyui-textbox" id="contractNum" name="contractNum" style="width: 180px;"/></td>
                        <td>划扣通道：</td>
                        <td>
                            <select class="easyui-combobox" id='paySysNo' name='paySysNo' data-options="editable: false, panelHeight:'auto'" style="width: 150px;">
                                    <option value="">全部</option>
                                    <c:forEach var="paySysNo" items="${paySysNos}">
                                        <option value="${paySysNo.code}">${paySysNo.desc}</option>
                                    </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>开户银行：</td>
                        <td>
                            <select class="easyui-combobox" name="bankCode" id="bankCode" style="width:180px;" 
                                data-options="validType:'comboboxValidator[\'#bankCode\',\'开户银行\']'"></select>
                        </td>
                        <td>合同来源：</td>
                        <td>
                            <select class="easyui-combobox" id='fundsSource' name='fundsSource' data-options="editable: false" style="width: 180px;">
                                    <option value="">全部</option>
                                    <c:forEach var="fundsSource" items="${fundsSources}">
                                        <option value="${fundsSource}">${fundsSource}</option>
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
        
        <!-- 实时划扣窗口 -->
        <div id="realTimeDeductWin" class="easyui-window" title="实时划扣"  style="padding:20px;" 
            data-options="closed:true,title:'实时划扣',collapsible : false,minimizable : false,maximizable : false" style="width: 450px; padding: 30px 60px">
            <form method="post" id="dataForm">
                <input type="hidden" name="loanId" />
                <table cellpadding="10">
                    <tr>
                        <td>借款人：</td>
                        <td>
                            <input class="easyui-textbox" id="borrowerName" name="name" data-options="required:true, editable:false"/>
                        </td>
                    </tr>
                    <tr>
                        <td>合同编号：</td>
                        <td><input class="easyui-textbox" id="offerContractNum" name="contractNum" data-options="required:true, editable:false"/></td>
                    </tr>
                    <tr>
                        <td>报盘金额：</td>
                        <td>
                            <input class="easyui-numberbox" id="offerAmount" name="offerAmount" 
                            data-options="precision:2,groupSeparator:',',decimalSeparator:'.',prefix:'',required:true" validType="numberRangeValid[0.01,99999999] "/>
                        </td>
                    </tr>
                </table>
            </form>
            <div style="text-align:center;padding-top:10px;">
                <a href="#" class="easyui-linkbutton" id="submitDeductBtn" name="realTimeDeductBtn" data-options="iconCls:'icon-ok'">提交</a>
                <a href="#" class="easyui-linkbutton" id="closeBtn" data-options="iconCls:'icon-cancel'">关闭</a>
            </div>
        </div>
        <!-- 实时划扣窗口 -->
    </body>
</html>