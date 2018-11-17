package com.zdmoney.credit.common.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 报表格式验证
 * @author 00236633
 *
 */
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReportFormatValidator.class)
@Documented
public @interface ReportFormat {
    String message() default "{com.zdmoney.credit.common.validator.ReportFormat}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
