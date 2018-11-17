package com.zdmoney.credit.debit.service;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.CompensatoryTypeEnum;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.DebitNotifyStateEnum;
import com.zdmoney.credit.common.constant.DebitOperateTypeEnum;
import com.zdmoney.credit.common.constant.DebitRepayTypeEnum;
import com.zdmoney.credit.common.constant.DebitResultStateEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStatueLufaxEnum;
import com.zdmoney.credit.common.constant.PayOffTypeEnum;
import com.zdmoney.credit.common.constant.PayPartyEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.SplitResultStateEnum;
import com.zdmoney.credit.common.constant.TradeKindEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.constant.lufax.LufaxConst;
import com.zdmoney.credit.common.constant.lufax.LufaxRepayInterfaceEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.LufaxUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.debit.dao.pub.IDebitQueueLogDao;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.framework.vo.lufax.entity.BuyBackCompensatoryDetailEntity;
import com.zdmoney.credit.framework.vo.lufax.entity.BuyBackTradeDetailEntity;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100009Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100009Vo.ItemLufax;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100018Vo;
import com.zdmoney.credit.ljs.dao.pub.ICompensatoryDetailLufaxDao;
import com.zdmoney.credit.ljs.dao.pub.ILoanStatusLufaxDao;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;
import com.zdmoney.credit.ljs.domain.LoanStatusLufax;
import com.zdmoney.credit.ljs.service.pub.ICompensatoryDetailLufaxService;
import com.zdmoney.credit.ljs.service.pub.IPublicAccountDetailService;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferRepayInfoDao;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.repay.vo.DebitQueueManagementVo;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;


/**
 * @author YM10112
 */
@Service
public class DebitQueueLogServiceImpl implements IDebitQueueLogService {
	
	private static final Logger logger = Logger.getLogger(DebitQueueLogServiceImpl.class);
	
	@Autowired
	private IDebitQueueLogDao debitQueueLogDao;
	
	@Autowired
	private ISequencesService sequencesService;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private IOfferFlowService offerFlowService;
	
	@Autowired
	private ILoanLedgerService loanLedgerService;
	
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	
	@Autowired
	private IOfferRepayInfoService offerRepayInfoService;
	
	@Autowired
	private ILoanLogService loanLogService;
	
	@Autowired
	ILoanBsbMappingDao loanBsbMappingDao;
	
	@Autowired
	IPublicAccountDetailService publicAccountDetailService;
	
	@Autowired
	ISendMailService sendMailService;
	
	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private IOfferRepayInfoDao OfferRepayInfoDaoImpl;
	
	@Autowired
    private ISplitQueueLogService splitQueueLogService;
	
	@Autowired
    private ICompensatoryDetailLufaxDao compensatoryDetailLufaxDao;
	
	@Autowired
	private ICompensatoryDetailLufaxService compensatoryDetailLufaxService;
	
	@Autowired
	private ILoanRepaymentDetailDao loanRepaymentDetailDao;
	
	@Autowired
	private ILoanStatusLufaxDao loanStatusLufaxDao;
	
	/**
	 * 划扣接口调用后，改状态
	 * @param debitQueueLogList
	 */
	public void updateDebitQueueLogAfterDebitInterface(List<DebitQueueLog> debitQueueLogList,String state, String anshuoBatchId) {
		for(DebitQueueLog debitQueueLog : debitQueueLogList){
			DebitQueueLog log = new DebitQueueLog();
			log.setId(debitQueueLog.getId());
			log.setBatchId(anshuoBatchId);
			log.setDebitNotifyDate(new Date());
			log.setDebitNotifyState(state);
			log.setDebitResultDate(new Date());
			log.setDebitNo(debitQueueLog.getDebitNo());
			if(DebitNotifyStateEnum.通知成功.getCode().equals(state)){
				log.setDebitResultState(DebitResultStateEnum.划扣中.getCode());
			}else{
				log.setDebitResultState(DebitResultStateEnum.划扣失败.getCode());
			}
			log.setUpdateTime(new Date());
			debitQueueLogDao.update(log);
		}
	}
	
	/**
	 * 划扣接口调用成功后，改状态
	 * @param debitQueueLogList
	 */
	@Override
	public void updateDebitQueueLogAfterGetDeductResultNotifyFromLufax(String serialno,String debitResultState) {
		debitQueueLogDao.updateDebitQueueLogAfterGetDeductResultNotifyFromLufax(serialno, debitResultState);
	}
	
    /**
     * 委托还款转发代扣
     * @param debitQueueLogList
     */
    public void executeEntrustDebit(List<DebitQueueLog> debitQueueLogList) {
        if(CollectionUtils.isEmpty(debitQueueLogList)){
            Map<String, Object> map = new HashMap<String, Object>();
            String toyday = DateTime.now().toString("yyyy-MM-dd");
            map.put("createDate", toyday);// 得到今天的时间 时间小于当天时间，只推送小于今天的数据给陆金所，进行划扣
            //map.put("debitType", DebitOperateTypeEnum.自动代扣.getCode());
            map.put("debitResultStates", new String[]{DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣失败.getCode()});
            map.put("repayTypes", new String[]{DebitRepayTypeEnum.委托还款.getCode()});
            debitQueueLogList = debitQueueLogDao.findListByMap(map);
        }
        if(CollectionUtils.isEmpty(debitQueueLogList)){
            throw new PlatformException(ResponseEnum.FULL_MSG, "划扣队列中没有【委托还款】待划扣的记录！");
        }
        executeDebitQueueLogList(debitQueueLogList);
    }
	
