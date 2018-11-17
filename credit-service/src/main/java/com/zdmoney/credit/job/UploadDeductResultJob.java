package com.zdmoney.credit.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.AbsUploadStatusEnum;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.grant.vo.LoanBaseGrantVo;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.repay.dao.pub.IAbsDeductResultUploadDao;
import com.zdmoney.credit.repay.domain.AbsDeductResultUpload;
import com.zdmoney.credit.repay.service.pub.IAbsDeductResultUploadService;
import com.zdmoney.credit.repay.service.pub.IAbsOneTimeSettlementAdviceService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * 证大扣款结果上传至数信
 * @author 10098  2016年11月29日 下午5:47:54
 */
@Service
public class UploadDeductResultJob {
	private static Logger logger = Logger.getLogger(UploadDeductResultJob.class);
	@Autowired
	private IAbsDeductResultUploadService absDeductResultUploadService;
	@Autowired
	private IAbsDeductResultUploadDao absDeductResultUploadDao;
    @Autowired
    private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IAbsOneTimeSettlementAdviceService absOneTimeSettlementAdviceService;
	@Autowired
	private IFinanceGrantService financeGrantService;
	@Autowired
	private ILoanLogService loanLogService;
	public void execute(){
		logger.info("开始执行 证大扣款结果上传至数信 JOB...");
		loanLogService.createLog("execute", "info","证大扣款结果上传至数信 JOB........", "SYSTEM");
		String isFtpUploadLoanData2Abs = sysParamDefineService.getSysParamValue("sysJob", "isUploadDeductResult");
		if(!Const.isClosing.equals(isFtpUploadLoanData2Abs)){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("payDay", Dates.getDateTime(Dates.getCurrDate(), "yyyy-MM-dd"));
			params.put("fundsSource", FundsSourcesTypeEnum.外贸2.name());
			//获取昨日扣款结果
			List<AbsDeductResultUpload> list = absDeductResultUploadService.getDeductResutList2Upload(params);
			//扣款结果保存至数据库
			absDeductResultUploadService.saveDeductResultList(list);
			//查询待上传 或者 上传失败的扣款信息'
			Map<String, Object> map = new HashMap<String, Object>();
			String []statusList = new String[]{AbsUploadStatusEnum.待上传.name(),AbsUploadStatusEnum.失败.name()};
			map.put("statusList", statusList);
			List<AbsDeductResultUpload> uploadList = absDeductResultUploadService.getDeductResutListByStatus(map);
			absDeductResultUploadService.uploadDeductList2Abs(uploadList);
			logger.info("证大扣款结果上传至数信 JOB结束");
			loanLogService.createLog("execute", "info","证大扣款结果上传至数信 JOB结束", "SYSTEM");
		}else{
			logger.warn("定时开关isUploadDeductResult关闭，此次不执行");
			loanLogService.createLog("execute", "info","定时开关isUploadDeductResult关闭，此次不执行", "SYSTEM");
		}
	}

	/**
	 * 一次结清通知数信
	 */
	public void executeOneTimeSettlementAdvice(){
		logger.info("开始执行 一次性提前结清通知数信JOB...");
		loanLogService.createLog("executeOneTimeSettlementAdvice", "info","开始执行 一次性提前结清通知数信JOB...", "SYSTEM");
		String isFtpUploadLoanData2Abs = sysParamDefineService.getSysParamValue("sysJob", "isAdviceAbsOneTimeSettlement");
		if(!Const.isClosing.equals(isFtpUploadLoanData2Abs)){
			absOneTimeSettlementAdviceService.adviceAbsOneTimeSettlementFinish();
			logger.info("一次性提前结清通知数信JOB完成......");
			loanLogService.createLog("executeOneTimeSettlementAdvice", "info","一次性提前结清通知数信JOB完成...", "SYSTEM");
		} else {
			logger.warn("定时开关isAdviceAbsOneTimeSettlement关闭，此次不执行");
			loanLogService.createLog("executeOneTimeSettlementAdvice", "info","定时开关isAdviceAbsOneTimeSettlement关闭，此次不执行.", "SYSTEM");
		}
	}

	/**
	 * 申请财务放款--调用数信的 财务放款申请接口
	 */
	public void executeFinanceGrantApply(){
		logger.info("开始执行 财务放款申请JOB---");
		loanLogService.createLog("executeFinanceGrantApply", "info","开始执行 财务放款申请JOB---", "SYSTEM");
		String isApplyFinanceGrant = sysParamDefineService.getSysParamValue("sysJob", "isApplyFinanceGrant");
		if (Const.isClosing.equals(isApplyFinanceGrant)) {
			logger.warn("定时开关isApplyFinanceGrant关闭，此次不执行");
			loanLogService.createLog("executeFinanceGrantApply", "info","定时开关isApplyFinanceGrant关闭，此次不执行", "SYSTEM");
			return;
		}
		List<LoanBaseGrantVo> loanBaseGrantVoList = financeGrantService.searchFinaceGrantApplyDetail("sx");
		if (CollectionUtils.isEmpty(loanBaseGrantVoList)) {
			logger.info("没有需要申请财务放款的的债权!结束executeFinanceGrantApply JOB");
			loanLogService.createLog("executeFinanceGrantApply", "info","没有需要申请财务放款的的债权!结束executeFinanceGrantApply JOB", "SYSTEM");
			return;
		}
		financeGrantService.batchRequestFinanceGrantApply(loanBaseGrantVoList);
		logger.info("执行executeFinanceGrantApply JOB结束");
		loanLogService.createLog("executeFinanceGrantApply", "info","执行executeFinanceGrantApply JOB结束", "SYSTEM");
	}
}
