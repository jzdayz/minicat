package io.github.jzdayz.minicat.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instance {

    private String ip;
    private int port;
    private String name;
    private long lastHeartBeat;
    private String serviceName;
    private PushInfo pushInfo;

}
