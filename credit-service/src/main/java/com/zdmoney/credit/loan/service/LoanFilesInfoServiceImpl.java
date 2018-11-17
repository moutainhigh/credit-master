package com.zdmoney.credit.loan.service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.MapUtil;
import com.zdmoney.credit.loan.dao.pub.ILoanFilesInfoDao;
import com.zdmoney.credit.loan.service.pub.ILoanFilesInfoService;
import com.zdmoney.credit.loan.vo.VLoanCollectionInfoList;
import com.zdmoney.credit.loan.vo.VLoanFilesInfo;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoBase;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoList;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoUpdate;
import com.zdmoney.credit.system.dao.pub.ISequencesDao;


@Service("loanFilesInfoService")
public class LoanFilesInfoServiceImpl implements ILoanFilesInfoService{
	
	@Autowired 
	@Qualifier("loanFilesInfoDao")
	private ILoanFilesInfoDao loanFilesInfoDao;
	
	@Autowired
	private ISequencesDao sequencesDao;

	/**
	 * 进入查询界面处理
	 * @author 00236633
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> listPage() {
		Map<String,Object> result = new HashMap<String,Object>();
		
		result.put("success", true);
		result.put("message", "查询成功");
		
		return result;
	}

	/**
	 * 查询客户档案信息列表
	 * @author 00236633
	 * @param vLoanFilesInfoList
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> list(VLoanFilesInfoList vLoanFilesInfoList) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		MapUtil.vObjectConvertToMap(vLoanFilesInfoList, params, false);
		
		User user = UserContext.getUser();
		params.put("orgCode", user.getOrgCode());
		
		int pageSize = vLoanFilesInfoList.getRows();
		int page = vLoanFilesInfoList.getPage() ;
		int startRow = (page-1)*pageSize+1;
		int endRow = page*pageSize;
		params.put("startRow", startRow);
		params.put("endRow", endRow);
		
		List<Map<String,Object>> loanFilesInfoList = loanFilesInfoDao.findLoanFilesInfoListByMap(params);
		int loanFilesInfoCount = loanFilesInfoDao.findLoanFilesInfoCountByMap(params);
		
		StringBuilder sb = new StringBuilder();
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		
		for(Map<String,Object> map: loanFilesInfoList){
			if(map.get("borrowIdNum")!=null){
				String borrowIdNum =map.get("borrowIdNum")+"";
				if(borrowIdNum.length()>4){
					map.put("borrowIdNum", "**"+borrowIdNum.substring(borrowIdNum.length()-4));					
				}
			}
			
			if(map.get("money")!=null){
				try {
					double money = Double.parseDouble(map.get("money")+"");
					map.put("money", decimalFormat.format(money));
				} catch (NumberFormatException e) {
				}
			}
			
			try {
				Long salesmanId = Long.parseLong(map.get("salesmanId")+"");
				sb.append(salesmanId+",");
			} catch (NumberFormatException e) {
			}
			
			try {
				Long crmId = Long.parseLong(map.get("crmId")+"");
				sb.append(crmId+",");
			} catch (NumberFormatException e) {
			}
		}
		
		Map<String,String> employeeMap = new HashMap<String, String>();
		if(sb.length()>0){
			params = new HashMap<String,Object>();
			params.put("ids", sb.substring(0, sb.length()-1));
			List<Map<String,Object>> employeeList = loanFilesInfoDao.findEmployeeListByIds(params);
			for(Map<String,Object> map:employeeList){
				employeeMap.put(map.get("id")+"", map.get("name")+"");
			}
		}
		
		for(Map<String,Object> map: loanFilesInfoList){
			String salesmanName = employeeMap.get(map.get("salesmanId")+"");
			if(salesmanName==null){
				salesmanName="";
			}
			
			String crmName= employeeMap.get(map.get("crmId")+"");
			if(crmName==null){
				crmName="";
			}
			
			map.put("salesmanName", salesmanName);
			map.put("crmName", crmName);
		}
		
		result.put("loanFilesInfoList", loanFilesInfoList);
		result.put("loanFilesInfoCount", loanFilesInfoCount);
		result.put("success", true);
		result.put("message", "查询成功");
		
		return result;
	}
	
	/**
	 * 进入添加界面处理
	 * @author 00236633
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> addPage(VLoanFilesInfoBase vLoanFilesInfoBase){
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		MapUtil.vObjectConvertToMap(vLoanFilesInfoBase, params, false);
		
		List<Map<String,Object>> LoanBaseInfoList = loanFilesInfoDao.findLoanBaseInfoById(params);
		
		if(LoanBaseInfoList.size()>0){
			result.put("loanBaseInfo", LoanBaseInfoList.get(0));
			result.put("success", true);
			result.put("message", "查询成功");
		}else{
			result.put("success", false);
			result.put("message", "借款信息不存在");
		}

		return result;
	}

	/**
	 * 添加客户档案
	 * @author 00236633
	 * @param vLoanFilesInfo
	 * @param user
	 * @return
	 */
	@Transactional
	@Override
	public Map<String, Object> add(VLoanFilesInfo vLoanFilesInfo){
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("loanId",vLoanFilesInfo.getLoanId());
		
		List<Map<String,Object>> borrowDocNumList = loanFilesInfoDao.findBorrowDocNumByLoanId(params);
		if(borrowDocNumList.size()!=0){
			StringBuilder borrowerDocFlowNum = new StringBuilder();
			
			
			borrowerDocFlowNum
			.append("C-DS-")
			.append(borrowDocNumList.get(0).get("departmentNum"))
			.append("-")
			.append(borrowDocNumList.get(0).get("loanTypeNum"))
			.append("-")
			.append(borrowDocNumList.get(0).get("borrowerDocFlowNum"));
			vLoanFilesInfo.setBorrowerDocFlowNum(borrowerDocFlowNum.toString());
			
			Object seqValue = sequencesDao.getSeq(SequencesEnum.LOAN_FILES_INFO.getSequencesName());
			vLoanFilesInfo.setId(Long.parseLong(seqValue+""));
			vLoanFilesInfo.setVersion(0l);
			
			User user = UserContext.getUser();
			vLoanFilesInfo.setOperatorId(user.getId());
		
			params.clear();
			vLoanFilesInfo.setMark("v2");//版本号
			MapUtil.vObjectConvertToMap(vLoanFilesInfo, params, false);
			
			try {
				int insertCount = loanFilesInfoDao.insert(params);
				if(insertCount!=0){
					result.put("success", true);
					result.put("message", "客户档案添加成功");
				}else{
					result.put("success", false);
					result.put("message", "客户档案添加失败");
				}
			} catch (Exception e) {
				params.clear();
				params.put("loanId", vLoanFilesInfo.getLoanId());
				List<Map<String,Object>> loanFilesInfoList = loanFilesInfoDao.findLoanFilesInfoById(params);
				if(loanFilesInfoList.size()>0){
					result.put("success", false);
					result.put("message", "该借款的客户信息已经添加，请刷新列表界面");
				}else{
					throw e;
				}
			}
		}else{
			result.put("success", false);
			result.put("message", "借款信息不存在");
		}

		return result;
	}
	
