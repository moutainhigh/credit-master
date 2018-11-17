package com.zdmoney.credit.system.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IPictureDao;
import com.zdmoney.credit.system.domain.Picture;
@Repository
public class PictureDaoImpl extends BaseDaoImpl<Picture> implements
		IPictureDao {

	@Override
	public String findPictureFileName(Picture picture) {

		return getSqlSession().selectOne(getIbatisMapperNameSpace() +".findPictureFileName", picture);
	}

	@Override
	public List<String> findSysName(Map<String, Object> paramMap) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() +".findSysName", paramMap);
	}

}
