package org.lsn.dubbo.client.beans;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.lsn.dubbo.client.DubboContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.GenericService;

public class DubboServiceFactory implements InitializingBean {

	public static final Map<String, Map<String, Object>> APICONFIG = new HashMap<>();

	public static final Map<String, String> APPINFO = new HashMap<>();

	private static final String DUBBO_CONFIG_NODE = "dubbo-config";

	private static final String DUBBO_APP_NODE = "dubbo:application";

	private static final String DUBBO_REG_NODE = "dubbo:registry";

	private static final String DUBBO_REF_NODE = "dubbo:reference";

	private Resource apiConfigLocation;

	private Resource dubboConfigLocation;

	private ApplicationConfig application;

	private RegistryConfig registry;

	public Object genericInvoke(Map<String, Object> apiInfo, String methodName, String[] argTypes,
			Object[] parameters) {

		ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
		reference.setApplication(application);
		reference.setRegistry(registry);
		reference.setInterface((String) apiInfo.get("interface")); // 接口名
		reference.setGeneric(true);
		reference.setCheck((Boolean) apiInfo.get("check"));
		reference.setVersion((String) apiInfo.get("version"));
		Map<String, Object> appInfo = DubboContext.APPINFO.get();
		RpcContext.getContext().setAttachment(DubboContext.APP_ID, (String) appInfo.get(DubboContext.APP_ID));
		RpcContext.getContext().setAttachment(DubboContext.APP_SECRET, (String) appInfo.get(DubboContext.APP_SECRET));
		ReferenceConfigCache cache = ReferenceConfigCache.getCache();
		GenericService genericService = cache.get(reference);
		return genericService.$invoke(methodName, argTypes, parameters);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		try (InputStream is = apiConfigLocation.getInputStream()) {
			MutablePropertySources mpss = new MutablePropertySources();
			PropertiesPropertySource pps = new PropertiesPropertySource(DUBBO_CONFIG_NODE,
					PropertiesLoaderUtils.loadProperties(dubboConfigLocation));
			mpss.addFirst(pps);
			PropertySourcesPropertyResolver pspr = new PropertySourcesPropertyResolver(mpss);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			NodeList appNodes = doc.getElementsByTagName(DUBBO_APP_NODE);
			NodeList regNodes = doc.getElementsByTagName(DUBBO_REG_NODE);
			String appName = appNodes.item(0).getAttributes().getNamedItem("name").getTextContent();
			appName = pspr.resolvePlaceholders(appName);
			APPINFO.put("appName", appName);
			String regInfo = regNodes.item(0).getAttributes().getNamedItem("address").getTextContent();
			regInfo = pspr.resolvePlaceholders(regInfo);
			APPINFO.put("regInfo", regInfo);
			NodeList nodeList = doc.getElementsByTagName(DUBBO_REF_NODE);
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap namedNodes = nodeList.item(i).getAttributes();
				Assert.notNull(namedNodes.getNamedItem("id"), DUBBO_REF_NODE + " element must set 'id' attribute");
				String id = namedNodes.getNamedItem("id").getTextContent();
				Assert.notNull(namedNodes.getNamedItem("interface"),
						DUBBO_REF_NODE + " element must set 'interface' attribute");
				String inter = namedNodes.getNamedItem("interface").getTextContent();

				String check = namedNodes.getNamedItem("check") == null ? "true"
						: namedNodes.getNamedItem("check").getTextContent();
				String version = namedNodes.getNamedItem("version") == null ? ""
						: namedNodes.getNamedItem("version").getTextContent();
				Map<String, Object> attrMap = new HashMap<>();
				attrMap.put("interface", inter);
				attrMap.put("check", StringUtils.isBlank(check) ? true : Boolean.valueOf(check));
				attrMap.put("version", version);
				APICONFIG.put(id, attrMap);
			}
			ApplicationConfig applicationConfig = new ApplicationConfig();
			applicationConfig.setName(APPINFO.get("appName"));
			RegistryConfig registryConfig = new RegistryConfig();
			registryConfig.setAddress(APPINFO.get("regInfo"));
			this.application = applicationConfig;
			this.registry = registryConfig;

		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public Resource getApiConfigLocation() {
		return apiConfigLocation;
	}

	public void setApiConfigLocation(Resource apiConfigLocation) {
		this.apiConfigLocation = apiConfigLocation;
	}

	public Resource getDubboConfigLocation() {
		return dubboConfigLocation;
	}

	public void setDubboConfigLocation(Resource dubboConfigLocation) {
		this.dubboConfigLocation = dubboConfigLocation;
	}

}
