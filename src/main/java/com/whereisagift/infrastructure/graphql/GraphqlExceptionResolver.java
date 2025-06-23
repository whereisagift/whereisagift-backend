package com.whereisagift.infrastructure.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GraphqlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
        List<GraphQLError> errors = new ArrayList<>();

        if (ex instanceof ConstraintViolationException violationEx) {
            for (ConstraintViolation<?> violation : violationEx.getConstraintViolations()) {
                String fieldPath = extractFieldPath(violation.getPropertyPath().toString());

                Map<String, Object> extensions = new LinkedHashMap<>();
                extensions.put("code", "ValidationError");
                extensions.put("field", fieldPath);
                extensions.put("message", violation.getMessage());

                errors.add(GraphqlErrorBuilder.newError(env)
                        .message("Validation failed on field '%s': %s".formatted(fieldPath, violation.getMessage()))
                        .extensions(extensions)
                        .errorType(ErrorType.BAD_REQUEST)
                        .build());
            }
        } else {
            String code;
            ErrorType type;

            if (ex instanceof IllegalArgumentException) {
                code = "BadRequest";
                type = ErrorType.BAD_REQUEST;
            } else {
                code = "Internal";
                type = ErrorType.INTERNAL_ERROR;
            }

            Map<String, Object> extensions = new LinkedHashMap<>();
            extensions.put("code", code);

            errors.add(GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .extensions(extensions)
                    .errorType(type)
                    .build());
        }

        return errors;
    }

    private String extractFieldPath(String rawPath) {
        String[] parts = rawPath.split("\\.");
        return parts.length > 0 ? parts[parts.length - 1] : rawPath;
    }
}
