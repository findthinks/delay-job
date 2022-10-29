package com.findthinks.delay.job.console.plugin;

import com.findthinks.delay.job.console.core.utils.AuthenticationUtils;
import com.findthinks.delay.job.console.core.utils.CookieUtils;
import com.findthinks.delay.job.share.lib.constants.SystemConstants;
import com.findthinks.delay.job.share.lib.enums.ExceptionEnum;
import com.findthinks.delay.job.share.lib.exception.DelayJobException;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String jwt = CookieUtils.getCookieValue(request.getCookies(), SystemConstants.JWT_COOKIE_NAME);
        if (StringUtils.isEmpty(jwt)) {
            throw new DelayJobException(ExceptionEnum.AUTHENTICATION_NOT_EXIST);
        }
        final Claims claims = AuthenticationUtils.parseJwt(jwt);
        if (claims.getExpiration().before(new Date())) {
            throw new DelayJobException(ExceptionEnum.AUTHENTICATION_NOT_EXIST);
        }
        return true;
    }
}