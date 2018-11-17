package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.vo.LoanPutOnRecordExportVo;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Map;

/**
 * 备案导出 service
 * Created  on 2016/9/26.
 */
public interface ILoanPutOnRecordExportService {

    public Pager queryLoanPutOnRecordInfosPage(Map<String,Object> params);

    /**
     * 获取深圳地区的 workbook对象
     * @return
     */
    public Workbook getSZputOnRecordWorkBook(LoanPutOnRecordExportVo lpreVo,String fileName);

    /**
     * 获取非深圳地区（异地）workbook 对象
     * @return
     */
    public Workbook getNotSZputOnRecordWorkBook(LoanPutOnRecordExportVo lpreVo,String fileName);
}
