package com.zdmoney.credit.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.common.constant.TrustGrantStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.CreateFailVO;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.core.service.pub.IFinanceCoreAfterLoanService;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.core.service.pub.IRepaymentDetialCoreService;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanTrustGrantInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanTrustGrantInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.OfferInfoServiceImpl;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;
import com.zdmoney.credit.person.service.pub.IPersonThirdPartyAccountService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.IStoredProcedureService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zendaimoney.thirdpp.rmi.service.request.ThirdPaymentService;
import com.zendaimoney.thirdpp.vo.ProcessResultInfoVo;
import com.zendaimoney.thirdpp.vo.RequestVo;
import com.zendaimoney.thirdpp.vo.YyouCustFinanceVo;
import com.zendaimoney.thirdpp.vo.enums.IsAsyn;
import com.zendaimoney.thirdpp.vo.enums.IsReSend;
import com.zendaimoney.thirdpp.vo.enums.RequestType;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;
import com.zendaimoney.thirdpp.vo.enums.ZendaiSys;
import com.zendaimoney.thirdpp.vo.expand.TPPEnum;

/**
 * 
 * @author 00232949
 *
 */
@Service
public class FinanceCoreService implements IFinanceCoreService{
	
	private static final Logger logger = Logger.getLogger(OfferInfoServiceImpl.class);
	
	/** 暂时取消调用 **/
	@Autowired(required = false)
	private ThirdPaymentService thirdPaymentService;
	
	@Autowired
	private IRepaymentDetialCoreService repaymentDetialCoreService;
	@Autowired
	private IComEmployeeService comEmployeeService;
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	@Autowired
	private ILoanBaseDao loanBaseDao;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanTrustGrantInfoService loanTrustGrantInfoService;
	@Autowired
	private IPersonInfoService personInfoService;
	@Autowired
	private IPersonThirdPartyAccountService personThirdPartyAccountService;
	@Autowired
	private ISequencesService sequencesService;
	
	@Autowired
	private IStoredProcedureService storedProcedureService;
	@Autowired
	private IFinanceCoreAfterLoanService financeCoreAfterLoanService;
	
	/**
     * 放款撤销接口
     * @param params 入参
     * @return 返回放款撤销是否成功的相关信息集合
     */
	@Override
    public Map<String,Object> rollbackGrantLoan(FinanceVo params){
    	Map<String, Object> json = null;
        Map<String, Object> results = repaymentDetialCoreService.queryRepayInfoByLoanId(params);
        if ((Integer)results.get("total") == 0) {
        	LoanBase loan = loanBaseDao.get(Long.parseLong(params.getLoanId()));
            if (loan!=null) {
                if (loan.getLoanFlowState().equals(LoanFlowStateEnum.正常.getValue())){
                    //调用存储过程
                	int count = storedProcedureService.rollbackGrantloan(Long.parseLong(params.getLoanId()), params.getUserCode());
                	if(count > 0) {
                        json = MessageUtil.returnHandleSuccessMessage();
                    } else {
                        json = MessageUtil.returnErrorMessage("失败:放款撤销失败！");
                    }
                } else {
                    json = MessageUtil.returnErrorMessage("失败:数据状态不正确，不可以放款撤销！");
                }
            } else {
                json = MessageUtil.returnErrorMessage("失败:" + params.getLoanId() + " 无记录！");
            }
        } else {
            json = MessageUtil.returnErrorMessage("失败:有还款记录不可以放款撤销！");
        }
        return json;
    }

