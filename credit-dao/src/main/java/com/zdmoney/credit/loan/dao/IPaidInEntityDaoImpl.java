package com.zdmoney.credit.loan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.framework.vo.abs.entity.PaidInEntity;
import com.zdmoney.credit.loan.dao.pub.IPaidInEntityDao;
import com.zdmoney.credit.loan.domain.PaidInEntityMemo;
@Repository
public class IPaidInEntityDaoImpl extends BaseDaoImpl<PaidInEntityMemo> implements IPaidInEntityDao {

    @Override
    public List<PaidInEntity> queryPaidEntityList() {
        List<PaidInEntity> paidInEntityMemos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryPaidEntityList");
        if (paidInEntityMemos != null && paidInEntityMemos.size() > 0) {
            return paidInEntityMemos;
        }
        return null;
    }

    @Override
    public String selectLoanId(String pactNo) {
        return super.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".selectLoanId" , pactNo);
    }
    
    @Override
    public void insertRequestParam(PaidInEntityMemo pe) {
        super.getSqlSession().insert(getIbatisMapperNameSpace() + ".insert", pe);
        
    }
 
}
