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

    private Map<String, List<SessionWrapper>> sessionList = new ConcurrentHashMap<>();

    private Map<String, SessionWrapper> sessionWrapperMap = new ConcurrentHashMap<>();

    public void register(String key, Session session) {
        List<SessionWrapper> sessions = sessionList.get(key);
        if (sessions == null) {
            sessions = new LinkedList<>();
            sessionList.put(key, sessions);
        }
        sessions.add(SessionWrapper.of(session));
        sessionWrapperMap.put(session.getId(), SessionWrapper.of(session));
    }


    public void unRegister(Session session) {
        String id = session.getId();
        SessionWrapper sessionWrapper = sessionWrapperMap.get(id);
        if (sessionWrapper != null) {
            sessionWrapper.close();
            log.info("关闭会话：" + id);
        }
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
        List<SessionWrapper> fuzzy = sessionList.get(FUZZY_PATH);
        autoSend(fuzzy, noticeModel);

        List<SessionWrapper> sessions = sessionList.get(uri);
        autoSend(sessions, noticeModel);
    }

    private void autoSend(List<SessionWrapper> sessions, NoticeModel noticeModel) {
        if (sessions != null) {
            Iterator<SessionWrapper> iterator = sessions.iterator();
            while (iterator.hasNext()) {
                SessionWrapper sessionWrapper = iterator.next();

                if (sessionWrapper.isClosed()) {
                    iterator.remove();
                } else {
                    try {
                        Session session = sessionWrapper.getSession();
                        if (session.isOpen()) {
                            //todo 仍旧打开
                            session.getBasicRemote().sendText(Jackson.toJson(noticeModel));
                        } else {
                            log.error("监测连接已关闭");
                        }
                    } catch (IOException e) {
                        log.error("发送失败", e);
                    }
                }

            }

        }
    }
}
