package com.zdmoney.credit.ljs.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.DebitResultStateEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStatueLufaxEnum;
import com.zdmoney.credit.common.constant.RepaymentStateLufaxEnum;
import com.zdmoney.credit.common.constant.lufax.LufaxConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.service.pub.IOperateLogService;
import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.vo.core.LoanVo;
import com.zdmoney.credit.debit.dao.pub.IDebitQueueLogDao;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.framework.vo.lufax.entity.LoanDetailLufax;
import com.zdmoney.credit.framework.vo.lufax.entity.PushLoanInfoFeeInfoEntity;
import com.zdmoney.credit.framework.vo.lufax.entity.PushLoanInfoRepayPlanEntity;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100017Vo;
import com.zdmoney.credit.ljs.dao.pub.ICompensatoryDetailLufaxDao;
import com.zdmoney.credit.ljs.dao.pub.ILoanStatusLufaxDao;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;
import com.zdmoney.credit.ljs.domain.LoanStatusLufax;
import com.zdmoney.credit.ljs.service.pub.ILoanStatusLufaxService;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailLufaxDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetailLufax;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * @author 10098  2017年5月15日 上午9:57:21
 */
@Service
public class LoanStatusLufaxServiceImpl implements ILoanStatusLufaxService {

	private Logger logger = Logger.getLogger(LoanStatusLufaxServiceImpl.class);
	@Autowired
	private ILoanStatusLufaxDao loanStatusLufaxDao;
	@Autowired
	private ISequencesService sequencesService;
	@Autowired
	private IVLoanInfoDao vLoanInfoDao;
	@Autowired
	private ILoanInitialInfoDao loanInitialInfoDao;
	@Autowired
	private ILoanProductDao loanProductDao;
	@Autowired
	private ILoanRepaymentDetailDao loanRepaymentDetailDao;
	@Autowired
	private ILoanRepaymentDetailLufaxDao loanRepaymentDetailLufaxDao;
	@Autowired
	private ILoanBankService loanBankService;
	@Autowired
	private ILoanBsbMappingDao loanBsbMappingDao;
	@Autowired
	private ICompensatoryDetailLufaxDao compensatoryDetailLufaxDao;
	@Autowired
	private IDebitQueueLogDao debitQueueLogDao;
	@Autowired
	private IWorkDayInfoService workDayInfoService;
	@Autowired
	private IOperateLogService operateLogService;
	@Override
	public List<LoanDetailLufax> findLoanDetailLufaxList(Map<String, Object> params) {	
		return loanStatusLufaxDao.findLoanDetailLufaxList(params);
	}

	@Override
	public LoanStatusLufax addLoanStatusLufax(Long loanId, String loanStatus) {
		LoanStatusLufax loanStatusLufax = new LoanStatusLufax();
		loanStatusLufax.setLoanId(loanId);
		loanStatusLufax.setLoanStatus(loanStatus);
		loanStatusLufax.setId(sequencesService.getSequences(SequencesEnum.LOAN_STATUS_LUFAX));
		return loanStatusLufaxDao.insert(loanStatusLufax);
	}

	public JSONObject pushRepayPlanAndLoanInfo2Lufax(LoanVo loanVo) {
		try{
			Lufax100017Vo lufax100017Vo = getLufax100017VoByParams(loanVo);
			JSONObject json = GatewayUtils.callCateWayInterface(lufax100017Vo, GatewayFuncIdEnum.证大同步正式还款计划借据接口.getCode());
			return json;
		}catch(PlatformException e){
			throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
		}catch (Exception e) {
			logger.error("证大同步正式还款计划借据异常",e);
			throw new PlatformException(ResponseEnum.FULL_MSG,"证大同步正式还款计划借据异常");
		}
	}

