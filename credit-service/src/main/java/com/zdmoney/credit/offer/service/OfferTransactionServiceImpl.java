package com.zdmoney.credit.offer.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.OfferTrxCodeEnum;
import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.TppRealPaySysNoEnum;
import com.zdmoney.credit.common.exception.OfferBackException;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.vo.TppCallBackData;
import com.zdmoney.credit.common.vo.TppCallBackData20;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferBankDicDao;
import com.zdmoney.credit.offer.dao.pub.IOfferInfoDao;
import com.zdmoney.credit.offer.dao.pub.IOfferTransactionDao;
import com.zdmoney.credit.offer.domain.OfferBankDic;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.BaseMessage;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISerialNoService;
import com.zdmoney.credit.system.service.pub.ISysConflictPreventionService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zendaimoney.thirdpp.common.enums.ThirdType;
import com.zendaimoney.thirdpp.rmi.service.request.ThirdPaymentService;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.CollectReqVo;
import com.zendaimoney.thirdpp.vo.RequestDetailVo;
import com.zendaimoney.thirdpp.vo.RequestVo;
import com.zendaimoney.thirdpp.vo.biz.BizTransferRequestVo;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;
import com.zendaimoney.thirdpp.vo.expand.ChinaUnionpay;
import com.zendaimoney.thirdpp.vo.expand.Fuiou;

/**
 * 
 * @author 00232949
 *
 */
@Service
@Transactional
public class OfferTransactionServiceImpl implements IOfferTransactionService{
	
	private static Logger logger = Logger.getLogger(OfferTransactionServiceImpl.class);
	
	@Autowired
	private IOfferCreateService offerCreateService;
	@Autowired
	private IOfferTransactionDao offerTransactionDao;
	@Autowired
	private IOfferInfoDao offerInfoDao;
	@Autowired
	private IOfferBankDicDao offerBankDicDao;
	@Autowired
	private IOfferInfoService offerInfoService;
	
	@Autowired(required = false)
	private ThirdPaymentService thirdPaymentService;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	private ISequencesService sequencesServiceImpl;
	
	@Autowired
	private ISerialNoService serialNoService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IAfterLoanService afterLoanService;
	@Autowired
	private IOfferRepayInfoService offerRepayInfoService;
	@Autowired
	private ISysConflictPreventionService sysConflictPreventionService;
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	@Autowired
	private IComOrganizationDao comOrganizationDao;
	@Autowired
	ILoanInitialInfoDao loanInitialInfoDao;//债权初始信息操作DAO
	@Autowired
	IComEmployeeDao comEmployeeDao;
	@Autowired
	IBaseMessageService  baseMessageServiceImpl;
	@Autowired
	IPersonInfoService personInfoService;
	
	
	private Map<String,String> bankMap = null;
	
	
	@Override
	public List<OfferTransaction> getWeiHuiPanTraByloan(Long loanId) {
		List<OfferTransaction> offerTransaction = offerTransactionDao.findByStateAndLoan(OfferTransactionStateEnum.已发送,loanId);
		return offerTransaction;
	}



