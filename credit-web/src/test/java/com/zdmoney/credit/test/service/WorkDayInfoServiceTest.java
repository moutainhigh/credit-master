package com.zdmoney.credit.test.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.Dates;

@RunWith(SpringJUnit4ClassRunner.class)
//使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
//加载配置文件
//@Transactional
public class WorkDayInfoServiceTest {
    
    @Autowired
    private IWorkDayInfoService workDayInfoService;
    
    @Test
    public void testGetWorkDaysInRegion(){
        Date beginDate = Dates.parse("2017-06-01", "yyyy-MM-dd");
        Date endDate = new Date();
        int overdueDays = workDayInfoService.getWorkDaysInRegion(beginDate, endDate);
        System.out.println(overdueDays);
    }
}