	/**
	 * 进入更新界面处理
	 * @author 00236633
	 * @param user
	 * @return
	 */
	@Override
	public Map<String, Object> updatePage(VLoanFilesInfoBase vLoanFilesInfoBase){
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		MapUtil.vObjectConvertToMap(vLoanFilesInfoBase, params, false);
		
		boolean operate = true;
		
		List<Map<String,Object>> LoanBaseInfoList = loanFilesInfoDao.findLoanBaseInfoById(params);
		if(LoanBaseInfoList.size()>0){
			result.put("loanBaseInfo", LoanBaseInfoList.get(0));
		}else{
			operate = false;
		}
		
		List<Map<String,Object>> loanFilesInfoList = loanFilesInfoDao.findLoanFilesInfoById(params);
		if(loanFilesInfoList.size()>0){
			result.put("loanFilesInfo", loanFilesInfoList.get(0));
		}else{
			operate=false;
		}
		result.put("success", operate);
		if(operate==true){
			result.put("message", "查询成功");
		}else{
			result.put("message", "借款信息不存在");
		}

		return result;
	}

	/**
	 * 更新客户档案
	 * @author 00236633
	 * @param vLoanFilesInfo
	 * @param user
	 * @return
	 */
	@Transactional
	@Override
	public Map<String, Object> update(VLoanFilesInfoUpdate vLoanFilesInfoUpdate){
		Map<String,Object> result = new HashMap<String,Object>();
		
		vLoanFilesInfoUpdate.setVersion(vLoanFilesInfoUpdate.getVersion()+1);
		User user = UserContext.getUser();
		vLoanFilesInfoUpdate.setOperatorId(user.getId());
		vLoanFilesInfoUpdate.setMark("v2");//版本号
		
		Map<String,Object> params = new HashMap<String,Object>();
		MapUtil.vObjectConvertToMap(vLoanFilesInfoUpdate, params, false);
		
		int count = loanFilesInfoDao.update(params);
		
		if(count>0){	
			result.put("success", true);
			result.put("message", "更新成功");
		}else{
			List<Map<String,Object>> loanFilesInfoList = loanFilesInfoDao.findLoanFilesInfoById(params);
			result.put("success", false);
			if(loanFilesInfoList.size()==0){
				result.put("message", "该客户档案信息已经被修改了，请刷新列表界面");
			}else{
				result.put("message", "客户档案信息不存在");
			}
		}
		
		return result;
	}
	
