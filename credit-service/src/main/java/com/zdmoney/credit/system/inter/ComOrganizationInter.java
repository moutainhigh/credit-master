package com.zdmoney.credit.system.inter;

import java.util.Arrays;
import java.util.List;
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
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class ComOrganizationInter {
	
	protected static Log logger = LogFactory.getLog(ComOrganizationInter.class);
	
	@Autowired
    RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("sysParamDefineServiceImpl")
	ISysParamDefineService sysParamDefineServiceImpl;
	
	@Autowired
	@Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;
	
	@Value("${synchronize.aps.key}")
	public String authKey;
	
	/** 支持变更归属关系的层级 **/
	List<String> allowChangeLevel = Arrays.asList(new String[] { "V102", "V103", "V104" });
	/**第三方地址**/
	String syncOrgUrl = "";
	/**
	 * 调用第三方接口同步营业网点信息
	 * @param oldComOrganization 
	 */
	public void syncOrg(ComOrganization comOrganization, ComOrganization oldComOrganization, String optType) {
		syncOrgOld(comOrganization,oldComOrganization,optType);
		syncOrgNew(comOrganization,  optType);
	}	
	/**
	 * 调用第三方接口同步营业网点信息(老系统)
	 * @param oldComOrganization
	 * @param comOrganization
	 * @param comOrganization2 
	 * @param optType
	 * @return
	 */
	public void syncOrgOld(ComOrganization comOrganization, ComOrganization oldComOrganization, String optType) {
		/** 以下代码调用第三方同步接口 **/
		try {
			/** 操作类型：0: 新增,1: 更新,2:删除 **/
			// String optType = "";
			/** 原机构代码 **/
			String oldOrgCode = "";
			/** 旧上级机构代码 **/
			String oldParentOrgCode = "";
			/** 机构代码 **/
			String orgCode = "";
			/** 上级机构代码 **/
			String parentOrgCode = "";
			/** 机构名称 **/
			String orgName = "";
			/**  **/
			String remark = "";

			String level = comOrganization.getvLevel();

			oldOrgCode = comOrganization.getOrgCode();
			orgCode = comOrganization.getOrgCode();
			orgName = comOrganization.getName();
//			ComOrganization oldComOrganization = null;

			switch (level) {
			case "V100":
				/** 公司 **/
				parentOrgCode = "root";
				break;
			case "V101":
				/** 区域 **/
			case "V102":
				/** 分部 **/
			case "V103":
				/** 城市 **/
			case "V104":
				/** 分店、营业部 **/
			case "V105":
				/** 团队、组 **/
				oldParentOrgCode = "";
				Long parentIda = Strings.convertValue(comOrganization.getParentId(), Long.class);
				parentOrgCode = comOrganizationServiceImpl.get(parentIda).getOrgCode();

				if (level.equalsIgnoreCase("V104")) {
					remark = Strings.convertValue(comOrganization.getDepLevel(), String.class);
				}
//				oldComOrganization=comOrganizationServiceImpl.get(comOrganization.getId());
				if (allowChangeLevel.contains(level)) {
					if ("1".equalsIgnoreCase(optType)) {
						oldOrgCode = oldComOrganization.getOrgCode();
						Long parentId12 = Strings.convertValue(oldComOrganization.getParentId(), Long.class);
						oldParentOrgCode = comOrganizationServiceImpl.get(parentId12).getOrgCode();
					} else {
						oldOrgCode = comOrganization.getOrgCode();
						oldParentOrgCode = parentOrgCode;
					}
				}
				break;
			default:
				/** level 数据异常 **/
				break;
			}

			JSONObject orgSyncParam = new JSONObject();
			orgSyncParam.put("oldOrgCode", oldOrgCode);
			orgSyncParam.put("orgCode", orgCode);
			orgSyncParam.put("orgName", orgName);
			orgSyncParam.put("oldParentOrgCode", oldParentOrgCode);
			orgSyncParam.put("parentOrgCode", parentOrgCode);
			orgSyncParam.put("remark", remark);
			//老系统 操作类型：0：新增 1：删除 2：更新 进行转换 
			if("1".equals(optType)){
				orgSyncParam.put("optType", "2");
			}else if("2".equals(optType)){
				orgSyncParam.put("optType", "1");
			}else{
				orgSyncParam.put("optType", optType);				
			}
			orgSyncParam = orgSyncOld(orgSyncParam);
			System.out.println(orgSyncParam);
		} catch (Exception ex) {
			logger.error(ex, ex);
			throw new PlatformException(ResponseEnum.HTTP_RESPONSE_ERROR,new String[]{syncOrgUrl,ex.getMessage()});
		}
	}
	/**
	 * 调用第三方接口同步营业网点信息(新系统)
	 * @param oldComOrganization
	 * @param comOrganization
	 * @param optType
	 * @return
	 */
	public void syncOrgNew(ComOrganization comOrganization, String optType) {
		/** 以下代码调用第三方同步接口 **/
		try {
			/** 机构名称 **/
			String orgName = "";
			/** 网点评级**/
			String remark = "";

			String level = comOrganization.getvLevel();

			orgName = comOrganization.getName();

			switch (level) {
			case "V100":
				/** 公司 **/
				break;
			case "V101":
				/** 区域 **/
			case "V102":
				/** 分部 **/
			case "V103":
				/** 城市 **/
			case "V104":
				/** 分店、营业部 **/
			case "V105":
				/** 团队、组 **/
				if (level.equalsIgnoreCase("V104")) {
					remark = Strings.convertValue(comOrganization.getDepLevel(), String.class);
				}
				break;
			default:
				/** level 数据异常 **/
				break;
			}

			JSONObject orgSyncParam = new JSONObject();
			orgSyncParam.put("authKey", MD5Util.md5Hex(authKey));//接口密钥
			orgSyncParam.put("id", comOrganization.getId());
			orgSyncParam.put("name", orgName);//机构名称
			orgSyncParam.put("parentId", comOrganization.getParentId());
			orgSyncParam.put("depLevel", remark);
			orgSyncParam.put("vLevel", level);
			orgSyncParam.put("orgCode", comOrganization.getOrgCode());
			orgSyncParam.put("optType", optType);//0: 新增,1: 更新,2:删除
			
			orgSyncParam = orgSyncNew(orgSyncParam);
			System.out.println(orgSyncParam);
			if(!orgSyncParam.getBooleanValue("data")){
				throw new PlatformException("同步错误：" + orgSyncParam.get("resMsg"));
			}
		} catch (Exception ex) {
			logger.error(ex, ex);
			throw new PlatformException(ResponseEnum.HTTP_RESPONSE_ERROR,new String[]{syncOrgUrl,ex.getMessage()});
		}
	}
	/**
	 * 调用第三方接口同步营业网点信息(新系统)
	 * @param param
	 * @return
	 */
	public JSONObject orgSyncNew(JSONObject jsonObj){		
		try {
			syncOrgUrl = sysParamDefineServiceImpl.getSysParamValueCache("organization", "sync.organization.new");
			Assert.notNullAndEmpty(syncOrgUrl, ResponseEnum.FULL_MSG,"orgSync缺少URL信息");
			syncOrgUrl += "/api/saveOrUpdateOrg";
			HttpHeaders headers =new HttpHeaders();
			MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(mediaType);
			String uuid =UUID.randomUUID().toString().replaceAll("-", "");
			logger.info("orgSync输入参数"+uuid+"："+jsonObj);
			HttpEntity<String> request = new HttpEntity<String>(jsonObj.toString(), headers);
			ResponseEntity<String> response = restTemplate.postForEntity(syncOrgUrl, request, String.class);
			try {
				logger.info("orgSync输出参数"+uuid+"："+JSONObject.parseObject(response.getBody()));
			} catch (Exception e) {
			}
			HttpStatus httpStatus = response.getStatusCode();
			String body = "";
			if (httpStatus == HttpStatus.OK) {
				body = response.getBody();
			} else {
				throw new PlatformException("状态码：" + httpStatus);
			}
			return JSONObject.parseObject(body);
		} catch(Exception ex) {
			logger.error(ex,ex);
			throw new PlatformException(ResponseEnum.HTTP_RESPONSE_ERROR,new String[]{syncOrgUrl,ex.getMessage()});
		}
	}
	/**
	 * 调用第三方接口同步营业网点信息(老系统)
	 * @param param
	 * @return
	 */
	public JSONObject orgSyncOld(JSONObject jsonObj){
		String syncOrgUrl = "";
		try {
			syncOrgUrl = sysParamDefineServiceImpl.getSysParamValueCache("organization", "sync.organization");
			Assert.notNullAndEmpty(syncOrgUrl, ResponseEnum.FULL_MSG,"orgSync缺少URL信息");
			syncOrgUrl += "/App/rpc/orgAndUser/orgSync";
			HttpHeaders headers =new HttpHeaders();
			MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
			headers.setContentType(mediaType);
			String uuid =UUID.randomUUID().toString().replaceAll("-", "");
			logger.info("orgSync输入参数"+uuid+"："+jsonObj);
			HttpEntity<String> request = new HttpEntity<String>(jsonObj.toString(), headers);
			ResponseEntity<String> response = restTemplate.postForEntity(syncOrgUrl, request, String.class);
			try {
				logger.info("orgSync输出参数"+uuid+"："+JSONObject.parseObject(response.getBody()));
			} catch (Exception e) {
			}
			HttpStatus httpStatus = response.getStatusCode();
			String body = "";
			if (httpStatus == HttpStatus.OK) {
				body = response.getBody();
			} else {
				throw new PlatformException("状态码：" + httpStatus);
			}
			return JSONObject.parseObject(body);
		} catch(Exception ex) {
			logger.error(ex,ex);
			throw new PlatformException(ResponseEnum.HTTP_RESPONSE_ERROR,new String[]{syncOrgUrl,ex.getMessage()});
		}
	}
}
