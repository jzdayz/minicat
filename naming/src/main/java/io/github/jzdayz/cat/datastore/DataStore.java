package io.github.jzdayz.cat.datastore;

import io.github.jzdayz.cat.core.Instance;
import io.github.jzdayz.cat.core.Service;

public interface DataStore {

    Service getService(String serviceName);

    void register(Service service);

    void register(Instance instance);

}
