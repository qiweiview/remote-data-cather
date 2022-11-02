package com.view.remote_data_catcher.model.config_model;

import com.view.remote_data_catcher.core.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
public class DataSender {


    /**
     * key: applicationId
     * value: mockApp
     */
    private Map<String, MockApp> map = new ConcurrentHashMap<>();

    public void loadFromYaml(CustomConfig customConfig) {
        customConfig.getMockApps().forEach(x -> {
            load(x);
        });
    }


    public void load(MockApp mockApp) {
        String applicationId = mockApp.getApplicationId();
        MockApp checkExist = map.get(applicationId);
        if (checkExist == null) {
            checkExist = mockApp.load();
            map.put(applicationId, checkExist);
        }
    }

    public String findNotNull(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (!"".equals(strings[i])) {
                return strings[i];
            }
        }
        return SystemConstant.EMPTY_APPLICATION_ID;
    }

    public void sendResponse(HttpServletRequest req, HttpServletResponse resp) {
        String requestURI = req.getRequestURI();


        String[] split = requestURI.split("/");
        String appId = findNotNull(split);


        MockApp mockApp = map.get(appId);
        if (mockApp == null) {
            throw new RuntimeException("未匹配应用：" + requestURI);
        } else {
            String realLocation = Stream.of(split).filter(x -> {
                //todo 滤掉空的
                return !"".equals(x);
            }).skip(1).collect(Collectors.joining("/"));
            mockApp.sendResponse("/" + realLocation, req, resp);
        }
    }


}
