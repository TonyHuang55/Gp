import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class LinearRegression {
    @Test
    public void test1() throws IOException {
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
                x[count - 1][d] = (Double.valueOf(record.get(d)));
            }
            y[count - 1] = (Double.valueOf(record.get(record.size() - 1)));
        }


        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        regression.newSampleData(y, x);

        double[] doubles = regression.estimateRegressionParameters();
//        for (double aDouble : doubles) {
//            System.out.println(aDouble);
//        }

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
        System.out.println(c);

        int c1 = 0;//971
        double[] doubles1 = {
                0.89349185061568425,
                0.00283593935273664,
                0.93162392818300117,
                0.96742933910576380,
                0.47280560133203818,
                0.98764077746888110,
                0.00673980241283284,
                -0.0112723948578575,
                0.89357513041230687,
                0.64939900046174506,
                0.93162324520678689,
                -0.0532382974182954};
        for (int count = 0; count < x.length; count++) {
            double[] x1 = x[count];
            double y1 = y[count];
            double cal = doubles1[0];
            for (int i = 0; i < x1.length; i++) {
                cal += doubles1[i + 1] * x1[i];
            }
            cal = Math.round(cal);
            if (cal != y1) {
                c1++;
            }
//            System.out.println("回归结果为：" + cal + ",实际结果为：" + y1);
        }
        System.out.println(c1);

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
        Double[][] x = new Double[totalData.size() - 1][12];
        for (int count = 1; count < totalData.size(); count++) {
            CSVRecord record = totalData.get(count);
            for (int d = 0; d < record.size(); d++) {
                x[count - 1][d] = (Double.valueOf(record.get(d)));
            }
        }
        LR(x);
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
                sumLoss = sumLoss.add(loss);

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
            BigDecimal loss1 = sumLoss.multiply(sumLoss).divide(new BigDecimal(2 * M.length), 4);
            BigDecimal derta = lastLoss.subtract(loss1);
//            System.out.println(derta);
            BigDecimal min = new BigDecimal(Double.MAX_VALUE);
            if (loss1.compareTo(min) <= 0) {
                min = loss1;
            }
            System.out.println("第" + i + "轮：" + loss1);
            if (loss1.compareTo(min) > 0 || loss1.compareTo(new BigDecimal(5)) > 0) {
                lastLoss = loss1;
            } else {
                break;
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

    private static BigDecimal iteritor(BigDecimal gradient, BigDecimal size, BigDecimal loss, BigDecimal x) {
        return gradient.subtract(size.multiply(loss).multiply(x));
    }

}
