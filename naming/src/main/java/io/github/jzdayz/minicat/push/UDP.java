package io.github.jzdayz.minicat.push;

import io.github.jzdayz.minicat.core.PushInfo;
import io.github.jzdayz.minicat.core.Service;
import io.github.jzdayz.minicat.lifecycle.AbstractLifecycle;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Data
public class UDP extends AbstractLifecycle {

    private DatagramSocket socket;
    private Map<String,UdpPacket> container = new ConcurrentHashMap<>();

    @Override
    public void start() {
        super.start();
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }



    public String key(DatagramPacket datagramPacket){
        return datagramPacket.getAddress().getHostAddress();
    }

    public void send(PushInfo pushInfo, DatagramPacket datagramPacket, Service service) throws IOException {

//        String key = key(pushInfo);
//        if (!container.containsKey(key)) {
//            container.put(key, UdpPacket.builder().key(key).service(service).build());
            socket.send(datagramPacket);
//        }

    }

    public void ack(DatagramPacket datagramPacket){
//        byte[] data = datagramPacket.getData();
//
//        if ("OK".equals(new String(data, StandardCharsets.UTF_8))){
//            container.remove()
//        }
    }

    @Override
    public void stop() {
        socket.close();
        log.info(" udp socket is closed... ");
    }
}
