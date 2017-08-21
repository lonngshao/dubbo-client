package org.lsn.dubbo.client.handler;

import java.lang.reflect.InvocationHandler;

/**
 * 
 * @author lsn
 *
 */
public interface DubboClientInvocationHandlerFactory {

	InvocationHandler create(Class<?> target);

	interface MethodHandler {

		Object invoke(Object[] argv) throws Throwable;
	}

	static final class Default implements DubboClientInvocationHandlerFactory {

		@Override
		public InvocationHandler create(Class<?> target) {
			return new DubboClientInvoker.DubboInvocationHandler(target);
		}
	}

}
