package com.view.remote_data_catcher.core;


import com.view.remote_data_catcher.model.config_model.CustomConfig;
import com.view.remote_data_catcher.model.config_model.DataSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {


    private final DataSender dataSender;

    private final CustomConfig customConfig;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String logo = "\n" +
                " _____           _            _                   \n" +
                "/  __ \\         | |          | |                  \n" +
                "| /  \\/   __ _  | |_    ___  | |__     ___   _ __ \n" +
                "| |      / _` | | __|  / __| | '_ \\   / _ \\ | '__|\n" +
                "| \\__/\\ | (_| | | |_  | (__  | | | | |  __/ | |   \n" +
                " \\____/  \\__,_|  \\__|  \\___| |_| |_|  \\___| |_|   \n" +
                "                                                  \n" +
                "                                                  " +
                "\n";


        log.info(logo + "\n" + "---------------启动成功---------------");


        dataSender.loadFromYaml(customConfig);


    }
}