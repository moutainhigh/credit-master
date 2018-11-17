package com.zdmoney.credit.loan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.vo.BsyhSpecialRepayVo;
import com.zdmoney.credit.common.constant.*;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.RequestInfo;
import com.zdmoney.credit.framework.vo.abs.entity.PayOverEntity;
import com.zdmoney.credit.framework.vo.abs.input.Abs100005Vo;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseOneTimeSettlementDao;
import com.zdmoney.credit.loan.dao.pub.ILoanSpecialRepaymentDao;
import com.zdmoney.credit.loan.domain.*;
import com.zdmoney.credit.loan.service.pub.*;
import com.zdmoney.credit.offer.dao.pub.IOfferRepayInfoDao;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.repay.domain.AbsOneTimeSettlementAdvice;
import com.zdmoney.credit.repay.service.pub.IRepayTrailService;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

@Service
@Transactional
public class LoanSpecialRepaymentServiceImpl implements ILoanSpecialRepaymentService {

	private static final Logger logger = Logger.getLogger(LoanSpecialRepaymentServiceImpl.class);

	@Autowired
	ILoanSpecialRepaymentDao loanSpecialRepaymentDao;

	@Autowired
	IVLoanInfoService iVLoanInfoService;

	@Autowired
	ISequencesService sequencesServiceImpl;

	@Autowired
	IComOrganizationDao comOrganizationDaoImpl;

	@Autowired
	IComOrganizationService comOrganizationServiceImpl;

	@Autowired
	IAfterLoanService afterLoanServiceImpl;

	@Autowired
	IOfferInfoService offerInfoServiceImpl;
	@Autowired
	IOfferCreateService offerCreateService;

	@Autowired
	ILoanProcessHistoryService loanProcessHistoryServiceImpl;
	@Autowired
	ILoanBaseOneTimeSettlementDao loanBaseOneTimeSettlementDaoImpl;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanBankService loanBankServiceImpl;
	@Autowired
	private ISpecialRepaymentApplyService specialRepaymentApplyService;
	@Autowired
	IRepayTrailService repayTrailService;
	@Autowired
	IOfferRepayInfoDao offerRepayInfoDao;

	@Value("${gateway.interface.url}")
	public String gatewayInterfaceUrl;

