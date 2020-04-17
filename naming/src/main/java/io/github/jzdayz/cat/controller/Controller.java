package io.github.jzdayz.cat.controller;

import io.github.jzdayz.cat.core.Instance;
import io.github.jzdayz.cat.core.R;
import io.github.jzdayz.cat.core.Service;
import io.github.jzdayz.cat.datastore.DataStore;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final DataStore dataStore;

    public Controller(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/list")
    public R list(String serviceName, @RequestHeader MultiValueMap<String,String> head){
        if (StringUtils.isEmpty(serviceName)){
            return R.error(" %s is empty ","serviceName");
        }
        Service service = dataStore.getService(serviceName);
        if (service == null){
            return R.error("no service");
        }
        return R.ok(service);
    }


    @PostMapping("/register")
    public R register(@RequestBody Instance instance){
        dataStore.register(instance);
        return R.ok();
    }

    @DeleteMapping("/deregister")
    public R deregister(String serviceName,String instanceName){
        if (StringUtils.isEmpty(instanceName) || StringUtils.isEmpty(serviceName)){
            return R.error(" %s or %s is empty ","instanceName","serviceName");
        }
        Instance instance = new Instance();
        instance.setName(instanceName);
        instance.setServiceName(serviceName);
        dataStore.deregister(instance);
        return R.ok();
    }

}
