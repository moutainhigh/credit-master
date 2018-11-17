package com.zdmoney.credit.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.zdmoney.credit.common.util.EnumUtil;

/**
 * 枚举验证器
 * @author 00236633
 *
 */
public class ExistEnumValidator  implements ConstraintValidator<ExistEnum, String>{
	
	private ExistEnum existEnum;

	@Override
	public void initialize(ExistEnum existEnum) {
		this.existEnum =existEnum;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext arg1) {
		if(value == null){
			return true;
		}
		try {
			Class clazz = existEnum.value();
			String keyName = existEnum.keyName();
			Enum enumTemp = EnumUtil.getEnum(clazz, value, keyName);
			if(enumTemp!=null){
				return true;
			}
		} catch (Exception e) {
		}

		return false;
	}

}
