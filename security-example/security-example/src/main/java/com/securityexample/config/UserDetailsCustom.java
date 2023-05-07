package com.securityexample.config;

import com.securityexample.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class UserDetailsCustom implements UserDetails {

    private final User user;

    /**
     * 사용자 아이디
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 사용자 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자 권한 정보
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority(a.getName())) //DB 에서 각 사용자에 대해 부여된 권한 이름을 매핑
                .collect(Collectors.toList());
    }

    /**
     * 계정 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 인증 정보(비밀번호) 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
