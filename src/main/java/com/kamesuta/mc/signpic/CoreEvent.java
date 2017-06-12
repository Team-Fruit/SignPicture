package com.kamesuta.mc.signpic;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Instead of Forge Subscribe Event
 * @author Kamesuta
 */
@Target(value={METHOD})
@Retention(value=RUNTIME)
public @interface CoreEvent {
}
