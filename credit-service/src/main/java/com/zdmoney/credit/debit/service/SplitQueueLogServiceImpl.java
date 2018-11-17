package com.zdmoney.credit.debit.service;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.service.pub.IRepayResultNotifyLogService;
import com.zdmoney.credit.common.constant.CompensatoryTypeEnum;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.DebitRepayTypeEnum;
import com.zdmoney.credit.common.constant.DebitResultStateEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanStatueLufaxEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.PayOffTypeEnum;
import com.zdmoney.credit.common.constant.PayPartyEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.RepaymentStateLufaxEnum;
import com.zdmoney.credit.common.constant.SplitNotifyStateEnum;
import com.zdmoney.credit.common.constant.SplitResultStateEnum;
import com.zdmoney.credit.common.constant.lufax.LufaxConst;
import com.zdmoney.credit.common.constant.lufax.LufaxRepayInterfaceEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.AccountTradeTypeEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.domain.OperateLog;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.service.pub.IOperateLogService;
import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.LufaxUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.core.LoanVo;
import com.zdmoney.credit.common.vo.core.OneBuyBackCompensatedVO;
import com.zdmoney.credit.common.vo.core.RepayInfoLufaxVO;
import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitQueueLogDao;
import com.zdmoney.credit.debit.dao.pub.IDebitTransactionDao;
import com.zdmoney.credit.debit.dao.pub.ISplitQueueLogDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100010Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100010Vo.Fee;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100010Vo.RepayPlanLufax;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100011Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100011Vo.FeeDetail;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100011Vo.RepayDetailChildLufax;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100011Vo.RepayDetailLufax;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100012Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100012Vo.RepayRecordLufax;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100019Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100019Vo.CompensateRepayRecord;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100020Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100020Vo.CompensateRepayPlan;
import com.zdmoney.credit.framework.vo.lufax.output.Lufax100018OutputVo;
import com.zdmoney.credit.framework.vo.lufax.output.Lufax100018OutputVo.LoanPlan;
import com.zdmoney.credit.ljs.dao.pub.ICompensatoryDetailLufaxDao;
import com.zdmoney.credit.ljs.dao.pub.ILoanStatusLufaxDao;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;
import com.zdmoney.credit.ljs.domain.LoanStatusLufax;
import com.zdmoney.credit.ljs.service.pub.ILoanStatusLufaxService;
import com.zdmoney.credit.ljs.service.pub.IPublicAccountDetailService;
import com.zdmoney.credit.ljs.vo.SplitRepaymentVo;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailLufaxDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetailLufax;
import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseGrantService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanContractService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailLufaxService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.service.pub.ILoanReturnRecordService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 
 * @author YM10112
 *
 */
 
@Service
public class SplitQueueLogServiceImpl implements ISplitQueueLogService {
	
	private static final Logger logger = Logger.getLogger(SplitQueueLogServiceImpl.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final String PRODUCT_TYPE = "1000500010"; //陆金所业务类型 
	private static final BigDecimal zero = new BigDecimal("0.00");
	private static final String OVERDUE_RETURN = "overdue";//逾期还回
	
	@Autowired
	ISplitQueueLogDao splitQueueLogDao;
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	IOfferFlowService offerFlowService;
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired
	ILoanContractService loanContractService;
	@Autowired
	IPersonInfoService personInfoService;
	@Autowired
	ILoanRepaymentDetailLufaxDao loanRepaymentDetailLufaxDao;
	@Autowired
	private ILoanRepaymentDetailLufaxService loanRepaymentDetailLufaxService;
	@Autowired
	private ILoanRepaymentDetailService loanRepaymentDetailService;
	@Autowired
	ILoanBsbMappingDao loanBsbMappingDao;
	@Autowired
	ILoanBankService loanBankService;
	@Autowired
	IDebitQueueLogDao debitQueueLogDao;
	@Autowired
	IDebitQueueLogService debitQueueLogService;
	@Autowired
    private IPublicAccountDetailService publicAccountDetailService;
	@Autowired
	ISendMailService sendMailService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanStatusLufaxDao loanStatusLufaxDao;
	@Autowired
	private IRepayResultNotifyLogService repayResultNotifyLogService;
	@Autowired
	private IDebitTransactionDao debitTransactionDao;
	@Autowired
	private IDebitBaseInfoDao debitBaseInfoDao;
	@Autowired
    private ICompensatoryDetailLufaxDao compensatoryDetailLufaxDao;
	@Autowired
	private IAfterLoanService afterLoanService;
	@Autowired
	private ILoanCoreService loanCoreService;
	@Autowired
	ILoanStatusLufaxService loanStatusLufaxService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IOperateLogService OperateLogService;
	@Autowired
    private ILoanBaseService loanBaseService;
	@Autowired
	private ILoanReturnRecordService loanReturnRecordService;
	@Autowired
	IWorkDayInfoService workDayInfoServiceImpl;
	@Autowired
	IOfferTransactionService offerTransactionServiceImpl;
	@Autowired
    private IFinanceGrantService financeGrantServiceImpl;
    @Autowired
    private ILoanBaseGrantService loanBaseGrantService;
	
	@Override
	public SplitQueueLog createSplitQueueLog(Long loanId, String debitNo, String lufaxNo, String batchId, BigDecimal frozenAmount) {
	    logger.info("※※※※※※※※※※开始保存分账队列表：loanId"+loanId);
		SplitQueueLog splitQueueLog = new SplitQueueLog();
		splitQueueLog.setId(sequencesService.getSequences(SequencesEnum.SPLIT_QUEUE_LOG));
		splitQueueLog.setLoanId(loanId);
		splitQueueLog.setDebitNo(debitNo);//划扣表中的 debit_queue_log.debit_no(同一天的loanId,重复)
		splitQueueLog.setSplitNotifyState(SplitNotifyStateEnum.待通知.getCode());
		splitQueueLog.setSplitResultState(SplitResultStateEnum.未分账.getCode());
		splitQueueLog.setPayOffType(PayOffTypeEnum.未结清.getCode());//未结算 
		splitQueueLog.setSplitNo(lufaxNo);//lufax还款请求号
		splitQueueLog.setBatchId(batchId);//转发代扣时的安硕批次号
		splitQueueLog.setFrozenAmount(frozenAmount); //冻结金额
		splitQueueLogDao.insert(splitQueueLog);
		logger.info("※※※※※※※※※※保存分账队列表成功："+JSON.toJSONString(splitQueueLog));
		return splitQueueLog;
	}
	
	/**
	 * 同步完还款计划，明细，记录后，更新分账队列表
	 * @param repayInfoList
	 */
	public void updateSplitQueueLogAfterSysncRepaymentInfo(List<RepayInfoLufaxVO> repayInfoList,String splitNotifyState,String splitResultState){
		for(RepayInfoLufaxVO vo :repayInfoList){
			SplitQueueLog splitQueueLog = new SplitQueueLog();
			splitQueueLog.setId(vo.getSplitId());
			splitQueueLog.setSplitNotifyState(splitNotifyState);
			splitQueueLog.setSplitNotifyDate(new Date());
			splitQueueLog.setSplitResultState(splitResultState);
			splitQueueLog.setSplitResultDate(new Date());
			if(splitResultState.equals(SplitResultStateEnum.分账成功.getCode())){
				splitQueueLog.setPayOffType(PayOffTypeEnum.已结清.getCode());//已结算
			}
			splitQueueLogDao.update(splitQueueLog);
		}
	}
	
	/**
	 * 逾期还回和委托还款同步完分账后，更新分账表
	 * @param repayInfoList
	 */
	public void updateSplitQueueLogInOverdueEntrust(List<RepayInfoLufaxVO> repayInfoList,String splitNotifyState,String splitResultState){
		for(RepayInfoLufaxVO vo :repayInfoList){
			SplitQueueLog splitQueueLog = new SplitQueueLog();
			splitQueueLog.setId(vo.getSplitId());
			splitQueueLog.setSplitNotifyState(splitNotifyState);
			splitQueueLog.setSplitNotifyDate(new Date());
			splitQueueLog.setSplitResultState(splitResultState);
			splitQueueLog.setSplitResultDate(new Date());
			splitQueueLog.setSendEntrustFlag("2");//分账失败
			if(splitResultState.equals(SplitResultStateEnum.分账成功.getCode())){
				splitQueueLog.setPayOffType(PayOffTypeEnum.已结清.getCode());//已结算
				splitQueueLog.setSendEntrustFlag("1");//分账成功
			}
			splitQueueLogDao.update(splitQueueLog);
		}
	}
	
	/**
	 * 推送完分账信息后，更新还款计划表中的 陆金所还款时间 为当前时间
	 * @param repayInfoList
	 */
	public void updateLoanRepaymentDetailLufax(List<RepayInfoLufaxVO> repayInfoList){
		for(RepayInfoLufaxVO repayInfo :repayInfoList){
			Long loanId = repayInfo.getLoanId();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			Date currTermReturnDate = afterLoanService.getCurrTermReturnDate(repayInfo.getTradeDate(), vLoanInfo.getPromiseReturnDate());//得到应还款日期
			LoanRepaymentDetailLufax loanRepaymentDetailLufax = loanRepaymentDetailLufaxService.findByLoanAndReturnDate(vLoanInfo,Dates.format(currTermReturnDate,Dates.DEFAULT_DATE_FORMAT));//获取当期还款明细
			//如果是提前结清的情况，需要把当期到最后一期的这个字段全部更新掉
			if(repayInfo.getRepayType().equals(DebitRepayTypeEnum.提前结清.getCode())){
				 Long currentTerm = loanRepaymentDetailLufax.getCurrentTerm();
		         Map<String, Object> map = new HashMap<String, Object>();
		         map.put("loanId", loanId);
		         map.put("currentTerm", currentTerm);
		         map.put("repayDateLufax", Dates.getDateTime(new Date(),Dates.DEFAULT_DATETIME_FORMAT));
		         loanRepaymentDetailLufaxDao.updateRepaymentDetailLufaxByLoanId(map);
			}else if(repayInfo.getRepayType().equals(DebitRepayTypeEnum.委托还款.getCode()) 
					|| repayInfo.getRepayType().equals(DebitRepayTypeEnum.机构还款.getCode()) 
					|| repayInfo.getRepayType().equals(DebitRepayTypeEnum.逾期代偿.getCode())
					|| repayInfo.getRepayType().equals(DebitRepayTypeEnum.一次性回购.getCode())){
				loanRepaymentDetailLufax.setRepayDateLufax(new Date());
				loanRepaymentDetailLufaxDao.update(loanRepaymentDetailLufax);
			}
		}
	}
	
	public void setBaseParamsMap(Map<String,Object> map){
		map.put("splitNotifyStates",new String[]{SplitNotifyStateEnum.待通知.getCode(),SplitNotifyStateEnum.通知失败.getCode()});
		map.put("splitResultStates",new String[]{SplitResultStateEnum.未分账.getCode(),SplitResultStateEnum.分账失败.getCode()});
		map.put("acctTitles", new String[]{Const.ACCOUNT_TITLE_INTEREST_EXP,Const.ACCOUNT_TITLE_LOAN_AMOUNT});//451   211
	}
	
	@Override
	public void executeOrganRepaymentInfo(HashSet<String> splitIds) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(CollectionUtils.isNotEmpty(splitIds) && splitIds.size() >0){
			map.put("splitIds",splitIds);
		}
		setBaseParamsMap(map);
		map.put("payParty", PayPartyEnum.借款人.getCode());
		map.put("repayTypes", new String[]{DebitRepayTypeEnum.机构还款.getCode()});
		List<RepayInfoLufaxVO> repayInfoList = splitQueueLogDao.findRepaymentInfoToLufax(map);
		if(CollectionUtils.isEmpty(repayInfoList)){
			logger.warn("没有待分账的【机构还款】的信息要同步给陆金所");
			return;
		}
		syncLufaxRepaymentInfo(repayInfoList);
	}
	
