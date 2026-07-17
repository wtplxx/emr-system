package com.erm.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    // 固定密钥，避免重启后所有token失效
    private static final String SECRET_STRING = "EMRHealthRecordSystemSecretKey2026ForJWTTokenGeneration";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION = 86400000; // 24小时

    public static String generateToken(Integer userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Integer getUserIdFromToken(String token) {
        return Integer.parseInt(getAllClaims(token).getSubject());
    }

    public static String getRoleFromToken(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    public static boolean isTokenExpired(String token) {
        try {
            return getAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
