package com.example.chenguan.method;

import com.example.chenguan.pojo.twoThousand;
import com.example.chenguan.websocket.websocketService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArraySet;


import static com.example.chenguan.method.leastSquare.leastSquare;

// 第二个RTK
public class receiveMessageTwo {
    private static boolean isRunning = true;
    private static Socket socket;

    static CopyOnWriteArraySet<websocketService> webSocketSet = websocketService.getWebSocketSet();

    double[] data_x = new double[5];
    double[] data_y = new double[5];
    double[] data_t = new double[5];
    double[] data_h = new double[5];
    int data_i = 0;

    public static void start() {


        try {
            // 创建Socket并连接到服务器的IP地址和端口号
            socket = new Socket("175.178.227.79", 8085);
//            socket = new Socket("localhost", 8085);
            System.out.println("连接到服务器成功！8085");


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

            System.out.println("8085发送信息" );

            // 创建receiveMessage实例并调用接收消息的方法
            receiveMessageTwo receiver = new receiveMessageTwo();
            receiver.receiveMessages();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 用于接收消息的方法
    void receiveMessages() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] data = new byte[1024];
            while (isRunning) {

                // 会阻塞，直到有数据
                int bytesRead = inputStream.read(data);

                String message = new String(data, 0, bytesRead, StandardCharsets.UTF_8);
                System.out.println("8805接收到的信息: " + message);


                // 解析坐标信息
                String[] coordinates = message.split(",");
                String weidu = coordinates[2];
                String jingdu = coordinates[4];
                String gaodu = coordinates[9];
                String time = coordinates[1];

                // 解析时间为秒
                double shi = Double.parseDouble(time.substring(0, 2));
                double fen = Double.parseDouble(time.substring(2, 5));
                double miao = Double.parseDouble(time.substring(5));
                double time_miao = shi * 60 * 60 + fen * 60 + miao;


//                weidu = Double.parseDouble(String.format("%.7f", weidu));
//                jingdu = Double.parseDouble(String.format("%.7f", jingdu));
                // 格式化double值，获取精度控制后的字符串

//
//                weidu = String.format("%.7f", weidu);
//                jingdu = String.format("%.7f", jingdu);


//                System.out.println("WEIDU" + weidu);
//                System.out.println("JINGDU" + jingdu);

                // 调用坐标转换方法
                double[] V = translate(weidu, jingdu);

                // 用于文本显示
                double x1 = V[0];
                double y1 = V[1];

                //  给2000X和Y赋值
//                twoThousand twoThousand = new twoThousand();
//                twoThousand.setTwoThousandX(x1);
//                twoThousand.setTwoThousandY(y1);
//                twoThousand.setTwoThousandH(Double.parseDouble(gaodu));

                if (data_i < 4)
                {
                    data_x[data_i] = x1;
                    data_y[data_i] = y1;
                    data_t[data_i] = time_miao;
                    data_h[data_i] = Double.parseDouble(gaodu);
                    data_i++;
                }
                else
                {
                    int erchengfa = leastSquare(data_t, data_x, data_y, data_h);
                    if (erchengfa == 1)
                    {
                        System.out.println("min=1");
                    }
                    else
                    {
                        // websock发送信息
                        webSocketSet.forEach(c -> {
                            try {
                                for (int i = 1; i < data_i; i+=2) {
                                    c.sendMessage("0004|" + String.valueOf(data_x[i]));
                                    c.sendMessage("0005|" + String.valueOf(data_y[i]));
                                    c.sendMessage("0006|" + String.valueOf(data_h[i]));
//                                    c.sendMessage("0008|rtk2_having_data");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    data_i = 0;
                }

//                System.out.println("2000x1=" + x1);
//                System.out.println("2000y1=" + y1);
//                System.out.println("高度" + gaodu);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 坐标转换
    public static double[] translate(String weidu, String jingdu) {
        double pi = 3.141592653589793238463;
        double p0 = 206264.8062470963551564;


        //double B = 23 + 6.0 / 60.0 + 58.63802 / (60.0 * 60.0);    // 如意坊 hs9点
        //double L = 113 + 13.0 / 60.0 + 30.79393 / (60.0 * 60.0);

        //double B = 23 + 9.0 / 60.0 + 54.96411 / (60.0 * 60.0);    // 小卖部点6
        //double L = 113 + 21.0 / 60.0 + 02.50062 / (60.0 * 60.0);

        //纬度:2309.5775158 经度:11321.0250801

        String b1 = weidu.substring(0, 2);//度
        String b2 = weidu.substring(2);//分
        //String b31 = weidu.Substring(5, 2);
        //String b32 = weidu.Substring(7, 6);
        //String b3 = b31 + '.' + b32;//秒
        //String b4 = b1 + b2 + b3;

        double B01 = Double.parseDouble(b1);
        double B2 = Double.parseDouble(b2);
        //double B3 = Double.Parse(b3);


        String l1 = jingdu.substring(0, 3);//度
        String l2 = jingdu.substring(3);//分
        //String l31 = jingdu.Substring(6, 2);
        //String l32 = jingdu.Substring(8, 6);
        //String l3 = l31 + '.' + l32;//秒
        //String l4 = l1 + l2 + l3;

        double L01 = Double.parseDouble(l1);
        double L2 = Double.parseDouble(l2);
        //double L3 = Double.Parse(l3);


        double B = B01 + B2 / 60.0;    // wc点8
        double L = L01 + L2 / 60.0;


        int ProjNo = 0;


        double iPI = 0.0174532925199433;  //3.1415926535898 / 180.0;
        double ZoneWide = 3; // 6度带宽
        //a = 6378245.0; f = 1.0 / 298.3; % 54年北京坐标系参数
        //a = 6378140.0; f = 1 / 298.257; % 80年西安坐标系参数

        double a = 6378137.0;       //  %CGCS2000半径
        double f = 1 / 298.257222101; // %CGCS2000坐标系参数

        ProjNo = (int) (L / ZoneWide);
        //L0 = double(ProjNo) * ZoneWide + ZoneWide / 2;
        //%longitude0 = longitude0 * iPI;

        double L0 = 114.0 * iPI;

        double L1 = L * iPI; //经度转换为弧度
        double B1 = B * iPI; //纬度转换为弧度
        double e2 = 2 * f - f * f;
        double ee = e2 * (1.0 - e2);
        double NN = a / Math.sqrt(1.0 - e2 * Math.sin(B1) * Math.sin(B1));         // 卯酉曲率半径

        double T = Math.tan(B1) * Math.tan(B1);

        double C = ee * Math.cos(B1) * Math.cos(B1);     // 好像就是yt

        double A = (L1 - L0) * Math.cos(B1);

        double M = a * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * B1 - (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * B1) + (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * B1) - (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * B1));


        //double xval = NN*(A + (1 - T + C)*A*A*A / 6 + (5 - 18 * T + T*T + 72 * C - 58 * ee)*A*A*A*A*A / 120);
        //double yval = M + NN*tan(B1)*(A*A / 2 + (5 - T + 9 * C + 4 * C*C)*A*A*A*A / 24 + (61 - 58 * T + T*T + 600 * C - 330 * ee)*A*A*A*A*A*A / 720);

        //double X0 = 500000.0;
        //double Y0 = 0;
        //double X1 = xval + double(X0);
        //double Y1 = yval + Y0;

        double yval = NN * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72 * C - 58 * ee) * A * A * A * A * A / 120);
        double xval = M + NN * Math.tan(B1) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61 - 58 * T + T * T + 600 * C - 330 * ee) * A * A * A * A * A * A / 720);

        double Y0 = 500000.0;
        double X0 = 0;
        double X1 = xval + X0;
        double Y1 = yval + Y0;
        //Debug.Log("X1=" + X1);
        //Debug.Log("Y1=" + Y1);

        //如意坊国家2000转广州城建坐标四个参数x0,y0,m,a
        double x0 = -2531587.55565934;
        double y0 = -374178.243875769;
        double m = 0.999903259977636;
        double at = ((16.0 / 60.0 + 38.69463 / (60.0 * 60.0)) / 180.0) * pi;


        //double x0 = -2534307.07957648;
        //double y0 = -79266.3497062558;
        //double m = 1.013953140281506;
        //double at = ((1 + 24.0 / 60.0 + 48.15307 / (60.0 * 60.0)) / 180.0) * pi;

        double x1 = x0 + m * (Math.sin(at) * Y1 + Math.cos(at) * X1);
        double y1 = y0 + m * (Math.cos(at) * Y1 - Math.sin(at) * X1);
        //Debug.Log("x1=" + x1);
        //Debug.Log("y1=" + y1);

        // 保留三位
//        X1 = Double.parseDouble(String.format("%.3f", Double.parseDouble(weidu)));
//        Y1 = Double.parseDouble(String.format("%.3f", Double.parseDouble(jingdu)));

        double[] result = {X1, Y1};

        // 与原来的比较验证

        //double x2 = 33221.024;  xiaomaibu
        // double y2 = 46878.910;

//        double x2 = 33308.121;//wc
//        double y2 = 46879.160;
//
//        //Debug.Log("translate方法：jingdu=" + jingdu);
//        //Debug.Log("translate方法：weidu=" + weidu);
//
//        //Debug.Log("translate方法：translate方法：b4=" + b4);
//        //Debug.Log("l4=" + l4);
//
//        //Debug.Log("translate方法：x=" + x1);
//        //Debug.Log("translate方法：y=" + y1);
//        //Debug.Log("dx=" + (x2 - x1));
//        //Debug.Log("dy=" + (y2 - y1));
//
//        System.out.println("chengjianx1=" + x1);
//        System.out.println("chengjiany1=" + y1);
//
//
//        // 保留三位小数
//        BigDecimal bg_x = new BigDecimal(x1);
//        BigDecimal bg_y = new BigDecimal(y1);
//        double f1 = bg_x.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
//        double f2 = bg_y.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//
//        double[] result = {f1, f1};

        return result;
    }

}
