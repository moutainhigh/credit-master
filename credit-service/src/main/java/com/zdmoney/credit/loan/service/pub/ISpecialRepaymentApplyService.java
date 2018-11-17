package com.zdmoney.credit.loan.service.pub;

        import com.zdmoney.credit.common.constant.flow.DepartmentTypeEnum;
        import com.zdmoney.credit.common.constant.flow.OverDueGradeEnum;
        import com.zdmoney.credit.common.constant.flow.ReliefAmountGradeEnum;
        import com.zdmoney.credit.common.util.Pager;
        import com.zdmoney.credit.loan.domain.SpecialRepaymentApply;
        import com.zdmoney.credit.loan.domain.SpecialTradeRelation;
        import com.zdmoney.credit.loan.vo.*;
        import com.zdmoney.credit.offer.domain.OfferRepayInfo;
        import com.zdmoney.credit.offer.vo.RepaymentInputVo;
        import org.apache.poi.ss.usermodel.Workbook;

        import java.math.BigDecimal;
        import java.util.Date;
        import java.util.List;
        import java.util.Map;

/**
 * Created by ym10094 on 2017/5/16.
 */
public interface ISpecialRepaymentApplyService {
    /**
     * 查看某个债权下是否已经存在状态为（申请，通过，生效）的减免申请
     * @param loanId
     * @return true 有 false 没有
     */
    public boolean isExistEffectiveSatusSpecialRepaymentApply(long loanId);

    /**
     * 获取某个债权下状态为（申请，通过，生效）的减免申请
     * @param loanId
     * @return
     */
    public List<SpecialRepaymentApply> getEffectiveSatusSpecialRepaymentApply(long loanId);

    /**
     * 获取申请减免状态为通过的 申请数据
     * @param loanId
     * @return
     */
    public SpecialRepaymentApply getApplyReliefStatusPass(long loanId);

    /**
     * 校验特殊还款申请（减免（逾期减免、一次性结清减免）） 申请校验
     * @param specialRepaymentApplyVo
     */
    public void checkOutSpecialRepaymentApply(SpecialRepaymentApplyVo specialRepaymentApplyVo);
    /**
     * 计算最大减免金额
     * @param reliefAmountCalculateVo
     * @param specialRepaymentApplyType
     * @param isSpecial
     * @return
     */
    public BigDecimal calculateMaxReliefMoney(ReliefAmountCalculateVo reliefAmountCalculateVo,String specialRepaymentApplyType,String isSpecial);

    /**
     * 计算当前的累计已还金额
     * @param currentTerm
     * @param loanId
     * @return
     */
    public BigDecimal calculateAlreadyRepayTotalMoney(String currentTerm,Long loanId);

    /**
     * 计算历史累计已还金额
     * @param loanId
     * @return
     */
    public BigDecimal calculateHistoryAlreadyRepayTotalMoney(Long loanId);
    /**
     * 获取历史逾期次数
     * @param loanId
     * @return
     */
    public int historyOverDueTime(Long loanId);

    /**
     * 获取历史减免金额
     * @param loanId
     * @return
     */
    public BigDecimal historyReliefAmount(Long loanId);

    /**
     * 获取历史减免次数
     * @param loanId
     * @return
     */
    public int  historyReliefTime(Long loanId);
    /**
     * 获取减免的计算金额
     * @param relieCalculateAmountParamVo
     * @return
     */
    public ReliefAmountCalculateVo getRelieCalculateAmount(RelieCalculateAmountParamVo relieCalculateAmountParamVo);

    /**
     * 申请减免分页
     * @param params
     * @return
     */
    public Pager queryAppltReliefPage(Map<String,Object> params);

    /**
     * 用户名 合同编号 身份证 三要素校验
     * @param contractNum
     * @param idNum
     * @param name
     */
    public VloanPersonInfo checkLoanPersonInfo(String contractNum,String idNum,String name);

    /**
     * 新增申请减免记录
     * @param specialRepaymentApply
     * @return
     */
    public SpecialRepaymentApply insertSpecialRepaymentApply(SpecialRepaymentApply specialRepaymentApply);

    /**
     * 修改申请减免记录
     * @param specialRepaymentApply
     * @return
     */
    public int updateSpecialRepaymentApply(SpecialRepaymentApply specialRepaymentApply);

