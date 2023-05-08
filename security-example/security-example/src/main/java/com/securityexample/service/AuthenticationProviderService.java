package com.securityexample.service;

import com.securityexample.config.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationProviderService implements AuthenticationProvider {

    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SCryptPasswordEncoder sCryptPasswordEncoder;

    /**
     * 실제로 인증을 수행하는 메소드로, Authentication 객체를 받아 인증을 진행하고
     * 유효한 인증 결과인 Authentication 객체를 반환
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetailsCustom loginUser = userDetailsServiceCustom.loadUserByUsername(username); //사용자 검색

        switch (loginUser.getUser().getAlgorithm()){ //java 14 부터 도입된 switch 문..
            case BCRYPT -> { //해싱 알고리즘에 따라 비밀번호 검증
                return checkPassword(loginUser, password, bCryptPasswordEncoder);
            }
            case SCRYPT -> {
                return checkPassword(loginUser, password, sCryptPasswordEncoder);
            }
        }

        throw new BadCredentialsException("password do not match...");
    }

    /**
     * 현재 AuthenticationProvider 가 Authentication 객체로 제공된 형식을 지원하는 true 를 반환
     * 해당 코드의 경우 UsernamePasswordAuthenticationToken 또는 하위 클래스인 경우만 true
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Authentication checkPassword(UserDetailsCustom loginUser, String rawPassword, PasswordEncoder encoder){
        if(encoder.matches(rawPassword, loginUser.getPassword())){
            /**
             * UsernamePasswordAuthenticationToken - Authentication 의 구현체로
             * 기본적인 사용자명과 비밀번호를 이용한 인증에 사용
             * 최종적으로 사용자 정보를 담아서 Authentication 객체로 반환
             */
            return new UsernamePasswordAuthenticationToken(
                    loginUser.getUsername(),
                    loginUser.getPassword(),
                    loginUser.getAuthorities()
            );
        }else{
            throw new BadCredentialsException("password do not match...");
        }
    }

}
