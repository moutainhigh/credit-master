package com.zdmoney.credit.loan.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LoanFilesInfoValidator implements Validator {
	
	private static List<Validator> validators = new ArrayList<Validator>();
	static{
		validators.add(new LoanFilesInfoAddValidator());
		validators.add(new LoanFilesInfoUpdateValidator());
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		for(Validator validator : validators){
			if(validator.supports(target.getClass())  ){
				validator.validate(target, errors);
			}
		}
	}
}
