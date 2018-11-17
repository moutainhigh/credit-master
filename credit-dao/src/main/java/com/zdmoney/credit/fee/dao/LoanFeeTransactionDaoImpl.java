package com.zdmoney.credit.fee.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeTransactionDao;
import com.zdmoney.credit.fee.domain.LoanFeeTransaction;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 借款收费回盘表 Dao实现层，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
@Repository
public class LoanFeeTransactionDaoImpl extends BaseDaoImpl<LoanFeeTransaction> implements ILoanFeeTransactionDao {

	@Override
	public boolean isExistsByFeeIdAndTrxState(Long feeId, String trxState) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("feeId", feeId);
		param.put("trxState", trxState);
		int count = Strings.convertValue(getSqlSession().selectOne(getIbatisMapperNameSpace() + ".isExists", param),
				Long.class).intValue();
		return count > 0;
	}

	@Override
	public Pager searchFeeOfferTransactionList(Map<String, Object> params) {
		Pager pager = (Pager) params.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".searchFeeOfferTransactionWithPgByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".searchFeeOfferTransactionByMapCount");
		return doPager(pager, params);
	}

}
