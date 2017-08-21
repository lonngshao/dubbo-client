package org.lsn.dubbo.client;

import java.util.Map;

public abstract class DubboContext {

	public static final String APP_ID = "appId";

	public static final String APP_SECRET = "appSecret";

	public static final ThreadLocal<Map<String, Object>> APPINFO = new ThreadLocal<>();
}
