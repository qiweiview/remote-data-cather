package com.view.remote_data_catcher.model.config_model;

import lombok.Data;

@Data
public class MockResponse {
    private Integer delay;

    private Integer sucCode;

    private String sucBody;

    private Integer errCode;

    private String errBody;
}
