package com.zdmoney.credit.api.app.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.api.app.vo.ContractMoneyRequest;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStatueLufaxEnum;
import com.zdmoney.credit.common.constant.grant.FinanceGrantEnum;
import com.zdmoney.credit.common.constant.lufax.LufaxConst;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.core.LoanVo;
import com.zdmoney.credit.core.calculator.base.CalculatorBase;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.debit.service.pub.IRechargeSearchService;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100003Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100007Vo;
import com.zdmoney.credit.framework.vo.lufax.output.Lufax100007OutputVo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800021Vo;
import com.zdmoney.credit.ljs.service.pub.ILoanStatusLufaxService;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBaseGrantService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.IProdCreditProductTermService;


/**
 * 陆金所相关接口
 * @author 10098  2017年4月19日 上午10:34:14
 */
@Controller
@RequestMapping("/lufax")
public class LufaxController {

	private static Logger log = LoggerFactory.getLogger(LufaxController.class);
	
	@Autowired
	IProdCreditProductTermService prodCreditProductTermServiceImpl;
	
	@Autowired
	IRechargeSearchService rechargeSearchServiceImpl;
	
	@Autowired
    IFinanceCoreService financeCoreService;
	
	@Autowired
    IFinanceGrantService financeGrantService;
	
	@Autowired
    ILoanStatusLufaxService loanStatusLufaxService;
	
	@Autowired
	ILoanCoreService loanCoreService;
	@Autowired
	ILoanBaseGrantService loanBaseGrantService;
	@Autowired
	IVLoanInfoService vInfoService;
	private static final String SUCCESS_RES = "0000";
	
