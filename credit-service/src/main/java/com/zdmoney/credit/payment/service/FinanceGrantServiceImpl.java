package com.zdmoney.credit.payment.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.dao.pub.IRepayBusLogDao;
import com.zdmoney.credit.bsyh.service.pub.IRepayResultNotifyLogService;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.TradeKindEnum;
import com.zdmoney.credit.common.constant.grant.FinanceGrantEnum;
import com.zdmoney.credit.common.constant.grant.TppGrantEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.RequestInfo;
import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.fee.offer.LoanFeeUtil;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100005Vo;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyAccountWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyBorrowerWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyLoanWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyRelationWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2101Vo;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2102Vo;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_5201Vo;
import com.zdmoney.credit.framework.vo.xdcore.entity.PayPlanListEntity;
import com.zdmoney.credit.framework.vo.xdcore.input.BYXY0016Vo;
import com.zdmoney.credit.grant.dao.pub.IDebitAccountInfoDao;
import com.zdmoney.credit.grant.dao.pub.ILoanBaseGrantDao;
import com.zdmoney.credit.grant.dao.pub.ILoanOfferInfoDao;
import com.zdmoney.credit.grant.domain.DebitAccountInfo;
import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.grant.domain.LoanOfferInfo;
import com.zdmoney.credit.grant.vo.GrantAccountVo;
import com.zdmoney.credit.grant.vo.GrantApplyVo;
import com.zdmoney.credit.grant.vo.GrantBorRelaPerVo;
import com.zdmoney.credit.grant.vo.GrantBorrowPersonVo;
import com.zdmoney.credit.grant.vo.GrantRepaymentDetailVo;
import com.zdmoney.credit.grant.vo.LoanBaseGrantVo;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBsbMappingService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.VloanPersonInfo;
import com.zdmoney.credit.offer.dao.pub.IOfferBankDicDao;
import com.zdmoney.credit.offer.domain.OfferBankDic;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.domain.PayChannel;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.service.pub.IPayChannelService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zendaimoney.thirdpp.common.enums.BizSys;
import com.zendaimoney.thirdpp.common.vo.Response;
import com.zendaimoney.thirdpp.trade.pub.service.IBrokerTradeService;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.PayReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.QueryReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo;

/**
 * Created by ym10094 on 2016/11/10.
 */
@Service
public class FinanceGrantServiceImpl implements IFinanceGrantService {
    
    private static final Logger log = LoggerFactory.getLogger(FinanceGrantServiceImpl.class);
    
    public static String msg="";
    public static String code = "";
    public static String backActive ="回退";
    public static String grantActive ="放款";
    public static final String 华瑞渤海实时代付 ="10075";
    public static final String 渤海2实时代付 ="10076";
    @Autowired
    private ILoanBaseGrantDao loanBaseGrantDao;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private LoanFeeUtil loanFeeUtil;
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    @Autowired
   	private IPersonInfoService personInfoService;
    @Autowired
    private ILoanBsbMappingService loanBsbMappingService;
    @Autowired
    private ILoanBaseDao loanbaseDao;
    @Autowired
    private ILoanInitialInfoDao loanInitialInfoDao;
    @Autowired
    private ILoanProductDao loanProductDao;
    @Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;//还款计划操作DAO
    @Autowired
    private ILoanBsbMappingDao loanBsbMappingDao;
    @Autowired
    private ILoanCoreService loanCoreService;
    @Autowired
    private IRepayResultNotifyLogService autoDebitRepayResultService;
    @Autowired
    @Qualifier("offerRepayInfoServiceImpl")
    IOfferRepayInfoService offerRepayInfoServiceImpl;
    @Autowired @Qualifier("loanSpecialRepaymentServiceImpl")
	ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
    @Autowired
	private ILoanLedgerService loanLedgerServiceImpl;
    @Autowired
	IAfterLoanService  afterLoanService;
    @Autowired
   	IRepayBusLogDao  repayBusLogDao;
    @Autowired
    IDebitAccountInfoDao debitAccountInfoDao;
    @Autowired
    private IRepayResultNotifyLogService repayResultNotifyLogService;
    @Autowired 
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
    @Autowired
    private IThirdLineOfferService thirdLineOfferService;
    @Autowired
	private IBrokerTradeService brokerTradeConsumer;
    @Value("${gateway.interface.url}")
    public String gatewayInterfaceUrl;
    @Value("${bstogateway.interface.url}")
    public String bstogatewayInterfaceUrl;
    @Autowired
	private ILoanOfferInfoDao loanOfferInfoDao;
    @Autowired
	private ILoanBankDao loanBankDao;
    @Autowired
	private IOfferBankDicDao offerBankDicDao;   
    @Autowired 
	IPayChannelService payChannelService;
    @Override
    public String grantStateConverter(String state) {
        if ("1".equals(state)) {
            return FinanceGrantEnum.放款成功.getCode();
        }
        if ("2".equals(state)) {
            return FinanceGrantEnum.放款失败.getCode();
        }
        return null;
    }

