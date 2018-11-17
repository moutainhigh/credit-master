package com.zdmoney.credit.fee.offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.exception.OfferBackException;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeTransactionDao;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.fee.domain.LoanFeeOfferQueue;
import com.zdmoney.credit.fee.domain.LoanFeeTransaction;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferQueueService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeTransactionService;
import com.zdmoney.credit.fee.vo.CreateFeeOfferVo;
import com.zdmoney.credit.fee.vo.CreateLoanFeeVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.system.service.pub.ISysConflictPreventionService;
import com.zendaimoney.thirdpp.common.vo.Response;
import com.zendaimoney.thirdpp.trade.pub.service.IBrokerTradeService;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.CollectReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo;

/**
 * 借款收费方法包装
 * 
 * @author Ivan
 *
 */
@Service
public class LoanFeeUtil {

	protected static Log logger = LogFactory.getLog(LoanFeeUtil.class);

	@Autowired
	@Qualifier("loanFeeInfoServiceImpl")
	ILoanFeeInfoService loanFeeInfoServiceImpl;

	@Autowired
	@Qualifier("loanFeeOfferServiceImpl")
	ILoanFeeOfferService loanFeeOfferServiceImpl;

	@Autowired
	@Qualifier("loanFeeTransactionServiceImpl")
	ILoanFeeTransactionService loanFeeTransactionServiceImpl;

	@Autowired
	IBrokerTradeService brokerTradeConsumer;

	@Autowired
	@Qualifier("offerInfoServiceImpl")
	IOfferInfoService offerInfoServiceImpl;

	@Autowired
	IFinanceCoreService financeCoreService;
	
	@Autowired
	ILoanFeeOfferQueueService loanFeeOfferQueueService;
	
	@Autowired
	IVLoanInfoService vLoanInfoService;
	
	@Autowired
	ISysConflictPreventionService sysConflictPreventionServiceImpl;

	/**
	 * 财务放款成功后，调用此方法生成服务费主记录和相关报盘信息
	 * 
	 * @param loanId
	 *            债权编号
	 */
	/**
	 * 财务放款成功后，调用此方法生成服务费主记录和相关报盘信息
	 * 
	 * @param loanId
	 *            债权编号
	 */
	public void createFee(Long loanId, String fundsSources) {
		FinanceVo financeVo = new FinanceVo();
		financeVo.setIds(Strings.convertValue(loanId, String.class));
		User user = UserContext.getUser();
		String userCode = "admin";
		if (user != null) {
			userCode = user.getUserCode();
		}
		financeVo.setUserCode(userCode);
		/** 调用核心放款接口 **/
		Map<String, Object> result = financeCoreService.grantLoan(financeVo);
		if (result.containsKey("code") && result.get("code").equals(Constants.SUCCESS_CODE)) {
			/** 放款接口返回成功 调用生成服务费方法 **/

		} else {
			throw new PlatformException(ResponseEnum.FULL_MSG, "loanId：" + loanId + " grantLoan处理异常 "
					+ result.toString());
		}

		/** 生成服务费主记录 核心放款接口 **/
		CreateLoanFeeVo createLoanFeeVo = new CreateLoanFeeVo();
		createLoanFeeVo.setLoanId(loanId);
		LoanFeeInfo loanFeeInfo = loanFeeInfoServiceImpl.createLoanFee(createLoanFeeVo);

		/** 生成服务费报盘 **/
		Long feeId = loanFeeInfo.getId();
		CreateFeeOfferVo createFeeOfferVo = new CreateFeeOfferVo();
		createFeeOfferVo.setFeeId(feeId);
		createFeeOfferVo.setFundsSources(fundsSources);
		LoanFeeOffer loanFeeOffer = loanFeeOfferServiceImpl.createOffer(createFeeOfferVo);
		
		/** 发送服务费报盘 **/
		List<LoanFeeOffer> loanFeeOfferList = new ArrayList<LoanFeeOffer>();
		/*//判断合同来源
		if(FundsSourcesTypeEnum.龙信小贷.getValue().equals(fundsSources)){
			loanFeeOffer.setBizSysNo(LxFeeConfig.BIZ_SYS_NO);
			loanFeeOffer.setInfoCategoryCode(LxFeeConfig.INFO_CATEGORY_CODE);
		}else if(FundsSourcesTypeEnum.外贸信托.getValue().equals(fundsSources)){
			loanFeeOffer.setInfoCategoryCode(WmxtFeeConfig.INFO_CATEGORY_CODE);
			loanFeeOffer.setBizSysNo(WmxtFeeConfig.BIZ_SYS_NO);
		}else{
			throw new PlatformException(ResponseEnum.FULL_MSG, "loanId：" + loanId + " 合同来源不能为空 ");
		}*/
		loanFeeOfferList.add(loanFeeOffer);
		
		/*try {
			sendOffer(loanFeeOfferList);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
		} catch (Exception ex) {
			logger.error(ex, ex);
		}*/
		sendOffer(loanFeeOfferList);
	}

