package com.zdmoney.credit.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.person.dao.pub.IPersonHouseInfoDao;
import com.zdmoney.credit.person.domain.PersonHouseInfo;
import com.zdmoney.credit.person.service.pub.IPersonHouseInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款人房产信息Service业务封装层
 * @author Ivan
 *
 */
@Service
public class PersonHouseInfoServiceImpl implements IPersonHouseInfoService {
	@Autowired @Qualifier("personHouseInfoDaoImpl")
	IPersonHouseInfoDao personHouseInfoDaoImpl;
	@Autowired
	ISequencesService sequencesService;
	
	/**
	 * 通过实体信息查询
	 * @param personHouseInfo
	 * @return
	 */
	@Override
	public List<PersonHouseInfo> findListByVo(PersonHouseInfo personHouseInfo) {
		return personHouseInfoDaoImpl.findListByVo(personHouseInfo);
	}

	/**
	 * 根据personId查找
	 * @param personId
	 * @return
	 */
	@Override
	public PersonHouseInfo findByPersonId(Long personId) {
		return personHouseInfoDaoImpl.findByPersonId(personId);
	}

	/**
	 * 保存或者更新
	 * @param personHouseInfo
	 */
	@Override
	public void saveOrUpdate(PersonHouseInfo personHouseInfo) {
		Long id = personHouseInfo.getId();
		if (Strings.isEmpty(id)) {
			personHouseInfo.setId(sequencesService.getSequences(SequencesEnum.PERSON_HOUSE_INFO));
			personHouseInfoDaoImpl.insert(personHouseInfo);
		} else {
			personHouseInfoDaoImpl.update(personHouseInfo);
		}
	}
}
