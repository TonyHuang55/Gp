import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinearRegression {
    @Test
    public void test1() throws IOException {
        String localURL = "src/main/resources/database/house_data.csv";
        FileReader fileReader = new FileReader(localURL);
        String combineHeaders = new BufferedReader(new FileReader(localURL)).readLine();
        String[] headers = combineHeaders.split(",");
//        for (int i = 0; i < headers.length; i++) {
//            String tmp = headers[i];
//            headers[i] = tmp.substring(1, tmp.length() - 1);
//        }
        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(',');
        CSVParser parser = new CSVParser(fileReader, format);
        List<CSVRecord> totalData = parser.getRecords();
        double[][] x = new double[totalData.size() - 1][13];
        double[] y = new double[totalData.size() - 1];
        for (int count = 1; count < totalData.size(); count++) {
            CSVRecord record = totalData.get(count);
            for (int d = 0; d < record.size() - 1; d++) {
                x[count - 1][d] = (Double.parseDouble(record.get(d)));
            }
            y[count - 1] = (Double.parseDouble(record.get(record.size() - 1)));
        }


        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        regression.newSampleData(y, x);

        double[] doubles = regression.estimateRegressionParameters();
        for (double aDouble : doubles) {
            System.out.println(aDouble);
        }

        int c = 0;//653
        for (int count = 0; count < x.length; count++) {
            double[] x1 = x[count];
            double y1 = y[count];
            double cal = doubles[0];
            for (int i = 0; i < x1.length; i++) {
                cal += doubles[i + 1] * x1[i];
            }
            cal = Math.round(cal);
            if (cal != y1) {
                c++;
            }
//            System.out.println("回归结果为：" + cal + ",实际结果为：" + y1);
        }
        double rate1 = (506.0 - c) / 506.0;
        System.out.println("调包训练结果正确率：" + rate1);

//        int c1 = 0;//971
//        double[] doubles1 = {
//                0.893713663898845514680373954587679,
//                0.018969579787319297522742306666234,
//                0.937489429299001823145834369383443,
//                0.967655980007482041172810789505129,
//                0.561695595591187104952198139457453,
//                0.988851272946083723180589020978109,
//                0.006456545570517347442260018332228,
//                -0.01197973134045061305633652537780,
//                0.893882570508861651299330427875804,
//                0.649855547611949487197998246734840,
//                0.931070066159703380903139968660764,
//                -0.08493077764696144878073784076785};
//        for (int count = 0; count < x.length; count++) {
//            double[] x1 = x[count];
//            double y1 = y[count];
//            double cal = doubles1[0];
//            for (int i = 0; i < x1.length; i++) {
//                cal += doubles1[i + 1] * x1[i];
//            }
//            cal = Math.round(cal);
//            if (cal != y1) {
//                c1++;
//            }
////            System.out.println("回归结果为：" + cal + ",实际结果为：" + y1);
//        }
//        double rate2 = (1599.0 - c1) / 1599.0;
//        System.out.println("自主训练结果正确率：" + rate2);

    }

    @Test
    public void test2() throws IOException {
        String localURL = "src/main/resources/database/winequality-red.csv";
        FileReader fileReader = new FileReader(localURL);
        String combineHeaders = new BufferedReader(new FileReader(localURL)).readLine();
        String[] headers = combineHeaders.split(";");
        for (int i = 0; i < headers.length; i++) {
            String tmp = headers[i];
            headers[i] = tmp.substring(1, tmp.length() - 1);
        }
        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(';');
        CSVParser parser = new CSVParser(fileReader, format);
        List<CSVRecord> totalData = parser.getRecords();
//        Double[][] x = new Double[totalData.size() - 1][12];
//        for (int count = 1; count < totalData.size(); count++) {
//            CSVRecord record = totalData.get(count);
//            for (int d = 0; d < record.size(); d++) {
//                x[count - 1][d] = (Double.valueOf(record.get(d)));
//            }
//        }
//        LR(x);
        double[][] x = new double[totalData.size() - 1][11];
        double[] y = new double[totalData.size() - 1];
        for (int count = 1; count < totalData.size(); count++) {
            CSVRecord record = totalData.get(count);
            for (int d = 0; d < record.size() - 1; d++) {
                x[count - 1][d] = (Double.parseDouble(record.get(d)));
            }
            y[count - 1] = Double.parseDouble(record.get(record.size() - 1));
        }

        double[] max = maxCalculate(x);
        double[] min = minCalculate(x);

        for (int i = 0; i < x.length; i++) {
            double[] list = x[i];
            for (int j = 0; j < list.length; j++) {
                list[j] = (list[j] - min[j]) / (max[j] - min[j]);
            }
        }

        LR(x, y);
    }

    @Test
    public void test3() throws IOException {
        String localURL = "src/main/resources/database/house_data.csv";
        FileReader fileReader = new FileReader(localURL);
        String combineHeaders = new BufferedReader(new FileReader(localURL)).readLine();
        String[] headers = combineHeaders.split(",");
//        for (int i = 0; i < headers.length; i++) {
//            String tmp = headers[i];
//            headers[i] = tmp.substring(1, tmp.length() - 1);
//        }
        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(',');
        CSVParser parser = new CSVParser(fileReader, format);
        List<CSVRecord> totalData = parser.getRecords();
        double[][] x = new double[totalData.size() - 1][13];
        double[] y = new double[totalData.size() - 1];
        for (int count = 1; count < totalData.size(); count++) {
            CSVRecord record = totalData.get(count);
            for (int d = 0; d < record.size() - 1; d++) {
                x[count - 1][d] = (Double.parseDouble(record.get(d)));
            }
            y[count - 1] = Double.parseDouble(record.get(record.size() - 1));
        }

        double[] max = maxCalculate(x);
        double[] min = minCalculate(x);

        for (int i = 0; i < x.length; i++) {
            double[] list = x[i];
            for (int j = 0; j < list.length; j++) {
                list[j] = (list[j] - min[j]) / (max[j] - min[j]);
            }
        }

        LR(x, y);
    }

    public static void LR(Double[][] M) {
        BigDecimal learning_rate = new BigDecimal("0.0005");
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
        BigDecimal minLoss = new BigDecimal(Double.MAX_VALUE);
        BigDecimal minDerta = new BigDecimal(Double.MAX_VALUE);
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

            BigDecimal sumLoss = new BigDecimal("0");
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

                BigDecimal loss = y.subtract(a.add(a0.multiply(x0)).add(a1.multiply(x1)).add(a2.multiply(x2)).add(a3.multiply(x3)).add(a4.multiply(x4)).
                        add(a5.multiply(x5)).add(a6.multiply(x6)).add(a7.multiply(x7)).add(a8.multiply(x8)).add(a9.multiply(x9)).add(a10.multiply(x10)));
                sumLoss = sumLoss.add(loss.multiply(loss));

                BigDecimal size = new BigDecimal(String.valueOf(1.0 / M.length));

                gradient_a = iteritor(gradient_a, size, loss, new BigDecimal("1"));
                gradient_a0 = iteritor(gradient_a0, size, loss, x0);
                gradient_a1 = iteritor(gradient_a1, size, loss, x1);
                gradient_a2 = iteritor(gradient_a2, size, loss, x2);
                gradient_a3 = iteritor(gradient_a3, size, loss, x3);
                gradient_a4 = iteritor(gradient_a4, size, loss, x4);
                gradient_a5 = iteritor(gradient_a5, size, loss, x5);
                gradient_a6 = iteritor(gradient_a6, size, loss, x6);
                gradient_a7 = iteritor(gradient_a7, size, loss, x7);
                gradient_a8 = iteritor(gradient_a8, size, loss, x8);
                gradient_a9 = iteritor(gradient_a9, size, loss, x9);
                gradient_a10 = iteritor(gradient_a10, size, loss, x10);
            }
            BigDecimal loss1 = sumLoss.divide(new BigDecimal(2 * M.length), 4);
            BigDecimal derta = lastLoss.subtract(loss1);
//            System.out.println(derta);
            if (loss1.compareTo(minLoss) < 0) {
                minLoss = loss1;
            }
            if ((derta.abs()).compareTo(minDerta) <= 0) {
                minDerta = derta.abs();
            }
            System.out.println("第" + (i + 1) + "轮：" + loss1);
            if (loss1.compareTo(minLoss) > 0 && loss1.compareTo(BigDecimal.ONE) < 0) {
                break;
            } else {
                lastLoss = loss1;
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

        System.out.println("====================================================");
        BigDecimal[] res = {a, a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10};
        for (BigDecimal cur : res) {
            System.out.println(cur);
        }
    }

    public static void LR(double[][] X, double[] Y) {
        double averageY = 0.0;
        for (double cury : Y) {
            averageY += cury;
        }
        averageY = averageY / Y.length;
        // α ：学习率
        BigDecimal learning_rate = new BigDecimal("0.1");
        // a为常数项，a0 ~ a10 是因子的系数项
        // 权重初始值全都为 1
        BigDecimal[] a = new BigDecimal[X[0].length + 1];
        Arrays.fill(a, new BigDecimal("1"));
//        BigDecimal a = new BigDecimal("1"),
//                a0 = new BigDecimal("1"),
//                a1 = new BigDecimal("1"),
//                a2 = new BigDecimal("1"),
//                a3 = new BigDecimal("1"),
//                a4 = new BigDecimal("1"),
//                a5 = new BigDecimal("1"),
//                a6 = new BigDecimal("1"),
//                a7 = new BigDecimal("1"),
//                a8 = new BigDecimal("1"),
//                a9 = new BigDecimal("1"),
//                a10 = new BigDecimal("1"),
//                a11 = new BigDecimal("1"),
//                a12 = new BigDecimal("1");

        // 迭代次数
        int iterator = 500;
        // 用于记录上一轮迭代的损失函数
        BigDecimal lastLoss = new BigDecimal(Double.MAX_VALUE);
        // 用于记录损失函数的最小值，作为终止条件
        BigDecimal minLoss = new BigDecimal(Double.MAX_VALUE);
        for (int i = 0; i < iterator; i++) {
            BigDecimal[] gradient_a = new BigDecimal[X[0].length + 1];
            Arrays.fill(gradient_a, new BigDecimal("0"));
//            BigDecimal gradient_a = new BigDecimal("0"),
//                    gradient_a0 = new BigDecimal("0"),
//                    gradient_a1 = new BigDecimal("0"),
//                    gradient_a2 = new BigDecimal("0"),
//                    gradient_a3 = new BigDecimal("0"),
//                    gradient_a4 = new BigDecimal("0"),
//                    gradient_a5 = new BigDecimal("0"),
//                    gradient_a6 = new BigDecimal("0"),
//                    gradient_a7 = new BigDecimal("0"),
//                    gradient_a8 = new BigDecimal("0"),
//                    gradient_a9 = new BigDecimal("0"),
//                    gradient_a10 = new BigDecimal("0"),
//                    gradient_a11 = new BigDecimal("0"),
//                    gradient_a12 = new BigDecimal("0");
            BigDecimal totalLoss = new BigDecimal("0");
            for (int k = 0; k < X.length; k++) {
//                BigDecimal x0 = BigDecimal.valueOf(X[k][0]),
//                        x1 = BigDecimal.valueOf(X[k][1]),
//                        x2 = BigDecimal.valueOf(X[k][2]),
//                        x3 = BigDecimal.valueOf(X[k][3]),
//                        x4 = BigDecimal.valueOf(X[k][4]),
//                        x5 = BigDecimal.valueOf(X[k][5]),
//                        x6 = BigDecimal.valueOf(X[k][6]),
//                        x7 = BigDecimal.valueOf(X[k][7]),
//                        x8 = BigDecimal.valueOf(X[k][8]),
//                        x9 = BigDecimal.valueOf(X[k][9]),
//                        x10 = BigDecimal.valueOf(X[k][10]),
//                        x11 = BigDecimal.valueOf(X[k][11]),
//                        x12 = BigDecimal.valueOf(X[k][12]),
//                        y = BigDecimal.valueOf(Y[k]);

                // y - h(x)
//                BigDecimal func = y.subtract(a.add(a0.multiply(x0)).add(a1.multiply(x1)).add(a2.multiply(x2)).add(a3.multiply(x3)).add(a4.multiply(x4)).
//                        add(a5.multiply(x5)).add(a6.multiply(x6)).add(a7.multiply(x7)).add(a8.multiply(x8)).add(a9.multiply(x9)).add(a10.multiply(x10)).
//                        add(a11.multiply(x11)).add(a12.multiply(x12)));
                BigDecimal func = BigDecimal.valueOf(Y[k]);
                func = func.subtract(a[0]);
                for (int j = 1; j < X[0].length; j++) {
                    func = func.subtract(BigDecimal.valueOf(X[k][j - 1]).multiply(a[j]));
                }

                BigDecimal size = new BigDecimal(String.valueOf(1.0 / X.length));
                // ∑(y - h(x))^2
                totalLoss = totalLoss.add(func.multiply(func));
                // ∑(y - h(x))

                gradient_a[0] = gradient_a[0].subtract(size.multiply(func).multiply(BigDecimal.ONE));
                for (int j = 1; j < gradient_a.length; j++) {
                    gradient_a[j] = gradient_a[j].subtract(size.multiply(func).multiply(BigDecimal.valueOf(X[k][j - 1])));
                }
//                gradient_a0 = gradient_a0.subtract(size.multiply(func).multiply(x0));
//                gradient_a1 = gradient_a1.subtract(size.multiply(func).multiply(x1));
//                gradient_a2 = gradient_a2.subtract(size.multiply(func).multiply(x2));
//                gradient_a3 = gradient_a3.subtract(size.multiply(func).multiply(x3));
//                gradient_a4 = gradient_a4.subtract(size.multiply(func).multiply(x4));
//                gradient_a5 = gradient_a5.subtract(size.multiply(func).multiply(x5));
//                gradient_a6 = gradient_a6.subtract(size.multiply(func).multiply(x6));
//                gradient_a7 = gradient_a7.subtract(size.multiply(func).multiply(x7));
//                gradient_a8 = gradient_a8.subtract(size.multiply(func).multiply(x8));
//                gradient_a9 = gradient_a9.subtract(size.multiply(func).multiply(x9));
//                gradient_a10 = gradient_a10.subtract(size.multiply(func).multiply(x10));
//                gradient_a11 = gradient_a11.subtract(size.multiply(func).multiply(x11));
//                gradient_a12 = gradient_a12.subtract(size.multiply(func).multiply(x12));

            }

            for (int j = 0; j < a.length; j++) {
                a[j] = a[j].subtract(learning_rate.multiply(gradient_a[j]));
            }
//            a = a.subtract(learning_rate.multiply(gradient_a));
//            a0 = a0.subtract(learning_rate.multiply(gradient_a0));
//            a1 = a1.subtract(learning_rate.multiply(gradient_a1));
//            a2 = a2.subtract(learning_rate.multiply(gradient_a2));
//            a3 = a3.subtract(learning_rate.multiply(gradient_a3));
//            a4 = a4.subtract(learning_rate.multiply(gradient_a4));
//            a5 = a5.subtract(learning_rate.multiply(gradient_a5));
//            a6 = a6.subtract(learning_rate.multiply(gradient_a6));
//            a7 = a7.subtract(learning_rate.multiply(gradient_a7));
//            a8 = a8.subtract(learning_rate.multiply(gradient_a8));
//            a9 = a9.subtract(learning_rate.multiply(gradient_a9));
//            a10 = a10.subtract(learning_rate.multiply(gradient_a10));
//            a11 = a11.subtract(learning_rate.multiply(gradient_a11));
//            a12 = a12.subtract(learning_rate.multiply(gradient_a12));
            // loss(θ) = 1/2n * ∑(y - h(x))^2
            BigDecimal lossFunc = totalLoss.divide(new BigDecimal(2 * X.length), 4);
            BigDecimal derta = lastLoss.subtract(lossFunc);
//            System.out.println(derta);
            if (lossFunc.compareTo(minLoss) < 0) {
                minLoss = lossFunc;
            }
            System.out.println("第" + (i + 1) + "轮：" + lossFunc);
            if (derta.abs().compareTo(new BigDecimal("0.0005")) < 0) {
                break;
            } else {
                lastLoss = lossFunc;
            }
//            if (lossFunc.compareTo(minLoss) > 0 && lossFunc.compareTo(BigDecimal.ONE) < 0) {
//                break;
//            } else {
//                lastLoss = lossFunc;
//            }
        }

        System.out.println("====================================================");

        for (BigDecimal re : a) {
            System.out.println(re);
        }

        double SSE = 0.0, SST = 0.0;
        double MAE = 0.0;
        for (int count = 0; count < X.length; count++) {
            double[] curX = X[count];
            double curY = Y[count];
            double cal = a[0].doubleValue();
            for (int i = 0; i < curX.length; i++) {
                cal += a[i + 1].doubleValue() * curX[i];
            }
            SSE += Math.pow((curY - cal), 2);
            SST += Math.pow((curY - averageY), 2);
            MAE += 1.0 / X.length * Math.abs(curY - cal);
        }
        double R2 = 1.0 - SSE / SST;
//        System.out.println("R^2 =" + R2);//R^2 =0.7282359232308551
        System.out.println("MAE = " + MAE);
    }

    private static BigDecimal iteritor(BigDecimal gradient, BigDecimal size, BigDecimal loss, BigDecimal x) {
        return gradient.subtract(size.multiply(loss).multiply(x));
    }

    private static double[] maxCalculate(double[][] x) {
        double[] maxFeature = new double[x[0].length];
        System.arraycopy(x[0], 0, maxFeature, 0, maxFeature.length);
        for (int count = 1; count < x.length; count++) {
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
        System.arraycopy(x[0], 0, minFeature, 0, minFeature.length);
        for (int count = 1; count < x.length; count++) {
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
