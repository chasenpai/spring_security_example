package com.securityexample.service;

import com.securityexample.config.UserDetailsCustom;
import com.securityexample.entity.User;
import com.securityexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceCustom implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsCustom loadUserByUsername(String username) throws UsernameNotFoundException {
        //함수형 프로그래밍 ..
        Supplier<UsernameNotFoundException> supplier = () -> //예외 처리를 위한 Supplier
                new UsernameNotFoundException("user name not found...");

        User user = userRepository.findByUsername(username)
                .orElseThrow(supplier); //해당 user 가 존재하면 User 객체 반환, 아니면 Supplier 가 생성한 예외 던짐

        return new UserDetailsCustom(user);
    }

}
