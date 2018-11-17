package com.zdmoney.credit.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.person.dao.pub.IPersonCarInfoDao;
import com.zdmoney.credit.person.domain.PersonCarInfo;
import com.zdmoney.credit.person.service.pub.IPersonCarInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款人车辆信息Service业务封装层
 * @author Ivan
 *
 */
@Service
public class PersonCarInfoServiceImpl implements IPersonCarInfoService{
	@Autowired @Qualifier("personCarInfoDaoImpl")
	IPersonCarInfoDao personCarInfoDaoImpl;
	@Autowired
	ISequencesService sequencesService;
	
	/**
	 * 通过实体信息查询
	 * @param personCarInfo
	 * @return
	 */
	@Override
	public List<PersonCarInfo> findListByVo(PersonCarInfo personCarInfo) {
		return personCarInfoDaoImpl.findListByVo(personCarInfo);
	}

	/**
	 * 通过personId查找
	 * @param personId
	 * @return
	 */
	@Override
	public PersonCarInfo findByPersonId(Long personId) {
		return personCarInfoDaoImpl.findByPersonId(personId);
	}

	/**
	 * 保存或者更新
	 * @param personCarInfo
	 */
	@Override
	public void saveOrUpdate(PersonCarInfo personCarInfo) {
		Long id = personCarInfo.getId();
		if (Strings.isEmpty(id)) {
			personCarInfo.setId(sequencesService.getSequences(SequencesEnum.PERSON_CAR_INFO));
			personCarInfoDaoImpl.insert(personCarInfo);
		} else {
			personCarInfoDaoImpl.update(personCarInfo);
		}
	}
	
}
