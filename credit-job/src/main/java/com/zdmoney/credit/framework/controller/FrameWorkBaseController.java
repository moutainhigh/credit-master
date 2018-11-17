package com.zdmoney.credit.framework.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
