package com.zdmoney.credit.core.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.blacklist.domain.Customer;

public interface IBlackListCoreService {
	
    /**
     * 把用户黑名单转换成json
     * @param params 入参
     * @return 返回转换好的json
     */
    public Map<String,Object> syncBlackCustomerToJson(List<Customer> customerAddList);
	/**
	 * 访问征审系统获得黑名单
	 * @param date
	 * @return
	 */
	public List<Customer> getCustomerBlackListWithZS(Date date);
}
