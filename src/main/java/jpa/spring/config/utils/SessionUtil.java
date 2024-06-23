package jpa.spring.config.utils;

import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static void setAttribute(HttpServletRequest request, String name, Object value) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(name, value);

    }

    public static void getAttribute(HttpServletRequest request, String name) {
        HttpSession httpSession = request.getSession(false);
        httpSession.getAttribute(name);
    }

    public static void removeAttribute(HttpServletRequest request, String name) {
        HttpSession httpSession = request.getSession(false);
        httpSession.removeAttribute(name);
    }

    public static void invalidate(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }

    }

    public static void listAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                System.out.println("Attribute name: " + name + ", value: " + session.getAttribute(name));
            }
        } else {
            System.out.println("No session found.");
        }
    }

}
