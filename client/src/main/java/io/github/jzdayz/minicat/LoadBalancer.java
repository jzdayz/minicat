package io.github.jzdayz.minicat;

import io.github.jzdayz.minicat.core.Instance;
import io.github.jzdayz.minicat.core.Service;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancer {

    private Map<String,ServiceLoad> serviceLoadMap;

    public LoadBalancer() {
        this.serviceLoadMap = new ConcurrentHashMap<>();
    }

    public String chose(String serviceName){
        ServiceLoad serviceLoad = serviceLoadMap.get(serviceName);
        if (serviceLoad == null){
            throw new RuntimeException(" no instance for service "+serviceName);
        }
        return serviceLoad.getUrl();
    }

    public void reload(Service service){
        ServiceLoad serviceLoad = serviceLoadMap.get(service.getName());
        if (serviceLoad == null){
            serviceLoad = new ServiceLoad();
            serviceLoad.setInstances(new ArrayList<>(service.getInstances().values()));
            serviceLoadMap.put(service.getName(),serviceLoad);
            return;
        }
        synchronized (serviceLoad){
            serviceLoad.setInstances(new ArrayList<>(service.getInstances().values()));
        }
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
