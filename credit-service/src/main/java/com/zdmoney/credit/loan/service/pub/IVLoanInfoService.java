package com.zdmoney.credit.loan.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.PublicStoreAccountVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.vo.LoanRepaymentDetailVo;
import com.zdmoney.credit.loan.vo.TotalMoney;
import com.zdmoney.credit.loan.vo.VLoanDebtFileInfo;
import com.zdmoney.credit.loan.vo.VLoanDebtInfo;
import com.zdmoney.credit.loan.vo.VeloanExport;
import com.zdmoney.credit.loan.vo.VloanDebtCheckInfo;
import com.zdmoney.credit.loan.vo.VloanJmhzExport;
import com.zdmoney.credit.loan.vo.VloanPersonInfo;
import com.zdmoney.credit.repay.vo.RepayStateDetail;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;
import com.zdmoney.credit.system.domain.ComOrganization;

/**
 * loan视图service
 * 
 * @author 00232949
 *
 */
public interface IVLoanInfoService {

    /**
     * 得到该营业部，指定日期的提前结清loan信息
     * 
     * @param salesDepartment
     * @param currDate
     * @return
     */
    public List<VLoanInfo> getTQJQLoanByDate(ComOrganization salesDepartment, Date currDate);

    /**
     * 得到该营业部，指定日期的提前还款loan信息
     * 
     * @param salesDepartment
     * @param currDate
     * @return
     */
    public List<VLoanInfo> getTQHKLoanByDate(ComOrganization salesDepartment, Date currDate);

    /**
     * 得到逾期的loan记录，根据营业部，其中不包含在特殊还款表中有申请记录的loan
     * 
     * @param salesDepartment
     * @return
     */
    public List<VLoanInfo> getYQHKLoanByOrg(ComOrganization salesDepartment);

    /**
     * 查找还款日正常还款的loan记录，记录中不包含申请提前还款或者提前结清的记录
     * 
     * @param salesDepartment
     * @param currDate
     * @return
     */
    public List<VLoanInfo> getZCHKLoanByOrg(ComOrganization salesDepartment, Date currDate);

    /**
     * 根据id查找
     * 
     * @param loanId
     */
    public VLoanInfo findByLoanId(Long loanId);

    /**
     * 查找指定还款日的逾期loan
     * 
     * @param promisereturnDate
     * @return
     */
    public List<VLoanInfo> getYQHKLoanByPromisereturnDate(int promisereturnDate);
    
    public List<VLoanInfo> getYQHKLoanByPromisereturnDateAndLoanBelong(int promisereturnDate, String loanBelong);

    /**
     * 根据借款人身份证号码、姓名、借款类型、签约日期查询债权信息
     * 
     * @param params
     * @return
     */
    public VLoanInfo getLoanByIdnumAndName(Map<String, Object> params);
    public VLoanInfo getVLoanBySignDateAndBorrowerAndLoanType(Map<String, Object> params);
    

    /**
     * 跟居实体查询结果集(公共)
     * 
     * @param vLoanInfo
     * @return
     */
    public List<VLoanInfo> findListByVO(VLoanInfo vLoanInfo);

    /***
     * 还款录入模块多表查询(分页查询)
     * 
     * @param paramMap
     *            参数集合
     * @return
     */
    public Pager searchRepaymentLoanWithPg(Map<String, Object> paramMap);

    /**
     * 还款试算 通过姓名，手机，身份证，部门类型，机构码查询（逾期，正常，坏账）债权
     * 
     * @param params
     * @return
     */
    public List<VLoanInfo> searchVLoanInfoList(Map<String, Object> params);
    
    /**
     * 还款试算 通过姓名，身份证，机构码查询（逾期，正常）债权
     * 
     * @param params
     * @return
     */
    public  List<VLoanInfo>  searchVLoanInfo(Map<String, Object> params);

    /**
     * 还款试算 通过姓名，手机，身份证，部门类型，机构码查询（逾期，正常，坏账）债权
     * 
     * @param params
     * @return
     */
    public Pager searchVLoanInfoWithPg(Map<String, Object> paramMap);

    /**
     * 跟据借款人、借款类型、借款状态、 还款日期查询借款数据
     * 
     * @param params
     *            borrowerId 借款人
     * @param params
     *            loanType 借款类型
     * @param params
     *            loanStates 借款状态
     * @param params
     *            startrDateBegin 还款日期（开始）
     * @param params
     *            startrDateEnd 还款日期（结束）
     * @return
     */
    public List<VLoanInfo> findByBorrowerAndTypeAndStateAndStartrDate(Long borrowerId, String loanType, String[] loanStates, Date startrDateBegin,
	    Date startrDateEnd);
    
