package com.zdmoney.credit.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.IProdCreditProductInfoDao;
import com.zdmoney.credit.system.dao.pub.IProdCreditProductTermDao;
import com.zdmoney.credit.system.domain.ProdCreditProductInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.IProdCreditProductInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class ProdCreditProductInfoServiceImpl implements
		IProdCreditProductInfoService {
	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	@Autowired
	@Qualifier("prodCreditProductInfoDaoImpl")
	IProdCreditProductInfoDao prodCreditProductInfoDao;
	
	@Autowired
	@Qualifier("prodCreditProductTermDaoImpl")
	IProdCreditProductTermDao prodCreditProductTermDaoImpl;

	@Override
	public ProdCreditProductInfo findById(ProdCreditProductTerm prodCreditProductTerm) {

		return prodCreditProductInfoDao.findById(prodCreditProductTerm);
	}

	@Override
	public Pager findWithPg(ProdCreditProductInfo prodCreditProductInfo) {
		// TODO Auto-generated method stub
		return prodCreditProductInfoDao.findWithPg(prodCreditProductInfo);
	}

	@Override
	public void saveOrUpdate(ProdCreditProductInfo prodCreditProductInfo) {
			Long id = prodCreditProductInfo.getId();
			if (Strings.isEmpty(id)) {
				/** 新增操作 **/
				prodCreditProductInfo.setId(sequencesServiceImpl
						.getSequences(SequencesEnum.PROD_CREDIT_PRODUCT_INFO));
				ProdCreditProductInfo p=		prodCreditProductInfoDao.insert(prodCreditProductInfo);
				ProdCreditProductTerm term=new ProdCreditProductTerm();
				term.setId(sequencesServiceImpl
						.getSequences(SequencesEnum.PROD_CREDIT_PRODUCT_TERM));
				term.setProductId(p.getId());
				term.setTerm(p.getTerm().getTerm());
				prodCreditProductTermDaoImpl.insert(term);
			} else {
				/** 修改操作 **/
				prodCreditProductInfoDao.update(prodCreditProductInfo);
				ProdCreditProductTerm term=new ProdCreditProductTerm();
				term.setId(prodCreditProductInfo.getTerm().getId());
				term.setTerm(prodCreditProductInfo.getTerm().getTerm());
				term.setProductId(prodCreditProductInfo.getId());
				prodCreditProductTermDaoImpl.update(term);
			}
	}

	@Override
	public void deleteById(ProdCreditProductInfo prodCreditProductInfo) {
		// TODO Auto-generated method stub
	//	prodCreditProductInfoDao.deleteById(prodCreditProductInfo.getId());
		
		//同时删除term表里面的数据
		prodCreditProductTermDaoImpl.deleteById(prodCreditProductInfo.getTerm().getId());
	}

	@Override
	public List<ProdCreditProductInfo> findProdCreditProductInfoList() {
		return prodCreditProductInfoDao.findAllList();
	}
	
	@Override
	public ProdCreditProductInfo get(ProdCreditProductInfo prodCreditProductInfo) {
		// TODO Auto-generated method stub
		return prodCreditProductInfoDao.get(prodCreditProductInfo);
	}
	
	@Override
	public ProdCreditProductInfo getIsThere(ProdCreditProductInfo prodCreditProductInfo) {
		// TODO Auto-generated method stub
		return prodCreditProductInfoDao.getIsThere(prodCreditProductInfo);
	}
}
