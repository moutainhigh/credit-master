/**
 * Copyright (c) 2017, lings@yuminsoft.com All Rights Reserved. <br/>
 * Date:2017年6月2日下午5:18:21
 *
 */

package com.zdmoney.credit.operation.service.pub;

import java.util.List;
import java.util.Map;

/**
 * ClassName:ILoanCsCloseBusinessInfoService <br/>
 * Date: 2017年6月2日 下午5:18:21 <br/>
 * 
 * @author lings@yuminsoft.com
 */
public interface ILoanCsCloseBusinessInfoService {
	public List<Map<String, Object>> searchShutShop();
	
	public Map<String, Object> editCloseDepartment(Map<String, Object> map);
	
	public List<Map<String, Object>> searchShutedShop();
	
	public int flushShutedShop();
}