    /**
     * 提前结清和委托还款转发代扣
     * @param debitQueueLogList
     */
    public void executeAdvanceClearDebit(List<DebitQueueLog> debitQueueLogList) {
        if(CollectionUtils.isEmpty(debitQueueLogList)){
            debitQueueLogList = debitQueueLogDao.findEntrustAndAdvanceClearDebit();
        }
        List<DebitQueueLog> resultList = (List<DebitQueueLog>) CollectionUtils.select(debitQueueLogList, new Predicate<DebitQueueLog>(){
            @Override
            public boolean evaluate(DebitQueueLog debitQueueLog) {
                // 排除机构还款未完成代扣的债权
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("loanId", debitQueueLog.getLoanId());
                param.put("repayTypes", new String []{DebitRepayTypeEnum.机构还款.getCode()});
                param.put("debitResultStates",new String[] {DebitResultStateEnum.划扣中.getCode()});
                List<DebitQueueLog> filterList1 = debitQueueLogDao.findListByMap(param);
                
                // 排除机构还款、委托还款未结清的债权
                param = new HashMap<String, Object>();
                param.put("loanId", debitQueueLog.getLoanId());
                param.put("repayTypes", new String []{DebitRepayTypeEnum.机构还款.getCode()});
                param.put("splitResultState", SplitResultStateEnum.未分账.getCode());
                param.put("payOffType", PayOffTypeEnum.未结清.getCode());
                List<SplitQueueLog> filterList3 = splitQueueLogService.findSplitQueueLogListByParams(param);
                
                return CollectionUtils.isEmpty(filterList1) && CollectionUtils.isEmpty(filterList3);
            }
        });
        if(CollectionUtils.isEmpty(resultList)){
            throw new PlatformException(ResponseEnum.FULL_MSG, "划扣队列中没有【提前结清】待划扣的记录！原因可能是存在【机构还款】或者【委托还款】未结算完成的记录。");
        }
        executeDebitQueueLogList(resultList);
    }
    
    /**
	 * 逾期代偿和委托还款转发代扣（准备金、保证金）
	 * @param debitQueueLogList
	 */
    public void overdueCompensatory(List<DebitQueueLog> debitQueueLogList,String type) {
        if(CollectionUtils.isEmpty(debitQueueLogList)){
        	Map<String, Object> params = new HashMap<String, Object>();
            debitQueueLogList = debitQueueLogDao.findEntrustAndOverdueDebit(params);
        }
        List<DebitQueueLog> resultList = (List<DebitQueueLog>) CollectionUtils.select(debitQueueLogList, new Predicate<DebitQueueLog>(){
            @Override
            public boolean evaluate(DebitQueueLog debitQueueLog) {
                // 排除机构还款，提前结清未完成代扣的债权
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("loanId", debitQueueLog.getLoanId());
                param.put("repayTypes", new String []{DebitRepayTypeEnum.机构还款.getCode()});
                param.put("debitResultStates",new String[] {DebitResultStateEnum.划扣中.getCode()});
                List<DebitQueueLog> filterList1 = debitQueueLogDao.findListByMap(param);
                // 排除机构还款、委托还款未结清的债权
                param = new HashMap<String, Object>();
                param.put("loanId", debitQueueLog.getLoanId());
                param.put("repayTypes", new String []{DebitRepayTypeEnum.机构还款.getCode()});
                param.put("splitResultState", SplitResultStateEnum.未分账.getCode());
                param.put("payOffType", PayOffTypeEnum.未结清.getCode());
                List<SplitQueueLog> filterList3 = splitQueueLogService.findSplitQueueLogListByParams(param);
                
                return CollectionUtils.isEmpty(filterList1) && CollectionUtils.isEmpty(filterList3);
            }
        });
        
        if(CollectionUtils.isEmpty(resultList)){
            throw new PlatformException(ResponseEnum.FULL_MSG, "划扣队列中没有【委托还款/逾期代偿】待划扣的记录！原因可能是存在【机构还款】未结算完成的记录。");
        }
        
		Map<String, List<DebitQueueLog>> result = new HashMap<String, List<DebitQueueLog>>();
		List<DebitQueueLog> list = null;
		for (DebitQueueLog debitQueueLog : resultList) {
			String key = debitQueueLog.getLoanId().toString();
			if (result.containsKey(key)) {
				list = result.get(key);
			} else {
				list = new ArrayList<DebitQueueLog>();
			}
			list.add(debitQueueLog);
			result.put(key, list);
		}
		for (String key : result.keySet()) {
			List<DebitQueueLog> list2 = result.get(key);
			this.updatePenaltyAmount(this.findPrimaryDebitQueueLog(list2));
		}
		
        /* 调用【陆金所】转发代扣接口*/
        executeDebitQueueLogList(resultList);
    }

    /**
     * 一次性回购转发代扣（准备金、保证金）
     * @param debitQueueLogList
     */
    public void oneTimeOverdueBuyBack(List<DebitQueueLog> debitQueueLogList) {
        if(CollectionUtils.isEmpty(debitQueueLogList)){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("repayType", DebitRepayTypeEnum.一次性回购.getCode());
            map.put("debitResultStates",new String[] {DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣失败.getCode()});
            debitQueueLogList = debitQueueLogDao.findListByMap(map);
        }
        // 刷选掉不符合条件划扣队列数据
        List<DebitQueueLog> resultList = (List<DebitQueueLog>) CollectionUtils.select(debitQueueLogList, new Predicate<DebitQueueLog>(){
            public boolean evaluate(DebitQueueLog debitQueueLog) {
                // 排除机构还款未完成代扣的债权
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("loanId", debitQueueLog.getLoanId());
                param.put("repayTypes", new String []{DebitRepayTypeEnum.机构还款.getCode()});
                param.put("debitResultStates",new String[] {DebitResultStateEnum.划扣中.getCode()});
                List<DebitQueueLog> filterList1 = debitQueueLogDao.findListByMap(param);
                // 排除委托还款未完成代扣的债权
                param = new HashMap<String, Object>();
                param.put("loanId", debitQueueLog.getLoanId());
                param.put("repayTypes", new String []{DebitRepayTypeEnum.委托还款.getCode()});
                param.put("debitResultStates",new String[] {DebitResultStateEnum.未划扣.getCode(),DebitResultStateEnum.划扣中.getCode(),DebitResultStateEnum.划扣失败.getCode()});
                List<DebitQueueLog> filterList2 = debitQueueLogDao.findListByMap(param);
                // 排除机构还款、委托还款未结清的债权
                param = new HashMap<String, Object>();
                param.put("loanId", debitQueueLog.getLoanId());
                param.put("repayTypes", new String []{DebitRepayTypeEnum.机构还款.getCode(), DebitRepayTypeEnum.委托还款.getCode()});
                param.put("splitResultState", SplitResultStateEnum.未分账.getCode());
                param.put("payOffType", PayOffTypeEnum.未结清.getCode());
                List<SplitQueueLog> filterList3 = splitQueueLogService.findSplitQueueLogListByParams(param);
                return CollectionUtils.isEmpty(filterList1) && CollectionUtils.isEmpty(filterList2) && CollectionUtils.isEmpty(filterList3);
            }
        });
        
        if(CollectionUtils.isEmpty(resultList)){
            throw new PlatformException(ResponseEnum.FULL_MSG, "划扣队列中没有【一次性回购】待划扣的记录！原因可能是存在【机构还款】或者【委托还款】未结算完成的记录。");
        }
        
        /*调用【陆金所】一次性回购代扣接口  */
        executeBuyBackDebitQueueLog(resultList);
    }
    
