package com.zdmoney.credit.core.service.pub;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import com.zdmoney.credit.common.domain.LoanContractTrialRepVo;
import com.zdmoney.credit.common.domain.ResidualPactMoneyRepVo;
import com.zdmoney.credit.common.vo.core.LoanTrialVo;
import com.zdmoney.credit.common.vo.core.LoanVo;
import com.zdmoney.credit.common.vo.core.PersonVo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100003Vo;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100007Vo;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;

public interface ILoanCoreService {
	
	/**
	 * 保存债权数据
	 * @param params
	 * @return
	 */
	public Map<String, Object> importSingleAPSLoan(LoanVo params) throws SQLException, Exception;
	
	/**
	 * 生成债权，创建还款计划
	 * @param params
	 * @return
	 */
	public Map<String, Object> createAPSLoan(LoanVo params) throws Exception;
	
	/**
	 * 更新借款状态接口
	 * @param params
	 * @return
	 */
	public Map<String, Object> updateLoanState(LoanVo params);
	
	/**
	 * 还款状态查询接口
	 * @param params
	 * @return
	 */
	public Map<String, Object> paymentStatus(PersonVo params);
	
	/**
	 * 客户债权状态查询接口
	 * @param params
	 * @return
	 */
	public Map<String, Object> loanStatus(PersonVo params);
	
	/**
	 * 初始化核心接口配置信息
	 */
	public void initFieldMapper();
	
	/**
	 * 贷前试算接口
	 * @param params
	 * @return
	 */
	public Map<String, Object> createLoanTrial(LoanTrialVo params) throws Exception;
	
	/**
	 * 查看借款接口
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryLoan(LoanVo params) throws Exception;
	
	/**
	 * 批量更新借款状态接口
	 * @param params
	 * @return
	 */
	public Map<String, Object> batchUpdateLoanState(LoanVo params);
	
	/**
	 * 内部匹配查询债权状态
	 * @param params
	 * @return
	 */
	public Map<String, Object> neibupipei(LoanVo params);
	
	/**
	 * 根据APP_NO查询债权状态
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryLoanState(LoanVo params);
	/**
	 * 包商银行放款后调用生成还款计划接口
	 * @return 
	 */
	public void createRepaymentDetailAfterGrantMoney(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct);

	/**
	 * 获取陆金所 还款计划 与费用计划明细
	 * @param appNo
	 * @param lufax100007Vo
	 */
	public void getRepayPlanAndFeePlay4Lufax(LoanVo params, Lufax100007Vo lufax100007Vo);

	/**
	 * 获取陆金所 确认合同 接口数据
	 * @param params
	 * @param lufax100003Vo
	 */
	public void getConfirmContractData4Lufax(LoanVo params, Lufax100003Vo lufax100003Vo);
	
	/**
	 * 放款后重新生成还款计划  改变loan_product 开始还款日期，结束还款日期，约定还款日
	 * @return 
	 */
	public void createRepaymentDetailAfterGrantLufax(Long loanId,Date date);
	
	/**
	 * 新增或更新包商银行业务流水号 接口
	 * @param loanNo
	 * @param busNumber
	 * @return
	 */
	public Map<String, Object> saveOrUpdateBsyhBusNo(String loanNo,
			String busNumber);

	/**
	 * 借款合同试算接口
	 * @param params
	 * @return
	 */
	public Map<String, Object> createLoanContractTrial(LoanContractTrialRepVo params) throws Exception;

	public Map<String,Object> getResidualPactMoney(ResidualPactMoneyRepVo params);

	/**
	 * 查询借款详细信息接口
	 * @param appNo
	 * @return
	 */
	public Map<String, Object> findLoanDetails(String appNo);
	
	/**
	 * 根据APP_NO查询债权状态
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryLoanStateNew(LoanVo params);
}
