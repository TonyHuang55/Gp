package Utils;

import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

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
        // 用于记录上一轮迭代的损失函数
        BigDecimal lastLoss = new BigDecimal(Double.MAX_VALUE);
        // 用于记录损失函数的最小值，作为终止条件
        BigDecimal minLoss = new BigDecimal(Double.MAX_VALUE);
        for (int i = 0; i < iterator; i++) {
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

            BigDecimal lossFunc = totalLoss.divide(new BigDecimal(2 * n), 4);
            BigDecimal derta = lastLoss.subtract(lossFunc);
//            System.out.println(derta);
            if (lossFunc.compareTo(minLoss) < 0) {
                minLoss = lossFunc;
            }
            System.out.println("第" + (i + 1) + "轮：" + lossFunc);
            if (lossFunc.compareTo(minLoss) > 0 || derta.abs().compareTo(new BigDecimal("0.001")) < 0) {
                break;
            } else {
                lastLoss = lossFunc;
            }
        }
        System.out.println("====================================================");

        for (BigDecimal bigDecimal : a) {
            System.out.println(bigDecimal);
        }

        return a;
    }

    public static void check(List<CSVRecord> totalData, BigDecimal[] a) {
        double[][] X = new double[totalData.size() - 1][13];
        double[] Y = new double[totalData.size() - 1];
        for (int count = 1; count < totalData.size(); count++) {
            CSVRecord record = totalData.get(count);
            for (int d = 0; d < record.size() - 1; d++) {
                X[count - 1][d] = (Double.parseDouble(record.get(d)));
            }
            Y[count - 1] = Double.parseDouble(record.get(record.size() - 1));
        }

        double[] max = maxCalculate(X);
        double[] min = minCalculate(X);

        for (int i = 0; i < X.length; i++) {
            double[] list = X[i];
            for (int j = 0; j < list.length; j++) {
                list[j] = (list[j] - min[j]) / (max[j] - min[j]);
            }
        }

        double averageY = 0.0;
        for (double cury : Y) {
            averageY += cury;
        }
        averageY = averageY / Y.length;

        double SSE = 0.0, SST = 0.0;
        for (int count = 0; count < X.length; count++) {
            double[] curX = X[count];
            double curY = Y[count];
            double cal = Double.parseDouble(String.valueOf(a[0]));
            for (int i = 0; i < curX.length; i++) {
                cal += Double.parseDouble(String.valueOf(a[i + 1])) * curX[i];
            }
            SSE += Math.pow((curY - cal), 2);
            SST += Math.pow((curY - averageY), 2);
        }
        double R2 = 1.0 - SSE / SST;
        System.out.println("R^2 =" + R2);
    }

    private static BigDecimal iteritor(BigDecimal gradient, BigDecimal size, BigDecimal loss) {
        return gradient.subtract(size.multiply(loss));
    }

    private static double[] maxCalculate(double[][] x) {
        double[] maxFeature = new double[x[0].length];
        for (int count = 0; count < x.length; count++) {
            double[] curX = x[count];
            for (int d = 0; d < curX.length; d++) {
                double current = curX[d];
                // 比较更新 max
                if (maxFeature[d] <= current) maxFeature[d] = current;
            }
        }
        return maxFeature;
    }

    private static double[] minCalculate(double[][] x) {
        double[] minFeature = new double[x[0].length];
        for (int count = 0; count < x.length; count++) {
            double[] curX = x[count];
            for (int d = 0; d < curX.length; d++) {
                double current = curX[d];
                // 比较更新 max
                if (minFeature[d] >= current) minFeature[d] = current;
            }
        }
        return minFeature;
    }
}
