package com.zdmoney.credit.loan.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LxxdUploadRespEnum;
import com.zdmoney.credit.common.constant.UploadFtpAddressType;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.EnumUtil;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.dao.pub.IUploadLoanDataInfoDao;
import com.zdmoney.credit.loan.domain.UploadLoanDataInfo;
import com.zdmoney.credit.loan.service.pub.IUploadLoanDataInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.VLoanDebtInfo;
import com.zdmoney.credit.loan.vo.VloanPersonInfo;
import com.zdmoney.credit.person.service.pub.IPersonContactInfoService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.video.vo.DownLoadVideoDirVo;
import com.zdmoney.credit.video.vo.DownLoadVideoFileVo;

/**
 * @author 10098  2017年3月1日 上午11:33:38
 */
@Service
public class UploadLoanDataInfoServiceImpl implements IUploadLoanDataInfoService {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private IVLoanInfoService vLoanInfoService;
	@Autowired
	private IUploadLoanDataInfoDao uploadLoanDataInfoDao;
	@Autowired
	private ISequencesService sequencesService;
	@Autowired
	private IPersonInfoService personInfoService;
	@Autowired
	private IPersonContactInfoService personContactInfoService;
	private static final String lendingNoticePath = "放款/放款通知书";
	@Override
	public void pushLoanData2UploadInfo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loanStates", new String[]{LoanStateEnum.正常.getValue(),
				LoanStateEnum.逾期.getValue(),LoanStateEnum.预结清.getValue(),LoanStateEnum.结清.getValue()});
		params.put("loanBelong", FundsSourcesTypeEnum.龙信小贷.getValue());
		Date date = Dates.getBeforeDays(30);
		params.put("grantMoneyDateStart", Dates.getDateTime(date, Dates.DEFAULT_DATE_FORMAT));
		List<VloanPersonInfo> list = vLoanInfoService.findLoanData2UploadInfo(params);
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		for(VloanPersonInfo vloanPersonInfo : list){
			try{
				UploadLoanDataInfo uploadLoanDataInfo  = new UploadLoanDataInfo();
				Long id = sequencesService.getSequences(SequencesEnum.UPLOAD_LOAN_DATA_INFO);
				uploadLoanDataInfo.setId(id);
				uploadLoanDataInfo.setAppNo(vloanPersonInfo.getAppNo());
				uploadLoanDataInfo.setContractNum(vloanPersonInfo.getContractNum());
				uploadLoanDataInfo.setIdNum(vloanPersonInfo.getIdnum());
				uploadLoanDataInfo.setLoanId(vloanPersonInfo.getId());
				uploadLoanDataInfo.setUploadAddress(UploadFtpAddressType.龙信小贷.getAddressType());
				uploadLoanDataInfo.setUploadStatus(UploadFtpAddressType.待上传.getUploadStatus());
				uploadLoanDataInfoDao.insert(uploadLoanDataInfo);
			}catch(Exception e){
				logger.error("债权信息上传至龙心小贷——插入上传信息表报错， 合同号："+vloanPersonInfo.getContractNum(), e);
				continue;
			}
		}
	}

	@Override
	public void pushLoanData2UploadInfoByBatchNum(String batchNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("batchNum", batchNum);
		List<VloanPersonInfo> list = vLoanInfoService.findLoanData2UploadInfo(params);
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		for(VloanPersonInfo vloanPersonInfo : list){
			try{
				UploadLoanDataInfo uploadLoanDataInfo  = new UploadLoanDataInfo();
				Long id = sequencesService.getSequences(SequencesEnum.UPLOAD_LOAN_DATA_INFO);
				uploadLoanDataInfo.setId(id);
				uploadLoanDataInfo.setBatchNum(vloanPersonInfo.getBatchNum());
				uploadLoanDataInfo.setAppNo(vloanPersonInfo.getAppNo());
				uploadLoanDataInfo.setContractNum(vloanPersonInfo.getContractNum());
				uploadLoanDataInfo.setIdNum(vloanPersonInfo.getIdnum());
				uploadLoanDataInfo.setLoanId(vloanPersonInfo.getId());
				uploadLoanDataInfo.setUploadAddress(UploadFtpAddressType.龙信小贷.getAddressType());
				uploadLoanDataInfo.setUploadStatus(UploadFtpAddressType.待上传.getUploadStatus());
				uploadLoanDataInfoDao.insert(uploadLoanDataInfo);
			}catch(Exception e){
				logger.error("债权信息上传至龙心小贷——插入上传信息表报错， 合同号："+vloanPersonInfo.getContractNum(), e);
				continue;
			}
		}
	}
	
	@Override
	public List<UploadLoanDataInfo> findUploadLoanDataInfo2LXXD() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uploadAddress", UploadFtpAddressType.龙信小贷.getAddressType());
		params.put("uploadStatusList", new String[]{UploadFtpAddressType.上传失败.getUploadStatus(), UploadFtpAddressType.待上传.getUploadStatus()});
		return uploadLoanDataInfoDao.findListByMap(params);
	}
	
	@Override
	public List<UploadLoanDataInfo> findUploadLoanDataInfo2LXXDByBatchNum(String batchNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("batchNum", batchNum);
		params.put("uploadAddress", UploadFtpAddressType.龙信小贷.getAddressType());
		params.put("uploadStatusList", new String[]{UploadFtpAddressType.上传失败.getUploadStatus(), UploadFtpAddressType.待上传.getUploadStatus()});
		return uploadLoanDataInfoDao.findUploadLoanDataInfoByBatchNum(params);
	}


	@Override
	public int updateLoanDataInfoByVo(UploadLoanDataInfo uploadLoanDataInfo) {
		return  uploadLoanDataInfoDao.update(uploadLoanDataInfo);
	}


	@Override
	public void addDownLoadDirs4LXXD(List<DownLoadVideoDirVo> downLoanList, UploadLoanDataInfo uploadLoanDataInfo) {
		addDownLoadDirsLendingNotice4LXXD(downLoanList, uploadLoanDataInfo);
		addDownLoadDirsCustomerInfo4LXXD(downLoanList, uploadLoanDataInfo);
    }

	/**
	 * 龙信小贷 上传文件 客户信息文件
	 * @param downLoanList
	 * @param uploadLoanDataInfo
	 */
	private boolean addDownLoadDirsCustomerInfo4LXXD(List<DownLoadVideoDirVo> downLoanList,UploadLoanDataInfo uploadLoanDataInfo) {
		boolean res = false;
		try{
			String workBookName =  "客户信息.xlsx";
	        // 获取文件后缀名
	        String extensionName = workBookName.substring(workBookName.lastIndexOf(".") + 1);
	        // 创建工作薄
	        Workbook workbook = ExcelExportUtil.createWorkbook(extensionName);
	        //创建客户信息表格页
			String customerSheeName = "客户信息";
	        String[] customerLabels  = new String[]{"客户名称","证件类型","证件类型编号","证件号码","出生日期","性别","性别编号","手机号码","通讯地址","居住状况","婚姻状况类型","婚姻状况类型编号","最高学历","学历类型编号","最高学位","学位编号","邮政编码","客户所属地区","客户所属地区编号","个人月收入","民族","政治面貌","户口性质","主要经济来源","其他经济来源","供养人数","家庭人口数"};
	        String[] customerFields = new String[]{"name","idType","idTypeCode","idNum","birthday","sex","sexCode","mobilePhone","contactAddress","livingStatus","marryStatus","marryStatusCode","edLevel","edLevelCode","degree","degreeNo","postCode","areaBelong","areaBelongCode","income"};
	        Map<String,Object> customerParams = new HashMap<String, Object>();
	        customerParams.put("idNum", uploadLoanDataInfo.getIdNum());
	        List<Map<String, Object>> customerDataList = personInfoService.getCustomerInfo4LXXD(customerParams);
	        if(CollectionUtils.isEmpty(customerDataList)){
	        	logger.info("龙信小贷债权信息上传，没找到客户信息");
	        	return res;
	        }
	        Long personId = Long.parseLong(customerDataList.get(0).get("personId").toString());
	        editDataDictionry(customerDataList);
	        ExcelExportUtil.createWorkSheetByMap(workbook,0,customerSheeName,customerLabels,customerFields,customerDataList);
	        //创建职业信息表格页
	        String professionSheetName = "职业信息";
	        String[] professionLabels = new String[]{"职业","所在单位","所属行业","单位行业类型编号","单位性质","单位性质编号","单位电话","单位地址","本单位工作起始年份","职务"};
	        String[] professionFields = new String[]{"profession","company","industryType","industryTypeCode","companyType","companyTypeCode","companyTel","companyAddress","companyEntryYear"};
	        ExcelExportUtil.createWorkSheetByMap(workbook,1,professionSheetName,professionLabels,professionFields,customerDataList);
	        //创建社会关系表格页
	        String contactSheetName = "社会关系";
	        String[] contactLabels = new String[]{"与客户关系","客户关系编号","姓名","证件号码","联系电话","工作单位","联系人居住地址"};
	        String[] contactFields = new String[]{"relation","relationTypeCode","name","idNum","mobilePhone","company","address"};
	        Map<String, Object> contactParams = new HashMap<String, Object>();
	        contactParams.put("personId", personId);
	        List<Map<String, Object>> contactDataList = personContactInfoService.findContactInfo4LXXD(contactParams);
	        ExcelExportUtil.createWorkSheetByMap(workbook, 2, contactSheetName, contactLabels, contactFields, contactDataList);
	        //创建贷款申请信息表格页
	        String loanSheetName = "贷款申请信息";
	        String[] loanLabels = new String[]{"贷款金额","期限月","期限日","放款金额","卡号","姓名","收款行"};
	        String[] loanFields = new String[]{"loanMoney","time","returnDay","pactMoney","bankAccount","name","bankName"};	        
	        List<Map<String, Object>> loanDataList = vLoanInfoService.findPersonLoanInfo(contactParams);
	        ExcelExportUtil.createWorkSheetByMap(workbook, 3, loanSheetName, loanLabels, loanFields, loanDataList);
	        
			DownLoadVideoDirVo dirVo = new DownLoadVideoDirVo();
			dirVo.setFullPath("");
			List<DownLoadVideoFileVo> fileList = new ArrayList<DownLoadVideoFileVo>();
			dirVo.setFileList(fileList);
			DownLoadVideoFileVo fileVo = new DownLoadVideoFileVo();
			fileList.add(fileVo);
			fileVo.setFileName(workBookName);
			ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream(2048);
			workbook.write(byteArrayos);
			fileVo.setOutputStream(byteArrayos);
			downLoanList.add(dirVo);
	        res = true;
		}catch(Exception e){
			logger.error("龙信小贷 获取客户信息文件流报错", e);
			return res;			
		}
		return res;
	}

	/**
	 * 龙信小贷 上传文件 放款通知书
	 * @param downLoanList
	 * @param uploadLoanDataInfo
	 */
	private boolean addDownLoadDirsLendingNotice4LXXD(List<DownLoadVideoDirVo> downLoanList, UploadLoanDataInfo uploadLoanDataInfo) {
		boolean res = false;
        Map<String, Object> paramMap = new HashMap<>();
		String lastReapyday1 = new SimpleDateFormat("yyyy-MM-dd").format(Dates.getLastRepayDate(new Date()));
		String lastReapyday2 = new SimpleDateFormat("yyyy-MM-dd").format(Dates.getLastRepayDate(Dates.getLastRepayDate(new Date())));
		paramMap.put("lastReapyday1", lastReapyday1);
		paramMap.put("lastReapyday2", lastReapyday2);
		paramMap.put("loanId", uploadLoanDataInfo.getLoanId());
		List<VLoanDebtInfo> lendingNoticeList = vLoanInfoService.getVLoanDebtInfoExportList(paramMap);
		if(CollectionUtils.isEmpty(lendingNoticeList)){
			return res;
		}
	    String fileName = uploadLoanDataInfo.getContractNum() + ".xls";
	    String[] labels = new String[] { "借款ID", "合同编号", "产品类型", "借款用途", "合同金额",
	            "借款期限", "已还期数", "首还款日期", "真实姓名", "性别", "身份证号", "产品批次号",
	            "放款银行卡开户行", "支行", "卡号", "放款金额", "服务费金额", "债权签约日期",
	            "到期日", "每月还款金额", "放款营业部","录单营业部类型" };
	    String[] fields = new String[] { "id", "contract_num", "loan_type",
	            "purpose", "pact_money", "time", "backTerm",
	            "startrdate", "acct_name", "borrower_sex",
	            "borrower_idnum", "batchNum", "bank_name", "full_name",
	            "account", "grant_money", "fuwufei", "sign_Date",
	            "endRDate", "returneterm", "signSalesDep_fullName","applyInputFlag" };
	    // 工作表名称
	    String sheetName = "Export";
	    // 创建工作簿
	    try {
			Workbook workbook = ExcelExportUtil.createExcelByVo("龙信债权_供理财.xls",labels, fields, lendingNoticeList, sheetName);
			DownLoadVideoDirVo dirVo = new DownLoadVideoDirVo();
			dirVo.setFullPath(lendingNoticePath);
			List<DownLoadVideoFileVo> fileList = new ArrayList<DownLoadVideoFileVo>();
			dirVo.setFileList(fileList);
			DownLoadVideoFileVo fileVo = new DownLoadVideoFileVo();
			fileList.add(fileVo);
			fileVo.setFileName(fileName);
			ByteArrayOutputStream byteArrayos = new ByteArrayOutputStream(2048);
			workbook.write(byteArrayos);
			fileVo.setOutputStream(byteArrayos);
			downLoanList.add(dirVo);
			res = true;
		} catch (Exception e) {
			logger.error("龙信小贷 获取放款通知书文件流报错", e);
			return res;
		}
	    return res;
	}

	//龙信小贷数据字典转换
	public void editDataDictionry(List<Map<String, Object>> dataList){
		for(Map<String, Object> map:dataList){
			if(map.containsKey("industryType")){
				String params = formatLxxdIndustryType(map.get("industryType").toString().trim());
				map.put("industryType", params.substring(params.indexOf("_")+1));
				map.put("industryTypeCode", params.substring(0, params.indexOf("_")));
			}
			if(map.containsKey("companyType")){
				String params = formatLxxdCompanyType(map.get("companyType").toString().trim());
				map.put("companyType", params.substring(params.indexOf("_")+1));
				map.put("companyTypeCode", params.substring(0, params.indexOf("_")));
			}
		}
	}
	
	private String formatLxxdIndustryType(String industryType){
		if(Strings.isEmpty(industryType)){
            return "z_未知";
        }
		for(String key:LXXD_INDUSTRY_TYPE.keySet()){
			if(isContain(industryType, LXXD_INDUSTRY_TYPE.get(key))){
				return key;
			}
		}
		return "z_未知";
	}
	
	private String formatLxxdCompanyType(String companyType){
		if(Strings.isEmpty(companyType)){
            return "9_其他";
        }
		for(String key:LXXD_COMPANY_TYPE.keySet()){
			if(isContain(companyType, LXXD_COMPANY_TYPE.get(key))){
				return key;
			}
		}
		return "9_其他";
	}
	
	private boolean isContain(String type,String ...values){
		for(String value:values){
			if(value.indexOf(type) >-1){
				return true;
			}
		}
		return false;
	}
	//龙信小贷 产业类型字典与核心映射关系
	public static final Map<String, String[]> LXXD_INDUSTRY_TYPE = new HashMap<String, String[]>();
	static{
		LXXD_INDUSTRY_TYPE.put("0_机关、组织、机构负责人", new String[]{""});
		LXXD_INDUSTRY_TYPE.put("1_专业技术人员", new String[]{"教育","建筑","电子","医疗","广告","金融","机械"});
		LXXD_INDUSTRY_TYPE.put("3_办事人员及有关人员", new String[]{"事业单位"});
		LXXD_INDUSTRY_TYPE.put("4_农林牧渔水利业生产人员", new String[]{"农","林","牧","渔"});
		LXXD_INDUSTRY_TYPE.put("6_商业服务人员", new String[]{"服务","销售","贸","零售","餐饮","服装","批发"});
		LXXD_INDUSTRY_TYPE.put("5_生产、运输设备操作人员", new String[]{"制造","物流","建材","加工","运输","生产","五金"});
		LXXD_INDUSTRY_TYPE.put("x_军人", new String[]{"军人"});
		LXXD_INDUSTRY_TYPE.put("y_不便分类的其它从业人员", new String[]{""});
		LXXD_INDUSTRY_TYPE.put("z_未知", new String[]{""});
	}
	
	//龙信小贷 公司类型字典与核心映射关系
	public static final Map<String, String[]> LXXD_COMPANY_TYPE = new HashMap<String, String[]>();
	static{
		LXXD_COMPANY_TYPE.put("1_机关事业", new String[]{"政府机构","事业单位"});
		LXXD_COMPANY_TYPE.put("2_三资", new String[]{"外资","合资"});
		LXXD_COMPANY_TYPE.put("3_国营", new String[]{"国企"});
		LXXD_COMPANY_TYPE.put("4_集体", new String[]{""});
		LXXD_COMPANY_TYPE.put("5_私营", new String[]{"私营","民营"});
		LXXD_COMPANY_TYPE.put("6_个体", new String[]{"个体"});
		LXXD_COMPANY_TYPE.put("9_其他", new String[]{"其他"});
	}

	@Override
	public void uploadLoanDataThread(final String batchNum) {
		ExecutorService executorService = null;
		try{
			executorService = Executors.newFixedThreadPool(1);
			executorService.execute(new Runnable() {
				public void run() {
					uploadLoanData2LXXD(batchNum);					
				}
			});
		}catch(Exception e){
			logger.error("上传文件至龙心小贷异常",e);
		}finally{
			if (executorService != null) {
				executorService.shutdown();
            }
		}
	}
	
	public void uploadLoanData2LXXD(String batchNum){
		boolean flag = false;
		//龙信小贷把放款成功的债权信息数据插入到上传表中
		pushLoanData2UploadInfoByBatchNum(batchNum);
		//获取待上传 与上传失败 状态的 债权信息
		List<UploadLoanDataInfo> list = findUploadLoanDataInfo2LXXDByBatchNum(batchNum);
		if(CollectionUtils.isEmpty(list)){
			logger.info("没有待上传的债权信息至龙信小贷...");
			return;
		}
		FTPUtil apsFtp  = new FTPUtil();
		FTPUtil lxxdFtp = new FTPUtil();
		try {
			apsFtp.connectServer(ConnectBhxtFtpServiceImpl.host,ConnectBhxtFtpServiceImpl.port,ConnectBhxtFtpServiceImpl.userName,ConnectBhxtFtpServiceImpl.password,ConnectBhxtFtpServiceImpl.storeDir,FTPUtil.dateTimes,FTPUtil.connectTimes*6);
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
			lxxdFtp.connectServer(ConnectBhxtFtpServiceImpl.hostLXXD,ConnectBhxtFtpServiceImpl.portLXXD,ConnectBhxtFtpServiceImpl.userNameLXXD,ConnectBhxtFtpServiceImpl.passWordLXXD,ConnectBhxtFtpServiceImpl.uploadFtpDirLXXD,FTPUtil.dateTimes,FTPUtil.connectTimes*6);
			lxxdFtp.enterLocalPassiveMode();
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
			addDownLoadDirs4LXXD(downLoanList,uploadLoanDataInfo);
			flag = uploadLoanDataInfo2Lxxd(lxxdFtp, downLoanList, uploadLoanDataInfo,ConnectBhxtFtpServiceImpl.uploadFtpDirLXXD);
			if(flag){
				idNums.add(uploadLoanDataInfo.getIdNum());
				uploadLoanDataInfo.setUploadStatus(UploadFtpAddressType.上传成功.getUploadStatus());
			}else{
				uploadLoanDataInfo.setUploadStatus(UploadFtpAddressType.上传失败.getUploadStatus());
			}
			updateLoanDataInfoByVo(uploadLoanDataInfo);
		}
		if(CollectionUtils.isNotEmpty(idNums)){
			invokingLxxdUploadPort(idNums,Dates.getDateTime(new Date(), Dates.DATAFORMAT_YYYYMMDD));	
		}
		logger.info("上传债权信息至龙信小贷结束。。。");
		try {
			apsFtp.closeServer();
			lxxdFtp.closeServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				String dirPath = Strings.getCharacterTransfer(filePath.toString(), Strings.CHARACTER_GBK, Strings.CHARACTER_ISO);
				lxxdFtp.createDeepFilePaths4Pasv(dirPath);
				List<DownLoadVideoFileVo> fileVos = dirVo.getFileList();
				for(DownLoadVideoFileVo fileVo:fileVos){
					String fileName = fileVo.getFileName();
					fileName = Strings.getCharacterTransfer(fileName, Strings.CHARACTER_GBK, Strings.CHARACTER_ISO);
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
}
