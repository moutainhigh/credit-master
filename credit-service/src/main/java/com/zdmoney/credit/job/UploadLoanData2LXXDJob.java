package com.zdmoney.credit.job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.LxxdUploadRespEnum;
import com.zdmoney.credit.common.constant.UploadFtpAddressType;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.EnumUtil;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.domain.UploadLoanDataInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IUploadLoanDataInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.video.vo.DownLoadVideoDirVo;
import com.zdmoney.credit.video.vo.DownLoadVideoFileVo;


/**
 * @author 10098  2017年3月1日 上午9:31:30
 */
@Service
public class UploadLoanData2LXXDJob {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
    private ISysParamDefineService sysParamDefineService;
	@Autowired
	private IUploadLoanDataInfoService uploadLoanDataInfoService;
	
	public static boolean flag = false;
	public void execute(){
		logger.info("开始执行放款成功后的债权信息至龙信小贷。。。");
		String isUploadLoanData2LXXD = sysParamDefineService.getSysParamValue("sysJob", "isUploadLoanData2LXXD");
		if(!Const.isClosing.equals(isUploadLoanData2LXXD)){
			//龙信小贷把放款成功的债权信息数据插入到上传表中
			uploadLoanDataInfoService.pushLoanData2UploadInfo();
			//获取待上传 与上传失败 状态的 债权信息
			List<UploadLoanDataInfo> list = uploadLoanDataInfoService.findUploadLoanDataInfo2LXXD();
			if(CollectionUtils.isEmpty(list)){
				logger.info("没有待上传的债权信息至龙信小贷...");
				return;
			}
			FTPUtil apsFtp  = new FTPUtil();
			FTPUtil lxxdFtp = new FTPUtil();
			try {
				apsFtp.connectServer(ConnectBhxtFtpServiceImpl.host,ConnectBhxtFtpServiceImpl.port,ConnectBhxtFtpServiceImpl.userName,ConnectBhxtFtpServiceImpl.password,ConnectBhxtFtpServiceImpl.storeDir,FTPUtil.connectTimes,FTPUtil.dateTimes);
			}catch(Exception e){
				logger.error("连接征审FTP异常", e);
				try {
					apsFtp.closeServer();
				} catch (IOException ie) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			try {
				lxxdFtp.connectServer(ConnectBhxtFtpServiceImpl.hostLXXD,ConnectBhxtFtpServiceImpl.portLXXD,ConnectBhxtFtpServiceImpl.userNameLXXD,ConnectBhxtFtpServiceImpl.passWordLXXD,ConnectBhxtFtpServiceImpl.uploadFtpDirLXXD,FTPUtil.connectTimes,FTPUtil.dateTimes);
			}catch(Exception e){
				logger.error("连接龙信小贷FTP异常", e);
				try {
					apsFtp.closeServer();
					lxxdFtp.closeServer();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			List<String> idNums = new ArrayList<String>();
			for(UploadLoanDataInfo uploadLoanDataInfo : list){
				List<DownLoadVideoDirVo> downLoanList = new ArrayList<DownLoadVideoDirVo>();
				downLoanList = getDownLoadListByLoanDataInfo(apsFtp, uploadLoanDataInfo);
				if(CollectionUtils.isEmpty(downLoanList)){
					uploadLoanDataInfo.setUploadStatus(UploadFtpAddressType.上传失败.getUploadStatus());
					continue;
				}
				uploadLoanDataInfoService.addDownLoadDirs4LXXD(downLoanList,uploadLoanDataInfo);
				flag = uploadLoanDataInfo2Lxxd(lxxdFtp, downLoanList, uploadLoanDataInfo,ConnectBhxtFtpServiceImpl.uploadFtpDirLXXD);
				if(flag){
					idNums.add(uploadLoanDataInfo.getIdNum());
					uploadLoanDataInfo.setUploadStatus(UploadFtpAddressType.上传成功.getUploadStatus());
				}else{
					uploadLoanDataInfo.setUploadStatus(UploadFtpAddressType.上传失败.getUploadStatus());
				}
				uploadLoanDataInfoService.updateLoanDataInfoByVo(uploadLoanDataInfo);
			}
			if(CollectionUtils.isNotEmpty(idNums)){
				invokingLxxdUploadPort(idNums,Dates.getDateTime(new Date(), Dates.DATAFORMAT_YYYYMMDD));	
			}
			logger.info("执行放款成功后的债权信息至龙信小贷JOB结束。。。");
			try {
				apsFtp.closeServer();
				lxxdFtp.closeServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			logger.warn("定时开关isUploadLoanData2LXXD关闭，此次不执行");
		}
	}

	public static void main(String args[]){
		List<String> list = new ArrayList<>();
		list.add("110101198609230097");
		list.add("370214198008289486");
//		list.add("440224196707221765");
		UploadLoanData2LXXDJob u = new UploadLoanData2LXXDJob();
		u.invokingLxxdUploadPort(list,"20170308");
	}
	/**
	 * 上传文件成功，调用接口
	 * @param idNum
	 * @param dateTime
	 */
	private  void invokingLxxdUploadPort(List<String> idNums, String uploadTime) {
		try{
			String name = ConnectBhxtFtpServiceImpl.userNameInterLXXD;
			String password = ConnectBhxtFtpServiceImpl.passWordInterLXXD;
			String url = ConnectBhxtFtpServiceImpl.urlInterLXXD;
//			String name = "ZDCF_LX";
//			String password = "ZDCF_LX";
//			String url = "http://115.29.55.185:9500/cusmanage/ApplyOtherAPI";
			String status = "1";
			String source = "ZDCF";
			StringBuffer idnum = new StringBuffer();
			for(String id:idNums){
				idnum.append("@");
				idnum.append(id);
			}
			if(idnum.length() < 1){
				return;
			}
			JSONObject json = new JSONObject();
			json.put("name", name);
			json.put("password", password);
			json.put("idnum", idnum.substring(1));
			json.put("uploadtime", uploadTime);
			json.put("status", status);
			json.put("source", source);
			
			String res = HttpUtils.doJsonPost(url, json.toJSONString());
			System.out.println(res);
			JSONObject resJson = JSONObject.parseObject(res);
			String code = resJson.getString("code");
			String msg = resJson.getString("msg");
			logger.info("龙信小贷债权信息上传——调用上传成功接口响应消息:"+msg);
			if(LxxdUploadRespEnum.接口调用成功.getCode().equals(code)){
				logger.info("龙信小贷债权信息上传——调用上传成功接口正常");
			}else{
				String erroMsg = EnumUtil.getEnumValue(LxxdUploadRespEnum.class, code, "code", "msg");
				if(Strings.isEmpty(erroMsg)){
					logger.error("龙信小贷债权信息上传——调用上传成功接口异常");
				}else{
					logger.error("龙信小贷债权信息上传——调用上传成功接口报错："+erroMsg);
				}
			}
		}catch(Exception e){
			logger.error("龙信小贷债权信息上传——调用上传成功接口报错", e);
		}
	}

	/**
	 * 上传文件至龙信小贷
	 * @param lxxdFtp
	 * @param downLoanList
	 * @param uploadLoanDataInfo
	 * @return
	 */
	private boolean uploadLoanDataInfo2Lxxd(FTPUtil lxxdFtp, List<DownLoadVideoDirVo> downLoanList, UploadLoanDataInfo uploadLoanDataInfo, String rootPath) {
		boolean flag = false;
		try{
			for(DownLoadVideoDirVo dirVo:downLoanList){
				StringBuffer filePath = new StringBuffer(rootPath);
				filePath.append("/");
				filePath.append(Dates.getDateTime(Dates.DATAFORMAT_YYYYMMDD));
				filePath.append("/ZDCF_");
				filePath.append(uploadLoanDataInfo.getIdNum());
				filePath.append("/");
				filePath.append(dirVo.getFullPath());
				String dirPath = Strings.getCharacterTransfer(filePath.toString(), Strings.CHARACTER_GBK, Strings.CHARACTER_UTF);
				lxxdFtp.createDeepFilePaths(dirPath);
				List<DownLoadVideoFileVo> fileVos = dirVo.getFileList();
				for(DownLoadVideoFileVo fileVo:fileVos){
					String fileName = fileVo.getFileName();
					fileName = Strings.getCharacterTransfer(fileName, Strings.CHARACTER_GBK, Strings.CHARACTER_UTF);
					ByteArrayOutputStream byteOutputStream = (ByteArrayOutputStream) fileVo.getOutputStream();
					InputStream is = new ByteArrayInputStream(byteOutputStream.toByteArray());
					lxxdFtp.changeDirectory(dirPath);
					lxxdFtp.uploadFile(is, fileName);
				}
			}
			uploadLoanDataInfo.setRemark("上传成功");
			flag = true;
		}catch(Exception e){
			uploadLoanDataInfo.setRemark("上传文件至龙信小贷异常");
			logger.error("上传文件至龙信小贷异常", e);
			return false;
		}
		return flag;
	}

	/**
	 * 获取待上传的文件流
	 * @param apsFtp
	 * @param uploadLoanDataInfo
	 * @return
	 */
	private List<DownLoadVideoDirVo> getDownLoadListByLoanDataInfo(FTPUtil ftp, UploadLoanDataInfo uploadLoanDataInfo) {
		try{
			String appNo = uploadLoanDataInfo.getAppNo();
			List<String> fileApsNames = ftp.getDicFileList(appNo);
			if(CollectionUtils.isEmpty(fileApsNames)){
				uploadLoanDataInfo.setRemark("征审下载目录为空");
				return null;
			}
			List<DownLoadVideoDirVo> dirList = new ArrayList<DownLoadVideoDirVo>();
			for(String fileApsName:fileApsNames){
				String[] lxxdFileName = lxxdFileName(fileApsName);
				if(lxxdFileName == null){
					continue;
				}
				List<DownLoadVideoFileVo> fileList = new ArrayList<DownLoadVideoFileVo>();
				List<String> fileNames = ftp.getFileList(appNo+"/"+fileApsName);
				for(String fileName:fileNames){
					DownLoadVideoFileVo fileVo  = new DownLoadVideoFileVo();
					fileVo.setFileName(fileName);
					ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream(2048);
					ftp.download(appNo+"/"+fileApsName+"/"+fileName, byteArrayos);
					fileVo.setOutputStream(byteArrayos);
					fileList.add(fileVo);
				}
				for(String lxxdDir:lxxdFileName){
					DownLoadVideoDirVo dirVo = new DownLoadVideoDirVo();
					dirVo.setFileList(fileList);
					dirVo.setFullPath(lxxdDir);
					dirList.add(dirVo);
				}
			}
			if(CollectionUtils.isEmpty(dirList)){
				uploadLoanDataInfo.setRemark("征审下载文件为空");
			}
			return dirList;
		}catch(Exception e){
			logger.error("上传征审数据至龙信小贷——获取文件流异常", e);
			uploadLoanDataInfo.setRemark("征审下载文件异常");
			return null;
		}
	}
	/**
	 * @param fileApsName
	 * @return
	 */
	private String[] lxxdFileName(String fileName) {
		String fileName1 = "";
		String fileName2 = "";
    	if (fileName.equals("A")) {
    		fileName1 = "贷款/贷款申请表";
		} else if (fileName.equals("S")) {
			fileName1 = "贷款/贷款相关资料";
		} else if (fileName.equals("M")) {
			fileName1 = "贷款/其他资料";
		} else if (fileName.equals("B")) {
			fileName1 = "贷款/身份信息";
			fileName2 = "放款/身份认证信息";
		} else if (fileName.equals("S4")) {
			fileName1 = "贷款/身份信息";
		} else if (fileName.equals("S7")) {
			fileName1 = "贷款/通话详情";
		} else if (fileName.equals("L")) {
			fileName1 = "贷款/征信报告";
		} else if (fileName.equals("S8")) {
			fileName1 = "贷款/证大咨询借款服务操作表";
			fileName2 = "放款/个人借款咨询服务协议";
		} else if (fileName.equals("S11")) {
			fileName1 = "放款/个人借款咨询服务风险基金协议";
		} else if (fileName.equals("S1")) {
			fileName1 = "放款/借款凭证";
			fileName2 = "放款/借款协议";
		} else if (fileName.equals("S5")) {
			fileName1 = "放款/银行卡";
		}else{
			fileName1 = "";
		}
    	if(Strings.isEmpty(fileName1) && Strings.isEmpty(fileName2)){
    		return null;
    	}else if(Strings.isEmpty(fileName1) || Strings.isEmpty(fileName2)){
    		return new String[]{Strings.isNotEmpty(fileName1)?fileName1:fileName2};
    	}else{
    		return new String[]{fileName1,fileName2};
    	}

	}
	
}
