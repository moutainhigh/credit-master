package com.zdmoney.credit.offer.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.dao.pub.IEarlyRepayCalculateDao;
import com.zdmoney.credit.bsyh.dao.pub.IRepayBusLogDao;
import com.zdmoney.credit.bsyh.domain.EarlyRepayCalculate;
import com.zdmoney.credit.bsyh.domain.RepayBusLog;
import com.zdmoney.credit.bsyh.vo.BsyhSpecialRepayVo;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.debit.service.pub.IDebitBaseInfoService;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100009Vo;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100017Vo;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100009Vo.Item;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanBsbMappingService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferNum;
import com.zdmoney.credit.offer.service.pub.IOfferNumService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Controller
@RequestMapping ("/applyBsyh")
public class ApplyBsyhDeductController extends BaseController {
	protected static Log logger = LogFactory.getLog(ApplyBsyhDeductController.class);
	 @Autowired
	    private ILoanSpecialRepaymentService loanSpecialRepaymentService;
	    @Autowired
	    ILoanBaseService loanBaseService;
	    @Autowired
	    IPersonInfoService personInfoService;
	    @Autowired
	    IAfterLoanService afterLoanService;
	    @Autowired
	    ILoanBsbMappingService loanBsbMappingService;
	    @Autowired
	    ISequencesService sequencesServiceImpl;
	    @Autowired
	    IRepayBusLogDao repayBusLogDao;
	    @Autowired
	    @Qualifier("loanSpecialRepaymentServiceImpl")
	    ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
	    @Value("${bstogateway.interface.url}")
	    public String bstogatewayInterfaceUrl;
	    @Autowired
	    IEarlyRepayCalculateDao earlyRepayCalculateDao;
	    @Autowired
	    IVLoanInfoService vLoanInfoService;
	    @Autowired
	    IDebitBaseInfoService debitBaseInfoService;
	    @Autowired
	    IDebitTransactionService debitTransactionService;
	

