package io.github.jzdayz.minicat.push;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jzdayz.minicat.core.Instance;
import io.github.jzdayz.minicat.core.PushInfo;
import io.github.jzdayz.minicat.core.Service;
import io.github.jzdayz.minicat.datastore.DataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.*;

@Slf4j
@Component
public class PushService {

    private static DatagramSocket socket;

    private ObjectMapper objectMapper;

    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            log.error("can't create udp socket");
            System.exit(1);
        }
    }

    public PushService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors()*3,
            30,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1000)
    );

    public void onUpdate(DataStore dataStore, Service service){
        
        executorService.submit(()->{
            dataStore.all().forEach((serviceName,serviceS)->{
                ConcurrentHashMap<String, Instance> instances = serviceS.getInstances();
                instances.values().forEach(instance->{
                    PushInfo pushInfo = instance.getPushInfo();
                    log.info("push service info to {}",pushInfo);
                    try {
                        doPush(pushInfo,service);
                    }catch (Exception e){
                        log.error(" push data error ",e);
                    }
                });
            });
        });

    }

    private void doPush(PushInfo pushInfo,Service service) throws Exception{

        byte[] bytes = objectMapper.writeValueAsBytes(service);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(pushInfo.getIp(), pushInfo.getPort());
        DatagramPacket packet = new DatagramPacket(bytes,bytes.length, inetSocketAddress);
        socket.send(packet);

    }

}