	@Override
	public LoanSpecialRepayment findbyLoanAndType(Long loanId, String type, String state) {
		LoanSpecialRepayment loanSpecialRepayment = new LoanSpecialRepayment();
		loanSpecialRepayment.setLoanId(loanId);
		loanSpecialRepayment.setSpecialRepaymentType(type);
		loanSpecialRepayment.setSpecialRepaymentState(state);

		List<LoanSpecialRepayment> list = loanSpecialRepaymentDao.findListByVo(loanSpecialRepayment);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public LoanSpecialRepayment findByLoanIdAndDateAndTypeAndState(Map<String, Object> map) {
		return loanSpecialRepaymentDao.findByLoanIdAndDateAndTypeAndState(map);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updateSpecialRepaymentStateAtEndOfDay(int promiseReturnDate1) {
		return loanSpecialRepaymentDao.updateSpecialRepaymentStateAtEndOfDay(promiseReturnDate1);
	}

	/**
	 * 通过债权ID、申请日期、类型数组、状态查询特殊还款
	 *
	 * @param loanId
	 *            债权ID
	 * @param requestDate
	 *            申请日期
	 * @param types
	 *            类型数组
	 * @param state
	 *            状态
	 * @return
	 */
	@Override
	public LoanSpecialRepayment findByLoanIdAndRequestDateAndTypesAndState(Long loanId, Date requestDate,
			String[] types, String state) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("requestDate", requestDate);
		paramMap.put("specialRepaymentTypes", types);
		paramMap.put("specialRepaymentState", state);
		List<LoanSpecialRepayment> list = loanSpecialRepaymentDao.findListByMap(paramMap);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int update(LoanSpecialRepayment loanSpecialRepayment) {
		return loanSpecialRepaymentDao.update(loanSpecialRepayment);
	}

	@Override
	@Transactional
	public void updateSpecialOneTimeState(Long loanId, String allCleanApplyState, String curCleanApplyState,
			Date allCleanClosingDate, Date curCleanClosingDate, Date requestDate) {
		Assert.notNull(loanId, "借款编号");
		// Assert.notNullAndEmpty(allCleanApplyState, "提前一次性结清状态");
		// Assert.notNullAndEmpty(curCleanApplyState, "提前扣款状态");

		if (!"on".equals(allCleanApplyState) && !"off".equals(allCleanApplyState)
				&& Strings.isNotEmpty(allCleanApplyState)) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "无效参数");
		}
		if (!"on".equals(curCleanApplyState) && !"off".equals(curCleanApplyState)
				&& Strings.isNotEmpty(curCleanApplyState)) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "无效参数");
		}
		if ("on".equals(allCleanApplyState) && "on".equals(curCleanApplyState)) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "提前一次性结清 和 提前扣款 不能同时申请");
		}
		/** 登陆者信息 **/
		User user = UserContext.getUser();

		VLoanInfo loanInfo = iVLoanInfoService.findByLoanId(loanId);
		Assert.notNull(loanInfo, ResponseEnum.FULL_MSG, "借款数据不存在[" + loanId + "]");
		
		// 合同来源
        /*String fundsSource = loanInfo.getFundsSources();
        if (FundsSourcesTypeEnum.陆金所.getValue().equals(fundsSource) && "on".equals(allCleanApplyState)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "合同来源为陆金所，禁止做提前结清申请");
        }*/

		/** 查询提前一次性结清申请状态 **/
		LoanSpecialRepayment allCleanApplyStateSpecialRepayment = findbyLoanAndType(loanId,
				SpecialRepaymentTypeEnum.一次性还款.name(), SpecialRepaymentStateEnum.申请.name());

		/** 查询提前扣款申请状态 **/
		LoanSpecialRepayment curCleanApplyStateSpecialRepayment = findbyLoanAndType(loanId,
				SpecialRepaymentTypeEnum.提前扣款.name(), SpecialRepaymentStateEnum.申请.name());

		/** 取消提前一次性结清申请 **/
		if ("off".equals(allCleanApplyState)) {
			if (loanInfo.getLoanState().equals(LoanStateEnum.预结清.name())) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "该笔借款预结清，不允许取消提前一次性结清");
			}
			/** 已经申请的记录，将状态更新成取消 **/
			if (allCleanApplyStateSpecialRepayment == null) {
				/** 提前一次性结清取消失败原因：未找到申请记录 **/
			} else {
				//先关闭申请减免
				specialRepaymentApplyService.offOneTimeSettle(loanId);
				allCleanApplyStateSpecialRepayment.setSpecialRepaymentState(SpecialRepaymentStateEnum.取消.name());
				loanSpecialRepaymentDao.update(allCleanApplyStateSpecialRepayment);

				/** 取消报盘数据 **/
//				offerCreateService.revokeOfferInfoBySpecialRepay(allCleanApplyStateSpecialRepayment.getId());
			}
		}
		/** 取消提前扣款申请 **/
		if ("off".equals(curCleanApplyState)) {
			/** 查询已经申请的记录，将状态更新成取消 **/
			if (curCleanApplyStateSpecialRepayment == null) {
				/** 提前一次性结清取消失败原因：未找到申请记录 **/
			} else {
				curCleanApplyStateSpecialRepayment.setSpecialRepaymentState(SpecialRepaymentStateEnum.取消.name());
				loanSpecialRepaymentDao.update(curCleanApplyStateSpecialRepayment);

				/** 取消报盘数据 **/
//				offerCreateService.revokeOfferInfoBySpecialRepay(curCleanApplyStateSpecialRepayment.getId());
			}
		}
		/** 申请提前一次性结清 **/
		if ("on".equals(allCleanApplyState)) {
			/** 如果借款状态为结清,不允许申请提前一次性结清 **/
			if (loanInfo.getLoanState().equals(LoanStateEnum.结清.name())) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "该笔借款已结清，不允许申请提前一次性结清");
			}
			/** 检查是否已申请过提前一次性结清 **/
			if (allCleanApplyStateSpecialRepayment != null) {
				/** 提前一次性结清已申请，请误重复申请 **/

			} else {
				allCleanApplyStateSpecialRepayment = new LoanSpecialRepayment();
				allCleanApplyStateSpecialRepayment.setId(sequencesServiceImpl
						.getSequences(SequencesEnum.LOAN_SPECIAL_REPAYMENT));
				allCleanApplyStateSpecialRepayment.setLoanId(loanId);
				allCleanApplyStateSpecialRepayment.setSpecialRepaymentType(SpecialRepaymentTypeEnum.一次性还款.name());
				allCleanApplyStateSpecialRepayment.setSpecialRepaymentState(SpecialRepaymentStateEnum.申请.name());
				allCleanApplyStateSpecialRepayment.setProposerId(user.getId());
				allCleanApplyStateSpecialRepayment.setMemo("");
				allCleanApplyStateSpecialRepayment.setRequestDate(Dates.getCurrDate());
				allCleanApplyStateSpecialRepayment.setAmount(BigDecimal.ZERO);
				loanSpecialRepaymentDao.insert(allCleanApplyStateSpecialRepayment);

				/** 写入报盘数据 **/
				/** 无论报盘是否生成，不影像申请提前结清操作 **/
//				offerCreateService.createOfferInfoBySpecialRepay(allCleanApplyStateSpecialRepayment.getId(), 0); 提前结清申请取消生成报盘文件
			}
		}
		/** 申请提前扣款 **/
		if ("on".equals(curCleanApplyState)) {
			// Assert.notNull(noOneTimeclosingDate, "自动取消时间");
			Assert.notNull(requestDate, "自动生效时间");

			/** 如果当期状态为结清,不允许申请提前扣款 **/
			List<LoanRepaymentDetail> repayList = afterLoanServiceImpl
					.getAllInterestOrLoan(Dates.getCurrDate(), loanId);
			Assert.notCollectionsEmpty(repayList, ResponseEnum.FULL_MSG, "逾期和当期均已结清，不允许申请提前扣款");

			/** 当期还款日 **/
			Date curReturnDate = repayList.get(repayList.size() - 1).getReturnDate();

			/** 自动生效时间不能大于还款日 **/
			if (Dates.compareTo(requestDate, curReturnDate) >= 0) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "自动生效时间必须小于当期还款日（"
						+ Dates.getDateTime(curReturnDate, Dates.DEFAULT_DATE_FORMAT) + "）!");
			}
			/** 自动取消时间不能大于还款日 **/
			// if (Dates.compareTo(noOneTimeclosingDate, curReturnDate) > 0) {
			// throw new PlatformException(ResponseEnum.FULL_MSG,
			// "自动取消时间必须小于当期还款日（含"
			// + Dates.getDateTime(curReturnDate,
			// Dates.DEFAULT_DATE_FORMAT) + "）!");
			// }

			/** 检查是否已申请过提前一次性结清 **/
			if (curCleanApplyStateSpecialRepayment != null) {
				/** 提前一次性结清已申请，请误重复申请 **/
				// noOneTimeLoanSpecialRepayment
				// .setClosingDate(noOneTimeclosingDate);
				curCleanApplyStateSpecialRepayment.setRequestDate(requestDate);
				loanSpecialRepaymentDao.update(curCleanApplyStateSpecialRepayment);
			} else {

				/** 判断挂账金额是否大于当期应还总额（逾期本息和 + 罚息 + 当期本息和） **/
				Date currDate = Dates.getCurrDate();
				List<LoanRepaymentDetail> repayDatailList = afterLoanServiceImpl.getAllInterestOrLoan(currDate, loanId);
				/** 逾期本息和 **/
				BigDecimal overdueAmount = afterLoanServiceImpl.getOverdueAmount(repayDatailList, currDate);
				/** 罚息 **/
				BigDecimal fine = afterLoanServiceImpl.getFine(repayDatailList, currDate);
				/** 当期本息和 **/
				BigDecimal currAmount = afterLoanServiceImpl.getCurrAmount(repayDatailList, currDate);
				/** 挂账金额 **/
				BigDecimal accAmount = afterLoanServiceImpl.getAccAmount(loanId);

				if (accAmount.compareTo(overdueAmount.add(fine).add(currAmount)) >= 0) {
					/** 挂账金额大于本期应还金额，系统拒绝申请提前扣款 **/
					throw new PlatformException(ResponseEnum.FULL_MSG, "挂账金额大于当期应还总额，无须申请提前扣款！");
				}

				curCleanApplyStateSpecialRepayment = new LoanSpecialRepayment();
				curCleanApplyStateSpecialRepayment.setId(sequencesServiceImpl
						.getSequences(SequencesEnum.LOAN_SPECIAL_REPAYMENT));
				curCleanApplyStateSpecialRepayment.setLoanId(loanId);
				curCleanApplyStateSpecialRepayment.setSpecialRepaymentType(SpecialRepaymentTypeEnum.提前扣款.name());
				curCleanApplyStateSpecialRepayment.setSpecialRepaymentState(SpecialRepaymentStateEnum.申请.name());
				curCleanApplyStateSpecialRepayment.setProposerId(user.getId());
				curCleanApplyStateSpecialRepayment.setMemo("");
				curCleanApplyStateSpecialRepayment.setRequestDate(requestDate);
				curCleanApplyStateSpecialRepayment.setAmount(BigDecimal.ZERO);
				// noOneTimeLoanSpecialRepayment
				// .setClosingDate(noOneTimeclosingDate);
				loanSpecialRepaymentDao.insert(curCleanApplyStateSpecialRepayment);
			}
