package com.example.highmobilitysource.highmobility;

import com.example.highmobilitysource.JwtConfigurationProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

@Service
public class HighMobilityApplicationService {

    private static final Logger log = LoggerFactory.getLogger(HighMobilityApplicationService.class);

    private final JwtConfigurationProperties configurationProperties;
    private final RestTemplate restTemplate;

    public HighMobilityApplicationService(JwtConfigurationProperties configurationProperties, RestTemplate restTemplate) {
        this.configurationProperties = configurationProperties;
        this.restTemplate = restTemplate;
    }

    public void handleVehicleLocationChangedEvent(String vin) {
        var jwt = Jwts.builder().signWith(SignatureAlgorithm.ES256, getPrivateKeyFromConfiguration(configurationProperties))
                .claim("ver", configurationProperties.apiVersion())
                .claim("app_id", configurationProperties.appId())
                .setAudience(configurationProperties.aud())
                .setIssuer(configurationProperties.iss())
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .setSubject("a074fac0-f836-46d9-b83d-aef4d1287c49")
                .compact();
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        var locationData = restTemplate.exchange(configurationProperties.aud() + "/vehicle_location",
                HttpMethod.GET, httpEntity, HighMobilityVehicleLocationData.class).getBody();
        log.info("Location data for vehicle " + vin + ": " + locationData.getLatitude() + ", " + locationData.getLongitude());
    }

    private static ECPrivateKey getPrivateKeyFromConfiguration(JwtConfigurationProperties configurationProperties) {
        final byte[] keyBytes = Base64.getMimeDecoder().decode(
                configurationProperties.privateKeyString()
                        .replace("-----BEGIN PRIVATE KEY-----\n", "")
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
