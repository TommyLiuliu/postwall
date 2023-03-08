package cn.postwall.blog.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    private Integer code;

    private String message;

    private Object data;

    private Boolean success;

    private Result(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.success = this.code.equals(200);
    }

    private Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = this.code.equals(200);
    }

    public static Result success() {
        return new Result(200, "success");
    }
    public static Result success(String msg) {
        return new Result(200, msg);
    }

    public static <T> Result success(T data) {
        return new Result(200, "success", data);
    }
    public static <T> Result success(T data,String msg) {
        return new Result(200, msg, data);
    }

    public static Result fail() {
        return new Result(500, "error");
    }
    public static Result fail(String msg) {
        return new Result(500, msg);
    }

    public static Result fail(int code, String msg) {
        return new Result(code, msg);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(4);
        map.put("code", code);
        map.put("success", success);
        map.put("message", message);
        map.put("data", data);
        return map;
    }
}
