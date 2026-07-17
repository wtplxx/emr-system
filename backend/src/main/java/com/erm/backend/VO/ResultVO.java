package com.erm.backend.VO;

import lombok.Data;
import java.util.Map;

@Data
public class ResultVO<T> {
    private Integer code;
    private T data;
    private String token;
}