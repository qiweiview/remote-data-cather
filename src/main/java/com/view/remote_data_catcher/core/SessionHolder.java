package com.view.remote_data_catcher.core;

import com.view.remote_data_catcher.model.biz_model.HeaderKV;
import com.view.remote_data_catcher.model.biz_model.NoticeModel;
import com.view.remote_data_catcher.uils.Jackson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SessionHolder {

    private static final String FUZZY_PATH = "*";

    private Map<String, List<Session>> sessionList = new ConcurrentHashMap<>();

    public void register(String key, Session session) {
        List<Session> sessions = sessionList.get(key);
        if (sessions == null) {
            sessions = new LinkedList<>();
            sessionList.put(key, sessions);
        }
        sessions.add(session);
    }


    public void notice(String uri, ServletRequest request) {


        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setRequestDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        noticeModel.setRequestUri(uri);

        HttpServletRequest req = (HttpServletRequest) request;
        String remoteHost = req.getRemoteHost();
        int remotePort = req.getRemotePort();

        noticeModel.setRemoteAddress(remoteHost + ":" + remotePort);

        List<HeaderKV> headerList = Collections.list(req.getHeaderNames())
                .stream()
                .map(x -> {
                    HeaderKV headerKV = new HeaderKV();
                    headerKV.setKey(x);
                    headerKV.setValue(req.getHeader(x));
                    return headerKV;
                }).collect(Collectors.toList());

        noticeModel.setHeaderList(headerList);

        noticeModel.setRequestMethod(req.getMethod());

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            try {
                String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                noticeModel.setRequestBody(collect);
            } catch (IOException e) {
                log.error("获取请求体异常");
            }
        }

        //通知匹配
        List<Session> fuzzy = sessionList.get(FUZZY_PATH);
        autoSend(fuzzy, noticeModel);

        List<Session> sessions = sessionList.get(uri);
        autoSend(sessions, noticeModel);
    }

    private void autoSend(List<Session> sessions, NoticeModel noticeModel) {
        if (sessions != null) {
            Iterator<Session> iterator = sessions.iterator();
            while (iterator.hasNext()) {
                Session next = iterator.next();
                if (!next.isOpen()) {
                    iterator.remove();
                } else {
                    try {
                        next.getBasicRemote().sendText(Jackson.toJson(noticeModel));
                    } catch (IOException e) {
                        log.error("发送失败", e);
                    }
                }

            }

        }
    }
}