    /**
     * 跟据借款人和借款状态查询借款数据
     * 
     * @param 
     *            borrowerId 借款人
     * @param 
     *            loanStates 借款状态
     */
    public List<VLoanInfo> findByBorrowerAndState(Long borrowerId, String[] loanStates);
    /**
     * 根据借款人姓名、合同编号查询借款数据
     * 
     * @param 
     *            borrowerId 借款人
     * @param 
     *            contractNum 合同编号
      * @param             
      *           loanStates 借款状态
     *            
     */
    public List<VLoanInfo> findByBorrowerAndContractNumAndState(Long borrowerId, String contractNum, String[] loanStates);
    /**
     * 根据借款人、借款类型、借款状态、 还款日期、合同编号查询借款数据
     * 
     * @param 
     *            borrowerId 借款人
     * 
     * @param     
     *            loanType   借款类型
     *            
     *  @param    loanStates 借款状态
     *  
     *  @param
     *            startrDateBegin  还款日期
     *  
     * @param        
     *            contractNum 合同编号
     *            
     */
    public List<VLoanInfo> findByBorrowerAndTypeAndStateAndStartrDateAndContractNum(Long borrowerId, String loanType, String[] loanStates, Date startrDateBegin,
    	    Date startrDateEnd,String contractNum);

    /**
     * 爱特债权导出
     * @param params 参数集合
     * @return
     */
    public List<Map<String, Object>> queryForLoanExport(Map<String, Object> params);
    
    /**
     * 爱特债权还款流水导出
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryForLoanRepayExp(Map<String, Object> params);
    
	/**
     * 积木盒子债权导出
     * @param params
     * @return
     */	
	public List<Map<String, Object>> getExternalDebtList(Map<String, Object> params);
    /**
     * 通过批次号查询非当天批次的债权
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumDetail(Map<String, Object> paramMap);
    
    /**
     * 通过批次号查询当天挖财2生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumWC2Detail(Map<String, Object> paramMap);
//    public Pager listquerBatchNumWC2DetailNotCurrentDay(Map<String, Object> paramMap);
    /**
     * 通过批次号查询当天非挖财2生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumNotWC2Detail(Map<String, Object> paramMap);
    
    /**
     * 通过当天批次号查询债权
     * @param paramMap
     * @return
     */
    public Pager getCurrentDayLoanBaseBybatchNum(Map<String, Object> paramMap);
    
    
    
	
    
    public List<VeloanExport> getVeloanExportList(Map<String, Object> paramMap);
    /**
     *  积木盒子债权导出
     * @param paramMap
     * @return
     */
    public List<VloanJmhzExport> getVeloanJmhzExportList(Map<String, Object> paramMap);
    /**
     * 还款计划债权导出
     * @param paramMap
     * @return
     */
    public List<LoanRepaymentDetailVo> getVeloanReymentExportList(Map<String, Object> paramMap);
    /**
     * 债权审核导出(供理财)
     * @param paramMap
     * @return
     */
    public List<VLoanDebtInfo> getVLoanDebtInfoExportList(Map<String, Object> paramMap);
    /**
     * 
     * @param paramMap
     * @return
     */
    public List<VloanDebtCheckInfo> getVLoanDebtCheckInfoExportList(Map<String, Object> paramMap);
    
    public List<TotalMoney> getTotalMoney(Map<String, Object> paramMap);
    
    public VLoanInfo getVLoanByIdnumAndName(Map<String, Object> params);
    public VLoanInfo getVLoanBlackInfo(Map<String, Object> params);

    /**
     * 判断登录用户是否为华澳信托
     * @param userCode
     * @return
     */
    public boolean checkFunds(String userCode);
    
    /**
     * 检查批次号是否重复
     * @param list
     * @return
     */
    public int checkBatchNum(List<Long> list);
    public int checkBatchNumUpdate(Map<String, Object> paramMap);
    
    /**
     * 更新批次号
     * @param paramMap
     * @return
     */
    public int updateBatchNum(Map<String, Object> paramMap);
    
    /**
     * 证大艾特
     * @param list
     * @return
     */
    public int updateBatchNumAT(List<Long> list);
    
    /**
     * 证大艾特通过批次号更新
     * @param paramMap
     * @return
     */
    public int updateBatchNumATByBatchNum(Map<String, Object> paramMap);
    
