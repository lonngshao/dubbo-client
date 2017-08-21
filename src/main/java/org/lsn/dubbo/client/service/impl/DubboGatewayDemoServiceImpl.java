package org.lsn.dubbo.client.service.impl;

import org.lsn.dubbo.client.service.DubboGatewayDemoService;

public class DubboGatewayDemoServiceImpl implements DubboGatewayDemoService {

	@Override
	public String hello() {
		return "hello";
	}

}
