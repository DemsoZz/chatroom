package com.chatroom.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer code;//响应码 1成功 0失败
    private String msg;//响应信息
    private Object data;//响应数据

    public static Result success(){
        return new Result(1,"success",null);
    }

    public static Result success(String msg, Object data){
        return new Result(1,msg,data);
    }

    public static Result error(String msg){
        return new Result(0,msg,null);
    }
}
