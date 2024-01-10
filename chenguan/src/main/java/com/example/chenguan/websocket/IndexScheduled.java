//package com.example.chenguan.websocket;
//
//import com.example.chenguan.pojo.twoThousand;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
////import java.net.http.WebSocket;
//import java.io.IOException;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
//
//@Slf4j
//@Component
//public class IndexScheduled {
//    twoThousand twoThousand = new twoThousand();
//    int i =0;
//
////    @Autowired
////    private IndexMapper indexMapper;
//
//    /**
//     * 每3秒执行一次
//     */
////    @Scheduled(cron = "0/1 * * * * ? ")  // 只能是整数
//    @Scheduled(fixedRate = 500) // 0.5s执行一次
//    public void nowOnline() {
//        System.err.println("*********   定时任务执行   **************");
//
//        CopyOnWriteArraySet<websocketService> webSocketSet = websocketService.getWebSocketSet();
//        webSocketSet.forEach(c -> {
//            try {
//
//                //  模拟给2000X和Y赋值
////                twoThousand twoThousand = new twoThousand();
////                twoThousand.setTwoThousandX(i++);
////                twoThousand.setTwoThousandY(i++ + 1);
////                twoThousand.setTwoThousandH(i++ + 2);
//
////                try {
////                    // 休眠 5000 毫秒（即 5 秒）
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    // 处理中断异常
////                    e.printStackTrace();
////                }
//
////                c.sendMessage("定时任务发送信息");
//                c.sendMessage("0001|"+String.valueOf(twoThousand.getTwoThousandX()));
//                c.sendMessage("0002|"+String.valueOf(twoThousand.getTwoThousandY()));
//                c.sendMessage("0003|"+String.valueOf(twoThousand.getTwoThousandH()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        System.err.println("/n 首页定时任务完成.......");
//    }
//
//}
