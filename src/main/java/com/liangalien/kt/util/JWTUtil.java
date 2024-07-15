package com.liangalien.kt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.liangalien.kt.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;


@Slf4j
@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(secret);
    }


    public UserDTO decode(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            UserDTO userDto = new UserDTO();
            userDto.setId(new BigInteger(decodedJWT.getClaim("id").asString()));
            userDto.setUsername(decodedJWT.getClaim("username").asString());
            userDto.setNickname(decodedJWT.getClaim("nickname").asString());
            userDto.setEmail(decodedJWT.getClaim("email").asString());
            userDto.setPhone(decodedJWT.getClaim("phone").asString());

            log.info("token={}", userDto);
            return userDto;
        } catch (Exception e) {
            log.error("解密TOKEN失败", e);
            return null;
        }
    }

    public String encode(UserDTO userDto) {
        try {
            // 构建JWT token
            String token = JWT.create()
                    .withClaim("id", userDto.getId().toString())
                    .withClaim("username", userDto.getUsername())
                    .withClaim("nickname", userDto.getNickname())
                    .withClaim("email", userDto.getEmail())
                    .withClaim("phone", userDto.getPhone())
                    .withExpiresAt(null)  // 设置token过期时间
                    .sign(algorithm);  // 使用指定算法进行签名

            return token;
        } catch (Exception e) {
            log.error("生成TOKEN失败", e);
            return null;
        }
    }

}
