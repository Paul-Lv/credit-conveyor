package com.example.deal.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = "Error in " + methodKey;

        try {
            if (response.body() != null) {
                String body = new BufferedReader(response.body().asReader())
                        .lines()
                        .collect(Collectors.joining("\n"));
                errorMessage = body;
            }
        } catch (Exception e) {
            log.error("Error reading response body", e);
        }

        HttpStatus status = HttpStatus.valueOf(response.status());

        return new ResponseStatusException(status, errorMessage);
    }
}