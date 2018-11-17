package com.zdmoney.credit.loan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.grant.FinanceGrantEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.grant.dao.pub.ILoanBaseGrantDao;
import com.zdmoney.credit.grant.domain.LoanBaseGrant;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBaseGrantService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * @author 10098  2017年5月4日 下午3:11:00
 */
@Service
public class LoanBaseGrantServiceImpl implements ILoanBaseGrantService {

	@Autowired
	ILoanBaseGrantDao loanBaseGrantDao;
	@Autowired
	ISequencesService sequencesService;

	@Override
	public LoanBaseGrant createLoanBaseGrant(VLoanInfo vLoanInfo) {
        LoanBaseGrant loanBaseGrant = new LoanBaseGrant();
        loanBaseGrant.setId(sequencesService.getSequences(SequencesEnum.LOAN_BASE_GRANT));
        loanBaseGrant.setLoanId(vLoanInfo.getId());
        loanBaseGrant.setGrantApplyDate(new Date());
        loanBaseGrant.setGrantState(FinanceGrantEnum.申请中.getCode());
        loanBaseGrant.setUpdateTime(new Date());
        loanBaseGrant.setAppNo(vLoanInfo.getAppNo());
        loanBaseGrant.setContractNum(vLoanInfo.getContractNum());
        return loanBaseGrantDao.insert(loanBaseGrant);
	}

	@Override
	public void updateLoanBaseGrantByLoanId(Long loanId, String status) {
		LoanBaseGrant loanBaseGrant = new LoanBaseGrant();
		loanBaseGrant.setLoanId(loanId);
		List<LoanBaseGrant> list = loanBaseGrantDao.findListByVo(loanBaseGrant);
		if(CollectionUtils.isEmpty(list)){
			throw new PlatformException(ResponseEnum.FULL_MSG, "根据loanId未找到放款申请记录");
		}
		LoanBaseGrant lbg = list.get(list.size()-1);
		lbg.setGrantState(status);
		if(FinanceGrantEnum.放款成功.getCode().equals(status)){
			lbg.setGrantApplyFinishDate(new Date());
		}
		loanBaseGrantDao.update(lbg);
	}

	@Override
	public boolean checkLoanBaseGrantStatus(Long loanId, String grantState) {
		LoanBaseGrant loanBaseGrant = new LoanBaseGrant();
		loanBaseGrant.setLoanId(loanId);
		List<LoanBaseGrant> list = loanBaseGrantDao.findListByVo(loanBaseGrant);
		if(CollectionUtils.isNotEmpty(list)){
			if(list.get(list.size()-1).getGrantState().equals(grantState)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public LoanBaseGrant findLoanBaseGrantByAppNo(String appNo) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appNo",appNo);
        paramMap.put("sort","id");//id排序，取最新的一条数据
        LoanBaseGrant loanBaseGrant = null;
        List<LoanBaseGrant> loanBaseGrantList = loanBaseGrantDao.findListByMap(paramMap);
        if (CollectionUtils.isNotEmpty(loanBaseGrantList)) {
        	loanBaseGrant  = loanBaseGrantList.get(loanBaseGrantList.size()-1);
        }
        return loanBaseGrant;
	 } 
	
}
