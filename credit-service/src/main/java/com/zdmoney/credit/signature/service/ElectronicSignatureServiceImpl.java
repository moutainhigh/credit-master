package com.zdmoney.credit.signature.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerOperateTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.common.constant.SignatureFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.signature.dao.pub.IElectronicSignatureLogDao;
import com.zdmoney.credit.signature.domain.ElectronicSignatureLog;
import com.zdmoney.credit.signature.service.pub.IElectronicSignatureService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * Created by ym10094 on 2016/12/5.
 */
@Service
public class ElectronicSignatureServiceImpl implements IElectronicSignatureService{
    public static Logger log = LoggerFactory.getLogger(ElectronicSignatureServiceImpl.class);

    @Autowired
    private IRequestManagementService requestManagementService;
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private IElectronicSignatureLogDao electronicSignatureLogDao;
    @Autowired
    private  IVLoanInfoDao  ivLoanInfoDao;

    public static final String zsStoreSignatureImageFileDir = "/S8";//政审存放需要签章的图片文件的目录(咨询服务费)
    public static final String zsStoreSignatureSucceFileDir = "/S8_PDF";//政审存放签章成功的pdf文件的目录(咨询服务费)
    public static final String zsStoreloanProtocolFileDir = "/S1";//政审存放贷款协议
    public static final String zsStoreloanProtocolPdfFileDir = "/S1_PDF";//政审存放政审存放贷款协议的pdf文件的目录
    public static final String zsStoreRiskFileDir = "/S11"; //政审存放风险金协议
    public static final String zsStoreRiskPdfFileDir = "/S11_PDF" ; //政审存放政审存风险金协议的pdf文件的目录
    public static final String zsStoreAccreditFileDir = "/S3"; //政审存放证大委托授权书
    public static final String zsStoreAccreditPdfFileDir = "/S3_PDF"; //政审存放政审存证大委托授权书的pdf文件的目录


