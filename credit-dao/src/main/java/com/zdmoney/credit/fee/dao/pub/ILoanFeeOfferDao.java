package com.zdmoney.credit.fee.dao.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * 借款收费报盘表 Dao层接口，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
public interface ILoanFeeOfferDao extends IBaseDao<LoanFeeOffer> {

	/**
	 * 查询服务费报盘数据（带分页）
	 * 
	 * @param params
	 * @return
	 */
	public Pager searchLoanFeeOfferWithPgByMap(Map<String, Object> params);

}
