package com.zdmoney.credit.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.ComOrganization;

/**
 * 营业网点、组织架构表Dao操作层
 * 
 * @author Ivan
 *
 */
@Repository
public class ComOrganizationDaoImpl extends BaseDaoImpl<ComOrganization> implements IComOrganizationDao {

	@Override
	public List<ComOrganization> searchWithFullName(ComOrganization comOrganization) {
		List<ComOrganization> resultList = getSqlSession().selectList(
				getIbatisMapperNameSpace() + ".searchWithFullName", comOrganization);
		return resultList;
	}

	/**
	 * 查询最大CODE记录
	 * 
	 * @param comOrganization
	 * @return
	 */
	@Override
	public ComOrganization getMaxCode(String vLevel) {
		if (Strings.isEmpty(vLevel)) {
			return null;
		}
		ComOrganizationEnum.Level levelEnum = ComOrganizationEnum.get(vLevel);
		if (levelEnum == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vLevel", vLevel);
		map.put("codeLen", -levelEnum.getInitCode().length());
		map.put("maxCodeLen", levelEnum.getMaxCodeLen());
		Object obj = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getMaxCode", map);
		return (ComOrganization) obj;
	}

	/**
	 * 查询当前机构的父机构信息
	 * 
	 * @param id
	 * @return
	 */
	public ComOrganization findParentOrganization(Long id) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findParentOrganization", id);
	}

	/**
	 * @author 00236633
	 * @param params
	 * @return list
	 */
	@Override
	public List<Map<String, Object>> findOrganization(Map<String, Object> params) {
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOrganization",
				params);
		return result;
	}

	/***
	 * 批量变更营业网点代码 包含所有子集
	 * 
	 * @param param
	 */
	@Override
	public void updateBatchOrgCode(Map<String, String> param) {
		getSqlSession().update(getIbatisMapperNameSpace() + ".updateBatchOrgCode", param);
	}

	/***
	 * 批量变更营业网点名称 包含所有子集
	 * 
	 * @param param
	 */
	@Override
	public void updateBatchFullName(Map<String, String> param) {
		getSqlSession().update(getIbatisMapperNameSpace() + ".updateBatchFullName", param);
	}

	@Override
	public Map queryPartOrgName(Long id) {
		Object obj = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryPartOrgName", id);
		return (Map) obj;
	}

	@Override
	public ComOrganization getNoValidSalesDepartment(Long id) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getNoValidSalesDepartment", id);
	}

	@Override
	public Integer getPaymentRouteDeptCount(Long orgId) {
		Object count = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getPaymentRouteDeptCount", orgId);
		int totalCount = Strings.convertValue(count.toString(),Integer.class);
		return totalCount;
	}
}
