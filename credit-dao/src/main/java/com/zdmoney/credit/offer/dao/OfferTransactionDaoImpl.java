package com.zdmoney.credit.offer.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferTransactionDao;
import com.zdmoney.credit.offer.domain.OfferTransaction;
/**
 * 
 * @author 00232949
 *
 */
@Repository
public class OfferTransactionDaoImpl extends BaseDaoImpl<OfferTransaction> implements IOfferTransactionDao{

	@Override
	public List<OfferTransaction> findByStateAndLoan(OfferTransactionStateEnum state,
			Long loanId) {
		
		OfferTransaction offerTransaction = new OfferTransaction();
		offerTransaction.setTrxState(state.getValue());
		offerTransaction.setLoanId(loanId);
		List<OfferTransaction> list = findListByVo(offerTransaction);
		
		return list;
	}

	@Override
	public OfferTransaction findByTrxSerialNo(String orderNo) {
		OfferTransaction offerTransaction = new OfferTransaction();
		offerTransaction.setTrxSerialNo(orderNo);
		List<OfferTransaction> list = findListByVo(offerTransaction);
		
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		
		return null;
	}
	
	@Override
	public Pager searchOfferTransactionInfoWithPgByMap(Map<String, Object> params){
		Pager pager = (Pager)params.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".searchOfferTransactionInfoWithPg");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".searchOfferTransactionInfoWithPgCount");
		return doPager(pager,params);
	}

	public OfferTransaction queryOffLineOfferTransaction(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".queryOffLineOfferTransaction", params);
	}

	@Override
	public List<OfferTransaction> findOfferTranscationByBankAcct(Map<String, Object> params){
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findOfferTranscationByBankAcct", params);
	}
}
