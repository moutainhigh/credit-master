package com.zdmoney.credit.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
// 加载配置文件
@Transactional
public class CalculatorTest {

	@Autowired
	IAfterLoanService afterLoanService;

	@Autowired
	IVLoanInfoService vLoanInfoServiceImpl;
	@Autowired
	IVLoanInfoDao vLoanInfoDao;

	@Test
	public void testQuery() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 准备查询参数 begin
		List<String> loanStates = new ArrayList<String>();
		loanStates.add(LoanStateEnum.正常.name());
		loanStates.add(LoanStateEnum.逾期.name());
		loanStates.add(LoanStateEnum.坏账.name());
		paramMap.put("idnum", "320323198611056831");

		List<VLoanInfo> list = vLoanInfoDao.queryLoanForCTS(paramMap);
		System.out.println(list);
	}

	// @Test
	public void getCalculatorInstance() {
		ICalculator calculatorInstace = null;
		try {
			VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(4836461L);
			System.out.println(vLoanInfo);
			long s = System.currentTimeMillis();
			calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
			calculatorInstace.getOnetimeRepaymentAmount(null, null, null);

			calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
			calculatorInstace.getOnetimeRepaymentAmount(null, null, null);

			System.out.println(System.currentTimeMillis() - s);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(calculatorInstace);
	}

}
