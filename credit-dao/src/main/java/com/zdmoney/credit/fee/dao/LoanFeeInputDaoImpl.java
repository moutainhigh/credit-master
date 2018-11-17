package com.zdmoney.credit.fee.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeInputDao;
import com.zdmoney.credit.fee.domain.vo.LoanFeeInput;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 收费录入Dao层实现类
 * @author 00236640
 * @version $Id: LoanFeeInputDaoImpl.java, v 0.1 2016年7月14日 下午5:47:59 00236640 Exp $
 */
@Repository
public class LoanFeeInputDaoImpl extends BaseDaoImpl<LoanFeeInput> implements ILoanFeeInputDao {

	@Override
	public Pager searchVLoanInfoFeeWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".searchVLoanInfoFeeResult");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".searchVLoanInfoFeeCount");
        return doPager(pager, paramMap);
	}

}
