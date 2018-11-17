package com.zdmoney.credit.offer.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanLog;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.offer.service.pub.IOfferService;
import com.zdmoney.credit.person.dao.pub.IPersonThirdPartyAccountDao;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zendaimoney.thirdpp.rmi.service.request.ThirdPaymentService;
import com.zendaimoney.thirdpp.vo.ActBindBankVo;
import com.zendaimoney.thirdpp.vo.ProcessResultInfoVo;
import com.zendaimoney.thirdpp.vo.RequestVo;
import com.zendaimoney.thirdpp.vo.enums.IsAsyn;
import com.zendaimoney.thirdpp.vo.enums.IsReSend;
import com.zendaimoney.thirdpp.vo.enums.RequestType;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;
import com.zendaimoney.thirdpp.vo.enums.ZendaiSys;

@Service
public class OfferServiceImpl implements IOfferService {

	@Autowired
	IPersonThirdPartyAccountDao personThirdPartyAccountDao;//第三方账户信息操作DAO
	
	@Autowired
	ISequencesService sequencesService;//数据库sequences操作Service
	@Autowired(required = false)
	ThirdPaymentService thirdPaymentService;
	@Autowired
	ILoanLogService loanLogService;//债权日志操作Service
	
	/**
	 * 核心接口开户绑卡
	 * @param personInfo
	 * @param loanBank
	 * @param comEmployee
	 * @return
	 */
	@Override
	public String openCoreAccount(PersonInfo personInfo, LoanBank loanBank, ComEmployee operator, String bankMobile) {
		
		RequestVo requestVo = new RequestVo();
		requestVo.setAsynTag(IsAsyn.IS_ASYN_TAG_0);//0 非异步 不需要回调 1 异步 需要回调
        requestVo.setReSendTag(IsReSend.IS_RESEND_TAG_0);//不需要重发
        requestVo.setRequestType(RequestType.REQUEST_TYPE_00);//请求类型
        requestVo.setRequestOperator("mccc");//操作员
        requestVo.setRequestSystem(ZendaiSys.ZENDAI_2004_SYS);//业务系统
        requestVo.setThirdPartyType(ThirdPartyType.YYoupay);//第三方
        requestVo.setRequestDate(new Date());
        
        ActBindBankVo actBindBankVo = new ActBindBankVo();
        //actBindBankVo.setTpCode("123132");//信贷系统ID ，商户号
        String creditUserId = sequencesService.getSequences(SequencesEnum.CREDIT_USER_TPP).toString();
        actBindBankVo.setCreditUserId(creditUserId);//信贷客户id ，ordNo
        actBindBankVo.setUserName(personInfo.getName());//姓名
        actBindBankVo.setSex("男".equals(personInfo.getSex()) ? "00":"01");//性别（00男，01女）
        actBindBankVo.setNation("汉");// 民族
        actBindBankVo.setIdCard(personInfo.getIdnum());// 身份证号
        actBindBankVo.setMobile(personInfo.getMphone());//客户手机号
        actBindBankVo.setAccountNo(loanBank.getAccount());//银行卡号
        actBindBankVo.setOpenBank(loanBank.getBranchBankCode());//开户行号（支行）
        actBindBankVo.setBankBindMobile(bankMobile != null ? bankMobile : personInfo.getMphone());//银行预留手机号
        actBindBankVo.setBankCode(loanBank.getBankCode());//总行名称
        
        requestVo.getRequestDetailInfo().getRequestDetails().add(actBindBankVo);
        
        /**开户绑卡**/
        RequestVo returnRequestVo = null;
//        RequestVo returnRequestVo = thirdPaymentService.openActBindBank(requestVo);
        if (true) {
    		throw new PlatformException(ResponseEnum.FULL_MSG,"为了适应接口切换，取消openCoreAccount接口调用(RMI接口)");
    	}
        ActBindBankVo actbinbank = (ActBindBankVo)returnRequestVo.getRequestDetailInfo().getRequestDetails().get(0);
        ProcessResultInfoVo processresultinfo = actbinbank.getProcessResultInfo();
        processresultinfo.getTppReturnCode(); //获取返回码 000000处理成功  111111处理失败
        processresultinfo.getTppResaon(); //操作描述
        processresultinfo.getThirdPartyBackInfo(); //第三方返回信息，成功无信息，失败有描述

        String errorMsg = null;

        if("000000".equals(processresultinfo.getTppReturnCode())) {
            //当操作成功才会有客户登录名和密码
        	PersonThirdPartyAccount tpAcct = buildThirdPartyAccount(actbinbank.getLoginName(), actbinbank.getPassword(), actbinbank.getCjzfId(), personInfo, loanBank, creditUserId);
        	personThirdPartyAccountDao.insert(tpAcct);
        	
        	LoanLog loanLog = new LoanLog();
        	loanLog.setCreator(operator.getId().toString());
        	loanLog.setCreateTime(new Date());
        	loanLog.setContent("开户绑卡成功");
        	loanLog.setLogName("OfferServiceImpl");
        	loanLog.setLogType("info");
        	loanLogService.createLog(loanLog);
        } else {
            errorMsg = processresultinfo.getTppResaon() + "," + processresultinfo.getThirdPartyBackInfo();
            
            LoanLog loanLog = new LoanLog();
        	loanLog.setCreator(operator.getId().toString());
        	loanLog.setCreateTime(new Date());
        	loanLog.setContent("开户绑卡失败,"+errorMsg);
        	loanLog.setLogName("OfferServiceImpl");
        	loanLog.setLogType("info");
        	loanLogService.createLog(loanLog);
        }
        
        return errorMsg;
	}
	
	/**
	 * 构建第三方绑卡信息
	 * @param login
	 * @param password
	 * @param thirdPartyId
	 * @param person
	 * @param bank
	 * @param creditUserTpp
	 * @return
	 */
	private PersonThirdPartyAccount buildThirdPartyAccount(String login, String password, String thirdPartyId, PersonInfo person, LoanBank bank, String creditUserTpp){
		
		PersonThirdPartyAccount tpAcct = new PersonThirdPartyAccount();
		tpAcct.setId(sequencesService.getSequences(SequencesEnum.PERSON_THIRD_PARTY_ACCOUNT));
        tpAcct.setLogin(login);
        tpAcct.setPassword(password);
        tpAcct.setThirdPartyId(thirdPartyId);
        tpAcct.setName(person.getName());
        tpAcct.setIdnum(person.getIdnum());
        tpAcct.setMphone(person.getMphone());

        tpAcct.setAccount(bank.getAccount());
        tpAcct.setBankCode(bank.getBankCode());
        tpAcct.setBranchBankCode(bank.getBranchBankCode());
        tpAcct.setBankName(bank.getBankName());
        tpAcct.setFullBankName(bank.getFullName());
        tpAcct.setType(ThirdPartyType.YYoupay.toString());
        tpAcct.setCreditUserTpp(creditUserTpp);
        
        return tpAcct;
    }

}
