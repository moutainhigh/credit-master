package com.zdmoney.credit.core.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.common.vo.core.RepaymentDetailParamVO;
import com.zdmoney.credit.common.vo.core.ReturnAccountCardVO;
import com.zdmoney.credit.core.AccountCard;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;

public interface IRepaymentDetialCoreService {

	/**
	 * 查询还款明细数据
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> queryRepayInfoByLoanId(FinanceVo params);

	/**
	 * 还款计划表接口
	 * @param paramVo 参数集合
	 * @return
	 */
	public Map<String,Object> queryRepaymentDetail(Map<String,Object> paramMap);
	
	/**
	 * 还款明细表接口
	 * @param paramVo 参数集合
	 * @return
	 */
	public Map<String,Object> queryFlow(Map<String,Object> paramMap);
	
	/**
	 * 封装获得的还款明细数据
	 * @param repayInfoList 还款明细数据集合
	 * @return
	 */
	public List<ReturnAccountCardVO> toReturnAccountCardVo(List<OfferRepayInfo> repayInfoList);
	
	/**
	 * 封装获得的还款明细数据
	 * @param repayInfoList 还款明细数据集合
	 * @return
	 */
	public AccountCard toReturnAccountCardVo(OfferRepayInfo offerRepayInfo);
	
	/**
	 * 查看还款汇总信息接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryRepaymentSummary(RepaymentDetailParamVO params) throws Exception;
	
	/**
	 * 查看还款详细信息接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryRepaymentDetailAll(Map<String,Object> paramMap) throws Exception;
	
}
