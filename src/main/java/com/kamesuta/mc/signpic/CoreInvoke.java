package com.kamesuta.mc.signpic;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * a method invoked by ASM
 * @author Kamesuta
 */
@Retention(SOURCE)
@Target({ TYPE, CONSTRUCTOR, METHOD, FIELD })
public @interface CoreInvoke {

}
