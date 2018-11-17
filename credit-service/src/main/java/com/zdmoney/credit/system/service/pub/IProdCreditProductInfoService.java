package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.ProdCreditProductInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

public interface IProdCreditProductInfoService {

	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public ProdCreditProductInfo findById(ProdCreditProductTerm prodCreditProductTerm);
	
	/**
	 * 带分页查询
	 * @param ProdCreditProductInfo 条件实体对象
	 * @return
	 */
	public Pager findWithPg(ProdCreditProductInfo prodCreditProductInfo);
	
	/**
	 *
	 * @param personInfo 
	 * @return
	 */
	public void saveOrUpdate(ProdCreditProductInfo prodCreditProductInfo);
	
	/**
	 * 根据id删除数据
	 * @author Ivan
	 * @param id
	 */
	public void deleteById( ProdCreditProductInfo prodCreditProductInfo);
	
	
	public List<ProdCreditProductInfo> findProdCreditProductInfoList();

	public ProdCreditProductInfo get(ProdCreditProductInfo prodCreditProductInfo);

	public ProdCreditProductInfo getIsThere(ProdCreditProductInfo prodCreditProductInfo);
}
