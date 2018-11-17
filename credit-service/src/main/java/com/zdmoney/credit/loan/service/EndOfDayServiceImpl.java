package com.zdmoney.credit.loan.service;

import java.util.Calendar;
import java.util.List;

import com.zdmoney.credit.ljs.service.pub.ILoanStatusLufaxService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IEndOfDayService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;


@Service
public class EndOfDayServiceImpl implements  IEndOfDayService{
	
	private static final Logger logger = Logger.getLogger(EndOfDayServiceImpl.class);
	
	static boolean transactional =true;
	
	@Autowired
    private  IAfterLoanService afterLoanService;
	@Autowired
    private  IVLoanInfoService vLoanInfoService;
	@Autowired
    private  ILoanBaseService loanBaseService;
	
	@Autowired
	private ILoanLedgerService loanLedgerService;
	
	@Autowired
	private IOfferRepayInfoService offerRepayInfoService;
	
	@Autowired
	ILoanSpecialRepaymentService loanSpecialRepaymentService;
	@Autowired
	ILoanLogService loanLogService;
	@Autowired
	ILoanStatusLufaxService loanStatusLufaxService;
	
	/**
	 * 日终处理
	 */
    @Override
	public void timerProcess()
    {
    	Calendar  tradeDate = Calendar.getInstance();
        int day= tradeDate.get(Calendar.DAY_OF_MONTH);
        for(int promisereturnDate : Const.PROMISERETURNDATE){
        	if(promisereturnDate==day){//如果是还款日
        		loanLogService.createLog("timerProcess", "info", "1或16日还款批处理开始........", "SYSTEM");
				repaymentDayDeal(tradeDate);
				loanStateUpdate();//改逾期状态
                updateSpecialRepaymentState(day);//更新 申请了提前扣款 而未还款客户的申请状态
                drawRiskFeeBatch(tradeDate);// 逾期客户提风险金（保证金）
                loanLogService.createLog("timerProcess", "info", "1或16日还款批处理结束........", "SYSTEM");
        	}
        }
        
        
    }

    /**
     * 还款日挂账部分处理
     * @param tradeDate
     */
    private void repaymentDayDeal(Calendar tradeDate)
    {
    	loanLogService.createLog("timerProcess", "info", "还款日挂账部分处理开始........", "SYSTEM");
         try {
			int promiseReturnDate = tradeDate.get(Calendar.DAY_OF_MONTH);//
			//获取有挂账的贷款户ID
			List<LoanLedger> list = loanLedgerService
					.getLoanLedgerHasBalanceByDate(promiseReturnDate);
			if (list == null) {
				logger.warn("还款日挂账部分处理:该日无数据！");
				return;
			}
			for (LoanLedger loanLedger : list) {
				try {
					offerRepayInfoService.createAccountingByRepaymentDayDeal(
							loanLedger, tradeDate);
				}
				catch(PlatformException ex){
		        	if(ex.getResponseCode().name().equals(ResponseEnum.SYS_WARN.name())){
		        		  //可以忽略的异常，不打印
		        	}else{
		        		logger.error(ex.getMessage());
		        	}
		        } catch (Exception e) {
					logger.error(e.getMessage());

				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
        loanLogService.createLog("timerProcess", "info", "还款日挂账部分处理结束........", "SYSTEM");
    }
    
    
    /**
     * 更新还款日未还款的贷款户的状态
     */
    private void loanStateUpdate()
    {
        try {
			loanBaseService.updateLoanStateAtEndOfDay();
			loanStatusLufaxService.updateLufuxStatusToYuqi(); //更新loanStatusLufax表状态
		} catch (Exception e) {
			logger.error("更新还款日未还款的贷款户状态失败："+e.getMessage(),e);
			loanLogService.createLog("timerProcess", "error", "更新还款日未还款的贷款户状态失败："+e.getMessage(), "SYSTEM");
		}
    }
    
    /**
    * 更新 申请了提前扣款 而未还款客户的申请状态
    * 
    * */
    private void updateSpecialRepaymentState(int promiseReturnDate1)
    {
    	try {
			loanSpecialRepaymentService.updateSpecialRepaymentStateAtEndOfDay(promiseReturnDate1);
		} catch (Exception e) {
			logger.error("更新 申请了提前扣款 而未还款客户的申请状态："+e.getMessage(),e);
			loanLogService.createLog("timerProcess", "error", "更新 申请了提前扣款 而未还款客户的申请状态："+e.getMessage(), "SYSTEM");
		}
    	
    }

    /**
    *	逾期客户提风险金（保证金）
    */
    private void drawRiskFeeBatch(Calendar tradeDate)
    {
    	loanLogService.createLog("timerProcess", "info", "1或16日风险金处理开始........", "SYSTEM");
        try {
			int promiseReturnDate = tradeDate.get(Calendar.DAY_OF_MONTH);//Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
			for (int promisereturnDate : Const.PROMISERETURNDATE) {
				if (promisereturnDate == promiseReturnDate) {//如果是还款日
					List<VLoanInfo> list = vLoanInfoService
							.getYQHKLoanByPromisereturnDate(promisereturnDate);

					for (VLoanInfo vLoanInfo : list) {
						try {
							afterLoanService.drawRiskFee(vLoanInfo, tradeDate);
						} catch (Exception ex) {
							logger.error("日终处理：逾期客户提风险金（保证金）异常! loanId="
									+ vLoanInfo.getId() + ex.getMessage(), ex);
							loanLogService.createLog(
									"EndOfDay",
									"error",
									"日终处理：逾期客户提风险金（保证金）异常! loanId="+ vLoanInfo.getId()+ ex.getMessage(), "SYSTEM");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("日终处理：逾期客户提风险金（保证金）异常!"+ e.getMessage(), e);
		}
		loanLogService.createLog("timerProcess", "info", "1或16日风险金批处理结束........", "SYSTEM");
    }

   
}
