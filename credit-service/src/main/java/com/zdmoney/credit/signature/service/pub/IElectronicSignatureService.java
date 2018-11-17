package com.zdmoney.credit.signature.service.pub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * Created by ym10094 on 2016/12/5.
 */
public interface IElectronicSignatureService {
    /**
     * 批量appNo签章
     */
    public void absElectronicSignatureBatch();

    /**
     * 单个appNo文件签章
     * @param zsftpService
     * @param hxFtpService
     * @param vLoanInfo
     * @return
     * @throws Exception
     */
    public boolean absElectronicSignatureByAppNo(FTPUtil zsftpService,FTPUtil hxFtpService,VLoanInfo vLoanInfo) throws Exception;

    /**
     * 把某个目录下的图片（image）转换成pdf并签章
     * @param zsftpService 下载图片的服务器
     * @param hxFtpService 电子签章的服务器
     * @param path 主路径
     * @param downDir 下载目录
     * @param upDir 上传目录
     * @return
     * @throws Exception
     */
    public boolean absElectronicSignatureSingle(FTPUtil zsftpService,FTPUtil hxFtpService,String path,String downDir,String upDir) throws Exception;
    /**
     * 创建pdf
     * @param byteArrayOutputStreamList
     * @return
     */
    public OutputStream createPdfFile(List<ByteArrayOutputStream> byteArrayOutputStreamList) throws Exception;

    /**
     * 设置盖章位置（给pdf添加水印）
     * @param inputStream
     * @return
     * @throws Exception
     */
    public OutputStream addImpress(InputStream inputStream) throws Exception;

    /**
     * 电子签章（上传核心的签章ftp服务器 并调用 签章服务接口 完成签章工作）
     * 公章名字：上海证大投资咨询有限公司业务公章
     * @param fileName
     * @param projectCode
     * @param ftpUtil
     * @param in
     * @return
     * @throws IOException
     */
    public OutputStream dealEsignature(String fileName,String projectCode,FTPUtil ftpUtil,InputStream in) throws IOException;

    /**
     *根据appNo把政审系统的（imag）转换成pdf
     * @param appNoFtpService
     * @param vLoanInfo
     * @return
     * @throws IOException
     */
    public boolean absCreatePdfFileByAppNo(FTPUtil appNoFtpService,VLoanInfo vLoanInfo) throws Exception;

    /**
     * 把某个目录下的图片(imag)转换成pdf
     * @param appNoFtpService 服务器
     * @param path 主路径
     * @param downDir 下载图片的目录
     * @param upDir 上传pdf的目录
     * @return
     * @throws Exception
     */
    public boolean absCreatePdfFileSingle(FTPUtil appNoFtpService,String path,String downDir,String upDir) throws Exception;


    public List<ByteArrayOutputStream> downloadDirAllFiles(String imageDir, FTPUtil zsftp) throws IOException;
}
