package com.zdmoney.credit.loan.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.repay.vo.RepayStateDetail;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.core.PublicStoreAccountVo;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.LoanRepaymentDetailVo;
import com.zdmoney.credit.loan.vo.TotalMoney;
import com.zdmoney.credit.loan.vo.VLoanDebtFileInfo;
import com.zdmoney.credit.loan.vo.VLoanDebtInfo;
import com.zdmoney.credit.loan.vo.VeloanExport;
import com.zdmoney.credit.loan.vo.VloanDebtCheckInfo;
import com.zdmoney.credit.loan.vo.VloanJmhzExport;
import com.zdmoney.credit.loan.vo.VloanPersonInfo;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.offer.service.pub.IOfferConfigService;
import com.zdmoney.credit.system.dao.pub.IComEmployeeRoleDao;
import com.zdmoney.credit.system.dao.pub.ILoanReturnRecordDao;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class VLoanInfoServiceImpl implements IVLoanInfoService {
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
    @Autowired
    private IVLoanInfoDao vLoanInfoDao;

	@Autowired
	IComEmployeeRoleDao comEmployeeRoleDao;
	
	@Autowired
	private ILoanProductDao loanProductDao;

	@Autowired
	private IOfferConfigService offerConfigService;
	
	@Autowired
	private IOfferFlowDao offerFlowDao;
	
	@Autowired
    private ILoanRepaymentDetailService loanRepaymentDetailService;
	
	@Autowired
	private ILoanReturnRecordDao loanReturnRecordDao;
	@Autowired
	private IAfterLoanService afterLoanService;
	@Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    @Override
    public List<VLoanInfo> getTQJQLoanByDate(ComOrganization salesDepartment, Date currDate) {
        // 查询当天发送报盘的所有不重复划扣类型
        List<String> debitTypeList = offerConfigService.queryAllDayDebitTypeList();
        return vLoanInfoDao.getSpecialLoanByDate(salesDepartment, currDate, SpecialRepaymentTypeEnum.一次性还款, SpecialRepaymentStateEnum.申请, debitTypeList);
    }

    @Override
    public List<VLoanInfo> getTQHKLoanByDate(ComOrganization salesDepartment, Date currDate) {
        // 查询当天发送报盘的所有不重复划扣类型
        List<String> debitTypeList = offerConfigService.queryAllDayDebitTypeList();
        return vLoanInfoDao.getSpecialLoanByDate(salesDepartment, currDate, SpecialRepaymentTypeEnum.提前扣款, SpecialRepaymentStateEnum.申请, debitTypeList);
    }

    @Override
    public List<VLoanInfo> getYQHKLoanByOrg(ComOrganization salesDepartment) {
        // 查询当天发送报盘的所有不重复划扣类型
        List<String> debitTypeList = offerConfigService.queryAllDayDebitTypeList();
        return vLoanInfoDao.getOverdueLoanByOrg(salesDepartment,debitTypeList);
    }

    @Override
    public List<VLoanInfo> getZCHKLoanByOrg(ComOrganization salesDepartment, Date currDate) {
        // 查询当天发送报盘的所有不重复划扣类型
        List<String> debitTypeList = offerConfigService.queryAllDayDebitTypeList();
        return vLoanInfoDao.getLoanByOrgAndRepDate(salesDepartment, currDate,debitTypeList);
    }

    @Override
    public VLoanInfo findByLoanId(Long loanId) {
    	return vLoanInfoDao.get(loanId);
    }

    public VLoanInfo getLoanByIdnumAndName(Map<String, Object> params) {
    	return vLoanInfoDao.getLoanByIdnumAndName(params);
    }

    public VLoanInfo getVLoanByIdnumAndName(Map<String, Object> params) {
    	return vLoanInfoDao.getVLoanByIdnumAndName(params);
    }
    public VLoanInfo getVLoanBySignDateAndBorrowerAndLoanType(Map<String, Object> params){
    	return vLoanInfoDao.getVLoanBySignDateAndBorrowerAndLoanType(params);
    }
    
    
    public VLoanInfo getVLoanBlackInfo(Map<String, Object> params){
    	return vLoanInfoDao.getVLoanBlackInfo(params);
    }


	@Override
    public List<VLoanInfo> getYQHKLoanByPromisereturnDate(int promisereturnDate) {

    	return vLoanInfoDao.getYQHKLoanByPromisereturnDate(promisereturnDate);
    }
	
	@Override
    public List<VLoanInfo> getYQHKLoanByPromisereturnDateAndLoanBelong(int promisereturnDate, String loanBelong) {

        return vLoanInfoDao.getYQHKLoanByPromisereturnDateAndLoanBelong(promisereturnDate,loanBelong);
    }

    /**
     * 跟居实体查询结果集(公共)
     * 
     * @param vLoanInfo
     * @return
     */
    @Override
    public List<VLoanInfo> findListByVO(VLoanInfo vLoanInfo) {
    	return vLoanInfoDao.findListByVo(vLoanInfo);
    }

    @Override
	public List<VLoanInfo> findListByMap(Map<String, Object> params) {
    	return vLoanInfoDao.findListByMap(params);
	}
    
    /***
     * 还款录入模块多表查询(分页查询)
     * 
     * @param paramMap
     *            参数集合
     * @return
     */
    @Override
    public Pager searchRepaymentLoanWithPg(Map<String, Object> paramMap) {
    	return vLoanInfoDao.searchRepaymentLoanWithPg(paramMap);
    }

    @Override
    public List<VLoanInfo> searchVLoanInfoList(Map<String, Object> params) {
    	return vLoanInfoDao.searchVLoanInfoList(params);
    }

    @Override
    public Pager searchVLoanInfoWithPg(Map<String, Object> paramMap) {
    	return vLoanInfoDao.searchVLoanInfoWithPg(paramMap);
    }
    
    /**
     * 跟据借款人、借款类型、借款状态、 还款日期查询借款数据
     * 
     * @param params
     *            borrowerId 借款人
     * @param params
     *            loanType 借款类型
     * @param params
     *            loanStates 借款状态
     * @param params
     *            startrDateBegin 还款日期（开始）
     * @param params
     *            startrDateEnd 还款日期（结束）
     * @return
     */
    @Override
    public List<VLoanInfo> findByBorrowerAndTypeAndStateAndStartrDate(Long borrowerId, String loanType, String[] loanStates, Date startrDateBegin,
	    Date startrDateEnd){
    	
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("borrowerId", borrowerId);
		paramMap.put("loanType", loanType);
		paramMap.put("loanStates", loanStates);
		paramMap.put("startrdateBegin", startrDateBegin);
		paramMap.put("startrdateEnd", startrDateEnd);
		return vLoanInfoDao.findListByMap(paramMap);
    }
    
    /**
     * 跟据借款人和借款状态查询借款数据
     * 
     * @param 
     *            borrowerId 借款人
     * @param 
     *            loanStates 借款状态
     */
    @Override
    public List<VLoanInfo> findByBorrowerAndState(Long borrowerId, String[] loanStates){
    	Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("borrowerId", borrowerId);
		paramMap.put("loanStates", loanStates);
		return vLoanInfoDao.findListByMap(paramMap);
    }

	@Override
	public  List<VLoanInfo>  searchVLoanInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.searchVLoanInfo(params);
	}

	public List<Map<String, Object>> queryForLoanExport(Map<String, Object> params){
		return vLoanInfoDao.queryForLoanExport(params);
	}
	
	
	public List<Map<String, Object>> queryForLoanRepayExp(Map<String, Object> params){
		return vLoanInfoDao.queryForLoanRepayExp(params);
	}

	@Override
	public List<Map<String, Object>> getExternalDebtList(Map<String, Object> params) {
		return vLoanInfoDao.getExternalDebtList(params);
	}

	@Override
	public Pager listquerBatchNumDetail(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.listquerBatchNumDetail(paramMap);
	}

	@Override
	public Pager getCurrentDayLoanBaseBybatchNum(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.getCurrentDayLoanBaseBybatchNum(paramMap);
	}

	@Override
	public List<VeloanExport> getVeloanExportList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.getVeloanExportList(paramMap);
	}

	@Override
	public List<TotalMoney> getTotalMoney(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.getTotalMoney(paramMap);
	}
	 public List<VloanJmhzExport> getVeloanJmhzExportList(Map<String, Object> paramMap){
		 return vLoanInfoDao.getVeloanJmhzExportList(paramMap);
	 }

	@Override
	public List<LoanRepaymentDetailVo> getVeloanReymentExportList(
			Map<String, Object> paramMap) {
		return vLoanInfoDao.getVeloanReymentExportList(paramMap);
	}
	 public List<VLoanDebtInfo> getVLoanDebtInfoExportList(Map<String, Object> paramMap){
		return vLoanInfoDao.getVLoanDebtInfoExportList(paramMap);
	}
	 public List<VloanDebtCheckInfo> getVLoanDebtCheckInfoExportList(Map<String, Object> paramMap){
		 return vLoanInfoDao.getVLoanDebtCheckInfoExportList(paramMap);
	 }

	@Override
	public boolean checkFunds(String userCode) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("usercode",userCode);
		params.put("roleName","信托放款");
		int result=comEmployeeRoleDao.findComEmployeeRoleByUserCodeAndRoleName(params);
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public int checkBatchNum(List<Long> list) {		
		return vLoanInfoDao.checkBatchNum(list);
	}
	public int checkBatchNumUpdate(Map<String, Object> paramMap) {		
		return vLoanInfoDao.checkBatchNumUpdate(paramMap);
	}
	
	@Override
	public int updateBatchNum(Map<String, Object> paramMap) {
		int affectNum = 0;
		String batchNum =paramMap.get("org") + new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		batchNum=batchNum+StringUtils.leftPad(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_BASE_BATCHNUM).toString(),5,'0');
		paramMap.put("batchNum", batchNum);
		affectNum=vLoanInfoDao.updateBatchNum(paramMap);
		return affectNum;
	}


	
	@Override
	public int updateBatchNumAT(List<Long> list) {
		return vLoanInfoDao.updateBatchNumAT(list);
	}

	@Override
	public int updateBatchNumATByBatchNum(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.updateBatchNumATByBatchNum(paramMap);
	}

	@Override
	public int updateBatchNumByBatchNumNull(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.updateBatchNumByBatchNumNull(paramMap);
	}
	public int updateBatchNumByBatchNum(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.updateBatchNumByBatchNum(paramMap);
	}

	@Override
	public Pager listquerBatchNumWC2Detail(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.listquerBatchNumWC2Detail(paramMap);
	}

	@Override
	public Pager listquerBatchNumNotWC2Detail(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return vLoanInfoDao.listquerBatchNumNotWC2Detail(paramMap);
	}
	@Override
    public List<VLoanInfo> getVLoanByMap(Map<String, Object> params){
		return vLoanInfoDao.getVLoanByMap(params);
	}
	/**
     * 根据借款人姓名、合同编号查询借款数据
     * 
     * @param 
     *            borrowerId 借款人
     * @param 
     *            contractNum 合同编号
     */
	@Override
	public List<VLoanInfo> findByBorrowerAndContractNumAndState(Long borrowerId,String contractNum, String[] loanStates) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("borrowerId", borrowerId);
		paramMap.put("contractNum", contractNum);
		paramMap.put("loanStates", loanStates);
		return vLoanInfoDao.findListByMap(paramMap);
	}
	/**
     * 根据借款人、借款类型、借款状态、 还款日期、合同编号查询借款数据
     * 
     * @param 
     *            borrowerId 借款人
     * 
     * @param     
     *            loanType   借款类型
     *            
     *  @param    loanStates 借款状态
     *  
     *  @param
     *            startrDateBegin  还款日期
     *  
     * @param        
     *            contractNum 合同编号
     *            
     */
	@Override
	public List<VLoanInfo> findByBorrowerAndTypeAndStateAndStartrDateAndContractNum(Long borrowerId, String loanType, String[] loanStates, Date startrDateBegin,
    	    Date startrDateEnd,String contractNum){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("borrowerId", borrowerId);
		paramMap.put("loanType", loanType);
		paramMap.put("loanStates", loanStates);
		paramMap.put("startrdateBegin", startrDateBegin);
		paramMap.put("startrdateEnd", startrDateEnd);
		paramMap.put("contractNum", contractNum);
		return vLoanInfoDao.findListByMap(paramMap);
	}

