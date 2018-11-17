package com.zdmoney.credit.repay.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

public class ManagerSalesDeptChangeUtil {
    
    private static final String CONTENT_TYPE_1 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    private static final String CONTENT_TYPE_2 = "application/vnd.ms-excel";
    
    private static final String CONTENT_TYPE_3 = "application/.xls";
    
    protected static Log logger = LogFactory.getLog(ManagerSalesDeptChangeUtil.class);
    
     /**
     * 文件校验
     * @param file
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String fileCheck(MultipartFile file){
        Date sysdate = new Date();
        int hour = sysdate.getHours();
        int minute = sysdate.getMinutes();
        // 生成报盘前后的15分钟内不允许进行管理营业部调整
        if((hour==0 || hour==9 ||hour==12 ||hour==15)&&minute >=30 && minute<=60){
            return "生成报盘前后的15分钟内不允许进行管理营业部调整，请稍后再试";
        }
        
        // 没有选择文件或者选中的文件是空文件
        if(file.isEmpty()|| file.getSize()<=0){
            return "导入的还款文件为空，不能导入！";
        }
        
        // 导入的文件不能超过50M
        if(file.getSize() > 1024*1024*50){
            return "导入的还款文件过大，不能导入！";
        }
        
        // 导入的文件类型错误
        if (!CONTENT_TYPE_1.equals(file.getContentType())
                && !CONTENT_TYPE_2.equals(file.getContentType())
                && !CONTENT_TYPE_3.equals(file.getContentType())) {
            return "导入的还款文件类型错误，不能导入！";
        }
        return null;
    }
    
    /**
     * 文件复制到指定目录
     * @param file
     * @param request
     */
    public static File createFile(MultipartFile file,HttpServletRequest request){
        Date sysdate = new Date();
        ServletContext servletContext = request.getSession().getServletContext();
        String webRootDir = servletContext.getRealPath(File.separatorChar+"uploads");
        // 存放文件的目录
        StringBuilder filePath = new StringBuilder();
        filePath.append(File.separatorChar);
        filePath.append("manageSalesDep");
        filePath.append(File.separatorChar);
        filePath.append(new SimpleDateFormat("yyyyMM").format(sysdate));
        filePath.append(File.separatorChar);
        File fileDir = new File(webRootDir, filePath.toString());
        // 创建文件目录
        fileDir.mkdirs();
        // 新文件名
        StringBuilder fileName = new StringBuilder();
        fileName.append(new SimpleDateFormat("MMddHHssSS").format(sysdate));
        fileName.append(file.getOriginalFilename());
        File saveFile = new File(fileDir, fileName.toString());
        try {
            // 复制源文件到指定的目标文件
            file.transferTo(saveFile);
        } catch (IllegalStateException e) {
            logger.error("文件复制失败：", e);
        } catch (IOException e) {
            logger.error("文件复制失败：", e);
        }
        return saveFile;
    }
    
    /**
     * 按指定格式将字符串日志转换为Date类型的日期
     * @param inputDate
     * @param format
     * @return
     */
    public static Date formatDate(String inputDate,String format){
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(inputDate);
        } catch (ParseException e) {
            date = new Date();
        }
        return date;
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
