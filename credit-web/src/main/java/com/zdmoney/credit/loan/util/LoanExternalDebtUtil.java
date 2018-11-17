package com.zdmoney.credit.loan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.zdmoney.credit.common.util.CommonsCompressZipUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FileDownUtils;
import com.zdmoney.credit.loan.vo.LoanImageFile;

public class LoanExternalDebtUtil {
    
    /**
     * 日志输出对象
     */
    private static final Logger logger = Logger.getLogger(LoanExternalDebtUtil.class);
    
    /**
     * 空字符串
     */
    private final static String EMPTY = "";
    
    /**
     * 压缩文件后缀名
     */
    private final static String SUFFIX_ZIP = ".zip";

    /**
     * 图片压缩打包处理
     * @param imageFileList
     * @return
     */
    public static String[] zipImages(List<LoanImageFile> imageFileList) {
        // 保存文件的父级目录
        String tempRoot = File.separator + "uploads" + File.separator + "temp4zip";
        // 系统时间
        String dateStr = Dates.getDateTime(new Date(), "yyyyMMddhhmmssSS");
        // 保存文件的目标目录
        String tempDir = tempRoot + File.separator + dateStr;
        // 客户姓名
        String personName = EMPTY;
        // 客户身份证号
        String idNum = EMPTY;
        // 图片类型
        String imageType = EMPTY;
        // 图片名称
        String imageName = EMPTY;
        // 文件名称
        String fileName = EMPTY;
        // 文件路径
        String filePath = EMPTY;
        StringBuilder rootDir = new StringBuilder();
        for (LoanImageFile loanImageFile : imageFileList) {
            // 每次循环，清空rootDir对象
            rootDir.setLength(0);
            personName = loanImageFile.getName();
            idNum = loanImageFile.getIdNum();
            imageType = loanImageFile.getImageType(); 
            imageName = loanImageFile.getImageName();
            fileName = loanImageFile.getFileName();
            filePath = loanImageFile.getFilepath();
            rootDir.append(tempDir).append(File.separator).append(personName)
                    .append(idNum).append(File.separator).append(imageType)
                    .append(File.separator).append(imageName);
            // 创建目录
            FileDownUtils.ensureDir(rootDir.toString());
            // 保存文件的目标地址
            String newPath = rootDir + File.separator + fileName;
            try {
                // 复制文件到指定的目录中，单个复制失败不影响后续的复制操作
                FileUtils.copyFile(new File(filePath), new File(newPath));
            } catch (IOException e) {
                logger.error("债权导出（供第三方） 复制文件异常，异常信息："+e.getMessage());
                return  new String[] { "未找到该附件信息"};
            }
        }
        // 压缩文件名
        String zipFilePath = tempRoot + File.separator + dateStr + SUFFIX_ZIP;
        try {
            // 压缩文件
            // FileDownUtils.zipFiles(tempDir, zipFilePath);
            CommonsCompressZipUtils.zip(tempDir, zipFilePath);
            // 删除临时保存文件的目录
            FileUtils.deleteDirectory(new File(tempDir));
        } catch (IOException e) {
            logger.error("债权导出（供第三方） 压缩文件异常，异常信息："+e.getMessage());
            return  new String[] { "债权导出（供第三方） 压缩文件异常"};
        }
        // 返回压缩文件名
        return new String[] { tempRoot, dateStr + SUFFIX_ZIP };
    }
    
    /**
     * 文件下载
     * @param filePath
     * @param fileName
     * @param request
     * @param response
     * @throws IOException
     */
    public static String downloadFile(String filePath, String fileName, HttpServletRequest request, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        File file = null;
        try {
//            response.reset();
            response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String path = filePath + File.separator + fileName;
            file = new File(path);
            in = new FileInputStream(file);
            out = response.getOutputStream();
            byte[] buffer = new byte[2048];
            int count = -1; // 每次读取字节数 
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            out.flush();
            // 下载完文件之后,删除这个文件用于节省硬盘空间
            // file.delete();
        } catch (UnsupportedEncodingException e) {
            logger.error("债权导出（供第三方） 字符转码异常，异常信息："+e.getMessage());
            return "债权导出（供第三方） 字符转码异常";
        } catch (IOException e) {
            logger.error("债权导出（供第三方） 文件下载异常，异常信息："+e.getMessage());
            return "债权导出（供第三方） 文件下载异常";
        }finally{
            try {
                if(null!= out){
                    out.close();
                }
                if(null!= in){
                    in.close();
                }
                // 下载完文件之后,删除这个文件用于节省硬盘空间
                file.delete();
            } catch (IOException e) {
                logger.error("债权导出（供第三方） 文件下载异常，异常信息："+e.getMessage());
                return "债权导出（供第三方） 文件下载异常";
            }
        }
        return null;
    }
}
