package io.github.jzdayz.minicat.push;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.DatagramPacket;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UdpPacket {
    private String key;
    private DatagramPacket datagramPacket;
    private int num;
}
