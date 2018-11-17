package com.zdmoney.credit.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.blackList.dao.pub.IBlackListDao;
import com.zdmoney.credit.blacklist.domain.ComBlacklist;
import com.zdmoney.credit.blacklist.domain.Customer;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.core.service.pub.IBlackListCoreService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class SyncBlackListZSJob {

	private static final Logger logger = Logger.getLogger(AdvanceRepaymentJob.class);
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IBlackListCoreService blackListCoreService;
	@Autowired
	private IBlackListDao dao;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	private ISequencesService sequencesServiceImpl;
	
	public void execute() {
    	String isSyncBlackListZS = sysParamDefineService.getSysParamValue("sysJob", "isSyncBlackListZS");
		if(!Const.isClosing.equals(isSyncBlackListZS)){
			logger.info("SyncBlackListZSJob开始........");
			loanLogService.createLog("SyncBlackListZSJob", "info", "SyncBlackListZSJob开始........", "SYSTEM");
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
			
			try {
				List<Customer> customerBlackList = blackListCoreService.getCustomerBlackListWithZS(c.getTime());
				for (Customer customer : customerBlackList) {
					ComBlacklist condition = new ComBlacklist();
					condition.setName(customer.getName());
					condition.setIdnum(customer.getIdCard());

					List<ComBlacklist> blackList = dao.findListByVo(condition);

					if (blackList.size() == 0) {
						if (!isMobilePhone(customer.getMobilePhone())) {
							customer.setMobilePhone(" ");
						}

						if (!isTel(customer.getTelePhone())) {
							customer.setTelePhone(" ");
						}

						ComBlacklist newBlacklist = new ComBlacklist();
						newBlacklist.setId(sequencesServiceImpl.getSequences(SequencesEnum.COM_BLACKLIST));
						newBlacklist.setComeFrome("征审系统");
						newBlacklist.setName(customer.getName());
						newBlacklist.setIdnum(customer.getIdCard());
						newBlacklist.setCompany(customer.getWorkUnit());
						newBlacklist.setMphone(customer.getMobilePhone());
						newBlacklist.setTel(customer.getTelePhone());
						newBlacklist.setCanSubmitRequestDays(-1L);
						newBlacklist.setLoanType("all");
						newBlacklist.setMemo(customer.getRiskCase());
						newBlacklist.setRejectDate(Strings.isNotEmpty(customer.getRiskTime()) ? Dates.parse(
								customer.getRiskTime(), "yyyy-MM-dd HH:mm:ss")
								: new Date());
						newBlacklist.setOperatorId(null);
						newBlacklist.setCreateTime(new Date());

						dao.insert(newBlacklist);
					}
				}
			} catch (Exception e) {
				logger.error("征审系统同步黑名单失败！"+e.getMessage(),e); 
				loanLogService.createLog("SyncBlackListZSJob", "error", "征审系统同步黑名单失败！"+e.getMessage(), "SYSTEM");
			}
			
			logger.info("SyncBlackListZSJob结束........");
			loanLogService.createLog("SyncBlackListZSJob", "info", "SyncBlackListZSJob结束........", "SYSTEM");
		}else{
			loanLogService.createLog("SyncBlackListZSJob", "info", "定时开关isSyncBlackListZS关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isSyncBlackListZS关闭，此次不执行");
		}
		

	}
	
    /**
     * 验证是否为手机号码
     * @param mobilePhone  手机号
     * @return
     */
    private boolean isMobilePhone(String mobilePhone){
        return  mobilePhone.matches("^1\\d{10}$");
    }

    /**
     * 验证是否为电话号码
     * @param tel  电话号码
     * @return
     */
    private boolean  isTel(String tel){
        return tel.matches("(\\d{3}-(\\d{7}|\\d{8})|\\d{4}-(\\d{7}|\\d{8}))([-]\\d+)?");
    }

}
