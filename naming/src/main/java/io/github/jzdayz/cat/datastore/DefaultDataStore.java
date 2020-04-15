package io.github.jzdayz.cat.datastore;

import io.github.jzdayz.cat.core.Instance;
import io.github.jzdayz.cat.core.Service;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DefaultDataStore implements DataStore{

    private ConcurrentHashMap<String,Service> storage =
            new ConcurrentHashMap<>();

    @Override
    public Service getService(String serviceName) {
        return storage.get(serviceName);
    }

    @Override
    public void register(Service service) {
        synchronized (DataStore.class){
            Service serviceGet = storage.get(service.getName());
            if (serviceGet!=null){
                serviceGet.getInstances().putAll(service.getInstances());
            }
            storage.put(service.getName(),service);
        }
    }

    @Override
    public void register(Instance instance) {
        Service service = storage.get(instance.getServiceName());
        if (service!=null){
//            service.getInstances().get()
        }
    }
}
