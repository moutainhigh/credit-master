package com.zdmoney.credit.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.dao.pub.IEarlyRepayCalculateDao;
import com.zdmoney.credit.bsyh.dao.pub.IRepayBusLogDao;
import com.zdmoney.credit.bsyh.domain.EarlyRepayCalculate;
import com.zdmoney.credit.bsyh.domain.RepayBusLog;
import com.zdmoney.credit.bsyh.vo.BsyhSpecialRepayVo;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.IDebitBaseInfoService;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100009Vo;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100009Vo.Item;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100017Vo;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanBsbMappingService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 申请包商银行提前划扣job
 * @author YM10104
 *
 */
@Service
public class ApplyBsyhDeductJob {

    private static final Logger logger = Logger.getLogger(ApplyBsyhDeductJob.class);

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

    /**
     * 非t日
     * 将提前还款的申请，发包银进行划扣
     */
    public void applyBsyhDeduct() {
    	try {
    		logger.info("当前日期为："+ DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"，开始执行【包商银行】提前还款的申请，发包银进行划扣JOB");
    		//获取已经申请提前还款（去除 申请状态成功划扣状态是空的，申请状态成功划扣状态成功。其余都发送）
        	List<BsyhSpecialRepayVo> bsyhSpecialRepayList=loanSpecialRepaymentService.findBsyhSpecialRepay(Dates.getCurrDate());
        	if(null!=bsyhSpecialRepayList && bsyhSpecialRepayList.size()>0){
        		for (int i = 0; i < bsyhSpecialRepayList.size(); i++) {
    				BsyhSpecialRepayVo vo = bsyhSpecialRepayList.get(i);
    				VLoanInfo loanInfo = vLoanInfoService.findByLoanId(vo.getLoanId());   				
    				if(loanInfo.getPromiseReturnDate().intValue()!=(Dates.getCurrentDay())){//非t日进行申请
    					Bsb100009Vo bsb100009Vo = getBsb100009Vo(vo.getLoanId());//封装参数信息
    					//创建第三方划扣信息
    					DebitBaseInfo offer = debitBaseInfoService.createThirdOffer(loanInfo, new BigDecimal(bsb100009Vo.getPayList().get(0).getScheduleTotal()));
    					//创建第三方划扣流水
    					DebitTransaction transaction = debitTransactionService.buildDebitTransaction(offer);
    					JSONObject jsonObj = GatewayUtils.callCateWayInterface(bsb100009Vo, GatewayFuncIdEnum.包商银行还款.getCode());
    					logger.info("☆☆☆☆☆☆☆☆包银提前还款申请：LoanId:"+vo.getLoanId()+"☆☆☆☆☆☆☆☆☆申请结果:"+jsonObj.toJSONString());
    					String respcd = jsonObj.getJSONObject("data").getString("respcd");//返回码
    					String resptx = jsonObj.getJSONObject("data").getString("resptx");//返回信息
    					String repayBusNumber = jsonObj.getJSONObject("data").getString("repayBusNumber");//还款申请业务流水号
    					if("0500".equals(respcd)){//申请成功，更改申请状态 
    						//插入包银还款日志表
    						RepayBusLog repayBusLog = new RepayBusLog();
    						repayBusLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.REPAY_BUS_LOG));
    						repayBusLog.setLoanId(vo.getLoanId());
    						repayBusLog.setCurrentTerm(bsb100009Vo.getPayList().get(0).getPeriod());
    						repayBusLog.setApplyState("t");//申请成功
    						repayBusLog.setReternMsg(resptx);
    						repayBusLog.setRespCd(respcd);
    						repayBusLog.setRepayBusNumber(repayBusNumber);
    						repayBusLog.setCreateTime(Dates.getNow());
    						repayBusLog.setApplyType((long) 1);//1申请提前扣款 2申请提前一次性结清
    						repayBusLogDao.insert(repayBusLog);
    						logger.info("债权去向为包商银行的债权申请【成功】记录："+ JSONObject.toJSONString(repayBusLog) + "已插入REPAY_BUS_LOG表！！！");
    						debitTransactionService.updateOfferStateToYibaopan(transaction,repayBusNumber);
    					}else{//申请失败
    						//插入包银还款日志表
    						RepayBusLog repayBusLog = new RepayBusLog();
    						repayBusLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.REPAY_BUS_LOG));
    						repayBusLog.setLoanId(vo.getLoanId());
    						repayBusLog.setCurrentTerm(bsb100009Vo.getPayList().get(0).getPeriod());
    						repayBusLog.setApplyState("f");//申请失败
    						repayBusLog.setReternMsg(resptx);
    						repayBusLog.setRespCd(respcd);
    						repayBusLog.setRepayBusNumber(repayBusNumber);
    						repayBusLog.setCreateTime(Dates.getNow());
    						repayBusLog.setApplyType((long) 1);//1申请提前扣款 2申请提前一次性结清
    						repayBusLogDao.insert(repayBusLog);
    						logger.info("债权去向为包商银行的债权申请【失败】记录："+ JSONObject.toJSONString(repayBusLog) + "已插入REPAY_BUS_LOG表！！！");
    						debitTransactionService.updateErrorMsgNow(respcd, resptx, offer, transaction);
    					}
    					
    				}
    			}
        	}else{
        		logger.info("☆☆☆☆☆☆☆☆没有包银提前还款申请");
        	}
		} catch (Exception e) {
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆申请包银进行划扣异常：" + e.getMessage(),e);
		}
    	logger.info("【包商银行】提前还款的申请，发包银进行划扣JOB!!!");
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
    /**
     * T日凌晨00:10
     * 将申请提前一次性结清的贷款发送包银
     */
    public void applyBsyhDeductAll() {
    	try {
    		//获取已经申请提前一次性结清，来源包银，状态预结清的贷款
        	List<BsyhSpecialRepayVo> bsyhSpecialRepayAllList=loanSpecialRepaymentService.findBsyhSpecialRepayAll();
        	if(null!=bsyhSpecialRepayAllList && bsyhSpecialRepayAllList.size()>0){
        		for (int i = 0; i < bsyhSpecialRepayAllList.size(); i++) {
        			BsyhSpecialRepayVo vo = bsyhSpecialRepayAllList.get(i);
        			VLoanInfo loanInfo = vLoanInfoService.findByLoanId(vo.getLoanId()); 
        			if(loanInfo.getPromiseReturnDate().intValue()==(Dates.getCurrentDay())){//t日进行申请
        				//调包银提前结清试算接口
        				Bsb100017Vo bsb100017Vo = new Bsb100017Vo();
        				bsb100017Vo.setOrderNo(loanBsbMappingService.getByLoanId(vo.getLoanId()).getOrderNo());//借据号
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
        				Bsb100009Vo bsb100009Vo = getBsb100009Vo (vo.getLoanId());   
        				bsb100009Vo.setTrantype("04");//交易类型提前结清
        				JSONObject jsonObj = GatewayUtils.callCateWayInterface(bsb100009Vo, GatewayFuncIdEnum.包商银行还款.getCode());//调取网关接口
        				logger.info("☆☆☆☆☆☆☆☆包银提前结清申请：LoanId:"+vo.getLoanId()+"☆☆☆☆☆☆☆☆☆申请结果:"+jsonObj.toJSONString());
        				String respcd = jsonObj.getJSONObject("data").getString("respcd");//返回码
        				String resptx = jsonObj.getJSONObject("data").getString("resptx");//返回信息
        				String repayBusNumber = jsonObj.getJSONObject("data").getString("repayBusNumber");//还款申请业务流水号
        				if("0500".equals(respcd)){//申请成功，更改申请状态
        					//插入包银还款日志表
        					RepayBusLog repayBusLog = new RepayBusLog();
        					repayBusLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.REPAY_BUS_LOG));
        					repayBusLog.setLoanId(vo.getLoanId());
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
        					repayBusLog.setLoanId(vo.getLoanId());
        					repayBusLog.setCurrentTerm(bsb100009Vo.getPayList().get(0).getPeriod());
        					repayBusLog.setApplyState("f");//申请失败
        					repayBusLog.setReternMsg(resptx);
        					repayBusLog.setRespCd(respcd);
        					repayBusLog.setRepayBusNumber(repayBusNumber);
        					repayBusLog.setCreateTime(Dates.getNow());
        					repayBusLog.setApplyType((long) 2);//1申请提前扣款 2申请提前一次性结清
        					repayBusLogDao.insert(repayBusLog);
        				}      				
        			}
				}
        	}else{
        		logger.info("☆☆☆☆☆☆☆☆☆包银没有提前一次性结清申请");
        	}
		} catch (Exception e) {
			System.out.println(e);
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆申请包银提前一次性结清异常：" + e.getMessage(),e);
		}
    }
}
