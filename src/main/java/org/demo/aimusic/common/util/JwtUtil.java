package org.demo.aimusic.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${security.jwt.secret}")
  private String secretString;

  @Value("${security.jwt.expiration-ms}")
  private long expirationMs;

  private SecretKey secretKey;

  @PostConstruct // Initializes the secret key after dependency injection is complete.
  public void init() {
    if (secretString == null || secretString.getBytes(StandardCharsets.UTF_8).length < 32) {
      throw new IllegalArgumentException(
          "JWT secret must be at least 32 bytes (256 bits) long for HS256");
    }
    this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Extracts the username (subject) from a JWT token.
   *
   * @param token The JWT token.
   * @return The username extracted from the token.
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts the expiration date from a JWT token.
   * @param token The JWT token.
   * @return The expiration date of the token.
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts a specific claim from a JWT token.
   * @param token The JWT token.
   * @param claimsResolver A function to resolve the desired claim from the Claims object.
   * @return The extracted claim.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extracts all claims from a JWT token.
   *
   * @param token The JWT token.
   * @return All claims contained within the token.
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Checks if a JWT token is expired.
   * @param token The JWT token.
   * @return True if the token is expired, false otherwise.
   */
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Generates a JWT token for the given user details.
   * @param userDetails The user details for which to generate the token.
   * @return The generated JWT token.
   */
  public String generateToken(UserDetails userDetails) {
    return createToken(userDetails.getUsername());
  }

  /**
   * Creates a JWT token for the given subject.
   * @param subject The subject of the token (e.g., username).
   * @return The generated JWT token.
   */
  private String createToken(String subject) {
    Date now = new Date(System.currentTimeMillis());
    Date expiryDate = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
        .claims()
        .subject(subject)
        .issuedAt(now)
        .expiration(expiryDate)
        .and()
        .signWith(secretKey)
        .compact();
  }

  /**
   * Validates a JWT token against user details.
   * @param token The JWT token to validate.
   * @param userDetails The user details to validate against.
   * @return True if the token is valid for the given user, false otherwise.
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
