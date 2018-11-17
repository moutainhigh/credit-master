package com.zdmoney.credit.fee.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.tpp.Bh2FeeConfig;
import com.zdmoney.credit.common.tpp.Bsxt2FeeConfig;
import com.zdmoney.credit.common.tpp.HrbhFeeConfig;
import com.zdmoney.credit.common.tpp.LufaxFeeConfig;
import com.zdmoney.credit.common.tpp.LxFeeConfig;
import com.zdmoney.credit.common.tpp.Wmxt2FeeConfig;
import com.zdmoney.credit.common.tpp.Wmxt3FeeConfig;
import com.zdmoney.credit.common.tpp.WmxtFeeConfig;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeOfferDao;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeTransactionService;
import com.zdmoney.credit.fee.vo.CreateFeeOfferVo;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款收费报盘表 Service实现层，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
@Service
public class LoanFeeOfferServiceImpl implements ILoanFeeOfferService {

	protected static Log logger = LogFactory.getLog(LoanFeeOfferServiceImpl.class);
	
	/** 中国银行代码 **/
	public final static String BANK_CODE_CB = "104";
	
	/** 上海银联支付通道 **/
	public final static String SH_UNION_PAY = "4";

	@Autowired
	@Qualifier("loanFeeOfferDaoImpl")
	ILoanFeeOfferDao loanFeeOfferDaoImpl;

	@Autowired
	@Qualifier("loanFeeInfoServiceImpl")
	ILoanFeeInfoService loanFeeInfoServiceImpl;

	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Autowired
	@Qualifier("VLoanInfoServiceImpl")
	IVLoanInfoService vLoanInfoServiceImpl;

	@Autowired
	@Qualifier("loanBankServiceImpl")
	ILoanBankService loanBankServiceImpl;

	@Autowired
	@Qualifier("loanFeeTransactionServiceImpl")
	ILoanFeeTransactionService loanFeeTransactionServiceImpl;

	@Autowired
	@Qualifier("personInfoServiceImpl")
	IPersonInfoService personInfoServiceImpl;

	@Autowired
	@Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;

	@Override
	public LoanFeeOffer findById(Long id) {
		if (Strings.isEmpty(id)) {
			return null;
		}
		return loanFeeOfferDaoImpl.get(id);
	}

	@Override
	public List<LoanFeeOffer> findListByVo(LoanFeeOffer loanFeeOffer) {
		return loanFeeOfferDaoImpl.findListByVo(loanFeeOffer);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LoanFeeOffer createOffer(CreateFeeOfferVo createFeeOfferVo) {
		Long feeId = createFeeOfferVo.getFeeId();
		/** 查询服务费主表信息 **/
		LoanFeeInfo loanFeeInfo = loanFeeInfoServiceImpl.findById(feeId);
		Assert.notNull(loanFeeInfo, ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "记录不存在");
		Long loanId = loanFeeInfo.getLoanId();
		Assert.notNull(loanId, ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "，缺少LoanId数据项");

		/** 判断是否有未回的报盘 **/
		boolean isSendding = loanFeeTransactionServiceImpl.isOfferSendding(feeId);
		if (isSendding) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + ",LoanId：" + loanId
					+ " 有未回盘记录，跳过生成报盘").applyLogLevel(LogLevel.WARN);
		}

		/** 查询债权信息 **/
		VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
		Assert.notNull(loanId, ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "，缺少LOAN_BASE记录");
		String contractNum = Strings.parseString(vLoanInfo.getContractNum());

		/** 管理营业部 **/
		ComOrganization comOrganization = comOrganizationServiceImpl.get(vLoanInfo.getSalesDepartmentId());
		Assert.notNull(comOrganization, ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "，缺少ComOrganization记录");

		PersonInfo personInfo = personInfoServiceImpl.findById(vLoanInfo.getBorrowerId());
		Assert.notNull(personInfo, ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "，缺少PersonInfo记录");

		/** 查询还款银行 **/
		LoanBank loanBank = loanBankServiceImpl.findById(vLoanInfo.getGiveBackBankId());
		Assert.notNull(loanId, ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "，缺少还款银行记录");

