package com.lobox.imdb.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RequestCountFilter implements Filter {

    private static final AtomicLong requestCount = new AtomicLong(0);

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            requestCount.incrementAndGet();
        }
        chain.doFilter(request, response);
    }

    public static long getRequestCount() {
        return requestCount.get();
    }
}
