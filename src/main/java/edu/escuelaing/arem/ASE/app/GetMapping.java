package edu.escuelaing.arem.ASE.app;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface GetMapping {

    String value();

}
