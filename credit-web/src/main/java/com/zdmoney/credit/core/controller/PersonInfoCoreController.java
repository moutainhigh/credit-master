package com.zdmoney.credit.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.service.pub.IPersonContactInfoService;
import com.zdmoney.credit.person.vo.PersonContactInfoVo;

/**
 * 借款人基础信息接口
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/core/personInfoCore")
public class PersonInfoCoreController extends BaseController {

	private static Log logger = LogFactory.getLog(BaseController.class);

	@Autowired
	IPersonContactInfoService personContactInfoServiceImpl;

	/**
	 * 维护借款人的联系人信息
	 * 
	 * @param personContactInfoVo
	 *            Vo值对象
	 * @param request
	 *            请求信息对象
	 * @param response
	 *            响应信息对象
	 * @throws Exception
	 */
	@RequestMapping("/modifyPersonContactInfo")
	@ResponseBody
	public String modifyPersonContactInfo(PersonContactInfoVo personContactInfoVo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AttachmentResponseInfo<PersonContactInfo> attachmentResponseInfo = null;
		try {
			Long id = personContactInfoVo.getId();
			if (Strings.isEmpty(id)) {
				/** 新增联系人 **/
			} else {
				/** 修改联系人 **/
			}
			personContactInfoServiceImpl.saveOrUpdate(personContactInfoVo);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(ResponseEnum.SYS_SUCCESS);
			attachmentResponseInfo.setAttachment(personContactInfoVo);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			attachmentResponseInfo = ex.toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<PersonContactInfo>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

}
