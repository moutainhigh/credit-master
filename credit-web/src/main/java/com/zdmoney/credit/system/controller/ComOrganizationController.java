package com.zdmoney.credit.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.tree.EasyUITree;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.inter.ComOrganizationInter;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 前端请求处理（营业网点模块）
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/system/comOrganization")
public class ComOrganizationController extends BaseController {

	protected static Log logger = LogFactory.getLog(ComOrganizationController.class);

	@Autowired
	@Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;

	@Autowired
	@Qualifier("sysParamDefineServiceImpl")
	ISysParamDefineService sysParamDefineServiceImpl;

	@Autowired
	@Qualifier("comOrganizationInter")
	ComOrganizationInter comOrganizationInter;

	/** 支持变更归属关系的层级 **/
	List<String> allowChangeLevel = Arrays.asList(new String[] { "V102", "V103", "V104" });

	/** 营业网点名称不能包含以下非法字符 **/
	char[] notAllowName = { '^', '$', '[', ']', '{', '}', '.', '?', '+', '*', '|', '\\' };

	/**
	 * 跳转到营业网点管理主页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jumpPage")
	public String jumpPage(HttpServletRequest request, HttpServletResponse response) {
		return "/system/comOrganizationManager";
	}

	/**
	 * 查询营业网点子级结点内容
	 * 
	 * @param parentId
	 *            父结点编号
	 * @return
	 */
	@RequestMapping("/loadChildNode/{parentId}")
	@ResponseBody
	public String loadChildNode(@PathVariable Long parentId, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<List<EasyUITree>> attachmentResponseInfo = new AttachmentResponseInfo<List<EasyUITree>>();
		try {
			/** 创建查询实体 **/
			ComOrganization comOrganization = new ComOrganization();
			comOrganization.setParentId(Strings.convertValue(parentId, String.class));
			List<ComOrganization> list = comOrganizationServiceImpl.findListByVo(comOrganization);
			List<EasyUITree> childNodes = new ArrayList<EasyUITree>();
			for (int i = 0; i < list.size(); i++) {
				comOrganization = list.get(i);
				String nodeId = Strings.convertValue(comOrganization.getId(), String.class);
				String nodeText = Strings.convertValue(comOrganization.getName(), String.class);
				EasyUITree node = new EasyUITree(nodeId, nodeText);
				node.getAttributes().put("vLevel", comOrganization.getvLevel());
				node.setIconCls("pic_4");
				childNodes.add(node);
			}
			attachmentResponseInfo = new AttachmentResponseInfo<List<EasyUITree>>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), Strings.convertValue(parentId, String.class));
			attachmentResponseInfo.setAttachment(childNodes);
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<List<EasyUITree>>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(parentId, String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 加载单个营业网点信息
	 * 
	 * @param id
	 *            营业网点编号
	 * @return
	 */
	@RequestMapping("/loadOrgData/{id}")
	@ResponseBody
	public String loadOrgData(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>();
		try {
			/** 创建查询实体 **/
			ComOrganization comOrganization = comOrganizationServiceImpl.get(id);
			if (comOrganization == null) {
				/** 从数据库加载实体为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.VALIDATE_ISNULL,
						"编号:" + id);
			} else {
				/** 正常返回 **/
				String level = comOrganization.getvLevel();

				attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS, "");
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("comOrganization", comOrganization);

				if (allowChangeLevel.contains(level)) {
					ComOrganizationEnum.Level levelEnum = ComOrganizationEnum.get(level).prevLevel();
					ComOrganization searchComOrganization = new ComOrganization();
					searchComOrganization.setvLevel(levelEnum.getName());

					List<ComOrganization> areaList = comOrganizationServiceImpl
							.searchWithFullName(searchComOrganization);
					result.put("areaList", areaList);
				}
				attachmentResponseInfo.setAttachment(result);
			}
		} catch (Exception ex) {
			/** 系统忙 **/
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(),
					"");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 新增、修改营业网点数据
	 * 
	 * @param comOrganization
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdateData")
	@ResponseBody
	public String saveOrUpdateData(ComOrganization comOrganization, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<ComOrganization> attachmentResponseInfo = null;
		try {
			Long id = comOrganization.getId();
			String name = comOrganization.getName();
			Assert.notNullAndEmpty(name, ResponseEnum.FULL_MSG, "缺少名称参数");

			if (StringUtils.containsAny(name, notAllowName)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "名称不能包含（<span style=\"color:red;\">"
						+ String.copyValueOf(notAllowName) + "</span>）字符");
			}
			if (Strings.isEmpty(id)) {

			} else {
				String level = comOrganization.getvLevel();
				if (allowChangeLevel.contains(level)) {
					String parentIdStr = request.getParameter("parentIds");
					comOrganization.setParentId(parentIdStr);
					Assert.notNullAndEmpty(parentIdStr, ResponseEnum.FULL_MSG, "缺少parentId参数!");
				}
			}
			
			if(comOrganization != null && "V104".equals(comOrganization.getvLevel())){
				Date openDate = comOrganization.getOpenDate();
				Date closeDate = comOrganization.getCloseDate();
				if(openDate != null && closeDate != null && closeDate.compareTo(openDate) == -1){
					throw new PlatformException(ResponseEnum.FULL_MSG, "停业日期不能小于开业日期");
				}
			}
			
			comOrganization = comOrganizationServiceImpl.saveOrUpdate(comOrganization);
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<ComOrganization>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setAttachment(comOrganization);
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<ComOrganization>(ResponseEnum.FULL_MSG.getCode(),ex.getMessage(),
					Strings.convertValue(comOrganization.getId(), String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 调用第三方接口同步营业网点信息
	 * 
	 * @param oldComOrganization
	 *            老网点信息
	 * @param comOrganization
	 *            新网点信息
	 * @param optType
	 *            操作类型：0：新增 1：删除 2：更新
	 * @return
	 */
	/*public JSONObject syncOrg(ComOrganization oldComOrganization, ComOrganization comOrganization, String optType) {
		*//** 以下代码调用第三方同步接口 **//*
		try {
			*//** 操作类型：0：新增 1：删除 2：更新 **//*
			// String optType = "";
			*//** 原机构代码 **//*
			String oldOrgCode = "";
			*//** 旧上级机构代码 **//*
			String oldParentOrgCode = "";
			*//** 机构代码 **//*
			String orgCode = "";
			*//** 上级机构代码 **//*
			String parentOrgCode = "";
			*//** 机构名称 **//*
			String orgName = "";
			*//**  **//*
			String remark = "";

			String level = comOrganization.getvLevel();

			oldOrgCode = comOrganization.getOrgCode();
			orgCode = comOrganization.getOrgCode();
			orgName = comOrganization.getName();

			switch (level) {
			case "V100":
				*//** 公司 **//*
				parentOrgCode = "root";
				break;
			case "V101":
				*//** 区域 **//*
			case "V102":
				*//** 分部 **//*
			case "V103":
				*//** 城市 **//*
			case "V104":
				*//** 分店、营业部 **//*
			case "V105":
				*//** 团队、组 **//*
				oldParentOrgCode = "";
				Long parentIda = Strings.convertValue(comOrganization.getParentId(), Long.class);
				parentOrgCode = comOrganizationServiceImpl.get(parentIda).getOrgCode();

				if (level.equalsIgnoreCase("V104")) {
					remark = Strings.convertValue(comOrganization.getDepLevel(), String.class);
				}

				if (allowChangeLevel.contains(level)) {
					if ("2".equalsIgnoreCase(optType)) {
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
				*//** level 数据异常 **//*
				break;
			}

			JSONObject orgSyncParam = new JSONObject();
			orgSyncParam.put("authKey", MD5Util.md5Hex("ymkj"));//接口密钥
//			orgSyncParam.put("oldOrgCode", oldOrgCode);
			orgSyncParam.put("id", comOrganization.getId());
			orgSyncParam.put("name", comOrganization.getName());//机构名称
//			orgSyncParam.put("oldParentOrgCode", oldParentOrgCode);
			orgSyncParam.put("parentOrgCode", parentOrgCode);
			orgSyncParam.put("parentId", comOrganization.getParentId());
			orgSyncParam.put("depLevel", remark);
			orgSyncParam.put("vLevel", level);
			orgSyncParam.put("optType", optType);//0: 新增,1: 更新,2:删除
			
			orgSyncParam = comOrganizationInter.orgSync(orgSyncParam);
			System.out.println(orgSyncParam);
			return orgSyncParam;
		} catch (Exception ex) {
			logger.error(ex, ex);
		}
		return null;
	}*/

	/**
	 * 删除营业网点数据
	 * 
	 * @param comOrganization
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteData/{id}")
	@ResponseBody
//	@PreAuthorize("hasRole('/system/comOrganization/deleteData')")
	@Secured({"/system/comOrganization/deleteData"})
	public String deleteData(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<ComOrganization> attachmentResponseInfo = null;
		try {
			ComOrganization comOrganization = comOrganizationServiceImpl.delete(id);

//			syncOrg(comOrganization, comOrganization, "2");

			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<ComOrganization>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setAttachment(comOrganization);
		}catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<ComOrganization>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(id, String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 借款查询 销售团队查询 张倩倩
	 */

	/**
	 * 加载单个营业网点信息
	 * 
	 * @param id
	 *            营业网点编号
	 * @return
	 */
	@RequestMapping("/comOrganizationLastList")
	@ResponseBody
	public String comOrganizationLastList(HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<List<ComOrganization>> attachmentResponseInfo = new AttachmentResponseInfo<List<ComOrganization>>();
		try {
			/** 创建查询实体 **/
			ComOrganization comOrganization = new ComOrganization();
			comOrganization.setvLevel("V104");
			List<ComOrganization> list = comOrganizationServiceImpl.findListByVo(comOrganization);
			if (list != null && list.size() > 0) {
				attachmentResponseInfo = new AttachmentResponseInfo<List<ComOrganization>>(
						ResponseEnum.SYS_SUCCESS.getCode(), ResponseEnum.SYS_SUCCESS.getDesc());
				attachmentResponseInfo.setAttachment(list);
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<List<ComOrganization>>(
						ResponseEnum.VALIDATE_ISNULL.getCode(), Strings.format(ResponseEnum.VALIDATE_ISNULL.getDesc()));
			}
		} catch (Exception ex) {
			/** 系统忙 **/
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<List<ComOrganization>>(
					ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc());
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 根据机构层级查询机构信息
	 * 
	 * @param employeeType
	 * @return
	 */
	@RequestMapping(value = "/findOrganizationByLevel", method = RequestMethod.POST)
	@ResponseBody
	public String findOrganizationByLevel(@RequestParam("vlevel") String vlevel,
			@RequestParam("showParentName") boolean showParentName) {
		AttachmentResponseInfo<Map<String, Object>> response = new AttachmentResponseInfo<Map<String, Object>>();
		Map<String, Object> data = new HashMap<String, Object>();
		response.setAttachment(data);
		try {
			Map<String, Object> serviceResult = comOrganizationServiceImpl.findOrganizationByLevel(vlevel,
					showParentName);
			if ((Boolean) serviceResult.get("success")) {
				data.putAll(serviceResult);
				response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
				response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			} else {
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				response.setResMsg(serviceResult.get("message") + "");
			}
		} catch (Exception e) {
			logger.error("path:/system/comOrganization/findOrganizationByLevel", e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg(" 根据机构层级查询机构信息失败");
		}

		return toResponseJSON(response);
	}

}
