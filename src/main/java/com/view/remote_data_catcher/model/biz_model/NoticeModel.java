package com.view.remote_data_catcher.model.biz_model;

import lombok.Data;

import java.util.List;

@Data
public class NoticeModel {


    private String requestDateTime;

    private String requestUri;

    private String remoteAddress;

    private String requestMethod;

    private List<HeaderKV> headerList;

    private String requestBody;
}