	/**
	 * 发送服务费报盘
	 * 
	 * @param loanFeeOfferList
	 */
	public void sendOffer(List<LoanFeeOffer> loanFeeOfferList) {
		/** 按照信息类别和业务系统分组发送报盘 **/
		RequestVo requestVo = new RequestVo();
		requestVo.setBizSysNo(loanFeeOfferList.get(0).getBizSysNo());
		requestVo.setInfoCategoryCode(loanFeeOfferList.get(0).getInfoCategoryCode());
		sendOffer(loanFeeOfferList, requestVo);
	}

	/**
	 * 发送服务费报盘
	 * 
	 * @param loanFeeOfferList
	 * @param requestVo
	 */
	public void sendOffer(List<LoanFeeOffer> loanFeeOfferList, RequestVo requestVo) {
		List<LoanFeeTransaction> transactionList = new ArrayList<LoanFeeTransaction>();
		for (int i = 0; i < loanFeeOfferList.size(); i++) {
			LoanFeeOffer loanFeeOffer = loanFeeOfferList.get(i);
			/** 生成回盘流水 开启新事务 **/
			LoanFeeTransaction loanFeeTransaction = loanFeeTransactionServiceImpl.buildTransaction(loanFeeOffer);
			transactionList.add(loanFeeTransaction);
			/** 组装Tpp报文信息 **/
			buildTppData(requestVo, loanFeeOffer, loanFeeTransaction);

			if ((transactionList.size() % 1000 == 0) || ((i + 1) == loanFeeOfferList.size())) {
				/** 发送Tpp报文 **/
				try {
					/** 调用第三方接口发送报表 **/
					Response response = brokerTradeConsumer.asynCollect(requestVo);
					if (TPPHelper.TPP20_RECEIVE_SUCCESS_CODE.equals(response.getCode())) {
						try {
							loanFeeTransactionServiceImpl.updateTransactionToSendding(transactionList);
						} catch (Exception ex) {
							logger.error("跟更新报盘文件状态出错!", ex);
						}
					} else {
						throw new PlatformException(ResponseEnum.FULL_MSG, "报盘发生异常,tpp2.0返回错误信息:" + response.getCode()
								+ "  " + response.getMsg());
					}
				} finally {
					transactionList.clear();
					requestVo.getList().clear();
				}
			}
		}
	}