    /**
     * 调用陆金所转发代扣接口
     * @param debitQueueLogList
     */
    public void executeDebitQueueLogList(List<DebitQueueLog> debitQueueLogList) {
        if (CollectionUtils.isEmpty(debitQueueLogList)) {
            logger.warn("划扣队列中没有待划扣的记录！");
            throw new PlatformException(ResponseEnum.FULL_MSG, "划扣队列中没有待划扣的记录！");
        }
        logger.info("【陆金所】转发代扣接口被调用Lufax100009Vo...");
        // 收集封装转发代扣接口参数
        Lufax100009Vo vo = buildLufax100009Vo(debitQueueLogList);
        try {
            JSONObject grantResut = GatewayUtils.callCateWayInterface(vo,GatewayFuncIdEnum.陆金所扣款指令.getCode());
            logger.info("【陆金所】转发代扣接口,陆金所返回的数据：" + grantResut);
            String respCode = (String) grantResut.get("ret_code");
            String respDesc = (String) grantResut.get("ret_msg");
            // 安硕批次号
            String anshuoBatchId = vo.getAnshuo_batch_id();
            if ("0000".equals(respCode)) {
                this.updateDebitQueueLogAfterDebitInterface(debitQueueLogList,DebitNotifyStateEnum.通知成功.getCode(), anshuoBatchId);
                logger.info("【陆金所】转发代扣接口文件交互成功，并已成功通知。" + respDesc);
            } else {
                this.updateDebitQueueLogAfterDebitInterface(debitQueueLogList,DebitNotifyStateEnum.通知失败.getCode(), anshuoBatchId);
                String repayType = debitQueueLogList.get(0).getRepayType();
                if(!DebitRepayTypeEnum.机构还款.getCode().equals(repayType)){
                	sendMailAfterDebitInterface(debitQueueLogList);
                }
                logger.info("调用【陆金所】转发代扣接口失败：原因：" + respDesc);
            }
        } catch (Exception e) {
            logger.error("【陆金所】转发代扣接口调用网关接口失败：" + e.getMessage(), e);
            throw new PlatformException(ResponseEnum.FULL_MSG, "【陆金所】转发代扣接口调用网关接口失败：" + e.getMessage());
        }
    }
    
