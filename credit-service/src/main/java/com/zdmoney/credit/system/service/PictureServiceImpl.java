package com.zdmoney.credit.system.service;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.system.dao.pub.IPictureDao;
import com.zdmoney.credit.system.domain.Picture;
import com.zdmoney.credit.system.service.pub.IPictureService;
@Service
public class PictureServiceImpl implements IPictureService {
	private static final Logger logger = Logger.getLogger(PictureServiceImpl.class);
	@Autowired
	@Qualifier("pictureDaoImpl")
	IPictureDao pictureDaoImpl;
	public String findPictureFileName(Picture picture) {
		return pictureDaoImpl.findPictureFileName(picture);
	}
	@Override
	public List<String> findSysName(Map<String, Object> paramMap) {
		return pictureDaoImpl.findSysName(paramMap);
	}

}