    /**
     * 计算减免生效金额 与 判断申请减免的状态
     * @param reliefAmountCalculateVo
     * @param specialRepaymentApply
     * @return
     */
    public SpecialRepaymentApply calculateEffectTotalMoney(ReliefAmountCalculateVo reliefAmountCalculateVo,SpecialRepaymentApply specialRepaymentApply);

    /**
     *处理减免生效金额录入和还款录入
     * @param specialRepaymentApply
     * @param repaymentInputVo
     */
    public OfferRepayInfo dealReliefMoneyInputAndRepaymentInput(SpecialRepaymentApply specialRepaymentApply,RepaymentInputVo repaymentInputVo,ReliefAmountCalculateVo reliefAmountCalculateVo);

    public SpecialTradeRelation insetSpecialTradeRelation(String tradeNo,Long loanId);

    public void updateSpecialRepaymentApplyFinish(Long effectiveId);

    public void updateSpecialRepaymentApplyPass(Long effectiveId);

    public void updateSpecialRepaymentApply(Long applyId,String applyStatus,String memo) ;



        /**
         * 系统减免分账完成的处理
         * @param repay
         */
    public void reliefRepayDealFinish(OfferRepayInfo repay);

    public void offOneTimeSettle(Long loanId);

    /**
     * 关闭申请减免 把状态为（申请 通过 生效）的置为 （取消 拒绝 失效）
     * @param specialRepaymentApply
     */
    public void offReliefApply(SpecialRepaymentApply specialRepaymentApply);

    /**
     * 添加申请减免记录
     * @param specialRepaymentApply
     * @return
     */
    public SpecialRepaymentApply addSpecialRepaymentApplyByVo(SpecialRepaymentApply specialRepaymentApply);

    public SpecialRepaymentApply getSpecialRepaymentApplyById(Long id);

    /**
     * 每日晚更新减免申请状态
     */
    public void executeApplyReliefStatusUpdate();

    /**
     * 是否营业部的M1逾期
     * @param loanId
     * @return
     */
    public boolean isM1(Long loanId);

    /**
     * 查询生效后的 减免信息
     * @param params
     * @return
     */
    public Map<String,Object> queryEffectiveLoanSpecialRepayment(Map<String, Object> params);

    /**
     * 是否无效的营业部（已经停业）
     * @param salesDepartmentId
     * @return true 停业
     */
    public boolean isNoValidSalesDepartment(Long salesDepartmentId);

    public BigDecimal getReleifAmount(Long loanId);

    /**
     * 减免不够罚息
     * @param effectiveId
     */
    public void releifNotEnoughFine(Long effectiveId);

    public OverDueGradeEnum queryOverDueGrade(String reliefType,int overDueDay,boolean isRuleIn);

    /**
     * 计算规则内最大减免金额
     * @param loanId
     * @return
     */
    public BigDecimal calculateRuleInMaxReliefAmount(Long loanId);

    /**
     * 获取最大减免金额
     * @param maxReliefAmountParamVo
     * @return
     */
    public BigDecimal getMaxReliefAmount(MaxReliefAmountParamVo maxReliefAmountParamVo);

    public ReliefAmountGradeEnum getReliefAmountGrade(BigDecimal applyReliefAmount,BigDecimal maxReliefAmount,OverDueGradeEnum overDueGradeEnum);

    /**
     * 获取结清减免等级
     * @param applyReliefAmount
     * @param fine
     * @param ruleInMaxReliefAmount
     * @return
     */
    public ReliefAmountGradeEnum getOneTimeReliefAmountGrade(BigDecimal applyReliefAmount,BigDecimal fine,BigDecimal ruleInMaxReliefAmount);
    /**
     * 校验特殊还款申请（减免（逾期减免、一次性结清减免）） 特殊减免申请校验
     * @param loanId
     */
    public void checkOutSpecialReliefApply(Long loanId);

    /**
     * 申请减免分页
     * @param params
     * @return
     */
    public Pager queryReliefInfoPage(Map<String,Object> params);

    /**
     * 获取查询减免 workbook
     * @param params
     * @return
     */
    public Workbook getQueryReliefInfoWorkbook(Map<String,Object> params,String fileName);

    /**
     * 金额减免申请前置 逻辑检验(催收系统 金额减免申请)
     * @param loanId
     * @param remitAmount
     * @param isSpecail
     */
    public void checkRemitAmountRequestState(Long loanId, BigDecimal remitAmount,boolean isSpecail);
}
