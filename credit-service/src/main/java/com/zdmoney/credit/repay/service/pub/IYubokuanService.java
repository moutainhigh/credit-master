package com.zdmoney.credit.repay.service.pub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;

public interface IYubokuanService {
    
    /**
     * 分页查询预拨款申请记录
     * @param params
     * @return
     */
    public Pager queryPageList(Map<String, Object> params);


    
    /**
     * 创建划拨申请书pdf文件输出流对象
     * @param params
     * @return
     */
    public ByteArrayOutputStream getApplyPdfOutput(Map<String, Object> params, String path);
    
    /**
     * 创建划拨申请书excel文件对象
     * @param batchNum
     * @param fileName
     * @return
     */
    public Workbook getApplyWorkbook(Map<String, Object> params, String path);

    /**
     * 上传渤海信托划款申请书 放款明细申请 还款计划 默认上传渤海信托 项目简码为 ZDCF01
     * @param jschSftpUtils
     * @param inputStreamMap
     * @return
     */
    public boolean uploadFtpBHXT(JschSftpUtils jschSftpUtils,Map<String,InputStream> inputStreamMap);
    /**
     * 上传划款申请书 放款明细申请 还款计划 到渤海的ftp 服务器
     * @param jschSftpUtils
     * @param inputStreamMap
     * @return
     */
    public boolean uploadFtpBHXT(JschSftpUtils jschSftpUtils,Map<String,InputStream> inputStreamMap,String projectCode);


    /**
     * 添加申请书文件下载上传记录
     * @param fundsSource
     * @param applyDate
     * @param applyAmount
     * @param fileBatchNum
     */
    public void createYubokuanRecord(String fundsSource, Date applyDate, String applyAmount, Long fileBatchNum,String filePath);


    /**
     * 根据债权批次号获取项目简码
     * @param batchNum
     * @return
     */
    public String getProjectCode(String batchNum);

    /**
     * 上传文件到盖章服务器上
     * @param in
     * @param fileName
     * @param projectCode
     * @return 返回盖章系统访问的http文件路径
     * @throws IOException
     */
    public String uploadEsignatureFile(InputStream in,String filePath,String fileName,String projectCode,FTPUtil ftpUtil)throws IOException;

    /**
     * 盖章
     * @param httpFilePath
     * @param fileName
     * @return
     */
    public String dealEsignature(String httpFilePath,String fileName);

    /**
     * 下载已盖章好的文件
     * @param httpUrl
     * @return
     */
    public ByteArrayOutputStream downloadEsignatureFileByHttpUrl(String httpUrl);

    /**
     * 查看是否文件是否已经签章过
     * @param dirName
     * @param fileName
     * @param ftpUtil
     * @return
     * @throws IOException
     */
    public String isExistEsignatureFtpFile(String dirName,String fileName,FTPUtil ftpUtil) throws IOException;

    /**
     *对于已经签章过的文件 直接从保存签章上传文件的ftp上下载
     * @param remoteFileName
     * @param ftpUtil
     * @return
     * @throws IOException
     */
    public ByteArrayOutputStream downloadEsignatureFileByFilePath(String remoteFileName,FTPUtil ftpUtil) throws IOException;


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
     * 是否上传过划拨申请书
     * @param batchNum
     * @return true 上传 false 未上传
     */
    public boolean isUploadApplyBook(String batchNum);

    /**
     * 查询最近一次的预拨申请记录
     * @param params 
     * @return
     */
	public LoanPreApplyRecord findLastRecord(Map<String, Object> params);
	/**
	 * 创建最近一次预拨申请记录中放款成功的放款明细文件（XLS）
	 * @param loanPreApplyRecord
	 * @param lastGrantDetailFileName 
	 * @throws Exception 
	 */
	public Workbook createGrantXls(LoanPreApplyRecord loanPreApplyRecord, String lastGrantDetailFileName) throws Exception;
	/**
	 * 预拨确认书文件（PDF
	 * @param params
	 * @param pdfFilePath
	 * @return
	 */
	public ByteArrayOutputStream getConfirmPdfOutput(
			LoanPreApplyRecord loanPreApplyRecord, String pdfFilePath);
	/**
	 * 获取预拨确认书文件 xls
	 * @param loanPreApplyRecord
	 * @param applyExcelFilePath
	 * @return
	 */
	public Workbook getConfirmWorkbook(LoanPreApplyRecord loanPreApplyRecord,
			String applyExcelFilePath);
	/**
	 * 获取最近一次取还款计划明细（XLS）
	 * @param loanPreApplyRecord
	 * @param lastPayPlanFileName 
	 * @return
	 * @throws Exception 
	 */
	public Workbook createPayPlanXls(LoanPreApplyRecord loanPreApplyRecord, String lastPayPlanFileName) throws Exception;
}
