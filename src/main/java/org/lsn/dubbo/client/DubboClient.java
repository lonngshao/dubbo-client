package org.lsn.dubbo.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 
 * @author lsn
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DubboClient {
	
	/**
	 * 服务编码
	 * @return
	 */
	@AliasFor("value")
	String code() default "";
	
	@AliasFor("code")
	String value() default "";
	
	String qualifier() default "";
	
	Class<?> fallback() default void.class;
	
	boolean primary() default true;
}
