package com.view.remote_data_catcher.model.config_model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class MockHeader<T> {
    private String name;


    private T value;

    public String stringValue() {
        if (null == value) {
            return "the value is null";
        }
        return value.toString();
    }
}
