package io.github.jzdayz.cat.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Service {
    private String name;
    private ConcurrentHashMap<String,Instance> instances;
}
