package com.zdmoney.credit.repay.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;
import com.zdmoney.credit.repay.vo.LoanApplyDetailVo;
import com.zdmoney.credit.repay.vo.YubokuanDetailVo;

/**
 * 预拨申请管理DAO接口类
 * @author YM10104
 *
 */
public interface IYubokuanDao extends IBaseDao<LoanPreApplyRecord>{

	Pager queryPageList(Map<String, Object> params);

	LoanPreApplyRecord findLastRecord(Map<String, Object> params);
	/**
	 * 查找预拨确认书填充信息
	 * @param loanPreApplyRecord
	 * @return 
	 */
	List<Map<String, Object>> findPreConfirmFileParams(LoanPreApplyRecord loanPreApplyRecord);
	/**
	 * 查询放款成功明细  
	 * @param loanPreApplyRecord
	 * @return
	 */
	List<YubokuanDetailVo> findGrantDetailList(LoanPreApplyRecord loanPreApplyRecord);
	/**
	 * 查询 还款计划
	 * @param loanPreApplyRecord
	 * @return
	 */
	List<Map<String, Object>> findPayPlanList(
			LoanPreApplyRecord loanPreApplyRecord);
	
}