    /**
     * 放款推送接口
     * @param params 入参
     * @return 推送结果数据
     */
	@SuppressWarnings("unused")
	@Override
	@Transactional
    public Map<String,Object> grantLoan(FinanceVo params){
        Map<String, Object> json = new HashMap<String, Object>();
        String userCode = params.getUserCode();
        ComEmployee employee = comEmployeeService.findByUserCode(userCode);
        if(employee==null){
            json = MessageUtil.returnErrorMessage("userCode输入有误，无法找到对应记录");
            return json;
        }
        List<String> ids  = Arrays.asList(( params.getIds()).split(","));
        List<VLoanInfo> loanList = new ArrayList<VLoanInfo>();
        List<CreateFailVO> createFaiVOs = new ArrayList<CreateFailVO>();
        for (int i = 0 ; i < ids.size(); i++) {
        	VLoanInfo loan = vLoanInfoService.findByLoanId(Long.parseLong(ids.get(i)));
            if(loan != null && loan.getLoanFlowState().equals(LoanFlowStateEnum.财务放款.getValue())) {
                loanList.add(loan);
            }else{
                CreateFailVO createFaiVO = new CreateFailVO();
                if(loan==null){
                    createFaiVO.setErrorMessage("no loan ");
                }else{
                    createFaiVO.setErrorMessage(" Can't loan reason loan.loanFlowState="+loan.getLoanFlowState());
                }
                createFaiVO.setLoanId(Long.parseLong(ids.get(i)));
                createFaiVOs.add(createFaiVO);
            }
        }
        Map<String, Object> map = this.huaaoTrustErrCheckIng(employee, loanList);
        Map<String, Object> createFaiMap = this.afterTheLoanFinancing(employee, map);
        createFaiVOs.addAll((Collection) map.get("createFaiVOs"));

        json = MessageUtil.returnQuerySuccessMessage(0,createFaiVOs,createFaiVOs.size(),"createFaiVOs");
        return json;
    }

    /**
     * 对华奥信托业务的进行错误验证，验证无误的先更新状态为“融资处理中”
     * @param ids 债权ID集合
     * @return 返回Map<String, Object>集合.  ids：去除掉不能进行放款的债权ID集合,createFaiVOs:放款失败的债权ID和失败信息
     */
    private Map<String, Object> huaaoTrustErrCheckIng(ComEmployee employee, List<VLoanInfo> loanList){
        Map<String, Object> map = new HashMap<String, Object>();
        List<CreateFailVO> createFaiVOs = new ArrayList<CreateFailVO>();

        /*Set<String> perList = employee.getPermissions();
        boolean flag = false;
        for(String s : perList){
            if(s.equals("/finance/trustGrant")){
                flag = true;
                break;
            }
        }*/

        for (int i = 0 ; i < loanList.size(); i++) {
        	VLoanInfo loan = loanList.get(i);
                if (FundsSourcesTypeEnum.华澳信托.name().equals(loan.getFundsSources())) {
                    CreateFailVO createFaiVO = new CreateFailVO();
                    /*if (!TrustGrantStateEnum.融资成功.getValue().equals(loan.getTrustGrantState())) {
                        createFaiVO.setErrorMessage("【借款人：" + loan.getBorrowerId() + "】"+ "您没有权限对华澳信托的合同进行放款");
                        createFaiVO.setLoanId(loan.getId());
                        createFaiVOs.add(createFaiVO);
                        loanList.remove(i);
                        i--;
                        continue;
                    }*/
                    if (Strings.isEmpty(loan.getTrustGrantState()) || TrustGrantStateEnum.融资失败.name().equals(loan.getTrustGrantState())) {
                    	try {
							LoanBase loanBase = loanBaseDao.get(loan.getId());
							loanBase.setTrustGrantState(TrustGrantStateEnum.融资处理中.name());
							loanBaseDao.update(loanBase);
						} catch (Exception e) {
							logger.error("【借款人：" + loan.getBorrowerId() + "】"+ "数据更新失败! "+e.getMessage());
							createFaiVO.setErrorMessage("【借款人：" + loan.getBorrowerId() + "】"+ "数据更新失败");
                            createFaiVO.setLoanId(loan.getId());
                            createFaiVOs.add(createFaiVO);
                            loanList.remove(i);
                            i--;
                            continue;
						}
                    } else if (TrustGrantStateEnum.融资处理中.name().equals(loan.getTrustGrantState())) {
                        createFaiVO.setErrorMessage("【借款人：" + loan.getBorrowerId() + "】"+ "当前融资状态为融资处理中,无法再次放款");
                        createFaiVO.setLoanId(loan.getId());
                        createFaiVOs.add(createFaiVO);
                        loanList.remove(i);
                        i--;
                        continue;
                    } else if (TrustGrantStateEnum.融资成功.name().equals(loan.getTrustGrantState())) {
                        createFaiVO.setErrorMessage("【借款人：" + loan.getBorrowerId() + "】"+ "当前融资状态为融资成功,无法再次放款");
                        createFaiVO.setLoanId(loan.getId());
                        createFaiVOs.add(createFaiVO);
                        loanList.remove(i);
                        i--;
                        continue;
                    }
            }
        }
        map.put("loanList", loanList);
        map.put("createFaiVOs", createFaiVOs);
        return map;
    }

