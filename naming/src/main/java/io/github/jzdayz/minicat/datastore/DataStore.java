package io.github.jzdayz.minicat.datastore;

import io.github.jzdayz.minicat.core.Instance;
import io.github.jzdayz.minicat.core.Service;

import java.util.Collection;
import java.util.Map;

public interface DataStore {

    Service getService(String serviceName);

    Collection<Service> getService();

    void register(Instance instance);

    void deregister(Instance instance);

    Map<String,Service> all();

}
