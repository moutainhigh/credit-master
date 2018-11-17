/**
 * Copyright (c) 2017, lings@yuminsoft.com All Rights Reserved. <br/>
 * Date:2017年6月2日下午5:17:12
 *
 */

package com.zdmoney.credit.operation.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.operation.dao.pub.ILoanContractCloseBusinessInfoDao;
import com.zdmoney.credit.operation.domain.LoanContractCloseBusinessInfo;
import com.zdmoney.credit.operation.service.pub.ILoanCsCloseBusinessInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zendaimoney.thirdpp.common.dao.BaseDao;

/**
 * ClassName:LoanCsCloseBusinessInfoServiceImpl <br/>
 * Date: 2017年6月2日 下午5:17:12 <br/>
 * 
 * @author lings@yuminsoft.com
 */
@Service
public class LoanCsCloseBusinessInfoServiceImpl implements
		ILoanCsCloseBusinessInfoService {
	
	@Autowired
	ILoanContractCloseBusinessInfoDao foOrgTypeDao;
	
	@Autowired
	private ILoanBaseDao loanBaseDao;
	
	@Autowired
	ISequencesService sequencesService;
	
	// 查询关门营业部列表
	@Override
	public List<Map<String, Object>> searchShutShop() {
		List<Map<String,Object>> list = foOrgTypeDao.selectShutShop();
		return list;
	}
	
	// 查询已关门营业部列表
	@Override
	public List<Map<String, Object>> searchShutedShop() {
		List<Map<String,Object>> list = foOrgTypeDao.selectShutedShop();
		return list;
	}

	@Override
	public Map<String, Object> editCloseDepartment(Map<String, Object> map) {
		String allOrgType = String.valueOf(map.get("allOrgType")).replace(
				"&quot;", "\"");
		int closeNum = 0;
		int deleteNum = 0;
		List<Map<String, String>> json = (List<Map<String, String>>) JSONArray
				.parse(allOrgType);
		for (int i = 0; i < json.size(); i++) {
			Long employeeId = Long.valueOf(json.get(i).get("Id"));
			// 当前选中的状态
			Long editType = Long.valueOf(json.get(i).get("editType"));
			LoanContractCloseBusinessInfo foOrgType = foOrgTypeDao.selectIsFoOrgType(employeeId);
			// 1是关闭的店铺并且数据库里没有这个部门的数据就新增,如果已经存在数据就直接修改
			if (editType == 1
					&& ((null == foOrgType) || (new Long(3L).equals(foOrgType
							.getEditType())))
					|| (new Long(2L).equals(foOrgType.getEditType()))) {
				LoanContractCloseBusinessInfo orgType = new LoanContractCloseBusinessInfo();
				// 如果不存在就获得部门id赋值给orgType对象的orgId
				if (foOrgType == null) {
					orgType.setOrgId(employeeId);
				} else {
					// 存在就直接给id赋值进行的是修改操作
					orgType.setId(foOrgType.getId());
				}
				orgType.setEditType(1L);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//下一天生效
				orgType.setActivityTime(addingDay(1));
				this.saveOrUpdateFoOrgType(orgType);
				closeNum++;
			} else if (editType == 3 && null != foOrgType) {
				LoanContractCloseBusinessInfo orgType = new LoanContractCloseBusinessInfo();
				orgType.setId(foOrgType.getId());
				orgType.setEditType(3L);
				orgType.setActivityTime(addingDay(1));
				this.saveOrUpdateFoOrgType(orgType);
				deleteNum++;
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultState", "新增关闭店铺" + closeNum + "个," + "取消关闭店铺"
				+ deleteNum + "个");
		return resultMap;
	}
	
	public LoanContractCloseBusinessInfo saveOrUpdateFoOrgType(LoanContractCloseBusinessInfo foOrgType){
		Long id = foOrgType.getId();
		if(id == null){
			foOrgType.setId(sequencesService.getSequences(SequencesEnum.COM_ORG_TYPE));
			foOrgTypeDao.insert(foOrgType);
		}else{
			foOrgTypeDao.update(foOrgType);
		}
		return foOrgType;
	}
	
	//往后推算日子
	public static Date addingDay(int num){
		//生效时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(date); 
		calendar.add(calendar.DATE,num);
		date=calendar.getTime();
		return date;
	}

	@Override
	public int flushShutedShop() {
		foOrgTypeDao.flushShutedShop();
		return 0;
	}

}
