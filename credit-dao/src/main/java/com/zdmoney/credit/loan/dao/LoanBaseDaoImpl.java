package com.zdmoney.credit.loan.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.LoanBaseAppVo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.vo.LoanImageFile;
import com.zdmoney.credit.loan.vo.TotalReturnLoanInfo;

@Repository
public class LoanBaseDaoImpl extends BaseDaoImpl<LoanBase> implements
		ILoanBaseDao {

	@Override
	public int updateLoanStateAtEndOfDay() {
		// Loan.executeUpdate("update Loan set loanState=:state where id in(select R.loan.id from RepaymentDetail as R where repaymentState in(:whk,:bzehk,:bzfx) and returnDate<=:date group by loan.id ) and loanState=:state2  ",\
		// [state:LoanState.逾期,whk:zdsys.RepaymentState.未还款,bzehk:zdsys.RepaymentState.不足额还款,bzfx:zdsys.RepaymentState.不足罚息
		// ,date:new Date(),state2:LoanState.正常]);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("returnDate", new Date());

		int affectNum = 0;
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateLoanStateAtEndOfDay",map);
		return affectNum;
	}

	@Override
	public LoanBase findByLoanId(Long id) {
		return get(id);
	}

	@Override
	public int updatePaidoffOverdueToZC(Long loanId, Date tradeDate) {
		// zdsys.Loan.executeUpdate("update Loan set loanState=:state where id=:loanid and loanState<>:state1 and loanState<>:state2 and not exists(select 1 from RepaymentDetail as r where r.loan.id=:loanid and r.repaymentState in (:whk,:bzehk,:bzfx) and r.returnDate<:date)",
		// [state: LoanState.正常,state1:LoanState.结清,state2:LoanState.预结清,loanid:
		// loanInfo.id,whk:zdsys.RepaymentState.未还款,bzehk:zdsys.RepaymentState.不足额还款,bzfx:zdsys.RepaymentState.不足罚息,date:
		// repay.riTradeDate])
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanId", loanId);
		map.put("tradeDate", tradeDate);
		int affectNum = 0;
		affectNum = getSqlSession().update(
				getIbatisMapperNameSpace() + ".updatePaidoffOverdueToZC", map);
		return affectNum;
	}

	@Override
	public int updateLoanState(Long id, String state) {
		LoanBase loanBase = new LoanBase();
		loanBase.setLoanState(state);
		loanBase.setId(id);
		loanBase.setUpdateTime(new Date());

		int affectNum = 0;
		affectNum = getSqlSession().update(
				getIbatisMapperNameSpace() + ".update", loanBase);
		return affectNum;
	}

	@Override
	public Pager findLoanBaseList(Map<String, Object> map) {
		Pager pager = (Pager) map.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".findLoanBaseList");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".findLoanBaseListCount");
		return doPager(pager, map);
	}

	@Override
	public int updateIssendByStateAndIssend(Map<String, Object> params) {
		int affectNum = 0;
		affectNum = getSqlSession().update(
				getIbatisMapperNameSpace() + ".updateIssendByStateAndIssend",
				params);
		return affectNum;
	}

	@Override
	public Pager getLoanBaseBybatchNum(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".getLoanBaseBybatchNum");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".getLoanBaseBybatchNumCount");
		return doPager(pager, paramMap);
	}

	@Override
	public Pager searchapprovalWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".searchapprovalWithPg");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".searchapprovalWithPgCount");
		return doPager(pager, paramMap);
	}
	
	@Override
	public Pager findLoanSpecialRepaymentTQKK(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".findLoanSpecialRepaymentTQKK");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".findLoanSpecialRepaymentTQKKCount");
		return doPager(pager, paramMap);
	}

	@Override
	public List<LoanImageFile> findLoanImageList(Map<String, Object> map) {
		String sqlId = getIbatisMapperNameSpace() + ".findLoanImageList";
        return getSqlSession().selectList(sqlId,map);
	}
	@Override
	public List<LoanImageFile> findLoanImageListXsOrHt(Map<String, Object> map) {
		String sqlId = getIbatisMapperNameSpace() + ".findLoanImageListXsOrHt";
        return getSqlSession().selectList(sqlId,map);
	}
	public List<LoanImageFile> findLoanBtchImageList(Map<String, Object> map) {
		String sqlId = getIbatisMapperNameSpace() + ".findLoanBtchImageList";
        return getSqlSession().selectList(sqlId,map);
	}
	@Override
	public List<LoanImageFile> findLoanImageListBlackName(Map<String, Object> map) {
		String sqlId = getIbatisMapperNameSpace() + ".findLoanImageListBlackName";
        return getSqlSession().selectList(sqlId,map);
	}
	@Override
	public Pager loanReturnWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanReturnWithPg");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanReturnWithPgCount");
		return doPager(pager, paramMap);
	}

	@Override
	public int updateLoanBaseByLoanReturn(LoanBase loanBase) {
		int affectNum = 0;
		affectNum = getSqlSession().update(
				getIbatisMapperNameSpace() + ".updateLoanBaseByLoanReturn", loanBase);
		return affectNum;
	}

	@Override
	public TotalReturnLoanInfo searchTotalReturnLoanInfo(Map<String, Object> map) {
		List<TotalReturnLoanInfo> totalInfos = getSqlSession().selectList( getIbatisMapperNameSpace()+".searchTotalReturnLoanInfo",map);
		TotalReturnLoanInfo totalInfo = null;
		if (null != totalInfos && !totalInfos.isEmpty() && totalInfos.size() == 1) {
			totalInfo = totalInfos.get(0);
		}
		return totalInfo;
	}

	/**
	 * 根据id更新状态、流程状态
	 * @param id
	 * @param state
	 */
	@Override
	public int updateAPSLoanState(LoanBase loanBase) {
		int affectNum = 0;
		/** 获取登陆者信息 **/
		User user = UserContext.getUser();
		if (user == null) {
			loanBase.setUpdator("admin");
		} else {
			loanBase.setUpdator(user.getName());
		}
		loanBase.setUpdateTime(new Date());
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateLoanState", loanBase);
		return affectNum;
	}

	@Override
	public LoanBaseAppVo findAppNo(Map<String, Object> map) {
		List<LoanBaseAppVo> loanBaseAppVos = getSqlSession().selectList( getIbatisMapperNameSpace()+".findAppNo",map);
		LoanBaseAppVo loanBaseAppVo = null;
		if (null != loanBaseAppVos && !loanBaseAppVos.isEmpty() && loanBaseAppVos.size() == 1) {
			loanBaseAppVo = loanBaseAppVos.get(0);
		}
		return loanBaseAppVo;
	}

	public List<LoanBaseAppVo> findAppNoList(Map<String, Object> map){
		String sqlId = getIbatisMapperNameSpace() + ".findAppNoList";
        return getSqlSession().selectList(sqlId,map);
	}

	@Override
	public List<LoanBase> getAllLoanBaseInfo(Map<String, Object> map) {
		List<LoanBase> loanBaseList = getSqlSession().selectList( getIbatisMapperNameSpace()+".getAllLoanBaseInfo",map);
		return loanBaseList;
	}

    /**
     * 分页查询债权批次信息
     * @param params
     * @return
     */
    public Pager queryBatchNumInfo(Map<String, Object> params) {
        Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryBatchNumInfoList");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryBatchNumInfoCount");
        return doPager(pager, params);
    }
	
    /**
	 * 通过客服id获取所属网点(管理营业部)
	 * */
	public Long getSalesDepartmentId(Long newCrmId) {
		// TODO Auto-generated method stub
		System.out.println("newCrmId:::"+newCrmId);
		 return  getSqlSession().selectOne(getIbatisMapperNameSpace()+".getSalesDepartmentId",newCrmId);
	   
	}

	@Override
	public Long findLoanIdByContractNum(String contractNum) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".findLoanIdByContractNum",contractNum);
	}

	@Override
	public List<String> findBatchNumByFundsSources(String fundsSources) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findBatchNumByFundsSources",fundsSources);
	}

	@Override
	public Pager findBuyBackLoanWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".searchBuyBackLoanWithPg");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".searchBuyBackLoanWithPgCount");
		return doPager(pager, paramMap);
	}

	@Override
	public int updateLoanBaseByBuyBackLoan(LoanBase loanBase) {
		int affectNum = 0;
		affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateLoanBaseByBuyBackLoan", loanBase);
		return affectNum;
	}
	
	@Override
	public List<Map<String, Object>> getRechargeSearchList() {
		return getSqlSession().selectList(this.getIbatisMapperNameSpace() + ".getRechargeSearchList");
	}
}
