package com.zdmoney.credit.core.calculator.factory;

import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.core.calculator.pub.ICalculator;

/**
 * 计算器工厂 抽象类
 * 
 * @author Ivan
 *
 */
public abstract class CalculatorFactory {
	/**
	 * 生成计算器实例
	 * 
	 * @param fundsSourcesTypeEnum
	 *            合同来源
	 * @param calculatorVersionEnum
	 *            计算器版本号
	 * @return
	 */
	public abstract ICalculator createCalculator(FundsSourcesTypeEnum fundsSourcesTypeEnum,
			CalculatorVersionEnum calculatorVersionEnum);
}
