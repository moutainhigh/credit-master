package com.zdmoney.credit.debit.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.debit.dao.pub.IDebitTransactionDao;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class DebitTransactionDaoImpl extends BaseDaoImpl<DebitTransaction> implements IDebitTransactionDao {
    
    @Override
    public DebitTransaction findBySerialNo(String serialNo) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findBySerialNo", serialNo);
    }
    
    @Override
    public List<DebitTransaction> findByStateAndLoan(OfferTransactionStateEnum state, Long loanId) {
        DebitTransaction debitTransaction = new DebitTransaction();
        debitTransaction.setState(state.getValue());
        debitTransaction.setLoanId(loanId);
        List<DebitTransaction> list = findListByVo(debitTransaction);
        return list;
    }

    @Override
    public DebitTransaction queryDebitTransactionByConditions(Map<String, Object> params) {
        return  getSqlSession().selectOne(getIbatisMapperNameSpace()+".queryDebitTransactionByConditions", params);
    }

    public List<DebitTransaction> queryDebitBatchNos(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace()+".queryDebitBatchNos", params);
    }
}
