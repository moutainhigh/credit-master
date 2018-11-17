package com.zdmoney.credit.offer.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;

/**
 * 交易凭证dao
 * @author 00232949
 *
 */
public interface IOfferRepayInfoDao extends IBaseDao<OfferRepayInfo> {

	/**
	 * 根据放款接口参数查询需要的凭证明细
	 * @param params
	 * @return
	 */
	List<OfferRepayInfo> findListByFinanceVo(FinanceVo params);
	
	/**
	 * afterLoanService中，drawJimuRisk方法获取repayInfo第一条数据的查询
	 * @param params loanId，tradeCodes列表，tradeDate排序
	 * @return
	 */
	public List<OfferRepayInfo> getDrawJimuRiskRepayInfo(Map<String, Object> params);

	/**
	 * 取最后一次提风险金的记录
	 * @param loanid
	 * @return
	 */
	OfferRepayInfo getLoanlastDrawRisk(Map<String, Object> params);
	
	/**
	 * 根据loanid和tradecode，获取最后一条OfferRepayInfo记录
	 * @param params
	 * @return
	 */
	public OfferRepayInfo getLoanLastRepayInfoByTradeCode(Map<String, Object> params);
	
	/**
	 * 分页查询还款明细数据
	 * @param paramMap 参数集合
	 * @return
	 */
	public Pager getOfferRepayInfoWithPg(Map<String,Object> paramMap);
	/**
	 * 结清证明打印时 根据合同id查询，根据id desc 取最后一条
	 */
	public OfferRepayInfo getLoanLastRepayInfoById(Map<String, Object> paramMap);

	/**
	 * 获取信托分账的OfferRepayInfo 记录 按照 loan_id ,id 排序
	 * @param paramMap
	 * @return
	 */
	public List<OfferRepayInfo> getTrustOfferRepayInfo(Map<String,Object> paramMap);

	/**
	 * 根据 tradeNo 获取还款记录
	 * @param tradeNo
	 * @return
	 */
	public OfferRepayInfo findByTradeNo(String tradeNo);

	/**
	 * 获取渤海2分账明细信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findSubAccountDetailList(Map<String, Object> params);
	/**
	 * 获取华瑞渤海对公分账明细信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findPublicSubAccountDetailList(Map<String, Object> params);

	/**
	 * 获取渤海信托分账明细信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findSubAccountDetailList4BHXT(Map<String, Object> params);
}