    @Override
    public void absElectronicSignatureBatch(){
        FTPUtil zsftpService=new FTPUtil();
        FTPUtil hxFtpService = null;
        try {
            //连接ftp服务器
            zsftpService.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, ConnectBhxtFtpServiceImpl.userName, ConnectBhxtFtpServiceImpl.password, ConnectBhxtFtpServiceImpl.storeDir, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
//        	zsftpService.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, ConnectBhxtFtpServiceImpl.pdfUserName, ConnectBhxtFtpServiceImpl.pdfPassword, ConnectBhxtFtpServiceImpl.storeDir, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
            log.info("连接政审ftp服务器成功!");
            hxFtpService = connectBhxtFtpService.getFtpEsignatureConnect();
            if(hxFtpService == null){
                throw new PlatformException(ResponseEnum.FULL_MSG,"连接服务器失败！");
            }
            List<VLoanInfo> vLoanInfoList = ivLoanInfoDao.findWMXT2ElectronicSignatureVLoanInfo();
            if (CollectionUtils.isEmpty(vLoanInfoList)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"没有需要签章跟转换格式的债权！");
            }
            for(VLoanInfo vLoanInfo:vLoanInfoList) {
                ///开始循环
                boolean signatureStatus = false;
                boolean loanProtocolPdfFileStatus = false;
                try {
                    signatureStatus = absElectronicSignatureByAppNo(zsftpService, hxFtpService, vLoanInfo);
                    loanProtocolPdfFileStatus = absCreatePdfFileByAppNo(zsftpService, vLoanInfo);
                }catch (PlatformException e){
                    log.info(e.getMessage());
                }catch (Exception e) {
                    e.printStackTrace();
                }
                ElectronicSignatureLog signatureLog = this.findElectronicSignatureLogByAppNo(vLoanInfo.getAppNo());
                int status = signatureStatus && loanProtocolPdfFileStatus ? 1 : 2;
                if (signatureLog == null) {
                    this.insertElectronicSignatureLog(vLoanInfo.getAppNo(),vLoanInfo.getContractNum(), status);
                    continue;
                }
                this.updateElectronicSignatureLog(signatureLog, status);
                //循环结束
            }
        }catch (Exception e){
            try {
                zsftpService.closeServer();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean absElectronicSignatureByAppNo(FTPUtil zsftpService,FTPUtil hxFtpService,VLoanInfo vLoanInfo) throws Exception {
        String fundsSource = vLoanInfo.getFundsSources();
        String appNo = vLoanInfo.getAppNo();
        String path = ConnectBhxtFtpServiceImpl.storeDir + vLoanInfo.getAppNo();
        log.info("开始对合同来源:{}appNo：{}的文件签章",fundsSource,appNo);
        Map<String,String> signatureFileDirMap = this.getSignatureDir(fundsSource);
        boolean succeStatus = false;
        for(Map.Entry<String,String> entry:signatureFileDirMap.entrySet()){
            String downDir = entry.getKey();
            String upDir = entry.getValue();
            succeStatus = this.absElectronicSignatureSingle(zsftpService, hxFtpService, path, downDir,upDir);
            if (!succeStatus) {
                return false;
            }
        }
        log.info("appNo:{}签章完成",appNo);
        return succeStatus;
    }
    public boolean absElectronicSignatureSingle(FTPUtil zsftpService,FTPUtil hxFtpService,String path,String downDir,String upDir) throws Exception {
        log.info("开始对{}目录下的文件签章",path);
        String imageDir = path + downDir;//政审服务器存放签章图片的目录
        String imageFileName = getFileName(imageDir, zsftpService,upDir.substring(1));
        if (Strings.isEmpty(imageFileName)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,String.format("目录%s下没有需要盖章的图片",imageDir));
        }
        List<ByteArrayOutputStream> signatureImageOutList = this.downloadDirAllFiles(imageDir, zsftpService);
        OutputStream pdfOutStream = this.createPdfFile(signatureImageOutList);
        InputStream pdfInStream = requestManagementService.outStreamToInSteam(pdfOutStream);
        ByteArrayOutputStream pdfStamperOutStream = (ByteArrayOutputStream) this.addImpress(pdfInStream);
        ByteArrayInputStream pdfStamperInStream = new ByteArrayInputStream(pdfStamperOutStream.toByteArray());
        String pdfFileName = imageFileName +".pdf";
        ByteArrayOutputStream uploadSignatureOutStream = (ByteArrayOutputStream) this.dealEsignature(pdfFileName, GatewayConstant.PROJ_NO, hxFtpService, pdfStamperInStream);
        InputStream uploadSignatureInStream = new ByteArrayInputStream(uploadSignatureOutStream.toByteArray());
        boolean succeStatus = this.uploadFile(zsftpService, path, uploadSignatureInStream,pdfFileName,upDir);
        pdfOutStream.close();
        log.info("对{}目录下的文件签章完成",path);
        return succeStatus;
    }

    public String getFileName(String path,FTPUtil ftpUtil,String filePrefix) throws IOException {
        log.info("获取{}下的文件名",path);
        List<String> fileNameList = ftpUtil.getFileList(path);
        if (CollectionUtils.isEmpty(fileNameList)) {
            return "";
        }
        String fileName = fileNameList.get(0);
        fileName =  fileName.substring(0,fileName.indexOf("."));
        try {
            int idex = fileName.indexOf("-");
            if (idex == -1){
                return filePrefix + fileName;
            }
            fileName = fileName.substring(idex);
            if (Strings.isEmpty(fileName)) {
                return filePrefix + fileName;
            }
        }catch (Exception e){
            e.printStackTrace();
            return filePrefix + fileName;
        }
        return  filePrefix + fileName;
    }
    /**
     *下载某个目录下的文件组
     * @param path
     * @param ftpUtil
     * @return
     * @throws IOException
     */
    public  List<ByteArrayOutputStream> downloadDirAllFiles(String path,FTPUtil ftpUtil) throws IOException {
        List<ByteArrayOutputStream> byteArrayOutputStreamList = new ArrayList<>();
        List<String> fileNameList = ftpUtil.getFileListAsc(path);
        ftpUtil.changeDirectory(path);
        for(String fileName:fileNameList) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ftpUtil.download(fileName, byteArrayOutputStream);
            byteArrayOutputStreamList.add(byteArrayOutputStream);
        }
        return byteArrayOutputStreamList;
    }

