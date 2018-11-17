package com.zdmoney.credit.repay.service;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.repay.service.pub.IRepayTrailService;
import com.zdmoney.credit.repay.vo.RepayTrailVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class RepayTrailServiceImpl implements IRepayTrailService{
	@Autowired
	private IAfterLoanService afterLoanService;

	@Autowired
	private IVLoanInfoService vLoanInfoService;

	@Autowired
	private IPersonInfoService personInfoService;
	@Override
	public Pager findWithPg(RepayTrailVo repayTrailVo) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 获取还款明细信息
	 * @param vLoanInfo
	 * @param tradeDate
	 * @param repayType
	 * @return
	 */
	public RepayTrailVo getRepaymentDetailInfo(VLoanInfo vLoanInfo, Date tradeDate, String repayType) {
		RepayTrailVo repayTrailVo=new RepayTrailVo();
		// 债权编号
		Long loanId = vLoanInfo.getId();
		// 查询借款客户信息
		PersonInfo person = personInfoService.findById(vLoanInfo.getBorrowerId());
		// 客户姓名
		repayTrailVo.setName(person.getName());
		// 证件类型
		repayTrailVo.setIdType("0");
		// 证件号码
		repayTrailVo.setIdNum(person.getIdnum());
		// 移动电话
		repayTrailVo.setMphone(person.getMphone());
		// 借款类型
		repayTrailVo.setLoanType(vLoanInfo.getLoanType());
		// 合同金额
		repayTrailVo.setPactMoney(vLoanInfo.getPactMoney());
		// 合同编号
		repayTrailVo.setContractNum(vLoanInfo.getContractNum());
		// 借款状态
		repayTrailVo.setLoanState(vLoanInfo.getLoanState());
		// 借款期数
		repayTrailVo.setTime(vLoanInfo.getTime().intValue());
		// 获取挂账金额
		BigDecimal accAmount = afterLoanService.getAccAmount(loanId);
		repayTrailVo.setAccAmount(accAmount);
		// 还款类型
		if("一次性".equals(repayType)){
			repayTrailVo.setRequestState("已申请");
		}else{
			repayTrailVo.setRequestState("未申请");
		}
		// 获取未还款信息
		List<LoanRepaymentDetail> repayList= afterLoanService.getAllInterestOrLoan(tradeDate, loanId);
		if(CollectionUtils.isNotEmpty(repayList)){
			/** 获取计算器实例 **/
			ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
			// 以下获取逾期还款信息
			int overDueTerm = afterLoanService.getOverdueTermCount(repayList,tradeDate);
			// 逾期总期数
			repayTrailVo.setOverDueTerm(overDueTerm);
			// 逾期本金
			BigDecimal overCorpus = afterLoanService.getOverdueCorpus(repayList, tradeDate);
			repayTrailVo.setOverCorpus(overCorpus);
			// 逾期起始日
			repayTrailVo.setOverDueDate(overDueTerm<1?null:repayList.get(0).getReturnDate());
			// 逾期利息
			BigDecimal overInterest = afterLoanService.getOverdueInterest(repayList,tradeDate);
			repayTrailVo.setOverInterest(overInterest);
			// 以下获取逾期罚息相关信息
			int fineDay = afterLoanService.getOverdueDay(repayList,tradeDate);
			// 罚息天数
			repayTrailVo.setFineDay(fineDay);
			// 罚息金额
			BigDecimal fine = afterLoanService.getFine(repayList, tradeDate);
			repayTrailVo.setFine(fine);
			// 罚息起算日
			repayTrailVo.setFineDate(fineDay<1?null:repayList.get(0).getPenaltyDate());
			// 以下获取当期的还款信息
			// 当期还款日期
			repayTrailVo.setCurrDate(repayList.get(repayList.size()-1).getReturnDate());
			// 当期期数
			repayTrailVo.setCurrTerm(repayList.get(repayList.size()-1).getCurrentTerm().intValue());
			// 当期本金
			BigDecimal currCorpus = new BigDecimal(0.0);
			if(vLoanInfo.getEndrdate().compareTo(tradeDate) >= 0){
				currCorpus = afterLoanService.getCurrCorpus(repayList,tradeDate);
			}
			repayTrailVo.setCurrCorpus(currCorpus);
			// 当期利息
			BigDecimal currInterest = new BigDecimal(0.0);
			if(vLoanInfo.getEndrdate().compareTo(tradeDate) >= 0){
				currInterest = afterLoanService.getCurrInterest(repayList,tradeDate);
			}
			repayTrailVo.setCurrInterest(currInterest);
			// 减免金额
			BigDecimal remnant = afterLoanService.getReliefOfFine(tradeDate,loanId);
			repayTrailVo.setRemnant(remnant);
			// 一次性结清金额
			BigDecimal oneTimeRepayment = calculatorInstace.getOnetimeRepaymentAmount(loanId, tradeDate, repayList);
			repayTrailVo.setOneTimeRepayment(oneTimeRepayment);
			// 剩余本金
			repayTrailVo.setResidualPactMoney(vLoanInfo.getResidualPactMoney());
			// 退费金额
			BigDecimal giveBackRate = afterLoanService.getGiveBackRate(repayList);
			repayTrailVo.setGiveBackRate(giveBackRate);
			// 违约金
			BigDecimal penalty = calculatorInstace.getPenalty(loanId, repayList, vLoanInfo);
			repayTrailVo.setPenalty(penalty);
			// 当期应还总额
			BigDecimal currAmount = new BigDecimal(0.0);
			if ("一次性".equals(repayType)) {
				currAmount = repayTrailVo.getOverInterest()
						.add(repayTrailVo.getCurrInterest())
						.add(repayTrailVo.getResidualPactMoney())
						.add(repayTrailVo.getFine())
						.add(repayTrailVo.getPenalty())
						.subtract(repayTrailVo.getGiveBackRate())
						.subtract(repayTrailVo.getAccAmount());
			} else {
				currAmount = repayTrailVo.getOverInterest()
						.add(repayTrailVo.getOverCorpus())
						.add(repayTrailVo.getCurrInterest())
						.add(repayTrailVo.getCurrCorpus())
						.add(repayTrailVo.getFine())
						.subtract(repayTrailVo.getAccAmount());
			}
			repayTrailVo.setCurrAmount(currAmount);
			if(repayTrailVo.getCurrAmount().compareTo(new BigDecimal(0))<0){
				repayTrailVo.setCurrAmount(new BigDecimal(0.0));
			}
		}
		repayTrailVo.setTradeDate(tradeDate);
		return repayTrailVo;
	}
}
