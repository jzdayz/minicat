package io.github.jzdayz.cat.datastore;

import io.github.jzdayz.cat.core.Instance;
import io.github.jzdayz.cat.core.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DataStore {

    Service getService(String serviceName);

    Collection<Service> getService();

    void register(Instance instance);

    void deregister(Instance instance);

    Map<String,Service> all();

}
