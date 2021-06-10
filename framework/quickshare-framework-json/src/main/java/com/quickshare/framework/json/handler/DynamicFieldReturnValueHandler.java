package com.quickshare.framework.json.handler;

import com.quickshare.framework.datasource.DataSourceUtils;
import com.quickshare.framework.json.dto.JsonField;
import com.quickshare.framework.json.serializer.DynamicJsonFieldSerializer;
import com.quickshare.framework.json.serializer.SerializerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liu_ke
 */
public class DynamicFieldReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final SerializerFactory factory;
    private final RequestResponseBodyMethodProcessor processor;

    public DynamicFieldReturnValueHandler(SerializerFactory factory,
                                          RequestResponseBodyMethodProcessor processor) {
        this.factory = factory;
        this.processor = processor;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return true;
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        String client = factory.getClient();
        if(StringUtils.isEmpty(client)){
            processor.handleReturnValue(o,methodParameter,modelAndViewContainer,nativeWebRequest);
        }
        else {
            modelAndViewContainer.setRequestHandled(true);
            HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

            DynamicJsonFieldSerializer serializer = factory.getFiler(client);
            String json = serializer.toJson(o);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
