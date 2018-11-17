package com.zdmoney.credit.repay.service.pub;

import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.repay.vo.LoanApplyDetailVo;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IRequestManagementService {
    
    /**
     * 分页查询债权批次信息
     * @param params
     * @return
     */
    public Pager queryBatchNumInfo(Map<String, Object> params);
    
    /**
     * 查询划拨申请书统计信息
     * @param params
     * @return
     */
    public Map<String,Object> queryApplyPdfInfo(Map<String,Object> params);

    /**
     * 查询放款申请明细
     * @param params
     * @return
     */
    public List<LoanApplyDetailVo> queryLoanApplyDetailList(Map<String,Object> params);
    
    /**
     * 查询还款计划信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryPayPlanList(Map<String,Object> params);
    
    /**
     * 创建划拨申请书pdf文件输出流对象
     * @param batchNum
     * @return
     */
    public ByteArrayOutputStream getApplyPdfOutput(String batchNum, String path);
    
    /**
     * 创建划拨申请书excel文件对象
     * @param batchNum
     * @param fileName
     * @return
     */
    public Workbook getApplyWorkbook(String batchNum, String fileName, String path);
    
    /**
     * 创建放款申请明细excel文件对象
     * @param batchNum
     * @param fileName
     * @return
     */
    public Workbook getLoanApplyWorkbook(String batchNum, String fileName, Map<String,Object> param);
    
    /**
     * 创建还款计划excel文件对象
     * @param batchNum
     * @param fileName
     * @return
     */
    public Workbook getPayPlanWorkbook(String batchNum, String fileName);
    /**
     * 创建还款计划excel文件对象
     * @param date
     * @param fileName
     * @param projectCode
     * @return
     */
    public Workbook getPayPlanWorkbook(String date,String fileName,String projectCode);

    /**
     * 创建放款申请明细TXT文件输出流对象
     * @param batchNum
     * @param fileName
     * @return
     */
    public OutputStream getLoanApplyTxtOutput(String batchNum, String fileName);
    
    /**
     * 创建放款明细excel文件对象 项目简码 ZDCF01
     * @param grantMoneyDate
     * @param fileName
     * @return
     */
    public Workbook getLoanDetailWorkbook(String grantMoneyDate, String fileName);

    /**
     * 创建放款明细excel文件对象
     * @param batchNum
     * @param fileName
     * @return
     */
    public Workbook getLoanGrantDetailWorkbook(String batchNum, String fileName);
    /**
     * 创建放款明细excel文件对象 根据项目简码
     * @param grantMoneyDate
     * @param fileName
     * @param projectCode
     * @return
     */
    public Workbook getLoanDetailWorkbook(String grantMoneyDate, String fileName, String projectCode);

    /**
     * 创建放款明细TXT文件输出流对象 项目简码 ZDCF01
     * @param grantMoneyDate
     * @param fileName
     * @return
     */
    public OutputStream getLoanDetailTxtOutput(String grantMoneyDate, String fileName);
    /**
     * 创建放款明细TXT文件输出流对象 项目简码
     * @param grantMoneyDate
     * @param fileName
     * @return
     */
    public OutputStream getLoanDetailTxtOutput(String grantMoneyDate, String fileName,  String projectCode);

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
     * 获取需要上传的划款申请书 放款明细申请 还款计划 输入流集合
     * @param batchNum
     * @return
     * @throws IOException
     */
    public Map<String,InputStream> getUploadApplyFileInputMap(String batchNum,String fileBatchNum) throws IOException;

    /**
     * 根据文件类型获取当天文件批次号
     * @param fileType
     * @return
     */
    public String getRequestManagerFileToDayBatchNum(String fileType,String batchNum);

    /**
     * 添加申请书文件下载上传记录
     * @param batchNum
     * @param fileType
     * @param operateType
     * @param fileBatchNum
     */
    public void createRequestManagerOperateRecord(String batchNum,String fileType,String operateType,String fileBatchNum);

    /**
     * 获取申请书管理渤海信托所需要的文件名 项目简码默认为；ZDCF01
     * @param reqFileTypeEnum
     * @param fileBatchNum
     * @return
     */
    public String getRequestManagementFileName(ReqManagerFileTypeEnum reqFileTypeEnum,String fileBatchNum);
    /**
     * 获取申请书管理所需要的文件名
     * @param reqFileTypeEnum
     * @param fileBatchNum
     * @return
     */
    public String getRequestManagementFileName(ReqManagerFileTypeEnum reqFileTypeEnum,String fileBatchNum,String projectCode);

    /**
     * Workbook 转换成inputstream
     * @param workbook
     * @return
     * @throws IOException
     */
    public ByteArrayInputStream outStreamToInSteam(Workbook workbook) throws  IOException;

    public InputStream outStreamToInSteam(OutputStream outSteam) throws  IOException;
    
    /**
     * 根据条件获取文件批次号
     * @param batchNum
     * @param fileType
     * @return
     */
	public String findFileSeqByBatchNum(String batchNum, String fileType);

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
     * 根据条件生成文件名
     * @param reqFileTypeEnum
     * @param date
     * @param fileBatchNum
     * @param projectCode
     * @return
     */
	String getRequestManagementFileName(ReqManagerFileTypeEnum reqFileTypeEnum,Date date, String fileBatchNum, String projectCode);

    /** 更新上传下载文件记录
     * @param batchNum
     * @param fileType
     * @param operateType
     * @param fileBatchNum
     */
	void checkRequestManagerOperateRecord(String batchNum, String fileType, String operateType, String fileBatchNum);

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
     * 添加划拨申请书信息
     * @param batchNum
     * @param fileBatchNum
     */
    public void createApplyBookInfos(String batchNum,String fileBatchNum);

    /**
     * 是否上传过划拨申请书
     * @param batchNum
     * @return true 上传 false 未上传
     */
    public boolean isUploadApplyBook(String batchNum);

    /**
     * 判断这个批次号下是否有未回盘的债权
     * @param bacthNum
     */
    public void isALLBackOffer(String bacthNum);

    public String previousBatchNumIsALLBackOffer(String currentBacthNum,String fundsSources);
}