	/**
	 * 查询客户档案根据loanId
	 * @author 00236633
	 * @param vLoanFilesInfoList
	 * @param user
	 * @return
	 */
	@Override
	public Map<String,Object> detail(VLoanFilesInfoBase vLoanFilesInfoBase){
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		MapUtil.vObjectConvertToMap(vLoanFilesInfoBase, params, false);
		
		boolean operate = true;
		
		List<Map<String,Object>> LoanBaseInfoList = loanFilesInfoDao.findLoanBaseInfoById(params);
		if(LoanBaseInfoList.size()>0){
			result.put("loanBaseInfo", LoanBaseInfoList.get(0));
		}else{
			operate = false;
		}
		
		List<Map<String,Object>> loanFilesInfoList = loanFilesInfoDao.findLoanFilesInfoById(params);
		if(loanFilesInfoList.size()>0){
			
			params.put("ids", loanFilesInfoList.get(0).get("operatorId"));
			List<Map<String,Object>> employeeList = loanFilesInfoDao.findEmployeeListByIds(params);
			
			if(employeeList.size()>0){
				loanFilesInfoList.get(0).put("operatorName", employeeList.get(0).get("name"));
			}else{
				loanFilesInfoList.get(0).put("operatorName", "");
			}
			result.put("loanFilesInfo", loanFilesInfoList.get(0));
		}else{
			operate=false;
		}
		
		result.put("success", operate);
		
		if(operate==true){
			result.put("success", "查询成功");
		}else{
			result.put("success", "借款信息不存在");
		}

		return result;
	}

	@Override
	public Map<String, Object> getLoanCollectionManageList(VLoanCollectionInfoList vLoanCollectionInfoList) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		MapUtil.vObjectConvertToMap(vLoanCollectionInfoList, params, false);
		
		
		int pageSize = vLoanCollectionInfoList.getRows();
		int page = vLoanCollectionInfoList.getPage() ;
		int startRow = (page-1)*pageSize+1;
		int endRow = page*pageSize;
		params.put("startRow", startRow);
		params.put("endRow", endRow);
		
		List<Map<String,Object>> loanCollectionManageList = loanFilesInfoDao.findLoanCollectionManageListByMap(params);
		int loanCollectionManageCount = loanFilesInfoDao.findLoanCollectionManageCountByMap(params);		
		result.put("loanCollectionManageList", loanCollectionManageList);
		result.put("loanCollectionManageCount", loanCollectionManageCount);
		result.put("success", true);
		result.put("message", "查询成功");
		
		return result;
	}

	
	/**
	 *更新档案催收信息 
	 */
	public int updateCollectionByIds(Map<String, Object> params) {
		return loanFilesInfoDao.updateCollectionByIds(params);
	}

	@Override
	public void checkCollectionExist(List<Long> idList) {
		Date date = new Date();
		User userInfo = UserContext.getUser();
		for(Long loanId: idList){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loanId", loanId);
			List<Map<String,Object>> list = loanFilesInfoDao.getLoanCollectionByMap(params);
			if(list == null || list.size() < 1){
				Object seqValue = sequencesDao.getSeq(SequencesEnum.LOAN_COLLECTION_MANAGE_RECORD.getSequencesName());
				params.put("id", Long.parseLong(seqValue+""));
				params.put("loanId", loanId);
				params.put("operateType", "未提交");
				params.put("creator", userInfo.getName());
				params.put("operateTime", Dates.getDateTime(date, "yyyy-MM-dd HH:mm:ss"));
				loanFilesInfoDao.insertLoanCollection(params);
			}
		}
	}
}
