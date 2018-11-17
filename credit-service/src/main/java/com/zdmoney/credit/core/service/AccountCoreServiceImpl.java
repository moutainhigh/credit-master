package com.zdmoney.credit.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.AccountVo;
import com.zdmoney.credit.core.service.pub.IAccountCoreService;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanLog;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.offer.dao.pub.IOfferYongyoubankBranchDao;
import com.zdmoney.credit.offer.dao.pub.IOfferYongyoubankHeadDao;
import com.zdmoney.credit.offer.domain.OfferYongyoubankBranch;
import com.zdmoney.credit.offer.domain.OfferYongyoubankHead;
import com.zdmoney.credit.offer.service.pub.IOfferService;
import com.zdmoney.credit.person.dao.pub.IPersonThirdPartyAccountDao;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class AccountCoreServiceImpl implements IAccountCoreService{

	private static final Logger logger = Logger.getLogger(AccountCoreServiceImpl.class);
	
	@Autowired
	IComEmployeeDao comEmployeeDao;//员工信息操作DAO
	@Autowired
	IOfferYongyoubankHeadDao offerYongyoubankHeadDao;//用友总行操作DAO
	@Autowired
	IOfferYongyoubankBranchDao offerYongyoubankBranchDao;//用友支行操作DAO
	@Autowired
	IPersonThirdPartyAccountDao personThirdPartyAccountDao;//第三方账户信息操作DAO
	@Autowired
	ILoanBankDao loanBankDao;//银行操作DAO
	
	@Autowired
	ISequencesService sequencesService;//数据库sequences操作Service
	@Autowired
	IOfferService OfferService;//绑卡操作Service
	@Autowired
	ILoanLogService loanLogService;//债权日志操作Service
	
	/**
	 * 绑卡验证接口 (征审系统发送将卡号信息发送到财务核心系统进项验证，返回账号信息)
	 * @param params
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> createYYAcount(AccountVo params) {
		Map<String, Object> json = null;
		
		/**校验银行信息是否存在**/
		Map<String, Object> yongyouMap = getOfferYongYouBank(params.getBankCode(), params.getBranchBankCode());
		if (!(boolean)yongyouMap.get("flag")) {
			json = MessageUtil.returnErrorMessage("失败:您输入的银行不存在！");
            return json;
		}
		
		/**封装借款人信息**/
		PersonInfo personInfo = new PersonInfo();
		personInfo.setIdnum(params.getIdnum());
		personInfo.setMphone(params.getMphone());
		personInfo.setName(params.getName());
		personInfo.setSex(params.getSex());
		
		/**封装银行信息**/
		LoanBank loanBank = getBankInfo(params, yongyouMap);
		
		/**查询操作人员**/
		ComEmployee operator = comEmployeeDao.findEmployeeByUserCode(params.getUserCode());
		
		/**查询帮信息，如果没有进行过绑卡，则进行绑卡**/
		PersonThirdPartyAccount thirdPartyAccount = personThirdPartyAccountDao.findByAccount(params.getAccount());
		String errorMsg = null;
		if (null == thirdPartyAccount) {
			loanBankDao.insert(loanBank);
			
			errorMsg = OfferService.openCoreAccount(personInfo, loanBank, operator, null);
		}
		
		if (null == errorMsg) {
			if (null != thirdPartyAccount) {
				LoanLog loanLog = new LoanLog();
            	loanLog.setCreator(operator.getId().toString());
            	loanLog.setCreateTime(new Date());
            	loanLog.setContent("开户绑卡，恢复开户绑卡的银行卡信息");
            	loanLog.setLogName("AccountCoreServiceImpl");
            	loanLog.setLogType("info");
            	loanLogService.createLog(loanLog);
            	
            	json = MessageUtil.returnHandleSuccessMessage();
                json.put("thirdPartyId", thirdPartyAccount.getThirdPartyId());
			} else {
				thirdPartyAccount = personThirdPartyAccountDao.findByAccount(params.getAccount());
				
				json = MessageUtil.returnHandleSuccessMessage();
                json.put("thirdPartyId", thirdPartyAccount.getThirdPartyId());
			}
		} else {
			logger.error("开户绑卡失败，"+errorMsg);
			json = MessageUtil.returnErrorMessage("失败:"+errorMsg);
		}
		
		return json;
	}
	
	/**
     * 获得 OFFER_YONGYOUBANK_HEAD，OFFER_YONGYOUBANK_BRANCH的信息，并且获得银行是否存在的状态
     * @param params params.bankCode, params.branchBankCode
     * @return map key： head，branch，flag
     */
    private Map<String, Object> getOfferYongYouBank(String bankCode, String branchBankCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean flag = false;
        OfferYongyoubankHead head = offerYongyoubankHeadDao.findByCode(bankCode);
        OfferYongyoubankBranch branch = offerYongyoubankBranchDao.findByBankId(branchBankCode);
        if (null != head && null != branch && head.getCode().equals(branch.getBankCode().substring(0,3))) {
            flag = true;
        }
        map.put("head", head);
        map.put("branch", branch);
        map.put("flag", flag);
        return map;
    }
    
    /**
     * 获得封装后的LoanBank对象信息
     * @param params
     * @param map
     * @return
     */
    private LoanBank getBankInfo(AccountVo params, Map<String,Object> map){
    	OfferYongyoubankHead head = (OfferYongyoubankHead)map.get("head");
    	OfferYongyoubankBranch branch = (OfferYongyoubankBranch)map.get("branch");
    	
        LoanBank bank = new LoanBank();
        bank.setId(sequencesService.getSequences(SequencesEnum.LOAN_BANK));
        bank.setBankName(head.getName());
        bank.setFullName(branch.getBankName());
        bank.setBankCode(params.getBankCode());
        bank.setBranchBankCode(params.getBranchBankCode());//数据库中可能查找不到支行信息
        bank.setBankDicType(params.getBankDicType());
        bank.setAccount(params.getAccount());

        return bank;
    }
