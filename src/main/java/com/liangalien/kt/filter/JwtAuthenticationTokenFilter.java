package com.liangalien.kt.filter;

import com.liangalien.kt.dto.UserDTO;
import com.liangalien.kt.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (token == null || "".equals(token.trim())) {
            //token为空的话, 就不管它, 让SpringSecurity中的其他过滤器处理请求
            filterChain.doFilter(request, response);
            return;
        }
        //token不为空时, 解析token
        UserDTO user = jwtUtil.decode(token);
        if (user == null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        } else {
            //将用户安全信息存入SecurityContextHolder, 在之后SpringSecurity的过滤器就不会拦截
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //放行
            filterChain.doFilter(request, response);
        }
    }
}
