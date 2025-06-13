package com.whereisagift.infrastructure.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter converter =
            new JwtGrantedAuthoritiesConverter();

    private final JwtProvider jwtProvider;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> auths = converter.convert(jwt);

        return new UsernamePasswordAuthenticationToken(jwtProvider.getAuthorizationPrincipal(jwt), jwt, auths);
    }
}