package com.zdmoney.credit.job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.loan.dao.pub.IUploadFtpFileLogDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.UploadFtpFileLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 上传渤海ftp服务器
 * Created by ym10094 on 2016/8/19.
 */
@Service
public class FtpUploadBorrowerPersonDataJob {
    private static final Logger logger = LoggerFactory.getLogger(FtpUploadBorrowerPersonDataJob.class);
    @Autowired
    private IVLoanInfoDao ivLoanInfoDao;
    @Autowired
    private IUploadFtpFileLogDao iUploadFtpFileLogDao;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    @Autowired
    private IRequestManagementService requestManagementService;
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private ILoanLogService loanLogService;
    /**
     * 上传文件
     */
    public void execute() {
        logger.info("开始执行个人借款信息上传JOB");
        Map<String, Object> map = new HashMap<>();
        map.put("curryDate", Dates.getDateTime(new Date(), Dates.DEFAULT_DATE_FORMAT));
        this.executeService(map);
    }

    public void executeParam(Map<String, Object> map) {
        this.executeService(map);
    }

    /**
     * @param map
     */
    public void executeService(Map<String, Object> map) {
        String isFtpUploadBorrowerPersonData = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadBorrowerPersonData");
        if (Strings.isEmpty(isFtpUploadBorrowerPersonData) || "0".equals(isFtpUploadBorrowerPersonData)) {
            return;
        }
        //获取需要上传的资料编号集合
        List<VLoanInfo> vLoanInfoList = ivLoanInfoDao.findUploadFtpBorrowAppNoCn(map);
        if (CollectionUtils.isEmpty(vLoanInfoList)) {
            logger.info("没有可上传的个人借款人资料,停止job!");
            return;
        }
        if (!getConnectionParam()) {
            logger.info("获取连接ftp服务器参数失败,停止Job!");
            return;
        }
        JschSftpUtils jschSftpUtils = connectBhxtFtpService.getFtpBhxtConnectJsch();
        FTPUtil ftpUtil = new FTPUtil();
        try {
            //连接ftp服务器
            ftpUtil.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, ConnectBhxtFtpServiceImpl.userName, ConnectBhxtFtpServiceImpl.password, ConnectBhxtFtpServiceImpl.storeDir, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
            logger.info("ftp服务器连接成功!");
            //连接渤海的ftp服务器
            boolean loginStatus = jschSftpUtils.login();
            if (!loginStatus) {
                logger.info("登录渤海ftp服务器失败，停止Job!");
                return;
            }
            String zipFileName = "";
            String uploandBhFtpDir = "";
            boolean isFlagBHXT = false;
            boolean isFlagBH2 = false;
            for (VLoanInfo vlif : vLoanInfoList) {
                try {
                    String projectCode = requestManagementService.getProjectCode(vlif.getBatchNum());
                    if (RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)) {
                        //渤海信托
                        zipFileName = RequestManagementConst.TRUST_PROJECT_CODE + "_" + vlif.getContractNum();
                        uploandBhFtpDir = ConnectBhxtFtpServiceImpl.uploadFtpDirBH;
                    } else if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)) {
                        //渤海2
                        zipFileName = RequestManagementConst.TRUST_PROJECT_CODE2 + "_" + vlif.getContractNum();
                        uploandBhFtpDir = ConnectBhxtFtpServiceImpl.uploadFtpDirBH2;
                    } else if (RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                        //华瑞渤海
                        zipFileName = RequestManagementConst.TRUST_PROJECT_CODE3 + "_" + vlif.getContractNum();
                        uploandBhFtpDir = ConnectBhxtFtpServiceImpl.uploadFtpDirHRBH;
                    } else {
                        break;
                    };
                    int statusValue = this.excuteFtpCompressUpload(vlif.getAppNo() + "/", ftpUtil, jschSftpUtils, zipFileName, uploandBhFtpDir);
                    if(statusValue==1) {
                        this.insetUploadFtpLog(vlif.getAppNo(), vlif.getContractNum(), 1);
                    }
                    if(statusValue == 1){
                        if(RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)){
                            isFlagBHXT=true;
                        }else if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)){
                            isFlagBH2=true;
                        }
                    }
                } catch (FTPConnectionClosedException ftpe) {
                    ftpe.printStackTrace();
                    logger.info("重新连接ftp服务器");
                    ftpUtil.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, ConnectBhxtFtpServiceImpl.userName, ConnectBhxtFtpServiceImpl.password, ConnectBhxtFtpServiceImpl.storeDir, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
                }
            }
            if (isFlagBHXT) {
                connectBhxtFtpService.uploadFlagFile(RequestManagementConst.FLAG_FILE_NAME, jschSftpUtils, ConnectBhxtFtpServiceImpl.uploadFtpDirBH);
            }
            if (isFlagBH2) {
                connectBhxtFtpService.uploadFlagFile(RequestManagementConst.FLAG_FILE_NAME, jschSftpUtils, ConnectBhxtFtpServiceImpl.uploadFtpDirBH2);
            }
            logger.info("个人借款资料上传job执行结束！");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jschSftpUtils.logout();
            try {
                ftpUtil.closeServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 放款明细上传--渤海信托
     */
    public void executeLoandetailUploadBHFtp() {
        logger.info("开始执行放款明细上传job");
        loanLogService.createLog("executeLoandetailUploadBHFtp", "info", "开始执行放款明细上传job===============", "SYSTEM");
        Date curryDate = Dates.getNow();
        this.executeLoandetailUploadBHFtpService(curryDate);
    }

    public void executeLoandetailUploadBHFtpService(Date curryDate) {
        List<String> projectCodeList = new ArrayList<>();
        String isFtpUploadLoandetailBHXT = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadLoandetailBHXT");
        if (!Strings.isEmpty(isFtpUploadLoandetailBHXT) && "1".equals(isFtpUploadLoandetailBHXT)) {
            projectCodeList.add(RequestManagementConst.TRUST_PROJECT_CODE);//渤海信托
        }
        String isFtpUploadLoandetailBH2 = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadLoandetailBH2");
        if (!Strings.isEmpty(isFtpUploadLoandetailBH2) && "1".equals(isFtpUploadLoandetailBH2)) {
            projectCodeList.add(RequestManagementConst.TRUST_PROJECT_CODE2);//渤海2
        }
        String isFtpUploadLoandetailHRBH = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadLoandetailHRBH");
        if (!Strings.isEmpty(isFtpUploadLoandetailHRBH) && "1".equals(isFtpUploadLoandetailHRBH)) {
            projectCodeList.add(RequestManagementConst.TRUST_PROJECT_CODE3);//华瑞渤海
        }
        if (CollectionUtils.isEmpty(projectCodeList)) {
            loanLogService.createLog("executeLoandetailUploadBHFtp", "info", "执行放款明细上传job是关闭，结束执行===============", "SYSTEM");
            return;
        }
        if (curryDate == null) {
            return;
        }
        try{
            for(String projectCode:projectCodeList){
                logger.info("开始为：{}上传放款明细文件！", projectCode);
                try {
                    boolean status = this.uploadLoandetail(projectCode, curryDate);
                    if (status) {
                        logger.info("{}上传放款明细文件成功！", projectCode);
                        loanLogService.createLog("executeLoandetailUploadBHFtp", "info", Strings.format("{0}上传放款明细文件成功！===============", projectCode), "SYSTEM");
                        continue;
                    }
                } catch (PlatformException pe) {
                    loanLogService.createLog("executeLoandetailUploadBHFtp", "info", Strings.format("{0}执行放款明细上传job异常：" + pe.getMessage(),projectCode) , "SYSTEM");
                }
                logger.info("{}上传放款明细文件失败！", projectCode);
                loanLogService.createLog("executeLoandetailUploadBHFtp", "info", Strings.format("{0}上传放款明细文件失败！===============", projectCode), "SYSTEM");
            }
        }catch (Exception e){
            e.printStackTrace();
            loanLogService.createLog("executeLoandetailUploadBHFtp", "info", "执行放款明细上传job异常："+e.getMessage()+"，结束执行===============", "SYSTEM");
        }finally {
            loanLogService.createLog("executeLoandetailUploadBHFtp", "info", "执行放款明细上传job结束！，结束执行===============", "SYSTEM");

        }
    }

    public boolean getConnectionParam() {
        boolean connectStatus = connectBhxtFtpService.getConnectionParam();
        if (!connectStatus) {
            return false;
        }
        if (StringUtils.isBlank(ConnectBhxtFtpServiceImpl.uploadFtpDirBH)) {
            logger.debug("上传文件目录为空！");
            return false;
        }
        return true;
    }

    static final int BUFFER = 1024;

    /**
     * 压缩一个来自ftp下载的文件
     *
     * @param out
     * @param basedir
     * @param ftpUtil
     * @param fileName
     * @throws IOException
     */
    private void compressFileFtp(ZipOutputStream out, String basedir, FTPUtil ftpUtil, String fileName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER);
        ftpUtil.download(basedir + fileName, outputStream);
        logger.debug("compressFileFtp：" + basedir + fileName);
        out.putNextEntry(new ZipEntry(basedir + fileName));
        outputStream.writeTo(out);
        out.closeEntry();
    }

    /**
     * 压缩ftp目录
     *
     * @param zipOut
     * @param basedir
     * @param ftpUtil
     * @throws IOException
     */
    private void compressDirFtp(ZipOutputStream zipOut, String basedir, FTPUtil ftpUtil) throws IOException {
        List<String> dirList = ftpUtil.getDicFileList(basedir);
        for (String dirName : dirList) {
            this.compressByTypeFtp(zipOut, basedir + dirName + "/", ftpUtil);
        }
    }

    /**
     * 判断是否ftp目录
     *
     * @param pathString
     * @param ftpUtil
     * @return
     * @throws IOException
     */
    private boolean isFtpDirectory(String pathString, FTPUtil ftpUtil) throws IOException {
        List<String> dirList = ftpUtil.getDicFileList(pathString);
        if (CollectionUtils.isEmpty(dirList)) {
            return false;
        }
        return true;
    }


    /**
     * 判断是ftp目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法
     *
     * @param zipOut
     * @param basedir
     * @param ftpUtil
     * @throws IOException
     */
    private void compressByTypeFtp(ZipOutputStream zipOut, String basedir, FTPUtil ftpUtil) throws IOException {
        if (this.isFtpDirectory(basedir, ftpUtil)) {
            logger.debug("压缩目录：" + basedir);
            this.compressDirFtp(zipOut, basedir, ftpUtil);
        } else {
            logger.debug("压缩目录：" + basedir);
            List<String> fileNameList = ftpUtil.getFileList(basedir);
            for (String fileName : fileNameList) {
                logger.debug("压缩文件：" + basedir + fileName);
                this.compressFileFtp(zipOut, basedir, ftpUtil, fileName);
            }

        }

    }

    /**
     * 执行压缩ftp操作
     *
     * @param srcPathName
     * @param ftpUtil
     * @param jschSftpUtils
     * @param zipFileName
     * @param zipDirName
     * @return 0 上传失败 1 上传成功  2其他问题导致上传失败 如参数为空等
     * @throws Exception
     */
    private int excuteFtpCompressUpload(String srcPathName, FTPUtil ftpUtil, JschSftpUtils jschSftpUtils, String zipFileName, String zipDirName) throws Exception {
        Assert.notNull(ftpUtil, "没有初始化ftpClient");
        Assert.notNull(jschSftpUtils, "没有初始化jschsft");
        if (!this.isFtpDirectory(srcPathName, ftpUtil)) {
            logger.info("没有找到这个下载目录：" + srcPathName);
            return 2;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        CheckedOutputStream cos = null;
        ZipOutputStream out = null;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(BUFFER * 10);
            cos = new CheckedOutputStream(byteArrayOutputStream, new Adler32());
            out = new ZipOutputStream(cos);
            this.compressByTypeFtp(out, srcPathName, ftpUtil);
            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            boolean uploadStatus = this.uploadBohaiFtp(zipDirName, zipFileName, byteArrayInputStream, jschSftpUtils);
            if (!uploadStatus) {
                //status 0失败 1 成功
                logger.info("上传文件：" + zipFileName + "失败！");
                return 0;
            }
//            savaLoadFile(byteArrayInputStream,zipFileName,zipDirName);
            logger.info("上传文件：" + zipFileName + "成功！");
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("执行压缩操作时发生异常:" + e);
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }

    private boolean uploadBohaiFtp(String zipDirName, String zipFileName, ByteArrayInputStream byteArrayInputStream, JschSftpUtils jschSftpUtils) {
        String fileName = zipFileName + ".zip";
        jschSftpUtils.changeDir(zipDirName);
        logger.info("上传文件当前目录：" + jschSftpUtils.currentDir());
        try {
            boolean uploadStatus = jschSftpUtils.uploadFile(zipDirName, fileName, byteArrayInputStream);
            if (uploadStatus) {
                logger.debug("上传" + zipFileName + "成功！");
                return true;
            }
            logger.debug("上传" + zipFileName + "失败!");
            //删除上传失败的文件
            if (jschSftpUtils.existFile(fileName)) {
                jschSftpUtils.delFile(fileName);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加一条上传记录
     *
     * @param appNo
     * @param contractNum
     * @param status
     */
    private void insetUploadFtpLog(String appNo, String contractNum, int status) {
        UploadFtpFileLog uploadFtpFileLog = new UploadFtpFileLog();
        uploadFtpFileLog.setId(sequencesService.getSequences(SequencesEnum.UPLOAD_FTP_FILE_LOG));
        uploadFtpFileLog.setAppNo(appNo);
        uploadFtpFileLog.setContractNum(contractNum);
        uploadFtpFileLog.setStatus(status);
        iUploadFtpFileLogDao.insert(uploadFtpFileLog);
    }

    /**
     * 修改一条上传记录信息
     *
     * @param uploadFtpFileLog
     */
    private void updateUploadFtpLog(UploadFtpFileLog uploadFtpFileLog, int status) {
        int recordtimes = uploadFtpFileLog.getRecordtimes() + 1;
        uploadFtpFileLog.setRecordtimes(recordtimes);
        if (status != 2) {
            uploadFtpFileLog.setStatus(status);
        }
        iUploadFtpFileLogDao.updateByPrimaryKeySelective(uploadFtpFileLog);
    }

    /**
     * 上传文件到渤海服务器
     *
     * @param workbook
     * @param fileName
     * @param projectCode
     * @return
     */
    public Boolean uploadFileToBHServer(Workbook workbook, String fileName, String projectCode) {
        boolean status = false;
        JschSftpUtils jschSftpUtils = null;
        try {
            jschSftpUtils = connectBhxtFtpService.getFtpBhxtConnectJsch();
            if (!jschSftpUtils.login()) {
                logger.info("登录渤海ftp服务器失败!");
                return false;
            }
            Map<String, InputStream> inputStreamMap = new HashMap<String, InputStream>();
            InputStream inputStreamXls = requestManagementService.outStreamToInSteam(workbook);
            inputStreamMap.put(fileName, inputStreamXls);
            status = requestManagementService.uploadFtpBHXT(jschSftpUtils, inputStreamMap, projectCode);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG, "上传文件到渤海服务器异常：" + e.getMessage());
        } finally {
            if (jschSftpUtils != null) {
                jschSftpUtils.logout();
            }
        }
        return status;
    }

    public Boolean uploadLoandetail(String projectCode, Date curryDays) {
        boolean status = false;
        Date nextDate = Dates.addDay(curryDays, -1);
        String date = Dates.getDateTime(nextDate, Dates.DEFAULT_DATE_FORMAT);
        String fileNameXLS = requestManagementService.getRequestManagementFileName(ReqManagerFileTypeEnum.放款明细xls,nextDate,"001", projectCode);
        Workbook workbook = requestManagementService.getLoanDetailWorkbook(date, fileNameXLS, projectCode);
        status = this.uploadFileToBHServer(workbook, fileNameXLS, projectCode);
        return status;
    }
    /**
     * 还款计划上传JOB--渤海
     */
    public void executeRepayPlanUploadBH() {
        logger.info("开始执行还款计划上传JOB");
        Date curryDate = Dates.getNow();
        this.executeRepayPlanUploadBHService(curryDate);
    }

    public void executeRepayPlanUploadBHService(Date curryDays){
        logger.info("开始执行还款计划上传JOB");
        loanLogService.createLog("executeRepayPlanUploadBHService", "info", "开始执行还款计划上传JOB！===============", "SYSTEM");
        List<String> projectCodeList = new ArrayList<>();
        String isRepayPlanUploadBH2 = sysParamDefineService.getSysParamValue("sysJob", "isRepayPlanUploadBH2");
        if (!Strings.isEmpty(isRepayPlanUploadBH2) && "1".equals(isRepayPlanUploadBH2)) {
            projectCodeList.add(RequestManagementConst.TRUST_PROJECT_CODE2);//渤海2
        }
        String isRepayPlanUploadHRBH = sysParamDefineService.getSysParamValue("sysJob", "isRepayPlanUploadHRBH");
        if (!Strings.isEmpty(isRepayPlanUploadHRBH) && "1".equals(isRepayPlanUploadHRBH)) {
            projectCodeList.add(RequestManagementConst.TRUST_PROJECT_CODE3);//华瑞渤海
        }
        if (CollectionUtils.isEmpty(projectCodeList)) {
            logger.info("还款计划上传job关闭！");
            loanLogService.createLog("executeRepayPlanUploadBHService", "info", "还款计划上传job关闭！===============", "SYSTEM");
            return;
        }
        try{
            for(String projectCode:projectCodeList){
                try {
                    boolean status = this.uploadRepayPlan(projectCode, curryDays);
                    if (status) {
                        logger.info("执行还款计划上传job成功！");
                        loanLogService.createLog("executeRepayPlanUploadBHService", "info", Strings.format("{0}执行还款计划上传job成功===============",projectCode), "SYSTEM");
                        continue;
                    }
                } catch (PlatformException pe) {
                    loanLogService.createLog("executeRepayPlanUploadBHService", "info", Strings.format("{0}执行还款计划上传job异常：" + pe.getMessage() + "===============",projectCode), "SYSTEM");
                }
                logger.info("执行还款计划上传job失败！");
                loanLogService.createLog("executeRepayPlanUploadBHService", "info", Strings.format("{0}执行还款计划上传job失败===============",projectCode), "SYSTEM");
            }
        }catch (Exception e){
            e.printStackTrace();
            loanLogService.createLog("executeRepayPlanUploadBHService", "info", "执行还款计划上传job异常："+e.getMessage()+"===============", "SYSTEM");
        }finally {
            loanLogService.createLog("executeRepayPlanUploadBHService", "info", "还款计划上传job结束！===============", "SYSTEM");
        }
    }

    /**
     * 上传还款计划xls格式
     *
     * @param projectCode
     * @param curryDays
     * @return
     */
    public boolean uploadRepayPlan(String projectCode, Date curryDays) {
        boolean status = false;
        String payPlanFileName = requestManagementService.getRequestManagementFileName(ReqManagerFileTypeEnum.还款计划xls, "001", projectCode);
        Date beforeDays = Dates.addDay(curryDays, -1);
        Workbook workbook = requestManagementService.getPayPlanWorkbook(Dates.getDateTime(beforeDays, Dates.DEFAULT_DATE_FORMAT), payPlanFileName, projectCode);
        status = this.uploadFileToBHServer(workbook, payPlanFileName, projectCode);
        return status;
    }
}