    @Override
    public OutputStream createPdfFile(List<ByteArrayOutputStream> byteArrayOutputStreamList) throws Exception {
        if (CollectionUtils.isEmpty(byteArrayOutputStreamList)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"生成pdf文件流不能为空");
        }
        OutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();//创建文本对象 默认A4，36，36，36，36
            PdfWriter.getInstance(document, outputStream);//初始化 pdf输出对象
            document.open();
            for (ByteArrayOutputStream byteArrayOutputStream : byteArrayOutputStreamList) {
                document.newPage();
                Image image = Image.getInstance(byteArrayOutputStream.toByteArray());
                float heigth = image.getHeight();
                float width = image.getWidth();
                int percent = getPercent2(heigth, width);
                image.setAlignment(Image.MIDDLE);
                image.scalePercent(percent);// 表示是原来图像的比例;
                document.add(image);
            }
            document.close();
        return outputStream;
    }
    /**
     * 按照宽度压缩
     *
     * @param h
     * @param w
     */
    public static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }

    public OutputStream addImpress(InputStream inputStream) throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        PdfReader pdfReader = new PdfReader(inputStream);
        PdfStamper pdfStamper = new PdfStamper(pdfReader,outputStream);
        Rectangle pageRect = null;
        int conutPage = pdfStamper.getReader().getNumberOfPages();
        pageRect = pdfStamper.getReader(). getPageSizeWithRotation(1);
        float x = pageRect.getWidth()-pageRect.getWidth()/7;
        float y = pageRect.getHeight()/4;
        PdfContentByte over = pdfStamper.getOverContent(conutPage);
        PdfGState pdfGState = new PdfGState();
        BaseColor color = new BaseColor(240,170,207);
        pdfGState.setFillOpacity(0.6f);
        pdfGState.setStrokeOpacity(0.6f);
        over.beginText();
        BaseFont bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        over.setFontAndSize(bf, 5);
        over.setTextMatrix(30, 30);
        over.setGState(pdfGState);
        over.setColorFill(color);
        over.showTextAligned(Element.ALIGN_RIGHT, RequestManagementConst.APPLY_COMMPAY_CHOP_NAME, x, y, 0);
        over.endText();
        pdfStamper.close();
        pdfReader.close();
        return outputStream;
    }

    @Override
    public OutputStream dealEsignature(String fileName, String projectCode, FTPUtil ftpUtil,InputStream in) throws IOException {
        OutputStream out = null;
        //上传文件到核心的签章服务器
        String  httpFilePath=requestManagementService.uploadEsignatureFile(in, ConnectBhxtFtpServiceImpl.esignatureFtpUploadPath, fileName, projectCode, ftpUtil);
        if(Strings.isEmpty(httpFilePath)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"上传签章文件到服务器失败！");
        }
        //调用签章接口签章
        String httpUrl =requestManagementService.dealEsignature(httpFilePath, fileName);
        if (Strings.isEmpty(httpUrl)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"签章失败！");
        }
        //从远程的签章服务器获取签章完成的文件
        out = requestManagementService.downloadEsignatureFileByHttpUrl(httpUrl);
        //已签完章的文件上传到核心签章服务器上供下载。
        requestManagementService.uploadEsignatureFile(requestManagementService.outStreamToInSteam(out), ConnectBhxtFtpServiceImpl.esignatureFtpDownloadPath, fileName,projectCode,ftpUtil);
        return out;
    }

    public ElectronicSignatureLog findElectronicSignatureLogByAppNo(String appNo){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appNo",appNo);
        return electronicSignatureLogDao.findElectronicSignatureLog(paramMap);
    }

    /**
     * 添加签章记录
     * @param appNo
     * @param status
     */
    public void insertElectronicSignatureLog(String appNo,String contractNum,int status){
        ElectronicSignatureLog log = new ElectronicSignatureLog();
        log.setId(sequencesService.getSequences(SequencesEnum.ELECTRONIC_SIGNATURE_LOG));
        log.setAppNo(appNo);
        log.setContractNum(contractNum);
        log.setSignatureBelong("abs");
        log.setSignatureName(RequestManagementConst.APPLY_COMMPAY_CHOP_NAME);
        log.setUpdateTime(new Date());
        log.setStatus(status);
        log.setFileType(SignatureFileTypeEnum.咨询服务协议.name());
        electronicSignatureLogDao.insert(log);
    }

    public void updateElectronicSignatureLog(ElectronicSignatureLog signatureLog,int status){
        Long signatureCount = signatureLog.getSignatureCount();
        signatureCount = signatureCount + 1;
        signatureLog.setSignatureCount(signatureCount);
        signatureLog.setStatus(status);
        signatureLog.setUpdateTime(new Date());
        electronicSignatureLogDao.update(signatureLog);
    }

    /**
     * 根据appNo把政审系统的（imag）转换成pdf
     * @param appNoFtpService
     * @param vLoanInfo
     * @return
     * @throws IOException
     */
    public boolean absCreatePdfFileByAppNo(FTPUtil appNoFtpService,VLoanInfo vLoanInfo) throws Exception {
        String fundsSource = vLoanInfo.getFundsSources();
        String appNo = vLoanInfo.getAppNo();
        String path = ConnectBhxtFtpServiceImpl.storeDir + vLoanInfo.getAppNo();
        log.info("开始对合同来源:{}appNo：{}生成pdf",fundsSource,appNo);
        Map<String,String> pdfFileDirMap = this.getCreatePdfDir(fundsSource);
        boolean status = false;
        for(Map.Entry<String,String> entry:pdfFileDirMap.entrySet()){
            String downDir = entry.getKey();
            String upDir = entry.getValue();
            status = this.absCreatePdfFileSingle(appNoFtpService,path,downDir,upDir);
            if (!status) {
                return false;
            }
        }
        log.info("appNo:{}生成pdf完成",appNo);
        return status;
    }
    public boolean absCreatePdfFileSingle(FTPUtil appNoFtpService,String path,String downDir,String upDir) throws Exception{
        log.info("开始对{}目录下的图片生成pdf文件",path);
        String imageDir = path + downDir;//政审存贷款服务协议书（ipg 格式）
        String imageFileName = getFileName(imageDir, appNoFtpService, upDir.substring(1));
        if (Strings.isEmpty(imageFileName)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,String.format("目录%s下没有需要转换成pdf的图片",imageDir));
        }
        List<ByteArrayOutputStream> byteArrayOutputStreamList = this.downloadDirAllFiles(imageDir, appNoFtpService);
        OutputStream pdfOutStream = this.createPdfFile(byteArrayOutputStreamList);
        InputStream pdfInStream = requestManagementService.outStreamToInSteam(pdfOutStream);
        String pdfFileName = imageFileName + ".pdf";
        boolean uploadStatus = this.uploadFile(appNoFtpService, path, pdfInStream, pdfFileName, upDir);
        pdfOutStream.close();
        log.info("对{}目录下的图片生成pdf文件结束",path);
        return uploadStatus;
    }

    public boolean uploadFile(FTPUtil ftpUtil,String path,InputStream in,String fileName,String dir) throws IOException {
        String directory = path + dir;//签章成功pdf文件的存放目录

        if(!ftpUtil.changeDirectory(directory)){
            if(!ftpUtil.changeDirectory(path)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"找不到需要上传的父目录");
            }
            if(!ftpUtil.createDirectory(directory)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"上传目录不存在！");
            }
            if(!ftpUtil.changeDirectory(directory)){
                throw new PlatformException(ResponseEnum.FULL_MSG,String.format("切换新创建存放文件的目录%s失败%s"),dir);
            }
        }
        boolean succeStatus = false;
        succeStatus = ftpUtil.uploadFile(in,fileName);
        return succeStatus;
    }

    public Map<String,String> getSignatureDir(String fundsSource){
        Map<String,String> dirMap = new HashMap<String,String>();
        if (FundsSourcesTypeEnum.外贸2.getValue().equals(fundsSource)){
            dirMap.put(zsStoreSignatureImageFileDir,zsStoreSignatureSucceFileDir);
            return dirMap;
        }
        if (FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)){
            dirMap.put(zsStoreSignatureImageFileDir, zsStoreSignatureSucceFileDir);
            return dirMap;
        }
        if ("包商银行".equals(fundsSource)){
            dirMap.put(zsStoreSignatureImageFileDir,zsStoreSignatureSucceFileDir);
            dirMap.put(zsStoreRiskFileDir,zsStoreRiskPdfFileDir);
            return dirMap;
        }
        return dirMap;
    }

    public Map<String,String> getCreatePdfDir(String fundsSource){
        Map<String,String> dirMap = new HashMap<String,String>();
        if (FundsSourcesTypeEnum.外贸2.getValue().equals(fundsSource)){
            dirMap.put(zsStoreloanProtocolFileDir,zsStoreloanProtocolPdfFileDir);
            return dirMap;
        }
        if (FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)){
            dirMap.put(zsStoreloanProtocolFileDir, zsStoreloanProtocolPdfFileDir);
            return dirMap;
        }
        if ("包商银行".equals(fundsSource)){
            dirMap.put(zsStoreAccreditFileDir,zsStoreAccreditPdfFileDir);
            return dirMap;
        }
        return dirMap;
    }
}
