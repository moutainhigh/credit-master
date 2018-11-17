package com.zdmoney.credit.system.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.ComOrganizationEnum.Level;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.inter.ComOrganizationInter;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 营业网点、组织架构表Service业务封装层
 * @author Ivan
 *
 */
@Service
public class ComOrganizationServiceImpl implements IComOrganizationService {
	
	@Autowired @Qualifier("comOrganizationDaoImpl")
	IComOrganizationDao comOrganizationDaoImpl;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired @Qualifier("comEmployeeServiceImpl")
	IComEmployeeService comEmployeeServiceImpl;
	
	@Autowired @Qualifier("comEmployeeDaoImpl")
	IComEmployeeDao comEmployeeDaoImpl;
	
	@Autowired
	@Qualifier("comOrganizationInter")
	ComOrganizationInter comOrganizationInter;
	
	
	/** 支持变更归属关系的层级 **/
	List<String> allowChangeLevel = Arrays.asList(new String[]{"V102","V103","V104"});
	
	/**
	 * 跟据实体信息查询实体集合（通常是组合条件查询）
	 * @param comOrganization 实体对象
	 * @author Ivan
	 */
	@Override
	public List<ComOrganization> findListByVo(ComOrganization comOrganization) {
		return comOrganizationDaoImpl.findListByVo(comOrganization);
	}
	
	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	@Override
	public ComOrganization get(Long id) {
		return comOrganizationDaoImpl.get(id);
	}
	
	/**
	 * 新增、修改营业网点数据
	 * @param comOrganization
	 * @return
	 */
	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public ComOrganization saveOrUpdate(ComOrganization comOrganization) {
		Long id = comOrganization.getId();
		
		/** 父结点编号 **/
		Long parentId = Strings.convertValue(comOrganization.getParentId(),Long.class);
		/** 父级网点代码 **/
		String parentOrgCode = "";
		/** 父级网点完整名称 **/
		String parentFullName = "";
		if (Strings.isEmpty(parentId)) {
			/** 缺少父结点编号 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"父结点编号"});
		}
		/** 通过编号查询父结点实体对象 **/
		ComOrganization comOrganizationParent = comOrganizationDaoImpl.get(parentId);
		if (comOrganizationParent == null) {
			if (parentId != 0L) {
				/** 获取父结点数据为空 **/
				throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"查询父结点实体"});
			}
		} else {
			parentOrgCode = comOrganizationParent.getOrgCode();
			parentFullName = comOrganizationParent.getFullName();
		}
		
		/** 设置完整的网点名称 **/
		String fullName = (Strings.isEmpty(parentFullName) ? "" : parentFullName + "/") + comOrganization.getName();
		comOrganization.setFullName(fullName);
		
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/
			
			/** 父结点的层级 **/
			Level parentLevel = ComOrganizationEnum.get(comOrganizationParent.getvLevel());
			/** 当前新增结点的层级 **/
			Level level = parentLevel.nextLevel();
			
			if (level == null) {
				/** 添加的结点层级已经超出层级范围 **/
				throw new PlatformException(ResponseEnum.ORGANIZATION_OUT_LEVEL);
			}
			
			/** 营业网点编码 **/
			String code = "";
			/** 查询某ParentId下最大CODE记录 **/
			ComOrganization comOrganizationMaxCode = comOrganizationDaoImpl.getMaxCode(comOrganization.getvLevel());
			if (comOrganizationMaxCode == null) {
				/** 不在在子结点 **/
				code = level.getInitCode();
			} else {
				/** 父级Code = 01，0101 **/
				/** 同级最大Code = 0101，01010005 **/
				code = comOrganizationMaxCode.getOrgCode();
				code = StringUtils.substring(code,-level.getInitCode().length());
				/** 进行自增操作 **/
				code = Strings.aa(code,code.length());
			}
			/** 拼接父级Code **/
			code = parentOrgCode + code;
			comOrganization.setOrgCode(code);
			comOrganization.setvLevel(level.getName());
			
			/** 部门类型 **/
			String departmentType = Strings.defaultValue(comOrganization.getDepartmentType(), "0", "");
			comOrganization.setDepartmentType(departmentType);
			
			/** 客户所属区域编号 **/
			String zoneCode = Strings.parseString(comOrganization.getZoneCode());
			
