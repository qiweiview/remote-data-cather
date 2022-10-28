package com.view.remote_data_catcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@EnableWebSocket
@ServletComponentScan("com.view.remote_data_catcher.core")
@SpringBootApplication
public class RemoteDataCatcherApplication {


    public static void main(String[] args) {
        SpringApplication.run(RemoteDataCatcherApplication.class, args);
    }

}
