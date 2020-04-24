package io.github.jzdayz.minicat.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class R {
    @Builder.Default
    private int code = 200;
    private String message;
    private Object body;

    public boolean success(){
        return code == 200;
    }

    public static R ok(){
        return new R();
    }

    public static R ok(Object body){
        return R.builder().body(body).build();
    }

    public static R error(){
        return R.builder().code(500).build();
    }

    public static R error(String message,Object... formatArgs){
        return R.builder().code(500).message(String.format(message,formatArgs)).build();
    }

    public static R error(String message){
        return R.builder().code(500).message(message).build();
    }
}
