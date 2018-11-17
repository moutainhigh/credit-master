package com.zdmoney.credit.loan.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 连接渤海信托ftp服务器
 */
@Service
public class ConnectBhxtFtpServiceImpl implements IConnectBhxtFtpService {
    private static final Logger logger= LoggerFactory.getLogger(ConnectBhxtFtpServiceImpl.class);

    @Autowired
    private ISysParamDefineService sysParamDefineService;

    /**
     * FTP服务器IP地址
     */
    public static String host;
    /**
     * FTP服务器端口号
     */
    public static String port;
    /**
     * FTP服务器登陆用户名
     */
    public static String userName;
    /**
     * FTP服务器登录密码
     */
    public static String password;
    /**
     * FTP服务器文件存储路径（下载文件的路径）
     */
    public static String storeDir;
    /**
     * 渤海ftp服务器ip:
     */
    public static String hostBH;
    /**
     * 渤海ftp服务器端口
     */
    public static String portBH;
    /**
     * 渤海ftp服务器连接用户名
     */
    public static String userNameBH;
    /**
     * 渤海ftp服务器连接密码
     */
    public static String passWordBH;
    /**
     * 渤海ftp服务器上传存储路径
     */
    public static String uploadFtpDirBH;
    /**
     *划款申请书
     */
    public static String applyUploadDirBH;
    /**
     * 放款明细申请书
     */
    public static String loanapplyUploadDirBH;
    /**
     * 还款计划
     */
    public static String payplanUploadDirBH;
    /**
     * 放款明细
     */
    public static  String loandetailUploadDirBH;
    /**
     *
     *回款确认书
     */
    public static  String paylogsumUploadDirBH;
    /**
     * 分账明细
     */
    public static  String paylogUploadDirBH;
    /**
     * 暂其他收款
     */
    public static  String shortreceiptUploadDirBH;
    /**
     * 渤海2上传渤海ftp服务器上传存储路径
     */
    public static String uploadFtpDirBH2;
    /**
     *划款申请书 渤海2
     */
    public static String applyUploadDirBH2;
    /**
     * 放款明细申请书 渤海2
     */
    public static String loanapplyUploadDirBH2;
    /**
     * 还款计划 渤海2
     */
    public static String payploanUploadDirBH2;
    /**
     * 放款明细 渤海2
     */
    public static  String loandetailUploadDirBH2;
    /**
     * 回款确认书 渤海2
     */
    public static  String paylogsumUploadDirBH2;
    /**
     * 还款状态文件 渤海2
     */
    public static  String repayStateDetailUploadDirBH2;
    /**
     * 分账明细 渤海2
     */
    public static  String paylogUploadDirBH2;
    /**
     * 暂其他收款 渤海2
     */
    public static  String shortreceiptUploadDirBH2;
    /**
     * 电子签章服务器IP
     */
    public static String esignatureFtpHost;
    /**
     * 电子签章服务器用户名
     */
    public static String esignatureFtpUserName;
    /**
     * 电子签章服务器密码
     */
    public static String esignatureFtpPassword;
    /**
     * 电子签章服务器端口
     */
    public static String esignatureFtpPort;
    /**
     * 电子签章服务上传文件路径
     */
    public static String esignatureFtpUploadPath;
    /**
     * 电子签章服务下载（备份）文件路径
     */
    public static String esignatureFtpDownloadPath;
    /**
     * 电子签章服务签章路径
     */
    public static String esignatureFtpEsignatureUrl;
    /**
     * 渤海2 贷款协议上传路径
     */
    public static String loanAgreementUploadBH2; 

    public static int times=300000;
    /**
     * 图片文件转PDF文件，电子签章ftp用户
     */
    public static String pdfUserName;
    /**
     * 图片文件转PDF文件，电子签章ftp密码
     */
    public static String pdfPassword;
    
