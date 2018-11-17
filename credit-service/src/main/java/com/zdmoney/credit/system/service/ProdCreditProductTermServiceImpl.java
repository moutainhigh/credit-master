package com.zdmoney.credit.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IProdCreditProductTermDao;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.IProdCreditProductTermService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
@Service
public class ProdCreditProductTermServiceImpl implements
		IProdCreditProductTermService {
	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	@Autowired
	@Qualifier("prodCreditProductTermDaoImpl")
	IProdCreditProductTermDao prodCreditProductTermDao;
	
	@Override
	public ProdCreditProductTerm findById(Long id) {
		// TODO Auto-generated method stub
		return prodCreditProductTermDao.get(id);
	}

	@Override
	public Pager findWithPg(ProdCreditProductTerm prodCreditProductTerm) {
		// TODO Auto-generated method stub
		return prodCreditProductTermDao.findWithPg(prodCreditProductTerm);
	}

	@Override
	public void saveOrUpdate(ProdCreditProductTerm prodCreditProductTerm) {
			Long id = prodCreditProductTerm.getId();
			if (Strings.isEmpty(id)) {
				/** 新增操作 **/
				prodCreditProductTerm.setId(sequencesServiceImpl
						.getSequences(SequencesEnum.PROD_CREDIT_PRODUCT_TERM));
				prodCreditProductTermDao.insert(prodCreditProductTerm);
			} else {
				/** 修改操作 **/
				prodCreditProductTermDao.update(prodCreditProductTerm);
			}
		

	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		prodCreditProductTermDao.deleteById(id);
	}
	
	@Override
	public ProdCreditProductTerm get(Long id) {
		// TODO Auto-generated method stub
		return prodCreditProductTermDao.get(id);
	}
	
	@Override
	public ProdCreditProductTerm get(ProdCreditProductTerm prodCreditProductTerm) {
		
		return prodCreditProductTermDao.get(prodCreditProductTerm);
	}

	@Override
	public ProdCreditProductTerm findBymap(Long term, String productCd,
			String contractSource) {
		return prodCreditProductTermDao.findBymap(term,productCd,contractSource);
	}

}
