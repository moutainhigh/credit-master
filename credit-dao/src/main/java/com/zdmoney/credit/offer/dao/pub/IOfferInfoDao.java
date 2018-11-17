package com.zdmoney.credit.offer.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferInfo;


	/**
	 * 报盘信息
	 * @author 00232949
	 *
	 */
public interface IOfferInfoDao  extends IBaseDao<OfferInfo> {
	
	/**
	 * 根据员工号得到员工最高划扣次数
	 * */
	public String getoffercount(String usercode);

	/**
	 * 
	 * @param offerInfo
	 */
	public int updateByPrimaryKeySelective(OfferInfo offerInfo);

	
	/**
	 * 统计客户还没有回盘的划扣请求次数
	 * @param offerInfo
	 * @return
	 */
	public Integer getOfferCountForNoDecuct(OfferInfo offerInfo);
	
	
	
	/**
	 * 统计客户当日实时划扣的次数
	 * @param offerInfo
	 * @return
	 */
	public Integer getOfferCountForYet3Deduct(OfferInfo offerInfo);
	
	
	/**
	 * 分页查询回盘信息
	 * @param paramMap 参数集合
	 * @return
	 */
	public Pager getOfferInfoWithPg(Map<String,Object> paramMap);
	
	/**
	 * 委托代扣回盘查询
	 * @param params
	 * @return
	 */
	public List<OfferInfo> findOfferReturnList(Map<String,Object> params);
	
	public int updateOfferInfo(Map<String, Object> params);
	
	/**
	 * 查询当天待划扣的报盘文件信息
	 * @param params
	 * @return
	 */
	public List<OfferInfo> queryTodayNeedSendOfferList(Map<String, Object> params);
	
	/**
	 * 根据债权编号查询报盘划扣类型
	 * @param loanId
	 * @return
	 */
	public String queryDebitTypeByLoanId(Long loanId);
	
	/**
	 * 查询线下还款报盘信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryOffLineOfferInfo(Map<String, Object> params);
}
