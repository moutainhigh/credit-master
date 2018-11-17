package com.zdmoney.credit.core.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.common.vo.core.FlowDetialsVO;
import com.zdmoney.credit.common.vo.core.RDetailVO;
import com.zdmoney.credit.common.vo.core.RepayInfoVo;
import com.zdmoney.credit.common.vo.core.RepaymentDetailParamVO;
import com.zdmoney.credit.common.vo.core.ReturnAccountCardVO;
import com.zdmoney.credit.common.vo.core.ReturnRepaymentDetailVo;
import com.zdmoney.credit.core.AccountCard;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.core.service.pub.IRepaymentDetialCoreService;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.offer.dao.pub.IOfferRepayInfoDao;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.dao.pub.IProdCreditProductInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductInfo;
import com.zdmoney.credit.zhuxue.dao.pub.IZhuxueOrganizationDao;
import com.zdmoney.credit.zhuxue.dao.pub.IZhuxueProductPlanDao;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
import com.zdmoney.credit.zhuxue.domain.ZhuxueProductPlan;

@Service
public class RepaymentDetialCoreServiceImpl implements IRepaymentDetialCoreService{
	
	@Autowired
	IOfferRepayInfoDao offerRepayInfoDao;
	@Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;
	@Autowired
	IOfferFlowDao offerFlowDao;
	@Autowired
	ILoanBaseDao loanBaseDao;//债权基本信息操作DAO
	@Autowired
	IPersonInfoDao personInfoDao;//人员信息操作DAO
	@Autowired
	ILoanInitialInfoDao loanInitialInfoDao;
	@Autowired
	IProdCreditProductInfoDao prodCreditProductInfoDao;
	@Autowired
	IZhuxueProductPlanDao zhuxueProductPlanDao;
	@Autowired
	IZhuxueOrganizationDao zhuxueOrganizationDao;
	@Autowired
	IAfterLoanService afterLoanService;
	@Autowired
	IVLoanInfoService vLoanInfoServiceImpl;
	
	//交易类型名称
	private static final Map<String, String> tradeMap = new HashMap<String, String>();
    static {
    	tradeMap.put(Const.TRADE_CODE_NORMAL.toString(),"账户还款");
    	tradeMap.put(Const.TRADE_CODE_IN.toString(),"账户存款");
    	tradeMap.put(Const.TRADE_CODE_OUT.toString(),"账户取款");
    	tradeMap.put(Const.TRADE_CODE_STDIN.toString(),"学生向保证金账户存款");
    	tradeMap.put(Const.TRADE_CODE_STDOUT.toString(),"学生从保证金账户取款");
    	tradeMap.put(Const.TRADE_CODE_ORGIN.toString(),"保证金账户存款");
    	tradeMap.put(Const.TRADE_CODE_ORGOUT.toString(),"保证金账户取款");
    	tradeMap.put(Const.TRADE_CODE_SETTLE.toString(),"结清处理");
    	tradeMap.put(Const.TRADE_CODE_ONEOFF.toString(),"一次性（提前还款）");
    	tradeMap.put(Const.TRADE_CODE_OPENACC.toString(),"个贷开户");
    	tradeMap.put(Const.TRADE_CODE_OPENACC_ASC.toString(),"助学贷开户");
    	tradeMap.put(Const.TRADE_CODE_SPECIA.toString(),"减免特殊还款");
    	tradeMap.put(Const.TRADE_CODE_MARGINREAY.toString(),"保证金还款");
    }
	
