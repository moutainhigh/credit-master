package com.zdmoney.credit.framework.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.SpringContextUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.system.service.pub.ISysActionLogService;

public class BaseController {

    protected static Log logger = LogFactory.getLog(BaseController.class);

    // 前台列表组件标识每行数据的Key值
    private static final String DATA_GRID_ROW_FLAG = "rows";
    // 前台列表组件标识总记录数的Key值
    private static final String DATA_GRID_TOTAL_COUNT_FLAG = "total";
    // 前台页面计算变量
    private static final String DATA_GRID_CALCULATE_FLAG = "calculteData";
    
    @Autowired
    private static ISysActionLogService sysActionLogService;
    
    /**
     * 创建操作日志<br>
     * type logName content三个必填，如果为空则不会创建log
     * 
     * @param request 用于获取ip，url等 ，如果为空则不记录数据库
     * @param type  操作类别使用SysActionLogTypeEnum
     * @param logName  log名称（大模块名）
     * @param content  log描述
     */
    public static void createLog(HttpServletRequest request,SysActionLogTypeEnum type,String logName,String content ){
    	if(type==null||Strings.isEmpty(logName)||Strings.isEmpty(content)){
    		logger.error("createLog获取参数不全！");
    		return;
    	}
    	User user = UserContext.getUser();
    	/** 登陆者员工姓名 **/
    	String employeeRealName = "";
    	/** 登陆者员工工号 **/
    	String employeeUserCode = "";
    	if (user == null) {
    		logger.error("createLog获取user==null！");
    		return;
    	} else {
    		employeeRealName = user.getName();
    		employeeUserCode = user.getUserCode();
    	}
    	String url = "";
    	String ip = "";
    	if(request!=null){
    		try {
    			Map param = request.getParameterMap();
        		url = request.getRequestURL()+"?" + JSONObject.toJSONString(param);
    			ip = getIpAddress(request);
    		} catch (IOException e) {
    			logger.error("createLog获取ip错误！");
    		}
    	}
    	if (sysActionLogService == null) {
    		sysActionLogService = (ISysActionLogService)SpringContextUtil.getBean("sysActionLogServiceImpl");
    	}
    	sysActionLogService.createLog(employeeUserCode,employeeRealName, type.getValue(), logName,content,url,ip);
    }
    
    public static void createLog(HttpServletRequest request,SysActionLogTypeEnum type,String logName,String content ,String applyInputFlag,String updateapplyinputflag  ){
    	if(type==null||Strings.isEmpty(logName)||Strings.isEmpty(content)){
    		logger.error("createLog获取参数不全！");
    		return;
    	}
    	User user = UserContext.getUser();
    	/** 登陆者员工姓名 **/
    	String employeeRealName = "";
    	/** 登陆者员工工号 **/
    	String employeeUserCode = "";
    	if (user == null) {
    		logger.error("createLog获取user==null！");
    		return;
    	} else {
    		employeeRealName = user.getName();
    		employeeUserCode = user.getUserCode();
    	}
    	String url = "";
    	String ip = "";
    	if(request!=null){
    		try {
    			Map param = request.getParameterMap();
        		url = request.getRequestURL()+"?" + JSONObject.toJSONString(param);
    			ip = getIpAddress(request);
    		} catch (IOException e) {
    			logger.error("createLog获取ip错误！");
    		}
    	}
    	if (sysActionLogService == null) {
    		sysActionLogService = (ISysActionLogService)SpringContextUtil.getBean("sysActionLogServiceImpl");
    	}
    	sysActionLogService.createLog(employeeUserCode,employeeRealName, type.getValue(), logName,content,url,ip,applyInputFlag,updateapplyinputflag);
    
    }
    
