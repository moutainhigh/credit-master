package com.zdmoney.credit.loan.dao;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanPutOnRecordExportDao;
import com.zdmoney.credit.loan.domain.LoanPutOnRecordExport;
import com.zdmoney.credit.loan.vo.LoanPutOnRecordExportVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created on 2016/9/26.
 * 备案导出 数据层实现类
 */
@Repository
public class LoanPutOnRecordExportDaoImpl extends BaseDaoImpl<LoanPutOnRecordExport> implements ILoanPutOnRecordExportDao {
    @Override
    public Pager queryLoanPutOnRecordExportInfos(Map<String, Object> params) {
        Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryLoanPutOnRecordExportInfosDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryLoanPutOnRecordExportInfosCount");
        return doPager(pager, params);
    }

    @Override
    public List<LoanPutOnRecordExport> exportLoanPutOnRecordInfos(LoanPutOnRecordExportVo lporVo) {
        List<LoanPutOnRecordExport> lpors = getSqlSession().selectList(getIbatisMapperNameSpace() + ".exportLoanPutOnRecordInfos",lporVo);
        return lpors;
    }
}
