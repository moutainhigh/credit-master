package com.zdmoney.credit.job;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.HttpUrlConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.domain.OperateLog;
import com.zdmoney.credit.common.service.pub.IOperateLogService;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.signature.service.ElectronicSignatureServiceImpl;
import com.zdmoney.credit.signature.service.pub.IElectronicSignatureService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.springframework.stereotype.Service;

/**
 * @author 10098  2017年3月23日 下午5:08:35
 */
@Service
public class FtpUploadLoanData2WM3Job {

	private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private  IVLoanInfoDao  ivLoanInfoDao;
	@Autowired
	private IElectronicSignatureService electronicSignatureService;
	@Autowired
    private IRequestManagementService requestManagementService;
	@Autowired
	private IOperateLogService operateLogService;
	@Autowired
	private HttpUrlConnection httpUrlConnection;
	//将当日放款成功的合同图片转pdf，并上传至外贸3
	public void execute(){
		logger.info("开始处理FtpUploadLoanData2WM3Job...");
		String isFtpUploadLoanData2WM3 = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadLoanData2WM3");
		if(!Const.isClosing.equals(isFtpUploadLoanData2WM3)){
			logger.info("开始执行上传放款成功的合同信息至外贸3");
			//征审系统ftp
			FTPUtil zsftp = new FTPUtil();
			//外贸3ftp
			FTPUtil wmftp = new FTPUtil();
			try{
				zsftp.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, ConnectBhxtFtpServiceImpl.userName, ConnectBhxtFtpServiceImpl.password, ConnectBhxtFtpServiceImpl.storeDir, ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
				wmftp.connectServer(ConnectBhxtFtpServiceImpl.uploadHostWM3, ConnectBhxtFtpServiceImpl.uploadPortWM3,
						ConnectBhxtFtpServiceImpl.uploadUserNameWM3, ConnectBhxtFtpServiceImpl.uploadPassWordWM3,
						ConnectBhxtFtpServiceImpl.uploadLoanInfoDirWM3,
						ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("fundsSources", FundsSourcesTypeEnum.外贸3.getValue());
				params.put("grantMoneyDate", Dates.getDateTime(Dates.getBeforeDays(1),Dates.DEFAULT_DATE_FORMAT));
				List<VLoanInfo> vLoanInfoList = ivLoanInfoDao.findLoanAgreementByParams(params);
				if(CollectionUtils.isEmpty(vLoanInfoList)){
					logger.info("外贸3 没有待上传贷款协议文件");
					return;
				}
				for(VLoanInfo vLoanInfo:vLoanInfoList){
					//获取征审贷款协议图片文件
					String imageDir = ConnectBhxtFtpServiceImpl.storeDir+vLoanInfo.getAppNo()+ElectronicSignatureServiceImpl.zsStoreloanProtocolFileDir;//政审存贷款服务协议书（ipg 格式）
					List<ByteArrayOutputStream> byteArrayOutputStreamList = electronicSignatureService.downloadDirAllFiles(imageDir, zsftp);
			        if (CollectionUtils.isEmpty(byteArrayOutputStreamList)) {
			        	logger.debug("没找到贷款协议图片 合同号为：" + vLoanInfo.getContractNum());
			            continue;
			        }
			        String pdfFileName = vLoanInfo.getContractNum()+".pdf";
			        OutputStream pdfOutStream = electronicSignatureService.createPdfFile(byteArrayOutputStreamList);
			        InputStream pdfInStream = requestManagementService.outStreamToInSteam(pdfOutStream);

			        String zsDir = ConnectBhxtFtpServiceImpl.storeDir+vLoanInfo.getAppNo();
			        zsftp.changeDirectory(zsDir);
			        boolean flag = false;
			        //征审系统存放转合同图片pdf文件地址
			        if(zsftp.createAndChangeDir(zsDir+ElectronicSignatureServiceImpl.zsStoreloanProtocolPdfFileDir)){
			        	flag = zsftp.uploadFile(pdfInStream, pdfFileName);
			        }
			        if(flag){
				        //外贸3存放合同pdf文件地址
						String wmUploadDir = ConnectBhxtFtpServiceImpl.uploadLoanInfoDirWM3;
						wmftp.changeDirectory(wmUploadDir);
						InputStream pftInstreamCp = requestManagementService.outStreamToInSteam(pdfOutStream);
						flag = wmftp.uploadFile(pftInstreamCp, pdfFileName);
			        }
			        
			        //上传成功 添加02-上传合同资料 记录
			        if(flag){
			        	operateLogService.addOperateLog(vLoanInfo.getId(), "02", "1");
			        }
				}
				logger.info("外贸3贷款协议jpg文件转换pdf,上传至渤海JOB结束");
			}catch(Exception e){
				logger.error("外贸3上传贷款协议异常",e);
			}finally{
				try {
					zsftp.closeServer();
					wmftp.closeServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			logger.warn("定时开关isFtpUploadLoanData2WM3关闭，此次不执行");
		}
	}
	
	//次日 将上传至外贸3的已盖章合同协议下载到征审系统
	public void executeDownLoad(){
		logger.info("开始处理FtpUploadLoanData2WM3Job...");
		String isFtpUploadLoanData2WM3 = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadLoanData2WM3");
		if(!Const.isClosing.equals(isFtpUploadLoanData2WM3)){
			logger.info("开始执行下载外贸3盖章合同。。。");;
			//外贸3ftp
			FTPUtil wmftp = new FTPUtil();
			try{
				wmftp.connectServer(ConnectBhxtFtpServiceImpl.uploadHostWM3, ConnectBhxtFtpServiceImpl.uploadPortWM3,
						ConnectBhxtFtpServiceImpl.uploadUserNameWM3, ConnectBhxtFtpServiceImpl.uploadPassWordWM3,
						ConnectBhxtFtpServiceImpl.downloadLoanInfoDirWM3,
						ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
				Map<String, Object> params = new HashMap<>();
				String operateDateStart = Dates.getDateTime(Dates.getBeforeDays(10),Dates.DEFAULT_DATE_FORMAT);
				String operateDateEnd = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT);
				params.put("operateDateStart", operateDateStart);
				params.put("operateDateEnd", operateDateEnd);
				List<OperateLog> list = operateLogService.findOperateLogList2DownLoad(params); 
				if(CollectionUtils.isEmpty(list)){
					logger.info("没有待下载的盖章合同记录");
					return;
				}
				String url = sysParamDefineService.getSysParamValue("system.thirdparty", "system.picsystem.url");
				for(OperateLog operateLog : list ){
					VLoanInfo vLoanInfo = new VLoanInfo();
					vLoanInfo.setId(operateLog.getLoanId());
					vLoanInfo = ivLoanInfoDao.findListByVo(vLoanInfo).get(0);
					String wmdir = ConnectBhxtFtpServiceImpl.downloadLoanInfoDirWM3;
					String fileName = vLoanInfo.getContractNum()+".pdf";
					wmftp.changeDirectory(wmdir);
					if(!wmftp.existFile(fileName)){
						continue;
					}
					OutputStream os = new ByteArrayOutputStream();
					wmftp.download(fileName, os);
					InputStream pdfInStream = requestManagementService.outStreamToInSteam(os);
					boolean flag = false;
					fileName = "贷款合同"+fileName;
					//上传文件至图片系统
					flag = httpUrlConnection.uploadData2PicSystem(vLoanInfo.getAppNo(),fileName,pdfInStream,url);
					 //上传成功 添加05-下载合同资料 记录
					if(flag){
						operateLogService.addOperateLog(vLoanInfo.getId(), "05", "1");
					}
				}
				logger.info("下载外贸3盖章合同JOB结束");
			}catch(Exception e){
				logger.error("下载外贸3盖章合同异常",e);
			}finally{
				try {
					wmftp.closeServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			logger.warn("定时开关isFtpUploadLoanData2WM3关闭，此次不执行");
		}		
	}
}
