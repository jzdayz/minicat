package io.github.jzdayz.cat.datastore;

import io.github.jzdayz.cat.core.Instance;
import io.github.jzdayz.cat.core.Service;
import io.github.jzdayz.cat.push.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DefaultDataStore implements DataStore{

    private Map<String,Service> storage =
            new ConcurrentHashMap<>();

    private PushService pushService;


    public DefaultDataStore(PushService pushService) {
        this.pushService = pushService;
    }

    @Override
    public Service getService(String serviceName) {
        return storage.get(serviceName);
    }

    @Override
    public Collection<Service> getService() {
        return storage.values();
    }

    @Override
    public void register(Instance instance) {
        instance.setLastHeartBeat(System.currentTimeMillis());
        Service service = storage.get(instance.getServiceName());
        if (service!=null){
            synchronized (service){
                Map<String, Instance> instances = service.getInstances();
                Instance instanceC = instances.get(instance.getName());
                if (instanceC!=null){
                    // 续约
                    instanceC.setLastHeartBeat(instance.getLastHeartBeat());
                    return;
                }
                instances.put(instance.getName(),instance);
                pushService.onUpdate(service);
                return;
            }
        }

        ConcurrentHashMap<String,Instance> instanceNew = new ConcurrentHashMap<>();
        instanceNew.put(instance.getName(),instance);
        Service serviceNew = new Service();
        serviceNew.setInstances(instanceNew);
        serviceNew.setName(instance.getServiceName());
        storage.put(serviceNew.getName(),serviceNew);
        pushService.onUpdate(service);
    }

    @Override
    public void deregister(Instance instance) {
        Service service = storage.get(instance.getServiceName());
        if (Objects.isNull(service)){
            log.warn(" service is not available !");
            return;
        }
        synchronized (service){
            Instance remove = service.getInstances().remove(instance.getName());
            if (remove!=null){
                pushService.onUpdate(service);
            }
        }
    }

    @Override
    public Map<String, Service> all() {
        return storage;
    }
}
