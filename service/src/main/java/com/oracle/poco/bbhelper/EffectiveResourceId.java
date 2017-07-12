package com.oracle.poco.bbhelper;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hhayakaw on 2017/07/12.
 */
@Documented
@Constraint(validatedBy = ResourceIdValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface EffectiveResourceId {

    String message() default "{com.oracle.poco.bbhelper.EffectiveResourceId.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
