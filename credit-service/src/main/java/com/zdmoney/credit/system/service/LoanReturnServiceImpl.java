package com.zdmoney.credit.system.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanReturn;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.system.dao.pub.ILoanReturnDao;
import com.zdmoney.credit.system.service.pub.ILoanReturnService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanReturnServiceImpl implements ILoanReturnService{
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired
	ILoanReturnDao loanReturnDaoImpl;
	@Autowired
	ILoanBaseService loanBaseService;
	@Autowired 
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
	@Override
	public void insertLoanReturn(LoanReturn loanReturn) {
		loanReturn.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_RETURN));
		loanReturnDaoImpl.insert(loanReturn);
	}

	@Override
	public int countLoanReturn(LoanReturn loanReturn) {
		// TODO Auto-generated method stub
		return loanReturnDaoImpl.countLoanReturn(loanReturn);
	}


	  /**
     * 插入债权回购信息， 更新债权
     * @param vloanPersonInfo
     * @param map
     */
    @Transactional
	public void saveBuyBackLoanAndUpdateLoan(VLoanInfo vloanInfo, Map<String, String> map) {
		LoanReturn loanReturn = new LoanReturn();
		loanReturn.setAmount(new BigDecimal(map.get("amount")));
		loanReturn.setBatchNum(vloanInfo.getBatchNum());
		loanReturn.setFundsSources(map.get("fundsSources"));
		loanReturn.setImportReason("债权回购导入");
		loanReturn.setLoanId(vloanInfo.getId());
		Map<String, Object> lrParams = new HashMap<String, Object>();
		lrParams.put("repaymentStates", new String[]{RepaymentStateEnum.未还款.getValueName(),RepaymentStateEnum.不足额还款.getValueName()
				,RepaymentStateEnum.不足罚息.getValueName()});
		lrParams.put("loanId", vloanInfo.getId());
		List<LoanRepaymentDetail> lrList = loanRepaymentDetailServiceImpl.findByLoanIdAndRepaymentState(lrParams);
		if(CollectionUtils.isEmpty(lrList)){
			//没有逾期记录 记录当期
			loanReturn.setCurrentTerm(Long.valueOf(vloanInfo.getCurrentTerm()));	
		}else{
			loanReturn.setCurrentTerm(lrList.get(0).getCurrentTerm());
		}
		//保存loanReturn
	    insertLoanReturn(loanReturn);
	    //更新loan_base
	    LoanBase loanBase=new LoanBase();
	    loanBase.setId(vloanInfo.getId());
	    String loanBelongs = map.get("loanBelongs");
	    if(Strings.isEmpty(loanBelongs)){
	    	loanBelongs = FundsSourcesTypeEnum.证大P2P.getValue();
	    }
	    loanBase.setLoanBelong(loanBelongs);
	    loanBaseService.updateLoanBaseByBuyBackLoan(loanBase); 
	}
}
