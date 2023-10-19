# jz-log-starter
一个自定义starter，用来记录用户操作日志

该starter整合了rabbitMQ，redis，JWT以及定时任务，通过aop与自定义注解的方式记录用户操作日志，并将日志信息与消息信息通过jpa框架存储进入MySQL数据库，
使用jpa框架是因为只需要简单的配置，jpa框架即可在我们没有对应的表时，创建表。

用户可配置项：
  rabbitMQ的队列，交换机，路由键，请求头中存储的uuid的key，JWT token串中存储用户id的key，JWT token串的签名。
  以上配置都有默认值。

注意事项：
  如果自己配置了队列，需要把listen包下监听的队列名改为自己配置的队列名，不然程序无法起效。

配置文件模板：
  spring:
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://${MYSQL_HOST:你的数据库ip}:${MYSQL_PORT:你的数据库端口}/你的数据库名?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
      username: ${MYSQL_USERNAME:数据库账号}
      password: ${MYSQL_PASSWORD:数据库密码}
    redis:
      host: ${REDIS_HOST:你的redis的ip}
      port: ${REDIS_PORT:你的redis的端口}
    rabbitmq:
      host: ${RABBITMQ_HOST:你的rabbitMQ的ip}
      port: ${RABBITMQ_PORT:你的rabbitMQ的端口}
      username: ${RABBITMQ_USERNAME:你的rabbitMQ用户名}
      password: ${RABBITMQ_PASSWORD:你的rabbitMQ密码}
      virtual-host: ${RABBITMQ_VIRTUAL_HOST:你的rabbitMQ虚拟主机}
      publisher-returns: true
      publisher-confirm-type: correlated
      listener:
        direct:
          acknowledge-mode: manual
        simple:
          acknowledge-mode: manual
    jpa:
      generate-ddl: true
      database: mysql
  jz:
    log:
      queue: jz-log-queue
      exchange: jz-log-exchange
      routingKey: jz-log-routingKey
      uuidKey: token
      jwtConfig: 
    	  signature: jz-log-starter
	      tokenKey: userId

