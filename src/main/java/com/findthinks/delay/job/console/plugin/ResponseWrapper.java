package com.findthinks.delay.job.console.plugin;

import com.findthinks.delay.job.console.web.ControllerLocation;
import com.findthinks.delay.job.share.lib.result.FoxResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackageClasses = { ControllerLocation.class })
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public Object beforeBodyWrite(Object ret, MethodParameter paramMethodParameter, MediaType paramMediaType, Class paramClass, ServerHttpRequest paramServerHttpRequest,
                                  ServerHttpResponse paramServerHttpResponse) {
        //null表示该controller没有返回值
        if (null == ret) {
            return buildDefaultDataPacket();
        }

        return buildDataPacket(ret);
    }

    @Override
    public boolean supports(MethodParameter paramMethodParameter, Class paramClass) {
        return true;
    }

    /**
     * Wrap unified result
     * 
     * @param data
     * @return
     */
    private FoxResult buildDataPacket(Object data) {
        return FoxResult.success(data);
    }

    /**
     * Build default respones
     * 
     * @return
     */
    protected FoxResult buildDefaultDataPacket() {
        return FoxResult.SUCCESS;
    }
}