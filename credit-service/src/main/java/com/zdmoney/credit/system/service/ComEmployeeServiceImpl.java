package com.zdmoney.credit.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.SysOperationTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.dao.pub.IComEmployeeRoleDao;
import com.zdmoney.credit.system.dao.pub.ISysOperationLogDao;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComPermission;
import com.zdmoney.credit.system.domain.SysOperationLog;
import com.zdmoney.credit.system.inter.ComEmployeeInter;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 员工数据表Service业务封装层
 * 
 * @author Ivan
 *
 */
@Service
public class ComEmployeeServiceImpl implements IComEmployeeService {
	
    protected static Log logger = LogFactory.getLog(ComEmployeeServiceImpl.class);
    
	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	@Autowired
	@Qualifier("comEmployeeDaoImpl")
	IComEmployeeDao comEmployeeDaoImpl;
	
	@Autowired
	@Qualifier("comEmployeeRoleDaoImpl")
	IComEmployeeRoleDao comEmployeeRoleDaoImpl;
	@Autowired
	ISendMailService sendMailService;

	@Autowired
	ISysOperationLogDao sysOperationLogDao;

	@Autowired
	@Qualifier("comEmployeeInter")
	private ComEmployeeInter comEmployeeInter;

	/**
	 * 查询实体信息在数据库中是否存在
	 * 
	 * @param comEmployee
	 *            实体信息
	 * @return
	 */
	@Override
	public ComEmployee get(ComEmployee comEmployee) {
		return comEmployeeDaoImpl.get(comEmployee);
	}

	/**
	 * 通过PK获取实体信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public ComEmployee get(Long id) {
		return comEmployeeDaoImpl.get(id);
	}

	/**
	 * 带分页查询
	 * 
	 * @param ComEmployee
	 *            条件实体对象
	 * @return
	 */
	@Override
	public Pager findWithPg(ComEmployee comEmployee) {
		return comEmployeeDaoImpl.findWithPg(comEmployee);
	}

	@Override
	public List findComPermission(ComPermission comPermission) {
		return comEmployeeDaoImpl.findListByVo(comPermission);
	}

	@Override
	public List<ComEmployee> findListByOrgId(Long orgId) {
		ComEmployee comEmployee = new ComEmployee();
		comEmployee.setOrgId(orgId);
		return comEmployeeDaoImpl.findListByVo(comEmployee);
	}

	@Override
	public ComEmployee saveOrUpdate(ComEmployee comEmployee) {
		return saveOrUpdate(comEmployee,true,true,true);
	}
	
	@Override
	public ComEmployee saveOrUpdate(ComEmployee comEmployee,boolean isSync){
		return saveOrUpdate(comEmployee,isSync,isSync,true);
	}
	
