package com.zdmoney.credit.job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.abs.service.pub.IAbsCommonService;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.UploadFtpFileLog;
import com.zdmoney.credit.signature.service.pub.IElectronicSignatureService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.video.vo.DownLoadVideoDirVo;
import com.zdmoney.credit.video.vo.DownLoadVideoFileVo;

/**
 * 上传债权影像资料至数信
 * @author 10098  2016年11月23日 下午2:53:20
 */
@Service
public class FtpUploadLoanData2AbsJob {

	private Logger logger = Logger.getLogger(FtpUploadLoanData2AbsJob.class);
	
    @Autowired
    private IVLoanInfoDao ivLoanInfoDao;
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private IAbsCommonService absCommonService;
	@Autowired
	private IElectronicSignatureService electronicSignatureService;
	public void execute(){
		logger.info("开始处理FtpUploadLoanData2AbsJob...");
		String isFtpUploadLoanData2Abs = sysParamDefineService.getSysParamValue("sysJob", "isFtpUploadLoanData2Abs");
		if(!Const.isClosing.equals(isFtpUploadLoanData2Abs)){
			logger.info("开始执行上传债权影像资料至数信JOB");
			absCommonService.pushUploadFtpFileLog();
			List<UploadFtpFileLog> flList = absCommonService.findWmxt2UploadFtpLog2Abs();
			if(flList == null || flList.size() == 0){
				logger.info("没有待上传的债权影像资料信息");
				return;
			}
			// FTP服务器IP地址
	        String apsHost = sysParamDefineService.getSysParamValueCache("download", "sftp.download.host");
	        // FTP服务器端口号
	        String apsPort = sysParamDefineService.getSysParamValueCache("download", "sftp.download.port");
	        // FTP服务器登陆用户名
	        String apsUserName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.userName");
	        // FTP服务器I登陆密码
	        String apsPassword = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pwd");
	        // FTP服务器下载文件路径
	        String apsRemoteFileName = "/aps/";
			FTPUtil apsFtp = new FTPUtil();
			try {
				apsFtp.connectServer(apsHost, apsPort, apsUserName, apsPassword, apsRemoteFileName,FTPUtil.dateTimes, FTPUtil.connectTimes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("连接征审ftp异常..."+e);
				return;
			}			
			// FTP服务器IP地址
	        String absHost = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.host");
	        // FTP服务器端口号
	        String absPort = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.port");
	        // FTP服务器登陆用户名
	        String absUserName = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.username");
	        // FTP服务器I登陆密码
	        String absPassword = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.password");
	        // FTP服务器下载文件路径
	        String absRemoteFileName = "";			
	        FTPUtil absFtp = new FTPUtil();
			try {
				absFtp.connectServer(absHost, absPort, absUserName, absPassword, absRemoteFileName,FTPUtil.dateTimes, FTPUtil.connectTimes);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("连接数信ftp异常..."+e);
				return;
			}
			
			for(UploadFtpFileLog fileLog:flList ){
				List<DownLoadVideoDirVo> dirList = getCoreLoanDataByFtpLog(fileLog, apsFtp);
				if(dirList == null){
					continue;
				}
				boolean flag = uploadLoanData2AbsByContract(dirList, absFtp, fileLog.getContractNum());
				if(flag){
					List<String> contractNos = new ArrayList<String>();
					contractNos.add(fileLog.getContractNum());
					absCommonService.uploadUploadFileLogStatus(contractNos);
				}
			}
			logger.info("传债权影像资料至数信JOB执行完成");
			try {
				apsFtp.closeServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				absFtp.closeServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			logger.warn("定时开关isFtpUploadLoanData2Abs关闭，此次不执行");
		}
	}

	/**
	 * 上传影像文件至数信 批量
	 * @param dirList
	 * @return
	 */
	private boolean uploadLoanData2Abs(List<DownLoadVideoDirVo> dirList) {
		boolean flag = false;
		try{
			// FTP服务器IP地址
	        String host = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.host");
	        // FTP服务器端口号
	        String port = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.port");
	        // FTP服务器登陆用户名
	        String userName = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.username");
	        // FTP服务器I登陆密码
	        String password = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.password");
	        // FTP服务器下载文件路径
	        String remoteFileName = "ImageData";
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(2048);
			ZipOutputStream zipOut = new ZipOutputStream(byteOutputStream);	                 
            for (DownLoadVideoDirVo dirVo : dirList) {
           	 	List<DownLoadVideoFileVo> fileList = dirVo.getFileList();
           	 	String fullPath = dirVo.getFullPath();
           	 	for (DownLoadVideoFileVo fileVo : fileList) {
           	 		String fileName = fileVo.getFileName();
           	 		ByteArrayOutputStream fileOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
     				/** 设置文件名 **/
     				zipOut.putNextEntry(new ZipEntry(fullPath + "/" + fileName));
     				fileOutputStream.writeTo(zipOut);
     				zipOut.closeEntry();
           	 	}
           }
           zipOut.close();
           InputStream iSteram =  new ByteArrayInputStream(byteOutputStream.toByteArray());
           String fileName = GatewayConstant.PROJ_NO + "_" + Dates.getDateTime("yyyyMMdd") + "_001.zip";
           FTPUtil ftp = new FTPUtil();
           ftp.connectServer(host, port, userName, password, remoteFileName,FTPUtil.dateTimes, FTPUtil.connectTimes);
           if(!ftp.changeDirectory(GatewayConstant.SYS_SOURCE)){
        	   ftp.createDirectory(GatewayConstant.SYS_SOURCE);
        	   ftp.changeDirectory(GatewayConstant.SYS_SOURCE);
           }
           flag = ftp.uploadFile(iSteram, fileName);
           if(flag){
        	    String classpath =  FTPUtil.class.getClassLoader().getResource("").getPath();
	   	        File file=new File(classpath+"//"+".IND");
	   	        if(!file.exists()){
	   	        	file.createNewFile();
	   	        }
	   	        ftp.uploadFile(new FileInputStream(file), GatewayConstant.PROJ_NO + "_" + Dates.getDateTime("yyyyMMdd")+"_001.IND");
           }
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
			logger.error("上传影像文件至数信失败...",e);
			return flag;
		}
		return flag;
	}
	
	
	/**
	 * 上传影像文件以及需盖章合同至数信  单个合同
	 * @param dirList
	 * @param ftpUtil
	 * @param contractNum
	 * @return
	 */
	private boolean uploadLoanData2AbsByContract(List<DownLoadVideoDirVo> dirList, FTPUtil ftpUtil, String contractNum) {
		boolean flag = true;
		try{
			for(DownLoadVideoDirVo dirVo:dirList){
				try{
					//上传需盖章合同信息
					if("S1_PDF".equals(dirVo.getFullPath()) || "S8_PDF".equals(dirVo.getFullPath())){
						ftpUtil.changeDirectory("UnsealContract");
						ftpUtil.createAndChangeDir(GatewayConstant.SYS_SOURCE);
						ftpUtil.createAndChangeDir(GatewayConstant.PROJ_NO);
						ftpUtil.createAndChangeDir(contractNum);
						List<DownLoadVideoFileVo> fileList = dirVo.getFileList();
						for(DownLoadVideoFileVo fileVo : fileList){
							String fileName = fileVo.getFileName();
							ByteArrayOutputStream byteOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
							InputStream is = new ByteArrayInputStream(byteOutputStream.toByteArray());
							ftpUtil.uploadFile(is, fileName);
						}
					}else{
						//上传其他影像资料
						ftpUtil.changeDirectory("ImageData");
						ftpUtil.createAndChangeDir(GatewayConstant.SYS_SOURCE);
						ftpUtil.createAndChangeDir(GatewayConstant.PROJ_NO);
						ftpUtil.createAndChangeDir(contractNum);
						ftpUtil.createAndChangeDir(dirVo.getFullPath());
						List<DownLoadVideoFileVo> fileList = dirVo.getFileList();
						for(DownLoadVideoFileVo fileVo : fileList){
							String fileName = fileVo.getFileName();
							ByteArrayOutputStream byteOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
							InputStream is = new ByteArrayInputStream(byteOutputStream.toByteArray());
							ftpUtil.uploadFile(is, fileName);
						}
					}
				}catch(Exception e){
					flag = false;
					logger.error("影像资料上传至数信报错,合同号为："+contractNum,e);
					return flag;
				}finally{
					//每一个文件夹内容上传完切换至根目录
					ftpUtil.changeDirectory("~");
				}
			}
			//资料上传无异常 则 上传成功标志文件
			if(flag){
				try{
					ftpUtil.changeDirectory("ImageData");
					ftpUtil.createAndChangeDir(GatewayConstant.SYS_SOURCE);
					ftpUtil.createAndChangeDir(GatewayConstant.PROJ_NO);
	        	    String classpath =  FTPUtil.class.getClassLoader().getResource("").getPath();
		   	        File file=new File(classpath+"//"+".IND");
		   	        if(!file.exists()){
		   	        	file.createNewFile();
		   	        }
		   	        ftpUtil.uploadFile(new FileInputStream(file), contractNum+".IND");
				}catch(Exception e){
					flag = false;
					logger.error("影像资料上传至数信报错,合同号为："+contractNum,e);
					return flag;
				}finally{
					//标志文件上传完切换至根目录
					ftpUtil.changeDirectory("~");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
			logger.error("上传影像文件至数信失败...",e);
		}finally{
			//标志文件上传完切换至根目录
			try {
				ftpUtil.changeDirectory("~");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 上传盖章合同至数信
	 * @param dirList
	 * @return
	 */
	private boolean uploadContracts2Abs(List<DownLoadVideoDirVo> dirList) {
		boolean flag = false;
		try{
			// FTP服务器IP地址
	        String host = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.host");
	        // FTP服务器端口号
	        String port = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.port");
	        // FTP服务器登陆用户名
	        String userName = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.username");
	        // FTP服务器I登陆密码
	        String password = sysParamDefineService.getSysParamValueCache("upload", "sftp.ftpuploadabs.password");
	        // FTP服务器下载文件路径
	        String remoteFileName = "UnsealContract";
	        FTPUtil ftp = new FTPUtil();
	        ftp.connectServer(host, port, userName, password, remoteFileName,FTPUtil.dateTimes, FTPUtil.connectTimes);
	        if(!ftp.changeDirectory(GatewayConstant.SYS_SOURCE)){
	        	ftp.createDirectory(GatewayConstant.SYS_SOURCE);
	        	ftp.changeDirectory(GatewayConstant.SYS_SOURCE);
	        }
	        if(!ftp.changeDirectory(GatewayConstant.PROJ_NO)){
	        	ftp.createDirectory(GatewayConstant.PROJ_NO);
	        	ftp.changeDirectory(GatewayConstant.PROJ_NO);
	        }
	        for(DownLoadVideoDirVo dirVo : dirList){
	        	String contractNum = dirVo.getFullPath();	
	        	List<DownLoadVideoFileVo> files = dirVo.getFileList();
	        	if(files == null || files.size() == 0){
	        		continue;
	        	}
	        	if(!ftp.changeDirectory(contractNum)){
	        		ftp.createDirectory(contractNum);
	        		ftp.changeDirectory(contractNum);
	        	}
	        	try{
		        	for(DownLoadVideoFileVo fileVo:files){
		        		String fileName = fileVo.getFileName();
		        		ByteArrayOutputStream byteOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
		                InputStream iSteram =  new ByteArrayInputStream(byteOutputStream.toByteArray());
		        		ftp.uploadFile(iSteram,fileName);
		        	}
	        	}catch(Exception e){
	        		e.printStackTrace();
	        		logger.error("上传盖章合同失败...",e);
	        	}finally{
	        		ftp.changeDirectory("..");
	        	}
	        }
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
			logger.error("上传盖章合同文件夹至数信失败...",e);
			return flag;
		}
		return flag;
	}

	/**
	 * 获取核心影像资料
	 * @param vlList
	 */
	public Map<String,Object> getCoreLoanData(List<UploadFtpFileLog> flList) {
		Map<String, Object> map = new HashMap<String, Object>();
        try {
			// FTP服务器IP地址
	        String host = sysParamDefineService.getSysParamValueCache("download", "sftp.download.host");
	        // FTP服务器端口号
	        String port = sysParamDefineService.getSysParamValueCache("download", "sftp.download.port");
	        // FTP服务器登陆用户名
	        String userName = sysParamDefineService.getSysParamValueCache("download", "sftp.download.userName");
	        // FTP服务器I登陆密码
	        String password = sysParamDefineService.getSysParamValueCache("download", "sftp.download.pwd");
	        // FTP服务器下载文件路径
	        String remoteFileName = "/aps/";
			FTPUtil ftp = new FTPUtil();
			ftp.connectServer(host, port, userName, password, remoteFileName,FTPUtil.dateTimes, FTPUtil.connectTimes);
			List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
			List<DownLoadVideoDirVo> contractFiles = new ArrayList<DownLoadVideoDirVo>();
			List<String> contractNums = new ArrayList<String>();
			for(UploadFtpFileLog fileLog : flList){
				String appNo = fileLog.getAppNo();
				if(Strings.isEmpty(appNo)){
					continue;
				}
				try{
					DownLoadVideoDirVo contractVo = new DownLoadVideoDirVo();
					contractFiles.add(contractVo);
					contractVo.setFullPath(fileLog.getContractNum());
					List<DownLoadVideoFileVo> cfileList  = new ArrayList<DownLoadVideoFileVo>();
					contractVo.setFileList(cfileList);
					List<String> fileCoreNames = ftp.getDicFileList(appNo);
					if(!fileCoreNames.contains("S1_PDF") || !fileCoreNames.contains("S8_PDF")){
						continue;
					}
					for(String folderName:fileCoreNames){
						//需盖章合同 与盖章咨询服务费协议 文件
						if("S1_PDF".equals(folderName) || "S8_PDF".equals(folderName)){
							List<String> fileNames = ftp.getFileList(appNo+"/"+folderName);
							for(String fileName:fileNames){
								DownLoadVideoFileVo fileVo  = new DownLoadVideoFileVo();
								fileVo.setFileName(fileName);
								ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream(2048);
								ftp.download(appNo+"/"+folderName+"/"+fileName, byteArrayos);
								fileVo.setOutputStream(byteArrayos);
								cfileList.add(fileVo);
							}
							continue;
						}
						String absFolderName = getAbsFileName(folderName);
						if(Strings.isEmpty(absFolderName)){
							continue;
						}
						DownLoadVideoDirVo dirVo = new DownLoadVideoDirVo();
						dirList.add(dirVo);	                    
						dirVo.setFullPath( GatewayConstant.PROJ_NO+"/"+fileLog.getContractNum()+"/"+absFolderName);
						List<DownLoadVideoFileVo> fileList = new ArrayList<DownLoadVideoFileVo>(); 
						dirVo.setFileList(fileList);
						List<String> fileNames = ftp.getFileList(appNo+"/"+folderName);
						for(String fileName:fileNames){
							DownLoadVideoFileVo fileVo  = new DownLoadVideoFileVo();
							fileVo.setFileName(fileName);
							ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream(2048);
							ftp.download(appNo+"/"+folderName+"/"+fileName, byteArrayos);
							fileVo.setOutputStream(byteArrayos);
							fileList.add(fileVo);
						}
					}
					contractNums.add(fileLog.getContractNum());
				}catch(Exception e){
					logger.error("获取影像文件失败，合同号为："+fileLog.getContractNum(),e);
					continue;
				}
			}
			map.put("contractFiles", contractFiles);
			map.put("dirList", dirList);
			map.put("contractNums", contractNums);
        } catch (IOException e) {
			// TODO Auto-generated catch block
        	e.printStackTrace();
        	logger.error("获取核心债权影像资料信息失败..."+e);
        	return map;
		}
        return map;
	}
	
	//根据ftp上传日志 获取文件
	public List<DownLoadVideoDirVo> getCoreLoanDataByFtpLog(UploadFtpFileLog fileLog, FTPUtil ftp) {
		try{
			String appNo = fileLog.getAppNo();
			if(Strings.isEmpty(appNo)){
				return null;
			}
			List<String> fileCoreNames = ftp.getDicFileList(appNo);
			if(!fileCoreNames.contains("S1_PDF") || !fileCoreNames.contains("S8_PDF")){
				return null;
			}
			List<DownLoadVideoDirVo> list = new ArrayList<DownLoadVideoDirVo>();
			for(String folderName:fileCoreNames){
				DownLoadVideoDirVo dirVo = new DownLoadVideoDirVo();
				List<DownLoadVideoFileVo> fileList  = new ArrayList<DownLoadVideoFileVo>();
				dirVo.setFileList(fileList);
				//需盖章合同 与盖章咨询服务费协议 文件
				if("S1_PDF".equals(folderName) || "S8_PDF".equals(folderName)){
					dirVo.setFullPath(folderName);
					List<String> fileNames = ftp.getFileList(appNo+"/"+folderName);
					for(String fileName:fileNames){
						DownLoadVideoFileVo fileVo  = new DownLoadVideoFileVo();
						fileVo.setFileName(fileName);
						ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream(2048);
						ftp.download(appNo+"/"+folderName+"/"+fileName, byteArrayos);
						fileVo.setOutputStream(byteArrayos);
						fileList.add(fileVo);
					}
				}else{
					String oldFolderName = folderName;
					String absFolderName = getAbsFileName(folderName);
					if(Strings.isEmpty(absFolderName)){
						continue;
					}
					dirVo.setFullPath(absFolderName);
					List<String> fileNames = ftp.getFileList(appNo+"/"+oldFolderName);
					for(String fileName:fileNames){
						DownLoadVideoFileVo fileVo  = new DownLoadVideoFileVo();
						fileVo.setFileName(fileName);
						ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream(2048);
						ftp.download(appNo+"/"+oldFolderName+"/"+fileName, byteArrayos);
						fileVo.setOutputStream(byteArrayos);
						fileList.add(fileVo);
					}
				}
				list.add(dirVo);
			}
			return list;
		}catch(Exception e){
			logger.error("获取待上传影像文件失败", e);
			return null;
		}
	}
	
	//解析文件路径 解压缩至输出流
	private void compressFtp2Zip(FTPUtil ftp, String filePath, ZipOutputStream zipOutStream) throws IOException {
		if(CollectionUtils.isNotEmpty(ftp.getDicFileList(filePath))){
			List<String> list = ftp.getDicFileList(filePath);
			for(String filePathName:list){
				compressFtp2Zip(ftp, filePath+"/"+filePathName,zipOutStream);
			}
		}else{
			List<String> fileNameList=ftp.getFileList(filePath);
			for(String fileName:fileNameList){
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
				ftp.download(filePath+"/"+fileName, outputStream);
				/** 设置文件名 **/
				zipOutStream.putNextEntry(new ZipEntry(filePath + "/" + fileName));
				outputStream.writeTo(zipOutStream);
				zipOutStream.closeEntry();
			}
		}
	}
	
	//数信目录转换
    private String getAbsFileName(String fileName) {
    	if (fileName.equals("A")) {
			fileName = "ApplicationForm";
		} else if (fileName.equals("D")) {
			fileName = "WorkingRevenue";
		} else if (fileName.equals("H")) {
			fileName = "InsurancePolicy";
		} else if (fileName.equals("N")) {
			fileName = "OnlineShoppingInfo";
		} else if (fileName.equals("K")) {
			fileName = "PrivateOwnerInfo";
		} else if (fileName.equals("L")) {
			fileName = "CreditReport";
		} else if (fileName.equals("S")) {
			fileName = "LoanContract";
		} else if (fileName.equals("S1")) {
			fileName = "LoanContract";
		} else if (fileName.equals("S2")) {
			fileName = "RepaymentReminder";
		} else if (fileName.equals("S3")) {
			fileName = "EntrustDebitAuth";
		} else if (fileName.equals("S4")) {
			fileName = "IdentityCard";
		} else if (fileName.equals("S5")) {
			fileName = "BankCard";
		} else if (fileName.equals("S8")){
			fileName = "ServiceFeeContract";
		} else{
			fileName = "";
		}
		return fileName;
    }
	/**
	 * 针对外贸2 给上传数信的合同签章
	 */
	public void excuteElectronicSignatureAbs(){
		logger.info("开始处理excuteElectronicSignatureAbs...");
		String isAbsElectronicSignature = sysParamDefineService.getSysParamValue("sysJob", "isAbsElectronicSignature");
		if(!Const.isClosing.equals(isAbsElectronicSignature)){
			logger.info("开始对上传数信的图片进行签章");
			electronicSignatureService.absElectronicSignatureBatch();
			logger.info("对上传数信的图片进行签章结束");
		}else{
			logger.warn("定时开关isAbsElectronicSignature关闭，此次不执行");
		}
	}
}
