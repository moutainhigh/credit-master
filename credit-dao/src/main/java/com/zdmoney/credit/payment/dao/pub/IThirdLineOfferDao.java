package com.zdmoney.credit.payment.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.payment.domain.ThirdLineOffer;
import com.zdmoney.credit.payment.vo.ThirdLineOfferVo;

public interface IThirdLineOfferDao extends IBaseDao<ThirdLineOffer>{
	
	public List<ThirdLineOfferVo> queryAllHaTwoOfferVo();
	
	/**
	 * 根据备注查询报盘
	 * @param paramMap
	 */
	public List<ThirdLineOffer> findHaTwoOfferByMap(Map<String, Object> paramMap);
	
	/**
	 * 查询未报盘、已回盘失败,财务放款的债权信息
	 * @param params
	 */
	public List<ThirdLineOffer> queryOfferInfo();
	
	/**
	 * 查询报盘信息
	 */
	public List<ThirdLineOffer> findOfferInfoList();
	
	public ThirdLineOffer findHaTwoOfferByFinancialType(String  findHaTwoOfferByFinancialType);
	
	public Pager searchOfferInfoWithPg(Map<String, Object> paramMap);
	/**
	 * 查询第三方线下放款相关信息
	 * @param paramMap
	 * @return
	 */
	public Pager searchOffLineLoanInfoWithPg(Map<String, Object> paramMap);

	public List<ThirdLineOffer> findOfferLineOfferByMap(Map<String, Object> paramMap);

}