	public Lufax100017Vo getLufax100017VoByParams(LoanVo loanVo){
		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByAppNo(loanVo.getAppNo());
		if(loanInitialInfo == null){
			throw new PlatformException(ResponseEnum.FULL_MSG, "该appNo未找到债权信息");
		}
		LoanProduct loanProduct = loanProductDao.findByLoanId(loanInitialInfo.getLoanId());
		//String paymentAccno = loanBankService.findNumByLoanId(loanInitialInfo.getLoanId());
		List<LoanRepaymentDetail> repaymentDetails = loanRepaymentDetailDao.findByLoanId(loanInitialInfo.getLoanId());
		//VLoanInfo vLoanInfo = vLoanInfoDao.get(loanVo.getLoanId());
		LoanBsbMapping loanBsbMapping = loanBsbMappingDao.getByLoanId(loanInitialInfo.getLoanId());
		Date putoutDate = loanBsbMapping.getValueDate();
		Lufax100017Vo lufax100017Vo = new Lufax100017Vo();
		lufax100017Vo.setLoanNo(loanVo.getApplyNo());
		lufax100017Vo.setApplyNo(loanVo.getApplyNo());
		lufax100017Vo.setAnshuo_batch_id(loanVo.getApplyNo());
		lufax100017Vo.setProductType(loanVo.getProductType());
		lufax100017Vo.setLufaxLoanReqId(loanVo.getLufaxLoanReqId());
		lufax100017Vo.setPutoutDate(Dates.getDateTime(putoutDate, Dates.DATAFORMAT_YYYYMMDD));
		lufax100017Vo.setMaturityDate(Dates.getDateTime(loanProduct.getEndrdate(),Dates.DATAFORMAT_YYYYMMDD));
		lufax100017Vo.setCurdeductDate(Dates.getDateTime(loanProduct.getStartrdate(),Dates.DATAFORMAT_YYYYMMDD));//借据会计日期取值？
		lufax100017Vo.setDefaultPaydate(loanProduct.getPromiseReturnDate().toString());
		lufax100017Vo.setNormalPrincipal(loanProduct.getPactMoney().toString());
		lufax100017Vo.setNextRepaydate(Dates.getDateTime(loanProduct.getStartrdate(),Dates.DATAFORMAT_YYYYMMDD));
		lufax100017Vo.setNextRateAdjustDate(Dates.getDateTime(loanProduct.getStartrdate(),Dates.DATAFORMAT_YYYYMMDD));
		List<PushLoanInfoRepayPlanEntity> repayPlans = new ArrayList<>();
		for(LoanRepaymentDetail repayment:repaymentDetails){
			PushLoanInfoRepayPlanEntity repayEntity = new PushLoanInfoRepayPlanEntity();
			repayEntity.setTermNo(repayment.getCurrentTerm().intValue());
			repayEntity.setPayDate(Dates.getDateTime(repayment.getReturnDate(),Dates.DATAFORMAT_YYYYMMDD));
			if(repayment.getCurrentTerm().intValue() == 1){
				repayEntity.setValueDate(Dates.getDateTime(putoutDate, Dates.DATAFORMAT_YYYYMMDD)); //第一期起息日为 放款日期？
			}else{
				repayEntity.setValueDate(Dates.getDateTime(Dates.addMonths(repayment.getReturnDate(), -1), Dates.DATAFORMAT_YYYYMMDD));
			}
			repayEntity.setExpiryDate(Dates.getDateTime(Dates.addDay(repayment.getReturnDate(), -1),Dates.DATAFORMAT_YYYYMMDD));
			repayEntity.setLoanRate(loanProduct.getRateey().multiply(new BigDecimal(100)));
			repayEntity.setPayprincipalamt(repayment.getReturneterm().subtract(repayment.getCurrentAccrual()));
			repayEntity.setActualPayprincipalamt(new BigDecimal(0.00));
			repayEntity.setPayinteamt(repayment.getCurrentAccrual());
			repayEntity.setActualPayinteamt(new BigDecimal(0.00));
			repayEntity.setPayfineamt(new BigDecimal(0.00));
			repayEntity.setActualPayfineamt(new BigDecimal(0.00));
			repayEntity.setPaycompound(new BigDecimal(0.00));
			repayEntity.setActualPaycompound(new BigDecimal(0.00));
//			repayEntity.setFineCalculateDate(Dates.getDateTime(repayment.getPenaltyDate(),Dates.DATAFORMAT_YYYYMMDD));
//			repayEntity.setFineBase(repaymentDetails.get(0).getDeficit());
//			repayEntity.setCompoundCalculateDate("");
//			repayEntity.setCompoundBase(new BigDecimal(0.0));
			repayEntity.setPrincipalBalance(repayment.getPrincipalBalance());
			repayEntity.setTermAmt(repayment.getReturneterm());
//			repayEntity.setFinishDate("");
			repayEntity.setStatus("01");
			repayPlans.add(repayEntity);
		}
		lufax100017Vo.setRepayPlans(repayPlans);
		List<PushLoanInfoFeeInfoEntity> feeList = new ArrayList<>();
		PushLoanInfoFeeInfoEntity feeInfo = new PushLoanInfoFeeInfoEntity();
		feeInfo.setFeeCode("PFM_FEE_ONE"); //服务费 一次性平台费 PFM_FEE_ONE
		feeInfo.setPayfeeamt(loanProduct.getPactMoney().subtract(loanInitialInfo.getMoney()));
		feeInfo.setActualPayfeeamt(loanProduct.getPactMoney().subtract(loanInitialInfo.getMoney()));
		feeList.add(feeInfo);
		lufax100017Vo.setFeeList(feeList);
		return lufax100017Vo;
	}

