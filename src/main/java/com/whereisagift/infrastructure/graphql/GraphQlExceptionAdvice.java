package com.whereisagift.infrastructure.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GraphQlExceptionAdvice {

  @GraphQlExceptionHandler(ConstraintViolationException.class)
  public List<GraphQLError> handleConstraintViolation(
      ConstraintViolationException ex, DataFetchingEnvironment env) {
    return ex.getConstraintViolations().stream()
        .map(
            violation ->
                GraphqlErrorBuilder.newError(env)
                    .message(violation.getMessage())
                    .path(extractFullPath(violation))
                    .extensions(Map.of("code", "ValidationError"))
                    .errorType(ErrorType.BAD_REQUEST)
                    .build())
        .toList();
  }

  private List<Object> extractFullPath(ConstraintViolation<?> violation) {
    return List.of(violation.getPropertyPath().toString().split("\\."));
  }
}
