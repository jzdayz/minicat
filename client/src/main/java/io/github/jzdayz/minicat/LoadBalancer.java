package io.github.jzdayz.minicat;

import io.github.jzdayz.minicat.core.Instance;
import io.github.jzdayz.minicat.core.Service;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LoadBalancer {

    private Map<String,ServiceLoad> serviceLoadMap;
    private Client client;

    public LoadBalancer(Client client) {
        this.serviceLoadMap = new ConcurrentHashMap<>();
        this.client = client;
    }

    public String chose(String serviceName){
        ServiceLoad serviceLoad = serviceLoadMap.get(serviceName);
        if (serviceLoad == null){
            Service service = client.list(serviceName);
            if (service == null){
                throw new RuntimeException(" no instance for service "+serviceName);
            }
            return reload(service).getUrl();
        }
        return serviceLoad.getUrl();
    }

    public ServiceLoad reload(Service service){
        log.info(" service {} instance is changed. reload it!",service.getName());
        ServiceLoad serviceLoad = serviceLoadMap.get(service.getName());
        if (serviceLoad == null){
            serviceLoad = new ServiceLoad();
            serviceLoad.setInstances(new ArrayList<>(service.getInstances().values()));
            serviceLoadMap.put(service.getName(),serviceLoad);
            return serviceLoad;
        }
        synchronized (serviceLoad){
            serviceLoad.setInstances(new ArrayList<>(service.getInstances().values()));
        }
        return serviceLoad;
    }


    @Data
    private static class ServiceLoad {

        private int index = 0;

        private static int threshold = Integer.MAX_VALUE - 10000;

        private List<Instance> instances;

        public String getUrl(){
            synchronized (this){
                Instance instance = instances.get(index++ % instances.size());
                if (index > threshold)
                    index = 0;
                return instance.getIp() + ":" + instance.getPort();
            }
        }

    }


}
