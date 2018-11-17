package com.zdmoney.credit.master.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.master.domain.MyTest;

public interface IMyTestService {
	/**
	 * 获取实体
	 * @param id PK值
	 * @return
	 */
	public MyTest get(Long id);
	
	/**
	 * 根据id删除数据
	 * @author Ivan
	 * @param id
	 */
	public void deleteById(Long id);
	
	/**
	 * 带分页查询
	 * @param myTest 条件实体对象
	 * @return
	 */
	public Pager findWithPg(MyTest myTest);
	
	/**
	 * 跟据实体信息查询实体集合（通常是组合条件查询）
	 * @param myTest 实体对象
	 * @author Ivan
	 */
	public List<MyTest> findListByVo(MyTest myTest);
	
	/**
	 * 新增、修改实体数据
	 * @param myTest
	 * @return
	 */
	public MyTest saveOrUpdate(MyTest myTest);
	
}






















