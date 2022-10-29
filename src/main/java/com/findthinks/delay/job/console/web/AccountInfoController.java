package com.findthinks.delay.job.console.web;

import com.findthinks.delay.job.console.core.service.AccountService;
import com.findthinks.delay.job.console.core.utils.AuthenticationUtils;
import com.findthinks.delay.job.console.core.utils.CookieUtils;
import com.findthinks.delay.job.console.web.rr.AccountInfo;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.API_PREFIX;
import static com.findthinks.delay.job.share.lib.constants.SystemConstants.JWT_COOKIE_NAME;

@RestController
@RequestMapping(value = API_PREFIX)
@Validated
public class AccountInfoController {

    @Resource
    private AccountService accountService;

    @GetMapping(value = "/account/info")
    public AccountInfo userInfo(HttpServletRequest request) {
        String jwt = CookieUtils.getCookieValue(request.getCookies(), JWT_COOKIE_NAME);
        Claims claims = AuthenticationUtils.parseJwt(jwt);
        String account = (String) claims.get("account");
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccount(account);
        return accountInfo;
    }

    @PostMapping(value = "/login")
    public boolean login(@RequestBody @Valid AccountInfo account, HttpServletResponse response) {
        accountService.authentication(account);
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, AuthenticationUtils.createJWT(account));
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
        return true;
    }

    @PostMapping(value = "/logout")
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        String jwt = CookieUtils.getCookieValue(request.getCookies(), JWT_COOKIE_NAME);
        if (!StringUtils.hasLength(jwt)) {
            return true;
        }

        Cookie cookie = new Cookie(JWT_COOKIE_NAME, jwt);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return true;
    }
}