    @Override
    public Boolean isExitpactNo(String pactNo) {
        LoanBaseGrant loanBaseGrant = loanBaseGrantDao.findLoanBaseGrantcontractNum(pactNo, FinanceGrantEnum.申请中.getCode());
        if (loanBaseGrant == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSuccessGrant(Long geantApplyId) {
        LoanBaseGrant loanBaseGrant = loanBaseGrantDao.findLoanBaseGrantById(geantApplyId);
        if (loanBaseGrant == null || !FinanceGrantEnum.放款成功.getCode().equals(loanBaseGrant.getGrantState())){
            return false;
        }
        return true;
    }

    @Override
    public void updateGrantState(LoanBaseGrant loanBaseGrant) {
        //修改 loan_base_grant
        try {
            loanBaseGrantDao.update(loanBaseGrant);
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"更新申请状态异常："+e.getMessage());
        }
    }

    @Override
    public void disposeFinanceGrantPushResultService(String pactNo,String grantState,String dealDesc) {
        // 根据合同号查询处于申请中的放款记录
        LoanBaseGrant loanBaseGrant = this.findInTheApplyLoanBaseGrantByContractNum(pactNo);
        if (loanBaseGrant == null) {
            throw new PlatformException(ResponseEnum.FULL_MSG,
                    "Loan_Base_Grant表没有找到对应的记录：{'pactNo':'"+ pactNo+ "'}");
        }
        // 放款状态转换
        String state = this.grantStateConverter(grantState);
        if (Strings.isEmpty(state)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"请返回符合约定的放款状态！");
        }
        loanBaseGrant.setGrantState(state);
        loanBaseGrant.setGrantApplyFinishDate(new Date());
        loanBaseGrant.setRemark(dealDesc);
        this.updateGrantState(loanBaseGrant);
        // 如果放款成功，则生成服务费报盘、实时划扣服务费
        if (FinanceGrantEnum.放款成功.getCode().equals(state)) {
            VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanBaseGrant.getLoanId());
            loanFeeUtil.createFee(loanBaseGrant.getLoanId(),vLoanInfo.getFundsSources());
        }
    }

    /**
     * 根据合同编号查询处再申请中状态的放款申请（外贸2）
     * @param contractNum
     * @return
     */
    public LoanBaseGrant findInTheApplyLoanBaseGrantByContractNum(String contractNum){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("contractNum",contractNum);
        paramMap.put("grantState",FinanceGrantEnum.申请中.getCode());
        return loanBaseGrantDao.queryLoanGrantBaseRelateVloanInfo(paramMap);
    }

    public Pager queryFinanceGrantPage(Map<String, Object> paramMap) {
        // 设置查询通用参数
        this.setSearchParams(paramMap);
        return loanBaseGrantDao.queryFinanceGrantPage(paramMap);
    }

    @Override
    public boolean isApplyFinanceGrant(Long loanId) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("loanId", loanId);
    	List<LoanBaseGrant> list = loanBaseGrantDao.findListByMap(paramMap);
    	if(CollectionUtils.isEmpty(list)){
    		return true;
    	}
    	for (int i = 0; i < list.size(); i++) {
    		LoanBaseGrant loanBaseGrant=list.get(i);
    		if(FinanceGrantEnum.申请中.getCode().equals(loanBaseGrant.getGrantState()) || FinanceGrantEnum.放款成功.getCode().equals(loanBaseGrant.getGrantState())){
    			return false;
    		}
		}
        return true;
    }
    private Map<String,Object> setSearchCountParams(Long loanId){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("loanFlowStates",new String[] { LoanFlowStateEnum.财务放款.getValue()});
        paramMap.put("loanStates", new String[] { LoanStateEnum.通过.getValue()});
        return paramMap;
    }

    /**
     * 存在 未放款 或 放款失败的 记录
     * @param paramMap
     * @return
     */
    private boolean isNotGrantOrGrantFailCountBigZero(Map<String,Object> paramMap){
        paramMap.put("grantState",FinanceGrantEnum.未放款.getCode());
        if(loanBaseGrantDao.queryFinanceGrantInfosCount(paramMap) > 0){
            return true;
        }
        paramMap.put("grantState", FinanceGrantEnum.放款失败.getCode());
        if (loanBaseGrantDao.queryFinanceGrantInfosCount(paramMap) >0) {
            return true;
        }
        return false;
    }
    @Override
    public Workbook getFinanceGrantWorkBook(Map<String, Object> paramMap, String fileName) {
        Workbook workbook = null;
        // 设置查询通用参数
        this.setSearchParams(paramMap);
        List<LoanBaseGrantVo> loanBaseGrantVos = loanBaseGrantDao.exportFinanceGrantInfos(paramMap);
        if (CollectionUtils.isEmpty(loanBaseGrantVos)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
        }
        String sheetName = "Sheet1";
        try {
            this.setExportLoanBaseGrantVos(loanBaseGrantVos);
            workbook = ExcelExportUtil.createExcelByVo(fileName, this.getFGlabels(), this.getFGfields(), loanBaseGrantVos, sheetName);
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"创建财务放款.xls失败！");
        }
        return workbook;
    }
    
    /** 设置查询通用参数 **/
    private void setSearchParams(Map<String, Object> paramMap){
         // 合同來源必须是外贸2和包商银行
        paramMap.put("founsSources", new String[]{FundsSourcesTypeEnum.外贸2.getValue(),FundsSourcesTypeEnum.包商银行.getValue()
        		,FundsSourcesTypeEnum.外贸3.getValue(), FundsSourcesTypeEnum.陆金所.getValue(),FundsSourcesTypeEnum.渤海2.getValue(),FundsSourcesTypeEnum.华瑞渤海.getValue()});
        // 债权状态
        paramMap.put("loanStates", new String[] { LoanStateEnum.通过.getValue(),
                LoanStateEnum.正常.getValue(), LoanStateEnum.逾期.getValue(),
                LoanStateEnum.结清.getValue(), LoanStateEnum.预结清.getValue() });
        // 审批状态
        paramMap.put("loanFlowStates",new String[] { LoanFlowStateEnum.财务放款.getValue(),
                LoanFlowStateEnum.正常.getValue() });
    }

    @Override
    public void requestFinanceGrantApply(Long loanId) {
    	VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
    	String  fundsSources = loanInfo.getFundsSources();
    	boolean isSuccess=false;
    	String portBatNo = "";
    	if(fundsSources.equals(FundsSourcesTypeEnum.包商银行.getValue())){//包商
    		Bsb100005Vo bsb100005Vo = this.findDealAfterGrantApplyVoBS(loanId);//封装参数
    		isSuccess = this.callGrantApplyBsInterface(bsb100005Vo);//掉用接口  httpPost
    	}else if(fundsSources.equals(FundsSourcesTypeEnum.外贸2.getValue())){//数信
    		GrantApplyVo grantApplyVo = this.findDealAfterGrantApplyVo(loanId);
	        isSuccess = this.callGrantApplyInterface(grantApplyVo);//掉用接口  httpPost
    	}else if(fundsSources.equals(FundsSourcesTypeEnum.外贸3.getValue())){//外贸3
        	portBatNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        	portBatNo += StringUtils.leftPad(loanInfo.getId().toString(), 12, '0');
    		//调用外贸3放款申请接口
    		ApplyLoanWm3Entity applyLoanWm3Vo = this.findApplyLoan4Wm3(loanId);
    		isSuccess = this.callGrantApplyWm3Interface(applyLoanWm3Vo, portBatNo);
    	}else if(FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSources) || FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSources)){
    		if(thirdLineOfferService.isApplyThirdLineGrant(loanId)){//可以线上放款
    			//创建放款报盘文件    			    			   			
    			LoanOfferInfo loanOfferInfo = createLoanOfferInfo(loanInfo);
    			//创建loan_base_grant数据
    			this.createFinanceGrantApplyLogData(loanId, portBatNo);
    			//调tpp放款接口    
    			callTppGrantInterface(loanInfo,loanOfferInfo);
        	}else{//不可以线上放款
        		throw new PlatformException(ResponseEnum.FULL_MSG,loanInfo.getContractNum()+"借款已申请线下放款，不能重复申请线上放款。");
        	}    		
    	}
    	if(isSuccess){
    		this.createFinanceGrantApplyLogData(loanId, portBatNo);// 根据loan_id VLoanInfo 保存 loan_base_grant 
    	}
    }
    /**
     * 调tpp线上放款接口
     * @param loanId
     * @param loanInfo
     * @param fundsSources
     * @param loanOfferInfo
     */
    private void callTppGrantInterface(VLoanInfo loanInfo,LoanOfferInfo loanOfferInfo) {
    	com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo requestVo=new RequestVo();
    	requestVo.setBizSysNo(BizSys.ZENDAI_2004_SYS.getCode());   	
    	requestVo.setInfoCategoryCode(loanOfferInfo.getCategoryCode());
    	ArrayList<PayReqVo> list = new ArrayList<PayReqVo>();
    	PayReqVo payReqVo = new PayReqVo();
    	payReqVo.setReceiverType("P");//P:个人，C:公司
    	payReqVo.setReceiverName(loanOfferInfo.getName());
    	payReqVo.setReceiverBankCardNo(loanOfferInfo.getBankAcct());
    	payReqVo.setReceiverBankCardType(loanOfferInfo.getCardType());//1储蓄卡2信用卡3存折
    	payReqVo.setReceiverIdType("0");//0=身份证1=户口簿2=护照
    	payReqVo.setReceiverId(loanOfferInfo.getIdNum());
    	payReqVo.setReceiverBankCode(loanOfferInfo.getBankCode());
    	payReqVo.setCurrency(loanOfferInfo.getCurrency());//0人民币
    	payReqVo.setAmount(loanOfferInfo.getAmount());
    	payReqVo.setBizFlow(loanOfferInfo.getSerialNo());
    	payReqVo.setPaySysNo(loanOfferInfo.getPaySysNo());
    	payReqVo.setIsNeedPush(0);
    	list.add(payReqVo);    	
    	requestVo.getList().add(payReqVo);
    	
		Response response = brokerTradeConsumer.syncPay(requestVo);
		log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆合同号:"+loanInfo.getContractNum()+",线上放款结果code："+response.getCode());
		dealTppGrantResult(loanInfo, loanOfferInfo, response);
	}
	/**
	 * 处理tpp放款和查询结果
	 * @param loanInfo
	 * @param loanOfferInfo
	 * @param response
	 * @param tppMsg
	 */
	private void dealTppGrantResult(VLoanInfo loanInfo,
			LoanOfferInfo loanOfferInfo, Response response) {
		if (TPPHelper.TPP20_RECEIVE_SUCCESS_CODE.equals(response.getCode())) {//放款成功
			//更新报盘文件为放款成功
			loanOfferInfo.setState(TppGrantEnum.放款成功.getCode());//报盘状态（01：未报盘、02：已报盘、03：放款成功、04：放款失败）
			loanOfferInfo.setReturnTime(new Date());
			loanOfferInfo.setReturnCode(response.getCode());
			loanOfferInfo.setReturnMsg(response.getMsg());
			loanOfferInfoDao.update(loanOfferInfo);
			//服务费划扣
			loanFeeUtil.createFee(loanInfo.getId(),loanInfo.getFundsSources());
			//更新loan_base_grant表
			updateLoanbaseGrant(loanInfo,FinanceGrantEnum.放款成功.getCode(),response.getMsg());
		}else if(TPPHelper.TPP20_RECEIVE_WORKING_CODE.equals(response.getCode())){//处理中
			//不做处理
			log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆合同号:"+loanInfo.getContractNum()+"状态是处理中！");
		}else{//失败
			//更新报盘文件为放款失败
			loanOfferInfo.setState(TppGrantEnum.放款失败.getCode());
			loanOfferInfo.setReturnTime(new Date());
			loanOfferInfo.setReturnCode(response.getCode());
			loanOfferInfo.setReturnMsg(response.getMsg());
			loanOfferInfoDao.update(loanOfferInfo);
			//更新loan_base_grant表 状态是放款失败
			updateLoanbaseGrant(loanInfo,FinanceGrantEnum.放款失败.getCode(),response.getMsg());
		}
	}
    /**
     * 更新不同状态数据
     * @param loanInfo
     * @param state
     * @param tppMsg 
     */
	private void updateLoanbaseGrant(VLoanInfo loanInfo, String state, String tppMsg) {
		Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("contractNum",loanInfo.getContractNum());
        paramMap.put("grantState",FinanceGrantEnum.申请中.getCode());
        LoanBaseGrant loanBaseGrant = loanBaseGrantDao.queryLoanGrantBaseRelateVloanInfo(paramMap);
        loanBaseGrant.setRemark(tppMsg);
        loanBaseGrant.setGrantState(state);
        if(FinanceGrantEnum.放款成功.getCode().equals(state)){
        	loanBaseGrant.setGrantApplyFinishDate(new Date());
        }
        loanBaseGrantDao.update(loanBaseGrant);       
	}
	/**
     * tpp放款结果查询
     */
    public void tppGrantResultQuery() {
        // 查询出LoanOfferInfo表中已报盘的数据
        List<LoanOfferInfo> loanOfferInfoList = loanOfferInfoDao.findInfoList();
        if (CollectionUtils.isEmpty(loanOfferInfoList)) {
            log.info("☆☆☆☆☆☆☆☆☆☆tppGrantResultQuery查询tpp放款结果结束:没有要查询的数据");
            return;
        }
        for (LoanOfferInfo loanOfferInfo : loanOfferInfoList) {
        	VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanOfferInfo.getLoanId());
            // 发送tpp查询
            com.zendaimoney.thirdpp.trade.pub.vo.req.biz.QueryReqVo queryReqVo = new QueryReqVo();
            queryReqVo.setBizSysNo(BizSys.ZENDAI_2004_SYS.getCode());
        	queryReqVo.setInfoCategoryCode(loanOfferInfo.getCategoryCode());
        	queryReqVo.setBizFlow(loanOfferInfo.getSerialNo());
//        	queryReqVo.setPaySysNo(loanOfferInfo.getPaySysNo());       	
        	
            Response response = brokerTradeConsumer.queryPay(queryReqVo);
            log.info("☆☆☆☆☆☆☆☆☆☆合同号:" + loanInfo.getContractNum() + ",线上放款查询结果code：" + response.getCode());
            // 处理查询结果
            this.dealTppGrantResult(loanInfo, loanOfferInfo, response);
        }
    }
   /**
    * 创建放款报盘文件
    * @param loanInfo
    * @param personInfo
    * @param loanBank
    */
	private LoanOfferInfo createLoanOfferInfo(VLoanInfo loanInfo) {
		String fundsSources = loanInfo.getFundsSources();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fundsSources", fundsSources);
		//查询是否配置过代付通道
		PayChannel payChannel = payChannelService.findPayChannel(params);
		if(payChannel == null){
			throw new PlatformException(ResponseEnum.FULL_MSG,"合同来源【"+fundsSources+"】没有配置相对应的代付通道，不能申请线上放款。");
		}
		PersonInfo personInfo = personInfoService.findById(loanInfo.getBorrowerId());
		LoanBank loanBank = loanBankDao.get(loanInfo.getGrantBankId());
		OfferBankDic offerBankDic = offerBankDicDao.findByCode(loanBank.getBankCode());
		LoanOfferInfo loanOfferInfo=new LoanOfferInfo();
		loanOfferInfo.setId(sequencesService.getSequences(SequencesEnum.LOAN_OFFER_INFO));
		loanOfferInfo.setLoanId(loanInfo.getId());
		loanOfferInfo.setSerialNo(loanInfo.getId().toString() +loanOfferInfo.getId().toString());
		loanOfferInfo.setName(personInfo.getName());
		loanOfferInfo.setIdNum(personInfo.getIdnum());
		loanOfferInfo.setBankAcct(loanBank.getAccount());
		loanOfferInfo.setCardType("1");
		loanOfferInfo.setBankCode(loanBank.getBankCode());
		loanOfferInfo.setCurrency("0");
		loanOfferInfo.setOfferDate(Dates.getNow());
		loanOfferInfo.setAmount(loanInfo.getPactMoney());
		if(FundsSourcesTypeEnum.渤海2.getValue().equals(loanInfo.getFundsSources())){			
			loanOfferInfo.setCategoryCode(this.渤海2实时代付);
		}else{
			loanOfferInfo.setCategoryCode(this.华瑞渤海实时代付);
		}
		loanOfferInfo.setPaySysNo(payChannel.getPaySysNo());
		loanOfferInfo.setState(TppGrantEnum.已报盘.getCode());//报盘状态（01：未报盘、02：已报盘、03：放款成功、04：放款失败）
		loanOfferInfo.setTppBankCode(offerBankDic.getTppBankCode());
		loanOfferInfoDao.insert(loanOfferInfo);
		return loanOfferInfo;
	}

    /**
     * 外贸3 调用网关申请放款接口
	 * @param applyLoanWm3Vo
	 * @return
	 */
	private boolean callGrantApplyWm3Interface(ApplyLoanWm3Entity applyLoanWm3Vo, String portBatNo) {
		List<ApplyLoanWm3Entity> list = new ArrayList<ApplyLoanWm3Entity>();
		list.add(applyLoanWm3Vo);
        JSONObject jsonObject = this.callGrantApplyBatchWm3Interface(list, portBatNo);
        JSONObject grantResut = GatewayUtils.getReponseContentJSONObject(jsonObject,true);
        JSONArray errList = grantResut.getJSONArray("list");
        if(errList.size() > 0){
        	throw new PlatformException(ResponseEnum.FULL_MSG, errList.toString());
        }
        return true;
	}
	
	private JSONObject callGrantApplyBatchWm3Interface(List<ApplyLoanWm3Entity> list, String batNo) {
        RequestInfo requestInfoVo = null;
        WM3_2101Vo wm3_2101Vo = new WM3_2101Vo();
        wm3_2101Vo.setList(list);
        wm3_2101Vo.setBatNo(batNo);
        wm3_2101Vo.setDataCnt(list.size());
        wm3_2101Vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        if (Strings.isEmpty(url)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"接口路径不存在");
        }
        try{
        	requestInfoVo = GatewayUtils.getSendGatewayRequestVo(wm3_2101Vo, GatewayFuncIdEnum.外贸3放款申请);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的外贸3放款申请接口签名异常");
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"获取发送网关外贸3放款申请接口的vo对象异常");
        }
        try {
            log.info("请求网关--外贸3放款申请接口url:{}参数：{}", url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            log.info("请求网关--外贸3放款申请接口url:{}响应：{}", url, JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"调用网关外贸3放款申请接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        return jsonObject;
	}

	/**
     * 获取外贸3 申请放款数据
	 * @param loanId
	 * @return
	 */
	private ApplyLoanWm3Entity findApplyLoan4Wm3(Long loanId) {
		ApplyLoanWm3Entity applyLoanWm3Vo = loanBaseGrantDao.findApplyLoanInfo4WM3(loanId);
//		List<ApplyBorrowerWm3Entity> listCom = loanBaseGrantDao.findApplyBorrower4WM3(applyLoanWm3Vo.getBorrowerId());
//		if(CollectionUtils.isNotEmpty(listCom)){
//			applyLoanWm3Vo.setListCom(listCom);
//		}
//		List<ApplyRelationWm3Entity> listRel = loanBaseGrantDao.findApplyRelation4Wm3(applyLoanWm3Vo.getBorrowerId());
//		if(CollectionUtils.isNotEmpty(listRel)){
//			applyLoanWm3Vo.setListRel(listRel);
//		}
		List<ApplyAccountWm3Entity> listAc = loanBaseGrantDao.findApplyAccount4Wm3(loanId);
		if(CollectionUtils.isNotEmpty(listAc)){
			applyLoanWm3Vo.setListAc(listAc);	
		}
		return applyLoanWm3Vo;
	}

	@Override
    public void insetLonaBaseGrant(LoanBaseGrant loanBaseGrant) {
        loanBaseGrantDao.insert(loanBaseGrant);
    }


    private void setExportLoanBaseGrantVos(List<LoanBaseGrantVo> loanBaseGrantVos) {
        for (LoanBaseGrantVo vo:loanBaseGrantVos) {
            if (FinanceGrantEnum.放款失败.getCode().equals(vo.getGrantState())) {
                vo.setOperate(this.backActive);
            }
            if (FinanceGrantEnum.申请中.getCode().equals(vo.getGrantState())) {
                vo.setOperate(this.grantActive);
            }
        }
    }
    private String[] getFGlabels(){
        return new String[]{"放款营业部","姓名","借款产品","借款期限","身份证号码","合同来源","合同金额","放款金额","所属银行","所属分行","银行卡号","签约日期","还款起始月","还款止日","咨询费","管理费","丙方管理费","丁方管理费","评估费","费用合计","风险金","放款申请时间","放款时间","放款状态"};
    }

    private String[] getFGfields(){
        return new String[]{"signSalesDepName","name","loanType","time","idNum","founsSource","pactMoney","grantMoney","bankName","branchBankName","bankAccount","signDate","starTrDate","endrDate","referRate","manageRate","manageRateForPartyc","manageRateForPartyd","evalRate","rateSum","risk","grantApplyDate","grantApplyFinishDate","grantState"};
    }

    /**
     * 获取经过处理之后的 GrantApplyVo（发送放款申请接口的请求参数）
     * @param loanId
     * @return
     */
    private GrantApplyVo findDealAfterGrantApplyVo(Long loanId){
        GrantApplyVo grantApplyVo = loanBaseGrantDao.getFinanceGrantApplyVo(loanId);
        grantApplyVo.setProjNo(GatewayConstant.PROJ_NO);
        List<GrantAccountVo> accountVos = loanBaseGrantDao.getFinanceGrantAccountVo(loanId);
        List<GrantBorrowPersonVo> borrowPersonVos = loanBaseGrantDao.getFinanceGrantBorrowPersonVo(grantApplyVo.getBorrowerId());
        List<GrantBorRelaPerVo> borRelaPerVos = loanBaseGrantDao.getFinanceGrantBorRelaPerVo(grantApplyVo.getBorrowerId());
        List<GrantRepaymentDetailVo> repaymentDetailVos = loanBaseGrantDao.getFinanceGrantRepaymentDetailVo(loanId);
        grantApplyVo.setSysSource(BaseParamVo.SYS_SOURCE);
        grantApplyVo.setProjNo(BaseParamVo.PROJ_NO);
        grantApplyVo.setListAc(accountVos);
        grantApplyVo.setListCom(borrowPersonVos);
        grantApplyVo.setListRel(borRelaPerVos);
        grantApplyVo.setListRepay(repaymentDetailVos);
        return grantApplyVo;
    }

    /**
     * 获取经过处理之后的 Bsb100005Vo（发送放款申请接口的请求参数）
     * @param loanId
     * @return
     */
    private Bsb100005Vo findDealAfterGrantApplyVoBS(Long loanId){
    	Bsb100005Vo bsb100005Vo = new Bsb100005Vo();//单独一张表 v_Loan_Info
    	VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
    	PersonInfo person = personInfoService.findById(loan.getBorrowerId());
    	LoanBsbMapping bsb = loanBsbMappingService.getByLoanId(loanId);
    	bsb100005Vo.setTxncd("BYXY0005");
    	bsb100005Vo.setCallbackUrl(bstogatewayInterfaceUrl);//通知回调地址  网关的地址
    	bsb100005Vo.setProdSubNo("400001");//产品小类编号
    	bsb100005Vo.setBusNumber(bsb == null?" ":bsb.getBusNumber());//业务流水号  从政审获取
    	bsb100005Vo.setCustName(person == null?"":person.getName());
    	bsb100005Vo.setIdNo(person == null?"":person.getIdnum());
    	bsb100005Vo.setIdType("01");
    	bsb100005Vo.setMobNo(person == null?"":person.getMphone());
        return bsb100005Vo;
    }
    
    private void createFinanceGrantApplyLogData(Long loanId, String portBatNo){
        VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
        User user = UserContext.getUser();
        LoanBaseGrant loanBaseGrant = new LoanBaseGrant();
        loanBaseGrant.setId(sequencesService.getSequences(SequencesEnum.LOAN_BASE_GRANT));
        loanBaseGrant.setLoanId(loanId);
        loanBaseGrant.setGrantApplyDate(new Date());
        loanBaseGrant.setGrantState(FinanceGrantEnum.申请中.getCode());
        loanBaseGrant.setUpdateTime(new Date());
        if (user != null)
            loanBaseGrant.setProposerId(user.getId());
        loanBaseGrant.setAppNo(vLoanInfo.getAppNo());
        loanBaseGrant.setContractNum(vLoanInfo.getContractNum());
        if(FundsSourcesTypeEnum.外贸3.getValue().equals(vLoanInfo.getFundsSources())){
        	loanBaseGrant.setRespStatus("01"); //01-待响应 需调用外部接口查询申请状态
        	loanBaseGrant.setPortBatNo(portBatNo);
        }
        this.insetLonaBaseGrant(loanBaseGrant);
    }

    private boolean callGrantApplyInterface(GrantApplyVo grantApplyVo){
        RequestInfo requestInfoVo = null;
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        if (Strings.isEmpty(url)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"接口路径不存在");
        }
        try{
        requestInfoVo = GatewayUtils.getSendGatewayRequestVo(grantApplyVo, GatewayFuncIdEnum.放款申请业务功能号);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的放款申请接口签名异常");
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"获取发送网关放款申请接口的vo对象异常");
        }
        try {
            log.info("请求网关--放款申请接口url:{}参数：{}", url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            log.info("请求网关--放款申请接口url:{}响应：{}", url, JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"调用网关放款申请接口异常");
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

    private boolean callGrantApplyBsInterface(Bsb100005Vo bsb100005Vo){
        RequestInfo requestInfoVo = null;
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        if (Strings.isEmpty(url)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包银】--接口路径不存在");
        }
        try{
        	requestInfoVo = GatewayUtils.getSendGatewayRequestVo(bsb100005Vo, GatewayFuncIdEnum.包商银行放款申请业务功能号);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包商银行】--生成调用网关的放款申请接口签名异常");
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包商银行】--获取发送网关放款申请接口的vo对象异常");
        }
        try {
            log.info("【包商银行】--请求网关放款申请接口url:{}参数：{}", url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            log.info("【包商银行】--请求网关放款申请接口url:{}响应：{}", url, JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包银】--调用网关放款申请接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        log.info("返回的json数据：----->"+jsonObject);
        if ("0000".equals(jsonObject.get("resCode").toString())) {
            JSONObject grantResut = jsonObject.getJSONObject("infos").getJSONObject("data");;
            if("0500".endsWith(grantResut.getString("respcd"))){
				log.info("【包商银行】--放款申请调用包银接口，返回码"+grantResut.getString("respcd"));
				return true;
			}else{
				log.info("【包商银行】--放款申请调用包银接口失败，原因："+grantResut.getString("resptx"));
				throw new PlatformException(ResponseEnum.FULL_MSG,"放款申请调用包银接口异常："+grantResut.getString("resptx"));
			}
        }
        throw new PlatformException(ResponseEnum.FULL_MSG,"【包商银行】--放款申请调用包银接口异常："+jsonObject.get("respDesc").toString());
    }
    
    @Override
    public Pager queryFinanceGrantDetailsPage(Map<String, Object> paramMap) {
        return loanBaseGrantDao.queryLoanBaseGrantDetailPage(paramMap);
    }

    public List<LoanBaseGrant> findListByVo(LoanBaseGrant loanBaseGrant) {
        return loanBaseGrantDao.findListByVo(loanBaseGrant);
    }

    public Pager queryFinanceGrantPage(LoanBaseGrantVo loanBaseGrantVo) {
        return loanBaseGrantDao.queryFinanceGrantPage(loanBaseGrantVo);
    }

    @Override
    public boolean isWaiMao2FinanceGrantApply(String appNo) {
        if(loanBaseGrantDao.findLoanBaseGrantByappNo(appNo,FinanceGrantEnum.申请中.getCode()) == null){
            return false;
        }
        return true;
    }

    /**
     * 设置根据appNo 查询记录总数的参数
     * @param appNo
     * @return
     */
    private Map<String,Object> setSearchCountParams(String appNo){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appNo",appNo);
        paramMap.put("loanFlowStates",new String[] { LoanFlowStateEnum.财务放款.getValue()});
        paramMap.put("loanStates", new String[] { LoanStateEnum.通过.getValue()});
        return paramMap;
    }

    @Override
    public List<LoanBaseGrantVo> searchFinaceGrantApplyDetail(String type) {
        Map<String,Object> paramMap = this.setSearchFinaceGrantApplyDetailParams(type);
        List<LoanBaseGrantVo> resultList = new ArrayList<LoanBaseGrantVo>();
        resultList = loanBaseGrantDao.exportFinanceGrantInfos(paramMap);
        paramMap.put("grantState",FinanceGrantEnum.放款失败.getCode());
        List<LoanBaseGrantVo> grantFailList = loanBaseGrantDao.exportFinanceGrantInfos(paramMap);
        resultList.addAll(grantFailList);
        return resultList;
    }

    /**
     * 财务放款申请的查询参数
     * @return
     */
    private Map<String,Object> setSearchFinaceGrantApplyDetailParams(String type){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("grantState",FinanceGrantEnum.未放款.getCode());
        if("bs".equals(type)){
        	paramMap.put("founsSources", new String[]{FundsSourcesTypeEnum.包商银行.getValue()});
        }else if("sx".equals(type)){
        	paramMap.put("founsSources", new String[]{FundsSourcesTypeEnum.外贸2.getValue()});
        }else if("wm3".equals(type)){
        	paramMap.put("founsSources", new String[]{FundsSourcesTypeEnum.外贸3.getValue()});
        }
    
        paramMap.put("loanStates", new String[] { LoanStateEnum.通过.getValue()});
        paramMap.put("loanFlowStates",new String[] { LoanFlowStateEnum.财务放款.getValue()});
        return paramMap;
    }

    @Override
    public void updateWaiMao2FinanceGrantApplyReturn(String contractNum){
        // 根据合同号查询处于申请中的放款记录
        LoanBaseGrant loanBaseGrant = this.findInTheApplyLoanBaseGrantByContractNum(contractNum);
        if (loanBaseGrant == null){
            throw new PlatformException(ResponseEnum.FULL_MSG,
                    "Loan_Base_Grant表没有找到对应的记录：{'contractNum':'"+ contractNum+ "'}");
        }
        loanBaseGrant.setGrantState(FinanceGrantEnum.放款失败.getCode());
        this.updateGrantState(loanBaseGrant);
    }

    @Override
    public String batchRequestFinanceGrantApply(String[] loanIds) {
        int succCount = 0;
        int failCount = 0;
        for (String loanId : loanIds) {
            if (this.isApplyFinanceGrant(Long.valueOf(loanId))) {
                try {
                    this.requestFinanceGrantApply(Long.valueOf(loanId));
                    succCount ++;
                }catch (PlatformException pe){
                	log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆loanId:"+loanId+"线上放款失败！原因："+pe.getMessage());
                    failCount ++;
                }
            }else{
            	log.info("☆☆☆☆☆☆☆☆☆☆☆☆☆loanId:"+loanId+"线上放款失败！原因：已申请线上放款！");
            	failCount ++;
            }
        }
//        return String.format("操作完成：成功%s条，失败%s条",succCount,failCount);
        return "操作成功";
    }

    @Override
    public void batchRequestFinanceGrantApply(List<LoanBaseGrantVo> loanBaseGrantVos) {
        log.info("此次批量处理放款申请{}条!",loanBaseGrantVos.size());
        int succCount = 0;
        for (LoanBaseGrantVo loanBaseGrantVo : loanBaseGrantVos) {
            try {
                if (this.isApplyFinanceGrant(loanBaseGrantVo.getLoanId())) {
                    this.requestFinanceGrantApply(loanBaseGrantVo.getLoanId());
                    succCount ++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("此次批量处理放款申请成功{}条!",succCount);
    }

	@Override
	public Map<String, Object> disposeFinanceGrantResult(BYXY0016Vo byxy0016Vo) throws PlatformException,Exception{
		Map<String, Object> json = new HashMap<String, Object>();
		String busNumber = byxy0016Vo.getBusNumber();//业务申请流水号
        String orderNo = byxy0016Vo.getOrderNo();//借据号
		LoanBsbMapping loanBsbMapping = loanBsbMappingDao.getByBusNumber(busNumber);
		loanBsbMapping.setOrderNo(orderNo);
		Long loanId = loanBsbMapping.getLoanId();
		VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
		LoanBase loanBase = loanbaseDao.get(loanId);
		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(loanId);
		LoanProduct loanProduct = loanProductDao.findByLoanId(loanId);
		String state = "";
		boolean flag = true;
		Long term = null;
		BigDecimal returnTeam = null;
		BigDecimal hj = null;
		Integer qs = null;
		BigDecimal bj = null;
		BigDecimal lx = null;
		if("0000".equals(byxy0016Vo.getRespcd())){
       	 	state = "1";
        }else{
       	 	state = "2";
        }
		List<PayPlanListEntity> payPlanList = byxy0016Vo.getPayPlanList();
        if(state.equals("1")){
        	LoanBsbMapping loanBsbMappingByNo = loanBsbMappingDao.getByOrderNo(orderNo);
        	if(loanBsbMappingByNo == null){
        		log.info("【包商银行】放款通知接口，借据号不存在，更新映射表，创建还款计划！");
        		int upCount = loanBsbMappingDao.update(loanBsbMapping);//放款成功后，更新映射表
        		if(upCount == 0){
        			throw new PlatformException(ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,"包银放款通知接口，更新loan_bsb_mapping映射表失败");
        		}
        		Integer firstQS =null;
        		String firstReturnDate = null;
        		SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
        		for(PayPlanListEntity payPlan : payPlanList ){
        			firstQS = payPlan.getPeriod();
        			if(firstQS == 1){
        				firstReturnDate = payPlan.getScheduleDate();
        			}
        		}
        		Date d = sdf.parse(firstReturnDate);
        		if (d.getDate() >= 1 && d.getDate() <= 15) {
        			loanProduct.setPromiseReturnDate(1L);
        		} else {
        			loanProduct.setPromiseReturnDate(16L);
        		}
        		loanProduct.setStartrdate(d);
        		d = (Date)d.clone();
        		d.setMonth(d.getMonth() + Integer.valueOf(loanProduct.getTime().toString()) - 1);
        		loanProduct.setEndrdate(d);
        		loanCoreService.createRepaymentDetailAfterGrantMoney(loanBase, loanInitialInfo, loanProduct);//包商银行放款后调用生成还款计划接口
        		List<LoanRepaymentDetail> loanRepaymentDetailList = loanRepaymentDetailDao.findByLoanId(loanId);
        		if(!loanRepaymentDetailList.isEmpty() && loanRepaymentDetailList.size() != 0 ){
        			for(int i=0;i<loanRepaymentDetailList.size();i++){
        				term = loanRepaymentDetailList.get(i).getCurrentTerm();
        				returnTeam= loanRepaymentDetailList.get(i).getReturneterm();
        				for(int j=0;j< payPlanList.size();j++){
        					qs = payPlanList.get(j).getPeriod();//期数
        					bj = payPlanList.get(j).getSchedulePrincipal();//应还本金
        					lx = payPlanList.get(j).getScheduleInterest();//应还利息
        					hj = bj.add(lx);
        					if(qs.longValue() == term){ //期数相等
        						if(returnTeam.compareTo(hj) != 0){//应还金额 = 应还本金 + 应还利息
        							flag = false;
        							log.error("【包银】返回的应还金额与【核心】生成的还款金额不一致！");
        							throw new PlatformException(ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,"【包银】返回的应还金额与【核心】生成的还款金额不一致");
        						}
        					}
        				}
        			}
        		}else{
        			flag = false;
        			log.error("error：放款通知接口，创建还款计划失败");
        			throw new PlatformException(ResponseEnum.GATEWAY_FINANCEGRANT_FAIL,"放款通知接口，创建还款计划失败");
        		}
        		if(flag){
        			//校验通过进行业务逻辑处理
        			disposeFinanceGrantPushResultService(loanInfo.getContractNum(),state,null);
        			json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
        			json.put("resMsg", "【包银】放款结果通知处理成功");
        		}
        	}else{
        		log.info("【包商银行】放款通知接口，借据号已存在，直接返回成功!");
        		json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
    			json.put("resMsg", "【包银】放款结果通知处理成功");
        	}
        } else {
        	this.updateWaiMao2FinanceGrantApplyReturn(loanInfo.getContractNum());
        	json.put("resCode", ResponseEnum.SYS_SUCCESS.getCode());
        	json.put("resMsg", "【包银】放款结果通知处理成功(包银放款失败。。。)");
        }
        return json;
	}

	//【包商银行】核心入账操作
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean createOfferRepayInfo(Long loanId,BigDecimal amount,String type){
		log.info("【包商银行】债权loanId:"+loanId+",一次性或正常还款入账操作开始...");
		OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
		offerRepayInfo.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
		offerRepayInfo.setLoanId(loanId);
		offerRepayInfo.setAmount(amount);//还款金额
		offerRepayInfo.setMemo("包商银行还款结果通知");
		offerRepayInfo.setOrgan("01");
		offerRepayInfo.setTeller("admin");
		offerRepayInfo.setTradeDate(Dates.getCurrDate());
		if ("3001".equals(type)) {
			offerRepayInfo.setTradeCode(Const.TRADE_CODE_ONEOFF);
		}else if("1001".equals(type)){
			offerRepayInfo.setTradeCode(Const.TRADE_CODE_NORMAL);
		}
		offerRepayInfo.setTradeTime(Dates.getNow());//实际交易时间，精确到秒
		offerRepayInfo.setTradeKind(TradeKindEnum.正常交易.name());
		offerRepayInfo.setTradeType("包商银行");
		offerRepayInfo.setTradeNo(afterLoanService.getTradeFlowNo(loanId));
		offerRepayInfoServiceImpl.save(offerRepayInfo);
		
		try{
			afterLoanService.repayDeal(offerRepayInfo);//分账流水
		}catch(Exception e){
			log.info("【包商银行】债权:"+loanId+",分账失败！"+e.getMessage(),e);
			return false;
		}
		return true;
	}

	@Override
	public List<LoanBaseGrant> findListByMap(Map<String, Object> params) {
		return loanBaseGrantDao.findListByMap(params);
	}

	
	@Override
	public List<LoanBaseGrant> executeCheckApplyLoanResultWm3(List<LoanBaseGrant> list) {
		for(LoanBaseGrant lbg:list){
			try{
				WM3_2102Vo wm3_2102Vo = new WM3_2102Vo();
				wm3_2102Vo.setBatchNo(lbg.getPortBatNo());
				wm3_2102Vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
				wm3_2102Vo.setPactNo(lbg.getContractNum());
				JSONObject jsonObject = this.callCheckApplyLoanResultWm3(wm3_2102Vo);
				JSONArray jsonArray = GatewayUtils.getReponseContentJSONArray(jsonObject,true);
				boolean flag = false;
				for(Object obj:jsonArray){
					JSONObject json = JSONObject.parseObject(obj.toString());
					if(json.getString("pactNo").equals(lbg.getContractNum())){
						flag = true;
						String dealSts = json.getString("dealSts");
						String dealDesc = json.getString("dealDesc");
						//响应结果 1-查无记录 2-数据错误 3-筛查不通过 4-审批中 5-审批不通过 6-待放款 7-已放款 8-放款成功 9-放款失败
						if("8".equals(dealSts)){
							lbg.setRespStatus("03"); //03-响应成功
							lbg.setGrantState(FinanceGrantEnum.放款成功.getCode());
							lbg.setGrantApplyFinishDate(new Date());
						}else if("1".equals(dealSts) || "2".equals(dealSts) || "3".equals(dealSts) || "5".equals(dealSts) || "9".equals(dealSts)){
							lbg.setRespStatus("03"); //03-响应成功
							lbg.setGrantState(FinanceGrantEnum.放款失败.getCode());
							lbg.setRemark(dealDesc);
						}
						break;
					}
				}
				if(!flag){
					lbg.setRespStatus("02"); //02-响应失败
					lbg.setRemark("未找到记录");
				}
			}catch(PlatformException p ){
				lbg.setRespStatus("02"); //02-响应失败
				lbg.setRemark(p.getMessage());
			}catch(Exception e){
				lbg.setRespStatus("02"); //02-响应失败
				lbg.setRemark("程序处理异常");
			}finally{
				loanBaseGrantDao.update(lbg);
			}
		}
		return list;
	}

	/**
	 * 调用外贸3 放款申请结果查询接口
	 * @param LoanBaseGrant
	 * @return
	 */
	private JSONObject callCheckApplyLoanResultWm3(WM3_2102Vo wm3_2102Vo) {
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        RequestInfo requestInfoVo = null;
        if (Strings.isEmpty(url)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"接口路径不存在");
        }
        try{
        	requestInfoVo = GatewayUtils.getSendGatewayRequestVo(wm3_2102Vo, GatewayFuncIdEnum.外贸3放款申请结果查询);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的外贸3放款申请结果查询接口签名异常");
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"获取发送网关外贸3放款申请结果查询接口的vo对象异常");
        }
        try {
            log.info("请求网关--外贸3放款申请结果查询接口url:{}参数：{}", url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            log.info("请求网关--外贸3放款申请结果查询接口url:{}响应：{}", url, JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"调用网关外贸3放款申请结果查询接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        return jsonObject;
	}


	@Override
	public void createDebitAccountInfo(VLoanInfo vLoanInfo) {
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loanId", vLoanInfo.getId());
			VloanPersonInfo loanPersonInfo = vLoanInfoService.findImportLoanInfo(params);
			WM3_5201Vo wm3_5201Vo = new WM3_5201Vo();
			wm3_5201Vo.setIdType("13"); //13-溢缴款账户开户
			wm3_5201Vo.setPactNo(loanPersonInfo.getContractNum());
			wm3_5201Vo.setCustName(loanPersonInfo.getName());
			wm3_5201Vo.setIdNo(loanPersonInfo.getIdnum());
			wm3_5201Vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
			wm3_5201Vo.setProjNo(BaseParamVo.PROJ_NO_WM3);
			JSONObject json = this.callOpenOverFlowAccountWm3(wm3_5201Vo);
			JSONObject info = GatewayUtils.getReponseContentJSONObject(json,true);
			String resultCode = info.getString("resultCode");
			String resultMsg = info.getString("resultMsg");
			log.info("外贸3账户开户接口："+resultMsg);
			String accountNo = info.getString("accountNo");
			String accountName = info.getString("accountName");
			DebitAccountInfo debitAccountInfo = new DebitAccountInfo();
			debitAccountInfo.setAccountName(accountName);
			debitAccountInfo.setAccountNo(accountNo);
			debitAccountInfo.setBrNo(wm3_5201Vo.getBrNo());
			debitAccountInfo.setCustName(loanPersonInfo.getName());
			debitAccountInfo.setIdNo(loanPersonInfo.getIdnum());
			debitAccountInfo.setLoanId(loanPersonInfo.getId());
			debitAccountInfo.setPactNo(loanPersonInfo.getContractNum());
			debitAccountInfo.setResultCode(resultCode);
			debitAccountInfo.setResultMsg(resultMsg);
			debitAccountInfo.setId(sequencesService.getSequences(SequencesEnum.DEBIT_ACCOUNT_INFO));
			debitAccountInfoDao.insert(debitAccountInfo);
		}catch(Exception e){
			log.error("外贸3溢缴款账户开户异常。。。",e);
		}
	}

	/**
	 * 外贸3 调用溢缴款开户接口
	 * @param wm3_5201Vo
	 * @return
	 */
	private JSONObject callOpenOverFlowAccountWm3(WM3_5201Vo wm3_5201Vo) {
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        RequestInfo requestInfoVo = null;
        if (Strings.isEmpty(url)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"接口路径不存在");
        }
        try{
        	requestInfoVo = GatewayUtils.getSendGatewayRequestVo(wm3_5201Vo, GatewayFuncIdEnum.外贸3账户开户);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的外贸3账户开户接口签名异常");
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"获取发送网关外贸3账户开户接口的vo对象异常");
        }
        try {
            log.info("请求网关--外贸3账户开户接口url:{}参数：{}", url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            log.info("请求网关--外贸3账户开户接口url:{}响应：{}", url, JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"调用网关外贸3账户开户接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        return jsonObject;
	}

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public OfferRepayInfo createOfferRepayInfoThird(Long loanId, BigDecimal amount, String type) {
        OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
        offerRepayInfo.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
        offerRepayInfo.setLoanId(loanId);
        offerRepayInfo.setAmount(amount);// 还款金额
        offerRepayInfo.setMemo("陆金所扣款结果入账");
        offerRepayInfo.setOrgan("01");
        offerRepayInfo.setTeller("admin");
        offerRepayInfo.setTradeDate(Dates.getCurrDate());
        offerRepayInfo.setTradeCode(type);
        offerRepayInfo.setTradeTime(Dates.getNow());// 实际交易时间，精确到秒
        offerRepayInfo.setTradeKind(TradeKindEnum.正常交易.name());
        offerRepayInfo.setTradeType(FundsSourcesTypeEnum.陆金所.getValue());
        offerRepayInfo.setTradeNo(afterLoanService.getTradeFlowNo(loanId));
        offerRepayInfo.setCreateDebitQueue(false);// 设置为false，标识在入账的时候不再创建划扣对列数据
        offerRepayInfoServiceImpl.save(offerRepayInfo);
        // 入账分账主处理
        afterLoanService.repayDeal(offerRepayInfo);
        return offerRepayInfo;
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean createWm3OfferRepayInfo(Long loanId, BigDecimal amount, String tradeCode) {
        log.info("【外贸3】债权编号:" + loanId + ",还款入账操作开始。。。。。。");
        OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
        offerRepayInfo.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
        offerRepayInfo.setLoanId(loanId);
        offerRepayInfo.setAmount(amount);// 还款金额
        offerRepayInfo.setMemo("外贸3扣款结果入账");
        offerRepayInfo.setOrgan("01");
        offerRepayInfo.setTeller("admin");
        offerRepayInfo.setTradeDate(Dates.getCurrDate());
        offerRepayInfo.setTradeCode(tradeCode);
        offerRepayInfo.setTradeTime(Dates.getNow());// 实际交易时间，精确到秒
        offerRepayInfo.setTradeKind(TradeKindEnum.正常交易.name());
        offerRepayInfo.setTradeType(FundsSourcesTypeEnum.外贸3.getValue());
        offerRepayInfo.setTradeNo(afterLoanService.getTradeFlowNo(loanId));
        offerRepayInfoServiceImpl.save(offerRepayInfo);
        try {
            afterLoanService.repayDeal(offerRepayInfo);// 分账流水
        } catch (Exception e) {
            log.info("【外贸3】债权编号:" + loanId + ",入账失败！", e);
            return false;
        }
        log.info("【外贸3】债权编号:" + loanId + ",还款入账操作结束。。。。。。");
        return true;
    }
}
