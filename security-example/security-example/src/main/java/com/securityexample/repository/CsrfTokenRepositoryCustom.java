package com.securityexample.repository;

import com.securityexample.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public class CsrfTokenRepositoryCustom implements CsrfTokenRepository {

    @Autowired
    TokenRepository tokenRepository;

    /**
     * 새로운 토큰을 생성할 때 호출
     */
    @Override
    public CsrfToken generateToken(HttpServletRequest request) {

        String uuid = UUID.randomUUID().toString();
        //시큐리티 기본 구현과 같이 헤더와 특성 이름을 유지
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", uuid);
    }

    /**
     * 생성된 토큰 저장
     */
    @Transactional
    @Override
    public void saveToken(CsrfToken csrfToken, HttpServletRequest request, HttpServletResponse response) {

        String identifier = request.getHeader("X-IDENTIFIER");
        Optional<Token> existToken = tokenRepository.findByIdentifier(identifier); //클라이언트 ID로 토큰 get

        if(existToken.isPresent()){
            existToken.get().updateToken(csrfToken.getToken()); //ID 존재 시 새로 생성된 토큰 update
        }else{
            Token token = Token.builder() //ID 없으면 토큰 save
                    .identifier(identifier)
                    .token(csrfToken.getToken())
                    .build();
            tokenRepository.save(token);
        }

    }

    /**
     * 토큰 세부 정보가 존재하면 로드
     */
    @Override
    public CsrfToken loadToken(HttpServletRequest request) {

        String identifier = request.getHeader("X-IDENTIFIER");
        Optional<Token> existToken = tokenRepository.findByIdentifier(identifier);

        return existToken.map(token -> new DefaultCsrfToken(
                "X-CSRF-TOKEN", "_csrf", token.getToken()))
                .orElse(null);
    }

}
