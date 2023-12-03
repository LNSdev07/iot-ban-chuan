package com.ptit.iot.auth;

import com.ptit.iot.exceptions.JwtTokenMalformedException;
import com.ptit.iot.exceptions.JwtTokenMissingException;
import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import com.ptit.iot.repository.UserRepository;
import com.ptit.iot.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
@Log4j2
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Get authorization header and validate
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null || token.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Get jwt token and validate
            token = JwtUtil.getAccessToken(token);
            Long userId = null;
            String phone;
            String email;
            try {
                Claims claims = jwtUtil.getClaims(token);
                userId = Long.parseLong(claims.get("user_id").toString());
                phone = (String) claims.get("phone");
                email = (String) claims.get("email");

            } catch (JwtTokenMalformedException | JwtTokenMissingException ex) {
                throw new RuntimeException(ex);
            }

            User user = userRepo.findById(userId).orElse(null);
            if(user == null){
                throw new ResException(ResErrorCode.ENTITY_NOT_EXISTS, "user not existed");
            }
            if(user.getStatus() ==0){
                throw new ResException(ResErrorCode.USER_INACTIVE, "user inactive");
            }
            if(!user.getToken().equals(token)){
                throw new ResException(ResErrorCode.TOKEN_INVALID, "token invalid");
            }

            UserDetails userDetails = userService.getUserDetail(userId);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails == null ? List.of() : userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            Payload payload = Payload.builder()
                    .phone(phone)
                    .userId(userId)
                    .email(email)
                    .token(token)
                    .build();
            request.setAttribute("payload", payload);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.info("error {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }
    }
}
