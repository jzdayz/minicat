package io.github.jzdayz.minicat.controller;

import io.github.jzdayz.minicat.core.Instance;
import io.github.jzdayz.minicat.core.Service;
import io.github.jzdayz.minicat.datastore.DataStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class ViewController {

    private final DataStore dataStore;

    public ViewController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @RequestMapping("/")
    public String test(Model model){
        Map<String, Service> all = dataStore.all();

        Service service = new Service();
        service.setName("a");
        Instance instance1 = new Instance();
//        service.setInstances();
//        all.put("a",)
        model.addAttribute("obj",all);
        return "index";
    }
}
