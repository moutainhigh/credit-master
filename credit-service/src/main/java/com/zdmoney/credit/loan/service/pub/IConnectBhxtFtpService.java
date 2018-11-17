package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;

/**
 *连接渤海信托ftp服务器
 */
public interface IConnectBhxtFtpService {
    /**
     * 获取连接的参数
     * @return
     */
    public boolean getConnectionParam();

    /**
     * 获取jsch连接
     * @return
     */
    public JschSftpUtils getFtpBhxtConnectJsch();

    /**
     * 生成标准文件
     * @param fileName
     * @return
     */
    public void uploadFlagFile(String fileName,JschSftpUtils jschSftpUtils,String pathName);

    /**
     * 连接存放盖章文件的ftp服务器
     * @return
     */
    public FTPUtil getFtpEsignatureConnect() throws PlatformException;
    /**
     * 通过 ftpClient 连接政审ftp服务器
     * @return
     */
    public FTPUtil getApsFtpConnectFtpClient() throws Exception;
}
