package com.zdmoney.credit.loan.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.repay.vo.RepayStateDetail;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.vo.core.PublicStoreAccountVo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.vo.LoanRepaymentDetailVo;
import com.zdmoney.credit.loan.vo.TotalMoney;
import com.zdmoney.credit.loan.vo.VLoanDebtFileInfo;
import com.zdmoney.credit.loan.vo.VLoanDebtInfo;
import com.zdmoney.credit.loan.vo.VeloanExport;
import com.zdmoney.credit.loan.vo.VloanDebtCheckInfo;
import com.zdmoney.credit.loan.vo.VloanJmhzExport;
import com.zdmoney.credit.loan.vo.VloanPersonInfo;
import com.zdmoney.credit.system.domain.ComOrganization;

/**
 * @author 00232949
 */
@Repository
public class VLoanInfoDaoImpl extends BaseDaoImpl<VLoanInfo> implements IVLoanInfoDao {

    @Override
    public List<VLoanInfo> getSpecialLoanByDate(ComOrganization salesDepartment, Date currDate,
            SpecialRepaymentTypeEnum type, SpecialRepaymentStateEnum state, List<String> debitTypeList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("specialRepaymentType", type.getValue());
        map.put("specialRepaymentState", state.getValue());
        map.put("requestDate", currDate);
        map.put("orgCode", salesDepartment.getOrgCode());
        if (CollectionUtils.isNotEmpty(debitTypeList)) {
            map.put("debitTypes", debitTypeList);
        }
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getSpecialLoanByDate", map);
    }

    @Override
    public List<VLoanInfo> getOverdueLoanByOrg(ComOrganization salesDepartment, List<String> debitTypeList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgCode", salesDepartment.getOrgCode());
        if (CollectionUtils.isNotEmpty(debitTypeList)) {
            map.put("debitTypes", debitTypeList);
        }
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getYQHKLoanByOrg", map);
    }

    @Override
    public List<VLoanInfo> getLoanByOrgAndRepDate(ComOrganization salesDepartment, Date promiseReturnDate, List<String> debitTypeList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orgCode", salesDepartment.getOrgCode());
        map.put("zcstate", LoanStateEnum.正常.getValue());
        Calendar ca = Calendar.getInstance();
        ca.setTime(promiseReturnDate);
        map.put("promiseReturnDate", ca.get(Calendar.DAY_OF_MONTH));
        if (CollectionUtils.isNotEmpty(debitTypeList)) {
            map.put("debitTypes", debitTypeList);
        }
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getLoanByOrgAndRepDate", map);
    }

    public VLoanInfo getLoanByIdnumAndName(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getLoanByIdnumAndName", params);
    }

    public VLoanInfo getVLoanByIdnumAndName(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getVLoanByIdnumAndName", params);
    }

    public VLoanInfo getVLoanBySignDateAndBorrowerAndLoanType(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getVLoanBySignDateAndBorrowerAndLoanType", params);
    }

    public VLoanInfo getVLoanBlackInfo(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getVLoanBlackInfo", params);
    }

    @Override
    public List<VLoanInfo> getYQHKLoanByPromisereturnDate(int promisereturnDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("promiseReturnDate", promisereturnDate);
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getYQHKLoanByPromisereturnDate", map);
    }
    
