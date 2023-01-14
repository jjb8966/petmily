package com.petmily.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResult<T> {

    private Integer count;
    private T data;

    public ApiResult(T data) {
        this.data = data;
    }
}