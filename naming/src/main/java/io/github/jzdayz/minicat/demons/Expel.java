package io.github.jzdayz.minicat.demons;

import io.github.jzdayz.minicat.config.Config;
import io.github.jzdayz.minicat.core.Service;
import io.github.jzdayz.minicat.datastore.DataStore;
import io.github.jzdayz.minicat.lifecycle.AbstractLifecycle;
import io.github.jzdayz.minicat.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@org.springframework.stereotype.Service
public class Expel extends AbstractLifecycle implements ApplicationListener<ApplicationStartedEvent> {

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
            try {
                Map<String, Service> all = dataStore.all();
                Utils.remove(all.values().iterator(),(service)->{
                    synchronized (service){
                        Utils.remove(service.getInstances().values().iterator(),(instance -> {
                            long lastHeartBeat = instance.getLastHeartBeat();
                            long now = System.currentTimeMillis();
                            return Utils.operation(now - lastHeartBeat > Config.instanceTimeout(),(val)->{
                                if (val)
                                    log.info(" expel instance {}",instance);
                            });
                        }));
                    }
                    return service.getInstances().size() == 0;
                });
            } catch (Exception e) {
                log.error("push data",e);
            }
        },2000,2000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        EXECUTOR_SERVICE.shutdown();
        log.info("expel is closed...");
    }
}
