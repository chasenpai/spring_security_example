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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetailsCustom loginUser = userDetailsServiceCustom.loadUserByUsername(username); //로그인 시도한 사용자 검색

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

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Authentication checkPassword(UserDetailsCustom loginUser, String rawPassword, PasswordEncoder encoder){
        if(encoder.matches(rawPassword, loginUser.getPassword())){
            return new UsernamePasswordAuthenticationToken( //최종적으로 사용자 정보를 담아서 Authentication 객체로 반환
                    loginUser.getUsername(),
                    loginUser.getPassword(),
                    loginUser.getAuthorities()
            );
        }else{
            throw new BadCredentialsException("password do not match...");
        }
    }

}