			switch (level) {
				case V100:
					/** 公司（证大） **/
					comOrganization.setDepartmentType("");
					comOrganization.setDepLevel("");
				case V101:
					/** 区域（东区、北区等） **/
					comOrganization.setDepartmentType("");
					comOrganization.setDepLevel("");
				case V102:
					/** 分部（江苏分部等） **/
					comOrganization.setDepartmentType("");
					comOrganization.setDepLevel("");
				case V103:
					/** 城市（上海市等） **/
					comOrganization.setDepartmentType("");
					comOrganization.setDepLevel("");
				case V104:
					/** 分店、营业部 **/
					/**
					 * 检查部门类型必填
					 * 生成档案袋使用的编号
					 * ZONE_CODE字段（前台录入） + （select count(*) + 1 where 部门类型 = '个贷'）不足三位前面补零
					 * 如 ZONE_CODE=021 档案袋使用的编号=021001
					 */
					if (Strings.isEmpty(zoneCode)) {
						/** 缺少参数 **/
						throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"所属区域编号"});
					}
					if (level == Level.V104) {
						if (Strings.isEmpty(departmentType)) {
							throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"部门类型"});
						}
						/** 
						 * 生成档案袋使用的编号 规则 
						 * ZONE_CODE字段（前台录入） + （select count(*) + 1 where 部门类型 = '个贷'）不足三位前面补零
						 * 如 ZONE_CODE=021 档案袋使用的编号=021001
						 * **/
						ComOrganization comOrganizationCount = new ComOrganization();
						comOrganizationCount.setDepartmentType(departmentType);
						int count = comOrganizationDaoImpl.queryCount(comOrganizationCount);
						count++;
						String departmentNum = "";
						departmentNum = String.format("%03d", count);
						departmentNum = zoneCode + departmentNum;
						comOrganization.setDepartmentNum(departmentNum);
					}
					break;
				case V105:
					/** 团队、组 **/
					comOrganization.setDepartmentType("");
					comOrganization.setDepLevel("");
					break;
			}
			/** 获取Sequences **/
			id = sequencesServiceImpl.getSequences(SequencesEnum.COM_ORGANIZATION);
			comOrganization.setId(id);
			comOrganizationDaoImpl.insert(comOrganization);
			comOrganizationInter.syncOrg(comOrganization,comOrganization,"0");
		} else {
			ComOrganization comOrganizationCheck = comOrganizationDaoImpl.get(id);
			if (comOrganizationCheck == null) {
				throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"编号[" + id + "]"});
			}
			String orgCode = comOrganizationCheck.getOrgCode();
			String level = comOrganization.getvLevel();
			Level levelEnum = ComOrganizationEnum.get(level);
			Assert.notNull(levelEnum, ResponseEnum.FULL_MSG,"层级异常![" + level + "]");
			int orgCodeLen = levelEnum.getInitCode().length();
			/** 获取当前结点Code内容（通常往后取2-5位） **/
			orgCode = orgCode.substring(orgCode.length() - orgCodeLen);
			/** 拼接最终Code **/
			orgCode = parentOrgCode + orgCode;
			comOrganization.setOrgCode(orgCode);
			
			/** 更新后完整的机构名称 **/
			String newFullName = comOrganization.getFullName();
			/** 更新前完整的机构名称 **/
			String oldFullName = comOrganizationCheck.getFullName();
			
			/** 更新后机构代码  **/
			String newOrgCode = comOrganization.getOrgCode();
			/** 更新前机构代码 **/
			String oldOrgCode = comOrganizationCheck.getOrgCode();
			
			Assert.notNullAndEmpty(newFullName, ResponseEnum.FULL_MSG,"缺少参数");
			Assert.notNullAndEmpty(oldFullName, ResponseEnum.FULL_MSG,"缺少参数");
			
			Assert.notNullAndEmpty(newOrgCode, ResponseEnum.FULL_MSG,"缺少参数");
			Assert.notNullAndEmpty(oldOrgCode, ResponseEnum.FULL_MSG,"缺少参数");
			
			if (!newFullName.equals(oldFullName)) {
				/** 完整的机构名称发生变更，批量更新数据  **/
				Map<String,String> param = new HashMap<String,String>();
				param.put("newFullName", newFullName);
				param.put("oldFullName", oldFullName);
				comOrganizationDaoImpl.updateBatchFullName(param);
			}
			
			if (!newOrgCode.equals(oldOrgCode)) {
				/** 机构代码发生变更，批量更新数据  **/
//				comOrganization = get(id);
//				String level = comOrganization.getvLevel();
				if (allowChangeLevel.contains(level)) {
					Map<String,String> param = new HashMap<String,String>();
					param.put("newOrgCode", newOrgCode);
					param.put("oldOrgCode", oldOrgCode);
					comOrganizationDaoImpl.updateBatchOrgCode(param);
					
//					changeOrgLevel(comOrganization);
				}
			}
			/** 修改操作 **/
			comOrganizationDaoImpl.update(comOrganization);
			comOrganizationInter.syncOrg(comOrganization,comOrganizationCheck,"1");
		}
		return comOrganization;
	}
	
	/**
	 * 更改营业网点归属关系
	 * @param comOrganization
	 */
	@Override
	public void changeOrgLevel(ComOrganization comOrganization){
//		String level = comOrganization.getvLevel();
//		
//		Assert.notNull(comOrganization, ResponseEnum.FULL_MSG,"@@@@@");
//		Long parentId = Strings.convertValue(comOrganization.getParentId(),Long.class);
//		ComOrganization parentComOrganization = get(parentId);
//		if (parentId == 0L) {
//			return;
//		}
//		Assert.notNull(parentComOrganization, ResponseEnum.FULL_MSG,"@@@@@");
//		/** 更新营业网点数据 **/
//		String parentCode = parentComOrganization.getOrgCode();
//		String code = comOrganization.getOrgCode();
//		Level levelEnum = ComOrganizationEnum.get(level);
//		Assert.notNull(levelEnum, ResponseEnum.FULL_MSG,"层级异常![" + level + "]");
//		int codeLen = levelEnum.getInitCode().length();
//		/** 获取当前结点Code内容（通常往后取2-5位） **/
//		code = code.substring(code.length() - codeLen);
//		/** 拼接最终Code **/
//		code = parentCode + code;
//		if (code.equalsIgnoreCase(comOrganization.getOrgCode())) {
//			/** 未更改营业部归属关系 **/
//			return;
//		}
//		comOrganization.setOrgCode(code);
//		comOrganizationDaoImpl.update(comOrganization);
//		
//		/** 更新员工信息 **/
//		List<ComEmployee> employeeList = comEmployeeServiceImpl.findListByOrgId(comOrganization.getId());
//		for (int k=0;k<employeeList.size();k++) {
//			ComEmployee comEmployee = employeeList.get(k);
//			String empNum = comEmployee.getEmpNum();
//			String employeeType = Strings.convertValue(comEmployee.getEmployeeType(),String.class);
//			if (employeeType.equalsIgnoreCase(EmployeeTypeEnum.业务员.name())) {
//				if (empNum.length() - 7 >= 0) {
//					empNum = empNum.substring(empNum.length() - 7);
//				} else {
//					empNum = "";
//				}
//			} else {
//				empNum = "";
//			}
//			empNum = code + empNum;
//			comEmployee.setEmpNum(empNum);
//			comEmployeeServiceImpl.saveOrUpdate(comEmployee,false);
//		}
//		
//		/** 查询子营业网点数据 **/
//		ComOrganization searchComOrganization = new ComOrganization();
//		searchComOrganization.setParentId(comOrganization.getId() + "");
//		List<ComOrganization> list = findListByVo(searchComOrganization);
//		for (int i=0;i<list.size();i++) {
//			changeOrgLevel(list.get(i));
//		}
	}
	
	/**
	 * 删除营业网点数据
	 * @param comOrganization
	 * @return
	 */
	@Override
	public ComOrganization delete(Long id) {
		ComOrganization comOrganization = comOrganizationDaoImpl.get(id);
		if (comOrganization == null) {
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"编号[" + id + "]"});
		}
		/** 查询是否包含子结点 **/
		ComOrganization comOrganizationCount = new ComOrganization();
		comOrganizationCount.setParentId(Strings.convertValue(id, String.class));
		int count = comOrganizationDaoImpl.queryCount(comOrganizationCount);
		if (count > 0) {
			/** 存在子结点无法删除 **/
			throw new PlatformException(ResponseEnum.ORGANIZATION_CANNOT_DELETE);
		} else {
			comOrganizationDaoImpl.deleteById(id);
			comOrganizationInter.syncOrg(comOrganization,comOrganization,"2");
		}
		return comOrganization;
	}
	
	/**
     * 查询当前机构的父机构信息
     * @param id
     * @return
     */
	public ComOrganization findParentOrganization(Long id) {
		return comOrganizationDaoImpl.findParentOrganization(id);
	}
	
	
	/**
	 * 根据机构层级查询机构信息
	 * @param employeeType
	 * @return
	 */
	public Map<String,Object> findOrganizationByLevel(String vlevel,boolean showParentName){
		Map<String,Object> result = new HashMap<String,Object>();
		User user = UserContext.getUser();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgCode", user.getOrgCode());
		params.put("vLevel", vlevel);
		if(showParentName==true){
			params.put("showParentName", true);
		}
		result.put("signSalesDeps", comOrganizationDaoImpl.findOrganization(params));
		result.put("success", true);
		result.put("message", "查询成功");
		return result;
	}
	
	/**
	 * 查询营业网点完整名称
	 */
	@Override
	public String searchOrgFullName(Long orgId){
		ComOrganization comorg = get(orgId);
		if (comorg == null) {
			return "";
		}
		String parentid = comorg.getParentId();
		if (parentid.equalsIgnoreCase("0")) {
			return comorg.getName();
		} else {
			String fullName = searchOrgFullName(Strings.convertValue(parentid,Long.class));
			fullName = fullName + "/" + comorg.getName();
			return fullName;
		}
	}
	
	/***
	 * 查询营业网点数据（带完整名称 如/证大投资/东区/济南分部/济南/济南解放路营业部）
	 * @param comOrganization
	 * @return
	 */
	@Override
	public List<ComOrganization> searchWithFullName(ComOrganization comOrganization) {
		return comOrganizationDaoImpl.searchWithFullName(comOrganization);
	}
	@Override
	public List<Map<String, Object>> findOrganization(Map<String, Object> params) {
		return comOrganizationDaoImpl.findOrganization(params);
	}

	@Override
	public Integer getPaymentRouteDeptCount(Long orgId) {
		return comOrganizationDaoImpl.getPaymentRouteDeptCount(orgId);
	}
	
}
