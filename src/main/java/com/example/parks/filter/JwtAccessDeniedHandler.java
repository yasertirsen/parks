package com.example.parks.filter;

import com.example.parks.model.HttpCustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.example.parks.constant.SecurityConstants.ACCESS_DENIED;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        HttpCustomResponse httpCustomResponse = new HttpCustomResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(), ACCESS_DENIED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, httpCustomResponse);
        outputStream.flush();

    }
}
