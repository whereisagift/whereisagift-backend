package com.whereisagift.auth;

import com.whereisagift.infrastructure.jwt.JwtCookieService;
import com.whereisagift.infrastructure.jwt.JwtProvider;
import com.whereisagift.user.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final JwtProvider jwtProvider;
  private final JwtCookieService jwtCookieService;

  @MutationMapping
  public User login(@Argument AuthPayload authPayload) {
    User user = authService.login(authPayload);
    jwtCookieService.writeToken(currentResponse(), jwtProvider.createToken(user.getId()));

    return user;
  }

  @MutationMapping
  private boolean logout() {
    jwtCookieService.clearToken(currentResponse());

    return true;
  }

  private HttpServletResponse currentResponse() {
    ServletRequestAttributes attrs =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attrs == null) {
      throw new IllegalStateException("No current request attributes");
    }
    HttpServletResponse response = attrs.getResponse();
    if (response == null) {
      throw new IllegalStateException("No current HTTP response");
    }

    return response;
  }
}
