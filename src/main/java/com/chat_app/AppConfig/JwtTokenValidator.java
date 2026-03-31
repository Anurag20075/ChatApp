package com.chat_app.AppConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // Correct Import
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        // 1. Check if Header exists and starts with "Bearer "
        if (jwt != null && jwt.startsWith("Bearer ")) {
            try {
                // Remove "Bearer " prefix (7 characters)
                jwt = jwt.substring(7);

                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                
                // 2. Parse the Token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // 3. Extract Data (Use keys consistent with your TokenProvider)
                String email = String.valueOf(claims.get("email")); // Usually "email" or "username"
                String authorities = String.valueOf(claims.get("authorities"));
                
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // 4. Create Authentication Object
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);
                
                // 5. Set Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // If token is expired or tampered with
                throw new BadCredentialsException("Invalid or Expired JWT Token...");
            }
        }
        
        // 6. VERY IMPORTANT: Always call doFilter so the request continues!
        filterChain.doFilter(request, response);
    }
}