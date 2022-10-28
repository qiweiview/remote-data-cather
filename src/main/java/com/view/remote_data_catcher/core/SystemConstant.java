package com.view.remote_data_catcher.core;

public class SystemConstant {
    public static final String EMPTY_APPLICATION_ID = "EMPTY_APPLICATION_ID";

    public static final String DEFAULT_RESPONSE_TYPE = SystemEnum.ResponseType.SUC_RESPONSE.code;

    public static final String RESPONSE_TYPE_QUERY_KEY = "respType";

    public static final String DEFAULT_SUC_RESPONSE = "{\"msg\":\"成功\"}";

    public static final String DEFAULT_ERR_RESPONSE = "{\"msg\":\"异常\"}";

    public static final Integer DEFAULT_SUC_CODE = 200;

    public static final Integer DEFAULT_ERR_CODE = 500;
}
