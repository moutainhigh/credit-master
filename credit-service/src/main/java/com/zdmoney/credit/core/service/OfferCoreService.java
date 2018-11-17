package com.zdmoney.credit.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.CreditVo;
import com.zdmoney.credit.core.NFCSWorkerHelper;
import com.zdmoney.credit.core.service.pub.IOfferCoreService;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;

@Service
public class OfferCoreService implements IOfferCoreService {

	@Autowired
	IComEmployeeDao comEmployeeDao;
	@Autowired
	IPersonInfoDao personInfoDao;
	
	/**
	 * 征信信息查询
	 * @param params
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> queryCredit(CreditVo params) throws Exception {
		Map<String,Object> json = null;
		
		ComEmployee employee = comEmployeeDao.findEmployeeByUserCode(params.getUserCode());
		if(employee == null){
			json = MessageUtil.returnErrorMessage("失败:雇员不存在");
            return json;
        }
		
		String jsonString = null;
		if("01".equals(params.getSearchCode())){
			PersonInfo personInfoVo = new PersonInfo();
			personInfoVo.setIdnum(params.getIdnum());
			personInfoVo.setName(params.getName());
			
			List<PersonInfo> personInfos = personInfoDao.findListByVo(personInfoVo);
			PersonInfo personInfo = null;
			if (personInfos != null && personInfos.size() > 0) {
				personInfo = personInfos.get(0);
			}
			
			if (personInfo == null) {
				json = MessageUtil.returnErrorMessage("失败:借款人不存在");
                return json;
			}
			
			jsonString = NFCSWorkerHelper.getCreditReport2(params.getIdnum(), params.getName());
		}
		
		if("02".equals(params.getSearchCode())){
            jsonString = NFCSWorkerHelper.getCreditReport(params.getIdnum(), params.getName());
        }
		
		JSONObject response = JSONObject.parseObject(jsonString);
		if (response.containsKey("success")) {
			if (response.getBoolean("success")) {
				json = MessageUtil.returnErrorMessage("未找到个人信息");
				return json;
			} else {
				json = MessageUtil.returnErrorMessage(response.containsKey("errors") ? response.getString("errors") : "未找到个人信息");
				return json;
			}
		} else {
			JSONObject resp = null;
			if (response.containsKey("result")) {
				resp = response.getJSONObject("result");
			} else {
				resp = response;
			}
			
			JSONObject jsonObject = resp.getJSONObject("网络金融版个人信用报告");
			json = MessageUtil.returnHandleSuccessMessage();
			json.put("responseJson",jsonObject != null ? jsonObject : "");
		}
		
		return json;
	}

}
