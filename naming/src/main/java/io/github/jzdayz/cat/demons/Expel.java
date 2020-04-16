package io.github.jzdayz.cat.demons;

import io.github.jzdayz.cat.config.Config;
import io.github.jzdayz.cat.core.Service;
import io.github.jzdayz.cat.datastore.DataStore;
import io.github.jzdayz.cat.util.Utils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@org.springframework.stereotype.Service
public class Expel implements ApplicationListener<ApplicationStartedEvent> {

    private static final ScheduledExecutorService EXECUTOR_SERVICE =
            new ScheduledThreadPoolExecutor(1, (r)->{
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("instance-timeout-clean");
                return thread;
            });

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        DataStore dataStore = event.getApplicationContext().getBean(DataStore.class);
        EXECUTOR_SERVICE.scheduleAtFixedRate(()->{
            Map<String, Service> all = dataStore.all();

            Utils.remove(all.values().iterator(),(service)->{
                synchronized (service){
                    Utils.remove(service.getInstances().values().iterator(),(instance -> {
                        long lastHeartBeat = instance.getLastHeartBeat();
                        long now = System.currentTimeMillis();
                        return now - lastHeartBeat > Config.instanceTimeout(TimeUnit.MILLISECONDS);
                    }));
                }
                return service.getInstances().size() == 0;
            });
        },2000,2000, TimeUnit.MILLISECONDS);
    }

}
