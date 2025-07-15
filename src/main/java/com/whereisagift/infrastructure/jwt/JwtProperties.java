package com.whereisagift.infrastructure.jwt;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
@Configuration("jwtProperties")
public class JwtProperties {
  private Duration ttl;
}
