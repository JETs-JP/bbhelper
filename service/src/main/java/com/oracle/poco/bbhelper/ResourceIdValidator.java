package com.oracle.poco.bbhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by hhayakaw on 2017/07/12.
 */
@Component
public class ResourceIdValidator implements ConstraintValidator<EffectiveResourceId, String> {

    /**
     * 会議室の一般情報を保持するキャッシュ
     */
    @Autowired
    private ResourceCache resourceCache;

    @Override
    public void initialize(EffectiveResourceId effectiveResourceId) {
        // do nothing
    }

    @Override
    public boolean isValid(String resourceId, ConstraintValidatorContext context) {
        if (resourceCache.getResource(resourceId) != null) {
            return true;
        }
        return false;
    }

}
