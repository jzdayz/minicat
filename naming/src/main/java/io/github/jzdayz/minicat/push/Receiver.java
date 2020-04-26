package io.github.jzdayz.minicat.push;

import io.github.jzdayz.minicat.lifecycle.AbstractLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;


@Slf4j
@Component
public class Receiver extends AbstractLifecycle {

    private UDP udp;
    private Thread thread;

    public Receiver(UDP udp) {
        this.udp = udp;
    }

    @Override
    public void start() {
        super.start();
        thread = new Thread(()->{
            while (true){
                try {
                    byte[] buffer = new byte[32];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    udp.getSocket().receive(packet);
                    udp.ack(packet);
                } catch (Exception e){
                    log.error("receiver",e);
                }
            }
        });
        thread.setName("udp-receiver");

    }

    @Override
    public void stop() {

    }
}