	/**
	 * 组装Tpp报文信息
	 * 
	 * @param requestVo
	 * @param loanFeeOffer
	 * @return
	 */
	public RequestVo buildTppData(RequestVo requestVo, LoanFeeOffer loanFeeOffer, LoanFeeTransaction loanFeeTransaction) {
		String bankCode = Strings.parseString(loanFeeOffer.getBankCode());
		String tppBankCode = offerInfoServiceImpl.getTppBankCode(bankCode);
		if (Strings.isEmpty(tppBankCode)) {
			throw new PlatformException(ResponseEnum.FULL_MSG, bankCode + "未找到Tpp银行代码");
		}
		CollectReqVo collectReqVo = new CollectReqVo();
		collectReqVo.setPaySysNo(loanFeeOffer.getPaySysNo());
		/** 付款方姓名 **/
		collectReqVo.setPayerName(loanFeeOffer.getName());
		/** 银行卡号 **/
		collectReqVo.setPayerBankCardNo(loanFeeOffer.getBankAcct());
		/** 付款方证件类型 0=身份证 **/
		collectReqVo.setPayerIdType("0");
		/** 证件号码 **/
		collectReqVo.setPayerId(loanFeeOffer.getIdNum());
		/** 银行编码(TPP) **/
		collectReqVo.setPayerBankCode(tppBankCode);
		/** 币种(0人民币) **/
		collectReqVo.setCurrency("0");
		/** 金额 **/
		collectReqVo.setAmount(loanFeeTransaction.getAmount());
		/** 业务流水号 **/
		collectReqVo.setBizFlow(loanFeeTransaction.getSerialNo());
		/** 报盘是否需要拆单(限额超过后)0不需要拆单1需要拆单 **/
		collectReqVo.setIsNeedSpilt(1);
		/** 手机号 **/
		collectReqVo.setPayerMobile(loanFeeOffer.getMobile());

		requestVo.getList().add(collectReqVo);
		return requestVo;
	}
	/**
	 * 第一次服务费划扣失败，再次划扣服务费job
	 */
	public void createFeeSecond(){
		//查出需要进行第二次服务费划扣的数据
		LoanFeeOfferQueue loanFeeOfferQueue = new LoanFeeOfferQueue();
		loanFeeOfferQueue.setState("01");
		List<LoanFeeOfferQueue> list = loanFeeOfferQueueService.findListByVo(loanFeeOfferQueue);
		if(CollectionUtils.isEmpty(list)){
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆没有需要job进行划扣的服务费");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			LoanFeeOfferQueue vo=list.get(i);
			try {
				// 1插入防冲突表，预防并发
				sysConflictPreventionServiceImpl.saveNow("FeeCusume" + vo.getId());
			} catch (Exception e) {
				throw new OfferBackException("服务费消费id:" + vo.getId() + "插入冲突表报错，可能已经消费,不再处理!");
			}
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(vo.getLoanId());
			//判断是否进行了手动划扣服务费
			LoanFeeInfo loanFeeInfo = loanFeeInfoServiceImpl.findLoanFeeInfoByLoanId(vo.getLoanId());
			if("未收取".equals(loanFeeInfo.getState())||"部分收取".equals(loanFeeInfo.getState())){
				LoanFeeOffer loanFeeOfferVo = new LoanFeeOffer();
				loanFeeOfferVo.setLoanId(vo.getLoanId());
				List<LoanFeeOffer> details = loanFeeOfferServiceImpl.findListByVo(loanFeeOfferVo);
				if(details.size()>1){//进行了手动划扣服务费
					logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆该笔债权已经进行了手动服务费划扣loanId："+vo.getLoanId());
					/**更新这笔服务费为已处理状态**/
					vo.setState("02");
					loanFeeOfferQueueService.updateByVo(vo);
					continue;
				}
			}else{
				logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆该笔债权已经进行了手动服务费划扣loanId："+vo.getLoanId());
				/**更新这笔服务费为已处理状态**/
				vo.setState("02");
				loanFeeOfferQueueService.updateByVo(vo);
				continue;
			}
			
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆首次划扣服务费失败，job第二次划扣服务费loanId:"+vo.getLoanId());
			/** 生成服务费报盘 **/
			CreateFeeOfferVo createFeeOfferVo=new CreateFeeOfferVo();
			createFeeOfferVo.setFeeId(vo.getFeeId());
			createFeeOfferVo.setFundsSources(vLoanInfo.getFundsSources());
			LoanFeeOffer loanFeeOffer = loanFeeOfferServiceImpl.createOffer(createFeeOfferVo);
			
			/** 发送报盘 **/
			List<LoanFeeOffer> loanFeeOfferList = new ArrayList<LoanFeeOffer>();
			loanFeeOfferList.add(loanFeeOffer);
			sendOffer(loanFeeOfferList);
			
			/**更新这笔服务费为已处理状态**/
			vo.setState("02");
			loanFeeOfferQueueService.updateByVo(vo);
		}			
	}
}
