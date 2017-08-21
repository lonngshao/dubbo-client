package org.lsn.dubbo.client.beans;

import org.lsn.dubbo.client.handler.DubboClientInvocationHandlerFactory;
import org.lsn.dubbo.client.handler.DubboClientInvoker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author lsn
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DubboClientFactoryBean implements FactoryBean<Object>, InitializingBean , ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	private DubboServiceFactory dubboServiceFactory;

	private Class<?> type;

	private String code;

	private Class<?> fallback = void.class;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(this.code, "usage of @DubboClient annotation must set 'code' attribute");
	}

	@Override
	public Object getObject() throws Exception {
		dubboServiceFactory = applicationContext.getBean(DubboServiceFactory.class);
		Assert.notNull(dubboServiceFactory, "Instance of class DubboServiceFactory can't be null , please register the bean into spring IOC context");
		return new DubboClientInvoker(new DubboClientInvocationHandlerFactory.Default() , dubboServiceFactory).newInstance(type);
	}

	@Override
	public Class<?> getObjectType() {
		return this.type;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Class<?> getFallback() {
		return fallback;
	}

	public void setFallback(Class<?> fallback) {
		this.fallback = fallback;
	}

	public DubboServiceFactory getDubboServiceFactory() {
		return dubboServiceFactory;
	}

	public void setDubboServiceFactory(DubboServiceFactory dubboServiceFactory) {
		this.dubboServiceFactory = dubboServiceFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
