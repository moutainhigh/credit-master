package com.zdmoney.credit.system.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.domain.Picture;

public interface IPictureDao extends IBaseDao<Picture>{
	  /**
     * 根据身份证号查找
     * 
     * @param idnum
     * @return
     */
	public String findPictureFileName(Picture picture);
	
	
	public List<String>  findSysName(Map<String, Object> paramMap);
}
