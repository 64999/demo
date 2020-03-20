package com.mhx.test.springSecurity.Config;

import com.mhx.test.springSecurity.Handler.CustomAuthenticationFailureHandler;
import com.mhx.test.springSecurity.Handler.CustomAuthenticationSuccessHandler;
import com.mhx.test.springSecurity.Handler.CustomExpiredSessionStrategy;
import com.mhx.test.springSecurity.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * Spring security 完成了异常处理，总结一下流程：
 *
 * –> AbstractAuthenticationProcessingFilter.doFilter()
 *
 * –> AbstractAuthenticationProcessingFilter.unsuccessfulAuthentication()
 *
 * –> SimpleUrlAuthenticationFailureHandler.onAuthenticationFailure()
 *
 * –> SimpleUrlAuthenticationFailureHandler.saveException()
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;


    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //若token表不存在，使用下面语句可初始化表，
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }


    /**
     * 注入自定义PermissionEvaluator
     */
    @Bean
    public DefaultWebSecurityExpressionHandler wesHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //密码加密
        //.passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(customAuthenticationProvider);
//        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence charSequence) {
//                return charSequence.toString();
//            }
//
//            @Override
//            public boolean matches(CharSequence charSequence, String s) {
//                return s.equals(charSequence.toString());
//            }
//        });
    }

    @Override
    protected void configure(HttpSecurity security) throws  Exception{
        security
                .authorizeRequests()
                //若有允许匿名的url，填在下面
                //.antMatchers().authenticated()
                .antMatchers("/login","/login/invalid","/getVerifyCode").permitAll()
                .anyRequest().authenticated()
                .and()
                //设置登陆页
                .formLogin().loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .authenticationDetailsSource(authenticationDetailsSource)

                // 自定义登陆用户名和密码参数，默认为username和password
//                .usernameParameter("username")
//                .passwordParameter("password")
                .and()
                //.addFilterBefore(new VerifyFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout().permitAll()
                // 自动登录
                .and().rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    //token有效时间
                    .tokenValiditySeconds(60)
                    //.userDetailsService(userDetailsService);
                .and().sessionManagement()
                        .invalidSessionUrl("/login/invalid")
                        //指定最大登陆数
                        .maximumSessions(1)
                        //当达到最大值时，是否保留已经登陆的用户
                        .maxSessionsPreventsLogin(true)
                        //当达到最大值时，就用户剔除后的操作
                        .expiredSessionStrategy(new CustomExpiredSessionStrategy())
                        // 当达到最大值时，旧用户被踢出后的操作
                        .expiredSessionStrategy(new CustomExpiredSessionStrategy())
                        .sessionRegistry(sessionRegistry());


        // 关闭CSRF跨域
        security.csrf().disable();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }
}
