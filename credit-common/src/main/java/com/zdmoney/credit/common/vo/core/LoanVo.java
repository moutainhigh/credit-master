package com.zdmoney.credit.common.vo.core;

/**
 * 封装征审中债权相关接口参数
 * @author 00234770
 *
 */
public class LoanVo {

	/**用户code*/
	private String userCode;
	
	/**债权保存接收的数据**/
	private String jsonStr;
	
	/**债权的appNo**/
	private String appNo;
	
	/**更新债权状态的状态码**/
	private String stateCode;
	
	/**债权ID**/
	private Long loanId;
	
	/**批量债权appNo**/
	private String appNos;
	
	/**批量更新债权状态的状态码**/
	private String stateCodes;
	
	/**债权状态**/
	private String loanState;
	
	/**状态**/
	private String loanFlowState;
	
	/**客户身份证号**/
	private String idNum;
	
	/** 陆金所-产品子类 **/
	private String productNo;
	/** 陆金所-产品大类 **/
	private String productType;
	/** 陆金所-进件流水号 **/
	private String apsApplyNo;
	/** 陆金所-申请单号 **/
	private String applyNo;
	/** 陆金所-前端主机ID **/
	private String lufaxLoanReqId;
	/**lufax网站用户名**/
	private String lujsName;
	/**陆金所-借款id**/
	private String lujsLoanReqId;
	
	/**是否是费率优惠客户y，n**/
	private String isRatePreferLoan;

	public String getLujsLoanReqId() {
		return lujsLoanReqId;
	}

	public void setLujsLoanReqId(String lujsLoanReqId) {
		this.lujsLoanReqId = lujsLoanReqId;
	}

	public String getLujsName() {
		return lujsName;
	}

	public void setLujsName(String lujsName) {
		this.lujsName = lujsName;
	}

	public String getIsRatePreferLoan() {
		return isRatePreferLoan;
	}

	public void setIsRatePreferLoan(String isRatePreferLoan) {
		this.isRatePreferLoan = isRatePreferLoan;
	}
	
	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	
	public String getAppNos() {
		return appNos;
	}

	public void setAppNos(String appNos) {
		this.appNos = appNos;
	}

	public String getStateCodes() {
		return stateCodes;
	}

	public void setStateCodes(String stateCodes) {
		this.stateCodes = stateCodes;
	}
	
	@Override
	public String toString() {
		return "LoanVo[userCode="+userCode+", jsonStr="+jsonStr+", appNo="+appNo+", stateCode="+stateCode+", loanId="+loanId + ", appNos="+appNos+", stateCodes="+stateCodes+",idNum="+idNum+","
				+ "isRatePreferLoan="+isRatePreferLoan+",loanState="+loanState+"]";
	}

	public String getLoanState() {
		return loanState;
	}

	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}

	public String getLoanFlowState() {
		return loanFlowState;
	}

	public void setLoanFlowState(String loanFlowState) {
		this.loanFlowState = loanFlowState;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getApsApplyNo() {
		return apsApplyNo;
	}

	public void setApsApplyNo(String apsApplyNo) {
		this.apsApplyNo = apsApplyNo;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getLufaxLoanReqId() {
		return lufaxLoanReqId;
	}

	public void setLufaxLoanReqId(String lufaxLoanReqId) {
		this.lufaxLoanReqId = lufaxLoanReqId;
	}
	
}