	@Override
	public List<LoanDetailLufax> findLoanDetailLufaxList2Transmit(Map<String, Object> params) {
		Date lastDate = Dates.parse(params.get("currDate").toString(), Dates.DEFAULT_DATE_FORMAT);
		List<LoanDetailLufax> loanDetailLufaxes = new ArrayList<>();
		List<LoanStatusLufax> list = loanStatusLufaxDao.findLoanStatusLufax2Transmit(params);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		for(LoanStatusLufax lsl : list){
			LoanDetailLufax loanDetailLufax = new LoanDetailLufax();
			try{
				VLoanInfo vLoanInfo = vLoanInfoDao.get(lsl.getLoanId());
				List<LoanRepaymentDetailLufax> repayList = loanRepaymentDetailLufaxDao.findByLoanId(lsl.getLoanId());
				CompensatoryDetailLufax cdlVo = new CompensatoryDetailLufax();
				cdlVo.setLoanId(lsl.getLoanId());
				List<CompensatoryDetailLufax> compenList = compensatoryDetailLufaxDao.findListByVo(cdlVo);
				if(compenList == null){
					compenList = new ArrayList<CompensatoryDetailLufax>();
				}
				BigDecimal normalPrincipal = new BigDecimal(0.00); // 正常本金
				BigDecimal overduePrincipal  = new BigDecimal(0.00); //逾期本金
				BigDecimal buyBackOverduePrincipal = new BigDecimal(0.00); //回购逾期本金
				BigDecimal buyBackNormalPrincipal = new BigDecimal(0.00); //回购正常本金
				BigDecimal overdueFine = new BigDecimal(0.00); //罚息金额
				Map<String, Object> dqParams = new HashMap<>();
				dqParams.put("loanId", vLoanInfo.getId());
				List<DebitQueueLog> debitList = debitQueueLogDao.findListByMap(dqParams);
				Map<String, String> dateMap = getDateMap4Lufax(lastDate, lsl, repayList, debitList);
				String nextRepaydate = dateMap.get("nextRepaydate"); //下个还款日
				String lastInterestExpiryDate = dateMap.get("lastInterestExpiryDate"); //上个结息日
				String finishDate = dateMap.get("finishDate"); //结清日期
				String batchBeginDate = dateMap.get("batchBeginDate"); //批扣发起日期
				String curTerm = dateMap.get("curTerm"); //当前执行期次
				int overDueDays = Integer.valueOf(dateMap.get("overDueDays")); //逾期天数
				String batchStatus = dateMap.get("batchStatus"); //报盘状态 0- 需报盘 1-不需报盘
				boolean hasBuyback = false; //是否发生过一次性回购
				int buybackTerm = 0; //一次性回购 期数
				for(CompensatoryDetailLufax cdl : compenList){
					if("02".equals(cdl.getType())){
						hasBuyback = true;
						buybackTerm = cdl.getTerm();
					}
				}
				boolean isOverdue = LoanStatueLufaxEnum.逾期.getCode().equals(lsl.getLoanStatus()) && !hasBuyback; //债权状态是否逾期
				for(CompensatoryDetailLufax cdl : compenList){
					if(hasBuyback){
						if(cdl.getTerm() >= (buybackTerm-3)){
							//一次性回购逾期本金为 第一次代偿本金到第四次发生一次性回购时的本金和
							buyBackOverduePrincipal = buyBackOverduePrincipal.add(cdl.getCorpusAmount());
						}
					}
					//是否追偿
					boolean hasRecovery =  hasRecoveryByTerm(cdl.getTerm(), repayList);
					if(isOverdue && !hasRecovery){
						//逾期本金 为 代偿本金且未被追偿的 本金和
						overduePrincipal = overduePrincipal.add(cdl.getCorpusAmount());
					}
				}
				for(LoanRepaymentDetailLufax ldl: repayList){
					if(ldl.getReturnDate().after(lastDate)){
						//正常本金 为昨日之后 还款计划本金和
						normalPrincipal = normalPrincipal.add(ldl.getReturneterm().subtract(ldl.getCurrentAccrual()));
					}
					if(hasBuyback){
						if(ldl.getCurrentTerm() > buybackTerm){
							//一次性回购正常本金为 回购当期之后 还款计划本金和
							buyBackNormalPrincipal = buyBackNormalPrincipal.add(ldl.getReturneterm().subtract(ldl.getCurrentAccrual()));
						}
					}
				}
				loanDetailLufax.setLoan_no(LufaxConst.ZDJR + vLoanInfo.getChannelPushNo());
				loanDetailLufax.setLoan_status(lsl.getLoanStatus());
				loanDetailLufax.setIs_need_offer("0");
				loanDetailLufax.setCurdeduct_date(Dates.getDateTime(Dates.DATAFORMAT_YYYYMMDD));
				if(hasBuyback){
					loanDetailLufax.setNormal_principal(buyBackNormalPrincipal);
					loanDetailLufax.setOverdue_principal(buyBackOverduePrincipal);
				}else{
					loanDetailLufax.setNormal_principal(normalPrincipal);
					loanDetailLufax.setOverdue_principal(overduePrincipal);
				}
				if(overDueDays > 3){
					overdueFine = loanDetailLufax.getOverdue_principal().multiply(new BigDecimal(overDueDays).multiply(vLoanInfo.getPenaltyRate()));
					overdueFine = overdueFine.setScale(2, RoundingMode.HALF_UP);
				}
				loanDetailLufax.setOverdue_fine(overdueFine);
				loanDetailLufax.setBatch_status(batchStatus);
				loanDetailLufax.setOverdue_days(String.valueOf(overDueDays));
				loanDetailLufax.setNext_repaydate(nextRepaydate);
				loanDetailLufax.setLast_interest_expiry_date(lastInterestExpiryDate);
				loanDetailLufax.setFinish_date(finishDate);
				loanDetailLufax.setBatch_begin_date(batchBeginDate);
				loanDetailLufax.setCurrent_term(Integer.valueOf(curTerm));
				loanDetailLufaxes.add(loanDetailLufax);
			}catch(Exception e){
				logger.error("获取借据信息异常：", e);
			}
		}
		return loanDetailLufaxes;
	}

