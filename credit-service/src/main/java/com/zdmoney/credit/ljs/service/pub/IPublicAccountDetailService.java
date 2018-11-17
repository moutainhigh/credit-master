package com.zdmoney.credit.ljs.service.pub;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.ljs.domain.PublicAccountDetail;
import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;


/***
 * 陆金所还款结果通知处理
 * @author YM10104
 *
 */
public interface IPublicAccountDetailService{
	
	/**
	 * 保存记录
	 * @param publicAccountDetail
	 * @return
	 */
	public PublicAccountDetail save(PublicAccountDetail publicAccountDetail);

	public Pager findWithPgByMap(Map<String, Object> paramMap);

	/**
	 * 通过合作机构查询其最后一次明细记录
	 * @param string
	 */
	public PublicAccountDetail getLastAccountDetail(String accountType);

	public List<PublicAccountDetail> findByMap(Map<String, Object> paramMap);
	/**
	 *  插入明细
	 * @param contractNum 借款合同编号
	 * @param batchNo 批次号
	 * @param tradeType 交易类型（0充值、1提现、2还款、3垫付、4回购）
	 * @param amount 金额
	 * @param memo 摘要
	 * @param state 状态（0:处理中,1:成功, 2:失败）
	 * @param errorMsg 失败信息
	 * @return
	 */
	public PublicAccountDetail insertAccDetail(String contractNum,String batchNo,String tradeType,BigDecimal amount,String memo,String state, String errorMsg);
	/**
	 *  通过batchNo更新明细
	 * @param contractNum 借款合同编号
	 * @param batchNo 批次号
	 * @param tradeType 交易类型（0充值、1提现、2还款、3垫付、4回购）
	 * @param amount 金额
	 * @param memo 摘要
	 * @param state 状态（0:处理中,1:成功, 2:失败）
	 * @param errorMsg 失败信息
	 * @return
	 */
	public void updateAccDetailByBatchNo(String contractNum,String batchNo,String tradeType,BigDecimal amount,String memo,String state, String errorMsg);
}
