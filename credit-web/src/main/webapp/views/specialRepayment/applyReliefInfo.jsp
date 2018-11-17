<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"  prefix="sec"  %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta http-equiv="pragma" content="no-cache"/>
  <meta http-equiv="cache-control" content="no-cache"/>
  <meta http-equiv="expires" content="0"/>
  <title>申请减免详情</title>
  <jsp:include page="/views/common/headIncludeFile.jsp" />

  <link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
  <script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination','datalist','form','messager'],'business', function() {
      $(function() {
        var urlJs = [];
        urlJs.push(global.contextPath + '/resources/js/jquery.form.js');
        urlJs.push(global.contextPath + '/resources/js/jquery.browser.js');
        urlJs.push(global.contextPath + '/resources/js/specialRepayment/applyReliefInfo.js');
        importJSExt(urlJs,function(){
          /** 脚本加载成功回调方法 **/

        });
      });
    });
  </script>
<style type="text/css">
  #repayForm{
    font-size: 14px;
    float: left;
  }
  #repayForm td{
    text-align: left;
  }
  #repayForm input,textarea{
    margin-left: 8px;
  }
  #repayForm table{
    border: 1px solid gray;
    margin: 10px;
    padding: 10px;
  }
</style>
</head>
<body class="easyui-layout">
<jsp:include page="/views/common/initPageMast.jsp" />
<!-- 申请减免详细信息窗口 -->
<div id="appleReliefInfoPanel" class="easyui-panel"
     style="padding: 0px; height: 100%;width: 100%; margin: 0px; text-align: center;"
      data-options="" style="" data-options="fit:true">
    <form id="repayForm" >
       <input type="hidden"  name="loanId" value="${vLoanInfo.id}"/>
       <input type="hidden"  name="reliefTypeCode" value="${reliefTypeEnum.code}"/>
       <input type="hidden"  name="maxReleifMoney" value="${maxReleifMoney}"/>
       <input type="hidden" name="flowId"/>
      <input type="hidden" name="ruleInMaxReliefAmount" value="${ruleInMaxReliefAmount}"/>
      <input type="hidden" name="specialReliefFlag" value="${specialReliefFlag}"/>
      <input type="hidden" name="repayLevel" value="${repaymentLevel}"/>
      <table cellpadding="5"  border="0" >
        <tr>
          <td width="300px" class="">客户姓名<input  readonly="readonly" class="ar_input" name="name" value="${vLoanInfo.personInfo.name}"/></td>
          <td width="300px" class="">证件类型<input  readonly="readonly" class="ar_input" name="idType" value="身份证"/></td>
          <td width="300px" class="">证件号码<input  readonly="readonly" class="ar_input" name="idnum" value="${vLoanInfo.personInfo.idnum}"/></td>
        </tr>
        <tr>
          <td width="" class="">合同编号<input  readonly="readonly" class="ar_input" name="contractNum" value="${vLoanInfo.contractNum}"/></td>
          <td width="" class="">借款产品<input  readonly="readonly" class="ar_input" name="loanType" value="${vLoanInfo.loanType}"/></td>
          <td width="" class="">借款额度<input  readonly="readonly" class=" ar_input" name="money" data-options="precision:2" value="${vLoanInfo.money}"/></td>
        </tr>
        <tr>
          <td width="" class="">借款期数<input  readonly="readonly" class="ar_input" name="time" value="${vLoanInfo.time}"/></td>
          <td width="" class="">放款日期<input  readonly="readonly" class="ar_input" name="grantMoneyDate" value="<fmt:formatDate value='${vLoanInfo.grantMoneyDate}' pattern='yyyy-MM-dd'/>"/></td>
          <td width="" class="">月还款额<input  readonly="readonly" class="ar_input" name="returnem"  data-options="precision:2" value="${returnem}"/></td>
        </tr>
        <tr>
          <td width="" class="">已还期数<input  readonly="readonly" class="ar_input" name="repayTime" value="${repayTime}"/></td>
          <td width="" class="">已还金额<input  readonly="readonly" class="ar_input" name="alreadyRepayAmount" data-options="precision:2" value="${repayAmount}"/></td>
          <td width="" class="">剩余本金<input  readonly="readonly" class="ar_input" name="residualPactMoney" data-options="precision:2" value="${vLoanInfo.residualPactMoney}"/></td>
        </tr>
        <tr>
          <td width="" class="">历史逾期次数<input style="width:140px" readonly="readonly" class="ar_input" name="overdueTime" value="${overdueTime}"/></td>
          <td width="" class="">历史减免次数<input style="width:140px" readonly="readonly" class="ar_input" name="reliefTime" value="${reliefTime}"/></td>
          <td width="" class="">历史减免金额<input style="width:140px" readonly="readonly" class="ar_input" name="reliefAmount" data-options="precision:2" value="${reliefAmount}"/></td>
        </tr>
        <tr>
          <td width="" class="">逾期起始日<input style="width:155px" readonly="readonly" class="ar_input" name="overDueDate" value="<fmt:formatDate value='${offerRepayInfo.overDueDate}' pattern='yyyy-MM-dd'/>"/></td>
          <td width="" class="">还款等级<input  readonly="readonly" class="ar_input" name="repaymentLevel" value="${repaymentLevel}"/></td>
          <td width="" class="">减免类型<input  readonly="readonly" class="ar_input" name="reliefType" value="${reliefTypeEnum.value}"/></td>
        </tr>
        <tr>
          <td width="" class="">罚息起始日<input style="width:155px" readonly="readonly" class="ar_input" name="fineDate" value="<fmt:formatDate value='${offerRepayInfo.fineDate}' pattern='yyyy-MM-dd'/>"/></td>
          <td width="" class="">罚息天数<input  readonly="readonly" class="ar_input" name="fineDay" value="${offerRepayInfo.fineDay}"/></td>
          <td width="" class="">罚息金额<input  readonly="readonly" class="ar_input thousand" name="fine"  data-options="precision:2" value="${offerRepayInfo.fine}"/></td>
        </tr>
        <tr>
          <td width="" class="">申请减免金额<span id="applyReliefSpan" style="margin-left:10px;"><input style="width:140px" class="ar_input easyui-numberbox"  name="applyReliefMoney" id="applyReliefMoney" data-options="precision:2,decimalSeparator:'.',prefix:'',required:true" value=""/></span></td>
          <td width="" class="">当前应还<input  readonly="readonly" class="ar_input" name="currAllAmount" value="${offerRepayInfo.currAllAmount}"/></td>
          <td width="" class="">减免后应还<input style="width:155px" readonly="readonly" class="ar_input" data-options="precision:2" name="afterReliefAmount" id="afterReliefAmount" value=""/></td>
        </tr>
        <tr>
          <td width="" colspan="3" class="">减免原因<span style="margin-left:10px;"><input class="easyui-textbox" name="memo" data-options="multiline:true"
                                                       style="width: 730px;height:60px;"></span></td>
        </tr>
<%--        <tr>
          <td colspan="3" class="">
            <div>审批流程
            <span style="margin-left:10px;">
            <c:forEach var="flowNode" items="#{flowNodeList}">
              <c:choose>
                <c:when test="${flowNode.stopNodeId == '0'}">
                  ${flowNode.startNodeName}
                </c:when>
                <c:otherwise>
                  ${flowNode.startNodeName}→
                </c:otherwise>
              </c:choose>
            </c:forEach>
            </span>
            </div>
          </td>
        </tr>--%>
        <tr>
          <td colspan="3" class="">
            <div  style="margin-bottom: 5px;" id = "showFlowChart">
              <div style="position:absolute;width: 70px;">审批流程</div>
            <div style="margin-left:70px;width: 85%" id="approveFlowChart" >
            </div>
            </div>
          </td>
        </tr>
      </table>
    </form>
    <div style="text-align:center;padding-top:15px;">
      <a href="javascript:void(0)" id="repaySubmitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"  style="margin-right:15px;">提交</a>
      <a href="javascript:void(0)" id="repayCloseBut" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" >关闭</a>
    </div>
</div>
</body>
</html>