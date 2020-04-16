package io.github.jzdayz.cat.datastore;

import io.github.jzdayz.cat.core.Instance;
import io.github.jzdayz.cat.core.Service;

import java.util.Map;

public interface DataStore {

    Service getService(String serviceName);

    void register(Instance instance);

    void deregister(Instance instance);

    Map<String,Service> all();

}
