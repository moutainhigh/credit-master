package com.zdmoney.credit.repay.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.constant.repay.YubokuanConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.NumberToChinese;
import com.zdmoney.credit.common.util.NumberUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.grant.dao.pub.ILoanOfferInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferDao;
import com.zdmoney.credit.repay.dao.pub.IApplyBookInfoDao;
import com.zdmoney.credit.repay.dao.pub.IRequestFileOperateRecordDao;
import com.zdmoney.credit.repay.dao.pub.IRequestManagementDao;
import com.zdmoney.credit.repay.dao.pub.IYubokuanDao;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;
import com.zdmoney.credit.repay.service.pub.IYubokuanService;
import com.zdmoney.credit.repay.vo.ApplyBookInfo;
import com.zdmoney.credit.repay.vo.LoanApplyDetailVo;
import com.zdmoney.credit.repay.vo.YubokuanDetailVo;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zendaimoney.esignature.websvc.DealProcessorService;
import com.zendaimoney.esignature.websvc.req.EsignVo;
import com.zendaimoney.esignature.websvc.req.Request;
import com.zendaimoney.esignature.websvc.req.SealInfoVo;
import com.zendaimoney.esignature.websvc.req.SealVo;
import com.zendaimoney.esignature.websvc.resp.Response;

@Service
public class YubokuanServiceImpl implements IYubokuanService {
    
    private Logger logger = LoggerFactory.getLogger(YubokuanServiceImpl.class);
    @Autowired
    private IRequestManagementDao requestManagementDao;

    @Autowired
    private ILoanBaseDao loanBaseDao;
    @Autowired
    private IRequestFileOperateRecordDao requestFileOperateRecordDao;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    @Autowired
    private DealProcessorService dealProcessorWsService;
	@Autowired
    private IApplyBookInfoDao applyBookInfoDao;
    @Autowired
    private ILoanLogService loanLogService;
    @Autowired
    private IThirdLineOfferDao thirdLineOfferDao;
    @Autowired
    private IVLoanInfoDao vLoanInfoDao;   
    @Autowired
    private  ILoanOfferInfoDao loanOfferinfoDao;
    @Autowired
    private  IYubokuanDao yubokuanDao;

    public Pager queryPageList(Map<String, Object> params) {
        return yubokuanDao.queryPageList(params);
    }
    
