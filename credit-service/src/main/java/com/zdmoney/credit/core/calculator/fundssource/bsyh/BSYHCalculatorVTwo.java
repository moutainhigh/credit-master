package com.zdmoney.credit.core.calculator.fundssource.bsyh;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.core.calculator.fundssource.common.ExcelGetInfo;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.excel.vo.VExcelInfo;
import com.zdmoney.credit.job.RepayResultDisposeBsyhJob;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.IProdCreditProductTermService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

import org.joda.time.DateTime;

/**
 * 包商银行 计算器 版本 v2.0
 * @author YM10104
 *
 */
@Component("FS_00018_v2")
public class BSYHCalculatorVTwo extends BSYHCalculatorBase implements ICalculator {
	private static final Logger logger = Logger.getLogger(BSYHCalculatorVTwo.class);
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;
	@Autowired
	IProdCreditProductTermService prodCreditProductTermServiceImpl;
	@Autowired
	ISysParamDefineService sysParamDefineService;
	@Autowired
	IAfterLoanService afterLoanService;
	@Autowired
    private RepayResultDisposeBsyhJob job;
	@Autowired
    private IVLoanInfoService vLoanInfoService;
	
	
	@Override
	public BigDecimal getOnetimeRepaymentAmount(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
		System.out.println("包商银行 计算器 版本 v2.0");
		return super.getOnetimeRepaymentAmountV1(loanId, currDate, repayList);
	}

	@Override
	public BigDecimal getPenalty(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
		System.out.println("包商银行  计算器 版本 v2.0");
		return super.getPenaltyV1(loanId, repayList, vLoanInfo);
	}

