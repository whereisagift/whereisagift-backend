package com.whereisagift.logout;

import com.whereisagift.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class LogoutController {

    @MutationMapping
    private LogoutResponse logout() {

        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("No current request attributes");
        }

        HttpServletResponse response = attrs.getResponse();
        if (response == null) {
            throw new IllegalStateException("No current HTTP response");
        }

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new LogoutResponse(true, "You have successfully logged out");
    }

}
