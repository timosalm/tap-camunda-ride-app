package com.example.highmobilitysource;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@ConfigurationProperties("highmoblity.jwt")
public record JwtConfigurationProperties(String apiVersion, String appId, String aud, String iss, String subject,
                                         String privateKeyString) {
    public String getToken(String vin) {
        return Jwts.builder().signWith(SignatureAlgorithm.ES256, getPrivateKeyFromConfiguration(privateKeyString()))
                .claim("ver", apiVersion())
                .claim("app_id", appId())
                .setAudience(aud())
                .setIssuer(iss())
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .setSubject(subject())
                .compact();
    }

    private static ECPrivateKey getPrivateKeyFromConfiguration(String privateKeyString) {
        final byte[] keyBytes = Base64.getMimeDecoder().decode(
                privateKeyString.replace("-----BEGIN PRIVATE KEY-----\n", "")
                        .replace("\n-----END PRIVATE KEY-----", ""));
        return (ECPrivateKey) getPrivateKey(keyBytes);
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes) {
        try {
            KeyFactory kf = KeyFactory.getInstance("EC");
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return kf.generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
