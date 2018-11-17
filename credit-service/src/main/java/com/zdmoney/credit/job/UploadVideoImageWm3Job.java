package com.zdmoney.credit.job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.domain.OperateLog;
import com.zdmoney.credit.common.service.pub.IOperateLogService;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.service.pub.IPictureService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 外贸3 上传影像资料 job
 * @author YM10112
 *
 */
 
@Service
public class UploadVideoImageWm3Job {

	private static final Logger logger = Logger.getLogger(UploadVideoImageWm3Job.class);
	@Autowired
	private IVLoanInfoDao vloanInfoDao;
	@Autowired
	private IVLoanInfoService loanInfoService;
	@Autowired
	private IOperateLogService operateLogService;
	@Autowired
    private ISequencesService sequencesService;
	@Autowired
    private IPictureService pictureServiceImpl;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	FTPUtil zsftpUtil = null;
	FTPUtil wm3ftpUtil = null;
	
	public static final String  APS= "aps";//老政审
	public static final String  CFS= "cfs";//新政审
	
	public static final String loanimagefileupload = "/loan_imagefile_upload";//外贸3  合同附件
	//public static final String inquiryimagefileupload = "/personal_credit_inquiry_imagefile_upload";//外贸3  个人查征文件
	
	public static final String  apsDKSQB= "A";//(证大政审-申请表) 对应   外贸3-贷款申请表
	public static final String  apsDKHTDZ= "S1";//(证大政审-贷款合同)对应  外贸3-贷款合同
	public static final String  apsKKSQS= "S3"; //(证大政审-委托扣款授权书_证大)对应  外贸3-扣款授权书
	public static final String  apsSFZ= "S4"; //(证大政审-身份证)对应     外贸3-身份证文件  包括身份证复印件和身份证  个人查证和合同附件都用
    public static final String  apsQYZP= "S10"; //(证大政审-面签照片)对应  外贸3-签约照片
    //public static final String  apsGRCZSQS= "Q"; //(证大政审-机构央行征信查询授权书)对应  外贸3-个人查征授权书
    
	//需上传的影像文件包括：
    //信贷合同附件-消费类(贷款合同S1,申请表A,扣款授权书S3,签约照片S10,身份证S4-身份证信息) 
    //征信查询授权文件(个人查征授权书Q,身份证S4-身份证信息)
	public void uploadVideoImageExecute() {
		logger.info("【外贸3】上传影像资料至外贸3JOB开始...");
		Map<String,Object> map = new HashMap<String, Object>();
		//正常 逾期的都是放款成功的  只发送外贸3
		map.put("operateType", "03");//（01：上传还款计划，02：上传合同资料、03：上传影像资料、04：上传分账明细,05-下载合同资料）'
		map.put("loanBelong", FundsSourcesTypeEnum.外贸3.getValue());
		List<VLoanInfo> vLoanInfoList =  loanInfoService.findGrantSuccessNotToWM3(map);
		if(CollectionUtils.isEmpty(vLoanInfoList)){
			logger.info("暂没有放款成功的未上传给外贸3的影像资料...");
			return;
		}
		executeUploadDownFile(vLoanInfoList);
	}
	
