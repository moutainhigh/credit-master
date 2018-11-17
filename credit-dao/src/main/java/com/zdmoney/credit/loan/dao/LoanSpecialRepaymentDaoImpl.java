package com.zdmoney.credit.loan.dao;

import java.util.*;

import com.zdmoney.credit.common.util.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.zdmoney.credit.bsyh.vo.BsyhSpecialRepayVo;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.SpecialRepayParamsVO;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanSpecialRepaymentDao;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;

/**
 * 
 * @author 00232949
 *
 */
@Repository
public class LoanSpecialRepaymentDaoImpl extends
		BaseDaoImpl<LoanSpecialRepayment> implements ILoanSpecialRepaymentDao {

	protected final Log log = LogFactory.getLog(getClass());

	private static final String LOANSPECIALREPAYMENT_MAPPER_NAME_SPANCE = "com.zdmoney.credit.loan.domain.LoanSpecialRepaymentMapper";

	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYLOANIDANDREPAYMENTSTATE = ".findByLoanIdAndDateAndTypeAndState";

	@Override
	public LoanSpecialRepayment findByLoanIdAndDateAndTypeAndState(
			Map<String, Object> map) {
		LoanSpecialRepayment loanSpecialRepayment = this.getSqlSession()
				.selectOne(
						LOANSPECIALREPAYMENT_MAPPER_NAME_SPANCE
								+ FINDBYLOANIDANDREPAYMENTSTATE, map);
		return loanSpecialRepayment;
	}

	@Override
	public int updateSpecialRepaymentStateAtEndOfDay(int promiseReturnDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("promiseReturnDate", promiseReturnDate);

		int affectNum = 0;
		affectNum = getSqlSession().update(
				getIbatisMapperNameSpace()
						+ ".updateSpecialRepaymentStateAtEndOfDay", map);
		return affectNum;

		// SpecialRepayment.executeUpdate("update SpecialRepayment  S set specialRepaymentState=:state where specialRepaymentType=:type and specialRepaymentState=:state2 and S.loan.id in (select id from Loan  where id=S.loan.id and promiseReturnDate=:date) ",
		// [state: SpecialRepaymentState.结束,type:
		// SpecialRepaymentType.提前扣款,state2: SpecialRepaymentState.申请, date:
		// promiseReturnDate1])

	}

	@Override
	public LoanSpecialRepayment findSpecialRepaymentByLoanIdAndTypeAndState(
			SpecialRepayParamsVO params) {
		LoanSpecialRepayment loanSpecialRepayment = this
				.getSqlSession()
				.selectOne(
						getIbatisMapperNameSpace()
								+ ".findSpecialRepaymentByLoanIdAndTypeAndState",
						params);
		return loanSpecialRepayment;
	}

	public Pager findSpecialRepaymentList(Map<String, Object> map) {
		Pager pager = (Pager) map.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanSpecialRepayment");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanSpecialRepaymentCount");
		return doPager(pager, map);
	}

	@Override
	public int findSpecialRepaymentByLoanId(Long loanId) {
		Object o = null;
		int count = 0;
		o = this.getSqlSession().selectOne(
				getIbatisMapperNameSpace() + ".findSpecialRepaymentByLoanId",
				loanId);
		if (o != null && !"".equals(o)) {
			count = Integer.parseInt(o.toString());
		}
		return count;
	}
	public int findLoanByUserIdAndLoanState(String userId) {
		Object o = null;
		int count = 0;
		o = this.getSqlSession().selectOne(
				getIbatisMapperNameSpace() + ".findLoanByUserIdAndLoanState",
				userId);
		if (o != null && !"".equals(o)) {
			count = Integer.parseInt(o.toString());
		}
		return count;
	}
	public Pager ZBfindSpecialRepaymentList(Map<String, Object> map) {
		Pager pager = (Pager) map.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanSpecialRepaymentZB");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanSpecialRepaymentCountZB");
		return doPager(pager, map);
	}
	
	@Override
	public int findLoanSpecialRepaymentByStateAndLoanId(Long loanId) {
		// TODO Auto-generated method stub
		Object o = null;
		int count = 0;
		o = this.getSqlSession().selectOne(
				getIbatisMapperNameSpace() + ".findLoanSpecialRepaymentByStateAndLoanId",
				loanId);
		if (o != null && !"".equals(o)) {
			count = Integer.parseInt(o.toString());
		}
		return count;
	}
	
	@Override
	public void updateFYJM(LoanSpecialRepayment loanSpecialRepayment) {
		// TODO Auto-generated method stub
		this.getSqlSession().update(getIbatisMapperNameSpace() + ".updateFYJM",loanSpecialRepayment);
	}
	@Override
	public Pager findListByMapLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Pager pager = (Pager) map.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".findListByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".findListByMapCount");
		return doPager(pager, map);
	}
	
	@Override
	public List findSpecialRepaymentByUserAndDate(String loanId) {
		
		return this.getSqlSession().selectList(getIbatisMapperNameSpace()
				+ ".findSpecialRepaymentByUserAndDate", loanId);
	}

	@Override
	public List<BsyhSpecialRepayVo> findBsyhSpecialRepay(Date date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currDate", date);
		return this.getSqlSession().selectList(getIbatisMapperNameSpace()
				+ ".findBsyhSpecialRepay",map);
	}

	@Override
	public List<BsyhSpecialRepayVo> findBsyhSpecialRepayAll() {
		return this.getSqlSession().selectList(getIbatisMapperNameSpace()
				+ ".findBsyhSpecialRepayAll");
	}

	@Override
	public List<LoanSpecialRepayment> findReleifApplyEffect(Long applyId) {
		Map<String,Object> params = new HashMap<>();
		params.put("applyId",applyId);
		List<LoanSpecialRepayment> loanSpecialRepayments = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findReleifApplyEffect",params);
		if (CollectionUtils.isEmpty(loanSpecialRepayments)) {
			return Collections.emptyList();
		}
		return loanSpecialRepayments;
	}
}