	@Override
	public Map<String, Object> queryRepayInfoByLoanId(FinanceVo params) {
		
		/*def searchClosure = {
            eq('riAccount',params.loanId)
            //not{'in'('riTradeCode',[Const.TRADE_CODE_OPENACC.toString(),Const.TRADE_CODE_OPENACC_ASC.toString(),Const.TRADE_CODE_DRAWRISK.toString(),Const.TRADE_CODE_DRAWRISK_STUDENT.toString()]) }
            not{'in'('riTradeCode',[Const.TRADE_CODE_OPENACC_ASC.toString(),Const.TRADE_CODE_DRAWRISK.toString(),Const.TRADE_CODE_DRAWRISK_STUDENT.toString()]) }//Const.TRADE_CODE_OPENACC.toString()去掉了（2015-07-22）
            order('riTradeNo','asc')
        }*/
		
		Pager pager = new Pager();
		pager.setSidx("TRADE_NO");
		pager.setSort(Pager.DIRECTION_ASC);
		
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("pager", pager);
		paramMap.put("loanId", params.getLoanId());
		paramMap.put("notEqTradeCode", new String[]{Const.TRADE_CODE_OPENACC_ASC, Const.TRADE_CODE_DRAWRISK, Const.TRADE_CODE_DRAWRISK_STUDENT});
		
		Pager returnPager =  offerRepayInfoDao.getOfferRepayInfoWithPg(paramMap);
		@SuppressWarnings("unchecked")
		List<OfferRepayInfo> repayInfoList = returnPager.getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
        
		map.put("total", repayInfoList != null ? repayInfoList.size() : 0);
        map.put("repayInfoList", repayInfoList);
        return  map;
	}

	/**
	 * 还款计划查询
	 * @param paramVo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String,Object> queryRepaymentDetail(Map<String,Object> paramMap) {
		List<RDetailVO> resultList=new ArrayList<RDetailVO>();
		Pager pager=loanRepaymentDetailDao.getRepaymentDatailWithPg(paramMap);
		
		List rdList=pager.getResultList();
		if(CollectionUtils.isNotEmpty(rdList)){//查询结果有数据返回
			Iterator iter=rdList.iterator();
			while(iter.hasNext()){
				LoanRepaymentDetail repayVo=(LoanRepaymentDetail)iter.next();
				RDetailVO rDetailVO=new RDetailVO();
				rDetailVO.setCurrentAccrual(repayVo.getCurrentAccrual());		//当期利息
	            rDetailVO.setCurrentTerm(repayVo.getCurrentTerm().intValue());	//当前期数
	            rDetailVO.setGiveBackRate(repayVo.getGiveBackRate());           //一次性还款退费
	            rDetailVO.setLoanId(repayVo.getLoanId());                       //借款ID
	            rDetailVO.setPrincipalBalance(repayVo.getPrincipalBalance());   //本金余额
	            rDetailVO.setRepaymentAll(repayVo.getRepaymentAll());           //一次性还款金额
	            rDetailVO.setReturnDate(repayVo.getReturnDate());               //还款日期
	            rDetailVO.setDeficit(repayVo.getDeficit());                  	//剩余欠款,用于记录不足额部分
	            rDetailVO.setRepaymentState(repayVo.getRepaymentState());       //当前还款状态
	            rDetailVO.setFactReturnDate(repayVo.getFactReturnDate());       //结清日期
	            rDetailVO.setPenaltyDate(repayVo.getPenaltyDate());             //罚息起算日期
	            rDetailVO.setReturneterm(repayVo.getReturneterm());            	//每期还款金额
	            rDetailVO.setPenalty(repayVo.getPenalty());                		//违约金
	            rDetailVO.setAccrualRevise(repayVo.getAccrualRevise());         //对应第三方的贴息或扣息 (积木盒子)
	            resultList.add(rDetailVO);	
			}
		}
		
		Map<String, Object> json = new HashMap<String, Object>();
        json.put("code", Constants.SUCCESS_CODE);
        json.put("message", "成功");
        json.put("total", resultList.size());
        json.put("max", pager.getRows());
        json.put("offset", pager.getPage());
        json.put("rDetailVOs", resultList);
        return json;
		//return MessageUtil.returnQuerySuccessMessage(pager.getRows(), resultList, resultList.size(), "rDetailVOs");
	}

	/**
	 * 还款明细表接口
	 * @param paramVo 参数集合
	 * @return
	 */
	@Override
	public Map<String, Object> queryFlow(Map<String,Object> paramMap) {
		paramMap.put("notEqTradeCode", new String[]{Const.TRADE_CODE_OPENACC_ASC, Const.TRADE_CODE_DRAWRISK, Const.TRADE_CODE_DRAWRISK_STUDENT});
		
		Pager returnPager =  offerRepayInfoDao.getOfferRepayInfoWithPg(paramMap);
		@SuppressWarnings("unchecked")
		List<OfferRepayInfo> repayInfoList = returnPager.getResultList();
		
		List<ReturnAccountCardVO> list = toReturnAccountCardVo(repayInfoList);
		
		int total = list != null ? list.size() : 0;
		Map<String,Object> json = MessageUtil.returnQuerySuccessMessage(returnPager.getRows(), list ,total,"accountCardVOs");
		
		return  json;
	}
	