    public ByteArrayOutputStream getApplyPdfOutput(Map<String, Object> params, String path) {
        PdfReader reader = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper = null;
        try {
            if(Strings.isEmpty(path)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有配置划拨申请书pdf模板文件的存放路径！");
            }
            File file = new File(path);
            if(!file.exists()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书pdf模板文件不存在！");
            }
            if(!file.isFile()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书pdf模板文件不能是目录！");
            }
            // 划拨申请书pdf模板文件名称
            String pdfFileName = file.getName();
            // pdf文件后缀名称
            String suffixName = pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1);
            if(!"pdf".equalsIgnoreCase(suffixName)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书pdf模板文件格式不正确！");
            }          
            // 读取pdf模板文件
            reader = new PdfReader(path);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            this.fillPdfFile(form,params);
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
        } catch (PlatformException e) {
            logger.error("获取划拨申请书PDF文件异常：", e);
        } catch (Exception e) {
            logger.error("获取划拨申请书PDF文件异常：", e);
        } finally {
            try {
                if(null!= stamper){
                    stamper.close();
                }
                if(null!= bos){
                    bos.close();
                }
                if(null!= reader){
                    reader.close();
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos;
    }

    private void fillPdfFile(AcroFields form,Map<String, Object> map) throws IOException,DocumentException{

        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String applyDate = year + "年" + month + "月" + day + "日";
        // 预拨款金额
        BigDecimal yuboAmt = new BigDecimal(map.get("yuboAmt").toString());
        // 理财机构码
        String organization =  (String) map.get("organization");
        //大写金额
        String chineseAmt = NumberUtil.numberToChinese(yuboAmt);
        // 设置pdf文件表单域的值
        form.setField("yuboAmt", NumberUtil.formatAmount(yuboAmt));
        form.setField("applyDate", applyDate);
        form.setField("chineseAmt", chineseAmt);
        if("华瑞渤海".equals(organization)){
        	 form.setField("name", "渤海信托·华瑞证大个人消费产品");
        }else{
        	form.setField("name", "证大个人消费贷款产品");
        }
    }
    public Workbook getApplyWorkbook(Map<String, Object> params, String path) {
        Workbook workbook = null;
        InputStream in = null;
        try {
            if(Strings.isEmpty(path)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有配置划拨申请书excel模板文件的存放路径！");
            }
            File file = new File(path);
            if(!file.exists()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书excel模板文件不存在！");
            }
            if(!file.isFile()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书excel模板文件不能是目录！");
            }
            // 划拨申请书pdf模板文件名称
            String excelFileName = file.getName();
            // pdf文件后缀名称
            String suffixName = excelFileName.substring(excelFileName.lastIndexOf(".") + 1);
            if(!"xls".equalsIgnoreCase(suffixName) && !"xlsx".equalsIgnoreCase(suffixName)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书excel模板文件格式不正确！");
            }
            // 模板文件输入流
            in = new FileInputStream(file);
            workbook = new HSSFWorkbook(in);
            // 填充excel模板数据
            if(!this.fillExcelFile(workbook,params)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "填充excel模板数据异常！");
            }

        } catch (PlatformException e) {
            workbook = null;
            logger.error("获取划拨申请书excel文件异常：", e);
        } catch (Exception e) {
            workbook = null;
            logger.error("获取划拨申请书excel文件异常：", e);
        } finally {
            try {
                if(null != in){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workbook;
    }

    /**
     * 填充excel模板数据
     * @param workbook
     * @param map
     */
    private boolean fillExcelFile(Workbook workbook, Map<String, Object> map) {
        // 预拨资金
        BigDecimal yuboAmt = new BigDecimal(map.get("yuboAmt").toString());        
        // 申请日期
        String stampDate = (String) map.get("applyDate");
        // 金额格式化
        String loanMoney =  NumberUtil.formatAmount(yuboAmt);
        // 金额转大写
        String loanMoneyCapital = NumberUtil.numberToChinese(yuboAmt);
        // 项目名称
        String name="";
		if("华瑞渤海".equals(map.get("organization"))){
       	 	name="渤海信托·华瑞证大个人消费产品";
        }else{
        	name="证大个人消费贷款产品";
        }
        try {
            /** excel单元格数据填充 **/
            Sheet sheet = workbook.getSheetAt(0);
            // 设置申请时间
            Cell cell = sheet.getRow(1).getCell(0);
            cell.setCellValue(stampDate);
            // 设置预拨资金
            cell = sheet.getRow(6).getCell(1);
            cell.setCellValue(loanMoney);
            // 设置预拨资金大写
            cell = sheet.getRow(6).getCell(2);
            cell.setCellValue(loanMoneyCapital);
            // 设置盖章时间
            cell = sheet.getRow(13).getCell(0);
            cell.setCellValue(stampDate);
            // 设置项目名称
            cell = sheet.getRow(4).getCell(0);
            cell.setCellValue(name);
            return true;
        } catch (Exception e) {
            logger.error("填充excel模板数据异常：", e);
            return false;
        }
    }
	@Override
    public boolean uploadFtpBHXT(JschSftpUtils jschSftpUtils,Map<String,InputStream> inputStreamMap) {
       return this.uploadFtpBHXT(jschSftpUtils,inputStreamMap,RequestManagementConst.TRUST_PROJECT_CODE);
    }

    @Override
    public boolean uploadFtpBHXT(JschSftpUtils jschSftpUtils, Map<String, InputStream> inputStreamMap, String projectCode) {
        Map<String,String> fileMap=new HashMap<String,String>();
        boolean delSatus=false;
        for(Map.Entry<String, InputStream> entry:inputStreamMap.entrySet()){
            String fileName=entry.getKey();
            String pathName=this.getUploadBhFtpPatch(fileName,projectCode);
            if(pathName == null){
                delSatus=true;
                break;
            }
            jschSftpUtils.changeDir(pathName);
            logger.info("上传文件当前目录："+jschSftpUtils.currentDir());
            boolean status=jschSftpUtils.uploadFile(pathName,fileName,entry.getValue());
            fileMap.put(fileName,pathName);
            if(!status){
                delSatus=true;
                break;
            }
        }
        if (delSatus){
            for(Map.Entry<String,String> entry:fileMap.entrySet()){
                if(jschSftpUtils.changeDir(entry.getValue())){
                    jschSftpUtils.delFile(entry.getKey());
                }
            }
            return false;
        }
        for(Map.Entry<String,String> entry:fileMap.entrySet()){
            if(jschSftpUtils.changeDir(entry.getValue())) {
                connectBhxtFtpService.uploadFlagFile(RequestManagementConst.FLAG_FILE_NAME, jschSftpUtils,entry.getValue());
                logger.info("上传项目简码：{}的文件{}成功！",projectCode,entry.getKey());
                loanLogService.createLog("RequestManagementServiceImpl.uploadFtpBHXT", "info", Strings.format("上传项目简码：{0}的文件{1}成功！",projectCode,entry.getKey()), "SYSTEM");
            }
        }
        return true;
    }  
    public ByteArrayInputStream outStreamToInSteam(Workbook workbook) throws  IOException{
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(8192);
        workbook.write(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return  byteArrayInputStream;
    }
    public InputStream outStreamToInSteam(OutputStream outSteam) throws  IOException{
        ByteArrayOutputStream byteOutStream=(ByteArrayOutputStream)outSteam;
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
        return byteInStream;
    }

    @Override
    public void createYubokuanRecord(String fundsSource, Date applyDate, String applyAmount, Long fileBatchNum,String filePath) {
    	LoanPreApplyRecord loanPreApplyRecord=new LoanPreApplyRecord();
    	loanPreApplyRecord.setId(sequencesService.getSequences(SequencesEnum.LOAN_PRE_APPLY_RECORD));
    	if (fundsSource.equals("BH2")) {
    	 fundsSource="渤海2";
        }else if (fundsSource.equals("HRBH")) {
       	 fundsSource="华瑞渤海";
        }else{
       	 fundsSource="未知";
        }
    	loanPreApplyRecord.setFundsSource(fundsSource);
    	loanPreApplyRecord.setApplyDate(applyDate);
    	loanPreApplyRecord.setApplyAmount(new BigDecimal(applyAmount));
    	loanPreApplyRecord.setFileSeq(fileBatchNum);
    	loanPreApplyRecord.setFilePath(filePath);
    	loanPreApplyRecord.setStatus1("2");
    	loanPreApplyRecord.setStatus2("1");
    	loanPreApplyRecord.setStatus3("1");
    	yubokuanDao.insert(loanPreApplyRecord);
    }
	
    @Override
    public String getProjectCode(String organization) {
        if (organization.equals("BHXT")) {
            return RequestManagementConst.TRUST_PROJECT_CODE;
        }else if (organization.equals("BH2")) {
            return  RequestManagementConst.TRUST_PROJECT_CODE2;
        }else if (organization.equals("HRBH")) {
            return RequestManagementConst.TRUST_PROJECT_CODE3;
        }
        throw new PlatformException(ResponseEnum.FULL_MSG,"根据合同编码找不到对应的信托简码");
    }

    @Override
    public String uploadEsignatureFile(InputStream in,String filePath,String fileName,String projectCode,FTPUtil ftpUtil) throws IOException{
        //切换到要上传的目录
        ftpUtil.changeDirectory(filePath);
        if(!ftpUtil.isChildDirectory(filePath,projectCode)){
            if(!ftpUtil.createDirectory(filePath+"/"+projectCode)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"上传目录不存在！");
            }
        }
        ftpUtil.changeDirectory(filePath+"/"+projectCode);
        boolean uploadStatus = ftpUtil.uploadFile(in,fileName);
        if (uploadStatus) {
            return ConnectBhxtFtpServiceImpl.esignatureFtpEsignatureUrl+ConnectBhxtFtpServiceImpl.esignatureFtpUploadPath+"/"+projectCode;
        }
        return "";
    }

    @Override
    public String dealEsignature(String httpUrl, String fileName) {
        //定义盖章实体
        EsignVo body=new EsignVo();
        List<SealVo> sealFiles = new ArrayList<SealVo>();
        SealVo sealVo = new SealVo();
        sealVo.setFileNo(fileName);
        sealVo.setFilePath(httpUrl+"/"+fileName);
        sealVo.setIsSecondSign("1");
        List<SealInfoVo> sealInfos = new ArrayList<SealInfoVo>();// 一个文件中请求盖章的列表
        SealInfoVo sealInfoVo1 = new SealInfoVo();
        sealInfoVo1.setSealType("0");
        sealInfoVo1.setName(RequestManagementConst.APPLY_COMMPAY_CHOP_NAME);
        sealInfos.add(sealInfoVo1);
        sealVo.setSealInfos(sealInfos);
        sealFiles.add(sealVo);
        body.setSealFiles(sealFiles);
        //组装盖章参数
        String reqId = UUID.randomUUID().toString().substring(0, 8) + RequestManagementConst.ESIGNATURE_REQUEST_CODE;
        Request request = new Request();
            request.setSysId(RequestManagementConst.ESIGNATURE_REQUEST_SYSID);
            request.setReqId(reqId);
            request.setReqCode(RequestManagementConst.ESIGNATURE_REQUEST_CODE);
            request.setBody(body);
        logger.info("发送签章接口参数："+ JSONUtil.toJSON(request));
        //调用盖章接口
        try {
            Response response = dealProcessorWsService.dispatchCommand(request);
            logger.info("调用签章接口响应结果：" + JSONUtil.toJSON(response));
            if (null == response || !RequestManagementConst.ESIGNATURE_SUCCESS.equals(response.getCode())) {
                logger.info("签章失败");
                return null;
            }
            List<SealVo> list = response.getSealFiles();
            if (CollectionUtils.isEmpty(list)) {
                logger.info("签章失败");
                return null;
            }
            for (SealVo vo : list) {
                logger.info("签章结果：code =" + vo.getCode() + ";msg=" + vo.getMsg() + "filePath=" + vo.getFilePath());
                return vo.getFilePath();
            }
        }catch (Exception e){
            logger.error("签章异常：");
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"签章失败！");
        }
        return null;
    }

    @Override
    public ByteArrayOutputStream downloadEsignatureFileByHttpUrl(String httpUrl) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        try {
            InputStream inputStream=HttpUtils.URLGet(httpUrl);
            byte[] bytes=new byte[1024];
            int i = 0;
            while((i=inputStream.read(bytes)) != -1){
                byteArrayOutputStream.write(bytes,0,i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream;
    }

    @Override
    public String isExistEsignatureFtpFile(String dirName, String fileName,FTPUtil ftpUtil) throws IOException{
        if(!ftpUtil.changeDirectory(dirName)){
            return null;
        }
        if(!ftpUtil.existFile(fileName)){
            return null;
        }
        return dirName+"/"+fileName;
    }

    @Override
    public ByteArrayOutputStream downloadEsignatureFileByFilePath(String remoteFileName, FTPUtil ftpUtil) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        ftpUtil.download(remoteFileName,out);
        return out;
    }

    private String getUploadBhFtpPatch(String fileName,String projectCode) {
        //渤海信托 ZDCF01
        if (RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)) {
            if(fileName.indexOf(ReqManagerFileTypeEnum.划拨申请书xls.getFileCode())!=-1){
                //划款申请书
                return ConnectBhxtFtpServiceImpl.applyUploadDirBH;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.放款申请书xls.getFileCode())!=-1){
                //放款明细申请
                return ConnectBhxtFtpServiceImpl.loanapplyUploadDirBH;

            }else if(fileName.indexOf(ReqManagerFileTypeEnum.还款计划xls.getFileCode())!=-1){
                //还款计划
                 return ConnectBhxtFtpServiceImpl.payplanUploadDirBH;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.放款明细xls.getFileCode())!=-1){
                return ConnectBhxtFtpServiceImpl.loandetailUploadDirBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.回款确认书.getFileCode()) != -1) {
                return ConnectBhxtFtpServiceImpl.paylogsumUploadDirBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.回款确认书pdf.getFileCode())!=-1){
                return ConnectBhxtFtpServiceImpl.paylogsumUploadDirBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.分账明细表.getFileCode())!=-1) {
                return ConnectBhxtFtpServiceImpl.paylogUploadDirBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.暂其他收款.getFileCode())!=-1) {
                return ConnectBhxtFtpServiceImpl.shortreceiptUploadDirBH;
            }

        }else if(RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)){
            //渤海2 ZDCF02
        	if(fileName.indexOf(ReqManagerFileTypeEnum.划拨确认书xls.getFileCode())!=-1){
            	return ConnectBhxtFtpServiceImpl.confirmUploadDirBH2;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.划拨申请书xls.getFileCode())!=-1){
                //划款申请书
                return ConnectBhxtFtpServiceImpl.applyUploadDirBH2;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.放款申请书xls.getFileCode())!=-1){
                //放款明细申请
                return ConnectBhxtFtpServiceImpl.loanapplyUploadDirBH2;

            }else if(fileName.indexOf(ReqManagerFileTypeEnum.还款计划xls.getFileCode())!=-1){
                //还款计划
                return ConnectBhxtFtpServiceImpl.payploanUploadDirBH2;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.放款明细xls.getFileCode())!=-1){
                return ConnectBhxtFtpServiceImpl.loandetailUploadDirBH2;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.回款确认书.getFileCode()) != -1) {
                return ConnectBhxtFtpServiceImpl.paylogsumUploadDirBH2;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.回款确认书pdf.getFileCode())!=-1){
                return ConnectBhxtFtpServiceImpl.paylogsumUploadDirBH2;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.分账明细表.getFileCode())!=-1) {
                return ConnectBhxtFtpServiceImpl.paylogUploadDirBH2;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.暂其他收款.getFileCode())!=-1) {
                return ConnectBhxtFtpServiceImpl.shortreceiptUploadDirBH2;
            } 
        }else if(RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)){
            //华瑞渤海 ZDCF03
        	if(fileName.indexOf(ReqManagerFileTypeEnum.划拨确认书xls.getFileCode())!=-1){
            	return ConnectBhxtFtpServiceImpl.confirmUploadDirHRBH;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.划拨申请书xls.getFileCode())!=-1){
                //划款申请书
                return ConnectBhxtFtpServiceImpl.applyUploadDirHRBH;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.放款申请书xls.getFileCode())!=-1){
                //放款明细申请
                return ConnectBhxtFtpServiceImpl.loanapplyUploadDirHRBH;

            }else if(fileName.indexOf(ReqManagerFileTypeEnum.还款计划xls.getFileCode())!=-1){
                //还款计划
                return ConnectBhxtFtpServiceImpl.payploanUploadDirHRBH;
            }else if(fileName.indexOf(ReqManagerFileTypeEnum.放款明细xls.getFileCode())!=-1){
                return ConnectBhxtFtpServiceImpl.loandetailUploadDirHRBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.回款确认书.getFileCode()) != -1) {
                return ConnectBhxtFtpServiceImpl.paylogsumUploadDirHRBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.回款确认书pdf.getFileCode())!=-1){
                return ConnectBhxtFtpServiceImpl.paylogsumUploadDirHRBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.分账明细表.getFileCode())!=-1) {
                return ConnectBhxtFtpServiceImpl.paylogUploadDirHRBH;
            }else if (fileName.indexOf(ReqManagerFileTypeEnum.暂其他收款.getFileCode())!=-1) {
                return ConnectBhxtFtpServiceImpl.shortreceiptUploadDirHRBH;
            } 
        }
        return "";
    }

    @Override
    public OutputStream dealEsignature(String fileName, String projectCode, FTPUtil ftpUtil,InputStream in) throws IOException {
        OutputStream out = null;
        //上传文件到核心的签章服务器
        String  httpFilePath=this.uploadEsignatureFile(in,ConnectBhxtFtpServiceImpl.esignatureFtpUploadPath,fileName,projectCode,ftpUtil);
        if(Strings.isEmpty(httpFilePath)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"上传签章文件到服务器失败！");
        }
        //调用签章接口签章
        String httpUrl =this.dealEsignature(httpFilePath, fileName);
        if (Strings.isEmpty(httpUrl)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"签章失败！");
        }
        //从远程的签章服务器获取签章完成的文件
        out = this.downloadEsignatureFileByHttpUrl(httpUrl);
        //已签完章的文件上传到核心签章服务器上供下载。
        this.uploadEsignatureFile(this.outStreamToInSteam(out),ConnectBhxtFtpServiceImpl.esignatureFtpDownloadPath,fileName,projectCode,ftpUtil);
        return out;
    }

    public String getFundsSources(String projectCode){
        if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)) {
            return FundsSourcesTypeEnum.渤海2.getValue();
        }else if(RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)){
            return FundsSourcesTypeEnum.渤海信托.getValue();
        }else if (RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
            return FundsSourcesTypeEnum.华瑞渤海.getValue();
        }
        throw new PlatformException(ResponseEnum.FULL_MSG,"根据信托简码找不到对应的合同来源！");
