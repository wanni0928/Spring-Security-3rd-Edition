package com.packtpub.springsecurity.configuration;

import com.packtpub.springsecurity.core.userdetails.CalendarUserDetailsService;
import com.packtpub.springsecurity.service.UserDetailsServiceImpl;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.Cas30ProxyTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author Mick Knutson
 * @since chapter 10.00
 */
@Configuration
public class CasConfig {


    static{
        System.setProperty("cas.server", "https://localhost:9443/cas");
        System.setProperty("cas.calendar.service", "https://localhost:8443");
    }

    @Value("#{systemProperties['cas.server']}")
    private static String casServer;

    @Value("#{systemProperties['cas.calendar.service']}")
    private static String casService;

    @Value("#{systemProperties['cas.server']}/login")
    private String casServerLogin;

    @Value("#{systemProperties['cas.server']}/logout")
    private String casServerLogout;

    @Value("#{systemProperties['cas.calendar.service']}/login")
    private String calendarServiceLogin;


    @Bean
    public ServiceProperties serviceProperties(){
        return new ServiceProperties(){{
            setService(calendarServiceLogin);
        }};
    }

    @Autowired
    private CalendarUserDetailsService calendarUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private Cas20ProxyTicketValidator ticketValidator;

    @Autowired
    private UserDetailsByNameServiceWrapper userDetailsByNameServiceWrapper;


    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint()
    throws Exception
    {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setServiceProperties(serviceProperties());
        entryPoint.setLoginUrl(casServerLogin);
        entryPoint.afterPropertiesSet();
        return entryPoint;
    }

    @Bean
    public CasAuthenticationFilter casFilter() {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager);
        casAuthenticationFilter.setFilterProcessesUrl("/login");
        return casAuthenticationFilter;
    }



    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setTicketValidator(ticketValidator());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setKey("casJbcpCalendar");
        casAuthenticationProvider.setAuthenticationUserDetailsService(userDetailsByNameServiceWrapper);
        return casAuthenticationProvider;
    }


    @Bean
    public Cas30ProxyTicketValidator ticketValidator(){
        return new Cas30ProxyTicketValidator(casServer);
    }



    /**
     * Single point logout filter
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        return new SingleSignOutFilter();
    }

    /**
     * Request single point exit filter
     */
    @Bean
    public LogoutFilter casLogoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(
                casServerLogout,
                new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout");
        return logoutFilter;
    }

    /**
     *  For single point logout
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
        listener.setEnabled(true);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    @Bean
    public FilterRegistrationBean characterEncodingFilterRegistration() {
        FilterRegistrationBean registrationBean =
                new FilterRegistrationBean(characterEncodingFilter());
        registrationBean.setName("CharacterEncodingFilter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    private CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }


} // The End...
