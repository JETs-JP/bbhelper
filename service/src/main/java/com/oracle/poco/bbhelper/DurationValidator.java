package com.oracle.poco.bbhelper;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZonedDateTime;

/**
 * Created by hhayakaw on 2017/06/08.
 */
@Component
public class DurationValidator implements ConstraintValidator<EffectiveDuration, Object> {

    private String fromdateField;

    private String todateField;

    @Override
    public void initialize(EffectiveDuration effectiveDuration) {
        this.fromdateField = effectiveDuration.fromdate();
        this.todateField = effectiveDuration.todate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        // fromdate and todate must be checked by @NotNull
        ZonedDateTime fromdate = (ZonedDateTime)beanWrapper.getPropertyValue(fromdateField);
        ZonedDateTime todate = (ZonedDateTime)beanWrapper.getPropertyValue(todateField);
        if (!fromdate.isBefore(todate)) {
            return false;
        }
        return true;
    }

}
