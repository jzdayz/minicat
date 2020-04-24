package io.github.jzdayz.minicat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.minicat")
@Data
public class MinicatProperties {

    private String url = "http://127.0.0.1:20202";

    private String appIp;

    private String service;

    private String instanceName;


}
