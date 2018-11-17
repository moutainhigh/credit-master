package com.zdmoney.credit.grant.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyAccountWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyBorrowerWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyLoanWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyRelationWm3Entity;
import com.zdmoney.credit.grant.dao.pub.ILoanBaseGrantDao;
import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.grant.vo.GrantAccountVo;
import com.zdmoney.credit.grant.vo.GrantApplyVo;
import com.zdmoney.credit.grant.vo.GrantBorRelaPerVo;
import com.zdmoney.credit.grant.vo.GrantBorrowPersonVo;
import com.zdmoney.credit.grant.vo.GrantRepaymentDetailVo;
import com.zdmoney.credit.grant.vo.LoanBaseGrantVo;

/**
 * Created by ym10094 on 2016/11/9.
 */
@Repository
public class LoanBaseGrantDaoImpl extends BaseDaoImpl<LoanBaseGrant> implements ILoanBaseGrantDao {
    
    @Override
    public LoanBaseGrant findLoanBaseGrantById(long grantApplyId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("id",grantApplyId);
        List<LoanBaseGrant> loanBaseGrants = this.findListByMap(paramMap);
        if (CollectionUtils.isEmpty(loanBaseGrants)) {
            return null;
        }
        return loanBaseGrants.get(0);
    }

    @Override
    public Pager queryFinanceGrantPage(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryFinanceGrantInfosDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryFinanceGrantInfosCount");
        return doPager(pager,paramMap);
    }

    @Override
    public List<LoanBaseGrantVo> exportFinanceGrantInfos(Map<String, Object> paramMap) {
        List<LoanBaseGrantVo> loanBaseGrantVos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".exportFinanceGrantInfos",paramMap);
        return loanBaseGrantVos;
    }

    @Override
    public GrantApplyVo getFinanceGrantApplyVo(Long loanId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getFinanceGrantApplyVo",paramMap);
    }

    @Override
    public List<GrantAccountVo> getFinanceGrantAccountVo(Long loanId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        List<GrantAccountVo> accountVos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getFinanceGrantAccountVo", paramMap);
        return accountVos;
    }

    @Override
    public List<GrantBorrowPersonVo> getFinanceGrantBorrowPersonVo(Long borrowerId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("borrowerId",borrowerId);
        List<GrantBorrowPersonVo> borrowPersonVos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getFinanceGrantBorrowPersonVo", paramMap);
        return borrowPersonVos;
    }

    @Override
    public List<GrantBorRelaPerVo> getFinanceGrantBorRelaPerVo(Long borrowerId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("borrowerId",borrowerId);
        List<GrantBorRelaPerVo> borRelaPerVos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getFinanceGrantBorRelaPerVo",paramMap);
        return borRelaPerVos;
    }

    @Override
    public List<GrantRepaymentDetailVo> getFinanceGrantRepaymentDetailVo(Long loanId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        List<GrantRepaymentDetailVo> repaymentDetailVos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getFinanceGrantRepaymentDetailVo",paramMap);
        return repaymentDetailVos;
    }

    @Override
    public LoanBaseGrant findLoanBaseGrantByappNo(String appNo,String grantState) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appNo",appNo);
        paramMap.put("grantState",grantState);
        List<LoanBaseGrant> loanBaseGrants = this.findListByMap(paramMap);
        if (CollectionUtils.isEmpty(loanBaseGrants)) {
            return null;
        }
        return loanBaseGrants.get(0);
    }

    @Override
    public LoanBaseGrant findLoanBaseGrantcontractNum(String contractNum, String grantState) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("contractNum",contractNum);
        paramMap.put("grantState",grantState);
        List<LoanBaseGrant> loanBaseGrants = this.findListByMap(paramMap);
        if (CollectionUtils.isEmpty(loanBaseGrants)) {
            return null;
        }
        return loanBaseGrants.get(0);
    }

    @Override
    public LoanBaseGrant findLoanBaseGrantByLoanId(Long loanId, String grantState) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("grantState",grantState);
        List<LoanBaseGrant> loanBaseGrants = this.findListByMap(paramMap);
        if (CollectionUtils.isEmpty(loanBaseGrants)) {
            return null;
        }
        return loanBaseGrants.get(0);
    }

    @Override
    public LoanBaseGrant queryLoanGrantBaseRelateVloanInfo(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryLoanGrantBaseRelateVloanInfo",paramMap);
    }

    public Pager queryFinanceGrantPage(LoanBaseGrantVo loanBaseGrantVo) {
        Pager pager = loanBaseGrantVo.getPager();
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryFinanceGrantInfosDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryFinanceGrantInfosCount");
        return doPager(pager,loanBaseGrantVo);
    }
    
    public Pager queryLoanBaseGrantDetailPage(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryLoanBaseGrantDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryLoanBaseGrantCount");
        return doPager(pager,paramMap);
    }

    @Override
    public int queryFinanceGrantInfosCount(Map<String, Object> paramMap) {
        Object count = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryFinanceGrantInfosCount",paramMap);
        int totalCount = Strings.convertValue(count.toString(), Integer.class);
        return totalCount;
    }

	@Override
	public List<LoanBaseGrant> findAbsData2Upload(Map<String, Object> params) {
		List<LoanBaseGrant> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findAbsData2Upload", params);
		return rstList;
	}
	
	@Override
    public List<LoanBaseGrantVo> queryGrantApplyDetail(Map<String, Object> paramMap){
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryGrantApplyDetail",paramMap);
    }

	@Override
	public ApplyLoanWm3Entity findApplyLoanInfo4WM3(Long loanId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findApplyLoanInfo4WM3",paramMap);
	}


	@Override
	public List<ApplyBorrowerWm3Entity> findApplyBorrower4WM3(Long borrowerId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("borrowerId",borrowerId);
        List<ApplyBorrowerWm3Entity> borrList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findApplyBorrower4WM3", paramMap);
        return borrList;
	}


	@Override
	public List<ApplyRelationWm3Entity> findApplyRelation4Wm3(Long borrowerId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("borrowerId",borrowerId);
        List<ApplyRelationWm3Entity> relList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findApplyRelation4Wm3",paramMap);
        return relList;
	}


	@Override
	public List<ApplyAccountWm3Entity> findApplyAccount4Wm3(Long loanId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        List<ApplyAccountWm3Entity> listAc = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findApplyAccount4Wm3", paramMap);
        return listAc;
	}

	@Override
	public List<LoanBaseGrant> findHrbhAndBh2Infos() {
		List<LoanBaseGrant> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findHrbhAndBh2Infos");
		return rstList;
	}
}
