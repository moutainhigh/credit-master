package com.zdmoney.credit.common.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 枚举验证注解
 * @author 00236633
 *
 */
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistEnumValidator.class)
@Documented
public @interface ExistEnum {
    String message() default "{com.zdmoney.credit.common.validator.ExistEnum}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    //枚举类型
    Class<?> value();
    
    //验证的key值
    String keyName() default "";
}
