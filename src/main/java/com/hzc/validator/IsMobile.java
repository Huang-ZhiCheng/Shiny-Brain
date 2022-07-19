package com.hzc.validator;

import com.hzc.utils.ValidatorUtil;
import com.hzc.vo.IsMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * @author : hzc
 * @date: 2022/4/11 - 04 - 11 - 14:02
 * @Description: com.hzc.validator
 * @version: 1.0
 */
/*
 * @Description:验证手机格式的注解
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class})
public @interface IsMobile {
    boolean required()default true;
    String message() default "手机号码格式错误";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
