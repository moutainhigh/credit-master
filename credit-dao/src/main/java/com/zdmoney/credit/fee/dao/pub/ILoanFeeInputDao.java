package com.zdmoney.credit.fee.dao.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.domain.vo.LoanFeeInput;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * 收费录入Dao层接口
 * @author 00236640
 * @version $Id: ILoanFeeInputDao.java, v 0.1 2016年7月14日 下午5:46:34 00236640 Exp $
 */
public interface ILoanFeeInputDao extends IBaseDao<LoanFeeInput> {
	/**
     * 收费查询
     * @param paramMap
     * @return
     */
    public Pager searchVLoanInfoFeeWithPg(Map<String, Object> paramMap);
}
