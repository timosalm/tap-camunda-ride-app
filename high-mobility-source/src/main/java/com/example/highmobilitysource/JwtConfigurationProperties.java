package com.example.highmobilitysource;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("highmoblity.jwt")
public
record JwtConfigurationProperties(String apiVersion, String appId, String aud, String iss, String privateKeyString) {
}
