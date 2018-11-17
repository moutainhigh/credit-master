package com.zdmoney.credit.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;

@Controller
@RequestMapping("/system/SysDictionary")
public class SysDictionaryController extends BaseController {

	@Autowired
	@Qualifier("sysDictionaryServiceImpl")
	ISysDictionaryService sysDictionaryServiceImpl;

	@RequestMapping(value = "findSysDictionaryList")
	@ResponseBody
	public String findSysDictionaryList(SysDictionary sysDictionary, int rows,
			int page, HttpServletRequest request, HttpServletResponse response) {
		// 定义分页实例
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("ASC");
		sysDictionary.setPager(pager);
		Object o = request.getParameter("parentId");
		sysDictionary.setFlage("f");
		long parentId = 0l;
		if (o != null&&!"".equals(o)) {
			parentId = Long.parseLong(request.getParameter("parentId")
					.toString());
			sysDictionary.setParentId(parentId);
			sysDictionary.setFlage("t");
		}
		
		
		
		// 调用Service层查询数据
		pager = sysDictionaryServiceImpl.findWithPg(sysDictionary);
		this.createLog(request, SysActionLogTypeEnum.查询, "数据字典", "数据字典列表");
		// 将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}

	
	
	@RequestMapping(value = "saveOrUpdateData")
	@ResponseBody
	public String saveOrUpdateData(SysDictionary sysDictionary,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<SysDictionary> attachmentResponseInfo = null;
		try {
			SysDictionary sys=null;
			// 正常返回
			// 根据父级查询codeName codeTitle
			 sys = sysDictionaryServiceImpl.getIsThere(sysDictionary);
			if (sys == null) {
				SysDictionary s=new SysDictionary();
				s.setId(sysDictionary.getParentId());
				 s=sysDictionaryServiceImpl.findListByVo(s).get(0);
				sysDictionary.setCodeType(s.getCodeType());
				sysDictionary.setCodeTypeTitle(s.getCodeTypeTitle());
				sysDictionaryServiceImpl.saveOrUpdate(sysDictionary);
				attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setResMsg("操作成功");
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
						ResponseEnum.SYS_EXIST.getCode(),
						ResponseEnum.SYS_EXIST.getDesc(), Strings.convertValue(
								sysDictionary.getId(), String.class));
			}
			this.createLog(request, SysActionLogTypeEnum.更新, "数据字典", "数据字典更新或者修改");
		} catch (PlatformException ex) {
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					responseEnum.getCode(), ex.getMessage(),
					Strings.convertValue(sysDictionary.getId(), String.class));
		} catch (Exception ex) {
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(
							sysDictionary.getId(), String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	
	//父新增修改
	@RequestMapping(value = "saveOrUpdateDataAndSysD")
	@ResponseBody
	public String saveOrUpdateDataAndSysD(SysDictionary sysDictionary,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<SysDictionary> attachmentResponseInfo = null;
		try {
			SysDictionary sys=null;
			// 正常返回
			// 根据父级查询codeName codeTitle
			 sys = sysDictionaryServiceImpl.getIsThere(sysDictionary);
			if (sys == null) {
			//	sysDictionary.setCodeName(sysDictionary.getCodeType());
				 //sysDictionary.setCodeTitle(sysDictionary.getCodeTypeTitle());
				sysDictionaryServiceImpl.saveOrUpdate(sysDictionary);
				attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setResMsg("操作成功");
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
						ResponseEnum.SYS_EXIST.getCode(),
						ResponseEnum.SYS_EXIST.getDesc(), Strings.convertValue(
								sysDictionary.getId(), String.class));
			}
			this.createLog(request, SysActionLogTypeEnum.更新, "数据字典", "数据字典更新或者修改");
		} catch (PlatformException ex) {
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					responseEnum.getCode(), ex.getMessage(),
					Strings.convertValue(sysDictionary.getId(), String.class));
		} catch (Exception ex) {
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(
							sysDictionary.getId(), String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	/**
	 * 删除
	 * 
	 * @param comOrganization
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteData/{id}")
	@ResponseBody
	public String deleteData(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<SysDictionary> attachmentResponseInfo = null;
		try {
			sysDictionaryServiceImpl.deleteById(id);
			this.createLog(request, SysActionLogTypeEnum.删除, "数据字典", "数据字典 删除");
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("操作成功");
		} catch (PlatformException ex) {
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					responseEnum.getCode(), ex.getMessage(),
					Strings.convertValue(id, String.class));
		} catch (Exception ex) {
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(id,
							String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 查询所有部门
	 * 
	 * @param sysDictionary
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "findParent")
	@ResponseBody
	public String findParent(SysDictionary sysDictionary,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<List<Map<String, String>>> attachmentResponseInfo = null;
		try {
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			List<SysDictionary> list = sysDictionaryServiceImpl.findParent();
			for (SysDictionary obj : list) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("codeName", obj.getCodeName().toString());
				map.put("codeTitle", obj.getCodeTitle());
				result.add(map);
			}
			attachmentResponseInfo = new AttachmentResponseInfo<List<Map<String, String>>>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setAttachment(result);
		} catch (PlatformException ex) {
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<List<Map<String, String>>>(
					responseEnum.getCode(), ex.getMessage(),
					Strings.convertValue(sysDictionary.getId(), String.class));
		} catch (Exception ex) {
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<List<Map<String, String>>>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(
							sysDictionary.getId(), String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 根据id查询数据
	 */
	/**
	 * 
	 * @param myTest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loadDataSysDictionary/{id}")
	@ResponseBody
	public String loadDataSysDictionary(@PathVariable Long id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<SysDictionary> attachmentResponseInfo = null;
		try {
			SysDictionary sysDictionary = sysDictionaryServiceImpl.get(id);
			if (sysDictionary == null) {
				/** 数据项为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
						ResponseEnum.VALIDATE_ISNULL.getCode(), Strings.format(
								ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:"
										+ id), Strings.convertValue(id,
								String.class));
			} else {
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(),
						Strings.convertValue(id, String.class));
				attachmentResponseInfo.setAttachment(sysDictionary);
			}

		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<SysDictionary>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(id,
							String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping("/SysDictionaryList")
	public String SysDictionaryList(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("数据字典列表");
		return "/sysDictionary/sysDictionary";

	}

	/**
	 * 中间方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/findSysDictionaryListBy")
	public String findSysDictionaryListBy(HttpServletRequest request,
			HttpServletResponse response) {
		Object o = request.getParameter("parentId");
		long parentId = 0l;
		if (o != null) {
			parentId = Long.parseLong(request.getParameter("parentId")
					.toString());
			request.setAttribute("parentId", parentId);
		}
		System.out.println("数据字典");
		return "/sysDictionary/sysDictionaryZY";

	}
	
	
}
