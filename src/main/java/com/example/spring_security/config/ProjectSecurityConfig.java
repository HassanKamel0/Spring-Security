package com.example.spring_security.config;

import com.example.spring_security.exceptionHandling.CustomerAccessDeniedHandler;
import com.example.spring_security.exceptionHandling.CustomerBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> {((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated();});
        http.sessionManagement(smc->smc//.sessionFixation(sfc->sfc.newSession())
                        .invalidSessionUrl("/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
                .requiresChannel(rcc->rcc.anyRequest().requiresInsecure()) //Only HTTP
                .csrf(csrfConfig->csrfConfig.disable())
                .authorizeHttpRequests((requests) -> {requests
                    .requestMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated()
                    .requestMatchers("/notices","/contact","/register","/error","/invalidSession").permitAll();
        });
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(hbc->hbc.authenticationEntryPoint(new CustomerBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc->ehc.accessDeniedHandler(new CustomerAccessDeniedHandler()));
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
////        UserDetails user=User.withUsername("user").password("{noop}Eazy_User@12345").authorities("read").build();
////        UserDetails admin=User.withUsername("admin").password("{bcrypt}$2a$12$yKIGt1lwEij7YLHiclJWruN79jDmBOqqs2tlZz4QyvO3VnzbPgODi").authorities("admin").build();
////        return new InMemoryUserDetailsManager(user, admin);
//
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