    /**
     * 龙信小贷ftp服务器ip:
     */
    public static String hostLXXD;
    /**
     * 龙信小贷ftp服务器端口
     */
    public static String portLXXD;
    /**
     * 龙信小贷ftp服务器连接用户名
     */
    public static String userNameLXXD;
    /**
     * 龙信小贷ftp服务器连接密码
     */
    public static String passWordLXXD;
    /**
     * 龙信小贷ftp服务器上传存储路径
     */
    public static String uploadFtpDirLXXD;
    /**
     * 龙信小贷上传通知 接口地址
     */
    public static String urlInterLXXD;
    /**
     * 龙信小贷上传通知 接口用户名
     */
    public static String userNameInterLXXD;
    /**
     * 龙信小贷上传通知接口密码
     */
    public static String passWordInterLXXD;
    /**
     * 外贸3ftp服务器ip:
     */
    public static String hostWM3;
    /**
     * 外贸3ftp服务器端口
     */
    public static String portWM3;
    /**
     * 外贸3ftp服务器连接用户名
     */
    public static String userNameWM3;
    /**
     * 外贸3ftp服务器连接密码
     */
    public static String passWordWM3;
    /**
     * 外贸3ftp服务器上传存储路径
     */
    public static String downloadFtpDirWM3;
    /**
     * 外贸3ftp服务器ip: 上传影像资料
     */
    public static String uploadHostWM3;
    /**
     * 外贸3ftp服务器端口 上传影像资料
     */
    public static String uploadPortWM3;
    /**
     * 外贸3ftp服务器连接用户名 上传影像资料
     */
    public static String uploadUserNameWM3;
    /**
     * 外贸3ftp服务器连接密码 上传影像资料
     */
    public static String uploadPassWordWM3;
    /**
     * 外贸3ftp服务器上传存储路径 上传影像资料
     */
    public static String uploadFtpDirWM3;
    /**
     *外贸3ftp服务器上传存储路径 上传债权信息
     */
    public static String uploadLoanInfoDirWM3;
    /**
     *外贸3ftp服务器下载存储路径 下载债权信息
     */
    public static String downloadLoanInfoDirWM3;

    /** 
     * 华瑞渤海 上传渤海ftp服务器上传存储路径 存放借款人资料
     */
    public static String uploadFtpDirHRBH;

    /**
     *划款申请书 华瑞渤海
     */
    public static String applyUploadDirHRBH;
    /**
     * 放款明细申请书 华瑞渤海
     */
    public static String loanapplyUploadDirHRBH;
    /**
     * 还款计划 华瑞渤海
     */
    public static String payploanUploadDirHRBH;
    /**
     * 放款明细 华瑞渤海
     */
    public static  String loandetailUploadDirHRBH;
    /**
     * 回款确认书 华瑞渤海
     */
    public static  String paylogsumUploadDirHRBH;
    /**
     * 分账明细 华瑞渤海
     */
    public static  String paylogUploadDirHRBH;
    /**
     * 暂其他收款 华瑞渤海
     */
    public static  String shortreceiptUploadDirHRBH;
    /**
     * 华瑞渤海 贷款协议上传路径 签章pdf
     */
    public static String loanAgreementUploadHRBH;
    /**
     * 渤海2划拨确认书 上传路径 pdf和xls
     */
	public static String confirmUploadDirBH2;
	/**
	 * 华瑞渤海划拨确认书 上传路径 pdf和xls
	 */
	public static String confirmUploadDirHRBH;
    
