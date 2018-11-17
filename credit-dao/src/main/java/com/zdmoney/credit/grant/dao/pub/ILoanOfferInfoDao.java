package com.zdmoney.credit.grant.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.grant.domain.LoanOfferInfo;

/**
 * 
 * @author YM10104
 *
 */
public interface ILoanOfferInfoDao extends IBaseDao<LoanOfferInfo> {
	/**
	 * 通过流水号查询报盘信息
	 * @param serialNo
	 * @return
	 */
	LoanOfferInfo findbySerialNo(String serialNo);
	/**
	 * 查询出已报盘的数据
	 * @return
	 */
	List<LoanOfferInfo> findInfoList();
	
}
