package com.zdmoney.credit.offer.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.BankEnum;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.CurrencyTypeEnum;
import com.zdmoney.credit.common.constant.DebitNotifyStateEnum;
import com.zdmoney.credit.common.constant.DebitOperateTypeEnum;
import com.zdmoney.credit.common.constant.DebitRepayTypeEnum;
import com.zdmoney.credit.common.constant.DebitResultStateEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.OfferRangeEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.common.constant.PayPartyEnum;
import com.zdmoney.credit.common.constant.RepayTypeEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.TPPInfoCategoryCodeEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.common.vo.core.OfferVO;
import com.zdmoney.credit.common.vo.core.RepayTrailParamsVO;
import com.zdmoney.credit.common.vo.core.TrailVO;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitDetailInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitTransactionDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitDetailInfo;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductMoneyListEntity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2311Vo;
import com.zdmoney.credit.framework.vo.wm3.output.WM3_2311OutputVo;
import com.zdmoney.credit.loan.dao.pub.ILoanOverdueHistoryDao;
import com.zdmoney.credit.loan.dao.pub.ILoanSpecialRepaymentDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanOverdueHistory;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferBankDicDao;
import com.zdmoney.credit.offer.dao.pub.IOfferInfoDao;
import com.zdmoney.credit.offer.dao.pub.IOfferTransactionDao;
import com.zdmoney.credit.offer.domain.OfferBankDic;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.service.pub.IOfferConfigService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;
import com.zdmoney.credit.person.service.pub.IPersonThirdPartyAccountService;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zendaimoney.thirdpp.common.enums.BizSys;
import com.zendaimoney.thirdpp.common.enums.ThirdType;
import com.zendaimoney.thirdpp.common.vo.Response;
import com.zendaimoney.thirdpp.rmi.service.request.ThirdPaymentService;
import com.zendaimoney.thirdpp.trade.pub.service.IBrokerTradeService;
import com.zendaimoney.thirdpp.vo.RequestVo;
import com.zendaimoney.thirdpp.vo.enums.IsAsyn;
import com.zendaimoney.thirdpp.vo.enums.IsReSend;
import com.zendaimoney.thirdpp.vo.enums.RequestType;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;
import com.zendaimoney.thirdpp.vo.enums.ZendaiSys;

/**
 * 报盘信息Service
 * 
 * @author 00232949
 *
 */
@Service
@Transactional
public class OfferInfoServiceImpl implements IOfferInfoService {

	private static final Logger logger = Logger.getLogger(OfferInfoServiceImpl.class);

	/**外贸3报盘批次数量**/
	private final static int BATCH_SIZE = 2000;
	
	@Autowired
	IOfferInfoDao offerInfoDao;
	@Autowired
	ILoanSpecialRepaymentService loanSpecialRepaymentService;
	@Autowired
	IAfterLoanService afterLoanService;
	@Autowired
	IPersonInfoService personInfoService;
	@Autowired
	ILoanBankService loanBankService;
	@Autowired
	IOfferBankDicDao offerBankDicDao;
	@Autowired
	ILoanLogService loanLogService;
	@Autowired
	IOfferTransactionService offerTransactionService;
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired
	ILoanRepaymentDetailService loanRepaymentDetailService;
	@Autowired
	IComOrganizationDao comOrganizationDao;
	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Autowired(required = false)
	ThirdPaymentService thirdPaymentService;

	@Autowired
	IComEmployeeDao comEmployeeDao;

	@Autowired
	IVLoanInfoDao vLoanInfoDao;

	@Autowired
	ILoanSpecialRepaymentDao loanSpecialRepaymentDao;

	@Autowired
	IOfferTransactionDao offerTransactionDao;
	@Autowired
	private IDebitBaseInfoDao debitBaseInfoDaoImpl;
	@Autowired
	private IDebitDetailInfoDao debitDetailInfoDaoImpl;
	@Autowired
	private IDebitTransactionDao debitTransactionDaoImpl;
	@Autowired
	private IDebitTransactionService debitTransactionServiceImpl;
	@Autowired
	private ISendMailService sendMailService;
	
	

	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanOverdueHistoryDao loanOverdueHistoryDao;

	@Autowired
	private IBrokerTradeService brokerTradeConsumer;
	@Autowired
	private IPersonThirdPartyAccountService personThirdPartyAccountService;

	@Autowired
    private IOfferConfigService offerConfigService;

	/** 用于存放tpp的银行配置信息 */
	private Map<String, OfferBankDic> bankRelateTppMap = null;
	
	@Autowired
	private ILoanFeeInfoService loanFeeInfoService;
	
    @Autowired
    private ISplitQueueLogService splitQueueLogService;
	
    @Autowired
    private IDebitQueueLogService debitQueueLogService;
	/**
	 * 创建报盘信息<br>
	 * 生成时不区分tpp通道，2个通道的数据都准备，在发送时区分
	 * 
	 * @param loan
	 * @param salesDepartment
	 * @param type
	 *            类型用于控制优先级 ；1：正常扣款，2：逾期扣款，3：提前扣款，4：提前结清 ，5：实时划扣
	 * @param offerAmount
	 *            报盘金额 ：如果是null则全额报盘
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public OfferInfo createOffer(VLoanInfo loan, ComOrganization salesDepartment, int type, BigDecimal offerAmount,String paySysNo) {
		if (this.bankRelateTppMap == null) {// 获得银行tpp对照信息
			initBankRelateTppMap();
		}
		// 针对龙信小贷、外贸信托、外贸2，判断是否已经完成划扣服务费，如果没有，则停止生成报盘信息
		if(!loanFeeInfoService.isAlreadyDebitServiceCharge(loan.getId())){
			return null;
		}
		
		// 计算还款金额
		BigDecimal amount = calculateOfferAmount(loan);
		if (offerAmount != null) {
			if (offerAmount.compareTo(new BigDecimal("0.01")) < 0 || offerAmount.compareTo(amount) > 0) {
				throw new RuntimeException("指定划扣金额小于0.01或者大于应还总额，创建报盘失败");
			}
			amount = offerAmount;
		}
		// 如果金额>0
		if (amount.compareTo(new BigDecimal(0)) > 0) {
			// 得到借款人
			PersonInfo personInfo = personInfoService.findById(loan.getBorrowerId());
			// 得到还款银行卡信息
			LoanBank giveBackBank = loanBankService.findById(loan.getGiveBackBankId());

			OfferInfo offerInstance = new OfferInfo();
			offerInstance.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_INFO));
			offerInstance.setBankCode(giveBackBank.getBankCode());
			offerInstance.setAcctType("00");// 银行卡
			offerInstance.setBankName(giveBackBank.getBankName());
			offerInstance.setBankAcct(giveBackBank.getAccount().trim());
			offerInstance.setName(personInfo.getName());
			offerInstance.setIdnum(personInfo.getIdnum());
			offerInstance.setAmount(amount);
			offerInstance.setOfferAmount(amount);
			offerInstance.setActualAmount(new BigDecimal(0));
			offerInstance.setCurrencyType(CurrencyTypeEnum.CNY.getValue());
			offerInstance.setLoanId(loan.getId());
			offerInstance.setState(OfferStateEnum.未报盘.getValue());
			offerInstance.setOfferDate(Dates.getCurrDate());
			offerInstance.setTel(personInfo.getMphone());
			// offerInstance.setTeller();柜员号原系统随机取了一个营业部的人员工号，新系统不放值
			offerInstance.setOrgan(salesDepartment.getOrgCode());
			offerInstance.setPriority(getPriorityByType(type));
			offerInstance.setCreator("SYSTEM");
			offerInstance.setCustId(String.valueOf(personInfo.getId()));
			if (type == IOfferInfoService.SHISHIHUAKOU) {
				offerInstance.setType(OfferTypeEnum.实时划扣.getValue());
			} else {
				offerInstance.setType(OfferTypeEnum.自动划扣.getValue());
			}
			offerInstance.setCreateTime(new Date());
			// 债权去向
			String loanBelong = loan.getLoanBelong();
			if(StringUtils.isNotEmpty(paySysNo)){
				//实时划扣	
				offerInstance.setPaySysNo(paySysNo);
			}else{
				// 修改支付通道按照债权去向来划分设置
				if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loanBelong)) {
					// 合同来源为华澳信托的建设银行客户切换通道为银联，代码105：中国建设银行
					// JIRA：CT-3286
					if (BankEnum.CCB.getCode().equals(offerInstance.getBankCode())) {
						offerInstance.setTppType(ThirdPartyType.SHUnionpay.name());// 1.0属性
						offerInstance.setPaySysNo(ThirdType.SHUNIONPAY.getCode());// 2.0属性
					} else {
						offerInstance.setTppType(ThirdPartyType.YYoupay.name());// 1.0属性
						offerInstance.setPaySysNo(ThirdType.YONGYOUUNIONPAY.getCode());// 2.0属性
					}
				} else if (FundsSourcesTypeEnum.挖财2.toString().equals(loanBelong) 
						|| FundsSourcesTypeEnum.渤海信托.toString().equals(loanBelong)
						|| FundsSourcesTypeEnum.龙信小贷.toString().equals(loanBelong)
						|| FundsSourcesTypeEnum.外贸2.toString().equals(loanBelong)) {
					offerInstance.setTppType(ThirdPartyType.Allinpay.name());// 1.0属性
					offerInstance.setPaySysNo(ThirdType.ALLINPAY.getCode());// 2.0属性
				} else if (FundsSourcesTypeEnum.渤海2.toString().equals(loanBelong)) {
					// 建设银行、中国农业银行、中国民生银行、上海浦东发展银行、中国银行走通联支付通道，其余情况走上海银联支付通道
					if (BankEnum.ABC.getCode().equals(offerInstance.getBankCode())
						||BankEnum.CCB.getCode().equals(offerInstance.getBankCode())
						||BankEnum.SPDB.getCode().equals(offerInstance.getBankCode())
						||BankEnum.CMBC.getCode().equals(offerInstance.getBankCode())
						||BankEnum.BOC.getCode().equals(offerInstance.getBankCode())) {//渤海2的通联商户
				            offerInstance.setTppType(ThirdPartyType.Allinpay.name());// 1.0属性
				            offerInstance.setPaySysNo(ThirdType.ALLINPAY.getCode());// 2.0属性
					}else{//p2p银联商户
						offerInstance.setTppType(ThirdPartyType.SHUnionpay.name());// 1.0属性
			            offerInstance.setPaySysNo(ThirdType.SHUNIONPAY.getCode());// 2.0属性
					}
				}/* else if (FundsSourcesTypeEnum.捞财宝.toString().equals(loanBelong)) {
					offerInstance.setTppType("");// 1.0属性
					offerInstance.setPaySysNo(ThirdType.ZENDAIPAY.getCode());// 2.0属性
				}*/ else {
					offerInstance.setTppType(bankRelateTppMap.get(offerInstance.getBankCode()).getTppType());// 1.0属性
					offerInstance.setPaySysNo(bankRelateTppMap.get(offerInstance.getBankCode()).getPaySysNo());// 2.0属性
				}
			