	/**
	 * 连接FTP，根据不同的类型，判断下载政审或上传外贸3
	 * @param type
	 * @throws Exception 
	 */
	public void getConnectServer(String type) throws Exception{
		try {
			if("download".equals(type)){ //下载 政审代前的影像资料 
				zsftpUtil=new FTPUtil();
				zsftpUtil.connectServer(ConnectBhxtFtpServiceImpl.host, ConnectBhxtFtpServiceImpl.port, 
						ConnectBhxtFtpServiceImpl.userName, ConnectBhxtFtpServiceImpl.password, ConnectBhxtFtpServiceImpl.storeDir, 
						ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
				logger.info("下载政审资料,连接ftp服务器成功!");
			}else if("upload".equals(type)){ //上传到 外贸3的 FTP上 
				wm3ftpUtil=new FTPUtil();
				wm3ftpUtil.connectServer(ConnectBhxtFtpServiceImpl.uploadHostWM3, ConnectBhxtFtpServiceImpl.uploadPortWM3, 
						ConnectBhxtFtpServiceImpl.uploadUserNameWM3, ConnectBhxtFtpServiceImpl.uploadPassWordWM3, 
						ConnectBhxtFtpServiceImpl.uploadFtpDirWM3,
						ConnectBhxtFtpServiceImpl.times, ConnectBhxtFtpServiceImpl.times);
				logger.info("上传政审资料至外贸3,连接ftp服务器成功!");
			}
		}catch (Exception e) {
			throw new Exception("外贸3上传影像资料，连接FTP失败。"+type);
		}
	}
	
	/**
	 * 从政审下载文件，上传到外贸3ftp
	 */
	private void executeUploadDownFile(List<VLoanInfo> vLoanInfoList) {
		String[] folderNames = {"A","S1","S3","S4","S10"};
		String appNoPath = null;
		try {
			getConnectServer("download");
			getConnectServer("upload");
			HashSet<String> set = new HashSet<String>();  
			for (VLoanInfo vLoanInfo : vLoanInfoList) {
				List<String> loanFileName = new ArrayList<String>();
				//List<String> inquiryFileName = new ArrayList<String>();
				String appNo = vLoanInfo.getAppNo();
				String conNum = vLoanInfo.getContractNum();
				set.add(appNo);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("appNo", appNo);
				paramMap.put("subclassSorts",true);//subclassSorts in ('A','S1','S3','S4','S10')
				List<String> sysNamesList = pictureServiceImpl.findSysName(paramMap);
				//1：根据appNo和subclassSorts in ('A','S1','S3','S4','S10') 查询图片库，有则往下走上传，无则删除，继续遍历appNo
				if(CollectionUtils.isEmpty(sysNamesList)){
					set.remove(appNo);
					continue;
				}
				for(String folder : folderNames){
					paramMap = new HashMap<String, Object>();
					paramMap.put("appNo", appNo);
					paramMap.put("subclassSort", folder);
					List<String> sysNames = pictureServiceImpl.findSysName(paramMap);
					//2：根据appNO和subclassSort 分别等于('A','S1','S3','S4','S10')查询图片库，是否有对应的文件，有则往下走上传，无则重新循环 subclassSort
					if(CollectionUtils.isEmpty(sysNames)){
						logger.info("政审FTP没有对应路径的证件类型文件："+appNo+"--"+folder+"，合同号为：" + conNum);
						continue;
					}
					//List<String> fileNames = null;
					List<String> loanFileNames = null;
					//List<String> inquiryFileNames = null;
					HashSet<String> setSysName = new  HashSet<String>(); 
			        List<String> newSysNames = new  ArrayList<String>(); 
			        for (String cd:sysNames) {
			           if(setSysName.add(cd)){
			        	   newSysNames.add(cd);
			           }
			        }
					for(String sysName : newSysNames){//aps 或者是  cfs 
						appNoPath = "/"+sysName+"/"+appNo+"/"+folder;//   aps/20170512120000182504/A  cfs/20170512120000182504/A
						List<String> fileList  = zsftpUtil.getFileList(appNoPath);
						if(CollectionUtils.isEmpty(fileList)){
							logger.info("政审FTP，此路径不存在："+appNoPath+"，合同号为：" + conNum);
				            continue;
						}
						logger.info("文件夹"+appNoPath+"，下面的文件："+fileList);
						if(appNoPath.endsWith(apsDKSQB)||appNoPath.endsWith(apsDKHTDZ)||appNoPath.endsWith(apsKKSQS)
								||appNoPath.endsWith(apsQYZP)||appNoPath.endsWith(apsSFZ)){
							loanFileNames = fileList;
							wm3ftpUtil.changeDirectory(loanimagefileupload);
							for(int i=0;i<loanFileNames.size();i++){
								String fileName = loanFileNames.get(i);
								ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream();
								zsftpUtil.download(appNoPath+"/"+fileName, byteArrayos);//从政审下载 对应的影像资料
								InputStream iSteram =  new ByteArrayInputStream(byteArrayos.toByteArray());
								String realName = appNo+sysName+getFileName(fileName,i,folder,sysName,"loan");
								loanFileName.add(realName);
								wm3ftpUtil.uploadFile(iSteram,realName);//上传 合同附件  至外贸3FTP
							}
						}/*else if(appNoPath.endsWith(apsGRCZSQS)){
							inquiryFileNames = fileList;
							wm3ftpUtil.changeDirectory(inquiryimagefileupload);
							for(int i=0;i<inquiryFileNames.size();i++){
								String fileName = inquiryFileNames.get(i);
								ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream();
								zsftpUtil.download(appNoPath+"/"+fileName, byteArrayos);//从政审下载 对应的影像资料
								InputStream iSteram =  new ByteArrayInputStream(byteArrayos.toByteArray());
								String realName = appNo+sysName+getFileName(fileName,i,folder,sysName,"inquiry");
								inquiryFileName.add(realName);
								wm3ftpUtil.uploadFile(iSteram,realName);//上传 查征授权文件 至外贸3FTP
							}
						}else if(appNoPath.endsWith(apsSFZ)){
							fileNames = fileList;
							for(int i=0;i<fileNames.size();i++){
								String fileName = fileNames.get(i);
								ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream();
								zsftpUtil.download(appNoPath+"/"+fileName, byteArrayos);//从政审下载 对应的影像资料
								InputStream iSteram =  new ByteArrayInputStream(byteArrayos.toByteArray());
								String realName = appNo+sysName+getFileName(fileName,i,folder,sysName,"loan");
								loanFileName.add(realName);
								//外贸3存放 合同附件 文件路径loan_imagefile_upload
								wm3ftpUtil.changeDirectory(loanimagefileupload);
								wm3ftpUtil.uploadFile(iSteram,realName);//上传 合同附件  至外贸3FTP
								
								InputStream iSteram2 =  new ByteArrayInputStream(byteArrayos.toByteArray());
								//外贸3存放 个人查证 文件路径inquiry_imagefile_upload
								wm3ftpUtil.changeDirectory(inquiryimagefileupload);
								String realName2 = appNo+sysName+getFileName(fileName,i,folder,sysName,"inquiry");
								inquiryFileName.add(realName2);
								wm3ftpUtil.uploadFile(iSteram2,realName2);//上传 查征授权文件 至外贸3FTP
							}
						}*/
					}
				}
				
				List<String> list1 = new ArrayList<String>();
				List<String> list2 = new ArrayList<String>();
				List<String> list3 = new ArrayList<String>();
				List<String> list4 = new ArrayList<String>();
				List<String> list5 = new ArrayList<String>();
				Map<String,List<String>> mapLoan = new HashMap<String, List<String>>();
				if(CollectionUtils.isNotEmpty(loanFileName)){
					for(String name : loanFileName){
						if(name.indexOf("DKHTDZ")>0){
							list1.add(name);
							mapLoan.put("DKHTDZ", list1);
						}else if(name.indexOf("DKSQB")>0){
							list2.add(name);
							mapLoan.put("DKSQB", list2);
						}else if(name.indexOf("KKSQS")>0){
							list3.add(name);
							mapLoan.put("KKSQS", list3);
						}else if(name.indexOf("QYZP")>0){
							list4.add(name);
							mapLoan.put("QYZP", list4);
						}else if(name.indexOf("SFZFYJ")>0){
							list5.add(name);
							mapLoan.put("SFZFYJ", list5);
						}
					}
					logger.info("合同附件-文档类型 Subtype包括 " +mapLoan);
					createLoanXML(appNo,conNum,mapLoan);
					
				}
				
				/*List<String> list6 = new ArrayList<String>();
				List<String> list7 = new ArrayList<String>();
				Map<String,List<String>> mapInquiry = new HashMap<String, List<String>>();
				if(CollectionUtils.isNotEmpty(inquiryFileName)){
					for(String name : inquiryFileName){
						if(name.indexOf("GRCZSQS")>0){
							list6.add(name);
							mapInquiry.put("GRCZSQS", list6);
						}else if(name.indexOf("SFZ")>0){
							list7.add(name);
							mapInquiry.put("SFZ", list7);
						}
					}
					logger.info("查证授权-文档类型 Subtype包括 " +mapInquiry);
					createInquiryXML(appNo,conNum,mapInquiry);
				}*/
			}
			if(set.size()>0){
				saveOperateLog(set);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				zsftpUtil.closeServer();
				wm3ftpUtil.closeServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 创建合同附件xml文件，并上传到loan_imagefile_upload文件夹
	 * @param appNo
	 * @param conNum
	 * @param map
	 * @return
	 */
	public void createLoanXML(String appNo,String conNum,Map<String,List<String>> map){
		FileInputStream inputStream = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(true);
            
            Element root = document.createElement("Root");//创建根节点 
            Element task = document.createElement("Task");
            
            Element sequenceIdElement = document.createElement("SequenceId");     
            sequenceIdElement.setTextContent(sdf.format(new Date())+"-"+appNo);
            Element branchCode = document.createElement("BranchCode");     
            branchCode.setTextContent("000100030005");
            Element source = document.createElement("Source");     
            source.setTextContent("003");
            Element otherParam = document.createElement("OtherParam");     
            Element documents = document.createElement("Documents"); 
            Element documentItem = null;
            	 
            for(Entry<String, List<String>> entry : map.entrySet()){
        		String subtypeKey = entry.getKey();
        		List<String> fileNames = entry.getValue();
        		Element files = document.createElement("Files");
           	 	documentItem = document.createElement("Document");
           	 	Element busstype = document.createElement("Busstype");    
                busstype.setTextContent("XWXFL");
                Element subtype = document.createElement("Subtype");  
                subtype.setTextContent(subtypeKey);
                Element bussNo = document.createElement("BussNo");     
                bussNo.setTextContent(conNum);//业务号，信贷合同上传为合同号、征信查询授权文件为查征流水号
                Element filesCount = document.createElement("FilesCount");     
                filesCount.setTextContent(String.valueOf(fileNames.size()));//动态值
                
           	    documentItem.appendChild(busstype);
                documentItem.appendChild(subtype);
                documentItem.appendChild(bussNo);
                documentItem.appendChild(filesCount);
                documentItem.appendChild(files);
                documents.appendChild(documentItem);
        	   
                for(int m=0;m<fileNames.size();m++){
                   	 String fileName = fileNames.get(m);
                   	 if (fileName.indexOf(subtypeKey) >=0) {
                   		 Element file = document.createElement("File");     
   	                	 Element name = document.createElement("Name");     
   	                	 name.setTextContent(fileName);//动态值
   	                	 Element fileOrder = document.createElement("FileOrder");     
   	                	 fileOrder.setTextContent(String.valueOf(m+1));//动态值
   	                	 
   	                	 file.appendChild(name);
   	                	 file.appendChild(fileOrder);
   	                	 files.appendChild(file);
                   	 }
                }
            }
            //为task增加子元素
            task.appendChild(sequenceIdElement); 
            task.appendChild(branchCode); 
            task.appendChild(source); 
            task.appendChild(otherParam); 
            task.appendChild(documents); 
            
            root.appendChild(task);
            document.appendChild(root);//将根节点添加到Document对象中 
            
            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建Transformer对象
            Transformer tf = tff.newTransformer();
            // 设置输出数据时换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource =  new DOMSource(document);
            // 使用Transformer的transform()方法将DOM树转换成XML
            //tf.transform(domSource, new StreamResult(dest));
            
            File localFile = new File(sdf.format(new Date())+"-"+appNo+".xml");
            FileOutputStream out = new FileOutputStream(localFile);
            inputStream = new FileInputStream(localFile);
            StreamResult xmlResult = new StreamResult(out);
            tf.transform(domSource, xmlResult);
            //System.out.println(localFile.getAbsolutePath());//测试文件输出的路径
            
            //外贸3存放 合同附件 文件路径loan_imagefile_upload
            wm3ftpUtil.changeDirectory(loanimagefileupload);
            wm3ftpUtil.uploadFile(inputStream,localFile.getName());//上传 合同附件 至外贸3FTP
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * 创建合同附件xml文件，并上传到inquiry_imagefile_upload文件夹
	 * @param appNo
	 * @param conNum
	 * @param map
	 * @return
	 */
	/*
	public void createInquiryXML(String appNo,String conNum,Map<String,List<String>> map){
		FileInputStream inputStream = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.setXmlVersion("1.0");
            document.setXmlStandalone(true);
            
            Element root = document.createElement("Root");//创建根节点 
            Element task = document.createElement("Task");      
            
            Element sequenceIdElement = document.createElement("SequenceId");     
            sequenceIdElement.setTextContent(sdf.format(new Date())+"-"+appNo);
            Element branchCode = document.createElement("BranchCode");     
            branchCode.setTextContent("000100060005");//征信查询业务查征授权文件上传的合作机构号为 000100060001。
            Element source = document.createElement("Source");     
            source.setTextContent("006");
            Element otherParam = document.createElement("OtherParam");     
            Element documents = document.createElement("Documents"); 
            Element documentItem = null;
            	 
            for(Entry<String, List<String>> entry : map.entrySet()){
        		String subtypeKey = entry.getKey();
        		List<String> fileNames = entry.getValue();
        		Element files = document.createElement("Files");
           	 	documentItem = document.createElement("Document");
           	 	Element busstype = document.createElement("Busstype");    
                busstype.setTextContent("GRCZ");
                Element subtype = document.createElement("Subtype");
                subtype.setTextContent(subtypeKey);//文档类型，标识上传的文档类型  贷款申请表 身份证 贷款合同 签约照片 扣款授权书
                Element bussNo = document.createElement("BussNo");//业务号，信贷合同上传为合同号、征信查询授权文件为查征流水号
                bussNo.setTextContent(conNum);// 进件批量申请2101接口中 查证流水号（czPactNo）  待确定
                Element filesCount = document.createElement("FilesCount");//文件个数，文档类型贷款申请表下有几个文件就填入相应数字；如有3个文件，则填写案例如下
                filesCount.setTextContent(String.valueOf(fileNames.size()));//动态值
                
           	    documentItem.appendChild(busstype);
                documentItem.appendChild(subtype);
                documentItem.appendChild(bussNo);
                documentItem.appendChild(filesCount);
                documentItem.appendChild(files);
                documents.appendChild(documentItem);
        	   
                for(int m=0;m<fileNames.size();m++){
                   	 String fileName = fileNames.get(m);
                   	 if (fileName.indexOf(subtypeKey) >=0) {
                   		 Element file = document.createElement("File");     
   	                	 Element name = document.createElement("Name");     
   	                	 name.setTextContent(fileName);//动态值
   	                	 Element fileOrder = document.createElement("FileOrder");     
   	                	 fileOrder.setTextContent(String.valueOf(m+1));//动态值
   	                	 
   	                	 file.appendChild(name);
   	                	 file.appendChild(fileOrder);
   	                	 files.appendChild(file);
                   	 }
                }
            }
            //为task增加子元素
            task.appendChild(sequenceIdElement); 
            task.appendChild(branchCode); 
            task.appendChild(source); 
            task.appendChild(otherParam); 
            task.appendChild(documents); 
            
            root.appendChild(task);
            document.appendChild(root);//将根节点添加到Document对象中 
            
            // 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
            // 创建Transformer对象
            Transformer tf = tff.newTransformer();
            // 设置输出数据时换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource =  new DOMSource(document);
            // 使用Transformer的transform()方法将DOM树转换成XML
            //tf.transform(domSource, new StreamResult(dest));
            File localFile = new File(sdf.format(new Date())+"-"+appNo+".xml");
            FileOutputStream out = new FileOutputStream(localFile);
            inputStream = new FileInputStream(localFile);
            StreamResult xmlResult = new StreamResult(out);
            tf.transform(domSource, xmlResult);
            //System.out.println(localFile.getAbsolutePath());//测试文件输出的路径
            
            //外贸3存放 个人查证  文件路径inquiry_imagefile_upload
            wm3ftpUtil.changeDirectory(inquiryimagefileupload);
            wm3ftpUtil.uploadFile(inputStream,localFile.getName());//上传 查征授权文件 至外贸3FTP
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
	
	
	
	/**
	 * 保存操作日志  只保存上传影像资料成功的 债权
	 * @param set 根据appNo,保存债权数据
	 */
	public void saveOperateLog(HashSet<String> set){
		logger.info("【外贸3】上传影像资料接口-保存操作日志!");
		Map<String,Object> map =new  HashMap<String,Object>();
		if(set.size()>0){
			logger.info("待保存到操作日子表中appNo有："+set);
			map.put("appNos", set);
			List<VLoanInfo> vLoanInfoList = vloanInfoDao.findListByMap(map);
			for(VLoanInfo vLoanInfo : vLoanInfoList){
				OperateLog log = new OperateLog();
				log.setId(sequencesService.getSequences(SequencesEnum.OPERATE_LOG));
				log.setLoanId(vLoanInfo.getId());
				log.setOperateType("03");//01：上传还款计划，02：上传合同资料、03：上传影像资料、04：上传分账明细 
				log.setOperateDate(new Date());
				log.setStatus("1");
				operateLogService.save(log);
			}
		}
	}
	
	/**
	 *  更改上传文件的名字
	 * @param fileName 
	 * @param i
	 * @param folderName
	 * @param sysName 系统名字 aps 或 cfs
	 * @param type  合同上传loan或 查征授权文件inquiry
	 * @return
	 */
	public String getFileName(String fileName,int i,String folderName,String sysName,String type){
		if(APS.equals(sysName)){
			if(fileName.substring(0,1).equals(apsDKSQB)){
				fileName = "DKSQB"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(fileName.substring(0,2).equals(apsDKHTDZ) && apsDKHTDZ.equals(folderName)){
				fileName = "DKHTDZ"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(fileName.substring(0,2).equals(apsKKSQS)){
				fileName = "KKSQS"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(fileName.substring(0,2).equals(apsSFZ) && "loan".equals(type)){
				fileName = "SFZFYJ"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(fileName.substring(0,3).equals(apsQYZP) && apsQYZP.equals(folderName)){
				fileName = "QYZP"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}/*else if(fileName.substring(0,1).equals(apsGRCZSQS)){
				fileName = "GRCZSQS"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(fileName.substring(0,2).equals(apsSFZ) && "inquiry".equals(type)){
				fileName = "SFZ"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}*/
		}else if(CFS.equals(sysName)){
			if(apsDKSQB.equals(folderName)){
				fileName = "DKSQB"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(apsDKHTDZ.equals(folderName)){
				fileName = "DKHTDZ"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(apsKKSQS.equals(folderName)){
				fileName = "KKSQS"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(apsSFZ.equals(folderName) && "loan".equals(type)){
				fileName = "SFZFYJ"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(apsQYZP.equals(folderName) && apsQYZP.equals(folderName)){
				fileName = "QYZP"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}/*else if(apsGRCZSQS.equals(folderName)){
				fileName = "GRCZSQS"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}else if(apsSFZ.equals(folderName) && "inquiry".equals(type)){
				fileName = "SFZ"+(i+1)+"."+fileName.substring(fileName.length()-3, fileName.length());
			}*/
		}
		return fileName;
	}
	
}

