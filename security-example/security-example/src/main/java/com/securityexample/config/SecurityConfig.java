package com.securityexample.config;

import com.securityexample.service.AuthenticationProviderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    //시큐리티 5.7 부터 WebSecurityConfigurerAdapter Deprecated, Bean 등록으로 구성 해야함

    /**
     * ProviderManager - AuthenticationManager 의 구현체로, AuthenticationProvider 를 사용하여 사용자 인증 처리
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProviderService service) throws Exception {
        return new ProviderManager(Collections.singletonList(service));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //http.httpBasic();
        //http.formLogin().defaultSuccessUrl("/");

//        http.authorizeHttpRequests()
//                .requestMatchers("/authority/read").hasAuthority("read")
//                .requestMatchers("/authority/write").hasAnyAuthority("read", "write")
//                .requestMatchers("/role/admin").hasRole("ADMIN")
//                .anyRequest().authenticated();
//        http.authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET, "/item").permitAll()
//                .requestMatchers(HttpMethod.POST, "/item").authenticated()
//                .requestMatchers(HttpMethod.DELETE, "/item").denyAll();
        http.authorizeHttpRequests()
                .requestMatchers("/v1/item/{param}").permitAll() //파라미터가 포함된 요청만 허가
                .requestMatchers("/v2/item/{param:^[0-9]*$}").permitAll() //파라미터에 숫자가 포함된 요청만 허가
                .anyRequest().denyAll();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        //BCryptPasswordEncoder - bcrypt 강력 해싱 함수로 암호를 인코딩
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SCryptPasswordEncoder sCryptPasswordEncoder(){
        return new SCryptPasswordEncoder(16384, 9, 1, 64, 16);
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        String encodedPwd = new BCryptPasswordEncoder().encode("1234");
//        List<UserDetails> users = List.of( User.withUsername("junwvwv")
//                .password(encodedPwd)
//                .authorities("read")
//                .build());
//
//        return new InMemoryUserDetailsService(users);
//    }

}
