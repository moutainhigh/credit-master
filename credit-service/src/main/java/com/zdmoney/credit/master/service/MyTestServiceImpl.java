package com.zdmoney.credit.master.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.master.dao.pub.IMyTestDao;
import com.zdmoney.credit.master.domain.MyTest;
import com.zdmoney.credit.master.service.pub.IMyTestService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class MyTestServiceImpl implements IMyTestService {
	
	@Autowired @Qualifier("myTestDaoImpl")
	IMyTestDao myTestDaoImpl;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	/**
	 * 获取实体
	 * @param id PK值
	 * @return
	 */
	@Override
	public MyTest get(Long id) {
		return myTestDaoImpl.get(id);
	}
	
	/**
	 * 根据id删除数据
	 * @author Ivan
	 * @param id
	 */
	@Override
	public void deleteById(Long id) {
		myTestDaoImpl.deleteById(id);
	}
	
	/**
	 * 带分页查询
	 * @param myTest 条件实体对象
	 * @return
	 */
	@Override
	public Pager findWithPg(MyTest myTest) {
		return myTestDaoImpl.findWithPg(myTest);
	}
	
	/**
	 * 跟据实体信息查询实体集合（通常是组合条件查询）
	 * @param myTest 实体对象
	 * @author Ivan
	 */
	@Override
	public List<MyTest> findListByVo(MyTest myTest) {
		return myTestDaoImpl.findListByVo(myTest);
	}

	/**
	 * 新增、修改实体数据
	 * @param myTest
	 * @return
	 */
	@Override
	public MyTest saveOrUpdate(MyTest myTest) {
		if (myTest == null) {
			return myTest;
		}
		Long id = myTest.getId();
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/
			myTest.setId(sequencesServiceImpl.getSequences(SequencesEnum.MY_TEST));
			myTestDaoImpl.insert(myTest);
		} else {
			/** 修改操作 **/
			myTestDaoImpl.update(myTest);
		}
		return myTest;
	}
	
}
























