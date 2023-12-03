package com.ptit.iot.auth;

import com.ptit.iot.exceptions.JwtTokenMalformedException;
import com.ptit.iot.exceptions.JwtTokenMissingException;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j2
public class JwtUtil {
    private static final String TOKEN_PATTERN = "Bearer (.*)";

    public static String getAccessToken(String token) {
        Pattern pattern = Pattern.compile(TOKEN_PATTERN);
        Matcher matcher = pattern.matcher(token);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return token;
    }

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiry-min}")
    private long jwtExpiryMin;


    public String generateToken(Long userId, Collection<String> authorityRoles,
                                Map<String, Object> info) {

        long jwtExpiryMiniSec = jwtExpiryMin * 60 * 1000;
        Date iss = new Date();
        Date exp = new Date(iss.getTime() + jwtExpiryMiniSec);
        info.put("authorities", authorityRoles);
        return Jwts.builder()
                .setClaims(info)
                .setSubject(String.valueOf(userId))
                .setIssuer("cjvina")
                .setIssuedAt(iss)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }


    public Claims getClaims(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }
}