    /** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     *  
     * @param request 
     * @return 
     * @throws IOException 
     */  
    public final static String getIpAddress(HttpServletRequest request) throws IOException {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
  
        String ip = request.getHeader("X-Forwarded-For");  
        if (logger.isDebugEnabled()) {  
            logger.debug("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);  
        }  
  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
                if (logger.isDebugEnabled()) {  
                    logger.debug("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
                if (logger.isDebugEnabled()) {  
                    logger.debug("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
                if (logger.isDebugEnabled()) {  
                    logger.debug("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
                if (logger.isDebugEnabled()) {  
                    logger.debug("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
                if (logger.isDebugEnabled()) {  
                    logger.debug("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);  
                }  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip;  
    }  

    
    /**
     * 重定向到指定页面
     * 
     * @param page
     * @return
     */
    public String redirect(String page) {
	return "redirect:" + page;
    }

    /**
     * 将查询出分页的数据采用FastJson进行转换成JSON字符串，返回给前端列表组件
     * 
     * @param pager
     *            带数据集的分页实例
     * @return
     */
    public String toPGJSON(Pager pager) {
	if (pager == null) {
	    logger.warn("ConvertToJSONStringFromPG Method Param IS NULL");
	    ;
	    return "{}";
	}
	long totalCount = pager.getTotalCount();
	List<Object> dataList = pager.getResultList();
	if (dataList == null) {
	    dataList = new ArrayList<Object>();
	}
	Map<String, Object> result = new LinkedHashMap<String, Object>();
	result.put(DATA_GRID_ROW_FLAG, dataList);
	result.put(DATA_GRID_TOTAL_COUNT_FLAG, totalCount);
	String jsonStr = JSONObject.toJSONString(result);
	return jsonStr;
    }

    /**
     * 将查询出分页的数据采用FastJson进行转换成JSON字符串，返回给前端列表组件(带响应状态码)
     * 
     * @param pager
     * @return
     */
    public String toPGJSONWithCode(Pager pager) {
	AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;
	if (pager == null) {
	    logger.warn("ConvertToJSONStringFromPG Method Param IS NULL");
	    ;
	    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.VALIDATE_ISNULL.getCode(),
		    ResponseEnum.VALIDATE_ISNULL.getDesc(), "");
	} else {
	    long totalCount = pager.getTotalCount();
	    List<Object> dataList = pager.getResultList();
	    if (dataList == null) {
		dataList = new ArrayList<Object>();
	    }
	    Map<String, Object> result = new LinkedHashMap<String, Object>();
	    result.put(DATA_GRID_ROW_FLAG, dataList);
	    result.put(DATA_GRID_TOTAL_COUNT_FLAG, totalCount);
	    
	    if (pager.getMapData() != null) {
	    	result.put(DATA_GRID_CALCULATE_FLAG, pager.getMapData());
	    }

	    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS.getCode(), ResponseEnum.SYS_SUCCESS.getDesc(), "");
	    attachmentResponseInfo.setAttachment(result);
	}
	String jsonStr = JSONObject.toJSONString(attachmentResponseInfo);
	return jsonStr;
    }

    /**
     * 转JSON字符串
     * 
     * @param responseInfo
     * @return
     */
    public static String toResponseJSON(ResponseInfo responseInfo) {
	if (responseInfo == null) {
	    logger.warn("ResponseInfoToJSONString Method Param IS NULL");
	    ;
	    return "{}";
	}
	String jsonStr = JSONObject.toJSONString(responseInfo);
	return jsonStr;
    }

    /**
     * 系统异常页面
     * 
     * @param runtimeException
     * @param modelMap
     * @return
     */
//    @ExceptionHandler
//    public String processUnauthenticatedException(NativeWebRequest request, Exception e) {
//	return redirect(WebConstants.PAGE_500_URI);
//    }

    /**
     * 添加时间的属性编辑器
     */
    @InitBinder
    public void InitBinder(ServletRequestDataBinder binder) {
	binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    
    public static boolean isAjaxRequest(HttpServletRequest request){
    	/** 下载文件（前台提交到IFrame）方式 **/
    	Object downLoadFileFlag = request.getAttribute("htmlDownloadFileToken");
    	String requestType = request.getHeader("X-Requested-With");  
        return downLoadFileFlag != null || "XMLHttpRequest".equals(requestType);
    }
}
