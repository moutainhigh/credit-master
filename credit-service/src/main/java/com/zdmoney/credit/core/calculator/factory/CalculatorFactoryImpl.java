package com.zdmoney.credit.core.calculator.factory;

import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.SpringContextUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.core.calculator.base.CalculatorBase;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现工厂抽象类 生成合同来源对应
 * 
 * @author Ivan
 *
 */
public class CalculatorFactoryImpl {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorFactoryImpl.class);

	/**
	 * 获取实例（通过合同来源获取）
	 * 
	 * @param vLoanInfo
	 *            债权实例
	 * @return
	 */
	public static ICalculator createCalculator(VLoanInfo vLoanInfo) {
		logger.info("实例获取到合同来源为{}：",vLoanInfo.getFundsSources());
		/** 合同来源 **/
		String fundsSource = Strings.parseString(vLoanInfo.getFundsSources());
		logger.info("实例获取到只符串处理之后的合同来源为{}：",vLoanInfo.getFundsSources());
		/** 合同来源 枚举 **/
		FundsSourcesTypeEnum fundsSourcesTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, fundsSource, "");
		logger.info("实例获取到同来源 枚举为{}：",fundsSourcesTypeEnum.getValue());

		/** 版本号 **/
		String calculatorVersion = Strings.parseString(vLoanInfo.getCalculatorType());
		/** 版本号 枚举 **/
		CalculatorVersionEnum calculatorVersionEnum = Assert.validEnum(CalculatorVersionEnum.class, calculatorVersion,
				"");
		/** 获取实例 **/
		return createCalculator(fundsSourcesTypeEnum, calculatorVersionEnum);
	}
	
	/**
	 * 获取实例（通过债权去向获取）
	 * 
	 * @param vLoanInfo
	 *            债权实例
	 * @return
	 */
	public static ICalculator createCalculatorByLoanBelong(VLoanInfo vLoanInfo) {
		/** 债权去向 **/
		String loanBelong = vLoanInfo.getLoanBelong();
		/** 合同来源 枚举 **/
		FundsSourcesTypeEnum loanBelongTypeEnum = Assert.validEnum(FundsSourcesTypeEnum.class, loanBelong, "");

		/** 版本号 **/
		String calculatorVersion = Strings.parseString(vLoanInfo.getCalculatorType());
		/** 版本号 枚举 **/
		CalculatorVersionEnum calculatorVersionEnum = Assert.validEnum(CalculatorVersionEnum.class, calculatorVersion,
				"");
		/** 获取实例 **/
		return createCalculator(loanBelongTypeEnum, calculatorVersionEnum);
	}

	/**
	 * 获取实例
	 * 
	 * @param fundsSourcesTypeEnum
	 *            合同来源枚举
	 * @param calculatorVersionEnum
	 *            版本号枚举
	 * @return
	 */
	public static ICalculator createCalculator(FundsSourcesTypeEnum fundsSourcesTypeEnum,
			CalculatorVersionEnum calculatorVersionEnum) {
		Assert.notNull(fundsSourcesTypeEnum, ResponseEnum.FULL_MSG, "合同来源枚举为NULL");
		Assert.notNull(calculatorVersionEnum, ResponseEnum.FULL_MSG, "版本号枚举为NULL");
		/**
		 * 跟据合同来源+版本 获取实例
		 */
		StringBuffer calculatorKey = new StringBuffer();
		calculatorKey.append("FS");
		calculatorKey.append("_");
		/** 合同来源Code **/
		calculatorKey.append(fundsSourcesTypeEnum.getCode());
		calculatorKey.append("_");
		/** 版本号 **/
		calculatorKey.append(calculatorVersionEnum.getValue());
		/** 在Spring 容器 获取指定 Bean **/
		ICalculator calculatorInstace = null;
		try {
			calculatorInstace = (ICalculator) SpringContextUtil.getBean(calculatorKey.toString());
		} finally {
			if (calculatorInstace == null) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "获取实例失败。Bean Id:" + calculatorKey.toString());
			}
		}
		return calculatorInstace;
	}
	
	/**
	 * 获取实例
	 * 
	 * @param fundsSourcesTypeEnum
	 *            合同来源枚举
	 * @return
	 */
	public static CalculatorBase createCalculatorBase(FundsSourcesTypeEnum fundsSourcesTypeEnum) {
		Assert.notNull(fundsSourcesTypeEnum, ResponseEnum.FULL_MSG, "合同来源枚举为NULL");
		/**
		 * 跟据合同来源+版本 获取实例
		 */
		StringBuffer calculatorKey = new StringBuffer();
		calculatorKey.append("FS");
		calculatorKey.append("_");
		/** 合同来源Code **/
		calculatorKey.append(fundsSourcesTypeEnum.getCode());
		/** 在Spring 容器 获取指定 Bean **/
		CalculatorBase calculatorInstace = null;
		try {
			calculatorInstace = (CalculatorBase) SpringContextUtil.getBean(calculatorKey.toString());
		} finally {
			if (calculatorInstace == null) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "获取实例失败。Bean Id:" + calculatorKey.toString());
			}
		}
		return calculatorInstace;
	}

}
