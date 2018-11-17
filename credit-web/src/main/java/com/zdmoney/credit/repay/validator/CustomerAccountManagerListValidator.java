package com.zdmoney.credit.repay.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.zdmoney.credit.common.util.ValidatorUtil;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerList;

public class CustomerAccountManagerListValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return VCustomerAccountManagerList.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		VCustomerAccountManagerList vCustomerAccountManagerList = (VCustomerAccountManagerList)target;
		String customerName = vCustomerAccountManagerList.getCustomerName();
		if(customerName==null){
			customerName="";
		}
		String customerIdNum = vCustomerAccountManagerList.getCustomerIdNum();
		if(customerIdNum==null){
			customerIdNum="";
		}
		if( customerName.trim().equals("") && customerIdNum.trim().equals("") ){
			errors.reject("customerNameAndIdNotBothNull", "客户名称和证件号码不能同时为空");
		}
	}

}