    @Override
    @PostConstruct
    public boolean getConnectionParam() {
        logger.info("初始化服务器连接参数！");
        host = sysParamDefineService.getSysParamValueCache("download", "sftp.download.host");
        port = sysParamDefineService.getSysParamValueCache("download", "sftp.download.port");
        userName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.userName");
        password = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pwd");
        //图片文件转PDF文件，电子签章ftp密码
        pdfUserName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pdfUserName");
        pdfPassword = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pdfPassword");
        
        storeDir = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadbohai.storeDir");
        //获取ftp渤海连接的信息
        hostBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftpuploadbohai.host");
        portBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftpuploadbohai.port");
        userNameBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftpuploadbohai.username");
        passWordBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftpuploadbohai.password");
        uploadFtpDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftpuploadbohai.directory");
        //渤海信托上传目录
        applyUploadDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftpapplyuploadbohai.directory");
        loanapplyUploadDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftploanapplyuploadbohai.directory");
        payplanUploadDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftppayplanuploadbohai.directory");
        loandetailUploadDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftploandetaiuploadbohai.directory");
        paylogsumUploadDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftppaylogsumuploadbohai.directory");
        paylogUploadDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftppayloguploadbohai.directory");
        shortreceiptUploadDirBH = sysParamDefineService.getSysParamValueCache("upload","sftp.ftpshortreceiptuploadbohai.directory");
        //渤海2 上传目录
        uploadFtpDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftpuploadbohai2.directory");
        applyUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftpapplyuploadbohai2.directory");
        loanapplyUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftploanapplyuploadbohai2.directory");
        payploanUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftppayloanuploadbohai2.directory");
        loandetailUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftploandetailuploadbohai2.directory");
        loanAgreementUploadBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftploanagreementuploadbohai2.directory");
        paylogsumUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftppaylogsumuploadbohai2.directory");
        paylogUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftppayloguploadbohai2.directory");
        shortreceiptUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftpshortreceiptuploadbohai2.directory");
        confirmUploadDirBH2 = sysParamDefineService.getSysParamValue("upload","sftp.ftpconfirmuploadbohai2.directory");
        //渤海2上传还款状态文件路径 /39
        repayStateDetailUploadDirBH2 = sysParamDefineService.getSysParamValueCache("upload","sftp.ftprepaystateuploadbohai2.directory");
        //查询连接电子签章的上传服务器
        esignatureFtpHost = sysParamDefineService.getSysParamValue("upload","ftp.esignatureFtp.host");
        esignatureFtpUserName = sysParamDefineService.getSysParamValue("upload","ftp.esignatureFtp.username");
        esignatureFtpPassword = sysParamDefineService.getSysParamValue("upload","ftp.esignatureFtp.password");
        esignatureFtpPort = sysParamDefineService.getSysParamValue("upload","ftp.esignatureFtp.port");
        esignatureFtpUploadPath = sysParamDefineService.getSysParamValue("upload","ftp.esignatureFtp.uploadpath");
        esignatureFtpDownloadPath = sysParamDefineService.getSysParamValue("upload","ftp.esignatureFtp.downloadpath");
        esignatureFtpEsignatureUrl = sysParamDefineService.getSysParamValue("upload","ftp.esignatureFtp.esignatureUrl");
        
        //获取ftp外贸3连接的信息  download
        hostWM3 = sysParamDefineService.getSysParamValueCache("download","sftp.download.host");
        portWM3 = sysParamDefineService.getSysParamValueCache("download","sftp.download.port");
        userNameWM3 = sysParamDefineService.getSysParamValueCache("download","sftp.download.userName");
        passWordWM3 = sysParamDefineService.getSysParamValueCache("download","sftp.download.pwd");
        downloadFtpDirWM3 = sysParamDefineService.getSysParamValueCache("download","sftp.download.directory");
        
        //获取ftp外贸3连接的信息  upload
        uploadHostWM3 = sysParamDefineService.getSysParamValueCache("upload","sftp.uploadwaimao3.host");
        uploadPortWM3 = sysParamDefineService.getSysParamValueCache("upload","sftp.uploadwaimao3.port");
        uploadUserNameWM3 = sysParamDefineService.getSysParamValueCache("upload","sftp.uploadwaimao3.userName");
        uploadPassWordWM3 = sysParamDefineService.getSysParamValueCache("upload","sftp.uploadwaimao3.pwd");
        uploadFtpDirWM3 = sysParamDefineService.getSysParamValueCache("upload","sftp.uploadwaimao3.directory");
        uploadLoanInfoDirWM3 = sysParamDefineService.getSysParamValueCache("upload","sftp.uploadwaimao3.loanInput");
        downloadLoanInfoDirWM3 = sysParamDefineService.getSysParamValueCache("upload","sftp.uploadwaimao3.loanOutput");

        //获取ftp龙信小贷连接的信息
        hostLXXD = sysParamDefineService.getSysParamValueCache("upload","ftp.ftpuploadlxxd.host");
        portLXXD = sysParamDefineService.getSysParamValueCache("upload","ftp.ftpuploadlxxd.port");
        userNameLXXD = sysParamDefineService.getSysParamValueCache("upload","ftp.ftpuploadlxxd.username");
        passWordLXXD = sysParamDefineService.getSysParamValueCache("upload","ftp.ftpuploadlxxd.password");
        uploadFtpDirLXXD = sysParamDefineService.getSysParamValueCache("upload","ftp.ftpuploadlxxd.storeDir");
        urlInterLXXD = sysParamDefineService.getSysParamValueCache("upload","lxxd.interface.url");
        userNameInterLXXD = sysParamDefineService.getSysParamValueCache("upload","lxxd.interface.username");
        passWordInterLXXD = sysParamDefineService.getSysParamValueCache("upload","lxxd.interface.password");

        //华瑞渤海 上传目录
        uploadFtpDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftpuploadbohai3.directory");
        applyUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftpapplyuploadbohai3.directory");
        loanapplyUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftploanapplyuploadbohai3.directory");
        payploanUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftppayloanuploadbohai3.directory");
        loandetailUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftploandetailuploadbohai3.directory");
        loanAgreementUploadHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftploanagreementuploadbohai3.directory");
        paylogsumUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftppaylogsumuploadbohai3.directory");
        paylogUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftppayloguploadbohai3.directory");
        shortreceiptUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftpshortreceiptuploadbohai3.directory");
        confirmUploadDirHRBH = sysParamDefineService.getSysParamValue("upload","sftp.ftpconfirmuploadbohai3.directory");

        if(StringUtils.isBlank(host)||StringUtils.isBlank(port)||StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
            logger.debug("FTP服务器IP地址:"+host+"端口号:"+port+"用户名:"+userName+"登陆密码:"+password);
            return false;
        }

        if(StringUtils.isBlank(hostBH)||StringUtils.isBlank(portBH)||StringUtils.isBlank(userNameBH)||StringUtils.isBlank(passWordBH)){
            logger.debug("BoHaiFTP服务器IP地址:"+hostBH+"端口号:"+portBH+"用户名:"+userNameBH+"登陆密码:"+passWordBH);
            return false;
        }
        if(StringUtils.isBlank(esignatureFtpHost)||StringUtils.isBlank(esignatureFtpUserName)||StringUtils.isBlank(esignatureFtpPassword)||StringUtils.isBlank(esignatureFtpPort)){
            logger.debug("电子签章服务器IP地址:"+esignatureFtpHost+"端口号:"+esignatureFtpPort+"用户名:"+esignatureFtpUserName+"登陆密码:"+esignatureFtpPassword);
            return false;
        }
        if(StringUtils.isBlank(uploadHostWM3)||StringUtils.isBlank(uploadPortWM3)||StringUtils.isBlank(uploadUserNameWM3)||StringUtils.isBlank(uploadPassWordWM3)){
            logger.debug("外贸3上传影像资料服务器IP地址:"+uploadHostWM3+"端口号:"+uploadPortWM3+"用户名:"+uploadUserNameWM3+"登陆密码:"+uploadPassWordWM3);
            return false;
        }
        logger.debug("FTP服务器IP地址:"+host+"端口号:"+port+"用户名:"+userName+"登陆密码:"+password+"下载文件路径:"+storeDir);
        logger.debug("BoHaiFTP服务器IP地址:"+hostBH+"端口号:"+portBH+"用户名:"+userNameBH+"登陆密码:"+passWordBH+"上传文件路径:"+uploadFtpDirBH+"划拨申请书："+applyUploadDirBH+"放款申请明细："+loanapplyUploadDirBH+"还款计划："+payplanUploadDirBH);
        return true;
    }

