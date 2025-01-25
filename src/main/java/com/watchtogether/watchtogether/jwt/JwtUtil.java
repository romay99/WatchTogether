package com.watchtogether.watchtogether.jwt;

import com.watchtogether.watchtogether.member.dto.MemberDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;

@Component
@Slf4j
public class JwtUtil {

  private final Key key;
  private final long accessTokenExpTime;

  // 생성자로 DI
  public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expTime}") long expTime) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenExpTime = expTime;
  }

  public String createAccessToken(MemberDto memberDto) {
    return createToken(memberDto, accessTokenExpTime);
  }

  /**
   * 토큰 생성하는 메서드
   *
   * @param memberDto          멤버 정보를 담은 DTO
   * @param accessTokenExpTime 토큰 만료 시간
   * @return JWT 를 return
   */
  private String createToken(MemberDto memberDto, long accessTokenExpTime) {
    Claims claims = Jwts.claims();

    // ID 를 JWT 의 Claim 에 넣는다.
    claims.put("memberId", memberDto.getMemberId());

    // 현재 시간과 만료 시간을 설정
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime exp = now.plusSeconds(accessTokenExpTime);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date.from(now.toInstant()))
        .setExpiration(Date.from(exp.toInstant()))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Token 에서 MemberId 추출하는 클래스
   */
  public String getUserId(String token) {
    return parseClaims(token).get("memberId", String.class);
  }

  /**
   * JWT 검증 메서드
   *
   * @param token 검증할 Token
   * @return 정상일때 true return
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  /**
   * JWT Claims 추출하는 메서드
   *
   * @param accessToken JWT
   */
  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
