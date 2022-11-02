package com.view.remote_data_catcher.core;

import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;

public class SpringCtxUtils {

    public static ApplicationContext applicationContext;


    public static <T> T getBean(Class<T> type) {
        try {
            return applicationContext.getBean(type);
        } catch (NoUniqueBeanDefinitionException e) {
            //出现多个，选第一个
            String beanName = applicationContext.getBeanNamesForType(type)[0];
            return applicationContext.getBean(beanName, type);
        }
    }

    public static <T> T getBean(String beanName, Class<T> type) {
        return applicationContext.getBean(beanName, type);
    }
}