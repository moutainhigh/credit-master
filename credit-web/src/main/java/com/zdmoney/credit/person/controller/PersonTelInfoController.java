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
import com.zdmoney.credit.person.domain.PersonTelInfo;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;

/**
 * 电话变更记录Controller
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/personTel")
public class PersonTelInfoController extends BaseController {
	
	@Autowired @Qualifier("personTelInfoServiceImpl")
	IPersonTelInfoService personTelInfoServiceImpl;
	
	/**
	 * 跳转到编辑页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jumpEditPage")
	public ModelAndView jumpPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/person/editTelInfo");
		modelAndView.addObject("telTypeEnum",ContactEnum.TelType.values());
		modelAndView.addObject("priorityEnum",ContactEnum.Priority.values());
		return modelAndView;
	}
	
	/**
	 * 查询电话变更数据
	 * @param personInfo 前端查询条件实例
	 * @param rows 每页总行
	 * @param page 查询的页数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="search")
	@ResponseBody
	public String search(PersonTelInfo personTelInfo,int rows, int page,HttpServletRequest request,HttpServletResponse response) {
		/** 定义分页实例 **/
		try {
			if (Strings.isEmpty(personTelInfo.getObjectId()) || Strings.isEmpty(personTelInfo.getClassName())) {
				throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"ObjectId 或 ClassName"});
			}
			Pager pager = new Pager();
			pager.setRows(rows);
			pager.setPage(page);
			pager.setSidx("ID");
			pager.setSort("ASC");
			personTelInfo.setPager(pager);
			/** 调用Service层查询数据 **/
			pager = personTelInfoServiceImpl.findWithPg(personTelInfo);
			/** 将数据对象转换成JSON字符串，返回给前台 **/
			return toPGJSONWithCode(pager);
		} catch(PlatformException ex) {
			logger.error(ex,ex);
			ResponseEnum responseEnum = ex.getResponseCode();
			AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>(responseEnum.getCode()
					,ex.getMessage(),"");
			return toResponseJSON(attachmentResponseInfo);
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),"");
			return toResponseJSON(attachmentResponseInfo);
		}
	}
	
	/**
	 * 查询电话详细数据
	 * @param id 电话编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="loadTelData/{id}")
	@ResponseBody
	public String loadTelData(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<PersonTelInfo> attachmentResponseInfo = null;
		try {
			PersonTelInfo personTelInfo = personTelInfoServiceImpl.findById(id);
			if (personTelInfo == null) {
				/** 结果集为空 **/
				attachmentResponseInfo = new AttachmentResponseInfo<PersonTelInfo>(ResponseEnum.VALIDATE_RESULT_ISNULL.getCode()
						,Strings.format(ResponseEnum.VALIDATE_RESULT_ISNULL.getDesc(), "编号:" + id),Strings.convertValue(id,String.class));
			} else {
				/** 正常返回 **/
				attachmentResponseInfo = new AttachmentResponseInfo<PersonTelInfo>(ResponseEnum.SYS_SUCCESS.getCode()
						,ResponseEnum.SYS_SUCCESS.getDesc(),Strings.convertValue(id,String.class));
				attachmentResponseInfo.setAttachment(personTelInfo);
			}
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonTelInfo>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 新增、修改电话变更记录
	 * @param personTelInfo 前端查询条件实例
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(PersonTelInfo personTelInfo,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<PersonTelInfo> attachmentResponseInfo = null;
		Long id = personTelInfo.getId();
		try {
			if (Strings.isEmpty(id)) {
				/** 新增 **/
				this.createLog(request, SysActionLogTypeEnum.新增, "客户查询", "新增电话信息");
			} else {
				/** 修改 **/
				this.createLog(request, SysActionLogTypeEnum.更新, "客户查询", "更新电话信息");
			}
			personTelInfoServiceImpl.saveOrUpdate(personTelInfo);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonTelInfo>(ResponseEnum.SYS_SUCCESS.getCode()
					,ResponseEnum.SYS_SUCCESS.getDesc(),"");
			attachmentResponseInfo.setAttachment(personTelInfo);
		} catch(PlatformException ex) {
			logger.error(ex,ex);
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<PersonTelInfo>(responseEnum.getCode()
					,ex.getMessage(),Strings.convertValue(id,String.class));
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonTelInfo>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
}