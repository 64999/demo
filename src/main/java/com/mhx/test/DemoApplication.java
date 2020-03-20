package com.mhx.test;

import com.mhx.test.Config.VerifyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    //注入验证码
    @Bean
    public ServletRegistrationBean indexServletRegistion(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new VerifyConfig());
        registrationBean.addUrlMappings("/getVerifyCode");
        return registrationBean;

    }

}