	@Override
	public void executeEntrustRepaymentInfo(HashSet<String> splitIds) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(CollectionUtils.isNotEmpty(splitIds) && splitIds.size() >0){
			map.put("splitIds",splitIds);
		}
		setBaseParamsMap(map);
		map.put("repayTypes", new String[]{DebitRepayTypeEnum.委托还款.getCode()});
		List<RepayInfoLufaxVO> repayInfoList = splitQueueLogDao.findRepaymentInfoToLufax(map);
		if(CollectionUtils.isEmpty(repayInfoList)){
			logger.warn("没有待分账的【委托还款】信息要同步给陆金所");
			return;
		}
		syncLufaxRepaymentInfo(repayInfoList);
	}

	@Override
	public void executeAdvanceClearRepaymentInfo(HashSet<String> splitIds) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(CollectionUtils.isNotEmpty(splitIds) && splitIds.size() >0){
			map.put("splitIds",splitIds);
		}
		List<RepayInfoLufaxVO> repayInfoList = splitQueueLogDao.findAdvanceEntrustSplitQueueLogList(map);
		if(CollectionUtils.isEmpty(repayInfoList)){
			logger.warn("没有待分账的【提前结清/委托还款】的信息要同步给陆金所");
			return;
		}
		syncLufaxRepaymentInfo(repayInfoList);
	}
	
	
	@Override
    public void syncOverdueCompensatoryInfo(HashSet<String> splitIds) {
        Map<String,Object> map = new HashMap<String, Object>();
        if(CollectionUtils.isNotEmpty(splitIds) && splitIds.size() >0){
			map.put("splitIds",splitIds);
		}
        setBaseParamsMap(map);
        map.put("repayTypes", new String[]{DebitRepayTypeEnum.委托还款.getCode(),DebitRepayTypeEnum.逾期代偿.getCode()});
        map.put("types", new String[]{CompensatoryTypeEnum.委托还款.getCode(),CompensatoryTypeEnum.逾期代偿.getCode()});
        List<RepayInfoLufaxVO> repayInfoList = splitQueueLogDao.findRepayInfoLufaxList(map);
        if(CollectionUtils.isEmpty(repayInfoList)){
        	logger.warn("没有待分账的【逾期代偿/委托还款】信息要同步给陆金所");
			return;
        }
        syncLufaxRepaymentInfo(repayInfoList);
    }

	@Override
	public void executeOneBuyBackRepaymentInfo(HashSet<String> splitIds) {
		Map<String,Object> map = new HashMap<String, Object>();
        if(CollectionUtils.isNotEmpty(splitIds) && splitIds.size() >0){
			map.put("splitIds",splitIds);
		}
        map.put("repayTypes", new String[]{DebitRepayTypeEnum.一次性回购.getCode()});
        List<RepayInfoLufaxVO> repayInfoList = splitQueueLogDao.findRepayInfoLufaxList(map);
        if(CollectionUtils.isEmpty(repayInfoList)){
        	logger.warn("没有待分账的一次性回购信息要同步给陆金所");
			return;
        }
        syncLufaxRepaymentPlanRecord(repayInfoList);
	}
	
	public void executeOverdueRepaymentInfo(HashSet<String> splitIds) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(splitIds != null && splitIds.size() >0){
			map.put("splitIds",splitIds);
		}
		List<RepayInfoLufaxVO> repayInfoList = splitQueueLogDao.findDebitSplitQueueLogList(map);
		if(CollectionUtils.isEmpty(repayInfoList)){
			logger.warn("没有待分账的【逾期还回/委托还款】信息要同步给陆金所");
			return;
		}
		syncLufaxOverdueRepaymentInfo(repayInfoList);
	}
	
	/**
	 * 掉用还款计划，明细，记录的接口
	 * @param repayInfoList
	 * @return
	 */
	public void syncLufaxRepaymentInfo(List<RepayInfoLufaxVO> repayInfoList){
		try{
			boolean repayPlan = syncLufaxRepaymentPlanInterface(repayInfoList);//证大同步还款计划给陆金所
			boolean repayDetail = syncLufaxRepaymentDetailInterface(repayInfoList);//证大同步还款明细给陆金所
			boolean repayRecord = syncLufaxRepaymentRecordInterface(repayInfoList);//证大同步还款记录给陆金所
			if(repayPlan && repayDetail && repayRecord){
				updateSplitQueueLogAfterSysncRepaymentInfo(repayInfoList,SplitNotifyStateEnum.通知成功.getCode(),SplitResultStateEnum.分账成功.getCode());//同步完还款计划，明细，记录后，更新分账队列表
				updateLoanRepaymentDetailLufax(repayInfoList);//更新还款计划表中的 陆金所还款时间 为当前时间
			}else{
				updateSplitQueueLogAfterSysncRepaymentInfo(repayInfoList,SplitNotifyStateEnum.通知成功.getCode(),SplitResultStateEnum.分账失败.getCode());//同步完还款计划，明细，记录后，更新分账队列表
			}
		}catch(PlatformException pe){
			logger.info("同步陆金所还款计划，明细，记录接口失败："+pe.getMessage(),pe);
		}
	}
	
	/**
	 * 一次性回购掉用还款计划，记录的接口
	 * @param repayInfoList
	 * @return
	 */
	public void syncLufaxRepaymentPlanRecord(List<RepayInfoLufaxVO> repayInfoList){
		try{
			boolean repayPlan = syncLufaxRepaymentPlanInterface(repayInfoList);//证大同步还款计划给陆金所
			boolean repayRecord = syncLufaxRepaymentRecordInterface(repayInfoList);//证大同步还款记录给陆金所
			if(repayPlan && repayRecord){
				updateSplitQueueLogAfterSysncRepaymentInfo(repayInfoList,SplitNotifyStateEnum.通知成功.getCode(),SplitResultStateEnum.分账成功.getCode());//同步完还款计划，明细，记录后，更新分账队列表
				updateLoanRepaymentDetailLufax(repayInfoList);//更新还款计划表中的 陆金所还款时间 为当前时间
			}else{
				updateSplitQueueLogAfterSysncRepaymentInfo(repayInfoList,SplitNotifyStateEnum.通知成功.getCode(),SplitResultStateEnum.分账失败.getCode());//同步完还款计划，明细，记录后，更新分账队列表
			}
		}catch(PlatformException pe){
			logger.info("一次性回购同步陆金所还款计划，记录接口失败："+pe.getMessage(),pe);
		}
	}
	
	/**
	 * 逾期还回，同步还款计划和还款记录信息
	 * @param repayInfoList
	 * @return
	 */
	public void syncLufaxOverdueRepaymentInfo(List<RepayInfoLufaxVO> repayInfoList){
		try{
			// 根据债权编号合并还款记录，同一笔债权存在多笔还款记录，只取第一条
			List<RepayInfoLufaxVO> resultList = this.mergeRepayInfoListByLoanId(repayInfoList);
			boolean repayPlan = syncLufaxRepaymentPlanOverdueInterface(resultList);//证大同步还款计划给陆金所  逾期还回专用 
			boolean repayRecord = syncLufaxRepaymentRecordOverdueInterface(repayInfoList);//证大同步还款记录给陆金所  逾期还回专用
			if(repayPlan && repayRecord){
				updateSplitQueueLogInOverdueEntrust(repayInfoList, SplitNotifyStateEnum.通知成功.getCode(), SplitResultStateEnum.分账成功.getCode());//同步完还款计划，明细，更新分账队列表
			}else{
				updateSplitQueueLogInOverdueEntrust(repayInfoList,SplitNotifyStateEnum.通知成功.getCode(),SplitResultStateEnum.分账失败.getCode());//同步完还款计划，明细，更新分账队列表
			}
		}catch(PlatformException pe){
			updateSplitQueueLogInOverdueEntrust(repayInfoList,SplitNotifyStateEnum.通知失败.getCode(),SplitResultStateEnum.分账失败.getCode());//同步完还款计划，明细，更新分账队列表
			logger.info("同步陆金所还款计划，记录 失败："+pe.getMessage(),pe);
		}
	}
	
	/**
	 * 证大同步还款计划给陆金所   Lufax100010Vo
	 * @param vLoanInfoList
	 */
	public boolean syncLufaxRepaymentPlanInterface(List<RepayInfoLufaxVO> repayInfoList){
		logger.info("【陆金所】同步还款计划接口被调用Lufax100010Vo...");
		Lufax100010Vo vo = getLufax100010Vo(repayInfoList,null);
		JSONObject grantResut = null;
		try{
			 grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步还款计划给陆金所.getCode());
		}catch(Exception e){
			logger.warn("【陆金所】同步还款计划接口调用网关接口失败："+e.getMessage(),e);
    		throw new PlatformException(ResponseEnum.FULL_MSG,"【陆金所】同步还款计划接口调用网关接口失败:"+e.getMessage());
		}
		logger.info("【陆金所】同步还款计划接口,陆金所返回的数据：" + grantResut);
		String ret_code = (String)grantResut.get("ret_code");
		String ret_msg = (String)grantResut.get("ret_msg");
		if("0000".equals(ret_code)){
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步还款计划接口成功！"+ret_msg);
			return true;
		}else{
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "调用【陆金所】同步还款计划接口失败：原因：" + ret_msg);
			return false;
		}
	}
	
	/**
	 * 逾期还回  - 证大同步还款计划给陆金所   Lufax100010Vo
	 * @param vLoanInfoList
	 */
	public boolean syncLufaxRepaymentPlanOverdueInterface(List<RepayInfoLufaxVO> repayInfoList){
		logger.info("【逾期还回-陆金所】同步还款计划接口被调用Lufax100010Vo...");
		Lufax100010Vo vo = getLufax100010Vo(repayInfoList,OVERDUE_RETURN);//逾期还回 
		JSONObject grantResut = null;
		try{
			 grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步还款计划给陆金所.getCode());
		}catch(Exception e){
			logger.warn("【逾期还回-陆金所】同步还款计划接口调用网关接口失败："+e.getMessage(),e);
    		throw new PlatformException(ResponseEnum.FULL_MSG,"【逾期还回-陆金所】同步还款计划接口调用网关接口失败:"+e.getMessage());
		}
		logger.info("【逾期还回-陆金所】同步还款计划接口,陆金所返回的数据：" + grantResut);
		String ret_code = (String)grantResut.get("ret_code");
		String ret_msg = (String)grantResut.get("ret_msg");
		if("0000".equals(ret_code)){
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "调用【逾期还回-陆金所】同步还款计划接口成功！"+ret_msg);
			return true;
		}else{
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "调用【逾期还回-陆金所】同步还款计划接口失败：原因：" + ret_msg);
			return false;
		}
	}
	
	/**
	 * 封装lufax100010
	 * @param list
	 */
	private Lufax100010Vo getLufax100010Vo(List<RepayInfoLufaxVO> repayInfoList,String type){
		Lufax100010Vo lufax100010Vo = new Lufax100010Vo();
		lufax100010Vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());//N
		lufax100010Vo.setInterface_id(LufaxRepayInterfaceEnum.证大同步还款计划给陆金所.getCode());
		lufax100010Vo.setInterface_reqser(LufaxUtil.createInterfaceReqId(lufax100010Vo.getInterface_id()));
		lufax100010Vo.setSign(LufaxUtil.createInterfaceReqId(lufax100010Vo.getInterface_id()));   //待定
		List<RepayPlanLufax> repayPlanLufaxList = new ArrayList<RepayPlanLufax>();
		for(RepayInfoLufaxVO repayInfo :repayInfoList){
			Long loanId = repayInfo.getLoanId();
			String splitNo = repayInfo.getLufax_repay_req_no();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			//String appNO = vLoanInfo.getAppNo();
			String channelPushNo = vLoanInfo.getChannelPushNo();
			LoanBank loanBank = loanBankService.findById(vLoanInfo.getGiveBackBankId());
			//同步还款计划时间戳 对于同一笔借款必须相同
			String bus_operation_time= Dates.getDateTime(new Date(), "yyyyMMddHHmmssSSS");
			// 根据债权编号查询一次性回购代垫明细记录
			CompensatoryDetailLufax buyBackCompensatoryDetail = debitQueueLogService.queryBuyBackCompensatoryDetailLufax(loanId);
			List<LoanRepaymentDetailLufax> loanRepaymentDetailLufaxList = loanRepaymentDetailLufaxDao.findByLoanId(loanId);
			for (LoanRepaymentDetailLufax loanRepayment : loanRepaymentDetailLufaxList) {
				RepayPlanLufax item = new RepayPlanLufax();
				item.setId_seq_no(loanRepayment.getId().toString());
				item.setLoan_no(LufaxConst.ZDJR + channelPushNo);
				item.setProduct_type(PRODUCT_TYPE);//业务类型
				item.setAnshuo_loan_accept_id(LufaxConst.ZDJR + channelPushNo);//安硕贷款受理号
				item.setLufax_loan_req_id(loanBsbMappingDao.getByLoanId(loanId).getOrderNo());//借款申请ID
				item.setPayment_accno(loanBank.getAccount());//还款帐号  待定 give_bank_id
				item.setPflg(loanRepayment.getLoanStatusLufax());//还款标志  待定
				item.setRpterm(String.valueOf(loanRepayment.getCurrentTerm()));//还款期数     待定 还款计划的当期期数
				item.setWrpdte(sdf.format(loanRepayment.getReturnDate()));//应还款日 
				item.setValue_date(Dates.getDateTime(Dates.addMonths(loanRepayment.getReturnDate(), -1), Dates.DATAFORMAT_YYYYMMDD));//起息日 yyyyMMdd  当期还款日 - 一个月
				if(loanRepayment.getCurrentTerm() == 1){//期数为第一期  则 起息日 = 签约日期
					item.setValue_date(Dates.getDateTime(vLoanInfo.getSignDate(),Dates.DATAFORMAT_YYYYMMDD));
				}
				item.setExpiry_date(Dates.getDateTime(Dates.addDay(loanRepayment.getReturnDate(), -1), Dates.DATAFORMAT_YYYYMMDD));//结息日 yyyyMMdd 当期还款日 - 1天
				// 当期已还本金
				BigDecimal corpusAmount = zero;
				// 当期已还利息
				BigDecimal accrualAmount = zero;
				// 当期已还金额
				BigDecimal repayAmount = loanRepayment.getReturneterm().subtract(loanRepayment.getDeficit());
				if(repayAmount.compareTo(loanRepayment.getCurrentAccrual()) >=0){
					accrualAmount = loanRepayment.getCurrentAccrual();
					corpusAmount = repayAmount.subtract(accrualAmount);
				} else {
					accrualAmount = repayAmount;
				}
				item.setPayprincipalamt(corpusAmount);//实还本金 
				item.setCapital(loanRepayment.getReturneterm().subtract(loanRepayment.getCurrentAccrual()));//应还本金
				item.setPayinteamt(accrualAmount);//实还利息
				item.setAint(loanRepayment.getCurrentAccrual());//应还利息 
				
				// 陆金所还款计划状态（pflg）
				String loanStatusLufax = loanRepayment.getLoanStatusLufax() == null ? "" : loanRepayment.getLoanStatusLufax();
				// 针对逾期代偿的做特殊处理  实还本金 = 应还本金  ,实还利息 = 应还利息 
				if (RepaymentStateLufaxEnum.当期逾期代垫.getCode().equals(loanStatusLufax) // 逾期代偿（准备金）、非最后一期
						|| RepaymentStateLufaxEnum.当期逾期保证金代垫.getCode().equals(loanStatusLufax) // 逾期代偿（保证金）、非最后一期
						|| RepaymentStateLufaxEnum.代偿结清.getCode().equals(loanStatusLufax) // 逾期代偿（准备金）、最后一期
						|| RepaymentStateLufaxEnum.保证金代偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.证大代偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.证大一次性结清.getCode().equals(loanStatusLufax)) { // 逾期代偿（保证金）、最后一期
					// 实还本金等于应还本金
					item.setPayprincipalamt(loanRepayment.getReturneterm().subtract(loanRepayment.getCurrentAccrual()));
					// 实还利息等于应还利息
					item.setPayinteamt(loanRepayment.getCurrentAccrual());
				}
				
				if(DebitRepayTypeEnum.一次性回购.getCode().equals(repayInfo.getRepayType())
						&& loanRepayment.getCurrentTerm().compareTo(repayInfo.getTerm())>0){
					// 应还利息等于零
					item.setAint(zero);
					// 实还利息等于零
					item.setPayinteamt(zero);
				}
				
				if(DebitRepayTypeEnum.逾期还回.getCode().equals(repayInfo.getRepayType())){
					// 回购这一期到最后一期的实还本金和实还利息都为零
					if(null != buyBackCompensatoryDetail && loanRepayment.getCurrentTerm().intValue() > buyBackCompensatoryDetail.getTerm().intValue()){
						// 应还利息等于零
						item.setAint(zero);
						// 实还利息等于零
						item.setPayinteamt(zero);
					}
				}
				//应还罚息
				BigDecimal deserveFine  = zero;
				//实还罚息
				BigDecimal actualFine = zero;
				BigDecimal totalAmount = loanRepayment.getReturneterm();
				//还款总额 = 实还本金 + 实还利息 + 实还罚息
				/*if (RepaymentStateLufaxEnum.当期逾期代垫.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.当期逾期保证金代垫.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.代偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.保证金代偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.追偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.当期追偿完成.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.证大代偿结清.getCode().equals(loanStatusLufax)) {
					List<BigDecimal> fines = setActualAndDeserveFine(loanRepayment.getLoanId(), loanRepayment.getCurrentTerm(), loanRepayment.getReturnDate());
					actualFine = fines.get(0);
					deserveFine = fines.get(1);
					totalAmount = item.getPayprincipalamt().add(item.getPayinteamt()).add(actualFine);
				}*/
				// 查询逾期代偿或者一次性回购超过三个宽限日所垫付的罚息
				actualFine = this.queryCompensatoryPenaltyAmount(loanId, loanRepayment.getCurrentTerm());
				// 应还罚息等于实还罚息
				deserveFine = actualFine;
				// 还款总额加上垫付的罚息
				totalAmount = totalAmount.add(actualFine);
				item.setTotal_amount(totalAmount);
				item.setPayfineamt(actualFine);//实还罚息
				item.setOint(deserveFine);//应还罚息  N
				item.setPaycompound(zero);//应还复利
				item.setActual_paycompound(zero);//实还复利
				item.setFine_base(zero);//罚息积数
				item.setCompound_base(zero);//复利积数
				item.setFine_calculate_date("");//罚息计算日期yyyyMMdd
				item.setCompound_calculate_date("");//复利计算日期yyyyMMdd
				// 本金余额等于当期本金余额加上当期未还的本金
				//BigDecimal principalBalance = loanRepayment.getPrincipalBalance().add(item.getCapital().subtract(corpusAmount));
				item.setPrincipal_balance(loanRepayment.getPrincipalBalance());//本金余额
				item.setTerm_amt(totalAmount);//期供金额
				item.setFinish_date("");//结清日期yyyyMMdd   提前结清、结清才更新 factFactReturnDate
				if(RepaymentStateEnum.结清.name().equals(loanRepayment.getRepaymentState())){
					item.setFinish_date(Dates.getDateTime(loanRepayment.getFactReturnDate(), "yyyyMMdd"));
				}
				item.setInsureamount(zero);//每期保费      待定
				item.setFee1(zero);//费用1   N
				item.setFee2(zero);//费用2   N
				item.setRrpdte(loanRepayment.getFactReturnDate()== null ? "" : sdf.format(loanRepayment.getFactReturnDate()));//实际还款日   N
				if (RepaymentStateLufaxEnum.部分还款.getCode().equals(loanRepayment.getLoanStatusLufax())){
					item.setRrpdte(sdf.format(repayInfo.getTradeDate()));
				}
	        	item.setFee_amt2(zero);//平台管理费	N
	        	item.setFee_amt3(zero);//信安咨询费	N
	        	item.setFee_amt4(zero);//小贷管理费	N
	        	item.setFee_amt5(zero);//风险准备金	N
	        	item.setFee_amt6(zero);//逾期违约金	N
	        	item.setOrate(vLoanInfo.getPenaltyRate());//逾期利率
	        	item.setRate(vLoanInfo.getRateey());//贷款利率
	        	item.setBus_operation_time(bus_operation_time);//同步还款计划时间戳 还款计划变更时间
	        	item.setLufax_repay_req_no(splitNo);//lufax还款请求号   来自分账队列表    待定
	        	if(Strings.isNotEmpty(type) && OVERDUE_RETURN.equals(type)){//逾期还回 追偿 设置为空 
	        		item.setLufax_repay_req_no("");
	        	}
	        	
	        	List<Fee> fee_list = new ArrayList<Fee>();
	        	/*Fee fee = new Fee();
	        	fee.setId_seq_no("");
	        	fee.setActual_payfeeamt("");
	        	fee.setFee_code("");
	        	fee.setPayfeeamt("");
	        	fee_list.add(fee);*/
	        	item.setFee_list(fee_list);
	        	
				repayPlanLufaxList.add(item);
			}
		}
		lufax100010Vo.setLine_sum(repayPlanLufaxList.size());
		lufax100010Vo.setDetail(repayPlanLufaxList);
		return lufax100010Vo;
	}

	/**
	 * 获取陆金所逾期还款计划
	 * @param list
	 * @return
	 */
	public Lufax100010Vo getLufax100010VoByRepaymentPlan(List<VLoanInfo> list) {
		Lufax100010Vo lufax100010Vo = new Lufax100010Vo();
		lufax100010Vo.setLine_sum(list.size());
		lufax100010Vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());//N
		lufax100010Vo.setInterface_id(LufaxRepayInterfaceEnum.证大同步还款计划给陆金所.getCode());
		lufax100010Vo.setInterface_reqser(LufaxUtil.createInterfaceReqId(lufax100010Vo.getInterface_id()));
		lufax100010Vo.setSign(LufaxUtil.createInterfaceReqId(lufax100010Vo.getInterface_id()));   //待定
		List<RepayPlanLufax> repayPlanLufaxList = new ArrayList<RepayPlanLufax>();
		for(VLoanInfo vLoanInfo: list){
			try {
				//String appNO = vLoanInfo.getAppNo();
				String channelPushNo = vLoanInfo.getChannelPushNo();
				LoanBank loanBank = loanBankService.findById(vLoanInfo.getGiveBackBankId());
				//同步还款计划时间戳 对于同一笔借款必须相同
				String bus_operation_time = Dates.getDateTime(new Date(), "yyyyMMddHHmmssSSS");
				String lufax_loan_req_id = loanBsbMappingDao.getByLoanId(vLoanInfo.getId()).getOrderNo();
				List<LoanRepaymentDetailLufax> pList = loanRepaymentDetailLufaxDao.findByLoanId(vLoanInfo.getId());
				for (LoanRepaymentDetailLufax loanRepayment : pList) {
					BigDecimal actualFine = zero;
					BigDecimal deserveFine = zero;
					List<BigDecimal> fines = setActualAndDeserveFine(loanRepayment.getLoanId(), loanRepayment.getCurrentTerm(), loanRepayment.getReturnDate());
					actualFine = fines.get(0);
					deserveFine = fines.get(1);
					RepayPlanLufax item = new RepayPlanLufax();
					item.setId_seq_no(loanRepayment.getId().toString());
					item.setLoan_no(LufaxConst.ZDJR + channelPushNo);
					item.setProduct_type(PRODUCT_TYPE);//业务类型
					item.setAnshuo_loan_accept_id(LufaxConst.ZDJR + channelPushNo);//安硕贷款受理号
					item.setLufax_loan_req_id(lufax_loan_req_id);//借款申请ID
					item.setPayment_accno(loanBank.getAccount());//还款帐号  待定 give_bank_id
					item.setPflg(loanRepayment.getLoanStatusLufax());//还款标志  待定
					item.setRpterm(String.valueOf(loanRepayment.getCurrentTerm()));//还款期数     待定 还款计划的当期期数
					item.setWrpdte(sdf.format(loanRepayment.getReturnDate()));//应还款日
					item.setValue_date(Dates.getDateTime(Dates.addMonths(loanRepayment.getReturnDate(), -1), Dates.DATAFORMAT_YYYYMMDD));//起息日 yyyyMMdd  当期还款日 - 一个月
					if (loanRepayment.getCurrentTerm() == 1) {//期数为第一期  则 起息日 = 签约日期
						item.setValue_date(Dates.getDateTime(vLoanInfo.getSignDate(), Dates.DATAFORMAT_YYYYMMDD));
					}
					item.setExpiry_date(Dates.getDateTime(Dates.addDay(loanRepayment.getReturnDate(), -1), Dates.DATAFORMAT_YYYYMMDD));//结息日 yyyyMMdd 当期还款日 - 1天

					// 当期已还本金
					BigDecimal corpusAmount = zero;
					// 当期已还利息
					BigDecimal accrualAmount = zero;
					// 当期已还金额
					BigDecimal repayAmount = loanRepayment.getReturneterm().subtract(loanRepayment.getDeficit());
					if (repayAmount.compareTo(loanRepayment.getCurrentAccrual()) >= 0) {
						accrualAmount = loanRepayment.getCurrentAccrual();
						corpusAmount = repayAmount.subtract(accrualAmount);
					} else {
						accrualAmount = repayAmount;
					}
					item.setPayprincipalamt(corpusAmount);//实还本金
					item.setCapital(loanRepayment.getReturneterm().subtract(loanRepayment.getCurrentAccrual()));//应还本金
					item.setPayinteamt(accrualAmount);//实还利息
					item.setAint(loanRepayment.getCurrentAccrual());//应还利息
					// 陆金所还款计划状态（pflg）
					String loanStatusLufax = loanRepayment.getLoanStatusLufax() == null ? "" : loanRepayment.getLoanStatusLufax();
					// 针对逾期代偿的做特殊处理  实还本金 = 应还本金  ,实还利息 = 应还利息
					if (RepaymentStateLufaxEnum.当期逾期代垫.getCode().equals(loanStatusLufax) // 逾期代偿（准备金）、非最后一期
							|| RepaymentStateLufaxEnum.当期逾期保证金代垫.getCode().equals(loanStatusLufax) // 逾期代偿（保证金）、非最后一期
							|| RepaymentStateLufaxEnum.代偿结清.getCode().equals(loanStatusLufax) // 逾期代偿（准备金）、最后一期
							|| RepaymentStateLufaxEnum.保证金代偿结清.getCode().equals(loanStatusLufax)
							|| RepaymentStateLufaxEnum.证大代偿结清.getCode().equals(loanStatusLufax)
							|| RepaymentStateLufaxEnum.证大一次性结清.getCode().equals(loanStatusLufax)) { // 逾期代偿（保证金）、最后一期
						// 实还本金等于应还本金
						item.setPayprincipalamt(loanRepayment.getReturneterm().subtract(loanRepayment.getCurrentAccrual()));
						// 实还利息等于应还利息
						item.setPayinteamt(loanRepayment.getCurrentAccrual());
					}
					item.setPayfineamt(actualFine);//实还罚息
					item.setOint(deserveFine);//应还罚息  N
					item.setPaycompound(zero);//应还复利
					item.setActual_paycompound(zero);//实还复利
					item.setFine_base(zero);//罚息积数
					item.setCompound_base(zero);//复利积数
					item.setFine_calculate_date("");//罚息计算日期yyyyMMdd
					item.setCompound_calculate_date("");//复利计算日期yyyyMMdd
					// 本金余额等于当期本金余额加上当期未还的本金
					//BigDecimal principalBalance = loanRepayment.getPrincipalBalance().add(item.getCapital().subtract(corpusAmount));
					item.setPrincipal_balance(loanRepayment.getPrincipalBalance());//本金余额
					item.setFinish_date("");//结清日期yyyyMMdd   提前结清、结清才更新 factFactReturnDate
					if (RepaymentStateEnum.结清.name().equals(loanRepayment.getRepaymentState())) {
						item.setFinish_date(Dates.getDateTime(loanRepayment.getFactReturnDate(), "yyyyMMdd"));
					}
					item.setInsureamount(zero);//每期保费      待定
					item.setFee1(zero);//费用1   N
					item.setFee2(zero);//费用2   N
					item.setRrpdte(loanRepayment.getRepayDateLufax() == null ? "" : sdf.format(loanRepayment.getRepayDateLufax()));//实际还款日   N
					item.setTotal_amount(loanRepayment.getReturneterm().add(actualFine));//  还款总额 = 应还金额 + 实还罚息
					// 还款总额 = 实还本金 + 实还利息 + 实还罚息
				/*if (RepaymentStateLufaxEnum.当期逾期代垫.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.当期逾期保证金代垫.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.代偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.保证金代偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.追偿结清.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.当期追偿完成.getCode().equals(loanStatusLufax)
						|| RepaymentStateLufaxEnum.证大代偿结清.getCode().equals(loanStatusLufax)) {
					item.setTotal_amount(item.getPayprincipalamt().add(item.getPayinteamt()).add(item.getPayfineamt()));
				}*/
					item.setTerm_amt(item.getTotal_amount());//期供金额
					item.setFee_amt2(zero);//平台管理费	N
					item.setFee_amt3(zero);//信安咨询费	N
					item.setFee_amt4(zero);//小贷管理费	N
					item.setFee_amt5(zero);//风险准备金	N
					item.setFee_amt6(zero);//逾期违约金	N
					item.setOrate(vLoanInfo.getPenaltyRate());//逾期利率
					item.setRate(vLoanInfo.getRateey());//贷款利率
					item.setBus_operation_time(bus_operation_time);//同步还款计划时间戳 还款计划变更时间
					item.setLufax_repay_req_no("");//lufax还款请求号   来自分账队列表    待定

					List<Fee> fee_list = new ArrayList<Fee>();
					item.setFee_list(fee_list);
					repayPlanLufaxList.add(item);
				}
			}catch (Exception e){
				throw new PlatformException(ResponseEnum.FULL_MSG,"债权编号："+vLoanInfo.getId()+"，获取逾期还款计划异常");
			}
		}

		lufax100010Vo.setDetail(repayPlanLufaxList);
		return lufax100010Vo;
	}

	/**
	 * 获取陆金所 逾期实还罚息 应还罚息
	 * @param loanId
	 * @param currentTerm
	 * @param returnDate
	 * @param actualFine
	 * @param deserveFine
	 */
	private List<BigDecimal> setActualAndDeserveFine(Long loanId, Long currentTerm, Date returnDate) {
		BigDecimal deserveFine = zero;
		BigDecimal actualFine = zero;
		Map<String, String> repayParams = new HashMap<>();
		repayParams.put("loanId", loanId.toString());
		repayParams.put("returnDate", Dates.getDateTime(returnDate, Dates.DEFAULT_DATE_FORMAT));
		LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailService.findRepaymentDetailByLoanAndReturnDate(repayParams);
		Map<String, Object> flowParams = new HashMap<>();
		flowParams.put("loanId", loanId);
		flowParams.put("memo2", currentTerm);
		flowParams.put("acctTitle", Const.ACCOUNT_TITLE_FINE_EXP);
		List<OfferFlow> offerFlows = offerFlowService.findOverDueOfferFlow4Lufax(flowParams);

		//实还罚息 为实际逾期还款金额
		if(CollectionUtils.isNotEmpty(offerFlows)){
			for(OfferFlow offerFlow:offerFlows){
				actualFine = actualFine.add(offerFlow.getTradeAmount());
			}
		}
		deserveFine = afterLoanService.getFine(loanRepaymentDetail, Dates.getCurrDate());
		//应还罚息为 实还金额+逾期金额
		deserveFine = deserveFine.add(actualFine);
		List<BigDecimal> list  = new ArrayList<>();
		list.add(actualFine);
		list.add(deserveFine);
		return list;
	}


	/**
	 * 证大同步还款明细给陆金所   Lufax100011Vo
	 * @param vLoanInfoList
	 */
	public boolean syncLufaxRepaymentDetailInterface(List<RepayInfoLufaxVO> list){
		logger.info("【陆金所】同步还款明细接口被调用Lufax100011Vo...");
		Lufax100011Vo vo = getLufax100011Vo(list);
		JSONObject grantResut = null;
		try{
			 grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步还款明细给陆金所.getCode());
		}catch(Exception e){
			logger.warn("【陆金所】同步还款明细接口调用网关接口失败："+e.getMessage(),e);
    		throw new PlatformException(ResponseEnum.FULL_MSG,"【陆金所】同步还款明细接口调用网关接口失败:"+e.getMessage());
		}
		logger.info("【陆金所】同步还款明细接口,陆金所返回的数据：" + grantResut);
		String ret_code = (String) grantResut.get("ret_code");
		String ret_msg = (String)grantResut.get("ret_msg");
		if("0000".equals(ret_code)){
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步还款明细接口成功！"+ret_msg);
			return true;
		}else{
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步还款记录接口失败：原因："+ret_msg);
			return false;
		}
	}	
	
	
	/**
	 * 封装lufax100011
	 * @param list
	 */
	private Lufax100011Vo getLufax100011Vo(List<RepayInfoLufaxVO> list) {
		Lufax100011Vo lufax100011Vo = new Lufax100011Vo();
		lufax100011Vo.setLine_sum(new BigDecimal(list.size()));
		lufax100011Vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());
		lufax100011Vo.setInterface_id(LufaxRepayInterfaceEnum.证大同步还款明细给陆金所.getCode());
		lufax100011Vo.setInterface_reqser(LufaxUtil.createInterfaceReqId(lufax100011Vo.getInterface_id()));
		lufax100011Vo.setSign(LufaxUtil.createInterfaceReqId(lufax100011Vo.getInterface_id()));
		List<RepayDetailLufax> detail = new ArrayList<RepayDetailLufax>();
		for(RepayInfoLufaxVO repayInfo : list){
			Long loanId = repayInfo.getLoanId();
			Long repayTerm = repayInfo.getTerm();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			LoanRepaymentDetailLufax loanRepaymentDetailLufax = getLoanRepaymentDetailLufaxByIdAndTerm(loanId,repayTerm);
			RepayDetailLufax item = new RepayDetailLufax();
			item.setId_seq_no(LufaxConst.ZDJR+LufaxUtil.createAnshuoSerialno());
			item.setLoan_no(LufaxConst.ZDJR + vLoanInfo.getChannelPushNo());
			LoanBsbMapping loanBsbMapping = loanBsbMappingDao.getByLoanId(loanId);
			item.setLufax_loan_req_id(loanBsbMapping.getOrderNo());//借款申请ID
			item.setLufax_borrower_username(loanBsbMapping.getBusNumber());//借款人lufax ID
			item.setInsureamount(zero);//担保费/保险费	N
			// 查询逾期代偿或者一次性回购超过三个宽限日所垫付的罚息
			BigDecimal oint = this.queryCompensatoryPenaltyAmount(loanId, repayTerm);
			item.setOint(oint);//罚息N
			item.setInte(repayInfo.getAint());//利息N
			item.setCapital(repayInfo.getCapital());//本金N
			item.setInsuremanagement_fee(zero);//担保管理费N
			item.setPenal_value(zero);//提前还款违约金N
			item.setSubrogation_fee(zero);//追偿款N
			item.setLate_fee(zero);//滞纳金N
			item.setThaw_amount(zero);//解冻金额N
			item.setRefer_fee(zero);//咨询服务费N
			item.setFee1(zero);//费用1N
			item.setFee2(zero);//费用2N
			item.setSumamount(repayInfo.getAint().add(repayInfo.getCapital()).add(oint));//分配金额和   本金+利息+罚息待定
			item.setObjectno(LufaxUtil.createAnshuoSerialno());//安硕明细流水号 随机生成
			item.setAnshuobatchid(repayInfo.getAnshuobatchid());//安硕发起批扣批次号   待定   转发代扣接口的安硕批次号
			item.setSerialno(repayInfo.getDebitNo());//安硕序号  待定   转发代扣接口的安硕序号   intege 类型
			item.setProduct_type(PRODUCT_TYPE);//业务类型  
			item.setOther_fee_amt1(zero);//提前还款P2P平台管理费
			item.setReserve_advance_amt(zero);//准备金代偿款
			item.setMicro_loan_advance_amt(zero);//小贷代偿款
			item.setLufax_repay_req_no(repayInfo.getLufax_repay_req_no());//lufax还款请求号
			
			List<FeeDetail>  feeDetailList = new ArrayList<FeeDetail>();
			
			List<RepayDetailChildLufax> detailChild= new ArrayList<RepayDetailChildLufax>();
			RepayDetailChildLufax itemChild = new RepayDetailChildLufax();
			itemChild.setId_seq_no(LufaxConst.ZDJR+LufaxUtil.createAnshuoSerialno());
			itemChild.setSerial_no(repayInfo.getDebitNo());//序列号	Y  待定
			itemChild.setRelation_key(repayInfo.getDebitNo());//新增 		与还款记录的关联主键
			itemChild.setActual_paydate(Dates.getDateTime(repayInfo.getTradeDate(),"yyyyMMdd"));//新增   	实际还款日
			itemChild.setRpterm(loanRepaymentDetailLufax.getCurrentTerm().intValue());//还款期次 	 Y
			itemChild.setCapital(repayInfo.getCapital());//本金      N
			itemChild.setAint(repayInfo.getAint());//利息  N
			itemChild.setOint(oint);//罚息  N
			itemChild.setInsureamount(zero);//担保费 N
			itemChild.setFee1(zero);//费用1 N
			itemChild.setFee2(zero);//费用2  N
			itemChild.setSubrogation_fee(zero);//代偿款  N
			itemChild.setLate_fee(zero);//延迟付款违约金(滞纳金)N
			itemChild.setFee_amt2(zero);//平台管理费	N
			itemChild.setFee_amt3(zero);//信安咨询费	N
			itemChild.setFee_amt4(zero);//小贷管理费	N
			itemChild.setFee_amt5(zero);//风险准备金	N
			itemChild.setFee_amt6(zero);//逾期违约金	N
			itemChild.setMoney_allocation("0");//代垫方  N P2P公司产品使用字段（0：无 1：准备金 2：小贷）
			itemChild.setReserve_advance_amt(zero);//准备金代偿款	N
			itemChild.setMicro_loan_advance_amt(zero);//小贷代偿款	N
			detailChild.add(itemChild);
			
			item.setFee_list(feeDetailList);
			item.setBack_detail(detailChild);//list集合
			detail.add(item);
		}
		lufax100011Vo.setDetail(detail);
		return lufax100011Vo;
	}
	
	/**
	 * 证大同步还款记录给陆金所   Lufax100012Vo
	 * @param vLoanInfoList
	 */
	public boolean syncLufaxRepaymentRecordInterface(List<RepayInfoLufaxVO> list){
		logger.info("【陆金所】同步还款记录接口被调用Lufax100012Vo...");
		Lufax100012Vo vo = getLufax100012Vo(list,null);
		JSONObject grantResut = null;
		try{
			 grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步还款记录给陆金所.getCode());
		}catch(Exception e){
			logger.warn("【陆金所】同步还款记录接口调用网关接口失败："+e.getMessage(),e);
    		throw new PlatformException(ResponseEnum.FULL_MSG,"【陆金所】同步还款记录接口调用网关接口失败:"+e.getMessage());
		}
		logger.info("【陆金所】同步还款记录接口,陆金所返回的数据：" + grantResut);
		String ret_code = (String) grantResut.get("ret_code");
		String ret_msg = (String)grantResut.get("ret_msg");
		if("0000".equals(ret_code)){
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步还款记录接口成功！"+ret_msg);
			return true;
		}else{
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步还款记录接口失败：原因："+ret_msg);
			return false;
		}
	}

	/**
	 * 逾期还回专用  证大同步还款记录给陆金所   Lufax100012Vo
	 * @param vLoanInfoList
	 */
	public boolean syncLufaxRepaymentRecordOverdueInterface(List<RepayInfoLufaxVO> list){
		logger.info("【逾期还回-陆金所】同步还款记录接口被调用Lufax100012Vo...");
		Lufax100012Vo vo = getLufax100012Vo(list,OVERDUE_RETURN);
		JSONObject grantResut = null;
		try{
			 grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步还款记录给陆金所.getCode());
		}catch(Exception e){
			logger.warn("【逾期还回-陆金所】同步还款记录接口调用网关接口失败："+e.getMessage(),e);
    		throw new PlatformException(ResponseEnum.FULL_MSG,"【陆金所】同步还款记录接口调用网关接口失败:"+e.getMessage());
		}
		logger.info("【逾期还回-陆金所】同步还款记录接口,陆金所返回的数据：" + grantResut);
		String ret_code = (String) grantResut.get("ret_code");
		String ret_msg = (String)grantResut.get("ret_msg");
		if("0000".equals(ret_code)){
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【逾期还回-陆金所】同步还款记录接口成功！"+ret_msg);
			return true;
		}else{
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【逾期还回-陆金所】同步还款记录接口失败：原因："+ret_msg);
			return false;
		}
	}
	
	/**
	 * 封装lufax100012
	 * @param list
	 * @return
	 */
	private Lufax100012Vo getLufax100012Vo(List<RepayInfoLufaxVO> list,String type) {
		Lufax100012Vo lufax100012Vo = new Lufax100012Vo();
		lufax100012Vo.setLine_sum(list.size());
		lufax100012Vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());
		lufax100012Vo.setInterface_id(LufaxRepayInterfaceEnum.证大同步还款记录给陆金所.getCode());
		lufax100012Vo.setInterface_reqser(LufaxUtil.createInterfaceReqId(lufax100012Vo.getInterface_id()));
		lufax100012Vo.setSign(LufaxUtil.createInterfaceReqId(lufax100012Vo.getInterface_id()));
		List<RepayRecordLufax> detail= new ArrayList<RepayRecordLufax>();
		for(RepayInfoLufaxVO repayInfo : list){
			BigDecimal capital = repayInfo.getCapital() == null? zero:repayInfo.getCapital();
			BigDecimal aint = repayInfo.getAint() == null? zero:repayInfo.getAint();
			Long loanId = repayInfo.getLoanId();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			//String appNo = vLoanInfo.getAppNo();
			// 最终申请单号
			String channelPushNo = vLoanInfo.getChannelPushNo();
			Long repayTerm = repayInfo.getTerm();
			if(Strings.isEmpty(repayTerm)){
				logger.debug("推送还款记录，获取期次为空。");
				continue;
			}
			LoanRepaymentDetailLufax loanRepaymentDetailLufax = getLoanRepaymentDetailLufaxByIdAndTerm(loanId,repayTerm);
			RepayRecordLufax item = new RepayRecordLufax();
			item.setId_seq_no(LufaxConst.ZDJR+LufaxUtil.createAnshuoSerialno());
			item.setLoan_no(LufaxConst.ZDJR + channelPushNo);
			BigDecimal actualFine = zero;
			// 还款类型
			String repayType = repayInfo.getRepayType();
			if(DebitRepayTypeEnum.机构还款.getCode().equals(repayType)){
				item.setTrans_code("NOM_REPAY");// 正常还款
			} else if(DebitRepayTypeEnum.提前结清.getCode().equals(repayType)) {
				item.setTrans_code("ADVANCE");// 提前还款
			} else if(DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType) 
					|| DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)){
			    item.setTrans_code("CLM_REPAY");// 代偿还款
			    // 查询逾期代偿或者一次性回购超过三个宽限日所垫付的罚息
				actualFine = this.queryCompensatoryPenaltyAmount(loanId, repayTerm);
			}else if(DebitRepayTypeEnum.逾期还回.getCode().equals(repayType)){
				item.setTrans_code("RECOVERY");// 追偿还款
				// 查询逾期代偿或者一次性回购超过三个宽限日所垫付的罚息
				if(ifNeedCalcActualFine(repayInfo)){
					actualFine = this.queryCompensatoryPenaltyAmount(loanId, repayTerm);
				}
			}else if(DebitRepayTypeEnum.委托还款.getCode().equals(repayType)){
				item.setTrans_code("CLM_REPAY");// 代偿还款
				if(Strings.isNotEmpty(type) && OVERDUE_RETURN.equals(type)){
					item.setTrans_code("RECOVERY");// 追偿还款
				}
			}
			item.setProduct_type(PRODUCT_TYPE);//业务类型
			item.setAnshuo_loan_accept_id(LufaxConst.ZDJR + channelPushNo);//安硕贷款受理号 待定 
			item.setLufax_loan_req_id(loanBsbMappingDao.getByLoanId(loanId).getOrderNo());//借款申请ID  待定 
			item.setRpterm(Integer.valueOf(loanRepaymentDetailLufax.getCurrentTerm().toString()));//还款期数 待定
			item.setWrpdte(sdf.format(loanRepaymentDetailLufax.getReturnDate()));//应还款日
			item.setRate(vLoanInfo.getRateey());//贷款利率
			item.setOrate(vLoanInfo.getPenaltyRate());//逾期利率
			item.setCapital(capital);//本金
			item.setAint(aint);//利息
			item.setOint(actualFine);//实还罚息	N
			item.setFee1(zero);//费用1	N
			item.setFee2(zero);//费用2	N
			item.setInsureamount(zero);//每期保费   待定 
			item.setRrpdte(sdf.format(new Date()));//实际还款日   N
			item.setInsuremanagement_fee(zero);//担保管理费  待定
			item.setPenal_value(zero);//提前还款违约金	N
			item.setSubrogation_fee(zero);//追偿款	N
			item.setLate_fee(zero);//滞纳金  	N
			item.setTotal_amount(capital.add(aint).add(actualFine));//还款总额
			//item.setRepayment_type("1");
			// 还款类型  "1":代扣还款 "2":手动还款"3":代偿还款"4":追偿还款"5":准备金代垫 "7":代垫追偿还款
			if (DebitRepayTypeEnum.机构还款.getCode().equals(repayType)
					|| DebitRepayTypeEnum.提前结清.getCode().equals(repayType)) {
				item.setRepayment_type("1");// 代扣还款
			}
			if (DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)) {
				item.setRepayment_type("5"); // 代垫还款
			}
			if (DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)) {
				item.setRepayment_type("3"); // 代偿还款
			}
			if (DebitRepayTypeEnum.逾期还回.getCode().equals(repayType)) {
				item.setRepayment_type("7");// 代垫追偿还款
			}
			if(DebitRepayTypeEnum.委托还款.getCode().equals(repayType)){
				item.setRepayment_type("5");// 代垫还款
				if(Strings.isNotEmpty(type) && OVERDUE_RETURN.equals(type)){
					item.setRepayment_type("7");// 代垫追偿还款
				}
			}
			item.setSerial_no(LufaxUtil.createAnshuoSerialno());//序列号	待定
			item.setOther_fee_amt1(zero);//提前还款平台管理费	N
			item.setFee_amt2(zero);//平台管理费  	N
			item.setFee_amt3(zero);//信安咨询费	N
			item.setFee_amt4(zero);//小贷管理费	N
			item.setFee_amt5(zero);//风险准备金	N
			item.setFee_amt6(zero);//逾期违约金	N
			item.setRepayment_party("0");// 1：借款人（个人账户）、2：服务公司（对公还款）、3：准备金（代偿、回购）、4：保证金（代偿、回购）
			if(DebitRepayTypeEnum.逾期代偿.getCode().equals(repayInfo.getRepayType()) 
				|| DebitRepayTypeEnum.一次性回购.getCode().equals(repayInfo.getRepayType())){
				item.setRepayment_party("3");
			}
			if(DebitRepayTypeEnum.委托还款.getCode().equals(repayInfo.getRepayType()) && !OVERDUE_RETURN.equals(type)){
				item.setRepayment_party("3");
			}
			item.setReserve_advance_amt(zero);//准备金代偿款N
			item.setMicro_loan_advance_amt(zero);//小贷代偿款   N
			item.setLufax_repay_req_no(repayInfo.getLufax_repay_req_no());//lufax还款请求号	//待定
			if(Strings.isNotEmpty(type) && OVERDUE_RETURN.equals(type)){//逾期还回 追偿 设置为空 
        		item.setLufax_repay_req_no("");
        	}
			detail.add(item);
		}
		lufax100012Vo.setDetail(detail);
		return lufax100012Vo;
	}

	//是否需要推送罚息
	private boolean ifNeedCalcActualFine(RepayInfoLufaxVO repayInfo) {
		boolean res = true;
		String debitNo = repayInfo.getDebitNo();
		DebitQueueLog debitQueueLog = new DebitQueueLog();
		debitQueueLog.setLoanId(repayInfo.getLoanId());
		debitQueueLog.setRepayTerm(repayInfo.getTerm());
		debitQueueLog.setRepayType(DebitRepayTypeEnum.逾期还回.getCode());
		List<DebitQueueLog> list = debitQueueLogDao.findListByVo(debitQueueLog);
		//若当期不是第一次逾期还款 则不需要推送
		for(DebitQueueLog dql : list){
			if(dql.getDebitNo().compareTo(debitNo) < 0){
				res = false;
				break;
			}
		}
		return res;
	}

	/**
     * 接收机构还款、委托还款、提前结清、逾期准备金代偿的划扣结果
     * @param repayInfoList
     */
    public void disposeDeductResultNotifyFromLufax(List<RepayInfoLufaxVO> repayInfoList) {
    	StringBuffer strBuffer = new StringBuffer();
        for (RepayInfoLufaxVO repayInfo : repayInfoList) {
            DebitQueueLog searchVo = new DebitQueueLog();
            searchVo.setBatchId(repayInfo.getAnshuobatchid());
            searchVo.setDebitNo(repayInfo.getSerialno());
            // 根据批次号和debit_no查询一次发送的债权
            List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByVo(searchVo);
            if (CollectionUtils.isEmpty(debitQueueLogList)) {
                logger.warn("查询不到对应的划扣队列信息，batchId：" + repayInfo.getAnshuobatchid() + "debitNo：" + repayInfo.getSerialno());
                continue;
            }
            // 判断多笔划扣中是否存在委托还款的划扣
            boolean isEntrust = this.existEntrustRepayment(debitQueueLogList);
            DebitQueueLog debitQueueLog = debitQueueLogList.get(0);
            // 安硕序号 
            String serialno = repayInfo.getSerialno();
            // 安硕批次号
            String anshuo_batch_id = repayInfo.getAnshuobatchid();
            if (DebitResultStateEnum.划扣成功.getCode().equals(debitQueueLog.getDebitResultState())) {
                logger.warn("【陆金所代扣结果通知-回盘】批次号：" + anshuo_batch_id + ",安硕序号:" + serialno + "，已经成功划扣通知！");
                continue;
            }
            // 冻结金额
            String frozen_amount = repayInfo.getFrozen_amount();
            BigDecimal frozenAmount = frozen_amount == "" ? new BigDecimal(0.0) : new BigDecimal(frozen_amount);
            BigDecimal amount = new BigDecimal(0.0);
            for (DebitQueueLog log : debitQueueLogList) {
                amount = amount.add(log.getAmount());
            }
            String repayType = debitQueueLog.getRepayType();
            // 陆金所返回请求号
            String lufax_repay_req_no = repayInfo.getLufax_repay_req_no();
            Long loanId = debitQueueLog.getLoanId();
            // 交易时间
            Date tradeDate = Dates.format(debitQueueLog.getCreateTime(), Dates.DEFAULT_DATE_FORMAT);
            // 查询债权信息
            VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
            String transactionState = null;
            String transactionRtnCode = null;
            String baseInfoState = null;
            boolean flag = true;
            // 如果为正常还款或提前还款 且扣款类型为 借款人(1)
            if (DebitRepayTypeEnum.机构还款.getCode().equals(repayType)) {
                // 如果陆金所返回的冻结金额《=0，则表示划扣失败
                if (frozenAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    flag = false;
                    transactionState = OfferTransactionStateEnum.扣款失败.getValue();
                    transactionRtnCode = ReturnCodeEnum.交易失败.getCode();
                    baseInfoState = OfferStateEnum.已回盘非全额.getValue();
                } else if (frozenAmount.compareTo(amount) < 0) {
                    transactionState = OfferTransactionStateEnum.扣款成功.getValue();
                    transactionRtnCode = ReturnCodeEnum.交易部分成功.getCode();
                    baseInfoState = OfferStateEnum.已回盘非全额.getValue();
                } else if (frozenAmount.compareTo(amount) == 0) {
                    transactionState = OfferTransactionStateEnum.扣款成功.getValue();
                    transactionRtnCode = ReturnCodeEnum.交易成功.getCode();
                    baseInfoState = OfferStateEnum.已回盘全额.getValue();
                }
            } else if (DebitRepayTypeEnum.委托还款.getCode().equals(repayType) 
                    || DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)
                    || DebitRepayTypeEnum.提前结清.getCode().equals(repayType)) {
                // 委托还款 逾期代偿  提前结清 报盘金额和回盘金额必须相等，否则回盘失败
                if (frozenAmount.compareTo(amount) != 0) {
                    flag = false;
                }
            }

            if (flag) {// 划扣成功
                if (DebitRepayTypeEnum.机构还款.getCode().equals(repayType)) {
                    // 更新报盘流水信息
                    changeDebitInfoState(debitQueueLogList, transactionState,transactionRtnCode, baseInfoState, frozenAmount);
                    // 保存消费队列表
                    saveRepayResultNotifyLog(loanId, lufax_repay_req_no, serialno, tradeDate, frozenAmount);
                }
                // 更新划扣表
                changeDebitQueueLogState(serialno, DebitResultStateEnum.划扣成功.getCode());
                // 更新还款计划表
                changeLoanRepaymentDetailLufax(debitQueueLog,frozenAmount);
                // 委托还款 提前结清 逾期代偿 一次性回购 时才 保存对公账户
                if (DebitRepayTypeEnum.委托还款.getCode().equals(repayType) 
                        || DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)
                        || DebitRepayTypeEnum.提前结清.getCode().equals(repayType)
                        ) {
                    // 保存对公账户明细
                    savePublicAccountDetail(loanInfo, frozenAmount, serialno,repayType);
                    SplitQueueLog splitQueueLog = new SplitQueueLog();
                    splitQueueLog.setLoanId(loanId);
                    splitQueueLog.setDebitNo(serialno);// 划扣表中的 debit_queue_log.debit_no(同一天的loanId,重复)
                    splitQueueLog.setSplitNotifyState(SplitNotifyStateEnum.待通知.getCode());
                    splitQueueLog.setSplitResultState(SplitResultStateEnum.未分账.getCode());
                    splitQueueLog.setPayOffType(PayOffTypeEnum.未结清.getCode());// 未结算
                    splitQueueLog.setSplitNo(lufax_repay_req_no);// lufax还款请求号
                    splitQueueLog.setBatchId(debitQueueLog.getBatchId());// 转发代扣时的安硕批次号
                    splitQueueLog.setFrozenAmount(frozenAmount);
                    splitQueueLog.setSendEntrustFlag(isEntrust?"0":"");
                    this.createSplitQueueLog(splitQueueLog);
                }
            } else {// 划扣失败
                if (DebitRepayTypeEnum.机构还款.getCode().equals(repayType)) {
                    // 更新报盘流水表
                    changeDebitInfoState(debitQueueLogList, transactionState,transactionRtnCode, baseInfoState, frozenAmount);
                }
                // 更新划扣表
                changeDebitQueueLogState(serialno, DebitResultStateEnum.划扣失败.getCode());
                // 发送划扣失败预警邮件
                if(!DebitRepayTypeEnum.机构还款.getCode().equals(repayType)){
                	//构建发送失败预警邮件的内容
                    strBuffer.append("划扣流水号="+serialno+"，合同号="+loanInfo.getContractNum()+"，陆金所回盘金额="+frozenAmount+"，证大报盘金额="+amount+"；");
                }
            }
        }
        //发送失败预警邮件
        if(strBuffer.length()>0){
        	disposeFailDebitResult(strBuffer);
        }
    }
   
	/**
	 * 更新状态（第三方结构划扣）
	 * @param debitQueueLogList  根据划扣流水号(debit_no)查出的 划扣对列表
	 * @param transactionState 扣款失败或扣款成功
	 * @param baseInfoState  已回盘非全额或已回盘全额
	 */
	public void changeDebitInfoState(List<DebitQueueLog> debitQueueLogList,String transactionState,String transactionRtnCode,String baseInfoState,BigDecimal frozenAmount){
		for(DebitQueueLog log : debitQueueLogList){
			DebitTransaction debitTransaction = debitTransactionDao.get(log.getDebitTransactionId());
			if(debitTransaction == null){
				continue;
			}
            debitTransaction.setState(transactionState);
            debitTransaction.setRtnCode(transactionRtnCode);
            debitTransaction.setResTime(new Date());
            debitTransaction.setActualAmount(frozenAmount);//实际划扣金额
            debitTransactionDao.update(debitTransaction);//第三方划扣流水表
            
            DebitBaseInfo debitBaseInfo = debitBaseInfoDao.get(debitTransaction.getDebitId());
            debitBaseInfo.setState(baseInfoState);
            /** 更新报盘金额 报盘金额=报盘金额-本次成功扣款金额 **/
            debitBaseInfo.setOfferAmount(debitBaseInfo.getOfferAmount().subtract(frozenAmount));
            /** 更新实际划扣金额 实际划扣金额=实际划扣金额 +本次成功扣款金额 **/
            debitBaseInfo.setActualAmount(debitBaseInfo.getActualAmount().add(frozenAmount));
            debitBaseInfoDao.update(debitBaseInfo);//第三方划扣信息表
            
            //跨日回盘 添加到消息中心
            offerTransactionServiceImpl.addBaseMessage(debitBaseInfo.getOfferDate(), debitBaseInfo.getLoanId());
		}
	}
	
	/**
	 * 更新划扣对列表状态
	 * @param serialno 划扣流水号
	 * @param state 划扣成功或划扣失败
	 */
	public void changeDebitQueueLogState(String serialno,String state){
		// 更新划扣队列状态信息
		debitQueueLogService.updateDebitQueueLogAfterGetDeductResultNotifyFromLufax(serialno,state);
	}
	
	
	/**
	 * 更改陆金所债权状态
	 * @param loanId  债权ID
	 * @param state 陆金所债权状态  逾期或结清
	 */
	private void changeLoanStatusLufaxState(Long loanId,LoanStatueLufaxEnum loanStatueLufaxEnum) {
		  LoanStatusLufax  loanStatusLufax = new LoanStatusLufax();
		  loanStatusLufax.setLoanId(loanId);
		  List<LoanStatusLufax> loanStatusLufaxList = loanStatusLufaxDao.findListByVo(loanStatusLufax);
		  if(CollectionUtils.isEmpty(loanStatusLufaxList)){
			  logger.info("【陆金所划扣结果回盘】债权状态表没有查到对应的债权："+loanId);
			  return ;
		  }
		  if(loanStatusLufaxList.size()>1){
			  logger.info("【陆金所划扣结果回盘】得到指定债权状态,得到过多的结果集！"+loanId);
              return ;
          }
		  loanStatusLufax = loanStatusLufaxList.get(0);
		  loanStatusLufax.setLoanStatus(loanStatueLufaxEnum.getCode());
		  loanStatusLufaxDao.update(loanStatusLufax);
	}
	
    /**
     * 更新陆金所专用的还款计划  loan_repayment_detail_lufax
     * @param amount 陆金所返回冻结金额
     * @param debitQueueLog 代扣对象
     */
    private void changeLoanRepaymentDetailLufax(DebitQueueLog debitQueueLog,BigDecimal amount) {
    	Date currentDate = Dates.format(new Date(),Dates.DATAFORMAT_SLASHDATE);
    	String repayType = debitQueueLog.getRepayType();
        String payParty = debitQueueLog.getPayParty();
        Long loanId = debitQueueLog.getLoanId();
        Date tradeDate = Dates.format(debitQueueLog.getCreateTime(), Dates.DATAFORMAT_SLASHDATE);
        Long currentTerm = debitQueueLog.getRepayTerm();
        // 查询债权信息
        VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
        // 查询当期还款计划信息，包含逾期代偿、一次性回购所处的期数对应的还款计划
        LoanRepaymentDetailLufax loanRepaymentDetailLufax = getLoanRepaymentDetailLufaxByIdAndTerm(loanId, currentTerm);
        // 当期的剩余欠款
        BigDecimal deficit = loanRepaymentDetailLufax.getDeficit();
        // 借据状态
        LoanStatueLufaxEnum loanStatueLufaxEnum = null;
        if (DebitRepayTypeEnum.提前结清.getCode().equals(repayType)) {// 提前结清还款
            // 更新 loanRepaymentDetailLufax 剩余的为 提前结清 剩余欠款 =0
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loanId", loanId);
            map.put("currentTerm", currentTerm);
            map.put("repaymentState", RepaymentStateEnum.结清.name());
            map.put("loanStatusLufax", RepaymentStateLufaxEnum.提前结清.getCode());
            map.put("deficit", 0.0);
            map.put("factReturnDate",Dates.getDateTime(tradeDate, "yyyy/MM/dd"));
            loanRepaymentDetailLufaxDao.updateRepaymentDetailLufaxByLoanId(map);
            // 更新借据状态信息：结清
            changeLoanStatusLufaxState(loanId, LoanStatueLufaxEnum.结清);
        } else if (DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType) ||
        		DebitRepayTypeEnum.委托还款.getCode().equals(repayType)) {// 逾期代偿（包括准备金代偿和保证金代偿）  加上 委托还款  
        	if(PayPartyEnum.准备金.getCode().equals(payParty)){
                loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.当期逾期代垫.getCode());
                loanStatueLufaxEnum = LoanStatueLufaxEnum.逾期;
                if (vLoanInfo.getTime() == loanRepaymentDetailLufax.getCurrentTerm()) {
                    loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.代偿结清.getCode());
                    loanStatueLufaxEnum = LoanStatueLufaxEnum.准备金代偿结清;
                }
            }else if(PayPartyEnum.保证金.getCode().equals(payParty)){
                loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.当期逾期保证金代垫.getCode());
                loanStatueLufaxEnum = LoanStatueLufaxEnum.逾期;
                if (vLoanInfo.getTime() == loanRepaymentDetailLufax.getCurrentTerm()) {
                    loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.保证金代偿结清.getCode());
                    loanStatueLufaxEnum = LoanStatueLufaxEnum.保证金代偿结清;
                }
                //回盘日期  - loanRepaymentDetailLufax.returnDate  > 3  
                int lufaxOverdueDay = workDayInfoServiceImpl.getWorkDaysInRegion(loanRepaymentDetailLufax.getReturnDate(), currentDate);
                if(lufaxOverdueDay > 3 ){
                	loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.证大代偿结清.getCode());
                }
            }
            loanRepaymentDetailLufaxDao.update(loanRepaymentDetailLufax);
            logger.info("※※※※※※※※※※：更新陆金所还款计划"+ JSON.toJSONString(loanRepaymentDetailLufax));
            // 更新借据状态信息
            changeLoanStatusLufaxState(loanId, loanStatueLufaxEnum);
        }else { // 机构还款、委托还款
            if (amount.compareTo(deficit) >= 0) {// 当期全部还清
                loanRepaymentDetailLufax.setDeficit(new BigDecimal(0.0));
                loanRepaymentDetailLufax.setRepaymentState(RepaymentStateEnum.结清.name());
                loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.已付清.getCode());
                loanRepaymentDetailLufax.setFactReturnDate(Dates.format(tradeDate, "yyyy/MM/dd"));
                loanRepaymentDetailLufaxDao.update(loanRepaymentDetailLufax);
                loanStatueLufaxEnum = LoanStatueLufaxEnum.正常;
                if (vLoanInfo.getTime() == loanRepaymentDetailLufax.getCurrentTerm()) {
                    loanStatueLufaxEnum = LoanStatueLufaxEnum.结清;
                }
                // 更新借据状态信息：正常或者结清
                changeLoanStatusLufaxState(loanId, loanStatueLufaxEnum);
            } else { // 当期部分还款
                loanRepaymentDetailLufax.setDeficit(deficit.subtract(amount));
                loanRepaymentDetailLufax.setRepaymentState(RepaymentStateEnum.不足额还款.name());
                loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.部分还款.getCode());
                loanRepaymentDetailLufaxDao.update(loanRepaymentDetailLufax);
            }
        }
    }
    
    /**
     * 划扣成功后保存 消费队列表
     * @param loanId 债权ID
     * @param lufax_repay_req_no 陆金所划扣回盘  lufax还款请求号
     * @param serialno 划扣流水号
     * @param tradeDate 交易日期
     * @param frozenAmount 陆金所返回冻结金额
     */
	public void saveRepayResultNotifyLog(Long loanId,String lufax_repay_req_no,String serialno,Date tradeDate,BigDecimal frozenAmount){
		 RepayResultNotifyLog repayResultNotify = new RepayResultNotifyLog();
		 repayResultNotify.setId(sequencesService.getSequences(SequencesEnum.REPAY_RESULT_NOTIFY_LOG));
		 repayResultNotify.setLoanId(loanId);
		 repayResultNotify.setOrderNo(lufax_repay_req_no);
		 repayResultNotify.setRepayBusNumber(serialno);
		 repayResultNotify.setScheduleDate(Dates.getDateTime(tradeDate, "yyyyMMdd"));
		 repayResultNotify.setTotalamt(frozenAmount);
		 repayResultNotify.setState("0");//消费状态 0未消费 1已消费
		 repayResultNotify.setNotifyType("3");//1提前扣款2结清代偿3自扣还款   有疑问？
		 repayResultNotify.setDeductState("t");//划扣状态 t成功 f失败
		 repayResultNotifyLogService.save(repayResultNotify);  
	}
    
	/**
	 * 划扣成功后保存 对公账户明细表
	 * @param loanInfo 债权
	 * @param frozenAmount  陆金所返回冻结金额
	 * @param serialno 划扣流水号
	 */
	private void savePublicAccountDetail(VLoanInfo loanInfo,BigDecimal frozenAmount,String serialno, String repayType){
		// 保存还款明细  2-还款  1-成功
	    String tradeType = "";
	    if(DebitRepayTypeEnum.委托还款.getCode().equals(repayType) 
                || DebitRepayTypeEnum.提前结清.getCode().equals(repayType)){
	        tradeType = AccountTradeTypeEnum.还款.getCode();
	    } else if(DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)){
	        tradeType = AccountTradeTypeEnum.垫付.getCode();
	    }else if(DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)){
            tradeType = AccountTradeTypeEnum.回购.getCode();
        }
		publicAccountDetailService.insertAccDetail(loanInfo.getContractNum(), serialno, tradeType, frozenAmount, "", "1", DebitResultStateEnum.划扣成功.getValue());
	}
	
	
	/**
	 * 陆金所回盘失败的处理 发送失败预警邮件
	 * @param detail 邮件的内容
	 */
	public void disposeFailDebitResult(StringBuffer detail){
		logger.info("陆金所代扣结果回盘失败,发送邮件开始...");
		Set<String> set = new HashSet<String>();
		set.add(detail.toString());
		logger.info("【陆金所-代扣结果给证大（回盘）失败发送邮件：】："+detail);
		String receiver = sysParamDefineService.getSysParamValueCache("lufaxMail", "mail.smtp.to");
		String title = DateTime.now().toString("yyyy-MM-dd")+"陆金所代扣结果回盘失败提示";
		String content = DateTime.now().toString("yyyy-MM-dd")+"，以下数据陆金所代扣结果回盘失败，请相关人员及时处理。详细信息如下："+set;
		try {
			sendMailService.sendTextMail(receiver, title, content);
			logger.info("陆金所代扣结果回盘失败,发送邮件结束...");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 判断是否可以进行还款、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanRepayment(Long loanId) {
        String[] repayTypes = new String[] { DebitRepayTypeEnum.委托还款.getCode(),
                DebitRepayTypeEnum.机构还款.getCode(),
                DebitRepayTypeEnum.逾期代偿.getCode(),
                DebitRepayTypeEnum.一次性回购.getCode(),
                DebitRepayTypeEnum.提前结清.getCode() };
        return this.isCanRepayment(loanId, repayTypes);
    }
    
    /**
     * 判断是否可以进行还款、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanRepayment(Long loanId, String[] repayTypes) {
        VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
        if(!FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getFundsSources())){
            return true;
        }
        // 陆金所逾期的债权，还款不做限制
        if(LoanStateEnum.逾期.name().equals(loanInfo.getLoanState())){
            return true;
        }
        // 是否存在未分账、分账中的分账记录，如果存在则不能做还款、入账操作
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("loanId", loanId);
        params.put("splitResultStates", new String[]{SplitResultStateEnum.未分账.getCode(), SplitResultStateEnum.分账中.getCode(), SplitResultStateEnum.分账失败.getCode()});
        // 还款类型
        params.put("repayTypes", repayTypes);
        
        // 查询未推送分账信息或推送分账信息失败的记录条数
        Integer count = splitQueueLogDao.queryPreSplitCount(params);
        if(count > 0){
            return false;
        }
        
        // 是否存在未划扣、划扣中的划扣记录，如果存在则不能做还款、入账操作
        params = new HashMap<String,Object>();
        params.put("loanId", loanId);
        params.put("debitResultStates", new String[]{DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣中.getCode(), DebitResultStateEnum.划扣失败.getCode()});
        params.put("repayTypes", repayTypes);
        List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByMap(params);
        if(CollectionUtils.isEmpty(debitQueueLogList)){
            return true;
        }
        for(DebitQueueLog debitQueueLog : debitQueueLogList){
            // 一次性回购未完成
            if(DebitRepayTypeEnum.一次性回购.getCode().equals(debitQueueLog.getRepayType())){
                return false;
            }
            // 逾期代偿未完成
            if(DebitRepayTypeEnum.逾期代偿.getCode().equals(debitQueueLog.getRepayType())){
                return false;
            }
            // 机构还款未完成
            if (DebitRepayTypeEnum.机构还款.getCode().equals(debitQueueLog.getRepayType())) {
                if (DebitResultStateEnum.未划扣.getCode().equals(debitQueueLog.getDebitResultState())
                    || DebitResultStateEnum.划扣中.getCode().equals(debitQueueLog.getDebitResultState())) {
                    return false;
                }
            }
        }
        return true;
    }
    
	@Override
	public Pager findWithPgByMap(Map<String, Object> paramMap) {
		return splitQueueLogDao.findWithPgByMap(paramMap);
	}
	
	public SplitQueueLog createSplitQueueLog(SplitQueueLog splitQueueLog) {
		splitQueueLog.setId(sequencesService.getSequences(SequencesEnum.SPLIT_QUEUE_LOG));
		splitQueueLogDao.insert(splitQueueLog);
		return splitQueueLog;
	}

	@Override
	public List<SplitQueueLog> findSplitQueueLogListByParams(Map<String, Object> params) {
		return splitQueueLogDao.findSplitQueueLogListByParams(params);
	}
	
    /**
     * 根据陆金所一次性代偿划扣结果，做相应逻辑处理
     * @param jsonArray
     */
    public void dealOverdueCompensatory(JSONArray jsonArray) {
        for (Object ja : jsonArray) {
            JSONObject jso = (JSONObject) ja;
            /* 序列号 */
            String serialno = jso.getString("serialno");
            /* 批次号 */
            String anshuo_batch_id = jso.getString("anshuo_batch_id");
            /* 代偿扣款状态 */
            String state = jso.getString("state");
            /* lufax还款请求号 */
            String lufax_repay_req_no = jso.getString("lufax_repay_req_no");

            DebitQueueLog searchVo = new DebitQueueLog();
            searchVo.setBatchId(anshuo_batch_id);
            searchVo.setDebitNo(serialno);
            // 根据批次号和debit_no查询一次发送的债权
            List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByVo(searchVo);
            if (CollectionUtils.isEmpty(debitQueueLogList)) {
                logger.warn("查询不到对应的划扣队列信息，batchId：" + anshuo_batch_id + "debitNo：" + serialno);
                continue;
            }
            DebitQueueLog debitQueueLog = debitQueueLogList.get(0);
            if (DebitResultStateEnum.划扣成功.getCode().equals(debitQueueLog.getDebitResultState())) {
                logger.warn("【陆金所一次性代偿结果通知-回盘】批次号：" + anshuo_batch_id + ",安硕序号:" + serialno + "，已经成功划扣！");
                continue;
            }
            // 状态一定成功1-成功
            if (!"1".equals(state)) {
                logger.warn("【陆金所一次性代偿结果通知-回盘】批次号：" + anshuo_batch_id + ",安硕序号:" + serialno + "，划扣失败！");
                changeDebitQueueLogState(serialno,DebitResultStateEnum.划扣失败.getCode());
                continue;
            }
            // 债权编号
            Long loanId = debitQueueLog.getLoanId();
            // 还款类型
            String repayType = debitQueueLog.getRepayType();
            // 代扣金额
            BigDecimal amount = debitQueueLog.getAmount();
            // 查询债权信息
            VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
            // 1、更新还款计划表（loan_repayment_detail_lufax）
            // 2、更新债权状态表（loan_status_lufax）
            changeLoanRepaymentDetailLufax4BuyBack(debitQueueLog);
            logger.info("①更新还款计划表（loan_repayment_detail_lufax）& ②更新债权状态表（loan_status_lufax）结束");

            // 3、更新划扣队列表（debit_queue_log）
            changeDebitQueueLogState(serialno, DebitResultStateEnum.划扣成功.getCode());
            logger.info("③更新划扣队列表（debit_queue_log）结束");

            // 4、记录数据到分账队列表（split_queue_log）
            createSplitQueueLog(loanId, serialno, lufax_repay_req_no, anshuo_batch_id, amount);
            logger.info("④记录数据到分账队列表（split_queue_log）结束");

            // 5、保存还款明细 2-还款 1-成功
            savePublicAccountDetail(loanInfo, amount, serialno, repayType);
            logger.info("⑤保存还款明细  2-还款  1-成功结束");

            // 6、生成回购明细数据（LOAN_RETURN、LOAN_RETURN_RECORD）更新债权去向（loan_base）
            loanReturnRecordService.createLoanReturnRecord4Lufax(loanInfo, debitQueueLog);
            logger.info("⑥生成回购明细数据、更新债权去向结束");
        }
    }
    
	/**
	 * 一次性回购 更新 还款计划表
	 * @param loanId
	 * @param tradeDate
	 */
	private void changeLoanRepaymentDetailLufax4BuyBack(DebitQueueLog debitQueueLog) {
		String payParty = debitQueueLog.getPayParty();
		long loanId = debitQueueLog.getLoanId();
		long repayTerm = debitQueueLog.getRepayTerm();
		Map<String, Object> params = new HashMap<>();
		params.put("loanId",loanId);
		params.put("orderByCurrentTerm","true");
		params.put("aftercurrentTerm", repayTerm);
		List<LoanRepaymentDetailLufax> list = loanRepaymentDetailLufaxDao.findListByMap(params);
		if(CollectionUtils.isEmpty(list)){
			logger.error("【陆金所一次性回购的回盘】没有查到相关的还款计划信息：loanId:"+loanId+",期数："+repayTerm);
			throw new PlatformException(ResponseEnum.FULL_MSG, "【陆金所一次性回购的回盘】没有查到相关的还款计划信息：loanId:"+loanId+",期数："+repayTerm);
		}
		Date currentDate = Dates.format(new Date(),Dates.DATAFORMAT_SLASHDATE);
		Date returnDate = Dates.format(list.get(0).getReturnDate(), Dates.DATAFORMAT_SLASHDATE);
		boolean flag =false;
		int lufaxOverdueDay = workDayInfoServiceImpl.getWorkDaysInRegion(returnDate, currentDate);
		if(lufaxOverdueDay > 3){
			flag=true;
		}
		for(LoanRepaymentDetailLufax lrl : list){
			if(PayPartyEnum.准备金.getCode().equals(payParty)){
				lrl.setLoanStatusLufax(RepaymentStateLufaxEnum.代偿结清.getCode());
			}else if (PayPartyEnum.保证金.getCode().equals(payParty)) {
				lrl.setLoanStatusLufax(RepaymentStateLufaxEnum.保证金代偿结清.getCode());
				if(flag){
					lrl.setLoanStatusLufax(RepaymentStateLufaxEnum.证大一次性结清.getCode());
				}
			}
			loanRepaymentDetailLufaxDao.update(lrl);
		}
		changeLoanStatusLufaxState(loanId, LoanStatueLufaxEnum.逾期);//更新陆金所债权状态  为代偿
	}

    /**
     * 保证金审核结果通知处理
     * @param jsonArray
     */
    public void marginAuditResultNotify(JSONArray jsonArray){
        for (Object ja : jsonArray) {
            JSONObject jso = (JSONObject) ja;
            /* 批次号 */
            String anshuo_batch_id = jso.getString("anshuo_batch_id");
            /* 序列号 */
            String serialno = jso.getString("serialno");
            /* 人工审批意见 */
            String suggestion = jso.getString("suggestion");
            /* 人工审核结论【S:同意，F:不同意，SPCS:审批超时】 */
            String conclusion = jso.getString("conclusion");
            /* 保证金阈值比例 */
            String bailRatio = jso.getString("bailRatio");
            /* 当前保证金比例 */
            String currentBailRatio = jso.getString("currentBailRatio");
            /* 保证金余额（元） */
            BigDecimal bailMargin = jso.getBigDecimal("bailMargin");
            /* 最低保证金（元） */
            BigDecimal bottomBail = jso.getBigDecimal("bottomBail");
            /* 保证金阀值水位(元) */
            BigDecimal bailThresholdValue = jso.getBigDecimal("bailThresholdValue");
            /* 初始保证金金额(元) */
            BigDecimal originalBail = jso.getBigDecimal("originalBail");
            // 审批结果
            if("S".equalsIgnoreCase(conclusion)){//【S:同意，F:不同意，SPCS:审批超时】 */
                // 审批通过只打印日志信息，不做其他处理
                logger.info("批次号:" + anshuo_batch_id + "-->序列号:" + serialno + "陆金所保证金审核通过！");
                continue;
            } 
            
            // 审批不通过、需更新划扣队列信息
            DebitQueueLog searchVo = new DebitQueueLog();
            searchVo.setBatchId(anshuo_batch_id);
            searchVo.setDebitNo(serialno);
            // 根据批次号和debit_no查询一次发送的债权
            List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByVo(searchVo);
            if(CollectionUtils.isEmpty(debitQueueLogList)){
                logger.warn("查询不到对应的划扣队列信息，batchId：" + anshuo_batch_id + "debitNo：" + serialno);
                continue;
            }
            switch (conclusion) {
            case "F":
                conclusion = "不同意";
                break;
            case "SPCS":
                conclusion = "审批超时";
                break;
            case "CZCG":
                conclusion = "充值成功";
                break;
            case "CZSB":
                conclusion = "充值失败";
                break;
            }
            for (DebitQueueLog queueLog : debitQueueLogList) {
                queueLog.setMemo("人工审核结论:" + conclusion + ",人工审批意见:" + suggestion + ",保证金阈值比例:" + bailRatio + ",当前保证金比例:" + currentBailRatio);
                queueLog.setDebitResultState(DebitResultStateEnum.划扣失败.getCode());
                logger.info("批次号:" + anshuo_batch_id + "-->序列号:" + serialno
                        + "陆金所保证金审核失败" + "人工审核结论:" + conclusion + ",人工审批意见:"
                        + suggestion + ",保证金阈值比例:" + bailRatio + ",当前保证金比例:"
                        + currentBailRatio + ",保证金余额（元）:" + bailMargin
                        + ",最低保证金（元）:" + bottomBail + ",保证金阀值水位(元):"
                        + bailThresholdValue + ",初始保证金金额(元):" + originalBail);
                debitQueueLogDao.update(queueLog);
            }
        }
    }

	@Override
	public List<SplitRepaymentVo> findSplitRepayment4Lufax(Map<String, Object> params) {
		return splitQueueLogDao.findSplitRepayment4Lufax(params);
	}
	
    public boolean updateRepaymentDetailAndLoanStatus4Lufax(SplitQueueLog splitQueueLog) {
        // 分账状态不是未分账、不做后续还款计划更新操作
        if(!SplitResultStateEnum.未分账.getCode().equals(splitQueueLog.getSplitResultState()) && !"0".equals(splitQueueLog.getSendEntrustFlag())){
            return false;
        }
        try {
            Map<String, Object> repayParams = new HashMap<>();
            repayParams.put("debitNo", splitQueueLog.getDebitNo());
            // 分期 获取 还款本金与利息
            List<SplitRepaymentVo> repaymentList = this.findSplitRepayment4Lufax(repayParams);
            Map<String, Object> repayPlanparams = new HashMap<String, Object>();
            repayPlanparams.put("loanId", splitQueueLog.getLoanId());
            repayPlanparams.put("repaymentStates",new String[] { RepaymentStateEnum.不足罚息.name(),RepaymentStateEnum.不足额还款.name(),RepaymentStateEnum.未还款.name() });
            repayPlanparams.put("currentReturnDate", Dates.getDateTime(splitQueueLog.getCreateTime(), Dates.DEFAULT_DATE_FORMAT));
            repayPlanparams.put("orderByCurrentTerm", "true");
            // 获取陆金所 还款计划中逾期信息
            List<LoanRepaymentDetailLufax> repayDetaillist = loanRepaymentDetailLufaxDao.findListByMap(repayPlanparams);
            if(CollectionUtils.isEmpty(repayDetaillist)){
                return false;
            }
            for (SplitRepaymentVo splitRepaymentVo : repaymentList) {
                // 逾期还款的债权编号
                Long loanId = splitRepaymentVo.getLoanId();
                // 逾期还款对应的还款期数
                Long currentTerm = splitRepaymentVo.getCurrentTerm();
                // 逾期还款的本利和
                BigDecimal repayAmount = splitRepaymentVo.getCapital().add(splitRepaymentVo.getInterest());
                for (LoanRepaymentDetailLufax loanRepaymentDetailLufax : repayDetaillist) {
                    if (loanId.equals(loanRepaymentDetailLufax.getLoanId()) && currentTerm.equals(loanRepaymentDetailLufax.getCurrentTerm())) {
                        if (repayAmount.compareTo(loanRepaymentDetailLufax.getDeficit()) >= 0) {
                            // 剩余欠款
                            loanRepaymentDetailLufax.setDeficit(BigDecimal.ZERO);
                            loanRepaymentDetailLufax.setRepaymentState(RepaymentStateEnum.结清.name());
                            loanRepaymentDetailLufax.setLoanStatusLufax(RepaymentStateLufaxEnum.当期追偿完成.getCode());
                            loanRepaymentDetailLufax.setFactReturnDate(Dates.format(splitQueueLog.getCreateTime(),Dates.DEFAULT_DATE_FORMAT));
                        } else {
                            loanRepaymentDetailLufax.setDeficit(loanRepaymentDetailLufax.getDeficit().subtract(repayAmount));
                            loanRepaymentDetailLufax.setRepaymentState(RepaymentStateEnum.不足额还款.name());
                        }
                        loanRepaymentDetailLufaxDao.update(loanRepaymentDetailLufax);
                        break;
                    }
                }
            }
            VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(splitQueueLog.getLoanId());
            // 最后一期逾期还款计划
            LoanRepaymentDetailLufax lastLoanRepaymentDetailLufax = repayDetaillist.get(repayDetaillist.size() - 1);
            // 还款状态
            String repaymentState = lastLoanRepaymentDetailLufax.getRepaymentState();
            // 还款期数
            Long currentTerm = lastLoanRepaymentDetailLufax.getCurrentTerm();
            // 逾期还款结清最后一期、 更新状态为追偿结清
            if (RepaymentStateEnum.结清.name().equals(repaymentState)) {
                LoanStatusLufax lsl = new LoanStatusLufax();
                lsl.setLoanId(splitQueueLog.getLoanId());
                LoanStatusLufax loanStatusLufax = loanStatusLufaxDao.findListByVo(lsl).get(0);
				if( vLoanInfo.getTime().equals(currentTerm)){
					loanStatusLufax.setLoanStatus(LoanStatueLufaxEnum.追偿结清.getCode());
				}else{
					loanStatusLufax.setLoanStatus(LoanStatueLufaxEnum.正常.getCode());
				}
                loanStatusLufaxDao.update(loanStatusLufax);
            }
            return true;
        } catch (Exception e) {
            logger.error("根据逾期还回队列信息 更新还款计划与债券状态异常：", e);
            return false;
        }
    }

	@Override
	public Lufax100018OutputVo investTimeNotify(long loanId,String investTime) {
		Lufax100018OutputVo outPut = new Lufax100018OutputVo();
		VLoanInfo vLoanInfo =vLoanInfoService.findByLoanId(loanId);
		LoanBank loanBank = loanBankService.findById(vLoanInfo.getGiveBackBankId());
    	//String appNo = vLoanInfo.getAppNo();
		LoanBsbMapping bsb = loanBsbMappingDao.getByLoanId(loanId);
		bsb.setValueDate(Dates.parse(investTime, Dates.DATAFORMAT_YYYYMMDDHHMMSS));
		loanBsbMappingDao.update(bsb);
		try{
			//根据陆金所入参的投资时间，重新生成还款计划，改变loan_product 开始还款日期，结束还款日期，约定还款日
			loanCoreService.createRepaymentDetailAfterGrantLufax(loanId,bsb.getValueDate());
			logger.info("【陆金所】推送投资时间到证大接口--重新生成还款计划成功！");
		}catch(Exception e){
			logger.error("【陆金所】推送投资时间到证大接口--重新生成还款计划失败：",e);
			throw new PlatformException("【陆金所】推送投资时间到证大接口--重新生成还款计划失败！！");
		}
		
		if (financeGrantServiceImpl.isApplyFinanceGrant(loanId)){
            loanBaseGrantService.createLoanBaseGrant(vLoanInfo);     
        }
		
		//保存 操作日志表 
		OperateLog operateLog = new OperateLog();
		operateLog.setId(sequencesService.getSequences(SequencesEnum.OPERATE_LOG));
		operateLog.setLoanId(loanId);
		operateLog.setOperateType("06");//核算推送投资时间到证
		operateLog.setOperateDate(new Date());
		operateLog.setStatus("1");//1：操作成功、2：操作失败
		OperateLogService.save(operateLog);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId",loanId);
		paramMap.put("orderByCurrentTerm","true");
    	List<LoanRepaymentDetailLufax> loanRepaymentDetailLufaxList = loanRepaymentDetailLufaxDao.findListByMap(paramMap);
    	int lastTerm = loanRepaymentDetailLufaxList.size()-1;
    	//得到指定还款计划最后一期的还款日 为到期日
    	String due_date = Dates.getDateTime(loanRepaymentDetailLufaxList.get(lastTerm).getReturnDate(),Dates.DATAFORMAT_YYYYMMDDHHMMSS);
    	List<LoanPlan> loan_plan = new ArrayList<LoanPlan>();
    	for(LoanRepaymentDetailLufax loanRepayment : loanRepaymentDetailLufaxList){
    		LoanPlan item = new LoanPlan();
			item.setPayment_accno(loanBank.getAccount());
			item.setRpterm(loanRepayment.getCurrentTerm().intValue());
			item.setPflg("01");//状态 01（正常）02（逾期）04（结清）05（提前结清）07（代偿中）08（追偿）09（代偿结清）10（追偿结清）
			item.setWrpdte(Dates.getDateTime(loanRepayment.getReturnDate(),Dates.DATAFORMAT_YYYYMMDDHHMMSS));//还款日
			item.setRrpdte(loanRepayment.getFactReturnDate()==null?"":sdf.format(loanRepayment.getFactReturnDate()));//结清日期
			item.setOrate(vLoanInfo.getPenaltyRate());//逾期利率
        	item.setRate(vLoanInfo.getRateey());//贷款利率
        	item.setTotal_amount(new BigDecimal(String.format("%.2f",loanRepayment.getReturneterm())));//  还款总额 = 应还金额
        	item.setCapital(new BigDecimal(String.format("%.2f",loanRepayment.getReturneterm().subtract(loanRepayment.getCurrentAccrual()))));//应还本金
			item.setAint(new BigDecimal(String.format("%.2f",loanRepayment.getCurrentAccrual())));//应还利息 
			item.setOint(zero);//罚息  
			item.setInsureamount(zero);//担保费
			item.setFee1(zero);//费用1   
			item.setFee2(zero);//费用2   
        	item.setFee_amt2(zero);//平台管理费	
        	item.setFee_amt3(zero);//信安咨询费	
        	item.setFee_amt4(zero);//小贷管理费	
        	item.setFee_amt5(zero);//风险准备金	
        	item.setFee_amt6(zero);//逾期违约金	
        	
        	loan_plan.add(item);
    	}
    	outPut.setLoan_plan(loan_plan);
    	outPut.setLufax_borrower_username(bsb.getBusNumber());
    	outPut.setLufax_loan_req_id(bsb.getOrderNo());
    	outPut.setAnshuo_loan_accept_id(LufaxConst.ZDJR + vLoanInfo.getChannelPushNo());
    	outPut.setValue_date(Dates.getDateTime(bsb.getValueDate(), Dates.DATAFORMAT_YYYYMMDDHHMMSS));
    	outPut.setDue_date(due_date);
    	return outPut;
	}
	
	/**
	 * 证大推送正式还款计划借据给陆金所
	 * @param loanId
	 */
	public void pushRepayPlanAndLoanInfo2Lufax(Long loanId){
        try {
			VLoanInfo vLoanInfo =vLoanInfoService.findByLoanId(loanId);
    		LoanBsbMapping bsb = loanBsbMappingDao.getByLoanId(loanId);
			LoanVo loanVo = new LoanVo();
    		loanVo.setAppNo(vLoanInfo.getAppNo());
    		loanVo.setLoanId(loanId);
    		loanVo.setApplyNo(LufaxConst.ZDJR + vLoanInfo.getChannelPushNo());
    		loanVo.setProductType(PRODUCT_TYPE);
    		loanVo.setLufaxLoanReqId(bsb.getOrderNo());
			loanLogService.createLog(loanId, "合同号为" + vLoanInfo.getContractNum() + "的债权同步信息中...", "PushFormalRepayPlans2Lufax", "info", "");
			JSONObject jsonResult  = loanStatusLufaxService.pushRepayPlanAndLoanInfo2Lufax(loanVo);
			if(GatewayConstant.RESPONSE_SUCCESS.equals(jsonResult.getString("ret_code"))){
				loanLogService.createLog(loanId, "合同号为" + vLoanInfo.getContractNum() + "的债权信息同步成功", "PushFormalRepayPlans2Lufax", "info", "");
			}else{
				String returnMsg = jsonResult.getString("ret_msg");
				returnMsg = returnMsg == null ? "" : returnMsg;
				if(returnMsg.length()>100){
					returnMsg = returnMsg.substring(0,100);
				}
				loanLogService.createLog(loanId, "合同号为" + vLoanInfo.getContractNum() + "的债权信息同步失败。"+returnMsg, "PushFormalRepayPlans2Lufax", "info", "");
				throw new PlatformException(ResponseEnum.FULL_MSG,returnMsg);
			}
        } catch (Exception e) {
            throw e;
        }
	}
	
    /**
     * 获取逾期代偿或者一次性回购所在还款期数的应还款时间
     * @return
     */
    public Date queryOverdueCompensatoryTradeDate(DebitQueueLog debitQueueLog, Date tradeDate ,String type) {
        CompensatoryDetailLufax vo = new CompensatoryDetailLufax();
        vo.setLoanId(debitQueueLog.getLoanId());
        vo.setDebitQueueId(debitQueueLog.getId());
        vo.setType(type);//垫付类型（01：逾期代偿、02：一次性回购）
        List<CompensatoryDetailLufax> compensatoryList = compensatoryDetailLufaxDao.findListByVo(vo);
        if(CollectionUtils.isNotEmpty(compensatoryList)){
            return Dates.format(compensatoryList.get(0).getTradeDate(), Dates.DEFAULT_DATE_FORMAT);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tradeDate);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) - 1);
        return Dates.format(calendar.getTime(), Dates.DEFAULT_DATE_FORMAT);
    }

    
    @Override
	public void syncOneBuyBackAndCompensatedRepayInfo() {
    	//4 4-36 追偿结清("100","追偿结清") defict = 0  repayment_state = 结清 
    	List<OneBuyBackCompensatedVO> oneBuyBackCompensatedList = splitQueueLogDao.findOneBuyBackAndCompensatedRepayInfo();
		if(CollectionUtils.isEmpty(oneBuyBackCompensatedList)){
			logger.info("【陆金所】同步追偿还款记录和还款计划--暂无对应的数据信息！！！");
			return;
		}
		for(OneBuyBackCompensatedVO oneBuyBackCompensated : oneBuyBackCompensatedList){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("loanId", oneBuyBackCompensated.getLoanId());
			map.put("currentTerm", oneBuyBackCompensated.getTerm());
			map.put("repaymentState", RepaymentStateEnum.结清.name());
			map.put("loanStatusLufax", RepaymentStateLufaxEnum.追偿结清.getCode());
			map.put("deficit", 0.0);
			map.put("factReturnDate",Dates.getDateTime(new Date(), "yyyy/MM/dd"));
			loanRepaymentDetailLufaxDao.updateRepaymentDetailLufaxByLoanId(map);
			
			//更新陆金所债权状态
			changeLoanStatusLufaxState(oneBuyBackCompensated.getLoanId(),LoanStatueLufaxEnum.追偿结清);
		}
		boolean repayRecord = syncOneBuyBackAndCompensatedRepayRecord(oneBuyBackCompensatedList);
		boolean repayPlan = syncOneBuyBackAndCompensatedRepayPlan(oneBuyBackCompensatedList);
		if(repayRecord && repayPlan){
			saveOperateLog(oneBuyBackCompensatedList, "08");
		}
	}

	public boolean syncOneBuyBackAndCompensatedRepayRecord(List<OneBuyBackCompensatedVO> oneBuyBackCompensatedList) {
		logger.info("【陆金所】同步追偿还款记录接口被调用Lufax100019Vo...");
		Lufax100019Vo vo = getLufax100019Vo(oneBuyBackCompensatedList);
		JSONObject grantResut = null;
		try{
			 grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步追偿还款记录接口.getCode());
		}catch(Exception e){
			logger.warn("【陆金所】同步追偿还款记录接口调用网关接口失败："+e.getMessage(),e);
			return false;
		}
		logger.info("【陆金所】证大同步追偿还款记录接口,陆金所返回的数据："+grantResut);
		String ret_code = (String) grantResut.get("ret_code");
		String ret_msg = (String)grantResut.get("ret_msg");
		if("0000".equals(ret_code)){
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步追偿还款记录接口成功！"+ret_msg);
			return true;
		}else{
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "调用【陆金所】同步还追偿还款记录接口失败：原因：" + ret_msg);
			return false;
		}
	}
	
	public boolean syncOneBuyBackAndCompensatedRepayPlan(List<OneBuyBackCompensatedVO> oneBuyBackCompensatedList) {
		logger.info("【陆金所】同步追偿还款计划接口被调用Lufax100020Vo...");
		Lufax100020Vo vo = getLufax100020Vo(oneBuyBackCompensatedList);
		JSONObject grantResut = null;
		try{
			 grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.证大同步追偿还款计划接口.getCode());
		}catch(Exception e){
			logger.warn("【陆金所】同步追偿还款计划接口调用网关接口失败："+e.getMessage(),e);
			return false;
		}
		logger.info("【陆金所】证大同步追偿还款计划接口,陆金所返回的数据："+grantResut);
		String ret_code = (String) grantResut.get("ret_code");
		String ret_msg = (String)grantResut.get("ret_msg");
		if("0000".equals(ret_code)){
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步追偿还款计划接口成功！"+ret_msg);
			return true;
		}else{
			logger.info(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+"调用【陆金所】同步还追偿还款计划接口失败：原因："+ret_msg);
			return false;
		}
	}
	
	public Lufax100019Vo getLufax100019Vo(List<OneBuyBackCompensatedVO> oneBuyBackCompensatedList){
		Lufax100019Vo vo = new Lufax100019Vo();
		vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());
		vo.setLine_sum(oneBuyBackCompensatedList.size());
		List<CompensateRepayRecord> repayRecordDetail = new ArrayList<CompensateRepayRecord>();
		for(OneBuyBackCompensatedVO backCompensate : oneBuyBackCompensatedList){
			CompensateRepayRecord record = new CompensateRepayRecord();
			Long loanId = backCompensate.getLoanId();
			//String appNo = backCompensate.getAppNo();
			record.setProduct_type(PRODUCT_TYPE);
			record.setAnshuo_loan_accept_id(LufaxConst.ZDJR + backCompensate.getChannelPushNo());
			record.setLufax_loan_req_id(loanBsbMappingDao.getByLoanId(loanId).getOrderNo());
			record.setCompensated_date(Dates.getDateTime(new Date(), Dates.DATAFORMAT_YYYYMMDD));//yyyyMMdd 追偿日期  Y
			record.setCapital(backCompensate.getCapital());
			record.setAint(backCompensate.getAint());
			record.setOint(backCompensate.getOint());
			record.setCompensated_amount(backCompensate.getCapital().add(backCompensate.getAint()).add(backCompensate.getOint()));//实还代偿款  Y
			record.setCompensated_sum(backCompensate.getCapital().add(backCompensate.getAint()).add(backCompensate.getOint()));  //追偿款总额  Y
			record.setInsurance_fee(zero);//实还担保费 Y
			record.setLate_fee(zero);//实还滞纳金 Y
			record.setFee_amt2(zero);
			record.setFee_amt3(zero);
			record.setFee_amt4(zero);
			record.setFee_amt5(zero);
			record.setFee_amt6(zero);
			record.setPenal_value(zero); //提前还款违约金
			repayRecordDetail.add(record);
		}
		vo.setDetail(repayRecordDetail);
		return vo;
	}
	
	public Lufax100020Vo getLufax100020Vo(List<OneBuyBackCompensatedVO> oneBuyBackCompenstatedList){
		Lufax100020Vo vo = new Lufax100020Vo();
		vo.setAnshuo_batch_id(LufaxUtil.createAnshuoBatchId());
		vo.setLine_sum(oneBuyBackCompenstatedList.size());
		List<CompensateRepayPlan> repayPlanDetail = new ArrayList<CompensateRepayPlan>();
		for(OneBuyBackCompensatedVO backCompensate : oneBuyBackCompenstatedList){
			CompensateRepayPlan plan = new CompensateRepayPlan();
			Long loanId = backCompensate.getLoanId();
			//String appNo = backCompensate.getAppNo();
			plan.setProduct_type(PRODUCT_TYPE); //Y
			plan.setAnshuo_loan_accept_id(LufaxConst.ZDJR + backCompensate.getChannelPushNo()); //Y
			plan.setLufax_loan_req_id(loanBsbMappingDao.getByLoanId(loanId).getOrderNo()); //Y
			plan.setCapital(backCompensate.getCapital());
			plan.setAint(backCompensate.getAint());
			plan.setOint(backCompensate.getOint());
			plan.setCompensated_amount(backCompensate.getCapital().add(backCompensate.getAint()).add(backCompensate.getOint()));//实还代偿款  Y
			plan.setCompensated_sum(backCompensate.getCapital().add(backCompensate.getAint()).add(backCompensate.getOint()));  //追偿款总额  Y
			plan.setInsurance_fee(zero);
			plan.setLate_fee(zero);
			plan.setFee_amt2(zero);
			plan.setFee_amt3(zero);
			plan.setFee_amt4(zero);
			plan.setFee_amt5(zero);
			plan.setFee_amt6(zero);
			plan.setPenal_value(zero);
			plan.setRecovery_status("1");
			repayPlanDetail.add(plan);
		}
		vo.setDetail(repayPlanDetail);
		return vo;
	}
	
	/**
	 * 保存操作日志
	 * @param oneBuyBackCompensatedList
	 */
	public void saveOperateLog(List<OneBuyBackCompensatedVO> oneBuyBackCompensatedList,String operateType){
		for(OneBuyBackCompensatedVO backCompensate : oneBuyBackCompensatedList){
			Long loanId = backCompensate.getLoanId();
			OperateLogService.addOperateLog(loanId, operateType, "1");
		}
	}
	
	
	public LoanRepaymentDetailLufax getLoanRepaymentDetailLufaxByIdAndTerm(Long loanId,Long term){
		LoanRepaymentDetailLufax loanRepaymentDetailLufax = new LoanRepaymentDetailLufax();
		loanRepaymentDetailLufax.setLoanId(loanId);
		loanRepaymentDetailLufax.setCurrentTerm(term);
		List<LoanRepaymentDetailLufax> loanRepaymentDetailLufaxList = loanRepaymentDetailLufaxDao.findListByVo(loanRepaymentDetailLufax);
		if(CollectionUtils.isEmpty(loanRepaymentDetailLufaxList)){
			logger.info("根据债权ID和期数得到的还款计划为空"+loanId+"，期数："+term);
			return new LoanRepaymentDetailLufax();
		}
		return loanRepaymentDetailLufaxList.get(0);
	}
	
    /**
     * 查询逾期代偿或者一次性回购超过三个宽限日所垫付的罚息
     * @param debitQueueLog
     * @return
     */
	public BigDecimal queryCompensatoryPenaltyAmount(Long loanId, Long term){
        // 逾期代偿或者一次性回购超过三个宽限日所垫付的罚息
        BigDecimal penaltyAmount = BigDecimal.ZERO;
        // 查询逾期代垫明细记录
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanId", loanId);
        params.put("term", term);
        List<CompensatoryDetailLufax> compensatoryDetailList = compensatoryDetailLufaxDao.findListByMap(params);
        if(CollectionUtils.isEmpty(compensatoryDetailList)){
            return penaltyAmount;
        }
        for(CompensatoryDetailLufax compensatoryDetail : compensatoryDetailList){
            penaltyAmount = penaltyAmount.add(compensatoryDetail.getPenaltyAmount());
        }
        return penaltyAmount;
    }

    /**
     * 根据债权编号合并还款记录，同一笔债权存在多笔还款记录，只取第一条
     * @param repayInfoList
     * @return
     */
    private List<RepayInfoLufaxVO> mergeRepayInfoListByLoanId(List<RepayInfoLufaxVO> repayInfoList) {
        List<RepayInfoLufaxVO> resultList = new ArrayList<RepayInfoLufaxVO>();
        Map<String, RepayInfoLufaxVO> resultMap = new HashMap<String, RepayInfoLufaxVO>();
        for (RepayInfoLufaxVO repayInfoVO : repayInfoList) {
            String key = String.valueOf(repayInfoVO.getLoanId());
            if (!resultMap.containsKey(key)) {
                resultMap.put(key, repayInfoVO);
            }
        }
        for (String key : resultMap.keySet()) {
            resultList.add(resultMap.get(key));
        }
        return resultList;
    }

    @Override
    public void syncRepaymentPlan(RepayInfoLufaxVO vo) {
        // 查询待同步分账队列数据
        List<RepayInfoLufaxVO> repayInfoList = this.getSyncData(vo);
        if(CollectionUtils.isEmpty(repayInfoList)){
            logger.warn("没有还款计划信息需要同步给陆金所！");
            return;
        }
        // 证大同步还款计划给陆金所
        syncLufaxRepaymentPlanInterface(repayInfoList);
    }

    @Override
    public void syncRepaymentRecord(RepayInfoLufaxVO vo) {
        // 查询待同步分账队列数据
        List<RepayInfoLufaxVO> repayInfoList = this.getSyncData(vo);
        if(CollectionUtils.isEmpty(repayInfoList)){
            logger.warn("没有还款记录信息需要同步给陆金所！");
            return;
        }
        // 证大同步还款记录给陆金所
        syncLufaxRepaymentRecordInterface(repayInfoList);
    }

    @Override
    public void syncRepaymentDetail(RepayInfoLufaxVO vo) {
        // 查询待同步分账队列数据
        List<RepayInfoLufaxVO> repayInfoList = this.getSyncData(vo);
        if(CollectionUtils.isEmpty(repayInfoList)){
            logger.warn("没有还款明细信息需要同步给陆金所！");
            return;
        }
        // 证大同步还款明细给陆金所
        syncLufaxRepaymentDetailInterface(repayInfoList);
    }
    
    /**
     * 查询待同步分账队列数据
     * @param vo
     * @return
     */
    private List<RepayInfoLufaxVO> getSyncData(RepayInfoLufaxVO vo){
        // 分账队列ID
        Long splitId = vo.getSplitId();
        // 还款类型
        String repayType = vo.getRepayType();
        // 查询待同步分账队列数据
        List<RepayInfoLufaxVO> repayInfoList = null;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("splitIds", new Long[]{splitId});
        map.put("repayTypes", new String[]{repayType});
        if(DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType) || DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)){
            repayInfoList = splitQueueLogDao.findRepayInfoLufaxList(map);
        } else if(DebitRepayTypeEnum.逾期还回.getCode().equals(repayType)){
            repayInfoList = splitQueueLogDao.findDebitSplitQueueLogList(map);
            if(CollectionUtils.isNotEmpty(repayInfoList)){
                for(RepayInfoLufaxVO repayInfo: repayInfoList){
                    SplitQueueLog splitQueueLog = splitQueueLogDao.get(repayInfo.getSplitId());
                    if(SplitResultStateEnum.未分账.getCode().equals(splitQueueLog.getSplitResultState())){
                        this.updateRepaymentDetailAndLoanStatus4Lufax(splitQueueLog);
                    }
                }
            }
        } else {
            repayInfoList = splitQueueLogDao.findRepaymentInfoToLufax(map);
        }
        return repayInfoList;
    }

    public void updateSplitQueue(SplitQueueLog vo) {
        splitQueueLogDao.update(vo);
    }
    
    
    /**
     * 判断是否可以进行还款、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanRepayment(Long loanId, String repayType) {
        VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
        if (!FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getFundsSources())) {
            return true;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        if (DebitRepayTypeEnum.机构还款.getCode().equals(repayType)) {
            // 查询是否存在未入账的划扣记录，如果存在则不能做还款、入账操作
            params.put("loanId", loanId);
            // 未消费
            params.put("state", "0");
            List<RepayResultNotifyLog> repayResultNotifyLogList = repayResultNotifyLogService.findListByMap(params);
            if (CollectionUtils.isNotEmpty(repayResultNotifyLogList)) {
                return false;
            }
            
            // 是否存在未划扣、划扣中的划扣记录，如果存在则不能做还款、入账操作
            params.clear();
            params.put("loanId", loanId);
            params.put("debitResultStates",new String[] { DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣中.getCode() });
            params.put("repayType", repayType);
            List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByMap(params);
            if (CollectionUtils.isNotEmpty(debitQueueLogList)) {
                return false;
            }
        } 
        
        // 是否存在没有结算完成的逾期代偿和一次性回购
        if (DebitRepayTypeEnum.逾期代偿.getCode().equals(repayType)
                || DebitRepayTypeEnum.一次性回购.getCode().equals(repayType)) {
            // 是否存在未分账、分账中的分账记录，如果存在则不能做还款、入账操作
            params.clear();
            params.put("loanId", loanId);
            params.put("splitResultStates", new String[] {
                    SplitResultStateEnum.未分账.getCode(),
                    SplitResultStateEnum.分账中.getCode(),
                    SplitResultStateEnum.分账失败.getCode() });
            // 还款类型
            params.put("repayType", repayType);
            // 查询未推送分账信息或推送分账信息失败的记录条数
            Integer count = splitQueueLogDao.queryPreSplitCount(params);
            if (count > 0) {
                return false;
            }
            
            // 是否存在未划扣、划扣中的划扣记录，如果存在则不能做还款、入账操作
            params.clear();
            params.put("loanId", loanId);
            params.put("debitResultStates",new String[] { DebitResultStateEnum.未划扣.getCode(),
                            DebitResultStateEnum.划扣中.getCode(),
                            DebitResultStateEnum.划扣失败.getCode() });
            params.put("repayType", repayType);
            List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByMap(params);
            if (CollectionUtils.isNotEmpty(debitQueueLogList)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断是否可以进行对公还款认领、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanPublicAccountRepayment(Long loanId) {
        VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
        if (!FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getFundsSources())) {
            return true;
        }
        // 查询是否存在未入账的划扣记录，如果存在则不能做还款、入账操作
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanId", loanId);
        // 未消费
        params.put("state", "0");
        List<RepayResultNotifyLog> repayResultNotifyLogList = repayResultNotifyLogService.findListByMap(params);
        if (CollectionUtils.isNotEmpty(repayResultNotifyLogList)) {
            return false;
        }
        // 是否存在未划扣、划扣中的机构划扣记录，如果存在则不能做还款、入账操作
        params.clear();
        params.put("loanId", loanId);
        params.put("debitResultStates",new String[] { DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣中.getCode() });
        params.put("repayType", DebitRepayTypeEnum.机构还款.getCode());
        List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByMap(params);
        if (CollectionUtils.isNotEmpty(debitQueueLogList)) {
            return false;
        }
        return true;
    }

	@Override
	public List<SplitQueueLog> findOverdueEntrustSplitQueueLogList(Map<String, Object> params) {
		return splitQueueLogDao.findOverdueEntrustSplitQueueLogList(params);
	}
    
    /**
     * 判断多笔划扣中是否存在委托还款的划扣
     * @param debitQueueLogList
     * @return
     */
	private boolean existEntrustRepayment(List<DebitQueueLog> debitQueueLogList) {
		boolean flag = true;
		for (DebitQueueLog log : debitQueueLogList) {
			if (DebitRepayTypeEnum.提前结清.getCode().equals(log.getRepayType())) {
				flag = false;
				break;
			}
		}
		for (DebitQueueLog log : debitQueueLogList) {
			if (flag && DebitRepayTypeEnum.委托还款.getCode().equals(log.getRepayType())) {
				return true;
			}
		}
		return false;
	}
}