		/** 剩余服务费金额 **/
		BigDecimal unpaidAmount = loanFeeInfo.getUnpaidAmount();
		if (unpaidAmount.compareTo(BigDecimal.ZERO) <= 0) {
			Assert.notNull(loanFeeInfo, ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "，服务费小于等于0元");
		}
		/** 服务费报盘金额 **/
		BigDecimal offerAmount = createFeeOfferVo.getAmount();
		if (offerAmount.compareTo(BigDecimal.ZERO) <= 0) {
			offerAmount = unpaidAmount;
		}
		if (offerAmount.compareTo(unpaidAmount) > 0) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "生成服务费报盘失败：FeeId：" + feeId + "，服务费报盘金额过大,欠款："
					+ unpaidAmount + "元").applyLogLevel(LogLevel.WARN);
		}

		LoanFeeOffer loanFeeOffer = new LoanFeeOffer();
		/** 服务费编号 **/
		loanFeeOffer.setFeeId(feeId);
		/** 债权编号 **/
		loanFeeOffer.setLoanId(loanId);
		/** 主键PK **/
		loanFeeOffer.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_OFFER));

		/** 合同编号 **/
		loanFeeOffer.setContractNum(contractNum);
		/** 报盘日期 （不包含时分秒） **/
		loanFeeOffer.setOfferDate(Dates.getCurrDate());

		/** 客户姓名 **/
		loanFeeOffer.setName(personInfo.getName());
		/** 客户身份证 **/
		loanFeeOffer.setIdNum(personInfo.getIdnum());
		/** 客户手机号 **/
		loanFeeOffer.setMobile(personInfo.getMphone());

		/** 报盘应收金额 **/
		loanFeeOffer.setAmount(offerAmount);
		/** 报盘已收金额 默认为0 **/
		loanFeeOffer.setReceiveAmount(BigDecimal.ZERO);
		/** 报盘未收金额 默认为应收金额 **/
		loanFeeOffer.setUnpaidAmount(offerAmount);
		/** 货币类型 **/
		loanFeeOffer.setCurrencyType("CNY");

		/** 银行代码 **/
		loanFeeOffer.setBankCode(Strings.parseString(loanBank.getBankCode()));
		/** 银行名称 **/
		loanFeeOffer.setBankName(Strings.parseString(loanBank.getBankName()));
		/** 银行账号 **/
		loanFeeOffer.setBankAcct(Strings.parseString(loanBank.getAccount()));
		/** 划扣类型（自动划扣，实时划扣） **/
		loanFeeOffer.setType(OfferTypeEnum.实时划扣.getValue());
		//判断是龙信小贷or外贸信托
		if(FundsSourcesTypeEnum.龙信小贷.getValue().equals(createFeeOfferVo.getFundsSources())){
			/** 业务系统编码（由TPP2.0平台提供统一分配） **/
			loanFeeOffer.setBizSysNo(LxFeeConfig.BIZ_SYS_NO);
			/** 信息类别编码（TPP2.0平台提供统一分配） **/
			loanFeeOffer.setInfoCategoryCode(LxFeeConfig.INFO_CATEGORY_CODE);
			/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
			/** 服务费代扣走 通联 通道 **/
			loanFeeOffer.setPaySysNo(LxFeeConfig.PAY_SYS_NO);
			/** 龙信服务费报盘，中国银行（银行代码：104）改上海银联支付通道（4） **/
			/*if(BANK_CODE_CB.equals(loanFeeOffer.getBankCode())){
				loanFeeOffer.setPaySysNo(ThirdType.SHUNIONPAY.getCode());
			}*/
		}else if (FundsSourcesTypeEnum.外贸信托.getValue().equals(createFeeOfferVo.getFundsSources())){
			/** 业务系统编码（由TPP2.0平台提供统一分配） **/
			loanFeeOffer.setBizSysNo(WmxtFeeConfig.BIZ_SYS_NO);
			/** 信息类别编码（TPP2.0平台提供统一分配） **/
			loanFeeOffer.setInfoCategoryCode(WmxtFeeConfig.INFO_CATEGORY_CODE);
			/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
			/** 服务费代扣走 通联 通道 **/
			loanFeeOffer.setPaySysNo(WmxtFeeConfig.PAY_SYS_NO);
		}else if(FundsSourcesTypeEnum.外贸2.getValue().equals(createFeeOfferVo.getFundsSources())){
			/** 业务系统编码（由TPP2.0平台提供统一分配） **/
			loanFeeOffer.setBizSysNo(Wmxt2FeeConfig.BIZ_SYS_NO);
			/** 信息类别编码（TPP2.0平台提供统一分配） **/
			loanFeeOffer.setInfoCategoryCode(Wmxt2FeeConfig.INFO_CATEGORY_CODE);
			/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
			/** 服务费代扣走 通联 通道 **/
			loanFeeOffer.setPaySysNo(Wmxt2FeeConfig.PAY_SYS_NO);
		}else if(FundsSourcesTypeEnum.外贸3.getValue().equals(createFeeOfferVo.getFundsSources())){
			/** 业务系统编码（由TPP2.0平台提供统一分配） **/
			loanFeeOffer.setBizSysNo(Wmxt3FeeConfig.BIZ_SYS_NO);
			/** 信息类别编码（TPP2.0平台提供统一分配） **/
			loanFeeOffer.setInfoCategoryCode(Wmxt3FeeConfig.INFO_CATEGORY_CODE);
			/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
			/** 服务费代扣走 快捷通 通道 **/
			loanFeeOffer.setPaySysNo(Wmxt3FeeConfig.PAY_SYS_NO);
		}else if(FundsSourcesTypeEnum.包商银行.getValue().equals(createFeeOfferVo.getFundsSources())){
			/** 业务系统编码（由TPP2.0平台提供统一分配） **/
			loanFeeOffer.setBizSysNo(Bsxt2FeeConfig.BIZ_SYS_NO);
			/** 信息类别编码（TPP2.0平台提供统一分配） **/
			loanFeeOffer.setInfoCategoryCode(Bsxt2FeeConfig.INFO_CATEGORY_CODE);
			/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
			/** 服务费代扣走 通联 通道 **/
			//服务费代扣走 快捷通  通道   2017-03-08
			loanFeeOffer.setPaySysNo(Bsxt2FeeConfig.PAY_SYS_NO);
		}else if(FundsSourcesTypeEnum.陆金所.getValue().equals(createFeeOfferVo.getFundsSources())){
		    /** 业务系统编码（由TPP2.0平台提供统一分配） **/
            loanFeeOffer.setBizSysNo(LufaxFeeConfig.BIZ_SYS_NO);
		    /** 信息类别编码（TPP2.0平台提供统一分配） **/
            loanFeeOffer.setInfoCategoryCode(LufaxFeeConfig.INFO_CATEGORY_CODE);
		    //① 农业银行通过通联划扣，划扣到通联商户号；②其他银行通过快捷通，划扣到快捷通商户号；
            loanFeeOffer.setPaySysNo(LufaxFeeConfig.PAY_SYS_NO);// 2.0属性
        }else if(FundsSourcesTypeEnum.渤海2.getValue().equals(createFeeOfferVo.getFundsSources())) {
			/** 业务系统编码（由TPP2.0平台提供统一分配） **/
			loanFeeOffer.setBizSysNo(Bh2FeeConfig.BIZ_SYS_NO);
			/** 信息类别编码（TPP2.0平台提供统一分配） **/
			loanFeeOffer.setInfoCategoryCode(Bh2FeeConfig.INFO_CATEGORY_CODE);
			/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
			/** 服务费代扣走 通联 通道 **/
			//服务费代扣走  通道   2017-04-28
			loanFeeOffer.setPaySysNo(Bh2FeeConfig.PAY_SYS_NO);
		}else if(FundsSourcesTypeEnum.华瑞渤海.getValue().equals(createFeeOfferVo.getFundsSources())){
			/** 业务系统编码（由TPP2.0平台提供统一分配） **/
			loanFeeOffer.setBizSysNo(HrbhFeeConfig.BIZ_SYS_NO);
			/** 信息类别编码（TPP2.0平台提供统一分配） **/
			loanFeeOffer.setInfoCategoryCode(HrbhFeeConfig.INFO_CATEGORY_CODE);
			/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
			/** 服务费代扣走 通联 通道 **/
			//服务费代扣走  通道   2017-04-18
			loanFeeOffer.setPaySysNo(HrbhFeeConfig.PAY_SYS_NO);
		}else{
			logger.warn("====合同来源为空====");
			throw new PlatformException(ResponseEnum.FULL_MSG, "loanId：" + loanId + " 合同来源不能为空 ");
		}
		
		/** 网点号 **/
		loanFeeOffer.setOrgan(comOrganization.getOrgCode());
		/** 报盘状态 **/
		loanFeeOffer.setState(OfferStateEnum.未报盘.getValue());
		/** 预留备注信息 **/
		loanFeeOffer.setMemo("");
		loanFeeOfferDaoImpl.insert(loanFeeOffer);

		return loanFeeOffer;
	}

	@Override
	public LoanFeeOffer updateOffer(LoanFeeOffer loanFeeOffer) {
		loanFeeOfferDaoImpl.update(loanFeeOffer);
		return loanFeeOffer;
	}

	@Override
	public Pager searchLoanFeeOfferWithPgByMap(Map<String, Object> params) {
		return loanFeeOfferDaoImpl.searchLoanFeeOfferWithPgByMap(params);
	}

	@Override
	public int getOfferCount(Long feeId, String type, Date offerDate) {
		if (Strings.isEmpty(feeId)) {
			return 0;
		}
		LoanFeeOffer loanFeeOffer = new LoanFeeOffer();
		loanFeeOffer.setFeeId(feeId);
		loanFeeOffer.setType(type);
		loanFeeOffer.setOfferDate(offerDate);
		return findListByVo(loanFeeOffer).size();
	}
}
