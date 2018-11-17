package com.zdmoney.credit.fee.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.vo.CreateLoanFeeVo;

/**
 * 借款收费主表 Service层接口，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
public interface ILoanFeeInfoService {

	/**
	 * PK换实体
	 * 
	 * @param id
	 */
	public LoanFeeInfo findById(Long id);

	/**
	 * 跟据实体类查询
	 * 
	 * @param loanFeeInfo
	 */
	public List<LoanFeeInfo> findListByVo(LoanFeeInfo loanFeeInfo);

	/**
	 * 生成收费主记录
	 * 
	 * @param createLoanFeeVo
	 * @return
	 */
	public LoanFeeInfo createLoanFee(CreateLoanFeeVo createLoanFeeVo);

	/**
	 * 生成收费主记录同时调用核心放款接口（2步在同事务中）
	 * 
	 * @param createLoanFeeVo
	 * @return
	 */
	public LoanFeeInfo createLoanFeeWithGrantLoan(CreateLoanFeeVo createLoanFeeVo);

	/**
	 * 更新操作
	 * 
	 * @param loanFeeInfo
	 * @return
	 */
	public LoanFeeInfo updateFeeInfo(LoanFeeInfo loanFeeInfo);

	/**
	 * 根据债权编号查询借款收费信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public LoanFeeInfo findLoanFeeInfoByLoanId(Long loanId);
	
	/**
	 * 外贸信托线下放款导入信息验证
	 * @param dataList
	 * @return 
	 */
	public List<String> checkWaiMaoXinTuo(List<String []> dataList);
	/**
	 * 第三方线下放款导入信息验证
	 * 
	 * @param sheetDataList
	 */
	public void checkOffLineLoanInfo(List<Map<String, String>> sheetDataList);

	/**
	 * 针对龙信小贷、外贸信托、外贸2，判断是否已经完成划扣服务费
	 * 
	 * @param loanId
	 * @return
	 */
	public boolean isAlreadyDebitServiceCharge(Long loanId);
	/**
	 * 针对外贸3，判断是否已经完成划扣服务费
	 * 
	 * @param loanId
	 * @return
	 */
	//public boolean isAlreadyDebitServiceChargeWM3(Long loanId);
}
