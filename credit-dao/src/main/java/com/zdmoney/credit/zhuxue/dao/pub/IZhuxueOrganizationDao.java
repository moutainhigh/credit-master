package com.zdmoney.credit.zhuxue.dao.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;

/**
 * 助学贷组织机构相关接口定义
 * @author 00234770
 * @date 2015年9月21日 下午2:43:38 
 *
 */
public interface IZhuxueOrganizationDao extends IBaseDao<ZhuxueOrganization> {

	/**
	 * 根据方案id查找机构
	 * @param planId
	 * @return
	 */
	ZhuxueOrganization findByPlanId(Long planId);
	
	/**
	 * 查找帐卡信息  
	 * @param paramMap
	 * @return
	 */
	public Pager findAccountCardPGByMap(Map<String,Object> paramMap);
}
