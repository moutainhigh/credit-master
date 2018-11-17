package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款收费导入数据
 * 
 * @author Ivan
 *
 */
public class LoanFeeImportData extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1768037573855385257L;
	/** 主键 **/
	private Long id;
	/** 导入批次号 **/
	private String batchCode;
	/** 序号 **/
	private Long no;
	/** 债权编号 **/
	private String contractNum;
	/** 姓名 **/
	private String name;
	/** 身份证号 **/
	private String idNum;
	/** 营业部 **/
	private String branchName;
	/** 放款日期 **/
	private Date grantMoneyDate;
	/** 贷款期限（月） **/
	private Long loanTime;
	/** 合同金额（元） **/
	private BigDecimal pactMoney;
	/** 结果 **/
	private String result;
	/** 备注 **/
	private String memo="";
	
	public LoanFeeImportData() {
		super();
	}
	
	
	
	public LoanFeeImportData(Long id, String batchCode, Long no, String contractNum, String name, String idNum,
			String branchName, String result, String memo) {
		super();
		this.id = id;
		this.batchCode = batchCode;
		this.no = no;
		this.contractNum = contractNum;
		this.name = name;
		this.idNum = idNum;
		this.branchName = branchName;
		this.result = result;
		this.memo = memo;
	}


	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode == null ? null : batchCode.trim();
	}

	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum == null ? null : contractNum.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum == null ? null : idNum.trim();
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName == null ? null : branchName.trim();
	}

	public Date getGrantMoneyDate() {
		return grantMoneyDate;
	}

	public void setGrantMoneyDate(Date grantMoneyDate) {
		this.grantMoneyDate = grantMoneyDate;
	}

	public Long getLoanTime() {
		return loanTime;
	}

	public void setLoanTime(Long loanTime) {
		this.loanTime = loanTime;
	}

	public BigDecimal getPactMoney() {
		return pactMoney;
	}

	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result == null ? null : result.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}
	
	public Map<String, Object> validate(Map<String, String> map){
		Map<String, Object> result = new HashMap<String, Object>();
		LoanFeeImportData loanFeeImportData = new LoanFeeImportData();
		List<String> list = new ArrayList<String>();
		
		if(!StringUtils.isEmpty(map.get("memo"))){
        	loanFeeImportData.setMemo(map.get("memo"));
        }
		
		if(!StringUtils.isEmpty(map.get("contractNum"))){
        	loanFeeImportData.setContractNum(map.get("contractNum"));
        }else{
        	list.add("债权号不能为空");
        }
		if(!StringUtils.isEmpty(map.get("idNum"))){
			loanFeeImportData.setIdNum(map.get("idNum"));
        }else{
        	list.add("身份证不能为空");
        }
		if(!StringUtils.isEmpty(map.get("name"))){
			loanFeeImportData.setName(map.get("name"));
        }else{
        	list.add("借款人姓名不能为空");
        }
		if(!StringUtils.isEmpty(map.get("branchName"))){
			loanFeeImportData.setBranchName(map.get("branchName"));
		}else{
			list.add("来源营业部不能为空");
		}
		if(!StringUtils.isEmpty(map.get("grantMoneyDate"))){
        	loanFeeImportData.setGrantMoneyDate(Dates.parse(map.get("grantMoneyDate"), "yyyy-MM-dd"));
        }else{
        	list.add("放款日期不能为空");
        }
        if(!StringUtils.isEmpty(map.get("loanTime"))){
        	loanFeeImportData.setLoanTime(Long.valueOf(map.get("loanTime")));
        }else{
        	list.add("贷款期限不能为空");
        }
        if(!StringUtils.isEmpty(map.get("pactMoney"))){
        	loanFeeImportData.setPactMoney(new BigDecimal(map.get("pactMoney")));
        }else{
        	list.add("合同金额不能为空");
        }
        if (!StringUtils.isEmpty(map.get("result")) && "成功".equals(map.get("result"))) {
        	loanFeeImportData.setResult(map.get("result"));
        }else if("失败".equals(map.get("result"))){
        	loanFeeImportData.setResult(map.get("result"));
        	list.add("结果状态为失败不做导入处理");
        }else{
        	list.add("结果不能为空");
        }
        
        if(!StringUtils.isEmpty(map.get("no"))){
        	loanFeeImportData.setNo(Long.valueOf(map.get("no")));
        }
        
        result.put("loanFeeImportData", loanFeeImportData); 
        if(list.size()>0){
        	result.put("validateResult", Arrays.toString(list.toArray()).replace("[", "").replace("]", ""));
        }
		return result;
	}
}