    /**
     * 通过批次号更新
     * @param paramMap
     * @return
     */
    public int updateBatchNumByBatchNumNull(Map<String, Object> paramMap);
    public int updateBatchNumByBatchNum(Map<String, Object> paramMap);
    /**
     * 查询贷款人所有的债权
     * @param params
     * @return
     */
    public List<VLoanInfo> getVLoanByMap(Map<String, Object> params);
    
    /**
     * 判断当前日是否为还款日
     * @param loanId
     * @return
     */
    public boolean isRepaymentDay(Long loanId);
    
    /**
     * 通过批次号查询当天海门小贷生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumHMDetail(Map<String, Object> paramMap);
    
    /**
     * 通过批次号查询当天龙信小贷生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumLXXDDetail(Map<String, Object> paramMap);
    
    /**
     * 通过批次号查询当天外贸信托生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumWMXTDetail(Map<String, Object> paramMap);
    /**
     * 通过批次号查询当天渤海信托2生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumBHXTDetail(Map<String, Object> paramMap);
    /**
     * 查询放款流水
     * @param paramMap
     * @return
     */
    public Pager listLoanOrder(Map<String, Object> paramMap);
    /**
     * 获取第三方线下放款导入需要验证的信息
     * @param paramMap
     * @return
     */
    public VloanPersonInfo findImportLoanInfo(Map<String, Object> paramMap);
    
    /**
     * 还款计划债权导出(供外贸信托)
     * @param paramMap
     * @return
     */
    public List<LoanRepaymentDetailVo> getWxmtLoanReymentExportList(Map<String, Object> paramMap);

    /**
     * 债权导出 供理财（外贸信托）
     * @param paramMap
     * @return
     */
	public List<VLoanDebtFileInfo> getExportDebtInfo4WMXT(Map<String, Object> paramMap);
    
    /**
     * 通过批次号查询当天渤海信托(不包含渤海信托2)生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumBHXT2Detail(Map<String, Object> paramMap);
    /**
     * 通过批次号查询当天华瑞渤海生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumBHXT3Detail(Map<String, Object> paramMap);

    /**
     * 通过批次号查询当天外贸2生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
	public Pager listquerBatchNumWM2Detail(Map<String, Object> paramMap);
    
    /**
     * 根据合同编号 查询债权归属 是否属于捞财宝
     * @param params
     * @return
     */
    public VLoanInfo getBelongVLoanInfoByNum(Map<String, Object> params);
    /**
	 * 通过批次号查询当天包商银行生成批次或者可以生成批次的记录
	 * @param paramMap
	 * @return
	 */
	public Pager listquerBatchNumBSYHDetail(Map<String, Object> paramMap);

	/**
	 * @param params
	 * @return
	 */
	public List<VloanPersonInfo> findLoanData2UploadInfo(Map<String, Object> params);

	/**
	 * 查找债权人贷款信息
	 * @param contactParams
	 * @return
	 */
	public List<Map<String, Object>> findPersonLoanInfo(Map<String, Object> contactParams);
	
	/**
	 * 查询放款成功且没有发送外贸3的债权
	 * @param map
	 * @return
	 */
	public List<VLoanInfo> findGrantSuccessNotToWM3(Map<String, Object> map);
	/**
	 * 跟据借款状态获取所有债权ID
	 * @param params
	 * @return
	 */
	public Pager getLoanVoByStateWithPg(Map<String, Object> params);
	
	public PublicStoreAccountVo getPublicStoreAccountDetail(Map<String, Object> params);

    /**
     * 分页查找账务债权信息
     * @param personVisitW
     * @return
     */
    public Pager findAcountLoanManageWithPg(VPersonVisit personVisit);

    public int updateLoanInfExtByLoanId(Map<String, Object> params );

    /**
     * 添加账务债权操作信息
     * @param id
     * @param vLoanInfo
     * @return
     */
    public int addLoanInfoExtByLoanId(VLoanInfo vLoanInfo);
    /**
     * 获取委外数据
     * @param loanId
     * @return
     */
	public Long getOutSourcing(Long loanId);
	
	/**
	 * 根据map参数查询实体类
	 * @param params
	 * @return
	 */
    public List<VLoanInfo> findListByMap(Map<String, Object> params);
    
    /**
     * 查询逾期表中的数据得到  逾期本金、逾期利息 、逾期起始日 、 逾期天数
     * @param paramMap
     * @return
     */
    public RepayStateDetail getRepayStateDetailOverdueValue(Map<String, Object> paramMap);
}
