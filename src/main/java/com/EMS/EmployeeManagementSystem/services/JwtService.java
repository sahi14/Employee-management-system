package com.EMS.EmployeeManagementSystem.services;

import com.EMS.EmployeeManagementSystem.CustomUserDetails;
import com.EMS.EmployeeManagementSystem.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey=null;

    public String generateToken(CustomUserDetails users) {
        Map<String, Object> claims=new HashMap<>();
        String roleUser = users.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("ROLE_USER");
        claims.put("role",roleUser);


        long expirationTime = 1000 * 60 * 60; // 1 hour

        return Jwts
                .builder()
                .claims(claims)
                .subject(users.getUsername())
                .issuer("AP")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(generateKey()) // Ensure this returns a valid key
                .compact();

    }

    private SecretKey generateKey() {
        byte[] decode = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }

    public String getSecretKey(){
        return secretKey="595b0c455b1a16fab0a4f7e4cda4ff7480afe40096d0fd330b189bc48dbbe9b7";
    }

    public String extractUserName(String token) {


        return extractClaims(token, Claims::getSubject);
    }

    private<T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        Claims claims=extractClaims(token);
        return claimsResolver.apply(claims);

    }

    private Claims extractClaims(String token) {
        //verify the token
        //get claims
        //get payload

        return  Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Generate Refresh Token (longer expiry time)
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days expiry
                .signWith(generateKey())
                //.signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        //get claim from token
        //match username
        //check expiration
        final String userName=extractUserName(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token) );
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token,Claims::getExpiration);
    }
}
