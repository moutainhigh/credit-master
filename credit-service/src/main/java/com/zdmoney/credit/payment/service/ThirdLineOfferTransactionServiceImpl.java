package com.zdmoney.credit.payment.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.payment.dao.pub.IOfferExportBatchDao;
import com.zdmoney.credit.payment.dao.pub.IOfferExportInfoDao;
import com.zdmoney.credit.payment.domain.OfferExportBatch;
import com.zdmoney.credit.payment.domain.OfferExportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferTransactionDao;
import com.zdmoney.credit.payment.domain.ThirdLineOfferTransaction;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferTransactionService;

@Service
public class ThirdLineOfferTransactionServiceImpl implements IThirdLineOfferTransactionService {
    
    @Autowired
    private IThirdLineOfferTransactionDao haTwoOfferTransactionDao ;
    @Autowired
    private IOfferExportBatchDao offerExportBatchDao;
    @Autowired
    private IOfferExportInfoDao offerExportInfoDao;

    public List<ThirdLineOfferTransaction> findListByMap(Map<String,Object> map) {
        return haTwoOfferTransactionDao.findListByMap(map);
    }

    public void updateState(Long id) {
        ThirdLineOfferTransaction thirdLineOfferTransaction = new ThirdLineOfferTransaction();
        thirdLineOfferTransaction.setId(id);
        thirdLineOfferTransaction.setSendTime(new Date());
        thirdLineOfferTransaction.setState("已报盘");
        haTwoOfferTransactionDao.update(thirdLineOfferTransaction);
    }

    @Override
    public OfferExportBatch findOfferExportBatch(OfferExportBatch offerExportBatch) {
        OfferExportBatch exportBatch= offerExportBatchDao.get(offerExportBatch);
        return exportBatch;
    }

    @Override
    public int insertOfferExportBatch(OfferExportBatch offerExportBatch) {
        offerExportBatchDao.insert(offerExportBatch);
        return 0;
    }

    @Override
    public int updateOfferExportBatch(OfferExportBatch offerExportBatch) {
        int falg=offerExportBatchDao.updateByPrimaryKeySelective(offerExportBatch);
        return falg;
    }

    @Override
    public List<OfferExportInfo> getGuoMinXinTuoThirdLine() {
        return offerExportInfoDao.getGuoMinXinTuoThirdLine();
    }

    @Override
    public BigDecimal getGuoMinXinTuoThirdLineTotalAmount() {
        BigDecimal totalAmount=offerExportInfoDao.getGuoMinXinTuoThirdLineTotalAmount();
        if(totalAmount==null){
            totalAmount=new BigDecimal(0);
        }
        return totalAmount;
    }
    
    public List<ThirdLineOfferTransaction> findOfferTransactionListByMap(Map<String, Object> params) {
        return haTwoOfferTransactionDao.findOfferTransactionListByMap(params);
    }
}
