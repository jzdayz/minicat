package io.github.jzdayz.cat;

import io.github.jzdayz.cat.core.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class simple {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            Client client = new Client("11.9.9.9",4000);
            client.register("testServiceName","testInstacneName1");

            client = new Client("12.9.9.9",4001);
            client.register("testServiceName","testInstacneName2");

            client = new Client("13.9.9.9",4002);
            client.register("testServiceName","testInstacneName3");


            Service s = client.list("testServiceName");

            if (s.getInstances().size() != 3){
                System.err.println("error--");
            }
        }

    }
}