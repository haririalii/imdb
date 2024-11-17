package com.lobox.imdb.controller;

import com.lobox.imdb.configuration.RequestCountFilter;
import com.lobox.imdb.controller.dto.HttpRequestMetricDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @GetMapping("/http_requests")
    public ResponseEntity<HttpRequestMetricDTO> getHttpRequestCount() {
        return ResponseEntity.ok(
                HttpRequestMetricDTO.builder()
                        .count(RequestCountFilter.getRequestCount() - 1)
                        .build()
        );
    }
}
