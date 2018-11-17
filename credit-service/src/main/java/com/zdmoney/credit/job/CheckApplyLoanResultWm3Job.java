package com.zdmoney.credit.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.grant.FinanceGrantEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.fee.offer.LoanFeeUtil;
import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;


/**
 * 外贸3检查放款申请结果
 * @author 10098  2017年3月17日 下午6:03:02
 */
@Service
public class CheckApplyLoanResultWm3Job {

	private static Logger logger = Logger.getLogger(CheckApplyLoanResultWm3Job.class);
    @Autowired
    private ISysParamDefineService sysParamDefineService;	
    @Autowired
    private IFinanceGrantService financeGrantService;
    @Autowired
    private LoanFeeUtil loanFeeUtil;
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
	public void execute(){
		logger.info("开始处理CheckApplyLoanResultWm3Job...");
		String isCheckApplyLoanResultWm3 = sysParamDefineService.getSysParamValue("sysJob", "isCheckApplyLoanResultWm3");
		if(!Const.isClosing.equals(isCheckApplyLoanResultWm3)){
			logger.info("开始执行外贸3检查放款申请结果JOB");
			Map<String, Object> params = new HashMap<String, Object>();
			String []respStatusList = new String[]{"01","02"};//01-待响应状态 02-响应失败
			params.put("respStatusList", respStatusList);
			List<LoanBaseGrant> list = financeGrantService.findListByMap(params);
			if(CollectionUtils.isEmpty(list)){
				logger.info("外贸3检查放款申请结果——没有待响应的结果");
				return;
			}
			financeGrantService.executeCheckApplyLoanResultWm3(list);
			for(LoanBaseGrant loanBaseGrant:list){
				if(FinanceGrantEnum.放款成功.getCode().equals(loanBaseGrant.getGrantState())){
					 VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanBaseGrant.getLoanId());
					 financeGrantService.createDebitAccountInfo(vLoanInfo);
					 try{
						 loanFeeUtil.createFee(loanBaseGrant.getLoanId(),vLoanInfo.getFundsSources());
					 }catch(Exception e){
						 logger.error("外贸3 放款成功服务费收取异常，合同号为："+loanBaseGrant.getContractNum(), e);
					 }
				}
			}
		}else{
			logger.warn("定时开关isCheckApplyLoanResultWm3关闭，此次不执行");
		}
	}
}
