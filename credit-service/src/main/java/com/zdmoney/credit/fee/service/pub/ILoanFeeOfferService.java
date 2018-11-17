package com.zdmoney.credit.fee.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.fee.vo.CreateFeeOfferVo;

/**
 * 借款收费报盘表 Service层接口，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
public interface ILoanFeeOfferService {

	/**
	 * PK换实体
	 * 
	 * @param id
	 */
	public LoanFeeOffer findById(Long id);

	/**
	 * 跟据实体类查询
	 * 
	 * @param loanFeeOffer
	 */
	public List<LoanFeeOffer> findListByVo(LoanFeeOffer loanFeeOffer);

	/**
	 * 创建服务费报盘
	 * 
	 * @param createFeeOfferVo
	 * @return
	 */
	public LoanFeeOffer createOffer(CreateFeeOfferVo createFeeOfferVo);

	/**
	 * 更新服务信息
	 * 
	 * @param loanFeeOffer
	 * @return
	 */
	public LoanFeeOffer updateOffer(LoanFeeOffer loanFeeOffer);

	/**
	 * 查询服务费报盘数据（带分页）
	 * 
	 * @param params
	 * @return
	 */
	public Pager searchLoanFeeOfferWithPgByMap(Map<String, Object> params);

	/**
	 * 查询生成的报盘数量
	 * 
	 * @param feeId
	 *            服务费编号
	 * @param type
	 *            类型 实时划扣 自动划扣
	 * @param offerDate
	 *            报盘日期
	 * @return
	 */
	public int getOfferCount(Long feeId, String type, Date offerDate);
}
