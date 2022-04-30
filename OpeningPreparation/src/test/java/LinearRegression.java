import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class LinearRegression {
    @Test
    public void wine() throws IOException {
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
    public void house() throws IOException {
        String localURL = "src/main/resources/database/house_data.csv";
        FileReader fileReader = new FileReader(localURL);
        String combineHeaders = new BufferedReader(new FileReader(localURL)).readLine();
        String[] headers = combineHeaders.split(";");
        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(';');
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

    public static void LR(double[][] X, double[] Y) {
        // α ：学习率
        BigDecimal learning_rate = new BigDecimal("0.1");
        // a为常数项，a0 ~ a10 是因子的系数项
        // 权重初始值全都为 1
        BigDecimal[] a = new BigDecimal[X[0].length + 1];
        Arrays.fill(a, new BigDecimal("1"));
        // 迭代次数
        int iterator = 500;
        // 用于记录上一轮迭代的损失函数
        BigDecimal lastLoss = new BigDecimal(Double.MAX_VALUE);
        // 用于记录损失函数的最小值，作为终止条件
        BigDecimal minLoss = new BigDecimal(Double.MAX_VALUE);
        for (int i = 0; i < iterator; i++) {
            BigDecimal[] gradient_a = new BigDecimal[X[0].length + 1];
            Arrays.fill(gradient_a, new BigDecimal("0"));
            BigDecimal totalLoss = new BigDecimal("0");
            for (int k = 0; k < X.length; k++) {
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
            }

            for (int j = 0; j < a.length; j++) {
                a[j] = a[j].subtract(learning_rate.multiply(gradient_a[j]));
            }
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
        }

        System.out.println("====================================================");

        System.out.println("训练模型为：");
        for (int j = 0; j < a.length; j++) {
            System.out.print("a" + j + " = " + a[j].setScale(5, BigDecimal.ROUND_HALF_UP));
            System.out.print("     ");
            if (j % 3 == 2) {
                System.out.println();
            }
        }

        double MAE = 0.0;
        for (int count = 0; count < X.length; count++) {
            double[] curX = X[count];
            double curY = Y[count];
            double cal = a[0].doubleValue();
            for (int i = 0; i < curX.length; i++) {
                cal += a[i + 1].doubleValue() * curX[i];
            }
            MAE += 1.0 / X.length * Math.abs(curY - cal);
        }
        System.out.println("平均绝对误差MAE = " + MAE);
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
