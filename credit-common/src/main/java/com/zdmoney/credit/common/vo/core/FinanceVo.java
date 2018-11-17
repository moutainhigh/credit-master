package com.zdmoney.credit.common.vo.core;

/**
 * 封装征审，催收接口的参数
 * @author 00232949
 *
 */
public class FinanceVo {
	/**用户code*/
	public String userCode;
	/**loan表ids*/
	public String ids;
	
	/**loan表id*/
	public String loanId;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}


	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	@Override
	public String toString() {
		return "ParamsVo [userCode=" + userCode + ", ids=" + ids + ", loanId="
				+ loanId + "]";
	}
	
}
