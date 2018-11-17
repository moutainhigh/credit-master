package com.zdmoney.credit.fee.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeOfferDao;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 借款收费报盘表 Dao实现层，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
@Repository
public class LoanFeeOfferDaoImpl extends BaseDaoImpl<LoanFeeOffer> implements ILoanFeeOfferDao {
	/**
	 * 查询服务费报盘数据（带分页）
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public Pager searchLoanFeeOfferWithPgByMap(Map<String, Object> params) {
		Pager pager = (Pager) params.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".searchLoanFeeOfferWithPgByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".searchLoanFeeOfferWithPgByMapCount");
		return doPager(pager, params);
	}
}
