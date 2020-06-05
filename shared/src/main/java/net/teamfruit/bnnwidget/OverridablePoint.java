package net.teamfruit.bnnwidget;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * オーバーライド用メソッドです
 * <p>
 * オーバーライドした際のsuperメソッドを呼ぶ必要はありません。
 *
 * @author TeamFruit
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface OverridablePoint {
}
