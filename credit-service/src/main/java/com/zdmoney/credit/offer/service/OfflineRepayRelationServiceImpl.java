package com.zdmoney.credit.offer.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.offer.dao.pub.IOfflineRepayRelationDao;
import com.zdmoney.credit.offer.service.pub.IOfflineRepayRelationService;

/**
 * @author 10098  2017年4月12日 上午10:10:54
 */
@Service
public class OfflineRepayRelationServiceImpl implements IOfflineRepayRelationService {

    private static Logger logger = Logger.getLogger(OfflineRepayRelationServiceImpl.class);
    
    @Autowired
    private IOfflineRepayRelationDao offlineRepayRelationDao;
    
}
