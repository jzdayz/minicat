package io.github.jzdayz.cat.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Config implements EnvironmentAware {

    private static Environment environment;

    public static String config(String key){
        return environment.getProperty(key);
    }
    public static String config(String key,String defaultVal){
        return environment.getProperty(key,defaultVal);
    }

    public static long instanceTimeout(TimeUnit timeUnit){
        return timeUnit.toSeconds(Long.parseLong(
                config(Constant.INSTANCE_TIMEOUT,Constant.INSTANCE_TIMEOUT_DEFAULT)
        ));
    }

    @Override
    public void setEnvironment(Environment environment) {
        Config.environment = environment;
    }
}
