package com.zdmoney.credit.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.person.dao.pub.IPersonEntrepreneurInfoDao;
import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;
import com.zdmoney.credit.person.service.pub.IPersonEntrepreneurInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款人企业经营信息Service业务封装层
 * @author Ivan
 *
 */
@Service
public class PersonEntrepreneurInfoServiceImpl implements IPersonEntrepreneurInfoService{
	@Autowired @Qualifier("personEntrepreneurInfoDaoImpl")
	IPersonEntrepreneurInfoDao personEntrepreneurInfoDaoImpl;
	@Autowired
	ISequencesService sequencesService;
	
	/**
	 * 通过实体信息查询
	 * @param personEntrepreneurInfo
	 * @return
	 */
	@Override
	public List<PersonEntrepreneurInfo> findListByVo(PersonEntrepreneurInfo personEntrepreneurInfo) {
		return personEntrepreneurInfoDaoImpl.findListByVo(personEntrepreneurInfo);
	}

	@Override
	public PersonEntrepreneurInfo findByPersonId(Long personId) {
		return personEntrepreneurInfoDaoImpl.findByPersonId(personId);
	}

	/**
	 * 保存或更新
	 * @param personEntrepreneurInfo
	 */
	@Override
	public void saveOrUpdate(PersonEntrepreneurInfo personEntrepreneurInfo) {
		Long id = personEntrepreneurInfo.getId();
		if (Strings.isEmpty(id)) {
			personEntrepreneurInfo.setId(sequencesService.getSequences(SequencesEnum.PERSON_ENTREPRENEUR_INFO));
			personEntrepreneurInfoDaoImpl.insert(personEntrepreneurInfo);
		} else {
			personEntrepreneurInfoDaoImpl.update(personEntrepreneurInfo);
		}
		
	}
	
}