			// 债权去向为证大P2P、海门小贷的中国银行（银行代码是：104）切换至银联。
			// 债权去向
			/*String loanBelong = loan.getLoanBelong();
			if(FundsSourcesTypeEnum.证大P2P.getValue().equals(loanBelong) || FundsSourcesTypeEnum.海门小贷.getValue().equals(loanBelong)){
				if ("104".equals(offerInstance.getBankCode())) {
					offerInstance.setTppType(ThirdPartyType.SHUnionpay.name());// 1.0属性
					offerInstance.setPaySysNo(ThirdType.SHUNIONPAY.getCode());// 2.0属性
				}
			}*/
			//	债权去向为挖财2 龙信小贷 海门小贷 证大P2P的中国银行（银行代码是：104）切换至快捷通。
				if(FundsSourcesTypeEnum.证大P2P.getValue().equals(loanBelong) || FundsSourcesTypeEnum.海门小贷.getValue().equals(loanBelong)
						|| FundsSourcesTypeEnum.龙信小贷.getValue().equals(loanBelong)|| FundsSourcesTypeEnum.挖财2.getValue().equals(loanBelong)
						|| FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
					if (BankEnum.BOC.getCode().equals(offerInstance.getBankCode())) {
						offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
					}
				}
				/** 债权去向为渤海信托、渤海2、华澳信托、国民信托的还款可以支持选择快捷通进行划扣。
					自动报盘时中国银行和工商银行选择快捷通，其余银行和原有逻辑保持一致。**/
				this.configurePayType(offerInstance, loanBelong); 
				/**债权去向为证大P2P、龙信小贷、海门小贷、包商银行的 中国建设银行（银行代码是：105）、华夏银行（银行代码是：304）、中国邮政储蓄银行（银行代码是：403）切换至快捷通。**/
				if(FundsSourcesTypeEnum.证大P2P.getValue().equals(loanBelong) || FundsSourcesTypeEnum.海门小贷.getValue().equals(loanBelong)
						|| FundsSourcesTypeEnum.龙信小贷.getValue().equals(loanBelong)|| FundsSourcesTypeEnum.包商银行.getValue().equals(loanBelong)
						|| FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
					if (BankEnum.CCB.getCode().equals(offerInstance.getBankCode())
					        || BankEnum.HXB.getCode().equals(offerInstance.getBankCode())
					        || BankEnum.PSBC.getCode().equals(offerInstance.getBankCode())) {
						offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
					}
					//2017-03-07 start   将债权去向为P2P、海门小贷、龙信小贷和包商银行的建设银行切换至通联；
					if(BankEnum.CCB.getCode().equals(offerInstance.getBankCode())){
						offerInstance.setPaySysNo(TppPaySysNoEnum.通联代扣.getCode());// 2.0属性
					}
					//2017-03-07 end
				}
				
