package io.github.jzdayz.cat.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushInfo {
    private String ip;
    private int port;
    private Type type;
}
