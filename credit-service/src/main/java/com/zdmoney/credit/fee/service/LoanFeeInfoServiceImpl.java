package com.zdmoney.credit.fee.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.dao.pub.ILoanProcessHistoryDao;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;
import com.zdmoney.credit.loan.service.pub.ILoanProcessHistoryService;
import com.zdmoney.credit.system.service.SysParamDefineServiceImpl;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanFeeStateEnum;
import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeInfoDao;
import com.zdmoney.credit.fee.domain.LoanFeeImportData;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.domain.LoanFeeRepayRecord;
import com.zdmoney.credit.fee.offer.LoanFeeUtil;
import com.zdmoney.credit.fee.service.pub.ILoanFeeImportDataService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeRepayRecordService;
import com.zdmoney.credit.fee.vo.CreateLoanFeeVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.VloanPersonInfo;
import com.zdmoney.credit.payment.vo.WaiMaoXinTuo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款收费主表 Service实现层，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
@Service
public class LoanFeeInfoServiceImpl implements ILoanFeeInfoService {

	protected static Log logger = LogFactory.getLog(LoanFeeInfoServiceImpl.class);

	@Autowired
	@Qualifier("loanFeeInfoDaoImpl")
	ILoanFeeInfoDao loanFeeInfoDaoImpl;

	@Autowired
	@Qualifier("VLoanInfoServiceImpl")
	IVLoanInfoService vLoanInfoServiceImpl;

	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Autowired
	IFinanceCoreService financeCoreService;

	@Autowired
	ILoanFeeImportDataService iLoanFeeImportDataService;
	
	@Autowired
	ILoanFeeRepayRecordService loanFeeRepayRecordService;

	@Autowired
	private LoanFeeUtil loanFeeUtil;

	@Autowired
	private ISysParamDefineService sysParamDefineService;

	@Autowired
	private ILoanProcessHistoryDao loanProcessHistoryDao;

	@Override
	public List<LoanFeeInfo> findListByVo(LoanFeeInfo loanFeeInfo) {
		return loanFeeInfoDaoImpl.findListByVo(loanFeeInfo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LoanFeeInfo createLoanFee(CreateLoanFeeVo createLoanFeeVo) {
		Long loanId = createLoanFeeVo.getLoanId();
		/** 查询借款信息 **/
		VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
		Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "生成借款收费主记录失败：LoanId：" + loanId + "记录不存在");
		String loanState = Strings.parseString(vLoanInfo.getLoanState());
		if (!loanState.equals(LoanStateEnum.正常.getValue())) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "生成借款收费主记录失败：借款未处于正常状态").applyLogLevel(LogLevel.WARN);
		}

