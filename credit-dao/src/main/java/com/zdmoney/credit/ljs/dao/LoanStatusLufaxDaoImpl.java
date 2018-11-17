package com.zdmoney.credit.ljs.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.framework.vo.lufax.entity.LoanDetailLufax;
import com.zdmoney.credit.ljs.dao.pub.ILoanStatusLufaxDao;
import com.zdmoney.credit.ljs.domain.LoanStatusLufax;

/**
 * @author 10098  2017年5月15日 上午9:54:42
 */
@Repository
public class LoanStatusLufaxDaoImpl extends BaseDaoImpl<LoanStatusLufax> implements ILoanStatusLufaxDao {

	@Override
	public List<LoanDetailLufax> findLoanDetailLufaxList(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanDetailLufaxList" , params);
	}

	@Override
	public List<LoanStatusLufax> findLoanStatusLufax2Transmit(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanStatusLufax2Transmit" , params);
	}

	@Override
	public void updateLufuxStatusToYuqi() {
		getSqlSession().update(getIbatisMapperNameSpace() + ".updateLufuxStatusToYuqi");
	}
}
