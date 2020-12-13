### spring security [个人demo代码](https://github.com/makai554892700/SpringSecurityDemo.git)
* 认证服务器搭建/oauth2-server
    * 新建SpringBoot项目并在build.gradle文件添加相关依赖
    
            ext {
                set('springCloudVersion', "Hoxton.SR4")
            }
            dependencies {
                implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
            }
            dependencyManagement {
                imports {
                    mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
                }
            }
            dependencies {
                api("org.springframework.boot:spring-boot-starter-web")
                // openfeign/hystrix 依赖
                implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
                implementation("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
                implementation("org.springframework.cloud:spring-cloud-starter-netflix-ribbon")
                implementation("org.springframework.cloud:spring-cloud-commons")
                // eureka 依赖
                implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
                //sql 依赖
                implementation("mysql:mysql-connector-java")
                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                // 一些个人工具 主要是oauth2相关数据库创建和oauth2用户相关数据库创建
                // 若担心安全问题可直接看源码复制出来使用,另需要自定义可自行修改相关类
                implementation("com.mys.www:oauth2-utils:0.0.2")
                implementation("com.mys.www:oauth2-user-utils:0.0.2")
                //oauth2 springSecurity 依赖
                implementation("org.springframework.cloud:spring-cloud-starter-oauth2")
            }
    
    * 至此开始编写关键代码，都在代码里，核心类为 AuthorizationServerConfig
    
* 资源服务器搭建/oauth2-client
    * 新建SpringBoot项目并在build.gradle文件添加相关依赖
    
            ext {
                set('springCloudVersion', "Hoxton.SR4")
            }
            dependencies {
                implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
            }
            dependencyManagement {
                imports {
                    mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
                }
            }
            dependencies {
                api("org.springframework.boot:spring-boot-starter-web")
                // 一些个人工具，主要用来操作pubKey文件 若担心安全问题可直接看源码复制出来使用
                implementation("com.mys.www:file-utils:0.0.1")
                //oauth2 springSecurity 依赖
                implementation("org.springframework.cloud:spring-cloud-starter-oauth2")
            }
    
    * 至此开始编写关键代码，都在代码里，核心类为 ResourceServerConfiguration
* 动态加载资源规则
    * 开启全局方法级别安全控制注解
        * @EnableGlobalMethodSecurity(prePostEnabled = true)
            * @PreAuthorize         方法执行之前进行安全控制
                * @PreAuthorize("hasRole('admin')")
            * @PreFilter            方法执行之前执行过滤器进行数据过滤
                * @PreFilter(filterTarget="ids", value="filterObject%2==0") 
            * @PostAuthorize        方法执行之后进行安全控制
                * @PostAuthorize("returnObject.name == authentication.name")
            * @PostFilter           方法执行之后执行过滤器进行数据过滤
                * @PostFilter("filterObject.name == authentication.name")

* 生成oauth2.0证书

        keytool -genkey -keystore test.jks -alias test -keyalg RSA
        keytool -list -rfc --keystore test.jks | openssl x509 -inform pem -pubkey

* 编写oauth2.0相关对象

        # 官方 oauth2 sql 文件
        https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql

* token相关

        # 获取code
        http://{{server}}:8100/oauth/authorize?response_type=code&client_id={{client_id}}&redirect_uri={{redirect_uri}}
        # 根据code获取token
        post(form) {{server}}:8100/oauth/token
            grant_type:authorization_code
            code:{{code}}
            redirect_uri:{{redirect_uri}}
            client_id:{{client_id}}
            client_secret:{{client_secret}}
        # 根据pass获取token
        post(form) {{server}}:8100/oauth/token
            grant_type:authorization_code
            code:{{code}}
            redirect_uri:{{redirect_uri}}
            client_id:{{client_id}}
            client_secret:{{client_secret}}
            username:{{username}}
            password:{{password}}
        # 检查token
        get {{server}}:8100/oauth/check_token?token={{access-token}}
        # 更新token
        post(form) {{server}}:8100/oauth/token
            grant_type:refresh_token 
            refresh_token:{{refresh-token}}
            client_id:{{client_id}}
            client_secret:{{client_secret}}
        # 调用接口
        get {{server}}:8101/api/info/1
            header: 
                Authorization: bearer {{token}} 
            






