	/**
	 * 封装获得的还款明细数据
	 * @param repayInfoList 还款明细数据集合
	 * @return
	 */
	@Override
	public List<ReturnAccountCardVO> toReturnAccountCardVo(List<OfferRepayInfo> repayInfoList){
		List<ReturnAccountCardVO> list = new ArrayList<ReturnAccountCardVO>();
		
		AccountCard accountCard = null;
		
		for (OfferRepayInfo offerRepayInfo : repayInfoList) {
			accountCard = new AccountCard();
			accountCard.setRepayInfo(offerRepayInfo);
			List<OfferFlow> offerFlows = offerFlowDao.findByTradeNo(offerRepayInfo.getTradeNo());
			if (tradeCodeisExist(offerRepayInfo.getTradeCode())) {
				formatAccountCardEx(accountCard, offerFlows);
			} else {
				formatAccountCard(accountCard, offerFlows);
				accountCard.setIncome(offerRepayInfo.getAmount());
			}
			
			list.add(returnAccountCardVOAttrAss(accountCard, offerFlows));
		}
		return list;
	}
	
	/**
	 * 封装获得的还款明细数据
	 * @param repayInfoList 还款明细数据集合
	 * @return
	 */
	@Override
	public AccountCard toReturnAccountCardVo(OfferRepayInfo offerRepayInfo){
		AccountCard accountCard = new AccountCard();
		accountCard.setRepayInfo(offerRepayInfo);
		List<OfferFlow> offerFlows = offerFlowDao.findByTradeNo(offerRepayInfo.getTradeNo());
		if (tradeCodeisExist(offerRepayInfo.getTradeCode())) {
			formatAccountCardEx(accountCard, offerFlows);
		} else {
			formatAccountCard(accountCard, offerFlows);
			accountCard.setIncome(offerRepayInfo.getAmount());
		}
		return accountCard;
	}
	
