package com.zdmoney.credit.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.zdmoney.credit.common.util.EnumUtil;

/**
 * 报表格式验证器
 * @author 00236633
 *
 */
public class ReportFormatValidator  implements ConstraintValidator<ReportFormat, String>{
	
	private ReportFormat reportFormat;

	@Override
	public void initialize(ReportFormat reportFormat) {
		this.reportFormat =reportFormat;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext arg1) {
		if(value==null || value.equals("html") || value.equals("xls") || value.equals("pdf") || value.equals("cvs") || value.equals("xlsx")){
			return true;
		}
		return false;
	}

}
