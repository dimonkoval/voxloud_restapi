package com.voxloud.provisioning.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "provisioning")
@Getter
@Setter
public class ProvisioningProperties {

    private String domain;

    private String port;

    private String codecs;
}
