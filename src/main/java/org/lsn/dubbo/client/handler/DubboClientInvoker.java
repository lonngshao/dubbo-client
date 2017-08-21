package org.lsn.dubbo.client.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.lsn.dubbo.client.DubboClient;
import org.lsn.dubbo.client.DubboInvoke;
import org.lsn.dubbo.client.beans.DubboServiceFactory;
import org.springframework.core.annotation.AnnotationUtils;

import com.alibaba.dubbo.rpc.service.GenericService;

/**
 * 
 * @author lsn
 *
 */
public class DubboClientInvoker {
	
	private final DubboClientInvocationHandlerFactory factory;
	
	private static DubboServiceFactory dubboServiceFactory;
	
	public DubboClientInvoker(DubboClientInvocationHandlerFactory factory , DubboServiceFactory dubboServiceFactory) {
		this.factory = factory;
		DubboClientInvoker.dubboServiceFactory = dubboServiceFactory;
	}

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> target) {
		for (Method method : target.getClass().getMethods()) {
			if (method.getDeclaringClass() == Object.class) {
				continue;
			}
		}
		InvocationHandler handler = factory.create(target);
		T proxy = (T) Proxy.newProxyInstance(target.getClassLoader(), new Class<?>[] { target, GenericService.class },
				handler);
		return proxy;
	}

	static class DubboInvocationHandler implements InvocationHandler {

		private final Class<?> target;

		DubboInvocationHandler(Class<?> target) {
			this.target = target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if ("equals".equals(method.getName())) {
				try {
					Object otherHandler = args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0])
							: null;
					return equals(otherHandler);
				} catch (IllegalArgumentException e) {
					return false;
				}
			} else if ("hashCode".equals(method.getName())) {
				return hashCode();
			} else if ("toString".equals(method.getName())) {
				return toString();
			}
			DubboClient dubboClient = AnnotationUtils.findAnnotation(target, DubboClient.class);
			String interName = dubboClient.value();
			DubboInvoke dubboInvoke = AnnotationUtils.getAnnotation(method, DubboInvoke.class);
			String[] argTypes = dubboInvoke.argTypes();
			
			return dubboServiceFactory.genericInvoke(DubboServiceFactory.APICONFIG.get(interName), dubboInvoke.methodName(), argTypes , args);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DubboInvocationHandler) {
				DubboInvocationHandler other = (DubboInvocationHandler) obj;
				return target.equals(other.target);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return target.hashCode();
		}

		@Override
		public String toString() {
			return target.toString();
		}
	}
}