	/**
	 * 设置Tpp对象,创建交易流水
	 * @param requestVo
	 * @param offerInfo
	 * @param tppType
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public OfferTransaction setRequestDetailVo(RequestVo requestVo, OfferInfo offerInfo, ThirdPartyType tppType) {
		
		OfferTransaction offerTra = buildOfferTransaction(offerInfo,"TPP1.0");
		if(offerTra!=null){
			//组装发送对象
			RequestDetailVo requestDetailVo = buildBizTransferRequestVo(offerInfo,offerTra, tppType);
			if(requestDetailVo==null){
				//如果为空循环下一个
				logger.warn("setRequestDetailVo异常，OfferTransaction或requestDetailVo为null！");
				throw new RuntimeException("setRequestDetailVo异常，OfferTransaction或requestDetailVo为null！");
			}
			
			requestVo.getRequestDetailInfo().getRequestDetails().add(requestDetailVo);
		}
		
		return offerTra;
//		offerInfo.setOfferTransaction(offerTra);//保留，用于发送完更新状态
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public OfferTransaction setRequestDetailVo20(com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo requestVo, OfferInfo offerInfo) {
		OfferTransaction offerTra = buildOfferTransaction(offerInfo,"TPP2.0");
		// 如果是线下还款的借款，则不组装发送TPP的数据对象
		if(offerTra!=null){
			//组装发送对象
			CollectReqVo collectReqVo = buildBizTransferRequestVo20(offerInfo,offerTra);
			if(collectReqVo==null){
				//如果为空循环下一个
				logger.warn("setRequestDetailVo20异常，collectReqVo为null！");
				throw new RuntimeException("setRequestDetailVo20异常，collectReqVo为null！");
			}
			requestVo.getList().add(collectReqVo);
		}
		
		return offerTra;
		
	}
	
	private CollectReqVo buildBizTransferRequestVo20(OfferInfo offer,
			OfferTransaction offerTra) {
		CollectReqVo collectReqVo = null;
		try {
			//获得银行对账字典表
			if(bankMap==null){
				bankMap = new HashMap<String,String>();
				List<OfferBankDic> bankList = offerBankDicDao.findAllList();
				for(OfferBankDic offerBankDic : bankList){
					bankMap.put(offerBankDic.getCode().trim(), offerBankDic.getTppBankCode());
				}
			}
			
			collectReqVo = new CollectReqVo();
			collectReqVo.setPaySysNo(offer.getPaySysNo());
//			collectReqVo.setBizSysAccountNo("");
//			collectReqVo.setZengdaiAccountNo("10000001");
//			collectReqVo.setReceiverAccountNo("10000010"); // 收款方账户编号
//			collectReqVo.setReveiverAccountName("证大财富");// 收款方姓名
			collectReqVo.setPayerName(offer.getName());// 付款方姓名
			collectReqVo.setPayerBankCardNo(offer.getBankAcct());// 付款方银行卡号
//			collectReqVo.setPayerBankCardType("1");// 付款方银行卡类型 1.借记卡，2信用卡
			collectReqVo.setPayerIdType("0");// 付款方证件类型 0=身份证
			collectReqVo.setPayerId(offer.getIdnum());// 付款方证件号
			collectReqVo.setPayerBankCode(bankMap.get(offer.getBankCode().trim()));// 付款方银行编码
//			collectReqVo.setPayerSubBankCode("10023942");// 付款方银行支行行号
			collectReqVo.setCurrency("0");// 币种(0人民币)
			collectReqVo.setAmount(offerTra.getPayAmount());// 金额
			if("实时划扣".equals(offer.getType())){
				if("999".equals(offer.getPaySysNo())){
					collectReqVo.setSpare1("0");//不选择划扣通道，划扣通道设置为999
				}else{
					collectReqVo.setSpare1("1");//选择划扣通道，
				}
			}
			collectReqVo.setBizFlow(offerTra.getTrxSerialNo());// 业务流水号
			collectReqVo.setIsNeedSpilt(1); //报盘是否需要拆单(限额超过后)0不需要拆单1需要拆单
			collectReqVo.setPayerMobile(offer.getTel());
			if(offer.getPaySysNo().equals(ThirdType.YONGYOUUNIONPAY.getCode())){
	        	String xintuoId = sysParamDefineService.getSysParamValueCache("codeHelper", "xintuoId");
	        	collectReqVo.setPayerAccountNo(offer.getCustId()); //信托账号，信贷端传值
	        	collectReqVo.setReceiverAccountNo(xintuoId);; //
	        }
			LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(offer.getLoanId());
			if(offer.getFundsSources().equals(FundsSourcesTypeEnum.捞财宝.toString())){
				//报盘是否需要拆单(限额超过后)0不需要拆单1需要拆单 
				collectReqVo.setPayerAccountNo(loanInitialInfo.getAppNo());
				if(offer.getPaySysNo().equals(TppRealPaySysNoEnum.爱特代扣.getCode())){
					collectReqVo.setIsNeedSpilt(0); //爱特代扣  不拆单
				}else{
					collectReqVo.setIsNeedSpilt(1); //通联代扣 可拆单
				}
			}
	        
		} catch (Exception e) {
			logger.error("buildBizTransferRequestVo20出错，此次报盘不发送！offerInfoId："+offer.getId(),e);
			return null;
		}
		
		return collectReqVo;
	}



	/**
	 * 创建报盘流水
	 * @param offerInfo
	 * @return
	 */
	private OfferTransaction buildOfferTransaction(OfferInfo offerInfo,String tppVer) {
		//检查银行卡是否符合要求
		if(offerInfo.getBankAcct().length()<10 || offerInfo.getBankAcct().length()>25 ){
			logger.warn("TPP卡号(payBankId)的字符长度范围为:10 至 25个字符,offinfo长度："
					+offerInfo.getBankAcct().length()+"，该条不参与报盘！offinfoId："+offerInfo.getId());
			throw new RuntimeException("TPP卡号(payBankId)的字符长度范围为:10 至 25个字符,offinfo长度："
					+offerInfo.getBankAcct().length()+"，该条不参与报盘！offinfoId："+offerInfo.getId());
		}
		if(!isNumeric(offerInfo.getBankAcct())){
			logger.warn("TPP卡号(payBankId)的输入值应全部为数字,该offInfo为："
					+offerInfo.getBankAcct()+"，该条不参与报盘！offinfoId："+offerInfo.getId());
			throw new RuntimeException("TPP卡号(payBankId)的输入值应全部为数字,该offInfo为："
					+offerInfo.getBankAcct()+"，该条不参与报盘！offinfoId："+offerInfo.getId());
		}
		
		//创建报盘流水信息
		OfferTransaction offerTra  = buildOfferTransactionByOfferInfo(offerInfo,tppVer);
		if(offerTra==null){
			//如果为空循环下一个
			logger.warn("buildOfferTransactionByOfferInfo异常，OfferTransaction为null！该条不参与报盘！offinfoId："+offerInfo.getId());
			throw new RuntimeException("buildOfferTransactionByOfferInfo异常，OfferTransaction为null！该条不参与报盘！offinfoId："+offerInfo.getId());
		}
		return offerTra;
	}



	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	/**
	 * 更新状态
	 * @param offerList
	 * @param offerState
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateOfferStateToYibaopan(List<OfferTransaction> list) {
		
		for(OfferTransaction offerTransaction : list){
			offerTransaction.setTrxState(OfferTransactionStateEnum.已发送.getValue());
			offerTransactionDao.update(offerTransaction);
			
			OfferInfo offerInfo = new OfferInfo();
			offerInfo.setId(offerTransaction.getOfferId());
			offerInfo.setState(OfferStateEnum.已报盘.getValue());
			offerInfo.setReturnCode(" ");//清空上次的code
			offerInfoDao.updateByPrimaryKeySelective(offerInfo);
			
		}
		
//		offerInfo.setState(OfferStateEnum.已报盘.getValue());
//		offerInfo.getOfferTransaction().setTrxState(OfferTransactionStateEnum.已发送.getValue());
//		
//		offerInfoDao.update(offerInfo);
//		offerTransactionDao.update(offerInfo.getOfferTransaction());
	}

	/**
	 * 创建tpp报盘对象，如果异常返回null
	 * @param offer
	 * @param offerTra 
	 * @param thirdPartyType
	 * @return
	 */
	private RequestDetailVo buildBizTransferRequestVo(OfferInfo offer,OfferTransaction offerTra, ThirdPartyType thirdPartyType) {
		BizTransferRequestVo transferRequestVo = null;
		try {
			//获得银行对账字典表
			if(bankMap==null){
				bankMap = new HashMap<String,String>();
				List<OfferBankDic> bankList = offerBankDicDao.findAllList();
				for(OfferBankDic offerBankDic : bankList){
					bankMap.put(offerBankDic.getCode().trim(), offerBankDic.getTppBankCode());
				}
			}
			
		 	transferRequestVo = new BizTransferRequestVo();
		 	
	        transferRequestVo.setPayBank(bankMap.get(offer.getBankCode().trim())); //付方银行代码     //BankDic.findByCode(offer.getOrBankCode().trim()).getTppBankCode().trim()
	        transferRequestVo.setPayName(offer.getName());//付方账户名
	        //transferRequestVo.setPayBank(offer.getOrBankCode());//付方银行名称
	        transferRequestVo.setPayBankId(offer.getBankAcct());//付方银行卡号
	        transferRequestVo.setPayCardType(new Integer(1));//付方证件类型
	        transferRequestVo.setPayCardId(offer.getIdnum());//付方证件号码
	        transferRequestVo.setAmount(offerTra.getPayAmount());//金额
//		        transferRequestVo.setSpare1(tppCallbackAddress);//回调地址

	        transferRequestVo.setOrdNo(offerTra.getTrxSerialNo());//请求流水,保证唯一ss
	        transferRequestVo.setPriority(offer.getPriority());
	        //以下信息为富有必填字段，通联可以不进行填写
	        if(thirdPartyType.equals(ThirdPartyType.Fuiou)){
	            transferRequestVo.getExpandProperties().put(Fuiou.BizTransferReq.merdt,new SimpleDateFormat("yyyyMMdd").format(new Date()));//请求日期
	            transferRequestVo.getExpandProperties().put(Fuiou.BizTransferReq.entseq,""); //企业流水号
	            transferRequestVo.getExpandProperties().put(Fuiou.BizTransferReq.mobile,offer.getTel()); //手机号码
	        }
	        //以下为银联必填字段
	        if(thirdPartyType.equals(ThirdPartyType.SHUnionpay)){
	            transferRequestVo.getExpandProperties().put(ChinaUnionpay.BizTransferReq.transDate, new SimpleDateFormat("yyyyMMdd").format(new Date()));  //商户日期 exp:"20100408"
	            transferRequestVo.getExpandProperties().put(ChinaUnionpay.BizTransferReq.cardType, "0");   //卡折标志 0卡 1折
	            transferRequestVo.getExpandProperties().put(ChinaUnionpay.BizTransferReq.curyId, "156");    // 货币类型 156人民币
	        }

	        if(thirdPartyType.equals(ThirdPartyType.YYoupay)){
	        	String xintuoId = sysParamDefineService.getSysParamValueCache("codeHelper", "xintuoId");
//	            def codeHelper = CodeHelper.findAll()[0];
	            transferRequestVo.setRecBankId(xintuoId); //信托账号，信贷端传值
	            transferRequestVo.setPayBankId(offer.getCustId()); //该笔还款用户ID
	        }
	        transferRequestVo.setCreateDate(new Date());
	        
		} catch (Exception e) {
			logger.error("buildBizTransferRequestVo出错，此次自动报盘不发送！offerInfoId："+offer.getId(),e);
			return null;
		}
		
        return  transferRequestVo;
	}