//        return "";
    }

    /**
     * 是否上传过划拨申请书
     * @return true 上传 false 未上传
     */
    public boolean isUploadApplyBook(String batchNum){
        ApplyBookInfo applyBookInfo = applyBookInfoDao.queryApplyBookInfoBybatchNum(batchNum);
        if (applyBookInfo == null){
            return false;
        }
        return true;
    }
	@Override
	public LoanPreApplyRecord findLastRecord(Map<String,Object> params) {
		if (params.get("organization").equals("BH2")) {
			params.put("organization", "渤海2");
        }else if (params.get("organization").equals("HRBH")) {
        	params.put("organization", "华瑞渤海");
        }else{
        	params.put("organization", "未知");
        }
		LoanPreApplyRecord loanPreApplyRecord=yubokuanDao.findLastRecord(params);
		return loanPreApplyRecord;
	}

	@Override
	public Workbook createGrantXls(LoanPreApplyRecord loanPreApplyRecord,String fileName) throws Exception {      
        // 创建放款申请明细excel文件对象
        Workbook workbook = null;
        // 查询放款成功明细     
        List<YubokuanDetailVo> loanApplyDetailList = yubokuanDao.findGrantDetailList(loanPreApplyRecord);
        if(CollectionUtils.isEmpty(loanApplyDetailList)){
            throw new PlatformException(ResponseEnum.FULL_MSG, "放款成功明细不存在！");
        }
        // 工作表名称
        String sheetName = "放款申请明细";
        // 创建工作簿
        workbook = ExcelExportUtil.createExcelByVo(fileName, YubokuanConst.getLoanApplyDetailLabels(), YubokuanConst.getLoanApplyDetailFields(),loanApplyDetailList, sheetName);

        return workbook;
	}

	@Override
	public ByteArrayOutputStream getConfirmPdfOutput(
			LoanPreApplyRecord loanPreApplyRecord, String confirmPdfFilePath) {
        PdfReader reader = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper = null;
        try {
            if(Strings.isEmpty(confirmPdfFilePath)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有配置划拨确认书pdf模板文件的存放路径！");
            }
            File file = new File(confirmPdfFilePath);
            if(!file.exists()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨确认书pdf模板文件不存在！");
            }
            if(!file.isFile()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨确认书pdf模板文件不能是目录！");
            }
            // 划拨申请书pdf模板文件名称
            String pdfFileName = file.getName();
            // pdf文件后缀名称
            String suffixName = pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1);
            if(!"pdf".equalsIgnoreCase(suffixName)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书pdf模板文件格式不正确！");
            }          
            // 读取pdf模板文件
            reader = new PdfReader(confirmPdfFilePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            this.fillConfirmPdfFile(form,loanPreApplyRecord);
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
        } catch (PlatformException e) {
            logger.error("获取划拨申请书PDF文件异常：", e);
        } catch (Exception e) {
            logger.error("获取划拨申请书PDF文件异常：", e);
        } finally {
            try {
                if(null!= stamper){
                    stamper.close();
                }
                if(null!= bos){
                    bos.close();
                }
                if(null!= reader){
                    reader.close();
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos;
    }
	private void fillConfirmPdfFile(AcroFields form,LoanPreApplyRecord loanPreApplyRecord) throws IOException,DocumentException{
        // 系统日期
        Calendar ca = Calendar.getInstance();
        ca.setTime(loanPreApplyRecord.getApplyDate());
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String applyDate = year + "年" + month + "月" + day + "日";
        // 生成日期
        Calendar ca2 = Calendar.getInstance();
        ca.setTime(Dates.getCurrDate());
        int year2 = ca2.get(Calendar.YEAR);
        int month2 = ca2.get(Calendar.MONTH) + 1;
        int day2 = ca2.get(Calendar.DAY_OF_MONTH);
        String createDate = year2 + "年" + month2 + "月" + day2 + "日";
        // 预拨款金额
        BigDecimal yuboAmt = loanPreApplyRecord.getApplyAmount();
        // 理财机构码
        String organization =  loanPreApplyRecord.getFundsSource();
        List<Map<String, Object>> params=yubokuanDao.findPreConfirmFileParams(loanPreApplyRecord);
        // 设置pdf文件表单域的值
        form.setField("yuboAmt", NumberUtil.formatAmount(yuboAmt));
        form.setField("applyDate", applyDate);
        form.setField("sumMoney",params.get(0).get("SUMMONEY").toString());//放款到帐金额
        form.setField("sumFee",params.get(0).get("SUMFEE").toString());//服务费
        form.setField("sumPactMoney",params.get(0).get("SUMPACTMONEY").toString());//贷款合同金额
        form.setField("lastAmount",loanPreApplyRecord.getApplyAmount().subtract(new BigDecimal(params.get(0).get("SUMPACTMONEY").toString())).toString());//未确认金额
        form.setField("createDate", createDate);
        if("华瑞渤海".equals(organization)){
        	 form.setField("name", "渤海信托·华瑞证大个人消费产品");
        }else{
        	form.setField("name", "证大个人消费贷款产品");
        }
    }

	@Override
	public Workbook getConfirmWorkbook(LoanPreApplyRecord loanPreApplyRecord,
			String confirmExcelFilePath) {
        Workbook workbook = null;
        InputStream in = null;
        try {
            if(Strings.isEmpty(confirmExcelFilePath)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有配置划拨确认书excel模板文件的存放路径！");
            }
            File file = new File(confirmExcelFilePath);
            if(!file.exists()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨确认书excel模板文件不存在！");
            }
            if(!file.isFile()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨确认书excel模板文件不能是目录！");
            }
            // 划拨申请书pdf模板文件名称
            String excelFileName = file.getName();
            // pdf文件后缀名称
            String suffixName = excelFileName.substring(excelFileName.lastIndexOf(".") + 1);
            if(!"xls".equalsIgnoreCase(suffixName) && !"xlsx".equalsIgnoreCase(suffixName)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书excel模板文件格式不正确！");
            }
            // 模板文件输入流
            in = new FileInputStream(file);
            workbook = new HSSFWorkbook(in);
            // 填充excel模板数据
            if(!this.fillConfirmExcelFile(workbook,loanPreApplyRecord)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "填充excel模板数据异常！");
            }

        } catch (PlatformException e) {
            workbook = null;
            logger.error("获取划拨申请书excel文件异常：", e);
        } catch (Exception e) {
            workbook = null;
            logger.error("获取划拨申请书excel文件异常：", e);
        } finally {
            try {
                if(null != in){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workbook;
    }

	private boolean fillConfirmExcelFile(Workbook workbook,
			LoanPreApplyRecord loanPreApplyRecord) {
        // 预拨资金
        BigDecimal yuboAmt = loanPreApplyRecord.getApplyAmount();   
        // 申请日期
        Calendar ca = Calendar.getInstance();
        ca.setTime(loanPreApplyRecord.getApplyDate());
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String applyDate = year + "年" + month + "月" + day + "日";
        // 生成日期
        Calendar ca2 = Calendar.getInstance();
        ca.setTime(Dates.getCurrDate());
        int year2 = ca2.get(Calendar.YEAR);
        int month2 = ca2.get(Calendar.MONTH) + 1;
        int day2 = ca2.get(Calendar.DAY_OF_MONTH);
        String createDate = year2 + "年" + month2 + "月" + day2 + "日";
        // 项目名称
        String name="";
        if("华瑞渤海".equals(loanPreApplyRecord.getFundsSource())){
        	name="渤海信托·华瑞证大个人消费产品";
        }else{
        	name= "证大个人消费贷款产品";
        }
        List<Map<String, Object>> params=yubokuanDao.findPreConfirmFileParams(loanPreApplyRecord);
        //放款到帐金额
        System.out.println(params.get(0).get("SUMMONEY"));
        String sumMoney = params.get(0).get("SUMMONEY").toString();
        //服务费
        String sumFee = params.get(0).get("SUMFEE").toString();
        //贷款合同金额
        String sumPactMoney = params.get(0).get("SUMPACTMONEY").toString();
        //未确认金额
        String lastAmount = loanPreApplyRecord.getApplyAmount().subtract(new BigDecimal(params.get(0).get("SUMPACTMONEY").toString())).toString();
        try {
            /** excel单元格数据填充 **/
            Sheet sheet = workbook.getSheetAt(0);
            // 设置申请时间
            Cell cell = sheet.getRow(1).getCell(0);
            cell.setCellValue(applyDate);
            //项目名字
            cell = sheet.getRow(4).getCell(0);
            cell.setCellValue(name);
            // 设置预拨资金
            cell = sheet.getRow(6).getCell(1);
            cell.setCellValue(yuboAmt.toString());
            // 设置放款到帐金额
            cell = sheet.getRow(6).getCell(2);
            cell.setCellValue(sumMoney);
            // 设置服务费
            cell = sheet.getRow(6).getCell(3);
            cell.setCellValue(sumFee);
            // 设置贷款合同金额
            cell = sheet.getRow(6).getCell(4);
            cell.setCellValue(sumPactMoney);
            // 设置未确认金额
            cell = sheet.getRow(6).getCell(8);
            cell.setCellValue(lastAmount);
            // 设置生成日期
            cell = sheet.getRow(10).getCell(2);
            cell.setCellValue(createDate);
            return true;
        } catch (Exception e) {
            logger.error("填充excel模板数据异常：", e);
            return false;
        }
    }

	@Override
	public Workbook createPayPlanXls(LoanPreApplyRecord loanPreApplyRecord,String fileName) throws Exception {
        Workbook workbook = null;
        // 查询放款申请明细
        List<Map<String, Object>> payPlanList = yubokuanDao.findPayPlanList(loanPreApplyRecord);
        if (CollectionUtils.isEmpty(payPlanList)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
        }
        // 工作表名称
        String sheetName = "还款计划";
        // 创建工作簿
        workbook = ExcelExportUtil.createExcelByMap(fileName,YubokuanConst.getPayPlanLabels(), YubokuanConst.getPayPlanFields(),payPlanList, sheetName);
       
        return workbook;
    }
}
