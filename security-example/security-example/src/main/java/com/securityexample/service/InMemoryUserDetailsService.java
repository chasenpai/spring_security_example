package com.securityexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class InMemoryUserDetailsService implements UserDetailsService {

    private final List<UserDetails> users; //메모리 내 사용자 정보를 UserDetails 로 관리

    /**
     * UserDetailService 를 이용해 사용자 세부 정보를 로드
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

}
