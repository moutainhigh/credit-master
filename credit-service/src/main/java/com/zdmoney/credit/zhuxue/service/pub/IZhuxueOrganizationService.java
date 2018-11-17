package com.zdmoney.credit.zhuxue.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;

/**
 * 助学贷组织机构service接口定义
 * @author 00234770
 * @date 2015年9月21日 下午3:06:09 
 *
 */
public interface IZhuxueOrganizationService {

	/**
	 * 根据方案id查找机构
	 * @param planId
	 * @return
	 */
	public ZhuxueOrganization findByPlanId(Long planId);
	
	/**
	 * 根据Map去查找
	 * @param param
	 * @return
	 */
	public Pager findWithPgByMap(Map<String, Object> param);
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public ZhuxueOrganization findById(Long id);
	
	/**
	 * 查找机构帐卡信息
	 * @param id
	 * @return
	 */
	public Pager getZhuxueOrganizationAccountCard(Map<String, Object> paramMap);
}
