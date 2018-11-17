package com.zdmoney.credit.framework.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.WebConstants;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;

public class BaseController {

    protected static Log logger = LogFactory.getLog(BaseController.class);

    // 前台列表组件标识每行数据的Key值
    private static final String DATA_GRID_ROW_FLAG = "rows";
    // 前台列表组件标识总记录数的Key值
    private static final String DATA_GRID_TOTAL_COUNT_FLAG = "total";

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
}
