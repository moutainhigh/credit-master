package com.zdmoney.credit.offer.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.system.domain.ComOrganization;

public interface IOfferCreateService {

	/**
	 * 根据营业部和类型来创建报盘记录
	 * 
	 * @param salesDepartment 营业部
	 * @param type  1：正常扣款，2：逾期扣款，3：提前扣款，4：提前结清,5实时划扣
	 * @return
	 */
	public int createOfferRecordWithOrgAndType(ComOrganization salesDepartment, int type);
	
	/**
	 * 实时扣款推送接口方法
	 * @param paramsVo 参数VO
	 */
	public Map<String,Object> createRealtimeOfferInfo(OfferParamsVo paramsVo);
	
	/**
	 * 特殊还款申请时创建报盘文件（仅当requestDate是当天时，才会创建，否则会在requestDate的日终自动生成）<br>
	 *  在创建特殊还款时，判断该债权有没有“未回盘”的报盘文件，如有，打标记，在回盘后自动创建新的报盘文件调整金额。如果没有就立即关闭之前的报盘，创建一条新报盘文件包含特殊还款的金额。<br>
		（这样不论逾期还是正常的债权都能在申请后自动生成报盘文件，如果无“未回盘”金额立即更新或创建，如有有“未回盘”，金额在回盘后自动更新）<br>
	 * 仅支持提前结清和提前还款
	 * @param specialRepayId 特殊还款表id
	 */
	public void createOfferInfoBySpecialRepay(Long specialRepayId);
	
	/**
	 * 撤销特殊还款申请时从新生成报盘文件（会在原报盘回盘后，从新生成新的报盘文件）
	 * @param specialRepayId 特殊还款表id
	 */
	public void revokeOfferInfoBySpecialRepay(Long specialRepayId);

	/**
	 * 关闭这个loan的当日自动报盘
	 * @param loan
	 */
	public void closeOfferInfo(VLoanInfo loan);

	/**
	 * 同createOfferInfoBySpecialRepay(Long specialRepayId)  
	 * @param specialRepayId
	 * @param isThrow 1：金额0时会抛出异常 ；0：金额0时不抛异常
	 */
	public void createOfferInfoBySpecialRepay(Long specialRepayId, int isThrow);
}
