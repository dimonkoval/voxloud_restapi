package com.voxloud.provisioning.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProvisioningProperties.class)
public class AppConfig {

}