	/**
     * 判断职业是否在本系统存在
     * @param
     * @return
     */
    private boolean tradeCodeisExist(String value){
        boolean flag = false;
        String[] arrayList = {Const.TRADE_CODE_IN.toString(),Const.TRADE_CODE_OUT.toString(),Const.TRADE_CODE_IN.toString(),
        		Const.TRADE_CODE_STDIN.toString(),Const.TRADE_CODE_ORGIN.toString(),Const.TRADE_CODE_ORGOUT.toString()};
        for (String arr : arrayList){
            if (value.equals(arr)){
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    /**
     * 格式化账卡信息 （这里针对1002，1003，1004，1006，1007这几种操作）
     * @param accountCard 账卡对象
     * @param offerFlows 流水明细集合
     */
    private void formatAccountCardEx(AccountCard accountCard, List<OfferFlow> offerFlows){
    	Double outlay = 0.00;
    	Double qichuBalance = 0.00;
        
        for (OfferFlow offerFlow : offerFlows) {
        	//存保证金期初金额
        	if (offerFlow.getTradeCode().equals(Const.TRADE_CODE_STDIN.toString())) {
        		qichuBalance = offerFlow.getEndAmount().doubleValue();
			} else {
				if (offerFlow.getDorc().equals("C")) {
					qichuBalance = offerFlow.getEndAmount().doubleValue() + offerFlow.getTradeAmount().doubleValue();
				} else {
					qichuBalance = offerFlow.getEndAmount().doubleValue() - offerFlow.getTradeAmount().doubleValue();
				}
			}
        	
        	//计算支出金额 (1003,1007)
            if (offerFlow.getTradeCode().equals(Const.TRADE_CODE_OUT.toString()) || offerFlow.getTradeCode().equals(Const.TRADE_CODE_ORGOUT.toString()) 
            		|| offerFlow.getTradeCode().equals(Const.TRADE_CODE_STDIN.toString())){
                outlay = offerFlow.getTradeAmount().doubleValue();
            }
            
            //计算收入金额(1002,1004,1006)
            if (!(offerFlow.getTradeCode().equals(Const.TRADE_CODE_OUT.toString()) || offerFlow.getTradeCode().equals(Const.TRADE_CODE_ORGOUT.toString()))){
                accountCard.setIncome(offerFlow.getTradeAmount());
            }else{
                accountCard.setIncome(new BigDecimal("0.00"));
            }
		}
        
        accountCard.setOutlay(BigDecimal.valueOf(outlay));
        accountCard.setQichuBalance(BigDecimal.valueOf(qichuBalance));
        accountCard.setQimoBalance(getQimoBalance(offerFlows));
    }
    
    /**
     * 格式化账卡信息(这里只针对1001这种正常还款)
     * @param accountCard 账卡对象
     * @param offerFlows 流水明细集合
     */
    private void formatAccountCard(AccountCard accountCard, List<OfferFlow> offerFlows){
    	Double outlay = 0.00;
        Double qichuBalance = 0.00;
        
        for (OfferFlow offerFlow : offerFlows) {
        	//计算期初金额
        	if (offerFlow.getAcctTitle().equals(Const.ACCOUNT_TITLE_AMOUNT) && offerFlow.getAppoDorc().equals("C")) {
        		qichuBalance += offerFlow.getTradeAmount().doubleValue();
			}
        	
        	//统计支出金额
        	if (!offerFlow.getAcctTitle().equals(Const.ACCOUNT_TITLE_AMOUNT.toString()) && !offerFlow.getAccount().equals(Const.ACCOUNT_GAINS) &&
        		!offerFlow.getAppoAcct().equals(Const.ACCOUNT_RISK) && 
        		!(!offerFlow.getTradeCode().equals(Const.TRADE_CODE_STDIN) && offerFlow.getAcctTitle().equals(Const.ACCOUNT_TITLE_OTHER_PAYABLE))) {
        		outlay += offerFlow.getTradeAmount().doubleValue();
			}
        	
        	//针对7月20以后的数据,应当统计完所有的金额,然后在减去减免的罚息金额
            if(offerFlow.getAccount().equals(Const.ACCOUNT_GAINS) && !offerFlow.getAccount().equals(offerFlow.getAppoAcct())){
                outlay -= offerFlow.getTradeAmount().doubleValue();
            }
            
            //针对一次性还款时退费的处理
            if(offerFlow.getTradeCode().equals(Const.TRADE_CODE_ONEOFF)){
                //此处判断是退费的判断条件
                if ("D".equals(offerFlow.getAppoDorc()) && "C".equals(offerFlow.getDorc())){
                    outlay -= offerFlow.getTradeAmount().doubleValue()*2;//因为之前已经统计过了退费金额，但实际应该是减去退费金额,所以这里应该是乘以2
                }
            }
		}
        
        accountCard.setOutlay(BigDecimal.valueOf(outlay));
        accountCard.setQichuBalance(BigDecimal.valueOf(qichuBalance));
        accountCard.setQimoBalance(getQimoBalance(offerFlows));
    }
    
    /**
     * 计算期末余额   (帐卡中使用)
     * @param offerFlows 流水明细集合
     * @return
     */
    private BigDecimal getQimoBalance(List<OfferFlow> offerFlows) {
    	BigDecimal result = new BigDecimal("0.00");
    	
    	if (offerFlows != null && offerFlows.size() > 1) {
    		//正常还款、一次性还款时 直接取挂账流水金额
    		for (OfferFlow offerFlow : offerFlows) {
				if (offerFlow.getAcctTitle().equals(Const.ACCOUNT_TITLE_AMOUNT.toString()) && offerFlow.getAppoDorc().equals("D")) {
					result = offerFlow.getTradeAmount();
				}
			}
		} else if (offerFlows != null && offerFlows.size() == 1) {
			OfferFlow offerFlow = offerFlows.get(0);
			if (offerFlow.getTradeCode().equals(Const.TRADE_CODE_IN.toString()) || offerFlow.getTradeCode().equals(Const.TRADE_CODE_OUT.toString())) {
				result = offerFlow.getEndAmount();
			} else {
				BigDecimal qimoBalance = offerFlowDao.findQimoBalance(offerFlow.getLoanId(), offerFlow.getTradeNo());
				if (null != qimoBalance) {
					result = qimoBalance;
				} else {
					result = BigDecimal.valueOf(0.00);
				}
			}
		} else {
			result = BigDecimal.valueOf(0.00);
		}
    	
    	return result;
    }
    
    /**
     * AccountCardVO创建新的对象，并且给属性赋值
     * @param accountCard
     * @param offerFlows AccountCardVO对象的flowDetialsVO属性值的数据来源
     * @return
     */
    private ReturnAccountCardVO returnAccountCardVOAttrAss(AccountCard accountCard, List<OfferFlow> offerFlows) {
    	ReturnAccountCardVO accountCardVO = new ReturnAccountCardVO();
         accountCardVO.setTradeDate(accountCard.getRepayInfo().getTradeDate());//交易日期，格式为yyyy-MM-dd，例如：2015-03-13。
         accountCardVO.setTradeType(accountCard.getRepayInfo().getTradeType());//交易方式, TradeType{现金,转账,通联代扣,富友代扣,上海银联代扣,冲正补记,冲正,挂账,保证金,系统使用保证金,风险金},详见数据字典.
         accountCardVO.setTradeCode(accountCard.getRepayInfo().getTradeCode());//交易类型
         accountCardVO.setTradeName(tradeMap.get(accountCard.getRepayInfo().getTradeCode()));//交易类型名称
         accountCardVO.setBeginBalance(accountCard.getQichuBalance());      //期初余额
         accountCardVO.setIncome(accountCard.getIncome());                  //收入
         accountCardVO.setOutlay(accountCard.getOutlay());                  //支出
         accountCardVO.setEndBalance(accountCard.getQimoBalance());         //期末余额
         accountCardVO.setMemo(accountCard.getRepayInfo().getMemo() == null ? "" : accountCard.getRepayInfo().getMemo());          //备注
         accountCardVO.setTradeNo(accountCard.getRepayInfo().getTradeNo());    //流水号
         List<FlowDetialsVO> flowDetialsVOList = new ArrayList<FlowDetialsVO>();
         
         for (OfferFlow flow : offerFlows) {
        	 FlowDetialsVO flowDetialsVO = new FlowDetialsVO();
             flowDetialsVO.setAcctTitle(flow.getAcctTitle());       //明细项目
             flowDetialsVO.setTime(flow.getMemo2() == null ? "" : flow.getMemo2());                //期数
             flowDetialsVO.setTradeAmount(flow.getTradeAmount());   //明细金额
             
             if(Const.ACCOUNT_TITLE_AMOUNT.equals(flow.getAcctTitle())){
                 if ("C".equals(flow.getAppoDorc())) {
                     flowDetialsVO.setAcctTitleName("期初余额");
                 } else {
                     flowDetialsVO.setAcctTitleName("期末余额");
                 }
             } else if (Const.ACCOUNT_TITLE_LOAN_AMOUNT.equals(flow.getAcctTitle())) {
                 flowDetialsVO.setAcctTitleName("本金");
             } else if (Const.ACCOUNT_TITLE_OTHER_PAYABLE.equals(flow.getAcctTitle())) {
                 flowDetialsVO.setAcctTitleName("其他应付款");
             } else if (Const.ACCOUNT_TITLE_INTEREST_EXP.equals(flow.getAcctTitle())) {
                 flowDetialsVO.setAcctTitleName("利息");
             } else if (Const.ACCOUNT_TITLE_FINE_EXP.equals(flow.getAcctTitle())) {
                 if(flow.getAccount().equals(Const.ACCOUNT_GAINS)){
                     flowDetialsVO.setAcctTitleName("罚息(减免)");
                 }else{
                     flowDetialsVO.setAcctTitleName("罚息");
                 }
             } else if (Const.ACCOUNT_TITLE_MANAGEC_EXP.equals(flow.getAcctTitle())) {
                 if("D".equals(flow.getAppoDorc()) && "C".equals(flow.getDorc())){
                     flowDetialsVO.setAcctTitleName("丙方管理费(退费)");
                 }else{
                     flowDetialsVO.setAcctTitleName("丙方管理费");
                 }
             } else if (Const.ACCOUNT_TITLE_CONSULT_EXP.equals(flow.getAcctTitle())) {
                 if("D".equals(flow.getAppoDorc()) && "C".equals(flow.getDorc())){
                     flowDetialsVO.setAcctTitleName("咨询费(退费)");
                 }else{
                     flowDetialsVO.setAcctTitleName("咨询费");
                 }
             } else if (Const.ACCOUNT_TITLE_APPRAISAL_EXP.equals(flow.getAcctTitle())) {
                 if("D".equals(flow.getAppoDorc()) && "C".equals(flow.getDorc())){
                     flowDetialsVO.setAcctTitleName("评估费(退费)");
                 }else{
                     flowDetialsVO.setAcctTitleName("评估费");
                 }
             } else if (Const.ACCOUNT_TITLE_MANAGE_EXP.equals(flow.getAcctTitle())) {
                 if("D".equals(flow.getAppoDorc()) && "C".equals(flow.getDorc())){
                     flowDetialsVO.setAcctTitleName("管理费(退费)");
                 }else{
                     flowDetialsVO.setAcctTitleName("管理费");
                 }
             } else if (Const.ACCOUNT_TITLE_PENALTY_EXP.equals(flow.getAcctTitle())) {
                 flowDetialsVO.setAcctTitleName(" 违约金");
             }
             
             flowDetialsVOList.add(flowDetialsVO);
         }
         accountCardVO.setFlowDetialsVO(flowDetialsVOList);//流水明细信息列表
         return accountCardVO;
    }
    
    /**
	 * 查看还款汇总信息接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> queryRepaymentSummary(RepaymentDetailParamVO params) throws Exception {
		Long loanId = params.getLoanId();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		LoanBase loanBase = loanBaseDao.findByLoanId(loanId);
		VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
		/** 获取计算器实例 **/
		ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
		if (loanBase == null || vLoanInfo == null) {
			resultMap.put("code", Constants.DATA_ERR_CODE);
			resultMap.put("message", "失败：loanId输入有误，未查到对应记录");
		} else if (loanBase != null && (LoanStateEnum.正常.getValue().equals(loanBase.getLoanState()) || LoanStateEnum.逾期.getValue().equals(loanBase.getLoanState()) || 
				LoanStateEnum.坏账.getValue().equals(loanBase.getLoanState()) || LoanStateEnum.结清.getValue().equals(loanBase.getLoanState()) ||
				LoanStateEnum.预结清.getValue().equals(loanBase.getLoanState()))) {
			
			Date tradeDate = new Date();
			RepayInfoVo repayInfoVo = new RepayInfoVo();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			List<LoanRepaymentDetail> loanRepaymentDetailList = afterLoanService.getAllInterestOrLoan(tradeDate, loanId);
			BigDecimal acctmount = afterLoanService.getAccAmount(loanId);
			acctmount = acctmount != null ? acctmount : new BigDecimal(0.00);
			repayInfoVo.setAccAmount(acctmount);
			if (loanRepaymentDetailList != null && loanRepaymentDetailList.size() > 0) {
				BigDecimal amount = new BigDecimal(0.00);//计算还款总额
	            BigDecimal temp = new BigDecimal(0.00);//临时变量，如逾期本金、利息
	            
	            //以下获取逾期还款信息
	            repayInfoVo.setOverDueTerm(afterLoanService.getOverdueTermCount(loanRepaymentDetailList, tradeDate));
	            temp = afterLoanService.getOverdueInterest(loanRepaymentDetailList, tradeDate);
	            temp = temp != null ? temp : new BigDecimal(0.00);
	            amount.add(temp);
	            repayInfoVo.setOverInterest(temp);
	            
	            temp = afterLoanService.getOverdueCorpus(loanRepaymentDetailList, tradeDate);
	            temp = temp != null ? temp : new BigDecimal(0.00);
	            amount.add(temp);
	            repayInfoVo.setOverCorpus(temp);
	            
	            String returnDate = loanRepaymentDetailList.get(0).getReturnDate() != null ? sdf.format(loanRepaymentDetailList.get(0).getReturnDate()) : "";
	            String overDueDate = repayInfoVo.getOverDueTerm() < 1 ? null : returnDate;
	            repayInfoVo.setOverDueDate(overDueDate);
	            
	           //以下获取逾期罚息相关信息
	            repayInfoVo.setFineDay(afterLoanService.getOverdueDay(loanRepaymentDetailList, tradeDate));
	            BigDecimal remnant = afterLoanService.getRemnant(loanRepaymentDetailList);
	            remnant = remnant != null ? remnant : new BigDecimal(0.00);
	            repayInfoVo.setRemnant(remnant);
	            repayInfoVo.setOverdueAmount(amount);
	            
	            temp = afterLoanService.getFine(loanRepaymentDetailList, tradeDate);//罚息
	            temp = temp != null ? temp : new BigDecimal(0.00);
	            amount.add(temp);
	            repayInfoVo.setFine(temp);
	            Double overdueAmount = amount.doubleValue() - acctmount.doubleValue() > 0 ? amount.doubleValue() - acctmount.doubleValue() : 0.00;
	            repayInfoVo.setOverdueAmount(BigDecimal.valueOf(overdueAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
	            
	            String penaltyDate = loanRepaymentDetailList.get(0).getPenaltyDate() != null ? sdf.format(loanRepaymentDetailList.get(0).getPenaltyDate()) : "";
	            String fineDate = repayInfoVo.getFineDay() < 1 ? null : penaltyDate;
	            repayInfoVo.setFineDate(fineDate);
	            
	            //以下获取当期的还款信息
	            int size = loanRepaymentDetailList.size();
	            LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailList.get(size - 1);
	            repayInfoVo.setCurrDate(loanRepaymentDetail.getReturnDate() != null ? sdf.format(loanRepaymentDetail.getReturnDate()) : "");
	            repayInfoVo.setCurrTerm(loanRepaymentDetail.getCurrentTerm() != null ? loanRepaymentDetail.getCurrentTerm() : 0);
	            temp = afterLoanService.getCurrInterest(loanRepaymentDetailList, tradeDate);
	            temp = temp != null ? temp : new BigDecimal(0.00);
	            amount.add(temp);
	            repayInfoVo.setCurrInterest(temp);
	            temp = afterLoanService.getCurrCorpus(loanRepaymentDetailList, tradeDate);
	            temp = temp != null ? temp : new BigDecimal(0.00);
	            amount.add(temp);
	            repayInfoVo.setCurrCorpus(temp);
	            Double currAmount = amount.doubleValue() - acctmount.doubleValue() > 0 ? amount.doubleValue() - acctmount.doubleValue() : 0.00;
	            repayInfoVo.setCurrAmount(BigDecimal.valueOf(currAmount));
	            //以上不要随意调整语句的顺序，因设计的amount的值计算
	            /** 获取一次性结清金额 **/
	            BigDecimal onetimeRepaymentAmount = calculatorInstace.getOnetimeRepaymentAmount(loanId, tradeDate, loanRepaymentDetailList);
	            repayInfoVo.setOneTimeRepayment(onetimeRepaymentAmount);
			}
			repayInfoVo.setTradeDate(sdf.format(tradeDate));
			
			repayInfoVo.setID(loanId.toString());
			
			PersonInfo personInfo = personInfoDao.get(loanBase.getBorrowerId());
			repayInfoVo.setName(personInfo.getName());
			repayInfoVo.setIdType(personInfo.getIdtype());
			repayInfoVo.setIdNum(personInfo.getIdnum());
			repayInfoVo.setMphone(personInfo.getMphone());
			
			resultMap.put("code", Constants.SUCCESS_CODE);
			resultMap.put("message", "成功");
			resultMap.put("repayInfo", repayInfoVo);
		} else {
			resultMap.put("code", Constants.DATA_ERR_CODE);
			resultMap.put("message", "失败：暂无还款汇总信息");
		}
		
		return resultMap;
	}

	/**
	 * 查看还款详细信息接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> queryRepaymentDetailAll(Map<String,Object> paramMap) throws Exception {
		Long loanId = (Long)paramMap.get("loanId");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		LoanBase loanBase = loanBaseDao.findByLoanId(loanId);
		if (loanBase == null) {
			resultMap.put("code", Constants.DATA_ERR_CODE);
			resultMap.put("message", "失败：loanId输入有误，未查到对应记录");
		} else if (loanBase != null && (LoanStateEnum.正常.getValue().equals(loanBase.getLoanState()) || LoanStateEnum.逾期.getValue().equals(loanBase.getLoanState()) || 
				LoanStateEnum.坏账.getValue().equals(loanBase.getLoanState()) || LoanStateEnum.结清.getValue().equals(loanBase.getLoanState()) ||
				LoanStateEnum.预结清.getValue().equals(loanBase.getLoanState()))) {
			
			Pager pager=loanRepaymentDetailDao.getRepaymentDatailWithPg(paramMap);
			List loanRepaymentDetailList=pager.getResultList();
			
			LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(loanId);
			
			List<ReturnRepaymentDetailVo> rDetailVoList=new ArrayList<ReturnRepaymentDetailVo>();
			if(CollectionUtils.isNotEmpty(loanRepaymentDetailList)){//查询结果有数据返回
				for (Iterator<LoanRepaymentDetail> iter = loanRepaymentDetailList.iterator(); iter.hasNext();) {
					LoanRepaymentDetail lrd = iter.next();
					
					ReturnRepaymentDetailVo rDetailVo=new ReturnRepaymentDetailVo();
					rDetailVo.setCurrentTerm(lrd.getCurrentTerm());            //当前期数
					rDetailVo.setReturnDate(lrd.getReturnDate());              //应还款日
					rDetailVo.setFactReturnDate(lrd.getFactReturnDate());      //实际还款日
					rDetailVo.setReturneterm(lrd.getReturneterm());            //还款金额
					rDetailVo.setRepaymentAll(lrd.getRepaymentAll());          //当期一次性还款金额
					rDetailVo.setRepaymentState(lrd.getRepaymentState());      //当期还款状态
					rDetailVo.setDeficit(lrd.getDeficit());                    //当期剩余欠款
					
					ProdCreditProductInfo creditProduct = null;
					if (loanInitialInfo != null && loanInitialInfo.getLoanType() != null) {
						creditProduct = prodCreditProductInfoDao.findByLoanType(loanInitialInfo.getLoanType());
					}
					ZhuxueProductPlan productPlan = null;
					if (loanBase.getPlanId() != null) {
						productPlan = zhuxueProductPlanDao.get(loanBase.getPlanId());
					}
					
					if (creditProduct != null && "1".equals(creditProduct.getCategory())) {
						PersonInfo personInfo = personInfoDao.get(loanBase.getBorrowerId());
						rDetailVo.setBorrowerName(personInfo.getName());           //还款方
					} else if (productPlan!= null && productPlan.getOrgRepayTerm() >= lrd.getCurrentTerm()) {
						ZhuxueOrganization organization = zhuxueOrganizationDao.get(productPlan.getOrganizationId());
						rDetailVo.setBorrowerName(organization.getName());         //还款方
					} else {
						PersonInfo personInfo = personInfoDao.get(loanBase.getBorrowerId());
						rDetailVo.setBorrowerName(personInfo.getName());           //还款方
					}
					
					rDetailVoList.add(rDetailVo);
				}
			}
			resultMap.put("code", Constants.SUCCESS_CODE);
			resultMap.put("message", "成功");
			resultMap.put("repaymentDetail", rDetailVoList);
		} else {
			resultMap.put("code", Constants.DATA_ERR_CODE);
			resultMap.put("message", "失败：暂无还款信息");
		}
		
        return resultMap;
	}

}
