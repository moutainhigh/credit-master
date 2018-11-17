package com.zdmoney.credit.zhuxue.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.zhuxue.dao.pub.IZhuxueOrganizationDao;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
import com.zdmoney.credit.zhuxue.vo.ZhuxueOrganizationAccountCardVo;

/**
 * 助学贷组织机构数据库操作
 * @author 00234770
 * @date 2015年9月21日 下午2:50:25 
 *
 */
@Repository
public class ZhuxueOrganizationDaoImpl extends BaseDaoImpl<ZhuxueOrganization>implements IZhuxueOrganizationDao {


	@Override
	public ZhuxueOrganization findByPlanId(Long planId) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".findByPlanId",planId);
	}
	
	/**
	 * 查找帐卡信息  
	 * @param paramMap
	 * @return
	 */
	@Override
	public Pager findAccountCardPGByMap(Map<String, Object> paramMap) {
		Pager pager = (Pager)paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		
		List<ZhuxueOrganizationAccountCardVo> accountCards = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findAccountCardPGByMap", paramMap);
		pager.setResultList(accountCards);
		pager.setTotalCount(accountCards != null ? accountCards.size() : 0);
		
		return pager;
	}

}
