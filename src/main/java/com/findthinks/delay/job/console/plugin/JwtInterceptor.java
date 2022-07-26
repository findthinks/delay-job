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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author YuBo
 */
public class JwtInterceptor implements HandlerInterceptor {

    private static List<String> SKIP_AUTH_URL=  Arrays.asList("/service", "/router", "/login", "/" ,"/favicon.ico","/index.html");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ctx = request.getContextPath();
        String uri = request.getRequestURI().replaceFirst(ctx, "");
        if (SKIP_AUTH_URL.contains(uri) ||
                uri.startsWith("/assets") ||
                uri.contains("/service-") ) {
            return true;
        }
        String jwt = CookieUtils.getCookieValue(request.getCookies(), SystemConstants.JWT_COOKIE_NAME);
        if (StringUtils.isEmpty(jwt)) {
            throw new DelayJobException(ExceptionEnum.AUTHENTICATION_NOT_EXIST);
        }
        final Claims claims = AuthenticationUtils.parseJwt(jwt);
        if (claims.getExpiration().before(new Date())) {
            throw new DelayJobException(ExceptionEnum.AUTHENTICATION_NOT_EXIST, "认证信息失效，请重新登录");
        }
        return true;
    }
}