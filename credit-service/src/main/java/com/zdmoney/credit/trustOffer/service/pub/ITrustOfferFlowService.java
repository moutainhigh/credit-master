package com.zdmoney.credit.trustOffer.service.pub;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.debit.domain.DebitOfferFlow;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.trustOffer.domain.TrustOfferFlow;
import com.zdmoney.credit.trustOffer.vo.SubjectAmount;
import com.zdmoney.credit.trustOffer.vo.TemporaryAmountVo;
import com.zdmoney.credit.trustOffer.vo.TrustOfferFlowVo;
/**
 * Created by ym10094 on 2016/10/17.
 */
public interface ITrustOfferFlowService {
    /**
     * 获取历史时间段的 还款交易凭证记录（OfferRepayInfo）
     * @param beginTradeDtae
     * @param endTradeDate
     * @return
     */
    public List<OfferRepayInfo> getTrustRepayMentOfferRepayInfoHistroy(String beginTradeDtae,String endTradeDate);

    /***
     * 获取昨天的还款交易凭证记录（OfferRepayInfo）
     * @return
     */
    public List<OfferRepayInfo> getTrustRepayMentOfferRepayInfoYesterDay();

    /**
     *根据 交易流水获取 记账offerFlow的记账流水
     * @param tradeNo
     * @return
     */
    public List<OfferFlow> findByTradeNo(String tradeNo);
    /**
     * 是否逾期
     * @param offerFlows
     * @return
     */
    public boolean isOverdue(List<OfferFlow> offerFlows);

    /**
     * 是否有减免金额
     * @param offerFlows
     * @return
     */
    public boolean isRelief(List<OfferFlow> offerFlows);

    /**
     * 获取罚息金额
     * @param offerFlows
     * @return
     */
    public BigDecimal getfineInterest(List<OfferFlow> offerFlows);

    public BigDecimal getAlreaDyFineInterest(Long loanId,Long currentTerm);

    /**
     * 获取减免金额
     * @param offerFlows
     * @param isOverdue
     * @param  repayInfo
     * @return
     */
    public BigDecimal getReliefAmount(List<OfferFlow> offerFlows,boolean  isOverdue,OfferRepayInfo repayInfo);

    /**
     * 获取挂账金额
     * @param offerFlows
     * @return
     */
//    public BigDecimal getRecordAmount(List<OfferFlow> offerFlows);
    /**
     * 获取销账金额
     * @param offerFlows
     * @return
     */
//    public BigDecimal getCancelAmount(List<OfferFlow> offerFlows);

    /**
     * 获取违约金
     * @param offerFlows
     * @param repayInfo
     * @return
     */
//    public Map<String,Object> getPenaltyAmount(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo);

    /**
     * 根据科目号统计总金额
     * @param trustOfferFlows
     * @param accTitles
     * @return
     */
    public BigDecimal getAcctTitleAmount(List<TrustOfferFlow> trustOfferFlows,List<String> accTitles);

    /**
     * 获取逾期期数
     * @param offerFlows
     * @param isOverdue
     * @param repayInfo
     * @return
     */
    public List<Long> getbeginOverdueCurrentTerm(List<OfferFlow> offerFlows,boolean isOverdue,OfferRepayInfo repayInfo);

    /**
     * 获取当前期数
     * @param offerFlows
     * @param repayInfo
     * @return
     */
    public Long getCurrentTerm(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo);

    public Long  getCurrentTerm(Long loanId);
    /**
     * 获取期数
     * @param trustOfferFlows
     * @return
     */
    public List<Long> getTerm(List<TrustOfferFlow> trustOfferFlows);

    /**
     * 获取挂账offerFlow流水
     * @param tradeNo
     * @return
     */
    public List<OfferFlow> getRecordAccounts(String tradeNo);

    /**
     * 获取销账offerFlow 流水
     * @param tradeNo
     * @return
     */
//    public List<OfferFlow> getCancelAccounts(String tradeNo);

    /**
     *逾期拆分记账--按照科目号生成记账流水
     * @param offerFlows
     * @param  repayInfo
     * @return
     */
    public void overdueSplitAccounting(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo);

    /**
     *正常拆分记账--按照科目号生成记账流水
     * @param repayInfo
     * @param repaymentDetails
     * @return
     */
    public void normalSplitAccounting(OfferRepayInfo repayInfo,List<LoanRepaymentDetail> repaymentDetails);

    /**
     * 一次性结算拆分记账--按照科目号生成记账流水
     * @param repayInfo
     * @param repaymentDetails
     */
    public void onetimeSlitAccounting(OfferRepayInfo repayInfo,List<LoanRepaymentDetail> repaymentDetails);


