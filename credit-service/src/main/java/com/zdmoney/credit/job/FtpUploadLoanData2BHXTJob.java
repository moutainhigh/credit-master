package com.zdmoney.credit.job;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.loan.dao.pub.IUploadLoanProtocolLogDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.UploadLoanProtocolLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.signature.service.ElectronicSignatureServiceImpl;
import com.zdmoney.credit.signature.service.pub.IElectronicSignatureService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class FtpUploadLoanData2BHXTJob {

    private Logger logger = LoggerFactory.getLogger(FtpUploadLoanData2BHXTJob.class);
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    @Autowired
    private IVLoanInfoDao ivLoanInfoDao;
    @Autowired
    private IElectronicSignatureService electronicSignatureService;
    @Autowired
    private IRequestManagementService requestManagementService;
    @Autowired
    private IUploadLoanProtocolLogDao uploadLoanProtocolLogDao;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private ILoanLogService loanLogService;

    public void excute() {
        logger.info("开始执行贷款协议jpg文件转换pdf,上传至渤海JOB");
        loanLogService.createLog("FtpUploadLoanData2BHXTJob.excute", "info", "开始执行贷款协议jpg文件转换pdf,上传至渤海JOB===============", "SYSTEM");
        String isFtpUploadLoanData2BHXTJob = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadLoanDataBH2Job");
        if (Const.isClosing.equals(isFtpUploadLoanData2BHXTJob)) {
            logger.warn("定时开关isFtpUploadLoanDataBH2Job关闭，此次不执行");
            loanLogService.createLog("FtpUploadLoanData2BHXTJob.excute", "info", "定时开关isFtpUploadLoanDataBH2Job关闭，此次不执行===============", "SYSTEM");
            return;
        }
        //征审系统ftp
        FTPUtil zsftp = new FTPUtil();
        //渤海系统ftp
        JschSftpUtils bhftp = null;
        try {
            zsftp.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, ConnectBhxtFtpServiceImpl.userName, ConnectBhxtFtpServiceImpl.password, ConnectBhxtFtpServiceImpl.storeDir, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
            bhftp = connectBhxtFtpService.getFtpBhxtConnectJsch();
            if (!bhftp.login()) {
                logger.info("登录渤海ftp服务器失败!");
                loanLogService.createLog("FtpUploadLoanData2BHXTJob.excute", "info", "登录渤海ftp服务器失败===============", "SYSTEM");
                return;
            }
//				bhftp.connectServer(ConnectBhxtFtpServiceImpl.hostBH, ConnectBhxtFtpServiceImpl.portBH, ConnectBhxtFtpServiceImpl.userNameBH, ConnectBhxtFtpServiceImpl.passWordBH, ConnectBhxtFtpServiceImpl.loanAgreementUploadBH2, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
            List<VLoanInfo> vLoanInfoList = this.findUploadLoanProtocolLoanInfo();
            for (VLoanInfo vLoanInfo : vLoanInfoList) {
                try {
                    this.generateLoanProtocolPdfAndUploadBHFtp(vLoanInfo, zsftp, bhftp);
                }catch (Exception e){
                    e.printStackTrace();
                    logger.info("上传合同号为：{}的贷款协议失败！异常：{}",vLoanInfo.getContractNum(),e.getMessage());
                    loanLogService.createLog("FtpUploadLoanData2BHXTJob.excute", "info", "上传合同号为：{}的贷款协议失败！异常:"+e.getMessage()+"===============", "SYSTEM");
                }
            }
            logger.info("贷款协议jpg文件转换pdf,上传至渤海JOB结束");
            loanLogService.createLog("FtpUploadLoanData2BHXTJob.excute", "info", "贷款协议jpg文件转换pdf,上传至渤海JOB结束===============", "SYSTEM");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("贷款协议jpg文件转换pdf,上传至渤海JOB失败。。。", e.getMessage());
        } finally {
            if (bhftp != null) {
                bhftp.logout();
            }
            try {
                zsftp.closeServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取pdf 文件名， 同一合同号 文件序号依次递增
     * @param contractNum
     * @param batchNum
     * @param bhftp
     * @return
     * @throws Exception
     */
    private String getFileName(String contractNum,String batchNum, JschSftpUtils bhftp) throws Exception {
        int i = 1;
        String projectCode = requestManagementService.getProjectCode(batchNum);
        String fileName = projectCode + "_" + contractNum + "_10_" + Strings.getFileSeq4Title(Strings.parseString(i), 3) + ".pdf";
/*		while(bhftp.existFile(fileName)){
            i++;
			fileName = projectCode + "_" + contractNum + "_10_" + Strings.getFileSeq4Title(Strings.parseString(i), 3) + ".pdf";
		}*/
        return fileName;
    }

    public List<VLoanInfo> findUploadLoanProtocolLoanInfo() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("fundsSourcesList", new String[]{FundsSourcesTypeEnum.渤海2.getValue(), FundsSourcesTypeEnum.华瑞渤海.getValue()});
//        params.put("grantMoneyDate", Dates.getDateTime(Dates.getBeforeDays(1), Dates.DEFAULT_DATE_FORMAT));
        List<VLoanInfo> vLoanInfoList = ivLoanInfoDao.findLoanAgreementByParams(params);
        if (CollectionUtils.isEmpty(vLoanInfoList)) {
            return Collections.emptyList();
        }
        return vLoanInfoList;
    }

    /**
     * 生成贷款协议pdf 并上传到渤海ftp服务器
     *
     * @param vLoanInfo
     */
    public void generateLoanProtocolPdfAndUploadBHFtp(VLoanInfo vLoanInfo, FTPUtil zsftp, JschSftpUtils bhftp) {
        try {
            //获取征审贷款协议图片文件
            String imageDir = ConnectBhxtFtpServiceImpl.storeDir + vLoanInfo.getAppNo() + ElectronicSignatureServiceImpl.zsStoreloanProtocolFileDir;//政审存贷款服务协议书（ipg 格式）
            List<ByteArrayOutputStream> byteArrayOutputStreamList = electronicSignatureService.downloadDirAllFiles(imageDir, zsftp);
            if (CollectionUtils.isEmpty(byteArrayOutputStreamList)) {
                logger.debug("没找到贷款协议图片 合同号为：" + vLoanInfo.getContractNum());
                return;
            }
            String pdfFileName = getFileName(vLoanInfo.getContractNum(), vLoanInfo.getBatchNum(),bhftp);
            OutputStream pdfOutStream = electronicSignatureService.createPdfFile(byteArrayOutputStreamList);
            InputStream pdfInStream = requestManagementService.outStreamToInSteam(pdfOutStream);
            String uploadPath = this.getBHFtpStoreLoanProtocolPath(requestManagementService.getProjectCode(vLoanInfo.getBatchNum()));
            if (!bhftp.changeDir(uploadPath)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "找不到存放贷款协议目录：{}", uploadPath);
            }
            boolean status = bhftp.uploadFile(uploadPath, pdfFileName, pdfInStream);
            if (status) {
                this.insertUploadLoanProtocolSuccessLog(vLoanInfo.getAppNo(),vLoanInfo.getContractNum());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG, e.getMessage());
        }
    }

    /**
     * 获取在渤海FTP服务器上存放贷款协议的路径
     *
     * @param projectCode
     * @return
     */
    public String getBHFtpStoreLoanProtocolPath(String projectCode) {
        if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)) {
            return ConnectBhxtFtpServiceImpl.loanAgreementUploadBH2;
        } else if (RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
            return ConnectBhxtFtpServiceImpl.loanAgreementUploadHRBH;
        }
        throw new PlatformException(ResponseEnum.FULL_MSG, "找不到对于项目简码在渤海FTP服务器上存放贷款协议的路径");
    }

    public void insertUploadLoanProtocolSuccessLog(String appNo,String contractNum){
        UploadLoanProtocolLog uploadLoanProtocolLog = new UploadLoanProtocolLog();
        uploadLoanProtocolLog.setId(sequencesService.getSequences(SequencesEnum.UPLOAD_LOAN_PROTOCOL_LOG));
        uploadLoanProtocolLog.setAppNo(appNo);
        uploadLoanProtocolLog.setContractNum(contractNum);
        uploadLoanProtocolLog.setUpdateTime(new Date());
        uploadLoanProtocolLogDao.insert(uploadLoanProtocolLog);
    }
}
