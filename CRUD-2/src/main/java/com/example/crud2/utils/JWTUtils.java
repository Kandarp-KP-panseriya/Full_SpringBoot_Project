package com.example.crud2.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;

public class JWTUtils
{

        public static final String SECRET_KEY = "secretkey";

        public String generateToken(String id, List<String> userRoles)
        {
                Claims claims = Jwts.claims();
                claims.put("id", id);
                claims.put("userRoles", userRoles);

                return Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + (10 * 60 * 1000)))
                        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                        .compact();
        }

        public static Claims decodeJwtToken(String token) {
                return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        }
}