package io.github.jzdayz.minicat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@ConditionalOnWebApplication
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({ServerProperties.class,MinicatProperties.class})
public class MinicatAutoConfiguration {

    private MinicatProperties minicatProperties;
    private ServerProperties serverProperties;
    private Environment environment;


    @Bean
    public Client minicat(){
        return new Client(
                minicatProperties.getUrl(),
                // 获取ip
                minicatProperties.getAppIp(),
                serverProperties.getPort()
        );
    }

    @Bean
    public LoadBalancer loadBalancer(){
        return new LoadBalancer();
    }

    @Bean
    public PushReceive pushReceive(Client client,LoadBalancer loadBalancer){
        return new PushReceive(client,loadBalancer);
    }

    @Bean
    public NamedScheduledExecutorService motion(Client client){
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1,r->{
            Thread t = new Thread(r);
            t.setName("minicat-register");
            return t;
        });
        service.scheduleWithFixedDelay(()-> {
                    try {
                        client.register(minicatProperties.getService(),
                                minicatProperties.getInstanceName() == null ? environment.getProperty("spring.application.name") : minicatProperties.getInstanceName());
                    }catch ( Exception e){
                        log.warn("register warning ... ",e);
                    }
                },
                500,15000, TimeUnit.MILLISECONDS);
        return new NamedScheduledExecutorService(service);
    }

    @AllArgsConstructor
    public class NamedScheduledExecutorService{
        private ScheduledExecutorService service;
    }


}
