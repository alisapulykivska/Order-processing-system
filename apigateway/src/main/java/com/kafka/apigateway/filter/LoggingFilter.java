package com.kafka.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Incoming request: {} {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI());

        var token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        var mutatedRequest = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, token != null ? token : "")
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .then(Mono.fromRunnable(() -> {
                    logger.info("Response status code: {}", exchange.getResponse().getStatusCode());
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
