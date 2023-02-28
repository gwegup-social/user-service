package com.ritrovo.userservice.util;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RestClient {

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T postForEntity(String endpoint,
                               Object request,
                               Class<T> responseType,
                               Map<String, String> headers) {

        HttpHeaders defaultHeaders = getDefaultHeaders();
        addInputHeaders(defaultHeaders, headers);
        HttpEntity<Object> requestEntity = new HttpEntity<>(request, defaultHeaders);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(endpoint, requestEntity, responseType);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }

        throw new RuntimeException("unable to send post request");
    }

    private void addInputHeaders(HttpHeaders defaultHeaders,
                                 Map<String, String> headers) {
        if (Objects.isNull(headers) || Objects.isNull(defaultHeaders))
            return;

        headers.forEach(defaultHeaders::set);
    }

    public <T> T getForEntity(String endpoint, Class<T> responseType, Map<String, Object> uriVariables) {

        ResponseEntity<T> responseEntity = restTemplate.getForEntity(endpoint, responseType, uriVariables);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return responseEntity.getBody();
        }
        throw new RuntimeException("unable to send http get request");
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(supportedMediaTypes);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