	/**
	 * 根据报盘文件，生成报盘交易,如果存在未回盘情况,或者发生异常返回null
	 * @param offer
	 * @return
	 */
	private OfferTransaction buildOfferTransactionByOfferInfo(OfferInfo offer,String tppVer) {
		OfferTransaction offerTransaction = null;
		
		try {
			// 查看之前的报盘流水是否未回盘
			List<OfferTransaction> list = getWeiHuiPanTraByloan(offer.getLoanId());
			if (list != null && list.size() > 0) {
				logger.warn("OfferInfo 存在未回盘情况，此次自动报盘不发送！ LoanId："+offer.getLoanId());
				return null;//有未回盘，返回null
			}
			offerTransaction = new OfferTransaction();
			offerTransaction.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_TRANSACTION));
			offerTransaction.setCreateTime(new Date());
			offerTransaction.setCreator("SYSTEM");
			offerTransaction.setLoanId(offer.getLoanId());
			offerTransaction.setOfferId(offer.getId());
			/** 报盘金额 **/
			BigDecimal payAmount = offer.getOfferAmount();
			if (payAmount.compareTo(BigDecimal.ZERO) <= 0) {
				logger.error("创建报盘交易出错，offerInfoid："+offer.getId() + "报盘金额小于等于0!");
				return null;
			}
			offerTransaction.setPayAmount(payAmount);
			offerTransaction.setReqReceiver(tppVer);
			offerTransaction.setReqSender("ZDCREDIT");//暂时这么写，将来有常量了拿出去
			offerTransaction.setReqTime(new Date());
			offerTransaction.setTrxCode(OfferTrxCodeEnum.自动划扣.getValue());
			offerTransaction.setTrxSerialNo(String.valueOf(offerTransaction.getId()));//因TPP只接收12位，这里直接送主键。serialNoService.generateTrxSerialNo()
			offerTransaction.setTrxState(OfferTransactionStateEnum.未发送.getValue());
			offerTransactionDao.insert(offerTransaction);
		} catch (Exception e) {
			logger.error("创建报盘交易出错，offerInfoid："+offer.getId(), e);
			return null;
		}
		return offerTransaction;
	}



	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Boolean executeOfferBack(TppCallBackData callBackData) {
		
		OfferTransaction offerTransaction = checkBackOfferTransaction(callBackData.getOrderNo()); //检查合法性，通过返回交易流水
		//得到报盘信息
		OfferInfo offerInfo = offerInfoService.findOfferById(offerTransaction.getOfferId());
		if(offerInfo==null){
			throw new OfferBackException("订单号:" + callBackData.getOrderNo() + "未找到offerInfo，回盘处理失败!");
		}
		
		try {
			//1插入防冲突表，预防并发
			sysConflictPreventionService.saveNow("OfferBack"+ callBackData.getOrderNo().trim());
		} catch (Exception e) {
			logger.error("订单号:" + callBackData.getOrderNo() + "插入冲突表报错，可能已经回盘完成,不再处理!");
			throw new OfferBackException("订单号:" + callBackData.getOrderNo() + "插入冲突表报错，可能已经回盘完成,不再处理!");
		}
		//2跟新交易流水
		offerTransaction.setRspReceiveTime(new Date());
		offerTransaction.setUpdateTime(offerTransaction.getRspReceiveTime());
		offerTransaction.setRdoTime(offerTransaction.getRspReceiveTime());
		offerTransaction.setRtnCode(callBackData.getReturnCode());
		offerTransaction.setRtnInfo(callBackData.getReturnInfo());
		offerTransaction.setPaySysNoReal(offerInfo.getPaySysNo());
		try {
			offerTransaction.setRspLogId(Long.parseLong(callBackData.getMsgInId()));
		} catch (Exception e) {
			logger.error("设置RspLogId失败:"+e.getMessage());
		}
		
		
		//3如果当前这条数据是扣款成功,那么则进行回款逻辑
		if(callBackData.getReturnCode().equals(TPPHelper.RESULT_SUCCESS_CODE)) {
			offerTransaction.setActualAmount(offerTransaction.getPayAmount());//1.0没有成功金额返回，只在成功时设置实际成功金额
			try {
				//这里实时记账
				afterLoanService.accountingByTransaction(offerTransaction);
			} catch (Exception e) {
				logger.error("订单号:" + callBackData.getOrderNo() + "，记账处理失败!",e);
			}
			
            offerTransaction.setTrxState(OfferTransactionStateEnum.扣款成功.getValue());
            offerInfo.setState(OfferStateEnum.已回盘全额.getValue());
        }else{
        	offerTransaction.setActualAmount(new BigDecimal(0));//失败时放0
        	offerTransaction.setTrxState(OfferTransactionStateEnum.扣款失败.getValue());
        	offerInfo.setState(OfferStateEnum.已回盘非全额.getValue());
        }
		
		//如果是实时划扣，自动再创建一次自动报盘
		if(OfferTypeEnum.实时划扣.getValue().equals(offerInfo.getType())){
			try {//实时划扣的回盘特殊操作，关闭原来的自动，新建一条
				executeOfferBackRealTime(offerInfo);
			} catch (Exception e) {
				logger.error("实时划扣回盘，创建自动报盘时出错！",e);
			}
		}
		
		offerTransactionDao.update(offerTransaction);
		//4跟新报盘文件
		offerInfo.setReturnCode(callBackData.getReturnCode());
		offerInfo.setCause(callBackData.getReturnInfo());
//		offerInfo.setReturnFileId(offerFileInstance);
		offerInfo.setActualAmount(offerTransaction.getActualAmount());
		offerInfoService.updateOffer(offerInfo);
	
		//5特殊还款检测，如果有重建报盘文件
		try {
			executeOfferBackSpecial(offerInfo);
		} catch (Exception e) {
			logger.error("实时划扣回盘，创建自动报盘时出错！",e);
		}
		
		return true;
	}

	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Boolean executeOfferBack20(TppCallBackData20 callBackData) {
		OfferTransaction offerTransaction = checkBackOfferTransaction(callBackData.getOrderNo()); //检查合法性，通过返回交易流水
		//得到报盘信息
		OfferInfo offerInfo = offerInfoService.findOfferById(offerTransaction.getOfferId());
		if(offerInfo==null){
			throw new OfferBackException("订单号:" + callBackData.getOrderNo() + "未找到offerInfo，回盘处理失败!");
		}
		
		try {
			//1插入防冲突表，预防并发
			sysConflictPreventionService.saveNow("OfferBack"+ callBackData.getOrderNo().trim());
		} catch (Exception e) {
			logger.error("订单号:" + callBackData.getOrderNo() + "插入冲突表报错，可能已经回盘完成,不再处理!");
			throw new OfferBackException("订单号:" + callBackData.getOrderNo() + "插入冲突表报错，可能已经回盘完成,不再处理!");
		}
		
		//2跟新交易流水
		offerTransaction.setRspReceiveTime(new Date());
		offerTransaction.setUpdateTime(offerTransaction.getRspReceiveTime());
		offerTransaction.setRdoTime(offerTransaction.getRspReceiveTime());
		offerTransaction.setRtnCode(callBackData.getReturnCode());
		offerTransaction.setRtnInfo(callBackData.getReturnInfo());
		offerTransaction.setMemo(callBackData.getFailReason());
		offerTransaction.setPaySysNoReal(callBackData.getPaySysNo());
		offerTransaction.setMerId(callBackData.getMerId());
		/** 成功代扣金额 **/
		BigDecimal successAmount = null;
		try {
			offerTransaction.setRspLogId(Long.parseLong(callBackData.getMsgInId()));
		} catch (Exception e) {
			logger.error("设置RspLogId失败:"+e.getMessage());
		}
		
		//3如果当前这条数据是扣款成功,那么则进行回款逻辑
		if(callBackData.getReturnCode().equals(TPPHelper.RESULT_SUCCESS_CODE)) {
			successAmount = new BigDecimal(callBackData.getSuccessAmount());
			offerTransaction.setActualAmount(successAmount);
			try {
				//这里实时记账
				afterLoanService.accountingByTransaction(offerTransaction);
			} catch (Exception e) {
				logger.error("订单号:" + callBackData.getOrderNo() + "，记账处理失败!",e);
			}
			
            offerTransaction.setTrxState(OfferTransactionStateEnum.扣款成功.getValue());
            offerInfo.setState(OfferStateEnum.已回盘全额.getValue());
            
        }else if(callBackData.getReturnCode().equals(TPPHelper.RESULT_BFSUCCESS_CODE)){//部分成功
        	successAmount = new BigDecimal(callBackData.getSuccessAmount());
        	offerTransaction.setActualAmount(successAmount);
			try {
				//这里实时记账
				afterLoanService.accountingByTransaction(offerTransaction);
			} catch (Exception e) {
				logger.error("订单号:" + callBackData.getOrderNo() + "，记账处理失败!",e);
			}
            offerTransaction.setTrxState(OfferTransactionStateEnum.扣款成功.getValue());
            offerInfo.setState(OfferStateEnum.已回盘非全额.getValue());
        	
        }else{
        	/** 失败时放0 **/
        	successAmount = new BigDecimal(0);
        	offerTransaction.setActualAmount(successAmount);
        	offerTransaction.setTrxState(OfferTransactionStateEnum.扣款失败.getValue());
        	offerInfo.setState(OfferStateEnum.已回盘非全额.getValue());
        }
		
		//如果是实时划扣，自动再创建一次自动报盘
//		if(OfferTypeEnum.实时划扣.getValue().equals(offerInfo.getType())){
//			try {//实时划扣的回盘特殊操作，关闭原来的自动，新建一条
//				executeOfferBackRealTime(offerInfo);
//			} catch (Exception e) {
//				logger.error("实时划扣回盘，创建自动报盘时出错！",e);
//			}
//		}
		
		offerTransactionDao.update(offerTransaction);
		//4跟新报盘文件
		offerInfo.setReturnCode(callBackData.getReturnCode());
		offerInfo.setCause(callBackData.getReturnInfo());
//		offerInfo.setReturnFileId(offerFileInstance);
		/** 更新报盘金额 报盘金额=报盘金额-本次成功扣款金额 **/
		offerInfo.setOfferAmount(offerInfo.getOfferAmount().subtract(successAmount));
		/** 更新成功扣款总金额 **/
		offerInfo.setActualAmount(offerInfo.getActualAmount().add(successAmount));
		/** tpp实际划扣通道 **/
		offerInfo.setPaySysNoReal(callBackData.getPaySysNo());
		offerInfoService.updateOffer(offerInfo);
	
		//5特殊还款检测，如果有重建报盘文件
		try {
			executeOfferBackSpecial(offerInfo);
		} catch (Exception e) {
			logger.error("实时划扣回盘，创建自动报盘时出错！",e);
		}
		
		//跨日回盘 添加到消息中心
		addBaseMessage(offerInfo.getOfferDate(),offerInfo.getLoanId());		
		return true;
	}
	/**
	 * 跨日回盘 添加到消息中心
	 * @param offerDate
	 * @param loanId
	 */
	public void addBaseMessage(Date offerDate,Long loanId) {
		if(Dates.compareTo(offerDate, Dates.getCurrDate())<0){
			BaseMessage baseMessage=new BaseMessage();
			VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
			PersonInfo personInfo = personInfoService.findById(vLoanInfo.getBorrowerId());
			Long orgId = vLoanInfo.getSalesDepartmentId();
			List<ComEmployee> list=comEmployeeDao.finListByOrgId(orgId);
			if(CollectionUtils.isNotEmpty(list)){
				for (int i = 0; i < list.size(); i++) {
					ComEmployee comEmployee = list.get(i);
					baseMessage.setReceiver(comEmployee.getId());//收件人
					baseMessage.setTitle("回盘提醒");
					baseMessage.setContent("客户"+personInfo.getName()+"跨日回盘，如涉及罚息减免，请及时申请，谢谢！");
					baseMessage.setState("1");//未读
					baseMessage.setType("2");//类型（0系统通知，1信件,2回盘提醒）
					baseMessage.setSendTime(new Date());
					baseMessageServiceImpl.inserBaseMessage(baseMessage);
				}
			}
		}
	}
	/**
	 * 特殊还款检测，如果有重建报盘文件
	 * @param offerInfo
	 */
	private void executeOfferBackSpecial(OfferInfo offerInfo) {
		if(IOfferInfoService.MOMO_REBUILD.equals(offerInfo.getSpt1())){
			if(OfferStateEnum.已回盘非全额.getValue().equals(offerInfo.getState()) 
					|| OfferStateEnum.未报盘.getValue().equals(offerInfo.getState())){
				offerInfo.setState(OfferStateEnum.已关闭.getValue());
				offerInfo.setUpdateTime(new Date());
				offerInfo.setUpdator("system");
				offerInfo.setMemo("检测到特殊还款申请，该报盘关闭从新生成");
				offerInfoService.updateOffer(offerInfo);
			}
			VLoanInfo loan = vLoanInfoService.findByLoanId(offerInfo.getLoanId());
			ComOrganization organization = comOrganizationDao.get(loan.getSalesDepartmentId());
			OfferInfo offerInfonew = offerInfoService.createOffer(loan, organization,IOfferInfoService.TIQIANJIEQING, null,null);//优先级按特殊还款
			if(offerInfonew==null){
				logger.warn("回盘后，特殊还款检测创建新的自动报盘失败！可能因报盘金额为0");
			}
		}
	}



	/**
	 * 检查回盘的信息是否合法，如果合法返回交易明细
	 * @param callBackData
	 */
	public OfferTransaction checkBackOfferTransaction(String orderNo) {
		OfferTransaction offerTransaction = offerTransactionDao.findByTrxSerialNo(orderNo);
		if(offerTransaction==null){
			throw  new OfferBackException("找不到订单号:" + orderNo + ",所对应的报盘数据!");
		}
		
		if(OfferTransactionStateEnum.扣款成功.getValue().equals(offerTransaction.getTrxState()) ||
				OfferTransactionStateEnum.扣款失败.getValue().equals(offerTransaction.getTrxState())){
			throw  new OfferBackException("订单号:" + orderNo + "已经回盘完成,不再处理!");
		}
		return offerTransaction;
		
	}



	/**
	 * 实时划扣成功的回盘特殊操作，关闭原来的自动报盘，新建一条
	 * @param offerInfo
	 */
	private void executeOfferBackRealTime(OfferInfo offerInfo) {
		VLoanInfo loan = vLoanInfoService.findByLoanId(offerInfo.getLoanId());
		
//		offerCreateService.closeOfferInfo(loan);发送时已经关闭
		ComOrganization organization = comOrganizationDao.get(loan.getSalesDepartmentId());
		OfferInfo offerInfonew = offerInfoService.createOffer(loan, organization,IOfferInfoService.YUQI, null,null);//优先级按逾期的
		if(offerInfonew==null){
			logger.warn("实时划扣回盘后，创建新的自动报盘失败！可能因报盘金额为0");
		}
	}



	public void setThirdPaymentService(ThirdPaymentService thirdPaymentService) {
		this.thirdPaymentService = thirdPaymentService;
	}



	@Override
	public Pager searchOfferInfoWithPgByMap(Map<String, Object> params) {
		return offerInfoDao.findWithPgByMap(params);
	}
	
	@Override
	public Pager searchOfferTransactionInfoWithPgByMap(Map<String, Object> params) {
		return offerTransactionDao.searchOfferTransactionInfoWithPgByMap(params);
	}

	@Override
	public Pager findWithPg(OfferTransaction offerTransaction) {
		return offerTransactionDao.findWithPg(offerTransaction);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateErrorMsgNow(String msg, OfferInfo offerInfo,
			OfferTransaction offerTran) {
		if(offerInfo!=null){
			offerInfo.setMemo(msg);
	    	offerInfoDao.updateByPrimaryKeySelective(offerInfo);
		}
		if(offerTran!=null){
			offerTran.setMemo(msg);
			offerTran.setRtnInfo(msg);
			offerTransactionDao.update(offerTran);
		}
		
	}


	
	
}
