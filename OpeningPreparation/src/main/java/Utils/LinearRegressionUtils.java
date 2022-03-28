package Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class LinearRegressionUtils {
    public static void LR(BigInteger[][] M) {
        BigDecimal learning_rate = new BigDecimal("0.00005");
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

        int iterator = 500;
        BigDecimal lastLoss = new BigDecimal(Double.MAX_VALUE);
        for (int i = 0; i < iterator; i++) {
            BigDecimal gradient_a = new BigDecimal("0"),
                    gradient_a0 = new BigDecimal("0"),
                    gradient_a1 = new BigDecimal("0"),
                    gradient_a2 = new BigDecimal("0"),
                    gradient_a3 = new BigDecimal("0"),
                    gradient_a4 = new BigDecimal("0"),
                    gradient_a5 = new BigDecimal("0"),
                    gradient_a6 = new BigDecimal("0"),
                    gradient_a7 = new BigDecimal("0"),
                    gradient_a8 = new BigDecimal("0"),
                    gradient_a9 = new BigDecimal("0"),
                    gradient_a10 = new BigDecimal("0");
            BigDecimal[] loss = new BigDecimal[M.length];
            for (int j = 0; j < M.length; j++) {
                BigDecimal x0 = new BigDecimal(M[j][0].toString()),
                        x1 = new BigDecimal(M[j][1].toString()),
                        x2 = new BigDecimal(M[j][2].toString()),
                        x3 = new BigDecimal(M[j][3].toString()),
                        x4 = new BigDecimal(M[j][4].toString()),
                        x5 = new BigDecimal(M[j][5].toString()),
                        x6 = new BigDecimal(M[j][6].toString()),
                        x7 = new BigDecimal(M[j][7].toString()),
                        x8 = new BigDecimal(M[j][8].toString()),
                        x9 = new BigDecimal(M[j][9].toString()),
                        x10 = new BigDecimal(M[j][10].toString()),
                        y = new BigDecimal(M[j][11].toString());

                loss[j] = y.subtract(a.add(a0.multiply(x0)).add(a1.multiply(x1)).add(a2.multiply(x2)).add(a3.multiply(x3)).add(a4.multiply(x4)).
                        add(a5.multiply(x5)).add(a6.multiply(x6)).add(a7.multiply(x7)).add(a8.multiply(x8)).add(a9.multiply(x9)).add(a10.multiply(x10)));
                BigDecimal size = new BigDecimal(String.valueOf(1.0 / M.length));

                gradient_a = iteritor(gradient_a, size, loss[j], new BigDecimal("1"));
                gradient_a0 = iteritor(gradient_a0, size, loss[j], x0);
                gradient_a1 = iteritor(gradient_a1, size, loss[j], x1);
                gradient_a2 = iteritor(gradient_a2, size, loss[j], x2);
                gradient_a3 = iteritor(gradient_a3, size, loss[j], x3);
                gradient_a4 = iteritor(gradient_a4, size, loss[j], x4);
                gradient_a5 = iteritor(gradient_a5, size, loss[j], x5);
                gradient_a6 = iteritor(gradient_a6, size, loss[j], x6);
                gradient_a7 = iteritor(gradient_a7, size, loss[j], x7);
                gradient_a8 = iteritor(gradient_a8, size, loss[j], x8);
                gradient_a9 = iteritor(gradient_a9, size, loss[j], x9);
                gradient_a10 = iteritor(gradient_a10, size, loss[j], x10);
            }
            Arrays.sort(loss);
            BigDecimal loss1 = loss[0].multiply(loss[0]).divide(new BigDecimal("2"));
            BigDecimal derta = lastLoss.subtract(loss1);
            System.out.println("第" + i + "轮与上一轮损失函数之差：" + derta);
            System.out.println("第" + i + "轮损失函数：" + loss1);

            a = a.subtract(learning_rate.multiply(gradient_a));
            a0 = a0.subtract(learning_rate.multiply(gradient_a0));
            a1 = a1.subtract(learning_rate.multiply(gradient_a1));
            a2 = a2.subtract(learning_rate.multiply(gradient_a2));
            a3 = a3.subtract(learning_rate.multiply(gradient_a3));
            a4 = a4.subtract(learning_rate.multiply(gradient_a4));
            a5 = a5.subtract(learning_rate.multiply(gradient_a5));
            a6 = a6.subtract(learning_rate.multiply(gradient_a6));
            a7 = a7.subtract(learning_rate.multiply(gradient_a7));
            a8 = a8.subtract(learning_rate.multiply(gradient_a8));
            a9 = a9.subtract(learning_rate.multiply(gradient_a9));
            a10 = a10.subtract(learning_rate.multiply(gradient_a10));
        }

        BigDecimal[] res = {a, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
        for (BigDecimal cur : res) {
            System.out.println(cur);
        }
    }

    private static BigDecimal iteritor(BigDecimal gradient, BigDecimal size, BigDecimal loss, BigDecimal x) {
        return gradient.subtract(size.multiply(loss).multiply(x));
    }
}
