package io.github.jzdayz.minicat.push;

import io.github.jzdayz.minicat.core.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UdpPacket {
    private String key;
    private Service service;
    private int sendNum;
}
