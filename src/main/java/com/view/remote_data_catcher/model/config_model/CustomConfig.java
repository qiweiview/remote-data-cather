package com.view.remote_data_catcher.model.config_model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "custom-config")
@Data
public class CustomConfig {
    private List<MockApp> mockApps;
}
