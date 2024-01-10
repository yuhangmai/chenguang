package com.example.chenguan.method;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class test {
    private boolean isRunning = true;
    private static Socket socket;

    public static void main(String[] args) {
        try {
            socket = new Socket("175.178.227.79", 9901);
            System.out.println("连接成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String message = "Unity";
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        try {
            outputStream.write(data, 0, data.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("发送的信息: " + message);
        test.receiveMessages();
    }

    public static void receiveMessages() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] data = new byte[1024];
            while (true) {
                int bytesRead = inputStream.read(data);
                String message = new String(data, 0, bytesRead, StandardCharsets.UTF_8);
                System.out.println("接收到的信息: " + message);

                // 解析坐标信息
                String[] coordinates = message.split(",");
                String weidu = coordinates[2];
                String jingdu = coordinates[4];
                weidu = String.format("%.7f", Double.parseDouble(weidu));
                jingdu = String.format("%.7f", Double.parseDouble(jingdu));

                System.out.println("WEIDU" + weidu);
                System.out.println("JINGDU" + jingdu);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
