package com.zdmoney.credit.loan.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.bsyh.vo.BsyhSpecialRepayVo;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.vo.abs.input.Abs100005Vo;
import com.zdmoney.credit.framework.vo.xdcore.entity.ResponseFeeReduceEntity;
import com.zdmoney.credit.framework.vo.xdcore.entity.XdFeeReduceEntity;
import com.zdmoney.credit.framework.vo.xdcore.input.Xdcore100003Vo;
import com.zdmoney.credit.loan.domain.LoanBaseOneTimeSettlement;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.repay.domain.AbsOneTimeSettlementAdvice;

/**
 *
 * @author 00232949
 *
 */
public interface ILoanSpecialRepaymentService {

    /**
     * 按状态得到这个债权的特殊还款申请
     *
     * @param id
     * @param type
     * @param State
     * @return
     */
    LoanSpecialRepayment findbyLoanAndType(Long id, String type, String State);

    /**
     * 通过债权ID、日期、类型、状态查询特殊还款
     *
     * @param map
     * @return
     */
    public LoanSpecialRepayment findByLoanIdAndDateAndTypeAndState(Map<String, Object> map);

    /**
     * 更新 申请了提前扣款 而未还款客户的申请状态
     *
     * @param promiseReturnDate1
     */
    public int updateSpecialRepaymentStateAtEndOfDay(int promiseReturnDate1);

    /**
     * 通过债权ID、申请日期、类型数组、状态查询特殊还款
     *
     * @param loanId
     *            债权ID
     * @param requestDate
     *            申请日期
     * @param types
     *            类型数组
     * @param state
     *            状态
     * @return
     */
    public LoanSpecialRepayment findByLoanIdAndRequestDateAndTypesAndState(Long loanId, Date requestDate, String[] types, String state);

    /**
     * 更新记录
     *
     * @param loanSpecialRepayment
     */
    int update(LoanSpecialRepayment loanSpecialRepayment);

    /**
     * 提前扣款、提前一次性结清申请状态变更
     *
     * @param loanId
     *            借款编号
     * @param allCleanApplyState
     *            提前一次性结清状态
     * @param curCleanApplyState
     *            提前扣款状态
     * @param allCleanClosingDate
     *            提前一次性结清(自动取消时间)
     * @param curCleanClosingDate
     *            提前扣款(自动取消时间)
     * @param requestDate
     *            提前扣款(自动生效时间)
     */
    public void updateSpecialOneTimeState(Long loanId, String allCleanApplyState, String curCleanApplyState, Date allCleanClosingDate
            , Date curCleanClosingDate,Date requestDate);

    /**
     * 更新特殊还款表的状态到结束
     *
     * @param loanId
     * @param specialRepaymentType
     *            原类型
     * @param state
     *            原状态
     */
    public void updateSpecialRepaymentToEnd(Long id, String specialRepaymentType, String state);

    /**
     * 根据id查找
     * @param specialRepayId
     * @return
     */
    public LoanSpecialRepayment findById(Long specialRepayId);

    /**
     * 查询结算list（门店）
     */
    public Pager findSpecialRepaymentList(Map<String, Object> map);

    /**
     * 查询结算list（总部）
     */
    public Pager ZBfindSpecialRepaymentList(Map<String, Object> map);
    /**
     * 根据合同id查询特殊还款表
     *
     */

    public int findSpecialRepaymentByLoanId(Long loanId);

    /**
     * 根据申请人id和状态查询合同条数
     */
    public int findLoanByUserIdAndLoanState(String userId);

    public LoanSpecialRepayment findLoanSpecialRepaymentByVO(LoanSpecialRepayment loanSpecialRepayment);

    public void insert(LoanSpecialRepayment loanSpecialRepayment);

    public int findLoanSpecialRepaymentByStateAndLoanId(Long loanId);
    //费用减免取消
    public void updateFYJM(LoanSpecialRepayment loanSpecialRepayment);

    public Pager findListByMap(Map<String, Object> map);

    /**
     * 罚息减免申请状态变更
     *
     * @param loanId
     *            借款编号
     * @param reliefPenaltyState
     *            罚息减免申请状态
     * @param money
     *            减免金额
     * @param proposerId
     *            申请人
     * @param memo
     *            备注
     * @throws Exception
     */
    public LoanSpecialRepayment updateReliefPenaltyState(Long loanId,boolean reliefPenaltyState, String money
            ,Long proposerId,String memo) throws Exception;

    LoanSpecialRepayment findSpecialRepaymentCount(LoanSpecialRepayment loanSpecialRepayment);

    public boolean findSpecialRepaymentByUserAndDate(String id);


    /**
     * 调用网关的一次新提起清贷接口
     * @param abs100005Vo
     * @return
     */
    public boolean callOneTimeSettlementApplyInterface(Abs100005Vo abs100005Vo);

    /**
     * 包装Abs100005Vo
     * @param vLoanInfo
     * @param absOneTimeSettlementAdvice
     * @return
     */
    public Abs100005Vo abs100005VoPackage(VLoanInfo vLoanInfo,AbsOneTimeSettlementAdvice absOneTimeSettlementAdvice);
    
    /**
     * 获取已经申请提前还款（去除 申请状态成功划扣状态是空的，申请状态成功划扣状态成功。其余都发送）
     * @param date 
     * @return
     */
	public List<BsyhSpecialRepayVo> findBsyhSpecialRepay(Date date);
	/**
	 * 获取已经申请一次性结清，来源包银，状态正常的贷款
	 * @return
	 */
	List<BsyhSpecialRepayVo> findBsyhSpecialRepayAll();

}
