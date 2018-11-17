package com.zdmoney.credit.loan.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.dao.LoanRepaymentDetailDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanLedgerDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.domain.BaseMessage;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;

@Service
@Transactional
public class LoanLedgerServiceImpl  implements ILoanLedgerService{
	
	private static final Logger logger = Logger.getLogger(LoanLedgerServiceImpl.class);
	
	@Autowired
    private  IAfterLoanService afterLoanService;
	@Autowired
    private  IOfferRepayInfoService offerRepayInfoService;
	@Autowired
	private ILoanLedgerDao loanLedgerDao ;
	@Autowired
	private ILoanRepaymentDetailDao loanRepaymentDetailDao;
	@Autowired
	private IVLoanInfoService vLoanInfoService;
	@Autowired
	IComEmployeeDao comEmployeeDao;
	@Autowired
	IBaseMessageService  baseMessageServiceImpl;
	@Autowired
	IPersonInfoService personInfoService;
	
	@Override
	public List<LoanLedger> getLoanLedgerHasBalanceByDate(int promiseReturnDate) {
		return loanLedgerDao.getLoanLedgerHasBalanceByDate(promiseReturnDate);
	}


	@Override
	public LoanLedger findByLoanId(Long id) {
		return loanLedgerDao.findByLoanId(id);
	}


	@Override
	public LoanLedger findByAccount(String account) {
		return loanLedgerDao.findByAccount(account);
	}
	
	@Override
	public LoanLedger findByLoanIdForUpdate(Long id) {
		return loanLedgerDao.findByLoanIdForUpdate(id);
	}


	@Override
	public LoanLedger findByAccountForUpdate(String account) {
		return loanLedgerDao.findByAccountForUpdate(account);
	}


	@Override
	public int update(LoanLedger led) {
		return loanLedgerDao.update(led);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveNow(LoanLedger led) {
		loanLedgerDao.insert(led);
	}

	public List<LoanLedger> getLoanLedgerInfoByLoanBelong(Map<String, Object> params) {
		return loanLedgerDao.getLoanLedgerInfoByLoanBelong(params);
	}
	@Override
	public int updateOtherPayableByLoanId(LoanLedger loanLedger) {
		return loanLedgerDao.updateOtherPayableByLoanId(loanLedger) ;
	}


	@Override
	public void dealBigAmount() {
		//查询出挂账金额大于0正常的数据
		List<LoanLedger> list= loanLedgerDao.findMoreThanZero();
		logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆挂账金额大于0的数据数量："+list.size());
		if(CollectionUtils.isNotEmpty(list)){
			for (LoanLedger loanLedger:list) {
				VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanLedger.getLoanId());
				Map<String, String> map = new HashMap<String,String>();		
				String returnDate =Dates.getDateTime(afterLoanService.getCurrTermReturnDate(new Date(), vLoanInfo.getPromiseReturnDate()), "yyyy-MM-dd") ;
				map.put("returnDate", returnDate);
				map.put("loanId", loanLedger.getLoanId().toString());
				LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findRepaymentDetailByLoanAndReturnDate(map);
				if(null==loanRepaymentDetail){
					continue;
				}
				if(loanLedger.getAmount().compareTo(loanRepaymentDetail.getReturneterm().multiply(new BigDecimal(6)))>=0 || 
						loanLedger.getAmount().compareTo(loanRepaymentDetail.getRepaymentAll().subtract(loanRepaymentDetail.getReturneterm().subtract(loanRepaymentDetail.getDeficit())))>=0){//挂账金额大于等于申请每月应还金额的6倍，或挂账金额大于等于当期一次性结清金额
					if (loanLedger.getAmount().compareTo(loanRepaymentDetail.getReturneterm())==0) {//等于当期金额则是最后一期
						continue;
					}
					//在核心系统消息中心显示
					BaseMessage baseMessage=new BaseMessage();
					PersonInfo personInfo = personInfoService.findById(vLoanInfo.getBorrowerId());
					Long orgId = vLoanInfo.getSalesDepartmentId();
					List<ComEmployee> comEmployees=comEmployeeDao.finJlFlByOrgId(orgId);
					if(CollectionUtils.isNotEmpty(comEmployees)){
						for (int i = 0; i < comEmployees.size(); i++) {
							ComEmployee comEmployee = comEmployees.get(i);
							baseMessage.setReceiver(comEmployee.getId());//收件人
							baseMessage.setTitle("挂账金额较大通知");
							baseMessage.setContent(personInfo.getName()+"客户,"+"合同号"+vLoanInfo.getContractNum()+",挂账金额较大，请及时跟进处理。");
							baseMessage.setState("1");//未读
							baseMessage.setType("0");//类型（0系统通知，1信件,2回盘提醒）
							baseMessage.setSendTime(new Date());
							baseMessageServiceImpl.inserBaseMessage(baseMessage);
						}
					}			
				}				
			}
		}		
	}
}
