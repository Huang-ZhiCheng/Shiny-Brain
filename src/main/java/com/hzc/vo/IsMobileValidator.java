package com.hzc.vo;

import com.hzc.utils.ValidatorUtil;
import com.hzc.validator.IsMobile;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author : hzc
 * @date: 2022/4/11 - 04 - 11 - 14:05
 * @Description: com.hzc.vo
 * @version: 1.0
 */
/*
 * @Description: 手机格式验证类
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required = false;
    @Override
    public void initialize(IsMobile constrainAnnotation) {
        required = constrainAnnotation.required();
    }
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            //必填
            return ValidatorUtil.isMobile(s);
        }else {
            //非必填
            if(StringUtils.isEmpty(s)){
                return true;
            }else {
                //填了就校验它
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
