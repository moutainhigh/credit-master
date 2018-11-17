package com.zdmoney.credit.payment.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zdmoney.credit.common.util.Dates;

/**
 * 外贸信托导入vo
 * @author fuhongxing 
 */
public class WaiMaoXinTuo {
	
	private String orgCode; 		//机构代码
	private String contractNum;		//合同编号
	private String result; 			//放款结果(10 未处理,20 正在处理,30 放款成功,40 放款失败)
	private Date backDate;			//银行回盘时间(yyyyMMddhh24mmss)
	private String memo;			//失败原因
	private String approveResult;	//审批结果
	private String approveDeny;		//审批拒绝原因
	private String remark;			//导入备注

	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Date getBackDate() {
		return backDate;
	}
	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getApproveResult() {
		return approveResult;
	}
	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}
	public String getApproveDeny() {
		return approveDeny;
	}
	public void setApproveDeny(String approveDeny) {
		this.approveDeny = approveDeny;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 数据验证
	 * @param list
	 * @return
	 */
	public List<WaiMaoXinTuo> validate(List<String []> data){
		List<WaiMaoXinTuo> list = new ArrayList<WaiMaoXinTuo>();
		List<String> message = new ArrayList<String>();
		WaiMaoXinTuo xintuo = null;
		for (String [] str : data) {
			message.clear();
			xintuo = new WaiMaoXinTuo();
			//机构代码验证
			if(StringUtils.isNotEmpty(str[0].trim())){
				xintuo.setOrgCode(str[0].trim());
			}else if(!"016".equals(str[0].trim())){
				message.add("机构代码不正确");
			}else{
				message.add("机构代码不能为空");
			}
			//合同编号
			if(StringUtils.isNotEmpty(str[1].trim())){
				xintuo.setContractNum(str[1].trim());
			}else if(!str[1].trim().startsWith("ZD")){
				message.add("合同编号格式不正确");
			}else{
				message.add("合同编号不能为空");
			}
			//放款结果
			if(StringUtils.isEmpty(str[2].trim())){
				message.add("放款结果不能为空");
			}else if("30".equals(str[2].trim())){
				xintuo.setResult(str[2].trim());
			}else{
				message.add("放款结果非放款成功状态不予处理");
			}
			//银行回盘时间(yyyyMMddhh24mmss)
			if(StringUtils.isNotEmpty(str[3].trim())){
				xintuo.setBackDate(Dates.parse(str[3].trim(), "yyyyMMddhhmmss"));
			}else{
				message.add("银行回盘时间不能为空");
			}
			//失败原因
			if(StringUtils.isNotEmpty(str[4].trim())){
				xintuo.setMemo(str[4].trim());
			}
			//审批结果
			if(StringUtils.isEmpty(str[5].trim())){
				message.add("审批结果不能为空");
			}else if("01".equals(str[5].trim())){
				xintuo.setApproveResult(str[5].trim());
			}else{
				message.add("审批结果非审批通过不予处理");
			}
			
			//审批拒绝原因
			if(str.length==7){
				xintuo.setApproveDeny(str[6].trim());
			}
			
			if(message.size()>0){
				xintuo.setRemark(Arrays.toString(message.toArray()).replace("[", "").replace("]", ""));
	        }
			list.add(xintuo);
		}
		return list;
	}

}
