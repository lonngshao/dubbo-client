package org.lsn.dubbo.client.clients;

import java.util.Map;

import org.lsn.dubbo.client.DubboClient;
import org.lsn.dubbo.client.DubboInvoke;

@DubboClient("circleReference")
public interface DemoDubboClient {

	@DubboInvoke(methodName="getCircle", argTypes={"java.lang.String"})
	public Map<String , Object> getCircle(String circleKey);
}
