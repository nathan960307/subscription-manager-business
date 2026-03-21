package com.project.subscription.business.infrastructure.jwt;

import com.project.subscription.auth.global.exception.CustomException;
import com.project.subscription.auth.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Component
public class JwtProvider {

    // secret key 설정, 32자 이상의 충분히 긴 문자 사용, 짧으면 jjwt 가 거부
    @Value("${jwt.secret-key}")
    private  String secretKey;

    // access token 만료 시간 설정
    private final long accessTokenExpire = 1000 * 60 * 30; // 1초(1000ms) * 60 * 30 = 30분

    // refresh token 만료 시간 설정
    private final long refreshTokenExpire = 1000 * 60 * 60 * 24 * 7; // 7일


    // at 생성
    public String createAccessToken(Long userId, String role) {

        // 시각 객체 생성
        Date now = new Date(); // 현재 시간 객체 생성
        Date expireDate = new Date(now.getTime() + accessTokenExpire); // 만료 시각 객체 생성

        // jwt 객체 조립
        String accessToken = Jwts.builder()
                .setSubject(userId.toString())
                .claim("role",role)
                .claim("type","access")
                .setIssuedAt(now) // 토큰 생성 시각
                .setExpiration(expireDate) // 토큰 만료 시각
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return accessToken;
    }

    // rt 생성
    public String createRefreshToken(Long userId) {

        // 시각 객체 생성
        Date now = new Date(); // 현재 시각 객체 설정
        Date expireDate = new Date(now.getTime() + refreshTokenExpire); // 만료 시각 객체 설정

        // jwt 객체 조립
        String refreshToken = Jwts.builder()
                .setSubject(userId.toString())
                .claim("type","refresh")
                .setIssuedAt(now) // 토큰 생성 시각
                .setExpiration(expireDate) // 토큰 만료 시각
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return refreshToken;
    }

    // token 검증
    // 1. 변조 여부 검증
    // 2. 만료 여부 검증
    public boolean validateToken(String token) {

        try{
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token); // 서명 검증, exp 검사, jwt 형식 검사
            return true;

        }catch (Exception e){

            return false;
        }

    }

    // token을 cliams 객체로 변환
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    // token에서 user id 추출
    public Long getUserIdFromToken(String token) {

//        Jws<Claims> jws = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token);
//
//        Claims claims = jws.getBody();

        Claims claims = parseClaims(token);

        Long userId = Long.valueOf(claims.getSubject());

        return userId;
    }

    // token에서 남은 만료시간 추출
    public long getRemainingTime(String token) {

//        Jws<Claims> jws = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token);
//
//        Claims claims = jws.getBody();

        Claims claims = parseClaims(token);

        Date expireDate = claims.getExpiration(); // 만료 시각

        long now = System.currentTimeMillis(); // 현재 시각

        long remainingTime  = expireDate.getTime() - now;

        return Math.max(0, remainingTime);
    }

    // token에서 role 추출
    public String getRoleFromToken(String token) {

//        Jws<Claims> jws = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token);
//
//        Claims claims = jws.getBody();

        Claims claims = parseClaims(token);

        String role = claims.get("role",String.class);

        return role;
    }


}
