package com.zdmoney.credit.core.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.constant.*;
import com.zdmoney.credit.loan.domain.*;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.SpecialRepayParamsVO;
import com.zdmoney.credit.common.vo.core.SpecialRepaymentVO;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.core.service.pub.ISpecialRepaymentCoreService;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.ILoanSpecialRepaymentDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.offer.vo.OfferRepayInfoVo;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class SpecialRepaymentCoreService implements ISpecialRepaymentCoreService {

	protected static Log logger = LogFactory.getLog(SpecialRepaymentCoreService.class);

	@Autowired
	ILoanSpecialRepaymentDao loanSpecialRepaymentDao;// 特殊还款表操作DAO

	@Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;// 还款计划表操作DAO

	@Autowired
	IVLoanInfoDao vLoanInfoDao;// 债权查询视图DAO

	@Autowired
	IAfterLoanService afterLoanService;

	@Autowired
	private ISequencesService sequencesService;

	@Autowired
	ILoanBaseDao loanBaseDao;
	@Autowired
	IComEmployeeDao comEmployeeDao;// 员工信息操作DAO

	@Autowired
	ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;

	@Autowired
	ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;

	@Autowired
	ISpecialRepaymentApplyService specialRepaymentApplyService;

	@Autowired
	private IOfferRepayInfoService offerRepayInfoService;

	@Autowired
	IComOrganizationService comOrganizationService;

	@Autowired
	private ILoanRepaymentDetailService loanRepaymentDetailService;
	/**
	 * 提前扣款更新接口方法
	 */
	public Map<String, Object> updateSpecialRepayment(SpecialRepayParamsVO paramsVo) {
		Date effectiveDate = null;
		LoanSpecialRepayment specialRepayment = null;
		Map<String, Object> json = new HashMap<String, Object>();

		Long loanId = paramsVo.getLoanId();
		LoanSpecialRepayment lSRParams = new LoanSpecialRepayment();
		lSRParams.setLoanId(loanId);
		lSRParams.setSpecialRepaymentState(SpecialRepaymentStateEnum.申请.name());
		lSRParams.setSpecialRepaymentType(SpecialRepaymentTypeEnum.提前扣款.name());
		List<LoanSpecialRepayment> result = loanSpecialRepaymentDao.findListByVo(lSRParams);// 获取"申请"状态的提前扣款记录
		if (result != null && result.size() > 0) {
			specialRepayment = result.get(0);
		} else {
			json = MessageUtil.returnErrorMessage("更新失败:该笔提前扣款没有处于申请中!");
			return json;
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			effectiveDate = sdf.parse(paramsVo.getEffectiveDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String errorMsg = validDateForLateOrearly(loanId, effectiveDate);// 校验更新时间
		if (errorMsg != null) {
			json = MessageUtil.returnErrorMessage(errorMsg);
			return json;
		} else {
			// 更新specialRepayment
			specialRepayment.setRequestDate(effectiveDate);
			specialRepayment.setUpdator(paramsVo.getUserCode());
			specialRepayment.setUpdateTime(new Date());
			loanSpecialRepaymentDao.update(specialRepayment);
			json = MessageUtil.returnHandleSuccessMessage();
			return json;
		}
	}

	/**
	 * 校验指定日期是否早于当前日期或晚于还款日
	 * 
	 * @param loanId
	 *            债权编号ID
	 * @param assginDate
	 *            指定日期
	 * @return
	 */
	public String validDateForLateOrearly(Long loanId, Date assginDate) {
		String message = null;
		boolean isLate = false;
		LoanRepaymentDetail repaymentDetail = null;
		Map<String, Object> params = new HashMap<String, Object>();

		VLoanInfo loanInfo = vLoanInfoDao.get(loanId);
		params.put("loanId", loanId);
		params.put("currentTerm", loanInfo.getCurrentTerm());
		List<LoanRepaymentDetail> result = loanRepaymentDetailDao.findByLoanIdAndRepaymentState(params);

		if (result != null && result.size() > 0) {
			repaymentDetail = result.get(0);
		}
		boolean isBefore = assginDate.before(new Date());
		if (new Date().before(repaymentDetail.getReturnDate())) {
			isLate = assginDate.after(repaymentDetail.getReturnDate());
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DATE, loanInfo.getPromiseReturnDate().intValue());
			isLate = assginDate.after(calendar.getTime());
		}

		if (isBefore || isLate) {
			message = "更新失败:指定日期早于当前时间或晚于还款日!";
		}
		return message;
	}

	@Transactional
	public Map<String, Object> handleSpecialRepayment(SpecialRepayParamsVO paramsVo, ComEmployee employee) {
		Long loanId = paramsVo.getLoanId();
		String repayType = paramsVo.getSpRepayType();
		String repayState = paramsVo.getSpRepayState();
		String evefectiveDate = paramsVo.getEffectiveDate();
		BigDecimal reliefAmount = paramsVo.getReliefAmount();
		boolean isSpRelief = paramsVo.isSpRelief();
		Map<String, Object> jsonResult = new HashMap<String, Object>();

		// 一次性还款、提前扣款
		if (SpecialRepaymentTypeCoreEnum.onetime.name().equals(repayType)
				|| SpecialRepaymentTypeCoreEnum.advanceDeduct.name().equals(repayType)) {
			jsonResult = handleOnetimeAndAdvanceDeduct(loanId, repayType, repayState, evefectiveDate, employee);
		} else if (SpecialRepaymentTypeCoreEnum.reduction.name().equals(repayType)) {// 减免罚息
			jsonResult = handleReduction(loanId, repayState, reliefAmount, employee,isSpRelief);
		}
		return jsonResult;
	}

	/**
	 * 一次性还款和提前结清业务处理
	 * 
	 * @param loanId
	 *            债权编号
	 * @param repayType
	 *            还款类型
	 * @param repayState
	 *            还款状态
	 * @param evefectiveDate
	 *            操作日期
	 * @param employeeId
	 *            操作员对象ID
	 * @return
	 */
	public Map<String, Object> handleOnetimeAndAdvanceDeduct(Long loanId, String repayType, String repayState,
			String evefectiveDate, ComEmployee employee) {
		Map<String, Object> json = new HashMap<String, Object>();

		User user = new User();
		user.setId(employee.getId());
		user.setName(employee.getName());
		user.setUserCode(employee.getUsercode());
		UserContext.setUser(user);

		try {
			if (SpecialRepaymentTypeCoreEnum.onetime.name().equals(repayType)) {
				/** 提前一次性结清 申请状态 on:申请 off:取消 **/
				String allCleanApplyState = "";
				if (SpecialRepaymentStateCoreEnum.application.name().equals(repayState)) {
					allCleanApplyState = "on";
				} else {
					allCleanApplyState = "off";
				}
				/** 提前扣款 申请状态 on:申请 off:取消 **/
				String curCleanApplyState = "";
				if ("on".equals(allCleanApplyState)) {
					curCleanApplyState = "off";
				}

				loanSpecialRepaymentServiceImpl.updateSpecialOneTimeState(loanId, allCleanApplyState,
						curCleanApplyState, null, null, null);
			} else {
				/** 提前一次性结清 申请状态 on:申请 off:取消 **/
				String allCleanApplyState = "";
				/** 提前扣款 申请状态 on:申请 off:取消 **/
				String curCleanApplyState = "";
				if (SpecialRepaymentStateCoreEnum.application.name().equals(repayState)) {
					curCleanApplyState = "on";
				} else {
					curCleanApplyState = "off";
				}
				/** 提前扣款 自动生效时间 **/
				Date requestDate = null;

				if ("on".equals(curCleanApplyState)) {
					requestDate = Assert.notDate(evefectiveDate, "自动生效时间");
					allCleanApplyState = "off";
				}
				loanSpecialRepaymentServiceImpl.updateSpecialOneTimeState(loanId, allCleanApplyState,
						curCleanApplyState, null, null, requestDate);
			}
			json = MessageUtil.returnHandleSuccessMessage();
		} catch (PlatformException ex) {
			throw ex;
			// logger.error(ex,ex);
			// json = MessageUtil.returnErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			// logger.error(ex,ex);
			throw ex;
			// json = MessageUtil.returnErrorMessage(ex.getMessage());
		}
		return json;
	}

	/**
	 * 罚息减免业务处理
	 * 
	 * @param loanId
	 *            债权ID
	 * @param repayState
	 *            还款状态
	 * @param reliefAmount
	 *            减免金额
	 * @param employee
	 *            操作员对象ID
	 * @return
	 */
	public Map<String, Object> handleReduction(Long loanId, String repayState, BigDecimal reliefAmount,
			ComEmployee employee,boolean isSpRelief) {
		Map<String, Object> json = new HashMap<String, Object>();

		if (SpecialRepaymentStateCoreEnum.application.name().equals(repayState)) {
			/** 申请减免操作 **/
			try {
				/** 判断当天是否已申请过减免以及减免金额是否正确 **/
				specialRepaymentApplyService.checkRemitAmountRequestState(loanId, reliefAmount,isSpRelief);
				SpecialRepaymentApplyTeyps applyType = afterLoanService.isOneTimeRepayment(loanId) ? SpecialRepaymentApplyTeyps.结清减免 : SpecialRepaymentApplyTeyps.一般减免;
				//还款等级
				Map levelMap = new HashMap();
				levelMap.put("loanId", loanId);
				String repaymentLevel = loanRepaymentDetailService.findRepaymentLevel(levelMap);
				SpecialRepaymentApply specialRepaymentApply = new SpecialRepaymentApply();
				specialRepaymentApply.setApplyType(applyType.getCode());
				specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.通过.getCode());
				specialRepaymentApply.setLoanId(loanId);
				specialRepaymentApply.setApplyNo("");
				specialRepaymentApply.setApplyAmount(reliefAmount);
				specialRepaymentApply.setFlag("1");
				specialRepaymentApply.setProposerId(employee.getId());
				specialRepaymentApply.setIsSpecial(isSpRelief ? SpecialReliefTypeEnum.特殊减免.getCode() : SpecialReliefTypeEnum.非特殊减免.getCode());
				specialRepaymentApply.setRepayLevel(repaymentLevel);
				specialRepaymentApplyService.insertSpecialRepaymentApply(specialRepaymentApply);
				ComOrganization comOrganization = comOrganizationService.get(employee.getOrgId());
				/** 还款录入Vo **/
				RepaymentInputVo repaymentInputVo = new RepaymentInputVo();
				repaymentInputVo.setLoanId(loanId);
				repaymentInputVo.setAmount(new BigDecimal(0.0));
				repaymentInputVo.setMemo("");
				repaymentInputVo.setOrgan(comOrganization.getOrgCode());
				repaymentInputVo.setTeller(employee.getUsercode());
				repaymentInputVo.setTradeDate(Dates.getCurrDate());
				repaymentInputVo.setTradeType("现金");
				offerRepayInfoService.dealRepaymentInput(repaymentInputVo);
			} catch (PlatformException ex) {
				String errMsg = ex.getMessage();
				ex.printStackTraceExt(logger);
				json = MessageUtil.returnErrorMessage(errMsg);
				return json;
			} catch (Exception ex) {
				String errMsg = "系统忙";
				logger.error(ex.getMessage(), ex);
				json = MessageUtil.returnErrorMessage(errMsg);
				return json;
			}
		} else if (SpecialRepaymentStateCoreEnum.cancel.name().equals(repayState)) {
			throw new PlatformException(ResponseEnum.FULL_MSG,"减免申请通过，不允许取消");
		}
		json = MessageUtil.returnHandleSuccessMessage();
		return json;
	}

	@Override
	public Map<String, Object> querySpecialRepayment(SpecialRepayParamsVO paramsVo) {
		String spRepayType = paramsVo.getSpRepayType();
		List<String> states = new ArrayList<String>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<SpecialRepaymentVO> queryResult = new ArrayList<SpecialRepaymentVO>();
		// 查询参数准备
		paramMap.put("loanId", paramsVo.getLoanId());
		paramMap.put("name", paramsVo.getName());
		paramMap.put("mphone", paramsVo.getMphone());
		paramMap.put("idnum", paramsVo.getIdnum());
		paramMap.put("salesmanId", paramsVo.getSalesmanId());

		if (SpecialRepaymentTypeCoreEnum.onetime.name().equals(spRepayType)) {// 一次性还款或提前扣款
			states.add(LoanStateEnum.正常.name());
			states.add(LoanStateEnum.逾期.name());
			states.add(LoanStateEnum.坏账.name());
		} else if (SpecialRepaymentTypeCoreEnum.reduction.name().equals(spRepayType)) {// 费用减免
			states.add(LoanStateEnum.逾期.name());
		}
		paramMap.put("loanStates", states);
		// 查询操作
		List<VLoanInfo> loanList = vLoanInfoDao.queryLoanForCTS(paramMap);
		queryResult = toSpecialRepaymentVOs(loanList, paramsVo);

		return MessageUtil.returnQuerySuccessMessage(paramsVo.getMax().intValue(), queryResult, queryResult.size(),
				"specialRepaymentVOs");
	}

	private List<SpecialRepaymentVO> toSpecialRepaymentVOs(List<VLoanInfo> loanList, SpecialRepayParamsVO paramsVo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<String> states = new ArrayList<String>();
		List<LoanSpecialRepayment> queryResult = null;
		List<SpecialRepaymentVO> result = new ArrayList<SpecialRepaymentVO>();

		for (VLoanInfo loan : loanList) {
			paramMap.put("loanId", loan.getId());
			SpecialRepaymentVO vo = new SpecialRepaymentVO();

			if (SpecialRepaymentTypeCoreEnum.reduction.name().equals(paramsVo.getSpRepayType())) {// 罚息减免申请状态查询

				states.add(SpecialRepaymentStateEnum.取消.name());
				states.add(SpecialRepaymentStateEnum.申请.name());
				paramMap.put("requestDate", new Date());
				paramMap.put("specialRepaymentType", SpecialRepaymentTypeEnum.减免.name());
				paramMap.put("specialRepaymentStates", states);

				queryResult = loanSpecialRepaymentDao.findListByMap(paramMap);
				if (CollectionUtils.isNotEmpty(queryResult)) {
					LoanSpecialRepayment tempObj = queryResult.get(0);
					vo.setReliefAmount(tempObj.getAmount());
					packageVO(tempObj, vo);
				}
			} else { // 一次性还款和提前扣款状态查询，两种状态只能同时存在一个申请状态
				states.add(SpecialRepaymentStateEnum.申请.name());
				paramMap.put("specialRepaymentStates", states);

				paramMap.put("specialRepaymentType", SpecialRepaymentTypeEnum.一次性还款.name());
				queryResult = loanSpecialRepaymentDao.findListByMap(paramMap);
				if (CollectionUtils.isNotEmpty(queryResult)) {
					packageVO(queryResult.get(0), vo);
				} else {
					states.add(SpecialRepaymentStateEnum.申请.name());
					paramMap.put("specialRepaymentType", states);
					paramMap.put("specialRepaymentType", SpecialRepaymentTypeEnum.提前扣款.name());
					queryResult = loanSpecialRepaymentDao.findListByMap(paramMap);
					if (CollectionUtils.isNotEmpty(queryResult)) {
						packageVO(queryResult.get(0), vo);
					}
				}
			}
			vo.setLoanId(loan.getId());
			result.add(vo);
		}
		return result;
	}

	private void packageVO(LoanSpecialRepayment specialRepayment, SpecialRepaymentVO specialRepaymentVO) {
		specialRepaymentVO.setSpRepayType(Const.SPECIAL_REPAYMENT_TYPE.get(specialRepayment.getSpecialRepaymentType()));
		specialRepaymentVO.setSpRepayState(Const.SPECIAL_REPAYMENT_STATE.get(specialRepayment
				.getSpecialRepaymentState()));
	}

	/**
	 * 罚息减免试算查询接口
	 * 
	 * @throws IOException
	 */
	@Override
	public Map<String, Object> queryReliefPenalty(SpecialRepayParamsVO paramsVo) {
		Long loanId = paramsVo.getLoanId();
		String userCode = paramsVo.getUserCode();
		BigDecimal reliefAmount = paramsVo.getReliefAmount();

		ComEmployee comEmployee = comEmployeeDao.findEmployeeByUserCode(userCode);
		LoanBase loanBase = loanBaseDao.get(loanId);

		if (null == comEmployee) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "userCode不存在，查询不到人员信息！").applyLogLevel(LogLevel.WARN);
		}

		if (null == loanBase) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "查询不到相应债权信息").applyLogLevel(LogLevel.WARN);
		}

		String loanState = loanBase.getLoanState();
		if (LoanStateEnum.预结清.name().equals(loanState) || LoanStateEnum.结清.name().endsWith(loanState)) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "无法试算结清和预结清债权").applyLogLevel(LogLevel.WARN);
		}

		Map<String, Object> map = calculateRemitItem(loanId, reliefAmount);
		return map;
	}

	/**
	 * 计算减免金额各科目金额如（罚息：1元，违约金：0元，逾期利息：X元，逾期本金：X元，正常利息：X元，正常本金：X元）
	 * 
	 * @param loanId
	 *            债权编号
	 * @param remitAmount
	 *            减免金额
	 * @return
	 */
	private Map<String, Object> calculateRemitItem(Long loanId, BigDecimal remitAmount) {
		/** 当前系统时间 **/
		Date currDate = Dates.getCurrDate();
		
		VLoanInfo vLoanInfo = vLoanInfoDao.get(loanId);
		Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + loanId);
		
		/** 获取计算器实例 **/
		ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);

		Map<String, Object> map = new HashMap<String, Object>();
		/** 违约金 **/
		BigDecimal penalty = BigDecimal.ZERO;
		/** 罚息金额 **/
		BigDecimal fine = BigDecimal.ZERO;
		/** 逾期利息 **/
		BigDecimal overInterest = BigDecimal.ZERO;
		/** 逾期本金 **/
		BigDecimal overCorpus = BigDecimal.ZERO;
		/** 正常利息 **/
		BigDecimal normalInterest = BigDecimal.ZERO;
		/** 正常本金 **/
		BigDecimal normalCorpus = BigDecimal.ZERO;
		/** 申请天数 **/
		int days = 0;
		/** normal(正常)或onetime(结清)状态 **/
		String type = "normal";

		/** 查询条件集合 **/
		Map<String, Object> queryParamMap = new HashMap<String, Object>();
		queryParamMap.put("loanId", loanId);
		queryParamMap.put("state1", RepaymentStateEnum.未还款.name());
		queryParamMap.put("state2", RepaymentStateEnum.不足额还款.name());
		queryParamMap.put("state3", RepaymentStateEnum.不足罚息.name());

		/** 获取所有未结清的还款计划列表 **/
		List<LoanRepaymentDetail> loanRepaymentDetailListTmp = loanRepaymentDetailServiceImpl
				.findByLoanIdAndRepaymentState(queryParamMap);
		Assert.notCollectionsEmpty(loanRepaymentDetailListTmp, ResponseEnum.FULL_MSG, "该笔债权已结清（依据来自还款计划表）");

		/** 获取逾期和当期的还款计划列表 **/
		List<LoanRepaymentDetail> loanRepaymentDetailList = afterLoanService.getAllInterestOrLoan(currDate, loanId);

		/** 检查减免金额是否合法 **/
		OfferRepayInfoVo offerRepayInfoVo = checkRemitAmountRequestState(loanId, remitAmount);
		/** 罚息金额 **/
		BigDecimal fineTmp = offerRepayInfoVo.getFine();
		/** 申请状态 onetime:已申请提前结清 normal:未申请提前结清 **/
		type = offerRepayInfoVo.getRequestState();

		/** 获取减免罚息金额 **/
		fine = fineTmp.min(remitAmount);

		/** 返回值填充减免罚息金额 **/
		map.put("fine", fine);
		/** 返回值填充申请天数 **/
		map.put("days", days);
		/** 返回值填充申请状态 **/
		map.put("type", type);
		/** 返回值填充逾期利息 **/
		map.put("overInterest", overInterest);
		/** 返回值填充逾期本金 **/
		map.put("overCorpus", overCorpus);
		/** 返回值填充当期利息 **/
		map.put("normalInterest", normalInterest);
		/** 返回值填充当期本金 **/
		map.put("normalCorpus", normalCorpus);
		/** 返回值填充违约金 **/
		map.put("penalty", penalty);

		/** 减免剩余金额 = 减免剩余金额 - 罚息; 判断是否大于0 做利息 和本金的计算 **/
		remitAmount = remitAmount.subtract(fineTmp);
		if (remitAmount.compareTo(BigDecimal.ZERO) <= 0) {
			/** 减免金额只够罚息 **/
			return map;
		}

		if ("onetime".equals(type)) {
			/** 提前结清 **/
			penalty = calculatorInstace.getPenalty(loanId, loanRepaymentDetailList, vLoanInfo);
		} else {
			/** 非提前结清 **/
			penalty = BigDecimal.ZERO;
		}
		penalty = remitAmount.min(penalty);
		map.put("penalty", penalty);
		/** 减免剩余金额 = 减免剩余金额 - 违约金; 判断是否大于0 做利息 和本金的计算 **/
		remitAmount = remitAmount.subtract(penalty);
		if (remitAmount.compareTo(BigDecimal.ZERO) <= 0) {
			/** 减免金额只够违约金 **/
			return map;
		}

		/** 所有逾期利息 + 当期利息 **/
		BigDecimal allTermInterest = BigDecimal.ZERO;
		/** 所有逾期本金 + 当期本金 **/
		BigDecimal allTermCorpus = BigDecimal.ZERO;

		for (LoanRepaymentDetail loanRepaymentDetail : loanRepaymentDetailList) {
			Date returnDate = loanRepaymentDetail.getReturnDate();
			if ("onetime".equals(type) || returnDate.compareTo(currDate) <= 0) {
				/** 汇总所有逾期利息 + 当期利息 **/
				BigDecimal interest = loanRepaymentDetail.getDeficit().subtract(
						(loanRepaymentDetail.getReturneterm().subtract(loanRepaymentDetail.getCurrentAccrual())));
				if (interest.compareTo(BigDecimal.ZERO) < 0) {
					interest = BigDecimal.ZERO;
				}
				allTermInterest = allTermInterest.add(interest);

				/** 汇总所有逾期本金 + 当期本金 **/
				BigDecimal corpus = loanRepaymentDetail.getDeficit().min(
						(loanRepaymentDetail.getReturneterm().subtract(loanRepaymentDetail.getCurrentAccrual())));
				if (corpus.compareTo(BigDecimal.ZERO) < 0) {
					corpus = BigDecimal.ZERO;
				}
				allTermCorpus = allTermCorpus.add(corpus);
			}
		}

		/** 计算减免利息部分 **/
		overInterest = remitAmount.min(allTermInterest);

		/** 减免剩余金额 = 减免剩余金额 - 减免利息; **/
		remitAmount = remitAmount.subtract(overInterest);
		map.put("overInterest", overInterest);
		if (remitAmount.compareTo(BigDecimal.ZERO) <= 0) {
			/** 减免金额只够利息 **/
			return map;
		}
		/** 剩余减免金额为减免本金 **/
		overCorpus = remitAmount;

		map.put("overCorpus", overCorpus);
		return map;
	}

	/**
	 * 金额减免申请前置 逻辑检验(催收系统 金额减免申请)
	 * 
	 * @param loanId
	 *            债权ID
	 * @param remitAmount
	 *            减免金额
	 */
	public OfferRepayInfoVo checkRemitAmountRequestState(Long loanId, BigDecimal remitAmount) {
		OfferRepayInfoVo offerRepayInfoVo = new OfferRepayInfoVo();
		VLoanInfo vLoanInfo = vLoanInfoDao.get(loanId);
		Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + loanId);
		/** 获取计算器实例 **/
		ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);

		Date currDate = Dates.getCurrDate();

		/** 判断当天是否有减免申请 **/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanId", loanId);
		map.put("currDate", Dates.getCurrDate());
		map.put("states", new String[] { SpecialRepaymentStateEnum.申请.name() });
		map.put("types", new String[] { SpecialRepaymentTypeEnum.正常费用减免.name(), SpecialRepaymentTypeEnum.减免.name() });

		/** 查询罚息减免申请状态 **/
		LoanSpecialRepayment reliefPenaltyStateFlag = loanSpecialRepaymentServiceImpl
				.findByLoanIdAndDateAndTypeAndState(map);
		if (reliefPenaltyStateFlag != null) {
			/** 当天已申请减免 不能重复申请 **/
			throw new PlatformException(ResponseEnum.FULL_MSG, "该笔债权已申请减免，不能重复申请！").applyLogLevel(LogLevel.WARN);
		}

		/** 获取逾期和当期的还款计划列表 **/
		List<LoanRepaymentDetail> loanRepaymentDetailList = afterLoanService.getAllInterestOrLoan(currDate, loanId);

		/** 判断是否申请提前结清 **/
		LoanSpecialRepayment allCleanSpecialState = loanSpecialRepaymentServiceImpl.findbyLoanAndType(loanId,
				SpecialRepaymentTypeEnum.一次性还款.name(), SpecialRepaymentStateEnum.申请.name());

		/** 减免金额上限 **/
		BigDecimal totalAmount = BigDecimal.ZERO;
		/** 获取逾期本息和(不含罚息) **/
		BigDecimal overdueAmount = afterLoanService.getOverdueAmount(loanRepaymentDetailList, currDate);
		/** 获取罚息金额 **/
		BigDecimal fineAmount = afterLoanService.getFine(loanRepaymentDetailList, currDate);
		/** 申请状态 onetime:已申请提前结清 normal:未申请提前结清 **/
		String requestState = "";

		if (allCleanSpecialState == null) {
			/** 未申请提前结清 **/

			/** 减免金额上限 = 罚息 + 逾期本息和 **/
			totalAmount = fineAmount.add(overdueAmount);
			requestState = "normal";
			/** 减免金额不能大于罚息+逾期本息和 **/
			if (remitAmount.compareTo(totalAmount) > 0) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "减免金额不能大于(罚息 + 逾期本息和);罚息：" + fineAmount + "元 逾期本息和："
						+ overdueAmount + "元").applyLogLevel(LogLevel.WARN);
			}
		} else {
			/** 已申请提前结清 **/

			/** 获取一次性结清金额 **/
			BigDecimal oneTimeAmount = calculatorInstace.getOnetimeRepaymentAmount(loanId, currDate,
					loanRepaymentDetailList);
			/** 挂账金额 **/
			BigDecimal accAmount = afterLoanService.getAccAmount(loanId);

			/** 减免金额上限 = 罚息 + 逾期本息和 + 一次性结清金额 - 挂账金额 **/
			totalAmount = fineAmount.add(overdueAmount).add(oneTimeAmount).subtract(accAmount);
			requestState = "onetime";
			/** 减免金额不能大于结清总额 **/
			if (remitAmount.compareTo(totalAmount) > 0) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "减免金额不能大于结清金额(罚息 + 逾期本息和  + 一次性金额 - 挂账);罚息："
						+ fineAmount + "元 逾期本息和：" + overdueAmount + "元 一次性金额：" + oneTimeAmount + "元 挂账：" + accAmount
						+ "元").applyLogLevel(LogLevel.WARN);
			}
		}
		/** 罚息金额 **/
		offerRepayInfoVo.setFine(fineAmount);
		/** 逾期本息和(不含罚息) **/
		offerRepayInfoVo.setOverAmount(overdueAmount);
		/** 申请状态 onetime:已申请提前结清 normal:未申请提前结清 **/
		offerRepayInfoVo.setRequestState(requestState);
		return offerRepayInfoVo;
	}
}
