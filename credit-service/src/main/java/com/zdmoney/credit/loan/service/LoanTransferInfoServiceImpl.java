package com.zdmoney.credit.loan.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.dao.pub.ILoanInfoExtDao;
import com.zdmoney.credit.loan.dao.pub.ILoanTransferInfoDao;
import com.zdmoney.credit.loan.domain.LoanEvaluateInfo;
import com.zdmoney.credit.loan.domain.LoanInfoExt;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanEvaluateInfoService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanTransferInfoServiceImpl implements ILoanTransferInfoService {
	 protected static Log logger = LogFactory.getLog(LoanTransferInfoServiceImpl.class);
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanTransferInfoDao loanTransferInfoDao;
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired
	ILoanInfoExtDao loanInfoExtDao;
	@Autowired
	ILoanEvaluateInfoService loanEvaluateInfoService;
	
	@Override
	public LoanTransferInfo bulidLoanTransferInfo(Map<String, String> map) {
		String manageDepartment = map.get("manageDepartment");//管理营业部
		String loanType = map.get("loanType");//贷款种类
		String customerName = map.get("customerName");//客户姓名
		String idNum = map.get("idNum");//身份证号码
		String signDate = map.get("signDate");//签约日期
		String promiseReturnDate = map.get("promiseReturnDate");//还款日
		String pactMoney = map.get("pactMoney");//合同金额
		String overdueStartDate = map.get("overdueStartDate");//逾期起始日
		String overdueTerm = map.get("overdueTerm");//逾期期数
		String overdueDay = map.get("overdueDay");//逾期天数
		String surplusCapital = map.get("surplusCapital");//剩余本金
		String overdueCapital = map.get("overdueCapital"); // 逾期本金
		String overdueAint = map.get("overdueAint"); //逾期利息
		String fineStartDate = map.get("fineStartDate"); //罚息起算日
		String fineAmount = map.get("fineAmount"); //罚息金额
		String returnTotalAmount = map.get("returnTotalAmount"); //应还总金额
		String lastReturnDate = map.get("lastReturnDate"); //最后一期应还款日期
		String fundsSources = map.get("fundsSources"); // 合同来源
		String loanBelong = map.get("loanBelong"); //债权去向
		String contractNum = map.get("contractNum").trim(); //合同编号
		String transferBatch = map.get("transferBatch").trim(); //转让批次
		
		Assert.notNullAndEmpty(signDate, "签约日期");
		Assert.notNullAndEmpty(overdueStartDate, "逾期起始日");
		Assert.notNullAndEmpty(overdueTerm, "逾期期数");
		Assert.notNullAndEmpty(overdueDay, "逾期天数");
		Assert.notNullAndEmpty(fineStartDate, "罚息起算日");
        Assert.notNullAndEmpty(lastReturnDate, "最后一期应还款日期");
		Assert.notNullAndEmpty(contractNum, "合同编号");
        Assert.notNullAndEmpty(transferBatch, "转让批次");
        
        VLoanInfo vLoanInfoVo = new VLoanInfo();
		vLoanInfoVo.setContractNum(contractNum);
		List<VLoanInfo> loanList = vLoanInfoService.findListByVO(vLoanInfoVo);
		Assert.notCollectionsEmpty(loanList, ResponseEnum.FULL_MSG, "合同编号不存在，无法转让。");
		
		Long loanId = loanList.get(0).getId();
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("contractNum",contractNum);
        
        //判断债权是否有评估标记，如果没有则不能导入转让.
        LoanEvaluateInfo loanEvaluateInfo = loanEvaluateInfoService.findLoanEvaluateInfo(params);
        if(loanEvaluateInfo == null || (loanEvaluateInfo != null && !"是".equals(loanEvaluateInfo.getIsEval()))){
        	throw new PlatformException(ResponseEnum.FULL_MSG, "债权未评估标记，无法转让。");
        }
        
        LoanTransferInfo info = this.findLoanTransferInfoByLoanId(loanId);
        if(info == null){
        	info= new LoanTransferInfo();
        }
		info.setLoanId(loanId);
		info.setManageDepartment(manageDepartment);
		info.setLoanType(loanType);
		info.setCustomerName(customerName);
		info.setIdNum(idNum);
		info.setSignDate(Dates.getDateByString(signDate, Dates.DEFAULT_DATE_FORMAT));
		info.setPromiseReturnDate(promiseReturnDate);
		info.setPactMoney(new BigDecimal(pactMoney));
		info.setOverdueStartDate(Dates.getDateByString(overdueStartDate, Dates.DEFAULT_DATE_FORMAT));
		info.setOverdueTerm(Long.valueOf(overdueTerm));
		info.setOverdueDay(Long.valueOf(overdueDay));
		info.setSurplusCapital(new BigDecimal(surplusCapital));
		info.setOverdueCapital(new BigDecimal(overdueCapital));
		info.setOverdueAint(new BigDecimal(overdueAint));
		info.setFineStartDate(Dates.getDateByString(fineStartDate, Dates.DEFAULT_DATE_FORMAT));
		info.setFineAmount(new BigDecimal(fineAmount));
		info.setReturnTotalAmount(new BigDecimal(returnTotalAmount));
		info.setLastReturnDate(Dates.getDateByString(lastReturnDate, Dates.DEFAULT_DATE_FORMAT));
		info.setFundsSources(fundsSources);
		info.setLoanBelong(loanBelong);
		info.setContractNum(contractNum);
		info.setTransferBatch(transferBatch.equals("未转让")?"":transferBatch);
		
		if(info.getId() != null){
			info.setId(info.getId());
			loanTransferInfoDao.update(info);
		}else{
			info.setId(sequencesService.getSequences(SequencesEnum.LOAN_TRANSFER_INFO));
			loanTransferInfoDao.insert(info);
		}
		
		/**
		 * 保存债权信息扩展表
		 */
		String assignState = "";
		if("未转让".equals(transferBatch)){
			assignState = "0";
		}else{
			assignState = "1";
		}
		LoanInfoExt loanInfoExt = null;
		params = new HashMap<String, Object>();
        params.put("loanId",loanId);
        List<LoanInfoExt> loanInfoExtList = loanInfoExtDao.findListByMap(params);
        if(CollectionUtils.isNotEmpty(loanInfoExtList)){
        	loanInfoExt = loanInfoExtList.get(0);
        	loanInfoExt.setId(loanInfoExt.getId());
        	loanInfoExt.setAssignState(assignState);
        	loanInfoExtDao.update(loanInfoExt);
        }else{
        	loanInfoExt = new LoanInfoExt();
        	loanInfoExt.setId(sequencesService.getSequences(SequencesEnum.LOAN_INFO_EXT));
        	loanInfoExt.setLoanId(loanId);
        	loanInfoExt.setAssignState(assignState);
        	loanInfoExtDao.insert(loanInfoExt);
        }
		
		return info;
	}
	
	@Override
	public List<Map<String, Object>> findLoanTransferBatchList() {
		return loanTransferInfoDao.findLoanTransferBatchList();
	}
	
	@Override
	public List<Map<String, Object>> findLoanTransferInfoList(Map<String, Object> params) {
		return loanTransferInfoDao.findLoanTransferInfoList(params);
	}

	@Override
	public boolean isLoanTransfer(Long personId,Long loanId){
		HashSet<Object> loanIds = new HashSet<Object>();
		if(Strings.isNotEmpty(personId) && Strings.isEmpty(loanId)){
			List<VLoanInfo> loanInfoList = vLoanInfoService.findByBorrowerAndState(personId, null);
			if(CollectionUtils.isEmpty(loanInfoList)){
				logger.info("该用户没有相应的债权！");
				return true;
			}
			for(VLoanInfo loan : loanInfoList){
				loanIds.add(loan.getId());
			}
		}else if(Strings.isEmpty(personId) && Strings.isNotEmpty(loanId)){
			loanIds.add(loanId);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("noTransfer", "未转让");
		params.put("loanIds", loanIds);
		List<LoanTransferInfo> loanTransferInfoList = loanTransferInfoDao.findListByMap(params);
		//如果不为空，则进行过债权转让   不能进行其他操作
		if(CollectionUtils.isNotEmpty(loanTransferInfoList)){
			return false;
		}
		return true;
	}
	
	@Override
	public LoanTransferInfo findLoanTransferInfoByLoanId(Long loanId) {
		return loanTransferInfoDao.findLoanTransferInfoByLoanId(loanId);
	}

}
