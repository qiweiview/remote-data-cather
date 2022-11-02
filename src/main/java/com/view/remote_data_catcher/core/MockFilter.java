package com.view.remote_data_catcher.core;

import com.view.remote_data_catcher.model.config_model.DataSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "logFilter2")
public class MockFilter implements Filter {

    public static final Set<String> whiteList = new HashSet<>();

    private final DataSender dataSender;

    private final SessionHolder sessionHolder;

    static {
        whiteList.add("/");
        whiteList.add("/websocket");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();

        sessionHolder.notice(requestURI, request);


        if (whiteList.contains(requestURI)) {
            //todo 白名单
            chain.doFilter(request, response);
            return;
        }

        try {
            dataSender.sendResponse(req, resp);
        } catch (Exception e) {
            //todo 走原有链路
            log.error("响应异常", e);
            chain.doFilter(request, response);
        }

    }


}