package org.example.freshdeliveryserver.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.freshdeliveryserver.utils.JwtUtil;
import org.example.freshdeliveryserver.utils.ResultVOUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtCheckFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = getJwtFromRequest(request);
        if(token != null){
            try{
                jwtUtil.validateToken(token);
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("Unauthorized: token验证失败！请重新登录！");
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    // 从请求头提取 JWT Token
    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null; // 没有或Authorization头部不正确，则返回null。
    }
}
