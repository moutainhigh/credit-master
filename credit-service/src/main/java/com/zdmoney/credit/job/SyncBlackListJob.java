package com.zdmoney.credit.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.blackList.service.pub.IBlackListService;
import com.zdmoney.credit.blacklist.domain.ComBlacklist;
import com.zdmoney.credit.blacklist.domain.Customer;
import com.zdmoney.credit.blacklist.domain.Enterprise;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 黑名单同步job
 * @author 00232949
 *
 */
@Service
public class SyncBlackListJob {
	
	private static final Logger logger = Logger.getLogger(SyncBlackListJob.class);
	@Autowired
	private ILoanLogService loanLogService;
	@Autowired
	private IBlackListService blackListService;
	@Autowired
	private ISysParamDefineService sysParamDefineService;
	
	public void execute() {
    	String isSyncBlackList = sysParamDefineService.getSysParamValue("sysJob", "isSyncBlackList");
		if(!Const.isClosing.equals(isSyncBlackList)){
	        loanLogService.createLog("SyncBlackListJob", "info", "SyncBlackListJob开始........", "SYSTEM");
	        logger.info("SyncBlackListJob开始........");
	        //同步集团个人黑名到信贷系统
	        SyncCustomerBlackListToCredit();
	        //同步集团企业黑名到信贷系统
	        SyncEnterpriseBlackListToCredit();

	        //同步信贷系统个人黑名单到集团
	        syncCustomerBlackListToZongbu();
	        //同步信贷系统企业黑名单到集团
	        syncEnterpriseBlackListToZongbu();

	        loanLogService.createLog("SyncBlackListJob", "info", "SyncBlackListJob結束........, ","SYSTEM");
	        logger.info("SyncBlackListJob結束........");
		}else{
			loanLogService.createLog("SyncBlackListJob", "info", "定时开关isSyncBlackList关闭，此次不执行", "SYSTEM");
			logger.warn("定时开关isSyncBlackList关闭，此次不执行");
		}
    }

	/**
	 * 同步信贷系统企业黑名单到集团
	 */
	private void syncEnterpriseBlackListToZongbu() {
		List<Enterprise> enterpriseAddList=new ArrayList<Enterprise>();
        List<ComBlacklist> blackListForEnterprise=blackListService.findLocalEnterpriseBlacklist(Dates.addDays(new Date(), -1));
        for(ComBlacklist comBlacklist : blackListForEnterprise){
        	 Enterprise enterprise = new Enterprise();
             enterprise.setName(comBlacklist.getCompany());
             enterprise.setTelePhone(comBlacklist.getTel());
             enterprise.setRiskCase(comBlacklist.getMemo());
             enterprise.setRiskTime(Dates.getDateTime(comBlacklist.getRejectDate(),null));
             enterprise.setInfoSource("信贷系统");
             enterprise.setCreateOrgan("证大投资咨询有限公司");
             enterprise.setCreator("信贷系统管理员");
             enterpriseAddList.add(enterprise);
        }
        blackListService.addEnterpriseBlackList(enterpriseAddList);
	}

	/**
	 * 同步信贷系统个人黑名单到集团
	 */
	private void syncCustomerBlackListToZongbu() {
		 List<Customer> customerAddList=new ArrayList<Customer>();
	        List<ComBlacklist> blackListForCustomer = blackListService.findLocalCustomerBlacklist(Dates.addDays(new Date(), -1)); 
	        for(ComBlacklist comBlacklist : blackListForCustomer ){
	        	Customer customer=new Customer();
	            customer.setName(comBlacklist.getName());
	            customer.setIdCard(comBlacklist.getIdnum());
	            customer.setMobilePhone(comBlacklist.getMphone());
	            customer.setTelePhone(comBlacklist.getTel());
	            customer.setWorkUnit(comBlacklist.getCompany());
	            customer.setRiskCase(comBlacklist.getMemo());
	            customer.setRiskTime(Dates.getDateTime(comBlacklist.getRejectDate(), null));
	            customer.setInfoSource("信贷系统");
	            customer.setCreateOrgan("证大投资咨询有限公司");
	            customer.setCreator("信贷系统管理员");
	            customerAddList.add(customer);
	        }
	        blackListService.addCustomerBlackList(customerAddList);
		
	}

	/**
	 *  同步集团企业黑名到信贷系统
	 */
	private void SyncEnterpriseBlackListToCredit() {
		List<Enterprise> enterpriseBlackList;
		try {
			enterpriseBlackList = blackListService.getEnterpriseBlackList("","","","","","");
			for(Enterprise enterprise : enterpriseBlackList){
	        	if(Strings.isNotEmpty(enterprise.getName()))  {
	        		ComBlacklist  blackList = blackListService.findByCompany(enterprise.getName());
	                if(blackList==null)
	                {
	                    if(!isTel(enterprise.getTelePhone())){
	                    	enterprise.setTelePhone("");
	                    }
	                    try {
							blackListService.createComBlacklist(enterprise);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
	                }
	            }
	        }
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 同步集团个人黑名到信贷系统
	 */
    private void SyncCustomerBlackListToCredit() {
    	List<Customer> customerBlackList;
		try {
			customerBlackList = blackListService.getCustomerBlackList("","","","","","");
			for(Customer customer: customerBlackList){
	        	 if(customer.getName()!=null && customer.getIdCard()!=null)  {
					ComBlacklist blackList = blackListService.findByNameAndIdnum(customer.getName(),customer.getIdCard());
	                 if(blackList==null)
	                 {
	                     if(!isMobilePhone(customer.getMobilePhone())){
	                    	 customer.setMobilePhone("");
	                     }
	                     if(!isTel(customer.getTelePhone())){
	                    	 customer.setTelePhone("");
	                     }
	                     try {
							blackListService.createComBlacklist(customer);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
	                 }
	             }
	        }
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		}
		
	}

	/**
     * 验证是否为手机号码  11位数字
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
