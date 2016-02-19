package com.vellkare.api

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.AbstractOAuth2SecurityExceptionHandler
import org.springframework.security.web.AuthenticationEntryPoint

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ApiEntryPoint extends AbstractOAuth2SecurityExceptionHandler implements
        AuthenticationEntryPoint {

    @Override
    void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        doHandle(request, response, authException);
        // TODO: figure out how to manage the response more effectively
        //request.getSession().setAttribute(WebAttributes.SAVED_REQUEST, null);
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        //response.outputStream.write('{"messageCode":"api.unauthorized"}'.bytes)
        //response.setContentType("application/json")
        //response.outputStream.flush()
        //response.flushBuffer()
        //response.outputStream.close()
    }

    @Override
    protected ResponseEntity<OAuth2Exception> enhanceResponse(ResponseEntity<OAuth2Exception> response, Exception exception) {
        HttpHeaders update = new HttpHeaders();
        update.putAll(response.getHeaders());
        return new ResponseEntity<OAuth2Exception>(response.getBody(), update, HttpStatus.FORBIDDEN);
    }
}
