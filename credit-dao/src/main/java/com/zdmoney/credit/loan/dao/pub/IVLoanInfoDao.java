package com.zdmoney.credit.loan.dao.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.PublicStoreAccountVo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
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
 * @author 00232949
 */
public interface IVLoanInfoDao extends IBaseDao<VLoanInfo> {

    /**
     * 与特殊还款表关联查找，得到指定类型，指定生效日期的loan信息
     * @param salesDepartment 营业部
     * @param currDate 日期
     * @param type 特殊还款类型
     * @param state 特殊还款状态
     * @param debitTypeList 划扣类型
     * @return
     */
    public List<VLoanInfo> getSpecialLoanByDate(ComOrganization salesDepartment, Date currDate,
            SpecialRepaymentTypeEnum type, SpecialRepaymentStateEnum state,List<String> debitTypeList);

    /**
     * 得到逾期的loan记录，根据营业部和划扣类型，其中不包含在特殊还款表中有申请记录的loan
     * @param salesDepartment
     * @param debitTypeList 划扣类型
     * @return
     */
    public List<VLoanInfo> getOverdueLoanByOrg(ComOrganization salesDepartment, List<String> debitTypeList);

    /**
     * 根据营业部、还款日和划扣类型得到正常还款的loan
     * @param salesDepartment
     * @param promiseReturnDate
     * @param debitTypeList 划扣类型
     * @return
     */
    public List<VLoanInfo> getLoanByOrgAndRepDate(ComOrganization salesDepartment, Date promiseReturnDate, List<String> debitTypeList);

    /**
	
	/**放款流水
	 * 债权loan
	 * @param paramMap
	 * @return
	 */
	public Pager searchLoanOrderWithPg(Map<String, Object> paramMap);
    /** 
     * 根据借款人身份证号码、姓名、借款类型、签约日期、合同编号查询债权信息
     * @param params
     * @return
     */
    public VLoanInfo getLoanByIdnumAndName(Map<String, Object> params);

    public VLoanInfo getVLoanByIdnumAndName(Map<String, Object> params);

    public VLoanInfo getVLoanBySignDateAndBorrowerAndLoanType(Map<String, Object> params);

    public VLoanInfo getVLoanBlackInfo(Map<String, Object> params);

    /**
     * 查找指定还款日的逾期loan
     * @param promisereturnDate
     * @return
     */
    public List<VLoanInfo> getYQHKLoanByPromisereturnDate(int promisereturnDate);
    
    public List<VLoanInfo> getYQHKLoanByPromisereturnDateAndLoanBelong(int promisereturnDate, String loanBelong);

    /***
     * 还款录入模块多表查询(分页查询)
     * @param paramMap 参数集合
     * @return
     */
    public Pager searchRepaymentLoanWithPg(Map<String, Object> paramMap);

    /**
     * 通过用户名，手机号，身份号查询(正常，逾期，坏账)债权loan
     * @param paramMap
     * @return
     */
    public List<VLoanInfo> searchVLoanInfoList(Map<String, Object> paramMap);

    /**
     * 还款试算(分页查询) 通过用户名，手机号，身份号查询(正常，逾期，坏账)债权loan
     * @param paramMap
     * @return
     */
    public Pager searchVLoanInfoWithPg(Map<String, Object> paramMap);

    /**
     * 特殊还款接口与还款试算查询DAO
     * @param paramMap
     * @return
     */
    public List<VLoanInfo> queryLoanForCTS(Map<String, Object> paramMap);

    /**
     * 还款试算 通过姓名，身份证，机构码查询（逾期，正常）债权
     * @param params
     * @return
     */
    public List<VLoanInfo> searchVLoanInfo(Map<String, Object> params);

    /**
     * 对应EMSService中buildWillSendPhone4WhiteList方法的查询
     * @param repayDate
     * @return
     */
    public List<String> getWillSendPhone4WhiteList(Integer returnDate);

    /**
     * 爱特债权导出查询
     * @param params
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
     * 通过批次号债权明细
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

    /**
     * 通过批次号查询当天非挖财2生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumNotWC2Detail(Map<String, Object> paramMap);

    /**
     * 查询当天批次债权
     * @param paramMap
     * @return
     */
    public Pager getCurrentDayLoanBaseBybatchNum(Map<String, Object> paramMap);

    /**
     * 爱特债权导出
     * @param paramMap
     * @return
     */
    public List<VeloanExport> getVeloanExportList(Map<String, Object> paramMap);

    /**
     * 积木盒子债权导出
     * @param paramMap
     * @return
     */
    public List<VloanJmhzExport> getVeloanJmhzExportList(Map<String, Object> paramMap);

    /**
     * 还款计划导出
     * @param paramMap
     * @return
     */
    public List<LoanRepaymentDetailVo> getVeloanReymentExportList(Map<String, Object> paramMap);

