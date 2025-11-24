package dev.betaqube.wc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	private final SecretKey secretKey;
	private final long expirationMillis;

	public JwtService(
			@Value("${security.jwt.secret}") String secret,
			@Value("${security.jwt.expiration}") long expirationMillis
	) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodeSecret(secret)));
		this.expirationMillis = expirationMillis;
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(Map.of("role", userDetails.getAuthorities().stream().findFirst().map(Object::toString).orElse("USER")),
				userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		Instant now = Instant.now();
		return Jwts.builder()
				.claims(extraClaims)
				.subject(userDetails.getUsername())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusMillis(expirationMillis)))
				.signWith(secretKey)
				.compact();
	}

	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String email = extractEmail(token);
		return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claimsResolver.apply(claims);
	}

	private String encodeSecret(String secret) {
		// jjwt requires a base64-encoded key; encode the raw secret for convenience.
		return java.util.Base64.getEncoder().encodeToString(secret.getBytes());
	}
}
