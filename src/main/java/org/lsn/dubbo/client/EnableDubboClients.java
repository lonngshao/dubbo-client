package org.lsn.dubbo.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lsn.dubbo.client.beans.DubboClientsRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DubboClientsRegistrar.class)
public @interface EnableDubboClients {

	@AliasFor("basePackages")
	String[] value() default {};

	@AliasFor("value")
	String[] basePackages() default {};
	
	Class<?>[] basePackageClasses() default {};

	Class<?>[] clients() default {};
}
