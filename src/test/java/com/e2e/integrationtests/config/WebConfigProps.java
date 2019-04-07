package com.e2e.integrationtests.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Data
public class WebConfigProps {
    @Value("${web.baseUrl}")
    private String baseUrl;
}
