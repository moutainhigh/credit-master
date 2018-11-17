package com.zdmoney.credit.framework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.framework.dao.pub.ISystemDao;
import com.zdmoney.credit.framework.service.pub.IBaseService;

/**
 * service基类、定义一些通用方法
 * @author 00236640
 * @version $Id: BaseServiceImpl.java, v 0.1 2015年11月13日 下午2:41:27 00236640 Exp $
 */
@Service
public class BaseServiceImpl implements IBaseService {
    
    @Autowired
    private ISystemDao systemDao;

    public void systemCheck() {
        systemDao.selectOne("1");
    }
}
