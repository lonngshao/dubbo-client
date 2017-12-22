# dubbo-client
java dubbo服务消费端代理，基于dubbo泛化机制，使用自定义注解+接口方式调用dubbo服务
用于简化dubbo服务消费客户端开发及对api包的依赖解耦，其中并未实现如消费端服务调用异常的降级处理等，有兴趣的使用者可以自行实现这部分

#示例：

## 假设服务提供者提供了一个服务：</br>

public interface TestService{ </br>
    public String getByName(String name); </br>
} </br>

  则引入该jar包后，对应的消费客户端开发写法为：</br>
//@DubboClient注解的属性值为：客户端配置文件service-api.xml中配置的引用bean的id </br>
//注意：该service-api.xml配置文件与dubbo系统无关，是自行解析处理的，只不过为了迎合传统消费端配置习惯，沿用了传统消费端基于dubbo命名空间的配置方式 </br>
@DubboClient("testService")</br>
public interface TestClient{</br>
	//method属性值为实际调用的服务端方法名，argTypes为实际服务端方法的入参类型，当该类型为复合类型时，可全部用Map方式传递</br>
	@DubboInvoke(method="getByName" , argTypes={"java.lang.String"})</br>
	public String getByName(String name);</br>
}</br>

#注意：使用该jar包的客户端，需要先进行配置，示例配置在该jar包的META-INF/spring/root-context.xml文件中