	@Override
	public ComEmployee saveOrUpdate(ComEmployee comEmployee,boolean isOldCall,boolean isNewCall,boolean isEncrypt) {
		if (comEmployee == null) {
			return comEmployee;
		}

		Long id = comEmployee.getId();
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/
			comEmployee.setId(sequencesServiceImpl
					.getSequences(SequencesEnum.COM_EMPLOYEE));
			if (isEncrypt) {
				comEmployee.setPassword(MD5Util.md5Hex(comEmployee.getPassword()));
			}
			comEmployeeDaoImpl.insert(comEmployee);
			comEmployeeInter.userSync(comEmployee, comEmployee,isOldCall,isNewCall, "0");
		} else {
			/** 修改操作 **/
			ComEmployee oldEmployee = this.get(comEmployee.getId());
			if(!oldEmployee.getPassword().equals(comEmployee.getPassword())){
				if (isEncrypt) {
					comEmployee.setPassword(MD5Util.md5Hex(comEmployee.getPassword()));
				}
			}
			comEmployeeDaoImpl.update(comEmployee);
			ComEmployee newEmployee = this.get(comEmployee.getId());
			comEmployeeInter.userSync(newEmployee, oldEmployee,isOldCall,isNewCall, "1");
		}
		return comEmployee;
	}

	@Override
	public List getComEmployeeList() {
		// TODO Auto-generated method stub
		return comEmployeeDaoImpl.findAllList();
	}

	public ComEmployee queryEmployeeByOrgIdAndRoleName(
			Map<String, Object> params) {
		return comEmployeeDaoImpl.queryEmployeeByOrgIdAndRoleName(params);
	}

	@Override
	public ComEmployee findByUserCode(String userCode) {
		ComEmployee c = comEmployeeDaoImpl.findEmployeeByUserCode(userCode);

		return c;
	}

	@Override
	public ComEmployee findComEmployeeByUser(ComEmployee comEmployee) {
		ComEmployee c = comEmployeeDaoImpl.findEmployeeByUserCode(comEmployee);

		return c;
	}

	/**
	 * @author 00236633
	 * @param employeeType
	 * @return
	 */
	public Map<String, Object> findEmployeeByEmployeeType(String employeeType) {
		Map<String, Object> result = new HashMap<String, Object>();

		User user = UserContext.getUser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", user.getOrgCode());
		params.put("employeeType", employeeType);
		params.put("inActive", "t");

		result.put("employeeList", comEmployeeDaoImpl.findEmployee(params));
		result.put("success", true);
		result.put("message", "查询成功");

		return result;
	}

	/**
	 * 查询员工(带分页)
	 * 
	 * @author Ivan
	 * @param params
	 * @return list
	 */
	@Override
	public Pager findEmployeeWithPg(Map<String, Object> params) {
		return comEmployeeDaoImpl.findEmployeeWithPg(params);
	}
	@Override
	public Pager findWithPgByMap(Map paramMap) {
		return comEmployeeDaoImpl.findWithPgByMap(paramMap);
	}
	
	@Override
	@Transactional
	public void forgetPwd(ComEmployee employee) throws Exception {
		String token = UUID.randomUUID().toString().replace("-", "");
		sendMailService.sendForgetPwdMail(employee.getUsercode(),
				employee.getEmail(), token);

		SysOperationLog log = new SysOperationLog();
		log.setId(sequencesServiceImpl
				.getSequences(SequencesEnum.SYS_OPERATION_LOG));
		log.setEmployeeId(employee.getId());
		log.setOperationType(SysOperationTypeEnum.RESETPWD.getValue());
		log.setToken(token);

		sysOperationLogDao.insert(log);
	}

	@Override
	@Transactional
	public void resetPwd(ComEmployee employee, String newPwd, String token,
			String ip) throws Exception {
		SysOperationLog condition = new SysOperationLog();
		condition.setEmployeeId(employee.getId());
		condition.setToken(token);

		SysOperationLog log = sysOperationLogDao
				.selectByEmployeeIdAndToken(condition);

		// token不匹配或该token已经进行过操作
		if (null == log || null != log.getUpdateTime()) {
			throw new Exception("操作已失效！请重新发起操作！");
		}

		// 超过1小时
		if (Dates.addMinutes(log.getCreateTime(), 60).before(Dates.getNow())) {
			throw new Exception("操作超时！");
		}

		ComEmployee newUser = new ComEmployee();
		newUser.setId(employee.getId());
		newUser.setPassword(MD5Util.md5Hex(newPwd));
		newUser.setInActive("t");

		comEmployeeDaoImpl.update(newUser);

		log.setOperateIp(ip);
		sysOperationLogDao.updateByEmployeeIdAndToken(log);

	}

	@Override
	@Transactional
	public void saveEmpAndRole(ComEmployee comEmployee, Long roleId) {

		if (comEmployee == null) {
			return;
		}

		Long id = comEmployee.getId();
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/
			comEmployee.setId(sequencesServiceImpl
					.getSequences(SequencesEnum.COM_EMPLOYEE));
			comEmployeeDaoImpl.insert(comEmployee);
			if(roleId!=null){
			 ComEmployeeRole comEmployeeRole=new ComEmployeeRole();
	           comEmployeeRole.setEmployeeId(comEmployee.getId());
	           comEmployeeRole.setRoleId(roleId);
	           comEmployeeRole.setId(sequencesServiceImpl
					.getSequences(SequencesEnum.COM_EMPLOYEE_ROLE));
	           comEmployeeRoleDaoImpl.insert(comEmployeeRole);
			}
			try {
				comEmployeeInter.userSync(comEmployee, comEmployee,true,true, "0");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		} else {
			/** 修改操作 **/
			ComEmployee oldEmployee = this.get(comEmployee.getId());
			comEmployeeDaoImpl.update(comEmployee);
			ComEmployee newEmployee = this.get(comEmployee.getId());
			if(roleId!=null)
			{
			  	//先根据用户id删除用户角色信息，在新增
				 ComEmployeeRole comEmployeeRole=new ComEmployeeRole();
		           comEmployeeRole.setEmployeeId(comEmployee.getId());
				comEmployeeRoleDaoImpl.deleteByIdList(comEmployeeRole);
				comEmployeeRole.setRoleId(roleId);
				 comEmployeeRole.setId(sequencesServiceImpl
							.getSequences(SequencesEnum.COM_EMPLOYEE_ROLE));
				comEmployeeRoleDaoImpl.insert(comEmployeeRole);
			}
			try {
				//newEmployee.setOrgId(2l);
				//oldEmployee.setOrgId(2l);
				comEmployeeInter.userSync(newEmployee, oldEmployee,true,true, "1");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		  
	}

	@Override
	public List<Map<String, String>> findLeaveEmployeeList(String orgCode) {
		
		return comEmployeeDaoImpl.findLeaveEmployeeList(orgCode);
	}
}
