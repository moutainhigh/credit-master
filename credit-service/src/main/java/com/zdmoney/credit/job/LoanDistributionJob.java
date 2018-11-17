package com.zdmoney.credit.job;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.system.service.pub.ISysSpecialDayService;

@Service
public class LoanDistributionJob {
	private static final Logger logger = Logger.getLogger(LoanDistributionJob.class);
	@Autowired
	private IComOrganizationService comOrganizationService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	@Autowired
	private ISysSpecialDayService sysSpecialDayService;
	
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	RestTemplate rt;
	
	public void execute() {
    	String isLoanDistribution = sysParamDefineService.getSysParamValue("sysJob", "isLoanDistribution");
		if(!Const.isClosing.equals(isLoanDistribution)){
			Date now = new Date();
			//remove hh:mi:ss
			now = Dates.parse(Dates.getDateTime(now, "yyyyMMdd"), "yyyyMMdd");
			if (now.before(Dates.parse("20150227", "yyyyMMdd"))) {
				return;
			}
			
			if (sysSpecialDayService.getWorkday(now, 4) != ToolUtils.getNextRepayDate(now) 
					&& !Dates.getDateTime(now, "yyyyMMdd").equals("20150227")) {
				return;
			}
			
			logger.info("LoanDistributionJob开始........");
			loanLogService.createLog("LoanDistributionJob", "info", "LoanDistributionJob开始........", "SYSTEM");
			
			ComOrganization comOrganization = new ComOrganization();
			comOrganization.setvLevel(ComOrganizationEnum.get(ComOrganizationEnum.INDEX_DEPARTMENT).getName());
			List<ComOrganization> salesDepList = comOrganizationService.findListByVo(comOrganization);
			String url = sysParamDefineService.getSysParamValueCache("codeHelper", "distributeCsUrl");
			for (ComOrganization org : salesDepList) {
				logger.info("营业部："+ org.getName() +",分配开始");
				loanLogService.createLog("LoanDistributionJob", "info", "营业部："+ org.getName() +",分配开始", "SYSTEM");
				
				try {
					rt.postForObject(url + "?salesDepartmentId=" + org.getId() + "&redistribution=false", null, String.class);
				} catch (Exception e) {
					if (!(e instanceof java.net.SocketTimeoutException)) {
						logger.error("营业部：" + org.getName() + ",分配异常：" + e.getMessage());
						int length = e.getMessage().length();
						loanLogService.createLog("LoanDistributionJob", "error", "营业部：" + org.getName() + ",分配异常：" + (length > 1500 ? e.getMessage().substring(0, 1500) : e.getMessage()), "SYSTEM");
					}
				}
				logger.info("营业部："+ org.getName() +",分配结束");
				loanLogService.createLog("LoanDistributionJob", "info", "营业部："+ org.getName() +",分配结束", "SYSTEM");

			}
			
			logger.info("LoanDistributionJob结束........");
			loanLogService.createLog("LoanDistributionJob", "info", "LoanDistributionJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("LoanDistributionJob", "info", "定时开关isLoanDistribution关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isLoanDistribution关闭，此次不执行");
		}
	}

}
