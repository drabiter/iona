package com.drabiter.iona.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Inherited
@Target({ TYPE })
@Retention(RUNTIME)
public @interface MentalModel {

    String endpoint() default "";

}
