package com.kafka.usermicroservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/")
                .realm("master")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .clientSecret("6aqtmyvJFBsacAC8VF6FdKCnH7adpEq3")
                .username("admin")
                .password("admin")
                .build();
    }
}
