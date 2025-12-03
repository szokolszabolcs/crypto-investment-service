package com.xm.crypto.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // 100 requests per minute
    private static final long CAPACITY = 100;
    private static final Duration DURATION = Duration.ofMinutes(1);

    private Bucket resolveBucket(String ip) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(CAPACITY)
                .refillIntervally(CAPACITY, DURATION)
                .build();

        return buckets.computeIfAbsent(ip, k -> Bucket.builder()
                .addLimit(limit)
                .build());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(429); // Too Many Requests
            httpResp.getWriter().write("Too many requests - rate limit exceeded");
        }
    }
}
