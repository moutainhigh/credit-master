package com.zdmoney.credit.system.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.system.domain.Picture;

public interface IPictureService {
	public String findPictureFileName(Picture picture) ;
	
	public List<String>  findSysName(Map<String, Object> paramMap) ;
}
