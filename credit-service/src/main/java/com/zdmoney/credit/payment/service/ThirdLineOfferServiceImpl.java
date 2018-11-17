package com.zdmoney.credit.payment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.CurrencyTypeEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.ThirdOfferFinancialTypeEnum;
import com.zdmoney.credit.common.constant.ThirdOfferStateEnum;
import com.zdmoney.credit.common.constant.offer.ThirdOfferConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferDao;
import com.zdmoney.credit.payment.domain.ThirdLineOffer;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferService;
import com.zdmoney.credit.payment.vo.ThirdLineOfferVo;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class ThirdLineOfferServiceImpl implements IThirdLineOfferService {

    protected static Log logger = LogFactory.getLog(ThirdLineOfferServiceImpl.class);
    
    @Autowired
    private IThirdLineOfferDao haTwoOfferDao;
    
    @Autowired
    private ILoanBaseDao loanBaseDao;
    
    @Autowired
    private ILoanBankDao loanBankDao;
    
    @Autowired
    private IPersonInfoDao personInfoDao;
    
    @Autowired
    private ILoanInitialInfoDao loanInitialInfoDao;
    
    @Autowired
    private ILoanProductDao loanProductDao;
    
    @Autowired
    private ISequencesService sequencesService;
    
    public void insert(ThirdLineOffer haTwoOffer) {
        haTwoOfferDao.insert(haTwoOffer);
    }

    public List<ThirdLineOfferVo> queryAllHaTwoOfferVo() {
        return haTwoOfferDao.queryAllHaTwoOfferVo();
    }

    public List<ThirdLineOffer> findHaTwoOfferByMap(Map<String, Object> paramMap) {
        return haTwoOfferDao.findHaTwoOfferByMap(paramMap);
    }
    
    /**
     * 跟据编号查询实体
     * @param id
     * @return
     */
    public ThirdLineOffer findById(Long id) {
        return haTwoOfferDao.get(id);
    }
    
    public Pager searchOfferInfoWithPg(Map<String, Object> paramMap) {
        return haTwoOfferDao.searchOfferInfoWithPg(paramMap);
    }
    
    /**
     * 导出报盘文件时，更新报盘表的状态
     */
    public void updateState(Long twoOfferId) {
        ThirdLineOffer thirdLineOffer = new ThirdLineOffer();
        thirdLineOffer.setState(ThirdOfferStateEnum.已报盘.getValue());
        thirdLineOffer.setId(twoOfferId);
        haTwoOfferDao.update(thirdLineOffer);
    }
    
    /**
     * 创建放款本金和手续费的报盘信息
     */
    public List<ThirdLineOffer> createThirdLineOffer(Map<String, Object> map) {
        // 查询财务放款、合同来源 = 渤海信托或渤海2 华瑞渤海
        List<LoanBase> loanBaseList = loanBaseDao.getAllLoanBaseInfo(map);
        if(CollectionUtils.isEmpty(loanBaseList)){
            throw new PlatformException(ResponseEnum.FULL_MSG, "【"+map.get("fundsSource") + "】合同来源下无待放款的债权，或都已申请线上放款。");
        }
        List<ThirdLineOffer> offerList = new ArrayList<ThirdLineOffer>();
        for (LoanBase loanBase:loanBaseList) {
            // 如果存在回盘失败的报盘，则只需更新报盘状态和报盘时间，不需再生成报盘信息
            // 创建放款本金的报盘信息
        	ThirdLineOffer offer =this.createOffer4Customer(loanBase);
        	offerList.add(offer);
            // 创建手续费的报盘信息 
            //this.createOffer4Company(loanBase);
        }
        return offerList;
    }

    /**
     * 创建放款本金的报盘信息
     * @param loanBase
     * @return 
     */
    private ThirdLineOffer createOffer4Customer(LoanBase loanBase) {
        Long loanId = loanBase.getId();
        // 根据债权编号和财务类型查询报盘信息
        ThirdLineOffer resultOffer = this.queryOfferByLoanId(loanId, ThirdOfferFinancialTypeEnum.放款本金);
        // 如果已存在报盘信息，则不需再生成报盘信息；如果报盘状态是扣款失败、则需要更新报盘状态
        if(null != resultOffer){
            if (ThirdOfferStateEnum.扣款失败.getValue().equals(resultOffer.getState())){
                resultOffer.setState(ThirdOfferStateEnum.未报盘.getValue());
                // 编辑报盘信息
                this.editOfferInfo(loanBase, resultOffer);
                // 更新报盘信息
                haTwoOfferDao.update(resultOffer);
            }
            return resultOffer;
        }
        // 编辑放款本金的报盘信息
        ThirdLineOffer  offerInstance = new ThirdLineOffer();
        offerInstance.setId(sequencesService.getSequences(SequencesEnum.THIRD_LINE_OFFER));
        // 通联支付用户编号
        //offerInstance.setTlPaymentNumber(ThirdOfferConst.TL_PAYMENT_NUMBER);
        // 编辑报盘信息
        this.editOfferInfo(loanBase, offerInstance);
        // 货币类型
        offerInstance.setCurrencyType(CurrencyTypeEnum.CNY.getValue());
        // 账户类型
        offerInstance.setAccountType(ThirdOfferConst.ACCOUNT_TYPE);
        // 债权id
        offerInstance.setLoanId(loanId);
        // 财务类型
        offerInstance.setFinancialType(ThirdOfferFinancialTypeEnum.放款本金.getValue()); 
        // 报盘时间
        offerInstance.setOfferTime(new Date());
        //备注（手续费）
        offerInstance.setRemark(ThirdOfferFinancialTypeEnum.放款本金.name());
        // 报盘状态
        offerInstance.setState(ThirdOfferStateEnum.未报盘.getValue());
        haTwoOfferDao.insert(offerInstance);
        return offerInstance;
    }

    /**
     * 编辑报盘信息
     * @param loanBase
     * @param offerInstance
     */
    private void editOfferInfo(LoanBase loanBase, ThirdLineOffer offerInstance) {
        // 查询客户银行账户信息
        LoanBank loanBank = loanBankDao.get(loanBase.getGiveBackBankId());
        // 银行代码
        offerInstance.setBankCode(loanBank.getBankCode());
        // 账号类型
        offerInstance.setAccountNumberType(ThirdOfferConst.ACCOUNT_NUMBER_TYPE);
        // 银行账号
        offerInstance.setAccountNumber(loanBank.getAccount());
        // 开户行名称
        offerInstance.setBankName(loanBank.getFullName());
        // 查询银行卡号上的姓名
        PersonInfo personInfo = personInfoDao.get(loanBase.getBorrowerId());
        offerInstance.setAccountName(personInfo.getName());
        long loanId = loanBase.getId();
        String fundsSource = loanBase.getFundsSources();
        if (FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource) || FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSource)) {
            // 查询债权产品信息
            LoanProduct loanProduct = loanProductDao.findByLoanId(loanId);
            // 金额 = 合同金额
            offerInstance.setAmount(loanProduct.getPactMoney());
            return;
        }
        // 金额 = 审批金额
        LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(loanId);
        offerInstance.setAmount(loanInitialInfo.getMoney());
    }
    
    /**
     * 创建手续费的报盘信息
     * @param loanBase
     */
    private void createOffer4Company(LoanBase loanBase) {
        Long loanId = loanBase.getId();
        // 根据债权编号和财务类型查询报盘信息
        ThirdLineOffer resultOffer = this.queryOfferByLoanId(loanId, ThirdOfferFinancialTypeEnum.手续费);
        // 如果已存在报盘信息，则不需再生成报盘信息；如果报盘状态是扣款失败、则需要更新报盘状态
        if(null != resultOffer){
            if (ThirdOfferStateEnum.扣款失败.getValue().equals(resultOffer.getState())){
                resultOffer.setState(ThirdOfferStateEnum.未报盘.getValue());
                //resultOffer.setOfferTime(new Date());
                haTwoOfferDao.update(resultOffer);
            }
            return;
        }
        // 编辑手续费的报盘信息 
        ThirdLineOffer  offerInstance = new ThirdLineOffer();
        offerInstance.setId(sequencesService.getSequences(SequencesEnum.THIRD_LINE_OFFER));
        // 通联支付用户编号
        //offerInstance.setTlPaymentNumber(ThirdOfferConst.TL_PAYMENT_NUMBER);
        // 银行代码
        offerInstance.setBankCode(ThirdOfferConst.ZD_BANK_CODE);
        // 账号类型
        offerInstance.setAccountNumberType(ThirdOfferConst.ACCOUNT_NUMBER_TYPE);
        // 银行账户为公司账户
        offerInstance.setAccountNumber(ThirdOfferConst.ZD_ACCOUNT_NUMBER);
        // 账户名称为公司名称
        offerInstance.setAccountName(ThirdOfferConst.ZD_ACCOUNT_NAME);
        // 开户行名称
        offerInstance.setBankName(ThirdOfferConst.ZD_BANK_NAME);
        // 查询债权产品信息
        LoanProduct loanProduct = loanProductDao.findByLoanId(loanId);
        // 咨询费
        BigDecimal referRate = loanProduct.getReferRate();
        // 评估费
        BigDecimal evalRate = loanProduct.getEvalRate();
        // 管理费
        BigDecimal manageRate = loanProduct.getManageRate();
        // 金额 = 咨询费＋评估费＋管理费
        offerInstance.setAmount(referRate.add(evalRate).add(manageRate));
        // 货币类型
        offerInstance.setCurrencyType(CurrencyTypeEnum.CNY.getValue());
        // 账户类型
        offerInstance.setAccountType(ThirdOfferConst.ACCOUNT_TYPE);
        //债权id
        offerInstance.setLoanId(loanId);
        //财务类型
        offerInstance.setFinancialType(ThirdOfferFinancialTypeEnum.手续费.getValue());
        //报盘时间
        offerInstance.setOfferTime(new Date());
        //备注（手续费）
        offerInstance.setRemark(ThirdOfferFinancialTypeEnum.手续费.name());
        //报盘状态
        offerInstance.setState(ThirdOfferStateEnum.未报盘.getValue());
        haTwoOfferDao.insert(offerInstance);
    }
    
    /**
     * 根据债权编号和财务类型查询报盘信息
     * @param loanId
     * @param financialType
     * @return
     */
    private ThirdLineOffer queryOfferByLoanId(Long loanId,ThirdOfferFinancialTypeEnum financialType){
        ThirdLineOffer searchVo = new ThirdLineOffer();
        searchVo.setLoanId(loanId);
        // 设置财务类型
        searchVo.setFinancialType(financialType.getValue());
        List<ThirdLineOffer> offerList = haTwoOfferDao.findListByVo(searchVo);
        if(CollectionUtils.isNotEmpty(offerList)){
            return offerList.get(0);
        }
        return null;
    }

    @Override
    public Pager searchOffLineLoanInfoWithPg(Map<String, Object> paramMap) {
        return haTwoOfferDao.searchOffLineLoanInfoWithPg(paramMap);
    }

	@Override
	public Boolean isApplyThirdLineGrant(Long loanId) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("loanId", loanId);
		List<ThirdLineOffer> list = haTwoOfferDao.findListByMap(paramMap);
		if(CollectionUtils.isEmpty(list)){//没有线下放过款,可以进行线上放款
			return true;
		}else{//线下有放款
			ThirdLineOffer thirdLineOffer = list.get(0);//渤海2和华瑞渤海只有一条
			if("扣款失败".equals(thirdLineOffer.getState())){//可以进行线上放款
				//可以进行线上放款
				return true;
			}else{//不可进行线上放款
				return false;
			}
		}
	}

}
