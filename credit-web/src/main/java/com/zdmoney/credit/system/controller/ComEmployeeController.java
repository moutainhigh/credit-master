package com.zdmoney.credit.system.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.SystemUtil;
import com.zdmoney.credit.common.util.UrlUtil;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.ComRole;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.IComEmployeePermissionService;
import com.zdmoney.credit.system.service.pub.IComEmployeeRoleService;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.IComPermissionService;
import com.zdmoney.credit.system.service.pub.IComRoleService;

/**
 * 前端请求处理（员工模块）
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/system/user")
public class ComEmployeeController extends BaseController {

	protected static Log logger = LogFactory.getLog(ComEmployeeController.class);

	@Autowired
	@Qualifier("comEmployeeServiceImpl")
	IComEmployeeService comEmployeeServiceImpl;

	@Autowired
	@Qualifier("comPermissionServiceImpl")
	IComPermissionService comPermissionServiceImpl;

	@Autowired
	@Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;

	@Autowired
	IBaseMessageService baseMessageServiceImpl;

	@Autowired
	IComRoleService comRoleServiceImpl;

	@Autowired
	IComEmployeePermissionService comEmployeePermissionServiceImpl;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	@Qualifier("comEmployeeRoleServiceImpl")
	IComEmployeeRoleService comEmployeeRoleServiceImpl;

	/**
	 * 忘记密码操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/forgetPwd", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String forgetPwd(ComEmployee comEmployee, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			if (comEmployee == null || Strings.isEmpty(comEmployee.getUsercode())) {
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.VALIDATE_ISNULL.getCode(),
						Strings.format(ResponseEnum.VALIDATE_ISNULL.getDesc(), "工号"), "");
				return toResponseJSON(attachmentResponseInfo);
			}
			String userCode = comEmployee.getUsercode();

			ComEmployee user = comEmployeeServiceImpl.findByUserCode(userCode);

			if (null == user) {
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(
						ResponseEnum.EMPLOYEE_PASSWORD_WRONG.getCode(), ResponseEnum.EMPLOYEE_PASSWORD_WRONG.getDesc());
				return toResponseJSON(attachmentResponseInfo);
			}

			String email = user.getEmail();
			if (Strings.isEmpty(email)) {
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.EMAIL_NOT_EXIST.getCode(),
						ResponseEnum.EMAIL_NOT_EXIST.getDesc());
				return toResponseJSON(attachmentResponseInfo);
			}
			comEmployeeServiceImpl.forgetPwd(user);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc());
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping("/resetPwd")
	@ResponseBody
	public String resetPwd(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("userCode") String userCode, @RequestParam("token") String token,
			@RequestParam("password1") String password1, @RequestParam("password2") String password2) {

		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			if (Strings.isEmpty(userCode) || Strings.isEmpty(token) || Strings.isEmpty(password1)
					|| Strings.isEmpty(password2)) {
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.VALIDATE_ISNULL.getCode(),
						Strings.format(ResponseEnum.VALIDATE_ISNULL.getDesc(), "工号，token或密码"), "");
				return toResponseJSON(attachmentResponseInfo);
			}

			ComEmployee user = comEmployeeServiceImpl.findByUserCode(userCode);

			if (null == user) {
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(
						ResponseEnum.EMPLOYEE_PASSWORD_WRONG.getCode(), ResponseEnum.EMPLOYEE_PASSWORD_WRONG.getDesc());
				return toResponseJSON(attachmentResponseInfo);
			}

			if (!password1.equals(password2)) {
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(
						ResponseEnum.EMPLOYEE_PASSWORD_NOT_EQ.getCode(),
						ResponseEnum.EMPLOYEE_PASSWORD_NOT_EQ.getDesc());
				return toResponseJSON(attachmentResponseInfo);
			}

			if (!IsPassword(password1)) {
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(
						ResponseEnum.EMPLOYEE_ILLEGAL_PWD.getCode(), ResponseEnum.EMPLOYEE_ILLEGAL_PWD.getDesc());
				return toResponseJSON(attachmentResponseInfo);
			}

			String ip = SystemUtil.getIpAddr(request);
			comEmployeeServiceImpl.resetPwd(user, password1, token, ip);

			// comEmployeeServiceImpl.forgetPwd(user);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc());
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode(),
					ex.getMessage(), "");
		}
		return toResponseJSON(attachmentResponseInfo);

	}

	/**
	 * @param comEmployee
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	@RequestMapping(value = "search")
	@ResponseBody
	public String search(ComEmployee comEmployee, int rows, int page, HttpServletRequest request,
			HttpServletResponse response) {
		/** 员工姓名 **/
		String name = Strings.convertValue(request.getParameter("name"), String.class);
		/** 员工工号 **/
		String usercode = Strings.convertValue(request.getParameter("usercode"), String.class);
		/** 登陆者信息 **/
		User user = UserContext.getUser(); 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgCode", user.getOrgCode());
		params.put("name", name);
		params.put("usercode", usercode);
		// 定义分页实例
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("ASC");
//		comEmployee.setPager(pager);
		params.put("pager", pager);
		// 调用Service层查询数据
		pager = comEmployeeServiceImpl.findWithPgByMap(params);
		// List<ComEmployee> empList = pager.getResultList();
		/*
		 * for (ComEmployee com : empList) {
		 * comEmployee=(ComEmployee)empList.get(i); String organName =
		 * comOrganizationServiceImpl.searchOrgFullName(comEmployee.getOrgId());
		 * comEmployee.setMemo(organName); }
		 */
		this.createLog(request, SysActionLogTypeEnum.查询, "员工管理", "员工信息查询列表");
		// 将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "insertComEmployee")
	@ResponseBody
	public String insertComEmployee(ComEmployee comEmployee, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;
		try {
			ComEmployee c = null;
			String orgId = request.getParameter("orgId");
			if (orgId != null && !"".equals(orgId)) {
				ComOrganization org = comOrganizationServiceImpl.get(Long.parseLong(orgId));
				comEmployee.setOrgId(Long.parseLong(orgId));
			}
			// 是否在职转译
			if (comEmployee.getInActive().equals("t")) {
				comEmployee.setInActive("t");
			} else if (comEmployee.getInActive().equals("f")) {
				comEmployee.setInActive("f");
				comEmployee.setLeaveOfficeDate(new Date());
			}
			// 根据usercode查询。判断员工号是否存在
			ComEmployee emp = comEmployeeServiceImpl.findComEmployeeByUser(comEmployee);
			if (emp == null) {
				c = comEmployeeServiceImpl.saveOrUpdate(comEmployee);
				this.createLog(request, SysActionLogTypeEnum.新增, "员工管理", "员工信息新增");
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setResMsg("操作成功");
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
						ResponseEnum.SYS_FAILD.getDesc(), "");
				attachmentResponseInfo.setResMsg("员工号存在");
			}
		} catch (Exception ex) {
			// 系统忙
			logger.error("path:" + UrlUtil.getUri(), ex);
			attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
			attachmentResponseInfo.setResMsg("操作失败");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping(value = "updateEmployee")
	@ResponseBody
	public String updateEmployee(ComEmployee comEmployee, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;
		try {
			String id = request.getParameter("id");
			comEmployee.setId(Long.parseLong(id));
			// comEmployee.setInActive("f");
			ComEmployee c = comEmployeeServiceImpl.saveOrUpdate(comEmployee);
			this.createLog(request, SysActionLogTypeEnum.更新, "员工管理", "员工信息修改");
			if (c != null) {
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setResMsg("修改成功");
			} else {
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
						ResponseEnum.SYS_FAILD.getDesc(), "");
				attachmentResponseInfo.setResMsg("修改失败");
			}
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping(value = "findComEmployeeById")
	@ResponseBody
	public String findComEmployeeById(ComEmployee comEmployee, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<ComEmployee> attachmentResponseInfo = null;
		try {
			String id = request.getParameter("id");
			ComEmployee c = comEmployeeServiceImpl.get(Long.parseLong(id));
			this.createLog(request, SysActionLogTypeEnum.查询, "员工管理", "员工信息查询个人信息");
			String orgName = comOrganizationServiceImpl.searchOrgFullName(c.getOrgId());
			c.setMemo(orgName);
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<ComEmployee>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("修改成功");
			attachmentResponseInfo.setAttachment(c);
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<ComEmployee>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping("/employeelist")
	public String employeeList(HttpServletRequest request, HttpServletResponse response) {
		return "/employee/employee";
	}

	@RequestMapping("/modifyPassword")
	public String modifyPassword(HttpServletRequest request, HttpServletResponse response) {
		return "/employee/modifyPassWord";
	}

	@RequestMapping("/savePassword")
	@ResponseBody
	public String savePassword(@RequestParam("userCode") String userCode, @RequestParam("passWord") String passWord,
			@RequestParam("newPassWord") String newPassWord, @RequestParam("newPassWordAgain") String newPassWordAgain,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("员工:" + userCode + "列表修改密码");
		AttachmentResponseInfo<String> attachmentResponseInfo = null;
		User user = UserContext.getUser();
		if (Strings.isNotEmpty(user)) {
			if (user.getUserCode().equals(userCode) && user.getPassword().equals(MD5Util.md5Hex(passWord))) {
				if (!IsPassword(newPassWord)) {
					attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
							ResponseEnum.SYS_FAILD.getDesc(), "");
					attachmentResponseInfo.setResMsg("新系统密码为6位-10位，必须是字母和数字的组合");
					return toResponseJSON(attachmentResponseInfo);
				}
				if (!newPassWord.equals(newPassWordAgain)) {
					attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
							ResponseEnum.SYS_FAILD.getDesc(), "");
					attachmentResponseInfo.setResMsg("两次输入的密码不一致");
					return toResponseJSON(attachmentResponseInfo);
				}
				ComEmployee comEmployee = new ComEmployee();
				comEmployee.setId(user.getId());
				comEmployee.setPassword(newPassWord);
				comEmployee = comEmployeeServiceImpl.saveOrUpdate(comEmployee);
				attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setResMsg("更新密码成功");
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
						ResponseEnum.SYS_FAILD.getDesc(), "");
				attachmentResponseInfo.setResMsg("旧密码或者登陆账号错误");
				return toResponseJSON(attachmentResponseInfo);
			}
		} else {
			attachmentResponseInfo = new AttachmentResponseInfo<String>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 验证输入密码条件(字符与数据同时出现)
	 *
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串 , 返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPassword(String str) {
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";
		return match(regex, str);
	}

	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 登出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/loginOut")
	@ResponseBody
	public String loginOut(HttpServletRequest request, HttpServletResponse response) {
		UserContext.clear();
		AttachmentResponseInfo<String> attachmentResponseInfo = new AttachmentResponseInfo<>(
				ResponseEnum.SYS_SUCCESS.getCode(), ResponseEnum.SYS_SUCCESS.getDesc(), "");
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 根据员工类型查询员工信息
	 * 
	 * @param employeeType
	 * @return
	 */
	@RequestMapping(value = "/findEmployeeByEmployeeType", method = RequestMethod.POST)
	@ResponseBody
	public String findEmployeeByEmployeeType(@RequestParam("employeeType") String employeeType) {
		AttachmentResponseInfo<List<Map<String, Object>>> response = new AttachmentResponseInfo<List<Map<String, Object>>>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		response.setAttachment(result);
		try {
			Map<String, Object> serviceResult = comEmployeeServiceImpl.findEmployeeByEmployeeType(employeeType);
			if ((Boolean) serviceResult.get("success")) {
				List<Map<String, Object>> employeeList = (List<Map<String, Object>>) serviceResult.get("employeeList");
				for (int i = 0; i < employeeList.size(); i++) {
					Map<String, Object> map = employeeList.get(i);
					/** 客户经理编号 **/
					String id = Strings.convertValue(map.get("id"), String.class);
					/** 客户经理姓名 **/
					String name = Strings.convertValue(map.get("name"), String.class);
					/** 客户经理工号 **/
					String userCode = Strings.convertValue(map.get("userCode"), String.class);
					if (Strings.isNotEmpty(id) && Strings.isNotEmpty(name) && Strings.isNotEmpty(userCode)) {
						map.clear();
						map.put("id", id);
						map.put("name", name + "(" + userCode + ")");
						result.add(map);
					}
				}
				// data.putAll(serviceResult);
				response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
				response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			} else {
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				response.setResMsg(serviceResult.get("message") + "");
			}
		} catch (Exception e) {
			logger.error("path:/system/user/findEmployeeByEmployeeType", e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("根据员工类型查询员工信息失败");
		}

		return toResponseJSON(response);
	}

	/**
	 * 根据员工类型、姓名、工号 查询员工信息 (带分页查询)
	 * 
	 * @param employeeType
	 * @return
	 */
	@RequestMapping(value = "/findEmployeeByEmployeeTypeDataGrid")
	@ResponseBody
	public String findEmployeeByEmployeeTypeDataGrid(int rows, int page, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<List<Map<String, Object>>> attachmentResponseInfo = null;
		/** 员工类型 **/
		String employeeType = Strings.convertValue(request.getParameter("employeeType"), String.class);
		/** 员工姓名 **/
		String name = Strings.convertValue(request.getParameter("name"), String.class);
		/** 员工工号 **/
		String userCode = Strings.convertValue(request.getParameter("userCode"), String.class);
		/** 是否在职（查询条件） t:在职 f:不在职 **/
		String inActive = Strings.convertValue(request.getParameter("inActive"), String.class);

		/** 登陆者信息 **/
		User user = UserContext.getUser();
		try {
//			com.zdmoney.credit.common.util.Assert.notNullAndEmpty(employeeType, "员工类型");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orgCode", user.getOrgCode());
			params.put("name", name);
			params.put("userCode", userCode);
			params.put("inActive", inActive);
			/** 员工类型数组 **/
			if (Strings.isNotEmpty(employeeType)) {
				String[] employeeTypes = employeeType.split(",");
				params.put("employeeTypes", employeeTypes);
			}

			Pager pager = new Pager();
			pager.setPage(page);
			pager.setRows(rows);
			pager.setSidx("A.ID");
			pager.setSort("ASC");
			params.put("pager", pager);

			pager = comEmployeeServiceImpl.findEmployeeWithPg(params);

			return toPGJSONWithCode(pager);
		} catch (PlatformException ex) {
			attachmentResponseInfo = ex.<List<Map<String, Object>>> toAttachmentResponseInfo();
		} catch (Exception ex) {
			attachmentResponseInfo = new AttachmentResponseInfo<List<Map<String, Object>>>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 根据员工类型、姓名、工号 查询员工信息 (带分页查询)
	 * 
	 * @param employeeType
	 * @return
	 */
	@RequestMapping(value = "/findEmployeeByEmployeeTypeNotCheckDataGrid")
	@ResponseBody
	public String findEmployeeByEmployeeTypeNotCheckDataGrid(int rows, int page, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<List<Map<String, Object>>> attachmentResponseInfo = null;
		/** 员工类型 **/
		String employeeType = Strings.convertValue(request.getParameter("employeeType"), String.class);
		/** 员工姓名 **/
		String name = Strings.convertValue(request.getParameter("name"), String.class);
		/** 员工工号 **/
		String userCode = Strings.convertValue(request.getParameter("userCode"), String.class);
		/** 是否在职（查询条件） t:在职 f:不在职 **/
		String inActive = Strings.convertValue(request.getParameter("inActive"), String.class);

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", name);
			params.put("userCode", userCode);
			params.put("inActive", inActive);
			/** 员工类型数组 **/
			if (Strings.isNotEmpty(employeeType)) {
				String[] employeeTypes = employeeType.split(",");
				params.put("employeeTypes", employeeTypes);
			}

			Pager pager = new Pager();
			pager.setPage(page);
			pager.setRows(rows);
			pager.setSidx("A.ID");
			pager.setSort("ASC");
			params.put("pager", pager);

			pager = comEmployeeServiceImpl.findEmployeeWithPg(params);

			return toPGJSONWithCode(pager);
		} catch (PlatformException ex) {
			attachmentResponseInfo = ex.<List<Map<String, Object>>> toAttachmentResponseInfo();
		} catch (Exception ex) {
			attachmentResponseInfo = new AttachmentResponseInfo<List<Map<String, Object>>>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 入职批量导入
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/entryImportEmp")
	@ResponseBody
	public void entryImportEmp(@RequestParam(value = "entryfile") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream outputStream = null;
		ResponseInfo responseInfo = null;
		Workbook workbook = null;
		// 导入文件名
		String fileName = file.getOriginalFilename();

		try {
			this.createLog(request, SysActionLogTypeEnum.导入, "用户管理", "入职批量导入");

			// 文件校验
			UploadFile uploadFile = new UploadFile();
			uploadFile.setFile(file);
			uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
			uploadFile.setFileMaxSize(1024 * 1024 * 10);
			UploadFileUtil.valid(uploadFile);
			// 创建excel工作簿
			workbook = WorkbookFactory.create(file.getInputStream());
			// 创建导入数据模板
			ExcelTemplet excelTemplet = new ExcelTemplet().new entryImportInputExcel();
			// 文件数据转换为List集合
			List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
			// 校验和保存导入数据
			this.validateAndSaveData(sheetDataList);
			// 在excel文件的每行末尾追加单元格提示信息
			ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType(file.getContentType());
			outputStream = response.getOutputStream();
			workbook.write(outputStream);
			outputStream.flush();
			return;
		} catch (Exception e) {
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
					workbook = null;
				} catch (IOException e) {
				}
			}
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(toResponseJSON(responseInfo));
		} catch (IOException e) {
		}
	}

	/**
	 * 校验和保存导入数据
	 * 
	 * @param sheetDataList
	 */
	private void validateAndSaveData(List<Map<String, String>> sheetDataList) {

		for (Map<String, String> map : sheetDataList) {
			try {

				String userCode = map.get("userCode").toString().trim();// 工号
				String name = map.get("name").toString().trim();// 姓名
				// String areaName = map.get("areaName").toString().trim();//区域
				// String cityName = map.get("cityName").toString().trim();//分部
				String workName = map.get("workName").toString().trim();// 营业部
				String tempgroup = map.get("group").toString().trim();// 小组
				String jobType = map.get("job").toString().trim();// 岗位
				String mobile = map.get("mobile").toString().trim();// 手机号
				String email = map.get("email").toString().trim();// 邮箱
				/*
				 * String reg="^00.*"; if (!userCode.matches(reg)) {
				 * map.put(ExcelTemplet.FEED_BACK_MSG,"工号格式有误,以00开头!");
				 * continue; }
				 */

				// 小组：不得以阿拉伯数字填写，如“1组”、“2”等；

				String tempstr = "";
				if (tempgroup != null && tempgroup.length() > 0 && !tempgroup.equals("null")) {
					tempstr = tempgroup.substring(0, tempgroup.lastIndexOf("组"));
				}
				if (isNumeric(tempstr)) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "小组不得以阿拉伯数字填写");
					continue;
				}
				// 业务主任和客户经理必须要有组别
				if (jobType.equals("业务主任") || jobType.equals("客户经理")) {
					if (tempgroup == null || "".equals(tempgroup) || tempgroup.equals("null")) {
						map.put(ExcelTemplet.FEED_BACK_MSG, "小组不得为空");
						continue;
					}
				}
				// 去除所有的空格
				String tempUserCode = removeSpace(userCode);
				if (tempUserCode.equals("null") || tempUserCode.equals("")) {
					map.put("remark", "工号不得为空!");
					continue;
				}
				/*
				 * String reg="^00[0-9]$"; if (!userCode.matches(reg)) {
				 * map.put(ExcelTemplet.FEED_BACK_MSG,"工号格式有误,以00开头!");
				 * continue; }
				 */
				ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
				// “岗位信息”，不得随意填写
				List<String> listJobs = getJobTypes();
				boolean bResult = false;
				for (String object : listJobs) {
					if (object.equals(jobType)) {
						bResult = true;
						break;
					}

				}
				if (!bResult) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "岗位信息不存在!");
					continue;
				}
				if (workName == null || workName.equals("")) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "营业部信息不能为空!");
					continue;
				}
				// “营业部信息”中的名单，名称不得随意填写
				ComOrganization comOrganization = getSalesDepartment(workName);

				if (comOrganization == null) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "营业部信息不存在!");
					continue;
				}

				if (comEmployee != null) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "该工号已存在!");
					continue;
				} else {
					comEmployee = new ComEmployee();
					ComOrganization baseArea = null;

					// 员工所属区域 = 营业部/小组
					// 查询营业部下面所有的销售团队 如：深圳车公庙营业部/一组
					ComOrganization salesDepartmentInstance = getSalesDepartment(workName);
					List<ComOrganization> salesTeams = findAllBySalesDepartment(salesDepartmentInstance.getId());
					boolean salResult = false;
					String baseAreaId = "";
					if (tempgroup != null && !tempgroup.equals("")) {
						for (ComOrganization salesTeam : salesTeams) {
							if (salesTeam.getName().equals(tempgroup)) {
								salResult = true;
								baseAreaId = salesTeam.getId() + "";
								break;
							}
						}
						baseArea = comOrganizationServiceImpl.get(Long.parseLong(baseAreaId));
					} else {
						salResult = true;
						baseArea = getSalesDepartment(workName);
					}
					if (!salResult) {
						map.put(ExcelTemplet.FEED_BACK_MSG, "组别不存在!");
						continue;
					}
					comEmployee.setName(name);
					comEmployee.setUsercode(userCode);
					String pwd = "zd123456";
					comEmployee.setPassword(MD5Util.md5Hex(pwd));
					comEmployee.setInActive("t");
					comEmployee.setOrgId(baseArea.getId());
					if (email.equals("null") || email.equals("")) {
						comEmployee.setEmail("1@1.com");
					}else {
						comEmployee.setEmail(email);
					}
					comEmployee.setMobile(mobile);
					ComRole comRole = new ComRole();
					// 根据岗位jobType来判断
					if (jobType.equals("客户经理")) {
						comEmployee.setEmployeeType("业务员");
						comRole.setRoleName("信贷-营业部-客户经理");
						comRole = comRoleServiceImpl.get(comRole);
					} else if (jobType.equals("客服专员")) {
						comEmployee.setEmployeeType("客服");
						comRole.setRoleName("信贷-营业部-客服");
						comRole = comRoleServiceImpl.get(comRole);
					} else if (jobType.equals("业务主任")) {
						comEmployee.setEmployeeType("管理人员");
						comRole.setRoleName("信贷-营业部-业务主任");
						comRole = comRoleServiceImpl.get(comRole);
					} else if (jobType.equals("营业部经理")) {
						comEmployee.setEmployeeType("管理人员");
						comRole.setRoleName("信贷-营业部-经理");
						comRole = comRoleServiceImpl.get(comRole);
					}else if ( jobType.equals("营业部副经理")) {
						comEmployee.setEmployeeType("管理人员");
						comRole.setRoleName("信贷-营业部-副理");
						comRole = comRoleServiceImpl.get(comRole);
					}
					// 先插入用户信息
					comEmployeeServiceImpl.saveEmpAndRole(comEmployee, comRole.getId());
					map.put(ExcelTemplet.FEED_BACK_MSG, "录入成功!");
				}

			} catch (Exception e) {
				map.put(ExcelTemplet.FEED_BACK_MSG, "录入失败!");
			}
		}
	}

	/**
	 * 离职批量导入
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/quitImportEmp")
	@ResponseBody
	public void quitImportEmp(@RequestParam(value = "quitfile") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream outputStream = null;
		ResponseInfo responseInfo = null;
		Workbook workbook = null;
		// 导入文件名
		String fileName = file.getOriginalFilename();

		try {
			this.createLog(request, SysActionLogTypeEnum.导入, "用户管理", "入职批量导入");
			// 文件校验
			UploadFile uploadFile = new UploadFile();
			uploadFile.setFile(file);
			uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
			uploadFile.setFileMaxSize(1024 * 1024 * 10);
			UploadFileUtil.valid(uploadFile);
			// 创建excel工作簿
			workbook = WorkbookFactory.create(file.getInputStream());
			// 创建导入数据模板
			ExcelTemplet excelTemplet = new ExcelTemplet().new quitImportInputExcel();
			// 文件数据转换为List集合
			List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
			// 校验和保存导入数据
			this.validateQuitAndSaveData(sheetDataList);
			// 在excel文件的每行末尾追加单元格提示信息
			ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType(file.getContentType());
			outputStream = response.getOutputStream();
			workbook.write(outputStream);
			outputStream.flush();
			return;
		} catch (Exception e) {
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
					workbook = null;
				} catch (IOException e) {
				}
			}
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(toResponseJSON(responseInfo));
		} catch (IOException e) {
		}
	}

	/**
	 * 异动批量导入
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/ydImportEmp")
	@ResponseBody
	public void ydImportEmp(@RequestParam(value = "ydfile") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream outputStream = null;
		ResponseInfo responseInfo = null;
		Workbook workbook = null;
		// 导入文件名
		String fileName = file.getOriginalFilename();

		try {
			this.createLog(request, SysActionLogTypeEnum.导入, "用户管理", "入职批量导入");
			// 文件校验
			UploadFile uploadFile = new UploadFile();
			uploadFile.setFile(file);
			uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
			uploadFile.setFileMaxSize(1024 * 1024 * 10);
			UploadFileUtil.valid(uploadFile);
			// 创建excel工作簿
			workbook = WorkbookFactory.create(file.getInputStream());
			// 创建导入数据模板
			ExcelTemplet excelTemplet = new ExcelTemplet().new quitImportInputExcel();
			// 文件数据转换为List集合
			List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
			// 校验和保存导入数据
			this.validateYdAndSaveData(sheetDataList);
			// 在excel文件的每行末尾追加单元格提示信息
			ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType(file.getContentType());
			outputStream = response.getOutputStream();
			workbook.write(outputStream);
			outputStream.flush();
			return;
		} catch (Exception e) {
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
					workbook = null;
				} catch (IOException e) {
				}
			}
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(toResponseJSON(responseInfo));
		} catch (IOException e) {
		}
	}

	/**
	 * 校验和保存导入数据
	 * 
	 * @param sheetDataList
	 */
	private void validateQuitAndSaveData(List<Map<String, String>> sheetDataList) {
		ComRole comRole = new ComRole();
		for (Map<String, String> map : sheetDataList) {
			try {

				String userCode = map.get("userCode").toString().trim();// 工号
				String name = map.get("name").toString().trim();// 姓名
				String workName = map.get("workName").toString().trim();// 营业部
				String tempgroup = map.get("group").toString().trim();// 小组
				String jobType = map.get("job").toString().trim();// 岗位
				String quitTime = map.get("quitTime").toString().trim();// 岗位
				String reg = "^00.*";
				if (!userCode.matches(reg)) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "工号格式有误,以00开头!");
					continue;
				}

				if (quitTime == null) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "生效日期不得为空!");
					continue;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				Date efdate = sdf.parse(quitTime);
				if (efdate.compareTo(date) > 0) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "生效日期不能大于当前日期!");
					continue;
				}
				String tempstr = "";
				if (tempgroup != null && tempgroup.length() > 0 && !tempgroup.equals("null")) {
					tempstr = tempgroup.substring(0, tempgroup.lastIndexOf("组"));
				}
				if (isNumeric(tempstr)) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "小组不得以阿拉伯数字填写");
					continue;
				}
				// 业务主任和客户经理必须要有组别
				if (jobType.equals("业务主任") || jobType.equals("客户经理")) {
					if (tempgroup == null || "".equals(tempgroup) || tempgroup.equals("null")) {
						map.put(ExcelTemplet.FEED_BACK_MSG, "小组不得为空");
						continue;
					}
				} else {
					tempgroup = "";
				}
				// 去除所有的空格
				String tempUserCode = removeSpace(userCode);
				if (tempUserCode.equals("null") || tempUserCode.equals("")) {
					map.put("remark", "工号不得为空!");
					continue;
				}

				ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
				if (comEmployee.getInActive().equals("f")) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "该员工已经离职");
					continue;
				}
				comEmployee.setUpdateTime(sdf.parse(quitTime));
				comEmployee.setLeaveOfficeDate(sdf.parse(quitTime));
				comEmployee.setInActive("f");
				comEmployeeServiceImpl.saveEmpAndRole(comEmployee, 0l);
				map.put(ExcelTemplet.FEED_BACK_MSG, "操作成功!");
			} catch (Exception e) {
				logger.info("导入离职员工异常", e);
				map.put(ExcelTemplet.FEED_BACK_MSG, "操作失败!");
			}
		}
	}

	/**
	 * 校验和保存导入数据
	 * 
	 * @param sheetDataList
	 */
	private void validateYdAndSaveData(List<Map<String, String>> sheetDataList) {
		for (Map<String, String> map : sheetDataList) {
			try {
				String userCode = map.get("userCode").toString().trim();// 工号
				String name = map.get("name").toString().trim();// 姓名
				String workName = map.get("workName").toString().trim();// 营业部
				String tempgroup = map.get("group").toString().trim();// 小组
				String jobType = map.get("job").toString().trim();// 岗位
				String quitTime = map.get("quitTime").toString().trim();// 岗位
				/*
				 * String reg="^00.*"; if (!userCode.matches(reg)) {
				 * map.put(ExcelTemplet.FEED_BACK_MSG,"工号格式有误,以00开头!");
				 * continue; }
				 */
				if (quitTime == null) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "生效日期不得为空!");
					continue;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				Date efdate = sdf.parse(quitTime);
				if (efdate.compareTo(date) > 0) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "生效日期不能大于当前日期!");
					continue;
				}
				String tempstr = "";
				if (tempgroup != null && tempgroup.length() > 0 && !tempgroup.equals("null")) {
					tempstr = tempgroup.substring(0, tempgroup.lastIndexOf("组"));
				}
				if (isNumeric(tempstr)) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "小组不得以阿拉伯数字填写");
					continue;
				}
				// 业务主任和客户经理必须要有组别
				if (jobType.equals("业务主任") || jobType.equals("客户经理")) {
					if (tempgroup == null || "".equals(tempgroup) || tempgroup.equals("null")) {
						map.put(ExcelTemplet.FEED_BACK_MSG, "小组不得为空");
						continue;
					}
				}
				// 去除所有的空格
				String tempUserCode = removeSpace(userCode);
				if (tempUserCode.equals("null") || tempUserCode.equals("")) {
					map.put("remark", "工号不得为空!");
					continue;
				}
				if (workName == null || workName.equals("")) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "营业部信息不能为空!");
					continue;
				}
				/*
				 * String reg="^00[0-9]$"; if (!userCode.matches(reg)) {
				 * map.put(ExcelTemplet.FEED_BACK_MSG,"工号格式有误,以00开头!");
				 * continue; }
				 */
				ComOrganization baseArea = null;
				// 员工所属区域 = 营业部/小组
				// 查询营业部下面所有的销售团队 如：深圳车公庙营业部/一组
				ComOrganization salesDepartmentInstance = getSalesDepartment(workName);
				List<ComOrganization> salesTeams = findAllBySalesDepartment(salesDepartmentInstance.getId());
				boolean salResult = false;
				String baseAreaId = "";
				if (tempgroup != null && !tempgroup.equals("")) {
					for (ComOrganization salesTeam : salesTeams) {
						if (salesTeam.getName().equals(tempgroup)) {
							salResult = true;
							baseAreaId = salesTeam.getId() + "";
							break;
						}
					}
					baseArea = comOrganizationServiceImpl.get(Long.parseLong(baseAreaId));
				} else {
					salResult = true;
					baseArea = getSalesDepartment(workName);
				}
				if (!salResult) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "组别不存在!");
					continue;
				}
				ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
				if (comEmployee == null) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "该员工不存在");
					continue;
				}
				if (comEmployee.getInActive().equals("f")) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "该员工已经离职");
					continue;
				}
				comEmployee.setUpdateTime(sdf.parse(quitTime));
				comEmployee.setOrgId(baseArea.getId());
				ComRole comRole = new ComRole();
				boolean flag = false;
				// 修改用户信息
				// 根据岗位jobType来判断
				if (jobType.equals("客户经理")) {
					comEmployee.setEmployeeType("业务员");
					comRole.setRoleName("信贷-营业部-客户经理");
					comRole = comRoleServiceImpl.get(comRole);
					flag = true;
				} else if (jobType.equals("客服专员")) {
					comEmployee.setEmployeeType("客服");
					comRole.setRoleName("信贷-营业部-客服");
					comRole = comRoleServiceImpl.get(comRole);
					flag = true;
				} else if (jobType.equals("业务主任")) {
					comEmployee.setEmployeeType("管理人员");
					comRole.setRoleName("信贷-营业部-业务主任");
					comRole = comRoleServiceImpl.get(comRole);
					flag = true;
				} else if (jobType.equals("营业部经理")) {
					comEmployee.setEmployeeType("管理人员");
					comRole.setRoleName("信贷-营业部-经理");
					comRole = comRoleServiceImpl.get(comRole);
					flag = true;
				} else if ( jobType.equals("营业部副经理")) {
					comEmployee.setEmployeeType("管理人员");
					comRole.setRoleName("信贷-营业部-副理");
					comRole = comRoleServiceImpl.get(comRole);
					flag = true;
				}
				if (!flag) {
					map.put(ExcelTemplet.FEED_BACK_MSG, "权限添加失败!");
					continue;
				}
				comEmployeeServiceImpl.saveEmpAndRole(comEmployee, comRole.getId());
				map.put(ExcelTemplet.FEED_BACK_MSG, "操作成功!");

			} catch (Exception e) {
				logger.error(e.getMessage());
				map.put(ExcelTemplet.FEED_BACK_MSG, "操作失败!");
			}
		}
	}

	/**
	 * 判断字符串是否是阿拉伯数字 用正则表达式
	 * 
	 * @author zhangln
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		boolean flag = false;
		if (str != null && str != "") {
			Pattern pattern = Pattern.compile("[0-9]*");
			flag = pattern.matcher(str).matches();
		}
		return flag;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<String> getJobTypes() {
		List list = new ArrayList();
		list.add("客户经理");
		list.add("业务主任");
		list.add("客服专员");
		list.add("营业部经理");
		list.add("营业部副经理");
		return list;
	}

	/**
	 * 根据excel中的营业部查询是否存在
	 * 
	 * @param workName
	 *            营业部
	 * @return
	 */
	private ComOrganization getSalesDepartment(String workName) {
		ComOrganization salesDepartment = new ComOrganization();
		salesDepartment.setName(workName);
		List<ComOrganization> list = comOrganizationServiceImpl.findListByVo(salesDepartment);
		if (list != null && list.size() > 0) {
			salesDepartment = list.get(0);
		}
		return salesDepartment;
	}

	/**
	 * 根据excel中的营业部查询是否存在
	 * 
	 * @param workName
	 *            营业部
	 * @return
	 */
	private List<ComOrganization> findAllBySalesDepartment(Long id) {
		ComOrganization salesDepartment = new ComOrganization();
		salesDepartment.setParentId(id + "");
		List<ComOrganization> list = comOrganizationServiceImpl.findListByVo(salesDepartment);
		return list;
	}

	/**
	 * 去掉空格
	 * 
	 * @author zhangln
	 */
	private String removeSpace(String str) {
		if (str != null && str != "") {
			// 先去掉前后空格
			str = str.trim();
			// 去除中间空格
			str = str.replace(" ", "");
		}
		return str;
	}
}