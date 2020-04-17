package io.github.jzdayz.cat;

import com.alibaba.fastjson.JSON;
import io.github.jzdayz.cat.core.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Objects;

@Slf4j
@Getter
@Setter
public class Client {

    private DatagramSocket datagramSocket;

    private OkHttpClient httpClient = new OkHttpClient();

    private String url;

    private static String DEFAULT_URL = "http://127.0.0.1:20202";

    private String localIp;

    private int port;

    public Client(String localIp, int port) {
        this.localIp = localIp;
        this.port = port;
        this.url = DEFAULT_URL;
    }

    public Client(String url, String localIp, int port) {
        this.url = url;
        this.localIp = localIp;
        this.port = port;
    }

    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.get("application/json; charset=utf-8");

    {
        try {
            this.datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            log.error("udp server up",e);
            System.exit(1);
        }
    }

    public void renewal(String serviceName,
                        String instanceName){
        register(serviceName, instanceName);
    }

    public void deregister(String serviceName,
                           String instanceName){
        Request request = new Request.Builder()
                .url(url+"/deregister?serviceName="+serviceName+"&instanceName="+instanceName)
                .build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            R res = JSON.parseObject(Objects.requireNonNull(response.body()).bytes(), R.class);
            if (res.success()){
                log.info(" deregister {} success",url);
            }else{
                log.warn(" deregister {} error",url);
            }
        } catch (IOException e) {
            log.error(" deregister "+url+" error ",e);
        }
    }

    public Service list(String serviceName){
        Request request = new Request.Builder()
                .url(url+"/list?serviceName="+serviceName)
                .build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            R res = JSON.parseObject(Objects.requireNonNull(response.body()).bytes(), R.class);
            if (res.success()){
                log.info(" get service {} info success -> {}",serviceName,url);
                return JSON.parseObject(JSON.toJSONString(res.getBody()),Service.class);
            }else{
                log.warn(" get service {} error -> {} ",serviceName,url);
                return null;
            }
        } catch (IOException e) {
            log.error(" get service  error ",e);
            throw  new RuntimeException(e);
        }
    }

    public void register(String serviceName,
                         String instanceName){

        Instance instance = new Instance();
        instance.setServiceName(serviceName);
        instance.setName(instanceName);
        instance.setIp(localIp);
        instance.setPort(port);
        PushInfo pushInfo = new PushInfo();
        pushInfo.setIp(localIp);
        pushInfo.setType(PushType.UDP);
        pushInfo.setPort(datagramSocket.getLocalPort());
        instance.setPushInfo(pushInfo);
        doRegister(instance,url);
    }

    private void doRegister(Instance instance,String url){
        RequestBody body = RequestBody.create(JSON.toJSONString(instance), MEDIA_TYPE_JSON);
        Request request = new Request.Builder()
                .url(url+"/register")
                .post(body)
                .build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            R res = JSON.parseObject(Objects.requireNonNull(response.body()).bytes(), R.class);
            if (res.success()){
                log.info(" register {} success",url);
            }else{
                log.warn(" register {} error",url);
            }
        } catch (IOException e) {
            log.error(" register "+url+" error ",e);
        }
    }
}
