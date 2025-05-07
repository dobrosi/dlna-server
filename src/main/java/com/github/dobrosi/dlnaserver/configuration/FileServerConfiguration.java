package com.github.dobrosi.dlnaserver.configuration;

import java.nio.file.Path;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "file.server")
@Data
public class FileServerConfiguration {
    private Path path;
}
