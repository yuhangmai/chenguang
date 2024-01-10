package com.example.chenguan.controller;

import com.example.chenguan.pojo.twoThousand;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.chenguan.method.*;

import java.util.Random;

@Controller
@RequestMapping("/map")
@CrossOrigin
//@CrossOrigin
public class zuobiaoController {

    @RequestMapping("/test")
    @ResponseBody
    public String fanhuishuju() {
        System.out.println("启动");
        return "你的数据"; // 替换成你想要返回的实际数据
    }

//    @MessageMapping("/send/twoThousand")
//    @SendTo("/topic/twoThousandData")
//    public twoThousand sendTwoThousandData(twoThousand data) {
//        return data;
//    }
}
