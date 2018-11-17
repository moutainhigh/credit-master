package com.zdmoney.credit.test.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.riskManage.dao.pub.IPersonVisitDao;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  
public class IPersonVisitDaoTest {
    @Autowired 
    private IPersonVisitDao personVisitDao;
    
    @Test
    public void testFindVisitManageInfoWithPg () {
        VPersonVisit personVisit = new VPersonVisit();
        personVisit.setName("张亮");
        Pager pager = new Pager();
        pager.setRows(10);
        pager.setPage(1);
        personVisit.setPager(pager);
        pager = personVisitDao.findVisitManageInfoWithPg(personVisit);
        Assert.assertNotNull(pager.getResultList());
    }
    
    @Test
    public void testFindVisitManagesByVo () {
        VPersonVisit personVisit = new VPersonVisit();
        personVisit.setName("张亮");
        List<VPersonVisit> personVisitList = personVisitDao.findVisitManagesByVo(personVisit);
        Assert.assertNotNull(personVisitList);
    }
}
