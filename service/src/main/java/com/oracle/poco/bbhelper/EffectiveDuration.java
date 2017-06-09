package com.oracle.poco.bbhelper;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hhayakaw on 2017/06/09.
 */
@Documented
@Constraint(validatedBy = DurationValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface EffectiveDuration {

    String message() default "{com.oracle.poco.bbhelper.EffectiveDuration.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fromdate();

    String todate();

}
