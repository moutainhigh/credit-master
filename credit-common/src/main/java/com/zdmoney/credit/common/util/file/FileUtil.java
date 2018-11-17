package com.zdmoney.credit.common.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;

import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.util.FieldUtils;
import com.zdmoney.credit.common.util.Strings;

/**
 * 
 * @author fuhongxing 
 */
public class FileUtil {
    
    private static final Logger logger = Logger.getLogger(FileUtil.class);
    
    /**
     * file copy
     */
    public static void copyFile(String srcPath,String destPath) throws Exception {
        //1、建立联系 源(存在且为文件) +目的地(文件可以不存在)  
        File src = new File(srcPath);
        File dest = new File(destPath);
        if (!src.isFile()) { // 不是文件或者为null
            System.out.println("只能拷贝文件");
            throw new IOException("只能拷贝文件");
        }
        // 2、选择流
        try (
            InputStream is = new FileInputStream(src); 
            OutputStream os = new FileOutputStream(dest);) {
            // 3、文件拷贝 循环+读取+写出
            byte[] flush = new byte[1024];
            int len = 0;
            // 读取
            while (-1 != (len = is.read(flush))) {
                // 写出
                os.write(flush, 0, len);
            }
            os.flush(); // 强制刷出
        } catch (Exception e) {
            logger.info("copy file exception", e);
        } 
    }
    
    /**
     * 文件内容读取
     * @param src
     * @return
     */
    public static List<String []> readFile(InputStream inputStream){
        //选择流
//        BufferedInputStream is =null;
        BufferedReader reader =null;
        InputStreamReader inputStreamReader = null;
        List<String []> list = new ArrayList<String []>();
        try {
//            is = new BufferedInputStream(inputStream);
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            //读取操作
            String line =null;
            String str [] = null;
            
            //读取
            while(null!=(line=reader.readLine())){
                //转义|+|
                if(StringUtils.isNotEmpty(line.trim())){
                    str = line.split("\\u007C\\u002B\\u007C");
                    if(str!=null && str.length > 1){
                        list.add(str);
                    }
                }
                
            }
        }catch (Exception e) {
            logger.error("文件读取异常", e);
        }finally{
            
            try {
                if (null != reader) {
                    reader.close();
                }
                if(inputStreamReader != null){
                    inputStreamReader.close();
                }
            } catch (Exception e) {
                logger.error("文件流关闭异常", e);
            }
        }
        return list;
    }
    
