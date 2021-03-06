# 服务注册与发现源码分析

## SpringCloud接口与Nacos实现

1. Nacos服务注册与发现使用同一接口NamingService，实现类为NacosNamingService。
2. SpringCloud服务注册接口为ServiceRegistry、Registration、AbstractAutoServiceRegistration，Nacos都提供了对应的适配类，最终调用NacosNamingService实现服务注册。
3. SpringCloud服务发现接口为DiscoveryClient，Nacos提供了对应的适配类，最终调用NacosNamingService实现服务发现。

## 服务注册与发现时序

1. AbstractAutoServiceRegistration监听WebServerInitializedEvent，服务启动后想注册中心注册服务。
2. NacosNamingService在第一次获取时构造，参考NacosServiceManager。
3. NacosNamingService初始化时构造HostReactor，在获取服务实例时会开启定时更新的任务。
4. HostReactor初始化时构造PushReceiver，可以接收远程UDP消息通知，及时感知到服务实例的更新。
5. NacosNamingService初始化时构造BeatReactor，负责服务实例的心跳。