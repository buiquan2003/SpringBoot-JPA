package jpa.spring.config.utils;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookeiUtils {

    public static void create(HttpServletResponse response, String name, String value, int maxAge, boolean secure,
            boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.getDomain();
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    // Method to read a cookie
    public static Optional<Cookie> read(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .findFirst();
    }

    // Method to delete a cookie
    public static void delete(HttpServletResponse response, String name) {
        create(response, name, null, 0, false, true);
    }

}
