package com.zdmoney.credit.grant.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyAccountWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyBorrowerWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyLoanWm3Entity;
import com.zdmoney.credit.framework.vo.wm3.entity.ApplyRelationWm3Entity;
import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.grant.vo.GrantAccountVo;
import com.zdmoney.credit.grant.vo.GrantApplyVo;
import com.zdmoney.credit.grant.vo.GrantBorRelaPerVo;
import com.zdmoney.credit.grant.vo.GrantBorrowPersonVo;
import com.zdmoney.credit.grant.vo.GrantRepaymentDetailVo;
import com.zdmoney.credit.grant.vo.LoanBaseGrantVo;

/**
 * Created by ym10094 on 2016/11/9.
 */
public interface ILoanBaseGrantDao extends IBaseDao<LoanBaseGrant> {
    
    /**
     * 根据id获取放款申请记录
     * @param grantApplyId
     * @return
     */
    public LoanBaseGrant findLoanBaseGrantById(long grantApplyId);

    /**
     * 分页查询
     * @param paramMap
     * @return
     */
    public Pager queryFinanceGrantPage(Map<String, Object> paramMap);

    /**
     * 导出
     * @param paramMap
     * @return
     */
    public List<LoanBaseGrantVo> exportFinanceGrantInfos(Map<String, Object> paramMap);

    /**
     * 根据 债权ID  获取发送放款申请接口的请求参数
     * @param loanId
     * @return
     */
    public GrantApplyVo getFinanceGrantApplyVo(Long loanId);

    /**
     * 根据 债权ID 获取 发送放款申请接口的账号信息
     * @param loanId
     * @return
     */
    public List<GrantAccountVo> getFinanceGrantAccountVo(Long loanId);

    /**
     * 根据 债权Id 获取发送放款申请接口的借款人信息
     */
    public List<GrantBorrowPersonVo> getFinanceGrantBorrowPersonVo(Long loanId);

    /**
     * 根据 债权id 获取发送放款申请接口的借款人联系信息
     * @param loanId
     * @return
     */
    public List<GrantBorRelaPerVo> getFinanceGrantBorRelaPerVo(Long loanId);

    /**
     * 根据 债权ID 获取发送放款申请接口的还款计划信息
     * @param loanId
     * @return
     */
    public List<GrantRepaymentDetailVo> getFinanceGrantRepaymentDetailVo(Long loanId);
    /**
     * 根据appNo 获取放款申请记录
     * @param appNo
     * @param grantState 可以为null
     * @return
     */
    public LoanBaseGrant findLoanBaseGrantByappNo(String appNo, String grantState);
    /**
     * 根据appNo 获取放款申请记录（appNo = prePactNo（数信给的预审批号））
     * @param contractNum
     * @param grantState 可以为null
     * @return
     */
    public LoanBaseGrant findLoanBaseGrantcontractNum(String contractNum, String grantState);

    /**
     * 根据债权ID 获取放款申请记录
     * @param loanId
     * @param grantState 可以为null
     * @return
     */
    public LoanBaseGrant findLoanBaseGrantByLoanId(Long loanId, String grantState);

    public LoanBaseGrant queryLoanGrantBaseRelateVloanInfo(Map<String,Object> params);
    
    /**
     * 分页查询
     * @param loanBaseGrantVo
     * @return
     */
    public Pager queryFinanceGrantPage(LoanBaseGrantVo loanBaseGrantVo);
    
    /**
     * 分页查询放款申请记录详细信息
     * @param paramMap
     * @return
     */
    public Pager queryLoanBaseGrantDetailPage(Map<String, Object> paramMap);

    /**
     * 查询记录总数
     * @param paramMap
     * @return
     */
    public  int queryFinanceGrantInfosCount(Map<String,Object> paramMap);

    /**
     * loanBaseGrant表中需上传的影像资料信息
     * @param params
     * @return
     */
	public List<LoanBaseGrant> findAbsData2Upload(Map<String, Object> params);
	
	 /**
     * 查询可放款的债权信息  
     */
    public List<LoanBaseGrantVo> queryGrantApplyDetail(Map<String, Object> paramMap);

	/**
	 * 外贸3 申请放款信息 
	 * @param loanId
	 * @return
	 */
	public ApplyLoanWm3Entity findApplyLoanInfo4WM3(Long loanId);

	/**
	 * 外贸3 申请放款 借款人信息
	 * @param borrowerId
	 * @return
	 */
	public List<ApplyBorrowerWm3Entity> findApplyBorrower4WM3(Long borrowerId);

	/**
	 * 外贸3 申请放款 借款关联人信息
	 * @param borrowerId
	 * @return
	 */
	public List<ApplyRelationWm3Entity> findApplyRelation4Wm3(Long borrowerId);

	/**
	 * 外贸3 申请放款 账卡信息
	 * @param loanId
	 * @return
	 */
	public List<ApplyAccountWm3Entity> findApplyAccount4Wm3(Long loanId);
	/**
	 * 查询华瑞渤海，渤海2申请中的数据
	 * @return
	 */
	public List<LoanBaseGrant> findHrbhAndBh2Infos();
}
