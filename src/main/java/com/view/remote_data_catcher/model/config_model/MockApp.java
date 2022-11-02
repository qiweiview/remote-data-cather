package com.view.remote_data_catcher.model.config_model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Data
public class MockApp {
    private String applicationId;

    private String globalResponseType;

    private List<MockMenu> mockMenus;

    private Map<String, MockMenu> urlMap;

    public MockApp load() {
        if (mockMenus == null) {
            log.error("加载失败规则集合为空");
            return this;
        }

        urlMap = new HashMap<>();
        mockMenus.forEach(x -> {
            urlMap.put(x.getUrl(), x);
        });
        return this;
    }

    public void sendResponse(String realLocation, HttpServletRequest req, HttpServletResponse resp) {
        MockMenu mockMenu = urlMap.get(realLocation);
        if (mockMenu == null) {
            throw new RuntimeException(applicationId + " 应用下未匹配路径 " + realLocation);
        }
        mockMenu.sendResponse(globalResponseType, req, resp);
    }


}
