package com.view.remote_data_catcher.core;

public class SystemEnum {

    public enum ResponseType {
        SUC_RESPONSE("SUC_RESPONSE", "成功返回"),
        ERR_RESPONSE("ERR_RESPONSE", "失败返回"),
        ;

        ResponseType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String code;

        public String name;
    }
}