/*
	@Override
	public Map<String, Object> createYYAcount(AccountVo params) {
		Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> map = this.getDic21AndDic22AndFlag(params);
        if ((Boolean)map.get("flag")==false) {
            json = MessageUtil.returnErrorMessage("失败:您输入的银行不存在！");
            return json;
        }

        //def loan = Loan.get(Long.parseLong(params.loanId));
        Loan loan = new Loan()
        Person person  = new Borrower();
        person.setIdnum(params.idnum)
        person.setMphone(params.mphone)
        person.setName(params.name)
        person.setSex(params.sex)
        loan.borrower = person
        Bank bank = this.getBankInfo(params, map);

        def errorMsg;
        ComEmployee employee = Employee.findByUsercode(params.userCode)
        PersonThirdPartyAccount exsistsAccount = ThirdPartyAccount.findByAccount(params.account)
        if (exsistsAccount == null) {
            if(bank.save()){
                //loan.borrower.addToBanks(bank).save(flush: true);
            } else{
                json = MessageUtil.returnErrorMessage("失败:绑卡验证失败！");
                return json;
            }

            errorMsg = offerService.openCoreAccount(loan,bank, employee);
        }

        if (errorMsg == null) {
            if (exsistsAccount != null) {
                new LoanLog(operator: employee, logTime: new Date(), content: "开户绑卡，恢复开户绑卡的银行卡信息").save(flush: true);
                json = MessageUtil.returnHandleSuccessMessage();
                json.put("thirdPartyId", exsistsAccount.thirdPartyId);
            } else {
                exsistsAccount = ThirdPartyAccount.findByAccount(params.account);
                json = MessageUtil.returnHandleSuccessMessage();
                json.put("thirdPartyId", exsistsAccount.thirdPartyId);
            }
        } else {
            json = MessageUtil.returnErrorMessage("失败:开户绑卡失败，${errorMsg}");
        }
        return json;
	}

	private Bank getBankInfo(AccountVo params, Map<String, Object> map) {
		BankDic21 dic21 = map.dic21;
        BankDic22 dic22 = map.dic22;
        Bank bank = new Bank();
        bank.bankName = dic21.name;
        bank.fullName = dic22.bankName;
        bank.bankCode = params.bankCode;
        bank.branchBankCode = params.branchBankCode;//数据库中可能查找不到支行信息
        bank.bankDicType = params.bankDicType;
        bank.account = params.account;
        bank.mphone = params.bmphone;

        return bank;
	}

	private Map<String, Object> getDic21AndDic22AndFlag(AccountVo params) {
		 Map<String, Object> map = new HashMap<String, Object>();
	        boolean flag = false;
	        BankDic21 dic21 = BankDic21.findByCode(params.bankCode);
	        BankDic22 dic22 = BankDic22.findByBankId(params.branchBankCode);
	        if (dic21 != null && dic22 != null && dic22.bankCode.substring(0,3) == dic21.code) {
	            flag = true;
	        }
	        map.put("dic21", dic21);
	        map.put("dic22", dic22);
	        map.put("flag", flag);
	        return map;
	}
*/
}
