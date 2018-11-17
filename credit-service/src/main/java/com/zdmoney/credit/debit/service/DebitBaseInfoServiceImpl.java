package com.zdmoney.credit.debit.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitTransactionDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.IDebitBaseInfoService;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * @ClassName:     DebitBaseInfoServiceImpl.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2017年3月22日 下午4:24:19
 */
@Service
public class DebitBaseInfoServiceImpl implements IDebitBaseInfoService{
	
	@Autowired
	IPersonInfoService personInfoService;
	
	@Autowired
	ILoanBankService loanBankService;
	
	@Autowired
	ISequencesService sequencesService;
	
	@Autowired
	IDebitBaseInfoDao debitBaseInfoDao; 
	
	@Autowired
	IDebitTransactionDao debitTransactionDao;
	
	@Autowired
	IOfferTransactionService offerTransactionServiceImpl;

	@Override
	public DebitBaseInfo createThirdOffer(VLoanInfo loan, BigDecimal offerAmount) {
		// 得到借款人
		PersonInfo personInfo = personInfoService.findById(loan.getBorrowerId());
		// 得到还款银行卡信息
		LoanBank giveBackBank = loanBankService.findById(loan.getGiveBackBankId());
		DebitBaseInfo offer = new DebitBaseInfo();
		offer.setId(sequencesService.getSequences(SequencesEnum.DEBIT_BASE_INFO));
		offer.setLoanId(loan.getId());
		offer.setPactNo(loan.getContractNum());
		offer.setIdType(personInfo.getIdtype());
		offer.setIdNo(personInfo.getIdnum());
		offer.setCustName(personInfo.getName());
		offer.setOfferDate(Dates.getCurrDate());
		offer.setAmount(offerAmount);
		offer.setOfferAmount(offerAmount);
		offer.setActualAmount(new BigDecimal(0));
		offer.setAcctNo(giveBackBank.getAccount().trim());
		offer.setAcctType("00");
		offer.setAcctName(personInfo.getName());
		offer.setBankCode(giveBackBank.getBankCode());
		offer.setBankName(giveBackBank.getBankName());
		offer.setPaySysNo(TppPaySysNoEnum.包商银行.getCode());
		offer.setType(OfferTypeEnum.实时划扣.getValue());
		offer.setState(OfferStateEnum.未报盘.getValue());
		offer.setCreateTime(new Date());
		offer.setCreator("admin");
		debitBaseInfoDao.insert(offer);
		return offer;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void createCallbackThirdOffer(VLoanInfo loan, BigDecimal offerAmount, boolean flag, String failReason) {
		// 得到借款人
		PersonInfo personInfo = personInfoService.findById(loan.getBorrowerId());
		// 得到还款银行卡信息
		LoanBank giveBackBank = loanBankService.findById(loan.getGiveBackBankId());
		DebitBaseInfo offer = new DebitBaseInfo();
		offer.setId(sequencesService.getSequences(SequencesEnum.DEBIT_BASE_INFO));
		offer.setLoanId(loan.getId());
		offer.setPactNo(loan.getContractNum());
		offer.setIdType(personInfo.getIdtype());
		offer.setIdNo(personInfo.getIdnum());
		offer.setCustName(personInfo.getName());
		offer.setOfferDate(Dates.getCurrDate());
		offer.setAmount(offerAmount);
		offer.setOfferAmount(flag == true ? BigDecimal.ZERO : offerAmount);
		offer.setActualAmount(flag == true ? offerAmount : BigDecimal.ZERO);
		offer.setAcctNo(giveBackBank.getAccount().trim());
		offer.setAcctType("00");
		offer.setAcctName(personInfo.getName());
		offer.setBankCode(giveBackBank.getBankCode());
		offer.setBankName(giveBackBank.getBankName());
		offer.setPaySysNo(TppPaySysNoEnum.包商银行.getCode());
		offer.setType(OfferTypeEnum.自动划扣.getValue());
		offer.setState(flag == true ? OfferStateEnum.已回盘全额.getValue() : OfferStateEnum.已回盘非全额.getValue());
		offer.setCreateTime(new Date());
		offer.setCreator("admin");
		offer.setUpdateTime(offer.getCreateTime());
		offer.setUpdator("admin");
		offer.setMemo(failReason);
		offer.setMemo(flag == true ? "交易成功" : failReason);
		debitBaseInfoDao.insert(offer);
		
		DebitTransaction transaction = new DebitTransaction();
		transaction.setId(sequencesService.getSequences(SequencesEnum.DEBIT_TRANSACTION));
		transaction.setDebitId(offer.getId());
		transaction.setSerialNo(transaction.getId().toString());
		transaction.setLoanId(offer.getLoanId());
		transaction.setPayAmount(offer.getAmount());
		transaction.setActualAmount(offer.getActualAmount());
		transaction.setReqTime(new Date());
		transaction.setResTime(transaction.getReqTime());
		transaction.setState(flag == true ? OfferTransactionStateEnum.扣款成功.getValue() : OfferTransactionStateEnum.扣款失败.getValue());
		transaction.setRtnCode(flag == true ? ReturnCodeEnum.交易成功.getCode() : ReturnCodeEnum.交易失败.getCode());
		transaction.setCreateTime(new Date());
		transaction.setCreator("admin");
		transaction.setUpdateTime(transaction.getCreateTime());
		transaction.setUpdator("admin");
		transaction.setMemo(flag == true ? "交易成功" : failReason);
		debitTransactionDao.insert(transaction);
		
		//跨日回盘 添加到消息中心
        offerTransactionServiceImpl.addBaseMessage(offer.getOfferDate(), offer.getLoanId());
	}
}
