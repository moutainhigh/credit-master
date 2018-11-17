package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * @author 10098  2017年5月4日 下午3:10:28
 */
public interface ILoanBaseGrantService {

	/**
	 * @param vLoanInfo
	 */
	public LoanBaseGrant createLoanBaseGrant(VLoanInfo vLoanInfo);

	/**
	 * 更新放款申请状态
	 * @param id
	 * @param code
	 */
	public void updateLoanBaseGrantByLoanId(Long loanId, String status);

	/**
	 * 检查放款申请 状态
	 * @param id
	 * @param code
	 * @return
	 */
	public boolean checkLoanBaseGrantStatus(Long loanId, String grantState);

	/**
	 * 根据appNo得到最新的债权放款申请信息
	 * @param appNo
	 * @return
	 */
	public LoanBaseGrant findLoanBaseGrantByAppNo(String appNo);
}
