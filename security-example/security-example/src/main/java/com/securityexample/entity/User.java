package com.securityexample.entity;

import com.securityexample.model.EncryptAlgorithm;
import com.securityexample.model.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EncryptAlgorithm algorithm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Authority> authorities;

    @Builder
    public User(String username, String password, EncryptAlgorithm algorithm, Role role, List<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.algorithm = algorithm;
        this.role = role;
        this.authorities = authorities;
    }

    public void setAuthorities(List<Authority> authorities){
        this.authorities = authorities;
    }

}
