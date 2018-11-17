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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.tree.EasyUITree;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComPermission;
import com.zdmoney.credit.system.service.pub.IComEmployeeRoleService;
import com.zdmoney.credit.system.service.pub.IComPermissionService;
import com.zdmoney.credit.system.service.pub.IComRolePermissionService;

/**
 * 前端请求处理（员工模块）
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/system/comPermission")
public class ComPermissionController extends BaseController {

	protected static Log logger = LogFactory
			.getLog(ComEmployeeController.class);

	
	@Autowired
	@Qualifier("comPermissionServiceImpl")
	IComPermissionService comPermissionServiceImpl;
	@Autowired
	@Qualifier("comRolePermissionServiceImpl")
	IComRolePermissionService comRolePermissionServiceImpl;
	@Autowired
	@Qualifier("comEmployeeRoleServiceImpl")
	IComEmployeeRoleService comEmployeeRoleServiceImpl;

	/**
	 * 动态加载树
	 * 
	 * @param ComEmployee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "createMuneTree")
	@ResponseBody
	public String createMuneTree(ComEmployee comEmployee,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			//ComEmployeeRole comEmployeeRole=new ComEmployeeRole();
			//comEmployeeRole.setEmployeeId(Long.parseLong(request.getParameter("empId").toString()));
			//comEmployeeRole.setEmployeeId(Long.parseLong("5"));
		//	String json=comEmployeeRoleServiceImpl.findComEmployeeRole(comEmployeeRole);
			//String[] roles=json.split(",");
			EasyUITree tree = new EasyUITree("0", "根目录");
			tree = isThere(tree, 0l);
			/*List treeList = tree.getChildren();
			ComRolePermission comRolePermission=new ComRolePermission();
			if(roles.length >0){
				for (int i = 0; i < roles.length; i++) {
					comRolePermission.setRoleId(Long.parseLong(roles[i]));
					String permission=comRolePermissionServiceImpl.findPermissionRole(comRolePermission);
					String[] permissionList=permission.split(",");
					
				}
			}*/
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
	 * 递归查询是否存在下一级,存在就装入
	 */
	public EasyUITree isThere(EasyUITree tree, Long parentId) {
		ComPermission comPermission = new ComPermission();
		comPermission.setParentId(parentId);
		List comPermissionList = comPermissionServiceImpl
				.findComPermission(comPermission);
		if(comPermissionList==null||comPermissionList.size()<=0)
		{
			tree.setChildren(null);
		}
		for (int i = 0; i < comPermissionList.size(); i++) {
			ComPermission cp = (ComPermission) comPermissionList.get(i);
			EasyUITree t = new EasyUITree(cp.getId().toString(),
					cp.getPermName());
			tree.getChildren().add(t);
			if (cp.getParentId() != null) {
				//System.out.println("-------------------id"+cp.getId());
				isThere(t, cp.getId());
			}
		}
		return tree;
	}

}