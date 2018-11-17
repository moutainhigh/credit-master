package com.zdmoney.credit.riskManage.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.riskManage.dao.pub.IPersonVisitDao;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;

@Repository
public class PersonVisitDaoImpl extends BaseDaoImpl<VPersonVisit> implements IPersonVisitDao {
    
    public Pager findVisitManageInfoWithPg(VPersonVisit personVisit) {
        Pager pager = personVisit.getPager();
        Assert.notNull(pager, "分页参数");
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findWithPGVisitManage");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".countVisitManage");
        return doPager(pager,BeanUtils.toMap(personVisit));
    }

    public List<VPersonVisit> findVisitManagesByVo(VPersonVisit personVisit) {
        return getSqlSession().selectList(getIbatisMapperNameSpace()+".findVisitManagesByVo",personVisit);
    }
}
