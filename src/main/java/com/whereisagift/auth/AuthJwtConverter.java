package com.whereisagift.auth;

import com.whereisagift.user.User;
import com.whereisagift.user.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthJwtConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;
    private final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public AuthJwtConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        // прокси
        User userProxy = userRepository.getReferenceById(userId);

        Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);
        return new ReferenceAuthenticationToken(userProxy, jwt, authorities);
    }

    private static class ReferenceAuthenticationToken
            extends UsernamePasswordAuthenticationToken {

        public ReferenceAuthenticationToken(Object principal,
                                            Object credentials,
                                            Collection<? extends GrantedAuthority> authorities) {
            super(principal, credentials, authorities);
        }

        @Override
        public String getName() {
            User user = (User) getPrincipal();
            return String.valueOf(user.getId());
        }
    }
}
