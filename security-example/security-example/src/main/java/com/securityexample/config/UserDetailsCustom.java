package com.securityexample.config;

import com.securityexample.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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

        //각 사용자에 대해 부여된 권한을 매핑
        Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority(a.getName())).collect(Collectors.toSet());

        //각 사용자에 대한 역할을 부여
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return authorities;
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