    @Override
    public List<VLoanInfo> getYQHKLoanByPromisereturnDateAndLoanBelong(int promisereturnDate, String loanBelong) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("promiseReturnDate", promisereturnDate);
        map.put("loanBelong", loanBelong);
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getYQHKLoanByPromisereturnDateAndLoanBelong", map);
    }

    /***
     * 还款录入模块多表查询(分页查询)
     * @param paramMap 参数集合
     * @return
     */
    @Override
    public Pager searchRepaymentLoanWithPg(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchRepaymentestLoan");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchRepaymentestLoanCount");
        return doPager(pager, paramMap);
    }

    @Override
    public List<VLoanInfo> searchVLoanInfoList(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".searchVLoanInfoList", paramMap);
    }

    @Override
    public Pager searchVLoanInfoWithPg(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchVLoanInfoRepayTrailListResult");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchVLoanInfoRepayTrailCount");
        return doPager(pager, paramMap);
    }

    @Override
    public List<VLoanInfo> queryLoanForCTS(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryLoanForCTS", paramMap);
    }

    @Override
    public List<VLoanInfo> searchVLoanInfo(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".searchVLoanInfo", params);
    }

    @Override
    public List<String> getWillSendPhone4WhiteList(Integer returnDate) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getWillSendPhone4WhiteList", returnDate);
    }

    public List<Map<String, Object>> queryForLoanExport(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".queryForLoanExport", params);
    }

    public List<Map<String, Object>> queryForLoanRepayExp(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".queryForLoanRepayExp", params);
    }

    /**
     * 积木盒子债权导出
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getExternalDebtList(Map<String, Object> params) {
        String sqlId = getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".getExternalDebtList";
        return getSqlSession().selectList(sqlId, params);
    }

    @Override
    public Pager listquerBatchNumDetail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumDetailCount");
        return doPager(pager, paramMap);
    }

    @Override
    public Pager getCurrentDayLoanBaseBybatchNum(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        // 理财机构
        String org = Strings.convertValue(paramMap.get("org"), String.class);
        if ("XL".equals(org) || "AT".equals(org)) {// 新浪财富
            pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listxlCurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listxlCurrentDayExternalDebtCount");
        } else if ("WC2-".equals(org)) {// 挖财2财富
            pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listwc2-CurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listwc2-CurrentDayExternalDebtCount");
        } else if("LXXD".equals(org)){
        	 pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listLxxdCurrentDayExternalDebt");
             pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listLxxdCurrentDayExternalDebtCount");
        } else if("WMXT".equals(org)){
       	     pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listWmxtCurrentDayExternalDebt");
             pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listWmxtCurrentDayExternalDebtCount");
       } else if ("HM".equals(org)) {
            pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listhm-CurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listhm-CurrentDayExternalDebtCount");
        } else if("BHXT".equals(org)){
            pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listbhxt-CurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listbhxt-CurrentDayExternalDebtCount");
        }else if("BH2-".equals(org)){
            pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listbhxt2-CurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listbhxt2-CurrentDayExternalDebtCount");        	
        }else if("WM2-".equals(org)){
            pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listwm2-CurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listwm2-CurrentDayExternalDebtCount");        	
        }else if("BSYH".equals(org)){
        	pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listbsyhCurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".lisbsyhCurrentDayExternalDebtCount");  
        } else if("HRBH".equals(org)){
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listbhxt3-CurrentDayExternalDebt");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listbhxt3-CurrentDayExternalDebtCount");
        }
        else {// 其他
            pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listCurrentDayExternalDebt");
            pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listCurrentDayExternalDebtCount");
        }
        return doPager(pager, paramMap);
    }

    @Override
    public List<VeloanExport> getVeloanExportList(Map<String, Object> params) {
        String sqlId = getIbatisMapperNameSpace() + ".getVeloanExportList";
        return getSqlSession().selectList(sqlId, params);
    }

    public List<VloanJmhzExport> getVeloanJmhzExportList(Map<String, Object> params) {
        String sqlId = getIbatisMapperNameSpace() + ".getVeloanExportJmhzList";
        return getSqlSession().selectList(sqlId, params);
    }

    public List<TotalMoney> getTotalMoney(Map<String, Object> paramMap) {
        String sqlId = getIbatisMapperNameSpace() + ".getTotalMoney";
        return getSqlSession().selectList(sqlId, paramMap);
    }

    public List<LoanRepaymentDetailVo> getVeloanReymentExportList(Map<String, Object> params) {
        String sqlId = getIbatisMapperNameSpace() + ".getVeloanReymentExportList";
        return getSqlSession().selectList(sqlId, params);
    }

    public List<VLoanDebtInfo> getVLoanDebtInfoExportList(Map<String, Object> paramMap) {
        String sqlId = getIbatisMapperNameSpace() + ".getVLoanDebtInfoExportList";
        return getSqlSession().selectList(sqlId, paramMap);
    }

    public List<VloanDebtCheckInfo> getVLoanDebtCheckInfoExportList(Map<String, Object> paramMap) {
        String sqlId = getIbatisMapperNameSpace() + ".getVLoanDebtCheckInfoExportList";
        return getSqlSession().selectList(sqlId, paramMap);
    }

    public int checkBatchNum(List<Long> list) {
        int total = 0;
        total = (int) getSqlSession().selectOne(getIbatisMapperNameSpace() + ".checkBatchNum", list);
        return total;
    }

    public int checkBatchNumUpdate(Map<String, Object> paramMap) {
        int total = 0;
        total = (int) getSqlSession().selectOne(getIbatisMapperNameSpace() + ".checkBatchNumUpdate", paramMap);
        return total;
    }

    @Override
    public int updateBatchNum(Map<String, Object> paramMap) {
        int affectNum = 0;
        affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateBatchNum", paramMap);
        return affectNum;
    }

    @Override
    public int updateBatchNumAT(List<Long> list) {
        int affectNum = 0;
        affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateBatchNumAT", list);
        return affectNum;
    }

    @Override
    public int updateBatchNumATByBatchNum(Map<String, Object> paramMap) {
        int affectNum = 0;
        affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateBatchNumATByBatchNum", paramMap);
        return affectNum;
    }

    @Override
    public int updateBatchNumByBatchNumNull(Map<String, Object> paramMap) {
        int affectNum = 0;
        affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateBatchNumByBatchNumNull", paramMap);
        return affectNum;
    }

    @Override
    public int updateBatchNumByBatchNum(Map<String, Object> paramMap) {
        int affectNum = 0;
        affectNum = getSqlSession().update(getIbatisMapperNameSpace() + ".updateBatchNumByBatchNum", paramMap);
        return affectNum;
    }

    @Override
    public Pager listquerBatchNumWC2Detail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumWC2Detail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumWC2DetailCount");
        return doPager(pager, paramMap);
    }

    @Override
    public Pager listquerBatchNumNotWC2Detail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumNotWC2Detail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumNotWC2DetailCount");
        return doPager(pager, paramMap);
    }

    @Override
    public List<VLoanInfo> getVLoanByMap(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getVLoanByMap", params);
    }

    public Pager listquerBatchNumHMDetail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumHMDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumHMDetailCount");
        return doPager(pager, paramMap);
    }
	
    public Pager listquerBatchNumLXXDDetail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumLxxdDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumLxxdDetailCount");
        return doPager(pager, paramMap);
    }

    @Override
    public Pager listquerBatchNumBHXTDetail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBHXTDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBHXTDetailCount");
        return doPager(pager, paramMap);
    }

    @Override
	public Pager searchVLoanInfoListWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager)paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchVLoanInfoRepayMarkListResult");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchVLoanInfoRepayMarkCount");
		return doPager(pager,paramMap);
	}
	
	@Override
	public Pager searchLoanOrderWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager)paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchVLoanOrderListResult");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".searchVLoanOrderCount");
		return doPager(pager,paramMap);
	}
	
	@Override
	public VloanPersonInfo findImportLoanInfo(Map<String, Object> paramMap) {
		VloanPersonInfo vloanPersonInfo= getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findImportLoanInfo", paramMap);
        return vloanPersonInfo;
	}

	@Override
	public VLoanInfo getVLoanInfoByIdnum(String idNum) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("idNum", idNum);
		List<VLoanInfo> vLoanInfoList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".searchVLoanInfoByIdnum", paramMap);
		if(CollectionUtils.isNotEmpty(vLoanInfoList)){
			return vLoanInfoList.get(0);
		}
		return null;
	}

	@Override
	public Pager listquerBatchNumWMXTDetail(Map<String, Object> paramMap) {
		   Pager pager = (Pager) paramMap.get("pager");
	        if (pager == null) {
	            pager = new Pager();
	        }
	        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumWmxtDetail");
	        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumWmxtDetailCount");
	        return doPager(pager, paramMap);
	}

    @Override
    public List<VLoanInfo> findUploadFtpBorrowAppNoCn(Map<String,Object> map) {
        List<VLoanInfo>  vLoanInfoList=getSqlSession().selectList(getIbatisMapperNameSpace() + ".findUploadFtpBorrowAppNoCn",map);
        return vLoanInfoList;
    }
    
	@Override
	public List<LoanRepaymentDetailVo> getWmxtLoanReymentExportList(Map<String, Object> paramMap){
			String sqlId = getIbatisMapperNameSpace() + ".getWmxtloanReymentExportList";
	       return getSqlSession().selectList(sqlId, paramMap);
	}

	@Override
	public List<VLoanDebtFileInfo> getWmxtLoanDebtExportList(
			Map<String, Object> paramMap) {
		String sqlId = getIbatisMapperNameSpace() + ".getWmxtLoanDebtExportList";
		return getSqlSession().selectList(sqlId, paramMap);
	}

	@Override
	public Pager listquerBatchNumBHXT2Detail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBHXT2Detail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBHXT2DetailCount");
        return doPager(pager, paramMap);
	}

    @Override
    public Pager listquerBatchNumBHXT3Detail(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBHXT3Detail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBHXT3DetailCount");
        return doPager(pager, paramMap);
    }

    @Override
	public List<VLoanInfo> findWmxt2VLoanInfo4Upload(Map<String, Object> params) {
		List<VLoanInfo>  vLoanInfoList=getSqlSession().selectList(getIbatisMapperNameSpace() + ".findWmxt2VLoanInfo4Upload",params);
        return vLoanInfoList;
	}

    @Override
    public List<VLoanInfo> findWMXT2ElectronicSignatureVLoanInfo() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findWMXT2ElectronicSignatureVLoanInfo");
    }

	@Override
	public Pager listquerBatchNumWM2Detail(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumWM2Detail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumWM2DetailCount");
        return doPager(pager, paramMap);
	}
    
    @Override
	public VLoanInfo getBelongVLoanInfoByNum(Map<String, Object> params) {
		 return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getBelongVLoanInfoByNum", params);
	}
    
    @Override
	public List<VLoanInfo> findListByBorrowId(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findListByBorrowId", params);
	}
    
    @Override
	public Pager listquerBatchNumBSYHDetail(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
	    if (pager == null) {
	        pager = new Pager();
	    }
	    pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBsyhDetail");
	    pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".listquerBatchNumBsyhDetailCount");
	    return doPager(pager, paramMap);
	}

	@Override
	public List<VLoanInfo> findLoanAgreementByParams(Map<String, Object> params) {
		 return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanAgreementByParams",params);
	}

	public List<VloanPersonInfo> findLoanData2UploadInfo(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanData2UploadInfo", params);
	}

	@Override
	public List<Map<String, Object>> findPersonLoanInfo(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findPersonLoanInfo", params);
	}

	@Override
	public List<VLoanInfo> findById(Long borrowerId) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByIdchangePhone", borrowerId);
	}
	
	@Override
	public List<VLoanInfo> findGrantSuccessNotToWM3(Map<String, Object> map) {
		return  getSqlSession().selectList(getIbatisMapperNameSpace() + ".findGrantSuccessNotToWM3",map);
	}
	@Override
	public Pager getLoanVoByStateWithPg(Map<String, Object> params) {
		Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".getLoanVoByStateWithPg");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".getLoanVoByStateWithPgCount");
        return doPager(pager, params);
	}
	
	public PublicStoreAccountVo getPublicStoreAccountDetail(Map<String, Object> params){
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getPublicStoreAccountDetail",params);
	}

    @Override
    public Pager findAcountLoanManageWithPg(VPersonVisit personVisit) {
        Pager pager = personVisit.getPager();
        Assert.notNull(pager, "分页参数");
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".findWithPGAcountLoanManage");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".countAcountLoanManage");
        return doPager(pager, BeanUtils.toMap(personVisit));
    }

    @Override
    public int updateLoanInfExtByLoanId(Map<String, Object> params) {
        return getSqlSession().update(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME)+".updateLoanInfExtByLoanId", params);
    }

    @Override
    public int addLoanInfoExtByLoanId(Map<String, Object> params) {
        return getSqlSession().insert(getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME)+".addLoanInfoExtByLoanId", params);
    }

	@Override
	public Long getOutSourcing(Long loanId) {
		 return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getOutSourcing", loanId);
	}
	
	@Override
	public RepayStateDetail getRepayStateDetailOverdueValue(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getRepayStateDetailOverdueValue", paramMap);
	}
}
