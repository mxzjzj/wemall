# xmall B2B2C多用户商城系统

### 介绍
xmall是采用Java语言开发的多用户商城系统，使用Spring+JPA+Velocity+Ehcache作为基础开发架构，应用SpringSecurity框架管理系统权限，
结合URL重写技术静态化商城前台页面。结合多年的电商开发经验及主流电商的功能特点，注重系统易用性、功能性、扩展性，充分考虑大数据、大并发、系统安全、SEO功能，
使得系统设计更加完善，更加高效稳定。xmall系统融合当前主流B2B2C、C2C购物平台主要功能，同时推出自身特色功能,如：商城广告、商城活动、积分商城等。

### 截图预览
![输入图片说明](https://images.gitee.com/uploads/images/2019/0522/160731_9d94c975_779947.png "首页.png")
![输入图片说明](https://images.gitee.com/uploads/images/2019/0522/160744_b86051e7_779947.png "购买.png")
![输入图片说明](https://images.gitee.com/uploads/images/2019/0522/160800_a609bc8e_779947.png "后台.png")
![输入图片说明](https://images.gitee.com/uploads/images/2019/0522/160904_c9294752_779947.png "移动端.png")


### 技术构架
+ 开发语言： Java
+ 数 据 库 ： MySQL、SQLServer、Oracle
+ 数据库持久层：阿里巴巴druid
+ 核心框架： Spring、SpringMVC、Hibernate
+ 缓存技术： Memcache
+ 全文检索： Lucene、HibernateSearch
+ 模板视图： Velocity、HTML、CSS、JQuery
+ 权限控制： SpringSecurity

### 技术特点
1. 基于Java语言开发，融合OOP编程思想，安全性高、性能优越、扩展方便；
2. 系统内部结构耦合性低，分模块开发，便于多服务器拆分部署，提高系统性能；
3. 多层安全控制机制，采用MVC开发模式，使用SpringSecurity管理权限，7层过滤器彻底解决安全问题；
4. 应用Memcache缓存框架，数据缓存和资源缓存并存机制，提高系统数据加载效率，支持分布式缓存管理；
5. Lucene全文检索原生API深度开发，完美支持电商平台，支持自定义词库管理，使得商城检索功能更加完善；
6. 内置自定义Velocity缓存标签，补充完善开源缓存框架不足，解决各类资源缓存，提升系统性能
7. 电商平台配属体系强大，完备的产品体系包含手机商城、微信商城、桌面助手等多种外相关电商统；


### 安装教程

1. 数据库配置：修改mysql数据库账号、密码和端口，本项目默认账号：root  密码：root123   端口：3306，若需修改可在resource/jdbc.properties 中修改
2. 导入源码：将项目源码导入eclipse中，设置项目源码编码为UTF-8
3. 启动tomcat
+ 商城前台地址：http://localhost:8080/
+ Wap前台地址: http://localhost:8080/wap/index.htm
+ 商城后台地址：http://localhost:8080/admin/index.htm


### 使用说明

1. 商城前台地址：http://localhost:8080/
2. Wap前台地址: http://localhost:8080/wap/index.htm
3. 商城后台地址：http://localhost:8080/admin/index.htm
4. 需要在后台执行以下操作：工具》全文索引》更新索引。才能，在前台搜索中，搜索出相应的产品

### 系统目录结构

+ Base	提供系统最基本能力支持，数据库表的持久化对象，各个表基本操作的接口以及文件上传功能。
+ Core	整个系统的核心组件，在此组件当中包含了缓存、安全、使用Spring通过orm组件来代理hibernate做数据库操作、构造统一的DAO、QueryService等等。
+ Buyer	针对买家管理实现用户中心、消息、收藏夹、购买订单等业务。
+ Admin	基于管理员的能力对系统进行管理，此组件主要包含管理员所能操作的业务。
+ Login-plug	系统中可使用其他登录方式，如QQ、新浪微博等，都在此组件进行接入。
+ Lucene	提供快速对系统、店铺、产品进行全文索引功能。
+ Pay	系统提供多种订单支持方式，其中有支付宝、网银在线、Paypal、财付通。
+ Seller	卖家业务管理统一在这组件内实现，主要有：宝贝管理、订单管理。
+ Timer	实现系统中的一些定时任务，统计任务等后台运行机制。
+ Uc	支持ucenter整合，实现用户的一站式注册、登录、退出以及社区其他数据的交互。
+ View	实现浏览查看功能，主要针对查询业务独立封装出来组件便于以后优化。


### 技术疑问交流

+ QQ:523019539 <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=74b7d17f4761bfffab58ea9130896426a89bcda8d5af1d4cda8e3227e4543bfe"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="xmall 技术交流群" title="xmall 技术交流群"></a>,可获取各项目详细图文文档、疑问解答! 
+ 完整版完全免费，完全免费，加入这个群可以学到前后端最新技术知识，技术交流，职业分享，工作推荐！！
