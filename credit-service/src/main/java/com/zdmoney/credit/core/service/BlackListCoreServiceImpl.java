package com.zdmoney.credit.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.blacklist.domain.Customer;
import com.zdmoney.credit.common.json.JackJsonUtil;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.UrlUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.core.service.pub.IBlackListCoreService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class BlackListCoreServiceImpl implements IBlackListCoreService{

	@Autowired
	ISysParamDefineService sysParamDefineService;
	private static final Logger logger = Logger.getLogger(BlackListCoreServiceImpl.class);

	private static final String SYNC_BLACK_CUSTOMER_INAME = "/rpc/syncBlackCustomer";
	
	@Override
	public List<Customer> getCustomerBlackListWithZS(Date date) {
		String serurl = sysParamDefineService.getSysParamValueCache("blackList", "aps.server.url");
		List<Customer> list = null;
		if (!Strings.isEmpty(serurl)) {
			String url = serurl + SYNC_BLACK_CUSTOMER_INAME;
			
			Map<String, String> map = new HashMap<String, String>();
			String dateStr = Dates.getDateTime(date, Dates.DEFAULT_DATE_FORMAT);
			map.put("riskTime", dateStr);
			map.put("organ", "信贷系统");
			
			String value = UrlUtil.methodPost(url, map);
			logger.info("征审系统黑名单返回信息："+value);
			list = getCustomerBlackList(value);
		} else {
			list = new ArrayList<Customer>();
			logger.error("获取abs.server.url异常！");
		}
		
		return list;
	}
	
	private List<Customer> getCustomerBlackList(String jsonValue) {
		List<Customer> list = new ArrayList<Customer>();
		
		try {

			@SuppressWarnings("unchecked")
			Map<String, Object> sourceMap = (Map<String, Object>) JackJsonUtil.strToObj(jsonValue, Map.class);
			
			String message = (String) sourceMap.get("message");
			String code = (String) sourceMap.get("code");
			
			if (!"000000".equals(code)) {
				logger.warn("获取数据返回失败：" + message);
	            return list;
			}
			
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> customerMap = (List<Map<String, Object>>) sourceMap.get("customer");
			for (Map<String, Object> dataMap : customerMap) {
	            Customer customer = new Customer();
	            customer.setCreateOrgan((String) dataMap.get("createOrgan"));
	            customer.setCreator((String) dataMap.get("creator"));
	            customer.setIdCard((String) dataMap.get("idCard"));
	            customer.setInfoSource((String) dataMap.get("infoSource"));
	            customer.setMobilePhone((String) dataMap.get("mobilePhone"));
	            customer.setName((String) dataMap.get("name"));
	            customer.setRiskCase((String) dataMap.get("riskCase"));
	            customer.setRiskTime((String) dataMap.get("riskTime"));
	            customer.setTelePhone((String) dataMap.get("telePhone"));
	            customer.setWorkUnit((String) dataMap.get("workUnit"));
	            list.add(customer);
			}
			
		}  catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return list;
	}

	@Override
	public Map<String, Object> syncBlackCustomerToJson(
			List<Customer> customerAddList) {
        Map<String, Object> json = new HashMap<String, Object>();
        json = MessageUtil.returnListSuccessMessage(customerAddList, "customer");
        return json;
	}

}
