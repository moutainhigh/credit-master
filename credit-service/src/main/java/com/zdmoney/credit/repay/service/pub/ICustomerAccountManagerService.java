package com.zdmoney.credit.repay.service.pub;

import java.util.Map;

import com.zdmoney.credit.repay.vo.VCustomerAccountManagerInfo;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerList;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerPrestore;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerRepaymentEarnestMoney;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerWithdrawDeposits;

public interface ICustomerAccountManagerService {
	
	/**
	 * 查询客户账户信息列表
	 * @author 00236633
	 * @param vCustomerAccountManagerList
	 * @return
	 */
	public Map<String,Object> list(VCustomerAccountManagerList vCustomerAccountManagerList);
	
	/**
	 * 进入取现页面处理
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @return
	 */
	public Map<String,Object> withdrawDepositsPage(VCustomerAccountManagerInfo vCustomerAccountManagerInfo);
	
	/**
	 * 取现操作
	 * @author 00236633
	 * @param vCustomerAccountManagerWithdrawDeposits
	 * @return
	 */
	public Map<String,Object> withdrawDeposits(VCustomerAccountManagerWithdrawDeposits vCustomerAccountManagerWithdrawDeposits);
	
	/**
	 * 进入预存页面处理
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @return
	 */
	public Map<String,Object> prestorePage(VCustomerAccountManagerInfo vCustomerAccountManagerInfo);
	
	/**
	 * 预存操作
	 * @author 00236633
	 * @param vCustomerAccountManagerPrestore
	 * @return
	 */
	public Map<String,Object> prestore(VCustomerAccountManagerPrestore vCustomerAccountManagerPrestore);
	
	/**
	 * 进入还保证金页面处理
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @return
	 */
	public Map<String,Object> repaymentEarnestMoneyPage(VCustomerAccountManagerInfo vCustomerAccountManagerInfo);
	
	/**
	 * 还保证金操作操作
	 * @author 00236633
	 * @param vCustomerAccountManagerRepaymentEarnestMoney
	 * @return
	 */
	public Map<String,Object> repaymentEarnestMoney(VCustomerAccountManagerRepaymentEarnestMoney vCustomerAccountManagerRepaymentEarnestMoney);
	
	/**
	 * 查询客户账号信息
	 * @author 00236633
	 * @param vCustomerAccountManagerInfo
	 * @return
	 */
	public Map<String,Object> findCustomerAccountInfo(VCustomerAccountManagerInfo vCustomerAccountManagerInfo);
}
