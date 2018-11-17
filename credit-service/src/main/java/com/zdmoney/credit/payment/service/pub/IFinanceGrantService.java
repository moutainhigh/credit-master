package com.zdmoney.credit.payment.service.pub;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.vo.xdcore.input.BYXY0016Vo;
import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.grant.vo.LoanBaseGrantVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;

/**
 * Created by ym10094 on 2016/11/10.
 */
public interface IFinanceGrantService {
    /**
     * 转换放款状态
     * @param state
     * @return
     */
    public String grantStateConverter(String state);

    /**
     * 请求唯一ID 是否存在
     * @param pactNo
     * @return
     */
    public Boolean isExitpactNo(String pactNo);
    /**
     * 判断放款申请是否成功
     * @param geantApplyId
     * @return
     */
    public boolean isSuccessGrant(Long geantApplyId);

    /**
     * 更新放款结果
     * @param loanBaseGrant
     * @return
     */
    public void updateGrantState(LoanBaseGrant loanBaseGrant);

    /**
     * 处理放款申请结果
     * @param pactNo
     * @param grantState
     * @param dealDesc
     * @return
     */
    public void disposeFinanceGrantPushResultService(String pactNo, String grantState, String dealDesc);

    /**
     * 根据合同编号查询处再申请中状态的放款申请（外贸2）
     * @param contractNum
     * @return
     */
    public LoanBaseGrant findInTheApplyLoanBaseGrantByContractNum(String contractNum);

    /**
     * 分页
     * @param paramMap
     * @return
     */
    public Pager queryFinanceGrantPage(Map<String, Object> paramMap);

    /**
     * 导出
     * @param paramMap
     * @param fileName
     * @return
     */
    public Workbook getFinanceGrantWorkBook(Map<String, Object> paramMap, String fileName);

    /**
     * 是否可以申请财务放款
     * @param loanId
     * @return true 可以 FALSE 不可以
     */
    public boolean isApplyFinanceGrant(Long loanId);
    /**
     * 请求放款申请
     * @param loanId
     */
    public void requestFinanceGrantApply(Long loanId);

    /**
     * 新增一条loanBaseGrant 记录
     * @param loanBaseGrant
     */
    public void insetLonaBaseGrant(LoanBaseGrant loanBaseGrant);
    /**
     * 财务放款详情页面
     * @param paramMap
     * @return
     */
    public Pager queryFinanceGrantDetailsPage(Map<String, Object> paramMap);
    
    /**
     * 根据指定条件查询放款申请信息
     * @param loanBaseGrant
     * @return
     */
    public List<LoanBaseGrant> findListByVo(LoanBaseGrant loanBaseGrant);
    
    /**
     * 分页查询
     * @param loanBaseGrantVo
     * @return
     */
    public Pager queryFinanceGrantPage(LoanBaseGrantVo loanBaseGrantVo);

    /**
     * 外贸2财务放款是否处在放款申请中
     * @param appNo
     * @return
     */
    public boolean isWaiMao2FinanceGrantApply(String appNo);

    /**
     * 查询财务放款申请的详细信息
     * @return
     */
    public List<LoanBaseGrantVo> searchFinaceGrantApplyDetail(String type);

    /**
     *财务放款申请回退 更新（外贸2）
     * @param contractNum
     */
    public void updateWaiMao2FinanceGrantApplyReturn(String contractNum);

    /**
     * 批量申请放款
     * @param loanIds
     * @return
     */
    public String batchRequestFinanceGrantApply(String[] loanIds);
    /**
     * 批量申请放款
     * @param loanBaseGrantVos
     * @return
     */
    public void batchRequestFinanceGrantApply(List<LoanBaseGrantVo> loanBaseGrantVos);
    
    /**
     * 处理放款结果，更新中间映射表，创建还款计划，创建费率
     * @param byxy0016Vo
     * @return
     * @throws Exception
     */
    public  Map<String, Object> disposeFinanceGrantResult(BYXY0016Vo byxy0016Vo) throws PlatformException,Exception;
    
    /**
     * 创建 报盘凭证 和 凭证明细表
     * @param loanId 债权id
     * @param amount 交易金额
     * @param type 交易类型 trade_code
     * @return
     */
    public boolean createOfferRepayInfo(Long loanId,BigDecimal amount,String type);

	/**
	 * @param params
	 * @return
	 */
	public List<LoanBaseGrant> findListByMap(Map<String, Object> params);

	/**
	 * 调用外贸3 放款申请结果 并执行结果
	 * @param list
	 * @return 
	 */
	public List<LoanBaseGrant> executeCheckApplyLoanResultWm3(List<LoanBaseGrant> list);

	/**
	 * 外贸3调用溢缴款接口 并生成开户信息
	 * @param vLoanInfo
	 */
	public void createDebitAccountInfo(VLoanInfo vLoanInfo);
	
	/**
     * 创建 报盘凭证 和 凭证明细表
     * @param loanId 债权id
     * @param amount 交易金额
     * @param type 交易类型 trade_code
     * @return
     */
    public OfferRepayInfo createOfferRepayInfoThird(Long loanId,BigDecimal amount,String type);
	
	/**
	 * tpp放款结果查询
	 */
	public void tppGrantResultQuery();
	
	/**
	 * 外贸3还款入账处理
	 * @param loanId
	 * @param amount
	 * @param tradeCode
	 * @return
	 */
	public boolean createWm3OfferRepayInfo(Long loanId, BigDecimal amount, String tradeCode);
}
