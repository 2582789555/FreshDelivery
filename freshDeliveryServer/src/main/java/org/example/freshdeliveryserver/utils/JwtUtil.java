package org.example.freshdeliveryserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.freshdeliveryserver.entity.CustomUserDetails;
import org.example.freshdeliveryserver.entity.Role;
import org.example.freshdeliveryserver.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
public class JwtUtil {
    @Value("${kansha.salt}")
    private String secrete;
    @Resource
    private ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    public String createJwt(User user) throws JsonProcessingException {
        Date issDate = new Date();
        Date expireDate = new Date(issDate.getTime()+1000*60*60*2);
        Map<String,Object> headerClaims = new HashMap<>();
        headerClaims.put("alg","HS256");
        headerClaims.put("typ","JWT");
        return JWT.create().withHeader(headerClaims)
                .withSubject(user.getUserId().toString())
                .withClaim("userId",user.getUserId())
                .withClaim("username",user.getUsername())
                .withClaim("email",user.getEmail())
                .withClaim("role",user.getRole().name())
                .withClaim("phone",user.getPhone())
                .withClaim("isVerified",user.getIsVerified())
                .withIssuer("freshdelivery")
                .withIssuedAt(issDate)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(secrete));
    }

    // 校验Token是否有效
    public void validateToken(String token){
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secrete)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            logger.info("JWT 验证成功！");
        }catch (SignatureVerificationException e) {
            logger.error("JWT 签名无效：" + e.getMessage());
            throw new BadCredentialsException("无效的 JWT 签名");
        } catch (TokenExpiredException e) {
            logger.warn("JWT 已过期：" + e.getMessage());
            throw new CredentialsExpiredException("JWT 令牌已过期");
        } catch (JWTVerificationException e) {
            logger.error("JWT 验证失败：" + e.getMessage());
            throw new AuthenticationServiceException("JWT 令牌验证失败");
        } catch (Exception e) {
            logger.error("未知错误：" + e.getMessage());
            throw new AuthenticationServiceException("JWT 认证失败");
        }
    }
    // 从Token中获取Authentication对象
    public Authentication getAuthentication(String token){
        try{
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secrete)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String userId = decodedJWT.getClaim("userId").asString();
            String username = decodedJWT.getClaim("username").asString();
            String email = decodedJWT.getClaim("email").asString();
            String role = decodedJWT.getClaim("role").asString();
            String phone = decodedJWT.getClaim("phone").asString();
            Boolean isVerified = decodedJWT.getClaim("isVerified").asBoolean();

            User user = new User(Integer.parseInt(userId), username, email, Role.valueOf(role), phone, isVerified);

            Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));

            UserDetails userDetails = new CustomUserDetails(user, authorities);

            return new UsernamePasswordAuthenticationToken(userDetails,token,userDetails.getAuthorities());
        } catch (SignatureVerificationException e) {
            logger.error("JWT 签名无效：" + e.getMessage(), e);
            throw new BadCredentialsException("无效的 JWT 签名");
        } catch (TokenExpiredException e) {
            logger.warn("JWT 已过期：" + e.getMessage(), e);
            throw new CredentialsExpiredException("JWT 令牌已过期");
        } catch (JWTVerificationException e) {
            logger.error("JWT 验证失败：" + e.getMessage(), e);
            throw new AuthenticationServiceException("JWT 令牌验证失败");
        } catch (Exception e) {
            logger.error("未知错误：" + e.getMessage(), e);
            throw new AuthenticationServiceException("JWT 认证失败");
        }
    }

}