	/**
	 * 获取借据同步相关信息
	 * @param lastDate
	 * @param lsl
	 * @param repayList
	 * @return
	 */
	private Map<String, String> getDateMap4Lufax(Date lastDate, LoanStatusLufax lsl, List<LoanRepaymentDetailLufax> repayList, List<DebitQueueLog> debitList) {
		VLoanInfo vLoanInfo = vLoanInfoDao.get(repayList.get(0).getLoanId());
		Map<String, String> map = new HashMap<>();
		//LoanRepaymentDetailLufax lastTerm = repayList.get(repayList.size()-1);
		String nextRepaydate = ""; //下个还款日
		String lastInterestExpiryDate = ""; //上个结息日
		String finishDate = ""; //结清日期
		String batchBeginDate = ""; //批扣发起日期
		int overDueDays  = 0; //逾期天数
		String batchStatus = "1"; //报盘状态 0- 需报盘 1-不需报盘
		int curTerm = vLoanInfo.getTime().intValue(); //当前执行期数
		if(LoanStatueLufaxEnum.结清.getCode().equals(lsl.getLoanStatus()) || LoanStatueLufaxEnum.追偿结清.getCode().equals(lsl.getLoanStatus())){
			nextRepaydate = "";
		}else{
			for(LoanRepaymentDetailLufax ldl:repayList){
				if(!ldl.getReturnDate().before(lastDate)){
					curTerm = ldl.getCurrentTerm().intValue();
					nextRepaydate = Dates.getDateTime(ldl.getReturnDate(),Dates.DATAFORMAT_YYYYMMDD);
					break;
				}
			}
		}
		for(LoanRepaymentDetailLufax ldl:repayList){
			if(ldl.getReturnDate().before(lastDate)){
				lastInterestExpiryDate = Dates.getDateTime(Dates.addDay(ldl.getReturnDate(), -1),Dates.DATAFORMAT_YYYYMMDD);
			}
		}
		if(CollectionUtils.isNotEmpty(debitList)){
			for(DebitQueueLog debitQueueLog: debitList){
				if(DebitResultStateEnum.划扣成功.getCode().equals(debitQueueLog.getDebitResultState())){
					if(debitQueueLog.getDebitResultDate() != null){
						finishDate = Dates.getDateTime(debitQueueLog.getDebitResultDate(), Dates.DATAFORMAT_YYYYMMDD);
					}

				}
				if(debitQueueLog.getDebitNotifyDate() != null){
					batchBeginDate = Dates.getDateTime(debitQueueLog.getDebitNotifyDate(), Dates.DATAFORMAT_YYYYMMDD);
				}
			}
			DebitQueueLog dql  = debitList.get(debitList.size()-1);
			// 划扣期数，如果字段为空，默认为第1期
			int repayTerm = dql.getRepayTerm() == null ? 1 : dql.getRepayTerm().intValue();
			LoanRepaymentDetailLufax lrd = null;
			for(LoanRepaymentDetailLufax ldl:repayList){
				if(ldl.getCurrentTerm().intValue() == repayTerm){
					lrd = ldl;
				}
			}
			if(!DebitResultStateEnum.划扣成功.getCode().equals(dql.getDebitResultState())){
				batchStatus = "0";
				overDueDays = workDayInfoService.getWorkDaysInRegion(lrd.getReturnDate(),Dates.getCurrDate());
			}else{
				if(dql.getDebitResultDate() != null){
					overDueDays = workDayInfoService.getWorkDaysInRegion(lrd.getReturnDate(),dql.getDebitResultDate());
				}
			}
		}

		map.put("nextRepaydate",nextRepaydate);
		map.put("lastInterestExpiryDate",lastInterestExpiryDate);
		map.put("finishDate",finishDate);
		map.put("batchBeginDate",batchBeginDate);
		map.put("curTerm",String.valueOf(curTerm));
		map.put("batchStatus",batchStatus);
		map.put("overDueDays",String.valueOf(overDueDays));
		return map;
	}

