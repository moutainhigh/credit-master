package com.zdmoney.credit.repay.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IYubokuanDao;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;
import com.zdmoney.credit.repay.vo.LoanApplyDetailVo;
import com.zdmoney.credit.repay.vo.YubokuanDetailVo;

@Repository
public class YubokuanDaoImpl extends BaseDaoImpl<LoanPreApplyRecord> implements IYubokuanDao {

	@Override
	public Pager queryPageList(Map<String, Object> params) {
		 Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryPageList");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryPageCount");
        return doPager(pager, params);
	}

	@Override
	public LoanPreApplyRecord findLastRecord(Map<String, Object> params) {
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLastRecord",params);
	}

	@Override
	public List<Map<String, Object>> findPreConfirmFileParams(LoanPreApplyRecord loanPreApplyRecord) {
		
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findPreConfirmFileParams", loanPreApplyRecord);
	}

	@Override
	public List<YubokuanDetailVo> findGrantDetailList(LoanPreApplyRecord loanPreApplyRecord) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findGrantDetailList", loanPreApplyRecord);
	}

	@Override
	public List<Map<String, Object>> findPayPlanList(
			LoanPreApplyRecord loanPreApplyRecord) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findPayPlanList", loanPreApplyRecord);
	}
	
}