	/**
	 * 封装lufax100009
	 * @param debitQueueLog
	 */
	private Lufax100009Vo buildLufax100009Vo(List<DebitQueueLog> debitQueueLogList) {
		Map<String, String> debitNoMap = new HashMap<String, String>();
		DebitQueueLog debitLog = this.findPrimaryDebitQueueLog(debitQueueLogList);
		for (DebitQueueLog debitQueueLog : debitQueueLogList) {
			debitQueueLog.setRepayType(debitLog.getRepayType());
			String key = debitQueueLog.getLoanId() + debitQueueLog.getRepayType();
			String anshuoSerialno = "";
			if (debitNoMap.containsKey(key)) {
				anshuoSerialno = debitNoMap.get(key);
			} else {
				anshuoSerialno = LufaxUtil.createAnshuoSerialno();
				debitNoMap.put(key, anshuoSerialno);
			}
			debitQueueLog.setDebitNo(anshuoSerialno);
		}
		debitQueueLogList = sortDebitQueueLog(debitQueueLogList);
		Lufax100009Vo vo = new Lufax100009Vo();
		vo.setInterface_id(LufaxRepayInterfaceEnum.证大发起一般还款_提前还款指令_逾期回购指令.getCode());
		vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());// 安硕批次号
		vo.setLine_sum(String.valueOf(debitQueueLogList.size()));// 总条数
		vo.setInterface_reqser(LufaxUtil.createInterfaceReqId(vo.getInterface_id()));
		List<ItemLufax> detail = new ArrayList<ItemLufax>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal capital = BigDecimal.ZERO;
		BigDecimal inte = BigDecimal.ZERO;
		BigDecimal oint = BigDecimal.ZERO;
		for (DebitQueueLog debitQueueLog : debitQueueLogList) {
			Long loanId = debitQueueLog.getLoanId();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			//String appNo = vLoanInfo.getAppNo();
			// 最终申请单号、和appNo可能会不一致
			String channelPushNo = vLoanInfo.getChannelPushNo();
			totalAmount = totalAmount.add(debitQueueLog.getAmount());
			ItemLufax item = new ItemLufax();
			item.setProduct_type(IDebitQueueLogService.PRODUCT_TYPE);// 业务类型
			item.setSerialno(debitQueueLog.getDebitNo());// 安硕序号
			item.setAnshuo_loan_accept_id(LufaxConst.ZDJR + channelPushNo);// 安硕贷款受理号（ZDJR_channelPushNo）
			item.setLufax_loan_req_id(loanBsbMappingDao.getByLoanId(loanId).getOrderNo());// 借款申请ID
			item.setAmount(debitQueueLog.getAmount().toString());// 金额
			item.setRpterm(0);// 期数
			item.setCapital(capital);// 本金
			item.setInte(inte);// 利息
			item.setOint(oint); // 罚息
			// 1：借款人2：服务公司（对公）3：准备金 (默认为1)4-保证金
			item.setPay_party(debitQueueLog.getPayParty());
			item.setType(debitQueueLog.getDebitType());
			if (DebitRepayTypeEnum.逾期代偿.getCode().equals(debitQueueLog.getRepayType())) {
				List<CompensatoryDetailLufax> compensatoryDetailList = this.findCompensatoryDetailLufaxList(debitQueueLog);
				if(CollectionUtils.isEmpty(compensatoryDetailList)){
					continue;
				}
				for(CompensatoryDetailLufax compensatoryDetail : compensatoryDetailList){
					capital = capital.add(compensatoryDetail.getCorpusAmount());
					inte = inte.add(compensatoryDetail.getAccrualAmount());
					oint = oint.add(compensatoryDetail.getPenaltyAmount());
				}
				item.setRpterm(debitQueueLog.getRepayTerm().intValue()); // 期数
				item.setCapital(capital);// 本金
				item.setInte(inte);// 利息
				item.setOint(oint); // 罚息
			}
			detail.add(item);
		}
		vo.setAmount_sum(totalAmount.toString());// 总金额
		vo.setDetail(detail);// 单笔交易明细
		return vo;
	}
	
	/**
	 * 合并loanId,金额相加，得到批次号和安硕序号（划扣流水号）
	 * @param list
	 * @return
	 */
	private List<DebitQueueLog> sortDebitQueueLog(List<DebitQueueLog> list) {
		Map<String, DebitQueueLog> tempMap = new HashMap<String, DebitQueueLog>();
		for (DebitQueueLog debitLog : list) {
			String key = debitLog.getLoanId().toString() + debitLog.getRepayType();
			if (tempMap.containsKey(key)) {
				DebitQueueLog tempLog = tempMap.get(key);
				tempLog.setAmount(tempLog.getAmount().add(debitLog.getAmount()));
				// 针对一天存在两笔还款（线下还当期 + 提前结清）的情况
				if (DebitRepayTypeEnum.提前结清.getCode().equals(debitLog.getRepayType())) {
					tempLog.setRepayType(debitLog.getRepayType());
					tempLog.setDebitType(debitLog.getDebitType());
				}
				tempMap.put(key, tempLog);
			} else {
				tempMap.put(key, debitLog);
			}
		}
		List<DebitQueueLog> logist = new ArrayList<DebitQueueLog>();
		for (String key : tempMap.keySet()) {
			logist.add(tempMap.get(key));
		}
		return logist;
	}
	
    /**
     * 还款日后一天挂账金额分账
     */
    public void executeBillRepayDeal(Date date) {
        Calendar tradeDate = Calendar.getInstance();
        if(null != date){
            tradeDate.setTime(date);
        }
        int day= tradeDate.get(Calendar.DAY_OF_MONTH);
        if(day != 2 && day != 17){
            logger.info("不是日终后一天（2、17日），定时任务不执行！");
            return;
        }
        int promiseReturnDate = tradeDate.get(Calendar.DAY_OF_MONTH) - 1;
        // 获取陆金所所有挂账的贷款户ID
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("loanBelong", FundsSourcesTypeEnum.陆金所.getValue());
        params.put("promiseReturnDate", promiseReturnDate);
        List<LoanLedger> loanLedgerList = loanLedgerService.getLoanLedgerInfoByLoanBelong(params);
        if (CollectionUtils.isEmpty(loanLedgerList)) {
            logger.warn("陆金所合同来源下不存在挂账金额的债权！");
            return;
        }
        logger.info("陆金所存在挂账金额进行自动分账记录条数为："+ loanLedgerList.size());
        loanLogService.createLog("executeBillRepayDeal", "info", "还款日后一天挂账金额自动分账处理开始。。。。。。", "SYSTEM");
        for(LoanLedger loanLedger : loanLedgerList){
            Long loanId = loanLedger.getLoanId();
            BigDecimal amount = loanLedger.getAmount();
            try {
                // 查询到当期应还但未还的还款计划信息
                List<LoanRepaymentDetail> loanRepaymentDetailList = afterLoanService.getAllInterestOrLoan(tradeDate.getTime(), loanId);
                if(CollectionUtils.isEmpty(loanRepaymentDetailList)){
                    logger.warn("当期已结清！债权编号："+ loanId);
                    continue;
                }
                // 当期还款计划信息
                LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailList.get(loanRepaymentDetailList.size() - 1);
                logger.info("陆金所存在挂账金额进行自动分账对应还款期数："+ loanRepaymentDetail.getCurrentTerm());
                // 查询债权信息
                VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
                // 生成还款流水信息
                OfferRepayInfo repayInfoInstance = new OfferRepayInfo();
                repayInfoInstance.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
                repayInfoInstance.setLoanId(loanId);
                repayInfoInstance.setAmount(new BigDecimal("0"));
                repayInfoInstance.setMemo("还款日后一天挂账金额自动分账");
                repayInfoInstance.setOrgan(loanInfo != null ? String.valueOf(loanInfo.getSalesDepartmentId()) : "999");
                repayInfoInstance.setTeller(Const.ENDOFDAY_TELLER);
                repayInfoInstance.setTradeCode(afterLoanService.isOneTimeRepayment(loanId) ? Const.TRADE_CODE_ONEOFF: Const.TRADE_CODE_NORMAL);
                repayInfoInstance.setTradeDate(tradeDate.getTime());
                repayInfoInstance.setTradeKind(TradeKindEnum.正常交易.getValue());
                repayInfoInstance.setTradeType(TradeTypeEnum.挂账.getValue());
                repayInfoInstance.setTradeNo(afterLoanService.getTradeFlowNo(loanId));
                repayInfoInstance.setCreateTime(new Date());
                offerRepayInfoService.save(repayInfoInstance);
                // 挂账金额销账处理
                offerFlowService.accountBalancePayDeal(repayInfoInstance, amount);
                // 当期分账处理、返回剩余金额(剩余金额如果大于零，会继续挂账)
                BigDecimal surplusAmount = offerFlowService.normalRepaymentDeal(repayInfoInstance, loanInfo, loanRepaymentDetail, amount);
                // 分账使用掉的金额
                BigDecimal splitAmount = amount.subtract(surplusAmount);
                if(splitAmount.compareTo(BigDecimal.ZERO) > 0){
                    // 创建划扣队列
                    DebitQueueLog debitQueueLog = new DebitQueueLog();
                    debitQueueLog.setLoanId(loanId);
                    debitQueueLog.setTradeNo(repayInfoInstance.getTradeNo());
                    debitQueueLog.setDebitNotifyState(DebitNotifyStateEnum.待通知.getCode());
                    debitQueueLog.setDebitResultState(DebitResultStateEnum.未划扣.getCode());
                    debitQueueLog.setDebitType(DebitOperateTypeEnum.当期代偿.getCode());
                    debitQueueLog.setAmount(splitAmount);
                    debitQueueLog.setPayParty(PayPartyEnum.准备金.getCode());
                    debitQueueLog.setRepayType(DebitRepayTypeEnum.委托还款.getCode());
                    // 记录还款期数
                    debitQueueLog.setRepayTerm(loanRepaymentDetail.getCurrentTerm());
                    saveDebitQueueLog(debitQueueLog);
                }
            } catch (Exception e) {
                logger.error("还款日后一天挂账金额自动分账异常，债权编号：" + loanId, e);
            }
        }
        loanLogService.createLog("executeBillRepayDeal", "info", "还款日后一天挂账金额自动分账处理结束。。。。。。", "SYSTEM");
    }
    
    /**
     * 掉用陆金所代扣接口失败，发送邮件
     * @param debitQueueLogList
     */
    public void sendMailAfterDebitInterface(List<DebitQueueLog> debitQueueLogList){
    	Set<String> set = new HashSet<String>();
		for(DebitQueueLog debitQueueLog : debitQueueLogList){
			Long loanId = debitQueueLog.getLoanId();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			String appNo = vLoanInfo.getAppNo();
			set.add(appNo);
		}
		String receiver = sysParamDefineService.getSysParamValueCache("lufaxMail", "mail.smtp.to");
    	String title = DateTime.now().toString("yyyy-MM-dd")+"陆金所代扣报盘失败提示";
    	String content = DateTime.now().toString("yyyy-MM-dd")+"，以下数据发送陆金所代扣报盘失败，请相关人员及时处理。appNo如下："+set;
    	try {
			sendMailService.sendTextMail(receiver, title, content);
			logger.info("陆金所发送代扣报盘失败,发送邮件结束...");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    }

	@Override
	public Pager findDebitQueueManagementWithPg(DebitQueueManagementVo debitQueueManagementVo) {
		return debitQueueLogDao.findDebitQueueManagementWithPg(debitQueueManagementVo);
	}

    /**
     * 重发代扣通用处理
     * @param debitQueueLogList
     * @param isUpdatePayParty
     */
    public void executeEntrustDebit(List<DebitQueueLog> debitQueueLogList,boolean isUpdatePayParty) {
        Map<String, List<DebitQueueLog>> map = new HashMap<String, List<DebitQueueLog>>();
        for (DebitQueueLog debitQueueLog : debitQueueLogList) {
            String repayType = debitQueueLog.getRepayType();
            if (DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType) || DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)) {
                if (isUpdatePayParty) {
                    // 逾期保证金代偿实时计算代扣罚息，并更新还款账户、代扣金额
                    this.updatePenaltyAmount(debitQueueLog);
                }
            }
            putDebitQUeueLogList2Map(map, repayType, debitQueueLog);
        }
        for (Map.Entry<String, List<DebitQueueLog>> entry : map.entrySet()) {
            String key = entry.getKey();
            if(Strings.isEmpty(key)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "待划扣记录的还款类型不能为空！");
            }
            List<DebitQueueLog> debitLogList = entry.getValue();
            if (DebitRepayTypeEnum.委托还款.getCode().equals(key)) {
                executeEntrustDebit(debitLogList);
            } else if (DebitRepayTypeEnum.逾期代偿.getCode().equals(key)) {
                overdueCompensatory(debitLogList,null);
            } else if (DebitRepayTypeEnum.一次性回购.getCode().equals(key)){
                oneTimeOverdueBuyBack(debitLogList);
            } else if (DebitRepayTypeEnum.提前结清.getCode().equals(key)){
                executeAdvanceClearDebit(debitLogList);
            } else {
                throw new PlatformException(ResponseEnum.FULL_MSG, "此还款类型的划扣记录无须发起代扣！");
            }
        }
    }
    
	/**
	 * 根据还款类型 分类 划扣队列信息
	 * @param map
	 * @param type
	 * @param debitQueueLog
	 */
	public void putDebitQUeueLogList2Map(Map<String, List<DebitQueueLog>> map, String type, DebitQueueLog debitQueueLog){
		if(map.containsKey(type)){
			List<DebitQueueLog> list = map.get(type);
			list.add(debitQueueLog);
		}else{
			List<DebitQueueLog> list = new ArrayList<DebitQueueLog>();
			list.add(debitQueueLog);
			map.put(type,list);
		}
	}


	@Override
	public List<DebitQueueLog> findDebitQueueListByMap(Map<String, Object> params) {
		return debitQueueLogDao.findListByMap(params);
	}

	public DebitQueueLog saveDebitQueueLog(DebitQueueLog debitQueueLog) {
		debitQueueLog.setId(sequencesService.getSequences(SequencesEnum.DEBIT_QUEUE_LOG));
		debitQueueLogDao.insert(debitQueueLog);
		return debitQueueLog;
	}

	@Override
	public String findTradeDateByDebitNo(String debitNo) {
		Map<String, Object> map =  new HashMap<>();
		map.put("debitNo",debitNo);
		Map<String, Object> result = debitQueueLogDao.findTradeDateByDebitNo(map);
		if(result != null && result.containsKey("tradeDate")){
			return result.get("tradeDate").toString();
		}
		return null;
	}
	
    @Override
    public void perpareOverdueCompensatory() {
        // 取当前日期
        Calendar calendar = Calendar.getInstance();
        // 取当前日期的前一天
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        // 日终还款日（1号、16号）
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // 上一期应还款日期
        Date tradeDate = Dates.format(calendar.getTime(), Dates.DEFAULT_DATE_FORMAT);
        // 创建委托还款的垫付明细记录
        this.createEntrustRepaymentCompensatoryDetail(tradeDate, day);
        List<VLoanInfo> loanInfoList = vLoanInfoService.getYQHKLoanByPromisereturnDateAndLoanBelong(day, FundsSourcesTypeEnum.陆金所.getValue());
        for (VLoanInfo loanInfo : loanInfoList) {
            // 债权编号
            Long loanId = loanInfo.getId();
            // 如果债权状态是逾期，且过了还款周期，则结束后续处理
            if (tradeDate.compareTo(loanInfo.getEndrdate()) > 0) {
                continue;
            }
            DebitQueueLog debitQueueLog = new DebitQueueLog();
            // 查询上一期的还款计划信息
            LoanRepaymentDetail currentDetail = afterLoanService.getLast(tradeDate, loanId);
            // 判断是否需要进行一次性回购
            boolean flag = this.isNeedOneTimeBuyBack(loanInfo, tradeDate);
            if (flag) {
                debitQueueLog.setRepayType(DebitRepayTypeEnum.一次性回购.getCode());
                BigDecimal amount = currentDetail.getDeficit().add(currentDetail.getPrincipalBalance());
                debitQueueLog.setAmount(amount);
                debitQueueLog.setDebitNo(LufaxUtil.createAnshuoSerialno());
            } else {
                debitQueueLog.setRepayType(DebitRepayTypeEnum.逾期代偿.getCode());
                debitQueueLog.setDebitType(DebitOperateTypeEnum.当期代偿.getCode());
                debitQueueLog.setAmount(afterLoanService.getCurrentDeficitAmount(tradeDate, loanId));
            }
            debitQueueLog.setId(sequencesService.getSequences(SequencesEnum.DEBIT_QUEUE_LOG));
            debitQueueLog.setLoanId(loanId);
            debitQueueLog.setPayParty(PayPartyEnum.准备金.getCode());
            debitQueueLog.setTradeNo(null);
            debitQueueLog.setDebitNotifyState(DebitNotifyStateEnum.待通知.getCode());
            debitQueueLog.setDebitResultState(DebitResultStateEnum.未划扣.getCode());
            debitQueueLog.setRepayTerm(currentDetail.getCurrentTerm());
            debitQueueLogDao.insert(debitQueueLog);
            
            // 保存逾期代偿明细信息
            compensatoryDetailLufaxService.saveCompensatoryDetailLufax(debitQueueLog, currentDetail);
            
            // 更新陆金所债权状态信息
            LoanStatusLufax lsl = new LoanStatusLufax();
            lsl.setLoanId(loanId);
            List<LoanStatusLufax> loanStatusLufaxList = loanStatusLufaxDao.findListByVo(lsl);
            if(CollectionUtils.isEmpty(loanStatusLufaxList)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"陆金所债权状态信息记录不存在！");
            }
            LoanStatusLufax loanStatusLufax = loanStatusLufaxList.get(0);
            loanStatusLufax.setLoanStatus(LoanStatueLufaxEnum.逾期.getCode());
            loanStatusLufaxDao.update(loanStatusLufax);
        }
    }

    // 判断是否需要一次性回购
    private boolean isNeedOneTimeBuyBack(VLoanInfo vLoanInfo, Date tradeDate) {
        Date currReturnDate = afterLoanService.getCurrTermReturnDate(tradeDate, vLoanInfo.getPromiseReturnDate());
        Map<String, Object> params = new HashMap<>();
        params.put("loanId", vLoanInfo.getId());
        params.put("repaymentStates",new String[] {RepaymentStateEnum.未还款.name(), RepaymentStateEnum.不足额还款.name(), RepaymentStateEnum.不足罚息.name()});
        params.put("CurrTermReturnDate", Dates.format(currReturnDate, Dates.DEFAULT_DATE_FORMAT));
        List<LoanRepaymentDetail> list = loanRepaymentDetailDao.findListByMap(params);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 3) {
            return true;
        }
        return false;
    }

	

    


	/**
	 * 一次性回购代扣
	 * @param list
	 */
	public void executeBuyBackDebitQueueLog(List<DebitQueueLog> debitQueueLogList) {
		if(CollectionUtils.isEmpty(debitQueueLogList)){
			logger.info("陆金所一次性逾期回购代扣,没有需要代扣的数据。。。");
			return;
		}
		Lufax100018Vo vo = buildLufax100018Vo(debitQueueLogList);
		try {
			JSONObject grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.陆金所一次性回购.getCode());
			logger.info("陆金所一次性逾期回购代扣,陆金所返回的数据：" + grantResut);
			String respCode = (String) grantResut.get("ret_code");
			String respDesc = (String) grantResut.get("ret_msg");
			// 安硕批次号
			String anshuoBatchId = vo.getAnshuo_batch_id();
			if ("0000".equals(respCode)) {
				this.updateDebitQueueLogAfterDebitInterface(debitQueueLogList, DebitNotifyStateEnum.通知成功.getCode(), anshuoBatchId);
				logger.info("陆金所一次性逾期回购代扣接口文件交互成功，并已成功通知。" + respDesc);
			} else {
				this.updateDebitQueueLogAfterDebitInterface(debitQueueLogList, DebitNotifyStateEnum.通知失败.getCode(), anshuoBatchId);
				sendMailAfterDebitInterface(debitQueueLogList);
				logger.info("调陆金所一次性逾期回购代扣接口失败：原因：" + respDesc);
			}
		} catch (Exception e) {
			logger.error("陆金所一次性逾期回购调用网关接口失败：" + e.getMessage(), e);
		}
	}

	private Lufax100018Vo buildLufax100018Vo(List<DebitQueueLog> debitQueueLogList) {
		Lufax100018Vo vo = new Lufax100018Vo();
		vo.setInterfaceId(LufaxRepayInterfaceEnum.证大发起一次性回购指令.getCode());
		vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());//安硕批次号
		vo.setLine_sum(String.valueOf(debitQueueLogList.size()));//总条数
		vo.setSign(LufaxUtil.createInterfaceReqId(vo.getInterfaceId()));
		List<BuyBackTradeDetailEntity> tradeDetails = new ArrayList<>();
		vo.setDetail(tradeDetails);
		for(DebitQueueLog debitQueueLog : debitQueueLogList){
			Long loanId = debitQueueLog.getLoanId();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			BuyBackTradeDetailEntity buyBackTrade = new BuyBackTradeDetailEntity();
			buyBackTrade.setProduct_type(IDebitQueueLogService.PRODUCT_TYPE);
			buyBackTrade.setSerialno(debitQueueLog.getDebitNo());//安硕序号
			buyBackTrade.setAnshuo_loan_accept_id(LufaxConst.ZDJR + vLoanInfo.getChannelPushNo());//安硕贷款受理号  ZDJR_channelPushNo
			buyBackTrade.setLufax_loan_req_id(loanBsbMappingDao.getByLoanId(loanId).getOrderNo());//借款申请ID
			// 查询垫付明细
			CompensatoryDetailLufax compensatoryDetailLufax = this.queryCompensatoryDetailLufaxByDebitLog(debitQueueLog);
			BigDecimal corpusBalance = compensatoryDetailLufax.getCorpusAmount().add(compensatoryDetailLufax.getCleanAmount());
			buyBackTrade.setSur_amount(corpusBalance.toString());
			List<BuyBackCompensatoryDetailEntity> compensatoryDetails = new ArrayList<>();
			buyBackTrade.setDetail(compensatoryDetails);
			BuyBackCompensatoryDetailEntity compensatoryDetial = new BuyBackCompensatoryDetailEntity();
			compensatoryDetial.setRpterm(compensatoryDetailLufax.getTerm());
			compensatoryDetial.setCapital(corpusBalance);
			compensatoryDetial.setAint(compensatoryDetailLufax.getAccrualAmount());
			compensatoryDetial.setOint(compensatoryDetailLufax.getPenaltyAmount());
			compensatoryDetial.setFee_amt2(new BigDecimal(0.00));
			compensatoryDetial.setFee_amt3(new BigDecimal(0.00));
			compensatoryDetial.setFee_amt4(new BigDecimal(0.00));
			compensatoryDetial.setFee_amt5(new BigDecimal(0.00));
			compensatoryDetial.setFee_amt6(new BigDecimal(0.00));
			compensatoryDetial.setPenal_value(new BigDecimal(0.00));
			if(PayPartyEnum.准备金.getCode().equals(debitQueueLog.getPayParty())){
				buyBackTrade.setAdvance_party("1"); // 1- 准备金
				compensatoryDetial.setIs_reserve_advance("1"); //1：准备金已代垫
			}else if (PayPartyEnum.保证金.getCode().equals(debitQueueLog.getPayParty())){
				buyBackTrade.setAdvance_party("3"); // 3- 保证金
				compensatoryDetial.setIs_reserve_advance("0");//0:未发生代垫
			}
			compensatoryDetails.add(compensatoryDetial);
			tradeDetails.add(buyBackTrade);
		}
		return vo;
	}

	@Override
    public void batchSaveDebitQueueLog(List<DebitQueueLog> list){
        for(DebitQueueLog data:list){
            data.setPayParty(PayPartyEnum.保证金.getCode());
            data.setUpdateTime(new Date());
            debitQueueLogDao.update(data);
            //TODO
            /*compensatoryDetailLufaxService.saveCompensatoryDetailLufax(data.getLoanId(), data.getId());*/
        }
    }

	@Override
	public void executeEntrustDebitAfterDebitOffer(List<DebitQueueLog> debitQueueLogList) {
		this.executeDebitQueueLogList(debitQueueLogList);
	}

	public Long getRepaymentCurrentTerm(Map<String, Object> params) {
		return debitQueueLogDao.getRepaymentCurrentTerm(params);
	}

	public Map<String, Object> getOverdueRepaymentTerm(Map<String, Object> params) {
		return debitQueueLogDao.getOverdueRepaymentTerm(params);
	}
	
    /**
     * 逾期保证金代偿实时计算代扣罚息，并更新还款账户、代扣金额
     * @param debitQueueLog
     */
    private void updatePenaltyAmount(DebitQueueLog debitQueueLog) {
        // 债权编号
        Long loanId = debitQueueLog.getLoanId();
        // 还款期数
        Long term = debitQueueLog.getRepayTerm();
        // 根据债权编号和还款期数查询对应的还款计划信息
        LoanRepaymentDetail loanRepaymentDetail = this.queryLoanRepaymentDetailByLoanIdAndTerm(loanId, term);
        if(null == loanRepaymentDetail){
            logger.warn("查询还款计划信息不存在，债权编号："+loanId+"，期数："+term);
            return;
        }
        // 计算应扣罚息金额
        BigDecimal fineAmount = afterLoanService.getFine(loanRepaymentDetail,Dates.getCurrDate());
        // 如果应扣罚息小于等于零，即当期代偿还在三个宽限日之内，只需更新划扣队列数据的还款账户字段为：保证金
        if (fineAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        
        // 查询逾期代垫明细记录
        CompensatoryDetailLufax compensatoryDetail = queryCompensatoryDetailLufaxByDebitLog(debitQueueLog);
        // 更新逾期代垫明细记录
        compensatoryDetail.setPenaltyAmount(fineAmount);
        // 更新代垫总金额字段
        BigDecimal totalAmount = compensatoryDetail.getCorpusAmount()
                .add(compensatoryDetail.getAccrualAmount())
                .add(compensatoryDetail.getCleanAmount())
                .add(compensatoryDetail.getPenaltyAmount());
        compensatoryDetail.setTotalAmount(totalAmount);
        compensatoryDetailLufaxDao.update(compensatoryDetail);
        
        // 更新划扣记录的代扣金额字段的值
        debitQueueLog.setAmount(totalAmount);
        debitQueueLogDao.update(debitQueueLog);
    }
    
    /**
     * 根据债权编号和期数查询对应的还款计划
     * @param loanId
     * @param term
     * @return
     */
    private LoanRepaymentDetail queryLoanRepaymentDetailByLoanIdAndTerm(Long loanId,Long term){
        LoanRepaymentDetail searchVo = new LoanRepaymentDetail();
        searchVo.setLoanId(loanId);
        searchVo.setCurrentTerm(term);
        List<LoanRepaymentDetail> loanRepaymentDetailLufaxList = loanRepaymentDetailDao.findListByVo(searchVo);
        if(CollectionUtils.isEmpty(loanRepaymentDetailLufaxList)){
            return null;
        }
        return loanRepaymentDetailLufaxList.get(0);
    }
    
    /**
     * 根据划扣队列记录查询代垫明细记录
     * @param debitQueueLog
     * @return
     */
    private CompensatoryDetailLufax queryCompensatoryDetailLufaxByDebitLog(DebitQueueLog debitQueueLog){
        // 查询逾期代垫明细记录
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("debitQueueId", debitQueueLog.getId());
        List<CompensatoryDetailLufax> compensatoryDetailList = compensatoryDetailLufaxDao.findListByMap(params);
        if(CollectionUtils.isEmpty(compensatoryDetailList)){
            return null;
        }
        return compensatoryDetailList.get(0);
    }
    
    
    /**
     * 根据loanId和term查询代垫明细记录
     * @param debitQueueLog
     * @return
     */
    private List<CompensatoryDetailLufax> findCompensatoryDetailLufaxList(DebitQueueLog debitQueueLog){
        // 查询逾期代垫明细记录  
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanId", debitQueueLog.getLoanId());
        params.put("term", debitQueueLog.getRepayTerm());
        List<CompensatoryDetailLufax> compensatoryDetailList = compensatoryDetailLufaxDao.findListByMap(params);
        if(CollectionUtils.isEmpty(compensatoryDetailList)){
            return null;
        }
        return compensatoryDetailList;
    }
    
    /**
     * 根据债权编号查询一次性回购代垫明细记录
     * @param loanId
     * @return
     */
    public CompensatoryDetailLufax queryBuyBackCompensatoryDetailLufax(Long loanId){
        // 查询一次性回购代垫明细记录
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanId", loanId);
        // 垫付类型是 02：一次性回购
        params.put("type", "02");
        List<CompensatoryDetailLufax> compensatoryDetailList = compensatoryDetailLufaxDao.findListByMap(params);
        if(CollectionUtils.isEmpty(compensatoryDetailList)){
            return null;
        }
        return compensatoryDetailList.get(0);
    }

    public void updateDebitQueueLog(DebitQueueLog debitQueueLog) {
        debitQueueLogDao.update(debitQueueLog);
    }
    
    /**
     * 创建委托还款的垫付明细记录
     *
     * @param tradeDate
     * @param day
     */
    private void createEntrustRepaymentCompensatoryDetail(Date tradeDate, int day) {
        // 查询未划扣的委托还款记录（查询条件是：创建时间小于等于tradeDate、划扣状态是：未划扣、还款类型是：委托还款）
        Map<String, Object> map = new HashMap<String, Object>();
        // 得到今天的时间 时间小于当天时间，只推送小于今天的数据给陆金所，进行划扣
        String toyday = Dates.getDateTime(Dates.addDay(tradeDate, 1), Dates.DEFAULT_DATE_FORMAT);
        map.put("createDate", toyday);
        map.put("debitResultStates", new String[]{DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣失败.getCode()});
        map.put("repayTypes", new String[]{DebitRepayTypeEnum.委托还款.getCode()});
        List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByMap(map);
        if(CollectionUtils.isEmpty(debitQueueLogList)){
            return;
        }
        for (DebitQueueLog log : debitQueueLogList) {
            // 债权编号
            Long loanId = log.getLoanId();
            // 查询对应债权信息
            VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
            // 每月2号只生成1号端委托还款的垫付明细信息、每月17号则生成16号端委托还款的垫付明细信息
            if(day != loanInfo.getPromiseReturnDate().intValue()) {
                continue;
            }
            // 还款交易流水号
            String tradeNo = log.getTradeNo();
            // 还款对应期数
            Long repayTerm = log.getRepayTerm();
            // 查询此笔委托还款是否已经生成过垫付明细记录、如果是，则不需要重新再生成
            CompensatoryDetailLufax resultCompensatoryDetailLufax = this.queryCompensatoryDetailLufaxByDebitLog(log);
            if(null != resultCompensatoryDetailLufax){
                continue;
            }
            // 查询委托还款对应的还款计划
            LoanRepaymentDetail repaymentDetail = this.queryLoanRepaymentDetailByLoanIdAndTerm(loanId, repayTerm);
            // 查询正常还款或一次性结清还款分账的本金（不包含逾期还款分账的本金、利息）
            BigDecimal principalAmount = offerFlowService.queryNormalPrincipalAmount(tradeNo, repaymentDetail);
            // 查询正常还款或一次性结清还款分账的利息（不包含逾期还款分账的本金、利息）
            BigDecimal interestAmount = offerFlowService.queryNormalInterestAmount(tradeNo, repaymentDetail);
            // 本金利息之和
            BigDecimal splitAmt = principalAmount.add(interestAmount);
            // 创建委托还款的垫付明细信息
            CompensatoryDetailLufax compensatoryDetailLufax = new CompensatoryDetailLufax();
            compensatoryDetailLufax.setLoanId(loanId);
            compensatoryDetailLufax.setTerm(repayTerm.intValue());
            compensatoryDetailLufax.setType(CompensatoryTypeEnum.委托还款.getCode());
            compensatoryDetailLufax.setTradeDate(tradeDate);
            compensatoryDetailLufax.setTotalAmount(splitAmt);
            compensatoryDetailLufax.setCorpusAmount(principalAmount);
            compensatoryDetailLufax.setAccrualAmount(interestAmount);
            compensatoryDetailLufax.setPenaltyAmount(BigDecimal.ZERO);
            compensatoryDetailLufax.setCleanAmount(BigDecimal.ZERO);
            compensatoryDetailLufax.setDebitQueueId(log.getId());
            compensatoryDetailLufaxService.saveCompensatoryDetailLufax(compensatoryDetailLufax);
        }
    }

	public DebitQueueLog findPrimaryDebitQueueLog(List<DebitQueueLog> debitQueueLogList) {
		if (CollectionUtils.isEmpty(debitQueueLogList)) {
			return null;
		}
		// 划扣类型
		String repayType = null;
		DebitQueueLog minDebitQueueLog = debitQueueLogList.get(0);
		for (DebitQueueLog log : debitQueueLogList) {
			repayType = log.getRepayType();
			if (DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)
					|| DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)
					|| DebitRepayTypeEnum.提前结清.getCode().equals(repayType)
					|| DebitRepayTypeEnum.机构还款.getCode().equals(repayType)) {
				return log;
			}
			if (log.getId().compareTo(minDebitQueueLog.getId()) <= 0) {
				minDebitQueueLog = log;
			}
		}
		return minDebitQueueLog;
	}
}
