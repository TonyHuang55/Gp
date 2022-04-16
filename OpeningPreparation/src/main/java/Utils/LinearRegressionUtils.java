package Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class LinearRegressionUtils {
    public static BigDecimal[] LR(BigInteger[][] M, int n) {
        // α ：学习率
        BigDecimal learning_rate = new BigDecimal("0.000001");
        // a为常数项，a0 ~ a12 是因子的系数项
        // 权重初始值全都为 1
        BigDecimal[] a = new BigDecimal[M.length];
        Arrays.fill(a, new BigDecimal("1"));

        // 迭代次数
        int iterator = 1000;
        int i = 0;
        // 用于记录上一轮迭代的损失函数
        BigDecimal lastLoss = new BigDecimal(Double.MAX_VALUE);
        // 用于记录损失函数的最小值，作为终止条件
        BigDecimal minLoss = new BigDecimal(Double.MAX_VALUE);
        BigDecimal lossFunc = null;
        for (; i < iterator; i++) {
            BigDecimal totalLoss = new BigDecimal("0");

            BigDecimal[] AX0 = new BigDecimal[M.length];
            AX0[0] = new BigDecimal(n + "");
            for (int j = 1; j < M.length; j++) {
                AX0[j] = new BigDecimal(M[0][j - 1]);
            }

            BigDecimal func = new BigDecimal(M[0][M[0].length - 1]);
            for (int j = 0; j < M.length; j++) {
                func = func.subtract(AX0[j].multiply(a[j]));
            }
            totalLoss = totalLoss.add(func.multiply(func));
            BigDecimal size = new BigDecimal(String.valueOf(1.0 / n));
            a[0] = a[0].add(learning_rate.multiply(size).multiply(func));

            for (int j = 1; j < M.length; j++) {
                func = new BigDecimal(M[j][M[0].length - 1]);
                BigDecimal[] AX = new BigDecimal[M.length];
                AX[0] = new BigDecimal(M[0][j - 1]);
                for (int k = 1; k < M.length; k++) {
                    AX[k] = new BigDecimal(M[j][k - 1]);
                }
                for (int k = 0; k < M.length; k++) {
                    func = func.subtract(AX[k].multiply(a[k]));
                }
                BigDecimal funcPow = func.divide(new BigDecimal(M[0][j - 1]), 4);
                totalLoss = totalLoss.add(funcPow.multiply(funcPow));
                a[j] = a[j].add(learning_rate.multiply(size).multiply(func));
            }

            lossFunc = totalLoss.divide(new BigDecimal(2 * n), 4);
            BigDecimal derta = lastLoss.subtract(lossFunc);
            if (lossFunc.compareTo(minLoss) < 0) {
                minLoss = lossFunc;
            }
            if (lossFunc.compareTo(minLoss) > 0 || derta.abs().compareTo(new BigDecimal("0.001")) < 0) {
                break;
            } else {
                lastLoss = lossFunc;
            }
        }
        System.out.println("训练结束，迭代" + (i + 1) + "轮");
        System.out.println("训练模型为：");
        for (int j = 0; j < a.length; j++) {
            System.out.print("a" + j + " = " + a[j].setScale(5, BigDecimal.ROUND_HALF_UP));
            System.out.print("     ");
            if (j % 3 == 2) { System.out.println(); }
        }
        System.out.println();

        return a;
    }
}
