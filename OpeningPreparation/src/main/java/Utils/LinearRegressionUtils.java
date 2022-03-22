package Utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LinearRegressionUtils {
    public static void LR(BigInteger[][] M) {
        BigDecimal learning_rate = new BigDecimal("0.001");
        BigDecimal a = new BigDecimal("0"),
                a0 = new BigDecimal("0"),
                a1 = new BigDecimal("0"),
                a2 = new BigDecimal("0"),
                a3 = new BigDecimal("0"),
                a4 = new BigDecimal("0"),
                a5 = new BigDecimal("0"),
                a6 = new BigDecimal("0"),
                a7 = new BigDecimal("0"),
                a8 = new BigDecimal("0"),
                a9 = new BigDecimal("0"),
                a10 = new BigDecimal("0");

        int iterator = 500;
        for (int i = 0; i < iterator; i++) {
            BigDecimal gradient_a = new BigDecimal("0");
            BigDecimal gradient_a0 = new BigDecimal("0");
            BigDecimal gradient_a1 = new BigDecimal("0");
            BigDecimal gradient_a2 = new BigDecimal("0");
            BigDecimal gradient_a3 = new BigDecimal("0");
            BigDecimal gradient_a4 = new BigDecimal("0");
            BigDecimal gradient_a5 = new BigDecimal("0");
            BigDecimal gradient_a6 = new BigDecimal("0");
            BigDecimal gradient_a7 = new BigDecimal("0");
            BigDecimal gradient_a8 = new BigDecimal("0");
            BigDecimal gradient_a9 = new BigDecimal("0");
            BigDecimal gradient_a10 = new BigDecimal("0");
            for (int j = 0; j < M.length; j++) {
                BigDecimal x0 = new BigDecimal(M[j][0].toString());
                BigDecimal x1 = new BigDecimal(M[j][1].toString());
                BigDecimal x2 = new BigDecimal(M[j][2].toString());
                BigDecimal x3 = new BigDecimal(M[j][3].toString());
                BigDecimal x4 = new BigDecimal(M[j][4].toString());
                BigDecimal x5 = new BigDecimal(M[j][5].toString());
                BigDecimal x6 = new BigDecimal(M[j][6].toString());
                BigDecimal x7 = new BigDecimal(M[j][7].toString());
                BigDecimal x8 = new BigDecimal(M[j][8].toString());
                BigDecimal x9 = new BigDecimal(M[j][9].toString());
                BigDecimal x10 = new BigDecimal(M[j][10].toString());
                BigDecimal y = new BigDecimal(M[j][11].toString());

                gradient_a = gradient_a.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10))))
                );
                gradient_a0 = gradient_a0.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x0)
                );
                gradient_a1 = gradient_a1.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x1)
                );
                gradient_a2 = gradient_a2.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x2)
                );
                gradient_a3 = gradient_a3.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x3)
                );
                gradient_a4 = gradient_a4.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x4)
                );
                gradient_a5 = gradient_a5.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x5)
                );
                gradient_a6 = gradient_a6.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x6)
                );
                gradient_a7 = gradient_a7.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x7)
                );
                gradient_a8 = gradient_a8.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x8)
                );
                gradient_a9 = gradient_a9.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x9)
                );
                gradient_a10 = gradient_a10.subtract(new BigDecimal(String.valueOf(1.0 / M.length)).
                        multiply(y.subtract(a.
                                add(a0.multiply(x0)).
                                add(a1.multiply(x1)).
                                add(a2.multiply(x2)).
                                add(a3.multiply(x3)).
                                add(a4.multiply(x4)).
                                add(a5.multiply(x5)).
                                add(a6.multiply(x6)).
                                add(a7.multiply(x7)).
                                add(a8.multiply(x8)).
                                add(a9.multiply(x9)).
                                add(a10.multiply(x10)))).
                        multiply(x10)
                );
            }
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
}
