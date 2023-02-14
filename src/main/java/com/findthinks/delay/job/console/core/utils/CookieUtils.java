package com.findthinks.delay.job.console.core.utils;

import javax.servlet.http.Cookie;

public final class CookieUtils {

    public static String getCookieValue(Cookie[] cookies, String cookieName) {
        String value = "";
        if (null != cookies && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }
}