	@Override
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm) {
		try {
			//读取excel文件获取数据
			String excelPath = sysParamDefineService.getSysParamValue("system.thirdparty", "excelPath");
			ExcelGetInfo t = new ExcelGetInfo(excelPath, loanInitialInfo, prodCreditProductTerm,loanProduct,loanBase);
			VExcelInfo excelInfo = t.getInfo();
			//债权对应产品初始化
//			super.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm); //包商银行从excel中读取数据
			loanProduct.setRate(prodCreditProductTerm.getRate());
			loanProduct.setPenaltyRate(excelInfo.getPenaltyRate());//逾期罚息
			loanProduct.setAccrualem(prodCreditProductTerm.getAccrualem());
			loanProduct.setPactMoney(excelInfo.getPactMoney().setScale(2, BigDecimal.ROUND_HALF_UP));//合同金额
			loanProduct.setGrantMoney(loanInitialInfo.getMoney());
			loanProduct.setRateem(excelInfo.getRateem());//月利率
			loanProduct.setRateey(excelInfo.getRateey());//年利率
			loanProduct.setRateed(excelInfo.getRateed());//日利率
			loanProduct.setResidualPactMoney(loanProduct.getPactMoney());
			
			loanProduct.setRisk(excelInfo.getRisk());// 风险金
			loanProduct.setRateSum(excelInfo.getPactMoney().subtract(loanInitialInfo.getMoney()).subtract(excelInfo.getRisk()));// 收入总和
			loanProduct.setManageRateForPartyC(BigDecimal.ZERO);// 丙方管理费
			loanProduct.setReferRate(excelInfo.getReferRate());//咨询费
			loanProduct.setEvalRate(excelInfo.getEvalRate());// 评估费
			loanProduct.setManageRate(excelInfo.getManageRate());// 管理费
		} catch (Exception e) {
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包商银行updateRate错误",e);
		}
	}

	@Override
	public Map<String, Object> createLoanRepaymentDetail(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		try {
			//读取excel文件获取数据
			String excelPath = sysParamDefineService.getSysParamValue("system.thirdparty", "excelPath");//获取excel路径
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包银计算器excel路径："+excelPath);
			ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
			ExcelGetInfo excelGetInfo = new ExcelGetInfo(excelPath, loanInitialInfo,prodCreditProductTerm,loanProduct,loanBase);
			VExcelInfo excelInfo = excelGetInfo.getInfo();

			for (int i = 0; i < excelInfo.getExcelDetailList().size(); i++) {
				LoanRepaymentDetail c = excelInfo.getExcelDetailList().get(i);

				c.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL));
				c.setLoanId(loanBase.getId());
				c.setCurrentTerm(i + 1L);//当前期数
				c.setRepaymentState(RepaymentStateEnum.未还款.name());
				Date dx = (Date) loanProduct.getStartrdate().clone();
				dx.setMonth(dx.getMonth() + i);
				c.setReturnDate(dx);//固定还款日期
				c.setPenaltyDate(dx);//罚息起算日期
				double accrualRevise = 0;
				c.setAccrualRevise(BigDecimal.valueOf(accrualRevise));
				loanRepaymentDetailDao.insert(c);
			}

		} catch (Exception e) {
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包商银行生成还款计划错误",e);
			throw new PlatformException("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包商银行生成还款计划错误");
		}
		return null;//不用调整精度
	}
	
	/**
	 * 贷前试算
	 * 适用于Excel表格
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	@Override
	public List<LoanRepaymentDetail> createLoanTrial(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct) {
		try {
			//读取excel文件获取数据
			String excelPath = sysParamDefineService.getSysParamValue("system.thirdparty", "excelPath");//获取excel路径
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包银计算器excel路径："+excelPath);
			ProdCreditProductTerm prodCreditProductTerm = prodCreditProductTermServiceImpl.findBymap(loanProduct.getTime(),loanInitialInfo.getLoanType(),loanBase.getFundsSources());
			ExcelGetInfo excelGetInfo = new ExcelGetInfo(excelPath, loanInitialInfo,prodCreditProductTerm,loanProduct,loanBase);
			VExcelInfo excelInfo = excelGetInfo.getInfo();
			List<LoanRepaymentDetail> detailList = excelInfo.getExcelDetailList();
			for (int i = 0; i < detailList.size(); i++) {
				LoanRepaymentDetail c = excelInfo.getExcelDetailList().get(i);

				c.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL));
				c.setLoanId(loanBase.getId());
				c.setCurrentTerm(i + 1L);//当前期数
				c.setRepaymentState(RepaymentStateEnum.未还款.name());
				Date dx = (Date) loanProduct.getStartrdate().clone();
				dx.setMonth(dx.getMonth() + i);
				c.setReturnDate(dx);//固定还款日期
				c.setPenaltyDate(dx);//罚息起算日期
				double accrualRevise = 0;
				c.setAccrualRevise(BigDecimal.valueOf(accrualRevise));
			}
			return detailList;
		} catch (Exception e) {
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包商银行试算生成还款计划错误",e);
			throw new PlatformException("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包商银行试算生成还款计划错误");
		}
	}
	
	/**
	 * 做为异步线程处理业务逻辑
	 * @author user
	 *
	 */
	class AsyncProcessThread implements Runnable {
		private Long loanId;
		AsyncProcessThread(Long loanId) {
			this.loanId = loanId;
		}
		@Override
		public void run() {
			logger.info("loanId：" + loanId + "还款日当天入账系统判定足额结清  开始向包银申请提前结清 子线程开始执行");
			try {
				/** 提前结清试算接口 BYXY0017 **/
	            job.advanceClearTrialInterface(loanId);
	            /** 还款接口 BYXY0009 **/
	            LoanRepaymentDetail currentDetail = afterLoanService.getLast(Dates.getCurrDate(),loanId);
	            job.repaymentInterface(loanId,currentDetail.getCurrentTerm().intValue());
	            logger.info("loanId：" + loanId + "还款日当天入账系统判定足额结清  开始向包银申请提前结清 子线程已执行完毕");
			} catch(Exception ex) {
				logger.error("loanId：" + loanId + "还款日当天入账系统判定足额结清  开始向包银申请提前结清 子线程执行出现异常",ex);	
			}
		}
	}

	@Override
	public boolean enterAccountAfter(OfferRepayInfo offerRepayInfo) {
		try {
			if (Const.TRADE_CODE_NORMAL.equals(offerRepayInfo.getTradeCode())
					|| Const.TRADE_CODE_ONEOFF.equals(offerRepayInfo.getTradeCode())) {
				/** 1001 正常还款   3001提前结清还款 **/
				Assert.notNull(offerRepayInfo, ResponseEnum.FULL_MSG, "入参：offerRepayInfo为空");
				Long loanId = offerRepayInfo.getLoanId();
				Assert.notNull(loanId, ResponseEnum.FULL_MSG, "入参：offerRepayInfo.loanId为空");
				/** 债权实体信息 **/
				VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
				Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "入参：loanId: " + loanId + "，未找到数据记录");
				/** 约定还款日 **/
				Long promiseReturnDate = vLoanInfo.getPromiseReturnDate();
				Assert.notNull(promiseReturnDate, ResponseEnum.FULL_MSG, "入参：loanId: " + loanId + ",约定还款日为空(PromiseReturnDate)");
				/** 判断当天是否为还款日 **/
			    if (promiseReturnDate.intValue() == DateTime.now().getDayOfMonth()) {
			    	String loanState = vLoanInfo.getLoanState();
			    	if (LoanStateEnum.预结清.getValue().equalsIgnoreCase(loanState)) {
			    		logger.info("loanId:" + loanId + ",还款日当天足额预结清 实时通知包银提前结清还款");
			    		/** 开启异步线程向包银申请提前结清还款 **/
			    		new Thread(new AsyncProcessThread(loanId)).start();
			    	}
			    }
			}
		} catch(PlatformException ex) {
			logger.error(ex,ex);
		} catch (Exception ex) {
			logger.error(ex,ex);
			return false;
		}
		return true;
		
	}

	@Override
	public boolean enterAccountBefore(OfferRepayInfo offerRepayInfo) {
		return true;
	}
}
