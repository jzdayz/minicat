package io.github.jzdayz.cat;

import com.alibaba.fastjson.JSON;
import io.github.jzdayz.cat.core.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Slf4j
public class PushReceive {

    private Client client;

    private LoadBalancer loadBalancer;

    public PushReceive(Client client, LoadBalancer loadBalancer) {
        this.client = client;
        this.loadBalancer = loadBalancer;
    }

    public void start(){
        DatagramSocket datagramSocket = client.getDatagramSocket();
        Thread thread = new Thread(()->{
            while (true){
                byte[] buffer = new byte[64 * 1026];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    datagramSocket.receive(packet);
                    log.info("receive push data");
                    Service service = JSON.parseObject(packet.getData(), Service.class);
                    this.loadBalancer.reload(service);
                } catch (IOException e) {
                    log.error("error receive server push data",e);
                }
            }
        });
        thread.setName("push-receive");
        thread.setDaemon(true);
        thread.start();
    }

}
