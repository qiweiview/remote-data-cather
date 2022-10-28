package com.view.remote_data_catcher.model.config_model;

import com.view.remote_data_catcher.core.SystemConstant;
import com.view.remote_data_catcher.core.SystemEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Data
public class MockMenu {
    private String name;

    private String url;

    private MockResponse mockResponse;

    private List<MockHeader> headers;


    public void sendResponse(HttpServletRequest req, HttpServletResponse resp) {


        Map<String, String> map = query2Map(req);


        //默认成功
        String respType = SystemConstant.DEFAULT_RESPONSE_TYPE;
        String latestType = map.get(SystemConstant.RESPONSE_TYPE_QUERY_KEY);
        if (latestType != null) {
            respType = latestType;
        }

        Integer code;
        String response;
        if (SystemEnum.ResponseType.SUC_RESPONSE.code.equals(respType)) {
            String sucBody = mockResponse.getSucBody();
            if (sucBody == null) {
                sucBody = SystemConstant.DEFAULT_SUC_RESPONSE;
            }
            response = sucBody;

            Integer sucCode = mockResponse.getSucCode();
            if (sucCode == null) {
                sucCode = SystemConstant.DEFAULT_SUC_CODE;
            }
            code = sucCode;

        } else if (SystemEnum.ResponseType.ERR_RESPONSE.code.equals(respType)) {
            String errBody = mockResponse.getErrBody();
            if (errBody == null) {
                errBody = SystemConstant.DEFAULT_ERR_RESPONSE;
            }
            response = errBody;

            Integer errCode = mockResponse.getErrCode();
            if (errCode == null) {
                errCode = SystemConstant.DEFAULT_ERR_CODE;
            }
            code = errCode;
        } else {
            throw new RuntimeException("不支持的类型:" + respType);
        }


        try (ServletOutputStream outputStream = resp.getOutputStream()) {

            //响应码
            resp.setStatus(code);

            //响应头
            if (null != headers) {
                headers.forEach(x -> {
                    resp.setHeader(x.getName(), x.stringValue());
                });
            }

            //响应体内
            outputStream.write(response.getBytes());
            outputStream.flush();

        } catch (Exception e) {
            log.error("响应异常", e);
            throw new RuntimeException("响应异常");
        }


    }

    private Map<String, String> query2Map(HttpServletRequest request) {
        Map<String, String> queryParameters = new HashMap<>();
        String queryString = request.getQueryString();
        if (null != queryString && !"".equals(queryString)) {
            try {
                queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                log.error("Url转码异常", e);
            }
            String[] parameters = queryString.split("&");
            for (String parameter : parameters) {
                String[] keyValuePair = parameter.split("=");
                queryParameters.put(keyValuePair[0], keyValuePair[1]);
            }
        }
        return queryParameters;
    }
}
