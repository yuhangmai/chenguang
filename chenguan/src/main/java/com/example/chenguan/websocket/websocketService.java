package com.example.chenguan.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import javax.websocket.Session;
import java.util.concurrent.CopyOnWriteArraySet;
import com.example.chenguan.method.receiveMessage;

@ServerEndpoint(value = "/webSocket")//主要是将目前的类定义成一个websocket服务器端, 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
@Component
@EnableScheduling// cron定时任务
@Data
public class websocketService {
    private static final Logger logger = LoggerFactory.getLogger(websocketService.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<websocketService> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    public static CopyOnWriteArraySet<websocketService> getWebSocketSet() {
        return webSocketSet;
    }

    public static void setWebSocketSet(CopyOnWriteArraySet<websocketService> webSocketSet) {
        websocketService.webSocketSet = webSocketSet;
    }

    /**
     * 从数据库查询相关数据信息,可以根据实际业务场景进行修改
     */
//    @Resource
//    private IndexService indexService;
//    private static IndexService indexServiceMapper;

//    @PostConstruct
//    public void init() {
//        websocketService.indexServiceMapper = this.indexService;
//    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 会话
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        this.session = session;
        webSocketSet.add(this);
//        //查询当前在线人数
//        int nowOnline = indexServiceMapper.nowOnline();
//        this.sendMessage(JSON.toJSONString(nowOnline));
        System.out.println("websocket连接成功");

        // 调用rtk接收信息函数
        receiveMessage receiverMessage = new receiveMessage();
        receiverMessage.start();

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("参数信息：{}", message);
        //群发消息
        for (websocketService item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session 会话
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("连接异常！");
        error.printStackTrace();
    }

    /**
     * 发送信息
     *
     * @param message 消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 自定义消息推送、可群发、单发
     *
     * @param message 消息
     */
    public static void sendInfo(String message) throws IOException {
        logger.info("信息:{}", message);
        for (websocketService item : webSocketSet) {
            item.sendMessage(message);
        }
    }
}