		/** 判断数据是否已存在 **/
		boolean isExists = loanFeeInfoDaoImpl.isExistsByLoanId(loanId);
		if (isExists) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "LoanId:" + loanId + " 记录已存在，无法保存")
					.applyLogLevel(LogLevel.WARN);
		}

		/** 审批金额 **/
		BigDecimal money = vLoanInfo.getMoney();
		/** 合同金额 **/
		BigDecimal pactMoney = vLoanInfo.getPactMoney();
		/** 服务费金额 **/
		BigDecimal feeMoney = pactMoney.subtract(money);
		if (feeMoney.compareTo(BigDecimal.ZERO) <= 0) {
			throw new PlatformException(ResponseEnum.FULL_MSG, "生成借款收费主记录失败：服务费金额小于等于0").applyLogLevel(LogLevel.WARN);
		}
		LoanFeeInfo loanFeeInfo = new LoanFeeInfo();
		/** 设置主键值 **/
		loanFeeInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_INFO));
		loanFeeInfo.setLoanId(loanId);
		/** 应收服务费金额 **/
		loanFeeInfo.setAmount(feeMoney);
		/** 已收服务费金额 默认为0 **/
		loanFeeInfo.setReceiveAmount(BigDecimal.ZERO);
		/** 未收服务费金额 默认为全额 **/
		loanFeeInfo.setUnpaidAmount(feeMoney);
		/** 服务费收取状态 **/
		loanFeeInfo.setState(LoanFeeStateEnum.未收取.getValue());
		/** 备注 **/
		loanFeeInfo.setMemo("");
		/** 保存入库 **/
		loanFeeInfoDaoImpl.insert(loanFeeInfo);
		/** 生成服务费分账信息**/
		/** 支出费用科目号*/
		String [] account_titles = {Const.ACCOUNT_TITLE_RISK_EXP,Const.ACCOUNT_TITLE_MANAGE_EXP,Const.ACCOUNT_TITLE_CONSULT_EXP,Const.ACCOUNT_TITLE_APPRAISAL_EXP};
		for(String account_title : account_titles){
			LoanFeeRepayRecord loanFeeRepayRecord = new LoanFeeRepayRecord();
			switch(account_title){
				case Const.ACCOUNT_TITLE_RISK_EXP:
					loanFeeRepayRecord.setAmount(vLoanInfo.getRisk());
					loanFeeRepayRecord.setRepayamount(vLoanInfo.getRisk());
				break;
				case Const.ACCOUNT_TITLE_MANAGE_EXP:
					loanFeeRepayRecord.setAmount(vLoanInfo.getManageRate());
					loanFeeRepayRecord.setRepayamount(vLoanInfo.getManageRate());
				break;
				case Const.ACCOUNT_TITLE_CONSULT_EXP:
					loanFeeRepayRecord.setAmount(vLoanInfo.getReferRate());
					loanFeeRepayRecord.setRepayamount(vLoanInfo.getReferRate());
				break;
				case Const.ACCOUNT_TITLE_APPRAISAL_EXP:
					loanFeeRepayRecord.setAmount(vLoanInfo.getEvalRate());
					loanFeeRepayRecord.setRepayamount(vLoanInfo.getEvalRate());
				break;
				default:
					loanFeeRepayRecord.setAmount(BigDecimal.ZERO);
				break;
			}
			//科目号
			loanFeeRepayRecord.setAcctTitle(account_title);
			//债权id
			loanFeeRepayRecord.setLoanId(loanId);
			//服务费id
			loanFeeRepayRecord.setfeeId(loanFeeInfo.getId());
			//剩余未还费用
			loanFeeRepayRecordService.saveLoanFeeRepayRecord(loanFeeRepayRecord);
		}
		return loanFeeInfo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LoanFeeInfo createLoanFeeWithGrantLoan(CreateLoanFeeVo createLoanFeeVo) {
		Long loanId = createLoanFeeVo.getLoanId();
		FinanceVo financeVo = new FinanceVo();
		financeVo.setIds(Strings.convertValue(loanId, String.class));
		User user = UserContext.getUser();
		String userCode = "admin";
		if (user != null) {
			userCode = user.getUserCode();
		}
		financeVo.setUserCode(userCode);
		/** 调用核心放款接口 **/
		Map<String, Object> result = financeCoreService.grantLoan(financeVo);
		if (result.containsKey("code") && result.get("code").equals(Constants.SUCCESS_CODE)) {
			/** 放款接口返回成功 调用生成服务费方法 **/
			LoanFeeInfo loanFeeInfo = createLoanFee(createLoanFeeVo);
			return loanFeeInfo;
		} else {
			throw new PlatformException(ResponseEnum.FULL_MSG, "loanId：" + loanId + " grantLoan处理异常 "
					+ result.toString());
		}

	}

	@Override
	public LoanFeeInfo findById(Long id) {
		if (Strings.isEmpty(id)) {
			return null;
		}
		return loanFeeInfoDaoImpl.get(id);
	}

	@Override
	public LoanFeeInfo updateFeeInfo(LoanFeeInfo loanFeeInfo) {
		loanFeeInfoDaoImpl.update(loanFeeInfo);
		return loanFeeInfo;
	}

	@Override
	public LoanFeeInfo findLoanFeeInfoByLoanId(Long loanId) {
		return loanFeeInfoDaoImpl.findLoanFeeInfoByLoanId(loanId);
	}
	
	/**
	 * 外贸信托导入数据验证
	 */
	public List<String> checkWaiMaoXinTuo(List<String []> dataList){
		String batchCode = "wmxt" + Dates.getDateTime(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();// 批次号
		LoanFeeImportData loanFeeImportData = null;// 日志记录对象
		WaiMaoXinTuo xintuo = new WaiMaoXinTuo();
		VloanPersonInfo vloanPersonInfo = null;
		List<WaiMaoXinTuo> list = xintuo.validate(dataList);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<String> result = new ArrayList<String>();//返回结果
		for (WaiMaoXinTuo wmxt : list) {
			try {
				// 记录导入日志
				loanFeeImportData = new LoanFeeImportData();
				
				if(StringUtils.isNotEmpty(wmxt.getRemark())){
					logger.info("外贸信托数据字段信息不完整==" + wmxt.getRemark());
					result.add(wmxt.getRemark());
					loanFeeImportData.setMemo(wmxt.getRemark());
					continue;
				}
				paramMap.put("contractNum", wmxt.getContractNum());
				paramMap.put("fundsSources", "外贸信托");
				vloanPersonInfo = vLoanInfoServiceImpl.findImportLoanInfo(paramMap);
				loanFeeImportData.setGrantMoneyDate(wmxt.getBackDate());
				loanFeeImportData.setResult(wmxt.getApproveResult());
				// 判断当前信息是否存在
				if (vloanPersonInfo != null && LoanFlowStateEnum.财务放款.getValue().equals(vloanPersonInfo.getLoanFlowState())) {
					if (wmxt.getBackDate() == null || Dates.format(vloanPersonInfo.getRequestDate(), "yyyy-MM-dd").compareTo(Dates.format(wmxt.getBackDate(), "yyyy-MM-dd")) > 0) {
						result.add("放款时间不得小于申请时间");
						loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:导入执行结果：放款时间小于申请时间!");
					}else{
						loanFeeUtil.createFee(vloanPersonInfo.getId(), FundsSourcesTypeEnum.外贸信托.getValue());
						// 执行借款收费主表 插入更新操作
						loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:导入执行结果：导入成功!");
						result.add("导入成功");
						logger.info("合同编号==" + wmxt.getContractNum() + "生成收费主表记录成功!");
					}
				}else {
					loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:借款信息不存在!");
					result.add("借款信息不存在");
					logger.info("借款信息不存在");
				}
				
			} catch (PlatformException e) {
				loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " 生成收费主表记录异常信息：" + e.getMessage());
				logger.error("生成收费主表记录异常：合同编号==" + wmxt.getContractNum() + "债权编号==" + vloanPersonInfo != null ? vloanPersonInfo.getId() : "", e);
				result.add(e.getMessage());
			} catch (Exception e) {
				loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:导入执行结果：导入信息验证异常.");
				logger.error("外贸信托导入第三方线下放款信息-->数据验证异常：合同编号==" + wmxt.getContractNum(), e);
				result.add("导入验证失败");
			} finally {
				loanFeeImportData.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_IMPORT_DATA));
				loanFeeImportData.setBatchCode(batchCode);
				iLoanFeeImportDataService.save(loanFeeImportData);
			}
		}
		return result;
	}
	
	public void checkOffLineLoanInfo(List<Map<String, String>> dataList) {
		logger.info("==========第三方线下放款导入信息验证=========");
		Map<String, Object> paramMap = null;
		VloanPersonInfo vloanPersonInfo = null;
		LoanFeeImportData loanFeeImportData = null;// 日志记录对象

		String batchCode = "lx" + Dates.getDateTime(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();// 批次号
		String idNum = "";
		String contractNum = "";
		for (Map<String, String> map : dataList) {
			try {
				// 记录导入日志
				loanFeeImportData = new LoanFeeImportData();
				
				// 验证excel每一行是否数据完整
				paramMap = loanFeeImportData.validate(map);
				loanFeeImportData = (LoanFeeImportData) paramMap.get("loanFeeImportData");
				if (paramMap.get("validateResult") != null) {
					logger.info("====excel文件列字段数据不完整====");
					loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:" + JSON.toJSONString(paramMap.get("validateResult")));
					map.put(ExcelTemplet.FEED_BACK_MSG, paramMap.get("validateResult").toString());
					continue;
				}
				idNum = map.get("idNum");
				contractNum = map.get("contractNum");
				paramMap.put("idNum", map.get("idNum"));
				paramMap.put("contractNum", map.get("contractNum"));
				paramMap.put("requestDate", map.get("grantMoneyDate"));
				paramMap.put("fundsSources", "龙信小贷");
				vloanPersonInfo = vLoanInfoServiceImpl.findImportLoanInfo(paramMap);
				
				// 判断当前信息是否存在
				if (vloanPersonInfo != null
						&& LoanFlowStateEnum.财务放款.getValue().equals(vloanPersonInfo.getLoanFlowState())) {
					int pactMoneyFlag = vloanPersonInfo.getPactMoney().compareTo(new BigDecimal(map.get("pactMoney")));
					if (StringUtils.isEmpty(map.get("grantMoneyDate"))
							|| Dates.format(vloanPersonInfo.getRequestDate(), "yyyy-MM-dd").compareTo(
									Dates.parse(map.get("grantMoneyDate"), "yyyy-MM-dd")) > 0) {
						map.put(ExcelTemplet.FEED_BACK_MSG, "放款时间不得小于申请时间");
						loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:导入执行结果：放款时间小于申请时间!");
					} else if (pactMoneyFlag > 0 || pactMoneyFlag < 0) {// 验证金额
						// 金额不匹配，请重新核对
						map.put(ExcelTemplet.FEED_BACK_MSG, "金额不匹配，请重新核对");
						loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:导入执行结果：金额不匹配!");
					} else {
						loanFeeUtil.createFee(vloanPersonInfo.getId(), FundsSourcesTypeEnum.龙信小贷.getValue());
						// 执行借款收费主表 插入更新操作
						map.put(ExcelTemplet.FEED_BACK_MSG, "导入成功!");
						loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:导入执行结果：导入成功!");
						logger.info("用户：" + loanFeeImportData.getName() + "idNum==" + idNum + "，合同编号==" + contractNum + "生成收费主表记录成功!");
					}

				} else if (vloanPersonInfo != null && !LoanFlowStateEnum.财务放款.getValue().equals(vloanPersonInfo.getLoanFlowState())) {
					loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:当前状态非财务放款状态，不予处理!");
					map.put(ExcelTemplet.FEED_BACK_MSG, "当前状态非财务放款状态，不予处理");
				} else {
					loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:借款信息不存在!");
					map.put(ExcelTemplet.FEED_BACK_MSG, "借款信息不存在");
				}
			} catch (PlatformException e) {
				loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " 生成收费主表记录异常信息：" + e.getMessage());
				logger.error("生成收费主表记录异常：idNum==" + idNum + "，合同编号==" + contractNum + "债权编号==" + vloanPersonInfo != null ? vloanPersonInfo.getId() : "", e);
				map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
			} catch (Exception e) {
				loanFeeImportData.setMemo(loanFeeImportData.getMemo() + " ps:导入执行结果：导入信息验证异常.");
				logger.error("导入第三方线下放款信息-->数据验证异常：idNum==" + idNum + "，合同编号==" + contractNum, e);
				map.put(ExcelTemplet.FEED_BACK_MSG, "导入验证失败！");
			} finally {
				loanFeeImportData.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_IMPORT_DATA));
				loanFeeImportData.setBatchCode(batchCode);
				iLoanFeeImportDataService.save(loanFeeImportData);
			}
		}
	}

    /**
     * 针对龙信小贷、外贸信托、外贸2、包商银行，判断是否已经完成划扣服务费
     * 
     * @param loanId
     * @return
     */
    public boolean isAlreadyDebitServiceCharge(Long loanId) {
        // 查询债权信息
        VLoanInfo loanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        // 合同来源
        String fundsSource = loanInfo.getFundsSources();
        if (!FundsSourcesTypeEnum.龙信小贷.getValue().equals(fundsSource)
            && !FundsSourcesTypeEnum.外贸信托.getValue().equals(fundsSource)
            && !FundsSourcesTypeEnum.外贸2.getValue().equals(fundsSource)
            && !FundsSourcesTypeEnum.包商银行.getValue().equals(fundsSource)
			&& !FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)
			&& !FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSource)
			&& !FundsSourcesTypeEnum.外贸3.getValue().equals(fundsSource)
			&& !FundsSourcesTypeEnum.陆金所.getValue().equals(fundsSource)) {
            return true;
        }
		if (FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)) {
			Date date = Dates.parse("2017-04-30",Dates.DEFAULT_DATE_FORMAT);
			if (loanInfo.getGrantMoneyDate().compareTo(date) < 0) {
				return true;
			}
		}
		if (FundsSourcesTypeEnum.陆金所.getValue().equals(fundsSource)) {
			String dateString = sysParamDefineService.getSysParamValue("param", "ljsToTppDate");
			Date date = Dates.parse(dateString,Dates.DEFAULT_DATETIME_FORMAT);
			//查询该笔债权的合同确认时间
			LoanProcessHistory loanProcessHistory=new LoanProcessHistory();
			loanProcessHistory.setLoanId(loanInfo.getId());
			loanProcessHistory.setLoanFlowState("财务审核");
			List<LoanProcessHistory> list = loanProcessHistoryDao.findListByVo(loanProcessHistory);
			LoanProcessHistory value=list.get(0);
			if (value.getCreateTime().compareTo(date) < 0) {
				return true;
			}
		}
        LoanFeeInfo searchVo = new LoanFeeInfo();
        searchVo.setLoanId(loanId);
        // 查询服务费收费信息
        List<LoanFeeInfo> resultList = loanFeeInfoDaoImpl.findListByVo(searchVo);
        if (CollectionUtils.isEmpty(resultList)) {
            return false;
        }
        // 收费状态
        String state = resultList.get(0).getState();
        if (LoanFeeStateEnum.已收取.getValue().equals(state)) {
            return true;
        }
        return false;
    }
}
