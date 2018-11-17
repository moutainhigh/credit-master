package com.zdmoney.credit.loan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;

@Repository
public class LoanInitialInfoDaoImpl extends BaseDaoImpl<LoanInitialInfo> implements ILoanInitialInfoDao{

	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYAPPNO = ".findByAppNo";
	
	
	@Override
	public LoanInitialInfo findByLoanId(Long loanId) {
		LoanInitialInfo LoanInitialInfo = new LoanInitialInfo();
		LoanInitialInfo.setLoanId(loanId);
		List<LoanInitialInfo> list = findListByVo(LoanInitialInfo);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * 根据appNo查找
	 * @param appNo
	 * @return
	 */
	@Override
	public LoanInitialInfo findByAppNo(String appNo) {
		LoanInitialInfo LoanInitialInfo = getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYAPPNO, appNo);
		return LoanInitialInfo;
	}
}
