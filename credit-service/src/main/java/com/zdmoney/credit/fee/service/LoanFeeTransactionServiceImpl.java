package com.zdmoney.credit.fee.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.OfferBackException;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.vo.TppCallBackData20;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeOfferQueueDao;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeTransactionDao;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.fee.domain.LoanFeeOfferQueue;
import com.zdmoney.credit.fee.domain.LoanFeeTransaction;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferQueueService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeRepayInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeTransactionService;
import com.zdmoney.credit.fee.vo.RepayDealVo;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysConflictPreventionService;
import com.zendaimoney.thirdpp.common.enums.ThirdType;

/**
 * 借款收费回盘表 Service实现层，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
@Service
public class LoanFeeTransactionServiceImpl implements ILoanFeeTransactionService {

	protected static Log logger = LogFactory.getLog(LoanFeeTransactionServiceImpl.class);

	@Autowired
	@Qualifier("loanFeeTransactionDaoImpl")
	ILoanFeeTransactionDao loanFeeTransactionDaoImpl;

	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Autowired
	@Qualifier("loanFeeOfferServiceImpl")
	ILoanFeeOfferService loanFeeOfferServiceImpl;

	@Autowired
	@Qualifier("sysConflictPreventionServiceImpl")
	ISysConflictPreventionService sysConflictPreventionServiceImpl;

	@Autowired
	@Qualifier("loanFeeRepayInfoServiceImpl")
	ILoanFeeRepayInfoService loanFeeRepayInfoServiceImpl;
	
	@Autowired
	ILoanFeeOfferQueueDao loanFeeOfferQueueDaompl;

	@Override
	public LoanFeeTransaction findById(Long id) {
		if (Strings.isEmpty(id)) {
			return null;
		}
		return loanFeeTransactionDaoImpl.get(id);
	}

	@Override
	public LoanFeeTransaction findBySerialNo(String serialNo) {
		if (Strings.isEmpty(serialNo)) {
			return null;
		}
		LoanFeeTransaction loanFeeTransaction = new LoanFeeTransaction();
		loanFeeTransaction.setSerialNo(serialNo);
		List<LoanFeeTransaction> list = findListByVo(loanFeeTransaction);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() == 0) {
			return null;
		} else {
			throw new PlatformException("findBySerialNo 返回多结果 serialNo：" + serialNo).applyLogLevel(LogLevel.WARN);
		}
	}

	@Override
	public List<LoanFeeTransaction> findListByVo(LoanFeeTransaction loanFeeTransaction) {
		return loanFeeTransactionDaoImpl.findListByVo(loanFeeTransaction);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LoanFeeTransaction buildTransaction(LoanFeeOffer loanFeeOffer) {
		/** 报盘编号 **/
		Long offerId = loanFeeOffer.getId();
		/** 债权编号 **/
		Long loanId = loanFeeOffer.getLoanId();
		/** 服务费编号 **/
		Long feeId = loanFeeOffer.getFeeId();
		/** 报盘金额 **/
		BigDecimal amount = loanFeeOffer.getAmount();

		LoanFeeTransaction loanFeeTransaction = new LoanFeeTransaction();
		/** 主键PK **/
		loanFeeTransaction.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_TRANSACTION));
		/** 债权编号 **/
		loanFeeTransaction.setLoanId(loanId);
		/** 服务费编号 **/
		loanFeeTransaction.setFeeId(feeId);
		/** 报盘编号 **/
		loanFeeTransaction.setOfferId(offerId);
		/** 报盘业务流水号 **/
		loanFeeTransaction.setSerialNo(buildSerialNo(loanFeeTransaction.getId()));
		/** 应划扣金额 **/
		loanFeeTransaction.setAmount(amount);
		/** 实际划扣金额 **/
		loanFeeTransaction.setFactAmount(BigDecimal.ZERO);
		/** 交易状态 **/
		loanFeeTransaction.setTrxState(OfferTransactionStateEnum.未发送.getValue());
		/** 预留备注信息 **/
		loanFeeTransaction.setMemo("");
		loanFeeTransactionDaoImpl.insert(loanFeeTransaction);
		return loanFeeTransaction;
	}

	/**
	 * 生成报盘业务流水号
	 * 
	 * @return
	 */
	private String buildSerialNo(Long id) {
		String result = "LX_FEE_" + Dates.getDateTime("yyyyMMdd") + (String.format("%08d", id));
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateTransactionToSendding(List<LoanFeeTransaction> transactionList) {
		if (transactionList == null) {
			return;
		}
		for (int i = 0; i < transactionList.size(); i++) {
			LoanFeeTransaction loanFeeTransaction = transactionList.get(i);
			if (Strings.isEmpty(loanFeeTransaction.getId())) {
				continue;
			}
			loanFeeTransaction.setTrxState(OfferTransactionStateEnum.已发送.getValue());
			loanFeeTransaction.setRequestTime(Dates.getNow());
			loanFeeTransactionDaoImpl.update(loanFeeTransaction);
			Long offerId = loanFeeTransaction.getOfferId();
			LoanFeeOffer loanFeeOffer = loanFeeOfferServiceImpl.findById(offerId);
			loanFeeOffer.setState(OfferStateEnum.已报盘.getValue());
			loanFeeOfferServiceImpl.updateOffer(loanFeeOffer);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean callBackTransaction(TppCallBackData20 tppCallBackData20) {
		try {
			/** 报盘流水号 **/
			String serialNo = Strings.parseString(tppCallBackData20.getOrderNo());
			if (Strings.isEmpty(serialNo)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "回盘处理失败：缺少报盘流水号").applyLogLevel(LogLevel.WARN);
			}
			LoanFeeTransaction loanFeeTransaction = findBySerialNo(serialNo);
			Assert.notNull(loanFeeTransaction, ResponseEnum.FULL_MSG, "回盘处理失败：serialNo:" + serialNo + " 未找到报盘记录");
			/** 债权编号 **/
			Long loanId = loanFeeTransaction.getLoanId();
			Assert.notNull(loanId, ResponseEnum.FULL_MSG, "回盘处理失败：serialNo:" + serialNo + " 缺少债权编号数据");
			/** 服务费编号 **/
			Long feeId = loanFeeTransaction.getFeeId();
			Assert.notNull(feeId, ResponseEnum.FULL_MSG, "回盘处理失败：serialNo:" + serialNo + " 缺少服务费编号数据");
			/** 报盘编号 **/
			Long offerId = loanFeeTransaction.getOfferId();
			Assert.notNull(offerId, ResponseEnum.FULL_MSG, "回盘处理失败：serialNo:" + serialNo + " 缺少服务编号数据");
			LoanFeeOffer loanFeeOffer = loanFeeOfferServiceImpl.findById(offerId);
			Assert.notNull(loanFeeOffer, ResponseEnum.FULL_MSG, "回盘处理失败：offerId:" + offerId + " 未找到报盘记录");

			String trxState = Strings.parseString(loanFeeTransaction.getTrxState());
			if (!trxState.equals(OfferTransactionStateEnum.已发送.getValue())) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "回盘处理失败：serialNo:" + serialNo + " 状态不一致或已处理 ")
						.applyLogLevel(LogLevel.WARN);
			}

			try {
				// 1插入防冲突表，预防并发
				sysConflictPreventionServiceImpl.saveNow("FeeBack" + tppCallBackData20.getOrderNo().trim());
			} catch (Exception e) {
				logger.error("插入冲突表报错，可能已经回盘完成,不再处理!", e);
				throw new OfferBackException("报盘流水号:" + tppCallBackData20.getOrderNo() + "插入冲突表报错，可能已经回盘完成,不再处理!");
			}

			/** Tpp划扣金额 **/
			BigDecimal successAmount = BigDecimal.ZERO;

			/**失败原因**/
			String failReason = Strings.parseString(tppCallBackData20.getFailReason());
			/** Tpp返回划扣结果Code **/
			String returnCode = Strings.parseString(tppCallBackData20.getReturnCode());
			/** Tpp返回划扣结果描述 **/
			String returnInfo = Strings.parseString(tppCallBackData20.getReturnInfo());

			switch (returnCode) {
			case TPPHelper.RESULT_SUCCESS_CODE:
				/** 全额回盘 **/

			case TPPHelper.RESULT_BFSUCCESS_CODE:
				/** 部分金额回盘 **/
				successAmount = new BigDecimal(tppCallBackData20.getSuccessAmount());
				loanFeeTransaction.setTrxState(OfferTransactionStateEnum.扣款成功.getValue());

				/** 服务费记账 **/
				RepayDealVo repayDealVo = new RepayDealVo();
				repayDealVo.setFeeId(feeId);
				repayDealVo.setOfferId(offerId);
				/** 获取支付通道名称 **/
				ThirdType thirdType = ThirdType.get(tppCallBackData20.getPaySysNo());
				if (thirdType != null) {
					repayDealVo.setTradeType(thirdType.getDesc());
				} else {
					repayDealVo.setTradeType(tppCallBackData20.getPaySysNo());				
				}
				repayDealVo.setAmount(successAmount);
				loanFeeRepayInfoServiceImpl.repayDeal(repayDealVo);
				break;
			default:
				/** 划扣失败 **/
				loanFeeTransaction.setTrxState(OfferTransactionStateEnum.扣款失败.getValue());
				break;
			}

			/** 更新 TRANSACTION 信息 **/
			/** 实际划扣金额 **/
			loanFeeTransaction.setFactAmount(successAmount);
			/** 交易响应时间 **/
			loanFeeTransaction.setResponseTime(Dates.getNow());
			/** 更新日志Id **/
			loanFeeTransaction.setLogId(Strings.convertValue(tppCallBackData20.getMsgInId(), Long.class));
			/** TPP返回状态码 **/
			loanFeeTransaction.setRtnCode(returnCode);
			/** TPP返回信息 **/
			loanFeeTransaction.setRtnInfo(returnInfo);
			/**TPP返回通道code**/
			loanFeeTransaction.setPaySysNoReal(tppCallBackData20.getPaySysNo());
			/**TPP返回划扣商户号**/
			loanFeeTransaction.setMerId(tppCallBackData20.getMerId());
			/**TPP返回失败原因信息**/
			loanFeeTransaction.setMemo(failReason);
			
			/** 更新 OFFER 信息 **/
			/** 已收取金额 **/
			BigDecimal receiveAmount = loanFeeOffer.getReceiveAmount();
			/** 已收取金额 + 本次回盘金额 **/
			receiveAmount = receiveAmount.add(successAmount);

			loanFeeOffer.setReceiveAmount(receiveAmount);
			/** 报盘未收金额 = 报盘应收金额 - 报盘已收金额 **/
			BigDecimal unpaidAmount = loanFeeOffer.getAmount().subtract(receiveAmount);
			if (unpaidAmount.compareTo(BigDecimal.ZERO) == -1) {
				unpaidAmount = BigDecimal.ZERO;
			}
			loanFeeOffer.setUnpaidAmount(unpaidAmount);
			if (unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
				loanFeeOffer.setState(OfferStateEnum.已回盘全额.getValue());
			} else {
				loanFeeOffer.setState(OfferStateEnum.已回盘非全额.getValue());
			}
			loanFeeOffer.setPaySysNoReal(tppCallBackData20.getPaySysNo());						
			
			/** TRANSACTION 保存入库 **/
			loanFeeTransactionDaoImpl.update(loanFeeTransaction);

			/** OFFER 保存入库 **/
			loanFeeOfferServiceImpl.updateOffer(loanFeeOffer);
			
			/**如果是首次划扣未成功，插入loan_fee_offer_queue收费报盘队列表**/	
			if(!returnCode.equals(TPPHelper.RESULT_SUCCESS_CODE)){//不等于成功
				LoanFeeOffer loanFeeOfferVo = new LoanFeeOffer();
				loanFeeOfferVo.setLoanId(loanId);
				List<LoanFeeOffer> list = loanFeeOfferServiceImpl.findListByVo(loanFeeOfferVo);
				
				if(null!=list && list.size()==1){
					logger.info("☆☆☆☆☆☆☆☆☆☆该笔服务费首次划扣未成功，插入loan_fee_offer_queue表。loanId："+loanId);
					LoanFeeOfferQueue loanFeeOfferQueue=new LoanFeeOfferQueue();
					loanFeeOfferQueue.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_OFFER_QUEUE));
					loanFeeOfferQueue.setLoanId(loanId);
					loanFeeOfferQueue.setFeeId(feeId);
					loanFeeOfferQueue.setState("01");
					loanFeeOfferQueueDaompl.insert(loanFeeOfferQueue);
				}
			}

			return true;
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			return false;
		} catch (Exception ex) {
			return false;
		} finally {

		}
	}

	@Override
	public boolean isOfferSendding(Long feeId) {
		if (Strings.isEmpty(feeId)) {
			return false;
		}
		return loanFeeTransactionDaoImpl.isExistsByFeeIdAndTrxState(feeId, OfferTransactionStateEnum.已发送.getValue());
	}

	@Override
	public Pager searchFeeOfferTransactionList(Map<String, Object> param) {
		return loanFeeTransactionDaoImpl.searchFeeOfferTransactionList(param);
	}
}
