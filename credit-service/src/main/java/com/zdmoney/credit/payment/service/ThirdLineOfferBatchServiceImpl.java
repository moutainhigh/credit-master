package com.zdmoney.credit.payment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferBatchDao;
import com.zdmoney.credit.payment.domain.ThirdLineOfferBatch;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferBatchService;

@Service
public class ThirdLineOfferBatchServiceImpl implements IThirdLineOfferBatchService {

    @Autowired
    private IThirdLineOfferBatchDao haTwoOfferBatchDao;

    public void insert(ThirdLineOfferBatch haTwoOfferBatch) {
        haTwoOfferBatchDao.insert(haTwoOfferBatch);
    }
    
    public void update(ThirdLineOfferBatch haTwoOfferBatch) {
        haTwoOfferBatchDao.update(haTwoOfferBatch);
    }

    public List<ThirdLineOfferBatch> findHaTwoOfferBatch() {
        return haTwoOfferBatchDao.findHaTwoOfferBatchNotExport();
    }

    public List<ThirdLineOfferBatch> findThirdLineOfferBatch() {
        return haTwoOfferBatchDao.findThirdLineOfferBatch();
    }

    public ThirdLineOfferBatch findOfferBatchId() {
        return haTwoOfferBatchDao.findOfferBatchId();
    }

    public ThirdLineOfferBatch getThirdLineOfferBatch(Long id) {
        return haTwoOfferBatchDao.get(id);
    }

    public ThirdLineOfferBatch findLatelyOfferBatchInfo() {
        return haTwoOfferBatchDao.findLatelyOfferBatchInfo();
    }
    
    public List<ThirdLineOfferBatch> findListByVo(ThirdLineOfferBatch offerBatch) {
        return haTwoOfferBatchDao.findListByVo(offerBatch);
    }
}
