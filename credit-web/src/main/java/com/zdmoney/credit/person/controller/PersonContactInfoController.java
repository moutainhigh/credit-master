package com.zdmoney.credit.person.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.service.pub.IPersonContactInfoService;

/**
 * 客户联系人请求处理
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/personContact")
public class PersonContactInfoController extends BaseController {
	
	@Autowired @Qualifier("personContactInfoServiceImpl")
	IPersonContactInfoService personContactInfoServiceImpl;
	@Autowired 
	ILoanTransferInfoService loanTransferInfoServiceImpl;
	
	/**
	 * 查看联系人信息(页面跳转)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/viewContactInfo/{id}")
	public ModelAndView viewContactInfo(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) {
		/** 查询客户名下所有联系人数据 **/
		List<PersonContactInfo> contacts = new ArrayList<PersonContactInfo>();
		if (Strings.isNotEmpty(id)) {
			PersonContactInfo personContactInfo = new PersonContactInfo();
			personContactInfo.setPersonId(id);
			contacts = personContactInfoServiceImpl.findListByVo(personContactInfo);
		}
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("/person/personDetail_contact");
		modelAndView.addObject("contacts", contacts);
		return modelAndView;
	}
	
	
	
//	/**
//	 * 跳转到客户查询主页面
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping("/viewPersonDetailPage")
//	public String viewPersonDetailPage(HttpServletRequest request, HttpServletResponse response) {
//		return "/person/personDetail.jsp";
//	}
//	
	/**
	 * 查询联系人数据
	 * @param personId 客户编号
	 * @param rows 每页总行
	 * @param page 查询的页数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="search/{personId}")
	@ResponseBody
	public String search(@PathVariable Long personId,int rows, int page,HttpServletRequest request,HttpServletResponse response) {
		PersonContactInfo personContactInfo = new PersonContactInfo();
		personContactInfo.setPersonId(personId);
		
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("ASC");
		personContactInfo.setPager(pager);
		//调用Service层查询数据
		pager = personContactInfoServiceImpl.findWithPg(personContactInfo);
		//将数据对象转换成JSON字符串，返回给前台
		return toPGJSONWithCode(pager);
	}
	
	/**
	 * 查询联系人数据
	 * @param id 联系人编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="loadContactData/{id}")
	@ResponseBody
	public String loadContactData(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<PersonContactInfo> attachmentResponseInfo = null;
		try {
			PersonContactInfo personContactInfo = personContactInfoServiceImpl.findById(id);
			if (personContactInfo == null) {
				attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(ResponseEnum.VALIDATE_ISNULL.getCode()
						,Strings.format(ResponseEnum.VALIDATE_ISNULL.getDesc(), "编号:" + id),Strings.convertValue(id,String.class));
			} else {
				attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(ResponseEnum.SYS_SUCCESS.getCode()
						,ResponseEnum.SYS_SUCCESS.getDesc(),Strings.convertValue(id,String.class));
				attachmentResponseInfo.setAttachment(personContactInfo);
			}
		} catch(Exception ex) {
			/** 系统忙 **/
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),Strings.convertValue(id,String.class));
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
	/**
	 * 新增、修改联系人信息
	 * @param personContactInfo 联系人信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="saveOrUpdateData")
	@ResponseBody
	public String saveOrUpdateData(PersonContactInfo personContactInfo,HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<PersonContactInfo> attachmentResponseInfo = null;
		try {
			//检查该借款人是否有已转让过的债权
			Long personId = personContactInfo.getPersonId();
			boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(personId,null);
			if(!flag){
				throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
			}
			Long id = personContactInfo.getId();
			if (Strings.isEmpty(id)) {
				/** 新增联系人 **/
				this.createLog(request, SysActionLogTypeEnum.新增, "客户查询", "新增联系人");
			} else {
				/** 修改联系人 **/
				this.createLog(request, SysActionLogTypeEnum.更新, "客户查询", "更新联系人");
			}
			personContactInfoServiceImpl.saveOrUpdate(personContactInfo);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(ResponseEnum.SYS_SUCCESS.getCode()
					,ResponseEnum.SYS_SUCCESS.getDesc(),"");
			attachmentResponseInfo.setAttachment(personContactInfo);
		} catch(PlatformException ex) {
			logger.error(ex,ex);
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(responseEnum.getCode()
					,ex.getMessage(),"");
		} catch(Exception ex) {
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(ResponseEnum.SYS_FAILD.getCode()
					,ResponseEnum.SYS_FAILD.getDesc(),"");
		}
		return toResponseJSON(attachmentResponseInfo);
	}
	
}