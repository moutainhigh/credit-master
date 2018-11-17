package com.zdmoney.credit.repay.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.ThirdOfferStateEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerOperateTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.file.FileUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.grant.dao.pub.ILoanOfferInfoDao;
import com.zdmoney.credit.grant.domain.LoanOfferInfo;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferDao;
import com.zdmoney.credit.payment.domain.ThirdLineOffer;
import com.zdmoney.credit.repay.dao.pub.IApplyBookInfoDao;
import com.zdmoney.credit.repay.dao.pub.IRequestFileOperateRecordDao;
import com.zdmoney.credit.repay.dao.pub.IRequestManagementDao;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.repay.vo.ApplyBookInfo;
import com.zdmoney.credit.repay.vo.LoanApplyDetailVo;
import com.zdmoney.credit.repay.vo.RequestFileOperateRecord;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zendaimoney.esignature.websvc.DealProcessorService;
import com.zendaimoney.esignature.websvc.req.EsignVo;
import com.zendaimoney.esignature.websvc.req.Request;
import com.zendaimoney.esignature.websvc.req.SealInfoVo;
import com.zendaimoney.esignature.websvc.req.SealVo;
import com.zendaimoney.esignature.websvc.resp.Response;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.olap4j.impl.ArrayMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Service
public class RequestManagementServiceImpl implements IRequestManagementService {
    
    private Logger logger = LoggerFactory.getLogger(RequestManagementServiceImpl.class);
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

    public Pager queryBatchNumInfo(Map<String, Object> params) {
        return loanBaseDao.queryBatchNumInfo(params);
    }

    public Map<String, Object> queryApplyPdfInfo(Map<String, Object> params) {
        return requestManagementDao.queryApplyPdfInfo(params);
    }

    public List<LoanApplyDetailVo> queryLoanApplyDetailList(Map<String, Object> params) {
        return requestManagementDao.queryLoanApplyDetailList(params);
    }

    public List<Map<String, Object>> queryPayPlanList(Map<String, Object> params) {
        return requestManagementDao.queryPayPlanList(params);
    }
    
    public ByteArrayOutputStream getApplyPdfOutput(String batchNum, String path) {
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
            // 查询划拨申请书统计信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("batchNum", batchNum);
            Map<String, Object> map = this.queryApplyPdfInfo(params);
            if(null == map){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询不到划拨申请书统计信息！");
            }
            String projectCode = this.getProjectCode(batchNum);
            if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode) || RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                BigDecimal diffMoney = new BigDecimal("0.00");
                String fundsSources = this.getFundsSources(projectCode);
                if (!isUploadApplyBook(batchNum)) {
                    diffMoney = this.getDiffMoney(batchNum, fundsSources);
                }
                map.put("diffMoney", diffMoney);
            }
            // 读取pdf模板文件
            reader = new PdfReader(path);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            this.fillPdfFile(form,map,projectCode);
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
        } catch (PlatformException e) {
            logger.error("导出划拨申请书pdf文件异常：", e);
        } catch (Exception e) {
            logger.error("导出划拨申请书pdf文件异常：", e);
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

    private void fillPdfFile(AcroFields form,Map<String, Object> map,String projectCode) throws IOException,DocumentException{

        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        if(RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)) {
            //渤海信托
            // 放款笔数
            Integer quantity = Strings.convertValue(map.get("quantity"), Integer.class);
            // 放款本金总额
            BigDecimal money = (BigDecimal) map.get("money");
            String requestDate = year + "/" + month + "/" + day;
            String stampDate = year + "年" + month + "月" + day + "日";
            // 设置pdf文件表单域的值
            form.setField("requestDate", requestDate);
            form.setField("loanMoney", NumberUtil.formatAmount(money));
            form.setField("loanMoneyCapital", NumberUtil.numberToChinese(money));
            form.setField("quantity", quantity.toString());
            form.setField("stampDate", stampDate);
//            form.setField("reviewDate", stampDate);
        }else if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode) || RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
            //渤海2
            // 笔数
            Integer quantity = Strings.convertValue(map.get("quantity"), Integer.class);
            // 应放款贷款金额
            BigDecimal money = (BigDecimal) map.get("money");
            // 调减金额
            BigDecimal diffMoney = (BigDecimal) map.get("diffMoney");
            //实际申请金额
            String applyMoney =   NumberUtil.formatAmount(money.subtract(diffMoney));
            String requestDate = year + "-" + month + "-" + day;
            String stampDate = year + "年" + month + "月" + day + "日";
            form.setField("requestDate",requestDate);
            form.setField("loanMoney",NumberUtil.formatAmount(money));
            form.setField("quantity",quantity.toString());
            form.setField("reduceAmount",NumberUtil.formatAmount(diffMoney));
            form.setField("factReqAmount",applyMoney);
            form.setField("stampDate", stampDate);
