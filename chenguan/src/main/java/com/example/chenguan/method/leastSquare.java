package com.example.chenguan.method;

public class leastSquare {
    public static int leastSquare(double[] time, double[] X,double[] Y,double[] H)
    //double leastSquare(int[] time, double[] X, double[] Y, double[] H)
    {
        double a = 0;
        double b = 0;
        double c = 0;
        double d = 0;
        double ex = 0;
        double fx = 0;
        double gx = 0;
        double Ax = 0;
        double Bx = 0;
        double Cx = 0;
        double ey = 0;
        double fy = 0;
        double gy = 0;
        double Ay = 0;
        double By = 0;
        double Cy = 0;
        double eh = 0;
        double fh = 0;
        double gh = 0;
        double Ah = 0;
        double Bh = 0;
        double Ch = 0;

        int n = 5;
        double[] Mx = new double[5];
        double[] My = new double[5];
        double[] Mh = new double[5];
        double[] minxyh = new double[3];
        int min = 0;
        double minx = 0;
        double miny = 0;
        double minh = 0;

        for (int i = 0; i < 5; i++)
        {
            a = a + time[i];
            b = b + time[i] * time[i];
            c = c + time[i] * time[i] * time[i];
            d = d + time[i] * time[i] * time[i] * time[i];
            ex = ex + time[i] * X[i];
            fx = fx + time[i] * time[i] * X[i];
            gx = gx + X[i];

            ey = ey + time[i] * Y[i];
            fy = fy + time[i] * time[i] * Y[i];
            gy = gy + Y[i];

            eh = eh + time[i] * H[i];
            fh = fh + time[i] * time[i] * H[i];
            gh = gh + H[i];



        }
//        System.out.println("a=" + a);
//        System.out.println("b=" + b);
//        System.out.println("c=" + c);
//        System.out.println("d=" + d);
//        System.out.println("ey=" + ey);
//        System.out.println("fy=" + fy);
//        System.out.println("gy=" + gy);
//        System.out.println("eh=" + eh);
//        System.out.println("fh=" + fh);
//        System.out.println("gh=" + gh);

        Ax = ((n * ex - gx * a) * (n * c - a * b) - (n * fx - b * gx) * (n * b - a * a)) / ((n * c - a * b) * (n * c - a * b) - (n * d - b * b) * (n * b - a * a));
        Bx = (n * ex - a * gx - Ax * (n * c - a * b)) / (n * b - a * a);
        Cx = (gx - Ax * b - Bx * a)/n;

        Ay = ((n * ey - gy * a) * (n * c - a * b) - (n * fy - b * gy) * (n * b - a * a)) / ((n * c - a * b) * (n * c - a * b) - (n * d - b * b) * (n * b - a * a));
        By = (n * ey - a * gy - Ay * (n * c - a * b)) / (n * b - a * a);
        Cy = (gy - Ay * b - By * a) / n;

        Ah = ((n * eh - gh * a) * (n * c - a * b) - (n * fh - b * gh) * (n * b - a * a)) / ((n * c - a * b) * (n * c - a * b) - (n * d - b * b) * (n * b - a * a));
        Bh = (n * eh - a * gh - Ah * (n * c - a * b)) / (n * b - a * a);
        Ch = (gh - Ah * b - Bh * a) / n;

//        System.out.println("Ax=" + Ax);
//        System.out.println("Bx=" + Bx);
//        System.out.println("Cx=" + Cx);
//        System.out.println("Ay=" + Ay);
//        System.out.println("By=" + By);
//        System.out.println("Cy=" + Cy);
//        System.out.println("Ah=" + Ah);
//        System.out.println("Bh=" + Bh);
//        System.out.println("Ch=" + Ch);


        for (int i = 0; i < 5; i++)
        {
            Mx[i] = Ax * time[i] * time[i] + Bx * time[i] + Cx;
            System.out.println("Mx=" + Mx[i]);

            minx = minx + (Mx[i] - X[i]) * (Mx[i] - X[i]);

            My[i] = Ay * time[i] * time[i] + By * time[i] + Cy;
            System.out.println("My=" + My[i]);
            miny = miny + (My[i] - Y[i]) * (My[i] - Y[i]);

            Mh[i] = Ah * time[i] * time[i] + Bh * time[i] + Ch;
            System.out.println("Mh=" + Mh[i]);
            minh = minh + (Mh[i] - H[i]) * (Mh[i] - H[i]);


        }
        if (minx>0.08||miny>0.08||minh>0.08)
        {
            min = 1;
        }
        minxyh[0] = minx;
        minxyh[1] = miny;
        minxyh[2] = minh;

        System.out.println("minx=" + minx);
        System.out.println("miny=" + miny);
        System.out.println("minh=" + minh);

        return min;
    }
}
