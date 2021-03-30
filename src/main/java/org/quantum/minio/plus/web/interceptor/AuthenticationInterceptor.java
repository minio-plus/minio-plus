package org.quantum.minio.plus.web.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.quantum.minio.plus.Constant;
import org.quantum.minio.plus.web.common.FailResponseStatus;
import org.quantum.minio.plus.web.annotation.OpenMapping;
import org.quantum.minio.plus.web.context.UserContextHolder;
import org.quantum.nucleus.component.dto.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(Constant.tokenName);

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(OpenMapping.class)) {
            OpenMapping openMapping = method.getAnnotation(OpenMapping.class);

            if (openMapping.required()) {
                return true;
            }
        }

        if (StringUtils.isEmpty(token)) {
            response.getWriter().print(Response.buildFailure(FailResponseStatus.UNAUTHORIZED).toJSONString());
            return false;
        }

        DecodedJWT decodedJWT = JWT.decode(token);

        String userId = decodedJWT.getId();
        if (userId == null) {
            response.getWriter().print(Response.buildFailure(FailResponseStatus.UNAUTHORIZED).toJSONString());
            return false;
        }

        Date date = new Date();
        if(decodedJWT.getExpiresAt().getTime() > date.getTime()){
            response.getWriter().print(Response.buildFailure(FailResponseStatus.UNAUTHORIZED).toJSONString());
        }

        UserContextHolder.set(userId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
