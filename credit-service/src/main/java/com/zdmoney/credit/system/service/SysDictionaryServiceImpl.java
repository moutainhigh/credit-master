package com.zdmoney.credit.system.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.SysDictionaryDao;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
@Service
public class SysDictionaryServiceImpl implements ISysDictionaryService {
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	@Autowired
	@Qualifier("sysDictionaryDaoImpl")
	SysDictionaryDao sysDictionaryDaoImpl;
	
	@Override
	public Pager findWithPg(SysDictionary sysDictionary) {
		
		return sysDictionaryDaoImpl.findWithPg(sysDictionary);
	}
	
	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		sysDictionaryDaoImpl.deleteById(id);
	}
	@Override
	public SysDictionary get(Long id) {
		// TODO Auto-generated method stub
		return sysDictionaryDaoImpl.get(id);
	}
	@Override
	public SysDictionary saveOrUpdate(SysDictionary sysDictionary) {
		if (sysDictionary == null) {
			return sysDictionary;
		}
		Long id = sysDictionary.getId();
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/
			sysDictionary.setId(sequencesServiceImpl.getSequences(SequencesEnum.SYS_DICTIONARY));
			sysDictionaryDaoImpl.insert(sysDictionary);
		} else {
			/** 修改操作 **/
			sysDictionaryDaoImpl.update(sysDictionary);
		}
		return sysDictionary;
	}
	@Override
	public List<SysDictionary> findParent() {
		
		return sysDictionaryDaoImpl.findAllList();
	}
	
	@Override
	public SysDictionary getSysDictionaryByParentId(Long id) {
		
		return sysDictionaryDaoImpl.get(id);
	}

	public List<SysDictionary> findListByVo(SysDictionary sysDictionary) {
		return sysDictionaryDaoImpl.findListByVo(sysDictionary);
	}
	@Override
	public SysDictionary get(SysDictionary sysDictionary) {
		// TODO Auto-generated method stub
		return sysDictionaryDaoImpl.get(sysDictionary);
	}
	@Override
	public SysDictionary getIsThere(SysDictionary sysDictionary) {
		// TODO Auto-generated method stub
		return sysDictionaryDaoImpl.getIsThere(sysDictionary);
	}

	public List<SysDictionary> findDictionaryListByVo(SysDictionary sysDictionary) {
		return sysDictionaryDaoImpl.findDictionaryListByVo(sysDictionary);
	}
}