    /**
     * 文件写入
     * @param <T>
     */
    public static void write(InputStream inputStream , List<String> list, HttpServletRequest request, HttpServletResponse response){
        BufferedWriter wr =null;
//        String path ="C:/Users/Administrator/Desktop/temp.txt";
        String path = request.getSession().getServletContext().getRealPath("/")+File.separator+"temp.txt";
        logger.info("upload path ="+request.getContextPath());
        File dest =new File(path);
        BufferedReader reader =null;
        InputStreamReader inputStreamReader = null;
        FileWriter fileWriter = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            fileWriter = new FileWriter(dest);
            wr =new BufferedWriter(fileWriter);
            
            String line =null;
            int index = 0;
            while(null!=(line=reader.readLine())){
                line = line + "导入结果："+list.get(index);
                wr.write(line);
            //wr.append("\r\n");//换行符号
                wr.newLine(); 
                index++;
            }
        }catch (Exception e) {
            logger.error("文件读取异常",e );
        }finally{
            try {
                if (null != wr) {
                    wr.flush();
                    wr.close();
                }
                if(null != fileWriter){
                    fileWriter.close();
                }
                if(null != reader){
                    reader.close();
                }
                if(null != inputStreamReader){
                    inputStreamReader.close();
                }
            } catch (Exception e) {
                logger.error("文件流关闭异常", e);
            }
        }
        download(path, request, response);
    }
    
    public static void download(String path, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream fis = null;
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名
            String fileName = file.getName();
            // 以流的形式下载文件
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String userAgent = request.getHeader("USER-AGENT");
            String finalFileName = "";
            if(org.apache.commons.lang3.StringUtils.contains(userAgent, "MSIE") || org.apache.commons.lang3.StringUtils.contains(userAgent, "11.0")){//IE浏览器
                finalFileName = URLEncoder.encode(fileName,"UTF8");
            }else if(org.apache.commons.lang3.StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                finalFileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
            }else{
                finalFileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器
            }
            // 设置response的Header
            response.setHeader("Content-Disposition", "attachment;filename="+finalFileName);
            outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
        } catch (IOException e) {
            logger.error("下载异常", e);
        } finally{
            try {
                if (null != outputStream) {
                    outputStream.flush();
                    outputStream.close();
                }
                if(null != fis){
                    fis.close();
                }
            } catch (Exception e) {
                logger.error("文件流关闭异常", e);
            }
        }
    }
    
    /**
     * 文本文件写到指定路径
     * @param dataList
     * @param path
     * @param fields
     * @param separator
     * @return
     */
    public static File write(List dataList, String path, List<String> fields, String separator) {
        // 被写入文件
        File file = new File(path);
        // 编辑文件数据
        StringBuffer contentBuf = buildContentBuf(dataList, fields, separator);
        try {
            FileUtils.writeStringToFile(file, contentBuf.toString(),RequestManagementConst.STRING_CHARSET, true);
        } catch (IOException e) {
            logger.error("文本文件数据写入输出流异常：", e);
        }
        return file;
    }
    
    /**
     * 文本文件数据写入输出流
     * @param dataList
     * @param fields
     * @param separator
     * @return
     */
    public static OutputStream write(List dataList, List<String> fields, String separator) {
        OutputStream os = new ByteArrayOutputStream();
        // 编辑文本文件数据
        StringBuffer contentBuf = buildContentBuf(dataList, fields, separator);
        try {
            os.write(contentBuf.toString().getBytes());
        } catch (IOException e) {
            logger.error("文本文件数据写入输出流异常：", e);
        }
        return os;
    }
    
    /**
     * 文本文件数据写入输出流
     * @param dataList
     * @param fields
     * @param separator
     * @return
     */
    public static InputStream write(List dataList, List<String> fields, String separator, String encoding) {
        InputStream is = null;
        if(Strings.isEmpty(encoding)){
        	encoding = "UTF-8";
        }
        // 编辑文本文件数据
        StringBuffer contentBuf = buildContentBuf(dataList, fields, separator);
        try {
            is = new ByteArrayInputStream(contentBuf.toString().getBytes(encoding));
        } catch (IOException e) {
            logger.error("文本文件数据写入输出流异常：", e);
        }
        return is;
    }

    /**
     * 编辑文本文件数据
     * @param dataList 源数据
     * @param fields 匹配的字段
     * @param separator 分隔符
     * @return
     */
    private static StringBuffer buildContentBuf(List<T> dataList, List<String> fields,String separator) {
        StringBuffer contentBuf = new StringBuffer();
        Field[] fieldList = null;
        String key = null;
        for(int i =0; i< dataList.size();i++){
            Object bean = dataList.get(i);
            if(i == 0){
                // 获取数据对象的所有属性
                fieldList = FieldUtils.getClassField(bean.getClass());
            }
            for(int j=0; j < fields.size(); j++){
                Object value = null;
                key = fields.get(j);
                for(Field field : fieldList){
                    if(key.equals(field.getName())){
                        try {
                            field.setAccessible(true);
                            value =field.get(bean);
                            break;
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(null== value){
                    value = "";
                }
                if(j == 0){
                    contentBuf.append(value.toString());
                }else{
                    contentBuf.append(separator);
                    contentBuf.append(value.toString());
                }
            }
            // 换行 注意linux 跟window环境换行符不同
            contentBuf.append("\n");
        }
        return contentBuf;
    }
}