    /**
     * 债权供理财导出
     * @param paramMap
     * @return
     */
    public List<VLoanDebtInfo> getVLoanDebtInfoExportList(Map<String, Object> paramMap);

    /**
     * 查询放款总额和合同总额
     * @param paramMap
     * @return
     */
    public List<TotalMoney> getTotalMoney(Map<String, Object> paramMap);

    /**
     * 债权审核导出（供理财）
     * @param paramMap
     * @return
     */
    public List<VloanDebtCheckInfo> getVLoanDebtCheckInfoExportList(Map<String, Object> paramMap);

    /**
     * 检查是否有批次号
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
     * 通过批次号查询当天龙信小贷生成批次或者可以生成批次的记录
     * @param paramMap
     * @return
     */
    public Pager listquerBatchNumWMXTDetail(Map<String, Object> paramMap);
    
    /**
 	 * 还款备注
 	 * 通过合同编号、合同来源、身份证号、债权状态（正常、结清、逾期、预结清）查询债权
 	 * @param paramMap
 	 * @return
 	 */ 
	public Pager searchVLoanInfoListWithPg(Map<String, Object> paramMap);
	
	/**
	 * 根据身份证号查询债券信息
	 * @param paramMap
	 * @return
	 */
	public VLoanInfo getVLoanInfoByIdnum(String idNum);
		
		
	/**
	 * 获取第三方线下放款导入需要验证的信息
	 * @param paramMap
	 * @return
	 */
	public VloanPersonInfo findImportLoanInfo(Map<String, Object> paramMap);
    public Pager listquerBatchNumBHXTDetail(Map<String, Object> paramMap);

    /***
     * 查询需要上传渤海ftp的借款人资料债权编号 跟 合同编号
     * @return
     */
    public List<VLoanInfo> findUploadFtpBorrowAppNoCn(Map<String,Object> map);
    
    /**
     * 还款计划导出（供外贸信托）
     * @param paramMap
     * @return
     */
    public List<LoanRepaymentDetailVo> getWmxtLoanReymentExportList(Map<String, Object> paramMap);
    
    /**
     * 根据合同编号，查询债权归属
     * @param params
     * @return
     */
    public VLoanInfo getBelongVLoanInfoByNum(Map<String, Object> params);

    /**
     * 外债导出 （供理财） -- 外贸信托
     * @param paramMap
     * @return
     */
	public List<VLoanDebtFileInfo> getWmxtLoanDebtExportList(Map<String, Object> paramMap);
	
    public Pager listquerBatchNumBHXT2Detail(Map<String, Object> paramMap);

    public Pager listquerBatchNumBHXT3Detail(Map<String, Object> paramMap);

    /**
     * 根据借款人id得到债权
     * @param params
     * @return
     */
    public List<VLoanInfo> findListByBorrowId(Map<String, Object> params);

    /**
     * 查找外贸信托2 需上传影像资料的债权列表
     * @param params
     * @return
     */
	public List<VLoanInfo> findWmxt2VLoanInfo4Upload(Map<String, Object> params);

    public List<VLoanInfo> findWMXT2ElectronicSignatureVLoanInfo();

	public Pager listquerBatchNumWM2Detail(Map<String, Object> paramMap);
	
	public Pager listquerBatchNumBSYHDetail(Map<String, Object> paramMap);

	/**
	 * 查找 放款成功的贷款协议文件
	 * @return
	 */
	public List<VLoanInfo> findLoanAgreementByParams(Map<String, Object> params);
	
	/**
	 * 查找 待上传的债权信息
	 * @param params
	 * @return
	 */
	public List<VloanPersonInfo> findLoanData2UploadInfo(Map<String, Object> params);

	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findPersonLoanInfo(Map<String, Object> params);
	/**
	 * 根据客户id查到对应的包银修改电话
	 * @param id
	 * @return
	 */
	public List<VLoanInfo> findById(Long borrowerId);
	
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
     * 查找账务债权信息
     * @param personVisit
     * @return
     */
    public Pager findAcountLoanManageWithPg(VPersonVisit personVisit);

    /**
     * 更新债权信息拓展表 状态
     * @param params
     * @return
     */
    public int updateLoanInfExtByLoanId(Map<String, Object> params);

    /**
     *添加债权信息拓展表
     * @param params
     * @return
     */
    public int addLoanInfoExtByLoanId(Map<String, Object> params);
    /**
     * 获取是否委外数据
     * @param loanId
     * @return
     */
	public Long getOutSourcing(Long loanId);
	
	/**
	 * 得到逾期的值
	 * @param paramMap
	 * @return
	 */
	public RepayStateDetail getRepayStateDetailOverdueValue(Map<String, Object> paramMap);
}
