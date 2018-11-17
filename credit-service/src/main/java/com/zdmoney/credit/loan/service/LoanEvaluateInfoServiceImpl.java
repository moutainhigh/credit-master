package com.zdmoney.credit.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.loan.dao.pub.ILoanEvaluateInfoDao;
import com.zdmoney.credit.loan.domain.LoanEvaluateInfo;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanEvaluateInfoService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanEvaluateInfoServiceImpl implements ILoanEvaluateInfoService {
	 protected static Log logger = LogFactory.getLog(LoanEvaluateInfoServiceImpl.class);
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanTransferInfoService loanTransferInfoService;
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired
	IPersonInfoService personInfoService;
	@Autowired
	ILoanEvaluateInfoDao loanEvaluateInfoDao;
	
	@Override
	public List<Map<String, Object>> findLoanEvaluateInfoList(Map<String, Object> params) {
		return loanEvaluateInfoDao.findLoanEvaluateInfoList(params);
	}

	@Override
	public LoanEvaluateInfo findLoanEvaluateInfo(Map<String, Object> params) {
		return loanEvaluateInfoDao.findLoanEvaluateInfo(params);
	}
	
	@Override
	public LoanEvaluateInfo bulidLoanEvaluateInfo(Map<String, String> map) {
		String contractNum = map.get("contractNum").trim();//管理营业部
		String name = map.get("name").trim();//姓名
		String idNum = map.get("idNum").trim();//身份证号码
		String isEval = map.get("isEval").trim();//是否评估
		Assert.notNullAndEmpty(contractNum, "合同编号");
		Assert.notNullAndEmpty(name, "姓名");
		Assert.notNullAndEmpty(idNum, "身份证号码");
		Assert.notNullAndEmpty(isEval, "是否评估");
        
        VLoanInfo vLoanInfoVo = new VLoanInfo();
		vLoanInfoVo.setContractNum(contractNum);
		List<VLoanInfo> loanList = vLoanInfoService.findListByVO(vLoanInfoVo);
		Assert.notCollectionsEmpty(loanList, ResponseEnum.FULL_MSG, "合同编号不存在，请核对信息后再次导入");
		
		//已转让过的债权，不能再次进行债权评估的导入
		Long loanId = loanList.get(0).getId();
		LoanTransferInfo transfer = loanTransferInfoService.findLoanTransferInfoByLoanId(loanId);
		if(transfer != null && !"未转让".equals(transfer.getTransferBatch())){
			throw new PlatformException(ResponseEnum.FULL_MSG, "该债权已转让，无法再次导入！");
		}
		
		Long personId = loanList.get(0).getBorrowerId();
		PersonInfo personInfo = personInfoService.findById(personId);
		Assert.notNullAndEmpty(personInfo, ResponseEnum.FULL_MSG, "借款人不存在");
		String perIdNum = personInfo.getIdnum();
		String perName = personInfo.getName();
		if(!name.equals(perName) || !idNum.equals(perIdNum)){
			throw new PlatformException(ResponseEnum.FULL_MSG, "姓名或身份证号与核心系统借款人不匹配！");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("contractNum",contractNum);
        LoanEvaluateInfo loanEvaluateInfo = this.findLoanEvaluateInfo(params);
        if(loanEvaluateInfo == null){
        	loanEvaluateInfo = new LoanEvaluateInfo();
        }
        loanEvaluateInfo.setLoanId(loanId);
        loanEvaluateInfo.setContractNum(contractNum);
        loanEvaluateInfo.setCustomerName(name);
        loanEvaluateInfo.setIdNum(perIdNum);
        loanEvaluateInfo.setIsEval(isEval);
        
        if(loanEvaluateInfo.getId() != null){
        	loanEvaluateInfo.setId(loanEvaluateInfo.getId());
        	loanEvaluateInfoDao.update(loanEvaluateInfo);
        	return loanEvaluateInfo;
        }
    	loanEvaluateInfo.setId(sequencesService.getSequences(SequencesEnum.LOAN_EVALUATE_INFO));
    	loanEvaluateInfoDao.insert(loanEvaluateInfo);
		return loanEvaluateInfo;
	}

	@Override
	public Map<String, Object> updateLoanEvaluateSign(String loanId) {
		Map<String, Object> json = new HashMap<String, Object>();
		//如果债权不存在，返回失败。
		VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(Long.valueOf(loanId));
		if (vLoanInfo == null) {
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：loanId输入有误，未查到对应的债权记录！");
			return json;
		}
		
		//如果债权已转让，无法修改，返回失败。
		LoanTransferInfo loanTransfer = loanTransferInfoService.findLoanTransferInfoByLoanId(Long.valueOf(loanId));
		if(loanTransfer != null && !"未转让".equals(loanTransfer.getTransferBatch())){
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：该债权已转让，无法修改评估标记！");
			return json;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loanId",loanId);
		LoanEvaluateInfo loanEval = this.findLoanEvaluateInfo(params);
		if(loanEval == null){
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：loanId输入有误，未找到对应的评估数据！");
			return json;
		}
		
		loanEval.setId(loanEval.getId());
		loanEval.setIsEval("否");//取消评估标记
		loanEvaluateInfoDao.update(loanEval);
		json.put("code", Constants.SUCCESS_CODE);
		json.put("message", "成功");
		return json;
	}

}
