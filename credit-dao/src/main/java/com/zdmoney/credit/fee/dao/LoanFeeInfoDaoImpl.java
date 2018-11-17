package com.zdmoney.credit.fee.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeInfoDao;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 借款收费主表 Dao实现层，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
@Repository
public class LoanFeeInfoDaoImpl extends BaseDaoImpl<LoanFeeInfo> implements ILoanFeeInfoDao {

	@Override
	public LoanFeeInfo findLoanFeeInfoByLoanId(Long loanId) {
		LoanFeeInfo loanFeeInfo = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLoanFeeInfoByLoanId",
				loanId);
		return loanFeeInfo;
	}

	@Override
	public boolean isExistsByLoanId(Long loanId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("loanId", loanId);
		int count = Strings.convertValue(getSqlSession().selectOne(getIbatisMapperNameSpace() + ".isExists", param),
				Long.class).intValue();
		return count > 0;
	}
}
