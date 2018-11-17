package com.zdmoney.credit.bsyh.vo;

/**
 * 包商银行特殊还款查询分装
 * @author YM10104
 *
 */
public class BsyhSpecialRepayVo {
	private Long loanId;

    private String applyState;//申请状态

    private String deductState;//划扣状态

    private String repayBusNumber;//包银申请流水号
    
    private Long logId;//日志id

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getApplyState() {
		return applyState;
	}

	public void setApplyState(String applyState) {
		this.applyState = applyState;
	}

	public String getDeductState() {
		return deductState;
	}

	public void setDeductState(String deductState) {
		this.deductState = deductState;
	}

	public String getRepayBusNumber() {
		return repayBusNumber;
	}

	public void setRepayBusNumber(String repayBusNumber) {
		this.repayBusNumber = repayBusNumber;
	}

  
	

}
