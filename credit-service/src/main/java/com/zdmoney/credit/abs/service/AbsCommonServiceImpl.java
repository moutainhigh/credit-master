package com.zdmoney.credit.abs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.abs.service.pub.IAbsCommonService;
import com.zdmoney.credit.common.constant.UploadFtpAddressType;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.grant.dao.pub.ILoanBaseGrantDao;
import com.zdmoney.credit.loan.dao.pub.IUploadFtpFileLogDao;
import com.zdmoney.credit.loan.domain.UploadFtpFileLog;
import com.zdmoney.credit.signature.dao.pub.IElectronicSignatureLogDao;
import com.zdmoney.credit.signature.domain.ElectronicSignatureLog;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class AbsCommonServiceImpl implements IAbsCommonService{
	
	private static Logger logger = Logger.getLogger(AbsCommonServiceImpl.class);

	@Autowired
	ILoanBaseGrantDao loanBaseGrantDaoImpl;
	@Autowired
	IUploadFtpFileLogDao uploadFtpFileLogDaoImpl;
	@Autowired
	ISequencesService sequencesServiceImpl;
    @Autowired
    private IElectronicSignatureLogDao electronicSignatureLogDao;
	
	@Override
	public boolean pushUploadFtpFileLog() {
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", "1");
			params.put("sort", "id");
			params.put("signatureBelong", "abs");
			params.put("uploadTimeStart", Dates.getDateTime(Dates.getBeforeDays(15), "yyyy-MM-dd"));
			List<ElectronicSignatureLog> electronisLogs =electronicSignatureLogDao.findAbsData2Upload(params);
			if(electronisLogs != null && electronisLogs.size() > 0){
				//避免重复申请
				for(ElectronicSignatureLog electronisLog:electronisLogs){
					String contractNum = electronisLog.getContractNum();
					String appNo = electronisLog.getAppNo();
					if(Strings.isEmpty(contractNum) || Strings.isEmpty(appNo)){
						continue;
					}
					UploadFtpFileLog uploadLog = new UploadFtpFileLog();
					uploadLog.setAppNo(appNo);
					uploadLog.setUploadAddress(UploadFtpAddressType.数信.getAddressType());
					uploadLog.setContractNum(contractNum);
					uploadLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.UPLOAD_FTP_FILE_LOG));
					uploadFtpFileLogDaoImpl.insert(uploadLog);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("生成待上传的影像文件记录报错UploadFtpFileLog...", e);
			return false;
		}
		return true;
	}

	@Override
	public List<UploadFtpFileLog> findWmxt2UploadFtpLog2Abs() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uploadAddress", UploadFtpAddressType.数信.getAddressType());
		params.put("status", 0);
		List<UploadFtpFileLog> list = uploadFtpFileLogDaoImpl.findUploadFtpFileByMap(params);
		return list;
	}

	@Override
	public void uploadUploadFileLogStatus(List<String> contractNos) {
		for(String contractNo : contractNos){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("contractNum", contractNo);
			params.put("uploadAddress", UploadFtpAddressType.数信.getAddressType());
			uploadFtpFileLogDaoImpl.updateUploadFtpFileLogStatus2Success(params);
		}
	}

}
