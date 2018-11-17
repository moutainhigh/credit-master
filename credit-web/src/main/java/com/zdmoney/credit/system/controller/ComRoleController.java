package com.zdmoney.credit.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.tree.EasyUITree;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ComRole;
import com.zdmoney.credit.system.domain.ComRoleHierarchy;
import com.zdmoney.credit.system.service.pub.IComEmployeeRoleService;
import com.zdmoney.credit.system.service.pub.IComRoleService;

/**
 * 前端请求处理（员工模块）
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/system/role")
public class ComRoleController extends BaseController {

	protected static Log logger = LogFactory
			.getLog(ComEmployeeController.class);

	@Autowired
	@Qualifier("comRoleServiceImpl")
	IComRoleService comRoleServiceImpl;
	@Autowired
	@Qualifier("comEmployeeRoleServiceImpl")
	IComEmployeeRoleService comEmployeeRoleServiceImpl;

	/**
	 * 默认查询所有的角色 分页
	 */
	@RequestMapping(value = "findRoleList")
	@ResponseBody
	public String findRoleList(ComRole comRole, int rows, int page,
			HttpServletRequest request, HttpServletResponse response) {
		// 定义分页实例
		Pager pager = new Pager();
		try {
			pager.setRows(rows);
			pager.setPage(page);
			pager.setSidx("ID");
			pager.setSort("ASC");
			comRole.setPager(pager);
			// 调用Service层查询数据
			pager = comRoleServiceImpl.findWithPg(comRole);
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
		}
		// 将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}

	/**
	 * 动态加载角色树
	 * 
	 * @param ComEmployee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "createRoleTree")
	@ResponseBody
	public String createRoleTree(ComRole comRole, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {

			EasyUITree tree = new EasyUITree("0", "根目录");
			List roleList = comRoleServiceImpl.findComRole(comRole);
			for (int i = 0; i < roleList.size(); i++) {
				ComRole cp = (ComRole) roleList.get(i);
				EasyUITree t = new EasyUITree(cp.getId().toString(),
						cp.getRoleName());
				tree.getChildren().add(t);
			}
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("正常");
			List a = new ArrayList<EasyUITree>();
			a.add(tree);
			attachmentResponseInfo.setAttachment(a);
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	
	
	/**
	 * 员工管理动态加载角色树
	 * 
	 * @param ComEmployee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "createRoleHTree")
	@ResponseBody
	public String createRoleHTree(ComRole comRole, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			//做角色下方时需要用的
			/*ComEmployeeRole comEmployeeRole=new ComEmployeeRole();
			User userInfo = UserContext.getUser();
			comEmployeeRole.setEmployeeId(userInfo.getId());*/
			/*String json=comEmployeeRoleServiceImpl.findComEmployeeRoleComRoleHierarchy(comEmployeeRole);
			String roleIDS[]=json.split(",");
			EasyUITree tree = new EasyUITree("0", "根目录");
			if(roleIDS!=null){
				for (int i = 0; i <roleIDS.length; i++) {
					if(!roleIDS[i].equals("")){
					ComRole cp = comRoleServiceImpl.get(Long.parseLong(roleIDS[i]));
					EasyUITree t = new EasyUITree(cp.getId().toString(),
							cp.getRoleName());
					tree.getChildren().add(t);
					}
				}
			}*/
			EasyUITree tree = new EasyUITree("0", "根目录");
					List roleList = comRoleServiceImpl.findComRole(comRole);
					for (int i = 0; i < roleList.size(); i++) {
						ComRole cp = (ComRole) roleList.get(i);
						EasyUITree t = new EasyUITree(cp.getId().toString(),
								cp.getRoleName());
						tree.getChildren().add(t);
					}
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("正常");
			List a = new ArrayList<EasyUITree>();
			a.add(tree);
			attachmentResponseInfo.setAttachment(a);
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	
	/**
	 * 新增 修改
	 * 
	 * @param myTest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdateDataRole")
	@ResponseBody
	public String saveOrUpdateDataRole(ComRole comRole,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			//判断是否存在
			ComRole role=comRoleServiceImpl.getIsThere(comRole);
			//不存在
			if(role==null)
			{
					comRoleServiceImpl.saveOrUpdate(comRole);
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(), "");
				attachmentResponseInfo.setResMsg("操作成功");
				
			}else
			{
				attachmentResponseInfo = new AttachmentResponseInfo<Object>(
						ResponseEnum.SYS_EXIST.getCode(),
						ResponseEnum.SYS_EXIST.getDesc(), "");
				attachmentResponseInfo.setResMsg("角色名称已经存在，请重新输入");
			}
				
			
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(
							comRole.getId(), String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 
	 * @param myTest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loadDataRole/{id}")
	@ResponseBody
	public String loadDataRole(@PathVariable Long id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<ComRole> attachmentResponseInfo = null;
		try {
			ComRole comRole = comRoleServiceImpl.get(id);
			if (comRole == null) {
				/** 数据项为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<ComRole>(
						ResponseEnum.VALIDATE_ISNULL.getCode(), Strings.format(
								ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:"
										+ id), Strings.convertValue(id,
								String.class));
			} else {
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<ComRole>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(),
						Strings.convertValue(id, String.class));
				attachmentResponseInfo.setAttachment(comRole);
			}

		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<ComRole>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(id,
							String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 *            记录主键编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteDataRole/{id}")
	@ResponseBody
	public String deleteDataRole(@PathVariable Long id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<ComRole> attachmentResponseInfo = null;
		try {
			ComRole comRole = comRoleServiceImpl.get(id);
			if (comRole == null) {
				/** 数据项为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<ComRole>(
						ResponseEnum.VALIDATE_ISNULL.getCode(), Strings.format(
								ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:"
										+ id), Strings.convertValue(id,
								String.class));
			} else {
				comRoleServiceImpl.deleteById(id);
				// 正常返回
				attachmentResponseInfo = new AttachmentResponseInfo<ComRole>(
						ResponseEnum.SYS_SUCCESS.getCode(),
						ResponseEnum.SYS_SUCCESS.getDesc(),
						Strings.convertValue(id, String.class));
				attachmentResponseInfo.setAttachment(comRole);
			}
		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<ComRole>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), Strings.convertValue(id,
							String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping("/rolelist")
	public String rolelist(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("员工列表");
		return "/role/role";

	}
	
	/**
	 * 根据角色id查询可分配角色信息
	 * 
	 * @param ComEmployee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "findComRoleHierarchyByRoleId")
	@ResponseBody
	public String findComRoleHierarchyByRoleId(ComRoleHierarchy comRoleHierarchy,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;
		try {
			String roleId=request.getParameter("roleId").toString();
			comRoleHierarchy.setRoleId(Long.parseLong(roleId));
			String json=comRoleServiceImpl.findRoleHierachy(comRoleHierarchy);
			this.createLog(request, SysActionLogTypeEnum.查询, "克分配角色", "根据角色id查询克分配角色");
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("正常");
			attachmentResponseInfo.setAttachment(json);

		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 新增 修改
	 * 
	 * @param myTest
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "saveComRoleHierarchy")
	@ResponseBody
	public String saveComRoleHierarchy(ComRoleHierarchy comRoleHierarchy,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;
		
		String pid=request.getParameter("pid").toString();
		try {
			comRoleServiceImpl.deleteComRoleHierarchy(pid,comRoleHierarchy.getRoleId());
			this.createLog(request, SysActionLogTypeEnum.删除, "插入角色", "插入角色");
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("修改成功");
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return toResponseJSON(attachmentResponseInfo);
	}
}