package com.zdmoney.credit.loan.service;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanPutOnRecordExportDao;
import com.zdmoney.credit.loan.domain.LoanPutOnRecordExport;
import com.zdmoney.credit.loan.service.pub.ILoanPutOnRecordExportService;
import com.zdmoney.credit.loan.vo.LoanPutOnRecordExportVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2016/9/26.
 * 备案导出
 */
@Service
public class LoanPutOnRecordExportServiceImpl implements ILoanPutOnRecordExportService {
    private Logger logger= LoggerFactory.getLogger(LoanPutOnRecordExportServiceImpl.class);
    @Autowired
    private ILoanPutOnRecordExportDao loanPutOnRecordExportDao;
    @Override
    public Pager queryLoanPutOnRecordInfosPage(Map<String, Object> params) {

        return loanPutOnRecordExportDao.queryLoanPutOnRecordExportInfos(params);
    }

    @Override
    public Workbook getSZputOnRecordWorkBook(LoanPutOnRecordExportVo lpreVo,String fileName){
        Workbook workbook = null;
            List<LoanPutOnRecordExport> lporeList = loanPutOnRecordExportDao.exportLoanPutOnRecordInfos(lpreVo);
            if (CollectionUtils.isEmpty(lporeList)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            String sheetName = "Sheet1";
        try {
            workbook = ExcelExportUtil.createExcelByVo(fileName, this.getSZlabels(), this.getSZfields(), lporeList, sheetName);
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"创建深圳备案.xls失败！");
        }
        return workbook;
    }

    @Override
    public Workbook getNotSZputOnRecordWorkBook(LoanPutOnRecordExportVo lpreVo,String fileName) {
        Workbook workbook = null;
        List<LoanPutOnRecordExport> lporeList = loanPutOnRecordExportDao.exportLoanPutOnRecordInfos(lpreVo);
        if (CollectionUtils.isEmpty(lporeList)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
        }
        String sheetName = "Sheet1";
        try {
            workbook = ExcelExportUtil.createExcelByVo(fileName, this.getNotSZlabels(), this.getNotSZfields(), lporeList, sheetName);
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,"创建异地备案.xls失败！");
        }
        return workbook;
    }

    /**
     * 深圳模板字段
     * @return
     */
    private String [] getSZlabels(){
        return  new String[]{"商户代码","费项","行别","账号","户名"};
    }
    private  String[] getSZfields(){
        return  new String[]{"enterpriseCode","expenditure","bankType","account","accountName"};
    }

    /**
     * 非深圳（异地）模板字段
     * @return
     */
    private String [] getNotSZlabels(){
        return  new String[]{"商户代码","费项","行别","行号（银行总行号）","帐号","户名","备注"};
    }
    private  String[] getNotSZfields(){
        return  new String[]{"enterpriseCode","expenditure","bankType","bankNo","account","accountName","remak"};
    }
}
