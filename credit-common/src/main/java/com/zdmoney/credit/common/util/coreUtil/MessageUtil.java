package com.zdmoney.credit.common.util.coreUtil;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

/**
 * Created by 石俊 on 15-3-19.
 */
public class MessageUtil {

    /**
     * 返回错误信息
     *
     * @param msg
     * @return
     */
    public static Map<String, Object> returnErrorMessage(String msg) {
        return returnErrorMessage(Constants.DATA_ERR_CODE, msg);
    }

    /**
     * 返回错误信息
     *
     * @param code
     * @param msg
     * @return
     */
    public static Map<String, Object> returnErrorMessage(String code, String msg) {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("code", code);
        json.put("message", msg);
        return json;
    }

    /**
     * 返回查询成功信息
     *
     * @param max
     * @param list
     * @param total
     * @param vosName 返回JSONArray的名称
     * @return
     */
    public static Map<String, Object> returnQuerySuccessMessage(int max, List list, int total, String vosName) {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("code", Constants.SUCCESS_CODE);
        json.put("message", "成功");
        json.put("total", total);
        json.put("max", max);
        json.put(vosName, list);
        return json;
    }
    
    /**
     * 返回查询成功信息
     *
     * @param max
     * @param list
     * @param total
     * @param vosName 返回JSONArray的名称
     * @return
     */
    public static Map<String, Object> returnQuerySuccessMessage(int max, List list, long total, String vosName) {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("code", Constants.SUCCESS_CODE);
        json.put("message", "成功");
        json.put("total", total);
        json.put("max", max);
        json.put(vosName, list);
        return json;
    }

    /**
     * 返回查询成功信息
     *
     * @param list
     * @param vosName 返回JSONArray的名称
     * @return
     */
    public static Map<String, Object> returnListSuccessMessage(List<?> list,String vosName) {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("code", Constants.SUCCESS_CODE);
        json.put("message", "成功");
        json.put(vosName, list);
        return json;
    }

    /**
     * 返回操作成功信息
     *
     * @return
     */
    public static Map<String, Object> returnHandleSuccessMessage() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("code", Constants.SUCCESS_CODE);
        json.put("message", "成功");
        return json;
    }
}
