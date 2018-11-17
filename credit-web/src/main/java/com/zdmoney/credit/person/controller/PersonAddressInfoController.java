package com.zdmoney.credit.person.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.ContactEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.person.domain.PersonAddressInfo;
import com.zdmoney.credit.person.service.pub.IPersonAddressInfoService;

/**
 * 地址变更记录Controller
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/personAddress")
public class PersonAddressInfoController extends BaseController {
	
	@Autowired @Qualifier("personAddressInfoServiceImpl")
	IPersonAddressInfoService personAddressInfoServiceImpl;
	
	
	/**
	 * 跳转到编辑页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jumpEditPage")
	public ModelAndView jumpPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/person/editAddressInfo");
		modelAndView.addObject("addressTypeEnum",ContactEnum.AddressType.values());
		modelAndView.addObject("priorityEnum",ContactEnum.Priority.values());
		return modelAndView;
	}
	
	/**
	 * 查询地址变更数据
	 * @param personInfo 前端查询条件实例
	 * @param rows 每页总行
	 * @param page 查询的页数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="search")
	@ResponseBody
	public String search(PersonAddressInfo personAddressInfo,int rows, int page,HttpServletRequest request,HttpServletResponse response) {
		/** 定义分页实例 **/
		try {
			Pager pager = new Pager();
			pager.setRows(rows);
			pager.setPage(page);
			pager.setSidx("ID");
			pager.setSort("ASC");
			personAddressInfo.setPager(pager);
			/** 调用Service层查询数据 **/
			pager = personAddressInfoServiceImpl.findWithPg(personAddressInfo);
			/** 将数据对象转换成JSON字符串，返回给前台 **/
			return toPGJSONWithCode(pager);
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),"");
			return toResponseJSON(attachmentResponseInfo);
		}
	}
	
	/**
	 * 查询地址详细数据
	 * @param id 地址编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="loadAddressData/{id}")
	@ResponseBody
	public String loadAddressData(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<PersonAddressInfo> attachmentResponseInfo = null;
		try {
			PersonAddressInfo personAddressInfo = personAddressInfoServiceImpl.findById(id);
			if (personAddressInfo == null) {
				/** 结果集为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<PersonAddressInfo>(ResponseEnum.VALIDATE_RESULT_ISNULL.getCode()
						,Strings.format(ResponseEnum.VALIDATE_RESULT_ISNULL.getDesc(), "编号:" + id),Strings.convertValue(id,String.class));
			} else {
				/** 正常返回 **/
				attachmentResponseInfo = new AttachmentResponseInfo<PersonAddressInfo>(ResponseEnum.SYS_SUCCESS.getCode()
						,ResponseEnum.SYS_SUCCESS.getDesc(),Strings.convertValue(id,String.class));
				attachmentResponseInfo.setAttachment(personAddressInfo);
			}
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonAddressInfo>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 新增、修改地址变更记录
	 * @param personAddressInfo 前端查询条件实例
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(PersonAddressInfo personAddressInfo,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<PersonAddressInfo> attachmentResponseInfo = null;
		Long id = personAddressInfo.getId();
		try {
			if (Strings.isEmpty(id)) {
				/** 新增 **/
				this.createLog(request, SysActionLogTypeEnum.新增, "客户查询", "新增地址信息");
			} else {
				/** 修改 **/
				this.createLog(request, SysActionLogTypeEnum.更新, "客户查询", "更新地址信息");
			}
			personAddressInfoServiceImpl.saveOrUpdate(personAddressInfo);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonAddressInfo>(ResponseEnum.SYS_SUCCESS.getCode()
					,ResponseEnum.SYS_SUCCESS.getDesc(),"");
			attachmentResponseInfo.setAttachment(personAddressInfo);
		} catch(PlatformException ex) {
			logger.error(ex,ex);
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<PersonAddressInfo>(responseEnum.getCode()
					,ex.getMessage(),Strings.convertValue(id,String.class));
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonAddressInfo>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
}