    /**
     * 复制OfferFlow的数据到TrustOfferFlow
     * @param offerFlows
     * @param repayInfo
     */
    public void copyOfferFlowAccounting(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo);

    /**
     * 获取不足罚息的还款计划
     * @param repayInfo
     * @param vLoanInfo
     * @return
     */
    public boolean isNotEnoughFineInterest(OfferRepayInfo repayInfo,VLoanInfo vLoanInfo);
    /**
     * 获取正常未还款的还款计划
     * @param currDate
     * @param loanId
     * @param promiseReturnDate 约定还款日
     * @return
     */

    public List<LoanRepaymentDetail> getNormalNotRepayDetail(Date currDate,Long loanId,Long promiseReturnDate);

    /**
     * 还款分账
     * @param repayInfo
     */
    public void trustRepayService(OfferRepayInfo repayInfo);

    /**
     * 挂账拆分记账
     * @param offerFlows
     * @param repayInfo
     */
//    public void recordAccountsSplitAccounting(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo);

    /**
     * 设置逾期标示
     * @param offerFlow
     * @param repayInfo
     */
    public void setIsOverdue(OfferFlow offerFlow,OfferRepayInfo repayInfo);

    /**
     * 根据已有的流水获取逾期流水
     * @param offerFlows
     * @param repayInfo
     * @return
     */
    public List<OfferFlow> getOverDueOfferFlow(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo);

    /**
     * 统计新分账流水中每期的已还金额 跟 还欠金额
     * @param subjectAmount
     * @param loanId
     * @param currentTerm
     */
    public void statisticsCurrentAccTitleDeficitAmount(SubjectAmount subjectAmount,Long loanId,Long currentTerm);

    /**
     *获取一次性结算的还款计划
     * @param currDate
     * @param loanId
     * @return
     */
    public List<LoanRepaymentDetail> getOnetimeRepayDetail(Date currDate,Long loanId);

    /**
     * 判断当前是否结清
     * @param loanId
     * @param currentTerm
     * @return 结清 true 否则 false
     */
    public boolean isCurrentTermSettle(Long loanId,Long currentTerm);

    /**
     * 判断当前是否结清
     * @param loanRepaymentDetail
     * @return 结清 true 否则 false
     */
    public boolean isCurrentTermSettle(LoanRepaymentDetail loanRepaymentDetail);

    /**
     * 获取逾期天数
     * @param currDate
     * @param penaltyDate
     * @return
     */
    public int getOverdueDay(Date currDate,Date penaltyDate);

    /**
     * 不足罚息记账
     * @param repayInfo
     */
    public void notEnoughFineInterestAccounting(OfferRepayInfo repayInfo);

    /**
     * 其他科目记账
     * @param term
     * @param repayInfo
     */
    public void otherAccttleAccounting(Long term,OfferRepayInfo repayInfo);

    /**
     * 是否已经还了部分违约金
     * @param loanId
     * @return
     */
    public boolean isRepaymentPenalty(Long loanId);

    /**
     * 对一次性结清的违约金记账
     * @param term
     * @param repayInfo
     */
    public void  oneTimepenaltyAccttleAccounting(Long term,OfferRepayInfo repayInfo);
    /**
     * 回款确认书分页
     * @param params
     * @return
     */
    public Pager queryRefundedOfMoneyConfirmationBookPage(Map<String,Object> params);

    /**
     * 获取回款确认书导出的文件名
     * @param fundsSources
     * @return
     */
    public String getRefundedOfMoneyConfirmationBookExportFileName(String fundsSources,Date tradeDate);
    /**
     * 根据合同来源获取项目简码
     * @param fundsSources
     * @return
     */
    public String getProjectCode(String fundsSources);
    /**
     * 创建回款确认书excel导出格式的 workBook 对象
     * @param path
     * @param offerFlowVo
     * @return
     */
    public Workbook createRefundedOfMoneyConfirmationBookWorkBook(String path,TrustOfferFlowVo offerFlowVo) throws Exception;

    public ByteArrayOutputStream createRefundedOfMoneyConfirmationBookOutStream(String path,TrustOfferFlowVo offerFlowVo) throws Exception;

    /**
     * 回款确认书中 文件名
     * @param fileTypeEnum
     * @param loanBelongs
     * @param tradeDate
     * @return
     */
	public String getReturnMoneyConfireFileName(ReqManagerFileTypeEnum fileTypeEnum, String loanBelongs, Date tradeDate);