//	@Override
//	public Pager listquerBatchNumWC2DetailNotCurrentDay(
//			Map<String, Object> paramMap) {
//		// TODO Auto-generated method stub
//		return vLoanInfoDao.listquerBatchNumWC2DetailNotCurrentDay(paramMap);
//	}
    
    /**
     * 判断当前日是否为还款日
     * @param loanId
     * @return
     */
    public boolean isRepaymentDay(Long loanId){
        if(Strings.isEmpty(loanId)){
            return false;
        }
        Calendar calendar=Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day != 1 && day != 16){
            return false;
        }
        LoanProduct loanInfo = loanProductDao.findByLoanId(loanId);
        if (null==loanInfo){
            return false;
        }
        if(day != loanInfo.getPromiseReturnDate().intValue()){
            return false;
        }
        return true;
    }

	public Pager listquerBatchNumHMDetail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumHMDetail(paramMap);
	}
	
	public Pager listquerBatchNumLXXDDetail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumLXXDDetail(paramMap);
	}
	
	public Pager listLoanOrder(Map<String, Object> paramMap) {
		return vLoanInfoDao.searchLoanOrderWithPg(paramMap);
	}

	@Override
	public VloanPersonInfo findImportLoanInfo(Map<String, Object> paramMap) {
		return vLoanInfoDao.findImportLoanInfo(paramMap);
	}

	@Override
	public Pager listquerBatchNumWMXTDetail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumWMXTDetail(paramMap);
	}

	@Override
	public Pager listquerBatchNumBHXTDetail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumBHXTDetail(paramMap);
	}
	
	@Override
	public List<LoanRepaymentDetailVo> getWxmtLoanReymentExportList(Map<String, Object> paramMap) {
		return vLoanInfoDao.getWmxtLoanReymentExportList(paramMap);
	}

	@Override
	public List<VLoanDebtFileInfo> getExportDebtInfo4WMXT(Map<String, Object> paramMap) {
		return vLoanInfoDao.getWmxtLoanDebtExportList(paramMap);
	}
	
	public Pager listquerBatchNumBHXT2Detail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumBHXT2Detail(paramMap);
	}

	@Override
	public Pager listquerBatchNumBHXT3Detail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumBHXT3Detail(paramMap);
	}

	public Pager listquerBatchNumWM2Detail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumWM2Detail(paramMap);
	}
	
	@Override
	public VLoanInfo getBelongVLoanInfoByNum(Map<String, Object> params) {
		return vLoanInfoDao.getBelongVLoanInfoByNum(params);
	}
	
	@Override
	public Pager listquerBatchNumBSYHDetail(Map<String, Object> paramMap) {
		return vLoanInfoDao.listquerBatchNumBSYHDetail(paramMap);
	}

	public List<VloanPersonInfo> findLoanData2UploadInfo(Map<String, Object> params) {
		return vLoanInfoDao.findLoanData2UploadInfo(params);
	}

	@Override
	public List<Map<String, Object>> findPersonLoanInfo(Map<String, Object> params) {
		return vLoanInfoDao.findPersonLoanInfo(params);
	}
	
	@Override
	public List<VLoanInfo> findGrantSuccessNotToWM3(Map<String, Object> map) {
		return vLoanInfoDao.findGrantSuccessNotToWM3(map);
	}
	@Override
	public Pager getLoanVoByStateWithPg(Map<String, Object> params) {
		return vLoanInfoDao.getLoanVoByStateWithPg(params);
	}
	
	public PublicStoreAccountVo getPublicStoreAccountDetail(Map<String, Object> params){
		return vLoanInfoDao.getPublicStoreAccountDetail(params);
	}

	@Override
	public Pager findAcountLoanManageWithPg(VPersonVisit personVisit) {
		return vLoanInfoDao.findAcountLoanManageWithPg(personVisit);
	}

	@Override
	public int updateLoanInfExtByLoanId(Map<String, Object> params) {
		return vLoanInfoDao.updateLoanInfExtByLoanId(params);
	}

	@Override
	public int addLoanInfoExtByLoanId(VLoanInfo vLoanInfo) {
		/** 登陆者信息 **/
		User user = UserContext.getUser();
		String creator = user == null ? "admin" : user.getName();
		Map<String, Object> params = new HashMap<>();
		params.put("loanId", vLoanInfo.getId());
		params.put("creator",creator);
		params.put("updator",creator);
		params.put("assignState","1");
		//查找是否已存在导入记录
		VPersonVisit vo = new VPersonVisit();
		vo.setContractNum(vLoanInfo.getContractNum());
		Pager pager = vLoanInfoDao.findAcountLoanManageWithPg(vo);
		Long count = pager.getTotalCount();
		if(count >0){
			//如果已经存在记录，判断是否已取消
			Map<String,Object> result = (Map<String, Object>) pager.getResultList().get(0);
			String assignState = (String) result.get("assignState");
			//债权未取消 抛出异常
			if(assignState.equals("1")){
				throw new PlatformException(ResponseEnum.FULL_MSG,"该笔债权已存在导入记录");
			}
			//债权已取消 更新记录
			return vLoanInfoDao.updateLoanInfExtByLoanId(params);
		}
		return vLoanInfoDao.addLoanInfoExtByLoanId(params);
	}

	@Override
	public Long getOutSourcing(Long loanId) {		
		return vLoanInfoDao.getOutSourcing(loanId);
	}
	
	@Override
	public RepayStateDetail getRepayStateDetailOverdueValue(Map<String, Object> paramMap) {
		return vLoanInfoDao.getRepayStateDetailOverdueValue(paramMap);
	}

}
