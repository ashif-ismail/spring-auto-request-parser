package com.ashifismail.autorequest.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Ashif Ismail
 * @email ashifismail.ae@gmail.com
 */
public class RequestContentArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(RequestContentArgumentResolver.class.getName());
    private final ObjectMapper objectMapper;


    public RequestContentArgumentResolver(@Qualifier("spring-auto-request") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestContent.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Object mappedObject = processRequest(request, parameter.getParameterType());

        if (parameterType.isInstance(mappedObject)) {
            return parameterType.cast(mappedObject);
        } else {
            throw new IllegalArgumentException("Unable to cast the object to " + parameterType.getName());
        }
    }

    private <T> T createInstanceFromMap(Class<T> clazz, Map<String, String[]> fieldMap) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = "";
            boolean isNamedJsonKey = field.isAnnotationPresent(JsonProperty.class);
            if (isNamedJsonKey) {
                String jsonKey = field.getAnnotation(JsonProperty.class).value();
                logger.info("[-] Named json key with name  - {}", jsonKey);
                fieldName = jsonKey;
            } else {
                fieldName = field.getName();
            }
            if (fieldMap.containsKey(fieldName)) {
                field.setAccessible(true);
                Object value = fieldMap.get(fieldName)[0];
                if (value != null) {
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(instance, Integer.parseInt(value.toString()));
                    } else if (field.getType() == String.class) {
                        field.set(instance, value.toString());
                    } else {
                        field.set(instance, value);
                    }
                } else {
                    logger.error("[-] map value received as null while setting field instance");
                }
            }
        }
        return instance;
    }

    private  <T> T processRequest(HttpServletRequest request, Class<T> clazz) {
        T responseType = null;
        String contentType = request.getContentType();
        if (contentType != null) {
            if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                logger.info("Initiating the conversion of JSON to class instance of {}", clazz.getName());
                ServletInputStream inputStream = null;
                try {
                    inputStream = request.getInputStream();
                } catch (IOException e) {
                    logger.error("[-] Exception occured while reading inputstream from HttpServletRequest instance",e);
                }
                if (inputStream != null) {
                    try {
                        responseType = objectMapper.readValue(inputStream, clazz);
                    } catch (IOException e) {
                        logger.error("[-] Exception occured while mapping inputstream to class instance ",e);
                    }
                } else {
                    logger.error("[-] Could not read from request input stream as it is null");
                    throw new NullPointerException("Request input stream is empty while reading JSON");
                }
            } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) || contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                logger.info("Initiating the conversion of Field Map to class instance of {}", clazz.getName());
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (!parameterMap.isEmpty()) {
                    try {
                        responseType = createInstanceFromMap(clazz, parameterMap);
                    } catch (Exception e) {
                        logger.error("[-] Exception occured while creating class instance from the field map", e);
                    }
                } else {
                    logger.error("[-] Could not read from request parameter map as it is empty");
                    throw new NullPointerException("Request parameter map is empty while reading form data");
                }
            }
        } else {
            logger.error("[-] Request does not contain a supported content type");
        }
        return responseType;
    }
}

