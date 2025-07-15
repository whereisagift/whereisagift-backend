package com.whereisagift.infrastructure.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {
  @Override
  protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
    if (ex instanceof ConstraintViolationException violationEx) {
      return violationEx.getConstraintViolations().stream()
          .map(
              violation -> {
                List<Object> path = extractFullPath(violation);
                String message = violation.getMessage();
                Map<String, Object> ext =
                    Map.of(
                        "code",
                        "ValidationError",
                        "field",
                        String.join(".", path.stream().map(Object::toString).toList()),
                        "message",
                        message);
                return GraphqlErrorBuilder.newError(env)
                    .message(
                        "Validation failed on field '%s': %s"
                            .formatted(path.get(path.size() - 1), message))
                    .path(path)
                    .extensions(ext)
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
              })
          .toList();
    }
    return null;
  }

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    ErrorType type;
    String code;

    if (ex instanceof IllegalArgumentException) {
      type = ErrorType.BAD_REQUEST;
      code = "BadRequest";
    } else {
      type = ErrorType.INTERNAL_ERROR;
      code = "Internal";
    }

    return GraphqlErrorBuilder.newError(env)
        .message(ex.getMessage())
        .path(env.getExecutionStepInfo().getPath().toList())
        .location(env.getField().getSourceLocation())
        .extensions(Map.of("code", code))
        .errorType(type)
        .build();
  }

  private List<Object> extractFullPath(ConstraintViolation<?> violation) {
    return List.of(violation.getPropertyPath().toString().split("\\."));
  }
}
