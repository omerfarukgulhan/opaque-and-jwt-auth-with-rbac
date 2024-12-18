package com.ofg.auth.core.util.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
