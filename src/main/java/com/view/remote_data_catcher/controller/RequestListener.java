package com.view.remote_data_catcher.controller;

import com.view.remote_data_catcher.core.SessionHolder;
import com.view.remote_data_catcher.core.SpringCtxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


/**
 * 非单例
 */
@Slf4j
@ServerEndpoint("/websocket")
@Component
public class RequestListener {

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        //todo 不做任何操作
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //todo 不做任何操作
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        SessionHolder sessionHolder = SpringCtxUtils.getBean(SessionHolder.class);
        sessionHolder.register(message, session);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //todo 不做任何操作
    }


}