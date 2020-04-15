package io.github.jzdayz.cat.demons;

import io.github.jzdayz.cat.core.Service;
import io.github.jzdayz.cat.datastore.DataStore;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@org.springframework.stereotype.Service
public class Expel implements ApplicationListener<ApplicationStartedEvent> {

    private static final ScheduledExecutorService EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(1);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        DataStore dataStore = event.getApplicationContext().getBean(DataStore.class);
        EXECUTOR_SERVICE.scheduleAtFixedRate(()->{
            ConcurrentHashMap<String, Service> all = dataStore.getAll();
        },2000,2000, TimeUnit.MILLISECONDS);
    }

}
