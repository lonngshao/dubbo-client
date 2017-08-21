package org.lsn.dubbo.client.clients;

import java.util.Map;

import org.lsn.dubbo.client.DubboClient;
import org.lsn.dubbo.client.DubboInvoke;

@DubboClient("demoApi")
public interface DemoApiClient {

    @DubboInvoke(methodName = "getService" , argTypes={})
    public String getService();

    @DubboInvoke(methodName = "selectUserById"  , argTypes = {"java.lang.Integer"})
    public Map<String , Object> selectUserById(Integer userId);

}
