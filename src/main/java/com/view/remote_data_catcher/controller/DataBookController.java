package com.view.remote_data_catcher.controller;


import com.view.remote_data_catcher.model.config_model.CustomConfig;
import com.view.remote_data_catcher.model.config_model.MockApp;
import com.view.remote_data_catcher.model.config_model.MockMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dataBook")
public class DataBookController {

    private final CustomConfig customConfig;


    @RequestMapping("/list")
    public List<MockMenu> dataBook() {
        List<MockApp> mockApps = customConfig.getMockApps();
        List<MockMenu> collect = mockApps.stream().flatMap(x -> {
            List<MockMenu> mockMenus = x.getMockMenus();
            mockMenus.forEach(y -> {
                if (y.getBelongAppId() == null) {
                    y.setBelongAppId(x.getApplicationId());
                }
            });
            return mockMenus.stream();
        }).collect(Collectors.toList());
        return collect;
    }
}