				if (FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelong)) {
					// 工商银行、农业银行、中国银行、建设银行、招商银行、交通银行、邮政储蓄银行、中信银行、民生银行、兴业银行、平安银行、广发银行、中国光大银行
					// 走快捷通其他银行走通联
					if (BankEnum.ICBC.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.ABC.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.BOC.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.CCB.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.CMB.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.BOCO.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.PSBC.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.CITIC.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.CMBC.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.CIB.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.PAB.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.CGB.getCode().equals(offerInstance.getBankCode())
							|| BankEnum.CEB.getCode().equals(offerInstance.getBankCode())) {
						offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
					} else {
						// 选择通联；
						offerInstance.setTppType(ThirdPartyType.Allinpay.name());// 1.0属性
						offerInstance.setPaySysNo(ThirdType.ALLINPAY.getCode());// 2.0属性
					}
				}
				
				if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loan.getFundsSources())
						&& FundsSourcesTypeEnum.证大P2P.getValue().equals(loanBelong)){
					offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
				}
				
				if (FundsSourcesTypeEnum.国民信托.getValue().equals(loanBelong)) {
					offerInstance.setTppSysNum(ZendaiSys.ZENDAI_2019_SYS.name());// 1.0属性
				} else if (FundsSourcesTypeEnum.挖财2.toString().equals(loanBelong)) {
					offerInstance.setTppSysNum(ZendaiSys.ZENDAI_2005_SYS.name());// 1.0属性
				} else {
					offerInstance.setTppSysNum(ZendaiSys.ZENDAI_2004_SYS.name());// 1.0属性
				}
				
				//2017-11-01 xgl 
				if(FundsSourcesTypeEnum.外贸信托.getValue().equals(loanBelong)){
					offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
				}
			}	
			if(ThirdType.YONGYOUUNIONPAY.getCode().equals(offerInstance.getPaySysNo())){
				PersonThirdPartyAccount personThirdPartyAccount = personThirdPartyAccountService.findByIdnum(personInfo.getIdnum());
				if(personThirdPartyAccount != null){
					offerInstance.setCustId(personThirdPartyAccount.getCreditUserTpp()); 
				}else{
					throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,"用友划扣通道：缺少第三方签约用户编号,债权id为:"+loan.getId());
				}
			}
			offerInstance.setBizSysNo(BizSys.ZENDAI_2004_SYS.getCode());// 2.0属性
			// 设置信息类别编码，tpp2.0独有,生成时不区分通道，2个通道的数据都准备，在发送时区分
			setTppInfoCategoryCode(loan, offerInstance, type);
			// 设置报盘的范围，是正常还是M7+-，控制报盘次数
			// setOfferRange(offerInstance,loan.getYuqitianshu());
			// 设置划扣类型
			String debitType = loan.getDebitType();
			if (Strings.isEmpty(debitType)) {
				debitType = offerInfoDao.queryDebitTypeByLoanId(loan.getId());
			}
			offerInstance.setOfferRange(debitType);
			// 记录债权去向
			offerInstance.setFundsSources(loanBelong);
			offerInfoDao.insert(offerInstance);
			return offerInstance;
		}

		return null;
	}

	/**
	 * 设置报盘的范围，是正常还是M7+-，控制报盘次数
	 * 
	 * @param offerInstance
	 * @param yuqitianshu
	 */
	private void setOfferRange(OfferInfo offerInstance, String yuqitianshu) {
		if (Strings.isEmpty(yuqitianshu)) {
			logger.debug("逾期天数为空，可能属于非逾期的报盘创建");
			return;
		}
		Integer i = null;
		try {
			i = Integer.parseInt(yuqitianshu);
		} catch (Exception e) {
			logger.error("setOfferRange 设置报盘范围时，转换逾期天数异常！" + e.getMessage());
			return;
		}
		if (i < 31) {
			offerInstance.setOfferRange(OfferRangeEnum.M1.getValue());
		} else if (i < 181) {
			offerInstance.setOfferRange(OfferRangeEnum.M7以下.getValue());
		} else {
			offerInstance.setOfferRange(OfferRangeEnum.M7以上.getValue());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public OfferInfo createRealtimeOffer(VLoanInfo loan, ComOrganization salesDepartment, int type,
			BigDecimal offerAmount) {
		return createOffer(loan, salesDepartment, type, offerAmount,null);
	}

	/**
	 * 设置信息类别编码，tpp2.0独有<br>
	 * 
	 * @param loan
	 * @param offerInstance
	 * @param type
	 */
	private void setTppInfoCategoryCode(VLoanInfo loan, OfferInfo offerInstance, int type) {
		// 修改信息类别按债权去向来划分设置
		String loanBelong = loan.getLoanBelong();
		switch (type) {
		case IOfferInfoService.ZHENGCHANG:
			if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷华澳信托正常扣款.getCode());
			} else if (FundsSourcesTypeEnum.国民信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷国民信托正常扣款.getCode());
			} else if (FundsSourcesTypeEnum.挖财2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷挖财正常扣款.getCode());
			} else if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷渤海信托正常扣款.getCode());
			} else if (FundsSourcesTypeEnum.龙信小贷.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷龙信小贷正常扣款.getCode());
			} else if (FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷渤海信托2正常扣款.getCode());
			} else if (FundsSourcesTypeEnum.外贸2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷外贸2正常扣款.getCode());
			} else if(FundsSourcesTypeEnum.捞财宝.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷捞财宝正常扣款.getCode());
			} else if(FundsSourcesTypeEnum.包商银行.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷包商银行正常扣款.getCode());
			} else if(FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷华瑞渤海正常扣款.getCode());
			} else if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷陆金所正常扣款.getCode());
			} else if(FundsSourcesTypeEnum.外贸信托.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷外贸信托正常扣款.getCode());
			} else {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷个贷正常扣款.getCode());
			}
			return;
		case IOfferInfoService.SHISHIHUAKOU:
			if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷华澳信托实时划扣.getCode());
			} else if (FundsSourcesTypeEnum.国民信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷国民信托实时划扣.getCode());
			} else if (FundsSourcesTypeEnum.挖财2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷挖财实时划扣.getCode());
			} else if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷渤海信托实时划扣.getCode());
			} else if (FundsSourcesTypeEnum.龙信小贷.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷龙信小贷实时划扣.getCode());
			} else if (FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷渤海信托2实时划扣.getCode());
			} else if (FundsSourcesTypeEnum.外贸2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷外贸2实时划扣.getCode());
			} else if(FundsSourcesTypeEnum.捞财宝.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷捞财宝实时划扣.getCode());
			} else if(FundsSourcesTypeEnum.包商银行.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷包商银行实时划扣.getCode());
			} else if (FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷华瑞渤海实时划扣.getCode());
			} else if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷陆金所实时划扣.getCode());
			}else if(FundsSourcesTypeEnum.外贸信托.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷外贸信托实时划扣.getCode());
			}  else {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷个贷实时划扣.getCode());
			}
			return;
		case IOfferInfoService.YUQI:
			boolean isM1 = true;// 是否M1
			LoanOverdueHistory loanOverdueHistory = loanOverdueHistoryDao.findByLoanId(loan.getId());
			if (loanOverdueHistory != null && loanOverdueHistory.getOverdueDay() > 30) {
				isM1 = false;
			}
			if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷华澳信托逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷华澳信托逾期M2以上划扣.getCode());
			} else if (FundsSourcesTypeEnum.国民信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷国民信托逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷国民信托逾期M2以上划扣.getCode());
			} else if (FundsSourcesTypeEnum.挖财2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷挖财逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷挖财逾期M2以上划扣.getCode());
			} else if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷渤海信托逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷渤海信托逾期M2以上划扣.getCode());
			} else if (FundsSourcesTypeEnum.龙信小贷.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷龙信小贷逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷龙信小贷逾期M2以上划扣.getCode());
			} else if (FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷渤海信托2逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷渤海信托2逾期M2以上划扣.getCode());
			} else if (FundsSourcesTypeEnum.外贸2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷外贸2逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷外贸2逾期M2以上划扣.getCode());
			} else if(FundsSourcesTypeEnum.捞财宝.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(isM1?  TPPInfoCategoryCodeEnum.信贷捞财宝逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷捞财宝逾期M2以上划扣.getCode());
			} else if(FundsSourcesTypeEnum.包商银行.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(isM1?  TPPInfoCategoryCodeEnum.信贷包商银行逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷包商银行逾期M2以上划扣.getCode());
			} else if (FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷华瑞渤海逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷华瑞渤海逾期M2以上划扣.getCode());
			} else if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(isM1?  TPPInfoCategoryCodeEnum.信贷陆金所逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷陆金所逾期M2以上划扣.getCode());
			} else if(FundsSourcesTypeEnum.外贸信托.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(isM1?  TPPInfoCategoryCodeEnum.信贷外贸信托逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷外贸信托逾期M2以上划扣.getCode());
			} else {
				offerInstance.setInfoCategoryCode(isM1 ? TPPInfoCategoryCodeEnum.信贷个贷逾期M1划扣.getCode()
						: TPPInfoCategoryCodeEnum.信贷个贷逾期M2以上划扣.getCode());
			}
			return;
		case IOfferInfoService.TIQIANKOUKUAN:
		case IOfferInfoService.TIQIANJIEQING:
			if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷华澳信托提前扣款.getCode());
			} else if (FundsSourcesTypeEnum.国民信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷国民信托提前扣款.getCode());
			} else if (FundsSourcesTypeEnum.挖财2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷挖财提前扣款.getCode());
			} else if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷渤海信托提前扣款.getCode());
			} else if (FundsSourcesTypeEnum.龙信小贷.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷龙信小贷提前扣款.getCode());
			} else if (FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷渤海信托2提前扣款.getCode());
			} else if (FundsSourcesTypeEnum.外贸2.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷外贸2提前扣款.getCode());
			} else if(FundsSourcesTypeEnum.捞财宝.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷捞财宝提前扣款.getCode());
			} else if(FundsSourcesTypeEnum.包商银行.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷包商银行提前扣款.getCode());
			} else if (FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelong)) {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷华瑞渤海提前扣款.getCode());
			} else if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷陆金所提前扣款.getCode());
			} else if(FundsSourcesTypeEnum.外贸信托.getValue().equals(loanBelong)){
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷外贸信托提前扣款.getCode());
			} else {
				offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷个贷提前扣款.getCode());
			}
			return;
		default:
			logger.warn("类型无法解析，返回默认:信贷个贷逾期M1划扣");
			offerInstance.setInfoCategoryCode(TPPInfoCategoryCodeEnum.信贷个贷逾期M1划扣.getCode());
			return;
		}
	}

	/**
	 * 根据类型返回优先级5~8
	 * <p>
	 * 
	 * 正常扣款，优先级5<br>
	 * 逾期扣款，优先级8<br>
	 * 提前扣款，优先级6<br>
	 * 提前还清，优先级6<br>
	 * 其余情况，如type无法解析，优先级7<br>
	 * 
	 * @param type
	 * @return
	 */
	private Integer getPriorityByType(int type) {
		switch (type) {
		case IOfferInfoService.ZHENGCHANG:
			return 5;
		case IOfferInfoService.SHISHIHUAKOU:
			return 5;
		case IOfferInfoService.YUQI:
			return 8;
		case IOfferInfoService.TIQIANKOUKUAN:
			return 6;
		case IOfferInfoService.TIQIANJIEQING:
			return 6;
		default:
			logger.warn("类型无法解析，返回默认报盘优先级：7");
			return 7;
		}
	}

	/**
	 * 计算报盘金额
	 * 
	 * @param loan
	 * @return
	 */
	private BigDecimal calculateOfferAmount(VLoanInfo loan) {
		// 得到特殊还款记录表信息
		LoanSpecialRepayment specialRepayment = loanSpecialRepaymentService.findbyLoanAndType(loan.getId(),
				SpecialRepaymentTypeEnum.提前扣款.getValue(), SpecialRepaymentStateEnum.申请.getValue());

		// 计算还款金额
		BigDecimal amount = new BigDecimal(0);
		Date currDate = Dates.getCurrDate();
		if (specialRepayment != null) {// 特殊还款申请
			// 当期的还款日<=今天||是一次性还清状态   T日
			if (afterLoanService.getCurrTermReturnDate(currDate, loan.getPromiseReturnDate()).compareTo(currDate) <= 0
					|| afterLoanService.isOneTimeRepayment(loan.getId())) {
				amount = afterLoanService.getAmount(currDate, loan.getId());
			} else { //非T日  提前扣款
				List<LoanRepaymentDetail> repayList = afterLoanService.getAllInterestOrLoan(currDate, loan.getId());
				if(FundsSourcesTypeEnum.包商银行.getValue().equals(loan.getLoanBelong())){
					amount = afterLoanService.getOverdueAmount(repayList, currDate)
							.add(afterLoanService.getFine(repayList, currDate))
							.subtract(afterLoanService.getAccAmount(loan.getId()))
							.subtract(afterLoanService.getReliefOfFine(currDate, loan.getId()));
				}else{
					//逾期总额+逾期罚息+当期总额-账户中余额-减免罚息金额
					amount = afterLoanService.getOverdueAmount(repayList, currDate)
							.add(afterLoanService.getFine(repayList, currDate))
							.add(afterLoanService.getCurrAmount(repayList, currDate))
							.subtract(afterLoanService.getAccAmount(loan.getId()))
							.subtract(afterLoanService.getReliefOfFine(currDate, loan.getId()));
				}
			}
		} else {// 无特殊，正常的还款计算
			amount = afterLoanService.getAmount(currDate, loan.getId());
		}
		amount = amount.setScale(2, RoundingMode.HALF_UP);

		return amount;
	}

	/**
	 * 初始化银行关系字典表
	 */
	public void initBankRelateTppMap() {
		List<OfferBankDic> bankList = offerBankDicDao.findAllList();
		Map<String, OfferBankDic> bankRelateTppMap = new HashMap<String, OfferBankDic>();
		for (OfferBankDic bank : bankList) {
			bankRelateTppMap.put(bank.getCode(), bank);
		}
		this.bankRelateTppMap = bankRelateTppMap;

	}

	/**
	 * 检查是否有未回盘的情况 查询该loan报盘明细OfferTransaction的所有记录，查看是否有未回盘的，如果有，不能再次生成
	 * 
	 * 
	 * @param loan
	 * @return
	 */
	public boolean checkHasOffer(VLoanInfo loan) {
		// 检查是否有未回盘的情况，如果未回盘，返回true
		List<OfferTransaction> offt = offerTransactionService.getWeiHuiPanTraByloan(loan.getId());
		if (offt != null && offt.size() > 0) {
			logger.info("流水号：" + offt.get(0).getTrxSerialNo() + "还未回盘，loanid=" + offt.get(0).getLoanId() + " ,报盘时间："
					+ Dates.getDateTime(offt.get(0).getReqTime(), null));
			return true;
		}
		// 查看今天是否有生成过报盘文件
		OfferInfo offerInfo = new OfferInfo();
		offerInfo.setLoanId(loan.getId());
		offerInfo.setOfferDate(Dates.getCurrDate());
		offerInfo.setType(OfferTypeEnum.自动划扣.getValue());
		offerInfo.setState(OfferStateEnum.已报盘.getValue());
		List<OfferInfo> offInfo = offerInfoDao.findListByVo(offerInfo);
		if (offInfo != null && offInfo.size() > 0) {
			logger.info("loanId：" + loan.getId() + " 当日已有自动报盘文件");
			return true;
		}
		return false;
	}

	/**
	 * 查找申请通过的并且还款日是今天的提前结清的loan记录
	 * 
	 * @param salesDepartment
	 *            营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByTQJQ(ComOrganization salesDepartment) {
		List<VLoanInfo> list = vLoanInfoService.getTQJQLoanByDate(salesDepartment, Dates.getCurrDate());
		return list;
	}

	/**
	 * 查找申请通过的并且还款日是今天的提前还款的loan记录
	 * 
	 * @param salesDepartment
	 *            营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByTQHK(ComOrganization salesDepartment) {
		List<VLoanInfo> list = vLoanInfoService.getTQHKLoanByDate(salesDepartment, Dates.getCurrDate());
		return list;
	}

	/**
	 * 查找逾期的loan记录，记录中不包含申请过提前结清的记录
	 * 
	 * @param salesDepartment
	 *            营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByYQHK(ComOrganization salesDepartment) {
		List<VLoanInfo> list = vLoanInfoService.getYQHKLoanByOrg(salesDepartment);
		return list;
	}

	/**
	 * 查找还款日正常还款的loan记录，记录中不包含申请提前还款或者提前结清的记录
	 * 
	 * @param salesDepartment
	 *            营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByZCHK(ComOrganization salesDepartment) {
		// 查找还款日正常还款的loan记录，记录中不包含申请提前还款或者提前结清的记录
		List<VLoanInfo> list = vLoanInfoService.getZCHKLoanByOrg(salesDepartment, Dates.getCurrDate());
		return list;
	}

	/**
	 * 按状态和日期查询报盘文件
	 * 
	 * @param currDate
	 * @param state
	 * @param notExists
	 *            要排除的报盘区间
	 * @return
	 */
	private List<OfferInfo> findOfferByOfferDateAndState(Date currDate, OfferStateEnum state, String[] notExists) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("state", state.getValue());
		paramMap.put("offerDate", currDate);
		paramMap.put("type", OfferTypeEnum.自动划扣.getValue());
		paramMap.put("notExistsOfferRange", notExists);

		List<OfferInfo> list = offerInfoDao.findListByMap(paramMap);
		return list;
	}

	@Override
	public List<OfferInfo> findOfferByOfferDateAndStates(Long loanId, Date currDate, String[] states) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		paramMap.put("offerDate", currDate);
		paramMap.put("type", OfferTypeEnum.自动划扣.getValue());
		paramMap.put("offerInfoStates", states);
		List<OfferInfo> list = offerInfoDao.findListByMap(paramMap);

		return list;
	}

	@Override
	public OfferInfo findOfferById(Long offerId) {

		return offerInfoDao.get(offerId);
	}

	@Override
	public void updateOffer(OfferInfo offerInfo) {
		offerInfoDao.update(offerInfo);
	}

	@Override
	public void realtimeDeductByDate(Date currDate) {
		// tpp报盘发送
		sendTppOffer();
		// 第三方报盘发送
		sendOfferToDebit();
	}
	
	/**
	 * 发送TPP报盘信息
	 */
	private void sendTppOffer() {
		// 获得当前报盘使用的tpp系统版本号
		List<OfferInfo> needSendOfferList = searchTodayNeedSendOfferList();
		logger.info("本次报盘文件数：" + needSendOfferList.size() + "============");
		String ver = sysParamDefineService.getSysParamValue("codeHelper", "auto_offer_tpp_ver");
		try {
			if (TPPHelper.TPP_VER20.equals(ver)) {
				logger.info("系统配置发送至tpp2.0接口============");
				// 进行分组
				Map<String, List<OfferInfo>> map = groupOfferByInfoCategoryCode(needSendOfferList);
				for (Map.Entry<String, List<OfferInfo>> entry : map.entrySet()) {
					try {
						sendOfferToTpp2(entry.getValue());
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
				
			} else {
				logger.info("系统配置发送至tpp1.0接口============");
				Map<ZendaiSys, Map<ThirdPartyType, List<OfferInfo>>> tppTypeOfferMapMap = groupOffer(needSendOfferList);
				for (Map.Entry<ZendaiSys, Map<ThirdPartyType, List<OfferInfo>>> entry : tppTypeOfferMapMap.entrySet()) {
					ZendaiSys tppSysNum = entry.getKey();
					Map<ThirdPartyType, List<OfferInfo>> tppTypeOfferListMap = entry.getValue();
					for (Map.Entry<ThirdPartyType, List<OfferInfo>> entry2 : tppTypeOfferListMap.entrySet()) {
						try {
							sendOfferToTpp1(entry2.getValue(), entry2.getKey(), tppSysNum);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				}
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
	}
	/**
	 * 分组根据信息类别
	 * 
	 * @param needSendOfferList
	 * @return
	 */
	private Map<String, List<OfferInfo>> groupOfferByInfoCategoryCode(List<OfferInfo> needSendOfferList) {

		Map<String, List<OfferInfo>> tppOfferListMap = new HashMap<String, List<OfferInfo>>();
		for (OfferInfo offer : needSendOfferList) {
			List<OfferInfo> offerInfos = tppOfferListMap.get(offer.getInfoCategoryCode());
			if (offerInfos == null) {
				offerInfos = new ArrayList<OfferInfo>();
				offerInfos.add(offer);
				tppOfferListMap.put(offer.getInfoCategoryCode(), offerInfos);
			} else {
				offerInfos.add(offer);
				tppOfferListMap.put(offer.getInfoCategoryCode(), offerInfos);
			}
		}
		return tppOfferListMap;
	}

	/**
	 * 得到当天需要去报盘的文件<br>
	 * 
	 * 现在根据逾期天数的不同，在不同的时间点排除不需要报盘的数据
	 * 
	 * @return
	 */
	public List<OfferInfo> searchTodayNeedSendOfferList() {
		List<OfferInfo> offerInfoList = new ArrayList<OfferInfo>();
		// 查询当次需要发送报盘的划扣类型和划扣合同来源
		Map<String,Object> debitParams = offerConfigService.queryDebitParams();
		if(null == debitParams || debitParams.size() == 0){
			return offerInfoList;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("states", new String[] { OfferStateEnum.未报盘.getValue(), OfferStateEnum.已回盘非全额.getValue() });
		params.put("offerDate", Dates.getCurrDate());
		params.put("type", OfferTypeEnum.自动划扣.getValue());
		params.put("debitTypes", debitParams.get("debitTypes"));
		params.put("fundsSourcesList", debitParams.get("fundsSourcesList"));
		return offerInfoDao.queryTodayNeedSendOfferList(params);
	}
	/**
	 * 得到当天第三方需要去报盘的文件<br>
	 * 
	 * 现在根据逾期天数的不同，在不同的时间点排除不需要报盘的数据
	 * 
	 * @return
	 */
	public List<DebitBaseInfo> searchTodayNeedSendOfferDebitList() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offerInfoStates", new String[] { OfferStateEnum.未报盘.getValue(), OfferStateEnum.已回盘非全额.getValue() });
		params.put("type", OfferTypeEnum.自动划扣.getValue());
		params.put("offerDate", Dates.getCurrDate());
		return debitBaseInfoDaoImpl.queryTodayNeedSendOfferDebitList(params);
	}
	/**
	 * 根据系统名和第三方机构分组
	 * 
	 * @param needSendOfferList
	 * @return
	 */
	public Map<ZendaiSys, Map<ThirdPartyType, List<OfferInfo>>> groupOffer(List<OfferInfo> needSendOfferList) {
		Map<ZendaiSys, Map<ThirdPartyType, List<OfferInfo>>> tppOfferMap = new HashMap<ZendaiSys, Map<ThirdPartyType, List<OfferInfo>>>();

		for (OfferInfo offer : needSendOfferList) {
			ZendaiSys orTppSysNum = Strings.isNotEmpty(offer.getTppSysNum()) ? ZendaiSys.valueOf(offer.getTppSysNum())
					: ZendaiSys.ZENDAI_2004_SYS;

			ThirdPartyType tppType = ThirdPartyType.valueOf(offer.getTppType());

			Map<ThirdPartyType, List<OfferInfo>> tppOfferListMap = tppOfferMap.get(orTppSysNum);
			if (tppOfferListMap == null) {
				tppOfferMap.put(orTppSysNum, new HashMap<ThirdPartyType, List<OfferInfo>>());
				tppOfferListMap = tppOfferMap.get(orTppSysNum);
			}

			if (tppOfferListMap.get(tppType) == null)
				tppOfferListMap.put(tppType, new ArrayList<OfferInfo>());
			tppOfferListMap.get(tppType).add(offer);
		}
		return tppOfferMap;
	}

	/**
	 * 按照批次（一个系统一个第三方扣款机构）发送tpp
	 * 
	 * @param offerList
	 * @param tppType
	 * @param tppSysNum
	 * @return
	 */
	/*
	 * @Transactional(propagation=Propagation.REQUIRED) public String
	 * sendOffer(List<OfferInfo> offerList, ThirdPartyType tppType,ZendaiSys
	 * tppSysNum) { //获得当前报盘使用的tpp系统版本号 String ver =
	 * sysParamDefineService.getSysParamValue("codeHelper",
	 * "auto_offer_tpp_ver");//"realtime_offer_tpp_ver"
	 * if(TPP_VER20.equals(ver)){ logger.info("系统配置发送至tpp2.0接口============");
	 * sendOfferToTpp2(offerList); }else{
	 * logger.info("系统配置发送至tpp1.0接口============");
	 * sendOfferToTpp1(offerList,tppType,tppSysNum); } return "000000";
	 * 
	 * }
	 */

	/**
	 * 发送至tpp1.0系统
	 * 
	 * @param offerList
	 * @param tppType
	 * @param tppSysNum
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendOfferToTpp1(List<OfferInfo> offerList, ThirdPartyType tppType, ZendaiSys tppSysNum) {
		logger.info("开始发送批次 ======ThirdPartyType:" + tppType + " ZendaiSys:" + tppSysNum + " ,个数：" + offerList.size());

		if (offerList != null && offerList.size() > 0) {
			try {
				RequestVo requestVo = buildTppRequestVo(tppType, tppSysNum);
				List<OfferTransaction> list = new ArrayList<OfferTransaction>();
				for (int i = 0; i < offerList.size(); i++) {
					OfferInfo offerInfo = offerList.get(i);
					try {
						// Calendar calendar=Calendar.getInstance();
						// int hour=calendar.get(Calendar.HOUR_OF_DAY);
						// /** 代扣通道为用友（畅捷） **/
						// if ("YYoupay".equals(offerList.get(i).getTppType())){
						// /** 如果报盘日是还款日 **/
						// if(vLoanInfoService.isRepaymentDay(offerList.get(i).getLoanId())){
						// /** 一天只能进行1次报盘（10:30） (01:30、13:30和16:30)不进行报盘 **/
						// if(hour == 1 || hour == 13 || hour == 16){
						// continue;
						// }
						// }else{
						// /** 非还款日取消自动报盘 **/
						// continue;
						// }
						// }
						// 针对龙信小贷、外贸信托、外贸2、包商银行，判断是否已经完成划扣服务费，如果没有，则无法进行自动划扣操作
				        if(!loanFeeInfoService.isAlreadyDebitServiceCharge(offerInfo.getLoanId())){
				        	throw new PlatformException(ResponseEnum.FULL_MSG,"该笔借款没有完成划扣服务费，无法进行自动划扣操作！");
				        }
				        VLoanInfo loan = vLoanInfoService.findByLoanId(offerInfo.getLoanId());
						Assert.notNull(loan, ResponseEnum.FULL_MSG,"未找到借款记录");
				        if(FundsSourcesTypeEnum.包商银行.getValue().equals(loan.getLoanBelong())){
				        	// 得到特殊还款记录表信息
				            LoanSpecialRepayment specialRepayment = loanSpecialRepaymentService.findByLoanIdAndRequestDateAndTypesAndState(offerInfo.getLoanId(),
				         				null,
				         				new String[]{SpecialRepaymentTypeEnum.提前扣款.getValue(),SpecialRepaymentTypeEnum.一次性还款.getValue()}, 
				         				SpecialRepaymentStateEnum.申请.getValue());
				            Map<String, Object> map = new HashMap<String, Object>();
				            map.put("loanId", offerInfo.getLoanId());
				            map.put("CurrTermReturnDate", Dates.getCurrDate());
				            List<LoanRepaymentDetail> planlist = loanRepaymentDetailService.findByLoanIdAndRepaymentState(map);
				            LoanRepaymentDetail last = planlist.get(planlist.size()-1);
				            //当提前申请并未生效，而且也没有逾期，不能自动划扣
				            if(specialRepayment == null && last.getRepaymentState().equals("结清")){
				            	throw new PlatformException("包商银行提前还款/结清申请未生效，无法进行自动划扣操作").applyLogLevel(LogLevel.ERROR);
				            }
				        }
						OfferTransaction offerTransaction = offerTransactionService.setRequestDetailVo(requestVo, offerInfo, tppType);
						if (offerTransaction != null) {
							if(this.isSendToTpp(offerInfo)){
								list.add(offerTransaction);
							} else {
								// 如果不发送TPP，则更新报盘状态、报盘流水状态
								this.updateOfferState(offerTransaction);
							}
						}
					} catch (Exception e) {
						logger.error("设置tpp对象，创建交易流水时异常！offerInfoId:" + offerList.get(i).getId() + e.getMessage(), e);
					}
					if ((i + 1) % 1000 == 0) {// 1000的倍数发送，防止批次太大一起失败
						// 发送
						sendToTppUpdateState(list, requestVo);
						// 清空容器，准备下个批次
						requestVo = buildTppRequestVo(tppType, tppSysNum);
						list = new ArrayList<OfferTransaction>();
					}
				}
				// 最后一次发送
				sendToTppUpdateState(list, requestVo);
			} catch (Exception e) {
				logger.error(tppType.toString() + "实时报盘发生异常,程序catchException:" + e.getMessage(), e);
				try {
					sendMailService.sendTextMail("", tppType.toString() + "实时报盘异常", tppType.toString()
							+ "实时报盘发生异常,异常原因:" + e.getMessage());
				} catch (Exception e1) {
					logger.error(e1.getMessage());
				}
			}

		} else {
			logger.info("本次" + tppType.toString() + "没有报盘数据!");
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendOfferToTpp1(OfferInfo offer) {
		if (offer != null) {
			OfferTransaction offerTransaction = null;
			try {
				RequestVo requestVo = buildTppRequestVo(ThirdPartyType.valueOf(offer.getTppType()),
						Strings.isNotEmpty(offer.getTppSysNum()) ? ZendaiSys.valueOf(offer.getTppSysNum())
								: ZendaiSys.ZENDAI_2004_SYS);
				offerTransaction = offerTransactionService.setRequestDetailVo(requestVo, offer, ThirdPartyType.valueOf(offer.getTppType()));
				if(this.isSendToTpp(offer)){
					// 发送
					sendToTppUpdateState(offerTransaction, requestVo);
				} else {
					// 如果不发送TPP，则更新报盘状态、报盘流水状态
					this.updateOfferState(offerTransaction);
				}
			} catch (Exception e) {
				offerTransactionService.updateErrorMsgNow(e.getMessage(), offer, offerTransaction);
				logger.error("loanid:" + offer.getLoanId() + " 实时划扣发生异常,程序catchException:" + e.getMessage(), e);
				throw new RuntimeException(e.getMessage());
			}
		}

	}

	/**
	 * 发送至tpp2.0系统
	 * 
	 * @param offerList
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendOfferToTpp2(List<OfferInfo> offerList) {
		if (offerList != null && offerList.size() > 0) {
			try {
				com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo requestVo = buildTppRequestVo20(offerList.get(0));
				List<OfferTransaction> list = new ArrayList<OfferTransaction>();
				for (int i = 0; i < offerList.size(); i++) {
					OfferInfo offerInfo = offerList.get(i);
					try {
						// Calendar calendar=Calendar.getInstance();
						// int hour=calendar.get(Calendar.HOUR_OF_DAY);
						// /** 代扣通道为用友（畅捷） **/
						// if ("YYoupay".equals(offerList.get(i).getTppType())){
						// /** 如果报盘日是还款日 **/
						// if(vLoanInfoService.isRepaymentDay(offerList.get(i).getLoanId())){
						// /** 一天只能进行1次报盘（10:30） (01:30、13:30和16:30)不进行报盘 **/
						// if(hour == 1 || hour == 13 || hour == 16){
						// continue;
						// }
						// }else{
						// /** 非还款日取消自动报盘 **/
						// continue;
						// }
						// }
						// 针对龙信小贷、外贸信托、外贸2、包商银行，判断是否已经完成划扣服务费，如果没有，则无法进行自动划扣操作
				        if(!loanFeeInfoService.isAlreadyDebitServiceCharge(offerInfo.getLoanId())){
				        	throw new PlatformException(ResponseEnum.FULL_MSG,"该笔借款没有完成划扣服务费，无法进行自动划扣操作！");
				        }
				        VLoanInfo loan = vLoanInfoService.findByLoanId(offerInfo.getLoanId());
						Assert.notNull(loan, ResponseEnum.FULL_MSG,"未找到借款记录");
				        if(FundsSourcesTypeEnum.包商银行.getValue().equals(loan.getLoanBelong())){
				        	// 得到特殊还款记录表信息
				            LoanSpecialRepayment specialRepayment = loanSpecialRepaymentService.findByLoanIdAndRequestDateAndTypesAndState(offerInfo.getLoanId(),
				         				null,
				         				new String[]{SpecialRepaymentTypeEnum.提前扣款.getValue(),SpecialRepaymentTypeEnum.一次性还款.getValue()}, 
				         				SpecialRepaymentStateEnum.申请.getValue());
				            Map<String, Object> map = new HashMap<String, Object>();
				            map.put("loanId", offerInfo.getLoanId());
				            map.put("CurrTermReturnDate", Dates.getCurrDate());
				            List<LoanRepaymentDetail> planlist = loanRepaymentDetailService.findByLoanIdAndRepaymentState(map);
				            LoanRepaymentDetail last = planlist.get(planlist.size()-1);
				            //当提前申请并未生效，而且也没有逾期，不能自动划扣
				            if(specialRepayment == null && last.getRepaymentState().equals("结清")){
				            	throw new PlatformException("包商银行提前还款/结清申请未生效，无法进行自动划扣操作").applyLogLevel(LogLevel.ERROR);
				            }
				        }
						OfferTransaction offerTransaction = offerTransactionService.setRequestDetailVo20(requestVo, offerInfo);
						if (offerTransaction != null) {
							list.add(offerTransaction);
						}
					} catch (Exception e) {
						logger.error("设置tpp对象，创建交易流水时异常！offerInfoId:" + offerList.get(i).getId() + e.getMessage(), e);
					}

					if ((i + 1) % 1000 == 0) {// 1000的倍数发送，防止批次太大一起失败
						// 发送
						sendToTppUpdateState20(list, requestVo);
						// 清空容器，准备下个批次
						requestVo = buildTppRequestVo20(offerList.get(0));
						list = new ArrayList<OfferTransaction>();
					}
				}
				// 最后一次发送
				sendToTppUpdateState20(list, requestVo);
			} catch (Exception e) {
				logger.error("2.0实时报盘发生异常,程序catchException:" + e.getMessage(), e);
				try {
					sendMailService.sendTextMail("", "2.0报盘异常", "报盘发生异常,异常原因:" + e.getMessage());
				} catch (Exception e1) {
					logger.error(e1.getMessage());
				}
			}

		} else {
			logger.info("本次 没有报盘数据!");
		}
	}

	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendOfferToTpp2(OfferInfo offer) {
		if (offer != null) {
			OfferTransaction offerTransaction = null;
			try {
				com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo requestVo = buildTppRequestVo20(offer);
				offerTransaction = offerTransactionService.setRequestDetailVo20(requestVo, offer);
				if(null != offerTransaction){
					// 发送TPP
					sendToTppUpdateState20(offerTransaction, requestVo);
				}
			} catch (Exception e) {
				offerTransactionService.updateErrorMsgNow(e.getMessage(), offer, offerTransaction);
				logger.error("2.0实时报盘发生异常,程序catchException:" + e.getMessage(), e);
				throw new RuntimeException(e.getMessage());
			}

		}
	}

	/**
	 * 发送tpp2.0并修改发送状态
	 * 
	 * @param list
	 * @param requestVo
	 */
	private void sendToTppUpdateState20(List<OfferTransaction> list,
			com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo requestVo) {
		if (list == null || list.size() == 0) {
			logger.warn("发送的报盘交易个数=0，该次未发送！");
			return;
		}
		Response response = brokerTradeConsumer.asynCollect(requestVo);

		if (TPPHelper.TPP20_RECEIVE_SUCCESS_CODE.equals(response.getCode())) {
			// 更新报盘数据的状态
			try {
				offerTransactionService.updateOfferStateToYibaopan(list);
			} catch (Exception e) {
				logger.error("跟更新报盘文件状态出错!", e);
			}
		} else {
			logger.error("报盘发生异常,tpp2.0返回错误信息:" + response.getMsg());
		}
	}

	/**
	 * 发送tpp2.0并修改发送状态
	 * 
	 * @param list
	 * @param requestVo
	 */
	private void sendToTppUpdateState20(OfferTransaction offer,
			com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo requestVo) {
		if (offer == null) {
			logger.warn("发送的报盘交易个数=0，该次未发送！");
			return;
		}
		Response response = brokerTradeConsumer.asynCollect(requestVo);

		if (TPPHelper.TPP20_RECEIVE_SUCCESS_CODE.equals(response.getCode())) {
			// 更新报盘数据的状态
			try {
				List<OfferTransaction> list = new ArrayList<OfferTransaction>();
				list.add(offer);
				offerTransactionService.updateOfferStateToYibaopan(list);
			} catch (Exception e) {
				logger.error("跟更新报盘文件状态出错!", e);
				throw new RuntimeException("loanid:" + offer.getLoanId() + "跟更新报盘文件状态出错!" + e.getMessage());
			}
		} else {
			logger.error("报盘发生异常,tpp2.0返回错误信息:" + response.getMsg());
			throw new RuntimeException("loanid:" + offer.getLoanId() + "报盘发生异常,tpp2.0返回错误信息:" + response.getMsg());
		}
	}

	/**
	 * tpp2.0 发送对象创建
	 * 
	 * @param offerList
	 * @return
	 */
	private com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo buildTppRequestVo20(OfferInfo offerInfo) {
		com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo vo = new com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo();
		vo.setBizSysNo(offerInfo.getBizSysNo());
		vo.setInfoCategoryCode(offerInfo.getInfoCategoryCode());
		return vo;
	}

	/**
	 * 发送报盘，并跟新状态
	 * 
	 * @param list
	 *            用于跟新状态
	 * @param requestVo
	 *            用于发送
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void sendToTppUpdateState(List<OfferTransaction> list, RequestVo requestVo) {
		if (list == null || list.size() == 0) {
			logger.warn("发送的报盘交易个数=0，该次未发送！");
			return;
		}
		String message = "";
		// String message =
		// thirdPaymentService.innerTransferAsync(requestVo).getMessage();
		if (true) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "为了适应接口切换，取消innerTransferAsync接口调用(RMI接口)");
		}
		if (TPPHelper.TPP_RECEIVE_SUCCESS_CODE.equals(message)) {
			// 更新报盘数据的状态
			try {
				offerTransactionService.updateOfferStateToYibaopan(list);
			} catch (Exception e) {
				logger.error("跟更新报盘文件状态出错!", e);
			}
		} else {
			logger.error("实时报盘发生异常,tpp返回错误信息:" + message);
		}

	}

	/**
	 * 发送报盘，并跟新状态 (单条)
	 * 
	 * @param offer
	 *            用于跟新状态
	 * @param requestVo
	 *            用于发送
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void sendToTppUpdateState(OfferTransaction offer, RequestVo requestVo) {
		if (offer == null) {
			logger.warn("发送的报盘交易个数=0，该次未发送！");
			throw new RuntimeException("发送的报盘交易个数=0，该次未发送！");
		}
		String message = "";
		// String message =
		// thirdPaymentService.innerTransferAsync(requestVo).getMessage();
		if (true) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "为了适应接口切换，取消innerTransferAsync接口调用(RMI接口)");
		}
		if (TPPHelper.TPP_RECEIVE_SUCCESS_CODE.equals(message)) {
			// 更新报盘数据的状态
			try {
				List<OfferTransaction> list = new ArrayList<OfferTransaction>();
				list.add(offer);
				offerTransactionService.updateOfferStateToYibaopan(list);
			} catch (Exception e) {
				logger.error("跟更新报盘文件状态出错!" + e.getMessage(), e);
				throw new RuntimeException("loanid:" + offer.getLoanId() + "跟更新报盘文件状态出错!" + e.getMessage());
			}
		} else {
			logger.error("实时报盘发生异常,tpp返回错误信息:" + message);
			throw new RuntimeException("loanid:" + offer.getLoanId() + " 实时报盘发生异常,tpp返回错误信息:" + message);
		}

	}

	/**
	 * 组装tpp发送对象基础信息
	 * 
	 * @param thirdPartyType
	 * @param tppSysNum
	 * @return
	 */
	public RequestVo buildTppRequestVo(ThirdPartyType thirdPartyType, ZendaiSys tppSysNum) {
		RequestVo requestVo = new RequestVo();
		requestVo.setRequestDate(new Date());// 请求时间
		requestVo.setAsynTag(IsAsyn.IS_ASYN_TAG_1);// 0 非异步 不需要回调 1 异步 需要回调
		requestVo.setRequestType(RequestType.COLLECTING);// 代收 ，PAYING为代付
		requestVo.setRequestSystem(tppSysNum);// 请求系统编号，此为信贷业务管理系统
		requestVo.setThirdPartyType(thirdPartyType);// 请求的第三方商户类型
		requestVo.setRequestOperator("credit");
		requestVo.setReSendTag(IsReSend.IS_RESEND_TAG_0);
		requestVo.setThirdPartyRequestStatus("0");
		requestVo.setRequestIp("");// IP地址

		return requestVo;
	}

	/**
	 * 实时扣款更新接口方法
	 * 
	 * @param paramsVo
	 *            报盘参数VO
	 */
	public Map<String, Object> updateOfferInfo(OfferParamsVo paramsVo) {
		Map<String, Object> json = new HashMap<String, Object>();

		OfferInfo offerInfo = new OfferInfo();
		offerInfo.setLoanId(paramsVo.getLoanId());
		List<OfferInfo> result = offerInfoDao.findListByVo(offerInfo);
		if (result != null && result.size() > 0) {
			offerInfo = result.get(0);// 最新报盘信息
		} else {
			json = MessageUtil.returnErrorMessage("还未生成报盘！");
			return json;
		}

		if (OfferStateEnum.未报盘.name().equals(offerInfo.getState())) {
			offerInfo.setOfferAmount(paramsVo.getOfferAmount());// 报盘金额
			offerInfo.setUpdateTime(new Date()); // 最后更新时间
			offerInfo.setUpdator(paramsVo.getUserCode()); // 最后更新人
		} else {
			json = MessageUtil.returnErrorMessage("已报盘不能修改！");
			return json;
		}

		offerInfoDao.update(offerInfo);
		json = MessageUtil.returnHandleSuccessMessage();
		return json;
	}

	/**
	 * 还款试算计算
	 */
	public Map<String, Object> queryTrail(RepayTrailParamsVO paramsVo) {
		Map<String, Object> json = new HashMap<String, Object>();
		List<TrailVO> trailVoList = new ArrayList<TrailVO>();

		List<VLoanInfo> loanList = searchLoanToList(paramsVo);// //查询借款信息
		if (CollectionUtils.isNotEmpty(loanList)) {

			for (VLoanInfo loan : loanList) {
				TrailVO trailVO = GetRepaymentInfoForTrail(loan, paramsVo.getRepayDate(), paramsVo.getRepayType());
				if (trailVO != null) {
					trailVO.setLoanId(loan.getId());
					trailVO.setName(loan.getPersonInfo().getName());
					trailVO.setIdNum(loan.getPersonInfo().getIdnum());
					trailVO.setLoanType(loan.getLoanType());
					trailVO.setPactMoney(loan.getPactMoney());
					trailVO.setTime(loan.getTime().intValue());
					trailVO.setLoanState(loan.getLoanState());
					trailVoList.add(trailVO);
				}
			}
			json = MessageUtil.returnListSuccessMessage(trailVoList, "trailVOs");
		} else {
			json = MessageUtil.returnErrorMessage("没有查询到还款信息！");
			return json;
		}
		return json;
	}

	public TrailVO GetRepaymentInfoForTrail(VLoanInfo loan, Date tradeDate, String repayType) {
		TrailVO vo = new TrailVO();
		Long loanId = loan.getId();
		List<LoanRepaymentDetail> rdList = afterLoanService.getAllInterestOrLoan(tradeDate, loanId);
		int tradeDay = tradeDate.getDate();
		boolean dateFlag = tradeDay == loan.getPromiseReturnDate();
		boolean typeFlag = repayType.equals(RepayTypeEnum.onetime.name());

		/** 获取计算器实例 **/
		ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(loan);

		vo.setAccAmount(afterLoanService.getAccAmount(loanId));
		if (CollectionUtils.isNotEmpty(rdList)) {
			vo.setOverCorpus(afterLoanService.getOverdueCorpus(rdList, tradeDate));// 逾期本金
			vo.setOverInterest(afterLoanService.getOverdueInterest(rdList, tradeDate));// 逾期利息
			vo.setFine(afterLoanService.getFine(rdList, tradeDate));// 逾期罚息
			vo.setCurrCorpus(dateFlag ? afterLoanService.getCurrCorpus(rdList, tradeDate) : new BigDecimal(0));// 当期应还本金
			vo.setCurrInterest(dateFlag || typeFlag ? afterLoanService.getCurrInterest(rdList, tradeDate)
					: new BigDecimal(0));// 当期应还利息
			vo.setBackAmount(typeFlag ? afterLoanService.getGiveBackRate(rdList) : new BigDecimal(0));// 退费
			/** 获取违约金 **/
			BigDecimal penalty = new BigDecimal(0);
			if (typeFlag) {
				penalty = calculatorInstace.getPenalty(loanId, rdList, loan);
			}
			vo.setPenalty(penalty);// 违约金
			vo.setRemnant(afterLoanService.getRemnant(rdList));// 剩余本息和
			vo.setCurrTime(loan.getCurrentTerm());// 当前期数
			vo.setOverdueStartDate(rdList.get(0).getReturnDate());
			vo.setOverDueDays(afterLoanService.getOverdueDay(rdList, tradeDate));
			vo.setResidualTerm(loan.getResidualTerm());
			vo.setReturneTerm(rdList.get(0).getReturneterm());// 每期还款金额
			// 规则计算
			vo.setOverDueAmount(vo.getOverCorpus().add(vo.getOverInterest()));
			vo.setOverDueAllAmt(vo.getOverCorpus().add(vo.getOverInterest()).add(vo.getFine()));
			vo.setCurrAMT(vo.getCurrCorpus().add(vo.getCurrInterest()));
			vo.setNextTimeRepayment(afterLoanService.getCurrAmount(rdList, tradeDate));
			vo.setInterest(vo.getCurrInterest().add(vo.getOverInterest()));
			vo.setPrincipal(typeFlag ? loan.getResidualPactMoney() : (vo.getOverCorpus().add(vo.getCurrCorpus())));
			/** 获取一次性结清金额 **/
			BigDecimal oneTimeRepayAmt = calculatorInstace.getOnetimeRepaymentAmount(loanId, tradeDate, rdList);
			vo.setOneTimeRepayAmt(oneTimeRepayAmt);
			BigDecimal tempValue = vo.getOverCorpus().add(vo.getOverInterest()).add(vo.getFine())
					.subtract(vo.getAccAmount());
			vo.setRepaymentAmount(tempValue);
			if (typeFlag) {
				// vo.setCurrAmount(vo.getOverInterest().add(vo.getCurrInterest()).add(loan.getResidualPactMoney()).add(vo.getFine()).add(vo.getPenalty()).subtract(vo.getBackAmount()).subtract(vo.getAccAmount()));
				vo.setCurrAmount(vo.getOverInterest().add(vo.getOverCorpus()).add(vo.getOneTimeRepayAmt())
						.add(vo.getFine()).subtract(vo.getAccAmount()));
			} else {
				vo.setCurrAmount(vo.getOverInterest().add(vo.getOverCorpus()).add(vo.getCurrInterest())
						.add(vo.getCurrCorpus()).add(vo.getFine()).subtract(vo.getAccAmount()));
			}

			if ((BigDecimal.ZERO.compareTo(vo.getCurrAmount()) == 1)) {
				vo.setCurrAmount(BigDecimal.ZERO);
			}
			/** 逾期期数 **/
			vo.setOverTerm(afterLoanService.getOverdueTermCount(rdList, tradeDate));
		} else {
			// return null;
		}
		return vo;
	}

	/**
	 * 还款试算查询债权信息
	 * 
	 * @param param
	 * @return
	 */
	public List<VLoanInfo> searchLoanToList(RepayTrailParamsVO param) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 准备查询参数 begin
		List<String> loanStates = new ArrayList<String>();
		loanStates.add(LoanStateEnum.正常.name());
		loanStates.add(LoanStateEnum.逾期.name());
		loanStates.add(LoanStateEnum.坏账.name());

		List<String> loanIdList = new ArrayList<String>();
		for (String str : param.getLoanIds()) {
			loanIdList.add(str);
		}

		paramMap.put("name", StringUtils.trim(param.getName()));
		paramMap.put("idnum", StringUtils.trim(param.getIdnum()));
		paramMap.put("mphone", StringUtils.trim(param.getMphone()));
		paramMap.put("loanStates", loanStates);
		paramMap.put("loanIdList", loanIdList);
		// 准备查询参数 end

		return vLoanInfoDao.queryLoanForCTS(paramMap);
	}

	public Map<String, Object> queryOffer(Map<String, Object> paramMap) {
		List<OfferVO> result = new ArrayList<OfferVO>();
		String returnCode = "";
		Pager pager = offerInfoDao.getOfferInfoWithPg(paramMap);
		List querytList = pager.getResultList();
		if (CollectionUtils.isNotEmpty(querytList)) {
			for (int i = 0; i < querytList.size(); i++) {
				OfferInfo o = (OfferInfo) querytList.get(i);
				
				OfferVO obj = new OfferVO();
				obj.setOfferId(o.getId());
				obj.setAmount(o.getAmount());
				obj.setOfferAmount(o.getOfferAmount());
				obj.setBankAcct(o.getBankAcct());
				obj.setBankName(o.getBankName());
				obj.setName(o.getName());
				obj.setCause(o.getCause());
				obj.setState(o.getState());
				obj.setType(o.getType());
				obj.setLoanId(o.getLoanId());
				obj.setNumber(o.getOfferNo());
				try {
					ThirdType thirdType = ThirdType.get(o.getPaySysNo());
					obj.setTppType(thirdType.getDesc());
				} catch (Exception e) {
					obj.setTppType("");
				}
				
				obj.setMemo(o.getMemo() != null ? o.getMemo() : "");
				/**
				 * if(o.getOfferFileId()!=null || o.getReturnFileId()!=null){
				 * OfferTransaction paramObj=new OfferTransaction();
				 * paramObj.setOfferId(o.getId()); List<OfferTransaction>
				 * otList=offerTransactionDao.findListByVo(paramObj);
				 * if(CollectionUtils.isNotEmpty(otList)){
				 * if(o.getOfferFileId()!=null)
				 * obj.setDate(DateFormatUtils.format
				 * (otList.get(0).getReqTime(), "yyyy-MM-dd"));//报盘时间
				 * if(o.getReturnFileId()!=null)
				 * obj.setReturnDate(DateFormatUtils
				 * .format(otList.get(0).getRdoTime(), "yyyy-MM-dd"));//回盘时间 } }
				 **/
				obj.setDate(DateFormatUtils.format(o.getOfferDate(), "yyyy-MM-dd"));// 报盘时间
				returnCode = o.getReturnCode();// 反馈码
				if (Const.RETURNOFFER_SUCCES.equals(returnCode) || Const.RETURNOFFER_SUCCES2.equals(returnCode)
						|| (returnCode != null && returnCode.indexOf(Const.RETURNOFFER_SUCCES_FI) >= 0)) {
					obj.setIsSuc("成功");
				} else if (StringUtils.isNotBlank(returnCode)) {
					obj.setIsSuc("失败");
				} else {
					obj.setIsSuc("");
				}
				result.add(obj);
			}
		}
		return MessageUtil.returnQuerySuccessMessage(pager.getRows(), result, pager.getTotalCount(), "offerVOs");
	}

	public List<OfferInfo> findOfferReturnList(Map<String, Object> params) {
		return offerInfoDao.findOfferReturnList(params);
	}

	@Override
	public int updateOfferInfo(Map<String, Object> params) {
		return offerInfoDao.updateOfferInfo(params);
	}

	/**
	 * 获取代扣通道
	 * 
	 * @param loanId
	 * @return
	 */
	public String getTppType(VLoanInfo loan) {
		if (null == loan) {
			return null;
		}
		// 查询银行账户信息
		LoanBank loanBankInfo = loanBankService.findById(loan.getGiveBackBankId());
		if (null == loanBankInfo) {
			return null;
		}
		if (FundsSourcesTypeEnum.挖财2.getValue().equals(loan.getFundsSources())) {
			return ThirdPartyType.Allinpay.name();
		}
		if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loan.getFundsSources())) {
			// 合同来源为华澳信托的建设银行客户切换通道为银联，代码105：中国建设银行
			if ("105".equals(loanBankInfo.getBankCode())) {
				return ThirdPartyType.SHUnionpay.name();
			} else {
				return ThirdPartyType.YYoupay.name();
			}
		}
		// 从银行关系字典表查找支付通道
		if (null != bankRelateTppMap) {
			return bankRelateTppMap.get(loanBankInfo.getBankCode()).getTppType();
		}
		return null;
	}

	/**
	 * 获取代扣通道
	 * 
	 * @param loanId
	 * @return
	 */
	public String getTppType(Long loanId) {
		VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
		return this.getTppType(loan);
	}

	@Override
	public String getTppBankCode(String bankCode) {
		if (this.bankRelateTppMap != null) {
			OfferBankDic offerBankDic = this.bankRelateTppMap.get(bankCode);
			if (offerBankDic != null) {
				return offerBankDic.getTppBankCode();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
	
	/**
	 * 是否发送TPP（合同来源是外贸信托不发送）
	 * @param offerInfo
	 * @return
	 */
	public boolean isSendToTpp(OfferInfo offerInfo) {
		if(null == offerInfo){
			return false;
		}
		if(FundsSourcesTypeEnum.外贸信托.getValue().equals(offerInfo.getFundsSources())){
			return false;
		}
		return true;
	}
	
	/**
	 * 更新报盘、报盘流水状态
	 * @param offerTransaction
	 */
	private void updateOfferState(OfferTransaction offerTransaction){
		// 如果不发送tpp，则更新报盘状态、报盘流水状态
		List<OfferTransaction> offerTransactionList = new ArrayList<OfferTransaction>();
		offerTransactionList.add(offerTransaction);
		offerTransactionService.updateOfferStateToYibaopan(offerTransactionList);
	}
	
	/**
	 * 外贸2是否回盘
	 * @param loanInfo
	 * @return
	 */
	@Override
	public boolean isExistWaiMao2BackOfferInfo(VLoanInfo loanInfo) {
		if (loanInfo.getLoanBelong().equals(FundsSourcesTypeEnum.外贸2.getValue()) && loanInfo.getFundsSources().equals(FundsSourcesTypeEnum.外贸2.getValue())) {
			return this.checkHasOffer(loanInfo);
		}
		return false;
	}
	
	/**
	 * 根据员工号得到员工最高划扣次数
	 * 
	 */
	@Override
	public String getoffernum(String usercode) {
		return offerInfoDao.getoffercount(usercode);
	}
	
    /**
     * 债权去向为渤海信托、渤海2、华澳信托、国民信托的还款可以支持选择快捷通进行划扣。
     * 自动报盘时中国银行和工商银行选择快捷通，其余银行和原有逻辑保持一致。
     * @param offerInstance
     * @param loanBelong
     */
    private void configurePayType(OfferInfo offerInstance, String loanBelong) {
        // 银行代码
        String bankCode = offerInstance.getBankCode();
        if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loanBelong)) {
            if ("102".equals(bankCode) || "104".equals(bankCode)) { // 中国银行（104）和工商银行（102）选择走快捷通支付通道
                offerInstance.setTppType(TppPaySysNoEnum.快捷通.getValue());// 1.0属性
                offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
            } else if ("105".equals(bankCode)) {// 合同来源为华澳信托的建设银行客户切换通道为银联，代码105：中国建设银行
                offerInstance.setTppType(ThirdPartyType.SHUnionpay.name());// 1.0属性
                offerInstance.setPaySysNo(ThirdType.SHUNIONPAY.getCode());// 2.0属性
            } else {
                offerInstance.setTppType(ThirdPartyType.YYoupay.name());// 1.0属性
                offerInstance.setPaySysNo(ThirdType.YONGYOUUNIONPAY.getCode());// 2.0属性
            }
        } else if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelong)) {
            if ("102".equals(bankCode) || "104".equals(bankCode)) { // 中国银行（104）和工商银行（102）选择走快捷通支付通道
                offerInstance.setTppType(TppPaySysNoEnum.快捷通.getValue());// 1.0属性
                offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
            } else {
                offerInstance.setTppType(ThirdPartyType.Allinpay.name());// 1.0属性
                offerInstance.setPaySysNo(ThirdType.ALLINPAY.getCode());// 2.0属性
            }
        } else if (FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelong)) {
            if ("102".equals(bankCode) || "104".equals(bankCode)) { // 中国银行（104）和工商银行（102）选择走快捷通支付通道
                offerInstance.setTppType(TppPaySysNoEnum.快捷通.getValue());// 1.0属性
                offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
            } else if ("103".equals(bankCode) // 建设银行、中国农业银行、中国民生银行、上海浦东发展银行走通联支付通道，其余情况走上海银联支付通道
                    || "105".equals(bankCode)
                    || "310".equals(bankCode)
                    || "305".equals(bankCode)) {// 渤海2的通联商户
                offerInstance.setTppType(ThirdPartyType.Allinpay.name());// 1.0属性
                offerInstance.setPaySysNo(ThirdType.ALLINPAY.getCode());// 2.0属性
            } else {// p2p银联商户
                offerInstance.setTppType(ThirdPartyType.SHUnionpay.name());// 1.0属性
                offerInstance.setPaySysNo(ThirdType.SHUNIONPAY.getCode());// 2.0属性
            }
        } else if (FundsSourcesTypeEnum.国民信托.getValue().equals(loanBelong) || FundsSourcesTypeEnum.证大P2P.getValue().equals(loanBelong)
        		|| FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)) {
            if ("102".equals(bankCode) || "104".equals(bankCode)) { // 中国银行（104）和工商银行（102）选择走快捷通支付通道
                offerInstance.setTppType(TppPaySysNoEnum.快捷通.getValue());// 1.0属性
                offerInstance.setPaySysNo(TppPaySysNoEnum.快捷通.getCode());// 2.0属性
            } else {
                offerInstance.setTppType(bankRelateTppMap.get(offerInstance.getBankCode()).getTppType());// 1.0属性
                offerInstance.setPaySysNo(bankRelateTppMap.get(offerInstance.getBankCode()).getPaySysNo());// 2.0属性
            }
        }
    }

    /**
	 * 创建第三方报盘信息<br>
	 * 
	 * @param loan
	 * @param salesDepartment
	 * @param type
	 *            类型用于控制优先级 ；1：正常扣款，2：逾期扣款，3：提前扣款，4：提前结清 ，5：实时划扣
	 * @param offerAmount
	 *            报盘金额 ：如果是null则全额报盘
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DebitBaseInfo createDebitOffer(VLoanInfo loan, ComOrganization salesDepartment, int type, BigDecimal offerAmount,String paySysNo) {
		// 债权去向
		String loanBelong = loan.getLoanBelong();
		// 判断是否已经完成划扣服务费，如果没有，则停止生成报盘信息
		if(!loanFeeInfoService.isAlreadyDebitServiceCharge(loan.getId())){
			logger.warn("该笔借款没有完成划扣服务费，不能生成报盘信息！");
			return null;
		}
		
		// 计算还款金额
		BigDecimal amount = calculateOfferAmount(loan);
		if (offerAmount != null) {
			if (offerAmount.compareTo(new BigDecimal("0.01")) < 0 || offerAmount.compareTo(amount) > 0) {
				throw new RuntimeException("指定划扣金额小于0.01或者大于应还总额，创建报盘失败");
			}
			amount = offerAmount;
		}
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			logger.warn("报盘金额必须大于零，创建报盘失败！");
			return null;
		}
		
		// 获取借款人信息
		PersonInfo personInfo = personInfoService.findById(loan.getBorrowerId());
		
		// 获取借款人还款银行卡信息
		LoanBank giveBackBank = loanBankService.findById(loan.getGiveBackBankId());
		
		// 第三方划扣信息表
		DebitBaseInfo debitBaseInfo = new DebitBaseInfo();
		debitBaseInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.DEBIT_BASE_INFO));
		debitBaseInfo.setLoanId(loan.getId());
		debitBaseInfo.setPactNo(loan.getContractNum());
		debitBaseInfo.setIdType("0");
		debitBaseInfo.setIdNo(personInfo.getIdnum());
		debitBaseInfo.setCustName(personInfo.getName());
		debitBaseInfo.setOfferDate(Dates.getCurrDate());
		debitBaseInfo.setAmount(amount);
		debitBaseInfo.setOfferAmount(amount);
		debitBaseInfo.setActualAmount(BigDecimal.ZERO);
		if(FundsSourcesTypeEnum.外贸3.getValue().equals(loanBelong)){
			debitBaseInfo.setPaySysNo(FundsSourcesTypeEnum.外贸3.getCode());
		}else if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
			debitBaseInfo.setPaySysNo(FundsSourcesTypeEnum.陆金所.getCode());
		}
		debitBaseInfo.setBankName(giveBackBank.getBankName());
		debitBaseInfo.setBankCode(giveBackBank.getBankCode());
		debitBaseInfo.setAcctNo(giveBackBank.getAccount().trim());
		debitBaseInfo.setAcctName(personInfo.getName());
		debitBaseInfo.setAcctType("11");// 个人借记卡
		if (type == IOfferInfoService.SHISHIHUAKOU) {
			debitBaseInfo.setType(OfferTypeEnum.实时划扣.getValue());
		} else {
			debitBaseInfo.setType(OfferTypeEnum.自动划扣.getValue());
		}
		debitBaseInfo.setState(OfferStateEnum.未报盘.getValue());
		
		// 第三方划扣明细信息表
		DebitDetailInfo debitDetailInfo = new DebitDetailInfo();
		debitDetailInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.DEBIT_DETAIL_INFO));
		debitDetailInfo.setDebitId(debitBaseInfo.getId());
		if(FundsSourcesTypeEnum.外贸3.getValue().equals(loanBelong)){
			debitDetailInfo.setAccountNo(BaseParamVo.SYS_SOURCE_WM3+"_"+loan.getContractNum());
		}
		debitDetailInfo.setBankName(giveBackBank.getFullName());
		// 得到所在市
		ComOrganization cityOrganization = comOrganizationDao.get(Long.parseLong(salesDepartment.getParentId()));
		if(null != cityOrganization){
			debitDetailInfo.setCity(cityOrganization.getName());
		}
		debitDetailInfo.setPhoneNo(personInfo.getMphone());
		debitDetailInfo.setEmail(personInfo.getEmail());
		if(FundsSourcesTypeEnum.外贸3.getValue().equals(loanBelong)){
			debitDetailInfo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
		}else if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)){
			debitDetailInfo.setBrNo(BaseParamVo.DEP_NO_LUFAX);
		}
		
		// 创建报盘
		debitBaseInfoDaoImpl.insert(debitBaseInfo);
		debitDetailInfoDaoImpl.insert(debitDetailInfo);
		
		//创建外贸3报盘信息成功
		return debitBaseInfo;
	}
	
	/**
	 * 发送至第三方系统
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendOfferToDebit() {
		//查询外贸3报盘信息
		List<DebitBaseInfo> debitBaseInfoList = searchTodayNeedSendOfferDebitList();
		if(CollectionUtils.isEmpty(debitBaseInfoList)){
			logger.info("第三方本次 没有报盘数据!");
			return;
		}
		sendOffer(debitBaseInfoList);
	}
	/**
	 * 创建第三方报盘流水信息并发送报盘
	 */
	public void sendOffer(List<DebitBaseInfo> debitBaseInfoList) {
		// 将不同债权去向的借款分开分别发送
		Map<String, List<DebitBaseInfo>> result = new HashMap<String, List<DebitBaseInfo>>();
		List<DebitBaseInfo> list = null;
		for (DebitBaseInfo vo : debitBaseInfoList) {
			String key = vo.getPaySysNo();
			if (result.containsKey(key)) {
				list = result.get(key);
			} else {
				list = new ArrayList<DebitBaseInfo>();
			}
			list.add(vo);
			result.put(key, list);
		}
		for (String key : result.keySet()) {
			List<DebitBaseInfo> subList = result.get(key);
			if (FundsSourcesTypeEnum.外贸3.getCode().equals(key)) {// 外贸三
				excuteWm3(subList);
			} else if (FundsSourcesTypeEnum.陆金所.getCode().equals(key)) {// 陆金所
				excuteLjs(subList);
			}
		}
	}
	
	/**
	 * 创建流水并发送至外贸三
	 * @param debitBaseInfoList
	 */
	private void excuteWm3(List<DebitBaseInfo> debitBaseInfoList) {
		// 报盘数量
		int size = debitBaseInfoList.size();
		// 报盘次数
		int loopCount = size % BATCH_SIZE == 0 ? size / BATCH_SIZE : size / BATCH_SIZE + 1;
		// 批次报盘数据
		List<DebitBaseInfo> subList = new ArrayList<DebitBaseInfo>();
		// 扣款渠道（CL0001中金支付、 CL0002广银联）
		String cardChn = sysParamDefineService.getSysParamValue("wm3", "cardChn");
		// 创建流水信息
		for (int i = 0; i < loopCount; i++) {
			if (i < loopCount - 1) {// 剩余报盘信息大于批次数量截取批次数量
				subList = debitBaseInfoList.subList(i * BATCH_SIZE, (i + 1)* BATCH_SIZE);
			} else {// 剩余报盘信息小于或等于批次数量截取剩余数量
				subList = debitBaseInfoList.subList(i * BATCH_SIZE, size);
			}
			// 获取系统时间为拼接批次号
			String batchNo = "WM3_A_" + DateTime.now().toString("yyyyMMddHHmmss") + i + RandomStringUtils.randomNumeric(6);
			List<DeductMoneyListEntity> deductMoneyListEntityList = new ArrayList<DeductMoneyListEntity>();
			// 记录数
			int dataCnt = 0;
			// 插入流水信息
			for (DebitBaseInfo debitBaseInfo : subList) {
				try {
					// 判断是否已经完成划扣服务费，如果没有，则无法进行自动划扣操作
					if (!loanFeeInfoService.isAlreadyDebitServiceCharge(debitBaseInfo.getLoanId())) {
						logger.warn("该笔借款没有完成划扣服务费，无法进行自动划扣操作！");
						continue;
					}
					// 数据设置
					VLoanInfo loan = vLoanInfoService.findByLoanId(debitBaseInfo.getLoanId());
					Assert.notNull(loan, ResponseEnum.FULL_MSG, "未找到借款记录");
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setId(sequencesServiceImpl.getSequences(SequencesEnum.DEBIT_TRANSACTION));
					debitTransaction.setLoanId(debitBaseInfo.getLoanId());
					debitTransaction.setDebitId(debitBaseInfo.getId());
					debitTransaction.setSerialNo(debitTransaction.getId().toString());
					debitTransaction.setPayAmount(debitBaseInfo.getAmount());
					debitTransaction.setReqTime(new Date());
					debitTransaction.setBatNo(batchNo);
					debitTransaction.setState(OfferTransactionStateEnum.未发送.getValue());
					// 数据插入
					debitTransactionDaoImpl.insert(debitTransaction);

					// 收集报盘参数信息
					DeductMoneyListEntity deductMoneyListEntity = new DeductMoneyListEntity();
					// 请求数据获取（单个报盘数据）
					deductMoneyListEntity.setAcNo(debitBaseInfo.getAcctNo());
					deductMoneyListEntity.setPactNo(debitBaseInfo.getPactNo());
					deductMoneyListEntity.setRepayAmt(debitBaseInfo.getAmount());
					// 扣款类型：03-溢缴款充值
					deductMoneyListEntity.setRepayType("03");
					deductMoneyListEntity.setSerialNo(debitTransaction.getSerialNo());
					// 扣款渠道、生产环境不设置
					if(Strings.isNotEmpty(cardChn)){
						deductMoneyListEntity.setCardChn(cardChn);
					}
					deductMoneyListEntityList.add(deductMoneyListEntity);
					// 报盘数量更新
					dataCnt++;
				} catch (Exception e) {
					logger.error("创建交易流水时异常！debitBaseInfoId:"+ debitBaseInfo.getId()+ e.getMessage(), e);
				}
			}
			if(CollectionUtils.isNotEmpty(deductMoneyListEntityList)){
				// 接口响应结果
				WM3_2311OutputVo wM3_2311OutputVo =null;
				// 请求数据获取（所有报盘数据）
				WM3_2311Vo wM3_2311Vo = new WM3_2311Vo();
				// 批次号
				wM3_2311Vo.setBatNo(batchNo);
				// 机构号
				wM3_2311Vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
				// 件数
				wM3_2311Vo.setDataCnt(dataCnt);
				// 接口具体参数信息
				wM3_2311Vo.setList(deductMoneyListEntityList);
				try {
					// 调用外贸3的扣款接口、扣款类型：03-溢缴款充值
					JSONObject jsonObject = GatewayUtils.callCateWayInterface(wM3_2311Vo, GatewayFuncIdEnum.外贸3线上扣款.getCode());
					if(null == jsonObject){
						logger.warn("外贸3线上扣款[2311]接口调用异常！");
						return;
					}
					logger.info("外贸3线上扣款[2311]接口响应结果："+ jsonObject.toJSONString());
					// 接口返回码
					String respCode = jsonObject.getString("respCode");
					// 接口返回描述
					String respDesc = jsonObject.getString("respDesc");
					if (!"0000".equals(respCode)) {
						logger.warn("外贸3线上扣款[2311]接口调用失败，原因是：" + respDesc);
						return;
					}
					// 接口返回响应结果数据
					String content = jsonObject.get("content").toString();
					logger.info("外贸3线上扣款[2311]接口返回业务数据："+ content);
					// 返回响应结果转换成json对象
					JSONObject result = JSONObject.parseObject(content);
					// 接口返回结果转换成bean对象
					wM3_2311OutputVo = JSONObject.toJavaObject(result, WM3_2311OutputVo.class);
					// 更新数据状态
					debitTransactionServiceImpl.updateOfferStateWM3ToYibaopan(wM3_2311Vo, wM3_2311OutputVo);
				} catch (Exception e) {
					logger.error("☆☆☆☆☆☆☆☆☆☆报盘发送外贸三异常！batchNo：" + batchNo + "☆☆☆☆☆☆☆" + e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 创建流水并发送至陆金所
	 * @param debitBaseInfoList
	 */
	private void excuteLjs(List<DebitBaseInfo> debitBaseInfoList) {
		// 报盘数量
		int size = debitBaseInfoList.size();
		// 报盘次数
		int loopCount = size % BATCH_SIZE == 0 ? size / BATCH_SIZE : size / BATCH_SIZE + 1;
		// 批次报盘数据
		List<DebitBaseInfo> subList = new ArrayList<DebitBaseInfo>();
		// 创建流水信息
		for (int i = 0; i < loopCount; i++) {
			if (i < loopCount - 1) {// 剩余报盘信息大于批次数量截取批次数量
				subList = debitBaseInfoList.subList(i * BATCH_SIZE, (i + 1)* BATCH_SIZE);
			} else {// 剩余报盘信息小于或等于批次数量截取剩余数量
				subList = debitBaseInfoList.subList(i * BATCH_SIZE, size);
			}
			List<DebitQueueLog> debitQueueLogList = new ArrayList<DebitQueueLog>();
			List<DebitTransaction> debitTransactionList = new ArrayList<DebitTransaction>();
			// 插入流水信息
			for (DebitBaseInfo debitBaseInfo : subList) {
				try {
					// 判断是否已经完成划扣服务费，如果没有，则无法进行自动划扣操作
					if (!loanFeeInfoService.isAlreadyDebitServiceCharge(debitBaseInfo.getLoanId())) {
						logger.warn("该笔借款没有完成划扣服务费，无法进行自动划扣操作！");
						continue;
					}
					// 数据设置
					VLoanInfo loan = vLoanInfoService.findByLoanId(debitBaseInfo.getLoanId());
					Assert.notNull(loan, ResponseEnum.FULL_MSG, "未找到借款记录");
					
					//保存 第三方DebitTransaction 划扣流水表
					DebitTransaction debitTransaction = new DebitTransaction();
					debitTransaction.setId(sequencesServiceImpl.getSequences(SequencesEnum.DEBIT_TRANSACTION));
					debitTransaction.setDebitId(debitBaseInfo.getId());
					debitTransaction.setSerialNo(debitTransaction.getId().toString());
					debitTransaction.setLoanId(debitBaseInfo.getLoanId());
					debitTransaction.setPayAmount(debitBaseInfo.getOfferAmount());
					debitTransaction.setReqTime(new Date());
					debitTransaction.setState(OfferTransactionStateEnum.未发送.getValue());
					debitTransactionDaoImpl.insert(debitTransaction);
					debitTransactionList.add(debitTransaction);
					
					//保存 第三方DebitQueueLog 代扣信息
					DebitQueueLog debitQueueLog = new DebitQueueLog();
					debitQueueLog.setDebitNotifyState(DebitNotifyStateEnum.待通知.getCode());
					debitQueueLog.setDebitResultState(DebitResultStateEnum.未划扣.getCode());
					debitQueueLog.setLoanId(debitBaseInfo.getLoanId());
					debitQueueLog.setAmount(debitBaseInfo.getOfferAmount());
					debitQueueLog.setDebitType(DebitOperateTypeEnum.自动代扣.getCode());
					debitQueueLog.setPayParty(PayPartyEnum.借款人.getCode());
					debitQueueLog.setRepayType(DebitRepayTypeEnum.机构还款.getCode());
					debitQueueLog.setDebitTransactionId(debitTransaction.getId());
					debitQueueLog.setRepayTerm(this.getRepaymentCurrentTerm(loan));
					debitQueueLogService.saveDebitQueueLog(debitQueueLog);
					debitQueueLogList.add(debitQueueLog);
				} catch (Exception e) {
					logger.error("创建交易流水时异常！debitBaseInfoId:"+ debitBaseInfo.getId()+ e.getMessage(), e);
				}
			}
			if(CollectionUtils.isNotEmpty(debitQueueLogList)){
				//发送代扣通知给陆金所
				debitQueueLogService.executeEntrustDebitAfterDebitOffer(debitQueueLogList);
				//发送代扣后 更新数据状态    为 已发送，已报盘
				debitTransactionServiceImpl.updateDebitStateToYibaopan(debitTransactionList);
			}
		}	
	}
	
    /**
     * 查询一笔还款当时所在期数
     * @param loanInfo
     * @return
     */
    private Long getRepaymentCurrentTerm(VLoanInfo loanInfo){
        Date tradeDate = Dates.format(new Date(), Dates.DEFAULT_DATE_FORMAT);
        if(tradeDate.compareTo(loanInfo.getEndrdate()) > 0){
            return loanInfo.getTime();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanId", loanInfo.getId());
        params.put("repayDate", tradeDate);
        return debitQueueLogService.getRepaymentCurrentTerm(params);
    }
}