	//判断当期是否追偿
	private boolean hasRecoveryByTerm(int term, List<LoanRepaymentDetailLufax> repayList) {
		boolean flag = false;
		for(LoanRepaymentDetailLufax lrl:repayList){
			if(term == lrl.getCurrentTerm()){
				if(RepaymentStateLufaxEnum.当期追偿完成.getCode().equals(lrl.getLoanStatusLufax())
						|| RepaymentStateLufaxEnum.追偿结清.getCode().equals(lrl.getLoanStatusLufax())){
					flag = true;
				}
			}
		}
		return flag;
	}
	
	public void createOperateLog(List<LoanDetailLufax> loanDetailLufaxList) {
		for (LoanDetailLufax loanDetailLufax : loanDetailLufaxList) {
			// 借据状态
			String loanStatus = loanDetailLufax.getLoan_status();
			// 借据号
			String loanNo = loanDetailLufax.getLoan_no();
			if (LoanStatueLufaxEnum.结清.getCode().equals(loanStatus)
					|| LoanStatueLufaxEnum.追偿结清.getCode().equals(loanStatus)
					|| LoanStatueLufaxEnum.准备金代偿结清.getCode().equals(loanStatus)
					|| LoanStatueLufaxEnum.保证金代偿结清.getCode().equals(loanStatus)) {
				try {
					loanNo = loanNo.replace(LufaxConst.ZDJR, "");
					VLoanInfo searchVo = new VLoanInfo();
					//searchVo.setAppNo(loanNo);
					searchVo.setChannelPushNo(loanNo);
					List<VLoanInfo> loanInfoList = vLoanInfoDao.findListByVo(searchVo);
					if (CollectionUtils.isNotEmpty(loanInfoList)) {
						VLoanInfo loanInfo = loanInfoList.get(0);
						operateLogService.addOperateLog(loanInfo.getId(), "09", "1");
					}
				} catch (Exception e) {
					logger.error("同步借据信息后记录操作日志发生异常：", e);
				}
			}
		}
	}

	@Override
	public void updateLufuxStatusToYuqi() {
		loanStatusLufaxDao.updateLufuxStatusToYuqi();
	}
}
