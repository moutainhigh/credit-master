package com.zdmoney.credit.system.inter;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 同步用户信息到第三方系统
 * @author 00236633
 *
 */
@Service
public class ComEmployeeInter {
	
	protected static Log logger = LogFactory.getLog(ComEmployeeInter.class);
	
	@Autowired
    RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("sysParamDefineServiceImpl")
	ISysParamDefineService sysParamDefineServiceImpl;
	
	@Autowired @Qualifier("comOrganizationDaoImpl")
	IComOrganizationDao comOrganizationDaoImpl;
	
	@Value("${synchronize.aps.key}")
	public String authKey;
	
	/**
	 * 同步员工信息(新平台)
	 * @param newEmployee
	 * @param oldEmployee
	 * @param optType
	 */
	public void userSyncNew(ComEmployee newEmployee,ComEmployee oldEmployee,String optType) {
		String syncUrl = "";
		try {
			syncUrl = sysParamDefineServiceImpl.getSysParamValueCache("organization", "sync.organization.new");
			Assert.notNullAndEmpty(syncUrl, ResponseEnum.FULL_MSG,"syncUrl缺少URL信息");
			syncUrl += "/api/saveOrUpdateEmployee";
			HttpHeaders headers =new HttpHeaders();
			MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(mediaType);
			
//			ComOrganization oldOrganization = comOrganizationDaoImpl.get(oldEmployee.getOrgId());
			ComOrganization newOrganization = comOrganizationDaoImpl.get(newEmployee.getOrgId());
			
			JSONObject paramsJson = new JSONObject();
			paramsJson.put("authKey", MD5Util.md5Hex(authKey));//接口密钥
			paramsJson.put("usercode", newEmployee.getUsercode());
			paramsJson.put("name", newEmployee.getName());
			paramsJson.put("userType", 1);//0:实习, 1:正式.目前没有区分，默认传1
//			paramsJson.put("oldParentOrgCode", oldOrganization.getOrgCode());
			paramsJson.put("orgId", newOrganization.getId());
			paramsJson.put("dataSource", 1);//1: 核心系统
			if("t".equalsIgnoreCase(newEmployee.getInActive())){
				paramsJson.put("inActive", "t");
			}else{
				paramsJson.put("inActive", "f");
			}
			if(newEmployee.getEmail()!=null){
				paramsJson.put("email", newEmployee.getEmail());
			}
			paramsJson.put("password", newEmployee.getPassword());
			paramsJson.put("optType", optType);
			
			String uuid =UUID.randomUUID().toString().replaceAll("-", "");
			logger.info("userSyncNew输入参数"+uuid+"："+paramsJson);
			HttpEntity<String> request = new HttpEntity<String>(paramsJson.toString(), headers);
			ResponseEntity<String> response = restTemplate.postForEntity(syncUrl, request, String.class);
			try {
				logger.info("userSyncNew输出参数"+uuid+"："+JSONObject.parseObject(response.getBody()));
			} catch (Exception e) {
			}
			HttpStatus httpStatus = response.getStatusCode();
			String body = "";
			if (httpStatus == HttpStatus.OK) {
				body = response.getBody();
			} else {
				throw new PlatformException("状态码：" + httpStatus);
			}
			
			JSONObject resultJson = JSONObject.parseObject(body);
			
//			if(!"0".equals(resultJson.get("status").toString())){
//				throw new PlatformException("同步错误：" + resultJson.get("desc"));
//			}
			if(!resultJson.getBooleanValue("data")){
				throw new PlatformException("同步错误：" + resultJson.get("resMsg"));
			}
		} catch(Exception ex) {
			logger.error(ex,ex);
			throw new PlatformException(ResponseEnum.HTTP_RESPONSE_ERROR,new String[]{syncUrl,ex.getMessage()});
		}
	}
	
	/**
	 * 同步员工信息(老平台)
	 * @param newEmployee
	 * @param oldEmployee
	 * @param optType
	 */
	public void userSyncOld(ComEmployee newEmployee,ComEmployee oldEmployee,String optType) {
		String syncUrl = "";
		try {
			syncUrl = sysParamDefineServiceImpl.getSysParamValueCache("organization", "sync.organization");
			Assert.notNullAndEmpty(syncUrl, ResponseEnum.FULL_MSG,"syncUrl缺少URL信息");
			syncUrl += "/App/rpc/orgAndUser/userSync";
			HttpHeaders headers =new HttpHeaders();
			MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(mediaType);
			
			ComOrganization oldOrganization = comOrganizationDaoImpl.get(oldEmployee.getOrgId());
			ComOrganization newOrganization = comOrganizationDaoImpl.get(newEmployee.getOrgId());
			
			JSONObject paramsJson = new JSONObject();
			paramsJson.put("userId", newEmployee.getUsercode());
			paramsJson.put("userName", newEmployee.getName());
			paramsJson.put("oldParentOrgCode", oldOrganization.getOrgCode());
			paramsJson.put("parentOrgCode", newOrganization.getOrgCode());
			if("t".equalsIgnoreCase(newEmployee.getInActive())){
				paramsJson.put("inActive", true);
			}else{
				paramsJson.put("inActive", false);
			}
			if(newEmployee.getEmail()!=null){
				paramsJson.put("email", newEmployee.getEmail());
			}
			paramsJson.put("loginPwd", newEmployee.getPassword());
			//老系统 0是新增 2是更新 进行转换
			if("1".equals(optType)){
				paramsJson.put("optType", "2");
			}else{
				paramsJson.put("optType", optType);
			}
			
			String uuid =UUID.randomUUID().toString().replaceAll("-", "");
			logger.info("userSyncOld输入参数"+uuid+"："+paramsJson);
			HttpEntity<String> request = new HttpEntity<String>(paramsJson.toString(), headers);
			ResponseEntity<String> response = restTemplate.postForEntity(syncUrl, request, String.class);
			try {
				logger.info("userSyncOld输出参数"+uuid+"："+JSONObject.parseObject(response.getBody()));
			} catch (Exception e) {
			}
			HttpStatus httpStatus = response.getStatusCode();
			String body = "";
			if (httpStatus == HttpStatus.OK) {
				body = response.getBody();
			} else {
				throw new PlatformException("状态码：" + httpStatus);
			}
			
			JSONObject resultJson = JSONObject.parseObject(body);
			
			if(!"0".equals(resultJson.get("status").toString())){
				throw new PlatformException("同步错误：" + resultJson.get("desc"));
			}
		} catch(Exception ex) {
			logger.error(ex,ex);
			throw new PlatformException(ResponseEnum.HTTP_RESPONSE_ERROR,new String[]{syncUrl,ex.getMessage()});
		}
	}
	
	/**
	 * 调用第三方接口同步营业网点信息
	 * @param param
	 * @return
	 */
	public void userSync(ComEmployee newEmployee,ComEmployee oldEmployee,boolean isOldCall,boolean isNewCall,String optType){
		if (isOldCall) {
			userSyncOld(newEmployee,oldEmployee,optType);
		}
		if (isNewCall) {
			userSyncNew(newEmployee,oldEmployee,optType);
		}
	}
}
