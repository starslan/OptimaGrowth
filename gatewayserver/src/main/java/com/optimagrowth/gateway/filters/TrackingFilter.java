package com.optimagrowth.gateway.filters;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    FilterUtils filterUtils;


    public TrackingFilter(FilterUtils filterUtils) {
        this.filterUtils = filterUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(requestHeaders)) {
            logger.debug("tmx-correlation-id found in tracking filter: {}. ",
                    filterUtils.getCorrelationId(requestHeaders));
        } else {
            String correlationID = generateCorrelationId();
            exchange = filterUtils.setCorrelationId(exchange, correlationID);
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", correlationID);
        }

//        System.out.println("The authentication name from the token is : " + getUsername(requestHeaders));



        return chain.filter(exchange);
    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        return filterUtils.getCorrelationId(requestHeaders) != null;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
/*
    private String getUsername(HttpHeaders requestHeaders){
        String username = "";
        if (filterUtils.getAuthToken(requestHeaders)!=null){
            String authToken = filterUtils.getAuthToken(requestHeaders).replace("Bearer ","");
            JSONPObject jsonObj = decodeJWT(authToken);
            try {
                username = jsonObj.getString("preferred_username");
            }catch(Exception e) {logger.debug(e.getMessage());}
        }
        return username;
    }


    private JSONPObject decodeJWT(String JWTToken) {
        String[] split_string = JWTToken.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64();
        String body = new String(base64Url.getDecoder().decode(base64EncodedBody));
        JSONPObject jsonObj = new JSONPObject(body);
        return jsonObj;
    }
*/
}
