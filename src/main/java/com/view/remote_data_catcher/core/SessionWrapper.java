package com.view.remote_data_catcher.core;


import lombok.Data;

import javax.websocket.Session;

@Data
public class SessionWrapper {

    private String id;

    private Session session;

    private volatile boolean closed = false;

    public SessionWrapper(Session session) {
        this.session = session;
        this.id = session.getId();
    }

    public static SessionWrapper of(Session session) {
        if (session == null) {
            throw new RuntimeException("对象不能为空");
        }
        return new SessionWrapper(session);
    }

    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}
