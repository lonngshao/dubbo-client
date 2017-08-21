package org.lsn.dubbo.client.resource;

import java.util.Map;

import org.lsn.dubbo.client.clients.DemoDubboClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("dubbo")
public class DemoDubboClientResource {

	@Autowired
	DemoDubboClient demoDubboClient;

	@RequestMapping(value = "demo/{key}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> test(@PathVariable("key") String circleKey) {
		return demoDubboClient.getCircle(circleKey);
	}
}