    /**
     * 对华奥信托进行融资，融资成功后进行放款，其他不用融资
     * @param employee 操作人相关信息
     * @param map 债权ID集合和放款失败的相关信息
     * @return 放款失败的ID和信息
     */
    @SuppressWarnings("unchecked")
	private Map<String, Object> afterTheLoanFinancing(ComEmployee employee, Map<String, Object> map){
        List<CreateFailVO> createFaiVOs =  (List<CreateFailVO>) map.get("createFaiVOs");
        List<VLoanInfo> loanList = (List<VLoanInfo>) map.get("loanList");
        for (VLoanInfo loan : loanList) {
        	CreateFailVO createFaiVO = new CreateFailVO();
                // 信托计划的放款，调用TPP放款接口
                if (loan.getFundsSources().equals(FundsSourcesTypeEnum.华澳信托.name())) {
                	LoanTrustGrantInfo trustGrantInfo = new LoanTrustGrantInfo();
                	trustGrantInfo.setId(sequencesService.getSequences(SequencesEnum.LOAN_TRUST_GRANT_INFO));
                    trustGrantInfo.setLoanId(loan.getId());
                    //得到第三方绑卡信息
                    PersonInfo personInfo = personInfoService.findById(loan.getBorrowerId());
                    PersonThirdPartyAccount personThirdAcc = personThirdPartyAccountService.findById(personInfo.getThirdPartyAccountId());
                    trustGrantInfo.setCreditUserCode(personThirdAcc.getCreditUserTpp());//从第三方绑卡信息中获取记录的creditUserTpp号
                    
                    trustGrantInfo.setAmount(loan.getPactMoney());
                    trustGrantInfo.setIncome(loan.getRateSum());
                    trustGrantInfo.setCreateTime(new Date());
                    trustGrantInfo.setUpdateTime(new Date());
                    loanTrustGrantInfoService.saveNow(trustGrantInfo);
                    // 调用TPP融资接口
                    String tppResult = this.callTppFinancing(loan, trustGrantInfo, employee);
                    // 融资失败的，不进行放款操作
                    if (!TrustGrantStateEnum.融资成功.name().equals(tppResult)) {
                        createFaiVO.setErrorMessage("【借款人：" + loan.getBorrowerId() + "】"+ tppResult);
                        createFaiVO.setLoanId(loan.getId());
                        createFaiVOs.add(createFaiVO);
                        continue;
                    }
                }
                
                try {
                	financeCoreAfterLoanService.financingGrantLoan(employee, loan);
				} catch (Exception e) {
                    logger.error("更改债权状态报错：", e);
                    createFaiVO.setErrorMessage(e.getMessage());
                    createFaiVO.setLoanId(loan.getId());
                    createFaiVOs.add(createFaiVO);
				}
        }
        map.put("loanList", loanList);
        map.put("createFaiVOs", createFaiVOs);
        return map;
    }

