package com.zdmoney.credit.riskManage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.riskManage.dao.pub.IPersonVisitDao;
import com.zdmoney.credit.riskManage.service.pub.IPersonVisitService;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class PersonVisitServiceImpl implements IPersonVisitService {

    @Autowired
    private IPersonVisitDao personVisitDao;
    
    @Autowired
    private ISequencesService sequencesService;
    
    public Pager findWithPg(VPersonVisit personVisit) {
        return personVisitDao.findWithPg(personVisit);
    }

    public Pager findVisitManageInfoWithPg(VPersonVisit personVisit) {
        return personVisitDao.findVisitManageInfoWithPg(personVisit);
    }

    public List<VPersonVisit> findListByVO(VPersonVisit personVisit) {
        return personVisitDao.findListByVo(personVisit);
    }

    public List<VPersonVisit> findVisitManagesByVo(VPersonVisit personVisit) {
        return personVisitDao.findVisitManagesByVo(personVisit);
    }

    public void saveCustomerVisitInfo(VPersonVisit personVisit) {
        Long id = sequencesService.getSequences(SequencesEnum.PERSON_VISIT);
        personVisit.setId(id);
        personVisitDao.insert(personVisit);
    }
}