	/**
	 * 获取分账明细表
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findSubAccountDetailList(Map<String, Object> params);

	/**
	 * 获取其他暂收款报表导出的workbook对象
	 * @param offerFlowVo
	 * @param temporaryAmountList
	 * @return
	 */
	public Workbook createTemporaryAmountExcelWorkBook(TrustOfferFlowVo offerFlowVo, List<TemporaryAmountVo> temporaryAmountList, String fileName);

	/**
	 * 获取其他暂收款报表导出的文件名
	 */
	public String getTemporaryAmountExcelFileName(String fundsSources, Date tradeDate);

	/**
	 * 查询其他暂收款报表信息
	 * @param offerFlowVo
	 * @return
	 */
	public List<TemporaryAmountVo> queryTemporaryAmount(TrustOfferFlowVo offerFlowVo);

	/**
	 * 获取外贸3 分账流水
	 * @param params
	 * @return
	 */
	public List<DebitOfferFlow> findDebitOfferFlowByParams(Map<String, Object> params);
    /**
     * 分账明细
     * @param fileName
     * @param offerFlowVo
     * @return
     */
    public Workbook getSubAccountDetailWorkbook(String fileName,TrustOfferFlowVo offerFlowVo);

    /**
     * 暂其他收款报表
     * @param fileName
     * @param offerFlowVo
     * @return
     */
    public Workbook getTemporaryAmountWorkbook(String fileName,TrustOfferFlowVo offerFlowVo);

    /**
     * 上传回款确认书管理的文件到渤海服务器(供JOB执行----- RefundedOfMoneyConfirmationJob)
     * @param fundSources
     * @param tradeDate
     * @param jschSftpUtils
     * @throws Exception
     */
    public Boolean uploadRefundedOfMoneyConfirmationFileToBh(String fundSources,Date tradeDate,JschSftpUtils jschSftpUtils);

    /**
     * 获取回款确认书pdf文件输出流
     * @param offerFlowVo
     * @param fileName
     * @return
     */
    public OutputStream getRefundedOfMoneyConfirmationBookPdfFileStream(TrustOfferFlowVo offerFlowVo,String fileName);

    /**
     * 校验上传文件的数据（暂时针对渤海信托） true  数据有问题 false 数据没问题
     * @param fundSources
     * @param tradeDate
     * @return
     */
    public boolean checkOutUploadFileData(String fundSources,Date tradeDate);

    /**
     * 是否发生邮件（true 发送 false 不发送）
     * @param fundSources
     * @param tradeDate
     * @return
     */
    public boolean isSendEmail(String fundSources,Date tradeDate);

    public void sendEmail(Date tradeDate);

    /**
     * 根据项目简码获取回款确认书存放路径
     * @param fundsSources
     * @param filePostfix (.xls;.pdf)
     * @return
     */
    public String getHkqrBookTemplatePath(String fundsSources,String filePostfix);
    /**
     * 再次发送上传失败的回款确认书管理文件到渤海服务器（供JOB执行----- RefundedOfMoneyConfirmationJob）
     * @param jschSftpUtils
     */
    public void againUploadFailIsHHQRSToBh(JschSftpUtils jschSftpUtils);

    /**
     * 添加上传回款确认书log
     * @param status true 成功 false 失败
     * @param fundSources
     * @param tradeDate
     */
    public void insertUploadHKQRSLog(boolean status,String fundSources,Date tradeDate);

    /**
     * 获取回款确认书的交易开始时间
     * @param tradeDate
     * @param loanBelongs
     * @return
     */
    public Date getRefundedOfMoneyConfirmationTradeBeginDate(Date tradeDate,String loanBelongs);

    /**
     * 第一个工作日上传回款确认书到渤海
     * @param fundSources
     * @param tradeDate
     * @param jschSftpUtils
     */
    public void firstWorkDayUploadRefundedOfMoneyConfirmationFileToBh(String fundSources,Date tradeDate,JschSftpUtils jschSftpUtils);
    /**
     * 某个交易日的合同来源是否上传过
     * @param fundsSource
     * @param tradeDate
     * @return
     */
    public boolean fundsSourceIsUpload(String fundsSource,Date tradeDate);
    
    /**
     * 查询渤海信托、渤海2、华瑞渤海实分账明细数据
     * @param params
     * @return
     */
    public List<Map<String, Object>> findSplitDetailList(Map<String, Object> params);

    /**
     * 调整分账明细表批次
     * @param list
     */
    public void adjustDebitOfferFlowBatNo(List<DebitOfferFlow> list);
    /**
     * 根据科目号、交易类型统计总金额
     * @param trustOfferFlows
     * @param accTitles
     * @return
     */
    public BigDecimal getAmountByAccTitleTradeType(List<TrustOfferFlow> trustOfferFlows,List<String> accTitles,List<String> tradeTypes);
}
