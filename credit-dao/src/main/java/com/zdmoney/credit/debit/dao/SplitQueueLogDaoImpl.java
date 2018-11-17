package com.zdmoney.credit.debit.dao;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.ljs.vo.SplitRepaymentVo;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.vo.core.OneBuyBackCompensatedVO;
import com.zdmoney.credit.common.vo.core.RepayInfoLufaxVO;
import com.zdmoney.credit.common.vo.core.SplitQueueManangeVO;
import com.zdmoney.credit.debit.dao.pub.ISplitQueueLogDao;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class SplitQueueLogDaoImpl extends BaseDaoImpl<SplitQueueLog> implements	ISplitQueueLogDao {
	@Override
	public List<RepayInfoLufaxVO> findRepaymentInfoToLufax(Map<String, Object> map) {
		 return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findRepaymentInfoToLufax", map);
	}

	@Override
	public List<SplitQueueLog> findSplitQueueLogListByParams(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findSplitQueueLogListByParams", params);
	}

	@Override
	public List<SplitRepaymentVo> findSplitRepayment4Lufax(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findSplitRepayment4Lufax", params);
	}

	@Override
	public List<RepayInfoLufaxVO> findDebitSplitQueueLogList(Map<String, Object> map) {
		 return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findDebitSplitQueueLogList", map);
	}
	
	@Override
    public List<RepayInfoLufaxVO> findRepayInfoLufaxList(Map<String, Object> map) {
         return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findRepayInfoLufaxList", map);
    }

	@Override
	public Integer queryPreSplitCount(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() +".queryPreSplitCount", params);
	}

	@Override
	public List<SplitQueueManangeVO> findNotToThirdSplitList(Map<String, Object> params) {
		 return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findNotToThirdSplitList", params);
	}

	@Override
	public List<OneBuyBackCompensatedVO> findOneBuyBackAndCompensatedRepayInfo() {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOneBuyBackAndCompensatedRepayInfo");
	}

	@Override
	public List<SplitQueueLog> findOverdueEntrustSplitQueueLogList(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOverdueEntrustSplitQueueLogList", params);
	}

	@Override
	public List<RepayInfoLufaxVO> findAdvanceEntrustSplitQueueLogList(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findAdvanceEntrustSplitQueueLogList", params);
	}
}