	private static final String SUC_8888_RES = "8888";
	/**
	 * 根据申请金额 获取 合同金额
	 * @param request
	 * @param response
	 * @param contractMoney
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getContractMoney", method= RequestMethod.POST, consumes="application/json")
	public String getContractMoney(HttpServletRequest request, HttpServletResponse response,@RequestBody ContractMoneyRequest contractMoney){
		AttachmentResponseInfo<Map<?,?>> attachmentResponseInfo = null;
		log.info("调用获取合同金额接口，请求参数："+JSONUtil.toJSON(contractMoney));
		try{
			Assert.notNullAndEmpty(contractMoney.getContractSource(), "合同资源");
			Assert.notNullAndEmpty(contractMoney.getProdName(), "产品名称");
			Assert.notNullAndEmpty(contractMoney.getRequestMoney(), "申请金额");
			Assert.notNullAndEmpty(contractMoney.getTerm(), "产品期限");
			ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(contractMoney.getTerm(),contractMoney.getProdName(),contractMoney.getContractSource());
			Assert.notNull(prodCreditProductTerm, ResponseEnum.FULL_MSG, "未找到产品信息");
			FundsSourcesTypeEnum fundsSourcesTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, contractMoney.getContractSource(), "");
			CalculatorBase alculatorBase = CalculatorFactoryImpl.createCalculatorBase(fundsSourcesTypeEnum);
			BigDecimal pactMoney = alculatorBase.pactMoney(contractMoney.getRequestMoney(), prodCreditProductTerm);
			attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.SYS_SUCCESS);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pactMoney", pactMoney);
			attachmentResponseInfo.setAttachment(map);
		}catch(PlatformException p){
			log.error("调用获取合同金额接口",p);
			attachmentResponseInfo = new AttachmentResponseInfo<>(p.getResponseCode(), p.getMessage());
		}catch(Exception e){
			log.error("调用获取合同金额接口",e);
			attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG,"获取合同金额异常");
		}
		log.info("获取合同金额，返回结果："+JSONUtil.toJSON(attachmentResponseInfo));
		return JSONUtil.toJSON(attachmentResponseInfo);
	}
	
	/**
	 * 陆金所 签约合同时  调用推送送还款计划和资金分配计划接口
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pushRepayPlanAndFeePlan", method=RequestMethod.POST, consumes="application/json")
	public String pushRepayPlanAndFeePlan(LoanVo params, HttpServletRequest request, HttpServletResponse response){
		AttachmentResponseInfo<Map<?,?>> attachmentResponseInfo = null;
		log.info("调用推送还款计划和资金分配接口，请求参数：");
		try{
			if(params == null || Strings.isEmpty(params.getAppNo())){
				throw new PlatformException(ResponseEnum.FULL_MSG, "调用推送还款计划和资金分配接口,缺少必要参数appNo");
			}
			Assert.notNullAndEmpty(params.getProductType(), "调用推送还款计划和资金分配接口,缺少必要参数productType");
			Assert.notNullAndEmpty(params.getProductNo(), "调用推送还款计划和资金分配接口,缺少必要参数productNo");
			Lufax100007Vo lufax100007Vo = new Lufax100007Vo();
			loanCoreService.getRepayPlanAndFeePlay4Lufax(params,lufax100007Vo);
			JSONObject json = GatewayUtils.callCateWayInterface(lufax100007Vo, GatewayFuncIdEnum.陆金所证大推送还款计划和资金分配计划.getCode());
			Lufax100007OutputVo lufax100007OutputVo = JSONObject.toJavaObject(json, Lufax100007OutputVo.class);
			if(SUCCESS_RES.equals(lufax100007OutputVo.getReturnCode())){
				attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.SYS_SUCCESS);
			}else{
				attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, "陆金所返回错误——"+lufax100007OutputVo.getReturnMsg());
			}
		}catch(PlatformException p){
			attachmentResponseInfo = new AttachmentResponseInfo<>(p.getResponseCode(), p.getMessage());
		}catch(Exception e){
			attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG,"调用推送还款计划和资金非配接口异常");
		}
		log.info("调用推送还款计划和资金非配接口，返回结果："+JSONUtil.toJSON(attachmentResponseInfo));
		return JSONUtil.toJSON(attachmentResponseInfo);
	}
	
	/**
	 * 陆金所—— 财务审核时 调用合同确认接口
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/confirmContract" , method= RequestMethod.POST, consumes="application/json")
	public String confirmContract(@RequestBody LoanVo params, HttpServletRequest request, HttpServletResponse response){
		AttachmentResponseInfo<Map<?,?>> attachmentResponseInfo = null;
		log.info("调用合同确认接口，请求参数：" + JSON.toJSONString(params));
		try{
			Lufax100003Vo lufax100003Vo = new Lufax100003Vo();
			if(params == null || Strings.isEmpty(params.getAppNo())){
				throw new PlatformException(ResponseEnum.FULL_MSG, "调用合同确认接口,缺少必要参数appNo");
			}
			loanCoreService.getConfirmContractData4Lufax(params,lufax100003Vo);
			JSONObject json = GatewayUtils.callCateWayInterface(lufax100003Vo, GatewayFuncIdEnum.陆金所确认合同接口.getCode());
			if(SUCCESS_RES.equals(json.get("ret_code")) || SUC_8888_RES.equals(json.get("ret_code"))){
				attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.SYS_SUCCESS);
			}else{
				attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, "网关调陆金所确认合同接口，"+json.get("ret_msg"));
			}
		}catch(PlatformException p){
			log.error("调用合同确认接口",p);
			attachmentResponseInfo = new AttachmentResponseInfo<>(p.getResponseCode(), p.getMessage());
		}catch(Exception e){
			log.error("调用合同确认接口",e);
			attachmentResponseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG,"调用合同确认接口异常");
		}
		log.info("调用合同确认接口，返回结果："+JSONUtil.toJSON(attachmentResponseInfo));
		return JSONUtil.toJSON(attachmentResponseInfo);
	}
	
	@ResponseBody
    @RequestMapping(value = "/loanResultNotify",method = RequestMethod.POST,consumes = "application/json")
    public String loanResultNotify(@RequestBody Lufax800021Vo vo) {
	    log.info("陆金所实时放款通知>>>>request message>>>>" + JSONUtil.toJSON(vo));
	    Map<String,Object> info = new HashMap<String, Object>();
	    /*String product_type = vo.getProduct_type();*///业务类型
	    String anshuo_loan_accept_id = vo.getAnshuo_loan_accept_id();//申请号
	    /*String lufax_loan_req_id = vo.getLufax_loan_req_id();*///借款人申请id
	    /*String withdraw_time = vo.getWithdraw_time();*///放款时间
	    String state = vo.getState();//放款结果 1-成功0-失败
	    if("0".equals(state)) state = "2";
	    String granting_message = vo.getGranting_message();//放款失败原因
        try {
            if(anshuo_loan_accept_id.indexOf(LufaxConst.ZDJR) < 0){
                throw new PlatformException(ResponseEnum.FULL_MSG, "申请件号格式不正确！");
            }
            anshuo_loan_accept_id = anshuo_loan_accept_id.replace(LufaxConst.ZDJR, "");
            VLoanInfo loan = new VLoanInfo();
            loan.setChannelPushNo(anshuo_loan_accept_id);
            List<VLoanInfo> loanInfoList = vInfoService.findListByVO(loan);
            if(CollectionUtils.isEmpty(loanInfoList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "申请件号不存在！");
            }
            VLoanInfo loanInfo = loanInfoList.get(0);
            boolean success = loanBaseGrantService.checkLoanBaseGrantStatus(loanInfo.getId(), FinanceGrantEnum.放款成功.getCode());
            if(success){
                throw new PlatformException(ResponseEnum.FULL_MSG, "该笔债权放款通知成功已发，无需再发，loanId:"+loanInfo.getId());
            }
            
            boolean flag = loanBaseGrantService.checkLoanBaseGrantStatus(loanInfo.getId(), FinanceGrantEnum.申请中.getCode());
            if(flag){
                financeGrantService.disposeFinanceGrantPushResultService(loanInfo.getContractNum(), state, granting_message);
                
                loanStatusLufaxService.addLoanStatusLufax(loanInfo.getId(), LoanStatueLufaxEnum.正常.getCode()); 
                
                info.put("ret_code", "0000");
                info.put("ret_msg", "放款成功处理完成！");
            }else{
                throw new PlatformException(ResponseEnum.FULL_MSG, "没找到状态为【申请中】 放款申请记录，loanId:"+loanInfo.getId());
            }
        } catch (PlatformException ex) {
            log.error("异常信息："+ex.getMessage(),ex);
            info.put("ret_code", "9999");
            info.put("ret_msg", ex.getMessage());
        } catch (Exception e) {
            log.error("异常信息："+e.getMessage(),e);
            info.put("ret_code", "9999");
            info.put("ret_msg", e.getMessage());
        }
        log.info("放款结果通知返回给网关的信息："+info);
        return  JSONUtil.toJSON(info);
	}
}
