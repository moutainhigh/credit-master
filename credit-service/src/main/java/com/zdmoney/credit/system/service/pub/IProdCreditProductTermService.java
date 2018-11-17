package com.zdmoney.credit.system.service.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

public interface IProdCreditProductTermService {

	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public ProdCreditProductTerm findById(Long id);
	
	/**
	 * 带分页查询
	 * @param prodCreditProductTerm 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ProdCreditProductTerm prodCreditProductTerm);
	
	/**
	 *
	 * @param personInfo 
	 * @return
	 */
	public void saveOrUpdate(ProdCreditProductTerm prodCreditProductTerm);
	
	/**
	 * 根据id删除数据
	 * @author Ivan
	 * @param id
	 */
	public void deleteById(Long id);
	
	
	public ProdCreditProductTerm get(Long id);
	public ProdCreditProductTerm get(ProdCreditProductTerm prodCreditProductTerm);
	
	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public ProdCreditProductTerm findBymap(Long term,String productCd,String contractSource);
}
