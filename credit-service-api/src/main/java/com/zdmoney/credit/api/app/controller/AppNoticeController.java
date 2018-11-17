package com.zdmoney.credit.api.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zdmoney.credit.common.constant.NoticeTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.app.domain.AppNotice;
import com.zdmoney.credit.app.service.pub.IAppNoticeService;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.json.FastJsonUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;

@Controller
@RequestMapping(value = "/app/notice")
public class AppNoticeController {

	protected static Log logger = LogFactory.getLog(AppNoticeController.class);

	@Autowired
	IAppNoticeService appNoticeServiceImpl;

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
	public String searchNotice(HttpServletRequest request, HttpServletResponse response, int pagerMax, int pagerNum) {
		AttachmentResponseInfo<Pager> attachmentResponseInfo = null;
		try {
			AppNotice appNotice = new AppNotice();
			Pager pager = new Pager();
			pager.setPage(pagerNum);
			pager.setRows(pagerMax);
			pager.setSidx("CREATE_TIME");
			pager.setSort("DESC");
			appNotice.setPager(pager);
			appNotice.setIsValid("1");
			appNotice.setNoticeType(NoticeTypeEnum.展业.getValue());
			pager = appNoticeServiceImpl.findWithPg(appNotice);
			attachmentResponseInfo = new AttachmentResponseInfo<Pager>(ResponseEnum.SYS_SUCCESS);
			attachmentResponseInfo.setAttachment(pager);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			attachmentResponseInfo = ex.toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Pager>(ResponseEnum.SYS_FAILD);
		}
		return FastJsonUtil.toJSONString(attachmentResponseInfo);
	}

}
