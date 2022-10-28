package com.view.remote_data_catcher.core;

import com.view.remote_data_catcher.model.config_model.DataSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "logFilter2")
public class MockFilter implements Filter {


    private final DataSender dataSender;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            dataSender.sendResponse(req, resp);
        } catch (Exception e) {
            //todo 走原有链路
            log.error("响应异常", e);
            chain.doFilter(request, response);
        }

    }


}