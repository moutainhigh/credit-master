package com.zdmoney.credit.fee.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.LoanFeeStateEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeRepayInfoDao;
import com.zdmoney.credit.fee.domain.LoanFeeAmountChange;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.domain.LoanFeeRepayInfo;
import com.zdmoney.credit.fee.service.pub.ILoanFeeAmountChangeService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeRepayInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeRepayRecordService;
import com.zdmoney.credit.fee.vo.RepayDealVo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款收费记账表 Service实现层，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
@Service
public class LoanFeeRepayInfoServiceImpl implements ILoanFeeRepayInfoService {

	protected static Log logger = LogFactory.getLog(LoanFeeRepayInfoServiceImpl.class);

	@Autowired
	@Qualifier("loanFeeRepayInfoDaoImpl")
	ILoanFeeRepayInfoDao loanFeeRepayInfoDaoImpl;

	@Autowired
	@Qualifier("loanFeeInfoServiceImpl")
	ILoanFeeInfoService loanFeeInfoServiceImpl;

	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Autowired
	@Qualifier("loanFeeAmountChangeServiceImpl")
	ILoanFeeAmountChangeService loanFeeAmountChangeServiceImpl;
	
	@Autowired
	ILoanFeeRepayRecordService loanFeeRepayRecordService;
	
	@Override
	public LoanFeeRepayInfo findById(Long id) {
		if (Strings.isEmpty(id)) {
			return null;
		}
		return loanFeeRepayInfoDaoImpl.get(id);
	}

	@Override
	public List<LoanFeeRepayInfo> findListByVo(LoanFeeRepayInfo loanFeeRepayInfo) {
		return loanFeeRepayInfoDaoImpl.findListByVo(loanFeeRepayInfo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public LoanFeeRepayInfo repayDeal(RepayDealVo repayDealVo) {
		Long feeId = repayDealVo.getFeeId();
		Assert.notNull(feeId, ResponseEnum.FULL_MSG, "服务费记账失败：服务费编号为空");
		/** 记账金额 **/
		BigDecimal amount = repayDealVo.getAmount();
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "服务费记账失败：记账金额为空或小于等于0").applyLogLevel(LogLevel.WARN);
		}
		LoanFeeInfo loanFeeInfo = loanFeeInfoServiceImpl.findById(feeId);
		Assert.notNull(loanFeeInfo, ResponseEnum.FULL_MSG, "服务费记账失败：feeId=" + feeId + "未找到记录");

		Long loanId = loanFeeInfo.getLoanId();
		Assert.notNull(loanId, ResponseEnum.FULL_MSG, "服务费记账失败：债权编号为空");

		/** 服务费收取状态 **/
		String feeState = Strings.parseString(loanFeeInfo.getState());
		if (feeState.equals(LoanFeeStateEnum.已收取.getValue())) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "服务费记账失败：feeId=" + feeId + "服务费已结清")
					.applyLogLevel(LogLevel.WARN);
		}

		LoanFeeRepayInfo loanFeeRepayInfo = new LoanFeeRepayInfo();
		/** 债权编号 **/
		loanFeeRepayInfo.setLoanId(loanId);
		/** 服务费编号 **/
		loanFeeRepayInfo.setFeeId(feeId);
		/** 报盘编号（线下还款入账为空） **/
		loanFeeRepayInfo.setOfferId(repayDealVo.getOfferId());
		/** 记账流水号 **/
		loanFeeRepayInfo.setSerialNo(buildSerialNo(feeId));
		/** 交易时间 **/
		loanFeeRepayInfo.setTradeTime(Dates.getNow());
		/** 交易类型 **/
		loanFeeRepayInfo.setTradeType(repayDealVo.getTradeType());
		/** 记账金额 **/
		loanFeeRepayInfo.setAmount(amount);
		/** 备注 **/
		loanFeeRepayInfo.setMemo(repayDealVo.getMemo());
		/** 主键 **/
		loanFeeRepayInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_REPAY_INFO));
		/** 保存入库 **/
		loanFeeRepayInfoDaoImpl.insert(loanFeeRepayInfo);

		/** 服务费主表逻辑处理 **/
		/** 未收服务费金额 **/
		BigDecimal unpaidAmount = loanFeeInfo.getUnpaidAmount();
		if (amount.compareTo(unpaidAmount) > 0) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "服务费记账失败：FeeId：" + feeId + "，服务费金额过大,欠款：" + unpaidAmount
					+ "元").applyLogLevel(LogLevel.WARN);
		}
		unpaidAmount = unpaidAmount.subtract(amount);
		if (unpaidAmount.compareTo(BigDecimal.ZERO) < 0) {
			unpaidAmount = BigDecimal.ZERO;
		}
		/** 剩余服务费金额 **/
		loanFeeInfo.setUnpaidAmount(unpaidAmount);
		/** 已收取服务费金额 **/
		loanFeeInfo.setReceiveAmount(loanFeeInfo.getAmount().subtract(unpaidAmount));
		if (unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
			/** 服务费已结清 **/
			loanFeeInfo.setState(LoanFeeStateEnum.已收取.getValue());
		} else {
			/** 服务费未结清 **/
			loanFeeInfo.setState(LoanFeeStateEnum.部分收取.getValue());
		}
		/** 更新服务费主表 **/
		loanFeeInfoServiceImpl.updateFeeInfo(loanFeeInfo);

		/** ====================服务费记账之后 保存当前实收和未收金额数据===================== **/
		LoanFeeAmountChange loanFeeAmountChange = new LoanFeeAmountChange();
		/** 记账ID **/
		loanFeeAmountChange.setRepayId(loanFeeRepayInfo.getId());
		/** 已收金额 **/
		loanFeeAmountChange.setReceiveAmount(loanFeeInfo.getReceiveAmount());
		/** 未收金额 **/
		loanFeeAmountChange.setUnpaidAmount(loanFeeInfo.getUnpaidAmount());
		loanFeeAmountChangeServiceImpl.insert(loanFeeAmountChange);
		/** ====================服务费记账之后 保存当前实收和未收金额数据===================== **/
	
		/** ====================服务费分账后期实现===================== **/
		loanFeeRepayRecordService.saveLoanFeeSplit(loanFeeRepayInfo);
		/** ====================服务费分账后期实现===================== **/

		return loanFeeRepayInfo;
	}

	/** 生成记账流水号 **/
	public String buildSerialNo(Long feeId) {
		String result = "TX" + Dates.getDateTime("yyyyMMddHHmmssSSS") + (String.format("%08d", feeId));
		return result;
	}

}
