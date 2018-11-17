<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="expires" content="0"/>
        <title>委托代扣回盘</title>
        <jsp:include page="/views/common/headIncludeFile.jsp" />
        <link type="text/css" rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
        <script type="text/javascript">
            importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist'
                              ,'form','switchbutton','tooltip','validatebox','combogrid'],'business', function() {
                $(function() {
                    var urlJs = [];
                    urlJs.push(global.contextPath + '/resources/js/offer/offerReturnList.js');
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
        <div class="easyui-panel" id="tb" style="padding: 5px;">
            <a href="#" class="easyui-linkbutton" id="searchBtn" iconCls="icon-search" plain="true">查询</a>
            <a href="#" class="easyui-linkbutton" id="clearBtn" data-options="iconCls:'icon-clear',plain:true" style="width:60px;float: none;">重置</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <sec:authorize ifAnyGranted="/offer/offerReturn/exportReturnFile">
                <a href="#" class="easyui-linkbutton" id="exportBtn" iconCls="pic_51" plain="true">EXCEL导出</a>
            </sec:authorize>
        </div>
        
        <div class="easyui-panel" title="查询条件" style="padding:5px;height:145px;margin: 0px;" data-options="region:'north'">
            <form id="searchForm">
                <table cellpadding="5">
                    <tr>
                        <td>姓名：</td>
                        <td>
                            <input class="easyui-textbox" id="name" name="name" data-options="validType:'maxLength[60,\'客户姓名\']'" style="width:140px"/>
                        </td>
                        <td>身份证号：</td>
                        <td>
                            <input class="easyui-textbox" id="idNum" name='idNum' validType="idCard" style="width:180px"/>
                        </td>
                        <td>是否速贷:</td>
                        <td>
                            <select class="easyui-combobox" id='memo' name="memo" style="width:140px" data-options="editable:false,panelHeight:'auto'">
                                <option value="">全部</option>
                                <option value="1">是</option>
                                <option value="2">否</option>
                            </select>
                        </td>
                        <td>报盘日期：</td>
                        <td>
                            <input class="easyui-datebox" id="startOfferDate" name="startOfferDate" style="width:120px" value="${sysdate}" data-options="validType:'date'"/> ~
                            <input class="easyui-datebox" id="endOfferDate" name="endOfferDate" style="width:120px" value="${sysdate}" data-options="validType:'date'"/>
                        </td>
                    </tr>
                     <tr>
                        <td>划扣状态：</td>
                        <td>
                            <select class="easyui-combobox" id='returnCode' name="returnCode" style="width:140px" data-options="editable:false,panelHeight:'auto'">
                                <option value="">全部</option>
                                <option value="sending">已发送</option>
                                <c:forEach var="returnCode" items="${returnCodes}">
                                    <option value="${returnCode.code}">${returnCode.desc}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>划扣通道：</td>
                        <td>
                            <select class="easyui-combobox" id='paySysNo' name='paySysNo' data-options="editable: false, panelHeight:'auto'" style="width: 180px;">
                                    <option value="">全部</option>
                                    <c:forEach var="paySysNo" items="${paySysNos}">
                                        <option value="${paySysNo.code}">${paySysNo.value}</option>
                                    </c:forEach>
                            </select>
                        </td>
                        <td>合同来源：</td>
                        <td>
                            <select class="easyui-combobox" id='fundsSource' name="fundsSource"  data-options="editable:false,panelHeight:'auto'">
                                <option value="">全部</option>
                                <c:forEach var="fundsSource" varStatus="status" items="${fundsSources}">
                                    <option value="${fundsSource.value}">${fundsSource.value}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>回盘日期：</td>
                        <td>
                            <input class="easyui-datebox" id="startReceiveDate" name="startReceiveDate" style="width:120px" value="${sysdate}" data-options="validType:'date'"/> ~
                            <input class="easyui-datebox" id="endReceiveDate" name="endReceiveDate" style="width:120px" value="${sysdate}" data-options="validType:'date'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>合同编号:</td>
					    <td><input class="easyui-textbox" id="contractNum" name="contractNum" style="width: 140px;"/></td>
					    <td>开户银行:</td>
						<td>
							<select class="easyui-combobox" name="bankCode" id="bankCode"  style="width:180px;" 
								data-options="validType:'comboboxValidator[\'#bankCode\',\'开户银行\']'"></select>
						</td>
						<td>债权去向:</td>
						<td>
                            <select class="easyui-combobox" id='loanBelong' name='loanBelong' data-options="editable: false">
                                    <option value="">全部</option>
                                    <c:forEach var="loanBelong" items="${loanBelongs}">
                                        <option value="${loanBelong.codeTitle}">${loanBelong.codeTitle}</option>
                                    </c:forEach>
                            </select>
                        </td>
                        <td style="text-align: left;">
							转让批次：
						</td>
						<td style="text-align: left;">
							<select class="easyui-combobox" id="transferBatch" name="transferBatch" style="width: 150px;" data-options="editable:false,panelHeight:'auto'">
								<option value="">请选择</option>
								<c:forEach items="${transferBatchs}" var="transferBatch">
									<option value="${transferBatch.batchs}" >${transferBatch.batchs}</option>
								</c:forEach>
							</select>
						</td>
                    </tr>
                </table>
            </form>
        </div>
        
        <!-- 表格标签 -->
        <div class="easyui-panel" style="padding: 0px; margin: 0px;"data-options="region:'center'">
            <table id="dataGrid" class="easyui-datagrid"></table>
        </div>
    </body>
</html>