	@RequestMapping ("/applyBsyhDeduct")
	@ResponseBody
	public String applyBsyhDeduct(HttpServletRequest request,HttpServletResponse response){
		ResponseInfo responseInfo = null;
    	try { 
    			long loanId = Long.parseLong(request.getParameter("loanId"));
    			VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId); 
    			if(loanInfo!=null){
    				//调包银提前结清试算接口
    				Bsb100017Vo bsb100017Vo = new Bsb100017Vo();
    				bsb100017Vo.setOrderNo(loanBsbMappingService.getByLoanId(loanId).getOrderNo());//借据号
    				bsb100017Vo.setProdSubNo("400001");//产品小类编号
    				bsb100017Vo.setTxncd("BYXY0017");
    				JSONObject jsonObj17 = GatewayUtils.callCateWayInterface(bsb100017Vo, GatewayFuncIdEnum.包商银行提前结清试算接口.getCode());//调取网关接口
    				String respcd17 = jsonObj17.getJSONObject("data").getString("respcd");//返回码
    				String resptx17 = jsonObj17.getJSONObject("data").getString("resptx");//返回信息
    				logger.info("☆☆☆☆☆☆☆☆☆提前结清试算结果:"+jsonObj17.toJSONString());
    				if("0000".equals(respcd17)){
    					EarlyRepayCalculate earlyRepayCalculate = new EarlyRepayCalculate();
    					//保存包银试算数据
    					String orderNo = jsonObj17.getJSONObject("data").getString("orderNo");//还款申请业务流水号
    					BigDecimal installTotalAmt = jsonObj17.getJSONObject("data").getBigDecimal("installTotalAmt");
    					BigDecimal repayBaseAmt = jsonObj17.getJSONObject("data").getBigDecimal("repayBaseAmt");
    					BigDecimal repyam = jsonObj17.getJSONObject("data").getBigDecimal("repyam");
    					BigDecimal installTotalInterest = jsonObj17.getJSONObject("data").getBigDecimal("installTotalInterest");
    					BigDecimal disCountInterest = jsonObj17.getJSONObject("data").getBigDecimal("disCountInterest");
    					Long repayPeriod = jsonObj17.getJSONObject("data").getLong("repayPeriod");
    					Long remainRepayTimes = jsonObj17.getJSONObject("data").getLong("remainRepayTimes");
    					BigDecimal liqdaRatio = jsonObj17.getJSONObject("data").getBigDecimal("liqdaRatio");
    					BigDecimal liqdaAmount = jsonObj17.getJSONObject("data").getBigDecimal("liqdaAmount");
    					BigDecimal earlyRepaymentAmt = jsonObj17.getJSONObject("data").getBigDecimal("earlyRepaymentAmt");
    					
    					earlyRepayCalculate.setId(sequencesServiceImpl.getSequences(SequencesEnum.EARLY_REPAY_CALCULATE));
    					earlyRepayCalculate.setOrderNo(orderNo);
    					earlyRepayCalculate.setRespcd(respcd17);
    					earlyRepayCalculate.setResptx(resptx17);
    					earlyRepayCalculate.setInstallTotalAmt(installTotalAmt);
    					earlyRepayCalculate.setRepayBaseAmt(repayBaseAmt);
    					earlyRepayCalculate.setRepyam(repyam);
    					earlyRepayCalculate.setInstallTotalInterest(installTotalInterest);
    					earlyRepayCalculate.setDisCountInterest(disCountInterest);
    					earlyRepayCalculate.setRepayPeriod(repayPeriod);
    					earlyRepayCalculate.setRemainRepayTimes(remainRepayTimes);
    					earlyRepayCalculate.setLiqdaRatio(liqdaRatio);
    					earlyRepayCalculate.setLiqdaAmount(liqdaAmount);
    					earlyRepayCalculate.setEarlyRepaymentAmt(earlyRepaymentAmt);
    					earlyRepayCalculateDao.insert(earlyRepayCalculate);
    				}
    				//申请包银提前一次性结清
    				Bsb100009Vo bsb100009Vo = getBsb100009Vo (loanId);   
    				bsb100009Vo.setTrantype("04");//交易类型提前结清
    				JSONObject jsonObj = GatewayUtils.callCateWayInterface(bsb100009Vo, GatewayFuncIdEnum.包商银行还款.getCode());//调取网关接口
    				logger.info("☆☆☆☆☆☆☆☆包银提前结清申请：LoanId:"+loanId+"☆☆☆☆☆☆☆☆☆申请结果:"+jsonObj.toJSONString());
    				String respcd = jsonObj.getJSONObject("data").getString("respcd");//返回码
    				String resptx = jsonObj.getJSONObject("data").getString("resptx");//返回信息
    				String repayBusNumber = jsonObj.getJSONObject("data").getString("repayBusNumber");//还款申请业务流水号
    				if("0500".equals(respcd)){//申请成功，更改申请状态
    					//插入包银还款日志表
    					RepayBusLog repayBusLog = new RepayBusLog();
    					repayBusLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.REPAY_BUS_LOG));
    					repayBusLog.setLoanId(loanId);
    					repayBusLog.setCurrentTerm(bsb100009Vo.getPayList().get(0).getPeriod());
    					repayBusLog.setApplyState("t");//申请成功
    					repayBusLog.setReternMsg(resptx);
    					repayBusLog.setRespCd(respcd);
    					repayBusLog.setRepayBusNumber(repayBusNumber);
    					repayBusLog.setCreateTime(Dates.getNow());
    					repayBusLog.setApplyType((long) 2);//1申请提前扣款 2申请提前一次性结清
    					repayBusLogDao.insert(repayBusLog);
    				}else{//申请失败
    					//插入包银还款日志表
    					RepayBusLog repayBusLog = new RepayBusLog();
    					repayBusLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.REPAY_BUS_LOG));
    					repayBusLog.setLoanId(loanId);
    					repayBusLog.setCurrentTerm(bsb100009Vo.getPayList().get(0).getPeriod());
    					repayBusLog.setApplyState("f");//申请失败
    					repayBusLog.setReternMsg(resptx);
    					repayBusLog.setRespCd(respcd);
    					repayBusLog.setRepayBusNumber(repayBusNumber);
    					repayBusLog.setCreateTime(Dates.getNow());
    					repayBusLog.setApplyType((long) 2);//1申请提前扣款 2申请提前一次性结清
    					repayBusLogDao.insert(repayBusLog);
    				}      
    				// 正常返回
					responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_SUCCESS.getCode(),"操作成功");
    			}else {
    				responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),"loanId："+loanId+"不存在！");
				}
		} catch (Exception e) {
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆申请包银提前一次性结清异常：" + e.getMessage(),e);
			responseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
		}
		return toResponseJSON(responseInfo);
	}
	/**
     * 封装包银当期提前还款参数
     * @param id
     * @return
     * @throws ParameterException 
     */
    private Bsb100009Vo getBsb100009Vo (Long id){
    	Bsb100009Vo bsb100009Vo=new Bsb100009Vo();
		LoanBase loanBase = loanBaseService.findLoanBase(id);
		PersonInfo personInfo = personInfoService.findById(loanBase.getBorrowerId());//获取客户信息
		List<LoanRepaymentDetail> repayList=afterLoanService.getAllInterestOrLoan(Dates.getCurrDate(), id);//获取到当前期数的明细list
		LoanRepaymentDetail currRepaymentDetail = repayList.get(repayList.size()-1);//获取最后一期，即为当前期数
		
		bsb100009Vo=new Bsb100009Vo();
		List<Item> payList = new ArrayList<Item>();
		Item item = new Item();
		item.setOrderNo(loanBsbMappingService.getByLoanId(id).getOrderNo());
		item.setPeriod(currRepaymentDetail.getCurrentTerm().toString());
		item.setScheduleTotal(currRepaymentDetail.getReturneterm().toString());
		payList.add(item);
		bsb100009Vo.setPayList(payList);//还款列表
		bsb100009Vo.setIdNo(personInfo.getIdnum());//证件号码
		bsb100009Vo.setCustName(personInfo.getName());//客户姓名
		bsb100009Vo.setDebitType("D01");//扣款类型  D01:客户银行卡，证大必传
		bsb100009Vo.setIdType("01");//证件类型 01身份证
		bsb100009Vo.setTrantype("02");//交易类型 01:违约，02:当期提前还款，03:部分提前还款，04:提前结清
		bsb100009Vo.setTxncd("BYXY0009");
		bsb100009Vo.setMobNo(personInfo.getMphone());//手机号码
		bsb100009Vo.setCallbackUrl(bstogatewayInterfaceUrl);//还款结果通知接口的回调地址
    	return bsb100009Vo;
    }
}
