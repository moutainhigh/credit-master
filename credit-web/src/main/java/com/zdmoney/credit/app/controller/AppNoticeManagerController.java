package com.zdmoney.credit.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zdmoney.credit.common.constant.NoticeTypeEnum;
import com.zdmoney.credit.common.util.EnumUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.app.domain.AppNotice;
import com.zdmoney.credit.app.service.pub.IAppNoticeService;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;

@Controller
@RequestMapping(value = "/app/noticeManager")
public class AppNoticeManagerController extends BaseController {

	protected static Log logger = LogFactory.getLog(AppNoticeManagerController.class);

	@Autowired
	IAppNoticeService appNoticeServiceImpl;

	/**
	 * 跳转页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/jumpPage")
	public ModelAndView jumpPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("noticeTypes", EnumUtil.enumConvertToMap(NoticeTypeEnum.class, "", "value"));
		modelAndView.setViewName("app/appNoticeManager");
		return modelAndView;
	}

	/**
	 * 查询App公告
	 * 
	 * @param request
	 * @param response
	 * @param sendMobileCodeVo
	 * @return
	 */
	@RequestMapping(value = "/searchNotice")
	@ResponseBody
	public String searchNotice(HttpServletRequest request, HttpServletResponse response, int rows, int page) {
		ResponseInfo responseInfo = null;
		try {
			AppNotice appNotice = new AppNotice();
			Pager pager = new Pager();
			pager.setPage(page);
			pager.setRows(rows);
			pager.setSidx("CREATE_TIME");
			pager.setSort("DESC");
			appNotice.setPager(pager);
			pager = appNoticeServiceImpl.findWithPg(appNotice);
			return toPGJSONWithCode(pager);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			responseInfo = ex.toResponseInfo();
		} catch (Exception ex) {
			logger.error(ex, ex);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(responseInfo);
	}

	/**
	 * 加载公告信息
	 * 
	 * @param id
	 *            营业网点编号
	 * @return
	 */
	@RequestMapping("/loadNoticeInfo")
	@ResponseBody
	public String loadNoticeInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<AppNotice> attachmentResponseInfo = null;
		try {
			AppNotice appNotice = appNoticeServiceImpl.findById(id);
			if (Strings.isEmpty(appNotice)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "缺少数据");
			}
			attachmentResponseInfo = new AttachmentResponseInfo<AppNotice>(ResponseEnum.SYS_SUCCESS);
			attachmentResponseInfo.setAttachment(appNotice);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			attachmentResponseInfo = ex.toAttachmentResponseInfo();
		} catch (Exception ex) {
			/** 系统忙 **/
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<AppNotice>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 新增、修改公告数据
	 * 
	 * @param appNotice
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdateData")
	@ResponseBody
	public String saveOrUpdateData(AppNotice appNotice, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<AppNotice> attachmentResponseInfo = null;
		try {
			Long id = appNotice.getId();

			appNoticeServiceImpl.saveOrUpdate(appNotice);

			/** 正常返回 **/
			attachmentResponseInfo = new AttachmentResponseInfo<AppNotice>(ResponseEnum.SYS_SUCCESS);
			attachmentResponseInfo.setAttachment(appNotice);
		} catch (PlatformException ex) {
			ResponseEnum responseEnum = ex.getResponseCode();
			attachmentResponseInfo = ex.toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<AppNotice>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

}
