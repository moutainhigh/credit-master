package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ComRole;
import com.zdmoney.credit.system.domain.SysDictionary;

/**
 * 数据字典
 * @author Administrator
 *
 */
public interface ISysDictionaryService {

	/**
	 * page
	 */
	/**
	 * 带分页查询
	 * @param ComEmployee 条件实体对象
	 * @return
	 */
	public Pager findWithPg(SysDictionary sysDictionary) ;
	

	/**
	 * 通过PK获取实体信息
	 * @param id
	 * @return
	 */
	public SysDictionary get(Long id) ;
	public SysDictionary get(SysDictionary sysDictionary) ;
	
	/**
	 * 新增、修改实体数据
	 * @param ComRole
	 * @return
	 */
	public SysDictionary saveOrUpdate(SysDictionary sysDictionary);
	
	
	/**
	 * 根据id删除数据
	 * @author Ivan
	 * @param id
	 */
	public void deleteById(Long id);
	
	/**
	 * 查询所有父级
	 */
	
	public List<SysDictionary> findParent();
	
	public SysDictionary getSysDictionaryByParentId(Long id);
	
	/**
	 * 查询满足条件的字典数据信息
	 * @param sysDictionary
	 * @return
	 */
	public List<SysDictionary> findListByVo(SysDictionary sysDictionary);


	public SysDictionary getIsThere(SysDictionary sysDictionary);
	
	/**
	 * 根据指定条件查询字典数据
	 * @param sysDictionary
	 * @return
	 */
	public List<SysDictionary> findDictionaryListByVo(SysDictionary sysDictionary);
}
