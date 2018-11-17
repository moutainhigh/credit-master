package com.zdmoney.credit.repay.service;

import com.zdmoney.credit.common.constant.*;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.MapUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferRepayInfoDao;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.repay.dao.pub.ICustomerAccountManagerDao;
import com.zdmoney.credit.repay.service.pub.ICustomerAccountManagerService;
import com.zdmoney.credit.repay.vo.*;
import com.zdmoney.credit.system.service.pub.ISequencesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("customerAccountManagerService")
public class CustomerAccountManagerServiceImpl implements ICustomerAccountManagerService{

	@Autowired 
	@Qualifier("customerAccountManagerDao")
	private ICustomerAccountManagerDao customerAccountManagerDao;
	
	@Autowired
	private IAfterLoanService afterLoanService;
	
	@Autowired
	private ISequencesService sequencesService;
	
	@Autowired
	private IOfferRepayInfoDao offerRepayInfoDao;

	@Autowired
	private ILoanBaseService loanBaseService;

	@Autowired
	private ILoanSpecialRepaymentService loanSpecialRepaymentService;
	
	@Autowired
	ILoanTransferInfoService loanTransferInfoService;
	
	/**
	 * 查询客户账户信息列表
	 * @author 00236633
	 * @param vCustomerAccountManagerList
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> list(VCustomerAccountManagerList vCustomerAccountManagerList) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();

		//将值对象转换成map
		MapUtil.vObjectConvertToMap(vCustomerAccountManagerList, params, false);
		
		int pageSize = vCustomerAccountManagerList.getRows();
		int page = vCustomerAccountManagerList.getPage() ;
		int startRow = (page-1)*pageSize+1;
		int endRow = page*pageSize;
		params.put("startRow", startRow);
		params.put("endRow", endRow);
		
		List<Map<String,Object>> loanFilesInfoList = customerAccountManagerDao.findCustomerAccountInfoList(params);
		int loanFilesInfoCount = customerAccountManagerDao.findCustomerAccountInfoCount(params);
		
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		for(Map<String,Object> map: loanFilesInfoList){
			if(map.get("accountBalance")!=null){
				try {
					double accountBalance = Double.parseDouble(map.get("accountBalance")+"");
					map.put("accountBalance", decimalFormat.format(accountBalance));
					Long loanId = Long.valueOf(map.get("accountId").toString());
					BigDecimal oneTimeRepaymentAll = getOneTimeRepaymentAll(loanId);
					map.put("oneTimeRepaymentAll",oneTimeRepaymentAll);
				} catch (NumberFormatException e) {
				}
			}
		}
		
		result.put("customerAccountManagerList", loanFilesInfoList);
		result.put("customerAccountManagerCount", loanFilesInfoCount);
		result.put("success", true);
		result.put("message", "查询成功");
		
		return result;
	}

	/**
	 * 进入取现页面处理
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> withdrawDepositsPage(VCustomerAccountManagerInfo vCustomerAccountManagerInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		//将值对象转换成map
		MapUtil.vObjectConvertToMap(vCustomerAccountManagerInfo, params, false);

		List<Map<String,Object>> customerAccountInfoList = customerAccountManagerDao.findCustomerAccountInfo(params);
		if(customerAccountInfoList.size()>0){
			BigDecimal accountBalance = afterLoanService.getAccAmount(vCustomerAccountManagerInfo.getAccountId());
			if(accountBalance!=null){
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
				customerAccountInfoList.get(0).put("accountBalance", decimalFormat.format(accountBalance.doubleValue()));
				if("0".equals(customerAccountInfoList.get(0).get("zhuxuedaiOrg")) && LoanStateEnum.预结清.getValue().equals(customerAccountInfoList.get(0).get("loanState"))){
					Long loanId = Long.valueOf(customerAccountInfoList.get(0).get("accountId").toString());
					BigDecimal effectAmount = getEffectAmount(loanId);
					BigDecimal oneTimeRepayment = getOneTimeRepaymentAll(loanId);
					BigDecimal withdrawDepositAmount = accountBalance.add(effectAmount).subtract(oneTimeRepayment);
					withdrawDepositAmount = new BigDecimal(0.00).compareTo(withdrawDepositAmount) == 1 ? new BigDecimal(0.00) : withdrawDepositAmount;
					customerAccountInfoList.get(0).put("withdrawDepositAmount", decimalFormat.format(withdrawDepositAmount.doubleValue()));
				}else {
					customerAccountInfoList.get(0).put("withdrawDepositAmount", decimalFormat.format(accountBalance.doubleValue()));
				}
			}else{
				customerAccountInfoList.get(0).put("accountBalance", "0.00");
				customerAccountInfoList.get(0).put("withdrawDepositAmount", "0.00");

			}
			result.put("customerAccountInfo", customerAccountInfoList.get(0));
			result.put("success", true);
			result.put("message", "操作成功");
		}else{
			result.put("success", false);
			result.put("message", "借款信息不存在");
		}

		return result;
	}

	/**
	 * 取现操作
	 * @author 00236633
	 * @param vCustomerAccountManagerWithdrawDeposits
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> withdrawDeposits(VCustomerAccountManagerWithdrawDeposits vCustomerAccountManagerWithdrawDeposits) {
		Map<String,Object> result = new HashMap<String,Object>();
		Long  loanId = vCustomerAccountManagerWithdrawDeposits.getAccountId();
		boolean flag = loanTransferInfoService.isLoanTransfer(null, loanId);
		if(!flag){
			result.put("success", false);
			result.put("message", Strings.errorMsg);
			return result;
			//throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
		}
		BigDecimal accountBalanceTemp = afterLoanService.getAccAmount(loanId);
		double accountBalance = 0;
		if(accountBalanceTemp!=null) {
			LoanBase loanBase = loanBaseService.findLoanBase(loanId);
			if ("0".equals(String.valueOf(vCustomerAccountManagerWithdrawDeposits.getZhuxuedaiOrg())) && LoanStateEnum.预结清.getValue().equals(loanBase.getLoanState())) {
				//非机构 预结清
				BigDecimal effectAmount = getEffectAmount(loanId);
				BigDecimal oneTimeRepayment = getOneTimeRepaymentAll(loanId);
				BigDecimal withdrawDepositAmount = accountBalanceTemp.add(effectAmount).subtract(oneTimeRepayment);
				withdrawDepositAmount = new BigDecimal(0.00).compareTo(withdrawDepositAmount) == 1 ? new BigDecimal(0.00) : withdrawDepositAmount;
				accountBalance = withdrawDepositAmount.doubleValue();
			}else {
				accountBalance = accountBalanceTemp.doubleValue();
			}
		}

		if(vCustomerAccountManagerWithdrawDeposits.getAmount()<=accountBalance){
			OfferRepayInfo repayInfo = new OfferRepayInfo();
			repayInfo.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
			repayInfo.setLoanId(vCustomerAccountManagerWithdrawDeposits.getAccountId());
			repayInfo.setAmount(BigDecimal.valueOf(vCustomerAccountManagerWithdrawDeposits.getAmount()));	
			repayInfo.setMemo(vCustomerAccountManagerWithdrawDeposits.getMemo());
			repayInfo.setOrgan(UserContext.getUser().getOrgCode());
			repayInfo.setTeller(UserContext.getUser().getUserCode());
			if (vCustomerAccountManagerWithdrawDeposits.getZhuxuedaiOrg() == 1) {
				repayInfo.setTradeCode(Const.TRADE_CODE_ORGOUT);
				repayInfo.setTradeType(TradeTypeEnum.保证金.getValue());
			} else {
				repayInfo.setTradeCode(Const.TRADE_CODE_OUT);
				repayInfo.setTradeType(TradeTypeEnum.现金.getValue());
			}
			repayInfo.setTradeDate(new Date());
			repayInfo.setTradeKind(TradeKindEnum.正常交易.getValue());
			
			repayInfo.setTradeNo(afterLoanService.getTradeFlowNo(vCustomerAccountManagerWithdrawDeposits.getAccountId()));
			repayInfo.setCreateTime(new Date());
			// 记录资金变动情况
			offerRepayInfoDao.insert(repayInfo);
			afterLoanService.repayDeal(repayInfo);
			
			result.put("success", true);
			result.put("message", "提现操作成功!");
		}else{
			result.put("success", false);
			result.put("message", "提现金额不能大于可提现余额!");
		}
		
		return result;
	}

	/**
	 * 进入预存页面处理
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> prestorePage(VCustomerAccountManagerInfo vCustomerAccountManagerInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		//将值对象转换成map
		MapUtil.vObjectConvertToMap(vCustomerAccountManagerInfo, params, false);

		List<Map<String,Object>> customerAccountInfoList = customerAccountManagerDao.findCustomerAccountInfo(params);
		if(customerAccountInfoList.size()>0){
			BigDecimal accountBalance = afterLoanService.getAccAmount(vCustomerAccountManagerInfo.getAccountId());
			if(accountBalance!=null){
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
				customerAccountInfoList.get(0).put("accountBalance", decimalFormat.format(accountBalance.doubleValue()));
			}else{
				customerAccountInfoList.get(0).put("accountBalance", "0.00");
			}
			result.put("customerAccountInfo", customerAccountInfoList.get(0));
			result.put("success", true);
			result.put("message", "操作成功");
		}else{
			result.put("success", false);
			result.put("message", "借款信息不存在");
		}

		return result;
	}

	/**
	 * 预存操作
	 * @author 00236633
	 * @param vCustomerAccountManagerPrestore
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> prestore(VCustomerAccountManagerPrestore vCustomerAccountManagerPrestore) {
		Long  loanId = vCustomerAccountManagerPrestore.getAccountId();
		Map<String,Object> result = new HashMap<String,Object>();
		boolean flag = loanTransferInfoService.isLoanTransfer(null, loanId);
		if(!flag){
			result.put("success", false);
			result.put("message", Strings.errorMsg);
			return result;
			//throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
		}
		if(vCustomerAccountManagerPrestore.getLoanType()!=null){
			Map<String,Object> params = new HashMap<String,Object>();
			MapUtil.vObjectConvertToMap(vCustomerAccountManagerPrestore, params, false);
			List<Map<String,Object>> customerAccountInfoList = customerAccountManagerDao.findCustomerAccountInfo(params);
			if(customerAccountInfoList.size()>0){
				String loanState = (String)customerAccountInfoList.get(0).get("loanState");
				if(loanState!=null&&(loanState.equals(LoanStateEnum.结清.getValue())||loanState.equals(LoanStateEnum.预结清.getValue()))){
					result.put("success", false);
					result.put("message", "当前客户的借款状态为结清,不能在进行预存操作!");
				}
			}else{
				result.put("success", false);
				result.put("message", "借款信息不存在");
			}
		}
		
		if(result.get("success")==null){
			OfferRepayInfo repayInfo = new OfferRepayInfo();
			repayInfo.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
			repayInfo.setLoanId(vCustomerAccountManagerPrestore.getAccountId());
			repayInfo.setAmount(BigDecimal.valueOf(vCustomerAccountManagerPrestore.getAmount()));	
			repayInfo.setMemo(vCustomerAccountManagerPrestore.getMemo());
			repayInfo.setOrgan(UserContext.getUser().getOrgCode());
			repayInfo.setTeller(UserContext.getUser().getUserCode());
			repayInfo.setTradeCode(Const.TRADE_CODE_IN);
			repayInfo.setTradeDate(new Date());
			repayInfo.setTradeKind(TradeKindEnum.正常交易.getValue());
			repayInfo.setTradeType(TradeTypeEnum.现金.getValue());
			repayInfo.setTradeNo(afterLoanService.getTradeFlowNo(vCustomerAccountManagerPrestore.getAccountId()));
			// 记录资金变动情况
			offerRepayInfoDao.insert(repayInfo);
			afterLoanService.repayDeal(repayInfo);
			result.put("success", true);
			result.put("message", "预存操作成功");
		}
		
		return result;
	}

	/**
	 * 进入还保证金页面处理
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> repaymentEarnestMoneyPage(VCustomerAccountManagerInfo vCustomerAccountManagerInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		//将值对象转换成map
		MapUtil.vObjectConvertToMap(vCustomerAccountManagerInfo, params, false);

		List<Map<String,Object>> customerAccountInfoList = customerAccountManagerDao.findCustomerAccountInfo(params);
		if(customerAccountInfoList.size()>0){
			BigDecimal accountBalance = afterLoanService.getAccAmount(vCustomerAccountManagerInfo.getAccountId());
			if(accountBalance!=null){
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
				customerAccountInfoList.get(0).put("accountBalance", decimalFormat.format(accountBalance.doubleValue()));
			}else{
				customerAccountInfoList.get(0).put("accountBalance", "0.00");
			}
			result.put("customerAccountInfo", customerAccountInfoList.get(0));
			result.put("success", true);
			result.put("message", "操作成功");
		}else{
			result.put("success", false);
			result.put("message", "借款信息不存在");
		}

		return result;
	}

	/**
	 * 还保证金操作操作
	 * @author 00236633
	 * @param vCustomerAccountManagerRepaymentEarnestMoney
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> repaymentEarnestMoney(VCustomerAccountManagerRepaymentEarnestMoney vCustomerAccountManagerRepaymentEarnestMoney) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		OfferRepayInfo repayInfo = new OfferRepayInfo();
		repayInfo.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
		repayInfo.setLoanId(vCustomerAccountManagerRepaymentEarnestMoney.getAccountId());
		repayInfo.setAmount(BigDecimal.valueOf(vCustomerAccountManagerRepaymentEarnestMoney.getAmount()));	
		repayInfo.setMemo(repayInfo.getMemo());
		repayInfo.setOrgan(UserContext.getUser().getOrgCode());
		repayInfo.setTeller(UserContext.getUser().getUserCode());
		repayInfo.setTradeCode(Const.TRADE_CODE_STDIN);
		repayInfo.setTradeDate(new Date());
		repayInfo.setTradeKind(TradeKindEnum.正常交易.getValue());
		repayInfo.setTradeType(TradeTypeEnum.现金.getValue());
		repayInfo.setTradeNo(afterLoanService.getTradeFlowNo(vCustomerAccountManagerRepaymentEarnestMoney.getAccountId()));
		// 记录资金变动情况
		offerRepayInfoDao.insert(repayInfo);
		afterLoanService.repayDeal(repayInfo);
		result.put("success", true);
		result.put("message", "还保证金操作成功");
		
		return result;
	}

	/**
	 * 查询客户账号信息
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @return
	 */
	@Override
	public Map<String, Object> findCustomerAccountInfo(VCustomerAccountManagerInfo vCustomerAccountManagerInfo) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		//将值对象转换成map
		MapUtil.vObjectConvertToMap(vCustomerAccountManagerInfo, params, false);

