# 功能

1. nacos服务注册与发现
2. spring-cloud-loadbalance负载均衡

# 测试方式

环境：本地安装并启动nacos release版本2.0.0-bugfix，下载地址https://github.com/alibaba/nacos/releases 。

方法一：启动多个service-registration，通过命令行指定不同端口。启动service-discovery，访问localhost:8080/echo/test，会在多个service-registration之间轮询。

方法二：运行service-discovery下的单元测试ServiceDiscoveryTest，它启动两个my-service的服务实例，并通过负债均衡调用。

# 服务注册实现方式

1. 使用注解@EnableDiscoveryClient，启用服务注册功能。
2. application.yml中配置spring.application.name和spring.cloud.nacos.discovery.server-addr，向nacos上报服务实例。

# 服务发现实现方式

1. 使用注解@EnableDiscoveryClient，启用服务发现功能。
2. application.yml中配置spring.application.name和spring.cloud.nacos.discovery.server-addr，从nacos获取服务实例。
3. WebClientConfig中通过@LoadBalanced配置WebClient的负载均衡，向my-service发起请求时会在多个服务实例之间轮询。

# 负载均衡源码分析

[负载均衡源码分析](source_code_lb.md)

# 服务注册与发现源码分析

[服务注册与发现源码分析](source_code_nacos_discovery.md)