//            form.setField("reviewDate", stampDate);
        }
    }
    public Workbook getApplyWorkbook(String batchNum, String fileName, String path) {
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
            String pdfFileName = file.getName();
            // pdf文件后缀名称
            String suffixName = pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1);
            if(!"xls".equalsIgnoreCase(suffixName) && !"xlsx".equalsIgnoreCase(suffixName)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "划拨申请书excel模板文件格式不正确！");
            }
            // 查询划拨申请书统计信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("batchNum", batchNum);
            Map<String, Object> map = this.queryApplyPdfInfo(params);
            if(null == map){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询不到划拨申请书统计信息！");
            }
            String projectCode = this.getProjectCode(batchNum);
/*            if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)) {
                BigDecimal diffMoney = new BigDecimal("0.00");
                String fundsSources = this.getFundsSources(projectCode);
                if (!isUploadApplyBook(batchNum) && isCurrentDayDownloadFirst(fundsSources,batchNum)) {
                    diffMoney = this.getDiffMoney(batchNum, fundsSources);
                }
                map.put("diffMoney", diffMoney);
            }*/
            // 模板文件输入流
            in = new FileInputStream(file);
            workbook = new HSSFWorkbook(in);
            // 填充excel模板数据
            if(RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)){
                if(!this.fillExcelFile(workbook,map)){
                    throw new PlatformException(ResponseEnum.FULL_MSG, "填充excel模板数据异常！");
                }
            }else if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode) || RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                BigDecimal diffMoney = new BigDecimal("0.00");
                String fundsSources = this.getFundsSources(projectCode);
                if (!isUploadApplyBook(batchNum)) {
                    diffMoney = this.getDiffMoney(batchNum, fundsSources);
                }
                map.put("diffMoney", diffMoney);
                if(!this.fillExcelFileBh2(workbook, map)){
                    throw new PlatformException(ResponseEnum.FULL_MSG, "填充excel模板数据异常！");
                }
            }
        } catch (PlatformException e) {
            workbook = null;
            logger.error("导出划拨申请书excel文件异常：", e);
        } catch (Exception e) {
            workbook = null;
            logger.error("导出划拨申请书excel文件异常：", e);
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
        // 放款笔数
        Integer quantity = Strings.convertValue(map.get("quantity"),Integer.class);
        // 放款本金总额
        BigDecimal money = (BigDecimal) map.get("money");
        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String requestDate = year + "/" + month + "/" + day;
        String stampDate = year + "年" + month + "月" + day + "日";
        // 金额格式化
        String loanMoney =  NumberUtil.formatAmount(money);
        // 金额转大写
        String loanMoneyCapital = NumberUtil.numberToChinese(money);
        try {
            /** excel单元格数据填充 **/
            Sheet sheet = workbook.getSheetAt(0);
            // 设置申请时间
            Cell cell = sheet.getRow(4).getCell(0);
            cell.setCellValue(requestDate);
            // 设置放款本金
            cell = sheet.getRow(16).getCell(1);
            cell.setCellValue(loanMoney);
            // 设置放款本金
            cell = sheet.getRow(16).getCell(2);
            cell.setCellValue(loanMoneyCapital);
            // 设置放款笔数
            cell = sheet.getRow(16).getCell(5);
            cell.setCellValue(quantity.toString());
            // 设置盖章时间
            cell = sheet.getRow(30).getCell(2);
            cell.setCellValue(stampDate);
            // 设置复核确认时间
//            cell = sheet.getRow(35).getCell(2);
//            cell.setCellValue(stampDate);
            return true;
        } catch (Exception e) {
            logger.error("填充excel模板数据异常：", e);
            return false;
        }
    }
    /**
     * 填充渤海2 拨款申请书xls
     * @param workbook
     * @param map
     * @return
     */
    private boolean fillExcelFileBh2(Workbook workbook, Map<String, Object> map) {
        // 放款笔数
        Integer quantity = Strings.convertValue(map.get("quantity"),Integer.class);
        // 贷款本金总额
        BigDecimal money = (BigDecimal) map.get("money");
        // 调减金额
        BigDecimal diffMoney = (BigDecimal) map.get("diffMoney");
        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String requestDate = year + "-" + month + "-" + day;
        String stampDate = year + "年" + month + "月" + day + "日";

        // 金额格式化
        String loanMoney =  NumberUtil.formatAmount(money);
        // 金额转大写
        String loanMoneyCapital = NumberUtil.numberToChinese(money);
        //实际申请金额
        String applyMoney =   NumberUtil.formatAmount(money.subtract(diffMoney));
        try {
            /** excel单元格数据填充 **/
            Sheet sheet = workbook.getSheetAt(0);
            // 设置申请时间
            Cell cell = sheet.getRow(4).getCell(0);
            cell.setCellValue(requestDate);
            // 设置放款本金
            cell = sheet.getRow(16).getCell(1);
            cell.setCellValue(loanMoney);
            // 设置放款笔数
            cell = sheet.getRow(16).getCell(2);
            cell.setCellValue(quantity.toString());
            // 设置调减金额
            cell = sheet.getRow(16).getCell(3);
            cell.setCellValue(NumberUtil.formatAmount(diffMoney));
            // 设置实际申请金额
            cell = sheet.getRow(16).getCell(4);
            cell.setCellValue(applyMoney);
            // 设置盖章时间
            cell = sheet.getRow(30).getCell(2);
            cell.setCellValue(stampDate);
            // 设置复核确认时间
//            cell = sheet.getRow(37).getCell(2);
//            cell.setCellValue(stampDate);
            return true;
        } catch (Exception e) {
            logger.error("填充excel模板数据异常：", e);
            return false;
        }
    }

    public Workbook getLoanApplyWorkbook(String batchNum, String fileName, Map<String,Object> param) {
        Workbook workbook = null;
        try {
            // 查询放款申请明细
            Map<String,Object> params = new HashMap<String,Object>();
            if(param != null && param.containsKey("apply")){
            	params.put("apply", param.get("apply"));
            }
            params.put("batchNum", batchNum);
            List<LoanApplyDetailVo> loanApplyDetailList = this.queryLoanApplyDetailList(params);
            if(CollectionUtils.isEmpty(loanApplyDetailList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            // 编辑放款申请明细数据
            this.editLoanApplyDetail(loanApplyDetailList,fileName,this.getProjectCode(batchNum),false);
            // 工作表名称
            String sheetName = "放款申请明细";
            // 创建工作簿
            workbook = ExcelExportUtil.createExcelByVo(fileName, RequestManagementConst.getLoanApplyDetailLabels(), RequestManagementConst.getLoanApplyDetailFields(),
                    loanApplyDetailList, sheetName);
        } catch (PlatformException e) {
            logger.error("导出放款申请明细异常：", e);
        } catch (Exception e) {
            logger.error("导出放款申请明细异常：", e);
        }
        return workbook;
    }
    
    public Workbook getPayPlanWorkbook(String batchNum, String fileName) {
        Workbook workbook = null;
        try {
            // 查询放款申请明细
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("batchNum", batchNum);
            List<Map<String, Object>> payPlanList = this.queryPayPlanList(params);
            if (CollectionUtils.isEmpty(payPlanList)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            // 工作表名称
            String sheetName = "还款计划";
            // 创建工作簿
            workbook = ExcelExportUtil.createExcelByMap(fileName,RequestManagementConst.getPayPlanLabels(), RequestManagementConst.getPayPlanFields(), 
                        payPlanList, sheetName);
        } catch (PlatformException e) {
            logger.error("导出还款计划异常：", e);
        } catch (Exception e) {
            logger.error("导出还款计划异常：", e);
        }
        return workbook;
    }

    @Override
    public Workbook getPayPlanWorkbook(String date, String fileName, String projectCode) {
        Workbook workbook = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)) {
                params.put("fundsSource", FundsSourcesTypeEnum.渤海2.getValue());
            }else if(RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)){
                params.put("fundsSource", FundsSourcesTypeEnum.渤海信托.getValue());
            }else if (RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                params.put("fundsSource", FundsSourcesTypeEnum.华瑞渤海.getValue());
            }
            params.put("grantMoneyDate", date);
            params.put("loanState", LoanStateEnum.正常.getValue());
            params.put("loanFlowState", LoanFlowStateEnum.正常.getValue());
            List<Map<String, Object>> payPlanList = this.queryPayPlanList(params);
            if (CollectionUtils.isEmpty(payPlanList)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            // 工作表名称
            String sheetName = "还款计划";
            // 创建工作簿
            workbook = ExcelExportUtil.createExcelByMap(fileName, RequestManagementConst.getPayPlanLabels(), RequestManagementConst.getPayPlanFields(),
                    payPlanList, sheetName);
            if (workbook == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
        }catch (PlatformException e){
            logger.error("导出还款计划异常：", e);
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        }catch (Exception e){
            logger.error("导出还款计划异常：", e);
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        }
        return workbook;
    }

    public OutputStream getLoanApplyTxtOutput(String batchNum, String fileName) {
        OutputStream os = null;
        try {
            // 查询放款申请明细
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("batchNum", batchNum);
            params.put("apply", "apply");
            List<LoanApplyDetailVo> loanApplyDetailList = this.queryLoanApplyDetailList(params);
            if (CollectionUtils.isEmpty(loanApplyDetailList)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            // 编辑放款申请明细数据
            this.editLoanApplyDetail(loanApplyDetailList,fileName,this.getProjectCode(batchNum),false);
            // 文本文件数据写入输出流
            os = FileUtil.write(loanApplyDetailList, RequestManagementConst.getLoanApplyDetailFields(),RequestManagementConst.SEPARATOR);
        } catch (PlatformException e) {
            logger.error("导出还款计划异常：", e);
        } catch (Exception e) {
            logger.error("导出还款计划异常：", e);
        }
        return os;
    }

    /**
     * 创建放款明细excel文件对象 项目简码 ZDCF01
     * @param grantMoneyDate
     * @param fileName
     * @return
     */
    public Workbook getLoanDetailWorkbook(String grantMoneyDate, String fileName) {
        return this.getLoanDetailWorkbook(grantMoneyDate,fileName,RequestManagementConst.TRUST_PROJECT_CODE);
    }

    @Override
    public Workbook getLoanGrantDetailWorkbook(String batchNum, String fileName) {
        Workbook workbook = null;
        try {
            // 查询放款申请明细
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("batchNum", batchNum);
            params.put("loanState", LoanStateEnum.正常.getValue());
            params.put("loanFlowState", LoanFlowStateEnum.正常.getValue());
            List<LoanApplyDetailVo> loanApplyDetailList = this.queryLoanApplyDetailList(params);
            if(CollectionUtils.isEmpty(loanApplyDetailList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            // 编辑放款申请明细数据
            this.editLoanApplyDetail(loanApplyDetailList, fileName,this.getProjectCode(batchNum),false);
            // 工作表名称
            String sheetName = "放款明细";
            // 创建工作簿
            workbook = ExcelExportUtil.createExcelByVo(fileName, RequestManagementConst.getLoanApplyDetailLabels(), RequestManagementConst.getLoanApplyDetailFields(),
                    loanApplyDetailList, sheetName);
        } catch (PlatformException e) {
            logger.error("导出放款明细异常：", e);
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        } catch (Exception e) {
            logger.error("导出放款明细异常：", e);
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        }
        return workbook;
    }

    @Override
    public Workbook getLoanDetailWorkbook(String grantMoneyDate, String fileName, String projectCode) {
        Workbook workbook = null;
        try {
            // 查询放款申请明细
            Map<String,Object> params = new HashMap<String,Object>();
            if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)) {
                params.put("fundsSource", FundsSourcesTypeEnum.渤海2.getValue());
            }else if(RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)){
                params.put("fundsSource", FundsSourcesTypeEnum.渤海信托.getValue());
            }else if (RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                params.put("fundsSource", FundsSourcesTypeEnum.华瑞渤海.getValue());
            }
            params.put("grantMoneyDate", grantMoneyDate);
            params.put("loanState", LoanStateEnum.正常.getValue());
            List<LoanApplyDetailVo> loanApplyDetailList = this.queryLoanApplyDetailList(params);
            if(CollectionUtils.isEmpty(loanApplyDetailList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            // 编辑放款申请明细数据
            this.editLoanApplyDetail(loanApplyDetailList, fileName,projectCode,false);
            // 工作表名称
            String sheetName = "放款明细";
            // 创建工作簿
            workbook = ExcelExportUtil.createExcelByVo(fileName, RequestManagementConst.getLoanApplyDetailLabels(), RequestManagementConst.getLoanApplyDetailFields(),
                    loanApplyDetailList, sheetName);
        } catch (PlatformException e) {
            logger.error("导出放款明细异常：", e);
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        } catch (Exception e) {
            logger.error("导出放款明细异常：", e);
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        }
        return workbook;
    }

    public OutputStream getLoanDetailTxtOutput(String grantMoneyDate, String fileName) {
        return this.getLoanDetailTxtOutput(grantMoneyDate,fileName,RequestManagementConst.TRUST_PROJECT_CODE);
    }

    @Override
    public OutputStream getLoanDetailTxtOutput(String grantMoneyDate, String fileName, String projectCode) {
        OutputStream os = null;
        try {
            // 查询放款申请明细
            Map<String, Object> params = new HashMap<String, Object>();
            if (RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)) {
                params.put("fundsSource", FundsSourcesTypeEnum.渤海信托.getValue());
            }else if(RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode)){
                params.put("fundsSource", FundsSourcesTypeEnum.渤海2.getValue());
            }else if (RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                params.put("fundsSource", FundsSourcesTypeEnum.华瑞渤海.getValue());
            }
            params.put("grantMoneyDate", grantMoneyDate);
            params.put("loanState", LoanStateEnum.正常.getValue());
            List<LoanApplyDetailVo> loanApplyDetailList = this.queryLoanApplyDetailList(params);
            if (CollectionUtils.isEmpty(loanApplyDetailList)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            // 编辑放款申请明细数据
            this.editLoanApplyDetail(loanApplyDetailList, fileName,projectCode,true);
            // 文本文件数据写入输出流
            os = FileUtil.write(loanApplyDetailList, RequestManagementConst.getLoanApplyDetailFields(),RequestManagementConst.SEPARATOR);
        } catch (PlatformException e) {
            logger.error("导出放款明细异常：", e);
        } catch (Exception e) {
            logger.error("导出放款明细异常：", e);
        }
        return os;
    }

    /**
     * 编辑放款申请明细数据 默认为渤海信托--ZDCF01
     * @param loanApplyDetailList
     * @param fileName
     */
    private void editLoanApplyDetail(List<LoanApplyDetailVo> loanApplyDetailList, String fileName) {
        this.editLoanApplyDetail(loanApplyDetailList,fileName,RequestManagementConst.TRUST_PROJECT_CODE,false);
    }
    /**
     * 编辑放款申请明细数据
     * @param loanApplyDetailList
     * @param fileName
     */
    private void editLoanApplyDetail(List<LoanApplyDetailVo> loanApplyDetailList, String fileName,String projectCode,boolean isBatchNumToFileseq){
        String  fileBatchNum = "";
        String pdfName = "";
        if(!isBatchNumToFileseq){
            // 获取文件批次号
            fileBatchNum = fileName.substring(fileName.lastIndexOf(".") - 12, fileName.lastIndexOf("."));
            // 获取划拨申请书pdf文件名称
            pdfName = this.getRequestManagementFileName(ReqManagerFileTypeEnum.划拨申请书pdf, fileBatchNum,projectCode);
        }
        for(LoanApplyDetailVo vo : loanApplyDetailList) {
        	/*
        	BigDecimal rate = new BigDecimal("1.1");
        	LoanProduct loanProduct = loanProductDao.findByLoanId(vo.getLoanId());        	
        	if(loanProduct != null && loanProduct.getRateem() != null){
        		rate = loanProduct.getRateem().multiply(new BigDecimal(100));
        	}
        	vo.setRate(rate);
        	*/
            if (!isBatchNumToFileseq) {
                vo.setApplyName(pdfName);
                continue;
            }
            // 获取文件批次号
           fileBatchNum = this.getFileSeqByBatchNum(vo.getBatchNum());
            // 获取划拨申请书pdf文件名称
           pdfName = this.getRequestManagementFileName(ReqManagerFileTypeEnum.划拨申请书pdf, fileBatchNum,projectCode);
            vo.setApplyName(pdfName);
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

    @Override
    public Map<String, InputStream> getUploadApplyFileInputMap(String batchNum,String fileBatchNum) throws IOException{
        Map<String,InputStream> inputMap=new HashMap<>();
        String projectCode = this.getProjectCode(batchNum);
        String fundsSources = this.getFundsSources(projectCode);
        String previousBatchNum = this.previousBatchNumIsALLBackOffer(batchNum,fundsSources);
        if (Strings.isEmpty(previousBatchNum)) {
            return inputMap;
        }
        //获取当天文件下载批次号
        String fileBatcNumSeq= this.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.划拨申请书pdf.getFileType(), previousBatchNum);
        //还款计划excel
        String payPlanFileName =this.getRequestManagementFileName(ReqManagerFileTypeEnum.还款计划xls,fileBatcNumSeq,projectCode);
        Workbook payPlanWorkbook= this.getPayPlanWorkbook(previousBatchNum, payPlanFileName);
        Assert.notNull(payPlanWorkbook,"生成还款计划excel文件失败！");
        ByteArrayInputStream payPlanInput=this.outStreamToInSteam(payPlanWorkbook);
        Assert.notNull(payPlanInput,"获取还款计划excel输入流失败！");
        inputMap.put(payPlanFileName,payPlanInput);
        //放款明细excel
        String loanGrantFileName =this.getRequestManagementFileName(ReqManagerFileTypeEnum.放款明细xls,fileBatcNumSeq,projectCode);
        Workbook loanGrantWorkbook= this.getLoanGrantDetailWorkbook(previousBatchNum, loanGrantFileName);
        Assert.notNull(loanGrantWorkbook,"生成放款明细excel文件失败！");
        ByteArrayInputStream loanGrantInput=this.outStreamToInSteam(loanGrantWorkbook);
        Assert.notNull(loanGrantInput,"获取放款明细excel输入流失败！");
        inputMap.put(loanGrantFileName,loanGrantInput);
        //放款申请明细excel
        String loanApplyfileName = this.getRequestManagementFileName(ReqManagerFileTypeEnum.放款申请书xls,fileBatchNum,projectCode);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("apply", "apply");
        Workbook loanApplyWorkbook=this.getLoanApplyWorkbook(previousBatchNum, loanApplyfileName, param);
        Assert.notNull(loanApplyWorkbook,"生成放款申请明细excel文件失败！");
        InputStream loanApplyInput=this.outStreamToInSteam(loanApplyWorkbook);
        Assert.notNull(loanApplyInput,"获取放款申请明细excel输入流失败！");
        inputMap.put(loanApplyfileName,loanApplyInput);
        return inputMap;
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
    public String getRequestManagerFileToDayBatchNum(String fileType,String batchNum) {
        String projectCode = this.getProjectCode(batchNum);
        String fundsSources = this.getFundsSources(projectCode);
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("fileType",fileType);
        paramMap.put("batchNum",batchNum);
        paramMap.put("operateDate",new Date());
        paramMap.put("fundsSources",fundsSources);
        String fileBatchNum= requestFileOperateRecordDao.findRequestFileBatchNum(paramMap);
        return fileBatchNum;
    }

    @Override
    public void createRequestManagerOperateRecord(String batchNum, String fileType, String operateType, String fileBatchNum) {
        String fileSeq=Strings.leftExcludeChar(fileBatchNum.substring(9),'0');;
        RequestFileOperateRecord record=new RequestFileOperateRecord();
            record.setBatchNum(batchNum);
            record.setFileType(ReqManagerFileTypeEnum.划拨申请书pdf.getFileType());
            record.setOperateType(operateType);
        List<RequestFileOperateRecord> recordList=requestFileOperateRecordDao.findRequestFileOperateRecord(record);
        if(CollectionUtils.isEmpty(recordList)){
            //当天第一次下载新增
            record.setId(new BigDecimal(sequencesService.getSequences(SequencesEnum.REQUEST_FILE_OPERATE_RECORD)));
            record.setOperateDate(Dates.getCurrDate());
            record.setTimes(1);
            record.setFileSeq(fileSeq);
            requestFileOperateRecordDao.insert(record);
            return;
        }
        record=recordList.get(0);
        record.setTimes(record.getTimes()+1);
        requestFileOperateRecordDao.updateByPrimaryKeySelective(record);
    }
    
    @Override
    public String getRequestManagementFileName(ReqManagerFileTypeEnum reqFileTypeEnum, String fileBatchNum) {
        return this.getRequestManagementFileName(reqFileTypeEnum,fileBatchNum,RequestManagementConst.TRUST_PROJECT_CODE);
    }

    @Override
    public String getRequestManagementFileName(ReqManagerFileTypeEnum reqFileTypeEnum, String fileBatchNum, String  projectCode) {
        // 下载文件名称编辑
        StringBuilder buildFileName = new StringBuilder(projectCode);
        buildFileName.append(reqFileTypeEnum.getFileCode());
        if (fileBatchNum.length()<=3) {
            buildFileName.append(Dates.getDateTime(new Date(), "yyyyMMdd"));
            buildFileName.append("_");
        }
        buildFileName.append(fileBatchNum);
        buildFileName.append(reqFileTypeEnum.getFileExtension());
        String fileName = buildFileName.toString();
        return fileName;
    }
    
    @Override
    public String getRequestManagementFileName(ReqManagerFileTypeEnum reqFileTypeEnum, Date date, String fileBatchNum, String  projectCode) {
        // 下载文件名称编辑
        StringBuilder buildFileName = new StringBuilder(projectCode);
        buildFileName.append(reqFileTypeEnum.getFileCode());
        if (fileBatchNum.length()<=3) {
            buildFileName.append(Dates.getDateTime(date, "yyyyMMdd"));
            buildFileName.append("_");
        }
        buildFileName.append(fileBatchNum);
        buildFileName.append(reqFileTypeEnum.getFileExtension());
        String fileName = buildFileName.toString();
        return fileName;
    }

	@Override
	public String findFileSeqByBatchNum(String batchNum, String fileType) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fileType", fileType);
		paramMap.put("operateType", ReqManagerOperateTypeEnum.downLoad.getOperateType()); //下载类型
		paramMap.put("operateDate", Dates.getDateTime("yyyy-MM-dd"));
		//获取当天该类型文件下载序号
		String fileSeq = requestFileOperateRecordDao.findFileSeqByParam(paramMap);
		if(Strings.isEmpty(fileSeq) || "0".equals(fileSeq)){
			return "1";
		}
		paramMap.put("batchNum", batchNum);
		//获取当天该批次文件下载序号
		String thisFileSeq = requestFileOperateRecordDao.findFileSeqByParam(paramMap);
		int seq = Integer.valueOf(fileSeq);
		if(Strings.isEmpty(thisFileSeq) || "0".equals(thisFileSeq)){
			seq++;
			thisFileSeq = String.valueOf(seq);
		}
		
		return thisFileSeq;
	}
	
    @Override
    public String getProjectCode(String batchNum) {
        String org = batchNum.substring(0,4);
        if (org.equals("BHXT")) {
            return RequestManagementConst.TRUST_PROJECT_CODE;
        }else if (org.equals("BH2-")) {
            return  RequestManagementConst.TRUST_PROJECT_CODE2;
        }else if (org.equals("HRBH")) {
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
            if(fileName.indexOf(ReqManagerFileTypeEnum.划拨申请书xls.getFileCode())!=-1){
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
            if(fileName.indexOf(ReqManagerFileTypeEnum.划拨申请书xls.getFileCode())!=-1){
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

    private String getFileSeqByBatchNum(String batchNum){
        Map<String,String> map=new HashMap<>();
        String fileSeq=map.get(batchNum);
        if(Strings.isEmpty(fileSeq)){
            fileSeq = this.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.划拨申请书pdf.getFileType(),batchNum);
            map.put(batchNum,fileSeq);
            return fileSeq;
        }
        return fileSeq;
    }
    
    @Override
    public void checkRequestManagerOperateRecord(String batchNum, String fileType, String operateType, String fileBatchNum) {
        String fileSeq=Strings.leftExcludeChar(fileBatchNum,'0');
        RequestFileOperateRecord record=new RequestFileOperateRecord();
            record.setBatchNum(batchNum);
            record.setFileType(fileType);
            record.setOperateType(operateType);
            record.setOperateDate(Dates.getCurrDate());
        List<RequestFileOperateRecord> recordList=requestFileOperateRecordDao.findRequestFileOperateRecord(record);
        if(CollectionUtils.isEmpty(recordList)){
            //当天第一次下载新增
            record.setId(new BigDecimal(sequencesService.getSequences(SequencesEnum.REQUEST_FILE_OPERATE_RECORD)));
            record.setOperateDate(Dates.getCurrDate());
            record.setTimes(1);
            record.setFileSeq(fileSeq);
            requestFileOperateRecordDao.insert(record);
            return;
        }
        record=recordList.get(0);
        record.setTimes(record.getTimes()+1);
        record.setFileSeq(fileSeq);
        record.setOperateDate(Dates.getCurrDate());
        record.setUpdateTime(new Date());
        requestFileOperateRecordDao.updateByPrimaryKeySelective(record);
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

    @Override
    public void createApplyBookInfos(String batchNum, String fileBatchNum) {
        if (!isUploadApplyBook(batchNum)) {
            ApplyBookInfo applyBookInfo = this.getApplyBookInfo(batchNum, fileBatchNum);
            insertApplyBookInfo(applyBookInfo);
        }
    }


    public ApplyBookInfo  getApplyBookInfo(String batchNum,String fileBatchNum){
        String projectCode = this.getProjectCode(batchNum);
        String fundsSources = this.getFundsSources(projectCode);
        ApplyBookInfo applyBookInfo = new ApplyBookInfo();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("batchNum", batchNum);
        Map<String, Object> map = this.queryApplyPdfInfo(params);
        //申请笔数
        Integer quantity = Strings.convertValue(map.get("quantity"), Integer.class);
        // 应放款金额
        BigDecimal money = (BigDecimal) map.get("money");
        //调减金额
        BigDecimal diffMoney = new BigDecimal("0.00");
        //是否是当天第一条申请书信息
//        if (isCurrentDayFirstBarApplyBookInfo(batchNum,fundsSources)) {
            diffMoney = this.getDiffMoney(batchNum, fundsSources);
//        }
        //实际申请金额
        BigDecimal applyMoney = money.subtract(diffMoney);

        applyBookInfo.setFileName(fileBatchNum);
        applyBookInfo.setBatchNum(batchNum);
        applyBookInfo.setGrantMoney(money);
        applyBookInfo.setLoanTimes(quantity);
        applyBookInfo.setDiffMoney(diffMoney);
        applyBookInfo.setApplyMoney(applyMoney);
        return applyBookInfo;
    }
    /**
     * 获取调减金额
     * @param batchNum
     * @param fundsSources
     * @return
     */
    public BigDecimal getDiffMoney(String batchNum,String fundsSources){
        String previousBatchNum = this.getPreviousBatchNum(fundsSources,batchNum);
        if (previousBatchNum == "") {
            return new BigDecimal("0.00");
        }
        List<ApplyBookInfo> applyBookInfos = this.queryPreviousDayApplyBookInfo(previousBatchNum,fundsSources);
        if (CollectionUtils.isEmpty(applyBookInfos)) {
            return new BigDecimal("0.00");
        }
        //前一个批次所在当天的应放款总金额
        BigDecimal toTalGrantMoney = this.getToTalGrantMoney(applyBookInfos);
        List<String> previousBatchNums = this.getPreviousBatchNums(applyBookInfos);
        BigDecimal alreadyGrantMoney = requestManagementDao.queryAlreadyGrantMoney(previousBatchNums);
        BigDecimal diffMoney = toTalGrantMoney.subtract(alreadyGrantMoney);
        if (diffMoney.compareTo(new BigDecimal("0.00")) == -1) {
            return new BigDecimal("0.00");
        }
        return diffMoney;
    }
    /**
     * 是否是今天第一天 划拨申请书记录
     * @param batchNum
     * @return 是 true
     */
    public boolean isCurrentDayFirstBarApplyBookInfo(String batchNum,String fundsSources){
        int count = applyBookInfoDao.queryCurrentDayCount(fundsSources);
        if (count > 0) {
            return false;
        }
        return true;
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
     *获取当前打包批次的前一个批次
     * @param fundsSources
     * @param batchNum
     * @return
     */
    public String getPreviousBatchNum(String fundsSources,String batchNum){
        List<String> batchNums = loanBaseDao.findBatchNumByFundsSources(fundsSources);
        int index = batchNums.indexOf(batchNum);
        if (index == -1){
            return "";
        }
        if (index == 0){
            return "";
        }
        return batchNums.get(index - 1);
    }

    public List<ApplyBookInfo> queryPreviousDayApplyBookInfo(String previousBatchNum,String fundsSources){
        ApplyBookInfo applyBookInfo = applyBookInfoDao.queryApplyBookInfoBybatchNum(previousBatchNum);
        if (applyBookInfo == null) {
            return null;
        }
        List<ApplyBookInfo> applyBookInfoList = new ArrayList<>();
        applyBookInfoList.add(applyBookInfo);
//        Date previousDay = applyBookInfo.getCreateTime();
//        List<ApplyBookInfo> applyBookInfoList = applyBookInfoDao.queryDayApplyBookInfos(fundsSources,previousDay);
        return applyBookInfoList;
    }

    private BigDecimal getToTalGrantMoney(List<ApplyBookInfo> applyBookInfos){
        BigDecimal money = new BigDecimal("0.00");
        for(ApplyBookInfo applyBookInfo:applyBookInfos){
            money = money.add(applyBookInfo.getGrantMoney());
        }
        return money;
    }

    private List<String> getPreviousBatchNums(List<ApplyBookInfo> applyBookInfos){
        List<String> batchNums = new ArrayList<>();
        for(ApplyBookInfo applyBookInfo:applyBookInfos){
            batchNums.add(applyBookInfo.getBatchNum());
        }
        return batchNums;
    }

    public void  insertApplyBookInfo(ApplyBookInfo applyBookInfo){
        applyBookInfo.setId(sequencesService.getSequences(SequencesEnum.APPLY_BOOK_INFO));
        User user = UserContext.getUser();
        if (user == null) {
            applyBookInfo.setUpdator("admin");
        } else {
            applyBookInfo.setUpdator(user.getName());
        }
        applyBookInfo.setUpdateTime(new Date());
        applyBookInfoDao.insert(applyBookInfo);
    }

    private boolean isCurrentDayDownloadFirst(String fundsSources,String batchNum){
        Map<String,Object>  paramMap = new ArrayMap<>();
        paramMap.put("fundsSources",fundsSources);
        paramMap.put("fileType",ReqManagerFileTypeEnum.划拨申请书pdf.getFileType());
        paramMap.put("operateType",ReqManagerOperateTypeEnum.downLoad.getOperateType());
        paramMap.put("createTime",new Date());
        List<RequestFileOperateRecord> recordList = requestFileOperateRecordDao.queryCurrentDayRequestFileOperateRecordByAsc(paramMap);
        if (CollectionUtils.isEmpty(recordList)) {
            return  true;
        }
        RequestFileOperateRecord firstRecord = recordList.get(0);
        if (!batchNum.equals(firstRecord.getBatchNum())) {
            return false;
        }
        return true;
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

    public void isALLBackOffer(String bacthNum){
        Map<String,Object> params = new HashMap<>();
        params.put("batchNum",bacthNum);
       /* List<ThirdLineOffer> thirdLineOffers = thirdLineOfferDao.findOfferLineOfferByMap(params);
        if (CollectionUtils.isEmpty(thirdLineOffers)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}未生成报盘文件",bacthNum));
        }
        for(ThirdLineOffer thirdLineOffer:thirdLineOffers) {
            String thirdLineOfferState = thirdLineOffer.getState();
            if (ThirdOfferStateEnum.未报盘.getValue().equals(thirdLineOfferState)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}有未报盘的债权",bacthNum));
            }
            if (ThirdOfferStateEnum.已报盘.getValue().equals(thirdLineOfferState)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}有已报盘还没回盘的债权",bacthNum));
            }
        }*/
        //判断前一个批次号线上线下是否全部回盘
        List<VLoanInfo> vLoanInfos = vLoanInfoDao.findListByMap(params);
        if (CollectionUtils.isEmpty(vLoanInfos)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}下没有债权",bacthNum));
        }
        for (int i = 0; i < vLoanInfos.size(); i++) {
        	Long loanId = vLoanInfos.get(i).getId();
        	 Map<String,Object> paramMap = new HashMap<>();
        	 paramMap.put("loanId",loanId);
        	 //查询线下放款报盘信息（多条）-- third_line_offer
        	 List<ThirdLineOffer> thirdLineOffers = thirdLineOfferDao.findListByMap(paramMap);
        	 //查询线上还款申请记录（多条）--loan_offer_info
        	 List<LoanOfferInfo> loanOfferinfos = loanOfferinfoDao.findListByMap(paramMap);
        	 if(CollectionUtils.isEmpty(thirdLineOffers)&&CollectionUtils.isEmpty(loanOfferinfos)){
        		 throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}未生成报盘文件",bacthNum));
        	 }
        	 if(!CollectionUtils.isEmpty(thirdLineOffers)){
        		 for(ThirdLineOffer thirdLineOffer:thirdLineOffers) {
        			 String thirdLineOfferState = thirdLineOffer.getState();
        			 if (ThirdOfferStateEnum.未报盘.getValue().equals(thirdLineOfferState)) {
        				 throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}有未报盘的债权",bacthNum));
        			 }
        			 if (ThirdOfferStateEnum.已报盘.getValue().equals(thirdLineOfferState)) {
        				 throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}有已报盘还没回盘的债权",bacthNum));
        			 }
        		 }       		 
        	 }
        	 if(!CollectionUtils.isEmpty(loanOfferinfos)){
        		 for(LoanOfferInfo loanOfferinfo:loanOfferinfos) {
        			 String loanOfferinfoState = loanOfferinfo.getState();
        			 if (ThirdOfferStateEnum.未报盘.getValue().equals(loanOfferinfoState)) {
        				 throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}有未报盘的债权",bacthNum));
        			 }
        			 if (ThirdOfferStateEnum.已报盘.getValue().equals(loanOfferinfoState)) {
        				 throw new PlatformException(ResponseEnum.FULL_MSG,Strings.format("上一个批次号{0}有已报盘还没回盘的债权",bacthNum));
        			 }
        		 }      		 
        	 }
		}
    }

    @Override
    public String previousBatchNumIsALLBackOffer(String currentBacthNum,String fundsSources) {
        String previousBatchNum = this.getPreviousBatchNum(fundsSources,currentBacthNum);
        if (Strings.isEmpty(previousBatchNum)) {
            return "";
        }
        //判断当前批次号的上一个批次号是否全部回盘
        this.isALLBackOffer(previousBatchNum);
        return previousBatchNum;
    }
}
