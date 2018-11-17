package com.zdmoney.credit.core.calculator.fundssource.jmhz;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.common.util.ExcelFunctionUtil;
import com.zdmoney.credit.core.calculator.base.CalculatorBase;
import com.zdmoney.credit.loan.dao.pub.IJimuProductDao;
import com.zdmoney.credit.loan.domain.JimuProduct;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

/**
 * 积木盒子 计算器 (父类)
 * 
 * 用于实现计算器公共方法
 * 
 * @author Ivan
 *
 */
@Component("FS_00007")
public class JMHZCalculatorBase extends CalculatorBase {

	@Autowired
	IJimuProductDao jimuProductDao;// 积木盒子产品操作DAO
	
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm) {

		super.updateRate(loanBase, loanInitialInfo, loanProduct,prodCreditProductTerm);
		
		JimuProduct jimuProduct = jimuProductDao.findByTime(loanProduct.getTime());
		Double risk = loanProduct.getPactMoney().doubleValue() * 1.5 / loanProduct.getTime().doubleValue();
		loanProduct.setRisk(BigDecimal.valueOf(risk).setScale(2, BigDecimal.ROUND_HALF_UP));
		loanProduct.setManageRateForPartyC(loanProduct.getPactMoney().multiply(jimuProduct.getManagerRateForPartyc()).setScale(2, BigDecimal.ROUND_HALF_UP));
		loanProduct.setRateSum(loanProduct.getPactMoney().subtract(loanInitialInfo.getMoney()).subtract(loanProduct.getRisk())); //收费合计
		
		BigDecimal tempRate = loanProduct.getRateSum().subtract(loanProduct.getManageRateForPartyC());
		loanProduct.setReferRate(tempRate.multiply(new BigDecimal("0.45")).setScale(2, BigDecimal.ROUND_HALF_UP));//咨询费
		loanProduct.setEvalRate(tempRate.multiply(new BigDecimal("0.45")).setScale(2, BigDecimal.ROUND_HALF_UP));//评估费
		loanProduct.setManageRate(loanProduct.getRateSum().subtract(loanProduct.getReferRate()).subtract(loanProduct.getEvalRate()).subtract(loanProduct.getManageRateForPartyC().setScale(2, BigDecimal.ROUND_HALF_UP)));//管理费
	}
	
	/**
	 * 积木盒子利率
	 * 
	 * @param loanProduct
	 * @return
	 */
	protected double jimuheziCalReapyEterm(LoanProduct loanProduct) {
		JimuProduct jimuProduct = jimuProductDao.findByTime(loanProduct
				.getTime());

		double rate4year = Double.valueOf(jimuProduct.getRateey().toString());
		double rate4month = Math.pow(1 + rate4year, 1 / 12) - 1;
		// 已知：期初、期末、月利率、求每月还款额
		double payEterm = -ExcelFunctionUtil.pmt(rate4month,
				Double.valueOf(loanProduct.getTime()),
				Double.valueOf(loanProduct.getPactMoney().toString()), 0);
		double payEterm2 = (double) (Math.round(payEterm * 10000) / 10000.0);// 四舍五入，保留4位小数，（中间数值非最终结果）
		return payEterm2;
	}
	
	/**
	 * 对应第三方的贴息或扣息 (积木盒子)
	 * 
	 * @param payEterm4Jimu
	 * @param returnETerm
	 * @param currentAccrual
	 * @return
	 */
	protected double jimuheziCalAccrualRevise(double payEterm4Jimu, double returnETerm, double currentAccrual) {
		double amount = payEterm4Jimu - returnETerm;
		if (amount < 0 && -amount > currentAccrual) {
			return amount = -currentAccrual;
		}
		return Double.parseDouble(String.format("%.2f", amount));
	}
	
	@Override
	public CalculatorVersionEnum getCalculatorVersion() {
		
		/**根据当前时间判断执行哪一个版本的计算器**/
		/*if(isV2Date()){
			return CalculatorVersionEnum.v1;
		} else {
			return CalculatorVersionEnum.v2;
		}*/
		
		return CalculatorVersionEnum.v1;
	}
	@Override
	public int getLoanType(String loanTypeStr) {
		
		 int loanType = "随薪贷、保单贷、公积金贷、网购达人贷A、网购达人贷B".indexOf(loanTypeStr) >= 0 ? 1 :
             ("薪生贷".indexOf(loanTypeStr) >= 0 ? 2 :
             ("随房贷、随房贷A、随房贷B、随车贷、淘宝商户贷、学历贷、卡友贷".indexOf(loanTypeStr) >= 0 ? 3 :
             ("随意贷、随意贷A、随意贷B、随意贷C".indexOf(loanTypeStr) >= 0 ? 4 : -1)));
		
		return loanType;
	}
}
