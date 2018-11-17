package com.zdmoney.credit.framework.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.service.pub.IBaseService;

@Controller
@RequestMapping("/frameWork")
public class FrameWorkBaseController extends BaseController {

    protected static Log logger = LogFactory.getLog(FrameWorkBaseController.class);
    
    @Autowired
    private IBaseService baseService;

    /**
     * 检测系统是否正常运行
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/systemCheck")
    @ResponseBody
    public String verify(HttpServletRequest request,HttpServletResponse response) {
        String msg = "OK";
        try {
            // 检测数据库连接是否正常
            baseService.systemCheck();
            //logger.info("系统检测正常！");
        } catch (Exception e) {
            //logger.info("数据库连接异常！");
            msg = "FAIL";
        }
        return msg;
    }

    /**
     * 会话超时(暂时取消，另一种方式来处理)
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/sessionTimeOut")
    @ResponseBody
    public void sessionTimeOut(HttpServletRequest request,HttpServletResponse response) {
        // if (isAjaxRequest(request)) {
        // /** 超时后 返回JSON到前端 **/
        // ResponseInfo responseInfo = new
        // ResponseInfo(ResponseEnum.SYS_SessionOutActionCode);
        // try {
        // response.setContentType("application/json");
        // response.getWriter().print(toResponseJSON(responseInfo));
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // } else {
        // /** 超时后直接跳转到应用根路径 **/
        // try {
        // String directUrl = request.getContextPath() + "/";
        // response.sendRedirect(directUrl);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
    }

    /**
     * 会话超时
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadBigFile")
    @ResponseBody
    public String downloadBigFile(HttpServletRequest request,HttpServletResponse response) {
		AttachmentResponseInfo<Map<String,Object>> attachmentResponseInfo = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		attachmentResponseInfo.setAttachment(data);
		
		try {
    		Boolean downloadFileState = (Boolean)request.getSession().getAttribute(request.getParameter("htmlDownloadFileToken"));
    		if(downloadFileState!=null && downloadFileState == true){
    			request.getSession().removeAttribute(request.getParameter("htmlDownloadFileToken"));
    		}
    		data.put("downloadFileState", downloadFileState);
    		attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
    		attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
		} catch (Exception e) {
			attachmentResponseInfo.setResCode(ResponseEnum.SYS_FAILD.getCode());
			attachmentResponseInfo.setResMsg("查询失败");
		}
		return toResponseJSON(attachmentResponseInfo);
    }
}