    @Override
    public JschSftpUtils getFtpBhxtConnectJsch() {
        JschSftpUtils jschSftpUtils=new JschSftpUtils(hostBH,Integer.parseInt(portBH),times,userNameBH,passWordBH);
        return jschSftpUtils;
    }

    @Override
    public void uploadFlagFile(String fileName,JschSftpUtils jschSftpUtils,String pathName) {
        FileInputStream fileInputStream=null;
        try {
           String classpath = this.getClass().getClassLoader().getResource("").getPath();
            File file=new File(classpath+"//"+fileName);
            if(!file.exists()){
                file.createNewFile();
            }
             fileInputStream=new FileInputStream(file);
            if(jschSftpUtils.changeDir(pathName)){
                boolean uploadStatus= jschSftpUtils.uploadFile(pathName,fileName,fileInputStream);
                if(uploadStatus){
                    logger.debug("上传标准文件成功");
                    return;
                }
            }
            logger.debug("上传标准文件失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FTPUtil getFtpEsignatureConnect() throws PlatformException {
        if(!this.getConnectionParam()){
            return null;
        }
        //连接储存盖章文件的ftp服务器
        FTPUtil ftpUtil=new FTPUtil();
        try {
            ftpUtil.connectServer(ConnectBhxtFtpServiceImpl.esignatureFtpHost,ConnectBhxtFtpServiceImpl.esignatureFtpPort,ConnectBhxtFtpServiceImpl.esignatureFtpUserName,ConnectBhxtFtpServiceImpl.esignatureFtpPassword,ConnectBhxtFtpServiceImpl.esignatureFtpUploadPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"连接服务器失败！");
        }
        return ftpUtil;
    }
    @Override
    public FTPUtil getApsFtpConnectFtpClient() throws Exception {
        FTPUtil zsftpService=new FTPUtil();
        try {
            zsftpService.connectServer(host, port, userName, password,storeDir);
            logger.info("连接政审服务器成功！");
        }catch (Exception e){
            logger.info("连接政审服务器异常！");
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"连接服务器失败！");
        }
        return zsftpService;
    }
}
