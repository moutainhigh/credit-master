package com.zdmoney.credit.loan.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanFilesInfoDao;
import com.zdmoney.credit.loan.domain.LoanFilesInfo;


@Repository("loanFilesInfoDao")
public class LoanFilesInfoDaoImpl extends BaseDaoImpl<LoanFilesInfo> implements ILoanFilesInfoDao{

	/**
	 * 根据登录用户Id查询业务人员
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findBusinessEmployeeListByOrgId(Map<String, Object> params) {
		
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findBusinessEmployeeListByOrgId", params);
		
		return result;
	}

	/**
	 * 查询客户档案列表
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findLoanFilesInfoListByMap(Map<String, Object> params) {
		
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanFilesInfoListByMap", params);
		
		return result;
	}

	/**
	 * 查询客户档案列表数量
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public int findLoanFilesInfoCountByMap(Map<String, Object> params) {
		
		int result = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLoanFilesInfoCountByMap", params);
		
		return result;
	}

	/**
	 * 根据员工号查询员工
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findEmployeeListByIds(Map<String, Object> params) {
		
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findEmployeeListByIds", params);
		
		return result;
	}

	/**
	 * 查询借款基本信息
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findLoanBaseInfoById(Map<String, Object> params) {
		
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanBaseInfoById", params);
		
		return result;
	}
	
	/**
	 * 查询档案袋使用的编号
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findBorrowDocNumByLoanId(Map<String, Object> params) {
		
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findBorrowDocNumByLoanId", params);
		
		return result;
	}
	
	/**
	 * 添加客户档案
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public int insert(Map<String, Object> params) {
		int insertCount =  getSqlSession().update(getIbatisMapperNameSpace() + ".insert", params);
		return insertCount;
	}

	/**
	 * 查询档案管理信息根据档案id
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String,Object>> findLoanFilesInfoById(Map<String, Object> params) {
		
		List<Map<String,Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanFilesInfoById", params);
		
		return result;
	}
	

	
	/**
	 * 更新客户档案
	 * @author 00236633
	 * @param params
	 * @return
	 */
	@Override
	public int update(Map<String, Object> params) {
		int updateCount = getSqlSession().update(getIbatisMapperNameSpace() + ".update", params);
		return updateCount;
	}

	/**
	 * 查询档案催收管理列表
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findLoanCollectionManageListByMap(Map<String, Object> params) {
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanCollectionManageListByMap", params);
		
		return result;
	}

	/**
	 * 查询档案催收管理表数据总计
	 * @param params
	 * @return
	 */
	public int findLoanCollectionManageCountByMap(Map<String, Object> params) {
	int result = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLoanCollectionManageCountByMap", params);
		
		return result;
	}

	/**
	 * 更新档案催收信息 操作类型
	 */
	public int updateCollectionByIds(Map<String, Object> params) {
		int updateCount = getSqlSession().update(getIbatisMapperNameSpace() + ".updateCollectionByIds", params);
		return updateCount;
	}

	@Override
	public List<Map<String, Object>> getLoanCollectionByMap(Map<String, Object> params) {
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getLoanCollectionByMap", params);
		return result;
	}

	@Override
	public int insertLoanCollection(Map<String, Object> params) {
		int insertCount =  getSqlSession().update(getIbatisMapperNameSpace() + ".insertLoanCollection", params);
		return insertCount;
	}
	
}
