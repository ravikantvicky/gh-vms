package com.global.vms.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.global.vms.exception.VMSException;

@Component
public class TokenUtils {
	private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);
	@Autowired
	private Environment env;

	public String generateToken(String ip) throws VMSException {
		try {
			Algorithm algorithm = Algorithm.HMAC256(env.getProperty("token.secret"));
			Date expDateTime = Date
					.from(LocalDateTime.now().plusMinutes(env.getProperty("token.duration.minutes", Long.class))
							.atZone(ZoneId.systemDefault()).toInstant());
			return JWT.create().withClaim(env.getProperty("token.claim.key.ip"), ip).withExpiresAt(expDateTime)
					.sign(algorithm);
		} catch (Exception e) {
			log.error("Error in token creation", e);
			throw new VMSException(env.getProperty("errormsg.token.create"));
		}
	}

	public boolean verifyToken(String token, String ip) throws VMSException {
		try {
			Algorithm algorithm = Algorithm.HMAC256(env.getProperty("token.secret"));
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			return !jwt.getClaim(env.getProperty("token.claim.key.ip")).isNull()
					&& jwt.getClaim(env.getProperty("token.claim.key.ip")).asString().equalsIgnoreCase(ip);
		} catch (JWTVerificationException e) {
			log.error("Error in token verification", e);
			return false;
		} catch (Exception e) {
			log.error("Error in token verification", e);
			throw new VMSException(env.getProperty("errormsg.token.verify"));
		}
	}
}
