package com.zdmoney.credit.ljs.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.framework.vo.lufax.entity.LoanDetailLufax;
import com.zdmoney.credit.ljs.domain.LoanStatusLufax;

/**
 * @author 10098  2017年5月15日 上午9:53:26
 */
public interface ILoanStatusLufaxDao extends IBaseDao<LoanStatusLufax> {

	/**
	 * 查找债权状态信息
	 * @param params
	 * @return
	 */
	public List<LoanDetailLufax> findLoanDetailLufaxList(Map<String, Object> params);

	/**
	 * 查找待推送的债权信息
	 * @param params
	 * @return
	 */
	public List<LoanStatusLufax> findLoanStatusLufax2Transmit(Map<String, Object> params);

	public void updateLufuxStatusToYuqi();
}
