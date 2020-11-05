package com.buildmanage.commandsexecute;

import com.buildmanage.config.BMPermissions;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BMCommandIndex {

    String[] value();
    boolean op();
    String help();
}