    /**
     * 融资处理
     * @param loan 债权ID
     * @param trustGrantInfo
     * @param employee 操作人对象
     * @return 融资处理结果信息
     */
    private String callTppFinancing(VLoanInfo loan, LoanTrustGrantInfo trustGrantInfo, ComEmployee employee) {
        RequestVo requestVo = new RequestVo();
        requestVo.setAsynTag(IsAsyn.IS_ASYN_TAG_0);// 0 非异步 不需要回调 1 异步 需要回调
        requestVo.setReSendTag(IsReSend.IS_RESEND_TAG_0);// 不需要重发
        requestVo.setRequestType(RequestType.PAYING);// 请求类型
        requestVo.setRequestOperator(employee.getName());// 操作员
        requestVo.setRequestSystem(ZendaiSys.ZENDAI_2004_SYS);// 业务系统
        requestVo.setThirdPartyType(ThirdPartyType.YYoupay);// 第三方
        requestVo.setRequestDate(new Date());
        Map<TPPEnum, String> map = new LinkedHashMap<TPPEnum, String>(); //预留扩张字段

        YyouCustFinanceVo vo = new YyouCustFinanceVo();
        
        vo.setTrustUserCode(sysParamDefineService.getSysParamValue("codeHelper", "xintuoId"));//信托用户id
        
      //得到第三方绑卡信息
        PersonInfo personInfo = personInfoService.findById(loan.getBorrowerId());
        PersonThirdPartyAccount personThirdAcc = personThirdPartyAccountService.findById(personInfo.getThirdPartyAccountId());
        vo.setCreditUserCode(personThirdAcc.getCreditUserTpp());//融资用户id
        
        vo.setAmount(loan.getPactMoney().toString());//融资金额(元)
        vo.setDealType("000");//交易类型(默认000)
        String tradeDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        vo.setTradeDate(tradeDate);//交易日期yyyymmdd24hhmiss
        vo.setFlow(String.valueOf(trustGrantInfo.getId()));//信贷订单流水号，查询接口需要用到
        vo.setActType("000");//账户类型(默认000)
        vo.setIncome(loan.getRateSum().toString());//手续费（元）
        vo.setExpandProperties(map);
        requestVo.getRequestDetailInfo().getRequestDetails().add(vo);

        //客户融资
        LoanBase  loanBase = null;
        try {
        	RequestVo returnRequestVo = null;
//            RequestVo returnRequestVo = thirdPaymentService.customerFinancing(requestVo);
        	/** 切换TPP2.0通道，取消华海融资处理功能(RMI接口) **/
        	if (true) {
        		throw new PlatformException(ResponseEnum.FULL_MSG,"为了适应接口切换，取消customerFinancing接口调用(RMI接口)");
        	}
            YyouCustFinanceVo finance = (YyouCustFinanceVo)returnRequestVo.getRequestDetailInfo().getRequestDetails().get(0);
            ProcessResultInfoVo result = finance.getProcessResultInfo();
            loanBase = loanBaseDao.findByLoanId(loan.getId()); 
            
            if("2".equals(returnRequestVo.getThirdPartyRequestStatus())){
                if("1".equals(result.getProcessStatus())){
                    if("000000".equals(result.getTppReturnCode())){//操作成功
                        trustGrantInfo.setTppReturnState(TrustGrantStateEnum.融资成功.name());
                        loanTrustGrantInfoService.updateNow(trustGrantInfo);
                        
                        loanBase.setTrustGrantState(TrustGrantStateEnum.融资成功.name());
                        loanBaseDao.update(loanBase);
                        return TrustGrantStateEnum.融资成功.name();
                    }
                    if("111111".equals(result.getTppReturnCode())){//操作失败
                        trustGrantInfo.setTppReturnState(TrustGrantStateEnum.融资失败.name());
                        trustGrantInfo.setMemo(result.getThirdPartyBackInfo());
                        loanTrustGrantInfoService.updateNow(trustGrantInfo);
                        
                        loanBase.setTrustGrantState(TrustGrantStateEnum.融资失败.name());
                        loanBaseDao.update(loanBase);
                        return result.getThirdPartyBackInfo();//第三方反回的失败原因
                    }
                }
            }
            trustGrantInfo.setTppReturnState(TrustGrantStateEnum.融资处理中.name());
            trustGrantInfo.setMemo("ThirdPartyRequestStatus:" + returnRequestVo.getThirdPartyRequestStatus() + " Message:" + returnRequestVo.getMessage());
            loanTrustGrantInfoService.updateNow(trustGrantInfo);
            
            loanBase.setTrustGrantState(TrustGrantStateEnum.融资处理中.name());
            loanBaseDao.update(loanBase);
            return TrustGrantStateEnum.融资处理中.name() + " ThirdPartyRequestStatus:" + returnRequestVo.getThirdPartyRequestStatus() + " Message:" + returnRequestVo.getMessage();
        } catch (Exception e) {
            trustGrantInfo.setTppReturnState(TrustGrantStateEnum.融资处理中.name());
            trustGrantInfo.setMemo(e.getMessage());
            loanTrustGrantInfoService.updateNow(trustGrantInfo);
            
            loanBase.setTrustGrantState(TrustGrantStateEnum.融资处理中.name());
            loanBaseDao.update(loanBase);
            return TrustGrantStateEnum.融资处理中.name()  + " " + e.getMessage();
        }
    }
}
