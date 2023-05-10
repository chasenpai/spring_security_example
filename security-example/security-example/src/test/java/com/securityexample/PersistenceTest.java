package com.securityexample;

import com.securityexample.entity.Authority;
import com.securityexample.entity.User;
import com.securityexample.model.EncryptAlgorithm;
import com.securityexample.model.Role;
import com.securityexample.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class PersistenceTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Commit
    void saveUser(){

        User user = User.builder()
                .username("manager1")
                .password("$2a$10$rsnsLbZyV87kM9M4XA9nWuBKOBIiXSnVq9fEuC90sBzM5Kp8Cf2ZG")
                .algorithm(EncryptAlgorithm.BCRYPT)
                .role(Role.MANAGER)
                .build();

        List<Authority> authorities = new ArrayList<>();
        Authority authority1 = Authority.builder()
                .user(user)
                .name("read")
                .build();
        Authority authority2 = Authority.builder()
                .user(user)
                .name("read")
                .build();
        authorities.add(authority1);
        authorities.add(authority2);

        user.setAuthorities(authorities);

        userRepository.save(user);

    }

}
