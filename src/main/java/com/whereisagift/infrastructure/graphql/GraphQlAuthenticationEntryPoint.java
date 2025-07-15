package com.whereisagift.infrastructure.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whereisagift.infrastructure.jwt.JwtCookieService;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GraphQlAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final JwtCookieService jwtCookieService;
  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    jwtCookieService.clearToken(response);

    GraphQLError gqlError =
        GraphqlErrorBuilder.newError()
            .message("Unauthorized")
            .errorType(ErrorType.UNAUTHORIZED)
            .extensions(Map.of("classification", "UNAUTHORIZED"))
            .build();

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("errors", List.of(gqlError.toSpecification()));
    body.put("data", null);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(response.getWriter(), body);
  }
}
