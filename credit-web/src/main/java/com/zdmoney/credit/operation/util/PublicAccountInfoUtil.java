package com.zdmoney.credit.operation.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.util.Strings;

public class PublicAccountInfoUtil {
	
	public static final String CONTENT_TYPE_1 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
	public static final String CONTENT_TYPE_2 = "application/vnd.ms-excel";
    
	public static final String CONTENT_TYPE_3 = "application/kswps";
    
	public static final String CONTENT_TYPE_4 = "application/kset";
    
	public static final String CONTENT_TYPE_5 = "application/ksdps";
    
    /**
     * 导入文件校验
     * @param file
     * @return
     */
	public static String fileCheck(MultipartFile file){
        // 没有选择文件或者选中的文件是空文件
        if(file.isEmpty()|| file.getSize()<=0){
            return "导入失败,导入文件不能为空!";
        }
        
        // 导入的文件不能超过50M
        if(file.getSize() > 1024*1024*50){
            return "导入失败,文件过大!";
        }
        
        // 导入的文件类型错误
        if (!CONTENT_TYPE_1.equals(file.getContentType())
                && !CONTENT_TYPE_2.equals(file.getContentType())
                && !CONTENT_TYPE_3.equals(file.getContentType())
                && !CONTENT_TYPE_4.equals(file.getContentType())
                && !CONTENT_TYPE_5.equals(file.getContentType())) {
            return "文件类型错误，不能导入!";
        }
        return null;
    }
	
    /**
     * 设置文件编辑模板
     * @return
     */
    public static List<String> getKeyList(){
        List<String> keyList = new ArrayList<String>();
        keyList.add("repayDate");
        keyList.add("firstAccount");
        keyList.add("secondAccount");
        keyList.add("time");
        keyList.add("type");
        keyList.add("amount");
        keyList.add("voucherNo");
        keyList.add("secondCompany");
        keyList.add("secondBank");
        keyList.add("purpose");
        keyList.add("remark");
        keyList.add("comments");
        return keyList;
    }
    
    /**
     * 校验导入的每行数据
     * @param map
     * @return
     */
    public static boolean validateData(Map<String, Object> map) {
        // 还款金额
        Object amount = map.get("amount");
        // 还款日期
        Object repayDate = map.get("repayDate");
        if(Strings.isEmpty(amount) && Strings.isEmpty(repayDate))  {
            return false;
        }
        return true;
    }
    
    /**
     * 编辑下载的文件名
     * @param request
     * @param fileName
     * @return
     */
    public static String editFileName(HttpServletRequest request, String fileName) {
        String finalFileName = null;
        String userAgent = request.getHeader("USER-AGENT");
        try {
            // IE浏览器
            if (StringUtils.contains(userAgent, "MSIE")) {
                finalFileName = URLEncoder.encode(fileName, "UTF8");
            // google,火狐浏览器
            } else if (StringUtils.contains(userAgent, "Mozilla")) {
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            // 其他浏览器
            } else {
                finalFileName = URLEncoder.encode(fileName, "UTF8");
            }
        } catch (UnsupportedEncodingException e) {
            finalFileName = fileName;
        }
        return finalFileName;
    }
}
