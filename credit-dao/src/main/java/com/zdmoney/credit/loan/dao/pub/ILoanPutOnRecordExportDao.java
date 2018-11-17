package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanPutOnRecordExport;
import com.zdmoney.credit.loan.vo.LoanPutOnRecordExportVo;

import java.util.List;
import java.util.Map;

/**
 * Created by  on 2016/9/26.
 * 备案导出 数据查询
 */
public interface ILoanPutOnRecordExportDao extends IBaseDao<LoanPutOnRecordExport> {
    public Pager queryLoanPutOnRecordExportInfos(Map<String,Object> params);

    public List<LoanPutOnRecordExport> exportLoanPutOnRecordInfos(LoanPutOnRecordExportVo lporVo);
}
