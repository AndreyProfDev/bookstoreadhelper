package com.AndreyProfDev.bookstoreadhelper.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@RestController
public class ControllerInterceptor {

    @Value("${website_server_address}")
    private String remoteHostUrl;

    @RequestMapping(value = "/**", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object get(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        try{
            HttpHeaders requestHeaders = new HttpHeaders();
            Enumeration<String> requestHeaderNames = request.getHeaderNames();
            while (requestHeaderNames.hasMoreElements()) {
                String requestHeaderName = requestHeaderNames.nextElement();
                requestHeaders.add(requestHeaderName, request.getHeader(requestHeaderName));
            }

            HttpEntity requestEntity = new HttpEntity<>(null, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange(
                    remoteHostUrl + clientURIToServerURI(request.getRequestURI()), HttpMethod.GET,
                    requestEntity, Object.class).getBody();

        } catch (HttpClientErrorException ex) {
            if (HttpStatus.UNAUTHORIZED.equals(ex.getStatusCode())){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }

            throw ex;
        }
    }

    @RequestMapping(value = "/**", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object post(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        try{
            HttpHeaders requestHeaders = new HttpHeaders();
            Enumeration<String> requestHeaderNames = request.getHeaderNames();
            while (requestHeaderNames.hasMoreElements()) {
                String requestHeaderName = requestHeaderNames.nextElement();
                requestHeaders.add(requestHeaderName, request.getHeader(requestHeaderName));
            }

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

            Enumeration<String> parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String parameterName = parameters.nextElement();
                body.add(parameterName, request.getParameter(parameterName));
            }

            HttpEntity requestEntity = new HttpEntity<>(body, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> exchange = restTemplate.exchange(remoteHostUrl + clientURIToServerURI(request.getRequestURI()), HttpMethod.POST, requestEntity, Object.class);

            List<String> cookies = ((ResponseEntity) exchange).getHeaders().get("Set-Cookie");

            for (String cookie : cookies) {
                int pos = cookie.indexOf("=");
                String key = cookie.substring(0, pos);
                String value = cookie.substring(pos + 1);
                value = value.substring(0, value.indexOf(";"));
                response.addCookie(new Cookie(key, value));
            }

            return exchange.getBody();

        } catch (HttpClientErrorException ex) {
            if (HttpStatus.UNAUTHORIZED.equals(ex.getStatusCode())){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }

            throw ex;
        }
    }

    private String clientURIToServerURI(String clientPath){
        if ("/server/login".equals(clientPath)){
            return clientPath.substring("/server".length());
        }

        return clientPath;
    }
}