//			if(!"包商银行".equals(loanInfo.getLoanBelong())){//包商银行不写入报盘数据
				/** 写入报盘数据 **/
//				offerCreateService.createOfferInfoBySpecialRepay(curCleanApplyStateSpecialRepayment.getId()); 申请提前还款时，取消自动报盘
//			}

		}

		if (curCleanApplyStateSpecialRepayment != null && allCleanApplyStateSpecialRepayment != null) {
			if (curCleanApplyStateSpecialRepayment.getSpecialRepaymentState().equals(
					SpecialRepaymentStateEnum.申请.name())
					&& allCleanApplyStateSpecialRepayment.getSpecialRepaymentState().equals(
							SpecialRepaymentStateEnum.申请.name())) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "提前一次性结清和提前扣款不能同时开启!");
			}
		}
	}

	/**
	 * 更新特殊还款表的状态到结束
	 *
	 * @param loanId
	 * @param specialRepaymentType
	 *            原类型
	 * @param state
	 *            原状态
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSpecialRepaymentToEnd(Long loanId, String specialRepaymentType, String state) {
		LoanSpecialRepayment loanSpecial = findbyLoanAndType(loanId, specialRepaymentType, state);
		if (loanSpecial != null) {
			loanSpecial.setSpecialRepaymentState(SpecialRepaymentStateEnum.结束.getValue());
			loanSpecial.setUpdateTime(new Date());
			update(loanSpecial);
		} else {
			logger.error("记账更新特殊还款表时，未找到记录！ loanid=" + loanId);
		}
	}

	public Pager findSpecialRepaymentList(Map<String, Object> map) {

		return loanSpecialRepaymentDao.findSpecialRepaymentList(map);
	}

	@Override
	public int findSpecialRepaymentByLoanId(Long loanId) {

		return loanSpecialRepaymentDao.findSpecialRepaymentByLoanId(loanId);
	}

	@Override
	public int findLoanByUserIdAndLoanState(String userId) {
		// TODO Auto-generated method stub

		return loanSpecialRepaymentDao.findLoanByUserIdAndLoanState(userId);
	}

	@Override
	public LoanSpecialRepayment findLoanSpecialRepaymentByVO(LoanSpecialRepayment loanSpecialRepayment) {
		// TODO Auto-generated method stub
		LoanSpecialRepayment l = null;
		List<LoanSpecialRepayment> list = loanSpecialRepaymentDao.findListByVo(loanSpecialRepayment);
		if (list != null && list.size() > 0) {
			l = list.get(0);
		}
		return l;
	}

	@Override
	public LoanSpecialRepayment findSpecialRepaymentCount(LoanSpecialRepayment loanSpecialRepayment) {
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String type = SpecialRepaymentTypeEnum.正常费用减免.name();
		String states = SpecialRepaymentStateEnum.区域总审批.name();
		paramMap.put("specialRepaymentType", type);
		paramMap.put("specialRepaymentState", states);
		paramMap.put("loanId", loanSpecialRepayment.getLoanId());
		List<LoanSpecialRepayment> list = loanSpecialRepaymentDao.findListByMap(paramMap);
		if (list != null && list.size() > 0) {
			loanSpecialRepayment = list.get(0);
		}
		return loanSpecialRepayment;
	}

	@Override
	public void insert(LoanSpecialRepayment loanSpecialRepayment) {
		// TODO Auto-generated method stub
		loanSpecialRepayment.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_SPECIAL_REPAYMENT));
		loanSpecialRepaymentDao.insert(loanSpecialRepayment);
	}

	@Override
	public Pager ZBfindSpecialRepaymentList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return loanSpecialRepaymentDao.ZBfindSpecialRepaymentList(map);
	}

	@Override
	public LoanSpecialRepayment findById(Long specialRepayId) {
		return loanSpecialRepaymentDao.get(specialRepayId);
	}

	/**
	 * 罚息减免申请状态变更
	 *
	 * @param loanId
	 *            借款编号
	 * @param reliefPenaltyState
	 *            罚息减免申请状态
	 * @param money
	 *            减免金额
	 * @param proposerId
	 *            申请人
	 * @param memo
	 *            备注
	 */
	@Override
	@Transactional
	public LoanSpecialRepayment updateReliefPenaltyState(Long loanId, boolean reliefPenaltyState, String money, Long proposerId,
			String memo) {
		Assert.notNull(loanId, "借款编号");

		VLoanInfo loanInfo = iVLoanInfoService.findByLoanId(loanId);
		Assert.notNull(loanInfo, ResponseEnum.FULL_MSG, "借款数据不存在[" + loanId + "]");
		if (!(loanInfo.getLoanState().equalsIgnoreCase(LoanStateEnum.逾期.name()) || loanInfo.getLoanState()
				.equalsIgnoreCase(LoanStateEnum.正常.name()))) {
			/** 只有逾期借款才能申请罚息减免 **/
			throw new PlatformException(ResponseEnum.FULL_MSG, "只有逾期借款和正常借款才能申请罚息减免[该笔借款当前状态为"
					+ loanInfo.getLoanState() + "]");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanId", loanId);
		map.put("currDate", Dates.getCurrDate());
		map.put("states", new String[] { SpecialRepaymentStateEnum.申请.name(), SpecialRepaymentStateEnum.取消.name() });
		map.put("type", SpecialRepaymentTypeEnum.减免.name());

		/** 查询罚息减免申请状态 **/
		LoanSpecialRepayment reliefPenaltyStateObj = findByLoanIdAndDateAndTypeAndState(map);

		/** 是否申请过标识位 **/
		boolean isRequested = false;
		if (reliefPenaltyStateObj != null
				&& reliefPenaltyStateObj.getSpecialRepaymentState().equals(SpecialRepaymentStateEnum.申请.name())) {
			isRequested = true;
		}

		if (reliefPenaltyState) {
			BigDecimal moneyValue = Assert.notBigDecimal(money, "减免金额");

			/** 检查是否已申请罚息减免 **/
			if (isRequested) {
				/** 已申请 存在修改减免金额的情况，进行更新数据 **/
				reliefPenaltyStateObj.setAmount(moneyValue);
				loanSpecialRepaymentDao.update(reliefPenaltyStateObj);
			} else {
				if (reliefPenaltyStateObj == null) {
					reliefPenaltyStateObj = new LoanSpecialRepayment();
					reliefPenaltyStateObj
							.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_SPECIAL_REPAYMENT));
					reliefPenaltyStateObj.setLoanId(loanId);
					reliefPenaltyStateObj.setAmount(moneyValue);
					reliefPenaltyStateObj.setSpecialRepaymentType(SpecialRepaymentTypeEnum.减免.name());
					reliefPenaltyStateObj.setSpecialRepaymentState(SpecialRepaymentStateEnum.申请.name());
					reliefPenaltyStateObj.setProposerId(proposerId);
					reliefPenaltyStateObj.setMemo(memo);
					reliefPenaltyStateObj.setRequestDate(Dates.getCurrDate());
					loanSpecialRepaymentDao.insert(reliefPenaltyStateObj);
				} else {
					reliefPenaltyStateObj.setProposerId(proposerId);
					reliefPenaltyStateObj.setMemo(memo);
					reliefPenaltyStateObj.setRequestDate(Dates.getCurrDate());
					reliefPenaltyStateObj.setAmount(moneyValue);
					reliefPenaltyStateObj.setSpecialRepaymentState(SpecialRepaymentStateEnum.申请.name());
					loanSpecialRepaymentDao.update(reliefPenaltyStateObj);
				}
			}
			/** 写入报盘数据 **/
//			if (loanInfo.getLoanState().equalsIgnoreCase(LoanStateEnum.逾期.name())) {
//				offerCreateService.createOfferInfoBySpecialRepay(reliefPenaltyStateObj.getId(), 0);
//			}
		} else {
			if (isRequested) {
				reliefPenaltyStateObj.setSpecialRepaymentState(SpecialRepaymentStateEnum.取消.name());
				loanSpecialRepaymentDao.update(reliefPenaltyStateObj);

				/** 取消报盘数据 **/
//				offerCreateService.revokeOfferInfoBySpecialRepay(reliefPenaltyStateObj.getId());

			} else {

			}
		}
		return reliefPenaltyStateObj;
	}

	@Override
	public int findLoanSpecialRepaymentByStateAndLoanId(Long loanId) {
		// TODO Auto-generated method stub
		return loanSpecialRepaymentDao.findLoanSpecialRepaymentByStateAndLoanId(loanId);
	}

	@Override
	public void updateFYJM(LoanSpecialRepayment loanSpecialRepayment) {
		// TODO Auto-generated method stub
		loanSpecialRepaymentDao.updateFYJM(loanSpecialRepayment);

		// 写入日志
		LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
		loanProcessHistory.setLoanId(loanSpecialRepayment.getLoanId());
		loanProcessHistory.setLoanState("正常费用减免");
		loanProcessHistory.setContent("费用减免取消");
		loanProcessHistory.setLoanFlowState("取消");
		loanProcessHistoryServiceImpl.insert(loanProcessHistory);
	}

	@Override
	public Pager findListByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return loanSpecialRepaymentDao.findListByMapLog(map);
	}

	@Override
	public boolean findSpecialRepaymentByUserAndDate(String loanId) {
		List LoanSpecialRepaymentList = loanSpecialRepaymentDao.findSpecialRepaymentByUserAndDate(loanId);
		if (LoanSpecialRepaymentList != null && LoanSpecialRepaymentList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean callOneTimeSettlementApplyInterface(Abs100005Vo abs100005Vo){
		logger.info("请求网关提前清贷申请接参数："+JSONUtil.toJSON(abs100005Vo));
		RequestInfo requestInfoVo = null;
		String rsultStr = "";
		String url = gatewayInterfaceUrl;
		if (Strings.isEmpty(url)) {
			throw new PlatformException(ResponseEnum.FULL_MSG,"远程端的访问地址不能为空");
		}
		try {
			requestInfoVo = GatewayUtils.getSendGatewayRequestVo(abs100005Vo, GatewayFuncIdEnum.一次性提前清贷业务功能号);
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
			throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的一次性结算接口签名异常");
		}catch (Exception e){
			e.printStackTrace();
			throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的一次性结算接口的Vo异常");
		}
		try{
			logger.info("请求网关--提前清贷申请接口url:"+url+"参数："+JSONUtil.toJSON(requestInfoVo));
			rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
			rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
			logger.info("请求网关--提前清贷申请接口url:"+url+"响应："+rsultStr);
		}catch (Exception e){
			e.printStackTrace();
			throw new PlatformException(ResponseEnum.FULL_MSG,"调用网关一次性结算接口异常");
		}
		JSONObject jsonObject = JSON.parseObject(rsultStr);
		String resCode =  (String)jsonObject.get("resCode");
		if (!GatewayConstant.RESPONSE_SUCCESS.equals(resCode)) {
			String respDesc = (String)jsonObject.get("respDesc");
			throw new PlatformException(ResponseEnum.FULL_MSG, respDesc);
		}
		JSONObject grantResut = jsonObject.getJSONObject("infos");
		String status = (String)grantResut.get("status");
		if (!"SUCCESS".equals(status)) {
			String msg = (String)grantResut.get("msg");
			throw new PlatformException(ResponseEnum.FULL_MSG,msg);
		}
		return true;
	}

	@Override
	public Abs100005Vo abs100005VoPackage(VLoanInfo vLoanInfo,AbsOneTimeSettlementAdvice absOneTimeSettlementAdvice){
		Abs100005Vo abs100005Vo = new Abs100005Vo();
		LoanBank loanBank = loanBankServiceImpl.findById(vLoanInfo.getGiveBackBankId());
		abs100005Vo.setSysSource(BaseParamVo.SYS_SOURCE);
		abs100005Vo.setProjNo(BaseParamVo.PROJ_NO);
		abs100005Vo.setPactNo(vLoanInfo.getContractNum());
		abs100005Vo.setAcName(loanBank.getAccount());
		abs100005Vo.setAcNo(loanBank.getAccount());
		abs100005Vo.setOpnCode(loanBank.getBankCode());
		abs100005Vo.setOpnName(loanBank.getFullName());
		abs100005Vo.setSerialNo(absOneTimeSettlementAdvice.getSerialno());
		abs100005Vo.setPayOver(absOneTimeSettlementAdvice.getPayover());
		abs100005Vo.setRepayTotal(absOneTimeSettlementAdvice.getRepaytotal());
		abs100005Vo.setRepayAmt(absOneTimeSettlementAdvice.getRepayamt());
		abs100005Vo.setRepayInte(absOneTimeSettlementAdvice.getRepayinte());
		abs100005Vo.setRepayOver(absOneTimeSettlementAdvice.getRepayover());
		//应收代扣费总额
		abs100005Vo.setFeeRec(new BigDecimal("0.00"));
		//代扣费总额
		abs100005Vo.setFeeTotal(new BigDecimal("0.00"));
		//虚拟账户渠道
		abs100005Vo.setCardChn("CL0002");
		List<PayOverEntity> payOverEntities = new ArrayList<>();
		Integer lastTerm = absOneTimeSettlementAdvice.getLastTerm();
		Integer startTerm = absOneTimeSettlementAdvice.getStartTerm();
		for(int i= startTerm;i<lastTerm;i++) {
			PayOverEntity overEntity = new PayOverEntity();
			overEntity.setCnt(i);
			overEntity.setTxPayOver((i == startTerm)?absOneTimeSettlementAdvice.getPayover():new BigDecimal("0.00"));
			payOverEntities.add(overEntity);
		}
		abs100005Vo.setListPayOver(payOverEntities);
		return abs100005Vo;
	}

	@Override
	public List<BsyhSpecialRepayVo> findBsyhSpecialRepay(Date date) {
		
		return loanSpecialRepaymentDao.findBsyhSpecialRepay(date);
	}

	@Override
	public List<BsyhSpecialRepayVo> findBsyhSpecialRepayAll() {
		
		return loanSpecialRepaymentDao.findBsyhSpecialRepayAll();
	}
}
