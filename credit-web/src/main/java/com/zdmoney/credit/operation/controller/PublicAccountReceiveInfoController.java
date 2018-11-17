package com.zdmoney.credit.operation.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.PublicAccountSystemTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfo;
import com.zdmoney.credit.operation.service.pub.IBasePublicAccountInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Controller
@RequestMapping("/operation")
public class PublicAccountReceiveInfoController extends BaseController {
    
    /**
     * 调用接口返回正常状态值
     */
    private static final String SUCCESS = "SUCCESS";
    
    /***
     * 角色名字
     */
    private static final String ROLE_NAME = "门店|总部-30天外认领";
    
    /***
     * 角色名字2
     */
    private static final String ROLE_NAME_TWO = "催收-全营业部停催";
    
    @Autowired
    private IBasePublicAccountInfoService basePublicAccountInfoService;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private ILoanLogService loanLogService;
    
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
    @Autowired
    private IPersonInfoService personInfoService;
    
    @Autowired
    private ILoanFeeInfoService loanFeeInfoService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired 
	ILoanTransferInfoService loanTransferInfoServiceImpl;
    /**
     * 领取还款记录查询
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchPublicAccountReceiveInfo")
    @ResponseBody
    public String searchPublicAccountReceiveInfo(int rows, int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "对公账户还款", "查询对公账户还款领取页面信息");
        Map<String,Object> params = new HashMap<String,Object>();
        // 借款人姓名
        String name = request.getParameter("name");
        if(Strings.isNotEmpty(name)){
            params.put("name", name);
        }
        // 联系电话
        String contractPhone = request.getParameter("contractPhone");
        if(Strings.isNotEmpty(contractPhone)){
            params.put("contractPhone", contractPhone);
        }
        // 身份证号码
        String idNum = request.getParameter("idNum");
        if(Strings.isNotEmpty(idNum)){
            params.put("idNum", idNum);
        }
        //合同编号
        String contractNum = request.getParameter("contractNum");
        if(Strings.isNotEmpty(contractNum)){
            params.put("contractNum", contractNum);
        }
        // 借款状态
        String[] loanStates = {LoanStateEnum.正常.getValue(),LoanStateEnum.逾期.getValue()};
        params.put("loanStates", loanStates);
        User user = UserContext.getUser();
        params.put("orgCode", user.getOrgCode());
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        params.put("pager", pager);
        // 调用Service层查询客户债权信息 
        pager = basePublicAccountInfoService.findWithPgByMap(params);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 确认领取
     * @param id
     * @param loanId
     * @param borrowerName
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/confirmReceive")
    @ResponseBody
    public String confirmReceive(Long id,Long loanId,HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.更新, "对公账户还款", "对公账户还款确认领取处理");
        User user = UserContext.getUser();
        // 如果没有领取权限
        if(!user.ifAnyGranted("/operation/confirmReceive")){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"您无领取操作权限！");
            return toResponseJSON(responseInfo);
        }
        
        //检查该借款是否有已转让
		boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,loanId);
		if(!flag){
			 responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),Strings.errorMsg);
	         return toResponseJSON(responseInfo);
		}
		
        // 对公还款领取状态校验，不能认领则返回提示信息
        String message = basePublicAccountInfoService.checkReceiveStatus();
        if(Strings.isNotEmpty(message)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),message);
            return toResponseJSON(responseInfo);
        }
        // 针对龙信小贷、外贸信托、外贸2，判断是否已经完成划扣服务费，如果没有，则不能认领
        if(!loanFeeInfoService.isAlreadyDebitServiceCharge(loanId)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"没有完成划扣服务费，不能认领！");
            return toResponseJSON(responseInfo);
        }
        // 查询对公账户信息
        BasePublicAccountInfo publicAccountInfo = basePublicAccountInfoService.get(id);
        if(Strings.isEmpty(publicAccountInfo)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"查询不到待认领的对公账户还款记录！");
            return toResponseJSON(responseInfo);
        }
        // 如果状态不是未认领，则不能领取
        if(!"未认领".equals(publicAccountInfo.getStatus())){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"状态不是未认领，不能领取");
            return toResponseJSON(responseInfo);
        }
        String dateStr = Dates.getDateTime(publicAccountInfo.getRepayDate(),Dates.DEFAULT_DATE_FORMAT);
        String timeStr =publicAccountInfo.getTime().trim();
        Date tradeDate = Dates.getDateByString(dateStr+" "+timeStr, Dates.DEFAULT_DATETIME_FORMAT);
        VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
        
        Date repayDate = publicAccountInfo.getRepayDate();  
        if(!user.ifAnyRole(ROLE_NAME_TWO)){//有这个角色 则可以进行认领
        	if(Dates.getAfterDays(repayDate,30).before(Dates.getCurrDate())){//该笔存款超过30天
        		if(!user.ifAnyRole(ROLE_NAME)){
        			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该笔存款超过30天，无法领取");
        			return toResponseJSON(responseInfo);     		
        		}
        	}else{//该笔存款未超过30天
        		if(user.ifAnyRole(ROLE_NAME)){
        			if(vLoanInfo.getGrantMoneyDate() != null && !tradeDate.before(vLoanInfo.getGrantMoneyDate())){//存款时间大于等于申请件的放款时间
        				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该数据在权限范围以外");
        				return toResponseJSON(responseInfo);
        			}
        		}else{
        			if(vLoanInfo.getGrantMoneyDate() != null && tradeDate.before(vLoanInfo.getGrantMoneyDate())){//存款时间小于申请件的放款时间
        				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该笔存款时间小于申请件放款时间");
        				return toResponseJSON(responseInfo);
        			}
        		}
        	}        	
        }
        // 调用车企贷和证方系统的接口（认领）
        int type = 1;
        String result = this.remoteInvoke(publicAccountInfo, type);
        if(Strings.isNotEmpty(result)){
            return result;
        }
        BasePublicAccountInfo accountInfo = new BasePublicAccountInfo();
        accountInfo.setId(id);
        accountInfo.setStatus("已认领");
        accountInfo.setRecOperatorId(new BigDecimal(user.getId()));
        accountInfo.setRecTime(new Date());
        if(Strings.isNotEmpty(loanId)){
            accountInfo.setLoanId(loanId);
        }
        try {
            String content = "认领";
            // 查询借款人信息
            PersonInfo personInfo = this.queryPersonInfo(loanId);
            if(Strings.isNotEmpty(personInfo)){
                content = content + "（"+personInfo.getName()+"："+personInfo.getIdnum()+"）";
            }
            // 日志记录
            loanLogService.createLog(publicAccountInfo.getId(), content,PublicAccountReceiveInfoController.class.getSimpleName(), "info", "对公账户认领");
            // 更新对公账户还款信息
            basePublicAccountInfoService.updateBasePublicAccountInfo(accountInfo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"对公账户"+ id +" 领取成功！");
            return toResponseJSON(responseInfo);
        } catch (Exception e) {
            logger.error("领取失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"领取失败！");
            return toResponseJSON(responseInfo);
        }
    }

    /**
     * 调用车企贷和证方系统的接口
     * @param publicAccountInfo
     * @param type
     * @return
     */
    private String remoteInvoke(BasePublicAccountInfo publicAccountInfo, int type) {
        ResponseInfo responseInfo = null;
        // 是否调用车企贷对公还款接口（t：是，f：否）
        String isCallCqdInterface = sysParamDefineService.getSysParamValue("sysCar", "isCallCqdInterface");
        if("t".equals(isCallCqdInterface)){
            // 调用车企贷对公还款更新接口
            String resultMsg = this.updateStatusToCqd(publicAccountInfo ,type , PublicAccountSystemTypeEnum.车企贷.getValue());
            if(!SUCCESS.equals(resultMsg)){
                responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "车企贷接口返回错误：" + resultMsg);
                return toResponseJSON(responseInfo);
            }
        }
        // 是否调用证方对公还款接口（t：是，f：否）
        String isCallZfInterface = sysParamDefineService.getSysParamValue("sysCar", "isCallZfInterface");
        if("t".equals(isCallZfInterface)){
            // 调用证方系统公还款更新接口
            String resultMsg = this.updateStatusToCqd(publicAccountInfo ,type , PublicAccountSystemTypeEnum.证方.getValue());
            if(!SUCCESS.equals(resultMsg)){
                responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "证方系统接口返回错误：" + resultMsg);
                return toResponseJSON(responseInfo);
            }
        }
        return null;
    }
    
    /**
     * 查询借款人信息
     * @param loanId
     * @return
     */
    private PersonInfo queryPersonInfo(Long loanId) {
        if(Strings.isEmpty(loanId)){
            return null;
        }
        // 查询债权信息
        VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
        if(Strings.isEmpty(loanInfo)){
            return null;
        }
        // 查询客户信息
        PersonInfo personInfo = personInfoService.findById(loanInfo.getBorrowerId());
        if(Strings.isEmpty(personInfo)){
            return null;
        }
        return personInfo;
    }
    
    /**
     * 撤销领取
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cancelReceive")
    @ResponseBody
    public String cancel(Long id,HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        BasePublicAccountInfo publicAccountInfo = basePublicAccountInfoService.get(id);
        if(Strings.isEmpty(publicAccountInfo)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"查询不到待认领的对公账户还款记录！");
            return toResponseJSON(responseInfo);
        }
        // 如果状态不是已认领或者渠道确认，则不能撤销
        String status = publicAccountInfo.getStatus();
        if(!"已认领".equals(status) && !"渠道确认".equals(status)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"状态不是已认领或者渠道确认，不能撤销");
            return toResponseJSON(responseInfo);
        }
        // 撤销认领
        int type = 2; 
        if ("渠道确认".equals(status)) {
            // 撤销渠道认领
            type = 4; 
        }
        // 调用车企贷和证方系统的接口（撤销领取）
        String result = this.remoteInvoke(publicAccountInfo, type);
        if(Strings.isNotEmpty(result)){
            return result;
        }
        BasePublicAccountInfo accountInfo = new BasePublicAccountInfo();
        accountInfo.setId(id);
        accountInfo.setStatus("未认领");
        accountInfo.setRecOperatorId(null);
        accountInfo.setRecTime(null);
        accountInfo.setLoanId(null);
        accountInfo.setUpdateTime(new Date());
        try {
            String content = "撤销";
            if(type==2 && Strings.isNotEmpty(publicAccountInfo.getLoanId())){
                // 查询借款人信息
                PersonInfo personInfo = this.queryPersonInfo(publicAccountInfo.getLoanId());
                if(Strings.isNotEmpty(personInfo)){
                    content = content + "（"+personInfo.getName()+"："+personInfo.getIdnum()+"）";
                }
            }
            // 日志记录
            loanLogService.createLog(publicAccountInfo.getId(), content, PublicAccountReceiveInfoController.class.getSimpleName(), "info", "对公账户撤销");
            // 更新对公账户还款信息（撤销认领）
            basePublicAccountInfoService.updateAccountInfoForCancel(accountInfo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"对公账户"+ id +" 撤销成功！");
            return toResponseJSON(responseInfo);
        } catch (Exception e) {
            logger.error("撤销失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"撤销失败！");
            return toResponseJSON(responseInfo);
        }
    }
    
    /**
     * 渠道确认
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/channelConfirm")
    @ResponseBody
    public String channelConfirm(Long id,HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        User user = UserContext.getUser();
        BasePublicAccountInfo publicAccountInfo = basePublicAccountInfoService.get(id);
        if(Strings.isEmpty(publicAccountInfo)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"查询不到待认领的对公账户还款记录！");
            return toResponseJSON(responseInfo);
        }
        // 如果状态不是未认领，则不能渠道确认
        if(!"未认领".equals(publicAccountInfo.getStatus())){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"状态不是未认领，不能渠道确认");
            return toResponseJSON(responseInfo);
        }
        // 渠道确认
        int type = 3;
        // 调用车企贷和证方系统的接口（渠道确认）
        String result = this.remoteInvoke(publicAccountInfo, type);
        if(Strings.isNotEmpty(result)){
            return result;
        }
        BasePublicAccountInfo accountInfo = new BasePublicAccountInfo();
        accountInfo.setId(id);
        accountInfo.setStatus("渠道确认");
        accountInfo.setRecOperatorId(new BigDecimal(user.getId()));
        accountInfo.setRecTime(new Date());
        try {
            // 日志记录
            loanLogService.createLog(publicAccountInfo.getId(), "渠道确认", PublicAccountReceiveInfoController.class.getSimpleName(), "info", "对公账户渠道确认");
            // 更新对公账户还款信息
            basePublicAccountInfoService.updateBasePublicAccountInfo(accountInfo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"渠道确认成功！");
            return toResponseJSON(responseInfo);
        } catch (Exception e) {
            logger.error("渠道确认失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"渠道确认失败！");
            return toResponseJSON(responseInfo);
        }
    }

    /**
     * 调用车企贷或者证方系统的接口
     * @param publicAccountInfo
     * @param type
     * @param systemType
     * @return
     */
    private String updateStatusToCqd(BasePublicAccountInfo publicAccountInfo, int type, String systemType) {
        // 当前时间
        long curTimeMS = System.currentTimeMillis();
        // 还款日期
        String repayDate = Dates.getDateTime(publicAccountInfo.getRepayDate(),"yyyy-MM-dd");
        // 还款金额
        String amount = publicAccountInfo.getAmount().toString();
        // 还款时间
        String repayTime = publicAccountInfo.getTime();
        // 对方单位
        String secondCompany = publicAccountInfo.getSecondCompany();
        if(Strings.isEmpty(secondCompany)){
            secondCompany = "";
        }
        // 是否发生了异常
        boolean isTryCatch = false;
        // 调用接口返回数据
        String result = null;
        // 查询调用车企贷或者证方更新接口的url
        String host = null;
        if(PublicAccountSystemTypeEnum.证方.getValue().equals(systemType)){
            host = sysParamDefineService.getSysParamValueCache("sysCar","zfUrl");
        }else if(PublicAccountSystemTypeEnum.车企贷.getValue().equals(systemType)){
            host = sysParamDefineService.getSysParamValueCache("sysCar","cqdUrl");
        }
        // 车企贷和证方更新对公账户状态的接口url后缀相同，host不同
        host = host + "/after/businessAccount/updateBusinessAccountStatus";
        StringBuilder url = new StringBuilder();
        url.append(host);
        url.append("?repayDate=");
        url.append(repayDate);
        url.append("&repayTime=");
        url.append(repayTime);
        url.append("&secondCompany=");
        try {
            url.append(URLEncoder.encode(secondCompany, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("编码转换异常：", e);
        }
        url.append("&type=");
        url.append(type);
        url.append("&amount=");
        url.append(amount);
        url.append("&system=");
        url.append(PublicAccountSystemTypeEnum.信贷.getValue());
        try {
            // 调用车企贷的更新接口
            result = restTemplate.postForObject(url.toString(), null,String.class);
        } catch (Exception e) {
            isTryCatch = true;
            result = e.getMessage();
            logger.error("调用车企贷或者证方系统的更新接口异常：", e);
        } finally {
            // 记录日志
            this.writeLoanLog(publicAccountInfo, type, systemType, isTryCatch, curTimeMS, result);
        }
        return result;
    }
    
    /**
     * 记录日志
     * @param publicAccountInfo
     * @param type
     * @param memo
     * @param isTryCatch
     * @param curTimeMS
     * @param result
     */
    private void writeLoanLog(BasePublicAccountInfo publicAccountInfo, int type, String systemType, boolean isTryCatch, long curTimeMS, String result){
        // 还款日期
        String repayDate = Dates.getDateTime(publicAccountInfo.getRepayDate(),"yyyy-MM-dd");
        // 还款金额
        String amount = publicAccountInfo.getAmount().toString();
        // 还款时间
        String repayTime = publicAccountInfo.getTime();
        // 对方单位
        String secondCompany = publicAccountInfo.getSecondCompany();
        if(Strings.isEmpty(secondCompany)){
            secondCompany = "";
        }
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("repayDate", repayDate);
        json.put("repayTime", repayTime);
        json.put("secondCompany", secondCompany);
        json.put("amount", amount);
        json.put("systemType", PublicAccountSystemTypeEnum.信贷.getValue());
        // 接口入参 JSON
        String params = json.toString();
        LoanLog loanLog = new LoanLog();
        loanLog.setObjectId(publicAccountInfo.getId());
        loanLog.setLogType("info");
        loanLog.setLogName(PublicAccountReceiveInfoController.class.getSimpleName());
        // 备注
        String memo = null;
        String interName = "";
        String subContent = null;
        if (type == 1) {
            interName = "认领";
        } else if (type == 2) {
            interName = "撤销";
        } else if (type == 3) {
            interName = "渠道确认";
        } else if (type == 4) {
            interName = "撤销渠道确认";
        }
        if(PublicAccountSystemTypeEnum.车企贷.getValue().equals(systemType)){
            memo = PublicAccountSystemTypeEnum.车企贷.name() + "["+ interName + "]";
            subContent = "调用"+ PublicAccountSystemTypeEnum.车企贷.name();
        }else if(PublicAccountSystemTypeEnum.证方.getValue().equals(systemType)){
            memo = PublicAccountSystemTypeEnum.证方.name() + "["+ interName + "]";
            subContent = "调用"+ PublicAccountSystemTypeEnum.证方.name();
        }
        loanLog.setMemo(memo);
        String content = subContent + interName + "接口入参：[" + params + "] ";
        if (!isTryCatch) {
            content += "出参： [" + result + "]";
        }
        content += "耗时：" + (System.currentTimeMillis() - curTimeMS) + "ms";
        loanLog.setContent(content);
        try {
            // 记录日志
            loanLogService.createLog(loanLog);
        } catch (Exception e) {
            logger.error("记录日志异常：", e);
        }
    }
}