		List<Map<String,Object>> customerAccountInfoList = customerAccountManagerDao.findCustomerAccountInfo(params);
		if(customerAccountInfoList.size()>0){
			BigDecimal accountBalance = afterLoanService.getAccAmount(vCustomerAccountManagerInfo.getAccountId());
			if(accountBalance!=null){
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
				customerAccountInfoList.get(0).put("accountBalance", decimalFormat.format(accountBalance.doubleValue()));
				if ("0".equals(customerAccountInfoList.get(0).get("zhuxuedaiOrg")) && LoanStateEnum.预结清.getValue().equals(customerAccountInfoList.get(0).get("loanState"))) {
					Long loanId = Long.valueOf(customerAccountInfoList.get(0).get("accountId").toString());
					BigDecimal effectAmount = getEffectAmount(loanId);
					BigDecimal oneTimeRepayment = getOneTimeRepaymentAll(loanId);
					BigDecimal withdrawDepositAmount = accountBalance.add(effectAmount).subtract(oneTimeRepayment);
					withdrawDepositAmount = new BigDecimal(0.00).compareTo(withdrawDepositAmount) == 1 ? new BigDecimal(0.00) : withdrawDepositAmount;
					customerAccountInfoList.get(0).put("withdrawDepositAmount", decimalFormat.format(withdrawDepositAmount.doubleValue()));
				}else {
					customerAccountInfoList.get(0).put("withdrawDepositAmount", decimalFormat.format(accountBalance.doubleValue()));
				}
			}else{
				customerAccountInfoList.get(0).put("accountBalance", "0.00");
			}
			result.put("customerAccountInfo", customerAccountInfoList.get(0));
			result.put("success", true);
			result.put("message", "操作成功");
		}else{
			result.put("success", false);
			result.put("message", "借款信息不存在");
		}

		return result;	
	}

	/**
	 * 一次性结清金额
	 * @param loanId
	 * @return
	 */
	private BigDecimal getOneTimeRepaymentAll(Long loanId){
		Date tradeDate = Dates.getCurrDate();
		List<LoanRepaymentDetail> repayList = afterLoanService.getAllInterestOrLoan(tradeDate,loanId);
		if (CollectionUtils.isEmpty(repayList)) {
			return new BigDecimal(0.00);
		}
		return repayList.get(repayList.size()-1).getRepaymentAll();
	}

	/**
	 * 生效金额
	 * @param loanId
	 * @return
	 */
	private BigDecimal getEffectAmount(Long loanId){
		LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentService.findbyLoanAndType(loanId, SpecialRepaymentTypeEnum.减免.getValue(), SpecialRepaymentStateEnum.申请.getValue());
		if (loanSpecialRepayment == null) {
			return new BigDecimal(0.00);
		}
		return loanSpecialRepayment.getAmount();
	}
}
