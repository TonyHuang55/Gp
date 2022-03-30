package Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class LinearRegressionUtils {
    public static void LR(BigInteger[][] M) {
        BigDecimal learning_rate = new BigDecimal("0.0000005");
        // 初始系数 θ
        BigDecimal a = new BigDecimal("1"),
                a0 = new BigDecimal("1"),
                a1 = new BigDecimal("1"),
                a2 = new BigDecimal("1"),
                a3 = new BigDecimal("1"),
                a4 = new BigDecimal("1"),
                a5 = new BigDecimal("1"),
                a6 = new BigDecimal("1"),
                a7 = new BigDecimal("1"),
                a8 = new BigDecimal("1"),
                a9 = new BigDecimal("1"),
                a10 = new BigDecimal("1");

        // 迭代次数
        int iterator = 500;
        BigDecimal[] gradient_a = new BigDecimal[12];
        Arrays.fill(gradient_a, new BigDecimal("0"));
        for (int i = 0; i < iterator; i++) {
            BigDecimal x0 = new BigDecimal(M.length),
                    x1 = new BigDecimal(M[0][1].toString()),
                    x2 = new BigDecimal(M[0][2].toString()),
                    x3 = new BigDecimal(M[0][3].toString()),
                    x4 = new BigDecimal(M[0][4].toString()),
                    x5 = new BigDecimal(M[0][5].toString()),
                    x6 = new BigDecimal(M[0][6].toString()),
                    x7 = new BigDecimal(M[0][7].toString()),
                    x8 = new BigDecimal(M[0][8].toString()),
                    x9 = new BigDecimal(M[0][9].toString()),
                    x10 = new BigDecimal(M[0][10].toString()),
                    y = new BigDecimal(M[0][11].toString());

            BigDecimal size = new BigDecimal(String.valueOf(1.0 / M.length));
            BigDecimal loss0 = y.subtract(a.add(a0.multiply(x0)).add(a1.multiply(x1)).add(a2.multiply(x2)).add(a3.multiply(x3)).add(a4.multiply(x4)).
                    add(a5.multiply(x5)).add(a6.multiply(x6)).add(a7.multiply(x7)).add(a8.multiply(x8)).add(a9.multiply(x9)).add(a10.multiply(x10)));
            gradient_a[0] = iteritor(gradient_a[0], size, loss0);
            for (int j = 1; j < M.length; j++) {
                x0 = new BigDecimal(M[0][j].toString());
                x1 = new BigDecimal(M[j][1].toString());
                x2 = new BigDecimal(M[j][2].toString());
                x3 = new BigDecimal(M[j][3].toString());
                x4 = new BigDecimal(M[j][4].toString());
                x5 = new BigDecimal(M[j][5].toString());
                x6 = new BigDecimal(M[j][6].toString());
                x7 = new BigDecimal(M[j][7].toString());
                x8 = new BigDecimal(M[j][8].toString());
                x9 = new BigDecimal(M[j][9].toString());
                x10 = new BigDecimal(M[j][10].toString());
                y = new BigDecimal(M[j][11].toString());

                BigDecimal loss = y.subtract(a.add(a0.multiply(x0)).add(a1.multiply(x1)).add(a2.multiply(x2)).add(a3.multiply(x3)).add(a4.multiply(x4)).
                        add(a5.multiply(x5)).add(a6.multiply(x6)).add(a7.multiply(x7)).add(a8.multiply(x8)).add(a9.multiply(x9)).add(a10.multiply(x10)));
                gradient_a[j] = iteritor(gradient_a[j], size, loss);
            }
            a = a.subtract(learning_rate.multiply(gradient_a[0]));
            a0 = a0.subtract(learning_rate.multiply(gradient_a[1]));
            a1 = a1.subtract(learning_rate.multiply(gradient_a[2]));
            a2 = a2.subtract(learning_rate.multiply(gradient_a[3]));
            a3 = a3.subtract(learning_rate.multiply(gradient_a[4]));
            a4 = a4.subtract(learning_rate.multiply(gradient_a[5]));
            a5 = a5.subtract(learning_rate.multiply(gradient_a[6]));
            a6 = a6.subtract(learning_rate.multiply(gradient_a[7]));
            a7 = a7.subtract(learning_rate.multiply(gradient_a[8]));
            a8 = a8.subtract(learning_rate.multiply(gradient_a[9]));
            a9 = a9.subtract(learning_rate.multiply(gradient_a[10]));
            a10 = a10.subtract(learning_rate.multiply(gradient_a[11]));
        }

        BigDecimal[] res = {a, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
        for (BigDecimal cur : res) {
            System.out.println(cur);
        }
    }

    private static BigDecimal iteritor(BigDecimal gradient, BigDecimal size, BigDecimal loss) {
        return gradient.subtract(size.multiply(loss));
    }
}
